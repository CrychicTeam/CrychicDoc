package com.mna.recipes.runeforging;

import com.google.gson.JsonObject;
import com.mna.api.recipes.IRunescribeRecipe;
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

public class RunescribingRecipe extends AMRecipeBase implements IRunescribeRecipe {

    private long hMutex;

    private long vMutex;

    private ResourceLocation patternItem;

    private Item __outputItem = null;

    public RunescribingRecipe(ResourceLocation idIn) {
        super(idIn);
        this.patternItem = new ResourceLocation("");
    }

    @Override
    public void parseExtraJson(JsonObject recipeJSON) {
        this.hMutex = recipeJSON.get("mutex_h").getAsLong();
        this.vMutex = recipeJSON.get("mutex_v").getAsLong();
        this.patternItem = new ResourceLocation(recipeJSON.get("output").getAsString());
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack checkStack = inv.m_8020_(0);
        return inv.m_6643_() == 1 && checkStack.getItem() == ItemInit.RUNE_PATTERN.get() ? ItemInit.RUNE_PATTERN.get().getHMutex(checkStack) == this.hMutex && ItemInit.RUNE_PATTERN.get().getVMutex(checkStack) == this.vMutex : false;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        return this.getResultItem();
    }

    @Override
    public ItemStack getResultItem() {
        if (this.__outputItem == null) {
            IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;
            if (itemRegistry != null) {
                this.__outputItem = itemRegistry.getValue(this.patternItem);
            }
        }
        return this.__outputItem != null ? new ItemStack(this.__outputItem) : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.RUNESCRIBING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.RUNESCRIBING_TYPE.get();
    }

    @Override
    public long getHMutex() {
        return this.hMutex;
    }

    public void setHMutex(long mutex) {
        this.hMutex = mutex;
    }

    @Override
    public long getVMutex() {
        return this.vMutex;
    }

    public void setVMutex(long mutex) {
        this.vMutex = mutex;
    }

    public ResourceLocation getOutputResource() {
        return this.patternItem;
    }

    public void setOutputResource(String output) {
        this.patternItem = new ResourceLocation(output);
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