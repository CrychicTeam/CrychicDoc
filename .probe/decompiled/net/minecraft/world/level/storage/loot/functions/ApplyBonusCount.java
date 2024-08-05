package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ApplyBonusCount extends LootItemConditionalFunction {

    static final Map<ResourceLocation, ApplyBonusCount.FormulaDeserializer> FORMULAS = Maps.newHashMap();

    final Enchantment enchantment;

    final ApplyBonusCount.Formula formula;

    ApplyBonusCount(LootItemCondition[] lootItemCondition0, Enchantment enchantment1, ApplyBonusCount.Formula applyBonusCountFormula2) {
        super(lootItemCondition0);
        this.enchantment = enchantment1;
        this.formula = applyBonusCountFormula2;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.APPLY_BONUS;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.TOOL);
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        ItemStack $$2 = lootContext1.getParamOrNull(LootContextParams.TOOL);
        if ($$2 != null) {
            int $$3 = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, $$2);
            int $$4 = this.formula.calculateNewCount(lootContext1.getRandom(), itemStack0.getCount(), $$3);
            itemStack0.setCount($$4);
        }
        return itemStack0;
    }

    public static LootItemConditionalFunction.Builder<?> addBonusBinomialDistributionCount(Enchantment enchantment0, float float1, int int2) {
        return m_80683_(p_79928_ -> new ApplyBonusCount(p_79928_, enchantment0, new ApplyBonusCount.BinomialWithBonusCount(int2, float1)));
    }

    public static LootItemConditionalFunction.Builder<?> addOreBonusCount(Enchantment enchantment0) {
        return m_80683_(p_79943_ -> new ApplyBonusCount(p_79943_, enchantment0, new ApplyBonusCount.OreDrops()));
    }

    public static LootItemConditionalFunction.Builder<?> addUniformBonusCount(Enchantment enchantment0) {
        return m_80683_(p_79935_ -> new ApplyBonusCount(p_79935_, enchantment0, new ApplyBonusCount.UniformBonusCount(1)));
    }

    public static LootItemConditionalFunction.Builder<?> addUniformBonusCount(Enchantment enchantment0, int int1) {
        return m_80683_(p_79932_ -> new ApplyBonusCount(p_79932_, enchantment0, new ApplyBonusCount.UniformBonusCount(int1)));
    }

    static {
        FORMULAS.put(ApplyBonusCount.BinomialWithBonusCount.TYPE, ApplyBonusCount.BinomialWithBonusCount::m_79955_);
        FORMULAS.put(ApplyBonusCount.OreDrops.TYPE, ApplyBonusCount.OreDrops::m_79979_);
        FORMULAS.put(ApplyBonusCount.UniformBonusCount.TYPE, ApplyBonusCount.UniformBonusCount::m_80018_);
    }

    static final class BinomialWithBonusCount implements ApplyBonusCount.Formula {

        public static final ResourceLocation TYPE = new ResourceLocation("binomial_with_bonus_count");

        private final int extraRounds;

        private final float probability;

        public BinomialWithBonusCount(int int0, float float1) {
            this.extraRounds = int0;
            this.probability = float1;
        }

        @Override
        public int calculateNewCount(RandomSource randomSource0, int int1, int int2) {
            for (int $$3 = 0; $$3 < int2 + this.extraRounds; $$3++) {
                if (randomSource0.nextFloat() < this.probability) {
                    int1++;
                }
            }
            return int1;
        }

        @Override
        public void serializeParams(JsonObject jsonObject0, JsonSerializationContext jsonSerializationContext1) {
            jsonObject0.addProperty("extra", this.extraRounds);
            jsonObject0.addProperty("probability", this.probability);
        }

        public static ApplyBonusCount.Formula deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            int $$2 = GsonHelper.getAsInt(jsonObject0, "extra");
            float $$3 = GsonHelper.getAsFloat(jsonObject0, "probability");
            return new ApplyBonusCount.BinomialWithBonusCount($$2, $$3);
        }

        @Override
        public ResourceLocation getType() {
            return TYPE;
        }
    }

    interface Formula {

        int calculateNewCount(RandomSource var1, int var2, int var3);

        void serializeParams(JsonObject var1, JsonSerializationContext var2);

        ResourceLocation getType();
    }

    interface FormulaDeserializer {

        ApplyBonusCount.Formula deserialize(JsonObject var1, JsonDeserializationContext var2);
    }

    static final class OreDrops implements ApplyBonusCount.Formula {

        public static final ResourceLocation TYPE = new ResourceLocation("ore_drops");

        @Override
        public int calculateNewCount(RandomSource randomSource0, int int1, int int2) {
            if (int2 > 0) {
                int $$3 = randomSource0.nextInt(int2 + 2) - 1;
                if ($$3 < 0) {
                    $$3 = 0;
                }
                return int1 * ($$3 + 1);
            } else {
                return int1;
            }
        }

        @Override
        public void serializeParams(JsonObject jsonObject0, JsonSerializationContext jsonSerializationContext1) {
        }

        public static ApplyBonusCount.Formula deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            return new ApplyBonusCount.OreDrops();
        }

        @Override
        public ResourceLocation getType() {
            return TYPE;
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<ApplyBonusCount> {

        public void serialize(JsonObject jsonObject0, ApplyBonusCount applyBonusCount1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, applyBonusCount1, jsonSerializationContext2);
            jsonObject0.addProperty("enchantment", BuiltInRegistries.ENCHANTMENT.getKey(applyBonusCount1.enchantment).toString());
            jsonObject0.addProperty("formula", applyBonusCount1.formula.getType().toString());
            JsonObject $$3 = new JsonObject();
            applyBonusCount1.formula.serializeParams($$3, jsonSerializationContext2);
            if ($$3.size() > 0) {
                jsonObject0.add("parameters", $$3);
            }
        }

        public ApplyBonusCount deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "enchantment"));
            Enchantment $$4 = (Enchantment) BuiltInRegistries.ENCHANTMENT.getOptional($$3).orElseThrow(() -> new JsonParseException("Invalid enchantment id: " + $$3));
            ResourceLocation $$5 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "formula"));
            ApplyBonusCount.FormulaDeserializer $$6 = (ApplyBonusCount.FormulaDeserializer) ApplyBonusCount.FORMULAS.get($$5);
            if ($$6 == null) {
                throw new JsonParseException("Invalid formula id: " + $$5);
            } else {
                ApplyBonusCount.Formula $$7;
                if (jsonObject0.has("parameters")) {
                    $$7 = $$6.deserialize(GsonHelper.getAsJsonObject(jsonObject0, "parameters"), jsonDeserializationContext1);
                } else {
                    $$7 = $$6.deserialize(new JsonObject(), jsonDeserializationContext1);
                }
                return new ApplyBonusCount(lootItemCondition2, $$4, $$7);
            }
        }
    }

    static final class UniformBonusCount implements ApplyBonusCount.Formula {

        public static final ResourceLocation TYPE = new ResourceLocation("uniform_bonus_count");

        private final int bonusMultiplier;

        public UniformBonusCount(int int0) {
            this.bonusMultiplier = int0;
        }

        @Override
        public int calculateNewCount(RandomSource randomSource0, int int1, int int2) {
            return int1 + randomSource0.nextInt(this.bonusMultiplier * int2 + 1);
        }

        @Override
        public void serializeParams(JsonObject jsonObject0, JsonSerializationContext jsonSerializationContext1) {
            jsonObject0.addProperty("bonusMultiplier", this.bonusMultiplier);
        }

        public static ApplyBonusCount.Formula deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            int $$2 = GsonHelper.getAsInt(jsonObject0, "bonusMultiplier");
            return new ApplyBonusCount.UniformBonusCount($$2);
        }

        @Override
        public ResourceLocation getType() {
            return TYPE;
        }
    }
}