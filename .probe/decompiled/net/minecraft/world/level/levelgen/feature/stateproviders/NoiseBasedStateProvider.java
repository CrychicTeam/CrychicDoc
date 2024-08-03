package net.minecraft.world.level.levelgen.feature.stateproviders;

import com.mojang.datafixers.Products.P3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public abstract class NoiseBasedStateProvider extends BlockStateProvider {

    protected final long seed;

    protected final NormalNoise.NoiseParameters parameters;

    protected final float scale;

    protected final NormalNoise noise;

    protected static <P extends NoiseBasedStateProvider> P3<Mu<P>, Long, NormalNoise.NoiseParameters, Float> noiseCodec(Instance<P> instanceP0) {
        return instanceP0.group(Codec.LONG.fieldOf("seed").forGetter(p_191435_ -> p_191435_.seed), NormalNoise.NoiseParameters.DIRECT_CODEC.fieldOf("noise").forGetter(p_191433_ -> p_191433_.parameters), ExtraCodecs.POSITIVE_FLOAT.fieldOf("scale").forGetter(p_191428_ -> p_191428_.scale));
    }

    protected NoiseBasedStateProvider(long long0, NormalNoise.NoiseParameters normalNoiseNoiseParameters1, float float2) {
        this.seed = long0;
        this.parameters = normalNoiseNoiseParameters1;
        this.scale = float2;
        this.noise = NormalNoise.create(new WorldgenRandom(new LegacyRandomSource(long0)), normalNoiseNoiseParameters1);
    }

    protected double getNoiseValue(BlockPos blockPos0, double double1) {
        return this.noise.getValue((double) blockPos0.m_123341_() * double1, (double) blockPos0.m_123342_() * double1, (double) blockPos0.m_123343_() * double1);
    }
}