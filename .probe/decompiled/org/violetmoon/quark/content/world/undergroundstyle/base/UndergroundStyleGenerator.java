package org.violetmoon.quark.content.world.undergroundstyle.base;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import org.violetmoon.zeta.world.generator.multichunk.ClusterBasedGenerator;

public class UndergroundStyleGenerator extends ClusterBasedGenerator {

    public final UndergroundStyleConfig info;

    public UndergroundStyleGenerator(UndergroundStyleConfig info, String name) {
        super(info.dimensions, info, (long) name.hashCode());
        this.info = info;
    }

    @Override
    public int getFeatureRadius() {
        return this.info.horizontalSize + this.info.horizontalVariation;
    }

    @Override
    public BlockPos[] getSourcesInChunk(WorldGenRegion world, Random random, ChunkGenerator generator, BlockPos chunkCorner) {
        if (this.info.rarity > 0 && random.nextInt(this.info.rarity) == 0) {
            int x = chunkCorner.m_123341_() + random.nextInt(16);
            int y = random.nextInt(this.info.minYLevel, this.info.maxYLevel);
            int z = chunkCorner.m_123343_() + random.nextInt(16);
            BlockPos pos = new BlockPos(x, y, z);
            if (this.info.biomes.canSpawn(this.getBiome(world, pos, true)) && world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) >= y) {
                return new BlockPos[] { pos };
            }
        }
        return new BlockPos[0];
    }

    @Override
    public ClusterBasedGenerator.IGenerationContext createContext(BlockPos src, ChunkGenerator generator, Random random, BlockPos chunkCorner, WorldGenRegion world) {
        return new UndergroundStyleGenerator.Context(world, src, generator, random, this.info);
    }

    public String toString() {
        return "UndergroundBiomeGenerator[" + this.info.style + "]";
    }

    public static class Context implements ClusterBasedGenerator.IGenerationContext {

        public final WorldGenRegion world;

        public final BlockPos source;

        public final ChunkGenerator generator;

        public final Random random;

        public final UndergroundStyleConfig info;

        public Context(WorldGenRegion world, BlockPos source, ChunkGenerator generator, Random random, UndergroundStyleConfig info) {
            this.world = world;
            this.source = source;
            this.generator = generator;
            this.random = random;
            this.info = info;
        }

        @Override
        public boolean canPlaceAt(BlockPos pos) {
            return this.world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.m_123341_(), pos.m_123343_()) > pos.m_123342_();
        }

        @Override
        public void consume(BlockPos pos) {
            this.info.style.fill(this, pos);
        }
    }
}