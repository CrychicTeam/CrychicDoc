package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageScreen;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.TabButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.util.LazyWidgetPositioner;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.TraderStorageMenu;
import io.github.lightman314.lightmanscurrency.common.menus.slots.CoinSlot;
import io.github.lightman314.lightmanscurrency.common.traders.permissions.Permissions;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketCollectCoins;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketOpenTrades;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketStoreCoins;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.util.NonNullSupplier;

public class TraderStorageScreen extends EasyMenuScreen<TraderStorageMenu> implements ITraderStorageScreen {

    private final Map<Integer, TraderStorageClientTab<?>> availableTabs = new HashMap();

    Map<Integer, TabButton> tabButtons = new HashMap();

    EasyButton buttonShowTrades;

    EasyButton buttonCollectMoney;

    EasyButton buttonStoreMoney;

    EasyButton buttonTradeRules;

    public final LazyWidgetPositioner leftEdgePositioner = LazyWidgetPositioner.create(this, LazyWidgetPositioner.MODE_BOTTOMUP, -20, 216, 20);

    public TraderStorageClientTab<?> currentTab() {
        return (TraderStorageClientTab<?>) this.availableTabs.get(((TraderStorageMenu) this.f_97732_).getCurrentTabIndex());
    }

    public TraderStorageScreen(TraderStorageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.resize(206, 236);
        ((TraderStorageMenu) this.f_97732_).getAllTabs().forEach((key, tab) -> {
            try {
                if (tab.createClientTab(this) instanceof TraderStorageClientTab<?> ct) {
                    this.availableTabs.put(key, ct);
                } else {
                    LightmansCurrency.LogError("Common tab of type '" + tab.getClass().getName() + "' did not create a valid client tab!");
                }
            } catch (Throwable var5) {
                LightmansCurrency.LogError("Error initializing the Trader Storage Client Tabs!", var5);
            }
        });
        if (this.availableTabs.isEmpty()) {
            LightmansCurrency.LogError("No client tabs were created for the Trader Storage Screen!");
        } else {
            LightmansCurrency.LogDebug("Storage Screen created with " + this.availableTabs.size() + " client tabs.");
        }
        menu.addListener(this::serverMessage);
    }

    @Override
    public void initialize(ScreenArea screenArea) {
        this.leftEdgePositioner.clear();
        this.addChild(this.leftEdgePositioner);
        this.tabButtons.clear();
        this.availableTabs.forEach((key, tab) -> {
            TabButton newButton = this.addChild(new TabButton(button -> this.changeTab(key), tab));
            if (key == ((TraderStorageMenu) this.f_97732_).getCurrentTabIndex()) {
                newButton.f_93623_ = false;
            }
            this.tabButtons.put(key, newButton);
        });
        if (this.availableTabs.isEmpty()) {
            LightmansCurrency.LogError("NO CLIENT TABS WERE INITIALIZED!");
        }
        this.tickTabButtons();
        this.buttonShowTrades = this.addChild(IconAndButtonUtil.traderButton(0, 0, this::PressTradesButton));
        this.buttonCollectMoney = this.addChild(IconAndButtonUtil.collectCoinButton(0, 0, this::PressCollectionButton, ((TraderStorageMenu) this.f_97732_).player, ((TraderStorageMenu) this.f_97732_)::getTrader));
        this.buttonStoreMoney = this.addChild(IconAndButtonUtil.storeCoinButton(this.f_97735_ + 71, this.f_97736_ + 120, this::PressStoreCoinsButton).withAddons(EasyAddonHelper.visibleCheck((NonNullSupplier<Boolean>) (() -> ((TraderStorageMenu) this.f_97732_).HasCoinsToAdd() && ((TraderStorageMenu) this.f_97732_).hasPermission("storeCoins") && ((TraderStorageMenu) this.f_97732_).areCoinSlotsVisible()))));
        this.buttonTradeRules = this.addChild(IconAndButtonUtil.tradeRuleButton(this.f_97735_ + this.f_97726_, this.f_97736_, this::PressTradeRulesButton).withAddons(EasyAddonHelper.visibleCheck((NonNullSupplier<Boolean>) (() -> ((TraderStorageMenu) this.f_97732_).hasPermission("editTradeRules") && this.currentTab().getTradeRuleTradeIndex() >= 0))));
        this.leftEdgePositioner.addWidgets(this.buttonShowTrades, this.buttonCollectMoney);
        TraderData trader = ((TraderStorageMenu) this.f_97732_).getTrader();
        if (trader != null) {
            trader.onStorageScreenInit(this, this::addChild);
        }
        this.currentTab().onOpen();
        this.m_181908_();
    }

    private void tickTabButtons() {
        int xPos = this.f_97735_ - 25;
        int index = 0;
        List<Pair<Integer, TabButton>> sortedButtons = new ArrayList();
        this.tabButtons.forEach((keyx, buttonx) -> sortedButtons.add(Pair.of(keyx, buttonx)));
        sortedButtons.sort(Comparator.comparingInt(Pair::getFirst));
        for (Pair<Integer, TabButton> buttonPair : sortedButtons) {
            int key = (Integer) buttonPair.getFirst();
            TabButton button = (TabButton) buttonPair.getSecond();
            TraderStorageClientTab<?> tab = (TraderStorageClientTab<?>) this.availableTabs.get(key);
            button.f_93624_ = tab != null && tab.tabButtonVisible() && tab.commonTab.canOpen(((TraderStorageMenu) this.f_97732_).player);
            if (button.f_93624_) {
                int yPos = this.f_97736_ + 25 * index++;
                button.reposition(xPos, yPos, 3);
            }
        }
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        if (((TraderStorageMenu) this.f_97732_).getTrader() == null) {
            this.m_7379_();
        } else {
            this.tickTabButtons();
            gui.renderNormalBackground(TraderScreen.GUI_TEXTURE, this);
            for (CoinSlot slot : ((TraderStorageMenu) this.f_97732_).getCoinSlots()) {
                if (slot.m_6659_()) {
                    gui.blit(TraderScreen.GUI_TEXTURE, slot.f_40220_ - 1, slot.f_40221_ - 1, this.f_97726_, 0, 18, 18);
                }
            }
            if (this.currentTab() != null) {
                try {
                    this.currentTab().renderBG(gui);
                } catch (Throwable var4) {
                    LightmansCurrency.LogError("Error rendering trader storage tab " + this.currentTab().getClass().getName(), var4);
                }
            }
            if (this.currentTab().shouldRenderInventoryText()) {
                gui.drawString(this.f_169604_, 23, this.f_97727_ - 94, 4210752);
            }
        }
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        if (((TraderStorageMenu) this.f_97732_).getTrader() != null) {
            try {
                this.currentTab().renderAfterWidgets(gui);
            } catch (Throwable var3) {
                LightmansCurrency.LogError("Error rendering trader storage tab tooltips " + this.currentTab().getClass().getName(), var3);
            }
        }
    }

    @Override
    public void screenTick() {
        if (((TraderStorageMenu) this.f_97732_).getTrader() == null) {
            this.m_7379_();
        } else {
            ((TraderStorageMenu) this.f_97732_).validateCoinSlots();
            if (!((TraderStorageMenu) this.f_97732_).hasPermission("openStorage")) {
                this.m_7379_();
                new CPacketOpenTrades(((TraderStorageMenu) this.f_97732_).getTrader().getID()).send();
            } else {
                if (!this.currentTab().commonTab.canOpen(((TraderStorageMenu) this.f_97732_).player)) {
                    this.changeTab(0);
                }
            }
        }
    }

    @Override
    public boolean blockInventoryClosing() {
        return this.currentTab().blockInventoryClosing();
    }

    private TabButton getTabButton(int key) {
        return this.tabButtons.containsKey(key) ? (TabButton) this.tabButtons.get(key) : null;
    }

    @Override
    public void changeTab(int newTab) {
        this.changeTab(newTab, true, null);
    }

    @Override
    public void changeTab(int newTab, boolean sendMessage, @Nullable LazyPacketData.Builder selfMessage) {
        if (newTab != ((TraderStorageMenu) this.f_97732_).getCurrentTabIndex()) {
            int oldTab = ((TraderStorageMenu) this.f_97732_).getCurrentTabIndex();
            this.currentTab().onClose();
            TabButton button = this.getTabButton(((TraderStorageMenu) this.f_97732_).getCurrentTabIndex());
            if (button != null) {
                button.f_93623_ = true;
            }
            ((TraderStorageMenu) this.f_97732_).changeTab(newTab);
            button = this.getTabButton(((TraderStorageMenu) this.f_97732_).getCurrentTabIndex());
            if (button != null) {
                button.f_93623_ = false;
            }
            if (selfMessage != null) {
                LazyPacketData message = selfMessage.build();
                this.currentTab().receiveSelfMessage(message);
            }
            this.currentTab().onOpen();
            if (oldTab != ((TraderStorageMenu) this.f_97732_).getCurrentTabIndex() && sendMessage) {
                ((TraderStorageMenu) this.f_97732_).SendMessage(((TraderStorageMenu) this.f_97732_).createTabChangeMessage(newTab, selfMessage));
            }
        }
    }

    @Override
    public void selfMessage(@Nonnull LazyPacketData.Builder message) {
        LazyPacketData bm = message.build();
        if (bm.contains("ChangeTab", (byte) 2)) {
            this.changeTab(bm.getInt("ChangeTab"), false, message);
        } else {
            this.currentTab().receiveSelfMessage(message.build());
        }
    }

    public void serverMessage(LazyPacketData message) {
        this.currentTab().receiveServerMessage(message);
    }

    private void PressTradesButton(EasyButton button) {
        new CPacketOpenTrades(((TraderStorageMenu) this.f_97732_).getTrader().getID()).send();
    }

    private void PressCollectionButton(EasyButton button) {
        if (((TraderStorageMenu) this.f_97732_).hasPermission("collectCoins")) {
            CPacketCollectCoins.sendToServer();
        } else {
            Permissions.PermissionWarning(((TraderStorageMenu) this.f_97732_).player, "collect stored coins", "collectCoins");
        }
    }

    private void PressStoreCoinsButton(EasyButton button) {
        if (((TraderStorageMenu) this.f_97732_).hasPermission("storeCoins")) {
            CPacketStoreCoins.sendToServer();
        } else {
            Permissions.PermissionWarning(((TraderStorageMenu) this.f_97732_).player, "store coins", "storeCoins");
        }
    }

    private void PressTradeRulesButton(EasyButton button) {
        if (this.currentTab().getTradeRuleTradeIndex() >= 0) {
            this.changeTab(101, true, LazyPacketData.simpleInt("TradeIndex", this.currentTab().getTradeRuleTradeIndex()));
        }
    }
}