package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.villager.events.VillagerTradingEventJS;
import com.almostreliable.morejs.features.villager.events.WandererTradingEventJS;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

public class TradingManager {

    @Nullable
    protected Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> tradesBackup;

    @Nullable
    protected Int2ObjectMap<List<VillagerTrades.ItemListing>> wandererTradesBackup;

    public void invokeVillagerTradeEvent(Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> originalTrades) {
        VillagerUtils.CACHED_PROFESSION_TRADES.clear();
        this.updateVanillaTrades(originalTrades);
        Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> trades = this.createMutableTradesMapByProfessions();
        Events.VILLAGER_TRADING.post(new VillagerTradingEventJS(trades));
        this.updateVanillaTrades(trades);
    }

    public void invokeWanderingTradeEvent(Int2ObjectMap<List<VillagerTrades.ItemListing>> originalTrades) {
        this.updateVanillaWanderingTrades(originalTrades);
        Int2ObjectMap<List<VillagerTrades.ItemListing>> wandererTrades = this.toListingsListMap(VillagerTrades.WANDERING_TRADER_TRADES);
        Events.WANDERING_TRADING.post(new WandererTradingEventJS(wandererTrades));
        this.updateVanillaWanderingTrades(wandererTrades);
    }

    public void reload() {
        this.invokeVillagerTradeEvent(this.getTradesBackup());
        this.invokeWanderingTradeEvent(this.getWandererTradesBackup());
    }

    public Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> getTradesBackup() {
        if (this.tradesBackup == null) {
            this.tradesBackup = this.createMutableTradesMapByProfessions();
        }
        return this.tradesBackup;
    }

    public Int2ObjectMap<List<VillagerTrades.ItemListing>> getWandererTradesBackup() {
        if (this.wandererTradesBackup == null) {
            this.wandererTradesBackup = this.toListingsListMap(VillagerTrades.WANDERING_TRADER_TRADES);
        }
        return this.wandererTradesBackup;
    }

    private Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> createMutableTradesMapByProfessions() {
        synchronized (VillagerTrades.TRADES) {
            Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> result = new HashMap();
            VillagerTrades.TRADES.forEach((profession, trades) -> {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> map = this.toListingsListMap(trades);
                result.put(profession, map);
            });
            return result;
        }
    }

    private synchronized Int2ObjectMap<List<VillagerTrades.ItemListing>> toListingsListMap(Int2ObjectMap<VillagerTrades.ItemListing[]> listingsMap) {
        Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>> result = new Int2ObjectOpenHashMap();
        listingsMap.forEach((level, listings) -> {
            ArrayList<VillagerTrades.ItemListing> newListings = new ArrayList(Arrays.stream(listings).toList());
            result.put(level, newListings);
        });
        return result;
    }

    private synchronized Int2ObjectMap<VillagerTrades.ItemListing[]> toListingsArrayMap(Int2ObjectMap<List<VillagerTrades.ItemListing>> listingsMap) {
        Int2ObjectOpenHashMap<VillagerTrades.ItemListing[]> result = new Int2ObjectOpenHashMap();
        listingsMap.forEach((level, listings) -> result.put(level, (VillagerTrades.ItemListing[]) listings.toArray(new VillagerTrades.ItemListing[0])));
        return result;
    }

    private void updateVanillaTrades(Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> trades) {
        synchronized (VillagerTrades.TRADES) {
            VillagerTrades.TRADES.clear();
            trades.forEach((profession, newTrades) -> {
                Int2ObjectMap<VillagerTrades.ItemListing[]> vanillaTrades = this.toListingsArrayMap(newTrades);
                VillagerTrades.TRADES.put(profession, vanillaTrades);
            });
        }
    }

    private void updateVanillaWanderingTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        synchronized (VillagerTrades.WANDERING_TRADER_TRADES) {
            VillagerTrades.WANDERING_TRADER_TRADES.clear();
            Int2ObjectMap<VillagerTrades.ItemListing[]> map = this.toListingsArrayMap(trades);
            VillagerTrades.WANDERING_TRADER_TRADES.putAll(map);
        }
    }
}