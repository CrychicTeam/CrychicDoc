package net.minecraft.util.valueproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class UniformFloat extends FloatProvider {

    public static final Codec<UniformFloat> CODEC = RecordCodecBuilder.create(p_146601_ -> p_146601_.group(Codec.FLOAT.fieldOf("min_inclusive").forGetter(p_146612_ -> p_146612_.minInclusive), Codec.FLOAT.fieldOf("max_exclusive").forGetter(p_146609_ -> p_146609_.maxExclusive)).apply(p_146601_, UniformFloat::new)).comapFlatMap(p_274956_ -> p_274956_.maxExclusive <= p_274956_.minInclusive ? DataResult.error(() -> "Max must be larger than min, min_inclusive: " + p_274956_.minInclusive + ", max_exclusive: " + p_274956_.maxExclusive) : DataResult.success(p_274956_), Function.identity());

    private final float minInclusive;

    private final float maxExclusive;

    private UniformFloat(float float0, float float1) {
        this.minInclusive = float0;
        this.maxExclusive = float1;
    }

    public static UniformFloat of(float float0, float float1) {
        if (float1 <= float0) {
            throw new IllegalArgumentException("Max must exceed min");
        } else {
            return new UniformFloat(float0, float1);
        }
    }

    @Override
    public float sample(RandomSource randomSource0) {
        return Mth.randomBetween(randomSource0, this.minInclusive, this.maxExclusive);
    }

    @Override
    public float getMinValue() {
        return this.minInclusive;
    }

    @Override
    public float getMaxValue() {
        return this.maxExclusive;
    }

    @Override
    public FloatProviderType<?> getType() {
        return FloatProviderType.UNIFORM;
    }

    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxExclusive + "]";
    }
}