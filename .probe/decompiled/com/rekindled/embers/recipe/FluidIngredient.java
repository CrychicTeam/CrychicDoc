package com.rekindled.embers.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class FluidIngredient {

    public static final FluidIngredient EMPTY = new FluidIngredient.Empty();

    private List<FluidStack> displayFluids;

    public abstract boolean test(Fluid var1);

    public abstract int getAmount(Fluid var1);

    public boolean test(FluidStack stack) {
        Fluid fluid = stack.getFluid();
        return stack.getAmount() >= this.getAmount(fluid) && this.test(stack.getFluid());
    }

    public List<FluidStack> getFluids() {
        if (this.displayFluids == null) {
            this.displayFluids = (List<FluidStack>) this.getAllFluids().stream().filter(stack -> {
                Fluid fluid = stack.getFluid();
                return fluid.isSource(fluid.defaultFluidState());
            }).collect(Collectors.toList());
        }
        return this.displayFluids;
    }

    protected abstract List<FluidStack> getAllFluids();

    public abstract JsonElement serialize();

    public void write(FriendlyByteBuf buffer) {
        Collection<FluidStack> fluids = this.getAllFluids();
        buffer.writeInt(fluids.size());
        for (FluidStack stack : fluids) {
            buffer.writeResourceLocation(((ResourceKey) ForgeRegistries.FLUIDS.getResourceKey(stack.getFluid()).get()).location());
            buffer.writeVarInt(stack.getAmount());
        }
    }

    public static FluidIngredient of(Fluid fluid, int amount) {
        return new FluidIngredient.FluidMatch(fluid, amount);
    }

    public static FluidIngredient of(FluidStack stack) {
        return of(stack.getFluid(), stack.getAmount());
    }

    public static FluidIngredient of(TagKey<Fluid> fluid, int amount) {
        return new FluidIngredient.TagMatch(fluid, amount);
    }

    public static FluidIngredient of(FluidIngredient... ingredients) {
        return new FluidIngredient.Compound(ingredients);
    }

    public static FluidIngredient deserialize(JsonObject parent, String name) {
        return deserialize(parent.get(name), name);
    }

    public static FluidIngredient deserialize(JsonElement json, String name) {
        if (json.isJsonObject()) {
            return deserializeObject(json.getAsJsonObject());
        } else if (json.isJsonArray()) {
            return FluidIngredient.Compound.deserialize(json.getAsJsonArray(), name);
        } else {
            throw new JsonSyntaxException("Fluid ingredient " + name + " must be either an object or array");
        }
    }

    private static FluidIngredient deserializeObject(JsonObject json) {
        if (json.entrySet().isEmpty()) {
            return EMPTY;
        } else if (json.has("name")) {
            if (json.has("tag")) {
                throw new JsonSyntaxException("An ingredient entry is either a tag or an fluid, not both");
            } else {
                return FluidIngredient.FluidMatch.deserialize(json);
            }
        } else if (json.has("tag")) {
            return FluidIngredient.TagMatch.deserialize(json);
        } else {
            throw new JsonSyntaxException("An ingredient entry needs either a tag or an fluid");
        }
    }

    public static FluidIngredient read(FriendlyByteBuf buffer) {
        int count = buffer.readInt();
        FluidIngredient[] ingredients = new FluidIngredient[count];
        for (int i = 0; i < count; i++) {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(buffer.readResourceLocation());
            if (fluid == null) {
                fluid = Fluids.EMPTY;
            }
            int amount = buffer.readVarInt();
            ingredients[i] = of(fluid, amount);
        }
        return count == 1 ? ingredients[0] : of(ingredients);
    }

    private static class Compound extends FluidIngredient {

        private final List<FluidIngredient> ingredients;

        private Compound(FluidIngredient[] ingredients) {
            this.ingredients = Arrays.asList(ingredients);
        }

        @Override
        public boolean test(Fluid fluid) {
            return this.ingredients.stream().anyMatch(ingredient -> ingredient.test(fluid));
        }

        @Override
        public boolean test(FluidStack stack) {
            return this.ingredients.stream().anyMatch(ingredient -> ingredient.test(stack));
        }

        @Override
        public int getAmount(Fluid fluid) {
            return this.ingredients.stream().filter(ingredient -> ingredient.test(fluid)).mapToInt(ingredient -> ingredient.getAmount(fluid)).findFirst().orElse(0);
        }

        @Override
        public List<FluidStack> getAllFluids() {
            return (List<FluidStack>) this.ingredients.stream().flatMap(ingredient -> ingredient.getFluids().stream()).collect(Collectors.toList());
        }

        @Override
        public JsonElement serialize() {
            return (JsonElement) this.ingredients.stream().map(FluidIngredient::serialize).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
        }

        private static FluidIngredient.Compound deserialize(JsonArray array, String name) {
            int size = array.size();
            if (size == 0) {
                throw new JsonSyntaxException("Fluid array cannot be empty, at least one fluid must be defined");
            } else {
                FluidIngredient[] ingredients = new FluidIngredient[size];
                for (int i = 0; i < size; i++) {
                    ingredients[i] = FluidIngredient.deserializeObject(GsonHelper.convertToJsonObject(array.get(i), name + "[" + i + "]"));
                }
                return new FluidIngredient.Compound(ingredients);
            }
        }
    }

    private static class Empty extends FluidIngredient {

        public Empty() {
        }

        @Override
        public boolean test(Fluid fluid) {
            return fluid == Fluids.EMPTY;
        }

        @Override
        public boolean test(FluidStack fluid) {
            return fluid.isEmpty();
        }

        @Override
        public int getAmount(Fluid fluid) {
            return 0;
        }

        @Override
        public List<FluidStack> getAllFluids() {
            return Collections.emptyList();
        }

        @Override
        public JsonElement serialize() {
            return new JsonObject();
        }
    }

    private static class FluidMatch extends FluidIngredient {

        private final Fluid fluid;

        private final int amount;

        public FluidMatch(Fluid fluid, int amount) {
            this.fluid = fluid;
            this.amount = amount;
        }

        @Override
        public boolean test(Fluid fluid) {
            return fluid == this.fluid;
        }

        @Override
        public int getAmount(Fluid fluid) {
            return this.amount;
        }

        @Override
        public List<FluidStack> getAllFluids() {
            return Collections.singletonList(new FluidStack(this.fluid, this.amount));
        }

        @Override
        public JsonElement serialize() {
            JsonObject object = new JsonObject();
            object.addProperty("name", ((ResourceLocation) Objects.requireNonNull(((ResourceKey) ForgeRegistries.FLUIDS.getResourceKey(this.fluid).get()).location())).toString());
            object.addProperty("amount", this.amount);
            return object;
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            buffer.writeInt(1);
            buffer.writeResourceLocation(((ResourceKey) ForgeRegistries.FLUIDS.getResourceKey(this.fluid).get()).location());
            buffer.writeVarInt(this.amount);
        }

        private static FluidIngredient.FluidMatch deserialize(JsonObject json) {
            String fluidName = GsonHelper.getAsString(json, "name");
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));
            if (fluid != null && fluid != Fluids.EMPTY) {
                int amount = GsonHelper.getAsInt(json, "amount");
                return new FluidIngredient.FluidMatch(fluid, amount);
            } else {
                throw new JsonSyntaxException("Unknown fluid '" + fluidName + "'");
            }
        }
    }

    private static class TagMatch extends FluidIngredient {

        private final TagKey<Fluid> tag;

        private final int amount;

        public TagMatch(TagKey<Fluid> tag, int amount) {
            this.tag = tag;
            this.amount = amount;
        }

        @Override
        public boolean test(Fluid fluid) {
            return fluid.is(this.tag);
        }

        @Override
        public int getAmount(Fluid fluid) {
            return this.amount;
        }

        @Override
        public List<FluidStack> getAllFluids() {
            return StreamSupport.stream(BuiltInRegistries.FLUID.m_206058_(this.tag).spliterator(), false).filter(Holder::m_203633_).map(fluid -> new FluidStack((Fluid) fluid.value(), this.amount)).toList();
        }

        @Override
        public JsonElement serialize() {
            JsonObject object = new JsonObject();
            object.addProperty("tag", this.tag.location().toString());
            object.addProperty("amount", this.amount);
            return object;
        }

        private static FluidIngredient.TagMatch deserialize(JsonObject json) {
            TagKey<Fluid> tag = TagKey.create(Registries.FLUID, ResourceLocation.tryParse(GsonHelper.getAsString(json, "tag")));
            int amount = GsonHelper.getAsInt(json, "amount");
            return new FluidIngredient.TagMatch(tag, amount);
        }
    }
}