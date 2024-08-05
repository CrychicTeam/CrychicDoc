package com.mna.api.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class BonefeatherCharmUsedEvent extends Event {

    Player player;

    public BonefeatherCharmUsedEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isCancelable() {
        return false;
    }
}