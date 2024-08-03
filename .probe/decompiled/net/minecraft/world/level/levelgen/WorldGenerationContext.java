package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;

public class WorldGenerationContext {

    private final int minY;

    private final int height;

    public WorldGenerationContext(ChunkGenerator chunkGenerator0, LevelHeightAccessor levelHeightAccessor1) {
        this.minY = Math.max(levelHeightAccessor1.getMinBuildHeight(), chunkGenerator0.getMinY());
        this.height = Math.min(levelHeightAccessor1.getHeight(), chunkGenerator0.getGenDepth());
    }

    public int getMinGenY() {
        return this.minY;
    }

    public int getGenDepth() {
        return this.height;
    }
}