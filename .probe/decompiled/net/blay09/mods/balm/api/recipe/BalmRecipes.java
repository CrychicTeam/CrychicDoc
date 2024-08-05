package net.blay09.mods.balm.api.recipe;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public interface BalmRecipes {

    <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Supplier<RecipeType<T>> var1, Supplier<RecipeSerializer<T>> var2, ResourceLocation var3);
}