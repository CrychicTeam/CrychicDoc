package io.github.lightman314.lightmanscurrency.api.traders.trade;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.events.TradeEvent;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.taxes.ITaxCollector;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.client.TradeRenderManager;
import io.github.lightman314.lightmanscurrency.api.traders.trade.comparison.TradeComparisonResult;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.trades_basic.BasicTradeEditTab;
import io.github.lightman314.lightmanscurrency.common.traders.rules.ITradeRuleHost;
import io.github.lightman314.lightmanscurrency.common.traders.rules.TradeRule;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class TradeData implements ITradeRuleHost {

    public static final String DEFAULT_KEY = "Trades";

    protected MoneyValue cost = MoneyValue.empty();

    List<TradeRule> rules = new ArrayList();

    private TradeData.TradeCostCache cachedCost = TradeData.TradeCostCache.EMPTY;

    private final boolean validateRules;

    public abstract TradeDirection getTradeDirection();

    public boolean validCost() {
        return this.getCost().isValidPrice();
    }

    public boolean isValid() {
        return this.validCost();
    }

    public MoneyValue getCost() {
        return this.cost;
    }

    protected void flagPriceAsChanged() {
        this.cachedCost = TradeData.TradeCostCache.EMPTY;
    }

    public final MoneyValue getCost(@Nonnull TradeContext context) {
        if (context.hasPlayerReference() && context.hasTrader()) {
            TradeEvent.TradeCostEvent event = context.getTrader().runTradeCostEvent(this, context);
            if (!this.cachedCost.matches(event)) {
                if (this.cachedCost != TradeData.TradeCostCache.EMPTY) {
                    LightmansCurrency.LogDebug("New price cached for a trade. Event results must have changed!");
                }
                this.cachedCost = TradeData.TradeCostCache.cache(event);
            }
            return this.cachedCost.getPrice();
        } else {
            return this.getCost();
        }
    }

    public MoneyValue getCostWithTaxes(TraderData trader) {
        MoneyValue cost = this.getCost();
        MoneyValue taxAmount = MoneyValue.empty();
        for (ITaxCollector entry : trader.getApplicableTaxes()) {
            taxAmount = taxAmount.addValue(cost.percentageOfValue(entry.getTaxRate()));
        }
        return cost.addValue(taxAmount);
    }

    public MoneyValue getCostWithTaxes(TradeContext context) {
        MoneyValue cost = this.getCost(context);
        if (!context.hasTrader()) {
            return cost;
        } else {
            TraderData trader = context.getTrader();
            MoneyValue taxAmount = MoneyValue.empty();
            for (ITaxCollector entry : trader.getApplicableTaxes()) {
                taxAmount = taxAmount.addValue(cost.percentageOfValue(entry.getTaxRate()));
            }
            return cost.addValue(taxAmount);
        }
    }

    public void setCost(MoneyValue value) {
        this.cost = value;
        this.flagPriceAsChanged();
    }

    public boolean outOfStock(@Nonnull TradeContext context) {
        return !this.hasStock(context);
    }

    public boolean hasStock(@Nonnull TradeContext context) {
        return this.getStock(context) > 0;
    }

    public abstract int getStock(@Nonnull TradeContext var1);

    public final int stockCountOfCost(TraderData trader) {
        if (this.getCost().isFree()) {
            return 1;
        } else if (!this.getCost().isValidPrice()) {
            return 0;
        } else {
            MoneyValue storedMoney = trader.getStoredMoney().getStoredMoney().valueOf(this.getCost().getUniqueName());
            MoneyValue price = this.getCostWithTaxes(trader);
            return (int) (storedMoney.getCoreValue() / price.getCoreValue());
        }
    }

    public final int stockCountOfCost(TradeContext context) {
        if (!context.hasTrader()) {
            return 0;
        } else {
            TraderData trader = context.getTrader();
            if (this.getCost().isFree()) {
                return 1;
            } else if (!this.getCost().isValidPrice()) {
                return 0;
            } else {
                MoneyValue storedMoney = trader.getStoredMoney().getStoredMoney().valueOf(this.getCost().getUniqueName());
                MoneyValue price = this.getCostWithTaxes(context);
                return (int) MathUtil.SafeDivide(storedMoney.getCoreValue(), price.getCoreValue(), 1L);
            }
        }
    }

    protected TradeData(boolean validateRules) {
        this.validateRules = validateRules;
        if (this.validateRules) {
            TradeRule.ValidateTradeRuleList(this.rules, this);
        }
    }

    public CompoundTag getAsNBT() {
        CompoundTag tradeNBT = new CompoundTag();
        tradeNBT.put("Price", this.cost.save());
        TradeRule.saveRules(tradeNBT, this.rules, "RuleData");
        return tradeNBT;
    }

    protected void loadFromNBT(CompoundTag nbt) {
        this.cost = MoneyValue.safeLoad(nbt, "Price");
        this.flagPriceAsChanged();
        if (nbt.contains("IsFree") && nbt.getBoolean("IsFree")) {
            this.cost = MoneyValue.free();
            this.flagPriceAsChanged();
        }
        this.rules.clear();
        if (nbt.contains("TradeRules")) {
            this.rules = TradeRule.loadRules(nbt, "TradeRules", this);
            for (TradeRule r : this.rules) {
                r.setActive(true);
            }
        } else {
            this.rules = TradeRule.loadRules(nbt, "RuleData", this);
        }
        if (this.validateRules) {
            TradeRule.ValidateTradeRuleList(this.rules, this);
        }
    }

    @Override
    public final boolean isTrader() {
        return false;
    }

    @Override
    public final boolean isTrade() {
        return true;
    }

    public void beforeTrade(TradeEvent.PreTradeEvent event) {
        for (TradeRule rule : this.rules) {
            if (rule.isActive()) {
                rule.beforeTrade(event);
            }
        }
    }

    public void tradeCost(TradeEvent.TradeCostEvent event) {
        for (TradeRule rule : this.rules) {
            if (rule.isActive()) {
                rule.tradeCost(event);
            }
        }
    }

    public void afterTrade(TradeEvent.PostTradeEvent event) {
        for (TradeRule rule : this.rules) {
            if (rule.isActive()) {
                rule.afterTrade(event);
            }
        }
    }

    @Nonnull
    @Override
    public List<TradeRule> getRules() {
        return new ArrayList(this.rules);
    }

    @Override
    public void markTradeRulesDirty() {
    }

    public void setRules(List<TradeRule> rules) {
        this.rules = rules;
    }

    public abstract TradeComparisonResult compare(TradeData var1);

    public abstract boolean AcceptableDifferences(TradeComparisonResult var1);

    public abstract List<Component> GetDifferenceWarnings(TradeComparisonResult var1);

    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public abstract TradeRenderManager<?> getButtonRenderer();

    public abstract void OnInputDisplayInteraction(@Nonnull BasicTradeEditTab var1, @Nullable Consumer<LazyPacketData.Builder> var2, int var3, int var4, @Nonnull ItemStack var5);

    public abstract void OnOutputDisplayInteraction(@Nonnull BasicTradeEditTab var1, @Nullable Consumer<LazyPacketData.Builder> var2, int var3, int var4, @Nonnull ItemStack var5);

    @Deprecated(since = "2.1.2.4")
    public void onInteraction(@Nonnull BasicTradeEditTab tab, @Nullable Consumer<CompoundTag> clientHandler, int mouseX, int mouseY, int button, @Nonnull ItemStack heldItem) {
    }

    public abstract void OnInteraction(@Nonnull BasicTradeEditTab var1, @Nullable Consumer<LazyPacketData.Builder> var2, int var3, int var4, int var5, @Nonnull ItemStack var6);

    @NotNull
    public final List<Integer> getRelevantInventorySlots(TradeContext context, List<Slot> slots) {
        List<Integer> results = new ArrayList();
        this.collectRelevantInventorySlots(context, slots, results);
        return results;
    }

    protected void collectRelevantInventorySlots(TradeContext context, List<Slot> slots, List<Integer> results) {
    }

    private static class TradeCostCache {

        private final boolean free;

        private final int percentage;

        private final MoneyValue price;

        public static final TradeData.TradeCostCache EMPTY = new TradeData.TradeCostCache(true, 0, MoneyValue.free());

        public MoneyValue getPrice() {
            return this.free ? MoneyValue.free() : this.price;
        }

        private TradeCostCache(boolean free, int percentage, MoneyValue price) {
            this.free = free;
            this.percentage = percentage;
            this.price = price;
        }

        public static TradeData.TradeCostCache cache(@Nonnull TradeEvent.TradeCostEvent event) {
            return new TradeData.TradeCostCache(event.getCostResultIsFree(), event.getPricePercentage(), event.getCostResult());
        }

        public boolean matches(@Nonnull TradeEvent.TradeCostEvent event) {
            return event.getCostResultIsFree() && this.free ? true : this.percentage == event.getPricePercentage();
        }
    }
}