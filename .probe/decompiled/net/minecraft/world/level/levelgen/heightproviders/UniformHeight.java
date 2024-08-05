package net.minecraft.world.level.levelgen.heightproviders;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.slf4j.Logger;

public class UniformHeight extends HeightProvider {

    public static final Codec<UniformHeight> CODEC = RecordCodecBuilder.create(p_162033_ -> p_162033_.group(VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter(p_162043_ -> p_162043_.minInclusive), VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter(p_162038_ -> p_162038_.maxInclusive)).apply(p_162033_, UniformHeight::new));

    private static final Logger LOGGER = LogUtils.getLogger();

    private final VerticalAnchor minInclusive;

    private final VerticalAnchor maxInclusive;

    private final LongSet warnedFor = new LongOpenHashSet();

    private UniformHeight(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1) {
        this.minInclusive = verticalAnchor0;
        this.maxInclusive = verticalAnchor1;
    }

    public static UniformHeight of(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1) {
        return new UniformHeight(verticalAnchor0, verticalAnchor1);
    }

    @Override
    public int sample(RandomSource randomSource0, WorldGenerationContext worldGenerationContext1) {
        int $$2 = this.minInclusive.resolveY(worldGenerationContext1);
        int $$3 = this.maxInclusive.resolveY(worldGenerationContext1);
        if ($$2 > $$3) {
            if (this.warnedFor.add((long) $$2 << 32 | (long) $$3)) {
                LOGGER.warn("Empty height range: {}", this);
            }
            return $$2;
        } else {
            return Mth.randomBetweenInclusive(randomSource0, $$2, $$3);
        }
    }

    @Override
    public HeightProviderType<?> getType() {
        return HeightProviderType.UNIFORM;
    }

    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}