package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class WeightedPlacedFeature {

    public static final Codec<WeightedPlacedFeature> CODEC = RecordCodecBuilder.create(p_191187_ -> p_191187_.group(PlacedFeature.CODEC.fieldOf("feature").forGetter(p_204789_ -> p_204789_.feature), Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(p_191189_ -> p_191189_.chance)).apply(p_191187_, WeightedPlacedFeature::new));

    public final Holder<PlacedFeature> feature;

    public final float chance;

    public WeightedPlacedFeature(Holder<PlacedFeature> holderPlacedFeature0, float float1) {
        this.feature = holderPlacedFeature0;
        this.chance = float1;
    }

    public boolean place(WorldGenLevel worldGenLevel0, ChunkGenerator chunkGenerator1, RandomSource randomSource2, BlockPos blockPos3) {
        return this.feature.value().place(worldGenLevel0, chunkGenerator1, randomSource2, blockPos3);
    }
}