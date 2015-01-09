package com.worldcretornica.plotme_core.bukkit;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IServerBridge;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

import java.io.IOException;

import static org.bukkit.Bukkit.getPluginManager;


public class PlotMe_CorePlugin extends JavaPlugin {

    private PlotMe_Core plotme;
    private IServerBridge serverObjectBuilder;

    @Override
    public void onDisable() {
        getAPI().disable();
    }

    @Override
    public void onEnable() {
        Plugin competition = getPluginManager().getPlugin("PlotSquared");
        if (competition != null) {
            getPluginLoader().disablePlugin(competition);
        }
        if (!Bukkit.getOnlineMode()) {
            getLogger().info("Offline Servers are not supported by PlotMe due to Mojangs recent change to UUID's.");
            getLogger().info("Since you are in offline mode, the plugin may not work properly.");
        }
        serverObjectBuilder = new BukkitServerBridge(this);
        plotme = new PlotMe_Core(serverObjectBuilder);
        getAPI().enable();
        doMetric();
    }

    public PlotMe_Core getAPI() {
        return plotme;
    }

    public IServerBridge getServerObjectBuilder() {
        return serverObjectBuilder;
    }

    /**
     * Metrics
     */
    private void doMetric() {
        try {
            Metrics metrics = new Metrics(this);

            Graph graphNbWorlds = metrics.createGraph("Plot worlds");

            graphNbWorlds.addPlotter(new Metrics.Plotter("Number of plot worlds") {
                @Override
                public int getValue() {
                    return getAPI().getPlotMeCoreManager().getPlotMaps().size();
                }
            });

            graphNbWorlds.addPlotter(new Metrics.Plotter("Average Plot size") {
                @Override
                public int getValue() {

                    if (!getAPI().getPlotMeCoreManager().getPlotMaps().isEmpty()) {
                        int totalplotsize = 0;

                        for (String plotter : getAPI().getPlotMeCoreManager().getPlotMaps().keySet()) {
                            if (getAPI().getPlotMeCoreManager().getGenMan(plotter) != null) {
                                if (getAPI().getPlotMeCoreManager().getGenMan(plotter).getPlotSize(plotter) != 0) {
                                    totalplotsize += getAPI().getPlotMeCoreManager().getGenMan(plotter).getPlotSize(plotter);
                                }
                            }
                        }

                        return totalplotsize / getAPI().getPlotMeCoreManager().getPlotMaps().size();
                    } else {
                        return 0;
                    }
                }
            });

            graphNbWorlds.addPlotter(new Metrics.Plotter("Number of plots") {
                @Override
                public int getValue() {
                    int nbplot = 0;

                    for (String map : getAPI().getPlotMeCoreManager().getPlotMaps().keySet()) {
                        nbplot += (int) getAPI().getSqlManager().getPlotCount(map);
                    }

                    return nbplot;
                }
            });

            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

    public BukkitPlayer wrapPlayer(Player player) {
        return new BukkitPlayer(player);
    }
}
