package net.minecraft.data.registries;

import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.data.worldgen.NoiseData;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.data.worldgen.Structures;
import net.minecraft.data.worldgen.biome.BiomeData;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.network.chat.ChatType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

public class VanillaRegistries {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.DIMENSION_TYPE, DimensionTypes::m_236473_).add(Registries.CONFIGURED_CARVER, Carvers::m_254873_).add(Registries.CONFIGURED_FEATURE, FeatureUtils::m_254925_).add(Registries.PLACED_FEATURE, PlacementUtils::m_255199_).add(Registries.STRUCTURE, Structures::m_255324_).add(Registries.STRUCTURE_SET, StructureSets::m_255117_).add(Registries.PROCESSOR_LIST, ProcessorLists::m_254888_).add(Registries.TEMPLATE_POOL, Pools::m_255181_).add(Registries.BIOME, BiomeData::m_272174_).add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterLists::m_274553_).add(Registries.NOISE, NoiseData::m_236475_).add(Registries.DENSITY_FUNCTION, NoiseRouterData::m_255288_).add(Registries.NOISE_SETTINGS, NoiseGeneratorSettings::m_254959_).add(Registries.WORLD_PRESET, WorldPresets::m_254897_).add(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPresets::m_254848_).add(Registries.CHAT_TYPE, ChatType::m_237021_).add(Registries.TRIM_PATTERN, TrimPatterns::m_266400_).add(Registries.TRIM_MATERIAL, TrimMaterials::m_266479_).add(Registries.DAMAGE_TYPE, DamageTypes::m_269594_);

    private static void validateThatAllBiomeFeaturesHaveBiomeFilter(HolderLookup.Provider holderLookupProvider0) {
        validateThatAllBiomeFeaturesHaveBiomeFilter(holderLookupProvider0.lookupOrThrow(Registries.PLACED_FEATURE), holderLookupProvider0.lookupOrThrow(Registries.BIOME));
    }

    public static void validateThatAllBiomeFeaturesHaveBiomeFilter(HolderGetter<PlacedFeature> holderGetterPlacedFeature0, HolderLookup<Biome> holderLookupBiome1) {
        holderLookupBiome1.listElements().forEach(p_256326_ -> {
            ResourceLocation $$2 = p_256326_.key().location();
            List<HolderSet<PlacedFeature>> $$3 = ((Biome) p_256326_.value()).getGenerationSettings().features();
            $$3.stream().flatMap(HolderSet::m_203614_).forEach(p_256657_ -> p_256657_.unwrap().ifLeft(p_256188_ -> {
                Holder.Reference<PlacedFeature> $$3x = holderGetterPlacedFeature0.getOrThrow(p_256188_);
                if (!validatePlacedFeature($$3x.value())) {
                    Util.logAndPauseIfInIde("Placed feature " + p_256188_.location() + " in biome " + $$2 + " is missing BiomeFilter.biome()");
                }
            }).ifRight(p_256575_ -> {
                if (!validatePlacedFeature(p_256575_)) {
                    Util.logAndPauseIfInIde("Placed inline feature in biome " + p_256326_ + " is missing BiomeFilter.biome()");
                }
            }));
        });
    }

    private static boolean validatePlacedFeature(PlacedFeature placedFeature0) {
        return placedFeature0.placement().contains(BiomeFilter.biome());
    }

    public static HolderLookup.Provider createLookup() {
        RegistryAccess.Frozen $$0 = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        HolderLookup.Provider $$1 = BUILDER.build($$0);
        validateThatAllBiomeFeaturesHaveBiomeFilter($$1);
        return $$1;
    }
}