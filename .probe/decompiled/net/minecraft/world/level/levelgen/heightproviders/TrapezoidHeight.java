package net.minecraft.world.level.levelgen.heightproviders;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.slf4j.Logger;

public class TrapezoidHeight extends HeightProvider {

    public static final Codec<TrapezoidHeight> CODEC = RecordCodecBuilder.create(p_162005_ -> p_162005_.group(VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(p_162021_ -> p_162021_.minInclusive), VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(p_162019_ -> p_162019_.maxInclusive), Codec.INT.optionalFieldOf("plateau", 0).forGetter(p_162014_ -> p_162014_.plateau)).apply(p_162005_, TrapezoidHeight::new));

    private static final Logger LOGGER = LogUtils.getLogger();

    private final VerticalAnchor minInclusive;

    private final VerticalAnchor maxInclusive;

    private final int plateau;

    private TrapezoidHeight(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1, int int2) {
        this.minInclusive = verticalAnchor0;
        this.maxInclusive = verticalAnchor1;
        this.plateau = int2;
    }

    public static TrapezoidHeight of(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1, int int2) {
        return new TrapezoidHeight(verticalAnchor0, verticalAnchor1, int2);
    }

    public static TrapezoidHeight of(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1) {
        return of(verticalAnchor0, verticalAnchor1, 0);
    }

    @Override
    public int sample(RandomSource randomSource0, WorldGenerationContext worldGenerationContext1) {
        int $$2 = this.minInclusive.resolveY(worldGenerationContext1);
        int $$3 = this.maxInclusive.resolveY(worldGenerationContext1);
        if ($$2 > $$3) {
            LOGGER.warn("Empty height range: {}", this);
            return $$2;
        } else {
            int $$4 = $$3 - $$2;
            if (this.plateau >= $$4) {
                return Mth.randomBetweenInclusive(randomSource0, $$2, $$3);
            } else {
                int $$5 = ($$4 - this.plateau) / 2;
                int $$6 = $$4 - $$5;
                return $$2 + Mth.randomBetweenInclusive(randomSource0, 0, $$6) + Mth.randomBetweenInclusive(randomSource0, 0, $$5);
            }
        }
    }

    @Override
    public HeightProviderType<?> getType() {
        return HeightProviderType.TRAPEZOID;
    }

    public String toString() {
        return this.plateau == 0 ? "triangle (" + this.minInclusive + "-" + this.maxInclusive + ")" : "trapezoid(" + this.plateau + ") in [" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}