package net.cristellib;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;

public class CristelLibRegistry {

    protected static ImmutableMap<String, Set<StructureConfig>> configs = ImmutableMap.of();

    protected CristelLibRegistry() {
    }

    public static ImmutableMap<String, Set<StructureConfig>> getConfigs() {
        return configs;
    }

    public void registerSetToConfig(String modid, ResourceLocation set, StructureConfig... configs) {
        for (StructureConfig config : configs) {
            config.addSet(new Pair(modid, set));
        }
    }
}