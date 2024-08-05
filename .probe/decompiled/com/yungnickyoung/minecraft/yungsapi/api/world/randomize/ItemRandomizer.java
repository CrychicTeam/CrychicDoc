package com.yungnickyoung.minecraft.yungsapi.api.world.randomize;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ItemRandomizer {

    public static final Codec<ItemRandomizer> CODEC = RecordCodecBuilder.create(instance -> instance.group(ItemRandomizer.Entry.CODEC.listOf().fieldOf("entries").forGetter(randomizer -> randomizer.entries), BuiltInRegistries.ITEM.m_194605_().fieldOf("defaultItem").forGetter(randomizer -> randomizer.defaultItem)).apply(instance, ItemRandomizer::new));

    private List<ItemRandomizer.Entry> entries = new ArrayList();

    private Item defaultItem = Items.AIR;

    public CompoundTag saveTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("defaultItemId", BuiltInRegistries.ITEM.m_7447_(this.defaultItem));
        ListTag entriesTag = Util.make(new ListTag(), tag -> this.entries.forEach(entry -> {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putInt("entryItemId", BuiltInRegistries.ITEM.m_7447_(entry.item));
            entryTag.putFloat("entryChance", entry.probability);
            tag.add(entryTag);
        }));
        compoundTag.put("entries", entriesTag);
        return compoundTag;
    }

    public ItemRandomizer(CompoundTag compoundTag) {
        this.defaultItem = BuiltInRegistries.ITEM.byId(compoundTag.getInt("defaultItemId"));
        this.entries = new ArrayList();
        ListTag entriesTag = compoundTag.getList("entries", 10);
        entriesTag.forEach(entryTag -> {
            CompoundTag entryCompoundTag = (CompoundTag) entryTag;
            Item item = BuiltInRegistries.ITEM.byId(entryCompoundTag.getInt("entryItemId"));
            float chance = entryCompoundTag.getFloat("entryChance");
            this.addItem(item, chance);
        });
    }

    public ItemRandomizer(List<ItemRandomizer.Entry> entries, Item defaultItem) {
        this.entries = entries;
        this.defaultItem = defaultItem;
    }

    public ItemRandomizer(Item defaultItem) {
        this.defaultItem = defaultItem;
    }

    public ItemRandomizer() {
    }

    public static ItemRandomizer from(Item... items) {
        ItemRandomizer randomizer = new ItemRandomizer();
        float chance = 1.0F / (float) items.length;
        for (Item item : items) {
            randomizer.addItem(item, chance);
        }
        return randomizer;
    }

    public ItemRandomizer addItem(Item item, float chance) {
        if (this.entries.stream().anyMatch(entry -> entry.item.equals(item))) {
            YungsApiCommon.LOGGER.warn("WARNING: duplicate item {} added to ItemRandomizer!", item.toString());
            return this;
        } else {
            float currTotal = (Float) this.entries.stream().map(entry -> entry.probability).reduce(Float::sum).orElse(0.0F);
            float newTotal = currTotal + chance;
            if (newTotal > 1.0F) {
                YungsApiCommon.LOGGER.warn("WARNING: item {} added to ItemRandomizer exceeds max probabiltiy of 1!", item.toString());
                return this;
            } else {
                this.entries.add(new ItemRandomizer.Entry(item, chance));
                return this;
            }
        }
    }

    public Item get(Random random) {
        float target = random.nextFloat();
        float currBottom = 0.0F;
        for (ItemRandomizer.Entry entry : this.entries) {
            if (currBottom <= target && target < currBottom + entry.probability) {
                return entry.item;
            }
            currBottom += entry.probability;
        }
        return this.defaultItem;
    }

    public Item get(RandomSource randomSource) {
        float target = randomSource.nextFloat();
        float currBottom = 0.0F;
        for (ItemRandomizer.Entry entry : this.entries) {
            if (currBottom <= target && target < currBottom + entry.probability) {
                return entry.item;
            }
            currBottom += entry.probability;
        }
        return this.defaultItem;
    }

    public void setDefaultItem(Item item) {
        this.defaultItem = item;
    }

    public Map<Item, Float> getEntriesAsMap() {
        Map<Item, Float> map = new HashMap();
        this.entries.forEach(entry -> map.put(entry.item, entry.probability));
        return map;
    }

    public List<ItemRandomizer.Entry> getEntries() {
        return this.entries;
    }

    public Item getDefaultItem() {
        return this.defaultItem;
    }

    public static class Entry {

        public static Codec<ItemRandomizer.Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.ITEM.m_194605_().fieldOf("item").forGetter(entry -> entry.item), Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(entry -> entry.probability)).apply(instance, ItemRandomizer.Entry::new));

        public Item item;

        public float probability;

        public Entry(Item item, float probability) {
            this.item = item;
            this.probability = probability;
        }

        public boolean equals(Object obj) {
            if (obj instanceof ItemRandomizer.Entry) {
                return this.item.equals(((ItemRandomizer.Entry) obj).item);
            } else {
                return obj instanceof Item ? this.item.equals(obj) : false;
            }
        }
    }
}