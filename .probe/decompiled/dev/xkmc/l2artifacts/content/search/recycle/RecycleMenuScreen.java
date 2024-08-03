package dev.xkmc.l2artifacts.content.search.recycle;

import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.content.search.filter.FilterScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.l2library.base.menu.stacked.StackedRenderHandle;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class RecycleMenuScreen extends AbstractScrollerScreen<RecycleMenu> {

    private boolean pressed;

    private boolean canDrag;

    private boolean dragging;

    private boolean enable;

    private boolean hover_a;

    private boolean hover_b;

    private static final String[] SUFFIX = new String[] { "", "k", "M", "G", "T" };

    public RecycleMenuScreen(RecycleMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, LangData.TAB_RECYCLE.get().withStyle(ChatFormatting.GRAY), FilterTabManager.RECYCLE);
    }

    @Override
    protected void renderBgExtra(GuiGraphics g, MenuLayoutConfig.ScreenRenderer sr, int mx, int my) {
        MenuLayoutConfig spr = ((RecycleMenu) this.f_97732_).sprite.get();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (this.isSelected(i * 6 + j)) {
                    sr.draw(g, "grid", "toggle_slot_1", j * 18 - 1, i * 18 - 1);
                }
            }
        }
        MenuLayoutConfig.Rect rect = spr.getComp("output");
        if (this.m_6774_(rect.x, rect.y, rect.w, rect.h, (double) mx, (double) my)) {
            sr.draw(g, "output", "delete_on");
        }
        g.pose().pushPose();
        g.pose().translate((float) this.f_97735_, (float) this.f_97736_, 0.0F);
        int btn_x = this.f_97728_ + this.f_96547_.width(this.m_96636_()) + 3;
        int btn_y = this.f_97729_;
        boolean h1 = this.m_6774_(btn_x, btn_y, 8, 8, (double) mx, (double) my);
        boolean p1 = this.pressed && h1;
        MenuLayoutConfig.Rect r = spr.getSide(p1 ? "button_1" : "button_1p");
        g.blit(spr.getTexture(), btn_x, btn_y, r.x, r.y, r.w, r.h);
        if (h1) {
            FilterScreen.renderHighlight(g, btn_x, btn_y, 8, 8, -2130706433);
        }
        btn_x += r.w + 3;
        boolean h2 = this.m_6774_(btn_x, btn_y, 8, 8, (double) mx, (double) my);
        boolean p2 = this.pressed && h2;
        r = spr.getSide(p2 ? "button_2" : "button_2p");
        g.blit(spr.getTexture(), btn_x, btn_y, r.x, r.y, r.w, r.h);
        if (h2) {
            FilterScreen.renderHighlight(g, btn_x, btn_y, 8, 8, -2130706433);
        }
        this.hover_a = h1;
        this.hover_b = h2;
        g.pose().popPose();
    }

    private boolean isSelected(int ind) {
        return ((RecycleMenu) this.f_97732_).sel.get(ind);
    }

    private void forceSelect(int ind) {
        ((RecycleMenu) this.f_97732_).sel.set(!((RecycleMenu) this.f_97732_).sel.get(ind), ind);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        MenuLayoutConfig.Rect r = ((RecycleMenu) this.f_97732_).sprite.get().getComp("grid");
        this.pressed = true;
        int x = r.x + this.getGuiLeft();
        int y = r.y + this.getGuiTop();
        if (mx >= (double) x && my >= (double) y && mx < (double) (x + r.w * r.rx) && my < (double) (y + r.h * r.ry)) {
            Slot slot = this.getSlotUnderMouse();
            if (slot != null && slot.getContainerSlot() >= ((RecycleMenu) this.f_97732_).extra) {
                this.enable = this.isSelected(slot.getContainerSlot() - ((RecycleMenu) this.f_97732_).extra);
                this.canDrag = true;
            }
        }
        return super.mouseClicked(mx, my, btn);
    }

    @Override
    protected boolean click(int btn) {
        if (btn >= 2 && btn < 38) {
            this.forceSelect(btn - 2);
        }
        return super.click(btn);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int pButton) {
        this.dragging = false;
        this.canDrag = false;
        this.pressed = false;
        MenuLayoutConfig.Rect rect = ((RecycleMenu) this.f_97732_).sprite.get().getComp("output");
        if (this.m_6774_(rect.x, rect.y, rect.w, rect.h, mx, my)) {
            this.click(50);
        }
        if (this.hover_a) {
            this.click(51);
            this.hover_a = false;
        }
        if (this.hover_b) {
            this.click(52);
            this.hover_b = false;
        }
        return super.m_6348_(mx, my, pButton);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        MenuLayoutConfig.Rect r = ((RecycleMenu) this.f_97732_).sprite.get().getComp("grid");
        int x = r.x + this.getGuiLeft();
        int y = r.y + this.getGuiTop();
        if (mx >= (double) x && my >= (double) y && mx < (double) (x + r.w * r.rx) && my < (double) (y + r.h * r.ry)) {
            Slot slot = this.getSlotUnderMouse();
            if (this.canDrag && slot != null && slot.getContainerSlot() >= ((RecycleMenu) this.f_97732_).extra) {
                int ind = slot.getContainerSlot() - ((RecycleMenu) this.f_97732_).extra;
                boolean selected = this.isSelected(ind);
                this.dragging = true;
                if (selected == this.enable) {
                    this.click(ind + 2);
                    return true;
                }
            }
        }
        return super.mouseDragged(mx, my, btn, dx, dy);
    }

    @Override
    protected void renderTooltip(GuiGraphics pPoseStack, int pX, int pY) {
        pPoseStack.pose().pushPose();
        pPoseStack.pose().translate(0.0F, (float) this.f_97736_, 0.0F);
        StackedRenderHandle handle = new StackedRenderHandle(this, pPoseStack, 8, -1, ((RecycleMenu) this.f_97732_).sprite.get());
        handle.drawText(LangData.TAB_INFO_TOTAL.get(((RecycleMenu) this.f_97732_).total_count.get()), false);
        handle.drawText(LangData.TAB_INFO_MATCHED.get(((RecycleMenu) this.f_97732_).current_count.get()), false);
        handle.drawText(LangData.TAB_INFO_EXP.get(formatNumber(((RecycleMenu) this.f_97732_).experience.get())), false);
        handle.drawText(LangData.TAB_INFO_SELECTED.get(((RecycleMenu) this.f_97732_).select_count.get()), false);
        handle.drawText(LangData.TAB_INFO_EXP_GAIN.get(formatNumber(((RecycleMenu) this.f_97732_).to_gain.get())), false);
        handle.flushText();
        pPoseStack.pose().popPose();
        if (!this.dragging) {
            super.m_280072_(pPoseStack, pX, pY);
        }
    }

    public static String formatNumber(int number) {
        int level;
        for (level = 0; number >= 1000 && level != SUFFIX.length - 1; level++) {
            if (number < 10000) {
                int a = (int) Math.round((double) number * 0.01);
                int b = a / 10;
                int c = a % 10;
                return b + "." + c + SUFFIX[level + 1];
            }
            number = (int) Math.round((double) number * 0.001);
        }
        return number + SUFFIX[level];
    }
}