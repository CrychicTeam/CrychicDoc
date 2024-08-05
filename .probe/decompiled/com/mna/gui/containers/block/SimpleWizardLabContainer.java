package com.mna.gui.containers.block;

import com.mna.api.affinity.Affinity;
import com.mna.blocks.tileentities.wizard_lab.WizardLabTile;
import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class SimpleWizardLabContainer<T extends AbstractContainerMenu, V extends WizardLabTile> extends AbstractContainerMenu {

    protected boolean closed = false;

    protected final V tile;

    protected final IItemHandler tileItemHandler;

    protected final Player user;

    public SimpleWizardLabContainer(MenuType<? extends SimpleWizardLabContainer<?, ?>> type, int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(type, i, playerInventory, (V) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos()));
    }

    public SimpleWizardLabContainer(MenuType<? extends SimpleWizardLabContainer<?, ?>> type, int i, Inventory playerInv, V tile) {
        super(type, i);
        this.user = playerInv.player;
        this.tile = tile;
        this.tileItemHandler = new InvWrapper(tile);
        this.initializeSlots(playerInv);
    }

    private void initializeSlots(Inventory playerInventory) {
        this.addInventorySlots();
        for (int xpos = 0; xpos < 3; xpos++) {
            for (int ypos = 0; ypos < 9; ypos++) {
                this.m_38897_(new Slot(playerInventory, ypos + xpos * 9 + 9, this.playerInventoryXStart() + ypos * 18, this.playerInventoryYStart() + xpos * 18));
            }
        }
        for (int var4 = 0; var4 < 9; var4++) {
            this.m_38897_(new Slot(playerInventory, var4, this.playerInventoryXStart() + var4 * 18, this.playerInventoryYStart() + 58));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    protected int playerInventoryYStart() {
        return 88;
    }

    protected int playerInventoryXStart() {
        return 48;
    }

    protected final int getInventorySize() {
        return this.tile.m_6643_();
    }

    protected abstract int addInventorySlots();

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.getInventorySize()) {
                if (!this.m_38903_(itemstack1, this.getInventorySize(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 0, this.getInventorySize(), false)) {
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

    public boolean canActivate() {
        return this.tile == null ? false : this.tile.canStart(this.user);
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonIndex) {
        return this.tile.clickMenuButton(player, buttonIndex);
    }

    public float getProgress() {
        return this.tile.getPctComplete();
    }

    public boolean isActive() {
        return this.tile.isActive();
    }

    public int getXPCost() {
        return this.tile.getXPCost(this.user);
    }

    public HashMap<Affinity, WizardLabTile.PowerStatus> powerRequirementStatus() {
        return this.tile.powerRequirementStatus();
    }
}