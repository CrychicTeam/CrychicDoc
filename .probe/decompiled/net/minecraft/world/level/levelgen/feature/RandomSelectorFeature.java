package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;

public class RandomSelectorFeature extends Feature<RandomFeatureConfiguration> {

    public RandomSelectorFeature(Codec<RandomFeatureConfiguration> codecRandomFeatureConfiguration0) {
        super(codecRandomFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<RandomFeatureConfiguration> featurePlaceContextRandomFeatureConfiguration0) {
        RandomFeatureConfiguration $$1 = featurePlaceContextRandomFeatureConfiguration0.config();
        RandomSource $$2 = featurePlaceContextRandomFeatureConfiguration0.random();
        WorldGenLevel $$3 = featurePlaceContextRandomFeatureConfiguration0.level();
        ChunkGenerator $$4 = featurePlaceContextRandomFeatureConfiguration0.chunkGenerator();
        BlockPos $$5 = featurePlaceContextRandomFeatureConfiguration0.origin();
        for (WeightedPlacedFeature $$6 : $$1.features) {
            if ($$2.nextFloat() < $$6.chance) {
                return $$6.place($$3, $$4, $$2, $$5);
            }
        }
        return $$1.defaultFeature.value().place($$3, $$4, $$2, $$5);
    }
}