package fuzs.puzzleslib.api.item.v2;

import fuzs.puzzleslib.impl.item.CreativeModeTabConfiguratorImpl;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public interface CreativeModeTabConfigurator {

    static CreativeModeTabConfigurator from(String modId, Supplier<ItemStack> icon) {
        return from(modId).icon(icon);
    }

    static CreativeModeTabConfigurator from(String modId) {
        return from(modId, "main");
    }

    static CreativeModeTabConfigurator from(String modId, String tabId) {
        return from(new ResourceLocation(modId, tabId));
    }

    static CreativeModeTabConfigurator from(ResourceLocation identifier) {
        return new CreativeModeTabConfiguratorImpl(identifier);
    }

    CreativeModeTabConfigurator icon(Supplier<ItemStack> var1);

    CreativeModeTabConfigurator icons(Supplier<ItemStack[]> var1);

    CreativeModeTabConfigurator displayItems(CreativeModeTab.DisplayItemsGenerator var1);

    CreativeModeTabConfigurator withSearchBar();

    CreativeModeTabConfigurator appendEnchantmentsAndPotions();
}