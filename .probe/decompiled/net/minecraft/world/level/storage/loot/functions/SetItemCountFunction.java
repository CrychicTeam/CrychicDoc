package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class SetItemCountFunction extends LootItemConditionalFunction {

    final NumberProvider value;

    final boolean add;

    SetItemCountFunction(LootItemCondition[] lootItemCondition0, NumberProvider numberProvider1, boolean boolean2) {
        super(lootItemCondition0);
        this.value = numberProvider1;
        this.add = boolean2;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_COUNT;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.value.m_6231_();
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        int $$2 = this.add ? itemStack0.getCount() : 0;
        itemStack0.setCount(Mth.clamp($$2 + this.value.getInt(lootContext1), 0, itemStack0.getMaxStackSize()));
        return itemStack0;
    }

    public static LootItemConditionalFunction.Builder<?> setCount(NumberProvider numberProvider0) {
        return m_80683_(p_165423_ -> new SetItemCountFunction(p_165423_, numberProvider0, false));
    }

    public static LootItemConditionalFunction.Builder<?> setCount(NumberProvider numberProvider0, boolean boolean1) {
        return m_80683_(p_165420_ -> new SetItemCountFunction(p_165420_, numberProvider0, boolean1));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetItemCountFunction> {

        public void serialize(JsonObject jsonObject0, SetItemCountFunction setItemCountFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setItemCountFunction1, jsonSerializationContext2);
            jsonObject0.add("count", jsonSerializationContext2.serialize(setItemCountFunction1.value));
            jsonObject0.addProperty("add", setItemCountFunction1.add);
        }

        public SetItemCountFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            NumberProvider $$3 = GsonHelper.getAsObject(jsonObject0, "count", jsonDeserializationContext1, NumberProvider.class);
            boolean $$4 = GsonHelper.getAsBoolean(jsonObject0, "add", false);
            return new SetItemCountFunction(lootItemCondition2, $$3, $$4);
        }
    }
}