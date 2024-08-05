package com.simibubi.create.content.kinetics.transmission;

import com.simibubi.create.AllBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ClutchBlock extends GearshiftBlock {

    public ClutchBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
            if (previouslyPowered != worldIn.m_276867_(pos)) {
                worldIn.setBlock(pos, (BlockState) state.m_61122_(POWERED), 18);
                this.detachKinetics(worldIn, pos, previouslyPowered);
            }
        }
    }

    @Override
    public BlockEntityType<? extends SplitShaftBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends SplitShaftBlockEntity>) AllBlockEntityTypes.CLUTCH.get();
    }
}