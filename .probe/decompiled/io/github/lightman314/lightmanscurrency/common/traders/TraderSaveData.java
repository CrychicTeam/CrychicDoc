package io.github.lightman314.lightmanscurrency.common.traders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.events.TraderEvent;
import io.github.lightman314.lightmanscurrency.api.misc.IEasyTickable;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.client.data.ClientTraderData;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.EjectionData;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.EjectionSaveData;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxSaveData;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import io.github.lightman314.lightmanscurrency.common.traders.auction.PersistentAuctionData;
import io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata.AuctionTradeData;
import io.github.lightman314.lightmanscurrency.network.LightmansCurrencyPacketHandler;
import io.github.lightman314.lightmanscurrency.network.message.data.trader.SPacketClearClientTraders;
import io.github.lightman314.lightmanscurrency.network.message.data.trader.SPacketMessageRemoveClientTrader;
import io.github.lightman314.lightmanscurrency.network.message.data.trader.SPacketUpdateClientTrader;
import io.github.lightman314.lightmanscurrency.util.FileUtil;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = "lightmanscurrency")
public class TraderSaveData extends SavedData {

    public static final String PERSISTENT_TRADER_FILENAME = "config/lightmanscurrency/PersistentTraders.json";

    public static final String PERSISTENT_TRADER_SECTION = "Traders";

    public static final String PERSISTENT_AUCTION_SECTION = "Auctions";

    private int cleanTick = 0;

    private long nextID = 0L;

    private final Map<Long, TraderData> traderData = new HashMap();

    private final Map<String, TraderSaveData.PersistentData> persistentTraderData = new HashMap();

    private final List<PersistentAuctionData> persistentAuctionData = new ArrayList();

    private final List<IEasyTickable> tickers = new ArrayList();

    private JsonObject persistentTraderJson = new JsonObject();

    private void validateAuctionHouse() {
        if (!LCConfig.SERVER.auctionHouseEnabled.get()) {
            LightmansCurrency.LogInfo("Will not create or validate the auction house as the auction house is disabled.");
        } else {
            AtomicBoolean hasAuctionHouse = new AtomicBoolean(false);
            this.traderData.forEach((id, data) -> {
                if (data instanceof AuctionHouseTrader) {
                    hasAuctionHouse.set(true);
                }
            });
            if (!hasAuctionHouse.get()) {
                AuctionHouseTrader ah = AuctionHouseTrader.TYPE.create();
                ah.setCreative(null, true);
                long traderID = this.getNextID();
                ah.setID(traderID);
                LightmansCurrency.LogInfo("Successfully created an auction house trader with id '" + traderID + "'!");
                this.AddTraderInternal(traderID, ah);
            }
        }
    }

    private long getNextID() {
        long id = this.nextID++;
        this.m_77762_();
        return id;
    }

    public TraderSaveData() {
        this.validateAuctionHouse();
        this.loadPersistentTraders();
    }

    public TraderSaveData(CompoundTag compound) {
        this.nextID = compound.getLong("NextID");
        LightmansCurrency.LogInfo("Loaded NextID (" + this.nextID + ") from tag.");
        ListTag traderData = compound.getList("TraderData", 10);
        for (int i = 0; i < traderData.size(); i++) {
            try {
                CompoundTag traderTag = traderData.getCompound(i);
                TraderData trader = TraderData.Deserialize(false, traderTag);
                if (trader != null) {
                    this.AddTraderInternal(trader.getID(), trader);
                } else {
                    LightmansCurrency.LogError("Error loading TraderData entry at index " + i);
                }
            } catch (Throwable var11) {
                LightmansCurrency.LogError("Error loading TraderData", var11);
            }
        }
        ListTag persistentData = compound.getList("PersistentData", 10);
        for (int i = 0; i < persistentData.size(); i++) {
            try {
                CompoundTag c = persistentData.getCompound(i);
                String name = c.getString("Name");
                long id = c.getLong("ID");
                CompoundTag tag = c.getCompound("Tag");
                this.persistentTraderData.put(name, new TraderSaveData.PersistentData(id, tag));
            } catch (Throwable var10) {
                LightmansCurrency.LogError("Error loading Persistent Data", var10);
            }
        }
        this.validateAuctionHouse();
        this.loadPersistentTraders();
    }

    @NotNull
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putLong("NextID", this.nextID);
        ListTag traderData = new ListTag();
        this.traderData.forEach((id, trader) -> {
            if (trader.isPersistent()) {
                try {
                    this.putPersistentTag(trader.getPersistentID(), trader.savePersistentData());
                } catch (Throwable var6) {
                    LightmansCurrency.LogError("Error saving persistent trader data:", var6);
                }
            } else {
                try {
                    traderData.add(trader.save());
                } catch (Throwable var5) {
                    LightmansCurrency.LogError("Error saving trader data:", var5);
                }
            }
        });
        compound.put("TraderData", traderData);
        ListTag persistentData = new ListTag();
        this.persistentTraderData.forEach((id, data) -> {
            try {
                CompoundTag c = new CompoundTag();
                c.putString("Name", id);
                c.putLong("ID", data.id);
                c.put("Tag", data.tag);
                persistentData.add(c);
            } catch (Throwable var4) {
                LightmansCurrency.LogError("Error saving Persistent Data:", var4);
            }
        });
        compound.put("PersistentData", persistentData);
        return compound;
    }

    private long getPersistentID(String traderID) {
        return this.persistentTraderData.containsKey(traderID) ? ((TraderSaveData.PersistentData) this.persistentTraderData.get(traderID)).id : -1L;
    }

    private void putPersistentID(String traderID, long id) {
        if (this.persistentTraderData.containsKey(traderID)) {
            ((TraderSaveData.PersistentData) this.persistentTraderData.get(traderID)).id = id;
        } else {
            this.persistentTraderData.put(traderID, new TraderSaveData.PersistentData(id, new CompoundTag()));
        }
        this.m_77762_();
    }

    @Deprecated
    public static long CheckOldPersistentID(String traderID) {
        TraderSaveData tsd = get();
        if (tsd != null) {
            long id = tsd.getPersistentID(traderID);
            if (id < 0L) {
                id = tsd.getNextID();
                tsd.putPersistentID(traderID, id);
            }
            return id;
        } else {
            return -1L;
        }
    }

    private CompoundTag getPersistentTag(String traderID) {
        return this.persistentTraderData.containsKey(traderID) ? ((TraderSaveData.PersistentData) this.persistentTraderData.get(traderID)).tag : new CompoundTag();
    }

    private void putPersistentTag(String traderID, CompoundTag tag) {
        if (this.persistentTraderData.containsKey(traderID)) {
            ((TraderSaveData.PersistentData) this.persistentTraderData.get(traderID)).tag = tag == null ? new CompoundTag() : tag;
        } else {
            this.persistentTraderData.put(traderID, new TraderSaveData.PersistentData(-1L, tag == null ? new CompoundTag() : tag));
        }
        this.m_77762_();
    }

    @Deprecated
    public static void GiveOldPersistentTag(String traderID, CompoundTag tag) {
        TraderSaveData tsd = get();
        if (tsd != null) {
            tsd.putPersistentTag(traderID, tag);
            for (TraderData pt : tsd.traderData.values().stream().filter(trader -> trader.isPersistent() && trader.getPersistentID().equals(traderID)).toList()) {
                pt.loadPersistentData(tsd.getPersistentTag(traderID));
                MarkTraderDirty(pt.save());
            }
        }
    }

    public static JsonObject getPersistentTraderJson() {
        TraderSaveData tsd = get();
        return tsd != null ? tsd.persistentTraderJson : new JsonObject();
    }

    public static JsonArray getPersistentTraderJson(String section) {
        JsonObject json = getPersistentTraderJson();
        if (json != null) {
            if (!json.has(section)) {
                JsonArray newSection = new JsonArray();
                json.add(section, newSection);
            }
            if (json.get(section).isJsonArray()) {
                return json.get(section).getAsJsonArray();
            }
            LightmansCurrency.LogError("Cannot get Persistent Data section '" + section + "' as it is not a JsonArray.");
        }
        return null;
    }

    public static void setPersistentTraderJson(JsonObject newData) {
        TraderSaveData tsd = get();
        if (tsd != null) {
            File ptf = new File("config/lightmanscurrency/PersistentTraders.json");
            try {
                tsd.loadPersistentTrader(newData);
            } catch (Exception var4) {
                LightmansCurrency.LogError("Error loading modified Persistent Trader Data. Ignoring request.", var4);
                return;
            }
            tsd.persistentTraderJson = newData;
            tsd.savePersistentTraderJson(ptf);
            tsd.resendTraderData();
        }
    }

    public static void setPersistentTraderSection(String section, JsonArray newData) {
        JsonObject json = getPersistentTraderJson();
        json.add(section, newData);
        setPersistentTraderJson(json);
    }

    public static void ReloadPersistentTraders() {
        TraderSaveData tsd = get();
        if (tsd != null) {
            tsd.loadPersistentTraders();
            tsd.resendTraderData();
        }
    }

    private void loadPersistentTraders() {
        File ptf = new File("config/lightmanscurrency/PersistentTraders.json");
        if (!ptf.exists()) {
            this.persistentTraderJson = generateDefaultPersistentTraderJson();
            this.savePersistentTraderJson(ptf);
        }
        try {
            this.persistentTraderJson = GsonHelper.parse(Files.readString(ptf.toPath()));
            LightmansCurrency.LogDebug("Loading PersistentTraders.json\n" + FileUtil.GSON.toJson(this.persistentTraderJson));
            this.loadPersistentTrader(this.persistentTraderJson);
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error loading Persistent Traders.", var3);
            this.persistentTraderJson = generateDefaultPersistentTraderJson();
        }
    }

    private static JsonObject generateDefaultPersistentTraderJson() {
        JsonObject fileData = new JsonObject();
        JsonArray traderList = new JsonArray();
        fileData.add("Traders", traderList);
        JsonArray auctions = new JsonArray();
        fileData.add("Auctions", auctions);
        return fileData;
    }

    private void loadPersistentTrader(JsonObject fileData) throws JsonSyntaxException, ResourceLocationException {
        boolean hadNone = true;
        if (fileData.has("Traders")) {
            hadNone = false;
            List<Long> removeTraderList = new ArrayList();
            this.traderData.forEach((idx, trader) -> {
                if (trader.isPersistent()) {
                    if (trader instanceof IEasyTickable t) {
                        this.tickers.remove(t);
                    }
                    this.putPersistentTag(trader.getPersistentID(), trader.savePersistentData());
                    removeTraderList.add(idx);
                }
            });
            for (long id : removeTraderList) {
                this.traderData.remove(id);
            }
            List<String> loadedIDs = new ArrayList();
            JsonArray traderList = fileData.getAsJsonArray("Traders");
            for (int i = 0; i < traderList.size(); i++) {
                try {
                    JsonObject traderTag = GsonHelper.convertToJsonObject(traderList.get(i), "Traders[" + i + "]");
                    String traderID = GsonHelper.getAsString(traderTag, "id", GsonHelper.getAsString(traderTag, "ID"));
                    if (loadedIDs.contains(traderID)) {
                        throw new JsonSyntaxException("Trader with id '" + traderID + "' already exists. Cannot have duplicate ids.");
                    }
                    if (traderID.isBlank()) {
                        throw new JsonSyntaxException("Trader cannot have a blank id!");
                    }
                    TraderData data = TraderData.Deserialize(traderTag);
                    data.loadPersistentData(this.getPersistentTag(traderID));
                    long id = this.getPersistentID(traderID);
                    if (id < 0L) {
                        id = this.getNextID();
                        this.putPersistentID(traderID, id);
                        this.m_77762_();
                        LightmansCurrency.LogInfo("Generated new ID for persistent trader '" + traderID + "' (" + id + ")");
                    }
                    data.makePersistent(id, traderID);
                    this.AddTraderInternal(id, data);
                    loadedIDs.add(traderID);
                    LightmansCurrency.LogInfo("Successfully loaded persistent trader '" + traderID + "' with ID " + id + ".");
                } catch (ResourceLocationException | JsonSyntaxException var13) {
                    LightmansCurrency.LogError("Error loading Persistent Trader at index " + i, var13);
                }
            }
        }
        if (fileData.has("Auctions")) {
            hadNone = false;
            this.persistentAuctionData.clear();
            List<String> loadedIDs = new ArrayList();
            JsonArray auctionList = fileData.getAsJsonArray("Auctions");
            for (int i = 0; i < auctionList.size(); i++) {
                try {
                    JsonObject auctionTag = auctionList.get(i).getAsJsonObject();
                    PersistentAuctionData data = PersistentAuctionData.load(auctionTag);
                    if (loadedIDs.contains(data.id)) {
                        throw new JsonSyntaxException("Auction with id '" + data.id + "' already exists. Cannot have duplicate ids.");
                    }
                    loadedIDs.add(data.id);
                    this.persistentAuctionData.add(data);
                    LightmansCurrency.LogInfo("Successfully loaded persistent auction '" + data.id + "'");
                } catch (ResourceLocationException | JsonSyntaxException var12) {
                    LightmansCurrency.LogError("Error loading Persistent Auction at index " + i, var12);
                }
            }
        }
        if (hadNone) {
            throw new JsonSyntaxException("Json Data has no 'Traders' or 'Auctions' entry.");
        }
    }

    private void savePersistentTraderJson(File ptf) {
        File dir = new File(ptf.getParent());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (dir.exists()) {
            try {
                ptf.createNewFile();
                String jsonString = FileUtil.GSON.toJson(this.persistentTraderJson);
                FileUtil.writeStringToFile(ptf, jsonString);
                LightmansCurrency.LogInfo("PersistentTraders.json does not exist. Creating a fresh copy.");
            } catch (Throwable var4) {
                LightmansCurrency.LogError("Error attempting to create 'persistentTraders.json' file.", var4);
            }
        }
    }

    private static TraderSaveData get() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            ServerLevel level = server.getLevel(Level.OVERWORLD);
            if (level != null) {
                return level.getDataStorage().computeIfAbsent(TraderSaveData::new, TraderSaveData::new, "lightmanscurrency_trader_data");
            }
        }
        return null;
    }

    public static void MarkTraderDirty(CompoundTag updateMessage) {
        TraderSaveData tsd = get();
        if (tsd != null) {
            tsd.m_77762_();
            new SPacketUpdateClientTrader(updateMessage).sendToAll();
        }
    }

    @Deprecated
    public static long RegisterOldTrader(TraderData newTrader) {
        if (newTrader instanceof AuctionHouseTrader) {
            TraderSaveData tsd = get();
            if (tsd != null) {
                for (TraderData trader : tsd.traderData.values()) {
                    if (trader instanceof AuctionHouseTrader) {
                        long id = trader.getID();
                        tsd.AddTraderInternal(id, newTrader);
                        return id;
                    }
                }
            }
        }
        return RegisterTrader(newTrader, null);
    }

    public static long RegisterTrader(TraderData newTrader, @Nullable Player player) {
        TraderSaveData tsd = get();
        if (tsd != null) {
            long newID = tsd.getNextID();
            tsd.AddTraderInternal(newID, newTrader);
            if (newTrader.shouldAlwaysShowOnTerminal() && player != null) {
                MinecraftForge.EVENT_BUS.post(new TraderEvent.CreateNetworkTraderEvent(newID, player));
            }
            return newID;
        } else {
            return -1L;
        }
    }

    private void AddTraderInternal(long traderID, TraderData trader) {
        trader.setID(traderID);
        this.traderData.put(traderID, trader.allowMarkingDirty());
        this.m_77762_();
        try {
            trader.OnRegisteredToOffice();
        } catch (Throwable var5) {
            LightmansCurrency.LogError("Error handling Trader-OnRegistration function!", var5);
        }
        new SPacketUpdateClientTrader(trader.save()).sendToAll();
        if (trader instanceof IEasyTickable t) {
            this.tickers.add(t);
        }
    }

    public static void DeleteTrader(long traderID) {
        TraderSaveData tsd = get();
        if (tsd != null && tsd.traderData.containsKey(traderID)) {
            TraderData trader = (TraderData) tsd.traderData.get(traderID);
            TaxSaveData.GetAllTaxEntries(false).forEach(e -> e.TaxableWasRemoved(trader));
            tsd.traderData.remove(traderID);
            if (trader instanceof IEasyTickable t) {
                tsd.tickers.remove(t);
            }
            tsd.m_77762_();
            new SPacketMessageRemoveClientTrader(traderID).sendToAll();
            if (trader.shouldAlwaysShowOnTerminal()) {
                MinecraftForge.EVENT_BUS.post(new TraderEvent.RemoveNetworkTraderEvent(traderID, trader));
            }
        }
    }

    public static List<TraderData> GetAllTraders(boolean isClient) {
        if (isClient) {
            return ClientTraderData.GetAllTraders();
        } else {
            TraderSaveData tsd = get();
            return tsd != null ? new ArrayList(tsd.traderData.values()) : new ArrayList();
        }
    }

    public static List<TraderData> GetAllTerminalTraders(boolean isClient) {
        return (List<TraderData>) GetAllTraders(isClient).stream().filter(TraderData::showOnTerminal).collect(Collectors.toList());
    }

    public static TraderData GetTrader(boolean isClient, long traderID) {
        if (isClient) {
            return ClientTraderData.GetTrader(traderID);
        } else {
            TraderSaveData tsd = get();
            return tsd != null ? (TraderData) tsd.traderData.get(traderID) : null;
        }
    }

    public static TraderData GetTrader(boolean isClient, String persistentTraderID) {
        if (isClient) {
            List<TraderData> validTraders = ClientTraderData.GetAllTraders().stream().filter(t -> t.getPersistentID().equals(persistentTraderID)).toList();
            if (!validTraders.isEmpty()) {
                return (TraderData) validTraders.get(0);
            }
        } else {
            TraderSaveData tsd = get();
            if (tsd != null) {
                return (TraderData) tsd.traderData.get(tsd.getPersistentID(persistentTraderID));
            }
        }
        return null;
    }

    public static TraderData GetAuctionHouse(boolean isClient) {
        if (isClient) {
            List<TraderData> validTraders = ClientTraderData.GetAllTraders().stream().filter(t -> t instanceof AuctionHouseTrader).toList();
            if (!validTraders.isEmpty()) {
                return (TraderData) validTraders.get(0);
            }
        } else {
            TraderSaveData tsd = get();
            if (tsd != null) {
                List<TraderData> validTraders = tsd.traderData.values().stream().filter(t -> t instanceof AuctionHouseTrader).toList();
                if (!validTraders.isEmpty()) {
                    return (TraderData) validTraders.get(0);
                }
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side.isServer()) {
            MinecraftServer server = event.getServer();
            if (server != null) {
                TraderSaveData tsd = get();
                if (tsd != null) {
                    if (tsd.cleanTick++ >= 1200 && event.haveTime()) {
                        tsd.cleanTick = 0;
                        List<TraderData> remove = new ArrayList();
                        for (TraderData traderData : new ArrayList(tsd.traderData.values())) {
                            if (traderData.shouldRemove(server)) {
                                remove.add(traderData);
                            }
                        }
                        for (TraderData traderDatax : remove) {
                            if (traderDatax instanceof IEasyTickable t) {
                                tsd.tickers.remove(t);
                            }
                            tsd.traderData.remove(traderDatax.getID());
                            try {
                                Level level = server.getLevel(traderDatax.getLevel());
                                BlockPos pos = traderDatax.getPos();
                                EjectionData e = EjectionData.create(level, pos, null, traderDatax, false);
                                EjectionSaveData.HandleEjectionData((Level) Objects.requireNonNull(level), pos, e);
                            } catch (NullPointerException var9) {
                                LightmansCurrency.LogError("Error deleting missing trader.", var9);
                            }
                            new SPacketMessageRemoveClientTrader(traderDatax.getID()).sendToAll();
                        }
                    }
                    if (server.getTickCount() % 20 == 0 && !tsd.persistentAuctionData.isEmpty()) {
                        List<TraderData> traders = tsd.traderData.values().stream().toList();
                        AuctionHouseTrader ah = null;
                        for (int i = 0; i < traders.size() && ah == null; i++) {
                            if (traders.get(i) instanceof AuctionHouseTrader) {
                                ah = (AuctionHouseTrader) traders.get(i);
                            }
                        }
                        if (ah != null) {
                            for (PersistentAuctionData pad : tsd.persistentAuctionData) {
                                if (!ah.hasPersistentAuction(pad.id)) {
                                    AuctionTradeData trade = pad.createAuction();
                                    if (trade != null) {
                                        ah.addTrade(trade, true);
                                        LightmansCurrency.LogInfo("Successfully added Persistent Auction '" + pad.id + "' into the auction house.");
                                    }
                                }
                            }
                        }
                    }
                    tsd.tickers.forEach(IEasyTickable::tick);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        TraderSaveData tsd = get();
        if (tsd != null) {
            PacketDistributor.PacketTarget target = LightmansCurrencyPacketHandler.getTarget(event.getEntity());
            SPacketClearClientTraders.INSTANCE.sendToTarget(target);
            tsd.traderData.forEach((id, trader) -> new SPacketUpdateClientTrader(trader.save()).sendToTarget(target));
        }
    }

    private void resendTraderData() {
        PacketDistributor.PacketTarget target = PacketDistributor.ALL.noArg();
        SPacketClearClientTraders.INSTANCE.sendToTarget(target);
        this.traderData.forEach((id, trader) -> new SPacketUpdateClientTrader(trader.save()).sendToTarget(target));
    }

    private static class PersistentData {

        public long id;

        public CompoundTag tag;

        public PersistentData(long id, CompoundTag tag) {
            this.id = id;
            this.tag = tag == null ? new CompoundTag() : tag;
        }
    }
}