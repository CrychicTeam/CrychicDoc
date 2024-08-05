package net.minecraft.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoiseThresholdProvider extends NoiseBasedStateProvider {

    public static final Codec<NoiseThresholdProvider> CODEC = RecordCodecBuilder.create(p_191486_ -> m_191425_(p_191486_).and(p_191486_.group(Codec.floatRange(-1.0F, 1.0F).fieldOf("threshold").forGetter(p_191494_ -> p_191494_.threshold), Codec.floatRange(0.0F, 1.0F).fieldOf("high_chance").forGetter(p_191492_ -> p_191492_.highChance), BlockState.CODEC.fieldOf("default_state").forGetter(p_191490_ -> p_191490_.defaultState), Codec.list(BlockState.CODEC).fieldOf("low_states").forGetter(p_191488_ -> p_191488_.lowStates), Codec.list(BlockState.CODEC).fieldOf("high_states").forGetter(p_191481_ -> p_191481_.highStates))).apply(p_191486_, NoiseThresholdProvider::new));

    private final float threshold;

    private final float highChance;

    private final BlockState defaultState;

    private final List<BlockState> lowStates;

    private final List<BlockState> highStates;

    public NoiseThresholdProvider(long long0, NormalNoise.NoiseParameters normalNoiseNoiseParameters1, float float2, float float3, float float4, BlockState blockState5, List<BlockState> listBlockState6, List<BlockState> listBlockState7) {
        super(long0, normalNoiseNoiseParameters1, float2);
        this.threshold = float3;
        this.highChance = float4;
        this.defaultState = blockState5;
        this.lowStates = listBlockState6;
        this.highStates = listBlockState7;
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.NOISE_THRESHOLD_PROVIDER;
    }

    @Override
    public BlockState getState(RandomSource randomSource0, BlockPos blockPos1) {
        double $$2 = this.m_191429_(blockPos1, (double) this.f_191419_);
        if ($$2 < (double) this.threshold) {
            return Util.getRandom(this.lowStates, randomSource0);
        } else {
            return randomSource0.nextFloat() < this.highChance ? Util.getRandom(this.highStates, randomSource0) : this.defaultState;
        }
    }
}