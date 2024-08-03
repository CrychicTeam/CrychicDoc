package com.simibubi.create.content.equipment.toolbox;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class ToolboxInventory extends ItemStackHandler {

    public static final int STACKS_PER_COMPARTMENT = 4;

    List<ItemStack> filters;

    boolean settling;

    private ToolboxBlockEntity blockEntity;

    private boolean limitedMode;

    public ToolboxInventory(ToolboxBlockEntity be) {
        super(32);
        this.blockEntity = be;
        this.limitedMode = false;
        this.filters = new ArrayList();
        this.settling = false;
        for (int i = 0; i < 8; i++) {
            this.filters.add(ItemStack.EMPTY);
        }
    }

    public void inLimitedMode(Consumer<ToolboxInventory> action) {
        this.limitedMode = true;
        action.accept(this);
        this.limitedMode = false;
    }

    public void settle(int compartment) {
        int totalCount = 0;
        boolean valid = true;
        boolean shouldBeEmpty = false;
        ItemStack sample = ItemStack.EMPTY;
        for (int i = 0; i < 4; i++) {
            ItemStack stackInSlot = this.getStackInSlot(compartment * 4 + i);
            totalCount += stackInSlot.getCount();
            if (!shouldBeEmpty) {
                shouldBeEmpty = stackInSlot.isEmpty() || stackInSlot.getCount() != stackInSlot.getMaxStackSize();
            } else if (!stackInSlot.isEmpty()) {
                valid = false;
                sample = stackInSlot;
            }
        }
        if (!valid) {
            this.settling = true;
            if (!sample.isStackable()) {
                for (int ix = 0; ix < 4; ix++) {
                    if (this.getStackInSlot(compartment * 4 + ix).isEmpty()) {
                        for (int j = ix + 1; j < 4; j++) {
                            ItemStack stackInSlot = this.getStackInSlot(compartment * 4 + j);
                            if (!stackInSlot.isEmpty()) {
                                this.setStackInSlot(compartment * 4 + ix, stackInSlot);
                                this.setStackInSlot(compartment * 4 + j, ItemStack.EMPTY);
                                break;
                            }
                        }
                    }
                }
            } else {
                for (int ixx = 0; ixx < 4; ixx++) {
                    ItemStack copy = totalCount <= 0 ? ItemStack.EMPTY : ItemHandlerHelper.copyStackWithSize(sample, Math.min(totalCount, sample.getMaxStackSize()));
                    this.setStackInSlot(compartment * 4 + ixx, copy);
                    totalCount -= copy.getCount();
                }
            }
            this.settling = false;
            this.blockEntity.sendData();
        }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (!stack.getItem().canFitInsideContainerItems()) {
            return false;
        } else if (slot >= 0 && slot < this.getSlots()) {
            int compartment = slot / 4;
            ItemStack filter = (ItemStack) this.filters.get(compartment);
            if (this.limitedMode && filter.isEmpty()) {
                return false;
            } else {
                return !filter.isEmpty() && !canItemsShareCompartment(filter, stack) ? false : super.isItemValid(slot, stack);
            }
        } else {
            return false;
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        super.setStackInSlot(slot, stack);
        int compartment = slot / 4;
        if (!stack.isEmpty() && ((ItemStack) this.filters.get(compartment)).isEmpty()) {
            this.filters.set(compartment, ItemHandlerHelper.copyStackWithSize(stack, 1));
            this.blockEntity.sendData();
        }
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        ItemStack insertItem = super.insertItem(slot, stack, simulate);
        if (insertItem.getCount() != stack.getCount()) {
            int compartment = slot / 4;
            if (!stack.isEmpty() && ((ItemStack) this.filters.get(compartment)).isEmpty()) {
                this.filters.set(compartment, ItemHandlerHelper.copyStackWithSize(stack, 1));
                this.blockEntity.sendData();
            }
        }
        return insertItem;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = super.serializeNBT();
        compound.put("Compartments", NBTHelper.writeItemList(this.filters));
        return compound;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (!this.settling && !this.blockEntity.m_58904_().isClientSide) {
            this.settle(slot / 4);
        }
        this.blockEntity.sendData();
        this.blockEntity.m_6596_();
        super.onContentsChanged(slot);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.filters = NBTHelper.readItemList(nbt.getList("Compartments", 10));
        if (this.filters.size() != 8) {
            this.filters.clear();
            for (int i = 0; i < 8; i++) {
                this.filters.add(ItemStack.EMPTY);
            }
        }
        super.deserializeNBT(nbt);
    }

    public ItemStack distributeToCompartment(@Nonnull ItemStack stack, int compartment, boolean simulate) {
        if (stack.isEmpty()) {
            return stack;
        } else if (((ItemStack) this.filters.get(compartment)).isEmpty()) {
            return stack;
        } else {
            for (int i = 3; i >= 0; i--) {
                int slot = compartment * 4 + i;
                stack = this.insertItem(slot, stack, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
            return stack;
        }
    }

    public ItemStack takeFromCompartment(int amount, int compartment, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            int remaining = amount;
            ItemStack lastValid = ItemStack.EMPTY;
            for (int i = 3; i >= 0; i--) {
                int slot = compartment * 4 + i;
                ItemStack extracted = this.extractItem(slot, remaining, simulate);
                remaining -= extracted.getCount();
                if (!extracted.isEmpty()) {
                    lastValid = extracted;
                }
                if (remaining == 0) {
                    return ItemHandlerHelper.copyStackWithSize(lastValid, amount);
                }
            }
            return remaining == amount ? ItemStack.EMPTY : ItemHandlerHelper.copyStackWithSize(lastValid, amount - remaining);
        }
    }

    public static ItemStack cleanItemNBT(ItemStack stack) {
        if (AllItems.BELT_CONNECTOR.isIn(stack)) {
            stack.removeTagKey("FirstPulley");
        }
        return stack;
    }

    public static boolean canItemsShareCompartment(ItemStack stack1, ItemStack stack2) {
        if (!stack1.isStackable() && !stack2.isStackable() && stack1.isDamageableItem() && stack2.isDamageableItem()) {
            return stack1.getItem() == stack2.getItem();
        } else {
            return AllItems.BELT_CONNECTOR.isIn(stack1) && AllItems.BELT_CONNECTOR.isIn(stack2) ? true : ItemHandlerHelper.canItemStacksStack(stack1, stack2);
        }
    }
}