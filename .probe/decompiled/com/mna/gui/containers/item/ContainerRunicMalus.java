package com.mna.gui.containers.item;

import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.HeldContainerBase;
import com.mna.gui.containers.slots.BaseSlot;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.filters.ItemFilterGroup;
import java.util.HashMap;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.items.IItemHandler;

public class ContainerRunicMalus extends HeldContainerBase {

    public ContainerRunicMalus(int i, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(i, playerInventory, new ItemInventoryBase(new ItemStack(ItemInit.RUNIC_MALUS.get(), 6), 6));
    }

    public ContainerRunicMalus(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(ContainerInit.RUNIC_MALUS.get(), i, playerInv, basebag);
    }

    @Override
    protected void initializeSlots(Inventory playerInv) {
        this.m_38897_(this.slot(this.inventory, 0, 42, 1));
        this.m_38897_(this.slot(this.inventory, 1, 118, 1));
        this.m_38897_(this.slot(this.inventory, 2, 36, 25));
        this.m_38897_(this.slot(this.inventory, 3, 124, 25));
        this.m_38897_(this.slot(this.inventory, 4, 42, 49));
        this.m_38897_(this.slot(this.inventory, 5, 118, 49));
        int slotIndex = 6;
        for (int xpos = 0; xpos < 3; xpos++) {
            for (int ypos = 0; ypos < 9; ypos++) {
                slotIndex++;
                this.m_38897_(new Slot(playerInv, ypos + xpos * 9 + 9, 8 + ypos * 18, 127 + xpos * 18));
            }
        }
        for (int var5 = 0; var5 < 9; var5++) {
            if (var5 == playerInv.selected) {
                this.mySlot = slotIndex;
            }
            this.m_38897_(new Slot(playerInv, var5, 8 + var5 * 18, 185));
            slotIndex++;
        }
    }

    @Override
    public BaseSlot slot(IItemHandler inv, int index, int x, int y) {
        return new ItemFilterSlot(inv, index, x, y, ItemFilterGroup.ANY_ENCHANTED_RUNE).setMaxStackSize(1);
    }

    @Override
    protected int slotsPerRow() {
        return 3;
    }

    @Override
    protected int numRows() {
        return 2;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        try {
            if (this.m_38853_(slotId).getItem().hashCode() == this.bagHash) {
                return;
            }
        } catch (Exception var6) {
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public void removed(Player player) {
        if (!player.m_9236_().isClientSide()) {
            HashMap<Enchantment, Integer> chants = new HashMap();
            HashMap<Enchantment, Float> bonuses = new HashMap();
            this.inventory.getAllItems().forEach(is -> {
                if (!is.isEmpty()) {
                    EnchantmentHelper.getEnchantments(is).forEach((ench, lvl) -> {
                        if (!chants.containsKey(ench) || lvl > (Integer) chants.get(ench)) {
                            chants.put(ench, lvl);
                        }
                    });
                }
            });
            this.inventory.getAllItems().forEach(is -> {
                if (!is.isEmpty()) {
                    EnchantmentHelper.getEnchantments(is).forEach((ench, lvl) -> {
                        float bonus = 0.0F;
                        int maxLvl = (Integer) chants.get(ench);
                        if (lvl == maxLvl - 1) {
                            bonus += 0.5F;
                        } else if (lvl == maxLvl) {
                            bonus++;
                        }
                        if (!bonuses.containsKey(ench)) {
                            bonuses.put(ench, bonus - 1.0F);
                        } else {
                            bonuses.put(ench, (Float) bonuses.get(ench) + bonus);
                        }
                    });
                }
            });
            bonuses.forEach((k, v) -> {
                if (chants.containsKey(k)) {
                    chants.put(k, (Integer) chants.get(k) + (int) Math.floor((double) v.floatValue()));
                }
            });
            EnchantmentHelper.setEnchantments(chants, this.inventory.getStack());
        }
        super.m_6877_(player);
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.numRows() * this.slotsPerRow()) {
                if (!this.m_38903_(itemstack1, this.numRows() * this.slotsPerRow(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 0, this.numRows() * this.slotsPerRow(), false)) {
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
}