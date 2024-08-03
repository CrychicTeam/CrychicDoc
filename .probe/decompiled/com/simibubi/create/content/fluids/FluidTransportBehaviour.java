package com.simibubi.create.content.fluids;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.WorldAttached;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.fluids.FluidStack;

public abstract class FluidTransportBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<FluidTransportBehaviour> TYPE = new BehaviourType<>();

    public Map<Direction, PipeConnection> interfaces;

    public FluidTransportBehaviour.UpdatePhase phase = FluidTransportBehaviour.UpdatePhase.WAIT_FOR_PUMPS;

    public static final WorldAttached<Map<BlockPos, Map<Direction, PipeConnection>>> interfaceTransfer = new WorldAttached<>($ -> new HashMap());

    public FluidTransportBehaviour(SmartBlockEntity be) {
        super(be);
    }

    public boolean canPullFluidFrom(FluidStack fluid, BlockState state, Direction direction) {
        return true;
    }

    public abstract boolean canHaveFlowToward(BlockState var1, Direction var2);

    @Override
    public void initialize() {
        super.initialize();
        this.createConnectionData();
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.getWorld();
        BlockPos pos = this.getPos();
        boolean onServer = !world.isClientSide || this.blockEntity.isVirtual();
        if (this.interfaces != null) {
            Collection<PipeConnection> connections = this.interfaces.values();
            PipeConnection singleSource = null;
            if (this.phase == FluidTransportBehaviour.UpdatePhase.WAIT_FOR_PUMPS) {
                this.phase = FluidTransportBehaviour.UpdatePhase.FLIP_FLOWS;
            } else {
                if (onServer) {
                    boolean sendUpdate = false;
                    for (PipeConnection connection : connections) {
                        sendUpdate |= connection.flipFlowsIfPressureReversed();
                        connection.manageSource(world, pos);
                    }
                    if (sendUpdate) {
                        this.blockEntity.notifyUpdate();
                    }
                }
                if (this.phase == FluidTransportBehaviour.UpdatePhase.FLIP_FLOWS) {
                    this.phase = FluidTransportBehaviour.UpdatePhase.IDLE;
                } else {
                    if (onServer) {
                        FluidStack availableFlow = FluidStack.EMPTY;
                        FluidStack collidingFlow = FluidStack.EMPTY;
                        for (PipeConnection connection : connections) {
                            FluidStack fluidInFlow = connection.getProvidedFluid();
                            if (!fluidInFlow.isEmpty()) {
                                if (availableFlow.isEmpty()) {
                                    singleSource = connection;
                                    availableFlow = fluidInFlow;
                                } else {
                                    if (!availableFlow.isFluidEqual(fluidInFlow)) {
                                        collidingFlow = fluidInFlow;
                                        break;
                                    }
                                    singleSource = null;
                                    availableFlow = fluidInFlow;
                                }
                            }
                        }
                        if (!collidingFlow.isEmpty()) {
                            FluidReactions.handlePipeFlowCollision(world, pos, availableFlow, collidingFlow);
                            return;
                        }
                        boolean sendUpdate = false;
                        for (PipeConnection connectionx : connections) {
                            FluidStack internalFluid = singleSource != connectionx ? availableFlow : FluidStack.EMPTY;
                            Predicate<FluidStack> extractionPredicate = extracted -> this.canPullFluidFrom(extracted, this.blockEntity.m_58900_(), connection.side);
                            sendUpdate |= connectionx.manageFlows(world, pos, internalFluid, extractionPredicate);
                        }
                        if (sendUpdate) {
                            this.blockEntity.notifyUpdate();
                        }
                    }
                    for (PipeConnection connectionx : connections) {
                        connectionx.tickFlowProgress(world, pos);
                    }
                }
            }
        }
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if (this.interfaces == null) {
            this.interfaces = new IdentityHashMap();
        }
        for (Direction face : Iterate.directions) {
            if (nbt.contains(face.getName())) {
                this.interfaces.computeIfAbsent(face, d -> new PipeConnection(d));
            }
        }
        if (this.interfaces.isEmpty()) {
            this.interfaces = null;
        } else {
            this.interfaces.values().forEach(connection -> connection.deserializeNBT(nbt, this.blockEntity.m_58899_(), clientPacket));
        }
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (clientPacket) {
            this.createConnectionData();
        }
        if (this.interfaces != null) {
            this.interfaces.values().forEach(connection -> connection.serializeNBT(nbt, clientPacket));
        }
    }

    public FluidStack getProvidedOutwardFluid(Direction side) {
        this.createConnectionData();
        return !this.interfaces.containsKey(side) ? FluidStack.EMPTY : ((PipeConnection) this.interfaces.get(side)).provideOutboundFlow();
    }

    @Nullable
    public PipeConnection getConnection(Direction side) {
        this.createConnectionData();
        return (PipeConnection) this.interfaces.get(side);
    }

    public boolean hasAnyPressure() {
        this.createConnectionData();
        for (PipeConnection pipeConnection : this.interfaces.values()) {
            if (pipeConnection.hasPressure()) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public PipeConnection.Flow getFlow(Direction side) {
        this.createConnectionData();
        return !this.interfaces.containsKey(side) ? null : (PipeConnection.Flow) ((PipeConnection) this.interfaces.get(side)).flow.orElse(null);
    }

    public void addPressure(Direction side, boolean inbound, float pressure) {
        this.createConnectionData();
        if (this.interfaces.containsKey(side)) {
            ((PipeConnection) this.interfaces.get(side)).addPressure(inbound, pressure);
            this.blockEntity.sendData();
        }
    }

    public void wipePressure() {
        if (this.interfaces != null) {
            for (Direction d : Iterate.directions) {
                if (!this.canHaveFlowToward(this.blockEntity.m_58900_(), d)) {
                    this.interfaces.remove(d);
                } else {
                    this.interfaces.computeIfAbsent(d, PipeConnection::new);
                }
            }
        }
        this.phase = FluidTransportBehaviour.UpdatePhase.WAIT_FOR_PUMPS;
        this.createConnectionData();
        this.interfaces.values().forEach(PipeConnection::wipePressure);
        this.blockEntity.sendData();
    }

    private void createConnectionData() {
        if (this.interfaces == null) {
            this.interfaces = new IdentityHashMap();
            for (Direction d : Iterate.directions) {
                if (this.canHaveFlowToward(this.blockEntity.m_58900_(), d)) {
                    this.interfaces.put(d, new PipeConnection(d));
                }
            }
        }
    }

    public FluidTransportBehaviour.AttachmentTypes getRenderedRimAttachment(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
        if (!this.canHaveFlowToward(state, direction)) {
            return FluidTransportBehaviour.AttachmentTypes.NONE;
        } else {
            BlockPos offsetPos = pos.relative(direction);
            BlockState facingState = world.m_8055_(offsetPos);
            if (facingState.m_60734_() instanceof PumpBlock && facingState.m_61143_(PumpBlock.FACING) == direction.getOpposite()) {
                return FluidTransportBehaviour.AttachmentTypes.NONE;
            } else if (AllBlocks.ENCASED_FLUID_PIPE.has(facingState) && (Boolean) facingState.m_61143_((Property) EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(direction.getOpposite()))) {
                return FluidTransportBehaviour.AttachmentTypes.RIM;
            } else {
                return FluidPropagator.hasFluidCapability(world, offsetPos, direction.getOpposite()) && !AllBlocks.HOSE_PULLEY.has(facingState) ? FluidTransportBehaviour.AttachmentTypes.DRAIN : FluidTransportBehaviour.AttachmentTypes.RIM;
            }
        }
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public static void cacheFlows(LevelAccessor world, BlockPos pos) {
        FluidTransportBehaviour pipe = BlockEntityBehaviour.get(world, pos, TYPE);
        if (pipe != null) {
            interfaceTransfer.get(world).put(pos, pipe.interfaces);
        }
    }

    public static void loadFlows(LevelAccessor world, BlockPos pos) {
        FluidTransportBehaviour newPipe = BlockEntityBehaviour.get(world, pos, TYPE);
        if (newPipe != null) {
            newPipe.interfaces = (Map<Direction, PipeConnection>) interfaceTransfer.get(world).remove(pos);
        }
    }

    public static enum AttachmentTypes {

        NONE,
        CONNECTION(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.CONNECTION),
        RIM(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.RIM_CONNECTOR, FluidTransportBehaviour.AttachmentTypes.ComponentPartials.RIM),
        PARTIAL_RIM(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.RIM),
        DRAIN(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.RIM_CONNECTOR, FluidTransportBehaviour.AttachmentTypes.ComponentPartials.DRAIN),
        PARTIAL_DRAIN(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.DRAIN);

        public final FluidTransportBehaviour.AttachmentTypes.ComponentPartials[] partials;

        private AttachmentTypes(FluidTransportBehaviour.AttachmentTypes.ComponentPartials... partials) {
            this.partials = partials;
        }

        public FluidTransportBehaviour.AttachmentTypes withoutConnector() {
            if (this == RIM) {
                return PARTIAL_RIM;
            } else {
                return this == DRAIN ? PARTIAL_DRAIN : this;
            }
        }

        public static enum ComponentPartials {

            CONNECTION, RIM_CONNECTOR, RIM, DRAIN
        }
    }

    public static enum UpdatePhase {

        WAIT_FOR_PUMPS, FLIP_FLOWS, IDLE
    }
}