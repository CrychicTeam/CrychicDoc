package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.handler.data.IRecipe;
import noppes.npcs.controllers.RecipeController;

public class RecipeCarpentry extends ShapedRecipe implements IRecipe {

    public Availability availability = new Availability();

    public boolean isGlobal = false;

    public boolean ignoreDamage = false;

    public boolean ignoreNBT = false;

    public boolean savesRecipe = true;

    public String name;

    public RecipeCarpentry(ResourceLocation location, int width, int height, NonNullList<Ingredient> recipe, ItemStack result) {
        super(location, "customnpcs", CraftingBookCategory.MISC, width, height, recipe, result);
    }

    public RecipeCarpentry(ResourceLocation location, String name) {
        super(location, "customnpcs", CraftingBookCategory.MISC, 0, 0, NonNullList.create(), ItemStack.EMPTY);
        this.name = name;
    }

    public static RecipeCarpentry load(CompoundTag compound) {
        ResourceLocation location = null;
        if (compound.contains("ID")) {
            location = new ResourceLocation("customnpcs", compound.getString("ID"));
        } else {
            location = new ResourceLocation(compound.getString("Id"));
        }
        RecipeCarpentry recipe = new RecipeCarpentry(location, compound.getInt("Width"), compound.getInt("Height"), NBTTags.getIngredientList(compound.getList("Materials", 10)), ItemStack.of(compound.getCompound("Item")));
        recipe.availability.load(compound.getCompound("Availability"));
        recipe.ignoreDamage = compound.getBoolean("IgnoreDamage");
        recipe.ignoreNBT = compound.getBoolean("IgnoreNBT");
        recipe.isGlobal = compound.getBoolean("Global");
        return recipe;
    }

    public CompoundTag writeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("Width", this.getRecipeWidth());
        compound.putInt("Height", this.getRecipeHeight());
        if (this.getResult() != null) {
            compound.put("Item", this.getResult().save(new CompoundTag()));
        }
        compound.put("Materials", NBTTags.nbtIngredientList(this.m_7527_()));
        compound.put("Availability", this.availability.save(new CompoundTag()));
        compound.putString("Name", this.name);
        compound.putString("Id", this.m_6423_().toString());
        compound.putBoolean("Global", this.isGlobal);
        compound.putBoolean("IgnoreDamage", this.ignoreDamage);
        compound.putBoolean("IgnoreNBT", this.ignoreNBT);
        return compound;
    }

    public static RecipeCarpentry createRecipe(ResourceLocation location, RecipeCarpentry recipe, ItemStack par1ItemStack, Object... limbSwingAmountArrayOfObj) {
        String var3x = "";
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;
        if (limbSwingAmountArrayOfObj[var4] instanceof String[]) {
            String[] var7 = (String[]) limbSwingAmountArrayOfObj[var4++];
            for (String var11 : var7) {
                var6++;
                var5 = var11.length();
                var3x = var3x + var11;
            }
        } else {
            while (limbSwingAmountArrayOfObj[var4] instanceof String) {
                String var13 = (String) limbSwingAmountArrayOfObj[var4++];
                var6++;
                var5 = var13.length();
                var3x = var3x + var13;
            }
        }
        HashMap var14;
        for (var14 = new HashMap(); var4 < limbSwingAmountArrayOfObj.length; var4 += 2) {
            Character var16 = (Character) limbSwingAmountArrayOfObj[var4];
            ItemStack var17 = ItemStack.EMPTY;
            if (limbSwingAmountArrayOfObj[var4 + 1] instanceof Item) {
                var17 = new ItemStack((Item) limbSwingAmountArrayOfObj[var4 + 1]);
            } else if (limbSwingAmountArrayOfObj[var4 + 1] instanceof Block) {
                var17 = new ItemStack((Block) limbSwingAmountArrayOfObj[var4 + 1], 1);
            } else if (limbSwingAmountArrayOfObj[var4 + 1] instanceof ItemStack) {
                var17 = (ItemStack) limbSwingAmountArrayOfObj[var4 + 1];
            }
            var14.put(var16, var17);
        }
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (int var9 = 0; var9 < var5 * var6; var9++) {
            char var18 = var3x.charAt(var9);
            if (var14.containsKey(var18)) {
                ingredients.add(var9, Ingredient.of(((ItemStack) var14.get(var18)).copy()));
            } else {
                ingredients.add(var9, Ingredient.EMPTY);
            }
        }
        RecipeCarpentry newrecipe = new RecipeCarpentry(location, var5, var6, ingredients, par1ItemStack);
        newrecipe.copy(recipe);
        if (var5 == 4 || var6 == 4) {
            newrecipe.isGlobal = false;
        }
        return newrecipe;
    }

    @Override
    public boolean matches(CraftingContainer inventoryCrafting, Level world) {
        for (int i = 0; i <= 4 - this.getRecipeWidth(); i++) {
            for (int j = 0; j <= 4 - this.getRecipeHeight(); j++) {
                if (this.checkMatch(inventoryCrafting, i, j, true)) {
                    return true;
                }
                if (this.checkMatch(inventoryCrafting, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        return this.getResult().isEmpty() ? ItemStack.EMPTY : this.getResult().copy();
    }

    private boolean checkMatch(Container inventoryCrafting, int limbSwingAmount, int par3, boolean par4) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int var7 = i - limbSwingAmount;
                int var8 = j - par3;
                Ingredient ingredient = Ingredient.EMPTY;
                if (var7 >= 0 && var8 >= 0 && var7 < this.getRecipeWidth() && var8 < this.getRecipeHeight()) {
                    if (par4) {
                        ingredient = (Ingredient) this.m_7527_().get(this.getRecipeWidth() - var7 - 1 + var8 * this.getRecipeWidth());
                    } else {
                        ingredient = (Ingredient) this.m_7527_().get(var7 + var8 * this.getRecipeWidth());
                    }
                }
                ItemStack var10 = ItemStack.EMPTY;
                if (!var10.isEmpty() || ingredient.getItems().length == 0) {
                    return false;
                }
                ItemStack var9 = ingredient.getItems()[0];
                if ((!var10.isEmpty() || !var9.isEmpty()) && !NoppesUtilPlayer.compareItems(var9, var10, this.ignoreDamage, this.ignoreNBT)) {
                    return false;
                }
            }
        }
        return true;
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inventoryCrafting) {
        NonNullList<ItemStack> list = NonNullList.withSize(inventoryCrafting.m_6643_(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++) {
            ItemStack itemstack = inventoryCrafting.m_8020_(i);
            list.set(i, ForgeHooks.getCraftingRemainingItem(itemstack));
        }
        return list;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    public void copy(RecipeCarpentry recipe) {
        this.availability = recipe.availability;
        this.isGlobal = recipe.isGlobal;
        this.ignoreDamage = recipe.ignoreDamage;
        this.ignoreNBT = recipe.ignoreNBT;
    }

    public ItemStack getCraftingItem(int i) {
        if (i >= this.m_7527_().size()) {
            return ItemStack.EMPTY;
        } else {
            Ingredient ingredients = (Ingredient) this.m_7527_().get(i);
            return ingredients.getItems().length == 0 ? ItemStack.EMPTY : ingredients.getItems()[0];
        }
    }

    public boolean isValid() {
        if (this.m_7527_().size() != 0 && !this.getResult().isEmpty()) {
            for (Ingredient ingredient : this.m_7527_()) {
                if (ingredient.getItems().length > 0) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ItemStack getResult() {
        return this.getResult();
    }

    @Override
    public boolean isGlobal() {
        return this.isGlobal;
    }

    @Override
    public void setIsGlobal(boolean bo) {
        this.isGlobal = bo;
    }

    @Override
    public boolean getIgnoreNBT() {
        return this.ignoreNBT;
    }

    @Override
    public void setIgnoreNBT(boolean bo) {
        this.ignoreNBT = bo;
    }

    @Override
    public boolean getIgnoreDamage() {
        return this.ignoreDamage;
    }

    @Override
    public void setIgnoreDamage(boolean bo) {
        this.ignoreDamage = bo;
    }

    @Override
    public void save() {
        RecipeController.instance.saveRecipe(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public int getWidth() {
        return this.getRecipeWidth();
    }

    @Override
    public int getHeight() {
        return this.getRecipeHeight();
    }

    @Override
    public ItemStack[] getRecipe() {
        List<ItemStack> list = new ArrayList();
        for (Ingredient ingredient : this.m_7527_()) {
            if (ingredient.getItems().length > 0) {
                list.add(ingredient.getItems()[0]);
            }
        }
        return (ItemStack[]) list.toArray(new ItemStack[list.size()]);
    }

    @Override
    public void saves(boolean bo) {
        this.savesRecipe = bo;
    }

    @Override
    public boolean saves() {
        return this.savesRecipe;
    }
}