package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BellBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final EnumProperty<BellAttachType> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final VoxelShape NORTH_SOUTH_FLOOR_SHAPE = Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 12.0);

    private static final VoxelShape EAST_WEST_FLOOR_SHAPE = Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 16.0);

    private static final VoxelShape BELL_TOP_SHAPE = Block.box(5.0, 6.0, 5.0, 11.0, 13.0, 11.0);

    private static final VoxelShape BELL_BOTTOM_SHAPE = Block.box(4.0, 4.0, 4.0, 12.0, 6.0, 12.0);

    private static final VoxelShape BELL_SHAPE = Shapes.or(BELL_BOTTOM_SHAPE, BELL_TOP_SHAPE);

    private static final VoxelShape NORTH_SOUTH_BETWEEN = Shapes.or(BELL_SHAPE, Block.box(7.0, 13.0, 0.0, 9.0, 15.0, 16.0));

    private static final VoxelShape EAST_WEST_BETWEEN = Shapes.or(BELL_SHAPE, Block.box(0.0, 13.0, 7.0, 16.0, 15.0, 9.0));

    private static final VoxelShape TO_WEST = Shapes.or(BELL_SHAPE, Block.box(0.0, 13.0, 7.0, 13.0, 15.0, 9.0));

    private static final VoxelShape TO_EAST = Shapes.or(BELL_SHAPE, Block.box(3.0, 13.0, 7.0, 16.0, 15.0, 9.0));

    private static final VoxelShape TO_NORTH = Shapes.or(BELL_SHAPE, Block.box(7.0, 13.0, 0.0, 9.0, 15.0, 13.0));

    private static final VoxelShape TO_SOUTH = Shapes.or(BELL_SHAPE, Block.box(7.0, 13.0, 3.0, 9.0, 15.0, 16.0));

    private static final VoxelShape CEILING_SHAPE = Shapes.or(BELL_SHAPE, Block.box(7.0, 13.0, 7.0, 9.0, 16.0, 9.0));

    public static final int EVENT_BELL_RING = 1;

    public BellBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(ATTACHMENT, BellAttachType.FLOOR)).m_61124_(POWERED, false));
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        boolean $$6 = level1.m_276867_(blockPos2);
        if ($$6 != (Boolean) blockState0.m_61143_(POWERED)) {
            if ($$6) {
                this.attemptToRing(level1, blockPos2, null);
            }
            level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(POWERED, $$6), 3);
        }
    }

    @Override
    public void onProjectileHit(Level level0, BlockState blockState1, BlockHitResult blockHitResult2, Projectile projectile3) {
        Entity $$4 = projectile3.getOwner();
        Player $$5 = $$4 instanceof Player ? (Player) $$4 : null;
        this.onHit(level0, blockState1, blockHitResult2, $$5, true);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        return this.onHit(level1, blockState0, blockHitResult5, player3, true) ? InteractionResult.sidedSuccess(level1.isClientSide) : InteractionResult.PASS;
    }

    public boolean onHit(Level level0, BlockState blockState1, BlockHitResult blockHitResult2, @Nullable Player player3, boolean boolean4) {
        Direction $$5 = blockHitResult2.getDirection();
        BlockPos $$6 = blockHitResult2.getBlockPos();
        boolean $$7 = !boolean4 || this.isProperHit(blockState1, $$5, blockHitResult2.m_82450_().y - (double) $$6.m_123342_());
        if ($$7) {
            boolean $$8 = this.attemptToRing(player3, level0, $$6, $$5);
            if ($$8 && player3 != null) {
                player3.awardStat(Stats.BELL_RING);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean isProperHit(BlockState blockState0, Direction direction1, double double2) {
        if (direction1.getAxis() != Direction.Axis.Y && !(double2 > 0.8124F)) {
            Direction $$3 = (Direction) blockState0.m_61143_(FACING);
            BellAttachType $$4 = (BellAttachType) blockState0.m_61143_(ATTACHMENT);
            switch($$4) {
                case FLOOR:
                    return $$3.getAxis() == direction1.getAxis();
                case SINGLE_WALL:
                case DOUBLE_WALL:
                    return $$3.getAxis() != direction1.getAxis();
                case CEILING:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean attemptToRing(Level level0, BlockPos blockPos1, @Nullable Direction direction2) {
        return this.attemptToRing(null, level0, blockPos1, direction2);
    }

    public boolean attemptToRing(@Nullable Entity entity0, Level level1, BlockPos blockPos2, @Nullable Direction direction3) {
        BlockEntity $$4 = level1.getBlockEntity(blockPos2);
        if (!level1.isClientSide && $$4 instanceof BellBlockEntity) {
            if (direction3 == null) {
                direction3 = (Direction) level1.getBlockState(blockPos2).m_61143_(FACING);
            }
            ((BellBlockEntity) $$4).onHit(direction3);
            level1.playSound(null, blockPos2, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
            level1.m_142346_(entity0, GameEvent.BLOCK_CHANGE, blockPos2);
            return true;
        } else {
            return false;
        }
    }

    private VoxelShape getVoxelShape(BlockState blockState0) {
        Direction $$1 = (Direction) blockState0.m_61143_(FACING);
        BellAttachType $$2 = (BellAttachType) blockState0.m_61143_(ATTACHMENT);
        if ($$2 == BellAttachType.FLOOR) {
            return $$1 != Direction.NORTH && $$1 != Direction.SOUTH ? EAST_WEST_FLOOR_SHAPE : NORTH_SOUTH_FLOOR_SHAPE;
        } else if ($$2 == BellAttachType.CEILING) {
            return CEILING_SHAPE;
        } else if ($$2 == BellAttachType.DOUBLE_WALL) {
            return $$1 != Direction.NORTH && $$1 != Direction.SOUTH ? EAST_WEST_BETWEEN : NORTH_SOUTH_BETWEEN;
        } else if ($$1 == Direction.NORTH) {
            return TO_NORTH;
        } else if ($$1 == Direction.SOUTH) {
            return TO_SOUTH;
        } else {
            return $$1 == Direction.EAST ? TO_EAST : TO_WEST;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.getVoxelShape(blockState0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.getVoxelShape(blockState0);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        Direction $$1 = blockPlaceContext0.m_43719_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        Level $$3 = blockPlaceContext0.m_43725_();
        Direction.Axis $$4 = $$1.getAxis();
        if ($$4 == Direction.Axis.Y) {
            BlockState $$5 = (BlockState) ((BlockState) this.m_49966_().m_61124_(ATTACHMENT, $$1 == Direction.DOWN ? BellAttachType.CEILING : BellAttachType.FLOOR)).m_61124_(FACING, blockPlaceContext0.m_8125_());
            if ($$5.m_60710_(blockPlaceContext0.m_43725_(), $$2)) {
                return $$5;
            }
        } else {
            boolean $$6 = $$4 == Direction.Axis.X && $$3.getBlockState($$2.west()).m_60783_($$3, $$2.west(), Direction.EAST) && $$3.getBlockState($$2.east()).m_60783_($$3, $$2.east(), Direction.WEST) || $$4 == Direction.Axis.Z && $$3.getBlockState($$2.north()).m_60783_($$3, $$2.north(), Direction.SOUTH) && $$3.getBlockState($$2.south()).m_60783_($$3, $$2.south(), Direction.NORTH);
            BlockState $$7 = (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, $$1.getOpposite())).m_61124_(ATTACHMENT, $$6 ? BellAttachType.DOUBLE_WALL : BellAttachType.SINGLE_WALL);
            if ($$7.m_60710_(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos())) {
                return $$7;
            }
            boolean $$8 = $$3.getBlockState($$2.below()).m_60783_($$3, $$2.below(), Direction.UP);
            $$7 = (BlockState) $$7.m_61124_(ATTACHMENT, $$8 ? BellAttachType.FLOOR : BellAttachType.CEILING);
            if ($$7.m_60710_(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos())) {
                return $$7;
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        BellAttachType $$6 = (BellAttachType) blockState0.m_61143_(ATTACHMENT);
        Direction $$7 = getConnectedDirection(blockState0).getOpposite();
        if ($$7 == direction1 && !blockState0.m_60710_(levelAccessor3, blockPos4) && $$6 != BellAttachType.DOUBLE_WALL) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if (direction1.getAxis() == ((Direction) blockState0.m_61143_(FACING)).getAxis()) {
                if ($$6 == BellAttachType.DOUBLE_WALL && !blockState2.m_60783_(levelAccessor3, blockPos5, direction1)) {
                    return (BlockState) ((BlockState) blockState0.m_61124_(ATTACHMENT, BellAttachType.SINGLE_WALL)).m_61124_(FACING, direction1.getOpposite());
                }
                if ($$6 == BellAttachType.SINGLE_WALL && $$7.getOpposite() == direction1 && blockState2.m_60783_(levelAccessor3, blockPos5, (Direction) blockState0.m_61143_(FACING))) {
                    return (BlockState) blockState0.m_61124_(ATTACHMENT, BellAttachType.DOUBLE_WALL);
                }
            }
            return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        Direction $$3 = getConnectedDirection(blockState0).getOpposite();
        return $$3 == Direction.UP ? Block.canSupportCenter(levelReader1, blockPos2.above(), Direction.DOWN) : FaceAttachedHorizontalDirectionalBlock.canAttach(levelReader1, blockPos2, $$3);
    }

    private static Direction getConnectedDirection(BlockState blockState0) {
        switch((BellAttachType) blockState0.m_61143_(ATTACHMENT)) {
            case FLOOR:
                return Direction.UP;
            case CEILING:
                return Direction.DOWN;
            default:
                return ((Direction) blockState0.m_61143_(FACING)).getOpposite();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, ATTACHMENT, POWERED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new BellBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, BlockEntityType.BELL, level0.isClientSide ? BellBlockEntity::m_155175_ : BellBlockEntity::m_155202_);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}