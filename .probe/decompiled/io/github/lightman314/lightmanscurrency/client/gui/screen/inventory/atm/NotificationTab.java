package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyValueWidget;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.ATMScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.menus.ATMMenu;
import io.github.lightman314.lightmanscurrency.common.menus.slots.SimpleSlot;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;

public class NotificationTab extends ATMTab {

    MoneyValueWidget notificationSelection;

    public NotificationTab(ATMScreen screen) {
        super(screen);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.of(Items.ENDER_PEARL);
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_ATM_NOTIFICATIONS.get();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        SimpleSlot.SetInactive(this.screen.m_6262_());
        this.notificationSelection = this.addChild(new MoneyValueWidget(screenArea.x, screenArea.y, this.notificationSelection, MoneyValue.empty(), this::onValueChanged));
        this.notificationSelection.drawBG = false;
        this.notificationSelection.allowFreeInput = false;
        this.onValueTypeChanged(this.notificationSelection);
        this.notificationSelection.setHandlerChangeListener(this::onValueTypeChanged);
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        this.hideCoinSlots(gui);
        IBankAccount account = ((ATMMenu) this.screen.m_6262_()).getBankAccount();
        if (account != null) {
            TextRenderUtil.drawCenteredMultilineText(gui, this.getRandomNotificationLevelText(), 5, this.screen.getXSize() - 10, 70, 4210752);
        }
    }

    @Nonnull
    private MutableComponent getRandomNotificationLevelText() {
        IBankAccount account = ((ATMMenu) this.screen.m_6262_()).getBankAccount();
        if (account != null) {
            Map<String, MoneyValue> limits = account.getNotificationLevels();
            if (limits.isEmpty()) {
                return LCText.GUI_BANK_NOTIFICATIONS_DISABLED.get();
            } else {
                List<MoneyValue> values = limits.values().stream().toList();
                int displayIndex = (int) (TimeUtil.getCurrentTime() / 2000L % (long) values.size());
                return LCText.GUI_BANK_NOTIFICATIONS_DETAILS.get(((MoneyValue) values.get(displayIndex)).getText());
            }
        } else {
            return EasyText.literal("ERROR!");
        }
    }

    @Override
    protected void closeAction() {
        SimpleSlot.SetActive(this.screen.m_6262_());
    }

    public void onValueChanged(MoneyValue value) {
        if (!value.isEmpty() && !value.isFree()) {
            ((ATMMenu) this.screen.m_6262_()).SetNotificationValueAndUpdate(value.getUniqueName(), value);
        } else {
            String type = this.notificationSelection.getCurrentHandlerType();
            ((ATMMenu) this.screen.m_6262_()).SetNotificationValueAndUpdate(type, MoneyValue.empty());
        }
    }

    public void onValueTypeChanged(@Nonnull MoneyValueWidget widget) {
        IBankAccount account = ((ATMMenu) this.screen.m_6262_()).getBankAccount();
        if (account != null) {
            String type = widget.getCurrentHandlerType();
            MoneyValue currentValue = account.getNotificationLevelFor(type);
            widget.changeValue(currentValue);
        }
    }
}