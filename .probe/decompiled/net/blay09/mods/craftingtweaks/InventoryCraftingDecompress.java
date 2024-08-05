package net.blay09.mods.craftingtweaks;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public class InventoryCraftingDecompress extends TransientCraftingContainer implements RecipeHolder {

    private Recipe<?> recipeUsed;

    public InventoryCraftingDecompress(AbstractContainerMenu menu, ItemStack itemStack) {
        super(menu, 3, 3);
        this.m_6836_(0, itemStack.copy());
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        this.recipeUsed = recipe;
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return this.recipeUsed;
    }
}