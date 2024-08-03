package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.slf4j.Logger;

public class EnchantRandomlyFunction extends LootItemConditionalFunction {

    private static final Logger LOGGER = LogUtils.getLogger();

    final List<Enchantment> enchantments;

    EnchantRandomlyFunction(LootItemCondition[] lootItemCondition0, Collection<Enchantment> collectionEnchantment1) {
        super(lootItemCondition0);
        this.enchantments = ImmutableList.copyOf(collectionEnchantment1);
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.ENCHANT_RANDOMLY;
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        RandomSource $$2 = lootContext1.getRandom();
        Enchantment $$5;
        if (this.enchantments.isEmpty()) {
            boolean $$3 = itemStack0.is(Items.BOOK);
            List<Enchantment> $$4 = (List<Enchantment>) BuiltInRegistries.ENCHANTMENT.stream().filter(Enchantment::m_6592_).filter(p_80436_ -> $$3 || p_80436_.canEnchant(itemStack0)).collect(Collectors.toList());
            if ($$4.isEmpty()) {
                LOGGER.warn("Couldn't find a compatible enchantment for {}", itemStack0);
                return itemStack0;
            }
            $$5 = (Enchantment) $$4.get($$2.nextInt($$4.size()));
        } else {
            $$5 = (Enchantment) this.enchantments.get($$2.nextInt(this.enchantments.size()));
        }
        return enchantItem(itemStack0, $$5, $$2);
    }

    private static ItemStack enchantItem(ItemStack itemStack0, Enchantment enchantment1, RandomSource randomSource2) {
        int $$3 = Mth.nextInt(randomSource2, enchantment1.getMinLevel(), enchantment1.getMaxLevel());
        if (itemStack0.is(Items.BOOK)) {
            itemStack0 = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(itemStack0, new EnchantmentInstance(enchantment1, $$3));
        } else {
            itemStack0.enchant(enchantment1, $$3);
        }
        return itemStack0;
    }

    public static EnchantRandomlyFunction.Builder randomEnchantment() {
        return new EnchantRandomlyFunction.Builder();
    }

    public static LootItemConditionalFunction.Builder<?> randomApplicableEnchantment() {
        return m_80683_(p_80438_ -> new EnchantRandomlyFunction(p_80438_, ImmutableList.of()));
    }

    public static class Builder extends LootItemConditionalFunction.Builder<EnchantRandomlyFunction.Builder> {

        private final Set<Enchantment> enchantments = Sets.newHashSet();

        protected EnchantRandomlyFunction.Builder getThis() {
            return this;
        }

        public EnchantRandomlyFunction.Builder withEnchantment(Enchantment enchantment0) {
            this.enchantments.add(enchantment0);
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new EnchantRandomlyFunction(this.m_80699_(), this.enchantments);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<EnchantRandomlyFunction> {

        public void serialize(JsonObject jsonObject0, EnchantRandomlyFunction enchantRandomlyFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, enchantRandomlyFunction1, jsonSerializationContext2);
            if (!enchantRandomlyFunction1.enchantments.isEmpty()) {
                JsonArray $$3 = new JsonArray();
                for (Enchantment $$4 : enchantRandomlyFunction1.enchantments) {
                    ResourceLocation $$5 = BuiltInRegistries.ENCHANTMENT.getKey($$4);
                    if ($$5 == null) {
                        throw new IllegalArgumentException("Don't know how to serialize enchantment " + $$4);
                    }
                    $$3.add(new JsonPrimitive($$5.toString()));
                }
                jsonObject0.add("enchantments", $$3);
            }
        }

        public EnchantRandomlyFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            List<Enchantment> $$3 = Lists.newArrayList();
            if (jsonObject0.has("enchantments")) {
                for (JsonElement $$5 : GsonHelper.getAsJsonArray(jsonObject0, "enchantments")) {
                    String $$6 = GsonHelper.convertToString($$5, "enchantment");
                    Enchantment $$7 = (Enchantment) BuiltInRegistries.ENCHANTMENT.getOptional(new ResourceLocation($$6)).orElseThrow(() -> new JsonSyntaxException("Unknown enchantment '" + $$6 + "'"));
                    $$3.add($$7);
                }
            }
            return new EnchantRandomlyFunction(lootItemCondition2, $$3);
        }
    }
}