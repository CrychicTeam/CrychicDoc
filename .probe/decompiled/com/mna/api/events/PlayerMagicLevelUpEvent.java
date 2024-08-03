package com.mna.api.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class PlayerMagicLevelUpEvent extends Event {

    private Player player;

    private int magicLevel;

    public PlayerMagicLevelUpEvent(Player player, int newLevel) {
        this.player = player;
        this.magicLevel = newLevel;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getMagicLevel() {
        return this.magicLevel;
    }
}