package net.minecraft.world.inventory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class FurnaceResultSlot extends Slot {

    private final Player player;

    private int removeCount;

    public FurnaceResultSlot(Player player0, Container container1, int int2, int int3, int int4) {
        super(container1, int2, int3, int4);
        this.player = player0;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack0) {
        return false;
    }

    @Override
    public ItemStack remove(int int0) {
        if (this.m_6657_()) {
            this.removeCount = this.removeCount + Math.min(int0, this.m_7993_().getCount());
        }
        return super.remove(int0);
    }

    @Override
    public void onTake(Player player0, ItemStack itemStack1) {
        this.checkTakeAchievements(itemStack1);
        super.onTake(player0, itemStack1);
    }

    @Override
    protected void onQuickCraft(ItemStack itemStack0, int int1) {
        this.removeCount += int1;
        this.checkTakeAchievements(itemStack0);
    }

    @Override
    protected void checkTakeAchievements(ItemStack itemStack0) {
        itemStack0.onCraftedBy(this.player.m_9236_(), this.player, this.removeCount);
        if (this.player instanceof ServerPlayer $$1 && this.f_40218_ instanceof AbstractFurnaceBlockEntity $$2) {
            $$2.awardUsedRecipesAndPopExperience($$1);
        }
        this.removeCount = 0;
    }
}