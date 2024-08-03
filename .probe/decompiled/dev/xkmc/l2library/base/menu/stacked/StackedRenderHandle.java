package dev.xkmc.l2library.base.menu.stacked;

import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StackedRenderHandle {

    static final int BTN_X_OFFSET = 3;

    static final int TEXT_BASE_HEIGHT = 8;

    private static final int SLOT_X_OFFSET = 7;

    private static final int SLOT_SIZE = 18;

    private static final int SPRITE_OFFSET = 176;

    final Screen scr;

    final GuiGraphics g;

    final MenuLayoutConfig sm;

    final Font font;

    final int text_color;

    private final int TEXT_Y_OFFSET;

    private final int TEXT_HEIGHT;

    private final int text_x_offset;

    private int current_y = 3;

    private int current_x = 0;

    final List<TextEntry> textList = new ArrayList();

    public StackedRenderHandle(Screen scr, GuiGraphics g, MenuLayoutConfig sm) {
        this(scr, g, sm, 3);
    }

    public StackedRenderHandle(Screen scr, GuiGraphics g, MenuLayoutConfig sm, int ty) {
        this(scr, g, 8, 4210752, sm, ty);
    }

    public StackedRenderHandle(Screen scr, GuiGraphics g, int x_offset, int color, MenuLayoutConfig sm) {
        this(scr, g, x_offset, color, sm, 3);
    }

    public StackedRenderHandle(Screen scr, GuiGraphics g, int x_offset, int color, MenuLayoutConfig sm, int ty) {
        this.font = Minecraft.getInstance().font;
        this.g = g;
        this.scr = scr;
        this.sm = sm;
        this.text_color = color;
        this.text_x_offset = x_offset;
        this.TEXT_Y_OFFSET = ty;
        this.TEXT_HEIGHT = 9 + ty + 1;
    }

    public void drawText(Component text, boolean shadow) {
        this.endCell();
        int y = this.current_y + this.TEXT_Y_OFFSET;
        this.textList.add(new TextEntry(text, this.text_x_offset, y, this.text_color, shadow));
        this.current_y = this.current_y + this.TEXT_HEIGHT;
    }

    public void drawTable(Component[][] table, int x_max, boolean shadow) {
        this.endCell();
        int w = table[0].length;
        int w1 = 0;
        int ws = 0;
        for (Component[] c : table) {
            w1 = Math.max(w1, this.font.width(c[0]));
            for (int i = 1; i < w; i++) {
                ws = Math.max(ws, this.font.width(c[i]));
            }
        }
        int sumw = w1 + ws * (w - 1);
        int x0 = this.text_x_offset;
        int x1 = x_max - this.text_x_offset;
        float space = (float) (x1 - x0 - sumw) * 1.0F / (float) (w - 1);
        for (Component[] c : table) {
            int y = this.current_y + this.TEXT_Y_OFFSET;
            float x_start = (float) x0;
            for (int i = 0; i < w; i++) {
                float wi = i == 0 ? (float) w1 : (float) ws;
                int x = Math.round(x_start);
                this.textList.add(new TextEntry(c[i], x, y, this.text_color, shadow));
                x_start += wi + space;
            }
            this.current_y = this.current_y + this.TEXT_HEIGHT;
        }
    }

    public TextButtonHandle drawTextWithButtons(Component text, boolean shadow) {
        this.endCell();
        int y = this.current_y + this.TEXT_Y_OFFSET;
        this.textList.add(new TextEntry(text, this.text_x_offset, y, this.text_color, shadow));
        int x_off = this.text_x_offset + this.font.width(text) + 3;
        TextButtonHandle ans = new TextButtonHandle(this, x_off, y + 9 / 2);
        this.current_y = this.current_y + this.TEXT_HEIGHT;
        return ans;
    }

    public CellEntry addCell(boolean toggled, boolean disabled) {
        this.startCell();
        int index = toggled ? 1 : (disabled ? 2 : 0);
        int x = 7 + this.current_x * 18;
        int u = 176 + index * 18;
        this.g.blit(this.sm.getTexture(), x, this.current_y, u, 0, 18, 18);
        CellEntry ans = new CellEntry(x + 1, this.current_y + 1, 16, 16);
        this.current_x++;
        if (this.current_x == 9) {
            this.endCell();
        }
        return ans;
    }

    private void startCell() {
        if (this.current_x < 0) {
            this.current_x = 0;
        }
    }

    private void endCell() {
        if (this.current_x > 0) {
            this.current_x = -1;
            this.current_y += 18;
        }
    }

    public void flushText() {
        this.textList.forEach(e -> this.g.drawString(this.font, e.text(), e.x(), e.y(), e.color(), e.shadow()));
    }
}