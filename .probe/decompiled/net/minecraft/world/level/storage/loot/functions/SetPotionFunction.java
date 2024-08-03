package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetPotionFunction extends LootItemConditionalFunction {

    final Potion potion;

    SetPotionFunction(LootItemCondition[] lootItemCondition0, Potion potion1) {
        super(lootItemCondition0);
        this.potion = potion1;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_POTION;
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        PotionUtils.setPotion(itemStack0, this.potion);
        return itemStack0;
    }

    public static LootItemConditionalFunction.Builder<?> setPotion(Potion potion0) {
        return m_80683_(p_193079_ -> new SetPotionFunction(p_193079_, potion0));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetPotionFunction> {

        public void serialize(JsonObject jsonObject0, SetPotionFunction setPotionFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setPotionFunction1, jsonSerializationContext2);
            jsonObject0.addProperty("id", BuiltInRegistries.POTION.getKey(setPotionFunction1.potion).toString());
        }

        public SetPotionFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            String $$3 = GsonHelper.getAsString(jsonObject0, "id");
            Potion $$4 = (Potion) BuiltInRegistries.POTION.m_6612_(ResourceLocation.tryParse($$3)).orElseThrow(() -> new JsonSyntaxException("Unknown potion '" + $$3 + "'"));
            return new SetPotionFunction(lootItemCondition2, $$4);
        }
    }
}