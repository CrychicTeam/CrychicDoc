package net.minecraft.world.entity.npc;

import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class ClientSideMerchant implements Merchant {

    private final Player source;

    private MerchantOffers offers = new MerchantOffers();

    private int xp;

    public ClientSideMerchant(Player player0) {
        this.source = player0;
    }

    @Override
    public Player getTradingPlayer() {
        return this.source;
    }

    @Override
    public void setTradingPlayer(@Nullable Player player0) {
    }

    @Override
    public MerchantOffers getOffers() {
        return this.offers;
    }

    @Override
    public void overrideOffers(MerchantOffers merchantOffers0) {
        this.offers = merchantOffers0;
    }

    @Override
    public void notifyTrade(MerchantOffer merchantOffer0) {
        merchantOffer0.increaseUses();
    }

    @Override
    public void notifyTradeUpdated(ItemStack itemStack0) {
    }

    @Override
    public boolean isClientSide() {
        return this.source.m_9236_().isClientSide;
    }

    @Override
    public int getVillagerXp() {
        return this.xp;
    }

    @Override
    public void overrideXp(int int0) {
        this.xp = int0;
    }

    @Override
    public boolean showProgressBar() {
        return true;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_YES;
    }
}