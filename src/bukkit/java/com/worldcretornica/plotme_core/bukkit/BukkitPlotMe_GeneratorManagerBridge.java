package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.api.*;
import com.worldcretornica.plotme_core.bukkit.api.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BukkitPlotMe_GeneratorManagerBridge implements IPlotMe_GeneratorManager {

    private final IBukkitPlotMe_GeneratorManager generatorManager;

    public BukkitPlotMe_GeneratorManagerBridge(IBukkitPlotMe_GeneratorManager generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public String getPlotId(ILocation location) {
        return generatorManager.getPlotId(((BukkitLocation) location).getLocation());
    }

    @Override
    public String getPlotId(IPlayer player) {
        return generatorManager.getPlotId(((BukkitPlayer) player).getPlayer());
    }

    @Override
    public List<IPlayer> getPlayersInPlot(String id) {
        List<IPlayer> players = new ArrayList<>();

        for (Player player : generatorManager.getPlayersInPlot(id)) {
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
    public ILocation getPlotBottomLoc(World world, String id) {
        return new BukkitLocation(generatorManager.getPlotBottomLoc(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public ILocation getPlotTopLoc(World world, String id) {
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
    public ILocation getTop(World world, String id) {
        return new BukkitLocation(generatorManager.getTop(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public ILocation getBottom(World world, String id) {
        return new BukkitLocation(generatorManager.getBottom(((BukkitWorld) world).getWorld(), id));
    }

    @Override
    public void clear(ILocation bottom, ILocation top) {
        generatorManager.clear(((BukkitLocation) bottom).getLocation(), ((BukkitLocation) top).getLocation());
    }

    @Override
    public Long[] clear(ILocation bottom, ILocation top, long maxBlocks, Long[] start) {
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
    public boolean isBlockInPlot(String id, ILocation location) {
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
    public ILocation getPlotHome(World world, String id) {
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
