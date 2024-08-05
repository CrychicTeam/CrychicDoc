package com.simibubi.create.content.kinetics.belt.transport;

import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import java.util.Random;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class TransportedItemStack implements Comparable<TransportedItemStack> {

    private static Random R = new Random();

    public ItemStack stack;

    public float beltPosition;

    public float sideOffset;

    public int angle;

    public int insertedAt;

    public Direction insertedFrom;

    public boolean locked;

    public boolean lockedExternally;

    public float prevBeltPosition;

    public float prevSideOffset;

    public FanProcessingType processedBy;

    public int processingTime;

    public TransportedItemStack(ItemStack stack) {
        this.stack = stack;
        boolean centered = BeltHelper.isItemUpright(stack);
        this.angle = centered ? 180 : R.nextInt(360);
        this.sideOffset = this.prevSideOffset = this.getTargetSideOffset();
        this.insertedFrom = Direction.UP;
    }

    public float getTargetSideOffset() {
        return (float) (this.angle - 180) / 1080.0F;
    }

    public int compareTo(TransportedItemStack o) {
        return this.beltPosition < o.beltPosition ? 1 : (this.beltPosition > o.beltPosition ? -1 : 0);
    }

    public TransportedItemStack getSimilar() {
        TransportedItemStack copy = new TransportedItemStack(this.stack.copy());
        copy.beltPosition = this.beltPosition;
        copy.insertedAt = this.insertedAt;
        copy.insertedFrom = this.insertedFrom;
        copy.prevBeltPosition = this.prevBeltPosition;
        copy.prevSideOffset = this.prevSideOffset;
        copy.processedBy = this.processedBy;
        copy.processingTime = this.processingTime;
        return copy;
    }

    public TransportedItemStack copy() {
        TransportedItemStack copy = this.getSimilar();
        copy.angle = this.angle;
        copy.sideOffset = this.sideOffset;
        return copy;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("Item", this.stack.serializeNBT());
        nbt.putFloat("Pos", this.beltPosition);
        nbt.putFloat("PrevPos", this.prevBeltPosition);
        nbt.putFloat("Offset", this.sideOffset);
        nbt.putFloat("PrevOffset", this.prevSideOffset);
        nbt.putInt("InSegment", this.insertedAt);
        nbt.putInt("Angle", this.angle);
        nbt.putInt("InDirection", this.insertedFrom.get3DDataValue());
        if (this.locked) {
            nbt.putBoolean("Locked", this.locked);
        }
        if (this.lockedExternally) {
            nbt.putBoolean("LockedExternally", this.lockedExternally);
        }
        return nbt;
    }

    public static TransportedItemStack read(CompoundTag nbt) {
        TransportedItemStack stack = new TransportedItemStack(ItemStack.of(nbt.getCompound("Item")));
        stack.beltPosition = nbt.getFloat("Pos");
        stack.prevBeltPosition = nbt.getFloat("PrevPos");
        stack.sideOffset = nbt.getFloat("Offset");
        stack.prevSideOffset = nbt.getFloat("PrevOffset");
        stack.insertedAt = nbt.getInt("InSegment");
        stack.angle = nbt.getInt("Angle");
        stack.insertedFrom = Direction.from3DDataValue(nbt.getInt("InDirection"));
        stack.locked = nbt.getBoolean("Locked");
        stack.lockedExternally = nbt.getBoolean("LockedExternally");
        return stack;
    }
}