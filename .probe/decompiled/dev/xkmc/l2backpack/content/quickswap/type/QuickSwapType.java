package dev.xkmc.l2backpack.content.quickswap.type;

import dev.xkmc.l2backpack.content.quickswap.common.QuickSwapOverlay;
import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2library.base.overlay.OverlayUtil;
import dev.xkmc.l2library.base.overlay.SelectionSideBar;
import dev.xkmc.l2library.base.overlay.TextBox;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class QuickSwapType {

    private final String name;

    private final int index;

    QuickSwapType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public QuickSwapType(String name) {
        this.name = name;
        this.index = QuickSwapTypes.register(this);
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public abstract boolean activePopup();

    public abstract ItemStack getSignatureItem(Player var1);

    public boolean isAvailable(Player player, ISwapEntry<?> token) {
        return true;
    }

    public boolean isAvailable(Player player, ISwapEntry<?> token, int index) {
        return this.isAvailable(player, token);
    }

    public void renderSelected(SelectionSideBar.Context ctx, Player player, ISwapEntry<?> token, int x, int y, boolean selected, boolean center) {
        List<ItemStack> list = token.asList();
        boolean shift = QuickSwapOverlay.hasShiftDown();
        boolean avail = this.isAvailable(player, token);
        for (int i = 0; i < list.size(); i++) {
            renderSelection(ctx.g(), x + i * 18, y, shift ? 127 : 64, avail, (!avail || this.isAvailable(player, token, i)) && selected);
        }
        if (selected && list.size() == 1) {
            ItemStack stack = (ItemStack) list.get(0);
            if (!stack.isEmpty()) {
                ctx.g().renderTooltip(ctx.font(), stack.getHoverName(), 0, 0);
                TextBox box = new TextBox(ctx.g(), center ? 0 : 2, 1, ctx.x0() + (center ? 22 : -6), y + 8, -1);
                box.renderLongText(ctx.font(), List.of(stack.getHoverName()));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            ctx.renderItem((ItemStack) list.get(i), x + i * 18, y);
        }
    }

    public static void renderSelection(GuiGraphics g, int x, int y, int a, boolean available, boolean selected) {
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