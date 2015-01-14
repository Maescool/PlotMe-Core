package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddDeniedEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.event.Cancellable;

public class PlotAddDeniedEvent extends PlotEvent implements Cancellable {

    private final InternalPlotAddDeniedEvent event;

    public PlotAddDeniedEvent(PlotMe_Core instance, org.bukkit.World world, Plot plot, org.bukkit.entity.Player player, String denied) {
        super(instance, plot, world);
        event = new InternalPlotAddDeniedEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(player), denied);
    }

    public PlotAddDeniedEvent(PlotMe_Core instance, World world, Plot plot, Player player, String denied) {
        super(instance, plot, world);
        event = new InternalPlotAddDeniedEvent(instance, world, plot, player, denied);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCanceled(cancel);
    }

    public org.bukkit.entity.Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public String getNewDenied() {
        return event.getNewDenied();
    }

    public InternalPlotAddDeniedEvent getInternal() {
        return event;
    }
}
