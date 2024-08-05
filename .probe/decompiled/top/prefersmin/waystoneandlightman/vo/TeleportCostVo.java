package top.prefersmin.waystoneandlightman.vo;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;

public class TeleportCostVo {

    public boolean canAfford;

    public MoneyValue cost;

    public boolean isCanAfford() {
        return this.canAfford;
    }

    public TeleportCostVo canAfford(boolean canAfford) {
        this.canAfford = canAfford;
        return this;
    }

    public MoneyValue getCost() {
        return this.cost;
    }

    public TeleportCostVo cost(MoneyValue cost) {
        this.cost = cost;
        return this;
    }

    public TeleportCostVo free() {
        this.canAfford = true;
        this.cost = CoinValue.free();
        return this;
    }
}