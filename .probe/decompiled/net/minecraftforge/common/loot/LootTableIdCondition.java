package net.minecraftforge.common.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootTableIdCondition implements LootItemCondition {

    public static final LootItemConditionType LOOT_TABLE_ID = new LootItemConditionType(new LootTableIdCondition.Serializer());

    public static final ResourceLocation UNKNOWN_LOOT_TABLE = new ResourceLocation("forge", "unknown_loot_table");

    private final ResourceLocation targetLootTableId;

    private LootTableIdCondition(ResourceLocation targetLootTableId) {
        this.targetLootTableId = targetLootTableId;
    }

    @Override
    public LootItemConditionType getType() {
        return LOOT_TABLE_ID;
    }

    public boolean test(LootContext lootContext) {
        return lootContext.getQueriedLootTableId().equals(this.targetLootTableId);
    }

    public static LootTableIdCondition.Builder builder(ResourceLocation targetLootTableId) {
        return new LootTableIdCondition.Builder(targetLootTableId);
    }

    public static class Builder implements LootItemCondition.Builder {

        private final ResourceLocation targetLootTableId;

        public Builder(ResourceLocation targetLootTableId) {
            if (targetLootTableId == null) {
                throw new IllegalArgumentException("Target loot table must not be null");
            } else {
                this.targetLootTableId = targetLootTableId;
            }
        }

        @Override
        public LootItemCondition build() {
            return new LootTableIdCondition(this.targetLootTableId);
        }
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootTableIdCondition> {

        public void serialize(JsonObject object, LootTableIdCondition instance, JsonSerializationContext ctx) {
            object.addProperty("loot_table_id", instance.targetLootTableId.toString());
        }

        public LootTableIdCondition deserialize(JsonObject object, JsonDeserializationContext ctx) {
            return new LootTableIdCondition(new ResourceLocation(GsonHelper.getAsString(object, "loot_table_id")));
        }
    }
}