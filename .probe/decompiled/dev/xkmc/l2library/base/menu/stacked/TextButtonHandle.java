package dev.xkmc.l2library.base.menu.stacked;

import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import net.minecraft.network.chat.Component;

public class TextButtonHandle {

    private final StackedRenderHandle parent;

    private final int y;

    private int x;

    protected TextButtonHandle(StackedRenderHandle parent, int x, int y) {
        this.parent = parent;
        this.x = x;
        this.y = y;
    }

    public CellEntry addButton(String btn) {
        MenuLayoutConfig.Rect r = this.parent.sm.getSide(btn);
        int y0 = this.y - (r.h + 1) / 2;
        this.parent.g.blit(this.parent.sm.getTexture(), this.x, y0, r.x, r.y, r.w, r.h);
        CellEntry c1 = new CellEntry(this.x, y0, r.w, r.h);
        this.x = this.x + r.w + 3;
        return c1;
    }

    public void drawText(CellEntry cell, Component text, boolean shadow) {
        int x0 = cell.x() + (cell.w() - this.parent.font.width(text) + 1) / 2;
        int y0 = cell.y() + (cell.h() + 1) / 2 - 9 / 2;
        this.parent.textList.add(new TextEntry(text, x0, y0, this.parent.text_color, shadow));
    }
}