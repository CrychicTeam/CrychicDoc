package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RedStoneWireBlock extends Block {

    public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;

    public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;

    public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;

    public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));

    protected static final int H = 1;

    protected static final int W = 3;

    protected static final int E = 13;

    protected static final int N = 3;

    protected static final int S = 13;

    private static final VoxelShape SHAPE_DOT = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);

    private static final Map<Direction, VoxelShape> SHAPES_FLOOR = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(3.0, 0.0, 0.0, 13.0, 1.0, 13.0), Direction.SOUTH, Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 16.0), Direction.EAST, Block.box(3.0, 0.0, 3.0, 16.0, 1.0, 13.0), Direction.WEST, Block.box(0.0, 0.0, 3.0, 13.0, 1.0, 13.0)));

    private static final Map<Direction, VoxelShape> SHAPES_UP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Shapes.or((VoxelShape) SHAPES_FLOOR.get(Direction.NORTH), Block.box(3.0, 0.0, 0.0, 13.0, 16.0, 1.0)), Direction.SOUTH, Shapes.or((VoxelShape) SHAPES_FLOOR.get(Direction.SOUTH), Block.box(3.0, 0.0, 15.0, 13.0, 16.0, 16.0)), Direction.EAST, Shapes.or((VoxelShape) SHAPES_FLOOR.get(Direction.EAST), Block.box(15.0, 0.0, 3.0, 16.0, 16.0, 13.0)), Direction.WEST, Shapes.or((VoxelShape) SHAPES_FLOOR.get(Direction.WEST), Block.box(0.0, 0.0, 3.0, 1.0, 16.0, 13.0))));

    private static final Map<BlockState, VoxelShape> SHAPES_CACHE = Maps.newHashMap();

    private static final Vec3[] COLORS = Util.make(new Vec3[16], p_154319_ -> {
        for (int $$1 = 0; $$1 <= 15; $$1++) {
            float $$2 = (float) $$1 / 15.0F;
            float $$3 = $$2 * 0.6F + ($$2 > 0.0F ? 0.4F : 0.3F);
            float $$4 = Mth.clamp($$2 * $$2 * 0.7F - 0.5F, 0.0F, 1.0F);
            float $$5 = Mth.clamp($$2 * $$2 * 0.6F - 0.7F, 0.0F, 1.0F);
            p_154319_[$$1] = new Vec3((double) $$3, (double) $$4, (double) $$5);
        }
    });

    private static final float PARTICLE_DENSITY = 0.2F;

    private final BlockState crossState;

    private boolean shouldSignal = true;

    public RedStoneWireBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(NORTH, RedstoneSide.NONE)).m_61124_(EAST, RedstoneSide.NONE)).m_61124_(SOUTH, RedstoneSide.NONE)).m_61124_(WEST, RedstoneSide.NONE)).m_61124_(POWER, 0));
        this.crossState = (BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(NORTH, RedstoneSide.SIDE)).m_61124_(EAST, RedstoneSide.SIDE)).m_61124_(SOUTH, RedstoneSide.SIDE)).m_61124_(WEST, RedstoneSide.SIDE);
        UnmodifiableIterator var2 = this.m_49965_().getPossibleStates().iterator();
        while (var2.hasNext()) {
            BlockState $$1 = (BlockState) var2.next();
            if ((Integer) $$1.m_61143_(POWER) == 0) {
                SHAPES_CACHE.put($$1, this.calculateShape($$1));
            }
        }
    }

    private VoxelShape calculateShape(BlockState blockState0) {
        VoxelShape $$1 = SHAPE_DOT;
        for (Direction $$2 : Direction.Plane.HORIZONTAL) {
            RedstoneSide $$3 = (RedstoneSide) blockState0.m_61143_((Property) PROPERTY_BY_DIRECTION.get($$2));
            if ($$3 == RedstoneSide.SIDE) {
                $$1 = Shapes.or($$1, (VoxelShape) SHAPES_FLOOR.get($$2));
            } else if ($$3 == RedstoneSide.UP) {
                $$1 = Shapes.or($$1, (VoxelShape) SHAPES_UP.get($$2));
            }
        }
        return $$1;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (VoxelShape) SHAPES_CACHE.get(blockState0.m_61124_(POWER, 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return this.getConnectionState(blockPlaceContext0.m_43725_(), this.crossState, blockPlaceContext0.getClickedPos());
    }

    private BlockState getConnectionState(BlockGetter blockGetter0, BlockState blockState1, BlockPos blockPos2) {
        boolean $$3 = isDot(blockState1);
        blockState1 = this.getMissingConnections(blockGetter0, (BlockState) this.m_49966_().m_61124_(POWER, (Integer) blockState1.m_61143_(POWER)), blockPos2);
        if ($$3 && isDot(blockState1)) {
            return blockState1;
        } else {
            boolean $$4 = ((RedstoneSide) blockState1.m_61143_(NORTH)).isConnected();
            boolean $$5 = ((RedstoneSide) blockState1.m_61143_(SOUTH)).isConnected();
            boolean $$6 = ((RedstoneSide) blockState1.m_61143_(EAST)).isConnected();
            boolean $$7 = ((RedstoneSide) blockState1.m_61143_(WEST)).isConnected();
            boolean $$8 = !$$4 && !$$5;
            boolean $$9 = !$$6 && !$$7;
            if (!$$7 && $$8) {
                blockState1 = (BlockState) blockState1.m_61124_(WEST, RedstoneSide.SIDE);
            }
            if (!$$6 && $$8) {
                blockState1 = (BlockState) blockState1.m_61124_(EAST, RedstoneSide.SIDE);
            }
            if (!$$4 && $$9) {
                blockState1 = (BlockState) blockState1.m_61124_(NORTH, RedstoneSide.SIDE);
            }
            if (!$$5 && $$9) {
                blockState1 = (BlockState) blockState1.m_61124_(SOUTH, RedstoneSide.SIDE);
            }
            return blockState1;
        }
    }

    private BlockState getMissingConnections(BlockGetter blockGetter0, BlockState blockState1, BlockPos blockPos2) {
        boolean $$3 = !blockGetter0.getBlockState(blockPos2.above()).m_60796_(blockGetter0, blockPos2);
        for (Direction $$4 : Direction.Plane.HORIZONTAL) {
            if (!((RedstoneSide) blockState1.m_61143_((Property) PROPERTY_BY_DIRECTION.get($$4))).isConnected()) {
                RedstoneSide $$5 = this.getConnectingSide(blockGetter0, blockPos2, $$4, $$3);
                blockState1 = (BlockState) blockState1.m_61124_((Property) PROPERTY_BY_DIRECTION.get($$4), $$5);
            }
        }
        return blockState1;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (direction1 == Direction.DOWN) {
            return blockState0;
        } else if (direction1 == Direction.UP) {
            return this.getConnectionState(levelAccessor3, blockState0, blockPos4);
        } else {
            RedstoneSide $$6 = this.getConnectingSide(levelAccessor3, blockPos4, direction1);
            return $$6.isConnected() == ((RedstoneSide) blockState0.m_61143_((Property) PROPERTY_BY_DIRECTION.get(direction1))).isConnected() && !isCross(blockState0) ? (BlockState) blockState0.m_61124_((Property) PROPERTY_BY_DIRECTION.get(direction1), $$6) : this.getConnectionState(levelAccessor3, (BlockState) ((BlockState) this.crossState.m_61124_(POWER, (Integer) blockState0.m_61143_(POWER))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(direction1), $$6), blockPos4);
        }
    }

    private static boolean isCross(BlockState blockState0) {
        return ((RedstoneSide) blockState0.m_61143_(NORTH)).isConnected() && ((RedstoneSide) blockState0.m_61143_(SOUTH)).isConnected() && ((RedstoneSide) blockState0.m_61143_(EAST)).isConnected() && ((RedstoneSide) blockState0.m_61143_(WEST)).isConnected();
    }

    private static boolean isDot(BlockState blockState0) {
        return !((RedstoneSide) blockState0.m_61143_(NORTH)).isConnected() && !((RedstoneSide) blockState0.m_61143_(SOUTH)).isConnected() && !((RedstoneSide) blockState0.m_61143_(EAST)).isConnected() && !((RedstoneSide) blockState0.m_61143_(WEST)).isConnected();
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2, int int3, int int4) {
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();
        for (Direction $$6 : Direction.Plane.HORIZONTAL) {
            RedstoneSide $$7 = (RedstoneSide) blockState0.m_61143_((Property) PROPERTY_BY_DIRECTION.get($$6));
            if ($$7 != RedstoneSide.NONE && !levelAccessor1.m_8055_($$5.setWithOffset(blockPos2, $$6)).m_60713_(this)) {
                $$5.move(Direction.DOWN);
                BlockState $$8 = levelAccessor1.m_8055_($$5);
                if ($$8.m_60713_(this)) {
                    BlockPos $$9 = $$5.m_121945_($$6.getOpposite());
                    levelAccessor1.neighborShapeChanged($$6.getOpposite(), levelAccessor1.m_8055_($$9), $$5, $$9, int3, int4);
                }
                $$5.setWithOffset(blockPos2, $$6).move(Direction.UP);
                BlockState $$10 = levelAccessor1.m_8055_($$5);
                if ($$10.m_60713_(this)) {
                    BlockPos $$11 = $$5.m_121945_($$6.getOpposite());
                    levelAccessor1.neighborShapeChanged($$6.getOpposite(), levelAccessor1.m_8055_($$11), $$5, $$11, int3, int4);
                }
            }
        }
    }

    private RedstoneSide getConnectingSide(BlockGetter blockGetter0, BlockPos blockPos1, Direction direction2) {
        return this.getConnectingSide(blockGetter0, blockPos1, direction2, !blockGetter0.getBlockState(blockPos1.above()).m_60796_(blockGetter0, blockPos1));
    }

    private RedstoneSide getConnectingSide(BlockGetter blockGetter0, BlockPos blockPos1, Direction direction2, boolean boolean3) {
        BlockPos $$4 = blockPos1.relative(direction2);
        BlockState $$5 = blockGetter0.getBlockState($$4);
        if (boolean3) {
            boolean $$6 = $$5.m_60734_() instanceof TrapDoorBlock || this.canSurviveOn(blockGetter0, $$4, $$5);
            if ($$6 && shouldConnectTo(blockGetter0.getBlockState($$4.above()))) {
                if ($$5.m_60783_(blockGetter0, $$4, direction2.getOpposite())) {
                    return RedstoneSide.UP;
                }
                return RedstoneSide.SIDE;
            }
        }
        return !shouldConnectTo($$5, direction2) && ($$5.m_60796_(blockGetter0, $$4) || !shouldConnectTo(blockGetter0.getBlockState($$4.below()))) ? RedstoneSide.NONE : RedstoneSide.SIDE;
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.below();
        BlockState $$4 = levelReader1.m_8055_($$3);
        return this.canSurviveOn(levelReader1, $$3, $$4);
    }

    private boolean canSurviveOn(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return blockState2.m_60783_(blockGetter0, blockPos1, Direction.UP) || blockState2.m_60713_(Blocks.HOPPER);
    }

    private void updatePowerStrength(Level level0, BlockPos blockPos1, BlockState blockState2) {
        int $$3 = this.calculateTargetStrength(level0, blockPos1);
        if ((Integer) blockState2.m_61143_(POWER) != $$3) {
            if (level0.getBlockState(blockPos1) == blockState2) {
                level0.setBlock(blockPos1, (BlockState) blockState2.m_61124_(POWER, $$3), 2);
            }
            Set<BlockPos> $$4 = Sets.newHashSet();
            $$4.add(blockPos1);
            for (Direction $$5 : Direction.values()) {
                $$4.add(blockPos1.relative($$5));
            }
            for (BlockPos $$6 : $$4) {
                level0.updateNeighborsAt($$6, this);
            }
        }
    }

    private int calculateTargetStrength(Level level0, BlockPos blockPos1) {
        this.shouldSignal = false;
        int $$2 = level0.m_277086_(blockPos1);
        this.shouldSignal = true;
        int $$3 = 0;
        if ($$2 < 15) {
            for (Direction $$4 : Direction.Plane.HORIZONTAL) {
                BlockPos $$5 = blockPos1.relative($$4);
                BlockState $$6 = level0.getBlockState($$5);
                $$3 = Math.max($$3, this.getWireSignal($$6));
                BlockPos $$7 = blockPos1.above();
                if ($$6.m_60796_(level0, $$5) && !level0.getBlockState($$7).m_60796_(level0, $$7)) {
                    $$3 = Math.max($$3, this.getWireSignal(level0.getBlockState($$5.above())));
                } else if (!$$6.m_60796_(level0, $$5)) {
                    $$3 = Math.max($$3, this.getWireSignal(level0.getBlockState($$5.below())));
                }
            }
        }
        return Math.max($$2, $$3 - 1);
    }

    private int getWireSignal(BlockState blockState0) {
        return blockState0.m_60713_(this) ? (Integer) blockState0.m_61143_(POWER) : 0;
    }

    private void checkCornerChangeAt(Level level0, BlockPos blockPos1) {
        if (level0.getBlockState(blockPos1).m_60713_(this)) {
            level0.updateNeighborsAt(blockPos1, this);
            for (Direction $$2 : Direction.values()) {
                level0.updateNeighborsAt(blockPos1.relative($$2), this);
            }
        }
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState3.m_60713_(blockState0.m_60734_()) && !level1.isClientSide) {
            this.updatePowerStrength(level1, blockPos2, blockState0);
            for (Direction $$5 : Direction.Plane.VERTICAL) {
                level1.updateNeighborsAt(blockPos2.relative($$5), this);
            }
            this.updateNeighborsOfNeighboringWires(level1, blockPos2);
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!boolean4 && !blockState0.m_60713_(blockState3.m_60734_())) {
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
            if (!level1.isClientSide) {
                for (Direction $$5 : Direction.values()) {
                    level1.updateNeighborsAt(blockPos2.relative($$5), this);
                }
                this.updatePowerStrength(level1, blockPos2, blockState0);
                this.updateNeighborsOfNeighboringWires(level1, blockPos2);
            }
        }
    }

    private void updateNeighborsOfNeighboringWires(Level level0, BlockPos blockPos1) {
        for (Direction $$2 : Direction.Plane.HORIZONTAL) {
            this.checkCornerChangeAt(level0, blockPos1.relative($$2));
        }
        for (Direction $$3 : Direction.Plane.HORIZONTAL) {
            BlockPos $$4 = blockPos1.relative($$3);
            if (level0.getBlockState($$4).m_60796_(level0, $$4)) {
                this.checkCornerChangeAt(level0, $$4.above());
            } else {
                this.checkCornerChangeAt(level0, $$4.below());
            }
        }
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (!level1.isClientSide) {
            if (blockState0.m_60710_(level1, blockPos2)) {
                this.updatePowerStrength(level1, blockPos2, blockState0);
            } else {
                m_49950_(blockState0, level1, blockPos2);
                level1.removeBlock(blockPos2, false);
            }
        }
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return !this.shouldSignal ? 0 : blockState0.m_60746_(blockGetter1, blockPos2, direction3);
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        if (this.shouldSignal && direction3 != Direction.DOWN) {
            int $$4 = (Integer) blockState0.m_61143_(POWER);
            if ($$4 == 0) {
                return 0;
            } else {
                return direction3 != Direction.UP && !((RedstoneSide) this.getConnectionState(blockGetter1, blockState0, blockPos2).m_61143_((Property) PROPERTY_BY_DIRECTION.get(direction3.getOpposite()))).isConnected() ? 0 : $$4;
            }
        } else {
            return 0;
        }
    }

    protected static boolean shouldConnectTo(BlockState blockState0) {
        return shouldConnectTo(blockState0, null);
    }

    protected static boolean shouldConnectTo(BlockState blockState0, @Nullable Direction direction1) {
        if (blockState0.m_60713_(Blocks.REDSTONE_WIRE)) {
            return true;
        } else if (blockState0.m_60713_(Blocks.REPEATER)) {
            Direction $$2 = (Direction) blockState0.m_61143_(RepeaterBlock.f_54117_);
            return $$2 == direction1 || $$2.getOpposite() == direction1;
        } else {
            return blockState0.m_60713_(Blocks.OBSERVER) ? direction1 == blockState0.m_61143_(ObserverBlock.f_52588_) : blockState0.m_60803_() && direction1 != null;
        }
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return this.shouldSignal;
    }

    public static int getColorForPower(int int0) {
        Vec3 $$1 = COLORS[int0];
        return Mth.color((float) $$1.x(), (float) $$1.y(), (float) $$1.z());
    }

    private void spawnParticlesAlongLine(Level level0, RandomSource randomSource1, BlockPos blockPos2, Vec3 vec3, Direction direction4, Direction direction5, float float6, float float7) {
        float $$8 = float7 - float6;
        if (!(randomSource1.nextFloat() >= 0.2F * $$8)) {
            float $$9 = 0.4375F;
            float $$10 = float6 + $$8 * randomSource1.nextFloat();
            double $$11 = 0.5 + (double) (0.4375F * (float) direction4.getStepX()) + (double) ($$10 * (float) direction5.getStepX());
            double $$12 = 0.5 + (double) (0.4375F * (float) direction4.getStepY()) + (double) ($$10 * (float) direction5.getStepY());
            double $$13 = 0.5 + (double) (0.4375F * (float) direction4.getStepZ()) + (double) ($$10 * (float) direction5.getStepZ());
            level0.addParticle(new DustParticleOptions(vec3.toVector3f(), 1.0F), (double) blockPos2.m_123341_() + $$11, (double) blockPos2.m_123342_() + $$12, (double) blockPos2.m_123343_() + $$13, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        int $$4 = (Integer) blockState0.m_61143_(POWER);
        if ($$4 != 0) {
            for (Direction $$5 : Direction.Plane.HORIZONTAL) {
                RedstoneSide $$6 = (RedstoneSide) blockState0.m_61143_((Property) PROPERTY_BY_DIRECTION.get($$5));
                switch($$6) {
                    case UP:
                        this.spawnParticlesAlongLine(level1, randomSource3, blockPos2, COLORS[$$4], $$5, Direction.UP, -0.5F, 0.5F);
                    case SIDE:
                        this.spawnParticlesAlongLine(level1, randomSource3, blockPos2, COLORS[$$4], Direction.DOWN, $$5, 0.0F, 0.5F);
                        break;
                    case NONE:
                    default:
                        this.spawnParticlesAlongLine(level1, randomSource3, blockPos2, COLORS[$$4], Direction.DOWN, $$5, 0.0F, 0.3F);
                }
            }
        }
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        switch(rotation1) {
            case CLOCKWISE_180:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH, (RedstoneSide) blockState0.m_61143_(SOUTH))).m_61124_(EAST, (RedstoneSide) blockState0.m_61143_(WEST))).m_61124_(SOUTH, (RedstoneSide) blockState0.m_61143_(NORTH))).m_61124_(WEST, (RedstoneSide) blockState0.m_61143_(EAST));
            case COUNTERCLOCKWISE_90:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH, (RedstoneSide) blockState0.m_61143_(EAST))).m_61124_(EAST, (RedstoneSide) blockState0.m_61143_(SOUTH))).m_61124_(SOUTH, (RedstoneSide) blockState0.m_61143_(WEST))).m_61124_(WEST, (RedstoneSide) blockState0.m_61143_(NORTH));
            case CLOCKWISE_90:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH, (RedstoneSide) blockState0.m_61143_(WEST))).m_61124_(EAST, (RedstoneSide) blockState0.m_61143_(NORTH))).m_61124_(SOUTH, (RedstoneSide) blockState0.m_61143_(EAST))).m_61124_(WEST, (RedstoneSide) blockState0.m_61143_(SOUTH));
            default:
                return blockState0;
        }
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        switch(mirror1) {
            case LEFT_RIGHT:
                return (BlockState) ((BlockState) blockState0.m_61124_(NORTH, (RedstoneSide) blockState0.m_61143_(SOUTH))).m_61124_(SOUTH, (RedstoneSide) blockState0.m_61143_(NORTH));
            case FRONT_BACK:
                return (BlockState) ((BlockState) blockState0.m_61124_(EAST, (RedstoneSide) blockState0.m_61143_(WEST))).m_61124_(WEST, (RedstoneSide) blockState0.m_61143_(EAST));
            default:
                return super.m_6943_(blockState0, mirror1);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(NORTH, EAST, SOUTH, WEST, POWER);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (!player3.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        } else {
            if (isCross(blockState0) || isDot(blockState0)) {
                BlockState $$6 = isCross(blockState0) ? this.m_49966_() : this.crossState;
                $$6 = (BlockState) $$6.m_61124_(POWER, (Integer) blockState0.m_61143_(POWER));
                $$6 = this.getConnectionState(level1, $$6, blockPos2);
                if ($$6 != blockState0) {
                    level1.setBlock(blockPos2, $$6, 3);
                    this.updatesOnShapeChange(level1, blockPos2, blockState0, $$6);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        }
    }

    private void updatesOnShapeChange(Level level0, BlockPos blockPos1, BlockState blockState2, BlockState blockState3) {
        for (Direction $$4 : Direction.Plane.HORIZONTAL) {
            BlockPos $$5 = blockPos1.relative($$4);
            if (((RedstoneSide) blockState2.m_61143_((Property) PROPERTY_BY_DIRECTION.get($$4))).isConnected() != ((RedstoneSide) blockState3.m_61143_((Property) PROPERTY_BY_DIRECTION.get($$4))).isConnected() && level0.getBlockState($$5).m_60796_(level0, $$5)) {
                level0.updateNeighborsAtExceptFromFacing($$5, blockState3.m_60734_(), $$4.getOpposite());
            }
        }
    }
}