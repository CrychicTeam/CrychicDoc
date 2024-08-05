package com.craisinlord.integrated_api.events;

import com.craisinlord.integrated_api.events.base.EventHandler;
import java.util.function.Consumer;
import net.minecraft.world.entity.npc.VillagerTrades;

public record RegisterWanderingTradesEvent(Consumer<VillagerTrades.ItemListing> basic, Consumer<VillagerTrades.ItemListing> rare) {

    public static final EventHandler<RegisterWanderingTradesEvent> EVENT = new EventHandler<>();

    public void addBasicTrade(VillagerTrades.ItemListing trade) {
        this.basic.accept(trade);
    }

    public void addRareTrade(VillagerTrades.ItemListing trade) {
        this.rare.accept(trade);
    }
}