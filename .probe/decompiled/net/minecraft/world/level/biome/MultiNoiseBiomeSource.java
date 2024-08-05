package net.minecraft.world.level.biome;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.levelgen.NoiseRouterData;

public class MultiNoiseBiomeSource extends BiomeSource {

    private static final MapCodec<Holder<Biome>> ENTRY_CODEC = Biome.CODEC.fieldOf("biome");

    public static final MapCodec<Climate.ParameterList<Holder<Biome>>> DIRECT_CODEC = Climate.ParameterList.codec(ENTRY_CODEC).fieldOf("biomes");

    private static final MapCodec<Holder<MultiNoiseBiomeSourceParameterList>> PRESET_CODEC = MultiNoiseBiomeSourceParameterList.CODEC.fieldOf("preset").withLifecycle(Lifecycle.stable());

    public static final Codec<MultiNoiseBiomeSource> CODEC = Codec.mapEither(DIRECT_CODEC, PRESET_CODEC).xmap(MultiNoiseBiomeSource::new, p_275170_ -> p_275170_.parameters).codec();

    private final Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> parameters;

    private MultiNoiseBiomeSource(Either<Climate.ParameterList<Holder<Biome>>, Holder<MultiNoiseBiomeSourceParameterList>> eitherClimateParameterListHolderBiomeHolderMultiNoiseBiomeSourceParameterList0) {
        this.parameters = eitherClimateParameterListHolderBiomeHolderMultiNoiseBiomeSourceParameterList0;
    }

    public static MultiNoiseBiomeSource createFromList(Climate.ParameterList<Holder<Biome>> climateParameterListHolderBiome0) {
        return new MultiNoiseBiomeSource(Either.left(climateParameterListHolderBiome0));
    }

    public static MultiNoiseBiomeSource createFromPreset(Holder<MultiNoiseBiomeSourceParameterList> holderMultiNoiseBiomeSourceParameterList0) {
        return new MultiNoiseBiomeSource(Either.right(holderMultiNoiseBiomeSourceParameterList0));
    }

    private Climate.ParameterList<Holder<Biome>> parameters() {
        return (Climate.ParameterList<Holder<Biome>>) this.parameters.map(p_275171_ -> p_275171_, p_275172_ -> ((MultiNoiseBiomeSourceParameterList) p_275172_.value()).parameters());
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return this.parameters().values().stream().map(Pair::getSecond);
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    public boolean stable(ResourceKey<MultiNoiseBiomeSourceParameterList> resourceKeyMultiNoiseBiomeSourceParameterList0) {
        Optional<Holder<MultiNoiseBiomeSourceParameterList>> $$1 = this.parameters.right();
        return $$1.isPresent() && ((Holder) $$1.get()).is(resourceKeyMultiNoiseBiomeSourceParameterList0);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int int0, int int1, int int2, Climate.Sampler climateSampler3) {
        return this.getNoiseBiome(climateSampler3.sample(int0, int1, int2));
    }

    @VisibleForDebug
    public Holder<Biome> getNoiseBiome(Climate.TargetPoint climateTargetPoint0) {
        return this.parameters().findValue(climateTargetPoint0);
    }

    @Override
    public void addDebugInfo(List<String> listString0, BlockPos blockPos1, Climate.Sampler climateSampler2) {
        int $$3 = QuartPos.fromBlock(blockPos1.m_123341_());
        int $$4 = QuartPos.fromBlock(blockPos1.m_123342_());
        int $$5 = QuartPos.fromBlock(blockPos1.m_123343_());
        Climate.TargetPoint $$6 = climateSampler2.sample($$3, $$4, $$5);
        float $$7 = Climate.unquantizeCoord($$6.continentalness());
        float $$8 = Climate.unquantizeCoord($$6.erosion());
        float $$9 = Climate.unquantizeCoord($$6.temperature());
        float $$10 = Climate.unquantizeCoord($$6.humidity());
        float $$11 = Climate.unquantizeCoord($$6.weirdness());
        double $$12 = (double) NoiseRouterData.peaksAndValleys($$11);
        OverworldBiomeBuilder $$13 = new OverworldBiomeBuilder();
        listString0.add("Biome builder PV: " + OverworldBiomeBuilder.getDebugStringForPeaksAndValleys($$12) + " C: " + $$13.getDebugStringForContinentalness((double) $$7) + " E: " + $$13.getDebugStringForErosion((double) $$8) + " T: " + $$13.getDebugStringForTemperature((double) $$9) + " H: " + $$13.getDebugStringForHumidity((double) $$10));
    }
}