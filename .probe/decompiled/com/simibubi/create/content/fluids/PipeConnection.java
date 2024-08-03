package com.simibubi.create.content.fluids;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;

public class PipeConnection {

    public Direction side;

    Couple<Float> pressure;

    Optional<FlowSource> source;

    Optional<FlowSource> previousSource;

    Optional<PipeConnection.Flow> flow;

    boolean particleSplashNextTick;

    Optional<FluidNetwork> network;

    public static final int MAX_PARTICLE_RENDER_DISTANCE = 20;

    public static final int SPLASH_PARTICLE_AMOUNT = 1;

    public static final float IDLE_PARTICLE_SPAWN_CHANCE = 0.001F;

    public static final float RIM_RADIUS = 0.265625F;

    public static final Random r = new Random();

    public PipeConnection(Direction side) {
        this.side = side;
        this.pressure = Couple.create(() -> 0.0F);
        this.flow = Optional.empty();
        this.previousSource = Optional.empty();
        this.source = Optional.empty();
        this.network = Optional.empty();
        this.particleSplashNextTick = false;
    }

    public FluidStack getProvidedFluid() {
        FluidStack empty = FluidStack.EMPTY;
        if (!this.hasFlow()) {
            return empty;
        } else {
            PipeConnection.Flow flow = (PipeConnection.Flow) this.flow.get();
            if (!flow.inbound) {
                return empty;
            } else {
                return !flow.complete ? empty : flow.fluid;
            }
        }
    }

    public boolean flipFlowsIfPressureReversed() {
        if (!this.hasFlow()) {
            return false;
        } else {
            boolean singlePressure = this.comparePressure() != 0.0F && (this.getInboundPressure() == 0.0F || this.getOutwardPressure() == 0.0F);
            PipeConnection.Flow flow = (PipeConnection.Flow) this.flow.get();
            if (singlePressure && this.comparePressure() < 0.0F != flow.inbound) {
                flow.inbound = !flow.inbound;
                if (!flow.complete) {
                    this.flow = Optional.empty();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public void manageSource(Level world, BlockPos pos) {
        if (this.source.isPresent() || this.determineSource(world, pos)) {
            FlowSource flowSource = (FlowSource) this.source.get();
            flowSource.manageSource(world);
        }
    }

    public boolean manageFlows(Level world, BlockPos pos, FluidStack internalFluid, Predicate<FluidStack> extractionPredicate) {
        Optional<FluidNetwork> retainedNetwork = this.network;
        this.network = Optional.empty();
        if (!this.source.isPresent() && !this.determineSource(world, pos)) {
            return false;
        } else {
            FlowSource flowSource = (FlowSource) this.source.get();
            if (!this.hasFlow()) {
                if (!this.hasPressure()) {
                    return false;
                } else {
                    boolean prioritizeInbound = this.comparePressure() < 0.0F;
                    for (boolean trueFalse : Iterate.trueAndFalse) {
                        boolean inbound = prioritizeInbound == trueFalse;
                        if (this.pressure.get(inbound) != 0.0F && this.tryStartingNewFlow(inbound, inbound ? flowSource.provideFluid(extractionPredicate) : internalFluid)) {
                            return true;
                        }
                    }
                    return false;
                }
            } else {
                PipeConnection.Flow flow = (PipeConnection.Flow) this.flow.get();
                FluidStack provided = flow.inbound ? flowSource.provideFluid(extractionPredicate) : internalFluid;
                if (this.hasPressure() && !provided.isEmpty() && provided.isFluidEqual(flow.fluid)) {
                    if (flow.inbound != this.comparePressure() < 0.0F) {
                        boolean inbound = !flow.inbound;
                        if (inbound && !provided.isEmpty() || !inbound && !internalFluid.isEmpty()) {
                            FluidPropagator.resetAffectedFluidNetworks(world, pos, this.side);
                            this.tryStartingNewFlow(inbound, inbound ? flowSource.provideFluid(extractionPredicate) : internalFluid);
                            return true;
                        }
                    }
                    flowSource.whileFlowPresent(world, flow.inbound);
                    if (!flowSource.isEndpoint()) {
                        return false;
                    } else if (!flow.inbound) {
                        return false;
                    } else {
                        this.network = retainedNetwork;
                        if (!this.hasNetwork()) {
                            this.network = Optional.of(new FluidNetwork(world, new BlockFace(pos, this.side), flowSource::provideHandler));
                        }
                        ((FluidNetwork) this.network.get()).tick();
                        return false;
                    }
                } else {
                    this.flow = Optional.empty();
                    return true;
                }
            }
        }
    }

    private boolean tryStartingNewFlow(boolean inbound, FluidStack providedFluid) {
        if (providedFluid.isEmpty()) {
            return false;
        } else {
            PipeConnection.Flow flow = new PipeConnection.Flow(inbound, providedFluid);
            this.flow = Optional.of(flow);
            return true;
        }
    }

    public boolean determineSource(Level world, BlockPos pos) {
        BlockPos relative = pos.relative(this.side);
        if (world.getChunk(relative.m_123341_() >> 4, relative.m_123343_() >> 4, ChunkStatus.FULL, false) == null) {
            return false;
        } else {
            BlockFace location = new BlockFace(pos, this.side);
            if (FluidPropagator.isOpenEnd(world, pos, this.side)) {
                if (this.previousSource.orElse(null) instanceof OpenEndedPipe) {
                    this.source = this.previousSource;
                } else {
                    this.source = Optional.of(new OpenEndedPipe(location));
                }
                return true;
            } else if (FluidPropagator.hasFluidCapability(world, location.getConnectedPos(), this.side.getOpposite())) {
                this.source = Optional.of(new FlowSource.FluidHandler(location));
                return true;
            } else {
                FluidTransportBehaviour behaviour = BlockEntityBehaviour.get(world, relative, FluidTransportBehaviour.TYPE);
                this.source = Optional.of(behaviour == null ? new FlowSource.Blocked(location) : new FlowSource.OtherPipe(location));
                return true;
            }
        }
    }

    public void tickFlowProgress(Level world, BlockPos pos) {
        if (this.hasFlow()) {
            PipeConnection.Flow flow = (PipeConnection.Flow) this.flow.get();
            if (!flow.fluid.isEmpty()) {
                if (world.isClientSide) {
                    if (!this.source.isPresent()) {
                        this.determineSource(world, pos);
                    }
                    this.spawnParticles(world, pos, flow.fluid);
                    if (this.particleSplashNextTick) {
                        this.spawnSplashOnRim(world, pos, flow.fluid);
                    }
                    this.particleSplashNextTick = false;
                }
                float flowSpeed = 0.03125F + Mth.clamp(this.pressure.get(flow.inbound) / 128.0F, 0.0F, 1.0F) * 31.0F / 32.0F;
                flow.progress.setValue((double) Math.min(flow.progress.getValue() + flowSpeed, 1.0F));
                if (flow.progress.getValue() >= 1.0F) {
                    flow.complete = true;
                }
            }
        }
    }

    public void serializeNBT(CompoundTag tag, boolean clientPacket) {
        CompoundTag connectionData = new CompoundTag();
        tag.put(this.side.getName(), connectionData);
        if (this.hasPressure()) {
            ListTag pressureData = new ListTag();
            pressureData.add(FloatTag.valueOf(this.getInboundPressure()));
            pressureData.add(FloatTag.valueOf(this.getOutwardPressure()));
            connectionData.put("Pressure", pressureData);
        }
        if (this.hasOpenEnd()) {
            connectionData.put("OpenEnd", ((OpenEndedPipe) this.source.get()).serializeNBT());
        }
        if (this.hasFlow()) {
            CompoundTag flowData = new CompoundTag();
            PipeConnection.Flow flow = (PipeConnection.Flow) this.flow.get();
            flow.fluid.writeToNBT(flowData);
            flowData.putBoolean("In", flow.inbound);
            if (!flow.complete) {
                flowData.put("Progress", flow.progress.writeNBT());
            }
            connectionData.put("Flow", flowData);
        }
    }

    private boolean hasOpenEnd() {
        return this.source.orElse(null) instanceof OpenEndedPipe;
    }

    public void deserializeNBT(CompoundTag tag, BlockPos blockEntityPos, boolean clientPacket) {
        CompoundTag connectionData = tag.getCompound(this.side.getName());
        if (connectionData.contains("Pressure")) {
            ListTag pressureData = connectionData.getList("Pressure", 5);
            this.pressure = Couple.create(pressureData.getFloat(0), pressureData.getFloat(1));
        } else {
            this.pressure.replace(f -> 0.0F);
        }
        this.source = Optional.empty();
        if (connectionData.contains("OpenEnd")) {
            this.source = Optional.of(OpenEndedPipe.fromNBT(connectionData.getCompound("OpenEnd"), blockEntityPos));
        }
        if (connectionData.contains("Flow")) {
            CompoundTag flowData = connectionData.getCompound("Flow");
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(flowData);
            boolean inbound = flowData.getBoolean("In");
            if (!this.flow.isPresent()) {
                this.flow = Optional.of(new PipeConnection.Flow(inbound, fluid));
                if (clientPacket) {
                    this.particleSplashNextTick = true;
                }
            }
            PipeConnection.Flow flow = (PipeConnection.Flow) this.flow.get();
            flow.fluid = fluid;
            flow.inbound = inbound;
            flow.complete = !flowData.contains("Progress");
            if (!flow.complete) {
                flow.progress.readNBT(flowData.getCompound("Progress"), clientPacket);
            } else {
                if (flow.progress.getValue() == 0.0F) {
                    flow.progress.startWithValue(1.0);
                }
                flow.progress.setValue(1.0);
            }
        } else {
            this.flow = Optional.empty();
        }
    }

    public float comparePressure() {
        return this.getOutwardPressure() - this.getInboundPressure();
    }

    public void wipePressure() {
        this.pressure.replace(f -> 0.0F);
        if (this.source.isPresent()) {
            this.previousSource = this.source;
        }
        this.source = Optional.empty();
        this.resetNetwork();
    }

    public FluidStack provideOutboundFlow() {
        if (!this.hasFlow()) {
            return FluidStack.EMPTY;
        } else {
            PipeConnection.Flow flow = (PipeConnection.Flow) this.flow.get();
            return flow.complete && !flow.inbound ? flow.fluid : FluidStack.EMPTY;
        }
    }

    public void addPressure(boolean inbound, float pressure) {
        this.pressure = this.pressure.mapWithContext((f, in) -> in == inbound ? f + pressure : f);
    }

    public Couple<Float> getPressure() {
        return this.pressure;
    }

    public boolean hasPressure() {
        return this.getInboundPressure() != 0.0F || this.getOutwardPressure() != 0.0F;
    }

    private float getOutwardPressure() {
        return this.pressure.getSecond();
    }

    private float getInboundPressure() {
        return this.pressure.getFirst();
    }

    public boolean hasFlow() {
        return this.flow.isPresent();
    }

    public boolean hasNetwork() {
        return this.network.isPresent();
    }

    public void resetNetwork() {
        this.network.ifPresent(FluidNetwork::reset);
    }

    public void spawnSplashOnRim(Level world, BlockPos pos, FluidStack fluid) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.spawnSplashOnRimInner(world, pos, fluid));
    }

    public void spawnParticles(Level world, BlockPos pos, FluidStack fluid) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.spawnParticlesInner(world, pos, fluid));
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnParticlesInner(Level world, BlockPos pos, FluidStack fluid) {
        if (world != Minecraft.getInstance().level || isRenderEntityWithinDistance(pos)) {
            if (this.hasOpenEnd()) {
                this.spawnPouringLiquid(world, pos, fluid, 1);
            } else if (r.nextFloat() < 0.001F) {
                this.spawnRimParticles(world, pos, fluid, 1);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnSplashOnRimInner(Level world, BlockPos pos, FluidStack fluid) {
        if (world != Minecraft.getInstance().level || isRenderEntityWithinDistance(pos)) {
            this.spawnRimParticles(world, pos, fluid, 1);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnRimParticles(Level world, BlockPos pos, FluidStack fluid, int amount) {
        if (this.hasOpenEnd()) {
            this.spawnPouringLiquid(world, pos, fluid, amount);
        } else {
            ParticleOptions particle = FluidFX.getDrippingParticle(fluid);
            FluidFX.spawnRimParticles(world, pos, this.side, amount, particle, 0.265625F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnPouringLiquid(Level world, BlockPos pos, FluidStack fluid, int amount) {
        ParticleOptions particle = FluidFX.getFluidParticle(fluid);
        Vec3 directionVec = Vec3.atLowerCornerOf(this.side.getNormal());
        if (this.hasFlow()) {
            PipeConnection.Flow flow = (PipeConnection.Flow) this.flow.get();
            FluidFX.spawnPouringLiquid(world, pos, amount, particle, 0.265625F, directionVec, flow.inbound);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isRenderEntityWithinDistance(BlockPos pos) {
        Entity renderViewEntity = Minecraft.getInstance().getCameraEntity();
        if (renderViewEntity == null) {
            return false;
        } else {
            Vec3 center = VecHelper.getCenterOf(pos);
            return !(renderViewEntity.position().distanceTo(center) > 20.0);
        }
    }

    public class Flow {

        public boolean complete;

        public boolean inbound;

        public LerpedFloat progress;

        public FluidStack fluid;

        public Flow(boolean inbound, FluidStack fluid) {
            this.inbound = inbound;
            this.fluid = fluid;
            this.progress = LerpedFloat.linear().startWithValue(0.0);
            this.complete = false;
        }
    }
}