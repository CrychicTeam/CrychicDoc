package me.jellysquid.mods.lithium.common.world.chunk;

import net.minecraft.world.level.chunk.Palette;

public interface CompactingPackedIntegerArray {

    <T> void compact(Palette<T> var1, Palette<T> var2, short[] var3);
}