package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;

public class RandomBooleanSelectorFeature extends Feature<RandomBooleanFeatureConfiguration> {

    public RandomBooleanSelectorFeature(Codec<RandomBooleanFeatureConfiguration> codecRandomBooleanFeatureConfiguration0) {
        super(codecRandomBooleanFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<RandomBooleanFeatureConfiguration> featurePlaceContextRandomBooleanFeatureConfiguration0) {
        RandomSource $$1 = featurePlaceContextRandomBooleanFeatureConfiguration0.random();
        RandomBooleanFeatureConfiguration $$2 = featurePlaceContextRandomBooleanFeatureConfiguration0.config();
        WorldGenLevel $$3 = featurePlaceContextRandomBooleanFeatureConfiguration0.level();
        ChunkGenerator $$4 = featurePlaceContextRandomBooleanFeatureConfiguration0.chunkGenerator();
        BlockPos $$5 = featurePlaceContextRandomBooleanFeatureConfiguration0.origin();
        boolean $$6 = $$1.nextBoolean();
        return ($$6 ? $$2.featureTrue : $$2.featureFalse).value().place($$3, $$4, $$1, $$5);
    }
}