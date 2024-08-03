package com.simibubi.create.content.contraptions.actors;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.BlockHelper;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AttachedActorBlock extends HorizontalDirectionalBlock implements IWrenchable, ProperWaterloggedBlock {

    protected AttachedActorBlock(BlockBehaviour.Properties p_i48377_1_) {
        super(p_i48377_1_);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.FAIL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction direction = (Direction) state.m_61143_(f_54117_);
        return AllShapes.HARVESTER_BASE.get(direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_54117_, WATERLOGGED);
        super.m_7926_(builder);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction direction = (Direction) state.m_61143_(f_54117_);
        BlockPos offset = pos.relative(direction.getOpposite());
        return BlockHelper.hasBlockSolidSide(worldIn.m_8055_(offset), worldIn, offset, direction);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing;
        if (context.m_43719_().getAxis().isVertical()) {
            facing = context.m_8125_().getOpposite();
        } else {
            BlockState blockState = context.m_43725_().getBlockState(context.getClickedPos().relative(context.m_43719_().getOpposite()));
            if (blockState.m_60734_() instanceof AttachedActorBlock) {
                facing = (Direction) blockState.m_61143_(f_54117_);
            } else {
                facing = context.m_43719_();
            }
        }
        return this.withWater((BlockState) this.m_49966_().m_61124_(f_54117_, facing), context);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        this.updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }
}