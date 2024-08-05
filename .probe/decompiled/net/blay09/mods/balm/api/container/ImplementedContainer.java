package net.blay09.mods.balm.api.container;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ImplementedContainer extends Container {

    NonNullList<ItemStack> getItems();

    static ImplementedContainer of(NonNullList<ItemStack> items) {
        return () -> items;
    }

    static ImplementedContainer ofSize(int size) {
        return of(NonNullList.withSize(size, ItemStack.EMPTY));
    }

    @Override
    default int getContainerSize() {
        return this.getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.getItem(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getItem(int slot) {
        return this.getItems().get(slot);
    }

    @Override
    default ItemStack removeItem(int slot, int count) {
        ItemStack result = ContainerHelper.removeItem(this.getItems(), slot, count);
        if (!result.isEmpty()) {
            this.setChanged();
        }
        this.slotChanged(slot);
        return result;
    }

    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        ItemStack itemStack = ContainerHelper.takeItem(this.getItems(), slot);
        this.slotChanged(slot);
        return itemStack;
    }

    @Override
    default void setItem(int slot, ItemStack stack) {
        this.getItems().set(slot, stack);
        if (stack.getCount() > this.m_6893_()) {
            stack.setCount(this.m_6893_());
        }
        this.setChanged();
        this.slotChanged(slot);
    }

    @Override
    default void clearContent() {
        this.getItems().clear();
        for (int i = 0; i < this.getItems().size(); i++) {
            this.slotChanged(i);
        }
    }

    @Override
    default void setChanged() {
    }

    default void slotChanged(int slot) {
    }

    @Override
    default boolean stillValid(Player player) {
        return true;
    }

    static NonNullList<ItemStack> deserializeInventory(CompoundTag tag, int minimumSize) {
        int size = Math.max(minimumSize, tag.contains("Size", 3) ? tag.getInt("Size") : minimumSize);
        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        ListTag itemTags = tag.getList("Items", 10);
        for (int i = 0; i < itemTags.size(); i++) {
            CompoundTag itemTag = itemTags.getCompound(i);
            int slot = itemTag.getInt("Slot");
            if (slot >= 0 && slot < items.size()) {
                items.set(slot, ItemStack.of(itemTag));
            }
        }
        return items;
    }

    default CompoundTag serializeInventory() {
        NonNullList<ItemStack> items = this.getItems();
        ListTag itemTags = new ListTag();
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                items.get(i).save(itemTag);
                itemTags.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", itemTags);
        nbt.putInt("Size", items.size());
        return nbt;
    }
}