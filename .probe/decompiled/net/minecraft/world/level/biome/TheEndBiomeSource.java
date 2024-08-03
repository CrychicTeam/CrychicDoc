package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.levelgen.DensityFunction;

public class TheEndBiomeSource extends BiomeSource {

    public static final Codec<TheEndBiomeSource> CODEC = RecordCodecBuilder.create(p_255555_ -> p_255555_.group(RegistryOps.retrieveElement(Biomes.THE_END), RegistryOps.retrieveElement(Biomes.END_HIGHLANDS), RegistryOps.retrieveElement(Biomes.END_MIDLANDS), RegistryOps.retrieveElement(Biomes.SMALL_END_ISLANDS), RegistryOps.retrieveElement(Biomes.END_BARRENS)).apply(p_255555_, p_255555_.stable(TheEndBiomeSource::new)));

    private final Holder<Biome> end;

    private final Holder<Biome> highlands;

    private final Holder<Biome> midlands;

    private final Holder<Biome> islands;

    private final Holder<Biome> barrens;

    public static TheEndBiomeSource create(HolderGetter<Biome> holderGetterBiome0) {
        return new TheEndBiomeSource(holderGetterBiome0.getOrThrow(Biomes.THE_END), holderGetterBiome0.getOrThrow(Biomes.END_HIGHLANDS), holderGetterBiome0.getOrThrow(Biomes.END_MIDLANDS), holderGetterBiome0.getOrThrow(Biomes.SMALL_END_ISLANDS), holderGetterBiome0.getOrThrow(Biomes.END_BARRENS));
    }

    private TheEndBiomeSource(Holder<Biome> holderBiome0, Holder<Biome> holderBiome1, Holder<Biome> holderBiome2, Holder<Biome> holderBiome3, Holder<Biome> holderBiome4) {
        this.end = holderBiome0;
        this.highlands = holderBiome1;
        this.midlands = holderBiome2;
        this.islands = holderBiome3;
        this.barrens = holderBiome4;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.of(this.end, this.highlands, this.midlands, this.islands, this.barrens);
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int int0, int int1, int int2, Climate.Sampler climateSampler3) {
        int $$4 = QuartPos.toBlock(int0);
        int $$5 = QuartPos.toBlock(int1);
        int $$6 = QuartPos.toBlock(int2);
        int $$7 = SectionPos.blockToSectionCoord($$4);
        int $$8 = SectionPos.blockToSectionCoord($$6);
        if ((long) $$7 * (long) $$7 + (long) $$8 * (long) $$8 <= 4096L) {
            return this.end;
        } else {
            int $$9 = (SectionPos.blockToSectionCoord($$4) * 2 + 1) * 8;
            int $$10 = (SectionPos.blockToSectionCoord($$6) * 2 + 1) * 8;
            double $$11 = climateSampler3.erosion().compute(new DensityFunction.SinglePointContext($$9, $$5, $$10));
            if ($$11 > 0.25) {
                return this.highlands;
            } else if ($$11 >= -0.0625) {
                return this.midlands;
            } else {
                return $$11 < -0.21875 ? this.islands : this.barrens;
            }
        }
    }
}