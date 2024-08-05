package dev.xkmc.l2artifacts.content.search.common;

import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabToken;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.l2library.base.menu.scroller.Scroller;
import dev.xkmc.l2library.base.menu.scroller.ScrollerScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class AbstractScrollerScreen<T extends AbstractScrollerMenu<T>> extends BaseContainerScreen<T> implements ScrollerScreen, IFilterScreen {

    private final Scroller scroller;

    private final FilterTabToken<?> tab;

    public AbstractScrollerScreen(T cont, Inventory plInv, Component title, FilterTabToken<?> tab) {
        super(cont, plInv, title);
        this.scroller = new Scroller(this, cont.sprite.get(), "slider_middle", "slider_light", "slider_dark");
        this.tab = tab;
    }

    @Override
    protected final void init() {
        super.m_7856_();
        new FilterTabManager(this, ((AbstractScrollerMenu) this.f_97732_).token).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, this.tab);
    }

    @Override
    protected void renderBg(GuiGraphics pose, float pTick, int mx, int my) {
        MenuLayoutConfig sm = ((AbstractScrollerMenu) this.f_97732_).sprite.get();
        MenuLayoutConfig.ScreenRenderer sr = sm.getRenderer(this);
        sr.start(pose);
        this.scroller.render(pose, sr);
        this.renderBgExtra(pose, sr, mx, my);
    }

    protected void renderBgExtra(GuiGraphics pose, MenuLayoutConfig.ScreenRenderer sr, int mx, int my) {
    }

    @Override
    protected void renderLabels(GuiGraphics g, int pMouseX, int pMouseY) {
        g.drawString(this.f_96547_, this.m_96636_(), this.f_97728_, this.f_97729_, 4210752, false);
        g.drawString(this.f_96547_, this.f_169604_.copy().withStyle(ChatFormatting.GRAY), this.f_97730_, this.f_97731_, 4210752, false);
    }

    @Override
    public void scrollTo(int i) {
        if (i < ((AbstractScrollerMenu) this.f_97732_).getScroll()) {
            this.click((((AbstractScrollerMenu) this.f_97732_).getScroll() - i) * 100);
        } else if (i > ((AbstractScrollerMenu) this.f_97732_).getScroll()) {
            this.click(1 + (i - ((AbstractScrollerMenu) this.f_97732_).getScroll()) * 100);
        }
    }

    @Override
    public int screenWidth() {
        return this.f_96543_;
    }

    @Override
    public int screenHeight() {
        return this.f_96544_;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        MenuLayoutConfig.Rect r = ((AbstractScrollerMenu) this.f_97732_).sprite.get().getComp("grid");
        int x = r.x + this.getGuiLeft();
        int y = r.y + this.getGuiTop();
        if (mx >= (double) x && my >= (double) y && mx < (double) (x + r.w * r.rx) && my < (double) (y + r.h * r.ry)) {
            Slot slot = this.getSlotUnderMouse();
            if (slot != null && slot.getContainerSlot() >= ((AbstractScrollerMenu) this.f_97732_).extra) {
                this.click(slot.getContainerSlot() - ((AbstractScrollerMenu) this.f_97732_).extra + 2);
                return true;
            }
        }
        return this.scroller.mouseClicked(mx, my, btn) || super.m_6375_(mx, my, btn);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        return this.scroller.mouseDragged(mx, my, btn, dx, dy) || super.m_7979_(mx, my, btn, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double d) {
        return this.scroller.mouseScrolled(mx, my, d) || super.m_6050_(mx, my, d);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 265 && ((AbstractScrollerMenu) this.f_97732_).getScroll() > 0) {
            this.scrollTo(((AbstractScrollerMenu) this.f_97732_).getScroll() - 1);
            return true;
        } else if (pKeyCode == 264 && ((AbstractScrollerMenu) this.f_97732_).getScroll() < ((AbstractScrollerMenu) this.f_97732_).getMaxScroll()) {
            this.scrollTo(((AbstractScrollerMenu) this.f_97732_).getScroll() + 1);
            return true;
        } else {
            return super.m_7933_(pKeyCode, pScanCode, pModifiers);
        }
    }
}