package net.minecraft.util.valueproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class ClampedNormalFloat extends FloatProvider {

    public static final Codec<ClampedNormalFloat> CODEC = RecordCodecBuilder.create(p_146431_ -> p_146431_.group(Codec.FLOAT.fieldOf("mean").forGetter(p_146449_ -> p_146449_.mean), Codec.FLOAT.fieldOf("deviation").forGetter(p_146447_ -> p_146447_.deviation), Codec.FLOAT.fieldOf("min").forGetter(p_146445_ -> p_146445_.min), Codec.FLOAT.fieldOf("max").forGetter(p_146442_ -> p_146442_.max)).apply(p_146431_, ClampedNormalFloat::new)).comapFlatMap(p_274935_ -> p_274935_.max < p_274935_.min ? DataResult.error(() -> "Max must be larger than min: [" + p_274935_.min + ", " + p_274935_.max + "]") : DataResult.success(p_274935_), Function.identity());

    private final float mean;

    private final float deviation;

    private final float min;

    private final float max;

    public static ClampedNormalFloat of(float float0, float float1, float float2, float float3) {
        return new ClampedNormalFloat(float0, float1, float2, float3);
    }

    private ClampedNormalFloat(float float0, float float1, float float2, float float3) {
        this.mean = float0;
        this.deviation = float1;
        this.min = float2;
        this.max = float3;
    }

    @Override
    public float sample(RandomSource randomSource0) {
        return sample(randomSource0, this.mean, this.deviation, this.min, this.max);
    }

    public static float sample(RandomSource randomSource0, float float1, float float2, float float3, float float4) {
        return Mth.clamp(Mth.normal(randomSource0, float1, float2), float3, float4);
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
        return FloatProviderType.CLAMPED_NORMAL;
    }

    public String toString() {
        return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.min + "-" + this.max + "]";
    }
}