package dev.architectury.mixin.inject;

import dev.architectury.extensions.injected.InjectedFoodPropertiesBuilderExtension;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ FoodProperties.Builder.class })
public class MixinFoodPropertiesBuilder implements InjectedFoodPropertiesBuilderExtension {
}