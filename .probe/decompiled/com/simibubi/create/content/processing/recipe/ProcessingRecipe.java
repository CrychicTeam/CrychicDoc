package com.simibubi.create.content.processing.recipe;

import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import org.slf4j.Logger;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ProcessingRecipe<T extends Container> implements Recipe<T> {

    protected ResourceLocation id;

    protected NonNullList<Ingredient> ingredients;

    protected NonNullList<ProcessingOutput> results;

    protected NonNullList<FluidIngredient> fluidIngredients;

    protected NonNullList<FluidStack> fluidResults;

    protected int processingDuration;

    protected HeatCondition requiredHeat;

    private RecipeType<?> type;

    private RecipeSerializer<?> serializer;

    private IRecipeTypeInfo typeInfo;

    private Supplier<ItemStack> forcedResult = null;

    public ProcessingRecipe(IRecipeTypeInfo typeInfo, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        this.typeInfo = typeInfo;
        this.processingDuration = params.processingDuration;
        this.fluidIngredients = params.fluidIngredients;
        this.fluidResults = params.fluidResults;
        this.serializer = typeInfo.getSerializer();
        this.requiredHeat = params.requiredHeat;
        this.ingredients = params.ingredients;
        this.type = typeInfo.getType();
        this.results = params.results;
        this.id = params.id;
        this.validate(typeInfo.getId());
    }

    protected abstract int getMaxInputCount();

    protected abstract int getMaxOutputCount();

    protected boolean canRequireHeat() {
        return false;
    }

    protected boolean canSpecifyDuration() {
        return false;
    }

    protected int getMaxFluidInputCount() {
        return 0;
    }

    protected int getMaxFluidOutputCount() {
        return 0;
    }

    private void validate(ResourceLocation recipeTypeId) {
        String messageHeader = "Your custom " + recipeTypeId + " recipe (" + this.id.toString() + ")";
        Logger logger = Create.LOGGER;
        int ingredientCount = this.ingredients.size();
        int outputCount = this.results.size();
        if (ingredientCount > this.getMaxInputCount()) {
            logger.warn(messageHeader + " has more item inputs (" + ingredientCount + ") than supported (" + this.getMaxInputCount() + ").");
        }
        if (outputCount > this.getMaxOutputCount()) {
            logger.warn(messageHeader + " has more item outputs (" + outputCount + ") than supported (" + this.getMaxOutputCount() + ").");
        }
        if (this.processingDuration > 0 && !this.canSpecifyDuration()) {
            logger.warn(messageHeader + " specified a duration. Durations have no impact on this type of recipe.");
        }
        if (this.requiredHeat != HeatCondition.NONE && !this.canRequireHeat()) {
            logger.warn(messageHeader + " specified a heat condition. Heat conditions have no impact on this type of recipe.");
        }
        ingredientCount = this.fluidIngredients.size();
        outputCount = this.fluidResults.size();
        if (ingredientCount > this.getMaxFluidInputCount()) {
            logger.warn(messageHeader + " has more fluid inputs (" + ingredientCount + ") than supported (" + this.getMaxFluidInputCount() + ").");
        }
        if (outputCount > this.getMaxFluidOutputCount()) {
            logger.warn(messageHeader + " has more fluid outputs (" + outputCount + ") than supported (" + this.getMaxFluidOutputCount() + ").");
        }
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public NonNullList<FluidIngredient> getFluidIngredients() {
        return this.fluidIngredients;
    }

    public List<ProcessingOutput> getRollableResults() {
        return this.results;
    }

    public NonNullList<FluidStack> getFluidResults() {
        return this.fluidResults;
    }

    public List<ItemStack> getRollableResultsAsItemStacks() {
        return (List<ItemStack>) this.getRollableResults().stream().map(ProcessingOutput::getStack).collect(Collectors.toList());
    }

    public void enforceNextResult(Supplier<ItemStack> stack) {
        this.forcedResult = stack;
    }

    public List<ItemStack> rollResults() {
        return this.rollResults(this.getRollableResults());
    }

    public List<ItemStack> rollResults(List<ProcessingOutput> rollableResults) {
        List<ItemStack> results = new ArrayList();
        for (int i = 0; i < rollableResults.size(); i++) {
            ProcessingOutput output = (ProcessingOutput) rollableResults.get(i);
            ItemStack stack = i == 0 && this.forcedResult != null ? (ItemStack) this.forcedResult.get() : output.rollOutput();
            if (!stack.isEmpty()) {
                results.add(stack);
            }
        }
        return results;
    }

    public int getProcessingDuration() {
        return this.processingDuration;
    }

    public HeatCondition getRequiredHeat() {
        return this.requiredHeat;
    }

    @Override
    public ItemStack assemble(T inv, RegistryAccess registryAccess) {
        return this.getResultItem(registryAccess);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.getRollableResults().isEmpty() ? ItemStack.EMPTY : ((ProcessingOutput) this.getRollableResults().get(0)).getStack();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public String getGroup() {
        return "processing";
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    public IRecipeTypeInfo getTypeInfo() {
        return this.typeInfo;
    }

    public void readAdditional(JsonObject json) {
    }

    public void readAdditional(FriendlyByteBuf buffer) {
    }

    public void writeAdditional(JsonObject json) {
    }

    public void writeAdditional(FriendlyByteBuf buffer) {
    }
}