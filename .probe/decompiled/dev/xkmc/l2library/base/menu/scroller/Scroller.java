package dev.xkmc.l2library.base.menu.scroller;

import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class Scroller {

    private final ScrollerScreen screen;

    private final MenuLayoutConfig sprite;

    private final String box;

    private final String light;

    private final String dark;

    private final int bx;

    private final int by;

    private final int bw;

    private final int bh;

    private final int sh;

    private boolean scrolling;

    private double percentage;

    public Scroller(ScrollerScreen screen, MenuLayoutConfig sprite, String slider_middle, String slider_light, String slider_dark) {
        this.screen = screen;
        this.sprite = sprite;
        this.box = slider_middle;
        this.light = slider_light;
        this.dark = slider_dark;
        MenuLayoutConfig.Rect scroller = sprite.getComp(this.box);
        this.bx = scroller.x;
        this.by = scroller.y;
        this.bw = scroller.w;
        this.bh = scroller.ry;
        MenuLayoutConfig.Rect slider = sprite.getSide(this.light);
        this.sh = slider.h;
    }

    public boolean mouseClicked(double mx, double my, int btn) {
        this.scrolling = false;
        int cx = this.screen.getGuiLeft() + this.bx;
        int cy = this.screen.getGuiTop() + this.by;
        if (mx >= (double) cx && mx < (double) (cx + this.bw) && my >= (double) cy && my < (double) (cy + this.bh)) {
            this.scrolling = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        if (this.scrolling && this.screen.getMenu().getMaxScroll() > 0) {
            int y0 = this.screen.getGuiTop() + this.by;
            int y1 = y0 + this.bh;
            this.percentage = (my - (double) y0 - (double) this.sh * 0.5) / (double) ((float) (y1 - y0) - 15.0F);
            this.percentage = Mth.clamp(this.percentage, 0.0, 1.0);
            this.updateIndex();
            return true;
        } else {
            return false;
        }
    }

    public boolean mouseScrolled(double mx, double my, double d) {
        if (this.screen.getMenu().getMaxScroll() > 0) {
            int i = this.screen.getMenu().getMaxScroll();
            double f = d / (double) i;
            this.percentage = Mth.clamp(this.percentage - f, 0.0, 1.0);
            this.updateIndex();
        }
        return true;
    }

    private void updateIndex() {
        this.screen.scrollTo((int) (this.percentage * (double) this.screen.getMenu().getMaxScroll() + 0.5));
    }

    public void render(GuiGraphics g, MenuLayoutConfig.ScreenRenderer sr) {
        if (this.screen.getMenu().getMaxScroll() == 0) {
            sr.draw(g, this.box, this.dark);
        } else {
            int off = (int) Math.round((double) (this.bh - this.sh) * this.percentage);
            sr.draw(g, this.box, this.light, 0, off);
        }
    }
}