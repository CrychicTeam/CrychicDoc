package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class SimpleRandomSelectorFeature extends Feature<SimpleRandomFeatureConfiguration> {

    public SimpleRandomSelectorFeature(Codec<SimpleRandomFeatureConfiguration> codecSimpleRandomFeatureConfiguration0) {
        super(codecSimpleRandomFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleRandomFeatureConfiguration> featurePlaceContextSimpleRandomFeatureConfiguration0) {
        RandomSource $$1 = featurePlaceContextSimpleRandomFeatureConfiguration0.random();
        SimpleRandomFeatureConfiguration $$2 = featurePlaceContextSimpleRandomFeatureConfiguration0.config();
        WorldGenLevel $$3 = featurePlaceContextSimpleRandomFeatureConfiguration0.level();
        BlockPos $$4 = featurePlaceContextSimpleRandomFeatureConfiguration0.origin();
        ChunkGenerator $$5 = featurePlaceContextSimpleRandomFeatureConfiguration0.chunkGenerator();
        int $$6 = $$1.nextInt($$2.features.size());
        PlacedFeature $$7 = $$2.features.get($$6).value();
        return $$7.place($$3, $$5, $$1, $$4);
    }
}