package com.mna.api.capabilities.resource;

import net.minecraft.resources.ResourceLocation;

public interface ICastingResourceGuiRegistry {

    void registerResourceGui(ResourceLocation var1, ICastingResourceGuiProvider var2);
}