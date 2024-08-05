package dev.xkmc.l2backpack.content.render;

import dev.xkmc.l2backpack.content.bag.AbstractBag;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class BagCountDeco implements IItemDecorator {

    @Override
    public boolean render(GuiGraphics g, Font font, ItemStack stack, int x, int y) {
        if (stack.getItem() instanceof AbstractBag item) {
            int size = item.getSize(stack);
            if (size == 0) {
                return false;
            } else {
                String s = size + "";
                g.pose().pushPose();
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
        } else {
            return false;
        }
    }

    private static int getZ() {
        return 250;
    }
}