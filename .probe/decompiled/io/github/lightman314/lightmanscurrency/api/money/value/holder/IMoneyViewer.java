package io.github.lightman314.lightmanscurrency.api.money.value.holder;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IMoneyViewer {

    @Nonnull
    MoneyView getStoredMoney();

    boolean hasStoredMoneyChanged(@Nullable Object var1);

    void flagAsKnown(@Nullable Object var1);

    void forgetContext(@Nonnull Object var1);
}