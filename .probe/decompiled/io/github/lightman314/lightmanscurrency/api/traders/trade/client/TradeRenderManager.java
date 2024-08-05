package io.github.lightman314.lightmanscurrency.api.traders.trade.client;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.events.TradeEvent;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.AlertData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayEntry;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidget;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

@OnlyIn(Dist.CLIENT)
public abstract class TradeRenderManager<T extends TradeData> {

    public final T trade;

    protected TradeRenderManager(T trade) {
        this.trade = trade;
    }

    public abstract int tradeButtonWidth(TradeContext var1);

    public abstract LazyOptional<ScreenPosition> arrowPosition(TradeContext var1);

    public ScreenPosition alertPosition(TradeContext context) {
        return this.arrowPosition(context).orElseGet(() -> ScreenPosition.ZERO);
    }

    public abstract DisplayData inputDisplayArea(TradeContext var1);

    public abstract List<DisplayEntry> getInputDisplays(TradeContext var1);

    public abstract DisplayData outputDisplayArea(TradeContext var1);

    public abstract List<DisplayEntry> getOutputDisplays(TradeContext var1);

    public final List<AlertData> getAlertData(TradeContext context) {
        if (context.isStorageMode) {
            return null;
        } else {
            List<AlertData> alerts = new ArrayList();
            this.addTradeRuleAlertData(alerts, context);
            if (context.hasTrader() && context.getTrader().exceedsAcceptableTaxRate()) {
                alerts.add(AlertData.error(LCText.TOOLTIP_TAX_LIMIT.get()));
            }
            this.getAdditionalAlertData(context, alerts);
            return alerts;
        }
    }

    private void addTradeRuleAlertData(List<AlertData> alerts, TradeContext context) {
        if (context.hasTrader() && context.hasPlayerReference()) {
            TradeEvent.PreTradeEvent pte = context.getTrader().runPreTradeEvent(this.trade, context);
            alerts.addAll(pte.getAlertInfo());
        }
    }

    protected abstract void getAdditionalAlertData(TradeContext var1, List<AlertData> var2);

    public void renderAdditional(EasyWidget button, EasyGuiGraphics gui, TradeContext context) {
    }

    public List<Component> getAdditionalTooltips(TradeContext context, int mouseX, int mouseY) {
        return null;
    }

    public final MutableComponent getStockTooltip(boolean isCreative, int stockCount) {
        return LCText.TOOLTIP_TRADE_INFO_STOCK.get(isCreative ? LCText.TOOLTIP_TRADE_INFO_STOCK_INFINITE.getWithStyle(ChatFormatting.GOLD) : EasyText.literal(String.valueOf(stockCount)).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GOLD);
    }
}