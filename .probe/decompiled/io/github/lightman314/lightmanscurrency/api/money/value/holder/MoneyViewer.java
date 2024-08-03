package io.github.lightman314.lightmanscurrency.api.money.value.holder;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Nullable;

public abstract class MoneyViewer implements IMoneyViewer {

    private MoneyView cachedValue = null;

    private final List<Object> knowsLatest = new ArrayList();

    @Nonnull
    @Override
    public final MoneyView getStoredMoney() {
        if (this.cachedValue == null || this.hasStoredMoneyChanged()) {
            MoneyView.Builder builder = MoneyView.builder();
            this.collectStoredMoney(builder);
            this.cachedValue = builder.build();
            this.knowsLatest.clear();
        }
        return this.cachedValue;
    }

    @Override
    public final boolean hasStoredMoneyChanged(@Nullable Object context) {
        return this.cachedValue == null || this.hasStoredMoneyChanged() || context != null && !this.knowsLatest.contains(context);
    }

    protected abstract boolean hasStoredMoneyChanged();

    protected abstract void collectStoredMoney(@Nonnull MoneyView.Builder var1);

    @Override
    public final void flagAsKnown(@Nullable Object context) {
        if (context != null && !this.knowsLatest.contains(context)) {
            this.knowsLatest.add(context);
        }
        this.onFlagAsKnown(context);
    }

    protected void onFlagAsKnown(@Nullable Object context) {
    }

    @Override
    public final void forgetContext(@Nonnull Object context) {
        this.knowsLatest.remove(context);
    }

    public abstract static class Slave implements IMoneyViewer {

        abstract IMoneyViewer getParent();

        @Nonnull
        @Override
        public final MoneyView getStoredMoney() {
            IMoneyViewer holder = this.getParent();
            return holder != null ? holder.getStoredMoney() : MoneyView.empty();
        }

        @Override
        public final boolean hasStoredMoneyChanged(@javax.annotation.Nullable Object context) {
            IMoneyViewer holder = this.getParent();
            return holder != null ? holder.hasStoredMoneyChanged(context) : false;
        }

        @Override
        public final void flagAsKnown(@javax.annotation.Nullable Object context) {
            IMoneyViewer holder = this.getParent();
            if (holder != null) {
                holder.flagAsKnown(context);
            }
        }

        @Override
        public final void forgetContext(@Nonnull Object context) {
            IMoneyViewer holder = this.getParent();
            if (holder != null) {
                holder.forgetContext(context);
            }
        }
    }
}