package org.violetmoon.quark.content.world.gen;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.violetmoon.quark.content.world.module.NewStoneTypesModule;
import org.violetmoon.quark.content.world.module.SpiralSpiresModule;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.util.BooleanSuppliers;
import org.violetmoon.zeta.world.generator.multichunk.MultiChunkFeatureGenerator;

public class SpiralSpireGenerator extends MultiChunkFeatureGenerator {

    public SpiralSpireGenerator(DimensionConfig dimConfig) {
        super(dimConfig, BooleanSuppliers.TRUE, 1892L);
    }

    @Override
    public int getFeatureRadius() {
        return SpiralSpiresModule.radius;
    }

    @Override
    public void generateChunkPart(BlockPos src, ChunkGenerator generator, Random random, BlockPos chunkCorner, WorldGenRegion world) {
        double dist = chunkCorner.m_123331_(src) / (double) (16 * SpiralSpiresModule.radius * 16 * SpiralSpiresModule.radius);
        if (!(dist > 0.5) || !(random.nextDouble() < 1.5 - dist)) {
            BlockPos pos = chunkCorner.offset(random.nextInt(16), 256, random.nextInt(16));
            Holder<Biome> biome = this.getBiome(world, pos, false);
            if (SpiralSpiresModule.biomes.canSpawn(biome)) {
                while (world.getBlockState(pos).m_60734_() != Blocks.END_STONE) {
                    pos = pos.below();
                    if (pos.m_123342_() < 10) {
                        return;
                    }
                }
                this.makeSpike(world, generator, random, pos);
            }
        }
    }

    @Override
    public BlockPos[] getSourcesInChunk(WorldGenRegion world, Random random, ChunkGenerator generator, BlockPos chunkCorner) {
        return !chunkCorner.m_123314_(Vec3i.ZERO, 1050.0) && SpiralSpiresModule.rarity > 0 && random.nextInt(SpiralSpiresModule.rarity) == 0 ? new BlockPos[] { chunkCorner } : new BlockPos[0];
    }

    public void makeSpike(WorldGenRegion world, ChunkGenerator chunk, Random rand, BlockPos pos) {
        int height = 50 + rand.nextInt(20);
        double heightComposition = 5.0 + rand.nextDouble() * 1.0;
        int start = -5;
        for (int y = start; y < height; y++) {
            BlockPos test = pos.above(y);
            BlockState state = world.getBlockState(test);
            if (!state.m_60795_() && state.m_60734_() != Blocks.END_STONE && state.m_60734_() != Blocks.CRYING_OBSIDIAN && state.m_60734_() != Blocks.OBSIDIAN && state.m_60734_() != SpiralSpiresModule.myalite_crystal) {
                return;
            }
        }
        int var31;
        for (var31 = start; var31 < height; var31++) {
            if (var31 >= 0 || world.getBlockState(pos.above(var31)).m_60815_()) {
                double r = Math.abs(((Math.PI / 2) - Math.atan(((double) Math.max(0, var31) + 0.5) / ((double) height / heightComposition))) * 4.0);
                int ri = (int) Math.ceil(r);
                for (int i = -ri + 1; i < ri; i++) {
                    for (int j = -ri + 1; j < ri; j++) {
                        if (i * i + j * j <= ri * ri) {
                            boolean edge = i == -ri + 1 || i == ri - 1 || j == -ri + 1 || j == ri - 1;
                            BlockState state = (edge && (double) rand.nextFloat() < 0.2 ? NewStoneTypesModule.myaliteBlock : SpiralSpiresModule.dusky_myalite).defaultBlockState();
                            world.m_7731_(pos.offset(i, var31, j), state, 2);
                        }
                    }
                }
            }
        }
        int steps = 80 + rand.nextInt(30);
        int substeps = 10;
        int fullSteps = steps * substeps;
        int deteroirate = (int) ((0.5 + rand.nextDouble() * 0.3) * (double) fullSteps);
        double spin = 0.12 + rand.nextDouble() * 0.16;
        double spread = 0.12 + rand.nextDouble() * 0.04;
        double upwardMotion = rand.nextDouble() * 0.2;
        if (rand.nextBoolean()) {
            spin *= -1.0;
        }
        BlockState state = SpiralSpiresModule.myalite_crystal.defaultBlockState();
        for (int i = 0; i < fullSteps; i++) {
            double t = (double) i * spin;
            int x = (int) (Math.sin(t / (double) substeps) * (double) i * spread / (double) substeps);
            int z = (int) (Math.cos(t / (double) substeps) * (double) i * spread / (double) substeps);
            int yp = var31 + (int) Math.round((double) i / (double) substeps * upwardMotion);
            BlockPos next = pos.offset(x, yp, z);
            float chance = 1.0F;
            if (i > deteroirate) {
                int deterStep = i - deteroirate;
                int maxSteps = fullSteps - deteroirate;
                chance -= (float) deterStep / (float) maxSteps;
            }
            if (rand.nextFloat() < chance) {
                world.m_7731_(next, state, 2);
            }
        }
    }
}