package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.util.Pools;
import icyllis.modernui.util.SparseBooleanArray;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.Platform;

public class KeyEvent extends InputEvent {

    public static final int KEY_UNKNOWN = -1;

    public static final int KEY_SPACE = 32;

    public static final int KEY_APOSTROPHE = 39;

    public static final int KEY_COMMA = 44;

    public static final int KEY_MINUS = 45;

    public static final int KEY_PERIOD = 46;

    public static final int KEY_SLASH = 47;

    public static final int KEY_0 = 48;

    public static final int KEY_1 = 49;

    public static final int KEY_2 = 50;

    public static final int KEY_3 = 51;

    public static final int KEY_4 = 52;

    public static final int KEY_5 = 53;

    public static final int KEY_6 = 54;

    public static final int KEY_7 = 55;

    public static final int KEY_8 = 56;

    public static final int KEY_9 = 57;

    public static final int KEY_SEMICOLON = 59;

    public static final int KEY_EQUAL = 61;

    public static final int KEY_A = 65;

    public static final int KEY_B = 66;

    public static final int KEY_C = 67;

    public static final int KEY_D = 68;

    public static final int KEY_E = 69;

    public static final int KEY_F = 70;

    public static final int KEY_G = 71;

    public static final int KEY_H = 72;

    public static final int KEY_I = 73;

    public static final int KEY_J = 74;

    public static final int KEY_K = 75;

    public static final int KEY_L = 76;

    public static final int KEY_M = 77;

    public static final int KEY_N = 78;

    public static final int KEY_O = 79;

    public static final int KEY_P = 80;

    public static final int KEY_Q = 81;

    public static final int KEY_R = 82;

    public static final int KEY_S = 83;

    public static final int KEY_T = 84;

    public static final int KEY_U = 85;

    public static final int KEY_V = 86;

    public static final int KEY_W = 87;

    public static final int KEY_X = 88;

    public static final int KEY_Y = 89;

    public static final int KEY_Z = 90;

    public static final int KEY_LEFT_BRACKET = 91;

    public static final int KEY_BACKSLASH = 92;

    public static final int KEY_RIGHT_BRACKET = 93;

    public static final int KEY_GRAVE_ACCENT = 96;

    public static final int KEY_WORLD_1 = 161;

    public static final int KEY_WORLD_2 = 162;

    public static final int KEY_ESCAPE = 256;

    public static final int KEY_ENTER = 257;

    public static final int KEY_TAB = 258;

    public static final int KEY_BACKSPACE = 259;

    public static final int KEY_INSERT = 260;

    public static final int KEY_DELETE = 261;

    public static final int KEY_RIGHT = 262;

    public static final int KEY_LEFT = 263;

    public static final int KEY_DOWN = 264;

    public static final int KEY_UP = 265;

    public static final int KEY_PAGE_UP = 266;

    public static final int KEY_PAGE_DOWN = 267;

    public static final int KEY_HOME = 268;

    public static final int KEY_END = 269;

    public static final int KEY_CAPS_LOCK = 280;

    public static final int KEY_SCROLL_LOCK = 281;

    public static final int KEY_NUM_LOCK = 282;

    public static final int KEY_PRINT_SCREEN = 283;

    public static final int KEY_PAUSE = 284;

    public static final int KEY_F1 = 290;

    public static final int KEY_F2 = 291;

    public static final int KEY_F3 = 292;

    public static final int KEY_F4 = 293;

    public static final int KEY_F5 = 294;

    public static final int KEY_F6 = 295;

    public static final int KEY_F7 = 296;

    public static final int KEY_F8 = 297;

    public static final int KEY_F9 = 298;

    public static final int KEY_F10 = 299;

    public static final int KEY_F11 = 300;

    public static final int KEY_F12 = 301;

    public static final int KEY_F13 = 302;

    public static final int KEY_F14 = 303;

    public static final int KEY_F15 = 304;

    public static final int KEY_F16 = 305;

    public static final int KEY_F17 = 306;

    public static final int KEY_F18 = 307;

    public static final int KEY_F19 = 308;

    public static final int KEY_F20 = 309;

    public static final int KEY_F21 = 310;

    public static final int KEY_F22 = 311;

    public static final int KEY_F23 = 312;

    public static final int KEY_F24 = 313;

    public static final int KEY_F25 = 314;

    public static final int KEY_KP_0 = 320;

    public static final int KEY_KP_1 = 321;

    public static final int KEY_KP_2 = 322;

    public static final int KEY_KP_3 = 323;

    public static final int KEY_KP_4 = 324;

    public static final int KEY_KP_5 = 325;

    public static final int KEY_KP_6 = 326;

    public static final int KEY_KP_7 = 327;

    public static final int KEY_KP_8 = 328;

    public static final int KEY_KP_9 = 329;

    public static final int KEY_KP_DECIMAL = 330;

    public static final int KEY_KP_DIVIDE = 331;

    public static final int KEY_KP_MULTIPLY = 332;

    public static final int KEY_KP_SUBTRACT = 333;

    public static final int KEY_KP_ADD = 334;

    public static final int KEY_KP_ENTER = 335;

    public static final int KEY_KP_EQUAL = 336;

    public static final int KEY_LEFT_SHIFT = 340;

    public static final int KEY_LEFT_CONTROL = 341;

    public static final int KEY_LEFT_ALT = 342;

    public static final int KEY_LEFT_SUPER = 343;

    public static final int KEY_RIGHT_SHIFT = 344;

    public static final int KEY_RIGHT_CONTROL = 345;

    public static final int KEY_RIGHT_ALT = 346;

    public static final int KEY_RIGHT_SUPER = 347;

    public static final int KEY_MENU = 348;

    public static final int META_SHIFT_ON = 1;

    public static final int META_CTRL_ON;

    public static final int META_ALT_ON = 4;

    public static final int META_SUPER_ON = 8;

    public static final int FLAG_SOFT_KEYBOARD = 2;

    public static final int FLAG_KEEP_TOUCH_MODE = 4;

    public static final int FLAG_FROM_SYSTEM = 8;

    public static final int FLAG_EDITOR_ACTION = 16;

    public static final int FLAG_CANCELED = 32;

    public static final int FLAG_VIRTUAL_HARD_KEY = 64;

    public static final int FLAG_LONG_PRESS = 128;

    public static final int FLAG_CANCELED_LONG_PRESS = 256;

    public static final int FLAG_TRACKING = 512;

    public static final int FLAG_FALLBACK = 1024;

    public static final int FLAG_PREDISPATCH = 536870912;

    public static final int FLAG_START_TRACKING = 1073741824;

    public static final int ACTION_DOWN = 0;

    public static final int ACTION_UP = 1;

    private static final Pools.Pool<KeyEvent> sPool;

    private int mAction;

    private int mKeyCode;

    private int mScanCode;

    private int mRepeatCount;

    private int mFlags;

    private int mModifiers;

    private long mEventTime;

    private KeyEvent() {
    }

    @NonNull
    private static KeyEvent obtain() {
        KeyEvent event = sPool.acquire();
        return event == null ? new KeyEvent() : event;
    }

    @NonNull
    public static KeyEvent obtain(long eventTime, int action, int code, int repeat, int modifiers, int scancode, int flags) {
        KeyEvent ev = obtain();
        ev.mEventTime = eventTime;
        ev.mAction = action;
        ev.mKeyCode = code;
        ev.mScanCode = scancode;
        ev.mRepeatCount = repeat;
        ev.mFlags = flags;
        ev.mModifiers = modifiers;
        return ev;
    }

    private void copyFrom(@NonNull KeyEvent other) {
        this.mEventTime = other.mEventTime;
        this.mAction = other.mAction;
        this.mKeyCode = other.mKeyCode;
        this.mScanCode = other.mScanCode;
        this.mRepeatCount = other.mRepeatCount;
        this.mFlags = other.mFlags;
        this.mModifiers = other.mModifiers;
    }

    public final int getAction() {
        return this.mAction;
    }

    public final int getKeyCode() {
        return this.mKeyCode;
    }

    public final int getScanCode() {
        return this.mScanCode;
    }

    public final int getRepeatCount() {
        return this.mRepeatCount;
    }

    public final int getModifiers() {
        return this.mModifiers;
    }

    public final boolean hasNoModifiers() {
        return this.mModifiers == 0;
    }

    public final boolean hasModifiers(int modifiers) {
        return modifiers == 0 ? this.mModifiers == 0 : this.mModifiers == modifiers;
    }

    public final boolean isShiftPressed() {
        return (this.mModifiers & 1) != 0;
    }

    public final boolean isCtrlPressed() {
        return (this.mModifiers & META_CTRL_ON) != 0;
    }

    public final boolean isAltPressed() {
        return (this.mModifiers & 4) != 0;
    }

    public final boolean isSuperPressed() {
        return (this.mModifiers & 8) != 0;
    }

    public final boolean isCapsLockOn() {
        return (this.mModifiers & 16) != 0;
    }

    public final boolean isNumLockOn() {
        return (this.mModifiers & 32) != 0;
    }

    public final char getMappedChar() {
        String s = GLFW.glfwGetKeyName(this.mKeyCode, this.mScanCode);
        return s != null && s.length() == 1 ? s.charAt(0) : '\u0000';
    }

    @Override
    public void recycle() {
        sPool.release(this);
    }

    @NonNull
    @Override
    public InputEvent copy() {
        KeyEvent ev = obtain();
        ev.copyFrom(this);
        return ev;
    }

    @Override
    public long getEventTime() {
        return this.mEventTime / 1000000L;
    }

    @Override
    public long getEventTimeNano() {
        return this.mEventTime;
    }

    public final boolean isCanceled() {
        return (this.mFlags & 32) != 0;
    }

    @Override
    public final void cancel() {
        this.mFlags |= 32;
    }

    public final void startTracking() {
        this.mFlags |= 1073741824;
    }

    public final boolean isTracking() {
        return (this.mFlags & 512) != 0;
    }

    public final boolean isLongPress() {
        return (this.mFlags & 128) != 0;
    }

    public static boolean isModifierKey(int keyCode) {
        return switch(keyCode) {
            case 340, 341, 342, 343, 344, 345, 346, 347 ->
                true;
            default ->
                false;
        };
    }

    static {
        if (Platform.get() == Platform.MACOSX) {
            META_CTRL_ON = 8;
        } else {
            META_CTRL_ON = 2;
        }
        sPool = Pools.newSynchronizedPool(10);
    }

    public static class DispatcherState {

        int mDownKeyCode;

        Object mDownTarget;

        SparseBooleanArray mActiveLongPresses = new SparseBooleanArray();

        public void reset() {
            this.mDownKeyCode = 0;
            this.mDownTarget = null;
            this.mActiveLongPresses.clear();
        }

        public void reset(Object target) {
            if (this.mDownTarget == target) {
                this.mDownKeyCode = 0;
                this.mDownTarget = null;
            }
        }

        public void startTracking(@NonNull KeyEvent event, Object target) {
            if (event.getAction() != 0) {
                throw new IllegalArgumentException("Can only start tracking on a down event");
            } else {
                this.mDownKeyCode = event.getKeyCode();
                this.mDownTarget = target;
            }
        }

        public boolean isTracking(@NonNull KeyEvent event) {
            return this.mDownKeyCode == event.getKeyCode();
        }

        public void performedLongPress(@NonNull KeyEvent event) {
            this.mActiveLongPresses.put(event.getKeyCode(), true);
        }

        public void handleUpEvent(@NonNull KeyEvent event) {
            int keyCode = event.getKeyCode();
            int index = this.mActiveLongPresses.indexOfKey(keyCode);
            if (index >= 0) {
                event.mFlags |= 288;
                this.mActiveLongPresses.removeAt(index);
            }
            if (this.mDownKeyCode == keyCode) {
                event.mFlags |= 512;
                this.mDownKeyCode = 0;
                this.mDownTarget = null;
            }
        }
    }
}