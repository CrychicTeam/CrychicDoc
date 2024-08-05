package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class TradeTrigger extends SimpleCriterionTrigger<TradeTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("villager_trade");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public TradeTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ContextAwarePredicate $$3 = EntityPredicate.fromJson(jsonObject0, "villager", deserializationContext2);
        ItemPredicate $$4 = ItemPredicate.fromJson(jsonObject0.get("item"));
        return new TradeTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, AbstractVillager abstractVillager1, ItemStack itemStack2) {
        LootContext $$3 = EntityPredicate.createContext(serverPlayer0, abstractVillager1);
        this.m_66234_(serverPlayer0, p_70970_ -> p_70970_.matches($$3, itemStack2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate villager;

        private final ItemPredicate item;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ContextAwarePredicate contextAwarePredicate1, ItemPredicate itemPredicate2) {
            super(TradeTrigger.ID, contextAwarePredicate0);
            this.villager = contextAwarePredicate1;
            this.item = itemPredicate2;
        }

        public static TradeTrigger.TriggerInstance tradedWithVillager() {
            return new TradeTrigger.TriggerInstance(ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, ItemPredicate.ANY);
        }

        public static TradeTrigger.TriggerInstance tradedWithVillager(EntityPredicate.Builder entityPredicateBuilder0) {
            return new TradeTrigger.TriggerInstance(EntityPredicate.wrap(entityPredicateBuilder0.build()), ContextAwarePredicate.ANY, ItemPredicate.ANY);
        }

        public boolean matches(LootContext lootContext0, ItemStack itemStack1) {
            return !this.villager.matches(lootContext0) ? false : this.item.matches(itemStack1);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("item", this.item.serializeToJson());
            $$1.add("villager", this.villager.toJson(serializationContext0));
            return $$1;
        }
    }
}