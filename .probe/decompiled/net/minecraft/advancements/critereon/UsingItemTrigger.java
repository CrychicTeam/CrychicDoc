package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class UsingItemTrigger extends SimpleCriterionTrigger<UsingItemTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("using_item");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public UsingItemTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ItemPredicate $$3 = ItemPredicate.fromJson(jsonObject0.get("item"));
        return new UsingItemTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, ItemStack itemStack1) {
        this.m_66234_(serverPlayer0, p_163870_ -> p_163870_.matches(itemStack1));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1) {
            super(UsingItemTrigger.ID, contextAwarePredicate0);
            this.item = itemPredicate1;
        }

        public static UsingItemTrigger.TriggerInstance lookingAt(EntityPredicate.Builder entityPredicateBuilder0, ItemPredicate.Builder itemPredicateBuilder1) {
            return new UsingItemTrigger.TriggerInstance(EntityPredicate.wrap(entityPredicateBuilder0.build()), itemPredicateBuilder1.build());
        }

        public boolean matches(ItemStack itemStack0) {
            return this.item.matches(itemStack0);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("item", this.item.serializeToJson());
            return $$1;
        }
    }
}