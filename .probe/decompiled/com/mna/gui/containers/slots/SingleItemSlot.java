package com.mna.gui.containers.slots;

import java.util.function.Consumer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class SingleItemSlot extends BaseSlot {

    private Item itemType;

    private int maxStackSize = -1;

    private boolean active = true;

    private Consumer<Integer> changedCallback;

    public SingleItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Item validItem) {
        super(itemHandler, index, xPosition, yPosition);
        this.itemType = validItem;
    }

    public SingleItemSlot setMaxStackSize(int stackSize) {
        this.maxStackSize = stackSize;
        return this;
    }

    @Override
    public int getMaxStackSize() {
        return this.maxStackSize > 0 ? this.maxStackSize : super.m_6641_();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return this.maxStackSize > 0 ? this.maxStackSize : super.m_5866_(stack);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem().equals(this.itemType);
    }

    public void addChangeListener(Consumer<Integer> callback) {
        this.changedCallback = callback;
    }

    @Override
    public void setChanged() {
        super.m_6654_();
        if (this.changedCallback != null) {
            this.changedCallback.accept(this.getSlotIndex());
        }
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.changedCallback != null) {
            this.changedCallback.accept(this.getSlotIndex());
        }
        return super.m_6201_(amount);
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}