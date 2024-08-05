package com.mna.blocks.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;

public class MAWaterFlowerBlock extends MAFlowerBlock {

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

    public MAWaterFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties.noCollission());
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Vec3 vec3d = state.m_60824_(worldIn, pos);
        return SHAPE.move(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        super.m_7892_(state, worldIn, pos, entityIn);
        if (worldIn instanceof ServerLevel && entityIn instanceof Boat) {
            worldIn.m_46953_(new BlockPos(pos), true, entityIn);
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        FluidState ifluidstate = worldIn.getFluidState(pos);
        return ifluidstate.getType() == Fluids.WATER;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockState soil = worldIn.m_8055_(pos.below());
        return soil.canSustainPlant(worldIn, pos.below(), Direction.UP, this);
    }

    @Override
    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.WATER;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (!stateIn.m_60710_(worldIn, currentPos)) {
            worldIn.scheduleTick(currentPos, this, 1);
        }
        return super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        if (!state.m_60710_(worldIn, pos)) {
            worldIn.m_46961_(pos, true);
        }
    }
}