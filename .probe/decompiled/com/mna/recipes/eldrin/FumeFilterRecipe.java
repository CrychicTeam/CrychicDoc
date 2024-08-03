package com.mna.recipes.eldrin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mna.api.affinity.Affinity;
import com.mna.api.tools.MATags;
import com.mna.recipes.AMRecipeBase;
import com.mna.recipes.RecipeInit;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class FumeFilterRecipe extends AMRecipeBase {

    private ResourceLocation itemOrTagID;

    private ItemStack cachedGuiRepStack = ItemStack.EMPTY;

    private Affinity affinity;

    private float totalGeneration;

    public FumeFilterRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public void parseExtraJson(JsonObject recipeJSON) {
        this.itemOrTagID = new ResourceLocation(recipeJSON.get("item").getAsString());
        JsonElement power_generated = recipeJSON.get("power_provided");
        if (power_generated != null && power_generated.isJsonObject()) {
            JsonObject elem = (JsonObject) power_generated;
            if (elem.has("affinity") && elem.has("amount")) {
                String affinity = elem.get("affinity").getAsString();
                this.totalGeneration = elem.get("amount").getAsFloat();
                this.affinity = Affinity.UNKNOWN;
                try {
                    this.affinity = Affinity.valueOf(affinity);
                } catch (Exception var6) {
                    return;
                }
            }
        }
    }

    public ResourceLocation getItemOrTagID() {
        return this.itemOrTagID;
    }

    public void setItemOrTagID(ResourceLocation itemOrTagID) {
        this.itemOrTagID = itemOrTagID;
    }

    public Affinity getAffinity() {
        return this.affinity;
    }

    public void setAffinity(Affinity affinity) {
        this.affinity = affinity;
    }

    public float getTotalGeneration() {
        return this.totalGeneration;
    }

    public void setTotalGeneration(float totalGeneration) {
        this.totalGeneration = totalGeneration;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.ELDRIN_FUME_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.FUME_FILTER_TYPE.get();
    }

    @Override
    public ItemStack getGuiRepresentationStack() {
        if (this.cachedGuiRepStack.isEmpty()) {
            List<Item> lookup = MATags.smartLookupItem(this.itemOrTagID);
            if (lookup.size() > 0) {
                this.cachedGuiRepStack = new ItemStack((ItemLike) lookup.get(0));
            }
        }
        return this.cachedGuiRepStack;
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        return inv.getWidth() == 1 && inv.getHeight() == 1 && MATags.smartLookupItem(this.itemOrTagID).contains(inv.m_8020_(0).getItem());
    }

    @Override
    public ResourceLocation getRegistryId() {
        return this.m_6423_();
    }

    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth == 1 && pHeight == 1;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }
}