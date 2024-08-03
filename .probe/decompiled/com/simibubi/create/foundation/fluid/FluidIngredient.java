package com.simibubi.create.foundation.fluid;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class FluidIngredient implements Predicate<FluidStack> {

    public static final FluidIngredient EMPTY = new FluidIngredient.FluidStackIngredient();

    public List<FluidStack> matchingFluidStacks;

    protected int amountRequired;

    public static FluidIngredient fromTag(TagKey<Fluid> tag, int amount) {
        FluidIngredient.FluidTagIngredient ingredient = new FluidIngredient.FluidTagIngredient();
        ingredient.tag = tag;
        ingredient.amountRequired = amount;
        return ingredient;
    }

    public static FluidIngredient fromFluid(Fluid fluid, int amount) {
        FluidIngredient.FluidStackIngredient ingredient = new FluidIngredient.FluidStackIngredient();
        ingredient.fluid = fluid;
        ingredient.amountRequired = amount;
        ingredient.fixFlowing();
        return ingredient;
    }

    public static FluidIngredient fromFluidStack(FluidStack fluidStack) {
        FluidIngredient.FluidStackIngredient ingredient = new FluidIngredient.FluidStackIngredient();
        ingredient.fluid = fluidStack.getFluid();
        ingredient.amountRequired = fluidStack.getAmount();
        ingredient.fixFlowing();
        if (fluidStack.hasTag()) {
            ingredient.tagToMatch = fluidStack.getTag();
        }
        return ingredient;
    }

    protected abstract boolean testInternal(FluidStack var1);

    protected abstract void readInternal(FriendlyByteBuf var1);

    protected abstract void writeInternal(FriendlyByteBuf var1);

    protected abstract void readInternal(JsonObject var1);

    protected abstract void writeInternal(JsonObject var1);

    protected abstract List<FluidStack> determineMatchingFluidStacks();

    public int getRequiredAmount() {
        return this.amountRequired;
    }

    public List<FluidStack> getMatchingFluidStacks() {
        return this.matchingFluidStacks != null ? this.matchingFluidStacks : (this.matchingFluidStacks = this.determineMatchingFluidStacks());
    }

    public boolean test(FluidStack t) {
        if (t == null) {
            throw new IllegalArgumentException("FluidStack cannot be null");
        } else {
            return this.testInternal(t);
        }
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this instanceof FluidIngredient.FluidTagIngredient);
        buffer.writeVarInt(this.amountRequired);
        this.writeInternal(buffer);
    }

    public static FluidIngredient read(FriendlyByteBuf buffer) {
        boolean isTagIngredient = buffer.readBoolean();
        FluidIngredient ingredient = (FluidIngredient) (isTagIngredient ? new FluidIngredient.FluidTagIngredient() : new FluidIngredient.FluidStackIngredient());
        ingredient.amountRequired = buffer.readVarInt();
        ingredient.readInternal(buffer);
        return ingredient;
    }

    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        this.writeInternal(json);
        json.addProperty("amount", this.amountRequired);
        return json;
    }

    public static boolean isFluidIngredient(@Nullable JsonElement je) {
        if (je == null || je.isJsonNull()) {
            return false;
        } else if (!je.isJsonObject()) {
            return false;
        } else {
            JsonObject json = je.getAsJsonObject();
            return json.has("fluidTag") ? true : json.has("fluid");
        }
    }

    public static FluidIngredient deserialize(@Nullable JsonElement je) {
        if (!isFluidIngredient(je)) {
            throw new JsonSyntaxException("Invalid fluid ingredient: " + Objects.toString(je));
        } else {
            JsonObject json = je.getAsJsonObject();
            FluidIngredient ingredient = (FluidIngredient) (json.has("fluidTag") ? new FluidIngredient.FluidTagIngredient() : new FluidIngredient.FluidStackIngredient());
            ingredient.readInternal(json);
            if (!json.has("amount")) {
                throw new JsonSyntaxException("Fluid ingredient has to define an amount");
            } else {
                ingredient.amountRequired = GsonHelper.getAsInt(json, "amount");
                return ingredient;
            }
        }
    }

    public static class FluidStackIngredient extends FluidIngredient {

        protected Fluid fluid;

        protected CompoundTag tagToMatch = new CompoundTag();

        void fixFlowing() {
            if (this.fluid instanceof FlowingFluid) {
                this.fluid = ((FlowingFluid) this.fluid).getSource();
            }
        }

        @Override
        protected boolean testInternal(FluidStack t) {
            if (!t.getFluid().isSame(this.fluid)) {
                return false;
            } else if (this.tagToMatch.isEmpty()) {
                return true;
            } else {
                CompoundTag tag = t.getOrCreateTag();
                return tag.copy().merge(this.tagToMatch).equals(tag);
            }
        }

        @Override
        protected void readInternal(FriendlyByteBuf buffer) {
            this.fluid = (Fluid) buffer.readRegistryId();
            this.tagToMatch = buffer.readNbt();
        }

        @Override
        protected void writeInternal(FriendlyByteBuf buffer) {
            buffer.writeRegistryId(ForgeRegistries.FLUIDS, this.fluid);
            buffer.writeNbt(this.tagToMatch);
        }

        @Override
        protected void readInternal(JsonObject json) {
            FluidStack stack = FluidHelper.deserializeFluidStack(json);
            this.fluid = stack.getFluid();
            this.tagToMatch = stack.getOrCreateTag();
        }

        @Override
        protected void writeInternal(JsonObject json) {
            json.addProperty("fluid", RegisteredObjects.getKeyOrThrow(this.fluid).toString());
            json.add("nbt", JsonParser.parseString(this.tagToMatch.toString()));
        }

        @Override
        protected List<FluidStack> determineMatchingFluidStacks() {
            return ImmutableList.of(this.tagToMatch.isEmpty() ? new FluidStack(this.fluid, this.amountRequired) : new FluidStack(this.fluid, this.amountRequired, this.tagToMatch));
        }
    }

    public static class FluidTagIngredient extends FluidIngredient {

        protected TagKey<Fluid> tag;

        @Override
        protected boolean testInternal(FluidStack t) {
            if (this.tag == null) {
                for (FluidStack accepted : this.getMatchingFluidStacks()) {
                    if (accepted.getFluid().isSame(t.getFluid())) {
                        return true;
                    }
                }
                return false;
            } else {
                return t.getFluid().is(this.tag);
            }
        }

        @Override
        protected void readInternal(FriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            this.matchingFluidStacks = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                this.matchingFluidStacks.add(buffer.readFluidStack());
            }
        }

        @Override
        protected void writeInternal(FriendlyByteBuf buffer) {
            List<FluidStack> matchingFluidStacks = this.getMatchingFluidStacks();
            buffer.writeVarInt(matchingFluidStacks.size());
            matchingFluidStacks.stream().forEach(buffer::writeFluidStack);
        }

        @Override
        protected void readInternal(JsonObject json) {
            ResourceLocation name = new ResourceLocation(GsonHelper.getAsString(json, "fluidTag"));
            this.tag = FluidTags.create(name);
        }

        @Override
        protected void writeInternal(JsonObject json) {
            json.addProperty("fluidTag", this.tag.location().toString());
        }

        @Override
        protected List<FluidStack> determineMatchingFluidStacks() {
            return (List<FluidStack>) ForgeRegistries.FLUIDS.tags().getTag(this.tag).stream().map(f -> f instanceof FlowingFluid ? ((FlowingFluid) f).getSource() : f).distinct().map(f -> new FluidStack(f, this.amountRequired)).collect(Collectors.toList());
        }
    }
}