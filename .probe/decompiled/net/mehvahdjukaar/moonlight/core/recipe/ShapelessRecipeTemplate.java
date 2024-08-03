package net.mehvahdjukaar.moonlight.core.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import net.mehvahdjukaar.moonlight.api.resources.recipe.IRecipeTemplate;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class ShapelessRecipeTemplate implements IRecipeTemplate<ShapelessRecipeBuilder.Result> {

    private final List<Object> conditions = new ArrayList();

    public final Item result;

    public final int count;

    public final String group;

    public final List<Ingredient> ingredients;

    public final CraftingBookCategory category;

    ShapelessRecipeTemplate(ShapedRecipeTemplate shaped) {
        this.result = shaped.result;
        this.count = shaped.count;
        this.group = shaped.group;
        this.ingredients = shaped.keys.values().stream().toList();
        this.category = shaped.category;
    }

    public ShapelessRecipeTemplate(JsonObject json) {
        JsonObject result = json.getAsJsonObject("result");
        ResourceLocation item = new ResourceLocation(result.get("item").getAsString());
        int count = 1;
        JsonElement c = result.get("count");
        if (c != null) {
            count = c.getAsInt();
        }
        this.result = BuiltInRegistries.ITEM.get(item);
        this.count = count;
        this.category = (CraftingBookCategory) CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
        JsonElement g = json.get("group");
        this.group = g == null ? "" : g.getAsString();
        List<Ingredient> ingredientsList = new ArrayList();
        JsonArray ingredients = json.getAsJsonArray("ingredients");
        ingredients.forEach(p -> ingredientsList.add(Ingredient.fromJson(p)));
        this.ingredients = ingredientsList;
    }

    public <T extends BlockType> ShapelessRecipeBuilder.Result createSimilar(T originalMat, T destinationMat, Item unlockItem, String id) {
        ItemLike newRes = BlockType.changeItemType(this.result, originalMat, destinationMat);
        if (newRes == null) {
            throw new UnsupportedOperationException(String.format("Could not convert output item %s from type %s to %s", this.result, originalMat, destinationMat));
        } else {
            ShapelessRecipeBuilder builder = new ShapelessRecipeBuilder(this.determineBookCategory(this.category), newRes, this.count);
            boolean atLeastOneChanged = false;
            for (Ingredient originalIng : this.ingredients) {
                Ingredient newIng = IRecipeTemplate.convertIngredients(originalMat, destinationMat, originalIng);
                if (newIng != null) {
                    atLeastOneChanged = true;
                } else {
                    newIng = originalIng;
                }
                builder.requires(newIng);
            }
            if (!atLeastOneChanged) {
                return null;
            } else {
                builder.group(this.group);
                builder.unlockedBy("has_planks", InventoryChangeTrigger.TriggerInstance.hasItems(unlockItem));
                AtomicReference<ShapelessRecipeBuilder.Result> newRecipe = new AtomicReference();
                if (id == null) {
                    builder.m_176498_(r -> newRecipe.set((ShapelessRecipeBuilder.Result) r));
                } else {
                    builder.m_176500_(r -> newRecipe.set((ShapelessRecipeBuilder.Result) r), id);
                }
                return (ShapelessRecipeBuilder.Result) newRecipe.get();
            }
        }
    }

    @Override
    public void addCondition(Object condition) {
        this.conditions.add(condition);
    }

    @Override
    public List<Object> getConditions() {
        return this.conditions;
    }
}