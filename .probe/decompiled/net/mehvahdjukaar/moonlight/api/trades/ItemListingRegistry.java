package net.mehvahdjukaar.moonlight.api.trades;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import net.mehvahdjukaar.moonlight.api.misc.CodecMapRegistry;
import net.mehvahdjukaar.moonlight.api.misc.MapRegistry;
import net.mehvahdjukaar.moonlight.api.misc.RegistryAccessJsonReloadListener;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

public class ItemListingRegistry extends RegistryAccessJsonReloadListener {

    protected static final CodecMapRegistry<ModItemListing> REGISTRY = MapRegistry.ofCodec();

    private static final Map<EntityType<?>, Int2ObjectArrayMap<List<ModItemListing>>> SPECIAL_CUSTOM_TRADES = new HashMap();

    private static final Map<VillagerProfession, Int2ObjectArrayMap<List<ModItemListing>>> CUSTOM_TRADES = new HashMap();

    private static final Map<EntityType<?>, Int2ObjectArrayMap<ModItemListing[]>> oldSpecialTrades = new HashMap();

    private static final Map<VillagerProfession, Int2ObjectArrayMap<ModItemListing[]>> oldTrades = new HashMap();

    private static int count = 0;

    public ItemListingRegistry() {
        super(new Gson(), "moonlight/villager_trades");
    }

    @Override
    public void parse(Map<ResourceLocation, JsonElement> jsons, RegistryAccess registryAccess) {
        this.mergeProfessionAndSpecial(false);
        count = 0;
        CUSTOM_TRADES.clear();
        SPECIAL_CUSTOM_TRADES.clear();
        DynamicOps<JsonElement> ops = ForgeHelper.addConditionOps(RegistryOps.create(JsonOps.INSTANCE, registryAccess));
        for (Entry<ResourceLocation, JsonElement> e : jsons.entrySet()) {
            JsonElement json = (JsonElement) e.getValue();
            ResourceLocation id = (ResourceLocation) e.getKey();
            if (id.getPath().contains("/")) {
                this.parseAndAddTrade(json, id, ops);
            }
        }
        this.mergeProfessionAndSpecial(true);
        if (count != 0) {
            Moonlight.LOGGER.info("Applied {} data villager trades", count);
        }
    }

    private void parseAndAddTrade(JsonElement json, ResourceLocation id, DynamicOps<JsonElement> ops) {
        ResourceLocation targetId = id.withPath((UnaryOperator<String>) (p -> p.substring(0, p.lastIndexOf(47))));
        Optional<VillagerProfession> profession = BuiltInRegistries.VILLAGER_PROFESSION.m_6612_(targetId);
        if (profession.isPresent()) {
            ModItemListing trade = parseOrThrow(json, id, ops);
            if (!(trade instanceof NoOpListing) && !(trade instanceof RemoveNonDataListingListing)) {
                ((List) ((Int2ObjectArrayMap) CUSTOM_TRADES.computeIfAbsent((VillagerProfession) profession.get(), t -> new Int2ObjectArrayMap())).computeIfAbsent(trade.getLevel(), a -> new ArrayList())).add(trade);
            }
        } else {
            Optional<EntityType<?>> entityType = BuiltInRegistries.ENTITY_TYPE.m_6612_(targetId);
            if (entityType.isPresent()) {
                ModItemListing trade = parseOrThrow(json, id, ops);
                if (!(trade instanceof NoOpListing)) {
                    ((List) ((Int2ObjectArrayMap) SPECIAL_CUSTOM_TRADES.computeIfAbsent((EntityType) entityType.get(), t -> new Int2ObjectArrayMap())).computeIfAbsent(trade.getLevel(), a -> new ArrayList())).add(trade);
                }
            } else {
                Moonlight.LOGGER.warn("Unknown villager type: {}", targetId);
            }
        }
    }

    private void mergeAll(Int2ObjectMap<VillagerTrades.ItemListing[]> originalValues, Int2ObjectArrayMap<List<ModItemListing>> newValues, boolean add) {
        ObjectIterator var4 = newValues.int2ObjectEntrySet().iterator();
        while (var4.hasNext()) {
            it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry<List<ModItemListing>> e = (it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry<List<ModItemListing>>) var4.next();
            int level = e.getIntKey();
            VillagerTrades.ItemListing[] elements = (VillagerTrades.ItemListing[]) originalValues.get(level);
            ArrayList<VillagerTrades.ItemListing> original = new ArrayList(elements == null ? List.of() : List.of(elements));
            List<ModItemListing> value = (List<ModItemListing>) e.getValue();
            if (add) {
                original.addAll(value);
                count = count + value.size();
            } else {
                original.removeAll(value);
            }
            originalValues.put(level, (VillagerTrades.ItemListing[]) original.toArray(VillagerTrades.ItemListing[]::new));
        }
    }

    private void mergeProfessionAndSpecial(boolean add) {
        for (Entry<VillagerProfession, Int2ObjectArrayMap<List<ModItemListing>>> p : CUSTOM_TRADES.entrySet()) {
            VillagerProfession profession = (VillagerProfession) p.getKey();
            Int2ObjectMap<VillagerTrades.ItemListing[]> map = (Int2ObjectMap<VillagerTrades.ItemListing[]>) VillagerTrades.TRADES.computeIfAbsent(profession, k -> new Int2ObjectArrayMap());
            Int2ObjectArrayMap<List<ModItemListing>> value = (Int2ObjectArrayMap<List<ModItemListing>>) p.getValue();
            this.mergeAll(map, value, add);
        }
        Int2ObjectArrayMap<List<ModItemListing>> wanderingStuff = (Int2ObjectArrayMap<List<ModItemListing>>) SPECIAL_CUSTOM_TRADES.get(EntityType.WANDERING_TRADER);
        if (wanderingStuff != null) {
            this.mergeAll(VillagerTrades.WANDERING_TRADER_TRADES, wanderingStuff, add);
        }
    }

    private static ModItemListing parseOrThrow(JsonElement j, ResourceLocation id, DynamicOps<JsonElement> ops) {
        return (ModItemListing) ((Pair) ModItemListing.CODEC.decode(ops, j).getOrThrow(false, errorMsg -> Moonlight.LOGGER.warn("Failed to parse custom trade with id {} - error: {}", id, errorMsg))).getFirst();
    }

    public static List<? extends VillagerTrades.ItemListing> getVillagerListings(VillagerProfession profession, int level) {
        VillagerTrades.ItemListing[] array = (VillagerTrades.ItemListing[]) ((Int2ObjectMap) VillagerTrades.TRADES.get(profession)).get(level);
        return array == null ? List.of() : Arrays.stream(array).toList();
    }

    public static List<? extends VillagerTrades.ItemListing> getSpecialListings(EntityType<?> entityType, int level) {
        if (entityType == EntityType.WANDERING_TRADER) {
            VillagerTrades.ItemListing[] array = (VillagerTrades.ItemListing[]) VillagerTrades.WANDERING_TRADER_TRADES.get(level);
            return array == null ? List.of() : Arrays.stream(array).toList();
        } else {
            Int2ObjectArrayMap<List<ModItemListing>> special = (Int2ObjectArrayMap<List<ModItemListing>>) SPECIAL_CUSTOM_TRADES.get(entityType);
            return special == null ? List.of() : (List) special.getOrDefault(level, List.of());
        }
    }

    public static void registerSerializer(ResourceLocation id, Codec<? extends ModItemListing> trade) {
        REGISTRY.register(id, trade);
    }

    public static void registerSimple(ResourceLocation id, VillagerTrades.ItemListing instance, int level) {
        ItemListingRegistry.SpecialListing specialListing = new ItemListingRegistry.SpecialListing(instance, level);
        registerSerializer(id, specialListing.getCodec());
    }

    static {
        REGISTRY.register(new ResourceLocation("simple"), SimpleItemListing.CODEC);
        REGISTRY.register(new ResourceLocation("remove_all_non_data"), RemoveNonDataListingListing.CODEC);
        REGISTRY.register(new ResourceLocation("no_op"), NoOpListing.CODEC);
    }

    private static class SpecialListing implements ModItemListing {

        private final Codec<ModItemListing> codec = Codec.unit(this);

        private final VillagerTrades.ItemListing listing;

        private final int level;

        public SpecialListing(VillagerTrades.ItemListing listing, int level) {
            this.listing = listing;
            this.level = level;
        }

        @Override
        public Codec<? extends ModItemListing> getCodec() {
            return this.codec;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            return this.listing.getOffer(trader, random);
        }

        @Override
        public int getLevel() {
            return this.level;
        }
    }
}