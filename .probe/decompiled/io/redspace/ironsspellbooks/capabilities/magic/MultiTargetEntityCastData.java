package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;

public class MultiTargetEntityCastData implements ICastDataSerializable {

    private List<UUID> targetUUIDs = new ArrayList();

    public MultiTargetEntityCastData(LivingEntity... targets) {
        Arrays.stream(targets).forEach(target -> this.targetUUIDs.add(target.m_20148_()));
    }

    @Override
    public void reset() {
        this.targetUUIDs.clear();
    }

    public List<UUID> getTargets() {
        return this.targetUUIDs;
    }

    public void addTarget(LivingEntity entity) {
        this.targetUUIDs.add(entity.m_20148_());
    }

    public void addTarget(UUID uuid) {
        this.targetUUIDs.add(uuid);
    }

    public boolean isTargeted(LivingEntity entity) {
        return this.targetUUIDs.contains(entity.m_20148_());
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeInt(this.targetUUIDs.size());
        this.targetUUIDs.forEach(buffer::m_130077_);
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buffer) {
        this.targetUUIDs = new ArrayList();
        int i = buffer.readInt();
        for (int j = 0; j < i; j++) {
            this.targetUUIDs.add(buffer.readUUID());
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag uuids = new ListTag();
        this.targetUUIDs.stream().map(NbtUtils::m_129226_).forEach(uuids::add);
        tag.put("targets", uuids);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.targetUUIDs = new ArrayList();
        ListTag listTag = nbt.getList("targets", 11);
        listTag.stream().map(NbtUtils::m_129233_).forEach(this.targetUUIDs::add);
    }
}