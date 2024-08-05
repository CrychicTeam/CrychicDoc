package io.github.lightman314.lightmanscurrency.api.events;

import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.AlertData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class TradeEvent extends Event {

    private final TradeData trade;

    private final TradeContext context;

    @Nonnull
    public final PlayerReference getPlayerReference() {
        return this.context.getPlayerReference();
    }

    @Nonnull
    public final TradeData getTrade() {
        return this.trade;
    }

    public final int getTradeIndex() {
        return this.getTrader().indexOfTrade(this.trade);
    }

    @Nonnull
    public final TraderData getTrader() {
        return this.context.getTrader();
    }

    public final TradeContext getContext() {
        return this.context;
    }

    protected TradeEvent(@Nonnull TradeData trade, @Nonnull TradeContext context) {
        this.trade = trade;
        this.context = context;
    }

    public static class PostTradeEvent extends TradeEvent {

        private boolean isDirty = false;

        private final MoneyValue pricePaid;

        private final MoneyValue taxesPaid;

        public MoneyValue getPricePaid() {
            return this.pricePaid;
        }

        public MoneyValue getTaxesPaid() {
            return this.taxesPaid;
        }

        public PostTradeEvent(@Nonnull TradeData trade, @Nonnull TradeContext context, @Nonnull MoneyValue pricePaid, @Nonnull MoneyValue taxesPaid) {
            super(trade, context);
            this.pricePaid = pricePaid;
            this.taxesPaid = taxesPaid;
        }

        public boolean isDirty() {
            return this.isDirty;
        }

        public void markDirty() {
            this.isDirty = true;
        }

        public void clean() {
            this.isDirty = false;
        }
    }

    @Cancelable
    public static class PreTradeEvent extends TradeEvent {

        private final List<AlertData> alerts = new ArrayList();

        public PreTradeEvent(@Nonnull TradeData trade, @Nonnull TradeContext context) {
            super(trade, context);
        }

        public void addAlert(@Nonnull AlertData alert, boolean cancelTrade) {
            this.alerts.add(alert);
            if (cancelTrade) {
                this.setCanceled(true);
            }
        }

        public void addHelpful(@Nonnull MutableComponent message) {
            this.addAlert(AlertData.helpful(message), false);
        }

        public void addNeutral(@Nonnull MutableComponent message) {
            this.addAlert(AlertData.neutral(message), false);
        }

        public void addWarning(@Nonnull MutableComponent message) {
            this.addAlert(AlertData.warn(message), false);
        }

        public void addError(@Nonnull MutableComponent message) {
            this.addAlert(AlertData.error(message), false);
        }

        public void addDenial(@Nonnull MutableComponent message) {
            this.addAlert(AlertData.error(message), true);
        }

        @Nonnull
        public List<AlertData> getAlertInfo() {
            return this.alerts;
        }
    }

    public static class TradeCostEvent extends TradeEvent {

        private boolean forceFree = false;

        private int pricePercentage = 100;

        MoneyValue baseCost;

        public boolean forcedFree() {
            return this.forceFree;
        }

        public void makeFree() {
            this.forceFree = true;
        }

        public void makeNotFree() {
            this.forceFree = false;
        }

        public int getPricePercentage() {
            return this.pricePercentage;
        }

        public void setPricePercentage(int pricePercentage) {
            this.pricePercentage = pricePercentage;
        }

        public void giveDiscount(int percentage) {
            this.pricePercentage -= percentage;
        }

        public void hikePrice(int percentage) {
            this.pricePercentage += percentage;
        }

        public MoneyValue getBaseCost() {
            return this.baseCost;
        }

        public boolean getCostResultIsFree() {
            return this.forceFree || this.pricePercentage <= 0 || this.baseCost.isFree();
        }

        public MoneyValue getCostResult() {
            return this.getCostResultIsFree() ? MoneyValue.free() : this.baseCost.percentageOfValue(this.pricePercentage);
        }

        public TradeCostEvent(@Nonnull TradeData trade, @Nonnull TradeContext context) {
            super(trade, context);
            this.baseCost = trade.getCost();
        }

        public final boolean matches(@Nonnull TradeData trade) {
            return this.getTrade() == trade;
        }

        public final boolean matches(@Nonnull TradeEvent.TradeCostEvent event) {
            return this.getTrade() == event.getTrade() && this.forceFree == event.forceFree && this.pricePercentage == event.pricePercentage && this.baseCost.equals(event.baseCost);
        }
    }
}