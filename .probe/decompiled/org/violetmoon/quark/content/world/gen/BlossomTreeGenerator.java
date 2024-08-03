package org.violetmoon.quark.content.world.gen;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.material.Fluids;
import org.violetmoon.quark.content.world.config.BlossomTreeConfig;
import org.violetmoon.zeta.world.generator.Generator;

public class BlossomTreeGenerator extends Generator {

    private final BlossomTreeConfig quarkConfig;

    private final ResourceKey<ConfiguredFeature<?, ?>> treeKey;

    public BlossomTreeGenerator(BlossomTreeConfig quarkConfig, ResourceKey<ConfiguredFeature<?, ?>> treeKey) {
        super(quarkConfig.dimensions);
        this.quarkConfig = quarkConfig;
        this.treeKey = treeKey;
    }

    @Override
    public void generateChunk(WorldGenRegion worldIn, ChunkGenerator generator, RandomSource rand, BlockPos pos) {
        BlockPos placePos = pos.offset(rand.nextInt(16), 0, rand.nextInt(16));
        if (this.quarkConfig.biomeConfig.canSpawn(this.getBiome(worldIn, placePos, false)) && rand.nextInt(this.quarkConfig.rarity) == 0) {
            placePos = worldIn.m_5452_(Heightmap.Types.MOTION_BLOCKING, placePos).below();
            BlockState ground = worldIn.getBlockState(placePos);
            if (ground.m_60734_().canSustainPlant(ground, worldIn, pos, Direction.UP, (SaplingBlock) Blocks.OAK_SAPLING)) {
                BlockPos up = placePos.above();
                BlockState upState = worldIn.getBlockState(up);
                Registry<ConfiguredFeature<?, ?>> cfgFeatureRegistry = worldIn.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
                ConfiguredFeature<?, ?> cfgFeature = cfgFeatureRegistry.get(this.treeKey);
                if (cfgFeature == null) {
                    return;
                }
                FeaturePlaceContext<TreeConfiguration> placeCtx = new FeaturePlaceContext<>(Optional.of(cfgFeature), worldIn, generator, rand, up, (TreeConfiguration) cfgFeature.config());
                if (upState.m_60722_(Fluids.WATER)) {
                    worldIn.m_7731_(up, Blocks.AIR.defaultBlockState(), 0);
                }
                Feature.TREE.place(placeCtx);
            }
        }
    }
}