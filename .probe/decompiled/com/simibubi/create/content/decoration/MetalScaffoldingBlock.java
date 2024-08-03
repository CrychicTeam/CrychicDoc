package com.simibubi.create.content.decoration;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MetalScaffoldingBlock extends ScaffoldingBlock implements IWrenchable {

    public MetalScaffoldingBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRand) {
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.m_61143_(f_56014_) ? AllShapes.SCAFFOLD_HALF : super.getCollisionShape(pState, pLevel, pPos, pContext);
    }

    public boolean isScaffolding(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if ((Boolean) pState.m_61143_(f_56014_)) {
            return AllShapes.SCAFFOLD_HALF;
        } else {
            return !pContext.isHoldingItem(pState.m_60734_().asItem()) ? AllShapes.SCAFFOLD_FULL : Shapes.block();
        }
    }

    @Override
    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return Shapes.block();
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        BlockState stateBelow = pLevel.m_8055_(pCurrentPos.below());
        return pFacing == Direction.DOWN ? (BlockState) pState.m_61124_(f_56014_, !stateBelow.m_60713_(this) && !stateBelow.m_60783_(pLevel, pCurrentPos.below(), Direction.UP)) : pState;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        if (!(neighborState.m_60734_() instanceof MetalScaffoldingBlock)) {
            return false;
        } else {
            return !neighborState.m_61143_(f_56014_) && state.m_61143_(f_56014_) ? false : dir.getAxis() != Direction.Axis.Y;
        }
    }
}