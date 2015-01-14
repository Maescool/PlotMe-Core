package com.worldcretornica.plotme_core.api.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;

public class InternalPlotRedstoneChangeEvent extends InternalPlotEvent implements ICancellable {

    private final Player player;
    private final boolean _protected;
    private boolean canceled;

    public InternalPlotRedstoneChangeEvent(PlotMe_Core instance, World world, Plot plot, Player player, boolean protect) {
        super(instance, plot, world);
        this.player = player;
        _protected = protect;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean getProtected() {
        return _protected;
    }
}
