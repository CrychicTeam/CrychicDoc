package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class EnchantWithLevelsFunction extends LootItemConditionalFunction {

    final NumberProvider levels;

    final boolean treasure;

    EnchantWithLevelsFunction(LootItemCondition[] lootItemCondition0, NumberProvider numberProvider1, boolean boolean2) {
        super(lootItemCondition0);
        this.levels = numberProvider1;
        this.treasure = boolean2;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.ENCHANT_WITH_LEVELS;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.levels.m_6231_();
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        RandomSource $$2 = lootContext1.getRandom();
        return EnchantmentHelper.enchantItem($$2, itemStack0, this.levels.getInt(lootContext1), this.treasure);
    }

    public static EnchantWithLevelsFunction.Builder enchantWithLevels(NumberProvider numberProvider0) {
        return new EnchantWithLevelsFunction.Builder(numberProvider0);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<EnchantWithLevelsFunction.Builder> {

        private final NumberProvider levels;

        private boolean treasure;

        public Builder(NumberProvider numberProvider0) {
            this.levels = numberProvider0;
        }

        protected EnchantWithLevelsFunction.Builder getThis() {
            return this;
        }

        public EnchantWithLevelsFunction.Builder allowTreasure() {
            this.treasure = true;
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new EnchantWithLevelsFunction(this.m_80699_(), this.levels, this.treasure);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<EnchantWithLevelsFunction> {

        public void serialize(JsonObject jsonObject0, EnchantWithLevelsFunction enchantWithLevelsFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, enchantWithLevelsFunction1, jsonSerializationContext2);
            jsonObject0.add("levels", jsonSerializationContext2.serialize(enchantWithLevelsFunction1.levels));
            jsonObject0.addProperty("treasure", enchantWithLevelsFunction1.treasure);
        }

        public EnchantWithLevelsFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            NumberProvider $$3 = GsonHelper.getAsObject(jsonObject0, "levels", jsonDeserializationContext1, NumberProvider.class);
            boolean $$4 = GsonHelper.getAsBoolean(jsonObject0, "treasure", false);
            return new EnchantWithLevelsFunction(lootItemCondition2, $$3, $$4);
        }
    }
}