package com.mna.tools;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;

public class BiomeUtils {

    public static List<Holder<Biome>> getAllBiomes(ServerLevel world) {
        List<Holder<Biome>> result = new ArrayList();
        Registry<Biome> registry = world.m_9598_().registryOrThrow(Registries.BIOME);
        registry.asHolderIdMap().forEach(holder -> result.add(holder));
        return result;
    }

    public static List<ResourceLocation> getAllBiomeIDs(ServerLevel world) {
        List<ResourceLocation> result = new ArrayList();
        Registry<Biome> registry = world.m_9598_().registryOrThrow(Registries.BIOME);
        registry.entrySet().forEach(entry -> result.add(((ResourceKey) entry.getKey()).location()));
        return result;
    }

    @Nullable
    public static ResourceLocation getBiomeID(ServerLevel world, Holder<Biome> biome) {
        if (!biome.isBound()) {
            return null;
        } else {
            Registry<Biome> registry = world.m_9598_().registryOrThrow(Registries.BIOME);
            return registry.getKey((Biome) biome.get());
        }
    }
}