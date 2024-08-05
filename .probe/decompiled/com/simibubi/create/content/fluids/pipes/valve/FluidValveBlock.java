package com.simibubi.create.content.fluids.pipes.valve;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.IAxisPipe;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Iterate;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public class FluidValveBlock extends DirectionalAxisKineticBlock implements IAxisPipe, IBE<FluidValveBlockEntity>, ProperWaterloggedBlock {

    public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");

    public FluidValveBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(ENABLED, false)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return AllShapes.FLUID_VALVE.get(getPipeAxis(state));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ENABLED, WATERLOGGED));
    }

    @Override
    protected boolean prefersConnectionTo(LevelReader reader, BlockPos pos, Direction facing, boolean shaftAxis) {
        if (!shaftAxis) {
            BlockPos offset = pos.relative(facing);
            BlockState blockState = reader.m_8055_(offset);
            return FluidPipeBlock.canConnectTo(reader, offset, blockState, facing);
        } else {
            return super.prefersConnectionTo(reader, pos, facing, shaftAxis);
        }
    }

    @Nonnull
    public static Direction.Axis getPipeAxis(BlockState state) {
        if (!(state.m_60734_() instanceof FluidValveBlock)) {
            throw new IllegalStateException("Provided BlockState is for a different block.");
        } else {
            Direction facing = (Direction) state.m_61143_(FACING);
            boolean alongFirst = !(Boolean) state.m_61143_(AXIS_ALONG_FIRST_COORDINATE);
            for (Direction.Axis axis : Iterate.axes) {
                if (axis != facing.getAxis()) {
                    if (alongFirst) {
                        return axis;
                    }
                    alongFirst = true;
                }
            }
            throw new IllegalStateException("Impossible axis.");
        }
    }

    @Override
    public Direction.Axis getAxis(BlockState state) {
        return getPipeAxis(state);
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
    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        return true;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.m_6807_(state, world, pos, oldState, isMoving);
        if (!world.isClientSide) {
            if (state != oldState) {
                world.m_186464_(pos, this, 1, TickPriority.HIGH);
            }
        }
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

    public static boolean isOpenAt(BlockState state, Direction d) {
        return d.getAxis() == getPipeAxis(state);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource r) {
        FluidPropagator.propagateChangedPipe(world, pos, state);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<FluidValveBlockEntity> getBlockEntityClass() {
        return FluidValveBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FluidValveBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends FluidValveBlockEntity>) AllBlockEntityTypes.FLUID_VALVE.get();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.withWater(super.getStateForPlacement(context), context);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        this.updateWater(world, state, pos);
        return state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return this.fluidState(state);
    }
}