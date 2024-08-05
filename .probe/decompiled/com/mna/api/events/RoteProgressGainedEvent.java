package com.mna.api.events;

import com.mna.api.spells.base.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class RoteProgressGainedEvent extends Event {

    private final Player player;

    private final ISpellComponent part;

    private float amount;

    public RoteProgressGainedEvent(Player player, ISpellComponent part, float amount) {
        this.player = player;
        this.amount = amount;
        this.part = part;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ISpellComponent getPart() {
        return this.part;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isCancelable() {
        return true;
    }
}