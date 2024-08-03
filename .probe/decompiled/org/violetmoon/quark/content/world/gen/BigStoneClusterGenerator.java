package org.violetmoon.quark.content.world.gen;

import java.util.Objects;
import java.util.Random;
import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.world.config.AirStoneClusterConfig;
import org.violetmoon.quark.content.world.config.BigStoneClusterConfig;
import org.violetmoon.quark.content.world.module.BigStoneClustersModule;
import org.violetmoon.zeta.world.generator.multichunk.ClusterBasedGenerator;

public class BigStoneClusterGenerator extends ClusterBasedGenerator {

    private final BigStoneClusterConfig config;

    private final BlockState placeState;

    public BigStoneClusterGenerator(BigStoneClusterConfig config, BlockState placeState, BooleanSupplier condition) {
        super(config.dimensions, () -> config.enabled && condition.getAsBoolean(), config, (long) Objects.toString(Quark.ZETA.registry.getRegistryName(placeState.m_60734_(), BuiltInRegistries.BLOCK)).hashCode());
        this.config = config;
        this.placeState = placeState;
    }

    @Override
    public BlockPos[] getSourcesInChunk(WorldGenRegion world, Random random, ChunkGenerator generator, BlockPos chunkCorner) {
        int chance = this.config.rarity;
        if (chance > 0 && random.nextInt(chance) == 0) {
            int lower = this.config.minYLevel;
            int range = Math.abs(this.config.maxYLevel - this.config.minYLevel);
            BlockPos pos = chunkCorner.offset(random.nextInt(16), random.nextInt(range) + lower, random.nextInt(16));
            if (this.config.biomes.canSpawn(this.getBiome(world, pos, true))) {
                return new BlockPos[] { pos };
            }
        }
        return new BlockPos[0];
    }

    public String toString() {
        return "BigStoneClusterGenerator[" + this.placeState + "]";
    }

    @Override
    public ClusterBasedGenerator.IGenerationContext createContext(BlockPos src, ChunkGenerator generator, Random random, BlockPos chunkCorner, final WorldGenRegion world) {
        return new ClusterBasedGenerator.IGenerationContext() {

            @Override
            public boolean canPlaceAt(BlockPos pos) {
                return BigStoneClusterGenerator.this.canPlaceBlock(world, pos);
            }

            @Override
            public void consume(BlockPos pos) {
                world.m_7731_(pos, BigStoneClusterGenerator.this.placeState, 0);
            }
        };
    }

    private boolean canPlaceBlock(ServerLevelAccessor world, BlockPos pos) {
        if (this.config instanceof AirStoneClusterConfig clusterConfig && clusterConfig.generateInAir) {
            return world.m_8055_(pos).m_60795_();
        }
        return BigStoneClustersModule.blockReplacePredicate.test(world.getLevel(), world.m_8055_(pos).m_60734_());
    }
}