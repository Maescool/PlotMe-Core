package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.*;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotRedstoneChangeEvent;
import java.util.logging.Level;

public class CmdRedstone extends PlotCommand {

    public CmdRedstone(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer player, String[] args) {
        if (player.hasPermission(PermissionNames.ADMIN_REDSTONE) || player.hasPermission(PermissionNames.USER_REDSTONE)) {
            IWorld world = player.getWorld();
            PlotMapInfo pmi = manager.getMap(world);
            if (manager.isPlotWorld(world)) {
                String id = manager.getPlotId(player);
                if (id.isEmpty()) {
                    player.sendMessage("§c" + C("MsgNoPlotFound"));
                } else if (!manager.isPlotAvailable(id, pmi)) {
                    if (args.length < 2) {
                        player.sendMessage(C("WordUsage") + " §c/plotme redstone <" + C("WordEnable") + "|" + C("WordDisable") + ">");
                    } else {
                        Plot plot = manager.getPlotById(id, pmi);

                        boolean enabled = false;
                        if (args[1].equalsIgnoreCase(C("WordEnable"))) {
                            enabled = true;
                        } else if (!args[1].equalsIgnoreCase(C("WordDisable"))) {
                            player.sendMessage(C("WordUsage") + " §c/plotme redstone <" + C("WordEnable") + "|" + C("WordDisable") + ">");
                            return true;
                        }

                        if (plot.getOwner().equalsIgnoreCase(player.getName()) || player.hasPermission(PermissionNames.ADMIN_REDSTONE)) {
                            if (enabled && plot.isRedstoneProtect()) {
                                player.sendMessage("§c" + C("MsgRedstoneAlreadyEnabled"));
                            } else if (!enabled && !plot.isRedstoneProtect()) {
                                player.sendMessage("§c" + C("MsgRedstoneAlreadyDisabled"));
                            } else {
                                InternalPlotRedstoneChangeEvent event = serverBridge.getEventFactory().callPlotRedstoneChangeEvent(plugin, world, plot, player, enabled);
                                if (!event.isCancelled()) {
                                    plot.setRedstoneProtect(enabled);
                                    plugin.getSqlManager().updatePlot(manager.getIdX(id), manager.getIdZ(id), plot.getWorld(), "redstoneprotect", enabled);
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
