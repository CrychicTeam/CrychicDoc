package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TubeWormBlock extends Block implements SimpleWaterloggedBlock {

    private static final VoxelShape STRAIGHT_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    private static final VoxelShape SHAPE_TURN_NORTH = Block.box(4.0, 0.0, 0.0, 12.0, 8.0, 12.0);

    private static final VoxelShape SHAPE_TURN_EAST = Block.box(4.0, 0.0, 4.0, 16.0, 8.0, 12.0);

    private static final VoxelShape SHAPE_TURN_SOUTH = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 16.0);

    private static final VoxelShape SHAPE_TURN_WEST = Block.box(0.0, 0.0, 4.0, 12.0, 8.0, 12.0);

    private static final VoxelShape SHAPE_ELBOW_NORTH = ACMath.buildShape(SHAPE_TURN_NORTH, STRAIGHT_SHAPE);

    private static final VoxelShape SHAPE_ELBOW_EAST = ACMath.buildShape(SHAPE_TURN_EAST, STRAIGHT_SHAPE);

    private static final VoxelShape SHAPE_ELBOW_SOUTH = ACMath.buildShape(SHAPE_TURN_SOUTH, STRAIGHT_SHAPE);

    private static final VoxelShape SHAPE_ELBOW_WEST = ACMath.buildShape(SHAPE_TURN_WEST, STRAIGHT_SHAPE);

    public static final EnumProperty<TubeWormBlock.TubeShape> TUBE_TYPE = EnumProperty.create("type", TubeWormBlock.TubeShape.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public TubeWormBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).requiresCorrectToolForDrops().strength(2.0F).sound(ACSoundTypes.TUBE_WORM));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false)).m_61124_(TUBE_TYPE, TubeWormBlock.TubeShape.STRAIGHT));
    }

    public static boolean canSupportWormAt(Level level, BlockState state, BlockPos blockPos) {
        if (state.m_60713_(ACBlockRegistry.TUBE_WORM.get()) && state.m_61143_(TUBE_TYPE) != TubeWormBlock.TubeShape.TURN) {
            BlockState aboveState = level.getBlockState(blockPos.above());
            return aboveState.m_60819_().is(FluidTags.WATER) && !aboveState.m_60659_(level, blockPos.above(), Direction.DOWN, SupportType.CENTER);
        } else {
            return false;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.m_61143_(TUBE_TYPE) == TubeWormBlock.TubeShape.STRAIGHT) {
            return STRAIGHT_SHAPE;
        } else {
            if (state.m_61143_(TUBE_TYPE) == TubeWormBlock.TubeShape.TURN) {
                switch((Direction) state.m_61143_(FACING)) {
                    case NORTH:
                        return SHAPE_TURN_NORTH;
                    case EAST:
                        return SHAPE_TURN_EAST;
                    case SOUTH:
                        return SHAPE_TURN_SOUTH;
                    case WEST:
                        return SHAPE_TURN_WEST;
                }
            } else {
                switch((Direction) state.m_61143_(FACING)) {
                    case NORTH:
                        return SHAPE_ELBOW_NORTH;
                    case EAST:
                        return SHAPE_ELBOW_EAST;
                    case SOUTH:
                        return SHAPE_ELBOW_SOUTH;
                    case WEST:
                        return SHAPE_ELBOW_WEST;
                }
            }
            return STRAIGHT_SHAPE;
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        if (!state.m_60710_(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            BlockState prior = state;
            BlockState above = levelAccessor.m_8055_(blockPos.above());
            if (state1.m_60713_(this) && direction != null && state.m_61143_(TUBE_TYPE) == TubeWormBlock.TubeShape.STRAIGHT && !above.m_60713_(this)) {
                if (direction.getAxis().isHorizontal()) {
                    BlockState below = levelAccessor.m_8055_(blockPos.below());
                    if (below.m_60659_(levelAccessor, blockPos.below(), Direction.UP, SupportType.CENTER)) {
                        prior = (BlockState) ((BlockState) state.m_61124_(TUBE_TYPE, TubeWormBlock.TubeShape.TURN)).m_61124_(FACING, direction);
                    } else {
                        prior = (BlockState) ((BlockState) state.m_61124_(TUBE_TYPE, TubeWormBlock.TubeShape.ELBOW)).m_61124_(FACING, direction);
                    }
                } else if (direction == Direction.DOWN) {
                    prior = (BlockState) state.m_61124_(TUBE_TYPE, TubeWormBlock.TubeShape.STRAIGHT);
                }
            }
            return prior;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        Direction direction = context.m_43719_();
        BlockPos blockpos = context.getClickedPos().relative(direction.getOpposite());
        BlockState neighbor = levelaccessor.m_8055_(blockpos);
        BlockState aboveNeighbor = levelaccessor.m_8055_(blockpos.above());
        if (neighbor.m_60713_(this)) {
            TubeWormBlock.TubeShape tubeShape = (TubeWormBlock.TubeShape) neighbor.m_61143_(TUBE_TYPE);
            if (tubeShape == TubeWormBlock.TubeShape.STRAIGHT && aboveNeighbor.m_60713_(this)) {
                return null;
            }
            if (tubeShape == TubeWormBlock.TubeShape.ELBOW && direction != Direction.UP) {
                return null;
            }
            if (tubeShape == TubeWormBlock.TubeShape.TURN && direction != neighbor.m_61143_(FACING)) {
                return null;
            }
        }
        BlockState tube = (BlockState) this.m_49966_().m_61124_(WATERLOGGED, levelaccessor.m_6425_(context.getClickedPos()).getType() == Fluids.WATER);
        if (direction.getAxis().isHorizontal()) {
            tube = (BlockState) ((BlockState) tube.m_61124_(TUBE_TYPE, TubeWormBlock.TubeShape.ELBOW)).m_61124_(FACING, direction.getOpposite());
        }
        return tube;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelAccessor, BlockPos blockPos) {
        BlockState belowState = levelAccessor.m_8055_(blockPos.below());
        if (belowState.m_60659_(levelAccessor, blockPos.below(), Direction.UP, SupportType.CENTER)) {
            return true;
        } else if (state.m_61143_(TUBE_TYPE) != TubeWormBlock.TubeShape.ELBOW) {
            return false;
        } else {
            BlockPos offset = blockPos.relative((Direction) state.m_61143_(FACING));
            BlockState offsetState = levelAccessor.m_8055_(offset);
            return offsetState.m_60659_(levelAccessor, offset, ((Direction) state.m_61143_(FACING)).getOpposite(), SupportType.CENTER) || offsetState.m_60713_(this);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TUBE_TYPE, WATERLOGGED);
    }

    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (randomSource.nextInt(3) == 0 && canSupportWormAt(level, state, pos) && AlexsCaves.PROXY.checkIfParticleAt(ACParticleRegistry.TUBE_WORM.get(), pos)) {
            Vec3 center = Vec3.upFromBottomCenterOf(pos, 0.5);
            level.addParticle(ACParticleRegistry.TUBE_WORM.get(), center.x, center.y, center.z, 0.0, 0.0, 0.0);
        }
    }

    public static enum TubeShape implements StringRepresentable {

        STRAIGHT("straight"), TURN("turn"), ELBOW("elbow");

        private final String name;

        private TubeShape(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}