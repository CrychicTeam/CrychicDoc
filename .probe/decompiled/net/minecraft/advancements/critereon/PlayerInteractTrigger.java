package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class PlayerInteractTrigger extends SimpleCriterionTrigger<PlayerInteractTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("player_interacted_with_entity");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    protected PlayerInteractTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ItemPredicate $$3 = ItemPredicate.fromJson(jsonObject0.get("item"));
        ContextAwarePredicate $$4 = EntityPredicate.fromJson(jsonObject0, "entity", deserializationContext2);
        return new PlayerInteractTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, ItemStack itemStack1, Entity entity2) {
        LootContext $$3 = EntityPredicate.createContext(serverPlayer0, entity2);
        this.m_66234_(serverPlayer0, p_61501_ -> p_61501_.matches(itemStack1, $$3));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        private final ContextAwarePredicate entity;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1, ContextAwarePredicate contextAwarePredicate2) {
            super(PlayerInteractTrigger.ID, contextAwarePredicate0);
            this.item = itemPredicate1;
            this.entity = contextAwarePredicate2;
        }

        public static PlayerInteractTrigger.TriggerInstance itemUsedOnEntity(ContextAwarePredicate contextAwarePredicate0, ItemPredicate.Builder itemPredicateBuilder1, ContextAwarePredicate contextAwarePredicate2) {
            return new PlayerInteractTrigger.TriggerInstance(contextAwarePredicate0, itemPredicateBuilder1.build(), contextAwarePredicate2);
        }

        public static PlayerInteractTrigger.TriggerInstance itemUsedOnEntity(ItemPredicate.Builder itemPredicateBuilder0, ContextAwarePredicate contextAwarePredicate1) {
            return itemUsedOnEntity(ContextAwarePredicate.ANY, itemPredicateBuilder0, contextAwarePredicate1);
        }

        public boolean matches(ItemStack itemStack0, LootContext lootContext1) {
            return !this.item.matches(itemStack0) ? false : this.entity.matches(lootContext1);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("item", this.item.serializeToJson());
            $$1.add("entity", this.entity.toJson(serializationContext0));
            return $$1;
        }
    }
}