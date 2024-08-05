package dev.xkmc.l2backpack.content.render;

import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import dev.xkmc.l2backpack.content.drawer.DrawerItem;
import dev.xkmc.l2backpack.content.remote.drawer.EnderDrawerItem;
import dev.xkmc.l2backpack.init.data.BackpackConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class DrawerCountDeco implements IItemDecorator {

    private static int renderTooltipContent = 0;

    public static void startTooltipRendering() {
        renderTooltipContent++;
    }

    public static void stopTooltipRendering() {
        renderTooltipContent = 0;
    }

    public static boolean showContent() {
        if (BackpackConfig.CLIENT.drawerAlwaysRenderFlat.get()) {
            return true;
        } else {
            return renderTooltipContent > 0 ? true : Screen.hasShiftDown() || Screen.hasControlDown() || Screen.hasAltDown();
        }
    }

    @Override
    public boolean render(GuiGraphics g, Font font, ItemStack stack, int x, int y) {
        if (stack.getItem() instanceof BaseDrawerItem item) {
            String s = getCount(item, stack);
            if (!s.isEmpty()) {
                g.pose().pushPose();
                if (showContent()) {
                    this.drawBG(g, item, x, y);
                }
                int height = getZ();
                int width = font.width(s);
                int x0 = Math.max(3, 17 - width);
                g.pose().translate((float) (x + x0), (float) (y + 16), (float) height);
                if (width > 14) {
                    float sc = 14.0F / (float) width;
                    g.pose().scale(sc, sc, 1.0F);
                }
                int col = -129;
                g.drawString(font, s, 0, -7, col);
                g.pose().popPose();
                return true;
            }
        }
        return false;
    }

    private void drawBG(GuiGraphics g, BaseDrawerItem item, int x, int y) {
        g.blit(item.backgroundLoc(), x, y, 0.0F, 0.0F, 16, 16, 16, 16);
    }

    private static String getCount(BaseDrawerItem item, ItemStack stack) {
        if (item.canSetNewItem(stack)) {
            return "";
        } else if (item instanceof DrawerItem) {
            int count = DrawerItem.getCount(stack);
            if (count == 0) {
                return "";
            } else {
                return count <= 999 ? count + "" : count / 1000 + "k";
            }
        } else if (item instanceof EnderDrawerItem) {
            return Screen.hasShiftDown() ? "?" : "";
        } else {
            return "";
        }
    }

    private static int getZ() {
        return 250;
    }
}