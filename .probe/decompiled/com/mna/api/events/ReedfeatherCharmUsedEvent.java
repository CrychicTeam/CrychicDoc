package com.mna.api.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class ReedfeatherCharmUsedEvent extends Event {

    Player player;

    public ReedfeatherCharmUsedEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isCancelable() {
        return false;
    }
}