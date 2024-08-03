package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class ChanneledLightningTrigger extends SimpleCriterionTrigger<ChanneledLightningTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("channeled_lightning");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public ChanneledLightningTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ContextAwarePredicate[] $$3 = EntityPredicate.fromJsonArray(jsonObject0, "victims", deserializationContext2);
        return new ChanneledLightningTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, Collection<? extends Entity> collectionExtendsEntity1) {
        List<LootContext> $$2 = (List<LootContext>) collectionExtendsEntity1.stream().map(p_21720_ -> EntityPredicate.createContext(serverPlayer0, p_21720_)).collect(Collectors.toList());
        this.m_66234_(serverPlayer0, p_21730_ -> p_21730_.matches($$2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate[] victims;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ContextAwarePredicate[] contextAwarePredicate1) {
            super(ChanneledLightningTrigger.ID, contextAwarePredicate0);
            this.victims = contextAwarePredicate1;
        }

        public static ChanneledLightningTrigger.TriggerInstance channeledLightning(EntityPredicate... entityPredicate0) {
            return new ChanneledLightningTrigger.TriggerInstance(ContextAwarePredicate.ANY, (ContextAwarePredicate[]) Stream.of(entityPredicate0).map(EntityPredicate::m_285787_).toArray(ContextAwarePredicate[]::new));
        }

        public boolean matches(Collection<? extends LootContext> collectionExtendsLootContext0) {
            for (ContextAwarePredicate $$1 : this.victims) {
                boolean $$2 = false;
                for (LootContext $$3 : collectionExtendsLootContext0) {
                    if ($$1.matches($$3)) {
                        $$2 = true;
                        break;
                    }
                }
                if (!$$2) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("victims", ContextAwarePredicate.toJson(this.victims, serializationContext0));
            return $$1;
        }
    }
}