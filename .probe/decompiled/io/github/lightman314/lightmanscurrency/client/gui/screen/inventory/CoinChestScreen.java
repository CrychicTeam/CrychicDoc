package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.coin_chest.CoinChestTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.coin_chest.DefaultTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.ITab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.TabButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinChestBlockEntity;
import io.github.lightman314.lightmanscurrency.common.menus.CoinChestMenu;
import io.github.lightman314.lightmanscurrency.common.upgrades.types.coin_chest.CoinChestUpgradeData;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class CoinChestScreen extends EasyMenuScreen<CoinChestMenu> {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/coin_chest.png");

    int currentTabIndex = 0;

    List<CoinChestTab> tabs = Lists.newArrayList(new CoinChestTab[] { new DefaultTab(this) });

    List<TabButton> tabButtons = new ArrayList();

    public final CoinChestBlockEntity be;

    public List<CoinChestTab> getTabs() {
        return this.tabs;
    }

    public CoinChestTab currentTab() {
        return (CoinChestTab) this.tabs.get(this.currentTabIndex);
    }

    public CoinChestScreen(CoinChestMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.be = ((CoinChestMenu) this.f_97732_).be;
        ((CoinChestMenu) this.f_97732_).AddExtraHandler(this::ClientMessageHandler);
        this.resize(176, 243);
    }

    private void safeAddTab(Object tab) {
        if (tab instanceof CoinChestTab t) {
            this.tabs.add(t);
        }
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        this.refreshTabs(false);
        this.currentTab().onOpen();
    }

    private void refreshTabs(boolean clearButtons) {
        Class<?> oldTabType = null;
        if (clearButtons) {
            this.currentTab().onClose();
            for (TabButton tab : this.tabButtons) {
                this.removeChild(tab);
            }
            oldTabType = this.currentTab().getClass();
        }
        this.tabs = Lists.newArrayList(new CoinChestTab[] { new DefaultTab(this) });
        UnmodifiableIterator var5 = ((CoinChestMenu) this.f_97732_).be.getChestUpgrades().iterator();
        while (var5.hasNext()) {
            CoinChestUpgradeData data = (CoinChestUpgradeData) var5.next();
            data.upgrade.addClientTabs(data, this, this::safeAddTab);
        }
        if (oldTabType != null) {
            for (int i = 0; i < this.tabs.size() && oldTabType != null; i++) {
                if (((CoinChestTab) this.tabs.get(i)).getClass() == oldTabType) {
                    this.currentTabIndex = i;
                    oldTabType = null;
                }
            }
            if (oldTabType != null) {
                this.currentTabIndex = 0;
            }
        }
        this.tabButtons = new ArrayList();
        for (int ix = 0; ix < this.tabs.size(); ix++) {
            TabButton button = this.addChild(new TabButton(this::clickedOnTab, (ITab) this.tabs.get(ix)));
            if (ix == 0) {
                button.hideTooltip = true;
            }
            button.f_93623_ = ix != this.currentTabIndex;
            this.tabButtons.add(button);
        }
        this.validateTabVisiblity();
        this.validateSlotVisibility();
        this.currentTab().onOpen();
    }

    public void validateTabVisiblity() {
        int y = 0;
        for (int i = 0; i < this.tabButtons.size() && i < this.tabs.size(); i++) {
            TabButton button = (TabButton) this.tabButtons.get(i);
            CoinChestTab tab = (CoinChestTab) this.tabs.get(i);
            button.f_93624_ = tab.isVisible();
            if (button.f_93624_) {
                button.reposition(this.f_97735_ - 25, this.f_97736_ + y++ * 25, 3);
            }
        }
    }

    public void validateSlotVisibility() {
        ((CoinChestMenu) this.f_97732_).SetUpgradeSlotVisibility(this.currentTab().upgradeSlotsVisible());
        ((CoinChestMenu) this.f_97732_).SetCoinSlotVisibility(this.currentTab().coinSlotsVisible());
        ((CoinChestMenu) this.f_97732_).SetInventoryVisibility(this.currentTab().inventoryVisible());
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.renderNormalBackground(GUI_TEXTURE, this);
        for (Slot s : ((CoinChestMenu) this.f_97732_).f_38839_) {
            if (s.isActive()) {
                gui.blit(GUI_TEXTURE, s.x - 1, s.y - 1, 176, 0, 18, 18);
            }
        }
        try {
            this.currentTab().renderBG(gui);
        } catch (Throwable var4) {
            LightmansCurrency.LogError("Error rendering " + this.currentTab().getClass().getName() + " tab.", var4);
        }
        if (this.currentTab().titleVisible()) {
            gui.drawString(this.f_96539_, 8, 6, 4210752);
        }
        if (this.currentTab().inventoryVisible()) {
            gui.drawString(this.f_169604_, 8, this.getYSize() - 94, 4210752);
        }
    }

    @Override
    protected void renderTick() {
        this.validateTabVisiblity();
        if (!this.currentTab().isVisible()) {
            this.changeTab(0);
        }
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        try {
            this.currentTab().renderAfterWidgets(gui);
        } catch (Exception var3) {
            LightmansCurrency.LogError("Error rendering " + this.currentTab().getClass().getName() + " tab.", var3);
        }
    }

    public void changeTab(int tabIndex) {
        this.currentTab().onClose();
        ((TabButton) this.tabButtons.get(this.currentTabIndex)).f_93623_ = true;
        this.currentTabIndex = MathUtil.clamp(tabIndex, 0, this.tabs.size() - 1);
        ((TabButton) this.tabButtons.get(this.currentTabIndex)).f_93623_ = false;
        this.currentTab().onOpen();
        this.validateSlotVisibility();
    }

    private void clickedOnTab(EasyButton tab) {
        int tabIndex = -1;
        if (tab instanceof TabButton) {
            tabIndex = this.tabButtons.indexOf(tab);
        }
        if (tabIndex >= 0) {
            this.changeTab(tabIndex);
        }
    }

    private void ClientMessageHandler(LazyPacketData message) {
        if (message.contains("RefreshTabs")) {
            this.refreshTabs(true);
        }
    }

    @Override
    public boolean blockInventoryClosing() {
        return this.currentTab().blockInventoryClosing();
    }
}