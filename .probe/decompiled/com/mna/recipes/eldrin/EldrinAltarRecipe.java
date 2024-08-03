package com.mna.recipes.eldrin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mna.api.affinity.Affinity;
import com.mna.recipes.ItemAndPatternCraftingInventory;
import com.mna.recipes.ItemAndPatternRecipe;
import com.mna.recipes.RecipeInit;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class EldrinAltarRecipe extends ItemAndPatternRecipe {

    private HashMap<Affinity, Float> powerRequirements;

    private int[] colors = new int[] { 0, 0 };

    public static final int MAX_ITEMS = 9;

    public EldrinAltarRecipe(ResourceLocation idIn) {
        super(idIn);
        this.powerRequirements = new HashMap();
    }

    @Override
    public void parseExtraJson(JsonObject recipeJSON) {
        super.parseExtraJson(recipeJSON);
        JsonElement power_reqs = recipeJSON.get("power_requirements");
        if (power_reqs != null && power_reqs.isJsonArray()) {
            JsonArray reqsArray = power_reqs.getAsJsonArray();
            reqsArray.forEach(e -> {
                if (e.isJsonObject()) {
                    JsonObject elem = (JsonObject) e;
                    if (elem.has("affinity") && elem.has("amount")) {
                        String affinity = elem.get("affinity").getAsString();
                        float amount = elem.get("amount").getAsFloat();
                        Affinity parsedAffinity = Affinity.UNKNOWN;
                        try {
                            parsedAffinity = Affinity.valueOf(affinity);
                        } catch (Exception var7x) {
                            return;
                        }
                        this.powerRequirements.put(parsedAffinity, amount);
                    }
                }
            });
        }
        if (recipeJSON.has("colors")) {
            JsonElement colorElem = recipeJSON.get("colors");
            if (colorElem.isJsonArray()) {
                JsonArray colors = colorElem.getAsJsonArray();
                for (int i = 0; i < this.colors.length; i++) {
                    if (colors.size() >= i) {
                        colorElem = colors.get(i);
                        if (colorElem.isJsonPrimitive()) {
                            this.colors[i] = colorElem.getAsInt();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected int maxItems() {
        return 9;
    }

    @Override
    protected int maxPatterns() {
        return 0;
    }

    public int getColorOne() {
        return this.colors[0];
    }

    public int getColorTwo() {
        return this.colors[1];
    }

    public HashMap<Affinity, Float> getPowerRequirements() {
        return this.powerRequirements;
    }

    public void setPowerRequirements(HashMap<Affinity, Float> map) {
        this.powerRequirements = map;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.ELDRIN_ALTAR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.ELDRIN_ALTAR_TYPE.get();
    }

    @Override
    public ItemStack getGuiRepresentationStack() {
        return this.getResultItem();
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        if (!(inv instanceof ItemAndPatternCraftingInventory mwInv)) {
            return false;
        } else {
            ArrayList<ItemStack> items = new ArrayList();
            ArrayList<String> patterns = new ArrayList(mwInv.getPatterns());
            for (int i = 0; i < 9; i++) {
                if (!inv.m_8020_(i).isEmpty()) {
                    items.add(inv.m_8020_(i));
                }
            }
            return this.itemsMatchShapeless(items) && this.patternsMatchShaped(patterns);
        }
    }

    @Override
    public ResourceLocation getRegistryId() {
        return this.m_6423_();
    }
}