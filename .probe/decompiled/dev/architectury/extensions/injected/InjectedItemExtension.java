package dev.architectury.extensions.injected;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;

public interface InjectedItemExtension extends InjectedRegistryEntryExtension<Item> {

    @Override
    default Holder<Item> arch$holder() {
        return ((Item) this).builtInRegistryHolder();
    }
}