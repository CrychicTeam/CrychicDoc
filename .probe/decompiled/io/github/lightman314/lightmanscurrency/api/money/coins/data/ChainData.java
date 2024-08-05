package io.github.lightman314.lightmanscurrency.api.money.coins.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.events.BuildDefaultMoneyDataEvent;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.atm.data.ATMData;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.client.CoinInputTypeHelper;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.CoinEntry;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.MainCoinEntry;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.SideBaseCoinEntry;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.ValueDisplayAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.ValueDisplayData;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.ValueDisplaySerializer;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.builtin.Null;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.common.capability.event_unlocks.CapabilityEventUnlocks;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.text.TextEntry;
import io.github.lightman314.lightmanscurrency.util.EnumUtil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ChainData {

    public static final Comparator<CoinEntry> SORT_HIGHEST_VALUE_FIRST = Comparator.comparingLong(CoinEntry::getCoreValue).reversed();

    public static final Comparator<CoinEntry> SORT_LOWEST_VALUE_FIRST = Comparator.comparingLong(CoinEntry::getCoreValue);

    public final boolean isEvent;

    public final String chain;

    private final MutableComponent displayName;

    private final CoinInputType inputType;

    private final ValueDisplayData displayData;

    private final ATMData atmData;

    private final List<CoinEntry> coreChain;

    private final List<List<CoinEntry>> sideChains;

    private final Map<ResourceLocation, CoinEntry> itemIdToEntryMap;

    private final List<CoinEntry> allEntryList;

    public MutableComponent getDisplayName() {
        return this.displayName;
    }

    public CoinInputType getInputType() {
        return this.inputType;
    }

    public ValueDisplayData getDisplayData() {
        return this.displayData;
    }

    public boolean isVisibleTo(@Nonnull Player player) {
        return !this.isEvent || CapabilityEventUnlocks.isUnlocked(player, this.chain) || LCAdminMode.isAdminPlayer(player);
    }

    public boolean hasATMData() {
        return this.atmData != null && !this.atmData.getExchangeButtons().isEmpty();
    }

    @Nonnull
    public ATMData getAtmData() {
        return this.atmData;
    }

    @Nonnull
    public MutableComponent formatValue(@Nonnull CoinValue value, @Nonnull MutableComponent empty) {
        return this.displayData.formatValue(value, empty);
    }

    public void formatCoinTooltip(@Nonnull ItemStack stack, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        this.displayData.formatCoinTooltip(stack, tooltip);
        if (flag.isAdvanced()) {
            CoinEntry entry = this.findEntry(stack);
            if (entry != null) {
                tooltip.add(LCText.TOOLTIP_COIN_ADVANCED_CHAIN.get(this.chain).withStyle(ChatFormatting.DARK_GRAY));
                tooltip.add(LCText.TOOLTIP_COIN_ADVANCED_VALUE.get(DecimalFormat.getIntegerInstance().format(entry.getCoreValue())).withStyle(ChatFormatting.DARK_GRAY));
                if (entry.isSideChain()) {
                    tooltip.add(LCText.TOOLTIP_COIN_ADVANCED_SIDE_CHAIN.get().withStyle(ChatFormatting.DARK_GRAY));
                } else {
                    tooltip.add(LCText.TOOLTIP_COIN_ADVANCED_CORE_CHAIN.get().withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }
    }

    protected ChainData(@Nonnull ChainData.Builder builder) {
        this.chain = builder.chain;
        this.displayName = builder.displayName;
        this.displayData = builder.displayData;
        this.displayData.setParent(this);
        this.inputType = builder.inputType;
        this.coreChain = ImmutableList.copyOf(builder.coreChain.entries);
        this.isEvent = builder.isEvent;
        List<List<CoinEntry>> temp = new ArrayList();
        builder.sideChains.forEach(chain -> temp.add(ImmutableList.copyOf(chain.entries)));
        this.sideChains = ImmutableList.copyOf(temp);
        this.atmData = builder.atmDataBuilder.build(this);
        this.defineEntryCoreValues();
        this.allEntryList = new ArrayList(this.coreChain);
        for (List<CoinEntry> sideChain : this.sideChains) {
            this.allEntryList.addAll(sideChain);
        }
        Map<ResourceLocation, CoinEntry> temp2 = new HashMap();
        for (CoinEntry entry : this.allEntryList) {
            temp2.put(ForgeRegistries.ITEMS.getKey(entry.getCoin()), entry);
        }
        this.itemIdToEntryMap = ImmutableMap.copyOf(temp2);
        this.cacheCoinExchanges();
    }

    protected ChainData(@Nonnull List<CoinEntry> existingEntries, @Nonnull JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        this.chain = GsonHelper.getAsString(json, "chain");
        if (this.chain.equalsIgnoreCase("null")) {
            throw new JsonSyntaxException("Chain id cannot be null!");
        } else {
            this.displayName = Component.Serializer.fromJson(json.get("name"));
            this.isEvent = GsonHelper.getAsBoolean(json, "EventChain", false);
            ResourceLocation displayType = new ResourceLocation(GsonHelper.getAsString(json, "displayType"));
            ValueDisplaySerializer displaySerializer = ValueDisplayAPI.get(displayType);
            if (displaySerializer == null) {
                throw new JsonSyntaxException(displayType + " is not a valid displayType");
            } else {
                displaySerializer.resetBuilder();
                displaySerializer.parseAdditional(json);
                this.inputType = EnumUtil.enumFromString(GsonHelper.getAsString(json, "InputType"), CoinInputType.values(), null);
                if (this.inputType == null) {
                    throw new JsonSyntaxException("InputType is not valid!");
                } else {
                    JsonArray coreChainArray = GsonHelper.getAsJsonArray(json, "CoreChain");
                    if (coreChainArray.isEmpty()) {
                        throw new JsonSyntaxException("CoreChain must have at least 1 entry!");
                    } else {
                        List<CoinEntry> coreChainTemp = new ArrayList();
                        try {
                            JsonObject baseEntry = coreChainArray.get(0).getAsJsonObject();
                            CoinEntry temp = CoinEntry.parse(baseEntry);
                            validateNoDuplicateCoins(temp, existingEntries);
                            coreChainTemp.add(temp);
                            displaySerializer.parseAdditionalFromCoin(temp, baseEntry);
                        } catch (ResourceLocationException | JsonSyntaxException var18) {
                            throw new JsonSyntaxException("Error parsing core chain entry #1 in the " + this.chain + " chain!", var18);
                        }
                        for (int i = 1; i < coreChainArray.size(); i++) {
                            try {
                                JsonObject entry = coreChainArray.get(i).getAsJsonObject();
                                CoinEntry temp = MainCoinEntry.parseMain(entry);
                                validateNoDuplicateCoins(temp, existingEntries);
                                displaySerializer.parseAdditionalFromCoin(temp, entry);
                                coreChainTemp.add(temp);
                            } catch (ResourceLocationException | JsonSyntaxException var17) {
                                throw new JsonSyntaxException("Error parsing core chain entry #" + (i + 1) + " in the " + this.chain + " chain!", var17);
                            }
                        }
                        this.coreChain = ImmutableList.copyOf(coreChainTemp);
                        List<List<CoinEntry>> sideChainsTemp = new ArrayList();
                        JsonArray sideChainsArray = GsonHelper.getAsJsonArray(json, "SideChains", new JsonArray());
                        for (int c = 0; c < sideChainsArray.size(); c++) {
                            try {
                                JsonArray chainArray = sideChainsArray.get(c).getAsJsonArray();
                                if (!chainArray.isEmpty()) {
                                    List<CoinEntry> tempList = new ArrayList();
                                    try {
                                        JsonObject baseEntry = chainArray.get(0).getAsJsonObject();
                                        CoinEntry temp = SideBaseCoinEntry.parseSub(baseEntry, this.coreChain);
                                        validateNoDuplicateCoins(temp, existingEntries);
                                        displaySerializer.parseAdditionalFromCoin(temp, baseEntry);
                                        tempList.add(temp);
                                    } catch (ResourceLocationException | JsonSyntaxException var16) {
                                        throw new JsonSyntaxException("Error parsing entry #1 in side chain #" + (c + 1) + " in the " + this.chain + " chain!", var16);
                                    }
                                    for (int i = 1; i < chainArray.size(); i++) {
                                        try {
                                            JsonObject entry = chainArray.get(i).getAsJsonObject();
                                            CoinEntry temp = MainCoinEntry.parseMain(entry, true);
                                            validateNoDuplicateCoins(temp, existingEntries);
                                            displaySerializer.parseAdditionalFromCoin(temp, entry);
                                            tempList.add(temp);
                                        } catch (ResourceLocationException | JsonSyntaxException var15) {
                                            throw new JsonSyntaxException("Error parsing entry #" + (i + 1) + " in side chain #" + (c + 1) + " in the " + this.chain + " chain!", var15);
                                        }
                                    }
                                    sideChainsTemp.add(ImmutableList.copyOf(tempList));
                                }
                            } catch (ResourceLocationException | JsonSyntaxException var19) {
                                throw new JsonSyntaxException("Error parsing side chain #" + (c + 1) + " in the " + this.chain + " chain!", var19);
                            }
                        }
                        this.sideChains = ImmutableList.copyOf(sideChainsTemp);
                        this.displayData = displaySerializer.build();
                        this.displayData.setParent(this);
                        if (json.has("ATMData")) {
                            this.atmData = ATMData.parse(GsonHelper.getAsJsonObject(json, "ATMData"), this);
                        } else {
                            this.atmData = ATMData.builder(null).build(this);
                        }
                        this.defineEntryCoreValues();
                        this.allEntryList = new ArrayList(this.coreChain);
                        for (List<CoinEntry> sideChain : this.sideChains) {
                            this.allEntryList.addAll(sideChain);
                        }
                        Map<ResourceLocation, CoinEntry> temp2 = new HashMap();
                        for (CoinEntry entry : this.allEntryList) {
                            temp2.put(ForgeRegistries.ITEMS.getKey(entry.getCoin()), entry);
                        }
                        this.itemIdToEntryMap = ImmutableMap.copyOf(temp2);
                        this.cacheCoinExchanges();
                    }
                }
            }
        }
    }

    private void defineEntryCoreValues() {
        long coreValue = 1L;
        for (int i = 0; i < this.coreChain.size(); i++) {
            CoinEntry entry = (CoinEntry) this.coreChain.get(i);
            if (i == 0) {
                entry.setCoreValue(coreValue);
            } else {
                coreValue *= (long) entry.getExchangeRate();
                entry.setCoreValue(coreValue);
            }
        }
        for (List<CoinEntry> sideChain : this.sideChains) {
            coreValue = 0L;
            for (int ix = 0; ix < sideChain.size(); ix++) {
                CoinEntry entry = (CoinEntry) sideChain.get(ix);
                if (ix == 0 && entry instanceof SideBaseCoinEntry e) {
                    coreValue = e.parentCoin.getCoreValue();
                }
                coreValue *= (long) entry.getExchangeRate();
                entry.setCoreValue(coreValue);
            }
        }
    }

    public JsonObject getAsJson() {
        JsonObject json = new JsonObject();
        json.addProperty("chain", this.chain);
        json.add("name", Component.Serializer.toJsonTree(this.displayName));
        json.addProperty("displayType", this.displayData.getType().toString());
        this.displayData.getSerializer().writeAdditional(this.displayData, json);
        json.addProperty("InputType", this.inputType.name());
        if (this.isEvent) {
            json.addProperty("EventChain", true);
        }
        JsonArray coreChainArray = new JsonArray();
        for (CoinEntry entry : this.coreChain) {
            coreChainArray.add(entry.serialize(this.displayData));
        }
        json.add("CoreChain", coreChainArray);
        if (!this.sideChains.isEmpty()) {
            JsonArray sideChainArray = new JsonArray();
            for (List<CoinEntry> sideChain : this.sideChains) {
                JsonArray chainArray = new JsonArray();
                for (CoinEntry entry : sideChain) {
                    chainArray.add(entry.serialize(this.displayData));
                }
                sideChainArray.add(chainArray);
            }
            json.add("SideChains", sideChainArray);
        }
        if (!this.atmData.getExchangeButtons().isEmpty()) {
            json.add("ATMData", this.atmData.save());
        }
        return json;
    }

    public boolean containsEntry(@Nonnull ItemStack item) {
        return this.findEntry(item) != null;
    }

    public boolean containsEntry(@Nonnull Item item) {
        return this.findEntry(item) != null;
    }

    @Nullable
    public CoinEntry findMatchingEntry(@Nonnull CoinEntry entry) {
        for (CoinEntry e : this.getAllEntries(true)) {
            if (e.matches(entry)) {
                return e;
            }
        }
        return null;
    }

    @Nullable
    public CoinEntry findEntry(@Nonnull ItemStack item) {
        return this.findEntry(item.getItem());
    }

    @Nullable
    public CoinEntry findEntry(@Nonnull Item item) {
        return (CoinEntry) this.itemIdToEntryMap.get(ForgeRegistries.ITEMS.getKey(item));
    }

    @Nonnull
    public List<CoinEntry> getAllEntries(boolean includeSideChains, @Nonnull Comparator<CoinEntry> sorter) {
        List<CoinEntry> list = this.getAllEntries(includeSideChains);
        list.sort(sorter);
        return list;
    }

    @Nonnull
    public List<CoinEntry> getAllEntries(boolean includeSideChains) {
        return includeSideChains ? new ArrayList(this.allEntryList) : new ArrayList(this.coreChain);
    }

    @Nonnull
    public List<CoinEntry> getCoreChain() {
        return this.coreChain;
    }

    @Nonnull
    public List<List<CoinEntry>> getSideChains() {
        return this.sideChains;
    }

    public long getCoreValue(@Nonnull ItemStack item) {
        return this.getCoreValue(item.getItem());
    }

    public long getCoreValue(@Nonnull Item item) {
        CoinEntry entry = this.findEntry(item);
        return entry == null ? 0L : entry.getCoreValue();
    }

    private void cacheCoinExchanges() {
        for (int i = 0; i < this.coreChain.size(); i++) {
            CoinEntry entry = (CoinEntry) this.coreChain.get(i);
            Pair<CoinEntry, Integer> down = null;
            if (i > 0) {
                down = Pair.of((CoinEntry) this.coreChain.get(i - 1), entry.getExchangeRate());
            }
            Pair<CoinEntry, Integer> up = null;
            if (i < this.coreChain.size() - 1) {
                CoinEntry nextEntry = (CoinEntry) this.coreChain.get(i + 1);
                up = Pair.of(nextEntry, nextEntry.getExchangeRate());
            }
            entry.defineExchanges(down, up);
        }
        for (List<CoinEntry> sideChain : this.sideChains) {
            for (int i = 0; i < sideChain.size(); i++) {
                CoinEntry entryx = (CoinEntry) sideChain.get(i);
                Pair<CoinEntry, Integer> downx = null;
                if (i == 0) {
                    if (entryx instanceof SideBaseCoinEntry e) {
                        downx = Pair.of(e.parentCoin, e.getExchangeRate());
                    }
                } else {
                    downx = Pair.of((CoinEntry) sideChain.get(i - 1), entryx.getExchangeRate());
                }
                Pair<CoinEntry, Integer> up = null;
                if (i < sideChain.size() - 1) {
                    CoinEntry nextEntry = (CoinEntry) sideChain.get(i + 1);
                    up = Pair.of(nextEntry, nextEntry.getExchangeRate());
                }
                entryx.defineExchanges(downx, up);
            }
        }
    }

    @Nullable
    public Pair<CoinEntry, Integer> getLowerExchange(@Nonnull Item item) {
        CoinEntry entry = this.findEntry(item);
        return entry == null ? null : entry.getLowerExchange();
    }

    @Nullable
    @Deprecated(since = "2.2.0.4")
    public Pair<CoinEntry, Integer> getLowerExchange(@Nonnull CoinEntry entry) {
        return entry.getLowerExchange();
    }

    @Nullable
    public Pair<CoinEntry, Integer> getUpperExchange(@Nonnull Item item) {
        CoinEntry entry = this.findEntry(item);
        return entry == null ? null : entry.getUpperExchange();
    }

    @Nullable
    @Deprecated(since = "2.2.0.4")
    public Pair<CoinEntry, Integer> getUpperExchange(@Nonnull CoinEntry entry) {
        return entry.getUpperExchange();
    }

    @OnlyIn(Dist.CLIENT)
    public Object getInputHandler() {
        return CoinInputTypeHelper.getHandler(this.inputType, this);
    }

    private static void validateNoDuplicateCoins(@Nonnull CoinEntry newEntry, @Nonnull List<CoinEntry> existingEntries) {
        for (CoinEntry entry : existingEntries) {
            if (entry.matches(newEntry.getCoin()) || newEntry.matches(entry)) {
                throw new JsonSyntaxException("Matching coin entry is already present");
            }
        }
        existingEntries.add(newEntry);
    }

    public static ChainData.Builder builder(@Nonnull String chain) {
        return new ChainData.Builder(BuildDefaultMoneyDataEvent.getExistingEntries(), chain, Component.translatable("lightmanscurrency.money.chain." + chain));
    }

    public static ChainData.Builder builder(@Nonnull String chain, @Nonnull MutableComponent displayName) {
        return new ChainData.Builder(BuildDefaultMoneyDataEvent.getExistingEntries(), chain, displayName);
    }

    public static ChainData.Builder builder(@Nonnull String chain, @Nonnull TextEntry displayName) {
        return new ChainData.Builder(BuildDefaultMoneyDataEvent.getExistingEntries(), chain, displayName.get());
    }

    public static ChainData fromJson(@Nonnull List<CoinEntry> existingEntries, @Nonnull JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        return new ChainData(existingEntries, (JsonObject) Objects.requireNonNull(json));
    }

    public static void addCoinTooltips(@Nonnull ItemStack stack, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag, @Nullable Player player) {
        ChainData chain = CoinAPI.API.ChainDataOfCoin(stack);
        if (chain != null && (player == null || flag.isAdvanced() || flag.isCreative() || chain.isVisibleTo(player))) {
            chain.formatCoinTooltip(stack, tooltip, flag);
        }
    }

    public static class Builder {

        private static ChainData.Builder latest = null;

        public final String chain;

        private final MutableComponent displayName;

        private boolean isEvent = false;

        private ValueDisplayData displayData = Null.INSTANCE;

        private CoinInputType inputType = CoinInputType.DEFAULT;

        private ChainData.Builder.ChainBuilder coreChain = null;

        private final List<ChainData.Builder.ChainBuilder> sideChains = new ArrayList();

        private final List<CoinEntry> existingEntries;

        private final ATMData.Builder atmDataBuilder = ATMData.builder(this);

        public static ChainData.Builder getLatest() {
            return latest;
        }

        private void validateNoDuplicateEntries(@Nonnull CoinEntry newEntry) {
            ChainData.validateNoDuplicateCoins(newEntry, this.existingEntries);
        }

        private Builder(@Nonnull List<CoinEntry> existingEntries, @Nonnull String chain, @Nonnull MutableComponent displayName) {
            this.chain = chain;
            this.displayName = displayName;
            this.existingEntries = existingEntries;
            latest = this;
        }

        public ChainData.Builder withDisplay(@Nonnull ValueDisplayData display) {
            this.displayData = display;
            return this;
        }

        public ChainData.Builder withInputType(@Nonnull CoinInputType inputType) {
            this.inputType = inputType;
            return this;
        }

        public ChainData.Builder asEvent() {
            this.isEvent = true;
            return this;
        }

        public ChainData.Builder.ChainBuilder withCoreChain(@Nonnull RegistryObject<? extends ItemLike> baseCoin) {
            return this.withCoreChain(baseCoin.get());
        }

        public ChainData.Builder.ChainBuilder withCoreChain(@Nonnull ItemLike baseCoin) {
            if (this.coreChain != null) {
                throw new IllegalArgumentException("Core Chain has already been built!");
            } else {
                this.coreChain = new ChainData.Builder.ChainBuilder(this, new CoinEntry(baseCoin.asItem()));
                return this.coreChain;
            }
        }

        public ChainData.Builder.ChainBuilder getCoreChain() {
            if (this.coreChain == null) {
                throw new IllegalArgumentException("Core Chain has not yet been built!");
            } else {
                return this.coreChain;
            }
        }

        public ChainData.Builder.ChainBuilder withSideChain(@Nonnull RegistryObject<? extends ItemLike> baseCoin, int exchangeRate, @Nonnull RegistryObject<? extends ItemLike> parentCoin) {
            return this.withSideChain(baseCoin.get(), exchangeRate, parentCoin.get());
        }

        public ChainData.Builder.ChainBuilder withSideChain(@Nonnull ItemLike baseCoin, int exchangeRate, @Nonnull ItemLike parentCoin) {
            if (this.coreChain == null) {
                throw new IllegalArgumentException("Cannot build a side chain until the core chain has been built!");
            } else {
                CoinEntry parentEntry = null;
                for (CoinEntry entry : this.coreChain.entries) {
                    if (entry.matches(parentCoin.asItem())) {
                        parentEntry = entry;
                        break;
                    }
                }
                if (parentEntry == null) {
                    throw new IllegalArgumentException("Coin is not in the core chain!");
                } else {
                    ChainData.Builder.ChainBuilder subChain = new ChainData.Builder.ChainBuilder(this, new SideBaseCoinEntry(baseCoin.asItem(), parentEntry, exchangeRate));
                    this.sideChains.add(subChain);
                    return subChain;
                }
            }
        }

        public List<ChainData.Builder.ChainBuilder> getSideChains() {
            return ImmutableList.copyOf(this.sideChains);
        }

        public ATMData.Builder atmBuilder() {
            return this.atmDataBuilder;
        }

        public void apply(@Nonnull BuildDefaultMoneyDataEvent event) {
            event.addDefault(this);
        }

        public void apply(@Nonnull BuildDefaultMoneyDataEvent event, boolean allowOverride) {
            event.addDefault(this, allowOverride);
        }

        @Nonnull
        public ChainData build() {
            return new ChainData(this);
        }

        public static final class ChainBuilder {

            private final ChainData.Builder parent;

            private final List<CoinEntry> entries = new ArrayList();

            private ChainBuilder(@Nonnull ChainData.Builder parent, @Nonnull CoinEntry baseCoin) {
                this.parent = parent;
                this.parent.validateNoDuplicateEntries(baseCoin);
                this.entries.add(baseCoin);
            }

            public ChainData.Builder.ChainBuilder withCoin(@Nonnull RegistryObject<? extends ItemLike> coin, int exchangeRate) {
                return this.withCoin(coin.get(), exchangeRate);
            }

            public ChainData.Builder.ChainBuilder withCoin(@Nonnull ItemLike coin, int exchangeRate) {
                CoinEntry newEntry = new MainCoinEntry(coin.asItem(), exchangeRate);
                this.parent.validateNoDuplicateEntries(newEntry);
                this.entries.add(newEntry);
                return this;
            }

            public ChainData.Builder back() {
                return this.parent;
            }

            public List<CoinEntry> getEntries() {
                return ImmutableList.copyOf(this.entries);
            }
        }
    }
}