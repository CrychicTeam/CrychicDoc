package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DaylightDetectorBlockEntity extends BlockEntity {

    public DaylightDetectorBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.DAYLIGHT_DETECTOR, blockPos0, blockState1);
    }
}