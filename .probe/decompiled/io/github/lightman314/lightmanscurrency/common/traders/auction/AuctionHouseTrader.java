package io.github.lightman314.lightmanscurrency.common.traders.auction;

import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.events.AuctionHouseEvent;
import io.github.lightman314.lightmanscurrency.api.misc.IEasyTickable;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.ownership.builtin.FakeOwner;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TradeResult;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.TraderType;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.permissions.PermissionOption;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.common.menus.TraderMenu;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.auction.AuctionCreateTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.auction.AuctionStorageTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.auction.AuctionTradeCancelTab;
import io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata.AuctionTradeData;
import io.github.lightman314.lightmanscurrency.network.message.auction.SPacketStartBid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class AuctionHouseTrader extends TraderData implements IEasyTickable {

    public static final TraderType<AuctionHouseTrader> TYPE = new TraderType<>(new ResourceLocation("lightmanscurrency", "auction_house"), AuctionHouseTrader::new);

    public static final IconData ICON = IconData.of(new ResourceLocation("lightmanscurrency", "textures/gui/icons.png"), 96, 16);

    private final List<AuctionTradeData> trades = new ArrayList();

    Map<UUID, AuctionPlayerStorage> storage = new HashMap();

    public static boolean isEnabled() {
        return LCConfig.SERVER.auctionHouseEnabled.get();
    }

    public static boolean shouldShowOnTerminal() {
        return isEnabled() && LCConfig.SERVER.auctionHouseOnTerminal.get();
    }

    @Override
    public boolean showOnTerminal() {
        return shouldShowOnTerminal();
    }

    @Override
    public boolean isCreative() {
        return true;
    }

    private AuctionHouseTrader() {
        super(TYPE);
        this.getOwner().SetOwner(FakeOwner.of(LCText.GUI_TRADER_AUCTION_HOUSE_OWNER.get()));
    }

    @Nonnull
    @Override
    public MutableComponent getName() {
        return LCText.GUI_TRADER_AUCTION_HOUSE.get();
    }

    @Override
    public int getTradeCount() {
        return this.trades.size();
    }

    public AuctionTradeData getTrade(int tradeIndex) {
        try {
            return (AuctionTradeData) this.trades.get(tradeIndex);
        } catch (Exception var3) {
            return null;
        }
    }

    public boolean hasPersistentAuction(String id) {
        for (AuctionTradeData trade : this.trades) {
            if (trade.isPersistentID(id) && trade.isValid()) {
                return true;
            }
        }
        return false;
    }

    public int getTradeIndex(AuctionTradeData trade) {
        return this.trades.indexOf(trade);
    }

    @Override
    public void markTradesDirty() {
        this.markDirty(new Consumer[] { this::saveTrades });
    }

    public AuctionPlayerStorage getStorage(Player player) {
        return this.getStorage(PlayerReference.of(player));
    }

    public AuctionPlayerStorage getStorage(PlayerReference player) {
        if (player == null) {
            return null;
        } else {
            if (!this.storage.containsKey(player.id)) {
                this.storage.put(player.id, new AuctionPlayerStorage(player));
                this.markStorageDirty();
            }
            return (AuctionPlayerStorage) this.storage.get(player.id);
        }
    }

    public void markStorageDirty() {
        this.markDirty(new Consumer[] { this::saveStorage });
    }

    @Override
    public void tick() {
        long currentTime = System.currentTimeMillis();
        boolean changed = false;
        boolean canDelete = this.getUserCount() <= 0;
        for (int i = 0; i < this.trades.size(); i++) {
            AuctionTradeData trade = (AuctionTradeData) this.trades.get(i);
            if (trade.hasExpired(currentTime)) {
                trade.ExecuteTrade(this);
                changed = true;
            }
            if (canDelete && !trade.isValid()) {
                this.trades.remove(i);
                i--;
            }
        }
        if (changed) {
            this.markDirty(new Consumer[] { this::saveTrades });
            this.markDirty(new Consumer[] { this::saveStorage });
        }
    }

    @Override
    public int getPermissionLevel(PlayerReference player, String permission) {
        return !Objects.equals(permission, "openStorage") && !Objects.equals(permission, "editTrades") ? 0 : 1;
    }

    @Override
    public int getPermissionLevel(Player player, String permission) {
        return !Objects.equals(permission, "openStorage") && !Objects.equals(permission, "editTrades") ? 0 : 1;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        this.saveTrades(compound);
        this.saveStorage(compound);
    }

    @Override
    protected final void saveTrades(CompoundTag compound) {
        ListTag list = new ListTag();
        for (AuctionTradeData trade : this.trades) {
            list.add(trade.getAsNBT());
        }
        compound.put("Trades", list);
    }

    protected final void saveStorage(CompoundTag compound) {
        ListTag list = new ListTag();
        this.storage.forEach((player, storage) -> list.add(storage.save(new CompoundTag())));
        compound.put("StorageData", list);
    }

    @Override
    public void loadAdditional(CompoundTag compound) {
        if (compound.contains("Trades")) {
            this.trades.clear();
            ListTag tradeList = compound.getList("Trades", 10);
            for (int i = 0; i < tradeList.size(); i++) {
                this.trades.add(new AuctionTradeData(tradeList.getCompound(i)));
            }
        }
        if (compound.contains("StorageData")) {
            this.storage.clear();
            ListTag storageList = compound.getList("StorageData", 10);
            for (int i = 0; i < storageList.size(); i++) {
                AuctionPlayerStorage storageEntry = new AuctionPlayerStorage(storageList.getCompound(i));
                if (storageEntry.getOwner() != null) {
                    this.storage.put(storageEntry.getOwner().id, storageEntry);
                }
            }
        }
        this.getOwner().SetOwner(FakeOwner.of(LCText.GUI_TRADER_AUCTION_HOUSE_OWNER.get()));
    }

    @Override
    public void addTrade(Player requestor) {
    }

    @Override
    public void removeTrade(Player requestor) {
    }

    public void addTrade(AuctionTradeData trade, boolean persistent) {
        AuctionHouseEvent.AuctionEvent.CreateAuctionEvent.Pre e1 = new AuctionHouseEvent.AuctionEvent.CreateAuctionEvent.Pre(this, trade, persistent);
        if (!MinecraftForge.EVENT_BUS.post(e1)) {
            trade = e1.getAuction();
            trade.startTimer();
            if (trade.isValid()) {
                this.trades.add(trade);
                this.markTradesDirty();
                AuctionHouseEvent.AuctionEvent.CreateAuctionEvent.Post e2 = new AuctionHouseEvent.AuctionEvent.CreateAuctionEvent.Post(this, trade, persistent);
                MinecraftForge.EVENT_BUS.post(e2);
            } else {
                LightmansCurrency.LogError("Auction Trade is not fully valid. Unable to add it to the list.");
            }
        }
    }

    @Override
    public TradeResult ExecuteTrade(TradeContext context, int tradeIndex) {
        if (!context.hasPlayer()) {
            return TradeResult.FAIL_NOT_SUPPORTED;
        } else {
            new SPacketStartBid(this.getID(), tradeIndex).sendTo(context.getPlayer());
            return TradeResult.SUCCESS;
        }
    }

    public void makeBid(Player player, TraderMenu menu, int tradeIndex, MoneyValue bidAmount) {
        AuctionTradeData trade = this.getTrade(tradeIndex);
        if (trade != null) {
            if (!trade.hasExpired(System.currentTimeMillis())) {
                AuctionHouseEvent.AuctionEvent.AuctionBidEvent.Pre e1 = new AuctionHouseEvent.AuctionEvent.AuctionBidEvent.Pre(this, trade, player, bidAmount);
                if (!MinecraftForge.EVENT_BUS.post(e1)) {
                    bidAmount = e1.getBidAmount();
                    TradeContext tradeContext = menu.getContext(this);
                    MoneyView funds = tradeContext.getAvailableFunds();
                    if (funds.containsValue(bidAmount) && trade.tryMakeBid(this, player, bidAmount)) {
                        tradeContext.getPayment(bidAmount);
                        this.markDirty(new Consumer[] { this::saveTrades });
                        this.markDirty(new Consumer[] { this::saveStorage });
                        AuctionHouseEvent.AuctionEvent.AuctionBidEvent.Post e2 = new AuctionHouseEvent.AuctionEvent.AuctionBidEvent.Post(this, trade, player, bidAmount);
                        MinecraftForge.EVENT_BUS.post(e2);
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    public List<? extends TradeData> getTradeData() {
        return (List<? extends TradeData>) (this.trades == null ? new ArrayList() : this.trades);
    }

    @Override
    public IconData getIcon() {
        return ICON;
    }

    @Override
    public boolean canMakePersistent() {
        return false;
    }

    @Override
    public void saveAdditionalPersistentData(CompoundTag compound) {
    }

    @Override
    public void loadAdditionalPersistentData(CompoundTag data) {
    }

    @Override
    public Function<TradeData, Boolean> getStorageDisplayFilter(@Nonnull ITraderStorageMenu menu) {
        return trade -> {
            if (trade instanceof AuctionTradeData at && at.isOwner(menu.getPlayer()) && at.isValid()) {
                return true;
            }
            return false;
        };
    }

    @Override
    public void initStorageTabs(@Nonnull ITraderStorageMenu menu) {
        menu.setTab(1, new AuctionStorageTab(menu));
        menu.setTab(2, new AuctionTradeCancelTab(menu));
        menu.setTab(3, new AuctionCreateTab(menu));
        menu.clearTab(10);
        menu.clearTab(11);
        menu.clearTab(12);
        menu.clearTab(50);
        menu.clearTab(100);
        menu.clearTab(101);
    }

    @Override
    public boolean shouldRemove(MinecraftServer server) {
        return false;
    }

    @Override
    public void getAdditionalContents(List<ItemStack> contents) {
    }

    @Override
    protected MutableComponent getDefaultName() {
        return this.getName();
    }

    @Override
    public boolean hasValidTrade() {
        return true;
    }

    @Override
    protected void saveAdditionalToJson(JsonObject json) {
    }

    @Override
    protected void loadAdditionalFromJson(JsonObject json) {
    }

    @Override
    protected boolean allowAdditionalUpgradeType(UpgradeType type) {
        return false;
    }

    @Override
    public int getTradeStock(int tradeIndex) {
        return 0;
    }

    @Override
    protected void addPermissionOptions(List<PermissionOption> options) {
    }

    @Override
    protected void modifyDefaultAllyPermissions(Map<String, Integer> defaultValues) {
        defaultValues.clear();
    }

    @Override
    protected void appendTerminalInfo(@Nonnull List<Component> list, @Nullable Player player) {
        int auctionCount = 0;
        for (AuctionTradeData auction : this.trades) {
            if (auction.isValid() && auction.isActive()) {
                auctionCount++;
            }
        }
        list.add(LCText.TOOLTIP_NETWORK_TERMINAL_AUCTION_HOUSE.get(auctionCount));
    }

    @Override
    public int getTerminalTextColor() {
        int auctionCount = 0;
        for (AuctionTradeData auction : this.trades) {
            if (auction.isValid() && auction.isActive()) {
                auctionCount++;
            }
        }
        return auctionCount > 0 ? 65280 : 4210752;
    }
}