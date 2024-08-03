package net.minecraft.util.valueproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.util.RandomSource;

public class TrapezoidFloat extends FloatProvider {

    public static final Codec<TrapezoidFloat> CODEC = RecordCodecBuilder.create(p_146578_ -> p_146578_.group(Codec.FLOAT.fieldOf("min").forGetter(p_146588_ -> p_146588_.min), Codec.FLOAT.fieldOf("max").forGetter(p_146586_ -> p_146586_.max), Codec.FLOAT.fieldOf("plateau").forGetter(p_146583_ -> p_146583_.plateau)).apply(p_146578_, TrapezoidFloat::new)).comapFlatMap(p_274953_ -> {
        if (p_274953_.max < p_274953_.min) {
            return DataResult.error(() -> "Max must be larger than min: [" + p_274953_.min + ", " + p_274953_.max + "]");
        } else {
            return p_274953_.plateau > p_274953_.max - p_274953_.min ? DataResult.error(() -> "Plateau can at most be the full span: [" + p_274953_.min + ", " + p_274953_.max + "]") : DataResult.success(p_274953_);
        }
    }, Function.identity());

    private final float min;

    private final float max;

    private final float plateau;

    public static TrapezoidFloat of(float float0, float float1, float float2) {
        return new TrapezoidFloat(float0, float1, float2);
    }

    private TrapezoidFloat(float float0, float float1, float float2) {
        this.min = float0;
        this.max = float1;
        this.plateau = float2;
    }

    @Override
    public float sample(RandomSource randomSource0) {
        float $$1 = this.max - this.min;
        float $$2 = ($$1 - this.plateau) / 2.0F;
        float $$3 = $$1 - $$2;
        return this.min + randomSource0.nextFloat() * $$3 + randomSource0.nextFloat() * $$2;
    }

    @Override
    public float getMinValue() {
        return this.min;
    }

    @Override
    public float getMaxValue() {
        return this.max;
    }

    @Override
    public FloatProviderType<?> getType() {
        return FloatProviderType.TRAPEZOID;
    }

    public String toString() {
        return "trapezoid(" + this.plateau + ") in [" + this.min + "-" + this.max + "]";
    }
}