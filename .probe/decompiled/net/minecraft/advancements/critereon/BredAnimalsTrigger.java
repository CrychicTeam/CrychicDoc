package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

public class BredAnimalsTrigger extends SimpleCriterionTrigger<BredAnimalsTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("bred_animals");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public BredAnimalsTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ContextAwarePredicate $$3 = EntityPredicate.fromJson(jsonObject0, "parent", deserializationContext2);
        ContextAwarePredicate $$4 = EntityPredicate.fromJson(jsonObject0, "partner", deserializationContext2);
        ContextAwarePredicate $$5 = EntityPredicate.fromJson(jsonObject0, "child", deserializationContext2);
        return new BredAnimalsTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4, $$5);
    }

    public void trigger(ServerPlayer serverPlayer0, Animal animal1, Animal animal2, @Nullable AgeableMob ageableMob3) {
        LootContext $$4 = EntityPredicate.createContext(serverPlayer0, animal1);
        LootContext $$5 = EntityPredicate.createContext(serverPlayer0, animal2);
        LootContext $$6 = ageableMob3 != null ? EntityPredicate.createContext(serverPlayer0, ageableMob3) : null;
        this.m_66234_(serverPlayer0, p_18653_ -> p_18653_.matches($$4, $$5, $$6));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate parent;

        private final ContextAwarePredicate partner;

        private final ContextAwarePredicate child;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ContextAwarePredicate contextAwarePredicate1, ContextAwarePredicate contextAwarePredicate2, ContextAwarePredicate contextAwarePredicate3) {
            super(BredAnimalsTrigger.ID, contextAwarePredicate0);
            this.parent = contextAwarePredicate1;
            this.partner = contextAwarePredicate2;
            this.child = contextAwarePredicate3;
        }

        public static BredAnimalsTrigger.TriggerInstance bredAnimals() {
            return new BredAnimalsTrigger.TriggerInstance(ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, ContextAwarePredicate.ANY);
        }

        public static BredAnimalsTrigger.TriggerInstance bredAnimals(EntityPredicate.Builder entityPredicateBuilder0) {
            return new BredAnimalsTrigger.TriggerInstance(ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicateBuilder0.build()));
        }

        public static BredAnimalsTrigger.TriggerInstance bredAnimals(EntityPredicate entityPredicate0, EntityPredicate entityPredicate1, EntityPredicate entityPredicate2) {
            return new BredAnimalsTrigger.TriggerInstance(ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicate0), EntityPredicate.wrap(entityPredicate1), EntityPredicate.wrap(entityPredicate2));
        }

        public boolean matches(LootContext lootContext0, LootContext lootContext1, @Nullable LootContext lootContext2) {
            return this.child == ContextAwarePredicate.ANY || lootContext2 != null && this.child.matches(lootContext2) ? this.parent.matches(lootContext0) && this.partner.matches(lootContext1) || this.parent.matches(lootContext1) && this.partner.matches(lootContext0) : false;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("parent", this.parent.toJson(serializationContext0));
            $$1.add("partner", this.partner.toJson(serializationContext0));
            $$1.add("child", this.child.toJson(serializationContext0));
            return $$1;
        }
    }
}