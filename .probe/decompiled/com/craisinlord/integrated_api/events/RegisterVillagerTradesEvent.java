package com.craisinlord.integrated_api.events;

import com.craisinlord.integrated_api.events.base.EventHandler;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

public record RegisterVillagerTradesEvent(VillagerProfession type, BiConsumer<Integer, VillagerTrades.ItemListing> trade) {

    public static final EventHandler<RegisterVillagerTradesEvent> EVENT = new EventHandler<>();

    public void addTrade(int level, VillagerTrades.ItemListing trade) {
        this.trade.accept(level, trade);
    }
}