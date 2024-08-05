package com.simibubi.create.foundation.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

public class BlockFace extends Pair<BlockPos, Direction> {

    public BlockFace(BlockPos first, Direction second) {
        super(first, second);
    }

    public boolean isEquivalent(BlockFace other) {
        return this.equals(other) ? true : this.getConnectedPos().equals(other.getPos()) && this.getPos().equals(other.getConnectedPos());
    }

    public BlockPos getPos() {
        return this.getFirst();
    }

    public Direction getFace() {
        return this.getSecond();
    }

    public Direction getOppositeFace() {
        return this.getSecond().getOpposite();
    }

    public BlockFace getOpposite() {
        return new BlockFace(this.getConnectedPos(), this.getOppositeFace());
    }

    public BlockPos getConnectedPos() {
        return this.getPos().relative(this.getFace());
    }

    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.put("Pos", NbtUtils.writeBlockPos(this.getPos()));
        NBTHelper.writeEnum(compoundNBT, "Face", this.getFace());
        return compoundNBT;
    }

    public static BlockFace fromNBT(CompoundTag compound) {
        return new BlockFace(NbtUtils.readBlockPos(compound.getCompound("Pos")), NBTHelper.readEnum(compound, "Face", Direction.class));
    }
}