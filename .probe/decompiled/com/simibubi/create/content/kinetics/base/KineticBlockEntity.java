package com.simibubi.create.content.kinetics.base;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.gearbox.GearboxBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class KineticBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IHaveHoveringInformation {

    @Nullable
    public Long network;

    @Nullable
    public BlockPos source;

    public boolean networkDirty;

    public boolean updateSpeed;

    public int preventSpeedUpdate;

    protected KineticEffectHandler effects = new KineticEffectHandler(this);

    protected float speed;

    protected float capacity;

    protected float stress;

    protected boolean overStressed;

    protected boolean wasMoved;

    private int flickerTally;

    private int networkSize;

    private int validationCountdown;

    protected float lastStressApplied;

    protected float lastCapacityProvided;

    public SequencedGearshiftBlockEntity.SequenceContext sequenceContext;

    public KineticBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.updateSpeed = true;
    }

    @Override
    public void initialize() {
        if (this.hasNetwork() && !this.f_58857_.isClientSide) {
            KineticNetwork network = this.getOrCreateNetwork();
            if (!network.initialized) {
                network.initFromTE(this.capacity, this.stress, this.networkSize);
            }
            network.addSilently(this, this.lastCapacityProvided, this.lastStressApplied);
        }
        super.initialize();
    }

    @Override
    public void tick() {
        if (!this.f_58857_.isClientSide && this.needsSpeedUpdate()) {
            this.attachKinetics();
        }
        super.tick();
        this.effects.tick();
        this.preventSpeedUpdate = 0;
        if (this.f_58857_.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.tickAudio());
        } else {
            if (this.validationCountdown-- <= 0) {
                this.validationCountdown = AllConfigs.server().kinetics.kineticValidationFrequency.get();
                this.validateKinetics();
            }
            if (this.getFlickerScore() > 0) {
                this.flickerTally = this.getFlickerScore() - 1;
            }
            if (this.networkDirty) {
                if (this.hasNetwork()) {
                    this.getOrCreateNetwork().updateNetwork();
                }
                this.networkDirty = false;
            }
        }
    }

    private void validateKinetics() {
        if (this.hasSource()) {
            if (!this.hasNetwork()) {
                this.removeSource();
            } else if (this.f_58857_.isLoaded(this.source)) {
                BlockEntity blockEntity = this.f_58857_.getBlockEntity(this.source);
                KineticBlockEntity sourceBE = blockEntity instanceof KineticBlockEntity ? (KineticBlockEntity) blockEntity : null;
                if (sourceBE == null || sourceBE.speed == 0.0F) {
                    this.removeSource();
                    this.detachKinetics();
                }
            }
        } else {
            if (this.speed != 0.0F && this.getGeneratedSpeed() == 0.0F) {
                this.speed = 0.0F;
            }
        }
    }

    public void updateFromNetwork(float maxStress, float currentStress, int networkSize) {
        this.networkDirty = false;
        this.capacity = maxStress;
        this.stress = currentStress;
        this.networkSize = networkSize;
        boolean overStressed = maxStress < currentStress && IRotate.StressImpact.isEnabled();
        this.m_6596_();
        if (overStressed != this.overStressed) {
            float prevSpeed = this.getSpeed();
            this.overStressed = overStressed;
            this.onSpeedChanged(prevSpeed);
            this.sendData();
        }
    }

    protected Block getStressConfigKey() {
        return this.m_58900_().m_60734_();
    }

    public float calculateStressApplied() {
        float impact = (float) BlockStressValues.getImpact(this.getStressConfigKey());
        this.lastStressApplied = impact;
        return impact;
    }

    public float calculateAddedStressCapacity() {
        float capacity = (float) BlockStressValues.getCapacity(this.getStressConfigKey());
        this.lastCapacityProvided = capacity;
        return capacity;
    }

    public void onSpeedChanged(float previousSpeed) {
        boolean fromOrToZero = previousSpeed == 0.0F != (this.getSpeed() == 0.0F);
        boolean directionSwap = !fromOrToZero && Math.signum(previousSpeed) != Math.signum(this.getSpeed());
        if (fromOrToZero || directionSwap) {
            this.flickerTally = this.getFlickerScore() + 5;
        }
        this.m_6596_();
    }

    @Override
    public void remove() {
        if (!this.f_58857_.isClientSide) {
            if (this.hasNetwork()) {
                this.getOrCreateNetwork().remove(this);
            }
            this.detachKinetics();
        }
        super.remove();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putFloat("Speed", this.speed);
        if (this.sequenceContext != null && (!clientPacket || this.syncSequenceContext())) {
            compound.put("Sequence", this.sequenceContext.serializeNBT());
        }
        if (this.needsSpeedUpdate()) {
            compound.putBoolean("NeedsSpeedUpdate", true);
        }
        if (this.hasSource()) {
            compound.put("Source", NbtUtils.writeBlockPos(this.source));
        }
        if (this.hasNetwork()) {
            CompoundTag networkTag = new CompoundTag();
            networkTag.putLong("Id", this.network);
            networkTag.putFloat("Stress", this.stress);
            networkTag.putFloat("Capacity", this.capacity);
            networkTag.putInt("Size", this.networkSize);
            if (this.lastStressApplied != 0.0F) {
                networkTag.putFloat("AddedStress", this.lastStressApplied);
            }
            if (this.lastCapacityProvided != 0.0F) {
                networkTag.putFloat("AddedCapacity", this.lastCapacityProvided);
            }
            compound.put("Network", networkTag);
        }
        super.write(compound, clientPacket);
    }

    public boolean needsSpeedUpdate() {
        return this.updateSpeed;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        boolean overStressedBefore = this.overStressed;
        this.clearKineticInformation();
        if (this.wasMoved) {
            super.read(compound, clientPacket);
        } else {
            this.speed = compound.getFloat("Speed");
            this.sequenceContext = SequencedGearshiftBlockEntity.SequenceContext.fromNBT(compound.getCompound("Sequence"));
            if (compound.contains("Source")) {
                this.source = NbtUtils.readBlockPos(compound.getCompound("Source"));
            }
            if (compound.contains("Network")) {
                CompoundTag networkTag = compound.getCompound("Network");
                this.network = networkTag.getLong("Id");
                this.stress = networkTag.getFloat("Stress");
                this.capacity = networkTag.getFloat("Capacity");
                this.networkSize = networkTag.getInt("Size");
                this.lastStressApplied = networkTag.getFloat("AddedStress");
                this.lastCapacityProvided = networkTag.getFloat("AddedCapacity");
                this.overStressed = this.capacity < this.stress && IRotate.StressImpact.isEnabled();
            }
            super.read(compound, clientPacket);
            if (clientPacket && overStressedBefore != this.overStressed && this.speed != 0.0F) {
                this.effects.triggerOverStressedEffect();
            }
            if (clientPacket) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate(this));
            }
        }
    }

    public float getGeneratedSpeed() {
        return 0.0F;
    }

    public boolean isSource() {
        return this.getGeneratedSpeed() != 0.0F;
    }

    public float getSpeed() {
        return this.overStressed ? 0.0F : this.getTheoreticalSpeed();
    }

    public float getTheoreticalSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean hasSource() {
        return this.source != null;
    }

    public void setSource(BlockPos source) {
        this.source = source;
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            if (this.f_58857_.getBlockEntity(source) instanceof KineticBlockEntity sourceBE) {
                this.setNetwork(sourceBE.network);
                this.copySequenceContextFrom(sourceBE);
            } else {
                this.removeSource();
            }
        }
    }

    protected void copySequenceContextFrom(KineticBlockEntity sourceBE) {
        this.sequenceContext = sourceBE.sequenceContext;
    }

    public void removeSource() {
        float prevSpeed = this.getSpeed();
        this.speed = 0.0F;
        this.source = null;
        this.setNetwork(null);
        this.sequenceContext = null;
        this.onSpeedChanged(prevSpeed);
    }

    public void setNetwork(@Nullable Long networkIn) {
        if (this.network != networkIn) {
            if (this.network != null) {
                this.getOrCreateNetwork().remove(this);
            }
            this.network = networkIn;
            this.m_6596_();
            if (networkIn != null) {
                this.network = networkIn;
                KineticNetwork network = this.getOrCreateNetwork();
                network.initialized = true;
                network.add(this);
            }
        }
    }

    public KineticNetwork getOrCreateNetwork() {
        return Create.TORQUE_PROPAGATOR.getOrCreateNetworkFor(this);
    }

    public boolean hasNetwork() {
        return this.network != null;
    }

    public void attachKinetics() {
        this.updateSpeed = false;
        RotationPropagator.handleAdded(this.f_58857_, this.f_58858_, this);
    }

    public void detachKinetics() {
        RotationPropagator.handleRemoved(this.f_58857_, this.f_58858_, this);
    }

    public boolean isSpeedRequirementFulfilled() {
        BlockState state = this.m_58900_();
        if (!(this.m_58900_().m_60734_() instanceof IRotate)) {
            return true;
        } else {
            IRotate def = (IRotate) state.m_60734_();
            IRotate.SpeedLevel minimumRequiredSpeedLevel = def.getMinimumRequiredSpeedLevel();
            return Math.abs(this.getSpeed()) >= minimumRequiredSpeedLevel.getSpeedValue();
        }
    }

    public static void switchToBlockState(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            BlockState currentState = world.getBlockState(pos);
            boolean isKinetic = blockEntity instanceof KineticBlockEntity;
            if (currentState != state) {
                if (blockEntity != null && isKinetic) {
                    KineticBlockEntity kineticBlockEntity = (KineticBlockEntity) blockEntity;
                    if (state.m_60734_() instanceof KineticBlock && !((KineticBlock) state.m_60734_()).areStatesKineticallyEquivalent(currentState, state)) {
                        if (kineticBlockEntity.hasNetwork()) {
                            kineticBlockEntity.getOrCreateNetwork().remove(kineticBlockEntity);
                        }
                        kineticBlockEntity.detachKinetics();
                        kineticBlockEntity.removeSource();
                    }
                    world.setBlock(pos, state, 3);
                } else {
                    world.setBlock(pos, state, 3);
                }
            }
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean notFastEnough = !this.isSpeedRequirementFulfilled() && this.getSpeed() != 0.0F;
        if (this.overStressed && AllConfigs.client().enableOverstressedTooltip.get()) {
            Lang.translate("gui.stressometer.overstressed").style(ChatFormatting.GOLD).forGoggles(tooltip);
            Component hint = Lang.translateDirect("gui.contraptions.network_overstressed");
            List<Component> cutString = TooltipHelper.cutTextComponent(hint, TooltipHelper.Palette.GRAY_AND_WHITE);
            for (int i = 0; i < cutString.size(); i++) {
                Lang.builder().add(((Component) cutString.get(i)).copy()).forGoggles(tooltip);
            }
            return true;
        } else if (!notFastEnough) {
            return false;
        } else {
            Lang.translate("tooltip.speedRequirement").style(ChatFormatting.GOLD).forGoggles(tooltip);
            MutableComponent hint = Lang.translateDirect("gui.contraptions.not_fast_enough", I18n.get(this.m_58900_().m_60734_().getDescriptionId()));
            List<Component> cutString = TooltipHelper.cutTextComponent(hint, TooltipHelper.Palette.GRAY_AND_WHITE);
            for (int i = 0; i < cutString.size(); i++) {
                Lang.builder().add(((Component) cutString.get(i)).copy()).forGoggles(tooltip);
            }
            return true;
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = false;
        if (!IRotate.StressImpact.isEnabled()) {
            return added;
        } else {
            float stressAtBase = this.calculateStressApplied();
            if (Mth.equal(stressAtBase, 0.0F)) {
                return added;
            } else {
                Lang.translate("gui.goggles.kinetic_stats").forGoggles(tooltip);
                this.addStressImpactStats(tooltip, stressAtBase);
                return true;
            }
        }
    }

    protected void addStressImpactStats(List<Component> tooltip, float stressAtBase) {
        Lang.translate("tooltip.stressImpact").style(ChatFormatting.GRAY).forGoggles(tooltip);
        float stressTotal = stressAtBase * Math.abs(this.getTheoreticalSpeed());
        Lang.number((double) stressTotal).translate("generic.unit.stress").style(ChatFormatting.AQUA).space().add(Lang.translate("gui.goggles.at_current_speed").style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
    }

    public void clearKineticInformation() {
        this.speed = 0.0F;
        this.source = null;
        this.network = null;
        this.overStressed = false;
        this.stress = 0.0F;
        this.capacity = 0.0F;
        this.lastStressApplied = 0.0F;
        this.lastCapacityProvided = 0.0F;
    }

    public void warnOfMovement() {
        this.wasMoved = true;
    }

    public int getFlickerScore() {
        return this.flickerTally;
    }

    public static float convertToDirection(float axisSpeed, Direction d) {
        return d.getAxisDirection() == Direction.AxisDirection.POSITIVE ? axisSpeed : -axisSpeed;
    }

    public static float convertToLinear(float speed) {
        return speed / 512.0F;
    }

    public static float convertToAngular(float speed) {
        return speed * 3.0F / 10.0F;
    }

    public boolean isOverStressed() {
        return this.overStressed;
    }

    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        return 0.0F;
    }

    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if (!this.canPropagateDiagonally(block, state)) {
            return neighbours;
        } else {
            Direction.Axis axis = block.getRotationAxis(state);
            BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).forEach(offset -> {
                if (axis.choose(offset.m_123341_(), offset.m_123342_(), offset.m_123343_()) == 0) {
                    if (offset.m_123331_(BlockPos.ZERO) == 2.0) {
                        neighbours.add(this.f_58858_.offset(offset));
                    }
                }
            });
            return neighbours;
        }
    }

    public boolean isCustomConnection(KineticBlockEntity other, BlockState state, BlockState otherState) {
        return false;
    }

    protected boolean canPropagateDiagonally(IRotate block, BlockState state) {
        return ICogWheel.isSmallCog(state);
    }

    public void requestModelDataUpdate() {
        super.requestModelDataUpdate();
        if (!this.f_58859_) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate(this));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        float componentSpeed = Math.abs(this.getSpeed());
        if (componentSpeed != 0.0F) {
            float pitch = Mth.clamp(componentSpeed / 256.0F + 0.45F, 0.85F, 1.0F);
            if (this.isNoisy()) {
                SoundScapes.play(SoundScapes.AmbienceGroup.KINETIC, this.f_58858_, pitch);
            }
            Block block = this.m_58900_().m_60734_();
            if (ICogWheel.isSmallCog(block) || ICogWheel.isLargeCog(block) || block instanceof GearboxBlock) {
                SoundScapes.play(SoundScapes.AmbienceGroup.COG, this.f_58858_, pitch);
            }
        }
    }

    protected boolean isNoisy() {
        return true;
    }

    public int getRotationAngleOffset(Direction.Axis axis) {
        return 0;
    }

    protected boolean syncSequenceContext() {
        return false;
    }
}