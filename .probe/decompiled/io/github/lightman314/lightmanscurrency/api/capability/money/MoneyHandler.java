package io.github.lightman314.lightmanscurrency.api.capability.money;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.MoneyViewer;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MoneyHandler extends MoneyViewer implements IMoneyHandler {

    public static IMoneyHandler combine(@Nonnull List<IMoneyHandler> handlers) {
        return new MoneyHandler.MultiMoneyHandler(handlers);
    }

    private static class MultiMoneyHandler extends MoneyHandler {

        private final List<IMoneyHandler> handlers;

        MultiMoneyHandler(@Nonnull List<IMoneyHandler> handlers) {
            this.handlers = handlers;
        }

        @Nonnull
        @Override
        public MoneyValue insertMoney(@Nonnull MoneyValue insertAmount, boolean simulation) {
            for (IMoneyHandler h : this.handlers) {
                if (h.isMoneyTypeValid(insertAmount)) {
                    insertAmount = h.insertMoney(insertAmount, simulation);
                    if (insertAmount.isEmpty()) {
                        return MoneyValue.empty();
                    }
                }
            }
            return insertAmount;
        }

        @Nonnull
        @Override
        public MoneyValue extractMoney(@Nonnull MoneyValue extractAmount, boolean simulation) {
            for (IMoneyHandler h : this.handlers) {
                extractAmount = h.extractMoney(extractAmount, simulation);
                if (extractAmount.isEmpty()) {
                    return MoneyValue.empty();
                }
            }
            return extractAmount;
        }

        @Override
        public boolean isMoneyTypeValid(@Nonnull MoneyValue value) {
            return this.handlers.stream().anyMatch(h -> h.isMoneyTypeValid(value));
        }

        @Override
        protected boolean hasStoredMoneyChanged() {
            return this.handlers.stream().anyMatch(h -> h.hasStoredMoneyChanged(this));
        }

        @Override
        protected void collectStoredMoney(@Nonnull MoneyView.Builder builder) {
            for (IMoneyHandler h : this.handlers) {
                builder.merge(h.getStoredMoney());
                h.flagAsKnown(this);
            }
        }
    }

    public abstract static class Slave implements IMoneyHandler {

        @Nullable
        protected abstract IMoneyHandler getParent();

        @Nonnull
        @Override
        public MoneyValue insertMoney(@Nonnull MoneyValue insertAmount, boolean simulation) {
            IMoneyHandler handler = this.getParent();
            return handler != null ? handler.insertMoney(insertAmount, simulation) : insertAmount;
        }

        @Nonnull
        @Override
        public MoneyValue extractMoney(@Nonnull MoneyValue extractAmount, boolean simulation) {
            IMoneyHandler handler = this.getParent();
            return handler != null ? handler.extractMoney(extractAmount, simulation) : extractAmount;
        }

        @Override
        public boolean isMoneyTypeValid(@Nonnull MoneyValue value) {
            IMoneyHandler handler = this.getParent();
            return handler != null ? handler.isMoneyTypeValid(value) : false;
        }

        @Nonnull
        @Override
        public MoneyView getStoredMoney() {
            IMoneyHandler handler = this.getParent();
            return handler != null ? handler.getStoredMoney() : MoneyView.empty();
        }

        @Override
        public boolean hasStoredMoneyChanged(@Nullable Object context) {
            IMoneyHandler handler = this.getParent();
            return handler != null ? handler.hasStoredMoneyChanged(context) : false;
        }

        @Override
        public void flagAsKnown(@Nullable Object context) {
            IMoneyHandler handler = this.getParent();
            if (handler != null) {
                handler.flagAsKnown(context);
            }
        }

        @Override
        public void forgetContext(@Nonnull Object context) {
            IMoneyHandler handler = this.getParent();
            if (handler != null) {
                handler.forgetContext(context);
            }
        }
    }
}