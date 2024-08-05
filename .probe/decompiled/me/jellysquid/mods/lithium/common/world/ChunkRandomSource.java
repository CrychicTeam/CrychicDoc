package me.jellysquid.mods.lithium.common.world;

import net.minecraft.core.BlockPos;

public interface ChunkRandomSource {

    void getRandomPosInChunk(int var1, int var2, int var3, int var4, BlockPos.MutableBlockPos var5);
}