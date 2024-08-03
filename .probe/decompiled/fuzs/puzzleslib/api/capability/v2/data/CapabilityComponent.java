package fuzs.puzzleslib.api.capability.v2.data;

import net.minecraft.nbt.CompoundTag;

public interface CapabilityComponent {

    default void write(CompoundTag tag) {
    }

    default void read(CompoundTag tag) {
    }

    default CompoundTag toCompoundTag() {
        CompoundTag tag = new CompoundTag();
        this.write(tag);
        return tag;
    }
}