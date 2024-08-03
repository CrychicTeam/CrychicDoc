package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class LootItemRandomChanceWithLootingCondition implements LootItemCondition {

    final float percent;

    final float lootingMultiplier;

    LootItemRandomChanceWithLootingCondition(float float0, float float1) {
        this.percent = float0;
        this.lootingMultiplier = float1;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.RANDOM_CHANCE_WITH_LOOTING;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.KILLER_ENTITY);
    }

    public boolean test(LootContext lootContext0) {
        Entity $$1 = lootContext0.getParamOrNull(LootContextParams.KILLER_ENTITY);
        int $$2 = 0;
        if ($$1 instanceof LivingEntity) {
            $$2 = EnchantmentHelper.getMobLooting((LivingEntity) $$1);
        }
        return lootContext0.getRandom().nextFloat() < this.percent + (float) $$2 * this.lootingMultiplier;
    }

    public static LootItemCondition.Builder randomChanceAndLootingBoost(float float0, float float1) {
        return () -> new LootItemRandomChanceWithLootingCondition(float0, float1);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootItemRandomChanceWithLootingCondition> {

        public void serialize(JsonObject jsonObject0, LootItemRandomChanceWithLootingCondition lootItemRandomChanceWithLootingCondition1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("chance", lootItemRandomChanceWithLootingCondition1.percent);
            jsonObject0.addProperty("looting_multiplier", lootItemRandomChanceWithLootingCondition1.lootingMultiplier);
        }

        public LootItemRandomChanceWithLootingCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            return new LootItemRandomChanceWithLootingCondition(GsonHelper.getAsFloat(jsonObject0, "chance"), GsonHelper.getAsFloat(jsonObject0, "looting_multiplier"));
        }
    }
}