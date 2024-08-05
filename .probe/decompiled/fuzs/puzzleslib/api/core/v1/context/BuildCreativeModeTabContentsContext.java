package fuzs.puzzleslib.api.core.v1.context;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public interface BuildCreativeModeTabContentsContext {

    default void registerBuildListener(String modId, CreativeModeTab.DisplayItemsGenerator itemsGenerator) {
        ResourceLocation identifier = new ResourceLocation(modId, "main");
        this.registerBuildListener(identifier, itemsGenerator);
    }

    default void registerBuildListener(ResourceLocation identifier, CreativeModeTab.DisplayItemsGenerator itemsGenerator) {
        ResourceKey<CreativeModeTab> resourceKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB, identifier);
        this.registerBuildListener(resourceKey, itemsGenerator);
    }

    void registerBuildListener(ResourceKey<CreativeModeTab> var1, CreativeModeTab.DisplayItemsGenerator var2);
}