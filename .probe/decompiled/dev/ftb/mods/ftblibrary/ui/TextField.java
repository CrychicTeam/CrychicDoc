package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.math.Bits;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;

public class TextField extends Widget {

    public Component component = Component.empty();

    private FormattedText[] formattedText = new FormattedText[0];

    private Component rawText = Component.empty();

    public int textFlags = 0;

    public int minWidth = 0;

    public int maxWidth = 5000;

    public int textSpacing = 10;

    public float scale = 1.0F;

    public Color4I textColor = Icon.empty();

    public boolean trim = false;

    private boolean tooltip = false;

    public TextField(Panel panel) {
        super(panel);
    }

    public TextField addFlags(int flags) {
        this.textFlags |= flags;
        return this;
    }

    public TextField setMinWidth(int width) {
        this.minWidth = width;
        return this;
    }

    public TextField setMaxWidth(int width) {
        this.maxWidth = width;
        return this;
    }

    public TextField setColor(Color4I color) {
        this.textColor = color;
        return this;
    }

    public TextField setScale(float s) {
        this.scale = s;
        return this;
    }

    public TextField setSpacing(int s) {
        this.textSpacing = s;
        return this;
    }

    public TextField setTrim() {
        this.trim = true;
        return this;
    }

    public TextField showTooltipForLongText() {
        this.tooltip = true;
        return this;
    }

    public TextField setText(Component txt) {
        Theme theme = this.getGui().getTheme();
        this.rawText = txt;
        this.formattedText = (FormattedText[]) theme.listFormattedStringToWidth(Component.literal("").append(txt), this.maxWidth).toArray(new FormattedText[0]);
        return this.resize(theme);
    }

    public TextField setText(String txt) {
        return this.setText(Component.literal(txt));
    }

    public TextField resize(Theme theme) {
        this.setWidth(0);
        for (FormattedText s : this.getDisplayedText()) {
            this.setWidth(Math.max(this.width, (int) ((float) theme.getStringWidth(s) * this.scale)));
        }
        this.setWidth(Mth.clamp(this.width, this.minWidth, this.maxWidth));
        this.setHeight((int) ((float) (Math.max(1, this.formattedText.length) * this.textSpacing - (this.textSpacing - theme.getFontHeight() + 1)) * this.scale));
        return this;
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if (this.tooltip && this.formattedText.length > 1) {
            list.add(this.rawText);
        }
    }

    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
    }

    private FormattedText[] getDisplayedText() {
        return this.trim && this.formattedText.length > 0 ? new FormattedText[] { this.formattedText[0] } : this.formattedText;
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        this.drawBackground(graphics, theme, x, y, w, h);
        if (this.formattedText.length != 0) {
            boolean centered = Bits.getFlag(this.textFlags, 4);
            boolean centeredV = Bits.getFlag(this.textFlags, 32);
            Color4I col = this.textColor;
            if (col.isEmpty()) {
                col = theme.getContentColor(WidgetType.mouseOver(Bits.getFlag(this.textFlags, 16)));
            }
            int tx = x + (centered ? w / 2 : 0);
            int ty = y + (centeredV ? (h - theme.getFontHeight()) / 2 : 0);
            if (this.scale == 1.0F) {
                for (int i = 0; i < this.getDisplayedText().length; i++) {
                    theme.drawString(graphics, this.formattedText[i], tx, ty + i * this.textSpacing, col, this.textFlags);
                }
            } else {
                graphics.pose().pushPose();
                graphics.pose().translate((double) tx, (double) ty, 0.0);
                graphics.pose().scale(this.scale, this.scale, 1.0F);
                for (int i = 0; i < this.getDisplayedText().length; i++) {
                    theme.drawString(graphics, this.formattedText[i], 0, i * this.textSpacing, col, this.textFlags);
                }
                graphics.pose().popPose();
            }
        }
    }

    public Optional<Style> getComponentStyleAt(Theme theme, int mouseX, int mouseY) {
        int line = (mouseY - this.getY()) / theme.getFontHeight();
        if (line >= 0 && line < this.getDisplayedText().length) {
            boolean centered = Bits.getFlag(this.textFlags, 4);
            int textWidth = theme.getFont().width(this.formattedText[line]);
            int xStart = centered ? this.getX() + (this.width - textWidth) / 2 : this.getX();
            if (mouseX >= xStart && mouseX <= xStart + textWidth) {
                return Optional.ofNullable(theme.getFont().getSplitter().componentStyleAtWidth(this.formattedText[line], mouseX - xStart));
            }
        }
        return Optional.empty();
    }
}