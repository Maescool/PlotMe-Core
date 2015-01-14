package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.api.IBiome;
import com.worldcretornica.plotme_core.api.IPlotMe_GeneratorManager;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBiome;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import com.worldcretornica.plotme_core.bukkit.api.IBukkitPlotMe_GeneratorManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BukkitPlotMe_GeneratorManagerBridge implements IPlotMe_GeneratorManager {

    private final IBukkitPlotMe_GeneratorManager generatorManager;

    public BukkitPlotMe_GeneratorManagerBridge(IBukkitPlotMe_GeneratorManager generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public String getPlotId(Location location) {
        return generatorManager.getPlotId(((BukkitLocation) location).getLocation());
    }

    @Override
    public String getPlotId(Player player) {
        return generatorManager.getPlotId(((BukkitPlayer) player).getPlayer());
    }

    @Override
    public List<Player> getPlayersInPlot(String id) {
        List<Player> players = new ArrayList<>();

        for (org.bukkit.entity.Player player : generatorManager.getPlayersInPlot(id)) {
            players.add(new BukkitPlayer(player));
        }

        return players;
    }

    @Override
    public void fillroad(String id1, String id2, World world) {
        generatorManager.fillroad(id1, id2, ((BukkitWorld) world).getWorld());
    }

    @Override
    public void fillmiddleroad(String id1, String id2, World world) {
        generatorManager.fillmiddleroad(id1, id2, ((BukkitWorld) world).getWorld());
    }

    @Override
    public void setOwnerDisplay(World world, String id, String line1, String line2, String line3, String line4) {
        generatorManager.setOwnerDisplay(((BukkitWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void setSellerDisplay(World world, String id, String line1, String line2, String line3, String line4) {
        generatorManager.setSellerDisplay(((BukkitWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void setAuctionDisplay(World world, String id, String line1, String line2, String line3, String line4) {
        generatorManager.setAuctionDisplay(((BukkitWorld) world).getWorld(), id, line1, line2, line3, line4);
    }

    @Override
    public void removeOwnerDisplay(World world, String id) {
        generatorManager.removeOwnerDisplay(((BukkitWorld) world).getWorld(), id);
    }

    @Override
    public void removeSellerDisplay(World world, String id) {
        generatorManager.removeSellerDisplay(((BukkitWorld) world).getWorld(), id);
    }

    @Override
    public void removeAuctionDisplay(World world, String id) {
        generatorManager.removeAuctionDisplay(((BukkitWorld) world).getWorld(), id);
    }

    @Override
    public int getIdX(String id) {
        return generatorManager.getIdX(id);
    }

    @Override
    public int getIdZ(String id) {
        return generatorManager.getIdZ(id);
    }

    @Override
    public Location getPlotBottomLoc(World world, String id) {
        return new BukkitLocation(generatorManager.getPlotBottomLoc(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public Location getPlotTopLoc(World world, String id) {
        return new BukkitLocation(generatorManager.getPlotTopLoc(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public void setBiome(World world, String id, IBiome biome) {
        generatorManager.setBiome(((BukkitWorld) world).getWorld(), id, ((BukkitBiome) biome).getBiome());
    }

    @Override
    public void refreshPlotChunks(World world, String id) {
        generatorManager.refreshPlotChunks(((BukkitWorld) world).getWorld(), id);
    }

    @Override
    public Location getTop(World world, String id) {
        return new BukkitLocation(generatorManager.getTop(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public Location getBottom(World world, String id) {
        return new BukkitLocation(generatorManager.getBottom(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public void clear(Location bottom, Location top) {
        generatorManager.clear(((BukkitLocation) bottom).getLocation(), ((BukkitLocation) top).getLocation());
    }

    @Override
    public Long[] clear(Location bottom, Location top, long maxBlocks, Long[] start) {
        return generatorManager.clear(((BukkitLocation) bottom).getLocation(), ((BukkitLocation) top).getLocation(), maxBlocks, start);
    }

    @Override
    public Long[] clear(World world, String id, long maxBlocks, Long[] start) {
        return generatorManager.clear(((BukkitWorld) world).getWorld(), id, maxBlocks, start);
    }

    @Override
    public void adjustPlotFor(World world, String id, boolean claimed, boolean protect, boolean auctionned, boolean forSale) {
        generatorManager.adjustPlotFor(((BukkitWorld) world).getWorld(), id, claimed, protect, auctionned, forSale);
    }

    @Override
    public boolean isBlockInPlot(String id, Location location) {
        return generatorManager.isBlockInPlot(id, ((BukkitLocation) location).getLocation());
    }

    @Override
    public boolean movePlot(World world, String idFrom, String idTo) {
        return generatorManager.movePlot(((BukkitWorld) world).getWorld(), idFrom, idTo);
    }

    @Override
    public int bottomX(String id, World world) {
        return generatorManager.bottomX(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public int bottomZ(String id, World world) {
        return generatorManager.bottomZ(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public int topX(String id, World world) {
        return generatorManager.topX(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public int topZ(String id, World world) {
        return generatorManager.topZ(id, ((BukkitWorld) world).getWorld());
    }

    @Override
    public Location getPlotHome(World world, String id) {
        return new BukkitLocation(generatorManager.getPlotHome(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public boolean isValidId(String id) {
        return generatorManager.isValidId(id);
    }

    @Override
    public boolean createConfig(String worldname, Map<String, String> args) {
        return generatorManager.createConfig(worldname, args);
    }

    @Override
    public Map<String, String> getDefaultGenerationConfig() {
        return generatorManager.getDefaultGenerationConfig();
    }

    @Override
    public int getPlotSize(String worldname) {
        return generatorManager.getPlotSize(worldname);
    }

    @Override
    public int getRoadHeight(String worldname) {
        return generatorManager.getRoadHeight(worldname);
    }

}
