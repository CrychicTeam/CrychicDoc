package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class PickedUpItemTrigger extends SimpleCriterionTrigger<PickedUpItemTrigger.TriggerInstance> {

    private final ResourceLocation id;

    public PickedUpItemTrigger(ResourceLocation resourceLocation0) {
        this.id = resourceLocation0;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    protected PickedUpItemTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ItemPredicate $$3 = ItemPredicate.fromJson(jsonObject0.get("item"));
        ContextAwarePredicate $$4 = EntityPredicate.fromJson(jsonObject0, "entity", deserializationContext2);
        return new PickedUpItemTrigger.TriggerInstance(this.id, contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, ItemStack itemStack1, @Nullable Entity entity2) {
        LootContext $$3 = EntityPredicate.createContext(serverPlayer0, entity2);
        this.m_66234_(serverPlayer0, p_221306_ -> p_221306_.matches(serverPlayer0, itemStack1, $$3));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        private final ContextAwarePredicate entity;

        public TriggerInstance(ResourceLocation resourceLocation0, ContextAwarePredicate contextAwarePredicate1, ItemPredicate itemPredicate2, ContextAwarePredicate contextAwarePredicate3) {
            super(resourceLocation0, contextAwarePredicate1);
            this.item = itemPredicate2;
            this.entity = contextAwarePredicate3;
        }

        public static PickedUpItemTrigger.TriggerInstance thrownItemPickedUpByEntity(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1, ContextAwarePredicate contextAwarePredicate2) {
            return new PickedUpItemTrigger.TriggerInstance(CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_ENTITY.getId(), contextAwarePredicate0, itemPredicate1, contextAwarePredicate2);
        }

        public static PickedUpItemTrigger.TriggerInstance thrownItemPickedUpByPlayer(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1, ContextAwarePredicate contextAwarePredicate2) {
            return new PickedUpItemTrigger.TriggerInstance(CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_PLAYER.getId(), contextAwarePredicate0, itemPredicate1, contextAwarePredicate2);
        }

        public boolean matches(ServerPlayer serverPlayer0, ItemStack itemStack1, LootContext lootContext2) {
            return !this.item.matches(itemStack1) ? false : this.entity.matches(lootContext2);
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