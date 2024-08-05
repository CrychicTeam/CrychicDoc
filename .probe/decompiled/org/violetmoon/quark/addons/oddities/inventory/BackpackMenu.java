package org.violetmoon.quark.addons.oddities.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.inventory.slot.BackpackSlot;
import org.violetmoon.quark.addons.oddities.inventory.slot.CachedItemHandlerSlot;
import org.violetmoon.quark.addons.oddities.module.BackpackModule;
import org.violetmoon.quark.base.util.InventoryIIH;

public class BackpackMenu extends InventoryMenu {

    public BackpackMenu(int windowId, Player player) {
        super(player.getInventory(), !player.m_9236_().isClientSide, player);
        this.f_38840_ = windowId;
        Inventory inventory = player.getInventory();
        for (Slot slot : this.f_38839_) {
            if (slot.container == inventory && slot.getSlotIndex() < inventory.getContainerSize() - 5) {
                slot.y += 58;
            }
        }
        Slot anchor = (Slot) this.f_38839_.get(9);
        int left = anchor.x;
        int top = anchor.y - 58;
        ItemStack backpack = inventory.armor.get(2);
        if (backpack.getItem() == BackpackModule.backpack) {
            InventoryIIH inv = new InventoryIIH(backpack);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    int k = j + i * 9;
                    this.m_38897_(new BackpackSlot(inv, k, left + j * 18, top + i * 18));
                }
            }
        }
    }

    public static BackpackMenu fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf buf) {
        return new BackpackMenu(windowId, playerInventory.player);
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        int topSlots = 8;
        int invStart = 9;
        int invEnd = 36;
        int hotbarStart = 36;
        int hotbarEnd = 45;
        int shieldSlot = 45;
        int backpackStart = 46;
        int backpackEnd = 73;
        ItemStack baseStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            baseStack = stack.copy();
            EquipmentSlot slotType = Mob.m_147233_(stack);
            int equipIndex = 8 - (slotType == null ? 0 : slotType.getIndex());
            if (index >= 9 && index != 45) {
                if (slotType != null && slotType.getType() == EquipmentSlot.Type.ARMOR && !((Slot) this.f_38839_.get(equipIndex)).hasItem()) {
                    if (!this.moveItemStackTo(stack, equipIndex, equipIndex + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotType != null && slotType == EquipmentSlot.OFFHAND && !((Slot) this.f_38839_.get(45)).hasItem()) {
                    if (!this.moveItemStackTo(stack, 45, 46, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 36) {
                    if (!this.moveItemStackTo(stack, 46, 73, false) && !this.moveItemStackTo(stack, 36, 45, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 45) {
                    if (!this.moveItemStackTo(stack, 9, 36, false) && !this.moveItemStackTo(stack, 46, 73, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(stack, 36, 45, false) && !this.moveItemStackTo(stack, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                ItemStack target = null;
                if (!this.moveItemStackTo(stack, 9, 45, false) && !this.moveItemStackTo(stack, 46, 73, false)) {
                    target = ItemStack.EMPTY;
                }
                if (target != null) {
                    return target;
                }
                if (index == 0) {
                    slot.onQuickCraft(stack, baseStack);
                }
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stack.getCount() == baseStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, stack);
            if (index == 0) {
                playerIn.drop(stack, false);
            }
        }
        return baseStack;
    }

    @Override
    public boolean moveItemStackTo(ItemStack stack, int start, int length, boolean r) {
        boolean successful = false;
        int i = !r ? start : length - 1;
        int iterOrder = !r ? 1 : -1;
        if (stack.isStackable()) {
            for (; stack.getCount() > 0 && (!r && i < length || r && i >= start); i += iterOrder) {
                Slot slot = (Slot) this.f_38839_.get(i);
                ItemStack existingStack = slot.getItem();
                if (!existingStack.isEmpty()) {
                    int maxStack = Math.min(stack.getMaxStackSize(), slot.getMaxStackSize());
                    int rmv = Math.min(maxStack, stack.getCount());
                    if (slot.mayPlace(cloneStack(stack, rmv)) && existingStack.getItem().equals(stack.getItem()) && ItemStack.isSameItemSameTags(stack, existingStack)) {
                        int existingSize = existingStack.getCount() + stack.getCount();
                        if (existingSize <= maxStack) {
                            stack.setCount(0);
                            existingStack.setCount(existingSize);
                            slot.set(existingStack);
                            successful = true;
                        } else if (existingStack.getCount() < maxStack) {
                            stack.shrink(maxStack - existingStack.getCount());
                            existingStack.setCount(maxStack);
                            slot.set(existingStack);
                            successful = true;
                        }
                    }
                }
            }
        }
        if (stack.getCount() > 0) {
            for (int var13 = !r ? start : length - 1; stack.getCount() > 0 && (!r && var13 < length || r && var13 >= start); var13 += iterOrder) {
                Slot slot = (Slot) this.f_38839_.get(var13);
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

    @Override
    public void clicked(int slotId, int dragType, @NotNull ClickType clickTypeIn, @NotNull Player player) {
        CachedItemHandlerSlot.cache(this);
        super.m_150399_(slotId, dragType, clickTypeIn, player);
        CachedItemHandlerSlot.applyCache(this);
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

    public static void saveCraftingInventory(Player player) {
        CraftingContainer crafting = ((InventoryMenu) player.containerMenu).getCraftSlots();
        for (int i = 0; i < crafting.m_6643_(); i++) {
            ItemStack stack = crafting.m_8020_(i);
            if (!stack.isEmpty() && !player.addItem(stack)) {
                player.drop(stack, false);
            }
        }
    }

    @NotNull
    @Override
    public MenuType<?> getType() {
        return BackpackModule.menyType;
    }
}