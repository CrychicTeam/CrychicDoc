package dev.architectury.extensions.injected;

import dev.architectury.registry.registries.DeferredSupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.ApiStatus.Experimental;

public interface InjectedItemPropertiesExtension {

    @Experimental
    default Item.Properties arch$tab(CreativeModeTab tab) {
        throw new UnsupportedOperationException();
    }

    @Experimental
    default Item.Properties arch$tab(DeferredSupplier<CreativeModeTab> tab) {
        throw new UnsupportedOperationException();
    }

    @Experimental
    default Item.Properties arch$tab(ResourceKey<CreativeModeTab> tab) {
        throw new UnsupportedOperationException();
    }
}