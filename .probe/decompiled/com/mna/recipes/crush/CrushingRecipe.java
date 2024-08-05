package com.mna.recipes.crush;

import com.google.gson.JsonObject;
import com.mna.api.recipes.IRuneforgeRecipe;
import com.mna.api.tools.MATags;
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

public class CrushingRecipe extends AMRecipeBase implements IRuneforgeRecipe {

    private ResourceLocation inputItem;

    private ResourceLocation outputItem;

    private Item __outputItem = null;

    private int outputQuantity = 1;

    public CrushingRecipe(ResourceLocation idIn) {
        super(idIn);
        this.inputItem = new ResourceLocation("");
        this.outputItem = new ResourceLocation("");
    }

    @Override
    public void parseExtraJson(JsonObject recipeJSON) {
        this.inputItem = new ResourceLocation(recipeJSON.get("input").getAsString());
        this.outputItem = new ResourceLocation(recipeJSON.get("output").getAsString());
        if (recipeJSON.has("output_quantity")) {
            this.outputQuantity = recipeJSON.get("output_quantity").getAsInt();
        }
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        if (inv.m_6643_() == 1) {
            ItemStack inputStack = inv.m_8020_(0);
            if (MATags.isItemEqual(inputStack, this.inputItem)) {
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
                this.__outputItem = itemRegistry.getValue(this.outputItem);
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
        return RecipeInit.CRUSHING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.CRUSHING_TYPE.get();
    }

    public int getOutputQuantity() {
        return this.outputQuantity;
    }

    public ResourceLocation getInputResource() {
        return this.inputItem;
    }

    public void setInputResource(ResourceLocation pattern) {
        this.inputItem = pattern;
    }

    public ResourceLocation getOutputResource() {
        return this.outputItem;
    }

    public void setOutputResource(ResourceLocation output) {
        this.outputItem = output;
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