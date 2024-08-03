package net.minecraft.world.level.levelgen.feature.stateproviders;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class DualNoiseProvider extends NoiseProvider {

    public static final Codec<DualNoiseProvider> CODEC = RecordCodecBuilder.create(p_191414_ -> p_191414_.group(InclusiveRange.codec(Codec.INT, 1, 64).fieldOf("variety").forGetter(p_191416_ -> p_191416_.variety), NormalNoise.NoiseParameters.DIRECT_CODEC.fieldOf("slow_noise").forGetter(p_191412_ -> p_191412_.slowNoiseParameters), ExtraCodecs.POSITIVE_FLOAT.fieldOf("slow_scale").forGetter(p_191405_ -> p_191405_.slowScale)).and(m_191459_(p_191414_)).apply(p_191414_, DualNoiseProvider::new));

    private final InclusiveRange<Integer> variety;

    private final NormalNoise.NoiseParameters slowNoiseParameters;

    private final float slowScale;

    private final NormalNoise slowNoise;

    public DualNoiseProvider(InclusiveRange<Integer> inclusiveRangeInteger0, NormalNoise.NoiseParameters normalNoiseNoiseParameters1, float float2, long long3, NormalNoise.NoiseParameters normalNoiseNoiseParameters4, float float5, List<BlockState> listBlockState6) {
        super(long3, normalNoiseNoiseParameters4, float5, listBlockState6);
        this.variety = inclusiveRangeInteger0;
        this.slowNoiseParameters = normalNoiseNoiseParameters1;
        this.slowScale = float2;
        this.slowNoise = NormalNoise.create(new WorldgenRandom(new LegacyRandomSource(long3)), normalNoiseNoiseParameters1);
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.DUAL_NOISE_PROVIDER;
    }

    @Override
    public BlockState getState(RandomSource randomSource0, BlockPos blockPos1) {
        double $$2 = this.getSlowNoiseValue(blockPos1);
        int $$3 = (int) Mth.clampedMap($$2, -1.0, 1.0, (double) ((Integer) this.variety.minInclusive()).intValue(), (double) ((Integer) this.variety.maxInclusive() + 1));
        List<BlockState> $$4 = Lists.newArrayListWithCapacity($$3);
        for (int $$5 = 0; $$5 < $$3; $$5++) {
            $$4.add(this.m_191449_(this.f_191439_, this.getSlowNoiseValue(blockPos1.offset($$5 * 54545, 0, $$5 * 34234))));
        }
        return this.m_191452_($$4, blockPos1, (double) this.f_191419_);
    }

    protected double getSlowNoiseValue(BlockPos blockPos0) {
        return this.slowNoise.getValue((double) ((float) blockPos0.m_123341_() * this.slowScale), (double) ((float) blockPos0.m_123342_() * this.slowScale), (double) ((float) blockPos0.m_123343_() * this.slowScale));
    }
}