package dev.architectury.registry.item;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.item.forge.ItemPropertiesRegistryImpl;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ItemPropertiesRegistry {

    private ItemPropertiesRegistry() {
    }

    @ExpectPlatform
    @Transformed
    public static ClampedItemPropertyFunction registerGeneric(ResourceLocation propertyId, ClampedItemPropertyFunction function) {
        return ItemPropertiesRegistryImpl.registerGeneric(propertyId, function);
    }

    @ExpectPlatform
    @Transformed
    public static ClampedItemPropertyFunction register(ItemLike item, ResourceLocation propertyId, ClampedItemPropertyFunction function) {
        return ItemPropertiesRegistryImpl.register(item, propertyId, function);
    }
}