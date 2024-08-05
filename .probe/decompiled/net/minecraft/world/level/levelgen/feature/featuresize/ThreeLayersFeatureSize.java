package net.minecraft.world.level.levelgen.feature.featuresize;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;

public class ThreeLayersFeatureSize extends FeatureSize {

    public static final Codec<ThreeLayersFeatureSize> CODEC = RecordCodecBuilder.create(p_68326_ -> p_68326_.group(Codec.intRange(0, 80).fieldOf("limit").orElse(1).forGetter(p_161335_ -> p_161335_.limit), Codec.intRange(0, 80).fieldOf("upper_limit").orElse(1).forGetter(p_161333_ -> p_161333_.upperLimit), Codec.intRange(0, 16).fieldOf("lower_size").orElse(0).forGetter(p_161331_ -> p_161331_.lowerSize), Codec.intRange(0, 16).fieldOf("middle_size").orElse(1).forGetter(p_161329_ -> p_161329_.middleSize), Codec.intRange(0, 16).fieldOf("upper_size").orElse(1).forGetter(p_161327_ -> p_161327_.upperSize), m_68286_()).apply(p_68326_, ThreeLayersFeatureSize::new));

    private final int limit;

    private final int upperLimit;

    private final int lowerSize;

    private final int middleSize;

    private final int upperSize;

    public ThreeLayersFeatureSize(int int0, int int1, int int2, int int3, int int4, OptionalInt optionalInt5) {
        super(optionalInt5);
        this.limit = int0;
        this.upperLimit = int1;
        this.lowerSize = int2;
        this.middleSize = int3;
        this.upperSize = int4;
    }

    @Override
    protected FeatureSizeType<?> type() {
        return FeatureSizeType.THREE_LAYERS_FEATURE_SIZE;
    }

    @Override
    public int getSizeAtHeight(int int0, int int1) {
        if (int1 < this.limit) {
            return this.lowerSize;
        } else {
            return int1 >= int0 - this.upperLimit ? this.upperSize : this.middleSize;
        }
    }
}