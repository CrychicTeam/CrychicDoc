package com.rekindled.embers.api.tile;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface ISparkable {

    void sparkProgress(BlockEntity var1, double var2);
}