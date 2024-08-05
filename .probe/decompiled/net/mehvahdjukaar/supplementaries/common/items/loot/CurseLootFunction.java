package net.mehvahdjukaar.supplementaries.common.items.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class CurseLootFunction extends LootItemConditionalFunction {

    private static final List<Enchantment> CURSES = new ArrayList();

    final double chance;

    public static void setup() {
        for (Enchantment e : BuiltInRegistries.ENCHANTMENT) {
            if (e.isCurse()) {
                CURSES.add(e);
            }
        }
    }

    CurseLootFunction(LootItemCondition[] pConditions, double chance) {
        super(pConditions);
        this.chance = chance;
    }

    @Override
    public LootItemFunctionType getType() {
        return (LootItemFunctionType) ModRegistry.CURSE_LOOT_FUNCTION.get();
    }

    @Override
    public ItemStack run(ItemStack pStack, LootContext pContext) {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(pStack);
        RandomSource random = pContext.getRandom();
        if ((double) random.nextFloat() < this.chance && CURSES.stream().noneMatch(map::containsKey) && !CURSES.isEmpty()) {
            Enchantment e = (Enchantment) CURSES.get(random.nextInt(CURSES.size()));
            map.put(e, 1);
        }
        EnchantmentHelper.setEnchantments(map, pStack);
        return pStack;
    }

    public static class Builder extends LootItemConditionalFunction.Builder<CurseLootFunction.Builder> {

        private final double chance;

        public Builder() {
            this(1.0);
        }

        public Builder(double chance) {
            this.chance = chance;
        }

        protected CurseLootFunction.Builder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new CurseLootFunction(this.m_80699_(), this.chance);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CurseLootFunction> {

        public void serialize(JsonObject jsonObject, CurseLootFunction function, JsonSerializationContext context) {
            super.serialize(jsonObject, function, context);
            jsonObject.addProperty("chance", function.chance);
        }

        public CurseLootFunction deserialize(JsonObject pObject, JsonDeserializationContext context, LootItemCondition[] pConditions) {
            double chance = GsonHelper.getAsDouble(pObject, "chance", 1.0);
            return new CurseLootFunction(pConditions, chance);
        }
    }
}