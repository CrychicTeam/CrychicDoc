package com.rekindled.embers.api.event;

import net.minecraft.world.level.block.entity.BlockEntity;

public class UpgradeEvent {

    BlockEntity tile;

    public UpgradeEvent(BlockEntity tile) {
        this.tile = tile;
    }

    public BlockEntity getTile() {
        return this.tile;
    }
}