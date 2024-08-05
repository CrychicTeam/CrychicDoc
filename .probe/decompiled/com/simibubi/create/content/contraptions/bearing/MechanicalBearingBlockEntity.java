package com.simibubi.create.content.contraptions.bearing;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.IControlContraption;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencerInstructions;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MechanicalBearingBlockEntity extends GeneratingKineticBlockEntity implements IBearingBlockEntity, IDisplayAssemblyExceptions {

    protected ScrollOptionBehaviour<IControlContraption.RotationMode> movementMode;

    protected ControlledContraptionEntity movedContraption;

    protected float angle;

    protected boolean running;

    protected boolean assembleNextTick;

    protected float clientAngleDiff;

    protected AssemblyException lastException;

    protected double sequencedAngleLimit;

    private float prevAngle;

    public MechanicalBearingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(3);
        this.sequencedAngleLimit = -1.0;
    }

    @Override
    public boolean isWoodenTop() {
        return false;
    }

    @Override
    protected boolean syncSequenceContext() {
        return true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.movementMode = new ScrollOptionBehaviour(IControlContraption.RotationMode.class, Lang.translateDirect("contraptions.movement_mode"), this, this.getMovementModeSlot());
        behaviours.add(this.movementMode);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.CONTRAPTION_ACTORS });
    }

    @Override
    public void remove() {
        if (!this.f_58857_.isClientSide) {
            this.disassemble();
        }
        super.remove();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", this.running);
        compound.putFloat("Angle", this.angle);
        if (this.sequencedAngleLimit >= 0.0) {
            compound.putDouble("SequencedAngleLimit", this.sequencedAngleLimit);
        }
        AssemblyException.write(compound, this.lastException);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        if (this.wasMoved) {
            super.read(compound, clientPacket);
        } else {
            float angleBefore = this.angle;
            this.running = compound.getBoolean("Running");
            this.angle = compound.getFloat("Angle");
            this.sequencedAngleLimit = compound.contains("SequencedAngleLimit") ? compound.getDouble("SequencedAngleLimit") : -1.0;
            this.lastException = AssemblyException.read(compound);
            super.read(compound, clientPacket);
            if (clientPacket) {
                if (this.running) {
                    if (this.movedContraption == null || !this.movedContraption.isStalled()) {
                        this.clientAngleDiff = AngleHelper.getShortestAngleDiff((double) angleBefore, (double) this.angle);
                        this.angle = angleBefore;
                    }
                } else {
                    this.movedContraption = null;
                }
            }
        }
    }

    @Override
    public float getInterpolatedAngle(float partialTicks) {
        if (this.isVirtual()) {
            return Mth.lerp(partialTicks + 0.5F, this.prevAngle, this.angle);
        } else {
            if (this.movedContraption == null || this.movedContraption.isStalled() || !this.running) {
                partialTicks = 0.0F;
            }
            float angularSpeed = this.getAngularSpeed();
            if (this.sequencedAngleLimit >= 0.0) {
                angularSpeed = (float) Mth.clamp((double) angularSpeed, -this.sequencedAngleLimit, this.sequencedAngleLimit);
            }
            return Mth.lerp(partialTicks, this.angle, this.angle + angularSpeed);
        }
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        this.assembleNextTick = true;
        this.sequencedAngleLimit = -1.0;
        if (this.movedContraption != null && Math.signum(prevSpeed) != Math.signum(this.getSpeed()) && prevSpeed != 0.0F) {
            if (!this.movedContraption.isStalled()) {
                this.angle = (float) Math.round(this.angle);
                this.applyRotation();
            }
            this.movedContraption.getContraption().stop(this.f_58857_);
        }
        if (!this.isWindmill() && this.sequenceContext != null && this.sequenceContext.instruction() == SequencerInstructions.TURN_ANGLE) {
            this.sequencedAngleLimit = this.sequenceContext.getEffectiveValue((double) this.getTheoreticalSpeed());
        }
    }

    public float getAngularSpeed() {
        float speed = convertToAngular(this.isWindmill() ? this.getGeneratedSpeed() : this.getSpeed());
        if (this.getSpeed() == 0.0F) {
            speed = 0.0F;
        }
        if (this.f_58857_.isClientSide) {
            speed *= ServerSpeedProvider.get();
            speed += this.clientAngleDiff / 3.0F;
        }
        return speed;
    }

    @Override
    public AssemblyException getLastAssemblyException() {
        return this.lastException;
    }

    protected boolean isWindmill() {
        return false;
    }

    @Override
    public BlockPos getBlockPosition() {
        return this.f_58858_;
    }

    public void assemble() {
        if (this.f_58857_.getBlockState(this.f_58858_).m_60734_() instanceof BearingBlock) {
            Direction direction = (Direction) this.m_58900_().m_61143_(BearingBlock.FACING);
            BearingContraption contraption = new BearingContraption(this.isWindmill(), direction);
            try {
                if (!contraption.assemble(this.f_58857_, this.f_58858_)) {
                    return;
                }
                this.lastException = null;
            } catch (AssemblyException var4) {
                this.lastException = var4;
                this.sendData();
                return;
            }
            if (this.isWindmill()) {
                this.award(AllAdvancements.WINDMILL);
            }
            if (contraption.getSailBlocks() >= 128) {
                this.award(AllAdvancements.WINDMILL_MAXED);
            }
            contraption.removeBlocksFromWorld(this.f_58857_, BlockPos.ZERO);
            this.movedContraption = ControlledContraptionEntity.create(this.f_58857_, this, contraption);
            BlockPos anchor = this.f_58858_.relative(direction);
            this.movedContraption.setPos((double) anchor.m_123341_(), (double) anchor.m_123342_(), (double) anchor.m_123343_());
            this.movedContraption.setRotationAxis(direction.getAxis());
            this.f_58857_.m_7967_(this.movedContraption);
            AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(this.f_58857_, this.f_58858_);
            if (contraption.containsBlockBreakers()) {
                this.award(AllAdvancements.CONTRAPTION_ACTORS);
            }
            this.running = true;
            this.angle = 0.0F;
            this.sendData();
            this.updateGeneratedRotation();
        }
    }

    public void disassemble() {
        if (this.running || this.movedContraption != null) {
            this.angle = 0.0F;
            this.sequencedAngleLimit = -1.0;
            if (this.isWindmill()) {
                this.applyRotation();
            }
            if (this.movedContraption != null) {
                this.movedContraption.disassemble();
                AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(this.f_58857_, this.f_58858_);
            }
            this.movedContraption = null;
            this.running = false;
            this.updateGeneratedRotation();
            this.assembleNextTick = false;
            this.sendData();
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.prevAngle = this.angle;
        if (this.f_58857_.isClientSide) {
            this.clientAngleDiff /= 2.0F;
        }
        if (!this.f_58857_.isClientSide && this.assembleNextTick) {
            this.assembleNextTick = false;
            if (!this.running) {
                if (this.speed == 0.0F && !this.isWindmill()) {
                    return;
                }
                this.assemble();
            } else {
                boolean canDisassemble = this.movementMode.get() == IControlContraption.RotationMode.ROTATE_PLACE || this.isNearInitialAngle() && this.movementMode.get() == IControlContraption.RotationMode.ROTATE_PLACE_RETURNED;
                if (this.speed == 0.0F && (canDisassemble || this.movedContraption == null || this.movedContraption.getContraption().getBlocks().isEmpty())) {
                    if (this.movedContraption != null) {
                        this.movedContraption.getContraption().stop(this.f_58857_);
                    }
                    this.disassemble();
                    return;
                }
            }
        }
        if (this.running) {
            if (this.movedContraption == null || !this.movedContraption.isStalled()) {
                float angularSpeed = this.getAngularSpeed();
                if (this.sequencedAngleLimit >= 0.0) {
                    angularSpeed = (float) Mth.clamp((double) angularSpeed, -this.sequencedAngleLimit, this.sequencedAngleLimit);
                    this.sequencedAngleLimit = Math.max(0.0, this.sequencedAngleLimit - (double) Math.abs(angularSpeed));
                }
                float newAngle = this.angle + angularSpeed;
                this.angle = newAngle % 360.0F;
            }
            this.applyRotation();
        }
    }

    public boolean isNearInitialAngle() {
        return (double) Math.abs(this.angle) < 22.5 || (double) Math.abs(this.angle) > 337.5;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (this.movedContraption != null && !this.f_58857_.isClientSide) {
            this.sendData();
        }
    }

    protected void applyRotation() {
        if (this.movedContraption != null) {
            this.movedContraption.setAngle(this.angle);
            BlockState blockState = this.m_58900_();
            if (blockState.m_61138_(BlockStateProperties.FACING)) {
                this.movedContraption.setRotationAxis(((Direction) blockState.m_61143_(BlockStateProperties.FACING)).getAxis());
            }
        }
    }

    @Override
    public void attach(ControlledContraptionEntity contraption) {
        BlockState blockState = this.m_58900_();
        if (contraption.getContraption() instanceof BearingContraption) {
            if (blockState.m_61138_(BearingBlock.FACING)) {
                this.movedContraption = contraption;
                this.m_6596_();
                BlockPos anchor = this.f_58858_.relative((Direction) blockState.m_61143_(BearingBlock.FACING));
                this.movedContraption.setPos((double) anchor.m_123341_(), (double) anchor.m_123342_(), (double) anchor.m_123343_());
                if (!this.f_58857_.isClientSide) {
                    this.running = true;
                    this.sendData();
                }
            }
        }
    }

    @Override
    public void onStall() {
        if (!this.f_58857_.isClientSide) {
            this.sendData();
        }
    }

    @Override
    public boolean isValid() {
        return !this.m_58901_();
    }

    @Override
    public boolean isAttachedTo(AbstractContraptionEntity contraption) {
        return this.movedContraption == contraption;
    }

    public boolean isRunning() {
        return this.running;
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (super.addToTooltip(tooltip, isPlayerSneaking)) {
            return true;
        } else if (isPlayerSneaking) {
            return false;
        } else if (!this.isWindmill() && this.getSpeed() == 0.0F) {
            return false;
        } else if (this.running) {
            return false;
        } else {
            BlockState state = this.m_58900_();
            if (!(state.m_60734_() instanceof BearingBlock)) {
                return false;
            } else {
                BlockState attachedState = this.f_58857_.getBlockState(this.f_58858_.relative((Direction) state.m_61143_(BearingBlock.FACING)));
                if (attachedState.m_247087_()) {
                    return false;
                } else {
                    TooltipHelper.addHint(tooltip, "hint.empty_bearing");
                    return true;
                }
            }
        }
    }

    @Override
    public void setAngle(float forcedAngle) {
        this.angle = forcedAngle;
    }

    public ControlledContraptionEntity getMovedContraption() {
        return this.movedContraption;
    }
}