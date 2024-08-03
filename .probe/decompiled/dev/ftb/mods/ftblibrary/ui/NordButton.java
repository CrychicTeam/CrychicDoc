package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.misc.NordColors;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

public abstract class NordButton extends SimpleTextButton {

    public NordButton(Panel panel, Component txt, Icon icon) {
        super(panel, txt, icon);
        this.setHeight(16);
    }

    @Override
    public void addMouseOverText(TooltipList list) {
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        (this.isMouseOver() ? NordColors.POLAR_NIGHT_4 : NordColors.POLAR_NIGHT_2).draw(graphics, x, y, w, h);
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        this.drawBackground(graphics, theme, x, y, w, h);
        int s = h >= 20 ? 16 : 8;
        int off = (h - s) / 2;
        FormattedText title = this.getTitle();
        int textY = y + (h - theme.getFontHeight() + 1) / 2;
        int sw = theme.getStringWidth(title);
        int mw = w - (this.hasIcon() ? off + s : 0) - 6;
        if (sw > mw) {
            sw = mw;
            title = theme.trimStringToWidth(title, mw);
        }
        int textX;
        if (this.renderTitleInCenter()) {
            textX = x + (mw - sw + 6) / 2;
        } else {
            textX = x + 4;
        }
        if (this.hasIcon()) {
            this.drawIcon(graphics, theme, x + off, y + off, s, s);
            textX += off + s;
        }
        theme.drawString(graphics, title, textX, textY, this.isMouseOver() ? NordColors.SNOW_STORM_3 : NordColors.SNOW_STORM_1, 0);
    }
}