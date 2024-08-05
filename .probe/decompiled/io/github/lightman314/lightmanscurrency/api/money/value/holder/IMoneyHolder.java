package io.github.lightman314.lightmanscurrency.api.money.value.holder;

import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;

public interface IMoneyHolder extends IMoneyHandler {

    default int priority() {
        return 0;
    }

    default int inversePriority() {
        return this.priority() * -1;
    }

    default void formatTooltip(@Nonnull List<Component> tooltip) {
        defaultTooltipFormat(tooltip, this.getTooltipTitle(), this.getStoredMoney());
    }

    static void defaultTooltipFormat(@Nonnull List<Component> tooltip, @Nonnull Component title, @Nonnull MoneyView contents) {
        if (!contents.isEmpty()) {
            tooltip.add(title);
            for (MoneyValue val : contents.allValues()) {
                tooltip.add(val.getText());
            }
        }
    }

    Component getTooltipTitle();

    static void sortPayFirst(@Nonnull List<IMoneyHolder> list) {
        list.sort(Comparator.comparingInt(IMoneyHolder::priority));
    }

    static void sortTakeFirst(@Nonnull List<IMoneyHolder> list) {
        list.sort(Comparator.comparingInt(IMoneyHolder::inversePriority));
    }
}