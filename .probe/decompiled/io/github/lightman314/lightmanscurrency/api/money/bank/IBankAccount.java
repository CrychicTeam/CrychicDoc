package io.github.lightman314.lightmanscurrency.api.money.bank;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyStorage;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyHolder;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.util.NonNullSupplier;

public interface IBankAccount extends IMoneyHolder, IClientTracker {

    @Nonnull
    MoneyStorage getMoneyStorage();

    @Nonnull
    MutableComponent getName();

    @Nonnull
    Map<String, MoneyValue> getNotificationLevels();

    @Nonnull
    MoneyValue getNotificationLevelFor(@Nonnull String var1);

    void setNotificationLevel(@Nonnull String var1, @Nonnull MoneyValue var2);

    void pushLocalNotification(@Nonnull Notification var1);

    default void pushNotification(@Nonnull NonNullSupplier<Notification> notification) {
        this.pushNotification(notification, true);
    }

    void pushNotification(@Nonnull NonNullSupplier<Notification> var1, boolean var2);

    @Nonnull
    List<Notification> getNotifications();

    @Nonnull
    default Component getBalanceText() {
        return LCText.GUI_BANK_BALANCE.get(this.getMoneyStorage().getRandomValueText());
    }

    void depositMoney(@Nonnull MoneyValue var1);

    @Nonnull
    MoneyValue withdrawMoney(@Nonnull MoneyValue var1);

    void applyInterest(double var1, @Nonnull List<MoneyValue> var3, boolean var4, boolean var5);
}