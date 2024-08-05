package net.minecraft.util.valueproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.util.RandomSource;

public class BiasedToBottomInt extends IntProvider {

    public static final Codec<BiasedToBottomInt> CODEC = RecordCodecBuilder.create(p_146373_ -> p_146373_.group(Codec.INT.fieldOf("min_inclusive").forGetter(p_146381_ -> p_146381_.minInclusive), Codec.INT.fieldOf("max_inclusive").forGetter(p_146378_ -> p_146378_.maxInclusive)).apply(p_146373_, BiasedToBottomInt::new)).comapFlatMap(p_274930_ -> p_274930_.maxInclusive < p_274930_.minInclusive ? DataResult.error(() -> "Max must be at least min, min_inclusive: " + p_274930_.minInclusive + ", max_inclusive: " + p_274930_.maxInclusive) : DataResult.success(p_274930_), Function.identity());

    private final int minInclusive;

    private final int maxInclusive;

    private BiasedToBottomInt(int int0, int int1) {
        this.minInclusive = int0;
        this.maxInclusive = int1;
    }

    public static BiasedToBottomInt of(int int0, int int1) {
        return new BiasedToBottomInt(int0, int1);
    }

    @Override
    public int sample(RandomSource randomSource0) {
        return this.minInclusive + randomSource0.nextInt(randomSource0.nextInt(this.maxInclusive - this.minInclusive + 1) + 1);
    }

    @Override
    public int getMinValue() {
        return this.minInclusive;
    }

    @Override
    public int getMaxValue() {
        return this.maxInclusive;
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderType.BIASED_TO_BOTTOM;
    }

    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}