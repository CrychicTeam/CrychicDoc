package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.features.villager.trades.CustomTrade;
import com.almostreliable.morejs.features.villager.trades.EnchantedItemTrade;
import com.almostreliable.morejs.features.villager.trades.MapPosInfo;
import com.almostreliable.morejs.features.villager.trades.PotionTrade;
import com.almostreliable.morejs.features.villager.trades.SimpleTrade;
import com.almostreliable.morejs.features.villager.trades.StewTrade;
import com.almostreliable.morejs.features.villager.trades.TransformableTrade;
import com.almostreliable.morejs.features.villager.trades.TreasureMapTrade;
import com.almostreliable.morejs.util.WeightedList;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;

public class VillagerUtils {

    public static final Map<VillagerProfession, List<VillagerTrades.ItemListing>> CACHED_PROFESSION_TRADES = new HashMap();

    public static final Set<Class<? extends VillagerTrades.ItemListing>> VANILLA_TRADE_TYPES = Set.of(VillagerTrades.DyedArmorForEmeralds.class, VillagerTrades.EnchantBookForEmeralds.class, VillagerTrades.EnchantedItemForEmeralds.class, VillagerTrades.ItemsForEmeralds.class, VillagerTrades.ItemsAndEmeraldsToItems.class, VillagerTrades.EmeraldForItems.class, VillagerTrades.TippedArrowForItemsAndEmeralds.class, VillagerTrades.SuspiciousStewForEmerald.class, VillagerTrades.TreasureMapForEmeralds.class);

    public static boolean isVanillaTrade(VillagerTrades.ItemListing listing) {
        return VANILLA_TRADE_TYPES.contains(listing.getClass());
    }

    public static boolean isModdedTrade(VillagerTrades.ItemListing listing) {
        return !isVanillaTrade(listing) && !isMoreJSTrade(listing);
    }

    public static boolean isMoreJSTrade(VillagerTrades.ItemListing listing) {
        return listing instanceof TransformableTrade || listing instanceof CustomTrade;
    }

    public static Collection<VillagerProfession> getProfessions() {
        return BuiltInRegistries.VILLAGER_PROFESSION.m_123024_().filter(p -> !p.name().equals("none")).toList();
    }

    public static VillagerProfession getProfession(ResourceLocation id) {
        VillagerProfession villagerProfession = BuiltInRegistries.VILLAGER_PROFESSION.get(id);
        if (villagerProfession == VillagerProfession.NONE) {
            throw new IllegalStateException("No profession with id " + id);
        } else {
            return villagerProfession;
        }
    }

    public static SimpleTrade createSimpleTrade(TradeItem[] inputs, TradeItem output) {
        return new SimpleTrade(inputs, output);
    }

    public static CustomTrade createCustomTrade(TransformableTrade.Transformer transformer) {
        return new CustomTrade(transformer);
    }

    public static TreasureMapTrade createStructureMapTrade(TradeItem[] inputs, WeightedList<Object> structures) {
        return TreasureMapTrade.forStructure(inputs, structures);
    }

    public static TreasureMapTrade createBiomeMapTrade(TradeItem[] inputs, WeightedList<Object> biomes) {
        return TreasureMapTrade.forBiome(inputs, biomes);
    }

    public static TreasureMapTrade createCustomMapTrade(TradeItem[] inputs, MapPosInfo.Provider func) {
        return new TreasureMapTrade(inputs, func);
    }

    public static EnchantedItemTrade createEnchantedItemTrade(TradeItem[] inputs, Item output) {
        return new EnchantedItemTrade(inputs, output);
    }

    public static StewTrade createStewTrade(TradeItem[] inputs, MobEffect[] effects, int duration) {
        return new StewTrade(inputs, effects, duration);
    }

    public static PotionTrade createPotionTrade(TradeItem[] inputs) {
        return new PotionTrade(inputs);
    }

    public static void setAbstractTrades(Map<Integer, VillagerTrades.ItemListing[]> tradeMap, int level, List<VillagerTrades.ItemListing> listings) {
        tradeMap.put(level, (VillagerTrades.ItemListing[]) listings.toArray(new VillagerTrades.ItemListing[0]));
    }

    public static List<VillagerTrades.ItemListing> getAbstractTrades(Map<Integer, VillagerTrades.ItemListing[]> tradeMap, int level) {
        VillagerTrades.ItemListing[] listings = (VillagerTrades.ItemListing[]) tradeMap.get(level);
        return listings == null ? new ArrayList() : new ArrayList(Arrays.asList(listings));
    }

    public static List<VillagerTrades.ItemListing> getVillagerTrades(VillagerProfession profession) {
        return (List<VillagerTrades.ItemListing>) CACHED_PROFESSION_TRADES.computeIfAbsent(profession, p -> {
            Int2ObjectMap<VillagerTrades.ItemListing[]> levelListings = (Int2ObjectMap<VillagerTrades.ItemListing[]>) VillagerTrades.TRADES.get(p);
            if (levelListings == null) {
                return List.of();
            } else {
                Builder<VillagerTrades.ItemListing> builder = ImmutableList.builder();
                ObjectIterator var3 = levelListings.values().iterator();
                while (var3.hasNext()) {
                    VillagerTrades.ItemListing[] listings = (VillagerTrades.ItemListing[]) var3.next();
                    for (VillagerTrades.ItemListing listing : listings) {
                        builder.add(listing);
                    }
                }
                return builder.build();
            }
        });
    }

    public static List<VillagerTrades.ItemListing> getVillagerTrades(VillagerProfession profession, int level) {
        Int2ObjectMap<VillagerTrades.ItemListing[]> levelListings = (Int2ObjectMap<VillagerTrades.ItemListing[]>) VillagerTrades.TRADES.get(profession);
        if (levelListings == null) {
            return List.of();
        } else {
            VillagerTrades.ItemListing[] listings = (VillagerTrades.ItemListing[]) levelListings.get(level);
            return listings == null ? List.of() : Arrays.asList(listings);
        }
    }

    public static VillagerTrades.ItemListing getRandomVillagerTrade(VillagerProfession profession) {
        List<VillagerTrades.ItemListing> trades = getVillagerTrades(profession);
        if (trades.isEmpty()) {
            throw new IllegalStateException("Profession " + profession + " has no trades");
        } else {
            return (VillagerTrades.ItemListing) trades.get(ThreadLocalRandom.current().nextInt(trades.size()));
        }
    }

    public static VillagerTrades.ItemListing getRandomVillagerTrade(VillagerProfession profession, int level) {
        List<VillagerTrades.ItemListing> trades = getVillagerTrades(profession, level);
        if (trades.isEmpty()) {
            throw new IllegalStateException("Profession " + profession + " on level " + level + " has no trades");
        } else {
            return (VillagerTrades.ItemListing) trades.get(ThreadLocalRandom.current().nextInt(trades.size()));
        }
    }

    public static List<VillagerTrades.ItemListing> getWandererTrades(int level) {
        VillagerTrades.ItemListing[] listings = (VillagerTrades.ItemListing[]) VillagerTrades.WANDERING_TRADER_TRADES.get(level);
        return listings == null ? List.of() : Arrays.asList(listings);
    }

    public static VillagerTrades.ItemListing getRandomWandererTrade(int level) {
        List<VillagerTrades.ItemListing> trades = getWandererTrades(level);
        if (trades.isEmpty()) {
            throw new IllegalStateException("Wanderer on level " + level + " has no trades");
        } else {
            return (VillagerTrades.ItemListing) trades.get(ThreadLocalRandom.current().nextInt(trades.size()));
        }
    }
}