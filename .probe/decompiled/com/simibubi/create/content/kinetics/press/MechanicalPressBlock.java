package com.simibubi.create.content.kinetics.press;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MechanicalPressBlock extends HorizontalKineticBlock implements IBE<MechanicalPressBlockEntity> {

    public MechanicalPressBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return context instanceof EntityCollisionContext && ((EntityCollisionContext) context).getEntity() instanceof Player ? AllShapes.CASING_14PX.get(Direction.DOWN) : AllShapes.MECHANICAL_PROCESSOR_SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return !AllBlocks.BASIN.has(worldIn.m_8055_(pos.below()));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction prefferedSide = this.getPreferredHorizontalFacing(context);
        return prefferedSide != null ? (BlockState) this.m_49966_().m_61124_(HORIZONTAL_FACING, prefferedSide) : super.getStateForPlacement(context);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(HORIZONTAL_FACING)).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == ((Direction) state.m_61143_(HORIZONTAL_FACING)).getAxis();
    }

    @Override
    public Class<MechanicalPressBlockEntity> getBlockEntityClass() {
        return MechanicalPressBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalPressBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends MechanicalPressBlockEntity>) AllBlockEntityTypes.MECHANICAL_PRESS.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}