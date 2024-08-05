package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderStorageScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.TradeButtonArea;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.trades_basic.BasicTradeEditTab;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;

public class BasicTradeEditClientTab<T extends BasicTradeEditTab> extends TraderStorageClientTab<T> implements TradeButtonArea.InteractionConsumer {

    TradeButtonArea tradeDisplay;

    EasyButton buttonAddTrade;

    EasyButton buttonRemoveTrade;

    public BasicTradeEditClientTab(Object screen, T commonTab) {
        super(screen, commonTab);
        this.commonTab.setClient(((TraderStorageScreen) screen)::selfMessage);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_TRADELIST;
    }

    @Override
    public Component getTooltip() {
        return LCText.TOOLTIP_TRADER_EDIT_TRADES.get();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.tradeDisplay = this.addChild(new TradeButtonArea(this.menu::getTrader, t -> this.menu.getContext(), screenArea.x + 3, screenArea.y + 17, screenArea.width - 6, 100, (t1, t2) -> {
        }, this.menu.getTrader() == null ? TradeButtonArea.FILTER_ANY : this.menu.getTrader().getStorageDisplayFilter(this.menu)));
        this.tradeDisplay.setInteractionConsumer(this);
        this.tradeDisplay.withTitle(screenArea.pos.offset(6, 6), screenArea.width - (this.renderAddRemoveButtons() ? 32 : 16), true);
        this.buttonAddTrade = this.addChild(IconAndButtonUtil.plusButton(screenArea.pos.offset(screenArea.width - 25, 4), this::AddTrade));
        this.buttonRemoveTrade = this.addChild(IconAndButtonUtil.minusButton(screenArea.pos.offset(screenArea.width - 14, 4), this::RemoveTrade));
        this.tick();
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
    }

    private boolean renderAddRemoveButtons() {
        return this.menu.getTrader() != null ? this.menu.getTrader().canEditTradeCount() : false;
    }

    @Override
    public void tick() {
        TraderData trader = this.menu.getTrader();
        if (trader != null) {
            this.buttonAddTrade.f_93624_ = this.buttonRemoveTrade.f_93624_ = trader.canEditTradeCount();
            this.buttonAddTrade.f_93623_ = trader.getTradeCount() < trader.getMaxTradeCount();
            this.buttonRemoveTrade.f_93623_ = trader.getTradeCount() > 1;
        } else {
            this.buttonAddTrade.f_93624_ = this.buttonRemoveTrade.f_93624_ = false;
        }
    }

    @Override
    public void onTradeButtonInputInteraction(TraderData trader, TradeData trade, int index, int mouseButton) {
        if (trader.hasPermission(this.menu.getPlayer(), "editTrades")) {
            trade.OnInputDisplayInteraction(this.commonTab, this.screen::selfMessage, index, mouseButton, this.menu.getHeldItem());
        } else {
            Permissions.PermissionWarning(this.menu.getPlayer(), "edit trade", "editTrades");
        }
    }

    @Override
    public void onTradeButtonOutputInteraction(TraderData trader, TradeData trade, int index, int mouseButton) {
        if (trader.hasPermission(this.menu.getPlayer(), "editTrades")) {
            trade.OnOutputDisplayInteraction(this.commonTab, this.screen::selfMessage, index, mouseButton, this.menu.getHeldItem());
        }
    }

    @Override
    public void onTradeButtonInteraction(TraderData trader, TradeData trade, int localMouseX, int localMouseY, int mouseButton) {
        if (trader.hasPermission(this.menu.getPlayer(), "editTrades")) {
            trade.OnInteraction(this.commonTab, this.screen::selfMessage, localMouseX, localMouseY, mouseButton, this.menu.getHeldItem());
        } else {
            Permissions.PermissionWarning(this.menu.getPlayer(), "edit trade", "editTrades");
        }
    }

    private void AddTrade(EasyButton button) {
        this.commonTab.addTrade();
    }

    private void RemoveTrade(EasyButton button) {
        this.commonTab.removeTrade();
    }
}