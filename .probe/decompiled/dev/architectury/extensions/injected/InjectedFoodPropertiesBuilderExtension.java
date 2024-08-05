package dev.architectury.extensions.injected;

import dev.architectury.hooks.item.food.FoodPropertiesHooks;
import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public interface InjectedFoodPropertiesBuilderExtension {

    default FoodProperties.Builder arch$effect(Supplier<? extends MobEffectInstance> effectSupplier, float chance) {
        FoodPropertiesHooks.effect((FoodProperties.Builder) this, effectSupplier, chance);
        return (FoodProperties.Builder) this;
    }
}