package io.github.lightman314.lightmanscurrency.common.menus.traderstorage.auction;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.auction.AuctionTradeCancelClientTab;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata.AuctionTradeData;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AuctionTradeCancelTab extends TraderStorageTab {

    private int tradeIndex = -1;

    public AuctionTradeCancelTab(@Nonnull ITraderStorageMenu menu) {
        super(menu);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object createClientTab(Object screen) {
        return new AuctionTradeCancelClientTab(screen, this);
    }

    @Override
    public boolean canOpen(Player player) {
        return true;
    }

    public int getTradeIndex() {
        return this.tradeIndex;
    }

    public AuctionTradeData getTrade() {
        if (this.menu.getTrader() instanceof AuctionHouseTrader trader) {
            if (this.tradeIndex < trader.getTradeCount() && this.tradeIndex >= 0) {
                return ((AuctionHouseTrader) this.menu.getTrader()).getTrade(this.tradeIndex);
            } else {
                this.menu.changeTab(0);
                this.menu.SendMessage(this.menu.createTabChangeMessage(0, null));
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onTabOpen() {
    }

    @Override
    public void onTabClose() {
    }

    @Override
    public void addStorageMenuSlots(Function<Slot, Slot> addSlot) {
    }

    public void setTradeIndex(int tradeIndex) {
        this.tradeIndex = tradeIndex;
    }

    public void cancelAuction(boolean giveToPlayer) {
        if (this.menu.getTrader() instanceof AuctionHouseTrader trader) {
            AuctionTradeData trade = trader.getTrade(this.tradeIndex);
            if (this.menu.isClient()) {
                this.menu.SendMessage(LazyPacketData.simpleBoolean("CancelAuction", giveToPlayer));
                return;
            }
            if (trade.isOwner(this.menu.getPlayer())) {
                trade.CancelTrade(trader, giveToPlayer, this.menu.getPlayer());
                trader.markTradesDirty();
                trader.markStorageDirty();
                this.menu.SendMessage(LazyPacketData.simpleBoolean("CancelSuccess", true));
            }
        }
    }

    @Override
    public void receiveMessage(LazyPacketData message) {
        if (message.contains("TradeIndex")) {
            this.tradeIndex = message.getInt("TradeIndex");
        }
        if (message.contains("CancelAuction")) {
            this.cancelAuction(message.getBoolean("CancelAuction"));
        }
    }
}