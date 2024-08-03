package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.trader_interface.blockentity.TraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.api.trader_interface.menu.TraderInterfaceClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.TabButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TraderInterfaceScreen extends EasyMenuScreen<TraderInterfaceMenu> {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/trader_interface.png");

    public static final int WIDTH = 206;

    public static final int HEIGHT = 236;

    Map<Integer, TraderInterfaceClientTab<?>> availableTabs = new HashMap();

    Map<Integer, TabButton> tabButtons = new HashMap();

    IconButton modeToggle;

    IconButton onlineModeToggle;

    public TraderInterfaceClientTab<?> currentTab() {
        return (TraderInterfaceClientTab<?>) this.availableTabs.get(((TraderInterfaceMenu) this.f_97732_).getCurrentTabIndex());
    }

    public TraderInterfaceScreen(TraderInterfaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        ((TraderInterfaceMenu) this.f_97732_).getAllTabs().forEach((key, tab) -> this.availableTabs.put(key, tab.createClientTab(this)));
        this.resize(206, 236);
    }

    @Override
    public void initialize(ScreenArea screenArea) {
        this.tabButtons.clear();
        this.availableTabs.forEach((key, tab) -> {
            TabButton newButton = this.addChild(new TabButton(button -> this.changeTab(key), tab));
            if (key == ((TraderInterfaceMenu) this.f_97732_).getCurrentTabIndex()) {
                newButton.f_93623_ = false;
            }
            this.tabButtons.put(key, newButton);
        });
        this.modeToggle = this.addChild(new IconButton(screenArea.x + screenArea.width, screenArea.y, this::ToggleMode, () -> IconAndButtonUtil.GetIcon(((TraderInterfaceMenu) this.f_97732_).getBE().getMode())).withAddons(EasyAddonHelper.tooltip(() -> this.getMode().getDisplayText())));
        this.onlineModeToggle = this.addChild(new IconButton(screenArea.x + screenArea.width, screenArea.y + 20, this::ToggleOnlineMode, () -> ((TraderInterfaceMenu) this.f_97732_).getBE().isOnlineMode() ? IconAndButtonUtil.ICON_ONLINEMODE_TRUE : IconAndButtonUtil.ICON_ONLINEMODE_FALSE).withAddons(EasyAddonHelper.tooltip(() -> ((TraderInterfaceMenu) this.f_97732_).getBE().isOnlineMode() ? LCText.TOOLTIP_INTERFACE_ONLINE_MODE_ON.get() : LCText.TOOLTIP_INTERFACE_ONLINE_MODE_OFF.get())));
        this.currentTab().onOpen();
        this.m_181908_();
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.renderNormalBackground(GUI_TEXTURE, this);
        try {
            this.currentTab().renderBG(gui);
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error rendering trader storage tab " + this.currentTab().getClass().getName(), var3);
        }
        gui.drawString(this.f_169604_, 23, this.getYSize() - 94, 4210752);
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        try {
            this.currentTab().renderAfterWidgets(gui);
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error rendering trader storage tab " + this.currentTab().getClass().getName(), var3);
        }
    }

    @Override
    public void screenTick() {
        if (!this.currentTab().commonTab.canOpen(((TraderInterfaceMenu) this.f_97732_).player)) {
            this.changeTab(0);
        }
        this.updateTabs();
    }

    private TraderInterfaceBlockEntity.ActiveMode getMode() {
        return ((TraderInterfaceMenu) this.f_97732_).getBE() != null ? ((TraderInterfaceMenu) this.f_97732_).getBE().getMode() : TraderInterfaceBlockEntity.ActiveMode.DISABLED;
    }

    private void ToggleMode(EasyButton button) {
        ((TraderInterfaceMenu) this.f_97732_).changeMode(this.getMode().getNext());
    }

    private void ToggleOnlineMode(EasyButton button) {
        ((TraderInterfaceMenu) this.f_97732_).setOnlineMode(!((TraderInterfaceMenu) this.f_97732_).getBE().isOnlineMode());
    }

    private void updateTabs() {
        int yPos = this.f_97736_ - 25;
        AtomicInteger index = new AtomicInteger(0);
        this.tabButtons.forEach((key, button) -> {
            TraderInterfaceClientTab<?> tab = (TraderInterfaceClientTab<?>) this.availableTabs.get(key);
            button.f_93624_ = tab.tabButtonVisible();
            if (button.f_93624_) {
                int xPos = this.f_97735_ + 25 * index.get();
                button.reposition(xPos, yPos, 0);
                index.set(index.get() + 1);
            }
        });
    }

    @Override
    public boolean blockInventoryClosing() {
        return this.currentTab().blockInventoryClosing();
    }

    private TabButton getTabButton(int key) {
        return this.tabButtons.containsKey(key) ? (TabButton) this.tabButtons.get(key) : null;
    }

    public void changeTab(int newTab) {
        this.changeTab(newTab, true, null);
    }

    public void changeTab(int newTab, boolean sendMessage, CompoundTag selfMessage) {
        if (newTab != ((TraderInterfaceMenu) this.f_97732_).getCurrentTabIndex()) {
            int oldTab = ((TraderInterfaceMenu) this.f_97732_).getCurrentTabIndex();
            this.currentTab().onClose();
            TabButton button = this.getTabButton(((TraderInterfaceMenu) this.f_97732_).getCurrentTabIndex());
            if (button != null) {
                button.f_93623_ = true;
            }
            ((TraderInterfaceMenu) this.f_97732_).changeTab(newTab);
            button = this.getTabButton(((TraderInterfaceMenu) this.f_97732_).getCurrentTabIndex());
            if (button != null) {
                button.f_93623_ = false;
            }
            if (selfMessage != null) {
                this.currentTab().receiveSelfMessage(selfMessage);
            }
            this.currentTab().onOpen();
            if (oldTab != ((TraderInterfaceMenu) this.f_97732_).getCurrentTabIndex() && sendMessage) {
                ((TraderInterfaceMenu) this.f_97732_).sendMessage(((TraderInterfaceMenu) this.f_97732_).createTabChangeMessage(newTab, null));
            }
        }
    }
}