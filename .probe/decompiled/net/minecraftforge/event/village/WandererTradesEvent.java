package net.minecraftforge.event.village;

import java.util.List;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.eventbus.api.Event;

public class WandererTradesEvent extends Event {

    protected List<VillagerTrades.ItemListing> generic;

    protected List<VillagerTrades.ItemListing> rare;

    public WandererTradesEvent(List<VillagerTrades.ItemListing> generic, List<VillagerTrades.ItemListing> rare) {
        this.generic = generic;
        this.rare = rare;
    }

    public List<VillagerTrades.ItemListing> getGenericTrades() {
        return this.generic;
    }

    public List<VillagerTrades.ItemListing> getRareTrades() {
        return this.rare;
    }
}