package dev.xkmc.l2library.compat.patchouli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import vazkii.patchouli.common.item.PatchouliItems;
import vazkii.patchouli.common.recipe.ShapedBookRecipe;

public class ShapedPatchouliBuilder extends ShapedRecipeBuilder {

    private final ResourceLocation book;

    public ShapedPatchouliBuilder(ResourceLocation book) {
        super(RecipeCategory.MISC, PatchouliItems.BOOK, 1);
        this.book = book;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pvd, ResourceLocation id) {
        this.m_126143_(id);
        this.f_126110_.parent(f_236353_).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        pvd.accept(new ShapedPatchouliBuilder.Result(this.book, id, this.f_126108_, this.f_126109_, this.f_126110_, id.withPrefix("recipes/")));
    }

    public static record Result(ResourceLocation book, ResourceLocation id, List<String> pattern, Map<Character, Ingredient> key, Advancement.Builder advancement, ResourceLocation advancementId) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray jsonarray = new JsonArray();
            for (String s : this.pattern) {
                jsonarray.add(s);
            }
            json.add("pattern", jsonarray);
            JsonObject jsonobject = new JsonObject();
            for (Entry<Character, Ingredient> entry : this.key.entrySet()) {
                jsonobject.add(String.valueOf(entry.getKey()), ((Ingredient) entry.getValue()).toJson());
            }
            json.add("key", jsonobject);
            json.addProperty("book", this.book.toString());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ShapedBookRecipe.SERIALIZER;
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
            return this.advancementId;
        }
    }
}