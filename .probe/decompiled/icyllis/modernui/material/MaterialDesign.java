package icyllis.modernui.material;

import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.StateSet;

public final class MaterialDesign {

    public static final float disabled_alpha_material_dark = 0.3F;

    public static final int bright_foreground_dark = -16777216;

    public static final int bright_foreground_light = -1;

    public static final int dim_foreground_light = -13487566;

    public static final int dim_foreground_light_disabled = -2144193998;

    public static final ColorStateList secondary_text_light;

    static {
        int[][] states = new int[][] { { 16842919, -16842910 }, { 16842913, -16842910 }, { 16842919 }, { 16842913 }, { 16843518 }, { -16842910 }, StateSet.WILD_CARD };
        int[] colors = new int[] { -2144193998, -2144193998, -13487566, -13487566, -13487566, -13487566, -13487566 };
        secondary_text_light = new ColorStateList(states, colors);
    }
}