package dev.xkmc.l2library.compat.patchouli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import vazkii.patchouli.common.item.PatchouliItems;
import vazkii.patchouli.common.recipe.ShapelessBookRecipe;

public class ShapelessPatchouliBuilder extends ShapelessRecipeBuilder {

    private final ResourceLocation book;

    public ShapelessPatchouliBuilder(ResourceLocation book) {
        super(RecipeCategory.MISC, PatchouliItems.BOOK, 1);
        this.book = book;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pvd, ResourceLocation id) {
        this.m_126207_(id);
        this.f_126176_.parent(f_236353_).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        pvd.accept(new ShapelessPatchouliBuilder.Shapeless(this.book, id, this.f_126175_, this.f_126176_, id.withPrefix("recipes/")));
    }

    public static record Shapeless(ResourceLocation book, ResourceLocation id, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advId) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray jsonarray = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.toJson());
            }
            json.add("ingredients", jsonarray);
            json.addProperty("book", this.book.toString());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ShapelessBookRecipe.SERIALIZER;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advId;
        }
    }
}