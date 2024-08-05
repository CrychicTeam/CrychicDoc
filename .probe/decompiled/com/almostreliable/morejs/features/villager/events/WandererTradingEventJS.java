package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.features.villager.TradeFilter;
import com.almostreliable.morejs.features.villager.TradeItem;
import com.almostreliable.morejs.features.villager.VillagerUtils;
import com.almostreliable.morejs.features.villager.trades.CustomTrade;
import com.almostreliable.morejs.features.villager.trades.SimpleTrade;
import com.almostreliable.morejs.features.villager.trades.TransformableTrade;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.entity.npc.VillagerTrades;

public class WandererTradingEventJS extends EventJS {

    private final Int2ObjectMap<List<VillagerTrades.ItemListing>> trades;

    public WandererTradingEventJS(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        this.trades = trades;
    }

    public List<VillagerTrades.ItemListing> getTrades(int level) {
        this.checkLevel(level);
        return (List<VillagerTrades.ItemListing>) this.trades.computeIfAbsent(level, $ -> new ArrayList());
    }

    public SimpleTrade addTrade(int level, TradeItem[] inputs, TradeItem output) {
        Preconditions.checkArgument(!output.isEmpty(), "Sell item cannot be empty");
        Preconditions.checkArgument(inputs.length != 0, "Buyer items cannot be empty");
        Preconditions.checkArgument(Arrays.stream(inputs).noneMatch(TradeItem::isEmpty), "Buyer items cannot be empty");
        SimpleTrade trade = VillagerUtils.createSimpleTrade(inputs, output);
        return this.addTrade(level, trade);
    }

    public <T extends VillagerTrades.ItemListing> T addTrade(int level, T trade) {
        Objects.requireNonNull(trade);
        this.getTrades(level).add(trade);
        return trade;
    }

    public void addCustomTrade(int level, TransformableTrade.Transformer transformer) {
        this.getTrades(level).add(new CustomTrade(transformer));
    }

    public void removeTrades(TradeFilter filter) {
        this.trades.forEach((level, listings) -> {
            filter.onMatch((first, second, output) -> ConsoleJS.SERVER.info("Removing wanderer trade for level " + level + ": " + first + " & " + second + " -> " + output));
            if (filter.matchMerchantLevel(level)) {
                listings.removeIf(itemListing -> itemListing instanceof TradeFilter.Filterable filterable ? filterable.matchesTradeFilter(filter) : false);
            }
        });
    }

    public void removeVanillaTrades(int level) {
        this.checkLevel(level);
        this.getTrades(level).removeIf(VillagerUtils::isVanillaTrade);
    }

    public void removeModdedTrades(int level) {
        this.checkLevel(level);
        this.getTrades(level).removeIf(VillagerUtils::isModdedTrade);
    }

    private void checkLevel(int level) {
        Preconditions.checkArgument(1 <= level && level <= 2, "Level must be between 1 and 2");
    }
}