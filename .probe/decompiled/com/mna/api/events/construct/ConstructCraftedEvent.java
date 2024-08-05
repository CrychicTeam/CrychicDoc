package com.mna.api.events.construct;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class ConstructCraftedEvent extends Event {

    Entity construct;

    Player crafter;

    public ConstructCraftedEvent(Entity construct, Player crafter) {
        this.construct = construct;
        this.crafter = crafter;
    }

    public Entity getConstruct() {
        return this.construct;
    }

    public Player getCrafter() {
        return this.crafter;
    }

    public boolean isCancelable() {
        return false;
    }
}