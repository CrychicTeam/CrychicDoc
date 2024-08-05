package com.rekindled.embers.api.tile;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface IEmberInjectable {

    void inject(BlockEntity var1, double var2);

    default boolean isValid() {
        return true;
    }
}