package io.github.lightman314.lightmanscurrency.common.menus.traderstorage.auction;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.auction.AuctionCreateClientTab;
import io.github.lightman314.lightmanscurrency.common.menus.slots.SimpleSlot;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata.AuctionTradeData;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AuctionCreateTab extends TraderStorageTab {

    List<SimpleSlot> slots = new ArrayList();

    SimpleContainer auctionItems = new SimpleContainer(2);

    public AuctionCreateTab(@Nonnull ITraderStorageMenu menu) {
        super(menu);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object createClientTab(Object screen) {
        return new AuctionCreateClientTab(screen, this);
    }

    @Override
    public boolean canOpen(Player player) {
        return true;
    }

    public List<SimpleSlot> getSlots() {
        return this.slots;
    }

    public SimpleContainer getAuctionItems() {
        return this.auctionItems;
    }

    @Override
    public void addStorageMenuSlots(Function<Slot, Slot> addSlot) {
        for (int i = 0; i < this.auctionItems.getContainerSize(); i++) {
            SimpleSlot newSlot = new SimpleSlot(this.auctionItems, i, 23 + i * 18, 122);
            addSlot.apply(newSlot);
            this.slots.add(newSlot);
        }
        SimpleSlot.SetActive(this.slots, false);
    }

    @Override
    public void onTabOpen() {
        SimpleSlot.SetActive(this.slots);
        for (SimpleSlot slot : this.slots) {
            slot.locked = false;
        }
    }

    @Override
    public void onTabClose() {
        SimpleSlot.SetInactive(this.slots);
        this.menu.clearContainer(this.auctionItems);
    }

    @Override
    public void onMenuClose() {
        this.menu.clearContainer(this.auctionItems);
    }

    public void createAuction(AuctionTradeData trade) {
        if (this.menu.getTrader() instanceof AuctionHouseTrader trader) {
            if (this.menu.isClient()) {
                this.menu.SendMessage(LazyPacketData.simpleTag("CreateAuction", trade.getAsNBT()));
                return;
            }
            trade.setAuctionItems(this.auctionItems);
            if (!trade.isValid()) {
                this.menu.SendMessage(LazyPacketData.simpleBoolean("AuctionCreated", false));
                return;
            }
            trader.addTrade(trade, false);
            this.auctionItems.clearContent();
            this.menu.SendMessage(LazyPacketData.simpleBoolean("AuctionCreated", true));
            for (SimpleSlot slot : this.slots) {
                slot.locked = true;
            }
        }
    }

    @Override
    public void receiveMessage(LazyPacketData message) {
        if (message.contains("CreateAuction")) {
            this.createAuction(new AuctionTradeData(message.getNBT("CreateAuction")));
        }
    }
}