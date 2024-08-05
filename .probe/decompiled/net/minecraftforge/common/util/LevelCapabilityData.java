package net.minecraftforge.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class LevelCapabilityData extends SavedData {

    public static final String ID = "capabilities";

    private INBTSerializable<CompoundTag> serializable;

    private CompoundTag capNBT = null;

    public LevelCapabilityData(@Nullable INBTSerializable<CompoundTag> serializable) {
        this.serializable = serializable;
    }

    public static LevelCapabilityData load(CompoundTag tag, @Nullable INBTSerializable<CompoundTag> serializable) {
        LevelCapabilityData data = new LevelCapabilityData(serializable);
        data.read(tag);
        return data;
    }

    public void read(CompoundTag nbt) {
        this.capNBT = nbt;
        if (this.serializable != null) {
            this.serializable.deserializeNBT(this.capNBT);
            this.capNBT = null;
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        if (this.serializable != null) {
            nbt = this.serializable.serializeNBT();
        }
        return nbt;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public void setCapabilities(INBTSerializable<CompoundTag> capabilities) {
        this.serializable = capabilities;
        if (this.capNBT != null && this.serializable != null) {
            this.serializable.deserializeNBT(this.capNBT);
            this.capNBT = null;
        }
    }
}