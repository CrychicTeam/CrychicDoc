package net.minecraft.world.level.saveddata.maps;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

public class MapFrame {

    private final BlockPos pos;

    private final int rotation;

    private final int entityId;

    public MapFrame(BlockPos blockPos0, int int1, int int2) {
        this.pos = blockPos0;
        this.rotation = int1;
        this.entityId = int2;
    }

    public static MapFrame load(CompoundTag compoundTag0) {
        BlockPos $$1 = NbtUtils.readBlockPos(compoundTag0.getCompound("Pos"));
        int $$2 = compoundTag0.getInt("Rotation");
        int $$3 = compoundTag0.getInt("EntityId");
        return new MapFrame($$1, $$2, $$3);
    }

    public CompoundTag save() {
        CompoundTag $$0 = new CompoundTag();
        $$0.put("Pos", NbtUtils.writeBlockPos(this.pos));
        $$0.putInt("Rotation", this.rotation);
        $$0.putInt("EntityId", this.entityId);
        return $$0;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getRotation() {
        return this.rotation;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public String getId() {
        return frameId(this.pos);
    }

    public static String frameId(BlockPos blockPos0) {
        return "frame-" + blockPos0.m_123341_() + "," + blockPos0.m_123342_() + "," + blockPos0.m_123343_();
    }
}