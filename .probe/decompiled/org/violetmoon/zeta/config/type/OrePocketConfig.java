package org.violetmoon.zeta.config.type;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import org.violetmoon.zeta.config.Config;

public class OrePocketConfig implements IConfigType {

    @Config
    @Config.Min(-64.0)
    @Config.Max(320.0)
    private int minHeight;

    @Config
    @Config.Min(-64.0)
    @Config.Max(320.0)
    private int maxHeight;

    @Config
    @Config.Min(0.0)
    public int clusterSize;

    @Config(description = "Can be a positive integer or a fractional value betweeen 0 and 1. If integer, it spawns that many clusters. If fractional, it has that chance to spawn a single cluster. Set exactly zero to not spawn at all.")
    @Config.Min(0.0)
    public double clusterCount;

    public OrePocketConfig(int minHeight, int maxHeight, int clusterSize, double clusterCount) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.clusterSize = clusterSize;
        this.clusterCount = clusterCount;
    }

    public int getRandomHeight(RandomSource rand) {
        return this.minHeight + rand.nextInt(this.maxHeight - this.minHeight);
    }

    public void forEach(BlockPos chunkCorner, RandomSource rand, Consumer<BlockPos> callback) {
        if (this.clusterCount < 1.0 && this.clusterCount > 0.0) {
            this.clusterCount = (double) (rand.nextDouble() < this.clusterCount ? 1 : 0);
        }
        for (int i = 0; (double) i < this.clusterCount; i++) {
            int x = chunkCorner.m_123341_() + rand.nextInt(16);
            int y = this.getRandomHeight(rand);
            int z = chunkCorner.m_123343_() + rand.nextInt(16);
            callback.accept(new BlockPos(x, y, z));
        }
    }
}