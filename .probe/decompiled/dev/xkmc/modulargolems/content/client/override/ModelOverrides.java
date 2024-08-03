package dev.xkmc.modulargolems.content.client.override;

import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;

public class ModelOverrides {

    private static final HashMap<ResourceLocation, ModelOverride> OVERRIDES = new HashMap();

    public static synchronized void registerOverride(ResourceLocation id, ModelOverride override) {
        OVERRIDES.put(id, override);
    }

    public static ModelOverride getOverride(ResourceLocation id) {
        return (ModelOverride) OVERRIDES.getOrDefault(id, ModelOverride.DEFAULT);
    }
}