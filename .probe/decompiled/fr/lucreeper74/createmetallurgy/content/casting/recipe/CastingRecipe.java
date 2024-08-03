package fr.lucreeper74.createmetallurgy.content.casting.recipe;

import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import fr.lucreeper74.createmetallurgy.content.casting.CastingBlockEntity;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public abstract class CastingRecipe implements Recipe<SmartInventory> {

    protected final ResourceLocation id;

    protected FluidIngredient fluidIngredient;

    protected Ingredient ingredient;

    protected int processingDuration;

    protected boolean moldConsumed;

    protected ProcessingOutput result;

    public CastingRecipe(ResourceLocation id, Ingredient ingredient, FluidIngredient fluid, int processingTime, boolean moldConsumed, ProcessingOutput result) {
        this.id = id;
        this.ingredient = ingredient;
        this.fluidIngredient = fluid;
        this.processingDuration = processingTime;
        this.moldConsumed = moldConsumed;
        this.result = result;
    }

    public static boolean match(CastingBlockEntity be, Recipe<?> recipe) {
        if (!(recipe instanceof CastingRecipe castingRecipe)) {
            return false;
        } else {
            FluidStack fluidInTank = be.getFluidTank().getFluidInTank(0);
            ItemStack mold = be.moldInv.getStackInSlot(0);
            Ingredient ingredient = castingRecipe.getIngredient();
            boolean fluidMatches = castingRecipe.getFluidIngredient().test(fluidInTank) && fluidInTank.getAmount() >= castingRecipe.getFluidIngredient().getRequiredAmount();
            boolean hasMold = !ingredient.isEmpty();
            boolean ingredientMatches = hasMold && ingredient.test(mold);
            return fluidMatches && (!hasMold || ingredientMatches);
        }
    }

    public boolean matches(SmartInventory pContainer, Level pLevel) {
        return false;
    }

    public ItemStack assemble(SmartInventory pContainer, RegistryAccess registryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result.getStack();
    }

    public ProcessingOutput getProcessingOutput() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public abstract RecipeSerializer<?> getSerializer();

    @Override
    public abstract RecipeType<?> getType();

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public FluidIngredient getFluidIngredient() {
        return this.fluidIngredient;
    }

    public int getProcessingDuration() {
        return this.processingDuration;
    }

    public boolean isMoldConsumed() {
        return this.moldConsumed;
    }
}