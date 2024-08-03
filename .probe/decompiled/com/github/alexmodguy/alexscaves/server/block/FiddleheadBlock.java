package com.github.alexmodguy.alexscaves.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FiddleheadBlock extends CavePlantBlock {

    protected static final VoxelShape SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 8.0, 10.0);

    public FiddleheadBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).noCollission().instabreak().randomTicks().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.m_60824_(getter, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public void randomTick(BlockState currentState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        super.m_213898_(currentState, level, blockPos, randomSource);
        if (randomSource.nextInt(7) == 0 && level.m_8055_(blockPos.above()).m_60795_()) {
            Direction facing = Direction.fromYRot((double) (randomSource.nextFloat() * 360.0F));
            level.m_46597_(blockPos, (BlockState) ((BlockState) ACBlockRegistry.CURLY_FERN.get().defaultBlockState().m_61124_(DoublePlantWithRotationBlock.f_52858_, DoubleBlockHalf.LOWER)).m_61124_(DoublePlantWithRotationBlock.FACING, facing));
            level.m_46597_(blockPos.above(), (BlockState) ((BlockState) ACBlockRegistry.CURLY_FERN.get().defaultBlockState().m_61124_(DoublePlantWithRotationBlock.f_52858_, DoubleBlockHalf.UPPER)).m_61124_(DoublePlantWithRotationBlock.FACING, facing));
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter getter, BlockPos pos) {
        return blockState.m_60713_(Blocks.GRASS_BLOCK) || blockState.m_60713_(Blocks.MOSS_BLOCK);
    }
}