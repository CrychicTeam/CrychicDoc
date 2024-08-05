package net.blay09.mods.waystones.api;

import net.minecraft.resources.ResourceLocation;

public class WaystoneStyle {

    private final ResourceLocation blockRegistryName;

    public WaystoneStyle(ResourceLocation blockRegistryName) {
        this.blockRegistryName = blockRegistryName;
    }

    public ResourceLocation getBlockRegistryName() {
        return this.blockRegistryName;
    }
}