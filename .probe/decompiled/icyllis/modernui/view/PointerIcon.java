package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import org.lwjgl.glfw.GLFW;

public final class PointerIcon {

    public static final int TYPE_DEFAULT = 0;

    public static final int TYPE_ARROW = 1000;

    public static final int TYPE_HAND = 1002;

    public static final int TYPE_TEXT = 1008;

    private static final PointerIcon DEFAULT_CURSOR = new PointerIcon(0, 0L);

    private static final PointerIcon TEXT_CURSOR = new PointerIcon(1008, GLFW.glfwCreateStandardCursor(221186));

    private static final PointerIcon HAND_CURSOR = new PointerIcon(1002, GLFW.glfwCreateStandardCursor(221188));

    private final int mType;

    private final long mHandle;

    private PointerIcon(int type, long handle) {
        this.mType = type;
        this.mHandle = handle;
    }

    public int getType() {
        return this.mType;
    }

    public long getHandle() {
        return this.mHandle;
    }

    @NonNull
    public static PointerIcon getSystemIcon(int type) {
        return switch(type) {
            case 1002 ->
                HAND_CURSOR;
            case 1008 ->
                TEXT_CURSOR;
            default ->
                DEFAULT_CURSOR;
        };
    }
}