package com.mna.gui.containers.slots;

import com.mna.items.filters.ItemFilter;
import com.mna.items.filters.ItemFilterGroup;
import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;

public class ItemFilterSlot extends BaseSlot {

    private ItemFilterGroup itemType;

    private int maxStackSize = -1;

    private Consumer<Integer> changedCallback;

    private boolean active = true;

    public ItemFilterSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ItemFilter validItems) {
        super(itemHandler, index, xPosition, yPosition);
        this.itemType = new ItemFilterGroup(validItems);
    }

    public ItemFilterSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, ItemFilterGroup validItems) {
        super(itemHandler, index, xPosition, yPosition);
        this.itemType = validItems;
    }

    public ItemFilterSlot setMaxStackSize(int stackSize) {
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
        return this.itemType.anyMatchesFilter(stack);
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isActive() {
        return this.active;
    }

    @OnlyIn(Dist.CLIENT)
    public void setActive(boolean active) {
        this.active = active;
    }
}