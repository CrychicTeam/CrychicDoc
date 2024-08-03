package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ContextMenu extends ModalPanel {

    private static final int MARGIN = 3;

    private final List<ContextMenuItem> items;

    private final boolean hasIcons;

    private int nColumns;

    private int columnWidth;

    private int maxRows;

    private boolean drawVerticalSeparators = true;

    public ContextMenu(Panel panel, List<ContextMenuItem> i) {
        super(panel);
        this.items = i;
        this.hasIcons = this.items.stream().anyMatch(item -> !item.getIcon().isEmpty());
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public void setDrawVerticalSeparators(boolean drawVerticalSeparators) {
        this.drawVerticalSeparators = drawVerticalSeparators;
    }

    @Override
    public void addWidgets() {
        this.items.forEach(item -> this.add(item.createWidget(this)));
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        boolean pressed = super.mousePressed(button);
        if (!pressed && !this.isMouseOver()) {
            this.closeContextMenu();
            return true;
        } else {
            return pressed;
        }
    }

    @Override
    public void alignWidgets() {
        this.setWidth(0);
        int totalHeight = 0;
        int maxWidth = 0;
        for (Widget widget : this.widgets) {
            maxWidth = Math.max(maxWidth, widget.width);
            totalHeight += widget.height + 1;
        }
        totalHeight += 6;
        this.nColumns = this.parent.getScreen().getGuiScaledHeight() > 0 ? totalHeight / this.parent.getScreen().getGuiScaledHeight() + 1 : 1;
        if (this.maxRows > 0) {
            this.nColumns = Math.max(this.nColumns, this.widgets.size() / this.maxRows);
        }
        int nRows = this.nColumns == 1 ? this.widgets.size() : this.widgets.size() / (this.nColumns + 1) + 1;
        this.columnWidth = maxWidth + 6;
        this.setWidth(this.columnWidth * this.nColumns);
        int yPos = 3;
        int prevCol = 0;
        int maxHeight = 0;
        for (int i = 0; i < this.widgets.size(); i++) {
            int col = i / nRows;
            if (prevCol != col) {
                yPos = 3;
                prevCol = col;
            }
            Widget widget = (Widget) this.widgets.get(i);
            widget.setPosAndSize(3 + this.columnWidth * col, yPos, maxWidth, widget.height);
            maxHeight = Math.max(maxHeight, yPos + widget.height + 1);
            yPos += widget.height + 1;
        }
        this.setHeight(maxHeight + 3 - 1);
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawContextMenuBackground(graphics, x, y, w, h);
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        GuiHelper.setupDrawing();
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 900.0F);
        Color4I.BLACK.withAlpha(45).draw(graphics, x + 3, y + 3, w, h);
        super.draw(graphics, theme, x, y, w, h);
        if (this.drawVerticalSeparators) {
            for (int i = 1; i < this.nColumns; i++) {
                Color4I.WHITE.withAlpha(130).draw(graphics, x + this.columnWidth * i, y + 3, 1, this.height - 6);
            }
        }
        graphics.pose().popPose();
    }

    public static class CButton extends Button {

        public final ContextMenu contextMenu;

        public final ContextMenuItem item;

        public CButton(ContextMenu panel, ContextMenuItem item) {
            super(panel, item.getTitle(), item.getIcon());
            this.contextMenu = panel;
            this.item = item;
            this.setSize(panel.getGui().getTheme().getStringWidth(item.getTitle()) + (this.contextMenu.hasIcons ? 14 : 4), 12);
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            this.item.addMouseOverText(list);
        }

        @Override
        public WidgetType getWidgetType() {
            if (!this.item.isClickable()) {
                return WidgetType.NORMAL;
            } else {
                return this.item.isEnabled() ? super.getWidgetType() : WidgetType.DISABLED;
            }
        }

        @Override
        public void drawIcon(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            this.item.drawIcon(graphics, theme, x, y, w, h);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            GuiHelper.setupDrawing();
            if (this.contextMenu.hasIcons) {
                this.drawIcon(graphics, theme, x + 1, y + 2, 8, 8);
                theme.drawString(graphics, this.getTitle(), x + 11, y + 2, theme.getContentColor(this.getWidgetType()), 2);
            } else {
                theme.drawString(graphics, this.getTitle(), x + 2, y + 2, theme.getContentColor(this.getWidgetType()), 2);
            }
        }

        @Override
        public void onClicked(MouseButton button) {
            if (this.item.isClickable()) {
                this.playClickSound();
            }
            if (this.item.getYesNoText().getString().isEmpty()) {
                this.item.onClicked(this, this.contextMenu, button);
            } else {
                this.getGui().openYesNo(this.item.getYesNoText(), Component.literal(""), () -> this.item.onClicked(this, this.contextMenu, button));
            }
        }
    }

    public static class CSeparator extends Button {

        public CSeparator(Panel panel) {
            super(panel);
            this.setHeight(5);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            theme.getContentColor(WidgetType.NORMAL).withAlpha(100).draw(graphics, x + 2, y + 2, this.parent.width - 10, 1);
            theme.getContentColor(WidgetType.DISABLED).withAlpha(100).draw(graphics, x + 3, y + 3, this.parent.width - 10, 1);
        }

        @Override
        public void onClicked(MouseButton button) {
        }
    }
}