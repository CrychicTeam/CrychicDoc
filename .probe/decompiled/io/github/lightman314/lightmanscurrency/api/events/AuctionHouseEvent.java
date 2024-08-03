package io.github.lightman314.lightmanscurrency.api.events;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata.AuctionTradeData;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class AuctionHouseEvent extends Event {

    protected final AuctionHouseTrader auctionHouse;

    @Nonnull
    public AuctionHouseTrader getAuctionHouse() {
        return this.auctionHouse;
    }

    protected AuctionHouseEvent(@Nonnull AuctionHouseTrader auctionHouse) {
        this.auctionHouse = auctionHouse;
    }

    public static class AuctionEvent extends AuctionHouseEvent {

        protected AuctionTradeData auction;

        @Nonnull
        public AuctionTradeData getAuction() {
            return this.auction;
        }

        protected AuctionEvent(@Nonnull AuctionHouseTrader auctionHouse, @Nonnull AuctionTradeData auction) {
            super(auctionHouse);
            this.auction = auction;
        }

        public static class AuctionBidEvent extends AuctionHouseEvent.AuctionEvent {

            protected final Player bidder;

            protected MoneyValue bidAmount;

            @Nonnull
            public Player getBidder() {
                return this.bidder;
            }

            @Nonnull
            public MoneyValue getBidAmount() {
                return this.bidAmount;
            }

            protected AuctionBidEvent(@Nonnull AuctionHouseTrader auctionHouse, @Nonnull AuctionTradeData auction, @Nonnull Player bidder, @Nonnull MoneyValue bidAmount) {
                super(auctionHouse, auction);
                this.bidder = bidder;
                this.bidAmount = bidAmount;
            }

            public static class Post extends AuctionHouseEvent.AuctionEvent.AuctionBidEvent {

                public Post(@Nonnull AuctionHouseTrader auctionHouse, @Nonnull AuctionTradeData auction, @Nonnull Player bidder, @Nonnull MoneyValue bidAmount) {
                    super(auctionHouse, auction, bidder, bidAmount);
                }
            }

            @Cancelable
            public static class Pre extends AuctionHouseEvent.AuctionEvent.AuctionBidEvent {

                public void setBidAmount(@Nonnull MoneyValue bidAmount) {
                    this.bidAmount = (MoneyValue) Objects.requireNonNull(bidAmount);
                }

                public Pre(@Nonnull AuctionHouseTrader auctionHouse, @Nonnull AuctionTradeData auction, @Nonnull Player bidder, @Nonnull MoneyValue bidAmount) {
                    super(auctionHouse, auction, bidder, bidAmount);
                }
            }
        }

        public static class AuctionCompletedEvent extends AuctionHouseEvent.AuctionEvent {

            List<ItemStack> items = this.auction.getAuctionItems();

            MoneyValue paymentAmount;

            public boolean hadBidder() {
                return this.auction.getLastBidPlayer() != null;
            }

            @Nonnull
            public List<ItemStack> getItems() {
                return this.items;
            }

            public void setItems(@Nonnull List<ItemStack> bidderRewards) {
                this.items = (List<ItemStack>) Objects.requireNonNull(bidderRewards);
            }

            @Nonnull
            public MoneyValue getPayment() {
                return this.paymentAmount;
            }

            public void setPayment(@Nonnull MoneyValue paymentAmount) {
                this.paymentAmount = (MoneyValue) Objects.requireNonNull(paymentAmount);
            }

            public AuctionCompletedEvent(@Nonnull AuctionHouseTrader auctionHouse, @Nonnull AuctionTradeData auction) {
                super(auctionHouse, auction);
                if (this.hadBidder()) {
                    this.paymentAmount = this.auction.getLastBidAmount();
                } else {
                    this.paymentAmount = MoneyValue.empty();
                }
            }
        }

        public static class CancelAuctionEvent extends AuctionHouseEvent.AuctionEvent {

            protected final Player player;

            @Nonnull
            public Player getPlayer() {
                return this.player;
            }

            public CancelAuctionEvent(@Nonnull AuctionHouseTrader auctionHouse, @Nonnull AuctionTradeData auction, @Nonnull Player player) {
                super(auctionHouse, auction);
                this.player = player;
            }
        }

        public static class CreateAuctionEvent extends AuctionHouseEvent.AuctionEvent {

            protected final boolean persistent;

            public boolean isPersistent() {
                return this.persistent;
            }

            protected CreateAuctionEvent(AuctionHouseTrader auctionHouse, AuctionTradeData auction, boolean persistent) {
                super(auctionHouse, auction);
                this.persistent = persistent;
            }

            public static final class Post extends AuctionHouseEvent.AuctionEvent.CreateAuctionEvent {

                public Post(@Nonnull AuctionHouseTrader auctionHouse, @Nonnull AuctionTradeData auction, boolean persistent) {
                    super(auctionHouse, auction, persistent);
                }
            }

            public static final class Pre extends AuctionHouseEvent.AuctionEvent.CreateAuctionEvent {

                public Pre(@Nonnull AuctionHouseTrader auctionHouse, @Nonnull AuctionTradeData auction, boolean persistent) {
                    super(auctionHouse, auction, persistent);
                }

                public void setAuction(AuctionTradeData auction) {
                    Objects.requireNonNull(auction);
                    this.auction = auction;
                }

                public boolean isCancelable() {
                    return !this.isPersistent();
                }
            }
        }
    }
}