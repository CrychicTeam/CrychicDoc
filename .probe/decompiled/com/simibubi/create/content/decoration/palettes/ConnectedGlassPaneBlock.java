package com.simibubi.create.content.decoration.palettes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ConnectedGlassPaneBlock extends GlassPaneBlock {

    public ConnectedGlassPaneBlock(BlockBehaviour.Properties builder) {
        super(builder);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return side.getAxis().isVertical() ? adjacentBlockState == state : super.m_6104_(state, adjacentBlockState, side);
    }
}