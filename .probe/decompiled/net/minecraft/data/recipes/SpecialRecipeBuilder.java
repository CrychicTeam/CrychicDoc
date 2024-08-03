package net.minecraft.data.recipes;

import com.google.gson.JsonObject;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SpecialRecipeBuilder extends CraftingRecipeBuilder {

    final RecipeSerializer<?> serializer;

    public SpecialRecipeBuilder(RecipeSerializer<?> recipeSerializer0) {
        this.serializer = recipeSerializer0;
    }

    public static SpecialRecipeBuilder special(RecipeSerializer<? extends CraftingRecipe> recipeSerializerExtendsCraftingRecipe0) {
        return new SpecialRecipeBuilder(recipeSerializerExtendsCraftingRecipe0);
    }

    public void save(Consumer<FinishedRecipe> consumerFinishedRecipe0, final String string1) {
        consumerFinishedRecipe0.accept(new CraftingRecipeBuilder.CraftingResult(CraftingBookCategory.MISC) {

            @Override
            public RecipeSerializer<?> getType() {
                return SpecialRecipeBuilder.this.serializer;
            }

            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(string1);
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public ResourceLocation getAdvancementId() {
                return new ResourceLocation("");
            }
        });
    }
}