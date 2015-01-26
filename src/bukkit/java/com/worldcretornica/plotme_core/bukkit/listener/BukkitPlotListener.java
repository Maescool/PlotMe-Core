package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMapInfo;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.PlotToClear;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlock;
import com.worldcretornica.plotme_core.bukkit.api.BukkitEntity;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.event.PlotWorldLoadEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class BukkitPlotListener implements Listener {

    private final PlotMe_Core api;
    private final PlotMe_CorePlugin plugin;

    public BukkitPlotListener(PlotMe_CorePlugin instance) {
        api = instance.getAPI();
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            Player player = event.getPlayer();
            boolean cannotBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                if (cannotBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    switch (ptc.getReason()) {
                        case Clear:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                            break;
                        case Reset:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                            break;
                        case Expired:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                            break;
                    }
                    event.setCancelled(true);
                } else {
                    Plot plot = api.getPlotMeCoreManager().getMap(location).getPlot(id);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (cannotBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(location).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlockPlaced().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                if (canBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    switch (ptc.getReason()) {
                        case Clear:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                            break;
                        case Reset:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                            break;
                        case Expired:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                            break;
                    }
                    event.setCancelled(true);
                } else {
                    Plot plot = api.getPlotMeCoreManager().getPlotById(id, location.getWorld());

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(location).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlockClicked().getLocation());

        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            if (api.getPlotMeCoreManager().isPlotWorld(location)) {
                String id = PlotMeCoreManager.getPlotId(location);

                if (id.isEmpty()) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                } else {
                    PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                    if (ptc != null) {
                        switch (ptc.getReason()) {
                            case Clear:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                                break;
                            case Reset:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                                break;
                            case Expired:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                                break;
                        }
                        event.setCancelled(true);
                    } else {
                        Plot plot = api.getPlotMeCoreManager().getPlotById(id, location.getWorld());

                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlockClicked().getLocation());
        if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            if (api.getPlotMeCoreManager().isPlotWorld(location)) {
                String id = PlotMeCoreManager.getPlotId(location);

                if (id.isEmpty()) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                } else {
                    PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                    if (ptc != null) {
                        switch (ptc.getReason()) {
                            case Clear:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                                break;
                            case Reset:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                                break;
                            case Expired:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                                break;
                        }
                        event.setCancelled(true);
                    } else {
                        Plot plot = api.getPlotMeCoreManager().getPlotById(id, location.getWorld());

                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {

        BukkitBlock block = new BukkitBlock(event.getClickedBlock());
        if (api.getPlotMeCoreManager().isPlotWorld(block.getWorld())) {
            Player player = event.getPlayer();

            String id = PlotMeCoreManager.getPlotId(block.getLocation());

            PlotToClear ptc = api.getPlotLocked(block.getWorld().getName(), id);

            if (ptc != null) {
                switch (ptc.getReason()) {
                    case Clear:
                        player.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                        break;
                    case Reset:
                        player.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                        break;
                    case Expired:
                        player.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                        break;
                }
                event.setCancelled(true);
            } else {
                boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
                PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(block.getWorld());

                if (event.isBlockInHand() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    id = PlotMeCoreManager.getPlotId(block.getLocation());

                    if (id.isEmpty()) {
                        if (canBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            if (canBuild) {
                                player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                                event.setCancelled(true);
                            }
                        } else {
                            plot.resetExpire(pmi.getDaysToExpiration());
                        }
                    }
                } else {
                    boolean blocked = false;
                    if (pmi.isProtectedBlock(block.getTypeId())) {
                        if (!player.hasPermission("plotme.unblock." + block.getTypeId())) {
                            blocked = true;
                        }
                    }

                    ItemStack item = event.getItem();

                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (item != null) {
                            int itemId = item.getType().getId();
                            byte itemData = item.getData().getData();

                            if (pmi.isPreventedItem(String.valueOf(itemId)) || pmi.isPreventedItem(itemId + ":" + itemData)) {
                                if (!player.hasPermission("plotme.unblock." + itemId)) {
                                    blocked = true;
                                }
                            }
                        }
                    }

                    if (blocked) {
                        id = PlotMeCoreManager.getPlotId(block.getLocation());

                        if (id.isEmpty()) {
                            if (canBuild) {
                                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    player.sendMessage(api.getUtil().C("ErrCannotUse"));
                                }
                                event.setCancelled(true);
                            }
                        } else {
                            Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                            if ((plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) && canBuild) {
                                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                    player.sendMessage(api.getUtil().C("ErrCannotUse"));
                                }
                                event.setCancelled(true);
                            }
                        }
                    }
                }

                if (pmi.isRedstoneBlock(block.getTypeId())) {
                    id = PlotMeCoreManager.getPlotId(block.getLocation());
                    if (!id.isEmpty()) {
                        Plot plot = PlotMeCoreManager.getPlotById(id, pmi);
                        if (plot != null && plot.isRedstoneProtect()) {
                            if ((!plot.isAllowed(player.getName(), player.getUniqueId())) && canBuild) {
                                player.sendMessage(api.getUtil().C("ErrCannotRedstone"));
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        BukkitLocation location = new BukkitLocation(event.getToBlock().getLocation());
        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            String id = PlotMeCoreManager.getPlotId(location);
            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());
        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            BlockFace face = event.getDirection();

            for (Block block : event.getBlocks()) {
                String id = PlotMeCoreManager.getPlotId(new BukkitLocation(block.getLocation().add(face.getModX(), face.getModY(), face.getModZ())));

                if (id.isEmpty()) {
                    event.setCancelled(true);
                } else {
                    PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                    if (ptc != null) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        BukkitBlock block = new BukkitBlock(event.getRetractLocation().getBlock());

        if (api.getPlotMeCoreManager().isPlotWorld(block.getWorld())) {
            String id = PlotMeCoreManager.getPlotId(block.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(block.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        List<BlockState> blocks = event.getBlocks();

        BukkitLocation location = new BukkitLocation(event.getLocation());
        if (!api.getPlotMeCoreManager().isPlotWorld(location)) {
            return;
        }

        for (int i = 0; i < blocks.size(); i++) {
            String id = PlotMeCoreManager.getPlotId(new BukkitLocation(blocks.get(i).getLocation()));
            if (id.isEmpty()) {
                blocks.remove(i);
                i--;
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        BukkitLocation location = new BukkitLocation(event.getLocation());

        PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(location);

        if (pmi != null && pmi.isDisableExplosion()) {
            event.setCancelled(true);
        } else {
            String id = PlotMeCoreManager.getPlotId(location);
            PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

            if (ptc != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getIgnitingEntity() == null) {
            return;
        }
        BukkitEntity entity = new BukkitEntity(event.getIgnitingEntity());

        PlotMapInfo pmi = api.getPlotMeCoreManager().getMap(entity);

        if (pmi == null) {
            return;
        }
        if (pmi.isDisableIgnition()) {
            event.setCancelled(true);
        } else {
            String id = PlotMeCoreManager.getPlotId(entity.getLocation());

            if (id.isEmpty()) {
                event.setCancelled(true);
            } else {
                PlotToClear ptc = api.getPlotLocked(entity.getWorld().getName(), id);

                Player player = null;
                if (ptc != null) {
                    if (event.getPlayer() != null) {
                        player = event.getPlayer();
                        switch (ptc.getReason()) {
                            case Clear:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                                break;
                            case Reset:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                                break;
                            case Expired:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                                break;
                        }
                    }
                    event.setCancelled(true);
                } else {
                    if (event.getPlayer() != null) {
                        player = event.getPlayer();
                    }
                    Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                    if (plot == null) {
                        event.setCancelled(true);
                    } else {
                        if (player != null && !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getBlock().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            String id = PlotMeCoreManager.getPlotId(location);
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

            if (id.isEmpty()) {
                if (canBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    switch (ptc.getReason()) {
                        case Clear:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                            break;
                        case Reset:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                            break;
                        case Expired:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                            break;
                    }
                    event.setCancelled(true);
                } else {
                    Plot plot = api.getPlotMeCoreManager().getPlotById(id, location.getWorld());

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(location).getDaysToExpiration());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {

        if (event.getRemover() instanceof Player) {
            BukkitPlayer player = (BukkitPlayer) plugin.wrapPlayer((Player) event.getRemover());

            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);

            if (api.getPlotMeCoreManager().isPlotWorld(player.getWorld())) {
                String id = PlotMeCoreManager.getPlotId(player.getLocation());

                if (id.isEmpty()) {
                    if (canBuild) {
                        player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else {
                    PlotToClear ptc = api.getPlotLocked(player.getWorld().getName(), id);

                    if (ptc != null) {
                        switch (ptc.getReason()) {
                            case Clear:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                                break;
                            case Reset:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                                break;
                            case Expired:
                                player.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                                break;
                        }
                        event.setCancelled(true);
                    } else {
                        Plot plot = api.getPlotMeCoreManager().getPlotById(id, player.getWorld());

                        if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                            if (canBuild) {
                                player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                                event.setCancelled(true);
                            }
                        } else {
                            plot.resetExpire(api.getPlotMeCoreManager().getMap(player).getDaysToExpiration());
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getRightClicked().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            boolean canBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                if (canBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            } else {
                PlotToClear ptc = api.getPlotLocked(location.getWorld().getName(), id);

                if (ptc != null) {
                    switch (ptc.getReason()) {
                        case Clear:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedClear"));
                            break;
                        case Reset:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedReset"));
                            break;
                        case Expired:
                            player.sendMessage(api.getUtil().C("MsgPlotLockedExpired"));
                            break;
                    }
                    event.setCancelled(true);
                } else {
                    Plot plot = api.getPlotMeCoreManager().getPlotById(id, location.getWorld());

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (canBuild) {
                            player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(location).getDaysToExpiration());
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        onPlayerInteractEntity(event);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        Player player = event.getPlayer();
        BukkitLocation location = new BukkitLocation(event.getEgg().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            boolean canBuild = player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
            String id = PlotMeCoreManager.getPlotId(location);

            if (id.isEmpty()) {
                if (!canBuild) {
                    player.sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                    event.setHatching(false);
                }
            } else {
                Plot plot = api.getPlotMeCoreManager().getPlotById(id, location.getWorld());

                if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                    if (!canBuild) {
                        player.sendMessage(api.getUtil().C("ErrCannotUseEggs"));
                        event.setHatching(false);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        BukkitLocation location = new BukkitLocation(event.getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            String id = PlotMeCoreManager.getPlotId(location);

            if (!id.isEmpty()) {
                PlotToClear plotLocked = api.getPlotLocked(location.getWorld().getName(), id);

                if (plotLocked != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamagebyEntity(EntityDamageByEntityEvent event) {
        BukkitEntity entity = new BukkitEntity(event.getDamager());
        if (api.getPlotMeCoreManager().isPlotWorld(entity)) {
            if (!(event.getDamager() instanceof Player)) {
                event.setCancelled(true);
            } else {
                Player player = (Player) event.getDamager();
                BukkitPlayer bukkitPlayer = (BukkitPlayer) plugin.wrapPlayer(player);
                boolean cantBuild = !player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE);
                String id = PlotMeCoreManager.getPlotId(entity.getLocation());
                if (id.isEmpty()) {
                    if (cantBuild) {
                        player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                        event.setCancelled(true);
                    }
                } else {
                    Plot plot = api.getPlotMeCoreManager().getPlotById(id, bukkitPlayer);
                    if (plot == null) {
                        if (cantBuild) {
                            bukkitPlayer.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else if (!plot.isAllowed(player.getName(), player.getUniqueId())) {
                        if (cantBuild) {
                            bukkitPlayer.sendMessage(api.getUtil().C("ErrCannotBuild"));
                            event.setCancelled(true);
                        }
                    } else {
                        plot.resetExpire(api.getPlotMeCoreManager().getMap(bukkitPlayer).getDaysToExpiration());
                    }
                }

            }
        }
    }

    @EventHandler
    public void onPlotWorldLoad(PlotWorldLoadEvent event) {
        api.getLogger().info("Done loading " + event.getNbPlots() + " plots for world " + event.getWorldName());
    }

}
