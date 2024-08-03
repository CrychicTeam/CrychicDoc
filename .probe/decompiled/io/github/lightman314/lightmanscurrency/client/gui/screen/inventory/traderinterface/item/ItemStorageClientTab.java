package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderinterface.item;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.trader_interface.menu.TraderInterfaceClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyScreenHelper;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IMouseListener;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderInterfaceScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.DirectionalSettingsWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollListener;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.ScrollBarWidget;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.blockentity.ItemTraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import io.github.lightman314.lightmanscurrency.common.menus.traderinterface.item.ItemStorageTab;
import io.github.lightman314.lightmanscurrency.common.traderinterface.handlers.ConfigurableSidedHandler;
import io.github.lightman314.lightmanscurrency.common.traders.item.TraderItemStorage;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ItemStorageClientTab extends TraderInterfaceClientTab<ItemStorageTab> implements IScrollable, IMouseListener {

    private static final int X_OFFSET = 13;

    private static final int Y_OFFSET = 17;

    private static final int COLUMNS = 8;

    private static final int ROWS = 2;

    private static final int WIDGET_OFFSET = 57;

    DirectionalSettingsWidget inputSettings;

    DirectionalSettingsWidget outputSettings;

    int scroll = 0;

    ScrollBarWidget scrollBar;

    public ItemStorageClientTab(TraderInterfaceScreen screen, ItemStorageTab commonTab) {
        super(screen, commonTab);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_STORAGE;
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_INTERFACE_STORAGE.get();
    }

    private ConfigurableSidedHandler.DirectionalSettings getInputSettings() {
        return this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity ? ((ItemTraderInterfaceBlockEntity) this.menu.getBE()).getItemHandler().getInputSides() : new ConfigurableSidedHandler.DirectionalSettings();
    }

    private ConfigurableSidedHandler.DirectionalSettings getOutputSettings() {
        return this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity be ? be.getItemHandler().getOutputSides() : new ConfigurableSidedHandler.DirectionalSettings();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.scrollBar = this.addChild(new ScrollBarWidget(screenArea.pos.offset(157, 17), 36, this));
        this.addChild(new ScrollListener(screenArea.pos, screenArea.width, 118, this));
        this.inputSettings = new DirectionalSettingsWidget(screenArea.pos.offset(33, 66), this.getInputSettings()::get, this.getInputSettings().ignoreSides, this::ToggleInputSide, this::addChild);
        this.outputSettings = new DirectionalSettingsWidget(screenArea.pos.offset(116, 66), this.getOutputSettings()::get, this.getInputSettings().ignoreSides, this::ToggleOutputSide, this::addChild);
        this.addChild(IconAndButtonUtil.quickInsertButton(screenArea.pos.offset(22, 115), b -> this.commonTab.quickTransfer(0)));
        this.addChild(IconAndButtonUtil.quickExtractButton(screenArea.pos.offset(34, 115), b -> this.commonTab.quickTransfer(1)));
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        if (this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity be) {
            this.validateScroll();
            int index = this.scroll * 8;
            TraderItemStorage storage = be.getItemBuffer();
            int hoveredSlot = this.isMouseOverSlot(gui.mousePos) + this.scroll * 8;
            for (int y = 0; y < 2; y++) {
                int yPos = 17 + y * 18;
                for (int x = 0; x < 8; x++) {
                    int xPos = 13 + x * 18;
                    gui.resetColor();
                    gui.blit(TraderInterfaceScreen.GUI_TEXTURE, xPos, yPos, 206, 0, 18, 18);
                    if (index < storage.getSlotCount()) {
                        gui.renderItem((ItemStack) storage.getContents().get(index), xPos + 1, yPos + 1, this.getCountText((ItemStack) storage.getContents().get(index)));
                    }
                    if (index == hoveredSlot) {
                        gui.renderSlotHighlight(xPos + 1, yPos + 1);
                    }
                    index++;
                }
            }
            gui.resetColor();
            for (Slot slot : this.commonTab.getSlots()) {
                gui.blit(TraderInterfaceScreen.GUI_TEXTURE, slot.x - 1, slot.y - 1, 206, 0, 18, 18);
            }
            gui.drawString(LCText.GUI_SETTINGS_INPUT_SIDE.get(), 33, 57, 4210752);
            Component outputText = LCText.GUI_SETTINGS_OUTPUT_SIDE.get();
            gui.drawString(outputText, 173 - gui.font.width(outputText), 57, 4210752);
        }
    }

    private String getCountText(ItemStack stack) {
        int count = stack.getCount();
        if (count <= 1) {
            return null;
        } else if (count >= 1000) {
            String countText = String.valueOf(count / 1000);
            if (count % 1000 / 100 > 0) {
                countText = countText + "." + count % 1000 / 100;
            }
            return countText + "k";
        } else {
            return String.valueOf(count);
        }
    }

    @Override
    public void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        if (this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity && ((TraderInterfaceMenu) this.screen.m_6262_()).m_142621_().isEmpty()) {
            int hoveredSlot = this.isMouseOverSlot(gui.mousePos);
            if (hoveredSlot >= 0) {
                hoveredSlot += this.scroll * 8;
                TraderItemStorage storage = ((ItemTraderInterfaceBlockEntity) this.menu.getBE()).getItemBuffer();
                if (hoveredSlot < storage.getContents().size()) {
                    ItemStack stack = (ItemStack) storage.getContents().get(hoveredSlot);
                    if (stack.isEmpty()) {
                        return;
                    }
                    EasyScreenHelper.RenderItemTooltipWithCount(gui, stack, storage.getMaxAmount(), ChatFormatting.YELLOW);
                }
            }
        }
    }

    private int isMouseOverSlot(ScreenPosition mousePos) {
        int foundColumn = -1;
        int foundRow = -1;
        int leftEdge = this.screen.getGuiLeft() + 13;
        int topEdge = this.screen.getGuiTop() + 17;
        for (int x = 0; x < 8 && foundColumn < 0; x++) {
            if (mousePos.x >= leftEdge + x * 18 && mousePos.x < leftEdge + x * 18 + 18) {
                foundColumn = x;
            }
        }
        for (int y = 0; y < 2 && foundRow < 0; y++) {
            if (mousePos.y >= topEdge + y * 18 && mousePos.y < topEdge + y * 18 + 18) {
                foundRow = y;
            }
        }
        return foundColumn >= 0 && foundRow >= 0 ? foundRow * 8 + foundColumn : -1;
    }

    private int totalStorageSlots() {
        return this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity ? ((ItemTraderInterfaceBlockEntity) this.menu.getBE()).getItemBuffer().getContents().size() : 0;
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (this.menu.getBE() instanceof ItemTraderInterfaceBlockEntity) {
            int hoveredSlot = this.isMouseOverSlot(ScreenPosition.of(mouseX, mouseY));
            if (hoveredSlot >= 0) {
                hoveredSlot += this.scroll * 8;
                this.commonTab.clickedOnSlot(hoveredSlot, Screen.hasShiftDown(), button == 0);
                return true;
            }
        }
        return false;
    }

    @Override
    public int currentScroll() {
        return this.scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        this.scroll = newScroll;
        this.validateScroll();
    }

    @Override
    public int getMaxScroll() {
        return Math.max((this.totalStorageSlots() - 1) / 8 - 2 + 1, 0);
    }

    private void ToggleInputSide(Direction side) {
        this.commonTab.toggleInputSlot(side);
    }

    private void ToggleOutputSide(Direction side) {
        this.commonTab.toggleOutputSlot(side);
    }
}