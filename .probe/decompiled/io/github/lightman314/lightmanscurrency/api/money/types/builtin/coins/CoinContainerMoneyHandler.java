package io.github.lightman314.lightmanscurrency.api.money.types.builtin.coins;

import io.github.lightman314.lightmanscurrency.api.capability.money.MoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.CoinEntry;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class CoinContainerMoneyHandler extends MoneyHandler {

    private final Container container;

    private Container containerCache = new SimpleContainer(1);

    private final Consumer<ItemStack> overflowHandler;

    public CoinContainerMoneyHandler(@Nonnull Container container, @Nonnull Consumer<ItemStack> overflowHandler) {
        this.container = container;
        this.overflowHandler = overflowHandler;
    }

    @Nonnull
    @Override
    public MoneyValue insertMoney(@Nonnull MoneyValue insertAmount, boolean simulation) {
        if (!(insertAmount instanceof CoinValue coinValue)) {
            return insertAmount;
        } else {
            Container active = simulation ? InventoryUtil.copyInventory(this.container) : this.container;
            List<ItemStack> coins = coinValue.getAsSeperatedItemList();
            List<ItemStack> extra = new ArrayList();
            for (ItemStack c : coins) {
                ItemStack e = InventoryUtil.TryPutItemStack(active, c);
                if (!e.isEmpty()) {
                    extra.add(e);
                }
            }
            if (!simulation) {
                for (ItemStack e : extra) {
                    this.overflowHandler.accept(e);
                }
            }
            return MoneyValue.empty();
        }
    }

    @Nonnull
    @Override
    public MoneyValue extractMoney(@Nonnull MoneyValue extractAmount, boolean simulation) {
        if (extractAmount instanceof CoinValue coinValue) {
            Container active = simulation ? InventoryUtil.copyInventory(this.container) : this.container;
            long change = takeObjectsOfValue(coinValue, active);
            if (change > 0L) {
                return CoinValue.fromNumber(coinValue.getChain(), change);
            } else {
                if (change < 0L) {
                    this.insertMoney(CoinValue.fromNumber(coinValue.getChain(), change * -1L), simulation);
                }
                return MoneyValue.empty();
            }
        } else {
            return extractAmount;
        }
    }

    @Override
    public boolean isMoneyTypeValid(@Nonnull MoneyValue value) {
        return value instanceof CoinValue;
    }

    @Override
    protected boolean hasStoredMoneyChanged() {
        return !InventoryUtil.ContainerMatches(this.container, this.containerCache);
    }

    @Override
    protected void collectStoredMoney(@Nonnull MoneyView.Builder builder) {
        queryContainerContents(this.container, builder);
        this.containerCache = InventoryUtil.copyInventory(this.container);
    }

    public static void queryContainerContents(@Nonnull Container container, @Nonnull MoneyView.Builder builder) {
        for (ChainData chain : CoinAPI.API.AllChainData()) {
            long totalValue = 0L;
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                totalValue += chain.getCoreValue(stack) * (long) stack.getCount();
            }
            if (totalValue > 0L) {
                builder.add(CoinValue.fromNumber(chain.chain, totalValue));
            }
        }
    }

    private static long takeObjectsOfValue(@Nonnull CoinValue valueToTake, @Nonnull Container container) {
        long value = valueToTake.getCoreValue();
        ChainData chainData = CoinAPI.API.ChainData(valueToTake.getChain());
        if (chainData == null) {
            return value;
        } else {
            List<CoinEntry> coinList = chainData.getAllEntries(true);
            coinList.sort(ChainData.SORT_HIGHEST_VALUE_FIRST);
            for (CoinEntry coinEntry : coinList) {
                long coinValue = coinEntry.getCoreValue();
                if (coinValue <= value) {
                    for (int i = 0; i < container.getContainerSize() && coinValue <= value; i++) {
                        ItemStack itemStack = container.getItem(i);
                        if (coinEntry.matches(itemStack)) {
                            while (coinValue <= value && !itemStack.isEmpty()) {
                                value -= coinValue;
                                itemStack.shrink(1);
                                if (itemStack.isEmpty()) {
                                    container.setItem(i, ItemStack.EMPTY);
                                }
                            }
                        }
                    }
                }
            }
            if (value > 0L) {
                coinList.sort(ChainData.SORT_LOWEST_VALUE_FIRST);
                for (CoinEntry coinEntryx : coinList) {
                    long coinValue = coinEntryx.getCoreValue();
                    for (int ix = 0; ix < container.getContainerSize() && value > 0L; ix++) {
                        ItemStack itemStack = container.getItem(ix);
                        if (coinEntryx.matches(itemStack)) {
                            while (value > 0L && !itemStack.isEmpty()) {
                                value -= coinValue;
                                itemStack.shrink(1);
                                if (itemStack.isEmpty()) {
                                    container.setItem(ix, ItemStack.EMPTY);
                                }
                            }
                        }
                    }
                }
            }
            return value;
        }
    }
}