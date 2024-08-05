package net.minecraft.advancements.critereon;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ConsumeItemTrigger extends SimpleCriterionTrigger<ConsumeItemTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("consume_item");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public ConsumeItemTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        return new ConsumeItemTrigger.TriggerInstance(contextAwarePredicate1, ItemPredicate.fromJson(jsonObject0.get("item")));
    }

    public void trigger(ServerPlayer serverPlayer0, ItemStack itemStack1) {
        this.m_66234_(serverPlayer0, p_23687_ -> p_23687_.matches(itemStack1));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1) {
            super(ConsumeItemTrigger.ID, contextAwarePredicate0);
            this.item = itemPredicate1;
        }

        public static ConsumeItemTrigger.TriggerInstance usedItem() {
            return new ConsumeItemTrigger.TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.ANY);
        }

        public static ConsumeItemTrigger.TriggerInstance usedItem(ItemPredicate itemPredicate0) {
            return new ConsumeItemTrigger.TriggerInstance(ContextAwarePredicate.ANY, itemPredicate0);
        }

        public static ConsumeItemTrigger.TriggerInstance usedItem(ItemLike itemLike0) {
            return new ConsumeItemTrigger.TriggerInstance(ContextAwarePredicate.ANY, new ItemPredicate(null, ImmutableSet.of(itemLike0.asItem()), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY));
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