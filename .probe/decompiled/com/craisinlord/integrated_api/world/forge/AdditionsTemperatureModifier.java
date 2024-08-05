package com.craisinlord.integrated_api.world.forge;

import com.craisinlord.integrated_api.modinit.forge.IABiomeModifiers;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record AdditionsTemperatureModifier(HolderSet<Biome> biomes, Holder<PlacedFeature> feature, GenerationStep.Decoration step, AdditionsTemperatureModifier.TEMPERATURE_RANGE temperatureRange) implements BiomeModifier {

    public static Codec<AdditionsTemperatureModifier> CODEC = RecordCodecBuilder.create(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(AdditionsTemperatureModifier::biomes), PlacedFeature.CODEC.fieldOf("feature").forGetter(AdditionsTemperatureModifier::feature), GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AdditionsTemperatureModifier::step), StringRepresentable.fromEnum(AdditionsTemperatureModifier.TEMPERATURE_RANGE::values).fieldOf("biome_temperature_allowed").stable().forGetter(AdditionsTemperatureModifier::temperatureRange)).apply(builder, AdditionsTemperatureModifier::new));

    @Override
    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == BiomeModifier.Phase.ADD && this.biomes.contains(biome)) {
            Biome rawBiome = (Biome) biome.get();
            String biomeNamespace = ((ResourceKey) biome.unwrapKey().get()).location().getNamespace();
            String biomePath = ((ResourceKey) biome.unwrapKey().get()).location().getPath();
            switch(this.temperatureRange) {
                case WARM:
                    if (this.nameMatch(biomePath, "hot", "tropic", "warm") && !this.nameMatch(biomePath, "lukewarm") || !this.nameExactMatch(biomeNamespace, "minecraft") && rawBiome.getModifiedClimateSettings().temperature() >= 1.5F) {
                        builder.getGenerationSettings().m_255419_(this.step, this.feature);
                    }
                    break;
                case LUKEWARM:
                    if (this.nameMatch(biomePath, "lukewarm") || !this.nameExactMatch(biomeNamespace, "minecraft") && rawBiome.getModifiedClimateSettings().temperature() >= 0.9F && rawBiome.getModifiedClimateSettings().temperature() < 1.5F) {
                        builder.getGenerationSettings().m_255419_(this.step, this.feature);
                    }
                    break;
                case NEUTRAL:
                    if (!this.nameMatch(biomePath, "hot", "tropic", "warm", "cold", "chilly", "frozen", "snow", "ice", "frost") || !this.nameExactMatch(biomeNamespace, "minecraft") && rawBiome.getModifiedClimateSettings().temperature() >= 0.5F && rawBiome.getModifiedClimateSettings().temperature() < 0.9F) {
                        builder.getGenerationSettings().m_255419_(this.step, this.feature);
                    }
                    break;
                case COLD:
                    if (this.nameMatch(biomePath, "cold", "chilly") || !this.nameExactMatch(biomeNamespace, "minecraft") && rawBiome.getModifiedClimateSettings().temperature() >= 0.0F && rawBiome.getModifiedClimateSettings().temperature() < 0.5F) {
                        builder.getGenerationSettings().m_255419_(this.step, this.feature);
                    }
                    break;
                case FROZEN:
                    if (this.nameMatch(biomePath, "frozen", "snow", "ice", "frost") || !this.nameExactMatch(biomeNamespace, "minecraft") && rawBiome.getModifiedClimateSettings().temperature() < 0.0F) {
                        builder.getGenerationSettings().m_255419_(this.step, this.feature);
                    }
            }
        }
    }

    private boolean nameMatch(String biomeName, String... targetMatch) {
        return Arrays.stream(targetMatch).anyMatch(biomeName::contains);
    }

    private boolean nameExactMatch(String biomeName, String... targetMatch) {
        return Arrays.asList(targetMatch).contains(biomeName);
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return IABiomeModifiers.ADDITIONS_TEMPERATURE_MODIFIER.get();
    }

    public static enum TEMPERATURE_RANGE implements StringRepresentable {

        WARM("WARM"), LUKEWARM("LUKEWARM"), NEUTRAL("NEUTRAL"), COLD("COLD"), FROZEN("FROZEN");

        private final String name;

        private static final Map<String, AdditionsTemperatureModifier.TEMPERATURE_RANGE> BY_NAME = Util.make(Maps.newHashMap(), hashMap -> {
            AdditionsTemperatureModifier.TEMPERATURE_RANGE[] var1 = values();
            for (AdditionsTemperatureModifier.TEMPERATURE_RANGE type : var1) {
                hashMap.put(type.name, type);
            }
        });

        private TEMPERATURE_RANGE(String name) {
            this.name = name;
        }

        public static AdditionsTemperatureModifier.TEMPERATURE_RANGE byName(String name) {
            return (AdditionsTemperatureModifier.TEMPERATURE_RANGE) BY_NAME.get(name.toUpperCase(Locale.ROOT));
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}