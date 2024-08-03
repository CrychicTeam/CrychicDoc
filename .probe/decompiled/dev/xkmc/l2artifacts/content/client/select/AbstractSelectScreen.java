package dev.xkmc.l2artifacts.content.client.select;

import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractSelectScreen extends Screen {

    public final SpriteManager manager;

    public final String[] slots;

    private int imageWidth;

    private int imageHeight;

    private int leftPos;

    private int topPos;

    private ItemStack hovered = null;

    protected AbstractSelectScreen(Component title, SpriteManager manager, String... slots) {
        super(title);
        this.manager = manager;
        this.slots = slots;
    }

    @Override
    protected void init() {
        this.imageWidth = 176;
        this.imageHeight = this.manager.get().getHeight();
        this.leftPos = (this.f_96543_ - this.imageWidth) / 2;
        this.topPos = (this.f_96544_ - this.imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pTick) {
        this.renderBg(g, pTick, mx, my);
        super.render(g, mx, my, pTick);
        g.pose().pushPose();
        g.pose().translate((double) this.leftPos, (double) this.topPos, 0.0);
        this.hovered = null;
        for (String c : this.slots) {
            this.renderSlotComp(g, c, mx, my);
        }
        this.renderLabels(g, mx, my);
        if (this.hovered != null && !this.hovered.isEmpty()) {
            g.pose().pushPose();
            g.pose().translate((float) (-this.leftPos), (float) (-this.topPos), 0.0F);
            g.renderTooltip(this.f_96547_, this.hovered, mx, my);
            g.pose().popPose();
        }
        g.pose().popPose();
    }

    protected abstract void renderLabels(GuiGraphics var1, int var2, int var3);

    protected abstract ItemStack getStack(String var1, int var2, int var3);

    private void renderSlotComp(GuiGraphics pose, String name, int mx, int my) {
        MenuLayoutConfig.Rect comp = this.manager.get().getComp(name);
        for (int i = 0; i < comp.rx; i++) {
            for (int j = 0; j < comp.ry; j++) {
                int sx = comp.x + comp.w * i;
                int sy = comp.y + comp.h * j;
                ItemStack stack = this.getStack(name, i, j);
                this.renderSlot(pose, sx, sy, stack);
                if (this.isHovering(name, i, j, (double) mx, (double) my)) {
                    AbstractContainerScreen.renderSlotHighlight(pose, sx, sy, -2130706433);
                    this.hovered = stack;
                }
            }
        }
    }

    private void renderSlot(GuiGraphics g, int x, int y, ItemStack stack) {
        String s = null;
        assert this.f_96541_ != null;
        assert this.f_96541_.player != null;
        g.renderItem(stack, x, y, x + y * this.imageWidth);
        g.renderItemDecorations(this.f_96547_, stack, x, y, s);
    }

    private void renderBg(GuiGraphics stack, float pt, int mx, int my) {
        MenuLayoutConfig.ScreenRenderer sr = this.manager.get().new ScreenRenderer(this, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
        sr.start(stack);
    }

    private boolean isHovering(String slot, int i, int j, double mx, double my) {
        MenuLayoutConfig.Rect comp = this.manager.get().getComp(slot);
        return this.isHovering(comp.x + comp.w * i, comp.y + comp.h * j, 16, 16, mx, my);
    }

    private boolean isHovering(int x, int y, int w, int h, double mx, double my) {
        int i = this.leftPos;
        int j = this.topPos;
        mx -= (double) i;
        my -= (double) j;
        return mx >= (double) (x - 1) && mx < (double) (x + w + 1) && my >= (double) (y - 1) && my < (double) (y + h + 1);
    }

    @Nullable
    protected AbstractSelectScreen.SlotResult findSlot(double mx, double my) {
        for (String c : this.slots) {
            MenuLayoutConfig.Rect comp = this.manager.get().getComp(c);
            for (int i = 0; i < comp.rx; i++) {
                for (int j = 0; j < comp.ry; j++) {
                    if (this.isHovering(c, i, j, mx, my)) {
                        return new AbstractSelectScreen.SlotResult(c, i, j);
                    }
                }
            }
        }
        return null;
    }

    public static record SlotResult(String name, int x, int y) {
    }
}