package com.github.alexmodguy.alexscaves.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.NetherVines;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArchaicVineBlock extends GrowingPlantHeadBlock {

    protected static final VoxelShape SHAPE = Block.box(4.0, 9.0, 4.0, 12.0, 16.0, 12.0);

    public ArchaicVineBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).randomTicks().noCollission().instabreak().sound(SoundType.VINE), Direction.DOWN, SHAPE, false, 0.1);
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource randomSource) {
        return NetherVines.getBlocksToGrowWhenBonemealed(randomSource);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
    }

    @Override
    protected Block getBodyBlock() {
        return ACBlockRegistry.ARCHAIC_VINE_PLANT.get();
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.m_60795_();
    }
}