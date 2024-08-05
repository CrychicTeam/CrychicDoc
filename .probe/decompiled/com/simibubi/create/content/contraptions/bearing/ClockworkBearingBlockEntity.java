package com.simibubi.create.content.contraptions.bearing;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.apache.commons.lang3.tuple.Pair;

public class ClockworkBearingBlockEntity extends KineticBlockEntity implements IBearingBlockEntity, IDisplayAssemblyExceptions {

    protected ControlledContraptionEntity hourHand;

    protected ControlledContraptionEntity minuteHand;

    protected float hourAngle;

    protected float minuteAngle;

    protected float clientHourAngleDiff;

    protected float clientMinuteAngleDiff;

    protected boolean running;

    protected boolean assembleNextTick;

    protected AssemblyException lastException;

    protected ScrollOptionBehaviour<ClockworkBearingBlockEntity.ClockHands> operationMode;

    private float prevForcedAngle;

    public ClockworkBearingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(3);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.operationMode = new ScrollOptionBehaviour(ClockworkBearingBlockEntity.ClockHands.class, Lang.translateDirect("contraptions.clockwork.clock_hands"), this, this.getMovementModeSlot());
        behaviours.add(this.operationMode);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.CLOCKWORK_BEARING });
    }

    @Override
    public boolean isWoodenTop() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_58857_.isClientSide) {
            this.prevForcedAngle = this.hourAngle;
            this.clientMinuteAngleDiff /= 2.0F;
            this.clientHourAngleDiff /= 2.0F;
        }
        if (!this.f_58857_.isClientSide && this.assembleNextTick) {
            this.assembleNextTick = false;
            if (!this.running) {
                this.assemble();
            } else {
                boolean canDisassemble = true;
                if (this.speed == 0.0F && (canDisassemble || this.hourHand == null || this.hourHand.getContraption().getBlocks().isEmpty())) {
                    if (this.hourHand != null) {
                        this.hourHand.getContraption().stop(this.f_58857_);
                    }
                    if (this.minuteHand != null) {
                        this.minuteHand.getContraption().stop(this.f_58857_);
                    }
                    this.disassemble();
                }
            }
        } else if (this.running) {
            if (this.hourHand == null || !this.hourHand.isStalled()) {
                float newAngle = this.hourAngle + this.getHourArmSpeed();
                this.hourAngle = newAngle % 360.0F;
            }
            if (this.minuteHand == null || !this.minuteHand.isStalled()) {
                float newAngle = this.minuteAngle + this.getMinuteArmSpeed();
                this.minuteAngle = newAngle % 360.0F;
            }
            this.applyRotations();
        }
    }

    @Override
    public AssemblyException getLastAssemblyException() {
        return this.lastException;
    }

    protected void applyRotations() {
        BlockState blockState = this.m_58900_();
        Direction.Axis axis = Direction.Axis.X;
        if (blockState.m_61138_(BlockStateProperties.FACING)) {
            axis = ((Direction) blockState.m_61143_(BlockStateProperties.FACING)).getAxis();
        }
        if (this.hourHand != null) {
            this.hourHand.setAngle(this.hourAngle);
            this.hourHand.setRotationAxis(axis);
        }
        if (this.minuteHand != null) {
            this.minuteHand.setAngle(this.minuteAngle);
            this.minuteHand.setRotationAxis(axis);
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (this.hourHand != null && !this.f_58857_.isClientSide) {
            this.sendData();
        }
    }

    public float getHourArmSpeed() {
        float speed = this.getAngularSpeed() / 2.0F;
        if (speed != 0.0F) {
            ClockworkBearingBlockEntity.ClockHands mode = ClockworkBearingBlockEntity.ClockHands.values()[this.operationMode.getValue()];
            float hourTarget = mode == ClockworkBearingBlockEntity.ClockHands.HOUR_FIRST ? this.getHourTarget(false) : (mode == ClockworkBearingBlockEntity.ClockHands.MINUTE_FIRST ? this.getMinuteTarget() : this.getHourTarget(true));
            float shortestAngleDiff = AngleHelper.getShortestAngleDiff((double) this.hourAngle, (double) hourTarget);
            if (shortestAngleDiff < 0.0F) {
                speed = Math.max(speed, shortestAngleDiff);
            } else {
                speed = Math.min(-speed, shortestAngleDiff);
            }
        }
        return speed + this.clientHourAngleDiff / 3.0F;
    }

    public float getMinuteArmSpeed() {
        float speed = this.getAngularSpeed();
        if (speed != 0.0F) {
            ClockworkBearingBlockEntity.ClockHands mode = ClockworkBearingBlockEntity.ClockHands.values()[this.operationMode.getValue()];
            float minuteTarget = mode == ClockworkBearingBlockEntity.ClockHands.MINUTE_FIRST ? this.getHourTarget(false) : this.getMinuteTarget();
            float shortestAngleDiff = AngleHelper.getShortestAngleDiff((double) this.minuteAngle, (double) minuteTarget);
            if (shortestAngleDiff < 0.0F) {
                speed = Math.max(speed, shortestAngleDiff);
            } else {
                speed = Math.min(-speed, shortestAngleDiff);
            }
        }
        return speed + this.clientMinuteAngleDiff / 3.0F;
    }

    protected float getHourTarget(boolean cycle24) {
        boolean isNatural = this.f_58857_.dimensionType().natural();
        int dayTime = (int) (this.f_58857_.getDayTime() * (long) (isNatural ? 1 : 24) % 24000L);
        int hours = (dayTime / 1000 + 6) % 24;
        int offset = ((Direction) this.m_58900_().m_61143_(ClockworkBearingBlock.FACING)).getAxisDirection().getStep();
        return (float) (offset * -360) / (cycle24 ? 24.0F : 12.0F) * (float) (hours % (cycle24 ? 24 : 12));
    }

    protected float getMinuteTarget() {
        boolean isNatural = this.f_58857_.dimensionType().natural();
        int dayTime = (int) (this.f_58857_.getDayTime() * (long) (isNatural ? 1 : 24) % 24000L);
        int minutes = dayTime % 1000 * 60 / 1000;
        int offset = ((Direction) this.m_58900_().m_61143_(ClockworkBearingBlock.FACING)).getAxisDirection().getStep();
        return (float) (offset * -360) / 60.0F * (float) minutes;
    }

    public float getAngularSpeed() {
        float speed = -Math.abs(this.getSpeed() * 3.0F / 10.0F);
        if (this.f_58857_.isClientSide) {
            speed *= ServerSpeedProvider.get();
        }
        return speed;
    }

    public void assemble() {
        if (this.f_58857_.getBlockState(this.f_58858_).m_60734_() instanceof ClockworkBearingBlock) {
            Direction direction = (Direction) this.m_58900_().m_61143_(BlockStateProperties.FACING);
            Pair<ClockworkContraption, ClockworkContraption> contraption;
            try {
                contraption = ClockworkContraption.assembleClockworkAt(this.f_58857_, this.f_58858_, direction);
                this.lastException = null;
            } catch (AssemblyException var4) {
                this.lastException = var4;
                this.sendData();
                return;
            }
            if (contraption != null) {
                if (contraption.getLeft() != null) {
                    if (!((ClockworkContraption) contraption.getLeft()).getBlocks().isEmpty()) {
                        BlockPos anchor = this.f_58858_.relative(direction);
                        ((ClockworkContraption) contraption.getLeft()).removeBlocksFromWorld(this.f_58857_, BlockPos.ZERO);
                        this.hourHand = ControlledContraptionEntity.create(this.f_58857_, this, (Contraption) contraption.getLeft());
                        this.hourHand.setPos((double) anchor.m_123341_(), (double) anchor.m_123342_(), (double) anchor.m_123343_());
                        this.hourHand.setRotationAxis(direction.getAxis());
                        this.f_58857_.m_7967_(this.hourHand);
                        if (((ClockworkContraption) contraption.getLeft()).containsBlockBreakers()) {
                            this.award(AllAdvancements.CONTRAPTION_ACTORS);
                        }
                        if (contraption.getRight() != null) {
                            anchor = this.f_58858_.relative(direction, ((ClockworkContraption) contraption.getRight()).offset + 1);
                            ((ClockworkContraption) contraption.getRight()).removeBlocksFromWorld(this.f_58857_, BlockPos.ZERO);
                            this.minuteHand = ControlledContraptionEntity.create(this.f_58857_, this, (Contraption) contraption.getRight());
                            this.minuteHand.setPos((double) anchor.m_123341_(), (double) anchor.m_123342_(), (double) anchor.m_123343_());
                            this.minuteHand.setRotationAxis(direction.getAxis());
                            this.f_58857_.m_7967_(this.minuteHand);
                            if (((ClockworkContraption) contraption.getRight()).containsBlockBreakers()) {
                                this.award(AllAdvancements.CONTRAPTION_ACTORS);
                            }
                        }
                        this.award(AllAdvancements.CLOCKWORK_BEARING);
                        this.running = true;
                        this.hourAngle = 0.0F;
                        this.minuteAngle = 0.0F;
                        this.sendData();
                    }
                }
            }
        }
    }

    public void disassemble() {
        if (this.running || this.hourHand != null || this.minuteHand != null) {
            this.hourAngle = 0.0F;
            this.minuteAngle = 0.0F;
            this.applyRotations();
            if (this.hourHand != null) {
                this.hourHand.disassemble();
            }
            if (this.minuteHand != null) {
                this.minuteHand.disassemble();
            }
            this.hourHand = null;
            this.minuteHand = null;
            this.running = false;
            this.sendData();
        }
    }

    @Override
    public void attach(ControlledContraptionEntity contraption) {
        if (contraption.getContraption() instanceof ClockworkContraption) {
            ClockworkContraption cc = (ClockworkContraption) contraption.getContraption();
            this.m_6596_();
            Direction facing = (Direction) this.m_58900_().m_61143_(BlockStateProperties.FACING);
            BlockPos anchor = this.f_58858_.relative(facing, cc.offset + 1);
            if (cc.handType == ClockworkContraption.HandType.HOUR) {
                this.hourHand = contraption;
                this.hourHand.setPos((double) anchor.m_123341_(), (double) anchor.m_123342_(), (double) anchor.m_123343_());
            } else {
                this.minuteHand = contraption;
                this.minuteHand.setPos((double) anchor.m_123341_(), (double) anchor.m_123342_(), (double) anchor.m_123343_());
            }
            if (!this.f_58857_.isClientSide) {
                this.running = true;
                this.sendData();
            }
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", this.running);
        compound.putFloat("HourAngle", this.hourAngle);
        compound.putFloat("MinuteAngle", this.minuteAngle);
        AssemblyException.write(compound, this.lastException);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        float hourAngleBefore = this.hourAngle;
        float minuteAngleBefore = this.minuteAngle;
        this.running = compound.getBoolean("Running");
        this.hourAngle = compound.getFloat("HourAngle");
        this.minuteAngle = compound.getFloat("MinuteAngle");
        this.lastException = AssemblyException.read(compound);
        super.read(compound, clientPacket);
        if (clientPacket) {
            if (this.running) {
                this.clientHourAngleDiff = AngleHelper.getShortestAngleDiff((double) hourAngleBefore, (double) this.hourAngle);
                this.clientMinuteAngleDiff = AngleHelper.getShortestAngleDiff((double) minuteAngleBefore, (double) this.minuteAngle);
                this.hourAngle = hourAngleBefore;
                this.minuteAngle = minuteAngleBefore;
            } else {
                this.hourHand = null;
                this.minuteHand = null;
            }
        }
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        this.assembleNextTick = true;
    }

    @Override
    public boolean isValid() {
        return !this.m_58901_();
    }

    @Override
    public float getInterpolatedAngle(float partialTicks) {
        if (this.isVirtual()) {
            return Mth.lerp(partialTicks, this.prevForcedAngle, this.hourAngle);
        } else {
            if (this.hourHand == null || this.hourHand.isStalled()) {
                partialTicks = 0.0F;
            }
            return Mth.lerp(partialTicks, this.hourAngle, this.hourAngle + this.getHourArmSpeed());
        }
    }

    @Override
    public void onStall() {
        if (!this.f_58857_.isClientSide) {
            this.sendData();
        }
    }

    @Override
    public void remove() {
        if (!this.f_58857_.isClientSide) {
            this.disassemble();
        }
        super.remove();
    }

    @Override
    public boolean isAttachedTo(AbstractContraptionEntity contraption) {
        if (!(contraption.getContraption() instanceof ClockworkContraption)) {
            return false;
        } else {
            ClockworkContraption cc = (ClockworkContraption) contraption.getContraption();
            return cc.handType == ClockworkContraption.HandType.HOUR ? this.hourHand == contraption : this.minuteHand == contraption;
        }
    }

    public boolean isRunning() {
        return this.running;
    }

    @Override
    public BlockPos getBlockPosition() {
        return this.f_58858_;
    }

    @Override
    public void setAngle(float forcedAngle) {
        this.hourAngle = forcedAngle;
    }

    static enum ClockHands implements INamedIconOptions {

        HOUR_FIRST(AllIcons.I_HOUR_HAND_FIRST), MINUTE_FIRST(AllIcons.I_MINUTE_HAND_FIRST), HOUR_FIRST_24(AllIcons.I_HOUR_HAND_FIRST_24);

        private String translationKey;

        private AllIcons icon;

        private ClockHands(AllIcons icon) {
            this.icon = icon;
            this.translationKey = "contraptions.clockwork." + Lang.asId(this.name());
        }

        @Override
        public AllIcons getIcon() {
            return this.icon;
        }

        @Override
        public String getTranslationKey() {
            return this.translationKey;
        }
    }
}