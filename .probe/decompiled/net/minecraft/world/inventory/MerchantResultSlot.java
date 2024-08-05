package net.minecraft.world.inventory;

import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;

public class MerchantResultSlot extends Slot {

    private final MerchantContainer slots;

    private final Player player;

    private int removeCount;

    private final Merchant merchant;

    public MerchantResultSlot(Player player0, Merchant merchant1, MerchantContainer merchantContainer2, int int3, int int4, int int5) {
        super(merchantContainer2, int3, int4, int5);
        this.player = player0;
        this.merchant = merchant1;
        this.slots = merchantContainer2;
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
    protected void onQuickCraft(ItemStack itemStack0, int int1) {
        this.removeCount += int1;
        this.checkTakeAchievements(itemStack0);
    }

    @Override
    protected void checkTakeAchievements(ItemStack itemStack0) {
        itemStack0.onCraftedBy(this.player.m_9236_(), this.player, this.removeCount);
        this.removeCount = 0;
    }

    @Override
    public void onTake(Player player0, ItemStack itemStack1) {
        this.checkTakeAchievements(itemStack1);
        MerchantOffer $$2 = this.slots.getActiveOffer();
        if ($$2 != null) {
            ItemStack $$3 = this.slots.getItem(0);
            ItemStack $$4 = this.slots.getItem(1);
            if ($$2.take($$3, $$4) || $$2.take($$4, $$3)) {
                this.merchant.notifyTrade($$2);
                player0.awardStat(Stats.TRADED_WITH_VILLAGER);
                this.slots.setItem(0, $$3);
                this.slots.setItem(1, $$4);
            }
            this.merchant.overrideXp(this.merchant.getVillagerXp() + $$2.getXp());
        }
    }
}