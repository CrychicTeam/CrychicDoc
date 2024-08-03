package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ApplyExplosionDecay extends LootItemConditionalFunction {

    ApplyExplosionDecay(LootItemCondition[] lootItemCondition0) {
        super(lootItemCondition0);
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.EXPLOSION_DECAY;
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        Float $$2 = lootContext1.getParamOrNull(LootContextParams.EXPLOSION_RADIUS);
        if ($$2 != null) {
            RandomSource $$3 = lootContext1.getRandom();
            float $$4 = 1.0F / $$2;
            int $$5 = itemStack0.getCount();
            int $$6 = 0;
            for (int $$7 = 0; $$7 < $$5; $$7++) {
                if ($$3.nextFloat() <= $$4) {
                    $$6++;
                }
            }
            itemStack0.setCount($$6);
        }
        return itemStack0;
    }

    public static LootItemConditionalFunction.Builder<?> explosionDecay() {
        return m_80683_(ApplyExplosionDecay::new);
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<ApplyExplosionDecay> {

        public ApplyExplosionDecay deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            return new ApplyExplosionDecay(lootItemCondition2);
        }
    }
}