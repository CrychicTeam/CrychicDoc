package dev.architectury.registry.fuel;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.fuel.forge.FuelRegistryImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class FuelRegistry {

    private FuelRegistry() {
    }

    @ExpectPlatform
    @Transformed
    public static void register(int time, ItemLike... items) {
        FuelRegistryImpl.register(time, items);
    }

    @ExpectPlatform
    @Transformed
    public static int get(ItemStack stack) {
        return FuelRegistryImpl.get(stack);
    }
}