package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class EnchantedItemTrigger extends SimpleCriterionTrigger<EnchantedItemTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("enchanted_item");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public EnchantedItemTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ItemPredicate $$3 = ItemPredicate.fromJson(jsonObject0.get("item"));
        MinMaxBounds.Ints $$4 = MinMaxBounds.Ints.fromJson(jsonObject0.get("levels"));
        return new EnchantedItemTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, ItemStack itemStack1, int int2) {
        this.m_66234_(serverPlayer0, p_27675_ -> p_27675_.matches(itemStack1, int2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        private final MinMaxBounds.Ints levels;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1, MinMaxBounds.Ints minMaxBoundsInts2) {
            super(EnchantedItemTrigger.ID, contextAwarePredicate0);
            this.item = itemPredicate1;
            this.levels = minMaxBoundsInts2;
        }

        public static EnchantedItemTrigger.TriggerInstance enchantedItem() {
            return new EnchantedItemTrigger.TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.ANY, MinMaxBounds.Ints.ANY);
        }

        public boolean matches(ItemStack itemStack0, int int1) {
            return !this.item.matches(itemStack0) ? false : this.levels.matches(int1);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("item", this.item.serializeToJson());
            $$1.add("levels", this.levels.m_55328_());
            return $$1;
        }
    }
}