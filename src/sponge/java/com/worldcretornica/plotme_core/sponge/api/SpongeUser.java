package com.worldcretornica.plotme_core.sponge.api;

import java.util.UUID;

import org.spongepowered.api.entity.player.User;

import com.worldcretornica.plotme_core.api.IOfflinePlayer;

public class SpongeUser implements IOfflinePlayer {

    private final User user;
    
    public SpongeUser(User user) {
        this.user = user;
    }

    @Override
    public boolean isOnline() {
        return user.isOnline();
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public UUID getUniqueId() {
        return user.getUniqueId();
    }    
}
