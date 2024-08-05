package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.trader.common;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.ITraderSource;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.trader.TraderClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.TradeButtonArea;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.TradeButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketExecuteTrade;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.world.inventory.Slot;

public class TraderInteractionTab extends TraderClientTab {

    TradeButtonArea tradeDisplay;

    private static long lastPress = 0L;

    public TraderInteractionTab(TraderScreen screen) {
        super(screen);
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.tradeDisplay = this.addChild(new TradeButtonArea(this.menu::getTraderSource, this.menu::getContext, screenArea.x + 3, screenArea.y + 17, screenArea.width - 6, 100, this::OnButtonPress, TradeButtonArea.FILTER_VALID));
        this.tradeDisplay.withTitle(screenArea.pos.offset(8, 6), screenArea.width - 16, true);
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        TradeButton hoveredButton = this.tradeDisplay.getHoveredButton(gui.mousePos);
        if (hoveredButton != null) {
            gui.resetColor();
            TradeData trade = hoveredButton.getTrade();
            TradeContext context = hoveredButton.getContext();
            for (int s : trade.getRelevantInventorySlots(context, this.menu.getSlots())) {
                if (s >= 0 && s < this.menu.getSlots().size()) {
                    Slot slot = (Slot) this.menu.getSlots().get(s);
                    gui.blit(TraderScreen.GUI_TEXTURE, slot.x - 1, slot.y - 1, this.screen.getXSize(), 24, 18, 18);
                }
            }
        }
    }

    private void OnButtonPress(TraderData trader, TradeData trade) {
        if (trader != null && trade != null) {
            if (!TimeUtil.compareTime(10L, lastPress)) {
                lastPress = TimeUtil.getCurrentTime();
                ITraderSource ts = this.menu.getTraderSource();
                if (ts == null) {
                    this.menu.getPlayer().closeContainer();
                } else {
                    List<TraderData> traders = ts.getTraders();
                    int ti = traders.indexOf(trader);
                    if (ti >= 0) {
                        TraderData t = (TraderData) traders.get(ti);
                        if (t != null) {
                            int tradeIndex = t.getTradeData().indexOf(trade);
                            if (tradeIndex >= 0) {
                                new CPacketExecuteTrade(ti, tradeIndex).send();
                            }
                        }
                    }
                }
            }
        }
    }
}