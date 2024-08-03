package com.simibubi.create.foundation.blockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

public class SmartBlockEntityTicker<T extends BlockEntity> implements BlockEntityTicker<T> {

    @Override
    public void tick(Level level0, BlockPos blockPos1, BlockState blockState2, T t3) {
        if (!t3.hasLevel()) {
            t3.setLevel(level0);
        }
        ((SmartBlockEntity) t3).tick();
    }
}