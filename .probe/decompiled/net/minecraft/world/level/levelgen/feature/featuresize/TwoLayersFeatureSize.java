package net.minecraft.world.level.levelgen.feature.featuresize;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;

public class TwoLayersFeatureSize extends FeatureSize {

    public static final Codec<TwoLayersFeatureSize> CODEC = RecordCodecBuilder.create(p_68356_ -> p_68356_.group(Codec.intRange(0, 81).fieldOf("limit").orElse(1).forGetter(p_161341_ -> p_161341_.limit), Codec.intRange(0, 16).fieldOf("lower_size").orElse(0).forGetter(p_161339_ -> p_161339_.lowerSize), Codec.intRange(0, 16).fieldOf("upper_size").orElse(1).forGetter(p_161337_ -> p_161337_.upperSize), m_68286_()).apply(p_68356_, TwoLayersFeatureSize::new));

    private final int limit;

    private final int lowerSize;

    private final int upperSize;

    public TwoLayersFeatureSize(int int0, int int1, int int2) {
        this(int0, int1, int2, OptionalInt.empty());
    }

    public TwoLayersFeatureSize(int int0, int int1, int int2, OptionalInt optionalInt3) {
        super(optionalInt3);
        this.limit = int0;
        this.lowerSize = int1;
        this.upperSize = int2;
    }

    @Override
    protected FeatureSizeType<?> type() {
        return FeatureSizeType.TWO_LAYERS_FEATURE_SIZE;
    }

    @Override
    public int getSizeAtHeight(int int0, int int1) {
        return int1 < this.limit ? this.lowerSize : this.upperSize;
    }
}