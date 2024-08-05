package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class DynamicLoot extends LootPoolSingletonContainer {

    final ResourceLocation name;

    DynamicLoot(ResourceLocation resourceLocation0, int int1, int int2, LootItemCondition[] lootItemCondition3, LootItemFunction[] lootItemFunction4) {
        super(int1, int2, lootItemCondition3, lootItemFunction4);
        this.name = resourceLocation0;
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.DYNAMIC;
    }

    @Override
    public void createItemStack(Consumer<ItemStack> consumerItemStack0, LootContext lootContext1) {
        lootContext1.addDynamicDrops(this.name, consumerItemStack0);
    }

    public static LootPoolSingletonContainer.Builder<?> dynamicEntry(ResourceLocation resourceLocation0) {
        return m_79687_((p_79487_, p_79488_, p_79489_, p_79490_) -> new DynamicLoot(resourceLocation0, p_79487_, p_79488_, p_79489_, p_79490_));
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<DynamicLoot> {

        public void serializeCustom(JsonObject jsonObject0, DynamicLoot dynamicLoot1, JsonSerializationContext jsonSerializationContext2) {
            super.serializeCustom(jsonObject0, dynamicLoot1, jsonSerializationContext2);
            jsonObject0.addProperty("name", dynamicLoot1.name.toString());
        }

        protected DynamicLoot deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, int int2, int int3, LootItemCondition[] lootItemCondition4, LootItemFunction[] lootItemFunction5) {
            ResourceLocation $$6 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "name"));
            return new DynamicLoot($$6, int2, int3, lootItemCondition4, lootItemFunction5);
        }
    }
}