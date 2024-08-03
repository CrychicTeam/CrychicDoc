package com.rekindled.embers.api.event;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class EmberRemoveEvent extends Event {

    private Player player;

    private double amount = 0.0;

    private double originalAmount = 0.0;

    private List<Double> reductions = new ArrayList();

    public EmberRemoveEvent(Player player, double amount) {
        this.player = player;
        this.originalAmount = amount;
        this.amount = amount;
    }

    public Player getPlayer() {
        return this.player;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getOriginal() {
        return this.originalAmount;
    }

    public void addReduction(double reduction) {
        this.reductions.add(reduction);
    }

    public double getFinal() {
        double coeff = 0.0;
        for (double d : this.reductions) {
            coeff += d;
        }
        return this.amount * Math.max(0.0, 1.0 - coeff);
    }
}