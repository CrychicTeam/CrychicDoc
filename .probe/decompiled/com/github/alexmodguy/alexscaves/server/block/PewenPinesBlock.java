package com.github.alexmodguy.alexscaves.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PewenPinesBlock extends BushBlock {

    public static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

    public PewenPinesBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN).instabreak().sound(SoundType.ROOTS).randomTicks().offsetType(BlockBehaviour.OffsetType.XZ).noOcclusion().replaceable().noCollission());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return state.m_60734_() == this ? levelReader.m_8055_(blockpos).m_60783_(levelReader, blockpos, Direction.UP) : this.m_6266_(levelReader.m_8055_(blockpos), levelReader, blockpos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.m_60824_(getter, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public float getMaxHorizontalOffset() {
        return 0.1F;
    }
}