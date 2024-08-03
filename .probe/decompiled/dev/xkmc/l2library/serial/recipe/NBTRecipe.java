package dev.xkmc.l2library.serial.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public record NBTRecipe(FinishedRecipe recipe, ItemStack stack) implements FinishedRecipe {

    @Override
    public void serializeRecipeData(JsonObject json) {
        this.recipe.serializeRecipeData(json);
        json.getAsJsonObject("result").addProperty("nbt", this.stack.getOrCreateTag().toString());
    }

    @Override
    public ResourceLocation getId() {
        return this.recipe.getId();
    }

    @Override
    public RecipeSerializer<?> getType() {
        return this.recipe.getType();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return this.recipe.serializeAdvancement();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return this.recipe.getAdvancementId();
    }
}