package com.rekindled.embers.util;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class ConsumerWrapperBuilder {

    private final List<ICondition> conditions = new ArrayList();

    @Nullable
    private final RecipeSerializer<?> override;

    @Nullable
    private final ResourceLocation overrideName;

    private ConsumerWrapperBuilder(@Nullable RecipeSerializer<?> override, @Nullable ResourceLocation overrideName) {
        this.override = override;
        this.overrideName = overrideName;
    }

    public static ConsumerWrapperBuilder wrap() {
        return new ConsumerWrapperBuilder(null, null);
    }

    public static ConsumerWrapperBuilder wrap(RecipeSerializer<?> override) {
        return new ConsumerWrapperBuilder(override, null);
    }

    public static ConsumerWrapperBuilder wrap(ResourceLocation override) {
        return new ConsumerWrapperBuilder(null, override);
    }

    public ConsumerWrapperBuilder addCondition(ICondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public Consumer<FinishedRecipe> build(Consumer<FinishedRecipe> consumer) {
        return recipe -> consumer.accept(new ConsumerWrapperBuilder.Wrapped(recipe, this.conditions, this.override, this.overrideName));
    }

    private static class Wrapped implements FinishedRecipe {

        private final FinishedRecipe original;

        private final List<ICondition> conditions;

        @Nullable
        private final RecipeSerializer<?> override;

        @Nullable
        private final ResourceLocation overrideName;

        private Wrapped(FinishedRecipe original, List<ICondition> conditions, @Nullable RecipeSerializer<?> override, @Nullable ResourceLocation overrideName) {
            if (original instanceof ConsumerWrapperBuilder.Wrapped toMerge) {
                this.original = toMerge.original;
                this.conditions = ImmutableList.builder().addAll(toMerge.conditions).addAll(conditions).build();
                if (toMerge.override == null && toMerge.overrideName == null) {
                    this.override = override;
                    this.overrideName = overrideName;
                } else {
                    this.override = toMerge.override;
                    this.overrideName = toMerge.overrideName;
                }
            } else {
                this.original = original;
                this.conditions = conditions;
                this.override = override;
                this.overrideName = overrideName;
            }
        }

        @Override
        public JsonObject serializeRecipe() {
            JsonObject json = new JsonObject();
            if (this.overrideName != null) {
                json.addProperty("type", this.overrideName.toString());
            } else {
                json.addProperty("type", ((ResourceLocation) Objects.requireNonNull(BuiltInRegistries.RECIPE_SERIALIZER.getKey(this.getType()))).toString());
            }
            this.serializeRecipeData(json);
            return json;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.conditions.isEmpty()) {
                JsonArray conditionsArray = new JsonArray();
                for (ICondition condition : this.conditions) {
                    conditionsArray.add(CraftingHelper.serialize(condition));
                }
                json.add("conditions", conditionsArray);
            }
            this.original.serializeRecipeData(json);
        }

        @Override
        public ResourceLocation getId() {
            return this.original.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.override != null ? this.override : this.original.getType();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.original.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.original.getAdvancementId();
        }
    }
}