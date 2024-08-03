package net.minecraft.util.valueproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class UniformInt extends IntProvider {

    public static final Codec<UniformInt> CODEC = RecordCodecBuilder.create(p_146628_ -> p_146628_.group(Codec.INT.fieldOf("min_inclusive").forGetter(p_146636_ -> p_146636_.minInclusive), Codec.INT.fieldOf("max_inclusive").forGetter(p_146633_ -> p_146633_.maxInclusive)).apply(p_146628_, UniformInt::new)).comapFlatMap(p_274957_ -> p_274957_.maxInclusive < p_274957_.minInclusive ? DataResult.error(() -> "Max must be at least min, min_inclusive: " + p_274957_.minInclusive + ", max_inclusive: " + p_274957_.maxInclusive) : DataResult.success(p_274957_), Function.identity());

    private final int minInclusive;

    private final int maxInclusive;

    private UniformInt(int int0, int int1) {
        this.minInclusive = int0;
        this.maxInclusive = int1;
    }

    public static UniformInt of(int int0, int int1) {
        return new UniformInt(int0, int1);
    }

    @Override
    public int sample(RandomSource randomSource0) {
        return Mth.randomBetweenInclusive(randomSource0, this.minInclusive, this.maxInclusive);
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
        return IntProviderType.UNIFORM;
    }

    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}