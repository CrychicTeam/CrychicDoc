package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ImageIcon;
import dev.ftb.mods.ftblibrary.icon.PartIcon;
import dev.ftb.mods.ftblibrary.math.Bits;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanStack;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

public class Theme {

    public static final Theme DEFAULT = new Theme();

    public static boolean renderDebugBoxes = false;

    public static final int DARK = 1;

    public static final int SHADOW = 2;

    public static final int CENTERED = 4;

    public static final int UNICODE = 8;

    public static final int MOUSE_OVER = 16;

    public static final int CENTERED_V = 32;

    private static final Color4I CONTENT_COLOR_MOUSE_OVER = Color4I.rgb(16777120);

    private static final Color4I CONTENT_COLOR_DISABLED = Color4I.rgb(10526880);

    private static final Color4I CONTENT_COLOR_DARK = Color4I.rgb(4210752);

    public static final ImageIcon BACKGROUND_SQUARES = (ImageIcon) Icon.getIcon("ftblibrary:textures/gui/background_squares.png");

    private static final ImageIcon TEXTURE_BEACON = (ImageIcon) Icon.getIcon("textures/gui/container/beacon.png");

    private static final ImageIcon TEXTURE_WIDGETS = (ImageIcon) Icon.getIcon("textures/gui/widgets.png");

    private static final ImageIcon TEXTURE_RECIPE_BOOK = (ImageIcon) Icon.getIcon("textures/gui/recipe_book.png");

    private static final ImageIcon TEXTURE_ENCHANTING_TABLE = (ImageIcon) Icon.getIcon("textures/gui/container/enchanting_table.png");

    private static final Icon GUI = new PartIcon(TEXTURE_RECIPE_BOOK, 82, 208, 32, 32, 8);

    private static final Icon GUI_MOUSE_OVER = GUI.withTint(Color4I.rgb(11515610));

    private static final Icon BUTTON = new PartIcon(TEXTURE_WIDGETS, 0, 66, 200, 20, 4);

    private static final Icon BUTTON_MOUSE_OVER = new PartIcon(TEXTURE_WIDGETS, 0, 86, 200, 20, 4);

    private static final Icon BUTTON_DISABLED = new PartIcon(TEXTURE_WIDGETS, 0, 46, 200, 20, 4);

    private static final Icon WIDGET = new PartIcon(TEXTURE_BEACON, 0, 219, 22, 22, 6);

    private static final Icon WIDGET_MOUSE_OVER = new PartIcon(TEXTURE_BEACON, 66, 219, 22, 22, 6);

    private static final Icon WIDGET_DISABLED = new PartIcon(TEXTURE_BEACON, 44, 219, 22, 22, 6);

    private static final Icon SLOT = new PartIcon(TEXTURE_BEACON, 35, 136, 18, 18, 3);

    private static final Icon SLOT_MOUSE_OVER = SLOT.combineWith(Color4I.WHITE.withAlpha(33));

    private static final Icon SCROLL_BAR_BG = SLOT;

    private static final Icon SCROLL_BAR_BG_DISABLED = SCROLL_BAR_BG.withTint(Color4I.BLACK.withAlpha(100));

    private static final Icon TEXT_BOX = new PartIcon(TEXTURE_ENCHANTING_TABLE, 0, 185, 108, 19, 6);

    private static final Icon TAB_H_UNSELECTED = TEXTURE_RECIPE_BOOK.withUV(150.0F, 2.0F, 35.0F, 26.0F, 256.0F, 256.0F);

    private static final Icon TAB_H_SELECTED = TEXTURE_RECIPE_BOOK.withUV(188.0F, 2.0F, 35.0F, 26.0F, 256.0F, 256.0F);

    private final BooleanStack fontUnicode = new BooleanArrayList();

    public Color4I getContentColor(WidgetType type) {
        return type == WidgetType.MOUSE_OVER ? CONTENT_COLOR_MOUSE_OVER : (type == WidgetType.DISABLED ? CONTENT_COLOR_DISABLED : Color4I.WHITE);
    }

    public Color4I getInvertedContentColor() {
        return CONTENT_COLOR_DARK;
    }

    public void drawGui(GuiGraphics graphics, int x, int y, int w, int h, WidgetType type) {
        (type == WidgetType.MOUSE_OVER ? GUI_MOUSE_OVER : GUI).draw(graphics, x - 3, y - 3, w + 6, h + 6);
    }

    public void drawWidget(GuiGraphics graphics, int x, int y, int w, int h, WidgetType type) {
        (type == WidgetType.MOUSE_OVER ? WIDGET_MOUSE_OVER : (type == WidgetType.DISABLED ? WIDGET_DISABLED : WIDGET)).draw(graphics, x, y, w, h);
    }

    public void drawSlot(GuiGraphics graphics, int x, int y, int w, int h, WidgetType type) {
        (type == WidgetType.MOUSE_OVER ? SLOT_MOUSE_OVER : SLOT).draw(graphics, x, y, w, h);
    }

    public void drawContainerSlot(GuiGraphics graphics, int x, int y, int w, int h) {
        SLOT.draw(graphics, x - 1, y - 1, w + 2, h + 2);
    }

    public void drawButton(GuiGraphics graphics, int x, int y, int w, int h, WidgetType type) {
        (type == WidgetType.MOUSE_OVER ? BUTTON_MOUSE_OVER : (type == WidgetType.DISABLED ? BUTTON_DISABLED : BUTTON)).draw(graphics, x, y, w, h);
    }

    public void drawScrollBarBackground(GuiGraphics graphics, int x, int y, int w, int h, WidgetType type) {
        (type == WidgetType.DISABLED ? SCROLL_BAR_BG_DISABLED : SCROLL_BAR_BG).draw(graphics, x, y, w, h);
    }

    public void drawScrollBar(GuiGraphics graphics, int x, int y, int w, int h, WidgetType type, boolean vertical) {
        (type == WidgetType.MOUSE_OVER ? WIDGET_MOUSE_OVER : WIDGET).draw(graphics, x + 1, y + 1, w - 2, h - 2);
    }

    public void drawTextBox(GuiGraphics graphics, int x, int y, int w, int h) {
        TEXT_BOX.draw(graphics, x, y, w, h);
    }

    public void drawCheckboxBackground(GuiGraphics graphics, int x, int y, int w, int h, boolean radioButton) {
        this.drawSlot(graphics, x, y, w, h, WidgetType.NORMAL);
    }

    public void drawCheckbox(GuiGraphics graphics, int x, int y, int w, int h, WidgetType type, boolean selected, boolean radioButton) {
        if (selected) {
            this.drawWidget(graphics, x, y, w, h, type);
        }
    }

    public void drawPanelBackground(GuiGraphics graphics, int x, int y, int w, int h) {
        Color4I.rgb(9145227).draw(graphics, x, y, w, h);
    }

    public void drawHorizontalTab(GuiGraphics graphics, int x, int y, int w, int h, boolean selected) {
        (selected ? TAB_H_SELECTED : TAB_H_UNSELECTED).draw(graphics, x, y, w, h);
    }

    public void drawContextMenuBackground(GuiGraphics graphics, int x, int y, int w, int h) {
        this.drawGui(graphics, x, y, w, h, WidgetType.NORMAL);
        Color4I.BLACK.withAlpha(90).draw(graphics, x, y, w, h);
    }

    public Font getFont() {
        return Minecraft.getInstance().font;
    }

    public final int getStringWidth(FormattedText text) {
        return text == Component.f_130760_ ? 0 : this.getFont().width(text);
    }

    public final int getStringWidth(FormattedCharSequence text) {
        return text == FormattedCharSequence.EMPTY ? 0 : this.getFont().width(text);
    }

    public final int getStringWidth(String text) {
        return text.isEmpty() ? 0 : this.getFont().width(text);
    }

    public final int getFontHeight() {
        return 9;
    }

    public final String trimStringToWidth(String text, int width) {
        return !text.isEmpty() && width > 0 ? this.getFont().plainSubstrByWidth(text, width, false) : "";
    }

    public final FormattedText trimStringToWidth(FormattedText text, int width) {
        return this.getFont().substrByWidth(text, width);
    }

    public final String trimStringToWidthReverse(String text, int width) {
        return !text.isEmpty() && width > 0 ? this.getFont().plainSubstrByWidth(text, width, true) : "";
    }

    public final List<FormattedText> listFormattedStringToWidth(FormattedText text, int width) {
        return width > 0 && text != Component.f_130760_ ? this.getFont().getSplitter().splitLines(text, width, Style.EMPTY) : Collections.emptyList();
    }

    public final int drawString(GuiGraphics graphics, @Nullable Object text, int x, int y, Color4I color, int flags) {
        if (text == null || text == FormattedCharSequence.EMPTY || text == Component.f_130760_ || text instanceof String s && s.isEmpty() || color.isEmpty()) {
            return x;
        } else {
            if (text instanceof FormattedCharSequence fcs) {
                if (Bits.getFlag(flags, 4)) {
                    x = (int) ((float) x - (float) this.getStringWidth(fcs) / 2.0F);
                }
                int i = graphics.drawString(this.getFont(), (FormattedCharSequence) text, x, y, color.rgba(), Bits.getFlag(flags, 2));
                GuiHelper.setupDrawing();
                return i;
            }
            if (text instanceof Component comp) {
                if (Bits.getFlag(flags, 4)) {
                    x = (int) ((float) x - (float) this.getStringWidth(comp) / 2.0F);
                }
                int i = graphics.drawString(this.getFont(), comp, x, y, color.rgba(), Bits.getFlag(flags, 2));
                GuiHelper.setupDrawing();
                return i;
            }
            if (text instanceof FormattedText) {
                return this.drawString(graphics, Language.getInstance().getVisualOrder((FormattedText) text), x, y, color, flags);
            } else {
                String s = String.valueOf(text);
                if (Bits.getFlag(flags, 4)) {
                    x = (int) ((float) x - (float) this.getStringWidth(s) / 2.0F);
                }
                int i = graphics.drawString(this.getFont(), s, x, y, color.rgba(), Bits.getFlag(flags, 2));
                GuiHelper.setupDrawing();
                return i;
            }
        }
    }

    public final int drawString(GuiGraphics graphics, @Nullable Object text, int x, int y, int flags) {
        return this.drawString(graphics, text, x, y, this.getContentColor(WidgetType.mouseOver(Bits.getFlag(flags, 16))), flags);
    }

    public final int drawString(GuiGraphics graphics, @Nullable Object text, int x, int y) {
        return this.drawString(graphics, text, x, y, this.getContentColor(WidgetType.NORMAL), 0);
    }
}