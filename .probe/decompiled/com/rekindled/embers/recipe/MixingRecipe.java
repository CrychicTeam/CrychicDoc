package com.rekindled.embers.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rekindled.embers.util.Misc;
import java.util.ArrayList;
import java.util.HashSet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class MixingRecipe implements IMixingRecipe {

    public static final MixingRecipe.Serializer SERIALIZER = new MixingRecipe.Serializer();

    public final ResourceLocation id;

    public final ArrayList<FluidIngredient> inputs;

    public final FluidStack output;

    public MixingRecipe(ResourceLocation id, ArrayList<FluidIngredient> inputs, FluidStack output) {
        this.id = id;
        this.inputs = inputs;
        this.output = output;
    }

    public boolean matches(MixingContext context, Level pLevel) {
        HashSet<FluidIngredient> remaining = new HashSet();
        remaining.addAll(this.inputs);
        for (IFluidHandler handler : context.fluids) {
            boolean matched = false;
            for (FluidIngredient fluid : remaining) {
                for (FluidStack stack : fluid.getAllFluids()) {
                    if (fluid.test(handler.drain(stack, IFluidHandler.FluidAction.SIMULATE))) {
                        remaining.remove(fluid);
                        matched = true;
                        break;
                    }
                }
                if (matched) {
                    break;
                }
            }
            if (!matched && !handler.drain(1, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                return false;
            }
        }
        return remaining.isEmpty();
    }

    @Override
    public FluidStack getOutput(MixingContext context) {
        return this.output;
    }

    @Override
    public FluidStack process(MixingContext context) {
        HashSet<FluidIngredient> remaining = new HashSet();
        remaining.addAll(this.inputs);
        for (IFluidHandler handler : context.fluids) {
            for (FluidIngredient fluid : remaining) {
                boolean matched = false;
                for (FluidStack stack : fluid.getAllFluids()) {
                    if (fluid.test(handler.drain(stack, IFluidHandler.FluidAction.SIMULATE))) {
                        handler.drain(stack, IFluidHandler.FluidAction.EXECUTE);
                        matched = true;
                        break;
                    }
                }
                if (matched) {
                    break;
                }
            }
        }
        return this.output;
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
    public ArrayList<FluidIngredient> getDisplayInputFluids() {
        return this.inputs;
    }

    @Override
    public FluidStack getDisplayOutput() {
        return this.output;
    }

    public static class Serializer implements RecipeSerializer<MixingRecipe> {

        public MixingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            FluidStack output = Misc.deserializeFluidStack(GsonHelper.getAsJsonObject(json, "output"));
            ArrayList<FluidIngredient> inputs = new ArrayList();
            JsonArray inputJson = GsonHelper.getAsJsonArray(json, "inputs", null);
            if (inputJson != null) {
                for (JsonElement element : inputJson) {
                    inputs.add(FluidIngredient.deserialize(element, "fluid"));
                }
            }
            return new MixingRecipe(recipeId, inputs, output);
        }

        @Nullable
        public MixingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ArrayList<FluidIngredient> inputs = buffer.readCollection(i -> new ArrayList(), buf -> FluidIngredient.read(buf));
            FluidStack output = FluidStack.readFromPacket(buffer);
            return new MixingRecipe(recipeId, inputs, output);
        }

        public void toNetwork(FriendlyByteBuf buffer, MixingRecipe recipe) {
            buffer.writeCollection(recipe.inputs, (buf, input) -> input.write(buf));
            recipe.output.writeToPacket(buffer);
        }
    }
}