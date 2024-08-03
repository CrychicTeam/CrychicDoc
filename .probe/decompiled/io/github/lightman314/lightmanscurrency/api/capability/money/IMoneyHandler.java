package io.github.lightman314.lightmanscurrency.api.capability.money;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyViewer;
import javax.annotation.Nonnull;

public interface IMoneyHandler extends IMoneyViewer {

    @Nonnull
    MoneyValue insertMoney(@Nonnull MoneyValue var1, boolean var2);

    @Nonnull
    MoneyValue extractMoney(@Nonnull MoneyValue var1, boolean var2);

    boolean isMoneyTypeValid(@Nonnull MoneyValue var1);
}