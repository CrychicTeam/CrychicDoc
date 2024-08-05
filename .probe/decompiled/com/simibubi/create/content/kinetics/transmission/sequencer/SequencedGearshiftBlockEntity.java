package com.simibubi.create.content.kinetics.transmission.sequencer;

import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.ComputerCraftProxy;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.List;
import java.util.Vector;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class SequencedGearshiftBlockEntity extends SplitShaftBlockEntity {

    Vector<Instruction> instructions = Instruction.createDefault();

    int currentInstruction = -1;

    int currentInstructionDuration = -1;

    float currentInstructionProgress = 0.0F;

    int timer = 0;

    boolean poweredPreviously = false;

    public AbstractComputerBehaviour computerBehaviour;

    public SequencedGearshiftBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(this.computerBehaviour = ComputerCraftProxy.behaviour(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isIdle()) {
            if (!this.f_58857_.isClientSide) {
                if (this.currentInstructionDuration >= 0) {
                    if (this.timer < this.currentInstructionDuration) {
                        this.timer++;
                        this.currentInstructionProgress = this.currentInstructionProgress + this.getInstruction(this.currentInstruction).getTickProgress(this.speed);
                    } else {
                        this.run(this.currentInstruction + 1);
                    }
                }
            }
        }
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        if (!this.isIdle()) {
            float currentSpeed = Math.abs(this.speed);
            if (Math.abs(previousSpeed) != currentSpeed) {
                Instruction instruction = this.getInstruction(this.currentInstruction);
                if (instruction != null) {
                    if (this.getSpeed() == 0.0F) {
                        this.run(-1);
                    }
                    this.currentInstructionDuration = instruction.getDuration(this.currentInstructionProgress, this.getTheoreticalSpeed());
                    this.timer = 0;
                }
            }
        }
    }

    public boolean isIdle() {
        return this.currentInstruction == -1;
    }

    public void onRedstoneUpdate(boolean isPowered, boolean isRunning) {
        if (!this.computerBehaviour.hasAttachedComputer()) {
            if (!this.poweredPreviously && isPowered) {
                this.risingFlank();
            }
            this.poweredPreviously = isPowered;
            if (this.isIdle()) {
                if (isPowered != isRunning) {
                    if (!this.f_58857_.m_276867_(this.f_58858_)) {
                        this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(SequencedGearshiftBlock.STATE, 0), 3);
                    } else if (this.getSpeed() != 0.0F) {
                        this.run(0);
                    }
                }
            }
        }
    }

    public void risingFlank() {
        Instruction instruction = this.getInstruction(this.currentInstruction);
        if (instruction != null) {
            if (!this.poweredPreviously) {
                this.poweredPreviously = true;
                switch(instruction.onRedstonePulse()) {
                    case CONTINUE:
                        this.run(this.currentInstruction + 1);
                }
            }
        }
    }

    public void run(int instructionIndex) {
        Instruction instruction = this.getInstruction(instructionIndex);
        if (instruction != null && instruction.instruction != SequencerInstructions.END) {
            this.detachKinetics();
            this.currentInstructionDuration = instruction.getDuration(0.0F, this.getTheoreticalSpeed());
            this.currentInstruction = instructionIndex;
            this.currentInstructionProgress = 0.0F;
            this.sequenceContext = SequencedGearshiftBlockEntity.SequenceContext.fromGearshift(instruction.instruction, (double) (this.getTheoreticalSpeed() * (float) this.getModifier()), instruction.value);
            this.timer = 0;
            this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(SequencedGearshiftBlock.STATE, instructionIndex + 1), 3);
        } else {
            if (this.getModifier() != 0) {
                this.detachKinetics();
            }
            this.currentInstruction = -1;
            this.currentInstructionDuration = -1;
            this.currentInstructionProgress = 0.0F;
            this.sequenceContext = null;
            this.timer = 0;
            if (!this.f_58857_.m_276867_(this.f_58858_)) {
                this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(SequencedGearshiftBlock.STATE, 0), 3);
            } else {
                this.sendData();
            }
        }
    }

    public Instruction getInstruction(int instructionIndex) {
        return instructionIndex >= 0 && instructionIndex < this.instructions.size() ? (Instruction) this.instructions.get(instructionIndex) : null;
    }

    @Override
    protected void copySequenceContextFrom(KineticBlockEntity sourceBE) {
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("InstructionIndex", this.currentInstruction);
        compound.putInt("InstructionDuration", this.currentInstructionDuration);
        compound.putFloat("InstructionProgress", this.currentInstructionProgress);
        compound.putInt("Timer", this.timer);
        compound.putBoolean("PrevPowered", this.poweredPreviously);
        compound.put("Instructions", Instruction.serializeAll(this.instructions));
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.currentInstruction = compound.getInt("InstructionIndex");
        this.currentInstructionDuration = compound.getInt("InstructionDuration");
        this.currentInstructionProgress = compound.getFloat("InstructionProgress");
        this.poweredPreviously = compound.getBoolean("PrevPowered");
        this.timer = compound.getInt("Timer");
        this.instructions = Instruction.deserializeAll(compound.getList("Instructions", 10));
        super.read(compound, clientPacket);
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.computerBehaviour.isPeripheralCap(cap) ? this.computerBehaviour.getPeripheralCapability() : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.computerBehaviour.removePeripheral();
    }

    @Override
    public float getRotationSpeedModifier(Direction face) {
        if (this.isVirtual()) {
            return 1.0F;
        } else {
            return this.hasSource() && face != this.getSourceFacing() ? (float) this.getModifier() : 1.0F;
        }
    }

    public int getModifier() {
        if (this.currentInstruction >= this.instructions.size()) {
            return 0;
        } else {
            return this.isIdle() ? 0 : ((Instruction) this.instructions.get(this.currentInstruction)).getSpeedModifier();
        }
    }

    public Vector<Instruction> getInstructions() {
        return this.instructions;
    }

    public static record SequenceContext(SequencerInstructions instruction, double relativeValue) {

        public static SequencedGearshiftBlockEntity.SequenceContext fromGearshift(SequencerInstructions instruction, double kineticSpeed, int absoluteValue) {
            return instruction.needsPropagation() ? new SequencedGearshiftBlockEntity.SequenceContext(instruction, kineticSpeed == 0.0 ? 0.0 : (double) absoluteValue / kineticSpeed) : null;
        }

        public double getEffectiveValue(double speedAtTarget) {
            return Math.abs(this.relativeValue * speedAtTarget);
        }

        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            NBTHelper.writeEnum(nbt, "Mode", this.instruction);
            nbt.putDouble("Value", this.relativeValue);
            return nbt;
        }

        public static SequencedGearshiftBlockEntity.SequenceContext fromNBT(CompoundTag nbt) {
            return nbt.isEmpty() ? null : new SequencedGearshiftBlockEntity.SequenceContext(NBTHelper.readEnum(nbt, "Mode", SequencerInstructions.class), nbt.getDouble("Value"));
        }
    }
}