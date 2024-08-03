package com.sihenzhang.crockpot.capability;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.sihenzhang.crockpot.CrockPot;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class FoodCounter implements IFoodCounter {

    private final Multiset<Item> counter = HashMultiset.create();

    @Override
    public boolean hasEaten(Item food) {
        return this.counter.contains(food);
    }

    @Override
    public void addFood(Item food) {
        this.counter.add(food);
    }

    @Override
    public int getCount(Item food) {
        return this.counter.count(food);
    }

    @Override
    public void setCount(Item food, int count) {
        this.counter.setCount(food, count);
    }

    @Override
    public void clear() {
        this.counter.clear();
    }

    @Override
    public Map<Item, Integer> asMap() {
        return Maps.asMap(this.counter.elementSet(), this.counter::count);
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        this.asMap().forEach((food, count) -> {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(food);
            if (key != null) {
                CompoundTag foodCount = new CompoundTag();
                foodCount.putString("Food", key.toString());
                foodCount.putInt("Count", count);
                list.add(foodCount);
            }
        });
        tag.put("FoodCounter", list);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.clear();
        ListTag foodCounter = nbt.getList("FoodCounter", 10);
        foodCounter.stream().map(CompoundTag.class::cast).forEach(foodCount -> {
            String key = foodCount.getString("Food");
            Item food = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
            if (food == null) {
                CrockPot.LOGGER.warn("Attempt to load unregistered item: \"{}\", will remove this.", key);
            } else {
                if (!food.isEdible()) {
                    CrockPot.LOGGER.warn("Attempting to load item that is not edible: \"{}\", will not remove this in case it becomes edible again later.", key);
                }
                this.setCount(food, foodCount.getInt("Count"));
            }
        });
    }
}