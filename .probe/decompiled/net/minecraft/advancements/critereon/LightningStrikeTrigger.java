package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.storage.loot.LootContext;

public class LightningStrikeTrigger extends SimpleCriterionTrigger<LightningStrikeTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("lightning_strike");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public LightningStrikeTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ContextAwarePredicate $$3 = EntityPredicate.fromJson(jsonObject0, "lightning", deserializationContext2);
        ContextAwarePredicate $$4 = EntityPredicate.fromJson(jsonObject0, "bystander", deserializationContext2);
        return new LightningStrikeTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, LightningBolt lightningBolt1, List<Entity> listEntity2) {
        List<LootContext> $$3 = (List<LootContext>) listEntity2.stream().map(p_153390_ -> EntityPredicate.createContext(serverPlayer0, p_153390_)).collect(Collectors.toList());
        LootContext $$4 = EntityPredicate.createContext(serverPlayer0, lightningBolt1);
        this.m_66234_(serverPlayer0, p_153402_ -> p_153402_.matches($$4, $$3));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate lightning;

        private final ContextAwarePredicate bystander;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ContextAwarePredicate contextAwarePredicate1, ContextAwarePredicate contextAwarePredicate2) {
            super(LightningStrikeTrigger.ID, contextAwarePredicate0);
            this.lightning = contextAwarePredicate1;
            this.bystander = contextAwarePredicate2;
        }

        public static LightningStrikeTrigger.TriggerInstance lighthingStrike(EntityPredicate entityPredicate0, EntityPredicate entityPredicate1) {
            return new LightningStrikeTrigger.TriggerInstance(ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicate0), EntityPredicate.wrap(entityPredicate1));
        }

        public boolean matches(LootContext lootContext0, List<LootContext> listLootContext1) {
            return !this.lightning.matches(lootContext0) ? false : this.bystander == ContextAwarePredicate.ANY || !listLootContext1.stream().noneMatch(this.bystander::m_285831_);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("lightning", this.lightning.toJson(serializationContext0));
            $$1.add("bystander", this.bystander.toJson(serializationContext0));
            return $$1;
        }
    }
}