package net.minecraft.world.level.levelgen.heightproviders;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.slf4j.Logger;

public class BiasedToBottomHeight extends HeightProvider {

    public static final Codec<BiasedToBottomHeight> CODEC = RecordCodecBuilder.create(p_161930_ -> p_161930_.group(VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(p_161943_ -> p_161943_.minInclusive), VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(p_161941_ -> p_161941_.maxInclusive), Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("inner", 1).forGetter(p_161936_ -> p_161936_.inner)).apply(p_161930_, BiasedToBottomHeight::new));

    private static final Logger LOGGER = LogUtils.getLogger();

    private final VerticalAnchor minInclusive;

    private final VerticalAnchor maxInclusive;

    private final int inner;

    private BiasedToBottomHeight(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1, int int2) {
        this.minInclusive = verticalAnchor0;
        this.maxInclusive = verticalAnchor1;
        this.inner = int2;
    }

    public static BiasedToBottomHeight of(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1, int int2) {
        return new BiasedToBottomHeight(verticalAnchor0, verticalAnchor1, int2);
    }

    @Override
    public int sample(RandomSource randomSource0, WorldGenerationContext worldGenerationContext1) {
        int $$2 = this.minInclusive.resolveY(worldGenerationContext1);
        int $$3 = this.maxInclusive.resolveY(worldGenerationContext1);
        if ($$3 - $$2 - this.inner + 1 <= 0) {
            LOGGER.warn("Empty height range: {}", this);
            return $$2;
        } else {
            int $$4 = randomSource0.nextInt($$3 - $$2 - this.inner + 1);
            return randomSource0.nextInt($$4 + this.inner) + $$2;
        }
    }

    @Override
    public HeightProviderType<?> getType() {
        return HeightProviderType.BIASED_TO_BOTTOM;
    }

    public String toString() {
        return "biased[" + this.minInclusive + "-" + this.maxInclusive + " inner: " + this.inner + "]";
    }
}