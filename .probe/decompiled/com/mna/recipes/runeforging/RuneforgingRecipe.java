package com.mna.recipes.runeforging;

import com.google.gson.JsonObject;
import com.mna.api.recipes.IRuneforgeRecipe;
import com.mna.items.ItemInit;
import com.mna.recipes.AMRecipeBase;
import com.mna.recipes.RecipeInit;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class RuneforgingRecipe extends AMRecipeBase implements IRuneforgeRecipe {

    private ResourceLocation patternItem;

    private ResourceLocation runeItem;

    private ResourceLocation materialItem;

    private Item __outputItem = null;

    private int hits = 10;

    private int outputQuantity = 1;

    public RuneforgingRecipe(ResourceLocation idIn) {
        super(idIn);
        this.patternItem = new ResourceLocation("");
        this.runeItem = new ResourceLocation("");
    }

    @Override
    public void parseExtraJson(JsonObject recipeJSON) {
        this.patternItem = new ResourceLocation(recipeJSON.get("pattern").getAsString());
        this.runeItem = new ResourceLocation(recipeJSON.get("output").getAsString());
        if (recipeJSON.has("material")) {
            this.materialItem = new ResourceLocation(recipeJSON.get("material").getAsString());
        }
        if (recipeJSON.has("hits")) {
            this.hits = recipeJSON.get("hits").getAsInt();
        }
        if (recipeJSON.has("output_quantity")) {
            this.outputQuantity = recipeJSON.get("output_quantity").getAsInt();
        }
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack patternStack = inv.m_8020_(0);
        ItemStack materialStack = inv.m_8020_(1);
        if (inv.m_6643_() == 2 && ForgeRegistries.ITEMS.getKey(patternStack.getItem()).compareTo(this.patternItem) == 0) {
            if (this.materialItem == null) {
                if (materialStack.getItem() == ItemInit.VINTEUM_INGOT_SUPERHEATED.get()) {
                    return true;
                }
            } else if (ForgeRegistries.ITEMS.getKey(materialStack.getItem()).compareTo(this.materialItem) == 0) {
                return true;
            }
        }
        return false;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        return this.getResultItem();
    }

    @Override
    public ItemStack getResultItem() {
        if (this.__outputItem == null) {
            IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;
            if (itemRegistry != null) {
                this.__outputItem = itemRegistry.getValue(this.runeItem);
            }
        }
        return this.__outputItem != null ? new ItemStack(this.__outputItem, this.outputQuantity) : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.RUNEFORGING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.RUNEFORGING_TYPE.get();
    }

    public int getHits() {
        return this.hits;
    }

    public int getOutputQuantity() {
        return this.outputQuantity;
    }

    public ResourceLocation getMaterial() {
        return this.materialItem == null ? ItemInit.VINTEUM_INGOT_SUPERHEATED.getId() : this.materialItem;
    }

    public ResourceLocation getPatternResource() {
        return this.patternItem;
    }

    public void setPatternResource(ResourceLocation pattern) {
        this.patternItem = pattern;
    }

    public ResourceLocation getOutputResource() {
        return this.runeItem;
    }

    public void setOutputResource(ResourceLocation output) {
        this.runeItem = output;
    }

    public void setMaterial(ResourceLocation material) {
        this.materialItem = material;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setOutputQuantity(int outputQuantity) {
        this.outputQuantity = outputQuantity;
    }

    @Override
    public ItemStack getGuiRepresentationStack() {
        return this.getResultItem();
    }

    @Override
    public ResourceLocation getRegistryId() {
        return this.m_6423_();
    }
}