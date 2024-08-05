package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class LootTableTrigger extends SimpleCriterionTrigger<LootTableTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("player_generates_container_loot");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    protected LootTableTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "loot_table"));
        return new LootTableTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, ResourceLocation resourceLocation1) {
        this.m_66234_(serverPlayer0, p_54606_ -> p_54606_.matches(resourceLocation1));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ResourceLocation lootTable;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ResourceLocation resourceLocation1) {
            super(LootTableTrigger.ID, contextAwarePredicate0);
            this.lootTable = resourceLocation1;
        }

        public static LootTableTrigger.TriggerInstance lootTableUsed(ResourceLocation resourceLocation0) {
            return new LootTableTrigger.TriggerInstance(ContextAwarePredicate.ANY, resourceLocation0);
        }

        public boolean matches(ResourceLocation resourceLocation0) {
            return this.lootTable.equals(resourceLocation0);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.addProperty("loot_table", this.lootTable.toString());
            return $$1;
        }
    }
}