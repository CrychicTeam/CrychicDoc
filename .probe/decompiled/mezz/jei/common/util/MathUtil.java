package mezz.jei.common.util;

import java.util.Collection;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.phys.Vec2;

public final class MathUtil {

    private MathUtil() {
    }

    public static int divideCeil(int numerator, int denominator) {
        return (int) Math.ceil((double) ((float) numerator / (float) denominator));
    }

    public static boolean intersects(Collection<ImmutableRect2i> areas, ImmutableRect2i comparisonArea) {
        return areas.stream().anyMatch(comparisonArea::intersects);
    }

    public static boolean contains(Rect2i rect, double x, double y) {
        return x >= (double) rect.getX() && y >= (double) rect.getY() && x < (double) (rect.getX() + rect.getWidth()) && y < (double) (rect.getY() + rect.getHeight());
    }

    public static ImmutableRect2i union(ImmutableRect2i rect1, ImmutableRect2i rect2) {
        if (rect1.isEmpty()) {
            return rect2;
        } else if (rect2.isEmpty()) {
            return rect1;
        } else {
            long tx2 = (long) rect1.getWidth();
            long ty2 = (long) rect1.getHeight();
            long rx2 = (long) rect2.getWidth();
            long ry2 = (long) rect2.getHeight();
            int tx1 = rect1.getX();
            int ty1 = rect1.getY();
            tx2 += (long) tx1;
            ty2 += (long) ty1;
            int rx1 = rect2.getX();
            int ry1 = rect2.getY();
            rx2 += (long) rx1;
            ry2 += (long) ry1;
            if (tx1 > rx1) {
                tx1 = rx1;
            }
            if (ty1 > ry1) {
                ty1 = ry1;
            }
            if (tx2 < rx2) {
                tx2 = rx2;
            }
            if (ty2 < ry2) {
                ty2 = ry2;
            }
            tx2 -= (long) tx1;
            ty2 -= (long) ty1;
            tx2 = Math.min(tx2, 2147483647L);
            ty2 = Math.min(ty2, 2147483647L);
            return new ImmutableRect2i(tx1, ty1, (int) tx2, (int) ty2);
        }
    }

    public static ImmutableRect2i centerTextArea(ImmutableRect2i outer, Font fontRenderer, String text) {
        int width = fontRenderer.width(text);
        int height = 9;
        return centerArea(outer, width, height);
    }

    public static ImmutableRect2i centerTextArea(ImmutableRect2i outer, Font fontRenderer, FormattedText text) {
        int width = fontRenderer.width(text);
        int height = 9;
        return centerArea(outer, width, height);
    }

    public static ImmutableRect2i centerArea(ImmutableRect2i outer, int width, int height) {
        return new ImmutableRect2i(outer.getX() + Math.round((float) (outer.getWidth() - width) / 2.0F), outer.getY() + Math.round((float) (outer.getHeight() - height) / 2.0F), width, height);
    }

    public static double distance(Vec2 start, Vec2 end) {
        double a = (double) (start.x - end.x);
        double b = (double) (start.y - end.y);
        return Math.sqrt(a * a + b * b);
    }
}