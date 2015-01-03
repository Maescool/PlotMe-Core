package com.worldcretornica.plotme_core.bukkit.utils;

import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import org.bukkit.entity.Player;

/**
 * Crafted in the heart of Wales by
 *
 * @author CaLxCyMru
 */
public class Utils {

    /**
     * Gets a IPlayer using a bukkit player
     *
     * @param player The bukkit player
     * @return The IPlayer
     */
    public static IPlayer getIPlayer(Player player){
        return new BukkitPlayer(player);
    }

}
