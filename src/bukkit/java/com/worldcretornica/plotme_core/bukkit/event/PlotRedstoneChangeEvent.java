package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRedstoneChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.event.Cancellable;

public class PlotRedstoneChangeEvent extends PlotEvent implements Cancellable {

    private final InternalPlotRedstoneChangeEvent event;

    public PlotRedstoneChangeEvent(PlotMe_Core instance, org.bukkit.World world, Plot plot, org.bukkit.entity.Player player, boolean protect) {
        super(plot, world);
        event = new InternalPlotRedstoneChangeEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(player), protect);
    }

    public PlotRedstoneChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, boolean protect) {
        super(plot, world);
        event = new InternalPlotRedstoneChangeEvent(instance, world, plot, player, protect);
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

    public boolean getProtected() {
        return event.getProtected();
    }

    public InternalPlotRedstoneChangeEvent getInternal() {
        return event;
    }
}
