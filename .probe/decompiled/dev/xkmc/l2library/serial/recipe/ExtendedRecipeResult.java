package dev.xkmc.l2library.serial.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public record ExtendedRecipeResult(FinishedRecipe impl, IExtendedRecipe parent) implements FinishedRecipe {

    @Override
    public void serializeRecipeData(JsonObject json) {
        this.impl.serializeRecipeData(json);
        this.parent.addAdditional(json);
    }

    @Override
    public ResourceLocation getId() {
        return this.impl().getId();
    }

    @Override
    public RecipeSerializer<?> getType() {
        return this.parent().getType();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return this.impl().serializeAdvancement();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return this.impl().getAdvancementId();
    }
}