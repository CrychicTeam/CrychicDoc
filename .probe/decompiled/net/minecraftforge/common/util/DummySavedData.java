package net.minecraftforge.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class DummySavedData extends SavedData {

    public static final DummySavedData DUMMY = new DummySavedData();

    private DummySavedData() {
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        return null;
    }
}