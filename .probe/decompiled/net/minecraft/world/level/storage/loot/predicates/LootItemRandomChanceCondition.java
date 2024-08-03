package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;

public class LootItemRandomChanceCondition implements LootItemCondition {

    final float probability;

    LootItemRandomChanceCondition(float float0) {
        this.probability = float0;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.RANDOM_CHANCE;
    }

    public boolean test(LootContext lootContext0) {
        return lootContext0.getRandom().nextFloat() < this.probability;
    }

    public static LootItemCondition.Builder randomChance(float float0) {
        return () -> new LootItemRandomChanceCondition(float0);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootItemRandomChanceCondition> {

        public void serialize(JsonObject jsonObject0, LootItemRandomChanceCondition lootItemRandomChanceCondition1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("chance", lootItemRandomChanceCondition1.probability);
        }

        public LootItemRandomChanceCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            return new LootItemRandomChanceCondition(GsonHelper.getAsFloat(jsonObject0, "chance"));
        }
    }
}