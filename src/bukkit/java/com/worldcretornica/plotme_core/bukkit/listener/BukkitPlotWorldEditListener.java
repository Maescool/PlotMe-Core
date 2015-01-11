package com.worldcretornica.plotme_core.bukkit.listener;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitMaterial;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import java.io.BufferedReader;
import java.io.File;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class BukkitPlotWorldEditListener implements Listener {

    private final PlotMe_Core api;
    private final PlotWorldEdit worldEdit;
    private final PlotMe_CorePlugin plugin;

    public BukkitPlotWorldEditListener(PlotMe_CorePlugin core, PlotWorldEdit worldEdit, PlotMe_CorePlugin plugin) {
        api = core.getAPI();
        this.plugin = plugin;
        this.worldEdit = worldEdit;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null || event.getFrom() == null) {
            return;
        }
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());

        if (event.getFrom().getWorld() == null) {
            plugin.getLogger().log(Level.WARNING, "Event from has no world");
        } else {
            if (from.getLocation() == null) {
                plugin.getLogger().log(Level.INFO, "BukkitLocation from's location-object is null");
            } else {
                if (from.getLocation().getWorld() == null) {
                    plugin.getLogger().log(Level.INFO, "BukkitLocation from's location-object's world is null");
                } else {
                    if (from.getLocation().getWorld().getName() == null) {
                        plugin.getLogger().log(Level.INFO, "BukkitLocation from's location-object's world's name is null");
                    }else{
                        plugin.getLogger().log(Level.INFO, "BukkitLocation From Worldname: {0}", from.getLocation().getWorld().getName());
                    }
                }
            }

        }

        if (event.getTo().getWorld() == null) {
            plugin.getLogger().log(Level.WARNING, "Event to has no world");
        } else {
            if (to.getLocation() == null) {
                plugin.getLogger().log(Level.INFO, "BukkitLocation to's location-object is null");
            } else {
                if (to.getLocation().getWorld() == null) {
                    plugin.getLogger().log(Level.INFO, "BukkitLocation to's location-object's world is null");
                } else {
                    if (to.getLocation().getWorld().getName() == null) {
                        plugin.getLogger().log(Level.INFO, "BukkitLocation to's location-object's world's name is null");
                    }else{
                        plugin.getLogger().log(Level.INFO, "BukkitLocation To Worldname: {0}", to.getLocation().getWorld().getName());
                    }
                }
            }

        }

        BukkitPlayer player = plugin.wrapPlayer(event.getPlayer());

        String idTo = "";

        boolean changemask = false;

        if (!from.getWorld().getName().equalsIgnoreCase(to.getWorld().getName())) {
            changemask = true;
        } else if (from.getLocation() != to.getLocation()) {
            String idFrom = PlotMeCoreManager.getPlotId(from);
            idTo = PlotMeCoreManager.getPlotId(to);

            if (!idFrom.equals(idTo)) {
                changemask = true;
            }
        }

        if (changemask && api.getPlotMeCoreManager().isPlotWorld(to.getWorld())) {
            if (api.getPlotMeCoreManager().isPlayerIgnoringWELimit(player)) {
                worldEdit.removeMask(player);
            } else {
                worldEdit.setMask(player, idTo);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            if (api.getPlotMeCoreManager().isPlayerIgnoringWELimit(player)) {
                worldEdit.removeMask(player);
            } else {
                worldEdit.setMask(player);
            }
        } else {
            worldEdit.removeMask(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());
        if (event.getTo() == null) {
            return;
        }
        if (api.getPlotMeCoreManager().isPlotWorld(from)) {
            if (api.getPlotMeCoreManager().isPlotWorld(to)) {
                worldEdit.setMask(player);
            } else {
                worldEdit.removeMask(player);
            }
        } else if (api.getPlotMeCoreManager().isPlotWorld(to)) {
            worldEdit.setMask(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        if (event.getFrom() == null || event.getTo() == null) {
            return;
        }
        BukkitLocation from = new BukkitLocation(event.getFrom());
        BukkitLocation to = new BukkitLocation(event.getTo());
        if (event.getTo() == null) {
            return;
        }
        if (api.getPlotMeCoreManager().isPlotWorld(from)) {
            if (api.getPlotMeCoreManager().isPlotWorld(to)) {
                worldEdit.setMask(player);
            } else {
                worldEdit.removeMask(player);
            }
        } else if (api.getPlotMeCoreManager().isPlotWorld(to)) {
            worldEdit.setMask(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());

        if (api.getPlotMeCoreManager().isPlotWorld(player)) {
            if (!api.getPlotMeCoreManager().isPlayerIgnoringWELimit(player)) {
                if (event.getMessage().startsWith("//gmask")) {
                    player.sendMessage(api.getUtil().C("ErrCannotUse"));
                    event.setCancelled(true);
                } else if (event.getMessage().startsWith("//up")) {
                    Plot plot = api.getPlotMeCoreManager().getPlotById(player);

                    if (plot == null || !plot.isAllowed(player.getName(), player.getUniqueId())) {
                        player.sendMessage(api.getUtil().C("ErrCannotUse"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        BukkitPlayer player = new BukkitPlayer(event.getPlayer());
        BukkitLocation location = new BukkitLocation(event.getClickedBlock().getLocation());

        if (api.getPlotMeCoreManager().isPlotWorld(location)) {
            if (!player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)
                    && !api.getPlotMeCoreManager().isPlayerIgnoringWELimit(player)
                    && (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) && ((BukkitMaterial) player.getItemInHand().getType()).getMaterial() != Material.AIR) {
                String id = PlotMeCoreManager.getPlotId(location);
                Plot plot = api.getPlotMeCoreManager().getMap(location).getPlot(id);

                if (plot != null && plot.isAllowed(player.getName(), player.getUniqueId())) {
                    worldEdit.setMask(player);
                } else {
                    player.sendMessage(api.getUtil().C("ErrCannotBuild"));
                    event.setCancelled(true);
                }
            }
        }
    }
}
