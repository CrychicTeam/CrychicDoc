package com.rekindled.embers.api.event;

import net.minecraft.world.level.block.entity.BlockEntity;

public class EmberEvent extends UpgradeEvent {

    EmberEvent.EnumType type;

    double amount;

    public EmberEvent(BlockEntity tile, EmberEvent.EnumType type, double amount) {
        super(tile);
        this.type = type;
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    public EmberEvent.EnumType getType() {
        return this.type;
    }

    public static enum EnumType {

        PRODUCE, CONSUME, TRANSFER
    }
}