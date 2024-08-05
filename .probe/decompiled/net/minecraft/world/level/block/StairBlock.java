package net.minecraft.world.level.block;

import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StairBlock extends Block implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape TOP_AABB = SlabBlock.TOP_AABB;

    protected static final VoxelShape BOTTOM_AABB = SlabBlock.BOTTOM_AABB;

    protected static final VoxelShape OCTET_NNN = Block.box(0.0, 0.0, 0.0, 8.0, 8.0, 8.0);

    protected static final VoxelShape OCTET_NNP = Block.box(0.0, 0.0, 8.0, 8.0, 8.0, 16.0);

    protected static final VoxelShape OCTET_NPN = Block.box(0.0, 8.0, 0.0, 8.0, 16.0, 8.0);

    protected static final VoxelShape OCTET_NPP = Block.box(0.0, 8.0, 8.0, 8.0, 16.0, 16.0);

    protected static final VoxelShape OCTET_PNN = Block.box(8.0, 0.0, 0.0, 16.0, 8.0, 8.0);

    protected static final VoxelShape OCTET_PNP = Block.box(8.0, 0.0, 8.0, 16.0, 8.0, 16.0);

    protected static final VoxelShape OCTET_PPN = Block.box(8.0, 8.0, 0.0, 16.0, 16.0, 8.0);

    protected static final VoxelShape OCTET_PPP = Block.box(8.0, 8.0, 8.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape[] TOP_SHAPES = makeShapes(TOP_AABB, OCTET_NNN, OCTET_PNN, OCTET_NNP, OCTET_PNP);

    protected static final VoxelShape[] BOTTOM_SHAPES = makeShapes(BOTTOM_AABB, OCTET_NPN, OCTET_PPN, OCTET_NPP, OCTET_PPP);

    private static final int[] SHAPE_BY_STATE = new int[] { 12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8 };

    private final Block base;

    private final BlockState baseState;

    private static VoxelShape[] makeShapes(VoxelShape voxelShape0, VoxelShape voxelShape1, VoxelShape voxelShape2, VoxelShape voxelShape3, VoxelShape voxelShape4) {
        return (VoxelShape[]) IntStream.range(0, 16).mapToObj(p_56945_ -> makeStairShape(p_56945_, voxelShape0, voxelShape1, voxelShape2, voxelShape3, voxelShape4)).toArray(VoxelShape[]::new);
    }

    private static VoxelShape makeStairShape(int int0, VoxelShape voxelShape1, VoxelShape voxelShape2, VoxelShape voxelShape3, VoxelShape voxelShape4, VoxelShape voxelShape5) {
        VoxelShape $$6 = voxelShape1;
        if ((int0 & 1) != 0) {
            $$6 = Shapes.or(voxelShape1, voxelShape2);
        }
        if ((int0 & 2) != 0) {
            $$6 = Shapes.or($$6, voxelShape3);
        }
        if ((int0 & 4) != 0) {
            $$6 = Shapes.or($$6, voxelShape4);
        }
        if ((int0 & 8) != 0) {
            $$6 = Shapes.or($$6, voxelShape5);
        }
        return $$6;
    }

    protected StairBlock(BlockState blockState0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(HALF, Half.BOTTOM)).m_61124_(SHAPE, StairsShape.STRAIGHT)).m_61124_(WATERLOGGED, false));
        this.base = blockState0.m_60734_();
        this.baseState = blockState0;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (blockState0.m_61143_(HALF) == Half.TOP ? TOP_SHAPES : BOTTOM_SHAPES)[SHAPE_BY_STATE[this.getShapeIndex(blockState0)]];
    }

    private int getShapeIndex(BlockState blockState0) {
        return ((StairsShape) blockState0.m_61143_(SHAPE)).ordinal() * 4 + ((Direction) blockState0.m_61143_(FACING)).get2DDataValue();
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        this.base.animateTick(blockState0, level1, blockPos2, randomSource3);
    }

    @Override
    public void attack(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3) {
        this.baseState.m_60686_(level1, blockPos2, player3);
    }

    @Override
    public void destroy(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2) {
        this.base.destroy(levelAccessor0, blockPos1, blockState2);
    }

    @Override
    public float getExplosionResistance() {
        return this.base.getExplosionResistance();
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState0.m_60734_())) {
            level1.neighborChanged(this.baseState, blockPos2, Blocks.AIR, blockPos2, false);
            this.base.m_6807_(this.baseState, level1, blockPos2, blockState3, false);
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            this.baseState.m_60753_(level1, blockPos2, blockState3, boolean4);
        }
    }

    @Override
    public void stepOn(Level level0, BlockPos blockPos1, BlockState blockState2, Entity entity3) {
        this.base.stepOn(level0, blockPos1, blockState2, entity3);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return this.base.isRandomlyTicking(blockState0);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        this.base.m_213898_(blockState0, serverLevel1, blockPos2, randomSource3);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        this.base.m_213897_(blockState0, serverLevel1, blockPos2, randomSource3);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        return this.baseState.m_60664_(level1, player3, interactionHand4, blockHitResult5);
    }

    @Override
    public void wasExploded(Level level0, BlockPos blockPos1, Explosion explosion2) {
        this.base.wasExploded(level0, blockPos1, explosion2);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        Direction $$1 = blockPlaceContext0.m_43719_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        FluidState $$3 = blockPlaceContext0.m_43725_().getFluidState($$2);
        BlockState $$4 = (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.m_8125_())).m_61124_(HALF, $$1 != Direction.DOWN && ($$1 == Direction.UP || !(blockPlaceContext0.m_43720_().y - (double) $$2.m_123342_() > 0.5)) ? Half.BOTTOM : Half.TOP)).m_61124_(WATERLOGGED, $$3.getType() == Fluids.WATER);
        return (BlockState) $$4.m_61124_(SHAPE, getStairsShape($$4, blockPlaceContext0.m_43725_(), $$2));
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return direction1.getAxis().isHorizontal() ? (BlockState) blockState0.m_61124_(SHAPE, getStairsShape(blockState0, levelAccessor3, blockPos4)) : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    private static StairsShape getStairsShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        Direction $$3 = (Direction) blockState0.m_61143_(FACING);
        BlockState $$4 = blockGetter1.getBlockState(blockPos2.relative($$3));
        if (isStairs($$4) && blockState0.m_61143_(HALF) == $$4.m_61143_(HALF)) {
            Direction $$5 = (Direction) $$4.m_61143_(FACING);
            if ($$5.getAxis() != ((Direction) blockState0.m_61143_(FACING)).getAxis() && canTakeShape(blockState0, blockGetter1, blockPos2, $$5.getOpposite())) {
                if ($$5 == $$3.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }
                return StairsShape.OUTER_RIGHT;
            }
        }
        BlockState $$6 = blockGetter1.getBlockState(blockPos2.relative($$3.getOpposite()));
        if (isStairs($$6) && blockState0.m_61143_(HALF) == $$6.m_61143_(HALF)) {
            Direction $$7 = (Direction) $$6.m_61143_(FACING);
            if ($$7.getAxis() != ((Direction) blockState0.m_61143_(FACING)).getAxis() && canTakeShape(blockState0, blockGetter1, blockPos2, $$7)) {
                if ($$7 == $$3.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }
                return StairsShape.INNER_RIGHT;
            }
        }
        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        BlockState $$4 = blockGetter1.getBlockState(blockPos2.relative(direction3));
        return !isStairs($$4) || $$4.m_61143_(FACING) != blockState0.m_61143_(FACING) || $$4.m_61143_(HALF) != blockState0.m_61143_(HALF);
    }

    public static boolean isStairs(BlockState blockState0) {
        return blockState0.m_60734_() instanceof StairBlock;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        Direction $$2 = (Direction) blockState0.m_61143_(FACING);
        StairsShape $$3 = (StairsShape) blockState0.m_61143_(SHAPE);
        switch(mirror1) {
            case LEFT_RIGHT:
                if ($$2.getAxis() == Direction.Axis.Z) {
                    switch($$3) {
                        case INNER_LEFT:
                            return (BlockState) blockState0.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT:
                            return (BlockState) blockState0.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT:
                            return (BlockState) blockState0.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return (BlockState) blockState0.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.OUTER_LEFT);
                        default:
                            return blockState0.m_60717_(Rotation.CLOCKWISE_180);
                    }
                }
                break;
            case FRONT_BACK:
                if ($$2.getAxis() == Direction.Axis.X) {
                    switch($$3) {
                        case INNER_LEFT:
                            return (BlockState) blockState0.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT:
                            return (BlockState) blockState0.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT:
                            return (BlockState) blockState0.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return (BlockState) blockState0.m_60717_(Rotation.CLOCKWISE_180).m_61124_(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT:
                            return blockState0.m_60717_(Rotation.CLOCKWISE_180);
                    }
                }
        }
        return super.m_6943_(blockState0, mirror1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, HALF, SHAPE, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}