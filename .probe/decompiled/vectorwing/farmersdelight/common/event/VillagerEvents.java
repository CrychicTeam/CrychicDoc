package vectorwing.farmersdelight.common.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModItems;

@EventBusSubscriber(modid = "farmersdelight")
@ParametersAreNonnullByDefault
public class VillagerEvents {

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (Configuration.FARMERS_BUY_FD_CROPS.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            VillagerProfession profession = event.getType();
            ResourceLocation professionKey = ForgeRegistries.VILLAGER_PROFESSIONS.getKey(profession);
            if (professionKey != null) {
                if (professionKey.getPath().equals("farmer")) {
                    ((List) trades.get(1)).add(emeraldForItemsTrade(ModItems.ONION.get(), 26, 16, 2));
                    ((List) trades.get(1)).add(emeraldForItemsTrade(ModItems.TOMATO.get(), 26, 16, 2));
                    ((List) trades.get(2)).add(emeraldForItemsTrade(ModItems.CABBAGE.get(), 16, 16, 5));
                    ((List) trades.get(2)).add(emeraldForItemsTrade(ModItems.RICE.get(), 20, 16, 5));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        if (Configuration.WANDERING_TRADER_SELLS_FD_ITEMS.get()) {
            List<VillagerTrades.ItemListing> trades = event.getGenericTrades();
            trades.add(itemForEmeraldTrade(ModItems.CABBAGE_SEEDS.get(), 1, 12));
            trades.add(itemForEmeraldTrade(ModItems.TOMATO_SEEDS.get(), 1, 12));
            trades.add(itemForEmeraldTrade(ModItems.RICE.get(), 1, 12));
            trades.add(itemForEmeraldTrade(ModItems.ONION.get(), 1, 12));
        }
    }

    public static BasicItemListing emeraldForItemsTrade(ItemLike item, int count, int maxTrades, int xp) {
        return new BasicItemListing(new ItemStack(item, count), new ItemStack(Items.EMERALD), maxTrades, xp, 0.05F);
    }

    public static BasicItemListing itemForEmeraldTrade(ItemLike item, int maxTrades, int xp) {
        return new BasicItemListing(1, new ItemStack(item), maxTrades, xp, 0.05F);
    }
}