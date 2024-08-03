package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;

public class CheckerboardColumnBiomeSource extends BiomeSource {

    public static final Codec<CheckerboardColumnBiomeSource> CODEC = RecordCodecBuilder.create(p_48244_ -> p_48244_.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(p_204246_ -> p_204246_.allowedBiomes), Codec.intRange(0, 62).fieldOf("scale").orElse(2).forGetter(p_151788_ -> p_151788_.size)).apply(p_48244_, CheckerboardColumnBiomeSource::new));

    private final HolderSet<Biome> allowedBiomes;

    private final int bitShift;

    private final int size;

    public CheckerboardColumnBiomeSource(HolderSet<Biome> holderSetBiome0, int int1) {
        this.allowedBiomes = holderSetBiome0;
        this.bitShift = int1 + 2;
        this.size = int1;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return this.allowedBiomes.stream();
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int int0, int int1, int int2, Climate.Sampler climateSampler3) {
        return this.allowedBiomes.get(Math.floorMod((int0 >> this.bitShift) + (int2 >> this.bitShift), this.allowedBiomes.size()));
    }
}