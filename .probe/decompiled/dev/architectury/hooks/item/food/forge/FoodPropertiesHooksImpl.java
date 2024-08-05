package dev.architectury.hooks.item.food.forge;

import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class FoodPropertiesHooksImpl {

    public static void effect(FoodProperties.Builder builder, Supplier<? extends MobEffectInstance> effectSupplier, float chance) {
        builder.effect(effectSupplier, chance);
    }
}