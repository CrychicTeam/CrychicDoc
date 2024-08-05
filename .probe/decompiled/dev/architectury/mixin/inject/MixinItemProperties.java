package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedItemPropertiesExtension;
import dev.architectury.impl.ItemPropertiesExtensionImpl;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredSupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ Item.Properties.class })
public class MixinItemProperties implements InjectedItemPropertiesExtension, ItemPropertiesExtensionImpl {

    @Unique
    private CreativeModeTab tab;

    @Unique
    private DeferredSupplier<CreativeModeTab> tabSupplier;

    @Override
    public Item.Properties arch$tab(CreativeModeTab tab) {
        this.tab = tab;
        this.tabSupplier = null;
        return (Item.Properties) this;
    }

    @Override
    public Item.Properties arch$tab(DeferredSupplier<CreativeModeTab> tab) {
        this.tab = null;
        this.tabSupplier = tab;
        return (Item.Properties) this;
    }

    @Override
    public Item.Properties arch$tab(ResourceKey<CreativeModeTab> tab) {
        this.tab = null;
        this.tabSupplier = CreativeTabRegistry.defer(tab);
        return (Item.Properties) this;
    }

    @Nullable
    @Override
    public CreativeModeTab arch$getTab() {
        return this.tab;
    }

    @Nullable
    @Override
    public DeferredSupplier<CreativeModeTab> arch$getTabSupplier() {
        return this.tabSupplier;
    }
}