package dev.xkmc.l2library.base.overlay;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public abstract class ItemSelSideBar<S extends SideBar.Signature<S>> extends SelectionSideBar<ItemStack, S> {

    public ItemSelSideBar(float duration, float ease) {
        super(duration, ease);
    }

    protected void renderEntry(SelectionSideBar.Context ctx, ItemStack stack, int i, int selected) {
        boolean shift = Minecraft.getInstance().options.keyShift.isDown();
        int y = 18 * i + ctx.y0();
        this.renderSelection(ctx.g(), ctx.x0(), y, shift ? 127 : 64, this.isAvailable(stack), selected == i);
        if (selected == i && !stack.isEmpty() && this.ease_time == this.max_ease) {
            boolean onCenter = this.onCenter();
            ctx.g().renderTooltip(ctx.font(), stack.getHoverName(), 0, 0);
            TextBox box = new TextBox(ctx.g(), onCenter ? 0 : 2, 1, ctx.x0() + (onCenter ? 22 : -6), y + 8, -1);
            box.renderLongText(ctx.font(), List.of(stack.getHoverName()));
        }
        ctx.renderItem(stack, ctx.x0(), y);
    }

    public void renderSelection(GuiGraphics g, int x, int y, int a, boolean available, boolean selected) {
        if (available) {
            OverlayUtil.fillRect(g, x, y, 16, 16, color(255, 255, 255, a));
        } else {
            OverlayUtil.fillRect(g, x, y, 16, 16, color(255, 0, 0, a));
        }
        if (selected) {
            OverlayUtil.drawRect(g, x, y, 16, 16, color(255, 170, 0, 255));
        }
    }

    public static int color(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }
}