package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.function.Consumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootItem extends LootPoolSingletonContainer {

    final Item item;

    LootItem(Item item0, int int1, int int2, LootItemCondition[] lootItemCondition3, LootItemFunction[] lootItemFunction4) {
        super(int1, int2, lootItemCondition3, lootItemFunction4);
        this.item = item0;
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.ITEM;
    }

    @Override
    public void createItemStack(Consumer<ItemStack> consumerItemStack0, LootContext lootContext1) {
        consumerItemStack0.accept(new ItemStack(this.item));
    }

    public static LootPoolSingletonContainer.Builder<?> lootTableItem(ItemLike itemLike0) {
        return m_79687_((p_79583_, p_79584_, p_79585_, p_79586_) -> new LootItem(itemLike0.asItem(), p_79583_, p_79584_, p_79585_, p_79586_));
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<LootItem> {

        public void serializeCustom(JsonObject jsonObject0, LootItem lootItem1, JsonSerializationContext jsonSerializationContext2) {
            super.serializeCustom(jsonObject0, lootItem1, jsonSerializationContext2);
            ResourceLocation $$3 = BuiltInRegistries.ITEM.getKey(lootItem1.item);
            if ($$3 == null) {
                throw new IllegalArgumentException("Can't serialize unknown item " + lootItem1.item);
            } else {
                jsonObject0.addProperty("name", $$3.toString());
            }
        }

        protected LootItem deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, int int2, int int3, LootItemCondition[] lootItemCondition4, LootItemFunction[] lootItemFunction5) {
            Item $$6 = GsonHelper.getAsItem(jsonObject0, "name");
            return new LootItem($$6, int2, int3, lootItemCondition4, lootItemFunction5);
        }
    }
}