package com.mna.api.events;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.eventbus.api.Event;

public class WanderingWizardSelectingTradesEvent extends Event {

    Merchant wandering_wizard;

    MerchantOffers offers;

    VillagerTrades.ItemListing[] newTrades;

    int maxNumbers;

    public WanderingWizardSelectingTradesEvent(Merchant wandering_wizard, MerchantOffers offers, VillagerTrades.ItemListing[] newTrades, int maxNumbers) {
        this.wandering_wizard = wandering_wizard;
        this.offers = offers;
        this.newTrades = newTrades;
        this.maxNumbers = maxNumbers;
    }

    public Merchant getWanderingWizard() {
        return this.wandering_wizard;
    }

    public MerchantOffers getOffers() {
        return this.offers;
    }

    public VillagerTrades.ItemListing[] getNewTrades() {
        return this.newTrades;
    }

    public int getMaxNumbers() {
        return this.maxNumbers;
    }

    public boolean isCancelable() {
        return false;
    }
}