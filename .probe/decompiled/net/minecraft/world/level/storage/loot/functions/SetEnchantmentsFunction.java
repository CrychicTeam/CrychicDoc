package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class SetEnchantmentsFunction extends LootItemConditionalFunction {

    final Map<Enchantment, NumberProvider> enchantments;

    final boolean add;

    SetEnchantmentsFunction(LootItemCondition[] lootItemCondition0, Map<Enchantment, NumberProvider> mapEnchantmentNumberProvider1, boolean boolean2) {
        super(lootItemCondition0);
        this.enchantments = ImmutableMap.copyOf(mapEnchantmentNumberProvider1);
        this.add = boolean2;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_ENCHANTMENTS;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>) this.enchantments.values().stream().flatMap(p_279081_ -> p_279081_.m_6231_().stream()).collect(ImmutableSet.toImmutableSet());
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        Object2IntMap<Enchantment> $$2 = new Object2IntOpenHashMap();
        this.enchantments.forEach((p_165353_, p_165354_) -> $$2.put(p_165353_, p_165354_.getInt(lootContext1)));
        if (itemStack0.getItem() == Items.BOOK) {
            ItemStack $$3 = new ItemStack(Items.ENCHANTED_BOOK);
            $$2.forEach((p_165343_, p_165344_) -> EnchantedBookItem.addEnchantment($$3, new EnchantmentInstance(p_165343_, p_165344_)));
            return $$3;
        } else {
            Map<Enchantment, Integer> $$4 = EnchantmentHelper.getEnchantments(itemStack0);
            if (this.add) {
                $$2.forEach((p_165366_, p_165367_) -> updateEnchantment($$4, p_165366_, Math.max((Integer) $$4.getOrDefault(p_165366_, 0) + p_165367_, 0)));
            } else {
                $$2.forEach((p_165361_, p_165362_) -> updateEnchantment($$4, p_165361_, Math.max(p_165362_, 0)));
            }
            EnchantmentHelper.setEnchantments($$4, itemStack0);
            return itemStack0;
        }
    }

    private static void updateEnchantment(Map<Enchantment, Integer> mapEnchantmentInteger0, Enchantment enchantment1, int int2) {
        if (int2 == 0) {
            mapEnchantmentInteger0.remove(enchantment1);
        } else {
            mapEnchantmentInteger0.put(enchantment1, int2);
        }
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetEnchantmentsFunction.Builder> {

        private final Map<Enchantment, NumberProvider> enchantments = Maps.newHashMap();

        private final boolean add;

        public Builder() {
            this(false);
        }

        public Builder(boolean boolean0) {
            this.add = boolean0;
        }

        protected SetEnchantmentsFunction.Builder getThis() {
            return this;
        }

        public SetEnchantmentsFunction.Builder withEnchantment(Enchantment enchantment0, NumberProvider numberProvider1) {
            this.enchantments.put(enchantment0, numberProvider1);
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new SetEnchantmentsFunction(this.m_80699_(), this.enchantments, this.add);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetEnchantmentsFunction> {

        public void serialize(JsonObject jsonObject0, SetEnchantmentsFunction setEnchantmentsFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setEnchantmentsFunction1, jsonSerializationContext2);
            JsonObject $$3 = new JsonObject();
            setEnchantmentsFunction1.enchantments.forEach((p_259023_, p_259024_) -> {
                ResourceLocation $$4 = BuiltInRegistries.ENCHANTMENT.getKey(p_259023_);
                if ($$4 == null) {
                    throw new IllegalArgumentException("Don't know how to serialize enchantment " + p_259023_);
                } else {
                    $$3.add($$4.toString(), jsonSerializationContext2.serialize(p_259024_));
                }
            });
            jsonObject0.add("enchantments", $$3);
            jsonObject0.addProperty("add", setEnchantmentsFunction1.add);
        }

        public SetEnchantmentsFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            Map<Enchantment, NumberProvider> $$3 = Maps.newHashMap();
            if (jsonObject0.has("enchantments")) {
                JsonObject $$4 = GsonHelper.getAsJsonObject(jsonObject0, "enchantments");
                for (Entry<String, JsonElement> $$5 : $$4.entrySet()) {
                    String $$6 = (String) $$5.getKey();
                    JsonElement $$7 = (JsonElement) $$5.getValue();
                    Enchantment $$8 = (Enchantment) BuiltInRegistries.ENCHANTMENT.getOptional(new ResourceLocation($$6)).orElseThrow(() -> new JsonSyntaxException("Unknown enchantment '" + $$6 + "'"));
                    NumberProvider $$9 = (NumberProvider) jsonDeserializationContext1.deserialize($$7, NumberProvider.class);
                    $$3.put($$8, $$9);
                }
            }
            boolean $$10 = GsonHelper.getAsBoolean(jsonObject0, "add", false);
            return new SetEnchantmentsFunction(lootItemCondition2, $$3, $$10);
        }
    }
}