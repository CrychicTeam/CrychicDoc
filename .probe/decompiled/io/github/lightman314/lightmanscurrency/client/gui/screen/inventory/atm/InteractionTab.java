package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyViewer;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.ATMScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.BankAccountWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.menus.ATMMenu;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class InteractionTab extends ATMTab implements BankAccountWidget.IBankAccountWidget {

    BankAccountWidget accountWidget;

    public InteractionTab(ATMScreen screen) {
        super(screen);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.of(ModBlocks.COINPILE_GOLD);
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_ATM_INTERACT.get();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.accountWidget = this.addChild(new BankAccountWidget(screenArea.y, this, 14, this::addChild));
        this.accountWidget.getAmountSelection().drawBG = false;
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        Component accountName = EasyText.literal("ERROR FINDING ACCOUNT");
        IBankAccount account = this.getBankAccount();
        if (account != null) {
            accountName = account.getName();
        }
        gui.drawString(accountName, 8, 75, 4210752);
        this.accountWidget.renderInfo(gui);
    }

    @Override
    public Screen getScreen() {
        return this.screen;
    }

    @Override
    public IBankAccount getBankAccount() {
        return ((ATMMenu) this.screen.m_6262_()).getBankAccount();
    }

    @Override
    public IMoneyViewer getCoinAccess() {
        return ((ATMMenu) this.screen.m_6262_()).getMoneyHandler();
    }
}