package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm.ATMTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm.ExchangeTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm.InteractionTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm.LogTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm.NotificationTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm.SelectionTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm.TransferTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.ITab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.TabButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.ATMMenu;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ATMScreen extends EasyMenuScreen<ATMMenu> {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/atm.png");

    public static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/atm_buttons.png");

    int currentTabIndex = 0;

    List<ATMTab> tabs = ImmutableList.of(new ExchangeTab(this), new SelectionTab(this), new InteractionTab(this), new NotificationTab(this), new LogTab(this), new TransferTab(this));

    List<TabButton> tabButtons = new ArrayList();

    boolean logError = true;

    public List<ATMTab> getTabs() {
        return this.tabs;
    }

    public ATMTab currentTab() {
        return (ATMTab) this.tabs.get(this.currentTabIndex);
    }

    public ATMScreen(ATMMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.resize(176, 243);
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        this.tabButtons = new ArrayList();
        for (int i = 0; i < this.tabs.size(); i++) {
            TabButton button = this.addChild(new TabButton(this::clickedOnTab, (ITab) this.tabs.get(i)));
            button.reposition(this.f_97735_ - 25, this.f_97736_ + i * 25, 3);
            button.f_93623_ = i != this.currentTabIndex;
            this.tabButtons.add(button);
        }
        this.currentTab().onOpen();
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.renderNormalBackground(GUI_TEXTURE, this);
        try {
            this.currentTab().renderBG(gui);
        } catch (Throwable var3) {
            if (this.logError) {
                LightmansCurrency.LogError("Error rendering " + this.currentTab().getClass().getName() + " tab.", var3);
                this.logError = false;
            }
        }
        gui.drawString(this.f_169604_, 8, this.getYSize() - 94, 4210752);
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        try {
            this.currentTab().renderAfterWidgets(gui);
        } catch (Throwable var3) {
            if (this.logError) {
                LightmansCurrency.LogError("Error rendering " + this.currentTab().getClass().getName() + " tab.", var3);
                this.logError = false;
            }
        }
    }

    public void changeTab(int tabIndex) {
        this.currentTab().onClose();
        ((TabButton) this.tabButtons.get(this.currentTabIndex)).f_93623_ = true;
        this.currentTabIndex = MathUtil.clamp(tabIndex, 0, this.tabs.size() - 1);
        ((TabButton) this.tabButtons.get(this.currentTabIndex)).f_93623_ = false;
        this.currentTab().onOpen();
        this.logError = true;
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

    @Override
    public boolean blockInventoryClosing() {
        return this.currentTab().blockInventoryClosing();
    }
}