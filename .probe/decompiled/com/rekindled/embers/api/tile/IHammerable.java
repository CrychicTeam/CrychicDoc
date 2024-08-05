package com.rekindled.embers.api.tile;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IHammerable {

    void onHit(BlockEntity var1);

    default boolean isValid() {
        return true;
    }
}