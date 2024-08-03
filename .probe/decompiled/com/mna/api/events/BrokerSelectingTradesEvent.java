package com.mna.api.events;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.eventbus.api.Event;

public class BrokerSelectingTradesEvent extends Event {

    Merchant broker;

    ImmutableList<Item> allFactionItems;

    MerchantOffers offers;

    VillagerTrades.ItemListing[] newTrades;

    int maxNumbers;

    public BrokerSelectingTradesEvent(Merchant broker, MerchantOffers offers, VillagerTrades.ItemListing[] newTrades, int maxNumbers, ImmutableList<Item> allFactionItems) {
        this.broker = broker;
        this.offers = offers;
        this.newTrades = newTrades;
        this.maxNumbers = maxNumbers;
        this.allFactionItems = allFactionItems;
    }

    public Merchant getBroker() {
        return this.broker;
    }

    public ImmutableList<Item> getAllFactionItems() {
        return this.allFactionItems;
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