package com.mna.gui.containers;

import com.mna.api.ManaAndArtificeMod;
import com.mna.gui.containers.slots.ExtendedItemStackSlot;
import com.mna.gui.containers.synchronizer.ExtendedContainerSynchronizer;
import com.mna.network.ServerMessageDispatcher;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class LargeContainer extends AbstractContainerMenu {

    protected final Inventory playerInv;

    protected final IExtendedItemHandler inventory;

    protected final boolean isClientside;

    public LargeContainer(MenuType<?> container, int id, Inventory playerInv, IExtendedItemHandler inventory) {
        super(container, id);
        this.playerInv = playerInv;
        this.inventory = inventory;
        this.isClientside = playerInv.player.m_9236_().isClientSide();
    }

    @Override
    public boolean stillValid(Player p_75145_1_) {
        return true;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            int oldDragEvent = this.f_38846_;
            this.f_38846_ = m_38947_(dragType);
            if ((oldDragEvent != 1 || this.f_38846_ != 2) && oldDragEvent != this.f_38846_) {
                this.m_38951_();
            } else if (this.m_142621_().isEmpty()) {
                this.m_38951_();
            } else if (this.f_38846_ == 0) {
                this.f_38845_ = m_38928_(dragType);
                if (m_38862_(this.f_38845_, player)) {
                    this.f_38846_ = 1;
                    this.f_38847_.clear();
                } else {
                    this.m_38951_();
                }
            } else if (this.f_38846_ == 1) {
                Slot addedSlot = (Slot) this.f_38839_.get(slotId);
                ItemStack mouseStack = this.m_142621_();
                if (addedSlot != null && m_38899_(addedSlot, mouseStack, true) && addedSlot.mayPlace(mouseStack) && (this.f_38845_ == 2 || mouseStack.getCount() > this.f_38847_.size()) && this.m_5622_(addedSlot)) {
                    this.f_38847_.add(addedSlot);
                }
            } else if (this.f_38846_ == 2) {
                if (!this.f_38847_.isEmpty()) {
                    ItemStack mouseStackCopy = this.m_142621_().copy();
                    int mouseCount = this.m_142621_().getCount();
                    for (Slot dragSlot : this.f_38847_) {
                        ItemStack mouseStack = this.m_142621_();
                        if (dragSlot != null && m_38899_(dragSlot, mouseStack, true) && dragSlot.mayPlace(mouseStack) && (this.f_38845_ == 2 || mouseStack.getCount() >= this.f_38847_.size()) && this.m_5622_(dragSlot)) {
                            ItemStack dragCopy = mouseStackCopy.copy();
                            int initialDragCount = dragSlot.hasItem() ? dragSlot.getItem().getCount() : 0;
                            int placeCount = m_278794_(this.f_38847_, this.f_38845_, dragCopy);
                            dragCopy.setCount(initialDragCount + placeCount);
                            int slotLimit = dragSlot.getMaxStackSize(dragCopy);
                            if (dragCopy.getCount() > slotLimit) {
                                dragCopy.setCount(slotLimit);
                            }
                            mouseCount -= dragCopy.getCount() - initialDragCount;
                            dragSlot.set(dragCopy);
                        }
                    }
                    mouseStackCopy.setCount(mouseCount);
                    this.m_142503_(mouseStackCopy);
                }
                this.m_38951_();
            } else {
                this.m_38951_();
            }
        } else if (this.f_38846_ != 0) {
            this.m_38951_();
        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999) {
                if (!this.m_142621_().isEmpty()) {
                    if (dragType == 0) {
                        player.drop(this.m_142621_(), true);
                        this.m_142503_(ItemStack.EMPTY);
                    }
                    if (dragType == 1) {
                        player.drop(this.m_142621_().split(1), true);
                    }
                }
            } else if (clickTypeIn == ClickType.QUICK_MOVE) {
                if (slotId < 0) {
                    return;
                }
                Slot transferSlot = (Slot) this.f_38839_.get(slotId);
                if (transferSlot == null || !transferSlot.mayPickup(player)) {
                    return;
                }
                for (ItemStack transferStack = this.m_7648_(player, slotId); !transferStack.isEmpty() && ItemStack.matches(transferSlot.getItem(), transferStack); transferStack = this.m_7648_(player, slotId)) {
                    itemstack = transferStack.copy();
                }
            } else {
                if (slotId < 0) {
                    return;
                }
                Slot clickedSlot = (Slot) this.f_38839_.get(slotId);
                if (clickedSlot != null) {
                    ItemStack slotStack = clickedSlot.getItem();
                    ItemStack mouseStack = this.m_142621_();
                    if (!slotStack.isEmpty()) {
                        itemstack = slotStack.copy();
                    }
                    if (slotStack.isEmpty()) {
                        if (!mouseStack.isEmpty() && clickedSlot.mayPlace(mouseStack)) {
                            int splitCount = dragType == 0 ? mouseStack.getCount() : 1;
                            if (splitCount > clickedSlot.getMaxStackSize(mouseStack)) {
                                splitCount = clickedSlot.getMaxStackSize(mouseStack);
                            }
                            clickedSlot.set(mouseStack.split(splitCount));
                            this.m_142503_(mouseStack);
                        }
                    } else if (clickedSlot.mayPickup(player)) {
                        if (mouseStack.isEmpty()) {
                            if (slotStack.isEmpty()) {
                                clickedSlot.set(ItemStack.EMPTY);
                                this.m_142503_(ItemStack.EMPTY);
                            } else {
                                int toMove = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                                if (clickedSlot instanceof ExtendedItemStackSlot && slotStack.getMaxStackSize() < slotStack.getCount()) {
                                    toMove = dragType == 0 ? slotStack.getMaxStackSize() : (slotStack.getMaxStackSize() + 1) / 2;
                                }
                                this.m_142503_(clickedSlot.remove(toMove));
                                if (slotStack.isEmpty()) {
                                    clickedSlot.set(ItemStack.EMPTY);
                                }
                                clickedSlot.onTake(player, this.m_142621_());
                            }
                        } else if (clickedSlot.mayPlace(mouseStack)) {
                            if (slotStack.getItem() == mouseStack.getItem() && ManaAndArtificeMod.getItemHelper().AreTagsEqual(slotStack, mouseStack)) {
                                int k2 = dragType == 0 ? mouseStack.getCount() : 1;
                                if (k2 > clickedSlot.getMaxStackSize(mouseStack) - slotStack.getCount()) {
                                    k2 = clickedSlot.getMaxStackSize(mouseStack) - slotStack.getCount();
                                }
                                mouseStack.shrink(k2);
                                slotStack.grow(k2);
                            } else if (mouseStack.getCount() <= clickedSlot.getMaxStackSize(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize()) {
                                clickedSlot.set(mouseStack);
                                this.m_142503_(slotStack);
                            }
                        } else if (slotStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1 && ManaAndArtificeMod.getItemHelper().AreTagsEqual(slotStack, mouseStack) && !slotStack.isEmpty()) {
                            int j2 = slotStack.getCount();
                            if (j2 + mouseStack.getCount() <= mouseStack.getMaxStackSize()) {
                                mouseStack.grow(j2);
                                slotStack = clickedSlot.remove(j2);
                                if (slotStack.isEmpty()) {
                                    clickedSlot.set(ItemStack.EMPTY);
                                }
                                clickedSlot.onTake(player, this.m_142621_());
                            }
                        }
                    }
                    clickedSlot.setChanged();
                }
            }
        } else if (clickTypeIn == ClickType.CLONE && player.getAbilities().instabuild && this.m_142621_().isEmpty() && slotId >= 0) {
            Slot cloneSlot = (Slot) this.f_38839_.get(slotId);
            if (cloneSlot != null && cloneSlot.hasItem()) {
                ItemStack clonedStack = cloneSlot.getItem().copy();
                clonedStack.setCount(clonedStack.getMaxStackSize());
                this.m_142503_(clonedStack);
            }
        } else if (clickTypeIn == ClickType.THROW && this.m_142621_().isEmpty() && slotId >= 0) {
            Slot throwSlot = (Slot) this.f_38839_.get(slotId);
            if (throwSlot != null && throwSlot.hasItem() && throwSlot.mayPickup(player)) {
                ItemStack thrownSlot = throwSlot.remove(dragType == 0 ? 1 : throwSlot.getItem().getCount());
                throwSlot.onTake(player, thrownSlot);
                player.drop(thrownSlot, true);
            }
        } else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
            Slot pickupSlot = (Slot) this.f_38839_.get(slotId);
            ItemStack mouseStackx = this.m_142621_();
            if (!mouseStackx.isEmpty() && (pickupSlot == null || !pickupSlot.hasItem() || !pickupSlot.mayPickup(player))) {
                int i = dragType == 0 ? 0 : this.f_38839_.size() - 1;
                int j = dragType == 0 ? 1 : -1;
                for (int k = 0; k < 2; k++) {
                    for (int l = i; l >= 0 && l < this.f_38839_.size() && mouseStackx.getCount() < mouseStackx.getMaxStackSize(); l += j) {
                        Slot slot1 = (Slot) this.f_38839_.get(l);
                        if (slot1.hasItem() && m_38899_(slot1, mouseStackx, true) && slot1.mayPickup(player) && this.m_5882_(mouseStackx, slot1)) {
                            ItemStack itemstack2 = slot1.getItem();
                            if (k != 0 || itemstack2.getCount() < slot1.getMaxStackSize(itemstack2)) {
                                int i1 = Math.min(mouseStackx.getMaxStackSize() - mouseStackx.getCount(), itemstack2.getCount());
                                ItemStack itemstack3 = slot1.remove(i1);
                                mouseStackx.grow(i1);
                                if (itemstack3.isEmpty()) {
                                    slot1.set(ItemStack.EMPTY);
                                }
                                slot1.onTake(player, itemstack3);
                            }
                        }
                    }
                }
            }
        }
        this.broadcastChanges();
        if (itemstack.getCount() > 64) {
            itemstack = itemstack.copy();
            itemstack.setCount(64);
        }
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }
        for (; !stack.isEmpty() && (reverseDirection ? i >= startIndex : i < endIndex); i += reverseDirection ? -1 : 1) {
            Slot slot = (Slot) this.f_38839_.get(i);
            ItemStack slotStack = slot.getItem();
            if (!slotStack.isEmpty() && slotStack.getItem() == stack.getItem() && ManaAndArtificeMod.getItemHelper().AreTagsEqual(stack, slotStack)) {
                int j = slotStack.getCount() + stack.getCount();
                int maxSize = slot.getMaxStackSize(slotStack);
                if (j <= maxSize) {
                    stack.setCount(0);
                    slotStack.setCount(j);
                    slot.setChanged();
                    flag = true;
                } else if (slotStack.getCount() < maxSize) {
                    stack.shrink(maxSize - slotStack.getCount());
                    slotStack.setCount(maxSize);
                    slot.setChanged();
                    flag = true;
                }
            }
        }
        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }
            while (reverseDirection ? i >= startIndex : i < endIndex) {
                Slot slot1 = (Slot) this.f_38839_.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
                    if (stack.getCount() > slot1.getMaxStackSize(stack)) {
                        try {
                            slot1.set(stack.split(slot1.getMaxStackSize(stack)));
                        } catch (ArrayIndexOutOfBoundsException var11) {
                            throw var11;
                        }
                    } else {
                        slot1.set(stack.split(stack.getCount()));
                    }
                    slot1.setChanged();
                    flag = true;
                    break;
                }
                i += reverseDirection ? -1 : 1;
            }
        }
        return flag;
    }

    protected boolean skipSlot(int i) {
        return this.skipSlot((Slot) this.f_38839_.get(i));
    }

    protected boolean skipSlot(Slot slot) {
        return false;
    }

    @Override
    public void broadcastChanges() {
        for (int i = 0; i < this.f_38839_.size(); i++) {
            if (!this.skipSlot(i)) {
                ItemStack itemstack = ((Slot) this.f_38839_.get(i)).getItem();
                ItemStack itemstack1 = (ItemStack) this.f_38841_.get(i);
                if (!ItemStack.matches(itemstack1, itemstack)) {
                    itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
                    this.f_38841_.set(i, itemstack1);
                    for (ContainerListener listener : this.f_38848_) {
                        Class<?> listenerEnclosingClass = listener.getClass().getEnclosingClass();
                        if (listenerEnclosingClass == ServerPlayer.class) {
                            ServerPlayer player = (ServerPlayer) this.playerInv.player;
                            this.syncSlot(player, i, itemstack1);
                        } else {
                            listener.slotChanged(this, i, itemstack1);
                        }
                    }
                }
            }
        }
        for (int j = 0; j < this.f_38842_.size(); j++) {
            DataSlot intreferenceholder = (DataSlot) this.f_38842_.get(j);
            if (intreferenceholder.checkAndClearUpdateFlag()) {
                for (ContainerListener icontainerlistener1 : this.f_38848_) {
                    icontainerlistener1.dataChanged(this, j, intreferenceholder.get());
                }
            }
        }
    }

    @Override
    public void setSynchronizer(ContainerSynchronizer synchronizer) {
        if (this.playerInv.player instanceof ServerPlayer) {
            super.setSynchronizer(new ExtendedContainerSynchronizer((ServerPlayer) this.playerInv.player));
        } else {
            super.setSynchronizer(synchronizer);
        }
    }

    public void syncInventory(ServerPlayer player) {
        for (int i = 0; i < this.f_38839_.size(); i++) {
            if (!this.skipSlot(i)) {
                ItemStack stack = ((Slot) this.f_38839_.get(i)).getItem();
                if (!stack.isEmpty()) {
                    ServerMessageDispatcher.sendExtendedItemStack(player, this.f_38840_, this.m_182424_(), i, stack);
                }
            }
        }
    }

    public void syncSlot(ServerPlayer player, int slot, ItemStack stack) {
        if (!this.skipSlot(slot)) {
            ServerMessageDispatcher.sendExtendedItemStack(player, this.f_38840_, this.m_182424_(), slot, stack);
        }
    }
}