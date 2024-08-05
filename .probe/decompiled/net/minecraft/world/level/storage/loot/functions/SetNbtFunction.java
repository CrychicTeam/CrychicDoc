package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetNbtFunction extends LootItemConditionalFunction {

    final CompoundTag tag;

    SetNbtFunction(LootItemCondition[] lootItemCondition0, CompoundTag compoundTag1) {
        super(lootItemCondition0);
        this.tag = compoundTag1;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_NBT;
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        itemStack0.getOrCreateTag().merge(this.tag);
        return itemStack0;
    }

    @Deprecated
    public static LootItemConditionalFunction.Builder<?> setTag(CompoundTag compoundTag0) {
        return m_80683_(p_81191_ -> new SetNbtFunction(p_81191_, compoundTag0));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetNbtFunction> {

        public void serialize(JsonObject jsonObject0, SetNbtFunction setNbtFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setNbtFunction1, jsonSerializationContext2);
            jsonObject0.addProperty("tag", setNbtFunction1.tag.toString());
        }

        public SetNbtFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            try {
                CompoundTag $$3 = TagParser.parseTag(GsonHelper.getAsString(jsonObject0, "tag"));
                return new SetNbtFunction(lootItemCondition2, $$3);
            } catch (CommandSyntaxException var5) {
                throw new JsonSyntaxException(var5.getMessage());
            }
        }
    }
}