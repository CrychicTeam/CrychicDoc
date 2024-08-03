package com.simibubi.create.content.fluids;

import com.simibubi.create.content.contraptions.actors.psi.PortableFluidInterfaceBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidNetwork {

    private static int CYCLES_PER_TICK = 16;

    Level world;

    BlockFace start;

    Supplier<LazyOptional<IFluidHandler>> sourceSupplier;

    LazyOptional<IFluidHandler> source;

    int transferSpeed;

    int pauseBeforePropagation;

    List<BlockFace> queued;

    Set<Pair<BlockFace, PipeConnection>> frontier;

    Set<BlockPos> visited;

    FluidStack fluid;

    List<Pair<BlockFace, LazyOptional<IFluidHandler>>> targets;

    Map<BlockPos, WeakReference<FluidTransportBehaviour>> cache;

    public FluidNetwork(Level world, BlockFace location, Supplier<LazyOptional<IFluidHandler>> sourceSupplier) {
        this.world = world;
        this.start = location;
        this.sourceSupplier = sourceSupplier;
        this.source = LazyOptional.empty();
        this.fluid = FluidStack.EMPTY;
        this.frontier = new HashSet();
        this.visited = new HashSet();
        this.targets = new ArrayList();
        this.cache = new HashMap();
        this.queued = new ArrayList();
        this.reset();
    }

    public void tick() {
        if (this.pauseBeforePropagation > 0) {
            this.pauseBeforePropagation--;
        } else {
            for (int cycle = 0; cycle < CYCLES_PER_TICK; cycle++) {
                boolean shouldContinue = false;
                Iterator<BlockFace> iterator = this.queued.iterator();
                while (iterator.hasNext()) {
                    BlockFace blockFace = (BlockFace) iterator.next();
                    if (this.isPresent(blockFace)) {
                        PipeConnection pipeConnection = this.get(blockFace);
                        if (pipeConnection != null) {
                            if (blockFace.equals(this.start)) {
                                this.transferSpeed = (int) Math.max(1.0F, pipeConnection.pressure.get(true) / 2.0F);
                            }
                            this.frontier.add(Pair.of(blockFace, pipeConnection));
                        }
                        iterator.remove();
                    }
                }
                iterator = this.frontier.iterator();
                while (iterator.hasNext()) {
                    Pair<BlockFace, PipeConnection> pair = (Pair<BlockFace, PipeConnection>) iterator.next();
                    BlockFace blockFace = pair.getFirst();
                    PipeConnection pipeConnection = pair.getSecond();
                    if (pipeConnection.hasFlow()) {
                        PipeConnection.Flow flow = (PipeConnection.Flow) pipeConnection.flow.get();
                        if (!this.fluid.isEmpty() && !flow.fluid.isFluidEqual(this.fluid)) {
                            iterator.remove();
                        } else if (!flow.inbound) {
                            if (pipeConnection.comparePressure() >= 0.0F) {
                                iterator.remove();
                            }
                        } else if (flow.complete) {
                            if (this.fluid.isEmpty()) {
                                this.fluid = flow.fluid;
                            }
                            boolean canRemove = true;
                            for (Direction side : Iterate.directions) {
                                if (side != blockFace.getFace()) {
                                    BlockFace adjacentLocation = new BlockFace(blockFace.getPos(), side);
                                    PipeConnection adjacent = this.get(adjacentLocation);
                                    if (adjacent != null) {
                                        if (!adjacent.hasFlow()) {
                                            if (adjacent.hasPressure() && adjacent.pressure.getSecond() > 0.0F) {
                                                canRemove = false;
                                            }
                                        } else {
                                            PipeConnection.Flow outFlow = (PipeConnection.Flow) adjacent.flow.get();
                                            if (outFlow.inbound) {
                                                if (adjacent.comparePressure() > 0.0F) {
                                                    canRemove = false;
                                                }
                                            } else if (!outFlow.complete) {
                                                canRemove = false;
                                            } else if (!adjacent.source.isPresent() && !adjacent.determineSource(this.world, blockFace.getPos())) {
                                                canRemove = false;
                                            } else if (adjacent.source.isPresent() && ((FlowSource) adjacent.source.get()).isEndpoint()) {
                                                this.targets.add(Pair.of(adjacentLocation, ((FlowSource) adjacent.source.get()).provideHandler()));
                                            } else if (this.visited.add(adjacentLocation.getConnectedPos())) {
                                                this.queued.add(adjacentLocation.getOpposite());
                                                shouldContinue = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if (canRemove) {
                                iterator.remove();
                            }
                        }
                    }
                }
                if (!shouldContinue) {
                    break;
                }
            }
            if (!this.source.isPresent()) {
                this.source = (LazyOptional<IFluidHandler>) this.sourceSupplier.get();
            }
            if (this.source.isPresent()) {
                this.keepPortableFluidInterfaceEngaged();
                if (!this.targets.isEmpty()) {
                    for (Pair<BlockFace, LazyOptional<IFluidHandler>> pair : this.targets) {
                        if (!pair.getSecond().isPresent() || this.world.getGameTime() % 40L == 0L) {
                            PipeConnection pipeConnection = this.get(pair.getFirst());
                            if (pipeConnection != null) {
                                pipeConnection.source.ifPresent(fs -> {
                                    if (fs.isEndpoint()) {
                                        pair.setSecond(fs.provideHandler());
                                    }
                                });
                            }
                        }
                    }
                    int flowSpeed = this.transferSpeed;
                    Map<IFluidHandler, Integer> accumulatedFill = new IdentityHashMap();
                    for (boolean simulate : Iterate.trueAndFalse) {
                        IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
                        IFluidHandler handler = this.source.orElse(null);
                        if (handler == null) {
                            return;
                        }
                        FluidStack transfer = FluidStack.EMPTY;
                        for (int i = 0; i < handler.getTanks(); i++) {
                            FluidStack contained = handler.getFluidInTank(i);
                            if (!contained.isEmpty() && contained.isFluidEqual(this.fluid)) {
                                FluidStack toExtract = FluidHelper.copyStackWithAmount(contained, flowSpeed);
                                transfer = handler.drain(toExtract, action);
                            }
                        }
                        if (transfer.isEmpty()) {
                            FluidStack genericExtract = handler.drain(flowSpeed, action);
                            if (!genericExtract.isEmpty() && genericExtract.isFluidEqual(this.fluid)) {
                                transfer = genericExtract;
                            }
                        }
                        if (transfer.isEmpty()) {
                            return;
                        }
                        if (simulate) {
                            flowSpeed = transfer.getAmount();
                        }
                        List<Pair<BlockFace, LazyOptional<IFluidHandler>>> availableOutputs = new ArrayList(this.targets);
                        while (!availableOutputs.isEmpty() && transfer.getAmount() > 0) {
                            int dividedTransfer = transfer.getAmount() / availableOutputs.size();
                            int remainder = transfer.getAmount() % availableOutputs.size();
                            Iterator<Pair<BlockFace, LazyOptional<IFluidHandler>>> iterator = availableOutputs.iterator();
                            while (iterator.hasNext()) {
                                Pair<BlockFace, LazyOptional<IFluidHandler>> pairx = (Pair<BlockFace, LazyOptional<IFluidHandler>>) iterator.next();
                                int toTransfer = dividedTransfer;
                                if (remainder > 0) {
                                    toTransfer = dividedTransfer + 1;
                                    remainder--;
                                }
                                if (transfer.isEmpty()) {
                                    break;
                                }
                                IFluidHandler targetHandler = pairx.getSecond().orElse(null);
                                if (targetHandler == null) {
                                    iterator.remove();
                                } else {
                                    int simulatedTransfer = toTransfer;
                                    if (simulate) {
                                        simulatedTransfer = toTransfer + (Integer) accumulatedFill.getOrDefault(targetHandler, 0);
                                    }
                                    FluidStack divided = transfer.copy();
                                    divided.setAmount(simulatedTransfer);
                                    int fill = targetHandler.fill(divided, action);
                                    if (simulate) {
                                        accumulatedFill.put(targetHandler, fill);
                                        fill -= simulatedTransfer - toTransfer;
                                    }
                                    transfer.setAmount(transfer.getAmount() - fill);
                                    if (fill < simulatedTransfer) {
                                        iterator.remove();
                                    }
                                }
                            }
                        }
                        flowSpeed -= transfer.getAmount();
                        transfer = FluidStack.EMPTY;
                    }
                }
            }
        }
    }

    private void keepPortableFluidInterfaceEngaged() {
        IFluidHandler handler = this.source.orElse(null);
        if (handler instanceof PortableFluidInterfaceBlockEntity.InterfaceFluidHandler) {
            if (!this.frontier.isEmpty()) {
                ((PortableFluidInterfaceBlockEntity.InterfaceFluidHandler) handler).keepAlive();
            }
        }
    }

    public void reset() {
        this.frontier.clear();
        this.visited.clear();
        this.targets.clear();
        this.queued.clear();
        this.fluid = FluidStack.EMPTY;
        this.queued.add(this.start);
        this.pauseBeforePropagation = 2;
    }

    @Nullable
    private PipeConnection get(BlockFace location) {
        BlockPos pos = location.getPos();
        FluidTransportBehaviour fluidTransfer = this.getFluidTransfer(pos);
        return fluidTransfer == null ? null : fluidTransfer.getConnection(location.getFace());
    }

    private boolean isPresent(BlockFace location) {
        return this.world.isLoaded(location.getPos());
    }

    @Nullable
    private FluidTransportBehaviour getFluidTransfer(BlockPos pos) {
        WeakReference<FluidTransportBehaviour> weakReference = (WeakReference<FluidTransportBehaviour>) this.cache.get(pos);
        FluidTransportBehaviour behaviour = weakReference != null ? (FluidTransportBehaviour) weakReference.get() : null;
        if (behaviour != null && behaviour.blockEntity.m_58901_()) {
            behaviour = null;
        }
        if (behaviour == null) {
            behaviour = BlockEntityBehaviour.get(this.world, pos, FluidTransportBehaviour.TYPE);
            if (behaviour != null) {
                this.cache.put(pos, new WeakReference(behaviour));
            }
        }
        return behaviour;
    }
}