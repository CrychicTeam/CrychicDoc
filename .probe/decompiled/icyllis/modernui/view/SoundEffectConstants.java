package icyllis.modernui.view;

public final class SoundEffectConstants {

    public static final int CLICK = 0;

    public static final int NAVIGATION_LEFT = 1;

    public static final int NAVIGATION_UP = 2;

    public static final int NAVIGATION_RIGHT = 3;

    public static final int NAVIGATION_DOWN = 4;

    public static final int NAVIGATION_REPEAT_LEFT = 5;

    public static final int NAVIGATION_REPEAT_UP = 6;

    public static final int NAVIGATION_REPEAT_RIGHT = 7;

    public static final int NAVIGATION_REPEAT_DOWN = 8;

    private SoundEffectConstants() {
    }

    public static int getContantForFocusDirection(int direction) {
        return switch(direction) {
            case 1, 33 ->
                2;
            case 2, 130 ->
                4;
            case 17 ->
                1;
            case 66 ->
                3;
            default ->
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
        };
    }
}