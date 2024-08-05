package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeUnlockedTrigger extends SimpleCriterionTrigger<RecipeUnlockedTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("recipe_unlocked");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public RecipeUnlockedTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "recipe"));
        return new RecipeUnlockedTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, Recipe<?> recipe1) {
        this.m_66234_(serverPlayer0, p_63723_ -> p_63723_.matches(recipe1));
    }

    public static RecipeUnlockedTrigger.TriggerInstance unlocked(ResourceLocation resourceLocation0) {
        return new RecipeUnlockedTrigger.TriggerInstance(ContextAwarePredicate.ANY, resourceLocation0);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ResourceLocation recipe;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ResourceLocation resourceLocation1) {
            super(RecipeUnlockedTrigger.ID, contextAwarePredicate0);
            this.recipe = resourceLocation1;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.addProperty("recipe", this.recipe.toString());
            return $$1;
        }

        public boolean matches(Recipe<?> recipe0) {
            return this.recipe.equals(recipe0.getId());
        }
    }
}