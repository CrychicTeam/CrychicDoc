package com.mna.api.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

public class DirectionalPoint {

    private final Direction direction;

    private final BlockPos position;

    private final String originalBlockName;

    public DirectionalPoint(BlockPos position, Direction direction, String originalBlockName) {
        this.position = position;
        this.direction = direction;
        this.originalBlockName = originalBlockName;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean isValid() {
        return this.position != null && this.direction != null;
    }

    @Nullable
    public String getBlock() {
        return this.originalBlockName;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        if (this.direction != null) {
            tag.putInt("direction", this.direction.get3DDataValue());
        }
        if (this.position != null) {
            tag.put("position", NbtUtils.writeBlockPos(this.position));
        }
        if (this.originalBlockName != null) {
            tag.putString("block", this.originalBlockName);
        }
        return tag;
    }

    public static DirectionalPoint of(CompoundTag tag) {
        if (tag.contains("position") && tag.contains("direction") && tag.contains("block")) {
            BlockPos pos = NbtUtils.readBlockPos(tag.getCompound("position"));
            Direction direction = Direction.from3DDataValue(tag.getInt("direction"));
            String name = tag.getString("block");
            return new DirectionalPoint(pos, direction, name);
        } else {
            return null;
        }
    }
}