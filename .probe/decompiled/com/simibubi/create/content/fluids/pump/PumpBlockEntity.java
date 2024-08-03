package com.simibubi.create.content.fluids.pump;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeConnection;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class PumpBlockEntity extends KineticBlockEntity {

    Couple<MutableBoolean> sidesToUpdate = Couple.create(MutableBoolean::new);

    boolean pressureUpdate;

    boolean scheduleFlip;

    public PumpBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(new PumpBlockEntity.PumpFluidTransferBehaviour(this));
        this.registerAwardables(behaviours, FluidPropagator.getSharedTriggers());
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.PUMP });
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_58857_.isClientSide || this.isVirtual()) {
            if (this.scheduleFlip) {
                this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) this.m_58900_().m_61124_(PumpBlock.FACING, ((Direction) this.m_58900_().m_61143_(PumpBlock.FACING)).getOpposite()));
                this.scheduleFlip = false;
            }
            this.sidesToUpdate.forEachWithContext((update, isFront) -> {
                if (!update.isFalse()) {
                    update.setFalse();
                    this.distributePressureTo(isFront ? this.getFront() : this.getFront().getOpposite());
                }
            });
        }
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        if (Math.abs(previousSpeed) != Math.abs(this.getSpeed())) {
            if (this.speed != 0.0F) {
                this.award(AllAdvancements.PUMP);
            }
            if (!this.f_58857_.isClientSide || this.isVirtual()) {
                this.updatePressureChange();
            }
        }
    }

    public void updatePressureChange() {
        this.pressureUpdate = false;
        BlockPos frontPos = this.f_58858_.relative(this.getFront());
        BlockPos backPos = this.f_58858_.relative(this.getFront().getOpposite());
        FluidPropagator.propagateChangedPipe(this.f_58857_, frontPos, this.f_58857_.getBlockState(frontPos));
        FluidPropagator.propagateChangedPipe(this.f_58857_, backPos, this.f_58857_.getBlockState(backPos));
        FluidTransportBehaviour behaviour = this.getBehaviour(FluidTransportBehaviour.TYPE);
        if (behaviour != null) {
            behaviour.wipePressure();
        }
        this.sidesToUpdate.forEach(MutableBoolean::setTrue);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (compound.getBoolean("Reversed")) {
            this.scheduleFlip = true;
        }
    }

    protected void distributePressureTo(Direction side) {
        if (this.getSpeed() != 0.0F) {
            BlockFace start = new BlockFace(this.f_58858_, side);
            boolean pull = this.isPullingOnSide(this.isFront(side));
            Set<BlockFace> targets = new HashSet();
            Map<BlockPos, Pair<Integer, Map<Direction, Boolean>>> pipeGraph = new HashMap();
            if (!pull) {
                FluidPropagator.resetAffectedFluidNetworks(this.f_58857_, this.f_58858_, side.getOpposite());
            }
            if (!this.hasReachedValidEndpoint(this.f_58857_, start, pull)) {
                ((Map) ((Pair) pipeGraph.computeIfAbsent(this.f_58858_, $ -> Pair.of(0, new IdentityHashMap()))).getSecond()).put(side, pull);
                ((Map) ((Pair) pipeGraph.computeIfAbsent(start.getConnectedPos(), $ -> Pair.of(1, new IdentityHashMap()))).getSecond()).put(side.getOpposite(), !pull);
                List<Pair<Integer, BlockPos>> frontier = new ArrayList();
                Set<BlockPos> visited = new HashSet();
                int maxDistance = FluidPropagator.getPumpRange();
                frontier.add(Pair.of(1, start.getConnectedPos()));
                while (!frontier.isEmpty()) {
                    Pair<Integer, BlockPos> entry = (Pair<Integer, BlockPos>) frontier.remove(0);
                    int distance = entry.getFirst();
                    BlockPos currentPos = entry.getSecond();
                    if (this.f_58857_.isLoaded(currentPos) && !visited.contains(currentPos)) {
                        visited.add(currentPos);
                        BlockState currentState = this.f_58857_.getBlockState(currentPos);
                        FluidTransportBehaviour pipe = FluidPropagator.getPipe(this.f_58857_, currentPos);
                        if (pipe != null) {
                            for (Direction face : FluidPropagator.getPipeConnections(currentState, pipe)) {
                                BlockFace blockFace = new BlockFace(currentPos, face);
                                BlockPos connectedPos = blockFace.getConnectedPos();
                                if (this.f_58857_.isLoaded(connectedPos) && !blockFace.isEquivalent(start)) {
                                    if (this.hasReachedValidEndpoint(this.f_58857_, blockFace, pull)) {
                                        ((Map) ((Pair) pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap()))).getSecond()).put(face, pull);
                                        targets.add(blockFace);
                                    } else {
                                        FluidTransportBehaviour pipeBehaviour = FluidPropagator.getPipe(this.f_58857_, connectedPos);
                                        if (pipeBehaviour != null && !(pipeBehaviour instanceof PumpBlockEntity.PumpFluidTransferBehaviour) && !visited.contains(connectedPos)) {
                                            if (distance + 1 >= maxDistance) {
                                                ((Map) ((Pair) pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap()))).getSecond()).put(face, pull);
                                                targets.add(blockFace);
                                            } else {
                                                ((Map) ((Pair) pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap()))).getSecond()).put(face, pull);
                                                ((Map) ((Pair) pipeGraph.computeIfAbsent(connectedPos, $ -> Pair.of(distance + 1, new IdentityHashMap()))).getSecond()).put(face.getOpposite(), !pull);
                                                frontier.add(Pair.of(distance + 1, connectedPos));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Map<Integer, Set<BlockFace>> validFaces = new HashMap();
            this.searchForEndpointRecursively(pipeGraph, targets, validFaces, new BlockFace(start.getPos(), start.getOppositeFace()), pull);
            float pressure = Math.abs(this.getSpeed());
            for (Set<BlockFace> set : validFaces.values()) {
                int parallelBranches = Math.max(1, set.size() - 1);
                for (BlockFace facex : set) {
                    BlockPos pipePos = facex.getPos();
                    Direction pipeSide = facex.getFace();
                    if (!pipePos.equals(this.f_58858_)) {
                        boolean inbound = (Boolean) ((Map) ((Pair) pipeGraph.get(pipePos)).getSecond()).get(pipeSide);
                        FluidTransportBehaviour pipeBehaviour = FluidPropagator.getPipe(this.f_58857_, pipePos);
                        if (pipeBehaviour != null) {
                            pipeBehaviour.addPressure(pipeSide, inbound, pressure / (float) parallelBranches);
                        }
                    }
                }
            }
        }
    }

    protected boolean searchForEndpointRecursively(Map<BlockPos, Pair<Integer, Map<Direction, Boolean>>> pipeGraph, Set<BlockFace> targets, Map<Integer, Set<BlockFace>> validFaces, BlockFace currentFace, boolean pull) {
        BlockPos currentPos = currentFace.getPos();
        if (!pipeGraph.containsKey(currentPos)) {
            return false;
        } else {
            Pair<Integer, Map<Direction, Boolean>> pair = (Pair<Integer, Map<Direction, Boolean>>) pipeGraph.get(currentPos);
            int distance = pair.getFirst();
            boolean atLeastOneBranchSuccessful = false;
            for (Direction nextFacing : Iterate.directions) {
                if (nextFacing != currentFace.getFace()) {
                    Map<Direction, Boolean> map = pair.getSecond();
                    if (map.containsKey(nextFacing)) {
                        BlockFace localTarget = new BlockFace(currentPos, nextFacing);
                        if (targets.contains(localTarget)) {
                            ((Set) validFaces.computeIfAbsent(distance, $ -> new HashSet())).add(localTarget);
                            atLeastOneBranchSuccessful = true;
                        } else if ((Boolean) map.get(nextFacing) == pull && this.searchForEndpointRecursively(pipeGraph, targets, validFaces, new BlockFace(currentPos.relative(nextFacing), nextFacing.getOpposite()), pull)) {
                            ((Set) validFaces.computeIfAbsent(distance, $ -> new HashSet())).add(localTarget);
                            atLeastOneBranchSuccessful = true;
                        }
                    }
                }
            }
            if (atLeastOneBranchSuccessful) {
                ((Set) validFaces.computeIfAbsent(distance, $ -> new HashSet())).add(currentFace);
            }
            return atLeastOneBranchSuccessful;
        }
    }

    private boolean hasReachedValidEndpoint(LevelAccessor world, BlockFace blockFace, boolean pull) {
        BlockPos connectedPos = blockFace.getConnectedPos();
        BlockState connectedState = world.m_8055_(connectedPos);
        BlockEntity blockEntity = world.m_7702_(connectedPos);
        Direction face = blockFace.getFace();
        if (PumpBlock.isPump(connectedState) && ((Direction) connectedState.m_61143_(PumpBlock.FACING)).getAxis() == face.getAxis() && blockEntity instanceof PumpBlockEntity pumpBE) {
            return pumpBE.isPullingOnSide(pumpBE.isFront(blockFace.getOppositeFace())) != pull;
        } else {
            FluidTransportBehaviour pipe = FluidPropagator.getPipe(world, connectedPos);
            if (pipe != null && pipe.canHaveFlowToward(connectedState, blockFace.getOppositeFace())) {
                return false;
            } else {
                if (blockEntity != null) {
                    LazyOptional<IFluidHandler> capability = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, face.getOpposite());
                    if (capability.isPresent()) {
                        return true;
                    }
                }
                return FluidPropagator.isOpenEnd(world, blockFace.getPos(), face);
            }
        }
    }

    public void updatePipesOnSide(Direction side) {
        if (this.isSideAccessible(side)) {
            this.updatePipeNetwork(this.isFront(side));
            this.getBehaviour(FluidTransportBehaviour.TYPE).wipePressure();
        }
    }

    protected boolean isFront(Direction side) {
        BlockState blockState = this.m_58900_();
        if (!(blockState.m_60734_() instanceof PumpBlock)) {
            return false;
        } else {
            Direction front = (Direction) blockState.m_61143_(PumpBlock.FACING);
            return side == front;
        }
    }

    @Nullable
    protected Direction getFront() {
        BlockState blockState = this.m_58900_();
        return !(blockState.m_60734_() instanceof PumpBlock) ? null : (Direction) blockState.m_61143_(PumpBlock.FACING);
    }

    protected void updatePipeNetwork(boolean front) {
        this.sidesToUpdate.get(front).setTrue();
    }

    public boolean isSideAccessible(Direction side) {
        BlockState blockState = this.m_58900_();
        return !(blockState.m_60734_() instanceof PumpBlock) ? false : ((Direction) blockState.m_61143_(PumpBlock.FACING)).getAxis() == side.getAxis();
    }

    public boolean isPullingOnSide(boolean front) {
        return !front;
    }

    class PumpFluidTransferBehaviour extends FluidTransportBehaviour {

        public PumpFluidTransferBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public void tick() {
            super.tick();
            for (Entry<Direction, PipeConnection> entry : this.interfaces.entrySet()) {
                boolean pull = PumpBlockEntity.this.isPullingOnSide(PumpBlockEntity.this.isFront((Direction) entry.getKey()));
                Couple<Float> pressure = ((PipeConnection) entry.getValue()).getPressure();
                pressure.set(pull, Math.abs(PumpBlockEntity.this.getSpeed()));
                pressure.set(!pull, 0.0F);
            }
        }

        @Override
        public boolean canHaveFlowToward(BlockState state, Direction direction) {
            return PumpBlockEntity.this.isSideAccessible(direction);
        }

        @Override
        public FluidTransportBehaviour.AttachmentTypes getRenderedRimAttachment(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
            FluidTransportBehaviour.AttachmentTypes attachment = super.getRenderedRimAttachment(world, pos, state, direction);
            return attachment == FluidTransportBehaviour.AttachmentTypes.RIM ? FluidTransportBehaviour.AttachmentTypes.NONE : attachment;
        }
    }
}