package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddAllowedEvent;
import com.worldcretornica.plotme_core.api.event.InternalPlotRedstoneChangeEvent;
import java.util.logging.Level;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdRedstone extends PlotCommand {

    public CmdRedstone(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(Player player, String[] args) {
        if (player.hasPermission(PermissionNames.ADMIN_REDSTONE) || player.hasPermission(PermissionNames.USER_REDSTONE)) {
            World world = player.getWorld();
            PlotMapInfo pmi = plugin.getPlotMeCoreManager().getMap(world);
            if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
                String id = PlotMeCoreManager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!PlotMeCoreManager.isPlotAvailable(id, pmi)) {
                    if (args.length < 2) {
                        player.sendMessage(C("WordUsage") + " §c/plotme redstone <" + C("WordEnable") + "|" + C("WordDisable") + ">");
                    } else {
                        Plot plot = PlotMeCoreManager.getPlotById(id, pmi);

                        boolean enabled = false;
                        if (args[1].equalsIgnoreCase(C("WordEnable"))) {
                            enabled = true;
                        } else if (!args[1].equalsIgnoreCase(C("WordDisable"))) {
                            player.sendMessage(C("WordUsage") + " §c/plotme redstone <" + C("WordEnable") + "|" + C("WordDisable") + ">");
                            return true;
                        }

                        if (plot.getOwner().equalsIgnoreCase(player.getName()) || player.hasPermission(PermissionNames.ADMIN_REDSTONE)) {
                            if (enabled && plot.isRedstoneProtect()) {
                                player.sendMessage("§c" + C("MsgAlreadyRedstoneEnabled"));
                            } else if (!enabled && !plot.isRedstoneProtect()) {
                                player.sendMessage("§c" + C("MsgAlreadyRedstoneDisabled"));
                            } else {
                                InternalPlotRedstoneChangeEvent event = serverBridge.getEventFactory().callPlotRedstoneChangeEvent(plugin, world, plot, player, enabled);
                                if (!event.isCancelled()) {
                                    plot.setRedstoneProtect(enabled);
                                    plugin.getSqlManager().updatePlot(PlotMeCoreManager.getIdX(id), PlotMeCoreManager.getIdZ(id), plot.getWorld(), "redstoneprotect", enabled);
                                    String word = C("WordDisabled");
                                    if (enabled) {
                                        player.sendMessage("§a" + C("MsgRedstoneNowEnabled"));
                                        word = C("WordEnabled");
                                    } else {
                                        player.sendMessage("§a" + C("MsgRedstoneNowDisabled"));
                                    }
                                    
                                    if (isAdvancedLogging()) {
                                        serverBridge.getLogger().log(Level.INFO, "{0} {1} redstone {2} {3} {4}", new Object[]{player.getName(), word, C("WordFor"), C("WordPlot"), id});
                                    }
                                }
                            }
                        } else {
                            player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgNotYoursNotAllowedAdd"));
                        }
                    }
                } else {
                    player.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage("§c" + C("MsgNotPlotWorld"));
            }
        } else {
            player.sendMessage("§c" + C("MsgPermissionDenied"));
            return false;
        }
        return true;
    }
}
