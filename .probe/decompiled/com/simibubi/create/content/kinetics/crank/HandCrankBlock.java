package com.simibubi.create.content.kinetics.crank;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HandCrankBlock extends DirectionalKineticBlock implements IBE<HandCrankBlockEntity>, ProperWaterloggedBlock {

    public HandCrankBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.CRANK.get((Direction) state.m_61143_(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    }

    public int getRotationSpeed() {
        return 32;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player.isSpectator()) {
            return InteractionResult.PASS;
        } else {
            this.withBlockEntityDo(worldIn, pos, be -> be.turn(player.m_6144_()));
            if (!player.m_21120_(handIn).is((Item) AllItems.EXTENDO_GRIP.get())) {
                player.causeFoodExhaustion((float) this.getRotationSpeed() * AllConfigs.server().kinetics.crankHungerMultiplier.getF());
            }
            if (player.getFoodData().getFoodLevel() == 0) {
                AllAdvancements.HAND_CRANK.awardTo(player);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = this.getPreferredFacing(context);
        BlockState defaultBlockState = this.withWater(this.m_49966_(), context);
        return preferred != null && (context.m_43723_() == null || !context.m_43723_().m_6144_()) ? (BlockState) defaultBlockState.m_61124_(FACING, preferred.getOpposite()) : (BlockState) defaultBlockState.m_61124_(FACING, context.m_43719_());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction facing = ((Direction) state.m_61143_(FACING)).getOpposite();
        BlockPos neighbourPos = pos.relative(facing);
        BlockState neighbour = worldIn.m_8055_(neighbourPos);
        return !neighbour.m_60812_(worldIn, neighbourPos).isEmpty();
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            Direction blockFacing = (Direction) state.m_61143_(FACING);
            if (fromPos.equals(pos.relative(blockFacing.getOpposite())) && !this.canSurvive(state, worldIn, pos)) {
                worldIn.m_46961_(pos, true);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        this.updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == ((Direction) state.m_61143_(FACING)).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(FACING)).getAxis();
    }

    @Override
    public Class<HandCrankBlockEntity> getBlockEntityClass() {
        return HandCrankBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HandCrankBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends HandCrankBlockEntity>) AllBlockEntityTypes.HAND_CRANK.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    public static Couple<Integer> getSpeedRange() {
        return Couple.create(32, 32);
    }
}