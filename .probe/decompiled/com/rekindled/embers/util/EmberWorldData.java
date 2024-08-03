package com.rekindled.embers.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class EmberWorldData extends SavedData {

    private static final String NAME = "embers_data";

    public EmberWorldData(CompoundTag nbt) {
        EmberGenUtil.offX = nbt.getInt("offX");
        EmberGenUtil.offZ = nbt.getInt("offZ");
    }

    public static EmberWorldData get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(EmberWorldData::new, () -> new EmberWorldData(new CompoundTag()), "embers_data");
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("offX", EmberGenUtil.offX);
        tag.putInt("offZ", EmberGenUtil.offZ);
        return tag;
    }
}