package com.simibubi.create.compat.storageDrawers;

import com.simibubi.create.compat.Mods;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;

public class StorageDrawers {

    public static boolean isDrawer(BlockEntity be) {
        return be != null && Mods.STORAGEDRAWERS.id().equals(RegisteredObjects.getKeyOrThrow(be.getType()).getNamespace());
    }

    public static float getTrueFillLevel(IItemHandler inv, FilteringBehaviour filtering) {
        float occupied = 0.0F;
        float totalSpace = 0.0F;
        for (int slot = 1; slot < inv.getSlots(); slot++) {
            ItemStack stackInSlot = inv.getStackInSlot(slot);
            int space = inv.getSlotLimit(slot);
            int count = stackInSlot.getCount();
            if (space != 0) {
                totalSpace++;
                if (filtering.test(stackInSlot)) {
                    occupied += (float) count * (1.0F / (float) space);
                }
            }
        }
        return totalSpace == 0.0F ? 0.0F : occupied / totalSpace;
    }
}