package com.simibubi.create.content.contraptions.piston;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionCollider;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.IControlContraption;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencerInstructions;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class LinearActuatorBlockEntity extends KineticBlockEntity implements IControlContraption, IDisplayAssemblyExceptions {

    public float offset;

    public boolean running;

    public boolean assembleNextTick;

    public boolean needsContraption;

    public AbstractContraptionEntity movedContraption;

    protected boolean forceMove;

    protected ScrollOptionBehaviour<IControlContraption.MovementMode> movementMode;

    protected boolean waitingForSpeedChange;

    protected AssemblyException lastException;

    protected double sequencedOffsetLimit;

    protected float clientOffsetDiff;

    public LinearActuatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.setLazyTickRate(3);
        this.forceMove = true;
        this.needsContraption = true;
        this.sequencedOffsetLimit = -1.0;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.movementMode = new ScrollOptionBehaviour(IControlContraption.MovementMode.class, Lang.translateDirect("contraptions.movement_mode"), this, this.getMovementModeSlot());
        this.movementMode.withCallback(t -> this.waitingForSpeedChange = false);
        behaviours.add(this.movementMode);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.CONTRAPTION_ACTORS });
    }

    @Override
    protected boolean syncSequenceContext() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.movedContraption != null && !this.movedContraption.m_6084_()) {
            this.movedContraption = null;
        }
        if (!this.isPassive()) {
            if (this.f_58857_.isClientSide) {
                this.clientOffsetDiff *= 0.75F;
            }
            if (this.waitingForSpeedChange) {
                if (this.movedContraption != null) {
                    if (this.f_58857_.isClientSide) {
                        float syncSpeed = this.clientOffsetDiff / 2.0F;
                        this.offset += syncSpeed;
                        this.movedContraption.setContraptionMotion(this.toMotionVector(syncSpeed));
                        return;
                    }
                    this.movedContraption.setContraptionMotion(Vec3.ZERO);
                }
            } else if (!this.f_58857_.isClientSide && this.assembleNextTick) {
                this.assembleNextTick = false;
                if (this.running) {
                    if (this.getSpeed() == 0.0F) {
                        this.tryDisassemble();
                    } else {
                        this.sendData();
                    }
                } else {
                    if (this.getSpeed() != 0.0F) {
                        try {
                            this.assemble();
                            this.lastException = null;
                        } catch (AssemblyException var6) {
                            this.lastException = var6;
                        }
                    }
                    this.sendData();
                }
            } else if (this.running) {
                boolean contraptionPresent = this.movedContraption != null;
                if (!this.needsContraption || contraptionPresent) {
                    float movementSpeed = this.getMovementSpeed();
                    boolean locked = false;
                    if (this.sequencedOffsetLimit > 0.0) {
                        this.sequencedOffsetLimit = Math.max(0.0, this.sequencedOffsetLimit - (double) Math.abs(movementSpeed));
                        locked = this.sequencedOffsetLimit == 0.0;
                    }
                    float newOffset = this.offset + movementSpeed;
                    if ((int) newOffset != (int) this.offset) {
                        this.visitNewPosition();
                    }
                    if (locked) {
                        this.forceMove = true;
                        this.resetContraptionToOffset();
                        this.sendData();
                    }
                    if (contraptionPresent && this.moveAndCollideContraption()) {
                        this.movedContraption.setContraptionMotion(Vec3.ZERO);
                        this.offset = (float) this.getGridOffset(this.offset);
                        this.resetContraptionToOffset();
                        this.collided();
                    } else {
                        if (!contraptionPresent || !this.movedContraption.isStalled()) {
                            this.offset = newOffset;
                        }
                        int extensionRange = this.getExtensionRange();
                        if (this.offset <= 0.0F || this.offset >= (float) extensionRange) {
                            this.offset = this.offset <= 0.0F ? 0.0F : (float) extensionRange;
                            if (!this.f_58857_.isClientSide) {
                                this.moveAndCollideContraption();
                                this.resetContraptionToOffset();
                                this.tryDisassemble();
                                if (this.waitingForSpeedChange) {
                                    this.forceMove = true;
                                    this.sendData();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean isPassive() {
        return false;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (this.movedContraption != null && !this.f_58857_.isClientSide) {
            this.sendData();
        }
    }

    protected int getGridOffset(float offset) {
        return Mth.clamp((int) (offset + 0.5F), 0, this.getExtensionRange());
    }

    public float getInterpolatedOffset(float partialTicks) {
        return Mth.clamp(this.offset + (partialTicks - 0.5F) * this.getMovementSpeed(), 0.0F, (float) this.getExtensionRange());
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        this.sequencedOffsetLimit = -1.0;
        if (!this.isPassive()) {
            this.assembleNextTick = true;
            this.waitingForSpeedChange = false;
            if (this.movedContraption != null && Math.signum(prevSpeed) != Math.signum(this.getSpeed()) && prevSpeed != 0.0F) {
                if (!this.movedContraption.isStalled()) {
                    this.offset = (float) (Math.round(this.offset * 16.0F) / 16);
                    this.resetContraptionToOffset();
                }
                this.movedContraption.getContraption().stop(this.f_58857_);
            }
            if (this.sequenceContext != null && this.sequenceContext.instruction() == SequencerInstructions.TURN_DISTANCE) {
                this.sequencedOffsetLimit = this.sequenceContext.getEffectiveValue((double) this.getTheoreticalSpeed());
            }
        }
    }

    @Override
    public void remove() {
        this.f_58859_ = true;
        if (!this.f_58857_.isClientSide) {
            this.disassemble();
        }
        super.remove();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", this.running);
        compound.putBoolean("Waiting", this.waitingForSpeedChange);
        compound.putFloat("Offset", this.offset);
        if (this.sequencedOffsetLimit >= 0.0) {
            compound.putDouble("SequencedOffsetLimit", this.sequencedOffsetLimit);
        }
        AssemblyException.write(compound, this.lastException);
        super.write(compound, clientPacket);
        if (clientPacket && this.forceMove) {
            compound.putBoolean("ForceMovement", this.forceMove);
            this.forceMove = false;
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        boolean forceMovement = compound.contains("ForceMovement");
        float offsetBefore = this.offset;
        this.running = compound.getBoolean("Running");
        this.waitingForSpeedChange = compound.getBoolean("Waiting");
        this.offset = compound.getFloat("Offset");
        this.sequencedOffsetLimit = compound.contains("SequencedOffsetLimit") ? compound.getDouble("SequencedOffsetLimit") : -1.0;
        this.lastException = AssemblyException.read(compound);
        super.read(compound, clientPacket);
        if (clientPacket) {
            if (forceMovement) {
                this.resetContraptionToOffset();
            } else if (this.running) {
                this.clientOffsetDiff = this.offset - offsetBefore;
                this.offset = offsetBefore;
            }
            if (!this.running) {
                this.movedContraption = null;
            }
        }
    }

    @Override
    public AssemblyException getLastAssemblyException() {
        return this.lastException;
    }

    public abstract void disassemble();

    protected abstract void assemble() throws AssemblyException;

    protected abstract int getExtensionRange();

    protected abstract int getInitialOffset();

    protected abstract ValueBoxTransform getMovementModeSlot();

    protected abstract Vec3 toMotionVector(float var1);

    protected abstract Vec3 toPosition(float var1);

    protected void visitNewPosition() {
    }

    protected void tryDisassemble() {
        if (this.f_58859_) {
            this.disassemble();
        } else if (this.getMovementMode() == IControlContraption.MovementMode.MOVE_NEVER_PLACE) {
            this.waitingForSpeedChange = true;
        } else {
            int initial = this.getInitialOffset();
            if ((int) (this.offset + 0.5F) != initial && this.getMovementMode() == IControlContraption.MovementMode.MOVE_PLACE_RETURNED) {
                this.waitingForSpeedChange = true;
            } else {
                this.disassemble();
            }
        }
    }

    protected IControlContraption.MovementMode getMovementMode() {
        return (IControlContraption.MovementMode) this.movementMode.get();
    }

    protected boolean moveAndCollideContraption() {
        if (this.movedContraption == null) {
            return false;
        } else if (this.movedContraption.isStalled()) {
            this.movedContraption.setContraptionMotion(Vec3.ZERO);
            return false;
        } else {
            Vec3 motion = this.getMotionVector();
            this.movedContraption.setContraptionMotion(this.getMotionVector());
            this.movedContraption.move(motion.x, motion.y, motion.z);
            return ContraptionCollider.collideBlocks(this.movedContraption);
        }
    }

    protected void collided() {
        if (this.f_58857_.isClientSide) {
            this.waitingForSpeedChange = true;
        } else {
            this.offset = (float) this.getGridOffset(this.offset - this.getMovementSpeed());
            this.resetContraptionToOffset();
            this.tryDisassemble();
        }
    }

    protected void resetContraptionToOffset() {
        if (this.movedContraption != null) {
            if (this.movedContraption.m_6084_()) {
                Vec3 vec = this.toPosition(this.offset);
                this.movedContraption.setPos(vec.x, vec.y, vec.z);
                if (this.getSpeed() == 0.0F || this.waitingForSpeedChange) {
                    this.movedContraption.setContraptionMotion(Vec3.ZERO);
                }
            }
        }
    }

    public float getMovementSpeed() {
        float movementSpeed = Mth.clamp(convertToLinear(this.getSpeed()), -0.49F, 0.49F) + this.clientOffsetDiff / 2.0F;
        if (this.f_58857_.isClientSide) {
            movementSpeed *= ServerSpeedProvider.get();
        }
        if (this.sequencedOffsetLimit >= 0.0) {
            movementSpeed = (float) Mth.clamp((double) movementSpeed, -this.sequencedOffsetLimit, this.sequencedOffsetLimit);
        }
        return movementSpeed;
    }

    public Vec3 getMotionVector() {
        return this.toMotionVector(this.getMovementSpeed());
    }

    @Override
    public void onStall() {
        if (!this.f_58857_.isClientSide) {
            this.forceMove = true;
            this.sendData();
        }
    }

    public void onLengthBroken() {
        this.offset = 0.0F;
        this.sendData();
    }

    @Override
    public boolean isValid() {
        return !this.m_58901_();
    }

    @Override
    public void attach(ControlledContraptionEntity contraption) {
        this.movedContraption = contraption;
        if (!this.f_58857_.isClientSide) {
            this.running = true;
            this.sendData();
        }
    }

    @Override
    public boolean isAttachedTo(AbstractContraptionEntity contraption) {
        return this.movedContraption == contraption;
    }

    @Override
    public BlockPos getBlockPosition() {
        return this.f_58858_;
    }
}