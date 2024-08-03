package com.mna.gui.containers.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class SlotNoPickup extends BaseSlot {

    public SlotNoPickup(IItemHandlerModifiable craftResult, int index, int x, int y) {
        super(craftResult, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int par1) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onQuickCraft(ItemStack par1ItemStack, int par2) {
    }

    @Override
    protected void checkTakeAchievements(ItemStack itemCrafted) {
    }

    @Override
    public void onQuickCraft(ItemStack par1ItemStack, ItemStack par2ItemStack) {
    }

    @Override
    public void onTake(Player par1EntityPlayer, ItemStack par2ItemStack) {
    }
}