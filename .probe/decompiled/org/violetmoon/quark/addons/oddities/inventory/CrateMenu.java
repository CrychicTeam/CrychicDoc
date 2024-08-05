package org.violetmoon.quark.addons.oddities.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.block.be.CrateBlockEntity;
import org.violetmoon.quark.addons.oddities.capability.CrateItemHandler;
import org.violetmoon.quark.addons.oddities.module.CrateModule;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.network.message.oddities.ScrollCrateMessage;

public class CrateMenu extends AbstractContainerMenu {

    public final CrateBlockEntity crate;

    public final Inventory playerInv;

    public static final int numRows = 6;

    public static final int numCols = 9;

    public static final int displayedSlots = 54;

    public final int totalSlots;

    public int scroll = 0;

    private final ContainerData crateData;

    public CrateMenu(int id, Inventory inv, CrateBlockEntity crate) {
        this(id, inv, crate, new SimpleContainerData(2));
    }

    public CrateMenu(int id, Inventory inv, CrateBlockEntity crate, ContainerData data) {
        super(CrateModule.menuType, id);
        crate.startOpen(inv.player);
        this.crate = crate;
        this.playerInv = inv;
        this.crateData = data;
        int i = 36;
        CrateItemHandler handler = crate.itemHandler();
        this.totalSlots = handler.getSlots();
        for (int j = 0; j < this.totalSlots; j++) {
            this.m_38897_(new CrateMenu.CrateSlot(handler, j, 8 + j % 9 * 18, 18 + j / 9 * 18));
        }
        for (int l = 0; l < 3; l++) {
            for (int j1 = 0; j1 < 9; j1++) {
                this.m_38897_(new Slot(inv, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }
        for (int i1 = 0; i1 < 9; i1++) {
            this.m_38897_(new Slot(inv, i1, 8 + i1 * 18, 161 + i));
        }
        this.m_38884_(this.crateData);
    }

    public int getTotal() {
        return this.crateData.get(0);
    }

    public int getStackCount() {
        return this.crateData.get(1);
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack activeStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            activeStack = stackInSlot.copy();
            if (index < this.totalSlots) {
                if (!this.moveItemStackTo(stackInSlot, this.totalSlots, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 0, this.totalSlots, false)) {
                return ItemStack.EMPTY;
            }
            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return activeStack;
    }

    @Override
    public boolean moveItemStackTo(ItemStack stack, int start, int length, boolean reverse) {
        boolean successful = false;
        int i = reverse ? length - 1 : start;
        int iterOrder = reverse ? -1 : 1;
        if (stack.isStackable()) {
            for (; stack.getCount() > 0 && (!reverse && i < length || reverse && i >= start); i += iterOrder) {
                Slot slot = (Slot) this.f_38839_.get(i);
                ItemStack existingStack = slot.getItem();
                if (!existingStack.isEmpty()) {
                    int maxStack = Math.min(stack.getMaxStackSize(), slot.getMaxStackSize());
                    int rmv = Math.min(maxStack, stack.getCount());
                    if (slot.mayPlace(cloneStack(stack, rmv)) && existingStack.getItem().equals(stack.getItem()) && ItemStack.isSameItemSameTags(stack, existingStack)) {
                        int existingSize = existingStack.getCount() + stack.getCount();
                        ItemStack existingStackCopy = existingStack.copy();
                        if (existingSize <= maxStack) {
                            stack.setCount(0);
                            existingStackCopy.setCount(existingSize);
                            slot.set(existingStackCopy);
                            successful = true;
                        } else if (existingStackCopy.getCount() < maxStack) {
                            stack.shrink(maxStack - existingStackCopy.getCount());
                            existingStackCopy.setCount(maxStack);
                            slot.set(existingStackCopy);
                            successful = true;
                        }
                    }
                }
            }
        }
        if (stack.getCount() > 0) {
            for (int var14 = reverse ? length - 1 : start; stack.getCount() > 0 && (!reverse && var14 < length || reverse && var14 >= start); var14 += iterOrder) {
                Slot slot = (Slot) this.f_38839_.get(var14);
                ItemStack existingStack = slot.getItem();
                if (existingStack.isEmpty()) {
                    int maxStack = Math.min(stack.getMaxStackSize(), slot.getMaxStackSize());
                    int rmv = Math.min(maxStack, stack.getCount());
                    if (slot.mayPlace(cloneStack(stack, rmv))) {
                        existingStack = stack.split(rmv);
                        slot.set(existingStack);
                        successful = true;
                    }
                }
            }
        }
        return successful;
    }

    private static ItemStack cloneStack(ItemStack stack, int size) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack copy = stack.copy();
            copy.setCount(size);
            return copy;
        }
    }

    public static CrateMenu fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        CrateBlockEntity te = (CrateBlockEntity) playerInventory.player.m_9236_().getBlockEntity(pos);
        return new CrateMenu(windowId, playerInventory, te);
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        return this.crate.stillValid(playerIn);
    }

    @Override
    public void removed(@NotNull Player playerIn) {
        super.removed(playerIn);
        this.crate.stopOpen(playerIn);
    }

    public void scroll(boolean down, boolean packet) {
        boolean did = false;
        if (down) {
            int maxScroll = this.getStackCount() / 9 * 9;
            int target = this.scroll + 9;
            if (target <= maxScroll) {
                this.scroll = target;
                did = true;
                for (Slot slot : this.f_38839_) {
                    if (slot instanceof CrateMenu.CrateSlot) {
                        slot.y -= 18;
                    }
                }
            }
        } else {
            int target = this.scroll - 9;
            if (target >= 0) {
                this.scroll = target;
                did = true;
                for (Slot slotx : this.f_38839_) {
                    if (slotx instanceof CrateMenu.CrateSlot) {
                        slotx.y += 18;
                    }
                }
            }
        }
        if (did) {
            this.m_38946_();
            if (packet) {
                QuarkClient.ZETA_CLIENT.sendToServer(new ScrollCrateMessage(down));
            }
        }
    }

    private class CrateSlot extends SlotItemHandler {

        public CrateSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public void setChanged() {
            CrateMenu.this.crate.itemHandler().onContentsChanged(this.getSlotIndex());
        }

        @Override
        public boolean isActive() {
            int index = this.getSlotIndex();
            return index >= CrateMenu.this.scroll && index < CrateMenu.this.scroll + 54;
        }
    }
}