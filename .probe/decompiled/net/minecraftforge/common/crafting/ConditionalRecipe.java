package net.minecraftforge.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.Nullable;

public class ConditionalRecipe {

    @ObjectHolder(registryName = "recipe_serializer", value = "forge:conditional")
    public static final RecipeSerializer<Recipe<?>> SERIALZIER = null;

    public static ConditionalRecipe.Builder builder() {
        return new ConditionalRecipe.Builder();
    }

    public static class Builder {

        private List<ICondition[]> conditions = new ArrayList();

        private List<FinishedRecipe> recipes = new ArrayList();

        private ResourceLocation advId;

        private ConditionalAdvancement.Builder adv;

        private List<ICondition> currentConditions = new ArrayList();

        public ConditionalRecipe.Builder addCondition(ICondition condition) {
            this.currentConditions.add(condition);
            return this;
        }

        public ConditionalRecipe.Builder addRecipe(Consumer<Consumer<FinishedRecipe>> callable) {
            callable.accept(this::addRecipe);
            return this;
        }

        public ConditionalRecipe.Builder addRecipe(FinishedRecipe recipe) {
            if (this.currentConditions.isEmpty()) {
                throw new IllegalStateException("Can not add a recipe with no conditions.");
            } else {
                this.conditions.add((ICondition[]) this.currentConditions.toArray(new ICondition[this.currentConditions.size()]));
                this.recipes.add(recipe);
                this.currentConditions.clear();
                return this;
            }
        }

        public ConditionalRecipe.Builder generateAdvancement() {
            return this.generateAdvancement(null);
        }

        public ConditionalRecipe.Builder generateAdvancement(@Nullable ResourceLocation id) {
            ConditionalAdvancement.Builder builder = ConditionalAdvancement.builder();
            for (int i = 0; i < this.recipes.size(); i++) {
                for (ICondition cond : (ICondition[]) this.conditions.get(i)) {
                    builder = builder.addCondition(cond);
                }
                builder = builder.addAdvancement((FinishedRecipe) this.recipes.get(i));
            }
            return this.setAdvancement(id, builder);
        }

        public ConditionalRecipe.Builder setAdvancement(ConditionalAdvancement.Builder advancement) {
            return this.setAdvancement(null, advancement);
        }

        public ConditionalRecipe.Builder setAdvancement(String namespace, String path, ConditionalAdvancement.Builder advancement) {
            return this.setAdvancement(new ResourceLocation(namespace, path), advancement);
        }

        public ConditionalRecipe.Builder setAdvancement(@Nullable ResourceLocation id, ConditionalAdvancement.Builder advancement) {
            if (this.adv != null) {
                throw new IllegalStateException("Invalid ConditionalRecipeBuilder, Advancement already set");
            } else {
                this.advId = id;
                this.adv = advancement;
                return this;
            }
        }

        public void build(Consumer<FinishedRecipe> consumer, String namespace, String path) {
            this.build(consumer, new ResourceLocation(namespace, path));
        }

        public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
            if (!this.currentConditions.isEmpty()) {
                throw new IllegalStateException("Invalid ConditionalRecipe builder, Orphaned conditions");
            } else if (this.recipes.isEmpty()) {
                throw new IllegalStateException("Invalid ConditionalRecipe builder, No recipes");
            } else {
                if (this.advId == null && this.adv != null) {
                    this.advId = new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath());
                }
                consumer.accept(new ConditionalRecipe.Finished(id, this.conditions, this.recipes, this.advId, this.adv));
            }
        }
    }

    private static class Finished implements FinishedRecipe {

        private final ResourceLocation id;

        private final List<ICondition[]> conditions;

        private final List<FinishedRecipe> recipes;

        private final ResourceLocation advId;

        private final ConditionalAdvancement.Builder adv;

        private Finished(ResourceLocation id, List<ICondition[]> conditions, List<FinishedRecipe> recipes, @Nullable ResourceLocation advId, @Nullable ConditionalAdvancement.Builder adv) {
            this.id = id;
            this.conditions = conditions;
            this.recipes = recipes;
            this.advId = advId;
            this.adv = adv;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray array = new JsonArray();
            json.add("recipes", array);
            for (int x = 0; x < this.conditions.size(); x++) {
                JsonObject holder = new JsonObject();
                JsonArray conds = new JsonArray();
                for (ICondition c : (ICondition[]) this.conditions.get(x)) {
                    conds.add(CraftingHelper.serialize(c));
                }
                holder.add("conditions", conds);
                holder.add("recipe", ((FinishedRecipe) this.recipes.get(x)).serializeRecipe());
                array.add(holder);
            }
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ConditionalRecipe.SERIALZIER;
        }

        @Override
        public JsonObject serializeAdvancement() {
            return this.adv == null ? null : this.adv.write();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return this.advId;
        }
    }

    public static class Serializer<T extends Recipe<?>> implements RecipeSerializer<T> {

        @Override
        public T fromJson(ResourceLocation recipeId, JsonObject json) {
            return this.fromJson(recipeId, json, ICondition.IContext.EMPTY);
        }

        public T fromJson(ResourceLocation recipeId, JsonObject json, ICondition.IContext context) {
            JsonArray items = GsonHelper.getAsJsonArray(json, "recipes");
            int idx = 0;
            for (JsonElement ele : items) {
                if (!ele.isJsonObject()) {
                    throw new JsonSyntaxException("Invalid recipes entry at index " + idx + " Must be JsonObject");
                }
                if (CraftingHelper.processConditions(GsonHelper.getAsJsonArray(ele.getAsJsonObject(), "conditions"), context)) {
                    return (T) RecipeManager.fromJson(recipeId, GsonHelper.getAsJsonObject(ele.getAsJsonObject(), "recipe"));
                }
                idx++;
            }
            return null;
        }

        @Override
        public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        }
    }
}