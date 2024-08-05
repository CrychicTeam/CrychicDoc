package icyllis.modernui.mc.ui;

import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.View;

public class ThemeControl {

    public static final int BACKGROUND_COLOR = -1071044052;

    public static final int THEME_COLOR = -3300456;

    public static final int THEME_COLOR_2 = -3303261;

    public static void addBackground(View view) {
        StateListDrawable background = new StateListDrawable();
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.setShape(0);
        drawable.setColor(-2136956768);
        drawable.setCornerRadius((float) view.dp(2.0F));
        background.addState(StateSet.get(64), drawable);
        background.setEnterFadeDuration(250);
        background.setExitFadeDuration(250);
        view.setBackground(background);
    }

    public static Drawable makeDivider(View view) {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.setShape(3);
        drawable.setColor(-3300456);
        drawable.setSize(-1, view.dp(2.0F));
        return drawable;
    }
}