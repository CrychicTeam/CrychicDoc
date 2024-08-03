package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LecternBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;

    public static final VoxelShape SHAPE_BASE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    public static final VoxelShape SHAPE_POST = Block.box(4.0, 2.0, 4.0, 12.0, 14.0, 12.0);

    public static final VoxelShape SHAPE_COMMON = Shapes.or(SHAPE_BASE, SHAPE_POST);

    public static final VoxelShape SHAPE_TOP_PLATE = Block.box(0.0, 15.0, 0.0, 16.0, 15.0, 16.0);

    public static final VoxelShape SHAPE_COLLISION = Shapes.or(SHAPE_COMMON, SHAPE_TOP_PLATE);

    public static final VoxelShape SHAPE_WEST = Shapes.or(Block.box(1.0, 10.0, 0.0, 5.333333, 14.0, 16.0), Block.box(5.333333, 12.0, 0.0, 9.666667, 16.0, 16.0), Block.box(9.666667, 14.0, 0.0, 14.0, 18.0, 16.0), SHAPE_COMMON);

    public static final VoxelShape SHAPE_NORTH = Shapes.or(Block.box(0.0, 10.0, 1.0, 16.0, 14.0, 5.333333), Block.box(0.0, 12.0, 5.333333, 16.0, 16.0, 9.666667), Block.box(0.0, 14.0, 9.666667, 16.0, 18.0, 14.0), SHAPE_COMMON);

    public static final VoxelShape SHAPE_EAST = Shapes.or(Block.box(10.666667, 10.0, 0.0, 15.0, 14.0, 16.0), Block.box(6.333333, 12.0, 0.0, 10.666667, 16.0, 16.0), Block.box(2.0, 14.0, 0.0, 6.333333, 18.0, 16.0), SHAPE_COMMON);

    public static final VoxelShape SHAPE_SOUTH = Shapes.or(Block.box(0.0, 10.0, 10.666667, 16.0, 14.0, 15.0), Block.box(0.0, 12.0, 6.333333, 16.0, 16.0, 10.666667), Block.box(0.0, 14.0, 2.0, 16.0, 18.0, 6.333333), SHAPE_COMMON);

    private static final int PAGE_CHANGE_IMPULSE_TICKS = 2;

    protected LecternBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(POWERED, false)).m_61124_(HAS_BOOK, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return SHAPE_COMMON;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        Level $$1 = blockPlaceContext0.m_43725_();
        ItemStack $$2 = blockPlaceContext0.m_43722_();
        Player $$3 = blockPlaceContext0.m_43723_();
        boolean $$4 = false;
        if (!$$1.isClientSide && $$3 != null && $$3.canUseGameMasterBlocks()) {
            CompoundTag $$5 = BlockItem.getBlockEntityData($$2);
            if ($$5 != null && $$5.contains("Book")) {
                $$4 = true;
            }
        }
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.m_8125_().getOpposite())).m_61124_(HAS_BOOK, $$4);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE_COLLISION;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        switch((Direction) blockState0.m_61143_(FACING)) {
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case EAST:
                return SHAPE_EAST;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_COMMON;
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
        stateDefinitionBuilderBlockBlockState0.add(FACING, POWERED, HAS_BOOK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new LecternBlockEntity(blockPos0, blockState1);
    }

    public static boolean tryPlaceBook(@Nullable Entity entity0, Level level1, BlockPos blockPos2, BlockState blockState3, ItemStack itemStack4) {
        if (!(Boolean) blockState3.m_61143_(HAS_BOOK)) {
            if (!level1.isClientSide) {
                placeBook(entity0, level1, blockPos2, blockState3, itemStack4);
            }
            return true;
        } else {
            return false;
        }
    }

    private static void placeBook(@Nullable Entity entity0, Level level1, BlockPos blockPos2, BlockState blockState3, ItemStack itemStack4) {
        if (level1.getBlockEntity(blockPos2) instanceof LecternBlockEntity $$6) {
            $$6.setBook(itemStack4.split(1));
            resetBookState(entity0, level1, blockPos2, blockState3, true);
            level1.playSound(null, blockPos2, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public static void resetBookState(@Nullable Entity entity0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        BlockState $$5 = (BlockState) ((BlockState) blockState3.m_61124_(POWERED, false)).m_61124_(HAS_BOOK, boolean4);
        level1.setBlock(blockPos2, $$5, 3);
        level1.m_220407_(GameEvent.BLOCK_CHANGE, blockPos2, GameEvent.Context.of(entity0, $$5));
        updateBelow(level1, blockPos2, blockState3);
    }

    public static void signalPageChange(Level level0, BlockPos blockPos1, BlockState blockState2) {
        changePowered(level0, blockPos1, blockState2, true);
        level0.m_186460_(blockPos1, blockState2.m_60734_(), 2);
        level0.m_46796_(1043, blockPos1, 0);
    }

    private static void changePowered(Level level0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        level0.setBlock(blockPos1, (BlockState) blockState2.m_61124_(POWERED, boolean3), 3);
        updateBelow(level0, blockPos1, blockState2);
    }

    private static void updateBelow(Level level0, BlockPos blockPos1, BlockState blockState2) {
        level0.updateNeighborsAt(blockPos1.below(), blockState2.m_60734_());
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        changePowered(serverLevel1, blockPos2, blockState0, false);
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            if ((Boolean) blockState0.m_61143_(HAS_BOOK)) {
                this.popBook(blockState0, level1, blockPos2);
            }
            if ((Boolean) blockState0.m_61143_(POWERED)) {
                level1.updateNeighborsAt(blockPos2.below(), this);
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    private void popBook(BlockState blockState0, Level level1, BlockPos blockPos2) {
        if (level1.getBlockEntity(blockPos2) instanceof LecternBlockEntity $$4) {
            Direction $$5 = (Direction) blockState0.m_61143_(FACING);
            ItemStack $$6 = $$4.getBook().copy();
            float $$7 = 0.25F * (float) $$5.getStepX();
            float $$8 = 0.25F * (float) $$5.getStepZ();
            ItemEntity $$9 = new ItemEntity(level1, (double) blockPos2.m_123341_() + 0.5 + (double) $$7, (double) (blockPos2.m_123342_() + 1), (double) blockPos2.m_123343_() + 0.5 + (double) $$8, $$6);
            $$9.setDefaultPickUpDelay();
            level1.m_7967_($$9);
            $$4.clearContent();
        }
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return direction3 == Direction.UP && blockState0.m_61143_(POWERED) ? 15 : 0;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        if ((Boolean) blockState0.m_61143_(HAS_BOOK)) {
            BlockEntity $$3 = level1.getBlockEntity(blockPos2);
            if ($$3 instanceof LecternBlockEntity) {
                return ((LecternBlockEntity) $$3).getRedstoneSignal();
            }
        }
        return 0;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if ((Boolean) blockState0.m_61143_(HAS_BOOK)) {
            if (!level1.isClientSide) {
                this.openScreen(level1, blockPos2, player3);
            }
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            ItemStack $$6 = player3.m_21120_(interactionHand4);
            return !$$6.isEmpty() && !$$6.is(ItemTags.LECTERN_BOOKS) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return !blockState0.m_61143_(HAS_BOOK) ? null : super.getMenuProvider(blockState0, level1, blockPos2);
    }

    private void openScreen(Level level0, BlockPos blockPos1, Player player2) {
        BlockEntity $$3 = level0.getBlockEntity(blockPos1);
        if ($$3 instanceof LecternBlockEntity) {
            player2.openMenu((LecternBlockEntity) $$3);
            player2.awardStat(Stats.INTERACT_WITH_LECTERN);
        }
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}