package io.github.lightman314.lightmanscurrency.api.traders.trade.comparison;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;

public class TradeComparisonResult {

    private boolean compatible = false;

    private final List<ProductComparisonResult> tradeProductResults = new ArrayList();

    private boolean priceIncompatible = false;

    private boolean priceGreater = false;

    private boolean priceCheaper = false;

    private MoneyValue priceChange = MoneyValue.empty();

    private boolean tradeTypeMatches = true;

    public boolean isCompatible() {
        return this.compatible;
    }

    public boolean ProductMatches() {
        for (ProductComparisonResult result : this.tradeProductResults) {
            if (!result.Identical()) {
                return false;
            }
        }
        return true;
    }

    public ProductComparisonResult getProductResult(int index) {
        return index >= 0 && index < this.tradeProductResults.size() ? (ProductComparisonResult) this.tradeProductResults.get(index) : null;
    }

    public int getProductResultCount() {
        return this.tradeProductResults.size();
    }

    public boolean PriceMatches() {
        return this.priceChange.isEmpty() && !this.priceIncompatible;
    }

    public boolean PriceIncompatible() {
        return this.priceIncompatible;
    }

    public MoneyValue priceDifference() {
        return this.priceChange;
    }

    public boolean isPriceCheaper() {
        return this.priceCheaper;
    }

    public boolean isPriceExpensive() {
        return this.priceGreater;
    }

    public boolean TypeMatches() {
        return this.tradeTypeMatches;
    }

    public boolean Identical() {
        return this.compatible && this.ProductMatches() && this.PriceMatches() && this.TypeMatches();
    }

    public void addProductResult(ProductComparisonResult result) {
        this.tradeProductResults.add(result);
    }

    public void addProductResults(Collection<? extends ProductComparisonResult> results) {
        this.tradeProductResults.addAll(results);
    }

    public void addProductResult(boolean sameProduct, boolean sameNBT, int quantityDifference) {
        this.tradeProductResults.add(ProductComparisonResult.CreateRaw(sameProduct, sameNBT, quantityDifference));
    }

    public void comparePrices(@Nonnull MoneyValue currentPrice, @Nonnull MoneyValue oldPrice) {
        if (!currentPrice.sameType(oldPrice)) {
            this.priceIncompatible = true;
        } else {
            if (currentPrice.containsValue(oldPrice)) {
                if (oldPrice.containsValue(currentPrice)) {
                    this.priceChange = MoneyValue.empty();
                    this.priceGreater = false;
                    this.priceCheaper = false;
                    return;
                }
                this.priceGreater = true;
                this.priceCheaper = false;
                this.priceChange = currentPrice.subtractValue(oldPrice);
            } else {
                this.priceGreater = false;
                this.priceCheaper = true;
                this.priceChange = oldPrice.subtractValue(currentPrice);
            }
        }
    }

    public void setTypeResult(boolean typeMatches) {
        this.tradeTypeMatches = typeMatches;
    }

    public void setCompatible() {
        this.compatible = true;
    }
}