package net.minecraft.util.valueproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;

public class WeightedListInt extends IntProvider {

    public static final Codec<WeightedListInt> CODEC = RecordCodecBuilder.create(p_185920_ -> p_185920_.group(SimpleWeightedRandomList.wrappedCodec(IntProvider.CODEC).fieldOf("distribution").forGetter(p_185918_ -> p_185918_.distribution)).apply(p_185920_, WeightedListInt::new));

    private final SimpleWeightedRandomList<IntProvider> distribution;

    private final int minValue;

    private final int maxValue;

    public WeightedListInt(SimpleWeightedRandomList<IntProvider> simpleWeightedRandomListIntProvider0) {
        this.distribution = simpleWeightedRandomListIntProvider0;
        List<WeightedEntry.Wrapper<IntProvider>> $$1 = simpleWeightedRandomListIntProvider0.m_146338_();
        int $$2 = Integer.MAX_VALUE;
        int $$3 = Integer.MIN_VALUE;
        for (WeightedEntry.Wrapper<IntProvider> $$4 : $$1) {
            int $$5 = $$4.getData().getMinValue();
            int $$6 = $$4.getData().getMaxValue();
            $$2 = Math.min($$2, $$5);
            $$3 = Math.max($$3, $$6);
        }
        this.minValue = $$2;
        this.maxValue = $$3;
    }

    @Override
    public int sample(RandomSource randomSource0) {
        return ((IntProvider) this.distribution.getRandomValue(randomSource0).orElseThrow(IllegalStateException::new)).sample(randomSource0);
    }

    @Override
    public int getMinValue() {
        return this.minValue;
    }

    @Override
    public int getMaxValue() {
        return this.maxValue;
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderType.WEIGHTED_LIST;
    }
}