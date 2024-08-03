package com.mna.api.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class EnderfeatherCharmUsedEvent extends Event {

    Player player;

    public EnderfeatherCharmUsedEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isCancelable() {
        return true;
    }
}