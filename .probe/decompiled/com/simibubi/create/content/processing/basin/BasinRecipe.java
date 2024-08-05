package com.simibubi.create.content.processing.basin;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.DummyCraftingContainer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class BasinRecipe extends ProcessingRecipe<SmartInventory> {

    public static boolean match(BasinBlockEntity basin, Recipe<?> recipe) {
        FilteringBehaviour filter = basin.getFilter();
        if (filter == null) {
            return false;
        } else {
            boolean filterTest = filter.test(recipe.getResultItem(basin.m_58904_().registryAccess()));
            if (recipe instanceof BasinRecipe basinRecipe && basinRecipe.getRollableResults().isEmpty() && !basinRecipe.getFluidResults().isEmpty()) {
                filterTest = filter.test(basinRecipe.getFluidResults().get(0));
            }
            return !filterTest ? false : apply(basin, recipe, true);
        }
    }

    public static boolean apply(BasinBlockEntity basin, Recipe<?> recipe) {
        return apply(basin, recipe, false);
    }

    private static boolean apply(BasinBlockEntity basin, Recipe<?> recipe, boolean test) {
        boolean isBasinRecipe = recipe instanceof BasinRecipe;
        IItemHandler availableItems = (IItemHandler) basin.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
        IFluidHandler availableFluids = (IFluidHandler) basin.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
        if (availableItems != null && availableFluids != null) {
            BlazeBurnerBlock.HeatLevel heat = BasinBlockEntity.getHeatLevelOf(basin.m_58904_().getBlockState(basin.m_58899_().below(1)));
            if (isBasinRecipe && !((BasinRecipe) recipe).getRequiredHeat().testBlazeBurner(heat)) {
                return false;
            } else {
                List<ItemStack> recipeOutputItems = new ArrayList();
                List<FluidStack> recipeOutputFluids = new ArrayList();
                List<Ingredient> ingredients = new LinkedList(recipe.getIngredients());
                List<FluidIngredient> fluidIngredients = (List<FluidIngredient>) (isBasinRecipe ? ((BasinRecipe) recipe).getFluidIngredients() : Collections.emptyList());
                for (boolean simulate : Iterate.trueAndFalse) {
                    if (!simulate && test) {
                        return true;
                    }
                    int[] extractedItemsFromSlot = new int[availableItems.getSlots()];
                    int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];
                    label99: for (int i = 0; i < ingredients.size(); i++) {
                        Ingredient ingredient = (Ingredient) ingredients.get(i);
                        for (int slot = 0; slot < availableItems.getSlots(); slot++) {
                            if (!simulate || availableItems.getStackInSlot(slot).getCount() > extractedItemsFromSlot[slot]) {
                                ItemStack extracted = availableItems.extractItem(slot, 1, true);
                                if (ingredient.test(extracted)) {
                                    if (!simulate) {
                                        availableItems.extractItem(slot, 1, false);
                                    }
                                    extractedItemsFromSlot[slot]++;
                                    continue label99;
                                }
                            }
                        }
                        return false;
                    }
                    boolean fluidsAffected = false;
                    label119: for (int i = 0; i < fluidIngredients.size(); i++) {
                        FluidIngredient fluidIngredient = (FluidIngredient) fluidIngredients.get(i);
                        int amountRequired = fluidIngredient.getRequiredAmount();
                        for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                            FluidStack fluidStack = availableFluids.getFluidInTank(tank);
                            if ((!simulate || fluidStack.getAmount() > extractedFluidsFromTank[tank]) && fluidIngredient.test(fluidStack)) {
                                int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
                                if (!simulate) {
                                    fluidStack.shrink(drainedAmount);
                                    fluidsAffected = true;
                                }
                                amountRequired -= drainedAmount;
                                if (amountRequired == 0) {
                                    extractedFluidsFromTank[tank] += drainedAmount;
                                    continue label119;
                                }
                            }
                        }
                        return false;
                    }
                    if (fluidsAffected) {
                        basin.getBehaviour(SmartFluidTankBehaviour.INPUT).forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
                        basin.getBehaviour(SmartFluidTankBehaviour.OUTPUT).forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
                    }
                    if (simulate) {
                        if (recipe instanceof BasinRecipe basinRecipe) {
                            recipeOutputItems.addAll(basinRecipe.rollResults());
                            recipeOutputFluids.addAll(basinRecipe.getFluidResults());
                            recipeOutputItems.addAll(basinRecipe.m_7457_(basin.getInputInventory()));
                        } else {
                            recipeOutputItems.add(recipe.getResultItem(basin.m_58904_().registryAccess()));
                            if (recipe instanceof CraftingRecipe craftingRecipe) {
                                recipeOutputItems.addAll(craftingRecipe.m_7457_(new DummyCraftingContainer(availableItems, extractedItemsFromSlot)));
                            }
                        }
                    }
                    if (!basin.acceptOutputs(recipeOutputItems, recipeOutputFluids, simulate)) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public static BasinRecipe convertShapeless(Recipe<?> recipe) {
        return new ProcessingRecipeBuilder<>(BasinRecipe::new, recipe.getId()).withItemIngredients(recipe.getIngredients()).withSingleItemOutput(recipe.getResultItem(Minecraft.getInstance().level.m_9598_())).build();
    }

    protected BasinRecipe(IRecipeTypeInfo type, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(type, params);
    }

    public BasinRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        this(AllRecipeTypes.BASIN, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 9;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 2;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 2;
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    public boolean matches(SmartInventory inv, @Nonnull Level worldIn) {
        return false;
    }
}