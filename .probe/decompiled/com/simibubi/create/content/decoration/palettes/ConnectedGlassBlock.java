package com.simibubi.create.content.decoration.palettes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ConnectedGlassBlock extends GlassBlock {

    public ConnectedGlassBlock(BlockBehaviour.Properties p_i48392_1_) {
        super(p_i48392_1_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return adjacentBlockState.m_60734_() instanceof ConnectedGlassBlock ? true : super.m_6104_(state, adjacentBlockState, side);
    }

    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
        return true;
    }
}