package io.github.lightman314.lightmanscurrency.common.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.events.BuildDefaultMoneyDataEvent;
import io.github.lightman314.lightmanscurrency.api.events.ChainDataReloadedEvent;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.atm.ATMAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.atm.data.ATMData;
import io.github.lightman314.lightmanscurrency.api.money.coins.atm.data.ATMExchangeButtonData;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.CoinInputType;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.CoinEntry;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.ValueDisplayAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.builtin.CoinDisplay;
import io.github.lightman314.lightmanscurrency.api.money.coins.display.builtin.NumberDisplay;
import io.github.lightman314.lightmanscurrency.api.money.coins.old_compat.OldCoinData;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.WalletCapability;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.common.items.WalletItem;
import io.github.lightman314.lightmanscurrency.network.LightmansCurrencyPacketHandler;
import io.github.lightman314.lightmanscurrency.network.message.data.SPacketSyncCoinData;
import io.github.lightman314.lightmanscurrency.util.FileUtil;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public final class CoinAPIImpl extends CoinAPI {

    public static final CoinAPIImpl INSTANCE = new CoinAPIImpl();

    public static final Comparator<ItemStack> SORTER = new CoinAPIImpl.CoinSorter();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    Map<String, ChainData> loadedChains = null;

    Map<ResourceLocation, ChainData> itemIdToChainMap = null;

    private boolean setup = false;

    private CoinAPIImpl() {
    }

    @Override
    public boolean NoDataAvailable() {
        return this.loadedChains == null;
    }

    @Override
    public void Setup() {
        if (!this.setup) {
            this.setup = true;
            MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
            MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::onJoinServer);
            MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::generateDefaultCoins);
            ValueDisplayAPI.Setup();
            ATMAPI.Setup();
        }
    }

    @Override
    public void ReloadCoinDataFromFile() {
        LightmansCurrency.LogInfo("Reloading Money Data");
        File mcl = new File("config/lightmanscurrency/MasterCoinList.json");
        if (!mcl.exists()) {
            LightmansCurrency.LogInfo("MasterCoinList.json does not exist. Creating a fresh copy.");
            this.createMoneyDataFile(mcl, this.generateDefaultMoneyData(), false);
        }
        try {
            JsonObject fileData = GsonHelper.parse(Files.readString(mcl.toPath()));
            if (fileData.has("CoinEntries") && !fileData.has("Chains")) {
                this.loadDeprecatedData(GsonHelper.getAsJsonArray(fileData, "CoinEntries"));
            } else {
                this.loadMoneyDataFromJson(fileData);
            }
        } catch (ResourceLocationException | IOException | JsonParseException var3) {
            LightmansCurrency.LogError("Error loading the Master Coin List. Using default values for now.", var3);
            this.loadData(this.generateDefaultMoneyData());
        }
        this.SyncCoinDataWith(PacketDistributor.ALL.noArg());
    }

    public static void LoadEditedData(@Nonnull String customJson) {
        try {
            JsonObject json = GsonHelper.parse(customJson);
            INSTANCE.loadMoneyDataFromJson(json);
            INSTANCE.createMoneyDataFile(new File("config/lightmanscurrency/MasterCoinList.json"), INSTANCE.loadedChains, false);
        } catch (ResourceLocationException | JsonParseException var2) {
            LightmansCurrency.LogError("Error parsing custom json data!", var2);
        }
    }

    private void loadData(@Nonnull Map<String, ChainData> dataMap) {
        ChainDataReloadedEvent.Pre event = new ChainDataReloadedEvent.Pre(dataMap);
        MinecraftForge.EVENT_BUS.post(event);
        this.loadedChains = event.getChainMap();
        Map<ResourceLocation, ChainData> temp = new HashMap();
        for (ChainData chain : this.loadedChains.values()) {
            for (CoinEntry entry : chain.getAllEntries(true)) {
                ResourceLocation coinID = ForgeRegistries.ITEMS.getKey(entry.getCoin());
                temp.put(coinID, chain);
            }
        }
        this.itemIdToChainMap = ImmutableMap.copyOf(temp);
        MinecraftForge.EVENT_BUS.post(new ChainDataReloadedEvent.Post(this.loadedChains));
    }

    private void loadDeprecatedData(@Nonnull JsonArray oldArray) throws JsonSyntaxException, ResourceLocationException {
        Map<String, List<OldCoinData>> oldData = new HashMap();
        for (int i = 0; i < oldArray.size(); i++) {
            try {
                OldCoinData data = OldCoinData.parse(GsonHelper.convertToJsonObject(oldArray.get(i), "CoinEntries[" + i + "]"));
                if (oldData.containsKey(data.chain)) {
                    ((List) oldData.get(data.chain)).add(data);
                } else {
                    List<OldCoinData> list = new ArrayList();
                    list.add(data);
                    oldData.put(data.chain, list);
                }
            } catch (ResourceLocationException | JsonSyntaxException var6) {
                LightmansCurrency.LogError("Error parsing CoinEntries[" + i + "] entry!", var6);
            }
        }
        Map<String, ChainData> tempMap = new HashMap();
        oldData.forEach((chain, dataList) -> {
            ChainData.Builder builder = ChainData.builder(chain, Component.translatable("lightmanscurrency.money.chain." + chain));
            CoinDisplay.Builder displayBuilder = CoinDisplay.builder();
            for (OldCoinData data : dataList) {
                Component initial = data.initialTranslation != null ? Component.translatable(data.initialTranslation) : null;
                Component plural = data.pluralTranslation != null ? Component.translatable(data.pluralTranslation) : null;
                if (initial != null || plural != null) {
                    displayBuilder.defineFor(data.coinItem, initial, plural);
                }
            }
            builder.withDisplay(displayBuilder.build());
            OldCoinData rootCoin = null;
            for (OldCoinData datax : dataList) {
                if (datax.worthOtherCoin == null && !datax.isHidden) {
                    rootCoin = datax;
                    break;
                }
            }
            dataList.remove(rootCoin);
            ChainData.Builder.ChainBuilder coreChain = builder.withCoreChain(rootCoin.coinItem);
            for (OldCoinData nextCoin = this.findNextInChain(dataList, rootCoin.coinItem, true); nextCoin != null; nextCoin = this.findNextInChain(dataList, nextCoin.coinItem, true)) {
                coreChain.withCoin(nextCoin.coinItem, nextCoin.worthOtherCoinCount);
                dataList.remove(nextCoin);
            }
            for (CoinEntry entry : coreChain.getEntries()) {
                for (OldCoinData sideChainRoot = this.findNextInChain(dataList, entry.getCoin(), false); sideChainRoot != null; sideChainRoot = this.findNextInChain(dataList, entry.getCoin(), false)) {
                    try {
                        dataList.remove(sideChainRoot);
                        ChainData.Builder.ChainBuilder sideChain = builder.withSideChain(sideChainRoot.coinItem, sideChainRoot.worthOtherCoinCount, entry.getCoin());
                        for (OldCoinData var19 = this.findNextInChain(dataList, sideChainRoot.coinItem, false); var19 != null; var19 = this.findNextInChain(dataList, var19.coinItem, false)) {
                            sideChain.withCoin(var19.coinItem, var19.worthOtherCoinCount);
                            dataList.remove(var19);
                        }
                    } catch (IllegalArgumentException var13) {
                    }
                }
            }
            if (!dataList.isEmpty()) {
                LightmansCurrency.LogWarning("Old MasterCoinList data could not be fully converted, likely due to multiple chain splits in a 'hidden' side-chain.");
            }
            if (chain.equals("main")) {
                ATMData.parseDeprecated(builder);
            }
            tempMap.put(chain, builder.build());
        });
        if (tempMap.isEmpty()) {
            throw new JsonSyntaxException("No valid chains could be converted to the new system!");
        } else {
            this.loadData(tempMap);
            LightmansCurrency.LogInfo("Old MasterCoinList data successfully converted to the new system! Replacing the old MasterCoinList.json file with the updated data.");
            this.createMoneyDataFile(new File("config/lightmanscurrency/MasterCoinList.json"), this.loadedChains, true);
        }
    }

    private OldCoinData findNextInChain(@Nonnull List<OldCoinData> dataList, @Nonnull Item coin, boolean coreChainOnly) {
        for (OldCoinData data : dataList) {
            if (data.worthOtherCoin == coin && (!coreChainOnly || !data.isHidden)) {
                return data;
            }
        }
        return null;
    }

    private void loadMoneyDataFromJson(@Nonnull JsonObject root) throws JsonSyntaxException, ResourceLocationException {
        List<CoinEntry> allEntries = new ArrayList();
        Map<String, ChainData> tempMap = new HashMap();
        JsonArray chainList = GsonHelper.getAsJsonArray(root, "Chains");
        for (int i = 0; i < chainList.size(); i++) {
            String chainName = "UNDEFINED";
            try {
                JsonObject entry = GsonHelper.convertToJsonObject(chainList.get(i), "Chains[" + i + "]");
                chainName = GsonHelper.getAsString(entry, "chain", null);
                ChainData chain = ChainData.fromJson(allEntries, entry);
                if (tempMap.containsKey(chain.chain)) {
                    throw new JsonSyntaxException("Multple '" + chain.chain + "' chains detected. Duplicate will be ignored!");
                }
                tempMap.put(chain.chain, chain);
            } catch (ResourceLocationException | JsonSyntaxException var9) {
                LightmansCurrency.LogError("Error loading Chain[" + i + "] (" + chainName + ") from the Master Coin List!", var9);
            }
        }
        if (tempMap.isEmpty()) {
            throw new JsonSyntaxException("No valid coin chains were registered!");
        } else if (!tempMap.containsKey("main")) {
            throw new JsonSyntaxException("At least 1 chain named 'main' must be present!");
        } else {
            this.loadData(tempMap);
        }
    }

    private void createMoneyDataFile(@Nonnull File mcl, @Nonnull Map<String, ChainData> data, boolean hideEventChains) {
        File dir = new File(mcl.getParent());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (dir.exists()) {
            try {
                mcl.createNewFile();
                JsonObject json = this.getDataJson(data, hideEventChains);
                FileUtil.writeStringToFile(mcl, GSON.toJson(json));
            } catch (IOException var6) {
                LightmansCurrency.LogError("Error attempting to create 'MasterCoinList.json' file.", var6);
            }
        }
    }

    @Nonnull
    private Map<String, ChainData> generateDefaultMoneyData() {
        BuildDefaultMoneyDataEvent event = new BuildDefaultMoneyDataEvent();
        try {
            MinecraftForge.EVENT_BUS.post(event);
        } catch (RuntimeException var3) {
            LightmansCurrency.LogError("Error generating default money data!", var3);
            return new HashMap();
        }
        Map<String, ChainData> results = new HashMap();
        event.getFinalResult().forEach((chain, builder) -> results.put(chain, builder.build()));
        return results;
    }

    private void generateDefaultCoins(BuildDefaultMoneyDataEvent event) {
        ChainData.builder("main", LCText.COIN_CHAIN_MAIN).withCoreChain(ModItems.COIN_COPPER).withCoin(ModItems.COIN_IRON, 10).withCoin(ModItems.COIN_GOLD, 10).withCoin(ModItems.COIN_EMERALD, 10).withCoin(ModItems.COIN_DIAMOND, 10).withCoin(ModItems.COIN_NETHERITE, 10).back().withSideChain(ModBlocks.COINPILE_COPPER, 9, ModItems.COIN_COPPER).withCoin(ModBlocks.COINBLOCK_COPPER, 4).back().withSideChain(ModBlocks.COINPILE_IRON, 9, ModItems.COIN_IRON).withCoin(ModBlocks.COINBLOCK_IRON, 4).back().withSideChain(ModBlocks.COINPILE_GOLD, 9, ModItems.COIN_GOLD).withCoin(ModBlocks.COINBLOCK_GOLD, 4).back().withSideChain(ModBlocks.COINPILE_EMERALD, 9, ModItems.COIN_EMERALD).withCoin(ModBlocks.COINBLOCK_EMERALD, 4).back().withSideChain(ModBlocks.COINPILE_DIAMOND, 9, ModItems.COIN_DIAMOND).withCoin(ModBlocks.COINBLOCK_DIAMOND, 4).back().withSideChain(ModBlocks.COINPILE_NETHERITE, 9, ModItems.COIN_NETHERITE).withCoin(ModBlocks.COINBLOCK_NETHERITE, 4).back().withDisplay(CoinDisplay.easyDefine()).atmBuilder().accept(ATMExchangeButtonData::generateMain).back().apply(event, true);
        ChainData.builder("emeralds", LCText.COIN_CHAIN_EMERALDS).withCoreChain(Items.EMERALD).withCoin(Items.EMERALD_BLOCK, 9).back().withInputType(CoinInputType.DEFAULT).withDisplay(new NumberDisplay(LCText.COIN_CHAIN_EMERALDS_DISPLAY, LCText.COIN_CHAIN_EMERALDS_DISPLAY_WORDY, Items.EMERALD)).apply(event, true);
    }

    @Nonnull
    private JsonObject getDataJson(@Nonnull Map<String, ChainData> data, boolean hideEventChains) {
        JsonObject fileJson = new JsonObject();
        JsonArray chainArray = new JsonArray();
        for (ChainData chain : data.values()) {
            if (!hideEventChains || !chain.isEvent) {
                chainArray.add(chain.getAsJson());
            }
        }
        fileJson.add("Chains", chainArray);
        return fileJson;
    }

    @Nonnull
    @Override
    public ItemStack getEquippedWallet(@Nonnull Player player) {
        ItemStack wallet = ItemStack.EMPTY;
        IWalletHandler walletHandler = WalletCapability.lazyGetWalletHandler(player);
        if (walletHandler != null) {
            wallet = walletHandler.getWallet();
        }
        if (!WalletItem.validWalletStack(wallet)) {
            LightmansCurrency.LogDebug(player.getName().getString() + "'s equipped wallet is not a valid WalletItem.");
            LightmansCurrency.LogDebug("Equipped wallet is of type " + wallet.getItem().getClass().getName());
            return ItemStack.EMPTY;
        } else {
            return wallet;
        }
    }

    @Nullable
    @Override
    public ChainData ChainData(@Nonnull String chain) {
        return this.NoDataAvailable() ? null : (ChainData) this.loadedChains.get(chain);
    }

    @Nonnull
    @Override
    public List<ChainData> AllChainData() {
        return this.NoDataAvailable() ? ImmutableList.of() : ImmutableList.copyOf(this.loadedChains.values());
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public ChainData ChainDataOfCoin(@Nonnull ItemStack coin) {
        return this.ChainDataOfCoin(coin.getItem());
    }

    @Nullable
    @Override
    public ChainData ChainDataOfCoin(@Nonnull Item coin) {
        return this.NoDataAvailable() ? null : (ChainData) this.itemIdToChainMap.get(ForgeRegistries.ITEMS.getKey(coin));
    }

    @Override
    public boolean IsCoin(@Nonnull ItemStack coin, boolean allowSideChains) {
        return !coin.isEmpty() && this.IsCoin(coin.getItem(), allowSideChains);
    }

    @Override
    public boolean IsCoin(@Nonnull Item coin, boolean allowSideChains) {
        if (coin == Items.AIR) {
            return false;
        } else {
            ChainData chainData = this.ChainDataOfCoin(coin);
            if (chainData != null) {
                if (allowSideChains) {
                    return true;
                } else {
                    CoinEntry entry = chainData.findEntry(coin);
                    return entry != null ? !entry.isSideChain() : false;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public void CoinExchangeAllUp(@Nonnull Container container) {
        if (!this.NoDataAvailable()) {
            for (ChainData chain : this.AllChainData()) {
                List<CoinEntry> entryList = chain.getAllEntries(false, ChainData.SORT_LOWEST_VALUE_FIRST);
                for (CoinEntry entry : entryList) {
                    this.CoinExchangeUp(container, entry.getCoin());
                }
                for (CoinEntry entry : entryList) {
                    this.CoinExchangeUp(container, entry.getCoin());
                }
                for (CoinEntry entry : entryList) {
                    this.CoinExchangeUp(container, entry.getCoin());
                }
            }
        }
    }

    @Override
    public void CoinExchangeUp(@Nonnull Container container, @Nonnull Item smallCoin) {
        if (!this.NoDataAvailable()) {
            ChainData chain = this.ChainDataOfCoin(smallCoin);
            if (chain != null) {
                Pair<CoinEntry, Integer> upperExchange = chain.getUpperExchange(smallCoin);
                if (upperExchange != null) {
                    Item largeCoin = ((CoinEntry) upperExchange.getFirst()).getCoin();
                    int smallCoinCount = (Integer) upperExchange.getSecond();
                    while (InventoryUtil.GetItemCount(container, smallCoin) >= smallCoinCount) {
                        InventoryUtil.RemoveItemCount(container, smallCoin, smallCoinCount);
                        ItemStack newCoinStack = new ItemStack(largeCoin, 1);
                        if (!InventoryUtil.PutItemStack(container, newCoinStack)) {
                            InventoryUtil.TryPutItemStack(container, new ItemStack(smallCoin, smallCoinCount));
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void CoinExchangeAllDown(@Nonnull Container container) {
        if (!this.NoDataAvailable()) {
            for (ChainData chain : this.AllChainData()) {
                List<CoinEntry> entryList = chain.getAllEntries(false, ChainData.SORT_HIGHEST_VALUE_FIRST);
                for (CoinEntry entry : entryList) {
                    this.CoinExchangeDown(container, entry.getCoin());
                }
                for (CoinEntry entry : entryList) {
                    this.CoinExchangeDown(container, entry.getCoin());
                }
            }
        }
    }

    @Override
    public void CoinExchangeDown(@Nonnull Container container, @Nonnull Item largeCoin) {
        if (!this.NoDataAvailable()) {
            ChainData chain = this.ChainDataOfCoin(largeCoin);
            if (chain != null) {
                Pair<CoinEntry, Integer> lowerExchange = chain.getLowerExchange(largeCoin);
                if (lowerExchange != null) {
                    Item smallCoin = ((CoinEntry) lowerExchange.getFirst()).getCoin();
                    int smallCoinCount = (Integer) lowerExchange.getSecond();
                    while (InventoryUtil.GetItemCount(container, largeCoin) > 0) {
                        InventoryUtil.RemoveItemCount(container, largeCoin, 1);
                        ItemStack newCoinStack = new ItemStack(smallCoin, smallCoinCount);
                        if (!InventoryUtil.PutItemStack(container, newCoinStack)) {
                            InventoryUtil.TryPutItemStack(container, new ItemStack(largeCoin, 1));
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void SortCoinsByValue(@Nonnull Container container) {
        InventoryUtil.MergeStacks(container);
        List<ItemStack> oldInventory = new ArrayList();
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) {
                oldInventory.add(container.getItem(i));
            }
        }
        container.m_6211_();
        oldInventory.sort(COIN_SORTER);
        int index = 0;
        while (!oldInventory.isEmpty()) {
            container.setItem(index++, (ItemStack) oldInventory.get(0));
            oldInventory.remove(0);
        }
    }

    private void onServerStart(@Nonnull ServerAboutToStartEvent event) {
        this.ReloadCoinDataFromFile();
    }

    private void onJoinServer(@Nonnull PlayerEvent.PlayerLoggedInEvent event) {
        LightmansCurrency.LogDebug("PlayerLoggedInEvent was called!");
        if (this.NoDataAvailable()) {
            this.ReloadCoinDataFromFile();
        }
        this.SyncCoinDataWith(LightmansCurrencyPacketHandler.getTarget(event.getEntity()));
    }

    @Override
    public void SyncCoinDataWith(@Nonnull PacketDistributor.PacketTarget target) {
        new SPacketSyncCoinData(this.getDataJson(this.loadedChains, false)).sendToTarget(target);
    }

    @Override
    public void HandleSyncPacket(@Nonnull SPacketSyncCoinData packet) {
        this.loadMoneyDataFromJson(packet.getJson());
    }

    private static class CoinSorter implements Comparator<ItemStack> {

        public int compare(ItemStack stack1, ItemStack stack2) {
            if (stack1.getItem() == stack2.getItem()) {
                return Integer.compare(stack2.getCount(), stack1.getCount());
            } else {
                ChainData chain1 = CoinAPI.API.ChainDataOfCoin(stack1);
                ChainData chain2 = CoinAPI.API.ChainDataOfCoin(stack2);
                if (chain1 == null && chain2 == null) {
                    return 0;
                } else if (chain2 == null) {
                    return 1;
                } else if (chain1 == null) {
                    return -1;
                } else if (chain1 != chain2) {
                    return chain2.getDisplayName().getString().compareToIgnoreCase(chain1.getDisplayName().getString());
                } else {
                    CoinEntry entry1 = chain1.findEntry(stack1);
                    CoinEntry entry2 = chain2.findEntry(stack2);
                    return Long.compare(entry2.getCoreValue(), entry1.getCoreValue());
                }
            }
        }
    }
}