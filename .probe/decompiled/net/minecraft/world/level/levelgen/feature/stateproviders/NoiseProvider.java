package net.minecraft.world.level.levelgen.feature.stateproviders;

import com.mojang.datafixers.Products.P4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoiseProvider extends NoiseBasedStateProvider {

    public static final Codec<NoiseProvider> CODEC = RecordCodecBuilder.create(p_191462_ -> noiseProviderCodec(p_191462_).apply(p_191462_, NoiseProvider::new));

    protected final List<BlockState> states;

    protected static <P extends NoiseProvider> P4<Mu<P>, Long, NormalNoise.NoiseParameters, Float, List<BlockState>> noiseProviderCodec(Instance<P> instanceP0) {
        return m_191425_(instanceP0).and(Codec.list(BlockState.CODEC).fieldOf("states").forGetter(p_191448_ -> p_191448_.states));
    }

    public NoiseProvider(long long0, NormalNoise.NoiseParameters normalNoiseNoiseParameters1, float float2, List<BlockState> listBlockState3) {
        super(long0, normalNoiseNoiseParameters1, float2);
        this.states = listBlockState3;
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.NOISE_PROVIDER;
    }

    @Override
    public BlockState getState(RandomSource randomSource0, BlockPos blockPos1) {
        return this.getRandomState(this.states, blockPos1, (double) this.f_191419_);
    }

    protected BlockState getRandomState(List<BlockState> listBlockState0, BlockPos blockPos1, double double2) {
        double $$3 = this.m_191429_(blockPos1, double2);
        return this.getRandomState(listBlockState0, $$3);
    }

    protected BlockState getRandomState(List<BlockState> listBlockState0, double double1) {
        double $$2 = Mth.clamp((1.0 + double1) / 2.0, 0.0, 0.9999);
        return (BlockState) listBlockState0.get((int) ($$2 * (double) listBlockState0.size()));
    }
}