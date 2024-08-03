package me.jellysquid.mods.sodium.client.world;

import net.minecraft.world.level.chunk.PalettedContainerRO;

public interface ReadableContainerExtended<T> {

    static <T> ReadableContainerExtended<T> of(PalettedContainerRO<T> container) {
        return (ReadableContainerExtended<T>) container;
    }

    static <T> PalettedContainerRO<T> clone(PalettedContainerRO<T> container) {
        return container == null ? null : of(container).sodium$copy();
    }

    void sodium$unpack(T[] var1);

    void sodium$unpack(T[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    PalettedContainerRO<T> sodium$copy();
}