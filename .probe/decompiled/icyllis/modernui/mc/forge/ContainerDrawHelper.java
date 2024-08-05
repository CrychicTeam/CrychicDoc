package icyllis.modernui.mc.forge;

import icyllis.modernui.graphics.Canvas;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;

public class ContainerDrawHelper {

    public static void drawItem(@Nonnull Canvas canvas, @Nonnull ItemStack item, float x, float y, float z, float size, int seed) {
        icyllis.modernui.mc.ContainerDrawHelper.drawItem(canvas, item, x, y, z, size, seed);
    }
}