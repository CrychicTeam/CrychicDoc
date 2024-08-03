package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.logging.LogUtils;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.slf4j.Logger;

public class SetItemDamageFunction extends LootItemConditionalFunction {

    private static final Logger LOGGER = LogUtils.getLogger();

    final NumberProvider damage;

    final boolean add;

    SetItemDamageFunction(LootItemCondition[] lootItemCondition0, NumberProvider numberProvider1, boolean boolean2) {
        super(lootItemCondition0);
        this.damage = numberProvider1;
        this.add = boolean2;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_DAMAGE;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.damage.m_6231_();
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        if (itemStack0.isDamageableItem()) {
            int $$2 = itemStack0.getMaxDamage();
            float $$3 = this.add ? 1.0F - (float) itemStack0.getDamageValue() / (float) $$2 : 0.0F;
            float $$4 = 1.0F - Mth.clamp(this.damage.getFloat(lootContext1) + $$3, 0.0F, 1.0F);
            itemStack0.setDamageValue(Mth.floor($$4 * (float) $$2));
        } else {
            LOGGER.warn("Couldn't set damage of loot item {}", itemStack0);
        }
        return itemStack0;
    }

    public static LootItemConditionalFunction.Builder<?> setDamage(NumberProvider numberProvider0) {
        return m_80683_(p_165441_ -> new SetItemDamageFunction(p_165441_, numberProvider0, false));
    }

    public static LootItemConditionalFunction.Builder<?> setDamage(NumberProvider numberProvider0, boolean boolean1) {
        return m_80683_(p_165438_ -> new SetItemDamageFunction(p_165438_, numberProvider0, boolean1));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetItemDamageFunction> {

        public void serialize(JsonObject jsonObject0, SetItemDamageFunction setItemDamageFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setItemDamageFunction1, jsonSerializationContext2);
            jsonObject0.add("damage", jsonSerializationContext2.serialize(setItemDamageFunction1.damage));
            jsonObject0.addProperty("add", setItemDamageFunction1.add);
        }

        public SetItemDamageFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            NumberProvider $$3 = GsonHelper.getAsObject(jsonObject0, "damage", jsonDeserializationContext1, NumberProvider.class);
            boolean $$4 = GsonHelper.getAsBoolean(jsonObject0, "add", false);
            return new SetItemDamageFunction(lootItemCondition2, $$3, $$4);
        }
    }
}