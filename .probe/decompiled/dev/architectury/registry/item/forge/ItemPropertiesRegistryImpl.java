package dev.architectury.registry.item.forge;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class ItemPropertiesRegistryImpl {

    public static ClampedItemPropertyFunction registerGeneric(ResourceLocation propertyId, ClampedItemPropertyFunction function) {
        ItemProperties.registerGeneric(propertyId, function);
        return function;
    }

    public static ClampedItemPropertyFunction register(ItemLike item, ResourceLocation propertyId, ClampedItemPropertyFunction function) {
        ItemProperties.register(item.asItem(), propertyId, function);
        return function;
    }
}