package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetInstrumentFunction extends LootItemConditionalFunction {

    final TagKey<Instrument> options;

    SetInstrumentFunction(LootItemCondition[] lootItemCondition0, TagKey<Instrument> tagKeyInstrument1) {
        super(lootItemCondition0);
        this.options = tagKeyInstrument1;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_INSTRUMENT;
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        InstrumentItem.setRandom(itemStack0, this.options, lootContext1.getRandom());
        return itemStack0;
    }

    public static LootItemConditionalFunction.Builder<?> setInstrumentOptions(TagKey<Instrument> tagKeyInstrument0) {
        return m_80683_(p_231015_ -> new SetInstrumentFunction(p_231015_, tagKeyInstrument0));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetInstrumentFunction> {

        public void serialize(JsonObject jsonObject0, SetInstrumentFunction setInstrumentFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setInstrumentFunction1, jsonSerializationContext2);
            jsonObject0.addProperty("options", "#" + setInstrumentFunction1.options.location());
        }

        public SetInstrumentFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            String $$3 = GsonHelper.getAsString(jsonObject0, "options");
            if (!$$3.startsWith("#")) {
                throw new JsonSyntaxException("Inline tag value not supported: " + $$3);
            } else {
                return new SetInstrumentFunction(lootItemCondition2, TagKey.create(Registries.INSTRUMENT, new ResourceLocation($$3.substring(1))));
            }
        }
    }
}