package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ShotCrossbowTrigger extends SimpleCriterionTrigger<ShotCrossbowTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("shot_crossbow");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public ShotCrossbowTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ItemPredicate $$3 = ItemPredicate.fromJson(jsonObject0.get("item"));
        return new ShotCrossbowTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, ItemStack itemStack1) {
        this.m_66234_(serverPlayer0, p_65467_ -> p_65467_.matches(itemStack1));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1) {
            super(ShotCrossbowTrigger.ID, contextAwarePredicate0);
            this.item = itemPredicate1;
        }

        public static ShotCrossbowTrigger.TriggerInstance shotCrossbow(ItemPredicate itemPredicate0) {
            return new ShotCrossbowTrigger.TriggerInstance(ContextAwarePredicate.ANY, itemPredicate0);
        }

        public static ShotCrossbowTrigger.TriggerInstance shotCrossbow(ItemLike itemLike0) {
            return new ShotCrossbowTrigger.TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(itemLike0).build());
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