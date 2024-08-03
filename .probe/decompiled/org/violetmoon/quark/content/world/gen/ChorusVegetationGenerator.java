package org.violetmoon.quark.content.world.gen;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.violetmoon.quark.content.world.module.ChorusVegetationModule;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.util.BooleanSuppliers;
import org.violetmoon.zeta.world.generator.multichunk.MultiChunkFeatureGenerator;

public class ChorusVegetationGenerator extends MultiChunkFeatureGenerator {

    public ChorusVegetationGenerator() {
        super(DimensionConfig.end(false), BooleanSuppliers.TRUE, 2093L);
    }

    @Override
    public int getFeatureRadius() {
        return ChorusVegetationModule.radius;
    }

    @Override
    public BlockPos[] getSourcesInChunk(WorldGenRegion world, Random random, ChunkGenerator generator, BlockPos chunkCorner) {
        if (!chunkCorner.m_123314_(Vec3i.ZERO, 1050.0) && ChorusVegetationModule.rarity > 0 && random.nextInt(ChorusVegetationModule.rarity) == 0) {
            Holder<Biome> b = this.getBiome(world, chunkCorner, true);
            if (b.is(Biomes.END_HIGHLANDS.location())) {
                return new BlockPos[] { chunkCorner };
            }
        }
        return new BlockPos[0];
    }

    @Override
    public void generateChunkPart(BlockPos src, ChunkGenerator generator, Random rand, BlockPos pos, WorldGenRegion worldIn) {
        for (int i = 0; i < ChorusVegetationModule.chunkAttempts; i++) {
            BlockPos placePos = pos.offset(rand.nextInt(16), 100, rand.nextInt(16));
            Holder<Biome> b = this.getBiome(worldIn, placePos, true);
            double chance = this.getChance(b);
            double dist = (double) ((placePos.m_123341_() - src.m_123341_()) * (placePos.m_123341_() - src.m_123341_()) + (placePos.m_123343_() - src.m_123343_()) * (placePos.m_123343_() - src.m_123343_()));
            int ditherStart = 6;
            ditherStart *= ditherStart;
            if (dist > (double) ditherStart) {
                chance *= 1.0 - Math.atan((dist - (double) ditherStart) / 50.0) / (Math.PI / 2);
            }
            if (chance > 0.0 && rand.nextDouble() < chance) {
                while (placePos.m_123342_() > 40) {
                    BlockState stateAt = worldIn.getBlockState(placePos);
                    if (stateAt.m_60734_() == Blocks.END_STONE) {
                        break;
                    }
                    placePos = placePos.below();
                }
                if (worldIn.getBlockState(placePos).m_60734_() == Blocks.END_STONE && worldIn.getBlockState(placePos.above()).m_60795_()) {
                    Block block = rand.nextDouble() < 0.1 ? ChorusVegetationModule.chorus_twist : ChorusVegetationModule.chorus_weeds;
                    worldIn.m_7731_(placePos.above(), block.defaultBlockState(), 2);
                }
            }
        }
    }

    private double getChance(Holder<Biome> b) {
        if (b.is(Biomes.END_HIGHLANDS.location())) {
            return ChorusVegetationModule.highlandsChance;
        } else {
            return b.is(Biomes.END_MIDLANDS.location()) ? ChorusVegetationModule.midlandsChance : ChorusVegetationModule.otherEndBiomesChance;
        }
    }
}