package com.simibubi.create.content.contraptions.pulley;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PulleyBlock extends HorizontalAxisKineticBlock implements IBE<PulleyBlockEntity> {

    public PulleyBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    private static void onRopeBroken(Level world, BlockPos pulleyPos) {
        if (world.getBlockEntity(pulleyPos) instanceof PulleyBlockEntity pulley) {
            pulley.initialOffset = 0;
            pulley.onLengthBroken();
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        super.m_6810_(state, worldIn, pos, newState, isMoving);
        if (!state.m_60713_(newState.m_60734_())) {
            if (!worldIn.isClientSide) {
                BlockState below = worldIn.getBlockState(pos.below());
                if (below.m_60734_() instanceof PulleyBlock.RopeBlockBase) {
                    worldIn.m_46961_(pos.below(), true);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.mayBuild()) {
            return InteractionResult.PASS;
        } else if (player.m_6144_()) {
            return InteractionResult.PASS;
        } else if (player.m_21120_(handIn).isEmpty()) {
            this.withBlockEntityDo(worldIn, pos, be -> be.assembleNextTick = true);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.PULLEY.get((Direction.Axis) state.m_61143_(HORIZONTAL_AXIS));
    }

    @Override
    public Class<PulleyBlockEntity> getBlockEntityClass() {
        return PulleyBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PulleyBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends PulleyBlockEntity>) AllBlockEntityTypes.ROPE_PULLEY.get();
    }

    public static class MagnetBlock extends PulleyBlock.RopeBlockBase {

        public MagnetBlock(BlockBehaviour.Properties properties) {
            super(properties);
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
            return AllShapes.PULLEY_MAGNET;
        }
    }

    public static class RopeBlock extends PulleyBlock.RopeBlockBase {

        public RopeBlock(BlockBehaviour.Properties properties) {
            super(properties);
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
            return AllShapes.FOUR_VOXEL_POLE.get(Direction.UP);
        }
    }

    private static class RopeBlockBase extends Block implements SimpleWaterloggedBlock {

        public RopeBlockBase(BlockBehaviour.Properties properties) {
            super(properties);
            this.m_49959_((BlockState) super.defaultBlockState().m_61124_(BlockStateProperties.WATERLOGGED, false));
        }

        @Override
        public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
            return false;
        }

        public PushReaction getPistonPushReaction(BlockState state) {
            return PushReaction.BLOCK;
        }

        public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
            return AllBlocks.ROPE_PULLEY.asStack();
        }

        @Override
        public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
            if (!isMoving && (!state.m_61138_(BlockStateProperties.WATERLOGGED) || !newState.m_61138_(BlockStateProperties.WATERLOGGED) || state.m_61143_(BlockStateProperties.WATERLOGGED) == newState.m_61143_(BlockStateProperties.WATERLOGGED))) {
                PulleyBlock.onRopeBroken(worldIn, pos.above());
                if (!worldIn.isClientSide) {
                    BlockState above = worldIn.getBlockState(pos.above());
                    BlockState below = worldIn.getBlockState(pos.below());
                    if (above.m_60734_() instanceof PulleyBlock.RopeBlockBase) {
                        worldIn.m_46961_(pos.above(), true);
                    }
                    if (below.m_60734_() instanceof PulleyBlock.RopeBlockBase) {
                        worldIn.m_46961_(pos.below(), true);
                    }
                }
            }
            if (state.m_155947_() && state.m_60734_() != newState.m_60734_()) {
                worldIn.removeBlockEntity(pos);
            }
        }

        @Override
        public FluidState getFluidState(BlockState state) {
            return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(BlockStateProperties.WATERLOGGED);
            super.createBlockStateDefinition(builder);
        }

        @Override
        public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
            if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(world));
            }
            return state;
        }

        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
            FluidState FluidState = context.m_43725_().getFluidState(context.getClickedPos());
            return (BlockState) super.getStateForPlacement(context).m_61124_(BlockStateProperties.WATERLOGGED, FluidState.getType() == Fluids.WATER);
        }
    }
}