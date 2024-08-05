package dev.architectury.registry.level.entity.trade;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.level.entity.trade.forge.TradeRegistryImpl;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

public class TradeRegistry {

    private TradeRegistry() {
    }

    public static void registerVillagerTrade(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        if (level < 1) {
            throw new IllegalArgumentException("Villager Trade level has to be at least 1!");
        } else {
            registerVillagerTrade0(profession, level, trades);
        }
    }

    @ExpectPlatform
    @Transformed
    private static void registerVillagerTrade0(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        TradeRegistryImpl.registerVillagerTrade0(profession, level, trades);
    }

    @ExpectPlatform
    @Transformed
    public static void registerTradeForWanderingTrader(boolean rare, VillagerTrades.ItemListing... trades) {
        TradeRegistryImpl.registerTradeForWanderingTrader(rare, trades);
    }
}