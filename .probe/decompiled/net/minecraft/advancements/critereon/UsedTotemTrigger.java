package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class UsedTotemTrigger extends SimpleCriterionTrigger<UsedTotemTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("used_totem");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public UsedTotemTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ItemPredicate $$3 = ItemPredicate.fromJson(jsonObject0.get("item"));
        return new UsedTotemTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, ItemStack itemStack1) {
        this.m_66234_(serverPlayer0, p_74436_ -> p_74436_.matches(itemStack1));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1) {
            super(UsedTotemTrigger.ID, contextAwarePredicate0);
            this.item = itemPredicate1;
        }

        public static UsedTotemTrigger.TriggerInstance usedTotem(ItemPredicate itemPredicate0) {
            return new UsedTotemTrigger.TriggerInstance(ContextAwarePredicate.ANY, itemPredicate0);
        }

        public static UsedTotemTrigger.TriggerInstance usedTotem(ItemLike itemLike0) {
            return new UsedTotemTrigger.TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(itemLike0).build());
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