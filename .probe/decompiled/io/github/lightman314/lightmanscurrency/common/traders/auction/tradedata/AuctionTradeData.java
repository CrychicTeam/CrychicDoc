package io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata;

import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.api.events.AuctionHouseEvent;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationSaveData;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeDirection;
import io.github.lightman314.lightmanscurrency.api.traders.trade.client.TradeRenderManager;
import io.github.lightman314.lightmanscurrency.api.traders.trade.comparison.TradeComparisonResult;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.trades_basic.BasicTradeEditTab;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseBidNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseBuyerNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseCancelNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseSellerNobidNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.auction.AuctionHouseSellerNotification;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionPlayerStorage;
import io.github.lightman314.lightmanscurrency.common.traders.auction.PersistentAuctionData;
import io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata.client.AuctionTradeButtonRenderer;
import io.github.lightman314.lightmanscurrency.util.FileUtil;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.ItemHandlerHelper;

public class AuctionTradeData extends TradeData {

    private boolean cancelled;

    private String persistentID = "";

    MoneyValue lastBidAmount = MoneyValue.empty();

    PlayerReference lastBidPlayer = null;

    MoneyValue minBidDifference = MoneyValue.empty();

    PlayerReference tradeOwner;

    long startTime = 0L;

    long duration = 0L;

    List<ItemStack> auctionItems = new ArrayList();

    public static long GetMinimumDuration() {
        return LCConfig.SERVER.auctionHouseDurationMin.get() > 0 ? 86400000L * (long) LCConfig.SERVER.auctionHouseDurationMin.get().intValue() : 3600000L;
    }

    public static long GetDefaultDuration() {
        return LCConfig.SERVER.auctionHouseDurationMin.get() > 0 ? 86400000L * (long) LCConfig.SERVER.auctionHouseDurationMin.get().intValue() : 86400000L;
    }

    public boolean hasBid() {
        return this.lastBidPlayer != null;
    }

    public boolean isPersistentID(String id) {
        return this.persistentID.equals(id);
    }

    public MoneyValue getLastBidAmount() {
        return this.lastBidAmount;
    }

    public PlayerReference getLastBidPlayer() {
        return this.lastBidPlayer;
    }

    public void setStartingBid(@Nonnull MoneyValue amount) {
        if (!this.isActive()) {
            this.lastBidAmount = amount;
            if (!this.minBidDifference.sameType(this.lastBidAmount)) {
                this.minBidDifference = this.lastBidAmount.getSmallestValue();
            }
        }
    }

    public MoneyValue getMinBidDifference() {
        return this.minBidDifference != null && this.lastBidAmount.sameType(this.minBidDifference) ? this.minBidDifference : (this.minBidDifference = this.lastBidAmount.getSmallestValue());
    }

    public void setMinBidDifferent(@Nonnull MoneyValue amount) {
        if (!this.isActive()) {
            if (this.lastBidAmount.sameType(amount)) {
                this.minBidDifference = amount;
                if (this.minBidDifference.isEmpty()) {
                    this.minBidDifference = this.lastBidAmount.getSmallestValue();
                }
            }
        }
    }

    public PlayerReference getOwner() {
        return this.tradeOwner;
    }

    public boolean isOwner(Player player) {
        return this.tradeOwner != null && this.tradeOwner.is(player) || LCAdminMode.isAdminPlayer(player);
    }

    public void setDuration(long duration) {
        if (!this.isActive()) {
            this.duration = Math.max(GetMinimumDuration(), duration);
        }
    }

    @Override
    public int getStock(@Nonnull TradeContext context) {
        return this.isValid() ? 1 : 0;
    }

    public List<ItemStack> getAuctionItems() {
        return this.auctionItems;
    }

    public void setAuctionItems(Container auctionItems) {
        if (!this.isActive()) {
            this.auctionItems.clear();
            for (int i = 0; i < auctionItems.getContainerSize(); i++) {
                ItemStack stack = auctionItems.getItem(i);
                if (!stack.isEmpty()) {
                    this.auctionItems.add(stack.copy());
                }
            }
        }
    }

    public AuctionTradeData(Player owner) {
        super(false);
        this.tradeOwner = PlayerReference.of(owner);
        this.setDuration(GetDefaultDuration());
    }

    public AuctionTradeData(CompoundTag compound) {
        super(false);
        this.loadFromNBT(compound);
    }

    public AuctionTradeData(PersistentAuctionData data) {
        super(false);
        this.persistentID = data.id;
        this.setDuration(data.duration);
        this.auctionItems = data.getAuctionItems();
        this.setStartingBid(data.getStartingBid());
        this.setMinBidDifferent(data.getMinimumBidDifference());
    }

    public boolean isActive() {
        return this.startTime != 0L && !this.cancelled;
    }

    @Override
    public boolean isValid() {
        if (this.cancelled) {
            return false;
        } else if (this.auctionItems.isEmpty()) {
            return false;
        } else if (this.isActive() && this.hasExpired(TimeUtil.getCurrentTime())) {
            return false;
        } else {
            return this.getMinBidDifference().isEmpty() ? false : !this.lastBidAmount.isEmpty();
        }
    }

    public void startTimer() {
        if (!this.isActive()) {
            this.startTime = TimeUtil.getCurrentTime();
        }
    }

    public long getRemainingTime(long currentTime) {
        return !this.isActive() ? this.duration : Math.max(0L, this.startTime + this.duration - currentTime);
    }

    public boolean hasExpired(long time) {
        return this.isActive() ? time >= this.startTime + this.duration : false;
    }

    public boolean tryMakeBid(AuctionHouseTrader trader, Player player, MoneyValue amount) {
        if (!this.validateBidAmount(amount)) {
            return false;
        } else {
            PlayerReference oldBidder = this.lastBidPlayer;
            if (this.lastBidPlayer != null) {
                AuctionPlayerStorage storage = trader.getStorage(this.lastBidPlayer);
                storage.giveMoney(this.lastBidAmount);
                trader.markStorageDirty();
            }
            this.lastBidPlayer = PlayerReference.of(player);
            this.lastBidAmount = amount;
            if (oldBidder != null) {
                NotificationSaveData.PushNotification(oldBidder.id, new AuctionHouseBidNotification(this));
            }
            return true;
        }
    }

    public boolean validateBidAmount(@Nonnull MoneyValue amount) {
        MoneyValue minAmount = this.getMinNextBid();
        return amount.containsValue(minAmount);
    }

    public MoneyValue getMinNextBid() {
        return this.lastBidPlayer == null ? this.lastBidAmount : this.lastBidAmount.addValue(this.minBidDifference);
    }

    public void ExecuteTrade(AuctionHouseTrader trader) {
        if (!this.cancelled) {
            this.cancelled = true;
            AuctionHouseEvent.AuctionEvent.AuctionCompletedEvent event = new AuctionHouseEvent.AuctionEvent.AuctionCompletedEvent(trader, this);
            MinecraftForge.EVENT_BUS.post(event);
            if (this.lastBidPlayer != null) {
                AuctionPlayerStorage buyerStorage = trader.getStorage(this.lastBidPlayer);
                for (ItemStack reward : event.getItems()) {
                    buyerStorage.giveItem(reward);
                }
                if (this.tradeOwner != null) {
                    AuctionPlayerStorage sellerStorage = trader.getStorage(this.tradeOwner);
                    sellerStorage.giveMoney(event.getPayment());
                }
                NotificationSaveData.PushNotification(this.lastBidPlayer.id, new AuctionHouseBuyerNotification(this));
                if (this.tradeOwner != null) {
                    NotificationSaveData.PushNotification(this.tradeOwner.id, new AuctionHouseSellerNotification(this));
                }
            } else if (this.tradeOwner != null) {
                AuctionPlayerStorage sellerStorage = trader.getStorage(this.tradeOwner);
                for (ItemStack item : event.getItems()) {
                    sellerStorage.giveItem(item);
                }
                NotificationSaveData.PushNotification(this.tradeOwner.id, new AuctionHouseSellerNobidNotification(this));
            }
        }
    }

    public void CancelTrade(AuctionHouseTrader trader, boolean giveToPlayer, Player player) {
        if (!this.cancelled) {
            this.cancelled = true;
            if (this.lastBidPlayer != null) {
                AuctionPlayerStorage buyerStorage = trader.getStorage(this.lastBidPlayer);
                buyerStorage.giveMoney(this.lastBidAmount);
                NotificationSaveData.PushNotification(this.lastBidPlayer.id, new AuctionHouseCancelNotification(this));
            }
            if (giveToPlayer) {
                for (ItemStack stack : this.auctionItems) {
                    ItemHandlerHelper.giveItemToPlayer(player, stack);
                }
            } else if (this.tradeOwner != null) {
                AuctionPlayerStorage sellerStorage = trader.getStorage(this.tradeOwner);
                for (ItemStack stack : this.auctionItems) {
                    sellerStorage.giveItem(stack);
                }
            }
            AuctionHouseEvent.AuctionEvent.CancelAuctionEvent event = new AuctionHouseEvent.AuctionEvent.CancelAuctionEvent(trader, this, player);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    @Override
    public CompoundTag getAsNBT() {
        CompoundTag compound = new CompoundTag();
        ListTag itemList = new ListTag();
        for (ItemStack auctionItem : this.auctionItems) {
            itemList.add(auctionItem.save(new CompoundTag()));
        }
        compound.put("SellItems", itemList);
        compound.put("LastBid", this.lastBidAmount.save());
        if (this.lastBidPlayer != null) {
            compound.put("LastBidPlayer", this.lastBidPlayer.save());
        }
        if (this.minBidDifference != null) {
            compound.put("MinBid", this.minBidDifference.save());
        }
        compound.putLong("StartTime", this.startTime);
        compound.putLong("Duration", this.duration);
        if (this.tradeOwner != null) {
            compound.put("TradeOwner", this.tradeOwner.save());
        }
        compound.putBoolean("Cancelled", this.cancelled);
        if (!this.persistentID.isBlank()) {
            compound.putString("PersistentID", this.persistentID);
        }
        return compound;
    }

    public JsonObject saveToJson(JsonObject json) {
        for (int i = 0; i < this.auctionItems.size(); i++) {
            json.add("Item" + (i + 1), FileUtil.convertItemStack((ItemStack) this.auctionItems.get(i)));
        }
        json.addProperty("Duration", this.duration);
        json.add("StartingBid", this.lastBidAmount.toJson());
        json.add("MinimumBid", this.minBidDifference.toJson());
        return json;
    }

    @Override
    public void loadFromNBT(CompoundTag compound) {
        ListTag itemList = compound.getList("SellItems", 10);
        this.auctionItems.clear();
        for (int i = 0; i < itemList.size(); i++) {
            ItemStack stack = ItemStack.of(itemList.getCompound(i));
            if (!stack.isEmpty()) {
                this.auctionItems.add(stack);
            }
        }
        this.lastBidAmount = MoneyValue.safeLoad(compound, "LastBid");
        if (compound.contains("LastBidPlayer")) {
            this.lastBidPlayer = PlayerReference.load(compound.getCompound("LastBidPlayer"));
        } else {
            this.lastBidPlayer = null;
        }
        this.minBidDifference = MoneyValue.safeLoad(compound, "MinBid");
        this.startTime = compound.getLong("StartTime");
        this.duration = compound.getLong("Duration");
        if (compound.contains("TradeOwner", 10)) {
            this.tradeOwner = PlayerReference.load(compound.getCompound("TradeOwner"));
        }
        this.cancelled = compound.getBoolean("Cancelled");
        if (compound.contains("PersistentID", 8)) {
            this.persistentID = compound.getString("PersistentID");
        }
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    public TradeRenderManager<?> getButtonRenderer() {
        return new AuctionTradeButtonRenderer(this);
    }

    @Override
    public void OnInputDisplayInteraction(@Nonnull BasicTradeEditTab tab, Consumer<LazyPacketData.Builder> clientHandler, int index, int button, @Nonnull ItemStack heldItem) {
        this.openCancelAuctionTab(tab);
    }

    @Override
    public void OnOutputDisplayInteraction(@Nonnull BasicTradeEditTab tab, Consumer<LazyPacketData.Builder> clientHandler, int index, int button, @Nonnull ItemStack heldItem) {
        this.openCancelAuctionTab(tab);
    }

    @Override
    public void OnInteraction(@Nonnull BasicTradeEditTab tab, Consumer<LazyPacketData.Builder> clientHandler, int mouseX, int mouseY, int button, @Nonnull ItemStack heldItem) {
        this.openCancelAuctionTab(tab);
    }

    private void openCancelAuctionTab(BasicTradeEditTab tab) {
        if (tab.menu.getTrader() instanceof AuctionHouseTrader ah) {
            int tradeIndex = ah.getTradeIndex(this);
            if (tradeIndex < 0) {
                return;
            }
            tab.sendOpenTabMessage(2, LazyPacketData.simpleInt("TradeIndex", tradeIndex));
        }
    }

    @Override
    public TradeDirection getTradeDirection() {
        return TradeDirection.OTHER;
    }

    @Override
    public TradeComparisonResult compare(TradeData otherTrade) {
        return new TradeComparisonResult();
    }

    @Override
    public boolean AcceptableDifferences(TradeComparisonResult result) {
        return false;
    }

    @Override
    public List<Component> GetDifferenceWarnings(TradeComparisonResult differences) {
        return new ArrayList();
    }
}