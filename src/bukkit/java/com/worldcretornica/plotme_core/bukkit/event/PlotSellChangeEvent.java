package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotSellChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotSellChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotSellChangeEvent event;

    public PlotSellChangeEvent(PlotMe_Core instance, World world, Plot plot, Player seller, double price,
            boolean isForSale) {
        super(plot, world);
        event = new InternalPlotSellChangeEvent(new BukkitWorld(world), plot, new BukkitPlayer(seller), price, isForSale);
    }

    public PlotSellChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer seller, double price, boolean isForSale) {
        super(plot, world);
        event = new InternalPlotSellChangeEvent(world, plot, seller, price, isForSale);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public double getPrice() {
        return event.getPrice();
    }

    public boolean isForSale() {
        return event.isForSale();
    }

    public InternalPlotSellChangeEvent getInternal() {
        return event;
    }
}
