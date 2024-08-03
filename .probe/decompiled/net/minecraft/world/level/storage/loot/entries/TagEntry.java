package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class TagEntry extends LootPoolSingletonContainer {

    final TagKey<Item> tag;

    final boolean expand;

    TagEntry(TagKey<Item> tagKeyItem0, boolean boolean1, int int2, int int3, LootItemCondition[] lootItemCondition4, LootItemFunction[] lootItemFunction5) {
        super(int2, int3, lootItemCondition4, lootItemFunction5);
        this.tag = tagKeyItem0;
        this.expand = boolean1;
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.TAG;
    }

    @Override
    public void createItemStack(Consumer<ItemStack> consumerItemStack0, LootContext lootContext1) {
        BuiltInRegistries.ITEM.m_206058_(this.tag).forEach(p_205094_ -> consumerItemStack0.accept(new ItemStack(p_205094_)));
    }

    private boolean expandTag(LootContext lootContext0, Consumer<LootPoolEntry> consumerLootPoolEntry1) {
        if (!this.m_79639_(lootContext0)) {
            return false;
        } else {
            for (final Holder<Item> $$2 : BuiltInRegistries.ITEM.m_206058_(this.tag)) {
                consumerLootPoolEntry1.accept(new LootPoolSingletonContainer.EntryBase() {

                    @Override
                    public void createItemStack(Consumer<ItemStack> p_79869_, LootContext p_79870_) {
                        p_79869_.accept(new ItemStack($$2));
                    }
                });
            }
            return true;
        }
    }

    @Override
    public boolean expand(LootContext lootContext0, Consumer<LootPoolEntry> consumerLootPoolEntry1) {
        return this.expand ? this.expandTag(lootContext0, consumerLootPoolEntry1) : super.expand(lootContext0, consumerLootPoolEntry1);
    }

    public static LootPoolSingletonContainer.Builder<?> tagContents(TagKey<Item> tagKeyItem0) {
        return m_79687_((p_205099_, p_205100_, p_205101_, p_205102_) -> new TagEntry(tagKeyItem0, false, p_205099_, p_205100_, p_205101_, p_205102_));
    }

    public static LootPoolSingletonContainer.Builder<?> expandTag(TagKey<Item> tagKeyItem0) {
        return m_79687_((p_205088_, p_205089_, p_205090_, p_205091_) -> new TagEntry(tagKeyItem0, true, p_205088_, p_205089_, p_205090_, p_205091_));
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<TagEntry> {

        public void serializeCustom(JsonObject jsonObject0, TagEntry tagEntry1, JsonSerializationContext jsonSerializationContext2) {
            super.serializeCustom(jsonObject0, tagEntry1, jsonSerializationContext2);
            jsonObject0.addProperty("name", tagEntry1.tag.location().toString());
            jsonObject0.addProperty("expand", tagEntry1.expand);
        }

        protected TagEntry deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, int int2, int int3, LootItemCondition[] lootItemCondition4, LootItemFunction[] lootItemFunction5) {
            ResourceLocation $$6 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "name"));
            TagKey<Item> $$7 = TagKey.create(Registries.ITEM, $$6);
            boolean $$8 = GsonHelper.getAsBoolean(jsonObject0, "expand");
            return new TagEntry($$7, $$8, int2, int3, lootItemCondition4, lootItemFunction5);
        }
    }
}