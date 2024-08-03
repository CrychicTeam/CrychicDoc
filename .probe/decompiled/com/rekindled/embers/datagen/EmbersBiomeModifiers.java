package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import java.util.List;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.holdersets.AndHolderSet;
import net.minecraftforge.registries.holdersets.NotHolderSet;
import net.minecraftforge.registries.holdersets.OrHolderSet;

public class EmbersBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ORE_LEAD_KEY = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation("embers", "add_lead_ore"));

    public static final ResourceKey<BiomeModifier> ORE_SILVER_KEY = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation("embers", "add_silver_ore"));

    public static final ResourceKey<BiomeModifier> GOLEM_SPAWN = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation("embers", "add_golem_spawn"));

    public static final TagKey<Biome> NO_MONSTERS = TagKey.create(Registries.BIOME, new ResourceLocation("forge", "no_default_monsters"));

    public static void generate(BootstapContext<BiomeModifier> bootstrap) {
        HolderGetter<PlacedFeature> placed = bootstrap.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biome = bootstrap.lookup(ForgeRegistries.Keys.BIOMES);
        HolderSet<Biome> overworldBiomes = biome.getOrThrow(BiomeTags.IS_OVERWORLD);
        List<HolderSet<Biome>> biomeBlackList = List.of(biome.getOrThrow(Tags.Biomes.IS_MUSHROOM), HolderSet.direct(biome.getOrThrow(Biomes.DEEP_DARK)), biome.getOrThrow(NO_MONSTERS));
        HolderSet<Biome> hostileSpawns = new AndHolderSet<>(List.of(overworldBiomes, new EmbersBiomeModifiers.NotHolderSetWrapper(new OrHolderSet(biomeBlackList))));
        bootstrap.register(ORE_LEAD_KEY, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placed.getOrThrow(EmbersPlacedFeatures.ORE_LEAD_KEY)), GenerationStep.Decoration.UNDERGROUND_ORES));
        bootstrap.register(ORE_SILVER_KEY, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placed.getOrThrow(EmbersPlacedFeatures.ORE_SILVER_KEY)), GenerationStep.Decoration.UNDERGROUND_ORES));
        bootstrap.register(GOLEM_SPAWN, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(hostileSpawns, List.of(new MobSpawnSettings.SpawnerData(RegistryManager.ANCIENT_GOLEM.get(), 15, 1, 1))));
    }

    public static class NotHolderSetWrapper<T> extends NotHolderSet<T> {

        public NotHolderSetWrapper(HolderSet<T> value) {
            super(null, value);
        }

        @Override
        public boolean canSerializeIn(HolderOwner<T> holderOwner) {
            return true;
        }
    }
}