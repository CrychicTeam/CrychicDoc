package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class BonusLevelTableCondition implements LootItemCondition {

    final Enchantment enchantment;

    final float[] values;

    BonusLevelTableCondition(Enchantment enchantment0, float[] float1) {
        this.enchantment = enchantment0;
        this.values = float1;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.TABLE_BONUS;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.TOOL);
    }

    public boolean test(LootContext lootContext0) {
        ItemStack $$1 = lootContext0.getParamOrNull(LootContextParams.TOOL);
        int $$2 = $$1 != null ? EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, $$1) : 0;
        float $$3 = this.values[Math.min($$2, this.values.length - 1)];
        return lootContext0.getRandom().nextFloat() < $$3;
    }

    public static LootItemCondition.Builder bonusLevelFlatChance(Enchantment enchantment0, float... float1) {
        return () -> new BonusLevelTableCondition(enchantment0, float1);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<BonusLevelTableCondition> {

        public void serialize(JsonObject jsonObject0, BonusLevelTableCondition bonusLevelTableCondition1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("enchantment", BuiltInRegistries.ENCHANTMENT.getKey(bonusLevelTableCondition1.enchantment).toString());
            jsonObject0.add("chances", jsonSerializationContext2.serialize(bonusLevelTableCondition1.values));
        }

        public BonusLevelTableCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            ResourceLocation $$2 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "enchantment"));
            Enchantment $$3 = (Enchantment) BuiltInRegistries.ENCHANTMENT.getOptional($$2).orElseThrow(() -> new JsonParseException("Invalid enchantment id: " + $$2));
            float[] $$4 = GsonHelper.getAsObject(jsonObject0, "chances", jsonDeserializationContext1, float[].class);
            return new BonusLevelTableCondition($$3, $$4);
        }
    }
}