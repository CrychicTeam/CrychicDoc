package dev.xkmc.l2library.serial.recipe;

import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.RecipeSerializer;

public interface IExtendedRecipe {

    void addAdditional(JsonObject var1);

    RecipeSerializer<?> getType();
}