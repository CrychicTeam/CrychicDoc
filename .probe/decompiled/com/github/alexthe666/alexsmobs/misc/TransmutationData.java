package com.github.alexthe666.alexsmobs.misc;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap.Entry;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class TransmutationData {

    private final Object2DoubleMap<ItemStack> itemstackData = new Object2DoubleOpenHashMap();

    public void onTransmuteItem(ItemStack beingTransmuted, ItemStack turnedInto) {
        double fromWeight = this.getWeight(beingTransmuted);
        double toWeight = this.getWeight(turnedInto);
        this.putWeight(beingTransmuted, fromWeight + calculateAddWeight(beingTransmuted.getCount()));
        this.putWeight(turnedInto, toWeight + calculateRemoveWeight(turnedInto.getCount()));
    }

    public double getWeight(ItemStack stack) {
        ObjectIterator var2 = this.itemstackData.object2DoubleEntrySet().iterator();
        while (var2.hasNext()) {
            Entry<ItemStack> entry = (Entry<ItemStack>) var2.next();
            if (ItemStack.isSameItemSameTags(stack, (ItemStack) entry.getKey())) {
                return entry.getDoubleValue();
            }
        }
        return 0.0;
    }

    private static double calculateAddWeight(int count) {
        return Math.log(Math.pow((double) count, AMConfig.transmutingWeightAddStep));
    }

    private static double calculateRemoveWeight(int count) {
        return -Math.log(Math.pow((double) count, AMConfig.transmutingWeightRemoveStep));
    }

    public void putWeight(ItemStack stack, double newWeight) {
        ItemStack replace = stack;
        ObjectIterator var5 = this.itemstackData.keySet().iterator();
        while (var5.hasNext()) {
            ItemStack entry = (ItemStack) var5.next();
            if (ItemStack.isSameItemSameTags(stack, entry)) {
                replace = entry;
                break;
            }
        }
        this.itemstackData.put(replace, Math.max(newWeight, 0.0));
    }

    @Nullable
    public ItemStack getRandomItem(Random random) {
        ItemStack result = null;
        double bestValue = Double.MAX_VALUE;
        ObjectIterator var5 = this.itemstackData.object2DoubleEntrySet().iterator();
        while (var5.hasNext()) {
            Entry<ItemStack> entry = (Entry<ItemStack>) var5.next();
            if (!(entry.getDoubleValue() <= 0.0)) {
                double value = -Math.log(random.nextDouble()) / entry.getDoubleValue();
                if (value < bestValue) {
                    bestValue = value;
                    result = ((ItemStack) entry.getKey()).copy();
                }
            }
        }
        return result;
    }

    public CompoundTag saveAsNBT() {
        CompoundTag compound = new CompoundTag();
        ListTag listTag = new ListTag();
        ObjectIterator var3 = this.itemstackData.object2DoubleEntrySet().iterator();
        while (var3.hasNext()) {
            Entry<ItemStack> entry = (Entry<ItemStack>) var3.next();
            CompoundTag tag = new CompoundTag();
            tag.put("Item", ((ItemStack) entry.getKey()).save(new CompoundTag()));
            tag.putDouble("Weight", entry.getDoubleValue());
            listTag.add(tag);
        }
        compound.put("TransmutationData", listTag);
        return compound;
    }

    public static TransmutationData fromNBT(CompoundTag compound) {
        TransmutationData data = new TransmutationData();
        if (compound.contains("TransmutationData")) {
            ListTag listtag = compound.getList("TransmutationData", 10);
            for (int i = 0; i < listtag.size(); i++) {
                CompoundTag innerTag = listtag.getCompound(i);
                try {
                    ItemStack from = ItemStack.of(innerTag.getCompound("Item"));
                    if (!from.isEmpty()) {
                        data.putWeight(from, innerTag.getDouble("Weight"));
                    }
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }
        }
        return data;
    }

    public double getTotalWeight() {
        return this.itemstackData.values().doubleStream().sum();
    }
}