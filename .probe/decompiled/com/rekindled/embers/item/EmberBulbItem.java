package com.rekindled.embers.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class EmberBulbItem extends EmberStorageItem implements IEmbersCurioItem {

    public static final double CAPACITY = 1000.0;

    public EmberBulbItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public double getCapacity() {
        return 1000.0;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EmberJarItem.EmberJarCapability(stack, this.getCapacity());
    }
}