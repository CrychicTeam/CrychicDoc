package com.mna.recipes.arcanefurnace;

import com.google.gson.JsonObject;
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

public class ArcaneFurnaceRecipe extends AMRecipeBase {

    private ResourceLocation inputItem;

    private ResourceLocation outputItem;

    private int burnTime;

    private int outputQuantity = 1;

    private Item __outputItem = null;

    public ArcaneFurnaceRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public void parseExtraJson(JsonObject object) {
        if (object.has("input") && object.has("output") && object.has("burnTime")) {
            this.inputItem = new ResourceLocation(object.get("input").getAsString());
            this.outputItem = new ResourceLocation(object.get("output").getAsString());
            this.burnTime = object.get("burnTime").getAsInt();
        }
        if (object.has("outputQuantity")) {
            this.outputQuantity = object.get("outputQuantity").getAsInt();
        }
    }

    public void setInputItem(ResourceLocation rLoc) {
        this.inputItem = rLoc;
    }

    public ResourceLocation getInputItem() {
        return this.inputItem;
    }

    public void setOutputItem(ResourceLocation rLoc) {
        this.outputItem = rLoc;
    }

    public ResourceLocation getOutputItem() {
        return this.outputItem;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    public int getOutputQuantity() {
        return this.outputQuantity;
    }

    public void setOutputQuantity(int quantity) {
        this.outputQuantity = quantity;
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        if (inv.m_6643_() != 1) {
            return false;
        } else {
            ItemStack stack = inv.m_8020_(0);
            return ForgeRegistries.ITEMS.getKey(stack.getItem()).equals(this.inputItem);
        }
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

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        return this.m_8043_(access);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.ARCANE_FURNACE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.ARCANE_FURNACE_TYPE.get();
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