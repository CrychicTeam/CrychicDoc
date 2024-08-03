package io.github.edwinmindcraft.origins.api.registry;

import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class OriginsDynamicRegistries {

    public static final ResourceKey<Registry<Origin>> ORIGINS_REGISTRY = ResourceKey.createRegistryKey(new ResourceLocation("origins", "origins"));

    public static final ResourceKey<Registry<OriginLayer>> LAYERS_REGISTRY = ResourceKey.createRegistryKey(new ResourceLocation("origins", "layers"));
}