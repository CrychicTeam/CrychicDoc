package com.simibubi.create.content.fluids.pump;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
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
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public class PumpBlock extends DirectionalKineticBlock implements SimpleWaterloggedBlock, ICogWheel, IBE<PumpBlockEntity> {

    public PumpBlock(BlockBehaviour.Properties p_i48415_1_) {
        super(p_i48415_1_);
        this.m_49959_((BlockState) super.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return (BlockState) originalState.m_61124_(FACING, ((Direction) originalState.m_61143_(FACING)).getOpposite());
    }

    @Override
    public BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
        return super.updateAfterWrenched(newState, context);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(FACING)).getAxis();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return AllShapes.PUMP.get((Direction) state.m_61143_(FACING));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block otherBlock, BlockPos neighborPos, boolean isMoving) {
        DebugPackets.sendNeighborsUpdatePacket(world, pos);
        Direction d = FluidPropagator.validateNeighbourChange(state, world, pos, otherBlock, neighborPos, isMoving);
        if (d != null) {
            if (isOpenAt(state, d)) {
                world.m_186464_(pos, this, 1, TickPriority.HIGH);
            }
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
        BlockState toPlace = super.getStateForPlacement(context);
        Level level = context.m_43725_();
        BlockPos pos = context.getClickedPos();
        Player player = context.m_43723_();
        toPlace = ProperWaterloggedBlock.withWater(level, toPlace, pos);
        Direction nearestLookingDirection = context.getNearestLookingDirection();
        Direction targetDirection = context.m_43723_() != null && context.m_43723_().m_6144_() ? nearestLookingDirection : nearestLookingDirection.getOpposite();
        Direction bestConnectedDirection = null;
        double bestDistance = Double.MAX_VALUE;
        for (Direction d : Iterate.directions) {
            BlockPos adjPos = pos.relative(d);
            BlockState adjState = level.getBlockState(adjPos);
            if (FluidPipeBlock.canConnectTo(level, adjPos, adjState, d)) {
                double distance = Vec3.atLowerCornerOf(d.getNormal()).distanceTo(Vec3.atLowerCornerOf(targetDirection.getNormal()));
                if (!(distance > bestDistance)) {
                    bestDistance = distance;
                    bestConnectedDirection = d;
                }
            }
        }
        if (bestConnectedDirection == null) {
            return toPlace;
        } else if (bestConnectedDirection.getAxis() == targetDirection.getAxis()) {
            return toPlace;
        } else {
            return player.m_6144_() && bestConnectedDirection.getAxis() != targetDirection.getAxis() ? toPlace : (BlockState) toPlace.m_61124_(FACING, bestConnectedDirection);
        }
    }

    public static boolean isPump(BlockState state) {
        return state.m_60734_() instanceof PumpBlock;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.m_6807_(state, world, pos, oldState, isMoving);
        if (!world.isClientSide) {
            if (state != oldState) {
                world.m_186464_(pos, this, 1, TickPriority.HIGH);
            }
            if (isPump(state) && isPump(oldState) && state.m_61143_(FACING) == ((Direction) oldState.m_61143_(FACING)).getOpposite()) {
                if (!(world.getBlockEntity(pos) instanceof PumpBlockEntity pump)) {
                    return;
                }
                pump.pressureUpdate = true;
            }
        }
    }

    public static boolean isOpenAt(BlockState state, Direction d) {
        return d.getAxis() == ((Direction) state.m_61143_(FACING)).getAxis();
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource r) {
        FluidPropagator.propagateChangedPipe(world, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        boolean blockTypeChanged = !state.m_60713_(newState.m_60734_());
        if (blockTypeChanged && !world.isClientSide) {
            FluidPropagator.propagateChangedPipe(world, pos, state);
        }
        super.m_6810_(state, world, pos, newState, isMoving);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<PumpBlockEntity> getBlockEntityClass() {
        return PumpBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PumpBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends PumpBlockEntity>) AllBlockEntityTypes.MECHANICAL_PUMP.get();
    }
}