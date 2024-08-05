package net.minecraft.world.level.levelgen.heightproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.levelgen.WorldGenerationContext;

public class WeightedListHeight extends HeightProvider {

    public static final Codec<WeightedListHeight> CODEC = RecordCodecBuilder.create(p_191539_ -> p_191539_.group(SimpleWeightedRandomList.wrappedCodec(HeightProvider.CODEC).fieldOf("distribution").forGetter(p_191541_ -> p_191541_.distribution)).apply(p_191539_, WeightedListHeight::new));

    private final SimpleWeightedRandomList<HeightProvider> distribution;

    public WeightedListHeight(SimpleWeightedRandomList<HeightProvider> simpleWeightedRandomListHeightProvider0) {
        this.distribution = simpleWeightedRandomListHeightProvider0;
    }

    @Override
    public int sample(RandomSource randomSource0, WorldGenerationContext worldGenerationContext1) {
        return ((HeightProvider) this.distribution.getRandomValue(randomSource0).orElseThrow(IllegalStateException::new)).sample(randomSource0, worldGenerationContext1);
    }

    @Override
    public HeightProviderType<?> getType() {
        return HeightProviderType.WEIGHTED_LIST;
    }
}