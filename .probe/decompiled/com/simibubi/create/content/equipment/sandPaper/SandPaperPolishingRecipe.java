package com.simibubi.create.content.equipment.sandPaper;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@ParametersAreNonnullByDefault
public class SandPaperPolishingRecipe extends ProcessingRecipe<SandPaperPolishingRecipe.SandPaperInv> {

    public SandPaperPolishingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.SANDPAPER_POLISHING, params);
    }

    public boolean matches(SandPaperPolishingRecipe.SandPaperInv inv, Level worldIn) {
        return this.ingredients.get(0).test(inv.m_8020_(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 1;
    }

    public static boolean canPolish(Level world, ItemStack stack) {
        return !getMatchingRecipes(world, stack).isEmpty();
    }

    public static ItemStack applyPolish(Level world, Vec3 position, ItemStack stack, ItemStack sandPaperStack) {
        List<Recipe<SandPaperPolishingRecipe.SandPaperInv>> matchingRecipes = getMatchingRecipes(world, stack);
        return !matchingRecipes.isEmpty() ? ((Recipe) matchingRecipes.get(0)).assemble(new SandPaperPolishingRecipe.SandPaperInv(stack), world.registryAccess()).copy() : stack;
    }

    public static List<Recipe<SandPaperPolishingRecipe.SandPaperInv>> getMatchingRecipes(Level world, ItemStack stack) {
        return world.getRecipeManager().getRecipesFor(AllRecipeTypes.SANDPAPER_POLISHING.getType(), new SandPaperPolishingRecipe.SandPaperInv(stack), world);
    }

    public static class SandPaperInv extends RecipeWrapper {

        public SandPaperInv(ItemStack stack) {
            super(new ItemStackHandler(1));
            this.inv.setStackInSlot(0, stack);
        }
    }
}