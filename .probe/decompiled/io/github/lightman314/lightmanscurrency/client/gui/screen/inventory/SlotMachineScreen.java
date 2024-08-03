package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.easy.rendering.Sprite;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.slot_machine.SlotMachineRenderer;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollListener;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.ScrollBarWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.slot_machine.SlotMachineEntryDisplayWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.util.LazyWidgetPositioner;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.menus.SlotMachineMenu;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineEntry;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineTraderData;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketCollectCoins;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketOpenNetworkTerminal;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketOpenStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SlotMachineScreen extends EasyMenuScreen<SlotMachineMenu> implements IScrollable {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/slot_machine.png");

    public static final int WIDTH = 176;

    public static final int HEIGHT = 222;

    public static final int ENTRY_ROWS = 2;

    public static final int ENTRY_COLUMNS = 2;

    public static final int ENTRIES_PER_PAGE = 4;

    public static final Sprite SPRITE_INFO = Sprite.SimpleSprite(GUI_TEXTURE, 176, 36, 10, 11);

    public static final Sprite SPRITE_INTERACT_1 = Sprite.SimpleSprite(GUI_TEXTURE, 176, 0, 18, 18);

    public static final Sprite SPRITE_INTERACT_5 = Sprite.SimpleSprite(GUI_TEXTURE, 194, 0, 18, 18);

    public static final Sprite SPRITE_INTERACT_10 = Sprite.SimpleSprite(GUI_TEXTURE, 212, 0, 18, 18);

    private boolean interactMode = true;

    private int scroll = 0;

    IconButton buttonOpenStorage;

    IconButton buttonCollectCoins;

    IconButton buttonOpenTerminal;

    EasyButton buttonInteract;

    EasyButton buttonInteract5;

    EasyButton buttonInteract10;

    EasyButton buttonInfo;

    ScrollListener scrollListener;

    private final ScreenPosition INFO_WIDGET_POSITION = ScreenPosition.of(160, 126);

    public final LazyWidgetPositioner leftEdgePositioner = LazyWidgetPositioner.create(this, LazyWidgetPositioner.MODE_BOTTOMUP, -20, 202, 20);

    private final SlotMachineRenderer slotRenderer = new SlotMachineRenderer(this);

    public final ScreenPosition SM_INFO_WIDGET = ScreenPosition.of(160, 8);

    public SlotMachineScreen(SlotMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.resize(176, 222);
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        this.buttonOpenStorage = this.addChild(IconAndButtonUtil.storageButton(this.f_97735_ + 15 - 20, this.f_97736_ + 118, this::OpenStorage, () -> ((SlotMachineMenu) this.f_97732_).getTrader() != null && ((SlotMachineMenu) this.f_97732_).getTrader().hasPermission(((SlotMachineMenu) this.f_97732_).player, "openStorage")));
        this.buttonCollectCoins = this.addChild(IconAndButtonUtil.collectCoinButton(this.f_97735_ + 15 - 20, this.f_97736_ + 138, this::CollectCoins, ((SlotMachineMenu) this.f_97732_).player, ((SlotMachineMenu) this.f_97732_)::getTrader));
        this.buttonOpenTerminal = this.addChild(IconAndButtonUtil.backToTerminalButton(this.f_97735_ + 15 - 20, this.f_97736_ + this.f_97727_ - 20, this::OpenTerminal, this::showTerminalButton));
        this.buttonOpenTerminal.f_93624_ = this.showTerminalButton();
        this.leftEdgePositioner.clear();
        this.leftEdgePositioner.addWidgets(this.buttonOpenTerminal, this.buttonOpenStorage, this.buttonCollectCoins);
        this.addChild(this.leftEdgePositioner);
        this.buttonInteract = this.addChild(new PlainButton(this.f_97735_ + 52, this.f_97736_ + 107, b -> this.ExecuteTrade(1), SPRITE_INTERACT_1).withAddons(EasyAddonHelper.tooltips((Supplier<List<Component>>) (() -> this.getInteractionTooltip(1))), EasyAddonHelper.activeCheck(this::allowInteraction), EasyAddonHelper.visibleCheck(this::isInteractMode)));
        this.buttonInteract5 = this.addChild(new PlainButton(this.f_97735_ + 29, this.f_97736_ + 107, b -> this.ExecuteTrade(5), SPRITE_INTERACT_5).withAddons(EasyAddonHelper.tooltips((Supplier<List<Component>>) (() -> this.getInteractionTooltip(5))), EasyAddonHelper.activeCheck(this::allowInteraction), EasyAddonHelper.visibleCheck(this::isInteractMode)));
        this.buttonInteract10 = this.addChild(new PlainButton(this.f_97735_ + 7, this.f_97736_ + 107, b -> this.ExecuteTrade(10), SPRITE_INTERACT_10).withAddons(EasyAddonHelper.tooltips((Supplier<List<Component>>) (() -> this.getInteractionTooltip(10))), EasyAddonHelper.activeCheck(this::allowInteraction), EasyAddonHelper.visibleCheck(this::isInteractMode)));
        this.buttonInfo = this.addChild(new PlainButton(screenArea.pos.offset(this.SM_INFO_WIDGET), this::ToggleMode, SPRITE_INFO).withAddons(EasyAddonHelper.tooltips(this::getInfoTooltip)));
        this.scrollListener = this.addChild(new ScrollListener(screenArea, this));
        this.scrollListener.active = this.isInfoMode();
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int displayIndex = y * 2 + x;
                this.addChild(new SlotMachineEntryDisplayWidget(screenArea.pos.offset(19 + x * 80, 10 + y * 46), ((SlotMachineMenu) this.f_97732_)::getTrader, () -> this.getTrueIndex(displayIndex)).withAddons(EasyAddonHelper.visibleCheck(this::isInfoMode)));
            }
        }
        this.addChild(new ScrollBarWidget(screenArea.pos.offset(8, 10), 92, this).withAddons(EasyAddonHelper.visibleCheck(this::isInfoMode)));
        this.addChild(this.slotRenderer);
    }

    private boolean isInteractMode() {
        return this.interactMode;
    }

    private boolean isInfoMode() {
        return !this.interactMode;
    }

    private void ToggleMode(EasyButton button) {
        if (!((SlotMachineMenu) this.f_97732_).hasPendingReward()) {
            this.interactMode = !this.interactMode;
            if (this.isInfoMode()) {
                this.validateScroll();
            }
        }
    }

    private boolean allowInteraction() {
        SlotMachineTraderData trader = ((SlotMachineMenu) this.f_97732_).getTrader();
        return !((SlotMachineMenu) this.f_97732_).hasPendingReward() && trader != null && trader.hasStock() && trader.hasValidTrade();
    }

    private boolean showTerminalButton() {
        return ((SlotMachineMenu) this.f_97732_).getTrader() != null ? ((SlotMachineMenu) this.f_97732_).getTrader().showOnTerminal() : false;
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.resetColor();
        gui.renderNormalBackground(GUI_TEXTURE, this);
        gui.blit(TraderScreen.GUI_TEXTURE, this.INFO_WIDGET_POSITION, 244, 0, 10, 10);
        if (this.isInteractMode()) {
            this.slotRenderer.render(gui);
        }
        gui.drawString(this.f_169604_, 8, this.getYSize() - 94, 4210752);
        Component valueText = ((SlotMachineMenu) this.f_97732_).getContext(null).getAvailableFunds().getRandomValueText();
        gui.drawString(valueText, 170 - gui.font.width(valueText) - 10, this.getYSize() - 94, 4210752);
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        gui.pushPose().TranslateToForeground();
        if (this.INFO_WIDGET_POSITION.offset(this).isMouseInArea(gui.mousePos, 10, 10)) {
            gui.renderComponentTooltip(((SlotMachineMenu) this.f_97732_).getContext().getAvailableFundsDescription());
        }
    }

    @Nullable
    private List<Component> getInfoTooltip() {
        SlotMachineTraderData trader = ((SlotMachineMenu) this.f_97732_).getTrader();
        if (trader != null) {
            List<Component> info = trader.getSlotMachineInfo();
            if (this.isInfoMode()) {
                LCText.TOOLTIP_SLOT_MACHINE_TO_INTERACT.tooltip(info);
            } else {
                LCText.TOOLTIP_SLOT_MACHINE_TO_INFO.tooltip(info);
            }
            return info;
        } else {
            return null;
        }
    }

    @Override
    protected void renderAfterTooltips(@Nonnull EasyGuiGraphics gui) {
        gui.popPose();
    }

    private List<Component> getInteractionTooltip(int count) {
        SlotMachineTraderData trader = ((SlotMachineMenu) this.f_97732_).getTrader();
        if (trader != null) {
            MoneyValue normalCost = trader.getPrice();
            MoneyValue currentCost = trader.runTradeCostEvent(trader.getTrade(0), ((SlotMachineMenu) this.f_97732_).getContext()).getCostResult();
            Component costText = currentCost.isFree() ? LCText.TOOLTIP_SLOT_MACHINE_COST_FREE.get() : currentCost.getText();
            List<Component> result;
            if (count == 1) {
                result = LCText.TOOLTIP_SLOT_MACHINE_ROLL_ONCE.get(count, costText);
            } else {
                result = LCText.TOOLTIP_SLOT_MACHINE_ROLL_MULTI.get(count, costText);
            }
            if (!currentCost.equals(normalCost) && count > 1) {
                result.add(LCText.TOOLTIP_SLOT_MACHINE_NORMAL_COST.get(normalCost.isFree() ? LCText.TOOLTIP_SLOT_MACHINE_COST_FREE.get() : normalCost.getText()));
            }
            return result;
        } else {
            return ImmutableList.of();
        }
    }

    private void ExecuteTrade(int count) {
        ((SlotMachineMenu) this.f_97732_).SendMessageToServer(LazyPacketData.builder().setInt("ExecuteTrade", count));
    }

    private void OpenStorage(EasyButton button) {
        if (((SlotMachineMenu) this.f_97732_).getTrader() != null) {
            new CPacketOpenStorage(((SlotMachineMenu) this.f_97732_).getTrader().getID()).send();
        }
    }

    private void CollectCoins(EasyButton button) {
        if (((SlotMachineMenu) this.f_97732_).getTrader() != null) {
            CPacketCollectCoins.sendToServer();
        }
    }

    private void OpenTerminal(EasyButton button) {
        if (this.showTerminalButton()) {
            new CPacketOpenNetworkTerminal().send();
        }
    }

    @Override
    protected void screenTick() {
        this.scrollListener.active = this.isInfoMode();
        if (this.isInfoMode()) {
            this.validateScroll();
        }
    }

    @Nonnull
    private List<SlotMachineEntry> getEntries() {
        SlotMachineTraderData trader = ((SlotMachineMenu) this.f_97732_).getTrader();
        return (List<SlotMachineEntry>) (trader != null ? trader.getValidEntries() : new ArrayList());
    }

    private int getTrueIndex(int displayIndex) {
        return displayIndex + this.scroll * 2;
    }

    @Override
    public int currentScroll() {
        return this.scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        this.scroll = newScroll;
    }

    @Override
    public int getMaxScroll() {
        return IScrollable.calculateMaxScroll(4, 2, this.getEntries().size());
    }
}