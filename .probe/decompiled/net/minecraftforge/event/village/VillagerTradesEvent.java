package net.minecraftforge.event.village;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.eventbus.api.Event;

public class VillagerTradesEvent extends Event {

    protected Int2ObjectMap<List<VillagerTrades.ItemListing>> trades;

    protected VillagerProfession type;

    public VillagerTradesEvent(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades, VillagerProfession type) {
        this.trades = trades;
        this.type = type;
    }

    public Int2ObjectMap<List<VillagerTrades.ItemListing>> getTrades() {
        return this.trades;
    }

    public VillagerProfession getType() {
        return this.type;
    }
}