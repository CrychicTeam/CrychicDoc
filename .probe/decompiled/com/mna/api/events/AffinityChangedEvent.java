package com.mna.api.events;

import com.mna.api.affinity.Affinity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class AffinityChangedEvent extends Event {

    private final Player player;

    private final Affinity affinity;

    private final float currentAmount;

    private float shift;

    public AffinityChangedEvent(Player player, Affinity affinity, float currentAmount, float shift) {
        this.player = player;
        this.affinity = affinity;
        this.currentAmount = currentAmount;
        this.shift = shift;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Affinity getAffinity() {
        return this.affinity;
    }

    public float getCurrentAmount() {
        return this.currentAmount;
    }

    public float getShift() {
        return this.shift;
    }

    public void setShift(float shift) {
        this.shift = shift;
    }

    public boolean isCancelable() {
        return true;
    }
}