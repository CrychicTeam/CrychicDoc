package dev.architectury.hooks.item.food;

import dev.architectury.hooks.item.food.forge.FoodPropertiesHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public final class FoodPropertiesHooks {

    private FoodPropertiesHooks() {
    }

    @ExpectPlatform
    @Transformed
    public static void effect(FoodProperties.Builder builder, Supplier<? extends MobEffectInstance> effectSupplier, float chance) {
        FoodPropertiesHooksImpl.effect(builder, effectSupplier, chance);
    }
}