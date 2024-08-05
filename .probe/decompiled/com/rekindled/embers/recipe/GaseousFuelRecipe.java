package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class GaseousFuelRecipe implements IGaseousFuelRecipe {

    public static final GaseousFuelRecipe.Serializer SERIALIZER = new GaseousFuelRecipe.Serializer();

    public final ResourceLocation id;

    public final FluidIngredient input;

    public final int burnTime;

    public final double powerMultiplier;

    public GaseousFuelRecipe(ResourceLocation id, FluidIngredient input, int burnTime, double powerMultiplier) {
        this.id = id;
        this.input = input;
        this.burnTime = burnTime;
        this.powerMultiplier = powerMultiplier;
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
    public int getBurnTime(FluidHandlerContext context) {
        return this.burnTime;
    }

    @Override
    public double getPowerMultiplier(FluidHandlerContext context) {
        return this.powerMultiplier;
    }

    @Override
    public int process(FluidHandlerContext context, int amount) {
        int trueAmount = amount;
        for (FluidStack stack : this.input.getAllFluids()) {
            FluidStack drainStack = new FluidStack(stack, stack.getAmount() * amount);
            if (this.input.test(context.fluid.drain(drainStack, IFluidHandler.FluidAction.SIMULATE))) {
                trueAmount = context.fluid.drain(drainStack, IFluidHandler.FluidAction.EXECUTE).getAmount() / stack.getAmount();
                break;
            }
        }
        return this.burnTime * trueAmount;
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
    public int getDisplayBurnTime() {
        return this.burnTime;
    }

    @Override
    public double getDisplayMultiplier() {
        return this.powerMultiplier;
    }

    public static class Serializer implements RecipeSerializer<GaseousFuelRecipe> {

        public GaseousFuelRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            FluidIngredient input = FluidIngredient.deserialize(json, "input");
            int burnTime = GsonHelper.getAsInt(json, "burn_time");
            double powerMultiplier = GsonHelper.getAsDouble(json, "power_multiplier");
            return new GaseousFuelRecipe(recipeId, input, burnTime, powerMultiplier);
        }

        @Nullable
        public GaseousFuelRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            FluidIngredient input = FluidIngredient.read(buffer);
            int burnTime = buffer.readInt();
            double powerMultiplier = buffer.readDouble();
            return new GaseousFuelRecipe(recipeId, input, burnTime, powerMultiplier);
        }

        public void toNetwork(FriendlyByteBuf buffer, GaseousFuelRecipe recipe) {
            recipe.input.write(buffer);
            buffer.writeInt(recipe.burnTime);
            buffer.writeDouble(recipe.powerMultiplier);
        }
    }
}