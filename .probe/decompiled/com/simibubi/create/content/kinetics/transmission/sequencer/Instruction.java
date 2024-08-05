package com.simibubi.create.content.kinetics.transmission.sequencer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.Vector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class Instruction {

    SequencerInstructions instruction;

    InstructionSpeedModifiers speedModifier;

    int value;

    public Instruction(SequencerInstructions instruction) {
        this(instruction, 1);
    }

    public Instruction(SequencerInstructions instruction, int value) {
        this(instruction, InstructionSpeedModifiers.FORWARD, value);
    }

    public Instruction(SequencerInstructions instruction, InstructionSpeedModifiers speedModifier, int value) {
        this.instruction = instruction;
        this.speedModifier = speedModifier;
        this.value = value;
    }

    int getDuration(float currentProgress, float speed) {
        speed *= (float) this.speedModifier.value;
        speed = Math.abs(speed);
        double target = (double) ((float) this.value - currentProgress);
        switch(this.instruction) {
            case TURN_ANGLE:
                double degreesPerTick = (double) KineticBlockEntity.convertToAngular(speed);
                return (int) Math.ceil(target / degreesPerTick) + 2;
            case TURN_DISTANCE:
                double metersPerTick = (double) KineticBlockEntity.convertToLinear(speed);
                return (int) Math.ceil(target / metersPerTick) + 2;
            case DELAY:
                return (int) target;
            case AWAIT:
                return -1;
            case END:
            default:
                return 0;
        }
    }

    float getTickProgress(float speed) {
        switch(this.instruction) {
            case TURN_ANGLE:
                return KineticBlockEntity.convertToAngular(speed);
            case TURN_DISTANCE:
                return KineticBlockEntity.convertToLinear(speed);
            case DELAY:
                return 1.0F;
            case AWAIT:
            case END:
            default:
                return 0.0F;
        }
    }

    int getSpeedModifier() {
        switch(this.instruction) {
            case TURN_ANGLE:
            case TURN_DISTANCE:
                return this.speedModifier.value;
            case DELAY:
            case AWAIT:
            case END:
            default:
                return 0;
        }
    }

    OnIsPoweredResult onRedstonePulse() {
        return this.instruction == SequencerInstructions.AWAIT ? OnIsPoweredResult.CONTINUE : OnIsPoweredResult.NOTHING;
    }

    public static ListTag serializeAll(Vector<Instruction> instructions) {
        ListTag list = new ListTag();
        instructions.forEach(i -> list.add(i.serialize()));
        return list;
    }

    public static Vector<Instruction> deserializeAll(ListTag list) {
        if (list.isEmpty()) {
            return createDefault();
        } else {
            Vector<Instruction> instructions = new Vector(5);
            list.forEach(inbt -> instructions.add(deserialize((CompoundTag) inbt)));
            return instructions;
        }
    }

    public static Vector<Instruction> createDefault() {
        Vector<Instruction> instructions = new Vector(5);
        instructions.add(new Instruction(SequencerInstructions.TURN_ANGLE, 90));
        instructions.add(new Instruction(SequencerInstructions.END));
        return instructions;
    }

    CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        NBTHelper.writeEnum(tag, "Type", this.instruction);
        NBTHelper.writeEnum(tag, "Modifier", this.speedModifier);
        tag.putInt("Value", this.value);
        return tag;
    }

    static Instruction deserialize(CompoundTag tag) {
        Instruction instruction = new Instruction(NBTHelper.readEnum(tag, "Type", SequencerInstructions.class));
        instruction.speedModifier = NBTHelper.readEnum(tag, "Modifier", InstructionSpeedModifiers.class);
        instruction.value = tag.getInt("Value");
        return instruction;
    }
}