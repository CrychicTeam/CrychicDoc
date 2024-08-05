package com.mna.gui.containers.slots;

import com.mna.gui.containers.block.ContainerMagiciansWorkbench;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class SlotMagiciansWorkbenchOutput extends Slot {

    private final Container craftMatrix;

    private final Player thePlayer;

    private final ContainerMagiciansWorkbench workbench;

    private int amountCrafted;

    public SlotMagiciansWorkbenchOutput(Player player, Container craftMatrix, Container craftResult, ContainerMagiciansWorkbench workbench, int index, int x, int y) {
        super(craftResult, index, x, y);
        this.thePlayer = player;
        this.craftMatrix = craftMatrix;
        this.workbench = workbench;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int par1) {
        if (this.m_6657_()) {
            this.amountCrafted = this.amountCrafted + Math.min(par1, this.m_7993_().getCount());
        }
        return super.remove(par1);
    }

    @Override
    protected void onQuickCraft(ItemStack par1ItemStack, int par2) {
        this.amountCrafted += par2;
        this.checkTakeAchievements(par1ItemStack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack itemCrafted) {
        itemCrafted.onCraftedBy(this.thePlayer.m_9236_(), this.thePlayer, this.amountCrafted);
        this.amountCrafted = 0;
        ItemStack[] components = new ItemStack[this.craftMatrix.getContainerSize()];
        for (int i = 0; i < components.length; i++) {
            if (this.craftMatrix.getItem(i) != null) {
                components[i] = this.craftMatrix.getItem(i).copy();
            } else {
                components[i] = null;
            }
        }
        Level world = this.workbench.getWorkbench().m_58904_();
        if (!world.isClientSide) {
            this.workbench.getWorkbench().rememberRecipe(itemCrafted, components);
        }
    }

    @Override
    public void onQuickCraft(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        int i = par2ItemStack.getCount() - par1ItemStack.getCount();
        if (i > 0) {
            this.onQuickCraft(par2ItemStack, i);
            PlayerEvent.ItemCraftedEvent event = new PlayerEvent.ItemCraftedEvent(this.thePlayer, par2ItemStack, this.craftMatrix);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    @Override
    public void onTake(Player par1EntityPlayer, ItemStack par2ItemStack) {
        this.checkTakeAchievements(par2ItemStack);
        PlayerEvent.ItemCraftedEvent event = new PlayerEvent.ItemCraftedEvent(par1EntityPlayer, par2ItemStack, this.craftMatrix);
        MinecraftForge.EVENT_BUS.post(event);
        this.doComponentDecrements(par1EntityPlayer);
    }

    private void doComponentDecrements(Player par1EntityPlayer) {
        for (int i = 0; i < this.craftMatrix.getContainerSize(); i++) {
            ItemStack itemstack1 = this.craftMatrix.getItem(i);
            if (itemstack1 != null) {
                if (itemstack1.getCount() <= 1 && this.searchAndDecrement(par1EntityPlayer, itemstack1)) {
                    this.workbench.slotsChanged(this.craftMatrix);
                } else {
                    this.doStandardDecrement(par1EntityPlayer, this.craftMatrix, itemstack1, i);
                }
            }
        }
    }

    private boolean searchAndDecrement(Player player, ItemStack itemstack1) {
        for (int n = ContainerMagiciansWorkbench.INVENTORY_STORAGE_START; n <= ContainerMagiciansWorkbench.INVENTORY_STORAGE_END; n++) {
            ItemStack wbStack = this.workbench.getWorkbench().m_8020_(n);
            if (wbStack != null && ItemStack.isSameItemSameTags(itemstack1, wbStack)) {
                this.doStandardDecrement(player, this.workbench.getWorkbench(), wbStack, n);
                return true;
            }
        }
        return false;
    }

    private void doStandardDecrement(Player player, Container inventory, ItemStack itemstack1, int i) {
        if (itemstack1.getItem().hasCraftingRemainingItem(itemstack1)) {
            ItemStack remainder = itemstack1.getItem().getCraftingRemainingItem(itemstack1);
            if (remainder.isDamageableItem() && remainder.getDamageValue() > remainder.getMaxDamage()) {
                remainder = ItemStack.EMPTY;
            }
            inventory.removeItem(i, 1);
            if (!remainder.isEmpty()) {
                if (inventory.getItem(i).isEmpty()) {
                    inventory.setItem(i, remainder);
                } else if (!player.addItem(remainder)) {
                    player.drop(remainder, false);
                }
            }
        } else {
            inventory.removeItem(i, 1);
        }
    }
}