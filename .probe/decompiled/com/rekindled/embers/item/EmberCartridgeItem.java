package com.rekindled.embers.item;

import com.rekindled.embers.api.item.IHeldEmberCell;
import com.rekindled.embers.power.DefaultEmberItemCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class EmberCartridgeItem extends EmberStorageItem {

    public static final double CAPACITY = 6000.0;

    public EmberCartridgeItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public double getCapacity() {
        return 6000.0;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EmberCartridgeItem.EmberCartridgeCapability(stack, this.getCapacity());
    }

    public static class EmberCartridgeCapability extends DefaultEmberItemCapability implements IHeldEmberCell {

        public EmberCartridgeCapability(ItemStack stack, double capacity) {
            super(stack, capacity);
        }
    }
}