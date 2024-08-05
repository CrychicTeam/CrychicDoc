package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.trade_rules;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.ITab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.TabButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.trade_rules.TradeRulesTab;
import io.github.lightman314.lightmanscurrency.common.traders.rules.ITradeRuleHost;
import io.github.lightman314.lightmanscurrency.common.traders.rules.TradeRule;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.MutableComponent;

public abstract class TradeRulesClientTab<T extends TradeRulesTab> extends TraderStorageClientTab<T> {

    private int selectedTab = 0;

    private final List<TabButton> tabButtons = new ArrayList();

    private final List<TradeRulesClientSubTab> tabs = new ArrayList();

    private TradeRulesClientSubTab getCurrentTab() {
        if (this.selectedTab < 0 || this.selectedTab >= this.tabs.size()) {
            this.selectedTab = 0;
        }
        return !this.tabs.isEmpty() ? (TradeRulesClientSubTab) this.tabs.get(this.selectedTab) : null;
    }

    protected TradeRulesClientTab(Object screen, T commonTab) {
        super(screen, commonTab);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_TRADE_RULES;
    }

    @Override
    public boolean blockInventoryClosing() {
        return true;
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.tabButtons.clear();
        this.refreshTabs();
        this.getCurrentTab().onOpen();
        this.tick();
        this.menu.SetCoinSlotsActive(false);
    }

    @Override
    public void closeAction() {
        this.menu.SetCoinSlotsActive(true);
    }

    public void refreshTabs() {
        this.tabs.clear();
        this.tabs.add(new RuleToggleTab(this));
        this.selectedTab = 0;
        ITradeRuleHost host = this.commonTab.getHost();
        if (host != null) {
            for (TradeRule rule : host.getRules()) {
                try {
                    this.tabs.add(rule.createTab(this));
                } catch (Throwable var5) {
                    LightmansCurrency.LogError("Trade Rule of type '" + rule.type + "' encountered an error creating its tab. Trade Rule will not be editable!", var5);
                }
            }
        }
        for (TabButton b : this.tabButtons) {
            this.removeChild(b);
        }
        this.tabButtons.clear();
        for (int i = 0; i < this.tabs.size(); i++) {
            int tabIndex = i;
            this.tabButtons.add(this.addChild(new TabButton(b -> this.openTab(tabIndex), (ITab) this.tabs.get(tabIndex))));
        }
    }

    public void openTab(int index) {
        if (index != this.selectedTab && index >= 0 && index < this.tabs.size()) {
            this.getCurrentTab().onClose();
            this.selectedTab = index;
            this.getCurrentTab().onOpen();
        }
    }

    @Override
    public void tick() {
        for (int i = 0; i < this.tabs.size() && i < this.tabButtons.size(); i++) {
            ((TabButton) this.tabButtons.get(i)).f_93624_ = ((TradeRulesClientSubTab) this.tabs.get(i)).isVisible();
            ((TabButton) this.tabButtons.get(i)).f_93623_ = this.selectedTab != i;
        }
        ScreenArea screenArea = this.screen.getArea();
        int yPos = 0;
        for (TabButton button : this.tabButtons) {
            if (button.f_93624_) {
                button.reposition(screenArea.pos.offset(screenArea.width, yPos), 1);
                yPos += 25;
            }
        }
        this.getCurrentTab().tick();
    }

    protected boolean hasBackButton() {
        return false;
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        this.getCurrentTab().renderBG(gui);
    }

    @Override
    public void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        this.getCurrentTab().renderAfterWidgets(gui);
    }

    public static class Trade extends TradeRulesClientTab<TradeRulesTab.Trade> {

        public Trade(Object screen, TradeRulesTab.Trade commonTab) {
            super(screen, commonTab);
        }

        @Override
        public boolean tabButtonVisible() {
            return false;
        }

        public MutableComponent getTooltip() {
            return LCText.TOOLTIP_TRADER_TRADE_RULES_TRADE.get();
        }

        @Override
        public void receiveSelfMessage(LazyPacketData message) {
            this.commonTab.receiveMessage(message);
        }
    }

    public static class Trader extends TradeRulesClientTab<TradeRulesTab.Trader> {

        public Trader(Object screen, TradeRulesTab.Trader commonTab) {
            super(screen, commonTab);
        }

        public MutableComponent getTooltip() {
            return LCText.TOOLTIP_TRADER_TRADE_RULES_TRADER.get();
        }
    }
}