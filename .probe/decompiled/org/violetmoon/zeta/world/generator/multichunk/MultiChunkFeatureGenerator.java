package org.violetmoon.zeta.world.generator.multichunk;

import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.world.generator.Generator;

public abstract class MultiChunkFeatureGenerator extends Generator {

    private final long seedXor;

    public MultiChunkFeatureGenerator(DimensionConfig dimConfig, BooleanSupplier condition, long seedXor) {
        super(dimConfig, condition);
        this.seedXor = seedXor;
    }

    @Override
    public final void generateChunk(WorldGenRegion world, ChunkGenerator generator, RandomSource rand, BlockPos pos) {
        int radius = this.getFeatureRadius();
        if (radius > 0) {
            int chunkRadius = (int) Math.ceil((double) radius / 16.0);
            long worldSeed = world.getSeed();
            Random worldRandom = new Random(worldSeed);
            long xSeed = worldRandom.nextLong();
            long zSeed = worldRandom.nextLong();
            int chunkX = pos.m_123341_() >> 4;
            int chunkZ = pos.m_123343_() >> 4;
            long chunkSeed = xSeed * (long) chunkX + zSeed * (long) chunkZ ^ worldSeed ^ this.seedXor;
            Random ourRandom = new Random(chunkSeed);
            for (int x = chunkX - chunkRadius; x <= chunkX + chunkRadius; x++) {
                for (int z = chunkZ - chunkRadius; z <= chunkZ + chunkRadius; z++) {
                    chunkSeed = xSeed * (long) x + zSeed * (long) z ^ worldSeed ^ this.seedXor;
                    Random chunkRandom = new Random(chunkSeed);
                    BlockPos chunkCorner = new BlockPos(x << 4, 0, z << 4);
                    for (BlockPos source : this.getSourcesInChunk(world, chunkRandom, generator, chunkCorner)) {
                        this.generateChunkPart(source, generator, ourRandom, pos, world);
                    }
                }
            }
        }
    }

    public abstract int getFeatureRadius();

    public abstract void generateChunkPart(BlockPos var1, ChunkGenerator var2, Random var3, BlockPos var4, WorldGenRegion var5);

    public abstract BlockPos[] getSourcesInChunk(WorldGenRegion var1, Random var2, ChunkGenerator var3, BlockPos var4);

    public void forEachChunkBlock(LevelReader level, BlockPos chunkCorner, int minY, int maxY, Consumer<BlockPos> func) {
        minY = Math.max(level.getMinBuildHeight() + 1, minY);
        maxY = Math.min(level.m_151558_() - 1, maxY);
        int chunkCornerX = chunkCorner.m_123341_();
        int chunkCornerZ = chunkCorner.m_123343_();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(0, 0, 0);
        for (int x = 0; x < 16; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = 0; z < 16; z++) {
                    mutable.set(chunkCornerX + x, y, chunkCornerZ + z);
                    func.accept(mutable);
                }
            }
        }
    }

    public boolean isInsideChunk(BlockPos pos, int chunkX, int chunkZ) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        return pos.m_123341_() > x && pos.m_123343_() > z && pos.m_123341_() < x + 16 && pos.m_123343_() < z + 16;
    }
}