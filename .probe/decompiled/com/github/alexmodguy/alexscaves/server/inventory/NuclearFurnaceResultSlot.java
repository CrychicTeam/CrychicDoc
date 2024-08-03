package com.github.alexmodguy.alexscaves.server.inventory;

import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;

public class NuclearFurnaceResultSlot extends FurnaceResultSlot {

    private Player player;

    private int removeCountNuclear;

    public NuclearFurnaceResultSlot(Player player, Container furnaceContainer, int slotId, int x, int y) {
        super(player, furnaceContainer, slotId, x, y);
        this.player = player;
    }

    @Override
    public ItemStack remove(int count) {
        if (this.m_6657_()) {
            this.removeCountNuclear = this.removeCountNuclear + Math.min(count, this.m_7993_().getCount());
        }
        return super.remove(count);
    }

    @Override
    protected void onQuickCraft(ItemStack itemStack, int i) {
        this.removeCountNuclear += i;
        this.checkTakeAchievements(itemStack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack itemStack) {
        itemStack.onCraftedBy(this.player.m_9236_(), this.player, this.removeCountNuclear);
        if (this.player instanceof ServerPlayer serverplayer && this.f_40218_ instanceof NuclearFurnaceBlockEntity nuclearFurnaceBlockEntity) {
            nuclearFurnaceBlockEntity.awardUsedRecipesAndPopExperience(serverplayer);
        }
        this.removeCountNuclear = 0;
        ForgeEventFactory.firePlayerSmeltedEvent(this.player, itemStack);
    }
}