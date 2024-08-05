package com.simibubi.create.content.redstone.rail;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ControllerRailBlock extends BaseRailBlock implements IWrenchable {

    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public static final BooleanProperty BACKWARDS = BooleanProperty.create("backwards");

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public ControllerRailBlock(BlockBehaviour.Properties properties) {
        super(true, properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(POWER, 0)).m_61124_(BACKWARDS, false)).m_61124_(SHAPE, RailShape.NORTH_SOUTH)).m_61124_(f_152149_, false));
    }

    public static Vec3i getAccelerationVector(BlockState state) {
        Direction pointingTo = getPointingTowards(state);
        return (isStateBackwards(state) ? pointingTo.getOpposite() : pointingTo).getNormal();
    }

    private static Direction getPointingTowards(BlockState state) {
        switch((RailShape) state.m_61143_(SHAPE)) {
            case ASCENDING_WEST:
            case EAST_WEST:
                return Direction.WEST;
            case ASCENDING_EAST:
                return Direction.EAST;
            case ASCENDING_SOUTH:
                return Direction.SOUTH;
            default:
                return Direction.NORTH;
        }
    }

    @Override
    protected BlockState updateDir(Level world, BlockPos pos, BlockState state, boolean p_208489_4_) {
        BlockState updatedState = super.updateDir(world, pos, state, p_208489_4_);
        if (updatedState.m_61143_(SHAPE) == state.m_61143_(SHAPE)) {
            return updatedState;
        } else {
            BlockState reversedUpdatedState = updatedState;
            if (getPointingTowards(state).getAxis() != getPointingTowards(updatedState).getAxis()) {
                for (boolean opposite : Iterate.trueAndFalse) {
                    Direction offset = getPointingTowards(updatedState);
                    if (opposite) {
                        offset = offset.getOpposite();
                    }
                    for (BlockPos adjPos : Iterate.hereBelowAndAbove(pos.relative(offset))) {
                        BlockState adjState = world.getBlockState(adjPos);
                        if (AllBlocks.CONTROLLER_RAIL.has(adjState) && getPointingTowards(adjState).getAxis() == offset.getAxis() && adjState.m_61143_(BACKWARDS) != reversedUpdatedState.m_61143_(BACKWARDS)) {
                            reversedUpdatedState = (BlockState) reversedUpdatedState.m_61122_(BACKWARDS);
                        }
                    }
                }
            }
            if (reversedUpdatedState != updatedState) {
                world.setBlockAndUpdate(pos, reversedUpdatedState);
            }
            return reversedUpdatedState;
        }
    }

    private static void decelerateCart(BlockPos pos, AbstractMinecart cart) {
        Vec3 diff = VecHelper.getCenterOf(pos).subtract(cart.m_20182_());
        cart.m_20334_(diff.x / 16.0, 0.0, diff.z / 16.0);
        if (cart instanceof MinecartFurnace fme) {
            fme.xPush = fme.zPush = 0.0;
        }
    }

    private static boolean isStableWith(BlockState testState, BlockGetter world, BlockPos pos) {
        return m_49936_(world, pos.below()) && (!((RailShape) testState.m_61143_(SHAPE)).isAscending() || m_49936_(world, pos.relative(getPointingTowards(testState))));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        Direction direction = p_196258_1_.m_8125_();
        BlockState base = super.getStateForPlacement(p_196258_1_);
        return (BlockState) (base == null ? this.m_49966_() : base).m_61124_(BACKWARDS, direction.getAxisDirection() == Direction.AxisDirection.POSITIVE);
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(SHAPE, POWER, BACKWARDS, f_152149_);
    }

    public void onMinecartPass(BlockState state, Level world, BlockPos pos, AbstractMinecart cart) {
        if (!world.isClientSide) {
            Vec3 accelerationVec = Vec3.atLowerCornerOf(getAccelerationVector(state));
            double targetSpeed = cart.getMaxSpeedWithRail() * (double) ((Integer) state.m_61143_(POWER)).intValue() / 15.0;
            if (cart instanceof MinecartFurnace fme) {
                fme.xPush = accelerationVec.x;
                fme.zPush = accelerationVec.z;
            }
            Vec3 motion = cart.m_20184_();
            if ((motion.dot(accelerationVec) >= 0.0 || motion.lengthSqr() < 1.0E-4) && targetSpeed > 0.0) {
                cart.m_20256_(accelerationVec.scale(targetSpeed));
            } else {
                decelerateCart(pos, cart);
            }
        }
    }

    @Override
    protected void updateState(BlockState state, Level world, BlockPos pos, Block block) {
        int newPower = this.calculatePower(world, pos);
        if ((Integer) state.m_61143_(POWER) != newPower) {
            this.placeAndNotify((BlockState) state.m_61124_(POWER, newPower), pos, world);
        }
    }

    private int calculatePower(Level world, BlockPos pos) {
        int newPower = world.m_277086_(pos);
        if (newPower != 0) {
            return newPower;
        } else {
            int forwardDistance = 0;
            int backwardsDistance = 0;
            BlockPos lastForwardRail = pos;
            BlockPos lastBackwardsRail = pos;
            int forwardPower = 0;
            int backwardsPower = 0;
            for (int i = 0; i < 15; i++) {
                BlockPos testPos = this.findNextRail(lastForwardRail, world, false);
                if (testPos == null) {
                    break;
                }
                forwardDistance++;
                lastForwardRail = testPos;
                forwardPower = world.m_277086_(testPos);
                if (forwardPower != 0) {
                    break;
                }
            }
            for (int i = 0; i < 15; i++) {
                BlockPos testPosx = this.findNextRail(lastBackwardsRail, world, true);
                if (testPosx == null) {
                    break;
                }
                backwardsDistance++;
                lastBackwardsRail = testPosx;
                backwardsPower = world.m_277086_(testPosx);
                if (backwardsPower != 0) {
                    break;
                }
            }
            if (forwardDistance > 8 && backwardsDistance > 8) {
                return 0;
            } else if (backwardsPower == 0 && forwardDistance <= 8) {
                return forwardPower;
            } else if (forwardPower == 0 && backwardsDistance <= 8) {
                return backwardsPower;
            } else {
                return backwardsPower != 0 && forwardPower != 0 ? Mth.ceil((double) (backwardsPower * forwardDistance + forwardPower * backwardsDistance) / (double) (forwardDistance + backwardsDistance)) : 0;
            }
        }
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockPos pos = context.getClickedPos();
            for (Rotation testRotation : new Rotation[] { Rotation.CLOCKWISE_90, Rotation.CLOCKWISE_180, Rotation.COUNTERCLOCKWISE_90 }) {
                BlockState testState = this.rotate(state, testRotation);
                if (isStableWith(testState, world, pos)) {
                    this.placeAndNotify(testState, pos, world);
                    return InteractionResult.SUCCESS;
                }
            }
            BlockState testState = (BlockState) state.m_61124_(BACKWARDS, !(Boolean) state.m_61143_(BACKWARDS));
            if (isStableWith(testState, world, pos)) {
                this.placeAndNotify(testState, pos, world);
            }
            return InteractionResult.SUCCESS;
        }
    }

    private void placeAndNotify(BlockState state, BlockPos pos, Level world) {
        world.setBlock(pos, state, 3);
        world.updateNeighborsAt(pos.below(), this);
        if (((RailShape) state.m_61143_(SHAPE)).isAscending()) {
            world.updateNeighborsAt(pos.above(), this);
        }
    }

    @Nullable
    private BlockPos findNextRail(BlockPos from, BlockGetter world, boolean reversed) {
        BlockState current = world.getBlockState(from);
        if (!(current.m_60734_() instanceof ControllerRailBlock)) {
            return null;
        } else {
            Vec3i accelerationVec = getAccelerationVector(current);
            BlockPos baseTestPos = reversed ? from.subtract(accelerationVec) : from.offset(accelerationVec);
            for (BlockPos testPos : Iterate.hereBelowAndAbove(baseTestPos)) {
                if (testPos.m_123342_() <= from.m_123342_() || ((RailShape) current.m_61143_(SHAPE)).isAscending()) {
                    BlockState testState = world.getBlockState(testPos);
                    if (testState.m_60734_() instanceof ControllerRailBlock && getAccelerationVector(testState).equals(accelerationVec)) {
                        return testPos;
                    }
                }
            }
            return null;
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return (Integer) state.m_61143_(POWER);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (rotation == Rotation.NONE) {
            return state;
        } else {
            RailShape railshape = (RailShape) ((BlockState) Blocks.POWERED_RAIL.defaultBlockState().m_61124_(SHAPE, (RailShape) state.m_61143_(SHAPE))).m_60717_(rotation).m_61143_(SHAPE);
            state = (BlockState) state.m_61124_(SHAPE, railshape);
            return rotation != Rotation.CLOCKWISE_180 && getPointingTowards(state).getAxis() == Direction.Axis.Z != (rotation == Rotation.COUNTERCLOCKWISE_90) ? state : (BlockState) state.m_61122_(BACKWARDS);
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if (mirror == Mirror.NONE) {
            return state;
        } else {
            RailShape railshape = (RailShape) ((BlockState) Blocks.POWERED_RAIL.defaultBlockState().m_61124_(SHAPE, (RailShape) state.m_61143_(SHAPE))).m_60715_(mirror).m_61143_(SHAPE);
            state = (BlockState) state.m_61124_(SHAPE, railshape);
            return getPointingTowards(state).getAxis() == Direction.Axis.Z == (mirror == Mirror.LEFT_RIGHT) ? (BlockState) state.m_61122_(BACKWARDS) : state;
        }
    }

    public static boolean isStateBackwards(BlockState state) {
        return (Boolean) state.m_61143_(BACKWARDS) ^ isReversedSlope(state);
    }

    public static boolean isReversedSlope(BlockState state) {
        return state.m_61143_(SHAPE) == RailShape.ASCENDING_SOUTH || state.m_61143_(SHAPE) == RailShape.ASCENDING_EAST;
    }
}