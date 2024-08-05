package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class MithrilBlock extends BaseEntityBlock {

    public MithrilBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.MITHRIL_BLOCK_ENTITY.get().create(pPos, pState);
    }
}