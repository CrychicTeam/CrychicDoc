package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyStorage;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.TabButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.TaxCollectorMenu;
import io.github.lightman314.lightmanscurrency.common.menus.tax_collector.TaxCollectorClientTab;
import io.github.lightman314.lightmanscurrency.common.menus.tax_collector.TaxCollectorTab;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxEntry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TaxCollectorScreen extends EasyMenuScreen<TaxCollectorMenu> {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/tax_collector.png");

    private int currentTab = 0;

    private final List<TaxCollectorClientTab<?>> tabs = new ArrayList();

    private final List<TabButton> tabButtons = new ArrayList();

    public TaxCollectorClientTab<?> getCurrentTab() {
        return this.currentTab >= 0 && this.currentTab < this.tabs.size() ? (TaxCollectorClientTab) this.tabs.get(this.currentTab) : null;
    }

    public TaxCollectorScreen(TaxCollectorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.resize(176, 176);
        ((TaxCollectorMenu) this.f_97732_).setTabChangeListener(this::ChangeTab);
        for (TaxCollectorTab tab : ((TaxCollectorMenu) this.f_97732_).getAllTabs()) {
            if (!(tab.createClientTab(this) instanceof TaxCollectorClientTab<?> clientTab)) {
                throw new RuntimeException("Tab of type " + tab.getClass() + " did not return a Client Tab object!");
            }
            this.tabs.add(clientTab);
        }
    }

    private void ChangeTab(int newTabIndex) {
        if (newTabIndex != this.currentTab) {
            TaxCollectorClientTab<?> oldTab = this.getCurrentTab();
            oldTab.onClose();
            this.currentTab = newTabIndex;
            TaxCollectorClientTab<?> tab = this.getCurrentTab();
            if (tab != null) {
                tab.onOpen();
            }
        }
    }

    public TaxEntry getEntry() {
        return ((TaxCollectorMenu) this.f_97732_).getEntry();
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        this.tabButtons.clear();
        for (TaxCollectorClientTab<?> tab : this.tabs) {
            TabButton button = this.addChild(new TabButton(this::TabButtonClick, tab));
            this.tabButtons.add(button);
        }
        this.tickTabButtons();
        this.addChild(IconAndButtonUtil.collectCoinButtonAlt(screenArea.pos.offset(screenArea.width, 0), b -> ((TaxCollectorMenu) this.f_97732_).CollectStoredMoney(), this::getMoneyStorage).withAddons(EasyAddonHelper.visibleCheck(this::storedMoneyVisible), EasyAddonHelper.activeCheck(this::storedMoneyActive)));
        try {
            this.getCurrentTab().onOpen();
        } catch (Throwable var5) {
        }
    }

    private MoneyStorage getMoneyStorage() {
        TaxEntry entry = ((TaxCollectorMenu) this.f_97732_).getEntry();
        return entry != null ? entry.getStoredMoney() : null;
    }

    private void tickTabButtons() {
        int xPos = this.f_97735_ - 25;
        int yPos = this.f_97736_;
        for (int i = 0; i < this.tabButtons.size(); i++) {
            TabButton button = (TabButton) this.tabButtons.get(i);
            if (i > this.tabs.size()) {
                button.setVisible(false);
            } else {
                TaxCollectorClientTab<?> tab = (TaxCollectorClientTab<?>) this.tabs.get(i);
                button.setVisible(tab.commonTab.canBeAccessed());
                if (button.isVisible()) {
                    button.setActive(this.currentTab != i);
                    button.reposition(xPos, yPos, 3);
                    yPos += 25;
                }
            }
        }
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        this.tickTabButtons();
        gui.renderNormalBackground(GUI_TEXTURE, this);
        TaxEntry entry = this.getEntry();
        if (entry != null) {
            try {
                this.getCurrentTab().renderBG(gui);
            } catch (Throwable var4) {
                LightmansCurrency.LogError("Error rendering tab BG!", var4);
            }
        }
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        try {
            this.getCurrentTab().renderAfterWidgets(gui);
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error rendering tab Tooltips!", var3);
        }
    }

    @Override
    public boolean blockInventoryClosing() {
        try {
            return this.getCurrentTab().blockInventoryClosing();
        } catch (Throwable var2) {
            return super.blockInventoryClosing();
        }
    }

    @Override
    protected void screenTick() {
        try {
            this.getCurrentTab().tick();
        } catch (Throwable var2) {
            LightmansCurrency.LogError("Error ticking tab!", var2);
        }
    }

    private boolean storedMoneyVisible() {
        TaxEntry entry = this.getEntry();
        return entry != null && (!entry.getStoredMoney().isEmpty() || !entry.isLinkedToBank());
    }

    private boolean storedMoneyActive() {
        TaxEntry entry = this.getEntry();
        return entry != null && !entry.getStoredMoney().isEmpty();
    }

    private void TabButtonClick(EasyButton button) {
        if (button instanceof TabButton) {
            int tabIndex = this.tabButtons.indexOf(button);
            if (tabIndex >= 0) {
                ((TaxCollectorMenu) this.f_97732_).ChangeTab(tabIndex, true);
            }
        }
    }
}