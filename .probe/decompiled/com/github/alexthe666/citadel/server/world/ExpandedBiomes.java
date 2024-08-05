package com.github.alexthe666.citadel.server.world;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;

public class ExpandedBiomes {

    private static Map<ResourceKey<LevelStem>, List<ResourceKey<Biome>>> biomes = new HashMap();

    public static void addExpandedBiome(ResourceKey<Biome> biome, ResourceKey<LevelStem> dimension) {
        List<ResourceKey<Biome>> list;
        if (!biomes.containsKey(dimension)) {
            list = new ArrayList();
        } else {
            list = (List<ResourceKey<Biome>>) biomes.get(dimension);
        }
        if (!list.contains(biome)) {
            list.add(biome);
        }
        biomes.put(dimension, list);
    }

    public static Set<Holder<Biome>> buildBiomeList(RegistryAccess registryAccess, ResourceKey<LevelStem> dimension) {
        List<ResourceKey<Biome>> list = (List<ResourceKey<Biome>>) biomes.get(dimension);
        if (list != null && !list.isEmpty()) {
            Registry<Biome> allBiomes = registryAccess.registryOrThrow(Registries.BIOME);
            Builder<Holder<Biome>> biomeHolders = ImmutableSet.builder();
            for (ResourceKey<Biome> biomeResourceKey : list) {
                Optional<Holder.Reference<Biome>> holderOptional = allBiomes.getHolder(biomeResourceKey);
                holderOptional.ifPresent(biomeHolders::add);
            }
            return biomeHolders.build();
        } else {
            return Set.of();
        }
    }
}