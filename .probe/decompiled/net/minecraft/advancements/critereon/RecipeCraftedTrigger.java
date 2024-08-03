package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class RecipeCraftedTrigger extends SimpleCriterionTrigger<RecipeCraftedTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("recipe_crafted");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    protected RecipeCraftedTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "recipe_id"));
        ItemPredicate[] $$4 = ItemPredicate.fromJsonArray(jsonObject0.get("ingredients"));
        return new RecipeCraftedTrigger.TriggerInstance(contextAwarePredicate1, $$3, List.of($$4));
    }

    public void trigger(ServerPlayer serverPlayer0, ResourceLocation resourceLocation1, List<ItemStack> listItemStack2) {
        this.m_66234_(serverPlayer0, p_282798_ -> p_282798_.matches(resourceLocation1, listItemStack2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ResourceLocation recipeId;

        private final List<ItemPredicate> predicates;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ResourceLocation resourceLocation1, List<ItemPredicate> listItemPredicate2) {
            super(RecipeCraftedTrigger.ID, contextAwarePredicate0);
            this.recipeId = resourceLocation1;
            this.predicates = listItemPredicate2;
        }

        public static RecipeCraftedTrigger.TriggerInstance craftedItem(ResourceLocation resourceLocation0, List<ItemPredicate> listItemPredicate1) {
            return new RecipeCraftedTrigger.TriggerInstance(ContextAwarePredicate.ANY, resourceLocation0, listItemPredicate1);
        }

        public static RecipeCraftedTrigger.TriggerInstance craftedItem(ResourceLocation resourceLocation0) {
            return new RecipeCraftedTrigger.TriggerInstance(ContextAwarePredicate.ANY, resourceLocation0, List.of());
        }

        boolean matches(ResourceLocation resourceLocation0, List<ItemStack> listItemStack1) {
            if (!resourceLocation0.equals(this.recipeId)) {
                return false;
            } else {
                List<ItemStack> $$2 = new ArrayList(listItemStack1);
                for (ItemPredicate $$3 : this.predicates) {
                    boolean $$4 = false;
                    Iterator<ItemStack> $$5 = $$2.iterator();
                    while ($$5.hasNext()) {
                        if ($$3.matches((ItemStack) $$5.next())) {
                            $$5.remove();
                            $$4 = true;
                            break;
                        }
                    }
                    if (!$$4) {
                        return false;
                    }
                }
                return true;
            }
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.addProperty("recipe_id", this.recipeId.toString());
            if (this.predicates.size() > 0) {
                JsonArray $$2 = new JsonArray();
                for (ItemPredicate $$3 : this.predicates) {
                    $$2.add($$3.serializeToJson());
                }
                $$1.add("ingredients", $$2);
            }
            return $$1;
        }
    }
}