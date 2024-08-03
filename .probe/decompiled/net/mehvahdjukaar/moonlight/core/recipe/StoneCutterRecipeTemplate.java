package net.mehvahdjukaar.moonlight.core.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import net.mehvahdjukaar.moonlight.api.resources.recipe.IRecipeTemplate;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class StoneCutterRecipeTemplate implements IRecipeTemplate<SingleItemRecipeBuilder.Result> {

    private final List<Object> conditions = new ArrayList();

    public final Item result;

    public final int count;

    public final String group;

    public final Ingredient ingredient;

    public final CraftingBookCategory category;

    public StoneCutterRecipeTemplate(JsonObject json) {
        JsonElement result = json.get("result");
        ResourceLocation item = new ResourceLocation(result.getAsString());
        int count = 1;
        JsonElement c = json.get("count");
        if (c != null) {
            count = c.getAsInt();
        }
        this.count = count;
        this.result = BuiltInRegistries.ITEM.get(item);
        this.category = (CraftingBookCategory) CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
        JsonElement g = json.get("group");
        this.group = g == null ? "" : g.getAsString();
        this.ingredient = Ingredient.fromJson(json.get("ingredient"));
    }

    public <T extends BlockType> SingleItemRecipeBuilder.Result createSimilar(T originalMat, T destinationMat, Item unlockItem, @Nullable String id) {
        ItemLike newRes = BlockType.changeItemType(this.result, originalMat, destinationMat);
        if (newRes == null) {
            throw new UnsupportedOperationException(String.format("Could not convert output item %s from type %s to %s", this.result, originalMat, destinationMat));
        } else {
            Ingredient ing = IRecipeTemplate.convertIngredients(originalMat, destinationMat, this.ingredient);
            if (ing == null) {
                return null;
            } else {
                SingleItemRecipeBuilder builder = SingleItemRecipeBuilder.stonecutting(ing, this.determineBookCategory(this.category), newRes);
                builder.group(this.group);
                builder.unlockedBy("has_planks", InventoryChangeTrigger.TriggerInstance.hasItems(unlockItem));
                AtomicReference<SingleItemRecipeBuilder.Result> newRecipe = new AtomicReference();
                if (id == null) {
                    builder.m_176498_(r -> newRecipe.set((SingleItemRecipeBuilder.Result) r));
                } else {
                    builder.m_176500_(r -> newRecipe.set((SingleItemRecipeBuilder.Result) r), id);
                }
                return (SingleItemRecipeBuilder.Result) newRecipe.get();
            }
        }
    }

    @Override
    public List<Object> getConditions() {
        return this.conditions;
    }

    @Override
    public void addCondition(Object condition) {
        this.conditions.add(condition);
    }
}