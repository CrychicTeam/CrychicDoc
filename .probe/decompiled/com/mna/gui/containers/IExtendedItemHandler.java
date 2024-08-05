package com.mna.gui.containers;

import com.mna.inventory.stack_extension.IInventoryListener;
import com.mna.inventory.stack_extension.MAInventoryData;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IExtendedItemHandler extends IItemHandlerModifiable {

    @Override
    default int getSlots() {
        return this.size();
    }

    int size();

    long getCountInSlot(int var1);

    void enlarge(int var1);

    CompoundTag serialize();

    void deserialize(CompoundTag var1);

    default void setInventoryData(MAInventoryData<?> data) {
    }

    @Nullable
    default MAInventoryData<?> getInventoryData() {
        return null;
    }

    default void markDirty() {
        MAInventoryData<?> data = this.getInventoryData();
        if (data != null) {
            data.setDirty();
        }
    }

    void addListener(IInventoryListener var1);
}