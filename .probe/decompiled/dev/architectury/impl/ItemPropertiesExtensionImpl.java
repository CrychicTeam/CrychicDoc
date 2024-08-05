package dev.architectury.impl;

import dev.architectury.registry.registries.DeferredSupplier;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface ItemPropertiesExtensionImpl {

    @Nullable
    CreativeModeTab arch$getTab();

    @Nullable
    DeferredSupplier<CreativeModeTab> arch$getTabSupplier();
}