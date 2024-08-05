package net.minecraft.util.valueproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class ClampedNormalInt extends IntProvider {

    public static final Codec<ClampedNormalInt> CODEC = RecordCodecBuilder.create(p_185887_ -> p_185887_.group(Codec.FLOAT.fieldOf("mean").forGetter(p_185905_ -> p_185905_.mean), Codec.FLOAT.fieldOf("deviation").forGetter(p_185903_ -> p_185903_.deviation), Codec.INT.fieldOf("min_inclusive").forGetter(p_185901_ -> p_185901_.min_inclusive), Codec.INT.fieldOf("max_inclusive").forGetter(p_185898_ -> p_185898_.max_inclusive)).apply(p_185887_, ClampedNormalInt::new)).comapFlatMap(p_274937_ -> p_274937_.max_inclusive < p_274937_.min_inclusive ? DataResult.error(() -> "Max must be larger than min: [" + p_274937_.min_inclusive + ", " + p_274937_.max_inclusive + "]") : DataResult.success(p_274937_), Function.identity());

    private final float mean;

    private final float deviation;

    private final int min_inclusive;

    private final int max_inclusive;

    public static ClampedNormalInt of(float float0, float float1, int int2, int int3) {
        return new ClampedNormalInt(float0, float1, int2, int3);
    }

    private ClampedNormalInt(float float0, float float1, int int2, int int3) {
        this.mean = float0;
        this.deviation = float1;
        this.min_inclusive = int2;
        this.max_inclusive = int3;
    }

    @Override
    public int sample(RandomSource randomSource0) {
        return sample(randomSource0, this.mean, this.deviation, (float) this.min_inclusive, (float) this.max_inclusive);
    }

    public static int sample(RandomSource randomSource0, float float1, float float2, float float3, float float4) {
        return (int) Mth.clamp(Mth.normal(randomSource0, float1, float2), float3, float4);
    }

    @Override
    public int getMinValue() {
        return this.min_inclusive;
    }

    @Override
    public int getMaxValue() {
        return this.max_inclusive;
    }

    @Override
    public IntProviderType<?> getType() {
        return IntProviderType.CLAMPED_NORMAL;
    }

    public String toString() {
        return "normal(" + this.mean + ", " + this.deviation + ") in [" + this.min_inclusive + "-" + this.max_inclusive + "]";
    }
}