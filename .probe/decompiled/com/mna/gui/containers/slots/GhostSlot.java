package com.mna.gui.containers.slots;

import com.mna.gui.containers.IExtendedItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class GhostSlot extends SlotItemHandler {

    public GhostSlot(IExtendedItemHandler iExtendedItemHandler0, int int1, int int2, int int3) {
        super(iExtendedItemHandler0, int1, int2, int3);
    }

    @Override
    public boolean mayPickup(Player player0) {
        this.m_5852_(ItemStack.EMPTY);
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        this.m_5852_(stack.copy());
        return false;
    }

    @Override
    public ItemStack remove(int par1) {
        this.m_5852_(ItemStack.EMPTY);
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
        this.m_5852_(ItemStack.EMPTY);
    }
}