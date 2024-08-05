package io.github.lightman314.lightmanscurrency.common.traders.paygate.tradedata.client;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.trade.client.TradeRenderManager;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.AlertData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayEntry;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.items.TicketItem;
import io.github.lightman314.lightmanscurrency.common.traders.paygate.PaygateTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.paygate.tradedata.PaygateTradeData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

@OnlyIn(Dist.CLIENT)
public class PaygateTradeButtonRenderer extends TradeRenderManager<PaygateTradeData> {

    public PaygateTradeButtonRenderer(PaygateTradeData trade) {
        super(trade);
    }

    @Override
    public int tradeButtonWidth(TradeContext context) {
        return 94;
    }

    @Override
    public LazyOptional<ScreenPosition> arrowPosition(TradeContext context) {
        return ScreenPosition.ofOptional(36, 1);
    }

    @Override
    public DisplayData inputDisplayArea(TradeContext context) {
        return new DisplayData(1, 1, 34, 16);
    }

    @Override
    public List<DisplayEntry> getInputDisplays(TradeContext context) {
        return this.trade.isTicketTrade() ? Lists.newArrayList(new DisplayEntry[] { DisplayEntry.of(TicketItem.CreateTicket(this.trade.getTicketItem(), this.trade.getTicketID(), this.trade.getTicketColor()), 1, LCText.TOOLTIP_TICKET_ID.getAsList(this.trade.getTicketID())) }) : Lists.newArrayList(new DisplayEntry[] { DisplayEntry.of(this.trade.getCost(context), context.isStorageMode ? LCText.TOOLTIP_TRADE_EDIT_PRICE.getAsList() : null) });
    }

    @Override
    public DisplayData outputDisplayArea(TradeContext context) {
        return new DisplayData(58, 1, 34, 16);
    }

    @Override
    public List<DisplayEntry> getOutputDisplays(TradeContext context) {
        return Lists.newArrayList(new DisplayEntry[] { DisplayEntry.of(PaygateTradeData.formatDurationDisplay(this.trade.getDuration()), TextRenderUtil.TextFormatting.create(), Lists.newArrayList(new Component[] { PaygateTradeData.formatDuration(this.trade.getDuration()) })) });
    }

    @Override
    protected void getAdditionalAlertData(TradeContext context, List<AlertData> alerts) {
        if (context.hasTrader() && context.getTrader() instanceof PaygateTraderData paygate) {
            if (paygate.isActive()) {
                alerts.add(AlertData.warn(LCText.TOOLTIP_TRADER_PAYGATE_ALREADY_ACTIVE));
            }
            if (!this.trade.canAfford(context)) {
                alerts.add(AlertData.warn(LCText.TOOLTIP_CANNOT_AFFORD));
            }
        }
    }
}