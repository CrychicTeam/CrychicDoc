package dev.latvian.mods.kubejs.create;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.BlockTagIngredient;
import dev.latvian.mods.kubejs.create.platform.FluidIngredientHelper;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.item.ingredient.TagContext;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentWithParent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.component.TimeComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public interface ProcessingRecipeSchema {

    RecipeKey<Either<OutputFluid, OutputItem>[]> RESULTS = FluidComponents.OUTPUT_OR_ITEM_ARRAY.key("results");

    RecipeKey<Either<InputFluid, InputItem>[]> INGREDIENTS = FluidComponents.INPUT_OR_ITEM_ARRAY.key("ingredients");

    RecipeKey<Either<InputFluid, InputItem>[]> INGREDIENTS_UNWRAPPED = (new RecipeComponentWithParent<Either<InputFluid, InputItem>[]>() {

        @Override
        public RecipeComponent<Either<InputFluid, InputItem>[]> parentComponent() {
            return ProcessingRecipeSchema.INGREDIENTS.component;
        }

        public JsonElement write(RecipeJS recipe, Either<InputFluid, InputItem>[] value) {
            JsonArray json = new JsonArray();
            for (Either<InputFluid, InputItem> either : value) {
                either.ifLeft(fluid -> json.add(FluidComponents.INPUT.write(recipe, fluid))).ifRight(item -> {
                    for (InputItem unwrapped : item.unwrap()) {
                        json.add(ItemComponents.INPUT.write(recipe, unwrapped));
                    }
                });
            }
            return json;
        }
    }).key("ingredients");

    RecipeKey<Long> PROCESSING_TIME = TimeComponent.TICKS.key("processingTime").optional(100L);

    RecipeKey<Long> PROCESSING_TIME_REQUIRED = TimeComponent.TICKS.key("processingTime").optional(100L).alwaysWrite();

    RecipeKey<String> HEAT_REQUIREMENT = new StringComponent("not a valid heat condition!", s -> {
        for (HeatCondition h : HeatCondition.values()) {
            if (h.name().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }).key("heatRequirement").defaultOptional().allowEmpty();

    RecipeKey<Boolean> KEEP_HELD_ITEM = BooleanComponent.BOOLEAN.key("keepHeldItem").optional(false);

    RecipeSchema PROCESSING_DEFAULT = new RecipeSchema(ProcessingRecipeSchema.ProcessingRecipeJS.class, ProcessingRecipeSchema.ProcessingRecipeJS::new, RESULTS, INGREDIENTS, PROCESSING_TIME, HEAT_REQUIREMENT);

    RecipeSchema PROCESSING_WITH_TIME = new RecipeSchema(ProcessingRecipeSchema.ProcessingRecipeJS.class, ProcessingRecipeSchema.ProcessingRecipeJS::new, RESULTS, INGREDIENTS, PROCESSING_TIME_REQUIRED, HEAT_REQUIREMENT);

    RecipeSchema PROCESSING_UNWRAPPED = new RecipeSchema(ProcessingRecipeSchema.ProcessingRecipeJS.class, ProcessingRecipeSchema.ProcessingRecipeJS::new, RESULTS, INGREDIENTS_UNWRAPPED, PROCESSING_TIME, HEAT_REQUIREMENT);

    RecipeSchema ITEM_APPLICATION = new RecipeSchema(ProcessingRecipeSchema.ItemApplicationRecipeJS.class, ProcessingRecipeSchema.ItemApplicationRecipeJS::new, RESULTS, INGREDIENTS, PROCESSING_TIME, HEAT_REQUIREMENT, KEEP_HELD_ITEM);

    public static class ItemApplicationRecipeJS extends ProcessingRecipeSchema.ProcessingRecipeJS {

        public RecipeJS keepHeldItem() {
            return this.setValue(ProcessingRecipeSchema.KEEP_HELD_ITEM, Boolean.valueOf(true));
        }
    }

    public static class ProcessingRecipeJS extends RecipeJS {

        @Override
        public InputFluid readInputFluid(Object from) {
            if (from instanceof CreateInputFluid) {
                return (CreateInputFluid) from;
            } else if (from instanceof FluidIngredient fluid) {
                return new CreateInputFluid(fluid);
            } else if (from instanceof FluidStackJS fluid) {
                return new CreateInputFluid(FluidIngredientHelper.toFluidIngredient(fluid));
            } else if (from instanceof FluidStack fluid) {
                return new CreateInputFluid(FluidIngredient.fromFluidStack(fluid));
            } else {
                JsonObject json = MapJS.json(from);
                return json != null ? new CreateInputFluid(FluidIngredient.deserialize(json)) : CreateInputFluid.EMPTY;
            }
        }

        @Override
        public JsonElement writeInputFluid(InputFluid value) {
            if (value instanceof CreateInputFluid fluid) {
                return fluid.ingredient().serialize();
            } else if (value instanceof FluidIngredient fluid) {
                return fluid.serialize();
            } else {
                return value instanceof FluidStackJS fluid ? FluidIngredientHelper.toFluidIngredient(fluid).serialize() : FluidIngredient.EMPTY.serialize();
            }
        }

        @Override
        public boolean inputItemHasPriority(Object from) {
            if (!(from instanceof InputItem) && !(from instanceof Ingredient) && !(from instanceof ItemStack)) {
                InputItem input = this.readInputItem(from);
                return input.ingredient instanceof BlockTagIngredient blockTag ? !((TagContext) TagContext.INSTANCE.getValue()).isEmpty(blockTag.getTag()) : !input.isEmpty();
            } else {
                return true;
            }
        }

        @Override
        public boolean inputFluidHasPriority(Object from) {
            return from instanceof InputFluid || FluidIngredient.isFluidIngredient(MapJS.json(from));
        }

        @Override
        public OutputItem readOutputItem(Object from) {
            if (from instanceof ProcessingOutput output) {
                return OutputItem.of(output.getStack(), (double) output.getChance());
            } else {
                OutputItem outputItem = super.readOutputItem(from);
                if (from instanceof JsonObject j && j.has("chance")) {
                    return outputItem.withChance((double) j.get("chance").getAsFloat());
                }
                return outputItem;
            }
        }

        public RecipeJS heated() {
            return this.setValue(ProcessingRecipeSchema.HEAT_REQUIREMENT, HeatCondition.HEATED.serialize());
        }

        public RecipeJS superheated() {
            return this.setValue(ProcessingRecipeSchema.HEAT_REQUIREMENT, HeatCondition.SUPERHEATED.serialize());
        }
    }
}