package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.coins.atm.ATMAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.atm.data.ATMPageManager;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.ATMScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.menus.ATMMenu;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.MutableComponent;

public class ExchangeTab extends ATMTab {

    private final ATMPageManager exchangeData;

    public ExchangeTab(ATMScreen screen) {
        super(screen);
        this.exchangeData = ATMAPI.getATMPageManager(((ATMMenu) screen.m_6262_()).player, this::addChild, this::removeChild, this::RunExchangeCommand);
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.exchangeData.initialize(screenArea);
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.drawString(this.getTooltip(), 8, 6, 4210752);
    }

    private void RunExchangeCommand(String command) {
        ((ATMMenu) this.screen.m_6262_()).SendCoinExchangeMessage(command);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.of(ModBlocks.ATM);
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_ATM_EXCHANGE.get();
    }
}