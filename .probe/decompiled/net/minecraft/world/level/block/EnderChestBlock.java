package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnderChestBlock extends AbstractChestBlock<EnderChestBlockEntity> implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

    private static final Component CONTAINER_TITLE = Component.translatable("container.enderchest");

    protected EnderChestBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0, () -> BlockEntityType.ENDER_CHEST);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState blockState0, Level level1, BlockPos blockPos2, boolean boolean3) {
        return DoubleBlockCombiner.Combiner::m_6502_;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState $$1 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.m_8125_().getOpposite())).m_61124_(WATERLOGGED, $$1.getType() == Fluids.WATER);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        PlayerEnderChestContainer $$6 = player3.getEnderChestInventory();
        BlockEntity $$7 = level1.getBlockEntity(blockPos2);
        if ($$6 != null && $$7 instanceof EnderChestBlockEntity) {
            BlockPos $$8 = blockPos2.above();
            if (level1.getBlockState($$8).m_60796_(level1, $$8)) {
                return InteractionResult.sidedSuccess(level1.isClientSide);
            } else if (level1.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                EnderChestBlockEntity $$9 = (EnderChestBlockEntity) $$7;
                $$6.setActiveChest($$9);
                player3.openMenu(new SimpleMenuProvider((p_53124_, p_53125_, p_53126_) -> ChestMenu.threeRows(p_53124_, p_53125_, $$6), CONTAINER_TITLE));
                player3.awardStat(Stats.OPEN_ENDERCHEST);
                PiglinAi.angerNearbyPiglins(player3, true);
                return InteractionResult.CONSUME;
            }
        } else {
            return InteractionResult.sidedSuccess(level1.isClientSide);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new EnderChestBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return level0.isClientSide ? m_152132_(blockEntityTypeT2, BlockEntityType.ENDER_CHEST, EnderChestBlockEntity::m_155517_) : null;
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        for (int $$4 = 0; $$4 < 3; $$4++) {
            int $$5 = randomSource3.nextInt(2) * 2 - 1;
            int $$6 = randomSource3.nextInt(2) * 2 - 1;
            double $$7 = (double) blockPos2.m_123341_() + 0.5 + 0.25 * (double) $$5;
            double $$8 = (double) ((float) blockPos2.m_123342_() + randomSource3.nextFloat());
            double $$9 = (double) blockPos2.m_123343_() + 0.5 + 0.25 * (double) $$6;
            double $$10 = (double) (randomSource3.nextFloat() * (float) $$5);
            double $$11 = ((double) randomSource3.nextFloat() - 0.5) * 0.125;
            double $$12 = (double) (randomSource3.nextFloat() * (float) $$6);
            level1.addParticle(ParticleTypes.PORTAL, $$7, $$8, $$9, $$10, $$11, $$12);
        }
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        BlockEntity $$4 = serverLevel1.m_7702_(blockPos2);
        if ($$4 instanceof EnderChestBlockEntity) {
            ((EnderChestBlockEntity) $$4).recheckOpen();
        }
    }
}