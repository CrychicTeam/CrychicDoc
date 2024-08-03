package com.almostreliable.morejs.mixin;

import java.util.List;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ BrewingRecipeRegistry.class })
public interface BrewingRecipeRegistryAccessor {

    @Accessor
    static List<IBrewingRecipe> getRecipes() {
        throw new AssertionError();
    }
}