package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemDurabilityTrigger extends SimpleCriterionTrigger<ItemDurabilityTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("item_durability_changed");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public ItemDurabilityTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ItemPredicate $$3 = ItemPredicate.fromJson(jsonObject0.get("item"));
        MinMaxBounds.Ints $$4 = MinMaxBounds.Ints.fromJson(jsonObject0.get("durability"));
        MinMaxBounds.Ints $$5 = MinMaxBounds.Ints.fromJson(jsonObject0.get("delta"));
        return new ItemDurabilityTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4, $$5);
    }

    public void trigger(ServerPlayer serverPlayer0, ItemStack itemStack1, int int2) {
        this.m_66234_(serverPlayer0, p_43676_ -> p_43676_.matches(itemStack1, int2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        private final MinMaxBounds.Ints durability;

        private final MinMaxBounds.Ints delta;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1, MinMaxBounds.Ints minMaxBoundsInts2, MinMaxBounds.Ints minMaxBoundsInts3) {
            super(ItemDurabilityTrigger.ID, contextAwarePredicate0);
            this.item = itemPredicate1;
            this.durability = minMaxBoundsInts2;
            this.delta = minMaxBoundsInts3;
        }

        public static ItemDurabilityTrigger.TriggerInstance changedDurability(ItemPredicate itemPredicate0, MinMaxBounds.Ints minMaxBoundsInts1) {
            return changedDurability(ContextAwarePredicate.ANY, itemPredicate0, minMaxBoundsInts1);
        }

        public static ItemDurabilityTrigger.TriggerInstance changedDurability(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1, MinMaxBounds.Ints minMaxBoundsInts2) {
            return new ItemDurabilityTrigger.TriggerInstance(contextAwarePredicate0, itemPredicate1, minMaxBoundsInts2, MinMaxBounds.Ints.ANY);
        }

        public boolean matches(ItemStack itemStack0, int int1) {
            if (!this.item.matches(itemStack0)) {
                return false;
            } else {
                return !this.durability.matches(itemStack0.getMaxDamage() - int1) ? false : this.delta.matches(itemStack0.getDamageValue() - int1);
            }
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("item", this.item.serializeToJson());
            $$1.add("durability", this.durability.m_55328_());
            $$1.add("delta", this.delta.m_55328_());
            return $$1;
        }
    }
}