package com.github.alexthe666.citadel.server.world;

import java.util.Map;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public interface ExpandedBiomeSource {

    void setResourceKeyMap(Map<ResourceKey<Biome>, Holder<Biome>> var1);

    Map<ResourceKey<Biome>, Holder<Biome>> getResourceKeyMap();

    void expandBiomesWith(Set<Holder<Biome>> var1);
}