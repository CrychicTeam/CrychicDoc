package icyllis.modernui.resources;

import icyllis.modernui.graphics.Color;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.StateSet;

public class SystemTheme {

    public static final int COLOR_FOREGROUND = -1;

    public static final int COLOR_FOREGROUND_NORMAL = -5197648;

    public static final int COLOR_FOREGROUND_DISABLED = -12632257;

    public static final float DISABLED_ALPHA = 0.3F;

    public static final float PRIMARY_CONTENT_ALPHA = 1.0F;

    public static final float SECONDARY_CONTENT_ALPHA = 0.7F;

    public static final int COLOR_CONTROL_ACTIVATED = -3300456;

    public static final ColorStateList TEXT_COLOR_SECONDARY;

    public static final ColorStateList COLOR_CONTROL_NORMAL;

    public static int modulateColor(int baseColor, float alphaMod) {
        if (alphaMod == 1.0F) {
            return baseColor;
        } else {
            int baseAlpha = Color.alpha(baseColor);
            int alpha = MathUtil.clamp((int) ((float) baseAlpha * alphaMod + 0.5F), 0, 255);
            return baseColor & 16777215 | alpha << 24;
        }
    }

    static {
        int[][] stateSet = new int[][] { { -16842910 }, { 16843623 }, StateSet.WILD_CARD };
        int[] colors = new int[] { -12632257, -1, -5197648 };
        TEXT_COLOR_SECONDARY = new ColorStateList(stateSet, colors);
        COLOR_CONTROL_NORMAL = TEXT_COLOR_SECONDARY;
    }
}