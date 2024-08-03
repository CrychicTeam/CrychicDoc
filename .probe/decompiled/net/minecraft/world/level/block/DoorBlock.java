package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DoorBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    protected static final float AABB_DOOR_THICKNESS = 3.0F;

    protected static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);

    protected static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape WEST_AABB = Block.box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape EAST_AABB = Block.box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);

    private final BlockSetType type;

    protected DoorBlock(BlockBehaviour.Properties blockBehaviourProperties0, BlockSetType blockSetType1) {
        super(blockBehaviourProperties0.sound(blockSetType1.soundType()));
        this.type = blockSetType1;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(OPEN, false)).m_61124_(HINGE, DoorHingeSide.LEFT)).m_61124_(POWERED, false)).m_61124_(HALF, DoubleBlockHalf.LOWER));
    }

    public BlockSetType type() {
        return this.type;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        Direction $$4 = (Direction) blockState0.m_61143_(FACING);
        boolean $$5 = !(Boolean) blockState0.m_61143_(OPEN);
        boolean $$6 = blockState0.m_61143_(HINGE) == DoorHingeSide.RIGHT;
        switch($$4) {
            case EAST:
            default:
                return $$5 ? EAST_AABB : ($$6 ? NORTH_AABB : SOUTH_AABB);
            case SOUTH:
                return $$5 ? SOUTH_AABB : ($$6 ? EAST_AABB : WEST_AABB);
            case WEST:
                return $$5 ? WEST_AABB : ($$6 ? SOUTH_AABB : NORTH_AABB);
            case NORTH:
                return $$5 ? NORTH_AABB : ($$6 ? WEST_AABB : EAST_AABB);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        DoubleBlockHalf $$6 = (DoubleBlockHalf) blockState0.m_61143_(HALF);
        if (direction1.getAxis() != Direction.Axis.Y || $$6 == DoubleBlockHalf.LOWER != (direction1 == Direction.UP)) {
            return $$6 == DoubleBlockHalf.LOWER && direction1 == Direction.DOWN && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        } else {
            return blockState2.m_60713_(this) && blockState2.m_61143_(HALF) != $$6 ? (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(FACING, (Direction) blockState2.m_61143_(FACING))).m_61124_(OPEN, (Boolean) blockState2.m_61143_(OPEN))).m_61124_(HINGE, (DoorHingeSide) blockState2.m_61143_(HINGE))).m_61124_(POWERED, (Boolean) blockState2.m_61143_(POWERED)) : Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public void playerWillDestroy(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        if (!level0.isClientSide && player3.isCreative()) {
            DoublePlantBlock.preventCreativeDropFromBottomPart(level0, blockPos1, blockState2, player3);
        }
        super.playerWillDestroy(level0, blockPos1, blockState2, player3);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        switch(pathComputationType3) {
            case LAND:
                return (Boolean) blockState0.m_61143_(OPEN);
            case WATER:
                return false;
            case AIR:
                return (Boolean) blockState0.m_61143_(OPEN);
            default:
                return false;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockPos $$1 = blockPlaceContext0.getClickedPos();
        Level $$2 = blockPlaceContext0.m_43725_();
        if ($$1.m_123342_() < $$2.m_151558_() - 1 && $$2.getBlockState($$1.above()).m_60629_(blockPlaceContext0)) {
            boolean $$3 = $$2.m_276867_($$1) || $$2.m_276867_($$1.above());
            return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.m_8125_())).m_61124_(HINGE, this.getHinge(blockPlaceContext0))).m_61124_(POWERED, $$3)).m_61124_(OPEN, $$3)).m_61124_(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        level0.setBlock(blockPos1.above(), (BlockState) blockState2.m_61124_(HALF, DoubleBlockHalf.UPPER), 3);
    }

    private DoorHingeSide getHinge(BlockPlaceContext blockPlaceContext0) {
        BlockGetter $$1 = blockPlaceContext0.m_43725_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        Direction $$3 = blockPlaceContext0.m_8125_();
        BlockPos $$4 = $$2.above();
        Direction $$5 = $$3.getCounterClockWise();
        BlockPos $$6 = $$2.relative($$5);
        BlockState $$7 = $$1.getBlockState($$6);
        BlockPos $$8 = $$4.relative($$5);
        BlockState $$9 = $$1.getBlockState($$8);
        Direction $$10 = $$3.getClockWise();
        BlockPos $$11 = $$2.relative($$10);
        BlockState $$12 = $$1.getBlockState($$11);
        BlockPos $$13 = $$4.relative($$10);
        BlockState $$14 = $$1.getBlockState($$13);
        int $$15 = ($$7.m_60838_($$1, $$6) ? -1 : 0) + ($$9.m_60838_($$1, $$8) ? -1 : 0) + ($$12.m_60838_($$1, $$11) ? 1 : 0) + ($$14.m_60838_($$1, $$13) ? 1 : 0);
        boolean $$16 = $$7.m_60713_(this) && $$7.m_61143_(HALF) == DoubleBlockHalf.LOWER;
        boolean $$17 = $$12.m_60713_(this) && $$12.m_61143_(HALF) == DoubleBlockHalf.LOWER;
        if ((!$$16 || $$17) && $$15 <= 0) {
            if ((!$$17 || $$16) && $$15 >= 0) {
                int $$18 = $$3.getStepX();
                int $$19 = $$3.getStepZ();
                Vec3 $$20 = blockPlaceContext0.m_43720_();
                double $$21 = $$20.x - (double) $$2.m_123341_();
                double $$22 = $$20.z - (double) $$2.m_123343_();
                return ($$18 >= 0 || !($$22 < 0.5)) && ($$18 <= 0 || !($$22 > 0.5)) && ($$19 >= 0 || !($$21 > 0.5)) && ($$19 <= 0 || !($$21 < 0.5)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            } else {
                return DoorHingeSide.LEFT;
            }
        } else {
            return DoorHingeSide.RIGHT;
        }
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (!this.type.canOpenByHand()) {
            return InteractionResult.PASS;
        } else {
            blockState0 = (BlockState) blockState0.m_61122_(OPEN);
            level1.setBlock(blockPos2, blockState0, 10);
            this.playSound(player3, level1, blockPos2, (Boolean) blockState0.m_61143_(OPEN));
            level1.m_142346_(player3, this.isOpen(blockState0) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos2);
            return InteractionResult.sidedSuccess(level1.isClientSide);
        }
    }

    public boolean isOpen(BlockState blockState0) {
        return (Boolean) blockState0.m_61143_(OPEN);
    }

    public void setOpen(@Nullable Entity entity0, Level level1, BlockState blockState2, BlockPos blockPos3, boolean boolean4) {
        if (blockState2.m_60713_(this) && (Boolean) blockState2.m_61143_(OPEN) != boolean4) {
            level1.setBlock(blockPos3, (BlockState) blockState2.m_61124_(OPEN, boolean4), 10);
            this.playSound(entity0, level1, blockPos3, boolean4);
            level1.m_142346_(entity0, boolean4 ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos3);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        boolean $$6 = level1.m_276867_(blockPos2) || level1.m_276867_(blockPos2.relative(blockState0.m_61143_(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
        if (!this.m_49966_().m_60713_(block3) && $$6 != (Boolean) blockState0.m_61143_(POWERED)) {
            if ($$6 != (Boolean) blockState0.m_61143_(OPEN)) {
                this.playSound(null, level1, blockPos2, $$6);
                level1.m_142346_(null, $$6 ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos2);
            }
            level1.setBlock(blockPos2, (BlockState) ((BlockState) blockState0.m_61124_(POWERED, $$6)).m_61124_(OPEN, $$6), 2);
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.below();
        BlockState $$4 = levelReader1.m_8055_($$3);
        return blockState0.m_61143_(HALF) == DoubleBlockHalf.LOWER ? $$4.m_60783_(levelReader1, $$3, Direction.UP) : $$4.m_60713_(this);
    }

    private void playSound(@Nullable Entity entity0, Level level1, BlockPos blockPos2, boolean boolean3) {
        level1.playSound(entity0, blockPos2, boolean3 ? this.type.doorOpen() : this.type.doorClose(), SoundSource.BLOCKS, 1.0F, level1.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return mirror1 == Mirror.NONE ? blockState0 : (BlockState) blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING))).m_61122_(HINGE);
    }

    @Override
    public long getSeed(BlockState blockState0, BlockPos blockPos1) {
        return Mth.getSeed(blockPos1.m_123341_(), blockPos1.below(blockState0.m_61143_(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).m_123342_(), blockPos1.m_123343_());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(HALF, FACING, OPEN, HINGE, POWERED);
    }

    public static boolean isWoodenDoor(Level level0, BlockPos blockPos1) {
        return isWoodenDoor(level0.getBlockState(blockPos1));
    }

    public static boolean isWoodenDoor(BlockState blockState0) {
        if (blockState0.m_60734_() instanceof DoorBlock $$1 && $$1.type().canOpenByHand()) {
            return true;
        }
        return false;
    }
}