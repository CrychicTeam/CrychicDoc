package net.minecraft.util.valueproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class ClampedInt extends IntProvider {

    public static final Codec<ClampedInt> CODEC = RecordCodecBuilder.create(p_146400_ -> p_146400_.group(IntProvider.CODEC.fieldOf("source").forGetter(p_146410_ -> p_146410_.source), Codec.INT.fieldOf("min_inclusive").forGetter(p_146408_ -> p_146408_.minInclusive), Codec.INT.fieldOf("max_inclusive").forGetter(p_146405_ -> p_146405_.maxInclusive)).apply(p_146400_, ClampedInt::new)).comapFlatMap(p_274932_ -> p_274932_.maxInclusive < p_274932_.minInclusive ? DataResult.error(() -> "Max must be at least min, min_inclusive: " + p_274932_.minInclusive + ", max_inclusive: " + p_274932_.maxInclusive) : DataResult.success(p_274932_), Function.identity());

    private final IntProvider source;

    private final int minInclusive;

    private final int maxInclusive;

    public static ClampedInt of(IntProvider intProvider0, int int1, int int2) {
        return new ClampedInt(intProvider0, int1, int2);
    }

    public ClampedInt(IntProvider intProvider0, int int1, int int2) {
        this.source = intProvider0;
        this.minInclusive = int1;
        this.maxInclusive = int2;
    }

    @Override
    public int sample(RandomSource randomSource0) {
        return Mth.clamp(this.source.sample(randomSource0), this.minInclusive, this.maxInclusive);
    }

    @Override
    public int getMinValue() {
        return Math.max(this.minInclusive, this.source.getMinValue());
    }

    @Override
    public int getMaxValue() {
        return Math.min(this.maxInclusive, this.source.getMaxValue());
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderType.CLAMPED;
    }
}