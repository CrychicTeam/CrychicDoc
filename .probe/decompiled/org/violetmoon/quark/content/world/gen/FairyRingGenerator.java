package org.violetmoon.quark.content.world.gen;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import org.violetmoon.quark.content.world.module.FairyRingsModule;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.util.BlockUtils;
import org.violetmoon.zeta.world.generator.Generator;

public class FairyRingGenerator extends Generator {

    public FairyRingGenerator(DimensionConfig dimConfig) {
        super(dimConfig);
    }

    @Override
    public void generateChunk(WorldGenRegion worldIn, ChunkGenerator generator, RandomSource rand, BlockPos corner) {
        int x = corner.m_123341_() + rand.nextInt(16);
        int z = corner.m_123343_() + rand.nextInt(16);
        BlockPos center = new BlockPos(x, 128, z);
        Holder<Biome> biome = this.getBiome(worldIn, center, false);
        double chance = 0.0;
        if (biome.is(BiomeTags.IS_FOREST)) {
            chance = FairyRingsModule.forestChance;
        } else if (biome.is(Tags.Biomes.IS_PLAINS)) {
            chance = FairyRingsModule.plainsChance;
        }
        if (rand.nextDouble() < chance) {
            BlockPos pos = center;
            BlockState state;
            for (state = worldIn.getBlockState(center); BlockUtils.isGlassBased(state, worldIn, corner) && pos.m_123342_() > 30; state = worldIn.getBlockState(pos)) {
                pos = pos.below();
            }
            if (BlockUtils.isGlassBased(state, worldIn, corner)) {
                spawnFairyRing(worldIn, generator, pos.below(), biome, rand);
            }
        }
    }

    public static void spawnFairyRing(WorldGenLevel world, ChunkGenerator generator, BlockPos pos, Holder<Biome> biome, RandomSource rand) {
        List<ConfiguredFeature<?, ?>> features = biome.value().getGenerationSettings().getFlowerFeatures();
        Holder<PlacedFeature> holder = features.isEmpty() ? null : ((RandomPatchConfiguration) ((ConfiguredFeature) features.get(0)).config()).feature();
        BlockState flowerState = holder == null ? Blocks.OXEYE_DAISY.defaultBlockState() : null;
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                float dist = (float) (i * i + j * j);
                if (!(dist < 7.0F) && !(dist > 10.0F)) {
                    for (int k = 5; k > -4; k--) {
                        BlockPos fpos = pos.offset(i, k, j);
                        BlockPos fposUp = fpos.above();
                        BlockState state = world.m_8055_(fpos);
                        if (state.m_60734_() instanceof AbstractGlassBlock && world.m_46859_(fposUp)) {
                            if (flowerState == null) {
                                holder.value().place(world, generator, rand, fposUp);
                                flowerState = world.m_8055_(fposUp);
                            } else {
                                world.m_7731_(fposUp, flowerState, 2);
                            }
                            break;
                        }
                    }
                } else {
                    for (int kx = 6; kx > -3; kx--) {
                        BlockPos fpos = pos.offset(i, kx, j);
                        BlockState state = world.m_8055_(fpos);
                        if (state.m_204336_(BlockTags.SMALL_FLOWERS)) {
                            world.m_7731_(fpos, Blocks.AIR.defaultBlockState(), 2);
                            break;
                        }
                    }
                }
            }
        }
        BlockPos orePos = pos.below(rand.nextInt(10) + 25);
        BlockState stoneState = world.m_8055_(orePos);
        for (int down = 0; !stoneState.m_204336_(Tags.Blocks.STONE) && down < 10; down++) {
            orePos = orePos.below();
            stoneState = world.m_8055_(orePos);
        }
        if (stoneState.m_204336_(Tags.Blocks.STONE)) {
            BlockState ore = (BlockState) FairyRingsModule.ores.get(rand.nextInt(FairyRingsModule.ores.size()));
            world.m_7731_(orePos, ore, 2);
            for (Direction face : Direction.values()) {
                if (rand.nextBoolean()) {
                    world.m_7731_(orePos.relative(face), ore, 2);
                }
            }
        }
    }
}