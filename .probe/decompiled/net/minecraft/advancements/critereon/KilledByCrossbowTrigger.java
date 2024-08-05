package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;

public class KilledByCrossbowTrigger extends SimpleCriterionTrigger<KilledByCrossbowTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("killed_by_crossbow");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public KilledByCrossbowTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ContextAwarePredicate[] $$3 = EntityPredicate.fromJsonArray(jsonObject0, "victims", deserializationContext2);
        MinMaxBounds.Ints $$4 = MinMaxBounds.Ints.fromJson(jsonObject0.get("unique_entity_types"));
        return new KilledByCrossbowTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, Collection<Entity> collectionEntity1) {
        List<LootContext> $$2 = Lists.newArrayList();
        Set<EntityType<?>> $$3 = Sets.newHashSet();
        for (Entity $$4 : collectionEntity1) {
            $$3.add($$4.getType());
            $$2.add(EntityPredicate.createContext(serverPlayer0, $$4));
        }
        this.m_66234_(serverPlayer0, p_46881_ -> p_46881_.matches($$2, $$3.size()));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate[] victims;

        private final MinMaxBounds.Ints uniqueEntityTypes;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ContextAwarePredicate[] contextAwarePredicate1, MinMaxBounds.Ints minMaxBoundsInts2) {
            super(KilledByCrossbowTrigger.ID, contextAwarePredicate0);
            this.victims = contextAwarePredicate1;
            this.uniqueEntityTypes = minMaxBoundsInts2;
        }

        public static KilledByCrossbowTrigger.TriggerInstance crossbowKilled(EntityPredicate.Builder... entityPredicateBuilder0) {
            ContextAwarePredicate[] $$1 = new ContextAwarePredicate[entityPredicateBuilder0.length];
            for (int $$2 = 0; $$2 < entityPredicateBuilder0.length; $$2++) {
                EntityPredicate.Builder $$3 = entityPredicateBuilder0[$$2];
                $$1[$$2] = EntityPredicate.wrap($$3.build());
            }
            return new KilledByCrossbowTrigger.TriggerInstance(ContextAwarePredicate.ANY, $$1, MinMaxBounds.Ints.ANY);
        }

        public static KilledByCrossbowTrigger.TriggerInstance crossbowKilled(MinMaxBounds.Ints minMaxBoundsInts0) {
            ContextAwarePredicate[] $$1 = new ContextAwarePredicate[0];
            return new KilledByCrossbowTrigger.TriggerInstance(ContextAwarePredicate.ANY, $$1, minMaxBoundsInts0);
        }

        public boolean matches(Collection<LootContext> collectionLootContext0, int int1) {
            if (this.victims.length > 0) {
                List<LootContext> $$2 = Lists.newArrayList(collectionLootContext0);
                for (ContextAwarePredicate $$3 : this.victims) {
                    boolean $$4 = false;
                    Iterator<LootContext> $$5 = $$2.iterator();
                    while ($$5.hasNext()) {
                        LootContext $$6 = (LootContext) $$5.next();
                        if ($$3.matches($$6)) {
                            $$5.remove();
                            $$4 = true;
                            break;
                        }
                    }
                    if (!$$4) {
                        return false;
                    }
                }
            }
            return this.uniqueEntityTypes.matches(int1);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("victims", ContextAwarePredicate.toJson(this.victims, serializationContext0));
            $$1.add("unique_entity_types", this.uniqueEntityTypes.m_55328_());
            return $$1;
        }
    }
}