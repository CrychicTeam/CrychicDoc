package net.blay09.mods.balm.api.item;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public interface BalmItems {

    Item.Properties itemProperties();

    default DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier) {
        return this.registerItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    DeferredObject<Item> registerItem(Supplier<Item> var1, ResourceLocation var2, @Nullable ResourceLocation var3);

    DeferredObject<CreativeModeTab> registerCreativeModeTab(Supplier<ItemStack> var1, ResourceLocation var2);

    @Deprecated
    default DeferredObject<CreativeModeTab> registerCreativeModeTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
        return this.registerCreativeModeTab(iconSupplier, identifier);
    }

    void addToCreativeModeTab(ResourceLocation var1, Supplier<ItemLike[]> var2);
}