package com.rekindled.embers.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class InfoGogglesEvent extends Event {

    Player player;

    boolean shouldDisplay;

    public InfoGogglesEvent(Player player, boolean shouldDisplay) {
        this.player = player;
        this.shouldDisplay = shouldDisplay;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean shouldDisplay() {
        return this.shouldDisplay;
    }

    public void setShouldDisplay(boolean shouldDisplay) {
        this.shouldDisplay = shouldDisplay;
    }
}