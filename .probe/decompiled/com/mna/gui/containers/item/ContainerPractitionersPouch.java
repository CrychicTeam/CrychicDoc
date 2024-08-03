package com.mna.gui.containers.item;

import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.LargeContainer;
import com.mna.gui.containers.slots.ExtendedItemStackFilterSlot;
import com.mna.gui.containers.slots.ExtendedItemStackSlot;
import com.mna.inventory.InventoryRitualKit;
import com.mna.items.ItemInit;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.ritual.PractitionersPouchPatches;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerPractitionersPouch extends LargeContainer {

    private InventoryRitualKit inventory;

    public int bagHash;

    protected int mySlot;

    private int myPlayerIndex;

    private HashMap<PractitionersPouchPatches, ArrayList<ExtendedItemStackFilterSlot>> patchSlots;

    public ContainerPractitionersPouch(int i, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(i, playerInventory, new InventoryRitualKit(playerInventory.getItem(playerInventory.selected)));
    }

    public ContainerPractitionersPouch(int windowID, Inventory playerInv, InventoryRitualKit kitInv) {
        super(ContainerInit.RITUAL_KIT.get(), windowID, playerInv, kitInv);
        this.inventory = kitInv;
        this.bagHash = kitInv.getStack().hashCode();
        this.myPlayerIndex = playerInv.selected;
        this.patchSlots = new HashMap();
        this.initializeSlots(playerInv);
    }

    protected void initializeSlots(Inventory playerInv) {
        int slotIndex = 0;
        int bagSlotIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 7; col++) {
                this.m_38897_(new ExtendedItemStackSlot(this.inventory, bagSlotIndex++, 18 + col * 18, 75 + row * 18).blacklistItem(ItemInit.PRACTITIONERS_POUCH.get()).blacklistItem(ItemInit.MITHIONS_MAGNIFICENT_MBAG.get()).blacklistItem(Items.SHULKER_BOX));
                slotIndex++;
            }
        }
        PractitionersPouchPatches[] patches = PractitionersPouchPatches.values();
        for (int i = 0; i < patches.length; i++) {
            PractitionersPouchPatches patch = patches[i];
            if (patch.hasInventory()) {
                ArrayList<ExtendedItemStackFilterSlot> patchSlotList = new ArrayList();
                for (int row = 0; row < patch.getInventoryRows(); row++) {
                    for (int col = 0; col < patch.getInventoryCols(); col++) {
                        ExtendedItemStackFilterSlot patchSlot = new ExtendedItemStackFilterSlot(this.inventory, bagSlotIndex++, 164 + col * 18, patch.getRowStart() + row * 18, patch.getSlotFilter());
                        if (playerInv.player.m_9236_().isClientSide()) {
                            patchSlot.setActive(false);
                        }
                        this.m_38897_(patchSlot);
                        patchSlotList.add(patchSlot);
                        slotIndex++;
                    }
                }
                this.patchSlots.put(patch, patchSlotList);
            }
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.m_38897_(new Slot(playerInv, col + row * 9 + 9, 48 + col * 18, 144 + row * 18));
                slotIndex++;
            }
        }
        for (int col = 0; col < 9; col++) {
            if (col == playerInv.selected) {
                this.mySlot = slotIndex;
            }
            this.m_38897_(new Slot(playerInv, col, 48 + col * 18, 202));
            slotIndex++;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void setPatchActive(PractitionersPouchPatches patch) {
        this.patchSlots.entrySet().forEach(e -> ((ArrayList) e.getValue()).forEach(v -> v.setActive(false)));
        if (patch != null && this.patchSlots.containsKey(patch)) {
            ((ArrayList) this.patchSlots.get(patch)).forEach(s -> s.setActive(true));
        }
    }

    @Override
    public boolean stillValid(Player p_75145_1_) {
        return true;
    }

    public boolean patchEnabled(PractitionersPouchPatches patch, int level) {
        return ((ItemPractitionersPouch) this.inventory.getStack().getItem()).getPatchLevel(this.inventory.getStack(), patch) >= level;
    }

    @Override
    public void broadcastChanges() {
        if (!this.isClientside) {
            this.inventory.writeItemStack();
        }
        super.broadcastChanges();
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId != this.mySlot && (clickTypeIn != ClickType.SWAP || dragType != this.myPlayerIndex)) {
            super.clicked(slotId, dragType, clickTypeIn, player);
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        int mainSlots = 21;
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < mainSlots) {
                if (!this.moveItemStackTo(itemstack1, mainSlots, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, mainSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean wrap) {
        boolean flag = false;
        int i = startIndex;
        if (wrap) {
            i = endIndex - 1;
        }
        if (stack.isStackable()) {
            while (!stack.isEmpty() && (wrap ? i >= startIndex : i < endIndex)) {
                Slot slot = (Slot) this.f_38839_.get(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(itemstack, stack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = slot.getMaxStackSize();
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.setChanged();
                        flag = true;
                    }
                }
                if (wrap) {
                    i--;
                } else {
                    i++;
                }
            }
        }
        if (!stack.isEmpty()) {
            if (wrap) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }
            while (wrap ? i >= startIndex : i < endIndex) {
                Slot slot1 = (Slot) this.f_38839_.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
                    if (stack.getCount() > slot1.getMaxStackSize()) {
                        slot1.set(stack.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.set(stack.split(stack.getCount()));
                    }
                    slot1.setChanged();
                    flag = true;
                    break;
                }
                if (wrap) {
                    i--;
                } else {
                    i++;
                }
            }
        }
        return flag;
    }
}