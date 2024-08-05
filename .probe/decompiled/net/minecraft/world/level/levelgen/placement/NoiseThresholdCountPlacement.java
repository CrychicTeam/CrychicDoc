package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;

public class NoiseThresholdCountPlacement extends RepeatingPlacement {

    public static final Codec<NoiseThresholdCountPlacement> CODEC = RecordCodecBuilder.create(p_191761_ -> p_191761_.group(Codec.DOUBLE.fieldOf("noise_level").forGetter(p_191771_ -> p_191771_.noiseLevel), Codec.INT.fieldOf("below_noise").forGetter(p_191769_ -> p_191769_.belowNoise), Codec.INT.fieldOf("above_noise").forGetter(p_191763_ -> p_191763_.aboveNoise)).apply(p_191761_, NoiseThresholdCountPlacement::new));

    private final double noiseLevel;

    private final int belowNoise;

    private final int aboveNoise;

    private NoiseThresholdCountPlacement(double double0, int int1, int int2) {
        this.noiseLevel = double0;
        this.belowNoise = int1;
        this.aboveNoise = int2;
    }

    public static NoiseThresholdCountPlacement of(double double0, int int1, int int2) {
        return new NoiseThresholdCountPlacement(double0, int1, int2);
    }

    @Override
    protected int count(RandomSource randomSource0, BlockPos blockPos1) {
        double $$2 = Biome.BIOME_INFO_NOISE.getValue((double) blockPos1.m_123341_() / 200.0, (double) blockPos1.m_123343_() / 200.0, false);
        return $$2 < this.noiseLevel ? this.belowNoise : this.aboveNoise;
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.NOISE_THRESHOLD_COUNT;
    }
}