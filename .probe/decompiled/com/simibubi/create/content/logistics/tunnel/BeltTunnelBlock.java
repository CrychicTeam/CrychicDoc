package com.simibubi.create.content.logistics.tunnel;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.logistics.funnel.BeltFunnelBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BeltTunnelBlock extends Block implements IBE<BeltTunnelBlockEntity>, IWrenchable {

    public static final Property<BeltTunnelBlock.Shape> SHAPE = EnumProperty.create("shape", BeltTunnelBlock.Shape.class);

    public static final Property<Direction.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public BeltTunnelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(SHAPE, BeltTunnelBlock.Shape.STRAIGHT));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BeltTunnelShapes.getShape(state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockState blockState = worldIn.m_8055_(pos.below());
        return !this.isValidPositionForPlacement(state, worldIn, pos) ? false : (Boolean) blockState.m_61143_(BeltBlock.CASING);
    }

    public boolean isValidPositionForPlacement(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockState blockState = worldIn.m_8055_(pos.below());
        return !AllBlocks.BELT.has(blockState) ? false : blockState.m_61143_(BeltBlock.SLOPE) == BeltSlope.HORIZONTAL;
    }

    public static boolean hasWindow(BlockState state) {
        return state.m_61143_(SHAPE) == BeltTunnelBlock.Shape.WINDOW || state.m_61143_(SHAPE) == BeltTunnelBlock.Shape.CLOSED;
    }

    public static boolean isStraight(BlockState state) {
        return hasWindow(state) || state.m_61143_(SHAPE) == BeltTunnelBlock.Shape.STRAIGHT;
    }

    public static boolean isJunction(BlockState state) {
        BeltTunnelBlock.Shape shape = (BeltTunnelBlock.Shape) state.m_61143_(SHAPE);
        return shape == BeltTunnelBlock.Shape.CROSS || shape == BeltTunnelBlock.Shape.T_LEFT || shape == BeltTunnelBlock.Shape.T_RIGHT;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getTunnelState(context.m_43725_(), context.getClickedPos());
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
        if (!(world instanceof WrappedWorld) && !world.isClientSide()) {
            this.withBlockEntityDo(world, pos, BeltTunnelBlockEntity::updateTunnelConnections);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing.getAxis().isVertical()) {
            return state;
        } else {
            if (!(worldIn instanceof WrappedWorld) && !worldIn.m_5776_()) {
                this.withBlockEntityDo(worldIn, currentPos, BeltTunnelBlockEntity::updateTunnelConnections);
            }
            BlockState tunnelState = this.getTunnelState(worldIn, currentPos);
            return tunnelState.m_61143_(HORIZONTAL_AXIS) == state.m_61143_(HORIZONTAL_AXIS) && hasWindow(tunnelState) == hasWindow(state) ? state : tunnelState;
        }
    }

    public void updateTunnel(LevelAccessor world, BlockPos pos) {
        BlockState tunnel = world.m_8055_(pos);
        BlockState newTunnel = this.getTunnelState(world, pos);
        if (tunnel != newTunnel && !world.m_5776_()) {
            world.m_7731_(pos, newTunnel, 3);
            BlockEntity be = world.m_7702_(pos);
            if (be != null && be instanceof BeltTunnelBlockEntity) {
                ((BeltTunnelBlockEntity) be).updateTunnelConnections();
            }
        }
    }

    private BlockState getTunnelState(BlockGetter reader, BlockPos pos) {
        BlockState state = this.m_49966_();
        BlockState belt = reader.getBlockState(pos.below());
        if (AllBlocks.BELT.has(belt)) {
            state = (BlockState) state.m_61124_(HORIZONTAL_AXIS, ((Direction) belt.m_61143_(BeltBlock.HORIZONTAL_FACING)).getAxis());
        }
        Direction.Axis axis = (Direction.Axis) state.m_61143_(HORIZONTAL_AXIS);
        Direction left = Direction.get(Direction.AxisDirection.POSITIVE, axis).getClockWise();
        boolean onLeft = this.hasValidOutput(reader, pos.below(), left);
        boolean onRight = this.hasValidOutput(reader, pos.below(), left.getOpposite());
        if (onLeft && onRight) {
            state = (BlockState) state.m_61124_(SHAPE, BeltTunnelBlock.Shape.CROSS);
        } else if (onLeft) {
            state = (BlockState) state.m_61124_(SHAPE, BeltTunnelBlock.Shape.T_LEFT);
        } else if (onRight) {
            state = (BlockState) state.m_61124_(SHAPE, BeltTunnelBlock.Shape.T_RIGHT);
        }
        if (state.m_61143_(SHAPE) == BeltTunnelBlock.Shape.STRAIGHT) {
            boolean canHaveWindow = this.canHaveWindow(reader, pos, axis);
            if (canHaveWindow) {
                state = (BlockState) state.m_61124_(SHAPE, BeltTunnelBlock.Shape.WINDOW);
            }
        }
        return state;
    }

    protected boolean canHaveWindow(BlockGetter reader, BlockPos pos, Direction.Axis axis) {
        Direction fw = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        BlockState blockState1 = reader.getBlockState(pos.relative(fw));
        BlockState blockState2 = reader.getBlockState(pos.relative(fw.getOpposite()));
        boolean funnel1 = blockState1.m_60734_() instanceof BeltFunnelBlock && blockState1.m_61143_(BeltFunnelBlock.SHAPE) == BeltFunnelBlock.Shape.EXTENDED && blockState1.m_61143_(BeltFunnelBlock.HORIZONTAL_FACING) == fw.getOpposite();
        boolean funnel2 = blockState2.m_60734_() instanceof BeltFunnelBlock && blockState2.m_61143_(BeltFunnelBlock.SHAPE) == BeltFunnelBlock.Shape.EXTENDED && blockState2.m_61143_(BeltFunnelBlock.HORIZONTAL_FACING) == fw;
        boolean valid1 = blockState1.m_60734_() instanceof BeltTunnelBlock || funnel1;
        boolean valid2 = blockState2.m_60734_() instanceof BeltTunnelBlock || funnel2;
        return valid1 && valid2 && (!funnel1 || !funnel2);
    }

    private boolean hasValidOutput(BlockGetter world, BlockPos pos, Direction side) {
        BlockState blockState = world.getBlockState(pos.relative(side));
        if (AllBlocks.BELT.has(blockState)) {
            return ((Direction) blockState.m_61143_(BeltBlock.HORIZONTAL_FACING)).getAxis() == side.getAxis();
        } else {
            DirectBeltInputBehaviour behaviour = BlockEntityBehaviour.get(world, pos.relative(side), DirectBeltInputBehaviour.TYPE);
            return behaviour != null && behaviour.canInsertFromSide(side);
        }
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (!hasWindow(state)) {
            return InteractionResult.PASS;
        } else {
            BeltTunnelBlock.Shape shape = (BeltTunnelBlock.Shape) state.m_61143_(SHAPE);
            shape = shape == BeltTunnelBlock.Shape.CLOSED ? BeltTunnelBlock.Shape.WINDOW : BeltTunnelBlock.Shape.CLOSED;
            Level world = context.getLevel();
            if (!world.isClientSide) {
                world.setBlock(context.getClickedPos(), (BlockState) state.m_61124_(SHAPE, shape), 2);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        Direction fromAxis = Direction.get(Direction.AxisDirection.POSITIVE, (Direction.Axis) state.m_61143_(HORIZONTAL_AXIS));
        Direction rotated = rotation.rotate(fromAxis);
        return (BlockState) state.m_61124_(HORIZONTAL_AXIS, rotated.getAxis());
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            if (fromPos.equals(pos.below()) && !this.canSurvive(state, worldIn, pos)) {
                worldIn.m_46961_(pos, true);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_AXIS, SHAPE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Class<BeltTunnelBlockEntity> getBlockEntityClass() {
        return BeltTunnelBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BeltTunnelBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends BeltTunnelBlockEntity>) AllBlockEntityTypes.ANDESITE_TUNNEL.get();
    }

    public static enum Shape implements StringRepresentable {

        STRAIGHT,
        WINDOW,
        CLOSED,
        T_LEFT,
        T_RIGHT,
        CROSS;

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}