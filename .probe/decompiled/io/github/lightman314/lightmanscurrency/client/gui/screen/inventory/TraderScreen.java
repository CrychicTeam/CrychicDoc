package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.customer.ITraderScreen;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.trader.TraderClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.trader.common.TraderInteractionTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.util.LazyWidgetPositioner;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.menus.TraderMenu;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketCollectCoins;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketOpenNetworkTerminal;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketOpenStorage;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class TraderScreen extends EasyMenuScreen<TraderMenu> implements ITraderScreen {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/trader.png");

    public static final int WIDTH = 206;

    public static final int HEIGHT = 236;

    private final ScreenPosition INFO_WIDGET_POSITION = ScreenPosition.of(175, 140);

    private final TraderClientTab DEFAULT_TAB = new TraderInteractionTab(this);

    IconButton buttonOpenStorage;

    IconButton buttonCollectCoins;

    IconButton buttonOpenTerminal;

    TraderClientTab currentTab = this.DEFAULT_TAB;

    public final LazyWidgetPositioner leftEdgePositioner = LazyWidgetPositioner.create(this, LazyWidgetPositioner.MODE_BOTTOMUP, -20, 216, 20);

    @Override
    public void setTab(@Nonnull TraderClientTab tab) {
        this.currentTab.onClose();
        this.currentTab = tab;
        this.currentTab.onOpen();
    }

    @Override
    public void closeTab() {
        this.setTab(this.DEFAULT_TAB);
    }

    protected boolean forceShowTerminalButton() {
        return false;
    }

    public TraderScreen(TraderMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.resize(206, 236);
    }

    @Override
    public void initialize(ScreenArea screenArea) {
        this.leftEdgePositioner.clear();
        this.addChild(this.leftEdgePositioner);
        this.buttonOpenStorage = this.addChild(IconAndButtonUtil.storageButton(this.f_97735_ + 15 - 20, this.f_97736_ + 118, this::OpenStorage, () -> ((TraderMenu) this.f_97732_).isSingleTrader() && ((TraderMenu) this.f_97732_).getSingleTrader().hasPermission(((TraderMenu) this.f_97732_).player, "openStorage")));
        this.buttonCollectCoins = this.addChild(IconAndButtonUtil.collectCoinButton(this.f_97735_ + 15 - 20, this.f_97736_ + 138, this::CollectCoins, ((TraderMenu) this.f_97732_).player, ((TraderMenu) this.f_97732_)::getSingleTrader));
        this.buttonOpenTerminal = this.addChild(IconAndButtonUtil.backToTerminalButton(this.f_97735_ + 15 - 20, this.f_97736_ + this.f_97727_ - 20, this::OpenTerminal, this::showTerminalButton));
        this.buttonOpenTerminal.f_93624_ = this.showTerminalButton();
        this.leftEdgePositioner.addWidgets(this.buttonOpenTerminal, this.buttonOpenStorage, this.buttonCollectCoins);
        if (((TraderMenu) this.f_97732_).isSingleTrader()) {
            TraderData trader = ((TraderMenu) this.f_97732_).getSingleTrader();
            if (trader != null) {
                trader.onScreenInit(this, this::addChild);
            }
        }
        this.currentTab.onOpen();
        this.m_181908_();
    }

    private boolean showTerminalButton() {
        return this.forceShowTerminalButton() || ((TraderMenu) this.f_97732_).isSingleTrader() && ((TraderMenu) this.f_97732_).getSingleTrader().showOnTerminal();
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.renderNormalBackground(GUI_TEXTURE, this);
        gui.blit(GUI_TEXTURE, this.INFO_WIDGET_POSITION, this.f_97726_ + 38, 0, 10, 10);
        for (Slot slot : ((TraderMenu) this.f_97732_).getCoinSlots()) {
            gui.blit(GUI_TEXTURE, slot.x - 1, slot.y - 1, this.f_97726_, 0, 18, 18);
        }
        if (((TraderMenu) this.f_97732_).getInteractionSlot().isActive()) {
            gui.blit(GUI_TEXTURE, ((TraderMenu) this.f_97732_).getInteractionSlot().f_40220_ - 1, ((TraderMenu) this.f_97732_).getInteractionSlot().f_40221_ - 1, this.f_97726_, 0, 18, 18);
        }
        try {
            this.currentTab.renderBG(gui);
        } catch (Throwable var4) {
            LightmansCurrency.LogError("Error rendering trader tab " + this.currentTab.getClass().getName(), var4);
        }
        gui.drawString(this.f_169604_, 23, this.f_97727_ - 94, 4210752);
        Component valueText = ((TraderMenu) this.f_97732_).getContext(null).getAvailableFunds().getRandomValueText();
        gui.drawString(valueText, 185 - gui.font.width(valueText) - 10, this.f_97727_ - 94, 4210752);
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        try {
            this.currentTab.renderAfterWidgets(gui);
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error rendering trader tab tooltips " + this.currentTab.getClass().getName(), var3);
        }
        if (this.INFO_WIDGET_POSITION.offset(this).isMouseInArea(gui.mousePos, 10, 10)) {
            gui.renderComponentTooltip(((TraderMenu) this.f_97732_).getContext(null).getAvailableFundsDescription());
        }
    }

    private void OpenStorage(EasyButton button) {
        if (((TraderMenu) this.f_97732_).isSingleTrader()) {
            new CPacketOpenStorage(((TraderMenu) this.f_97732_).getSingleTrader().getID()).send();
        }
    }

    private void CollectCoins(EasyButton button) {
        if (((TraderMenu) this.f_97732_).isSingleTrader()) {
            CPacketCollectCoins.sendToServer();
        }
    }

    private void OpenTerminal(EasyButton button) {
        if (this.showTerminalButton()) {
            new CPacketOpenNetworkTerminal().send();
        }
    }

    @Override
    public boolean blockInventoryClosing() {
        return this.currentTab.blockInventoryClosing();
    }
}