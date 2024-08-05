package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;

public class NoiseBasedCountPlacement extends RepeatingPlacement {

    public static final Codec<NoiseBasedCountPlacement> CODEC = RecordCodecBuilder.create(p_191736_ -> p_191736_.group(Codec.INT.fieldOf("noise_to_count_ratio").forGetter(p_191746_ -> p_191746_.noiseToCountRatio), Codec.DOUBLE.fieldOf("noise_factor").forGetter(p_191744_ -> p_191744_.noiseFactor), Codec.DOUBLE.fieldOf("noise_offset").orElse(0.0).forGetter(p_191738_ -> p_191738_.noiseOffset)).apply(p_191736_, NoiseBasedCountPlacement::new));

    private final int noiseToCountRatio;

    private final double noiseFactor;

    private final double noiseOffset;

    private NoiseBasedCountPlacement(int int0, double double1, double double2) {
        this.noiseToCountRatio = int0;
        this.noiseFactor = double1;
        this.noiseOffset = double2;
    }

    public static NoiseBasedCountPlacement of(int int0, double double1, double double2) {
        return new NoiseBasedCountPlacement(int0, double1, double2);
    }

    @Override
    protected int count(RandomSource randomSource0, BlockPos blockPos1) {
        double $$2 = Biome.BIOME_INFO_NOISE.getValue((double) blockPos1.m_123341_() / this.noiseFactor, (double) blockPos1.m_123343_() / this.noiseFactor, false);
        return (int) Math.ceil(($$2 + this.noiseOffset) * (double) this.noiseToCountRatio);
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.NOISE_BASED_COUNT;
    }
}