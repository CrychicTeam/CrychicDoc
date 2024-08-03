package net.minecraft.world.level.storage.loot.parameters;

import net.minecraft.resources.ResourceLocation;

public class LootContextParam<T> {

    private final ResourceLocation name;

    public LootContextParam(ResourceLocation resourceLocation0) {
        this.name = resourceLocation0;
    }

    public ResourceLocation getName() {
        return this.name;
    }

    public String toString() {
        return "<parameter " + this.name + ">";
    }
}