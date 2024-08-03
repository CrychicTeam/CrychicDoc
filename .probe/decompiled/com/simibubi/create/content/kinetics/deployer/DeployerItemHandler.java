package com.simibubi.create.content.kinetics.deployer;

import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class DeployerItemHandler implements IItemHandlerModifiable {

    private DeployerBlockEntity be;

    private DeployerFakePlayer player;

    public DeployerItemHandler(DeployerBlockEntity be) {
        this.be = be;
        this.player = be.player;
    }

    @Override
    public int getSlots() {
        return 1 + this.be.overflowItems.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= this.be.overflowItems.size() ? this.getHeld() : (ItemStack) this.be.overflowItems.get(slot);
    }

    public ItemStack getHeld() {
        return this.player == null ? ItemStack.EMPTY : this.player.m_21205_();
    }

    public void set(ItemStack stack) {
        if (this.player != null) {
            if (!this.be.m_58904_().isClientSide) {
                this.player.m_21008_(InteractionHand.MAIN_HAND, stack);
                this.be.m_6596_();
                this.be.sendData();
            }
        }
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot < this.be.overflowItems.size()) {
            return stack;
        } else if (!this.isItemValid(slot, stack)) {
            return stack;
        } else {
            ItemStack held = this.getHeld();
            if (held.isEmpty()) {
                if (!simulate) {
                    this.set(stack);
                }
                return ItemStack.EMPTY;
            } else if (!ItemHandlerHelper.canItemStacksStack(held, stack)) {
                return stack;
            } else {
                int space = held.getMaxStackSize() - held.getCount();
                ItemStack remainder = stack.copy();
                ItemStack split = remainder.split(space);
                if (space == 0) {
                    return stack;
                } else {
                    if (!simulate) {
                        held = held.copy();
                        held.setCount(held.getCount() + split.getCount());
                        this.set(held);
                    }
                    return remainder;
                }
            }
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else if (slot < this.be.overflowItems.size()) {
            ItemStack itemStack = (ItemStack) this.be.overflowItems.get(slot);
            int toExtract = Math.min(amount, itemStack.getCount());
            ItemStack extracted = simulate ? itemStack.copy() : itemStack.split(toExtract);
            extracted.setCount(toExtract);
            if (!simulate && itemStack.isEmpty()) {
                this.be.overflowItems.remove(slot);
            }
            if (!simulate && !extracted.isEmpty()) {
                this.be.m_6596_();
            }
            return extracted;
        } else {
            ItemStack held = this.getHeld();
            if (amount == 0 || held.isEmpty()) {
                return ItemStack.EMPTY;
            } else if (!this.be.filtering.getFilter().isEmpty() && this.be.filtering.test(held)) {
                return ItemStack.EMPTY;
            } else if (simulate) {
                return held.copy().split(amount);
            } else {
                ItemStack toReturn = held.split(amount);
                this.be.m_6596_();
                this.be.sendData();
                return toReturn;
            }
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return Math.min(this.getStackInSlot(slot).getMaxStackSize(), 64);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        FilteringBehaviour filteringBehaviour = this.be.getBehaviour(FilteringBehaviour.TYPE);
        return filteringBehaviour == null || filteringBehaviour.test(stack);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot < this.be.overflowItems.size()) {
            this.be.overflowItems.set(slot, stack);
        } else {
            this.set(stack);
        }
    }
}