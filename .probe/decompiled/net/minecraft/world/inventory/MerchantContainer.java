package net.minecraft.world.inventory;

import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class MerchantContainer implements Container {

    private final Merchant merchant;

    private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(3, ItemStack.EMPTY);

    @Nullable
    private MerchantOffer activeOffer;

    private int selectionHint;

    private int futureXp;

    public MerchantContainer(Merchant merchant0) {
        this.merchant = merchant0;
    }

    @Override
    public int getContainerSize() {
        return this.itemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack $$0 : this.itemStacks) {
            if (!$$0.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int int0) {
        return this.itemStacks.get(int0);
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        ItemStack $$2 = this.itemStacks.get(int0);
        if (int0 == 2 && !$$2.isEmpty()) {
            return ContainerHelper.removeItem(this.itemStacks, int0, $$2.getCount());
        } else {
            ItemStack $$3 = ContainerHelper.removeItem(this.itemStacks, int0, int1);
            if (!$$3.isEmpty() && this.isPaymentSlot(int0)) {
                this.updateSellItem();
            }
            return $$3;
        }
    }

    private boolean isPaymentSlot(int int0) {
        return int0 == 0 || int0 == 1;
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        return ContainerHelper.takeItem(this.itemStacks, int0);
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        this.itemStacks.set(int0, itemStack1);
        if (!itemStack1.isEmpty() && itemStack1.getCount() > this.m_6893_()) {
            itemStack1.setCount(this.m_6893_());
        }
        if (this.isPaymentSlot(int0)) {
            this.updateSellItem();
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.merchant.getTradingPlayer() == player0;
    }

    @Override
    public void setChanged() {
        this.updateSellItem();
    }

    public void updateSellItem() {
        this.activeOffer = null;
        ItemStack $$0;
        ItemStack $$1;
        if (this.itemStacks.get(0).isEmpty()) {
            $$0 = this.itemStacks.get(1);
            $$1 = ItemStack.EMPTY;
        } else {
            $$0 = this.itemStacks.get(0);
            $$1 = this.itemStacks.get(1);
        }
        if ($$0.isEmpty()) {
            this.setItem(2, ItemStack.EMPTY);
            this.futureXp = 0;
        } else {
            MerchantOffers $$4 = this.merchant.getOffers();
            if (!$$4.isEmpty()) {
                MerchantOffer $$5 = $$4.getRecipeFor($$0, $$1, this.selectionHint);
                if ($$5 == null || $$5.isOutOfStock()) {
                    this.activeOffer = $$5;
                    $$5 = $$4.getRecipeFor($$1, $$0, this.selectionHint);
                }
                if ($$5 != null && !$$5.isOutOfStock()) {
                    this.activeOffer = $$5;
                    this.setItem(2, $$5.assemble());
                    this.futureXp = $$5.getXp();
                } else {
                    this.setItem(2, ItemStack.EMPTY);
                    this.futureXp = 0;
                }
            }
            this.merchant.notifyTradeUpdated(this.getItem(2));
        }
    }

    @Nullable
    public MerchantOffer getActiveOffer() {
        return this.activeOffer;
    }

    public void setSelectionHint(int int0) {
        this.selectionHint = int0;
        this.updateSellItem();
    }

    @Override
    public void clearContent() {
        this.itemStacks.clear();
    }

    public int getFutureXp() {
        return this.futureXp;
    }
}