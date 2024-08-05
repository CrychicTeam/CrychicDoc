package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import com.rekindled.embers.util.Misc;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class BoilingRecipe implements IBoilingRecipe {

    public static final BoilingRecipe.Serializer SERIALIZER = new BoilingRecipe.Serializer();

    public final ResourceLocation id;

    public final FluidIngredient input;

    public final FluidStack output;

    public BoilingRecipe(ResourceLocation id, FluidIngredient input, FluidStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    public boolean matches(FluidHandlerContext context, Level pLevel) {
        for (FluidStack stack : this.input.getAllFluids()) {
            if (this.input.test(context.fluid.drain(stack, IFluidHandler.FluidAction.SIMULATE))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FluidStack getOutput(FluidHandlerContext context) {
        return this.output;
    }

    @Override
    public FluidStack process(FluidHandlerContext context, int amount) {
        int trueAmount = amount;
        for (FluidStack stack : this.input.getAllFluids()) {
            FluidStack drainStack = new FluidStack(stack, stack.getAmount() * amount);
            if (this.input.test(context.fluid.drain(drainStack, IFluidHandler.FluidAction.SIMULATE))) {
                trueAmount = context.fluid.drain(drainStack, IFluidHandler.FluidAction.EXECUTE).getAmount() / stack.getAmount();
                break;
            }
        }
        return new FluidStack(this.output, this.output.getAmount() * trueAmount);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public FluidIngredient getDisplayInput() {
        return this.input;
    }

    @Override
    public FluidStack getDisplayOutput() {
        return this.output;
    }

    public static class Serializer implements RecipeSerializer<BoilingRecipe> {

        public BoilingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            FluidStack output = Misc.deserializeFluidStack(GsonHelper.getAsJsonObject(json, "output"));
            FluidIngredient input = FluidIngredient.deserialize(json, "input");
            return new BoilingRecipe(recipeId, input, output);
        }

        @Nullable
        public BoilingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            FluidIngredient input = FluidIngredient.read(buffer);
            FluidStack output = FluidStack.readFromPacket(buffer);
            return new BoilingRecipe(recipeId, input, output);
        }

        public void toNetwork(FriendlyByteBuf buffer, BoilingRecipe recipe) {
            recipe.input.write(buffer);
            recipe.output.writeToPacket(buffer);
        }
    }
}