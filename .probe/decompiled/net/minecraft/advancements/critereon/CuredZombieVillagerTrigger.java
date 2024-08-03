package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.storage.loot.LootContext;

public class CuredZombieVillagerTrigger extends SimpleCriterionTrigger<CuredZombieVillagerTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("cured_zombie_villager");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public CuredZombieVillagerTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ContextAwarePredicate $$3 = EntityPredicate.fromJson(jsonObject0, "zombie", deserializationContext2);
        ContextAwarePredicate $$4 = EntityPredicate.fromJson(jsonObject0, "villager", deserializationContext2);
        return new CuredZombieVillagerTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, Zombie zombie1, Villager villager2) {
        LootContext $$3 = EntityPredicate.createContext(serverPlayer0, zombie1);
        LootContext $$4 = EntityPredicate.createContext(serverPlayer0, villager2);
        this.m_66234_(serverPlayer0, p_24285_ -> p_24285_.matches($$3, $$4));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate zombie;

        private final ContextAwarePredicate villager;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ContextAwarePredicate contextAwarePredicate1, ContextAwarePredicate contextAwarePredicate2) {
            super(CuredZombieVillagerTrigger.ID, contextAwarePredicate0);
            this.zombie = contextAwarePredicate1;
            this.villager = contextAwarePredicate2;
        }

        public static CuredZombieVillagerTrigger.TriggerInstance curedZombieVillager() {
            return new CuredZombieVillagerTrigger.TriggerInstance(ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, ContextAwarePredicate.ANY);
        }

        public boolean matches(LootContext lootContext0, LootContext lootContext1) {
            return !this.zombie.matches(lootContext0) ? false : this.villager.matches(lootContext1);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("zombie", this.zombie.toJson(serializationContext0));
            $$1.add("villager", this.villager.toJson(serializationContext0));
            return $$1;
        }
    }
}