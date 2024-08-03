package com.simibubi.create.content.fluids.transfer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class FillingRecipe extends ProcessingRecipe<RecipeWrapper> implements IAssemblyRecipe {

    public FillingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.FILLING, params);
    }

    public boolean matches(RecipeWrapper inv, Level p_77569_2_) {
        return this.ingredients.get(0).test(inv.getItem(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    public FluidIngredient getRequiredFluid() {
        if (this.fluidIngredients.isEmpty()) {
            throw new IllegalStateException("Filling Recipe: " + this.id.toString() + " has no fluid ingredient!");
        } else {
            return this.fluidIngredients.get(0);
        }
    }

    @Override
    public void addAssemblyIngredients(List<Ingredient> list) {
    }

    @Override
    public void addAssemblyFluidIngredients(List<FluidIngredient> list) {
        list.add(this.getRequiredFluid());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getDescriptionForAssembly() {
        List<FluidStack> matchingFluidStacks = this.fluidIngredients.get(0).getMatchingFluidStacks();
        return matchingFluidStacks.size() == 0 ? Components.literal("Invalid") : Lang.translateDirect("recipe.assembly.spout_filling_fluid", ((FluidStack) matchingFluidStacks.get(0)).getDisplayName().getString());
    }

    @Override
    public void addRequiredMachines(Set<ItemLike> list) {
        list.add((ItemLike) AllBlocks.SPOUT.get());
    }

    @Override
    public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
        return () -> SequencedAssemblySubCategory.AssemblySpouting::new;
    }
}