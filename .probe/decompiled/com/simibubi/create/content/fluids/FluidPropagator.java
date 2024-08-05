package com.simibubi.create.content.fluids;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.pipes.AxisPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.VanillaFluidTargets;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidPropagator {

    public static CreateAdvancement[] getSharedTriggers() {
        return new CreateAdvancement[] { AllAdvancements.WATER_SUPPLY, AllAdvancements.CROSS_STREAMS, AllAdvancements.HONEY_DRAIN };
    }

    public static void propagateChangedPipe(LevelAccessor world, BlockPos pipePos, BlockState pipeState) {
        List<Pair<Integer, BlockPos>> frontier = new ArrayList();
        Set<BlockPos> visited = new HashSet();
        Set<Pair<PumpBlockEntity, Direction>> discoveredPumps = new HashSet();
        frontier.add(Pair.of(0, pipePos));
        while (!frontier.isEmpty()) {
            Pair<Integer, BlockPos> pair = (Pair<Integer, BlockPos>) frontier.remove(0);
            BlockPos currentPos = pair.getSecond();
            if (!visited.contains(currentPos)) {
                visited.add(currentPos);
                BlockState currentState = currentPos.equals(pipePos) ? pipeState : world.m_8055_(currentPos);
                FluidTransportBehaviour pipe = getPipe(world, currentPos);
                if (pipe != null) {
                    pipe.wipePressure();
                    for (Direction direction : getPipeConnections(currentState, pipe)) {
                        BlockPos target = currentPos.relative(direction);
                        if (!(world instanceof Level l) || l.isLoaded(target)) {
                            BlockEntity blockEntity = world.m_7702_(target);
                            BlockState targetState = world.m_8055_(target);
                            if (blockEntity instanceof PumpBlockEntity) {
                                if (AllBlocks.MECHANICAL_PUMP.has(targetState) && ((Direction) targetState.m_61143_(PumpBlock.FACING)).getAxis() == direction.getAxis()) {
                                    discoveredPumps.add(Pair.of((PumpBlockEntity) blockEntity, direction.getOpposite()));
                                }
                            } else if (!visited.contains(target)) {
                                FluidTransportBehaviour targetPipe = getPipe(world, target);
                                if (targetPipe != null) {
                                    Integer distance = pair.getFirst();
                                    if ((distance < getPumpRange() || targetPipe.hasAnyPressure()) && targetPipe.canHaveFlowToward(targetState, direction.getOpposite())) {
                                        frontier.add(Pair.of(distance + 1, target));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        discoveredPumps.forEach(pairx -> ((PumpBlockEntity) pairx.getFirst()).updatePipesOnSide((Direction) pairx.getSecond()));
    }

    public static void resetAffectedFluidNetworks(Level world, BlockPos start, Direction side) {
        List<BlockPos> frontier = new ArrayList();
        Set<BlockPos> visited = new HashSet();
        frontier.add(start);
        while (!frontier.isEmpty()) {
            BlockPos pos = (BlockPos) frontier.remove(0);
            if (!visited.contains(pos)) {
                visited.add(pos);
                FluidTransportBehaviour pipe = getPipe(world, pos);
                if (pipe != null) {
                    for (Direction d : Iterate.directions) {
                        if (!pos.equals(start) || d == side) {
                            BlockPos target = pos.relative(d);
                            if (!visited.contains(target)) {
                                PipeConnection connection = pipe.getConnection(d);
                                if (connection != null && connection.hasFlow()) {
                                    PipeConnection.Flow flow = (PipeConnection.Flow) connection.flow.get();
                                    if (flow.inbound) {
                                        connection.resetNetwork();
                                        frontier.add(target);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Direction validateNeighbourChange(BlockState state, Level world, BlockPos pos, Block otherBlock, BlockPos neighborPos, boolean isMoving) {
        if (world.isClientSide) {
            return null;
        } else {
            otherBlock = world.getBlockState(neighborPos).m_60734_();
            if (otherBlock instanceof FluidPipeBlock) {
                return null;
            } else if (otherBlock instanceof AxisPipeBlock) {
                return null;
            } else if (otherBlock instanceof PumpBlock) {
                return null;
            } else if (otherBlock instanceof LiquidBlock) {
                return null;
            } else if (getStraightPipeAxis(state) == null && !AllBlocks.ENCASED_FLUID_PIPE.has(state)) {
                return null;
            } else {
                for (Direction d : Iterate.directions) {
                    if (pos.relative(d).equals(neighborPos)) {
                        return d;
                    }
                }
                return null;
            }
        }
    }

    public static FluidTransportBehaviour getPipe(BlockGetter reader, BlockPos pos) {
        return BlockEntityBehaviour.get(reader, pos, FluidTransportBehaviour.TYPE);
    }

    public static boolean isOpenEnd(BlockGetter reader, BlockPos pos, Direction side) {
        BlockPos connectedPos = pos.relative(side);
        BlockState connectedState = reader.getBlockState(connectedPos);
        FluidTransportBehaviour pipe = getPipe(reader, connectedPos);
        if (pipe != null && pipe.canHaveFlowToward(connectedState, side.getOpposite())) {
            return false;
        } else if (PumpBlock.isPump(connectedState) && ((Direction) connectedState.m_61143_(PumpBlock.FACING)).getAxis() == side.getAxis()) {
            return false;
        } else if (VanillaFluidTargets.shouldPipesConnectTo(connectedState)) {
            return true;
        } else if (BlockHelper.hasBlockSolidSide(connectedState, reader, connectedPos, side.getOpposite()) && !AllTags.AllBlockTags.FAN_TRANSPARENT.matches(connectedState)) {
            return false;
        } else {
            return hasFluidCapability(reader, connectedPos, side.getOpposite()) ? false : connectedState.m_247087_() && connectedState.m_60800_(reader, connectedPos) != -1.0F || connectedState.m_61138_(BlockStateProperties.WATERLOGGED);
        }
    }

    public static List<Direction> getPipeConnections(BlockState state, FluidTransportBehaviour pipe) {
        List<Direction> list = new ArrayList();
        for (Direction d : Iterate.directions) {
            if (pipe.canHaveFlowToward(state, d)) {
                list.add(d);
            }
        }
        return list;
    }

    public static int getPumpRange() {
        return AllConfigs.server().fluids.mechanicalPumpRange.get();
    }

    public static boolean hasFluidCapability(BlockGetter world, BlockPos pos, Direction side) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null) {
            return false;
        } else {
            LazyOptional<IFluidHandler> capability = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, side);
            return capability.isPresent();
        }
    }

    @Nullable
    public static Direction.Axis getStraightPipeAxis(BlockState state) {
        if (state.m_60734_() instanceof PumpBlock) {
            return ((Direction) state.m_61143_(PumpBlock.FACING)).getAxis();
        } else if (state.m_60734_() instanceof AxisPipeBlock) {
            return (Direction.Axis) state.m_61143_(AxisPipeBlock.f_55923_);
        } else if (!FluidPipeBlock.isPipe(state)) {
            return null;
        } else {
            Direction.Axis axisFound = null;
            int connections = 0;
            for (Direction.Axis axis : Iterate.axes) {
                Direction d1 = Direction.get(Direction.AxisDirection.NEGATIVE, axis);
                Direction d2 = Direction.get(Direction.AxisDirection.POSITIVE, axis);
                boolean openAt1 = FluidPipeBlock.isOpenAt(state, d1);
                boolean openAt2 = FluidPipeBlock.isOpenAt(state, d2);
                if (openAt1) {
                    connections++;
                }
                if (openAt2) {
                    connections++;
                }
                if (openAt1 && openAt2) {
                    if (axisFound != null) {
                        return null;
                    }
                    axisFound = axis;
                }
            }
            return connections == 2 ? axisFound : null;
        }
    }
}