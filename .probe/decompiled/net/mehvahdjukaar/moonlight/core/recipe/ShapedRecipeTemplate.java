package net.mehvahdjukaar.moonlight.core.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import net.mehvahdjukaar.moonlight.api.resources.recipe.IRecipeTemplate;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class ShapedRecipeTemplate implements IRecipeTemplate<ShapedRecipeBuilder.Result> {

    private final List<Object> conditions = new ArrayList();

    public final Item result;

    public final int count;

    public final String group;

    public final List<String> pattern;

    public final Map<Character, Ingredient> keys;

    public final CraftingBookCategory category;

    public ShapedRecipeTemplate(JsonObject json) {
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
        List<String> patternList = new ArrayList();
        JsonArray patterns = json.getAsJsonArray("pattern");
        patterns.forEach(p -> patternList.add(p.getAsString()));
        Map<Character, Ingredient> keyMap = new HashMap();
        JsonObject keys = json.getAsJsonObject("key");
        keys.entrySet().forEach(e -> keyMap.put(((String) e.getKey()).charAt(0), Ingredient.fromJson((JsonElement) e.getValue())));
        this.keys = keyMap;
        this.pattern = patternList;
    }

    public <T extends BlockType> ShapedRecipeBuilder.Result createSimilar(T originalMat, T destinationMat, Item unlockItem, String id) {
        ItemLike newRes = BlockType.changeItemType(this.result, originalMat, destinationMat);
        if (newRes == null) {
            throw new UnsupportedOperationException(String.format("Could not convert output item %s from type %s to %s", this.result, originalMat, destinationMat));
        } else {
            ShapedRecipeBuilder builder = new ShapedRecipeBuilder(this.determineBookCategory(this.category), newRes, this.count);
            boolean atLeastOneChanged = false;
            for (Entry<Character, Ingredient> e : this.keys.entrySet()) {
                Ingredient ing = (Ingredient) e.getValue();
                Ingredient newIng = IRecipeTemplate.convertIngredients(originalMat, destinationMat, ing);
                if (newIng != null) {
                    atLeastOneChanged = true;
                } else {
                    newIng = ing;
                }
                builder.define((Character) e.getKey(), newIng);
            }
            if (!atLeastOneChanged) {
                return null;
            } else {
                this.pattern.forEach(builder::m_126130_);
                builder.group(this.group);
                builder.unlockedBy("has_planks", InventoryChangeTrigger.TriggerInstance.hasItems(unlockItem));
                AtomicReference<ShapedRecipeBuilder.Result> newRecipe = new AtomicReference();
                if (id == null) {
                    builder.m_176498_(r -> newRecipe.set((ShapedRecipeBuilder.Result) r));
                } else {
                    builder.m_176500_(r -> newRecipe.set((ShapedRecipeBuilder.Result) r), id);
                }
                return (ShapedRecipeBuilder.Result) newRecipe.get();
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

    public boolean shouldBeShapeless() {
        return this.pattern.size() == 1 && ((String) this.pattern.get(0)).length() == 1;
    }

    public ShapelessRecipeTemplate toShapeless() {
        return new ShapelessRecipeTemplate(this);
    }
}