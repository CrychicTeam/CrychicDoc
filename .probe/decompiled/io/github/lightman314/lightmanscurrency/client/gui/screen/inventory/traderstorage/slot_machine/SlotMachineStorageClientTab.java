package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.slot_machine;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyScreenHelper;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IMouseListener;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollListener;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.ScrollBarWidget;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.slot_machine.SlotMachineStorageTab;
import io.github.lightman314.lightmanscurrency.common.traders.item.TraderItemStorage;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineTraderData;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotMachineStorageClientTab extends TraderStorageClientTab<SlotMachineStorageTab> implements IScrollable, IMouseListener {

    private static final int X_OFFSET = 13;

    private static final int Y_OFFSET = 17;

    private static final int COLUMNS_NORMAL = 8;

    private static final int COLUMNS_PERSISTENT = 10;

    private static final int ROWS = 5;

    int scroll = 0;

    ScrollBarWidget scrollBar;

    int columns = 8;

    public SlotMachineStorageClientTab(Object screen, SlotMachineStorageTab commonTab) {
        super(screen, commonTab);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_STORAGE;
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_TRADER_STORAGE.get();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.addChild(this);
        this.columns = 8;
        if (this.menu.getTrader() instanceof SlotMachineTraderData trader && trader.isPersistent()) {
            this.columns = 10;
        }
        this.scrollBar = this.addChild(new ScrollBarWidget(screenArea.pos.offset(13 + 18 * this.columns, 17), 90, this));
        this.addChild(new ScrollListener(screenArea.pos, screenArea.width, 118, this));
        if (this.menu.getTrader() instanceof SlotMachineTraderData trader && !trader.isPersistent()) {
            this.addChild(IconAndButtonUtil.quickInsertButton(screenArea.pos.offset(22, 115), b -> this.commonTab.quickTransfer(0)));
            this.addChild(IconAndButtonUtil.quickExtractButton(screenArea.pos.offset(34, 115), b -> this.commonTab.quickTransfer(1)));
        }
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.drawString(LCText.TOOLTIP_TRADER_STORAGE.get(), 8, 6, 4210752);
        if (this.menu.getTrader() instanceof SlotMachineTraderData trader) {
            this.validateScroll();
            int index = this.scroll * this.columns;
            TraderItemStorage storage = trader.getStorage();
            int hoverSlot = this.isMouseOverSlot(gui.mousePos) + this.scroll * this.columns;
            for (int y = 0; y < 5; y++) {
                int yPos = 17 + y * 18;
                for (int x = 0; x < this.columns; x++) {
                    int xPos = 13 + x * 18;
                    gui.resetColor();
                    gui.blit(TraderScreen.GUI_TEXTURE, xPos, yPos, 206, 0, 18, 18);
                    if (index < storage.getSlotCount()) {
                        gui.renderItem((ItemStack) storage.getContents().get(index), xPos + 1, yPos + 1, this.getCountText((ItemStack) storage.getContents().get(index)));
                    }
                    if (index == hoverSlot) {
                        gui.renderSlotHighlight(xPos + 1, yPos + 1);
                    }
                    index++;
                }
            }
            gui.resetColor();
            for (Slot slot : this.commonTab.getSlots()) {
                gui.blit(TraderScreen.GUI_TEXTURE, slot.x - 1, slot.y - 1, 206, 0, 18, 18);
            }
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
        if (this.menu.getTrader() instanceof SlotMachineTraderData trader && this.screen.getMenu().getHeldItem().isEmpty()) {
            int hoveredSlot = this.isMouseOverSlot(gui.mousePos);
            if (hoveredSlot >= 0) {
                hoveredSlot += this.scroll * this.columns;
                TraderItemStorage storage = trader.getStorage();
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
        for (int x = 0; x < this.columns && foundColumn < 0; x++) {
            if (mousePos.x >= leftEdge + x * 18 && mousePos.x < leftEdge + x * 18 + 18) {
                foundColumn = x;
            }
        }
        for (int y = 0; y < 5 && foundRow < 0; y++) {
            if (mousePos.y >= topEdge + y * 18 && mousePos.y < topEdge + y * 18 + 18) {
                foundRow = y;
            }
        }
        return foundColumn >= 0 && foundRow >= 0 ? foundRow * this.columns + foundColumn : -1;
    }

    private int totalStorageSlots() {
        return this.menu.getTrader() instanceof SlotMachineTraderData trader ? trader.getStorage().getContents().size() : 0;
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (this.menu.getTrader() instanceof SlotMachineTraderData) {
            int hoveredSlot = this.isMouseOverSlot(ScreenPosition.of(mouseX, mouseY));
            if (hoveredSlot >= 0) {
                hoveredSlot += this.scroll * this.columns;
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
        return Math.max((this.totalStorageSlots() - 1) / this.columns - 5 + 1, 0);
    }
}