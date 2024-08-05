package net.minecraft.world.level.levelgen.heightproviders;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.slf4j.Logger;

public class VeryBiasedToBottomHeight extends HeightProvider {

    public static final Codec<VeryBiasedToBottomHeight> CODEC = RecordCodecBuilder.create(p_162057_ -> p_162057_.group(VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(p_162070_ -> p_162070_.minInclusive), VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(p_162068_ -> p_162068_.maxInclusive), Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("inner", 1).forGetter(p_162063_ -> p_162063_.inner)).apply(p_162057_, VeryBiasedToBottomHeight::new));

    private static final Logger LOGGER = LogUtils.getLogger();

    private final VerticalAnchor minInclusive;

    private final VerticalAnchor maxInclusive;

    private final int inner;

    private VeryBiasedToBottomHeight(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1, int int2) {
        this.minInclusive = verticalAnchor0;
        this.maxInclusive = verticalAnchor1;
        this.inner = int2;
    }

    public static VeryBiasedToBottomHeight of(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1, int int2) {
        return new VeryBiasedToBottomHeight(verticalAnchor0, verticalAnchor1, int2);
    }

    @Override
    public int sample(RandomSource randomSource0, WorldGenerationContext worldGenerationContext1) {
        int $$2 = this.minInclusive.resolveY(worldGenerationContext1);
        int $$3 = this.maxInclusive.resolveY(worldGenerationContext1);
        if ($$3 - $$2 - this.inner + 1 <= 0) {
            LOGGER.warn("Empty height range: {}", this);
            return $$2;
        } else {
            int $$4 = Mth.nextInt(randomSource0, $$2 + this.inner, $$3);
            int $$5 = Mth.nextInt(randomSource0, $$2, $$4 - 1);
            return Mth.nextInt(randomSource0, $$2, $$5 - 1 + this.inner);
        }
    }

    @Override
    public HeightProviderType<?> getType() {
        return HeightProviderType.VERY_BIASED_TO_BOTTOM;
    }

    public String toString() {
        return "biased[" + this.minInclusive + "-" + this.maxInclusive + " inner: " + this.inner + "]";
    }
}