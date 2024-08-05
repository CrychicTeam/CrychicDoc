package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class EmptyLootItem extends LootPoolSingletonContainer {

    EmptyLootItem(int int0, int int1, LootItemCondition[] lootItemCondition2, LootItemFunction[] lootItemFunction3) {
        super(int0, int1, lootItemCondition2, lootItemFunction3);
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.EMPTY;
    }

    @Override
    public void createItemStack(Consumer<ItemStack> consumerItemStack0, LootContext lootContext1) {
    }

    public static LootPoolSingletonContainer.Builder<?> emptyItem() {
        return m_79687_(EmptyLootItem::new);
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<EmptyLootItem> {

        public EmptyLootItem deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, int int2, int int3, LootItemCondition[] lootItemCondition4, LootItemFunction[] lootItemFunction5) {
            return new EmptyLootItem(int2, int3, lootItemCondition4, lootItemFunction5);
        }
    }
}