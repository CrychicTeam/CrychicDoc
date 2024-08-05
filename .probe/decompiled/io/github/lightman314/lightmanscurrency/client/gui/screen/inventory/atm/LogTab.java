package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.ATMScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.notifications.NotificationDisplayWidget;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.ATMMenu;
import io.github.lightman314.lightmanscurrency.common.menus.slots.SimpleSlot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;

public class LogTab extends ATMTab {

    NotificationDisplayWidget logWidget;

    public LogTab(ATMScreen screen) {
        super(screen);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.of(Items.WRITABLE_BOOK);
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_ATM_LOGS.get();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        SimpleSlot.SetInactive(this.screen.m_6262_());
        this.logWidget = this.addChild(new NotificationDisplayWidget(screenArea.x + 7, screenArea.y + 15, screenArea.width - 14, 6, this::getNotifications));
        this.logWidget.backgroundColor = 0;
    }

    private List<Notification> getNotifications() {
        IBankAccount ba = ((ATMMenu) this.screen.m_6262_()).getBankAccount();
        return (List<Notification>) (ba != null ? ba.getNotifications() : new ArrayList());
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        this.hideCoinSlots(gui);
        gui.drawString(this.getTooltip(), 8, 6, 4210752);
    }

    @Override
    protected void closeAction() {
        SimpleSlot.SetActive(this.screen.m_6262_());
    }
}