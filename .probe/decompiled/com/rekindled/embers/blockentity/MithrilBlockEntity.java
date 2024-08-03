package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MithrilBlockEntity extends BlockEntity {

    public MithrilBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.MITHRIL_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}