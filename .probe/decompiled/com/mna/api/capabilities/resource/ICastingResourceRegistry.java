package com.mna.api.capabilities.resource;

import net.minecraft.resources.ResourceLocation;

public interface ICastingResourceRegistry {

    void register(ResourceLocation var1, Class<? extends ICastingResource> var2);
}