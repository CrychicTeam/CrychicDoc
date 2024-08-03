package net.minecraft.world.level.biome;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;

public class FixedBiomeSource extends BiomeSource implements BiomeManager.NoiseBiomeSource {

    public static final Codec<FixedBiomeSource> CODEC = Biome.CODEC.fieldOf("biome").xmap(FixedBiomeSource::new, p_204259_ -> p_204259_.biome).stable().codec();

    private final Holder<Biome> biome;

    public FixedBiomeSource(Holder<Biome> holderBiome0) {
        this.biome = holderBiome0;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.of(this.biome);
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int int0, int int1, int int2, Climate.Sampler climateSampler3) {
        return this.biome;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int int0, int int1, int int2) {
        return this.biome;
    }

    @Nullable
    @Override
    public Pair<BlockPos, Holder<Biome>> findBiomeHorizontal(int int0, int int1, int int2, int int3, int int4, Predicate<Holder<Biome>> predicateHolderBiome5, RandomSource randomSource6, boolean boolean7, Climate.Sampler climateSampler8) {
        if (predicateHolderBiome5.test(this.biome)) {
            return boolean7 ? Pair.of(new BlockPos(int0, int1, int2), this.biome) : Pair.of(new BlockPos(int0 - int3 + randomSource6.nextInt(int3 * 2 + 1), int1, int2 - int3 + randomSource6.nextInt(int3 * 2 + 1)), this.biome);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Pair<BlockPos, Holder<Biome>> findClosestBiome3d(BlockPos blockPos0, int int1, int int2, int int3, Predicate<Holder<Biome>> predicateHolderBiome4, Climate.Sampler climateSampler5, LevelReader levelReader6) {
        return predicateHolderBiome4.test(this.biome) ? Pair.of(blockPos0, this.biome) : null;
    }

    @Override
    public Set<Holder<Biome>> getBiomesWithin(int int0, int int1, int int2, int int3, Climate.Sampler climateSampler4) {
        return Sets.newHashSet(Set.of(this.biome));
    }
}