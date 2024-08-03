package icyllis.modernui.view;

import icyllis.arc3d.core.Matrix4;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.Matrix;
import icyllis.modernui.util.Pools;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class MotionEvent extends InputEvent {

    public static final int INVALID_POINTER_ID = -1;

    public static final int ACTION_MASK = 255;

    public static final int ACTION_DOWN = 0;

    public static final int ACTION_UP = 1;

    public static final int ACTION_MOVE = 2;

    public static final int ACTION_CANCEL = 3;

    public static final int ACTION_OUTSIDE = 4;

    public static final int ACTION_POINTER_DOWN = 5;

    public static final int ACTION_POINTER_UP = 6;

    public static final int ACTION_HOVER_MOVE = 7;

    public static final int ACTION_SCROLL = 8;

    public static final int ACTION_HOVER_ENTER = 9;

    public static final int ACTION_HOVER_EXIT = 10;

    public static final int ACTION_BUTTON_PRESS = 11;

    public static final int ACTION_BUTTON_RELEASE = 12;

    public static final int ACTION_POINTER_INDEX_MASK = 65280;

    public static final int ACTION_POINTER_INDEX_SHIFT = 8;

    @Internal
    public static final int FLAG_HOVER_EXIT_PENDING = 4;

    @Internal
    public static final int FLAG_TAINTED = Integer.MIN_VALUE;

    public static final int BUTTON_PRIMARY = 1;

    public static final int BUTTON_SECONDARY = 2;

    public static final int BUTTON_TERTIARY = 4;

    public static final int BUTTON_BACK = 8;

    public static final int BUTTON_FORWARD = 16;

    private static final String[] BUTTON_SYMBOLIC_NAMES = new String[] { "BUTTON_PRIMARY", "BUTTON_SECONDARY", "BUTTON_TERTIARY", "BUTTON_BACK", "BUTTON_FORWARD", "0x00000020", "0x00000040", "0x00000080", "0x00000100", "0x00000200", "0x00000400", "0x00000800", "0x00001000", "0x00002000", "0x00004000", "0x00008000", "0x00010000", "0x00020000", "0x00040000", "0x00080000", "0x00100000", "0x00200000", "0x00400000", "0x00800000", "0x01000000", "0x02000000", "0x04000000", "0x08000000", "0x10000000", "0x20000000", "0x40000000", "0x80000000" };

    public static final int TOOL_TYPE_UNKNOWN = 0;

    public static final int AXIS_X = 0;

    public static final int AXIS_Y = 1;

    public static final int AXIS_VSCROLL = 9;

    public static final int AXIS_HSCROLL = 10;

    private static final int INITIAL_PACKED_AXIS_VALUES = 2;

    private static final Pools.Pool<MotionEvent> sPool = Pools.newSynchronizedPool(10);

    private int mAction;

    private int mActionButton;

    private int mFlags;

    private int mModifiers;

    private int mButtonState;

    private final Matrix4 mTransform = new Matrix4();

    private float mRawXCursorPosition;

    private float mRawYCursorPosition;

    private long mEventTime;

    private long mPackedAxisBits;

    private float[] mPackedAxisValues;

    private MotionEvent() {
    }

    @NonNull
    private static MotionEvent obtain() {
        MotionEvent event = sPool.acquire();
        return event == null ? new MotionEvent() : event;
    }

    @NonNull
    public static MotionEvent obtain(long eventTime, int action, float x, float y, int modifiers) {
        return obtain(eventTime, action, 0, x, y, modifiers, 0, 0);
    }

    @NonNull
    public static MotionEvent obtain(long eventTime, int action, int actionButton, float x, float y, int modifiers, int buttonState, int flags) {
        if ((action & 0xFF) != action) {
            throw new IllegalArgumentException("Multiple pointers are disabled");
        } else if ((action == 11 || action == 12) && actionButton == 0) {
            throw new IllegalArgumentException("actionButton should be defined for action press or release");
        } else {
            MotionEvent event = obtain();
            event.initialize(action, actionButton, flags, modifiers, buttonState, x, y, eventTime);
            return event;
        }
    }

    private void copyFrom(@NonNull MotionEvent other) {
        this.mAction = other.mAction;
        this.mActionButton = other.mActionButton;
        this.mFlags = other.mFlags;
        this.mModifiers = other.mModifiers;
        this.mButtonState = other.mButtonState;
        this.mTransform.set(other.mTransform);
        this.mRawXCursorPosition = other.mRawXCursorPosition;
        this.mRawYCursorPosition = other.mRawYCursorPosition;
        this.mEventTime = other.mEventTime;
        long bits = other.mPackedAxisBits;
        this.mPackedAxisBits = bits;
        if (bits != 0L) {
            float[] otherValues = other.mPackedAxisValues;
            int count = Long.bitCount(bits);
            float[] values = this.mPackedAxisValues;
            if (values == null || count > values.length) {
                values = new float[otherValues.length];
                this.mPackedAxisValues = values;
            }
            System.arraycopy(otherValues, 0, values, 0, count);
        }
    }

    private void initialize(int action, int actionButton, int flags, int modifiers, int buttonState, float rawXCursorPosition, float rawYCursorPosition, long eventTime) {
        this.mAction = action;
        this.mActionButton = actionButton;
        this.mFlags = flags;
        this.mModifiers = modifiers;
        this.mButtonState = buttonState;
        this.mTransform.setIdentity();
        this.mRawXCursorPosition = rawXCursorPosition;
        this.mRawYCursorPosition = rawYCursorPosition;
        this.mEventTime = eventTime;
        this.mPackedAxisBits = 0L;
    }

    @Deprecated
    private void updateCursorPosition() {
        float x = 0.0F;
        float y = 0.0F;
        int pointerCount = this.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += this.getX(i);
            y += this.getY(i);
        }
        x /= (float) pointerCount;
        y /= (float) pointerCount;
        this.setCursorPosition(x, y);
    }

    @Deprecated
    private void setCursorPosition(float x, float y) {
    }

    @Override
    public void recycle() {
        sPool.release(this);
    }

    public int getAction() {
        return this.mAction;
    }

    public int getActionMasked() {
        return this.mAction & 0xFF;
    }

    @Deprecated
    private int getActionIndex() {
        return (this.mAction & 0xFF00) >> 8;
    }

    public boolean isTouchEvent() {
        return switch(this.mAction & 0xFF) {
            case 0, 1, 2, 3, 4, 5, 6 ->
                true;
            default ->
                false;
        };
    }

    public void setAction(int action) {
        this.mAction = action;
    }

    public int getButtonState() {
        return this.mButtonState;
    }

    public boolean isButtonPressed(int button) {
        return button == 0 ? false : (this.mButtonState & button) == button;
    }

    public int getPointerCount() {
        return 1;
    }

    public int getPointerId(int pointerIndex) {
        if (pointerIndex >= 0 && pointerIndex < this.getPointerCount()) {
            return 0;
        } else {
            throw new IllegalArgumentException("pointerIndex out of range");
        }
    }

    public int getToolType(int pointerIndex) {
        if (pointerIndex >= 0 && pointerIndex < this.getPointerCount()) {
            return 0;
        } else {
            throw new IllegalArgumentException("pointerIndex out of range");
        }
    }

    public boolean isHoverExitPending() {
        return (this.mFlags & 4) != 0;
    }

    public void setHoverExitPending(boolean hoverExitPending) {
        if (hoverExitPending) {
            this.mFlags |= 4;
        } else {
            this.mFlags &= -5;
        }
    }

    @NonNull
    public MotionEvent copy() {
        MotionEvent ev = obtain();
        ev.copyFrom(this);
        return ev;
    }

    @Deprecated
    private long getDownTime() {
        return this.mEventTime / 1000000L;
    }

    @Override
    public long getEventTime() {
        return this.mEventTime / 1000000L;
    }

    @Override
    public long getEventTimeNano() {
        return this.mEventTime;
    }

    @Override
    public void cancel() {
        this.setAction(3);
    }

    @Deprecated
    private MotionEvent.PointerCoords getRawPointerCoords(int pointerIndex) {
        throw new IllegalStateException();
    }

    @Deprecated
    private float getRawAxisValue(int axis, int pointerIndex) {
        if (pointerIndex >= 0 && pointerIndex < this.getPointerCount()) {
            return this.getRawPointerCoords(pointerIndex).getAxisValue(axis);
        } else {
            throw new IllegalArgumentException("pointerIndex out of range");
        }
    }

    @Deprecated
    private float getAxisValue(int axis, int pointerIndex) {
        if (pointerIndex >= 0 && pointerIndex < this.getPointerCount()) {
            return this.getRawPointerCoords(pointerIndex).getAxisValue(axis);
        } else {
            throw new IllegalArgumentException("pointerIndex out of range");
        }
    }

    public void setAxisValue(int axis, float value) {
        switch(axis) {
            case 0:
            case 1:
                throw new IllegalArgumentException("Axis X and Y are not expected to change.");
            default:
                if (axis >= 0 && axis <= 63) {
                    long bits = this.mPackedAxisBits;
                    long axisBit = Long.MIN_VALUE >>> axis;
                    int index = Long.bitCount(bits & ~(-1L >>> axis));
                    float[] values = this.mPackedAxisValues;
                    if ((bits & axisBit) == 0L) {
                        if (values == null) {
                            values = new float[2];
                            this.mPackedAxisValues = values;
                        } else {
                            int count = Long.bitCount(bits);
                            if (count < values.length) {
                                if (index != count) {
                                    System.arraycopy(values, index, values, index + 1, count - index);
                                }
                            } else {
                                float[] newValues = new float[count * 2];
                                System.arraycopy(values, 0, newValues, 0, index);
                                System.arraycopy(values, index, newValues, index + 1, count - index);
                                values = newValues;
                                this.mPackedAxisValues = newValues;
                            }
                        }
                        this.mPackedAxisBits = bits | axisBit;
                    }
                    values[index] = value;
                } else {
                    throw new IllegalArgumentException("Axis out of range.");
                }
        }
    }

    public float getAxisValue(int axis) {
        switch(axis) {
            case 0:
                return this.getX();
            case 1:
                return this.getY();
            default:
                if (axis >= 0 && axis <= 63) {
                    long bits = this.mPackedAxisBits;
                    long axisBit = Long.MIN_VALUE >>> axis;
                    if ((bits & axisBit) == 0L) {
                        return 0.0F;
                    } else {
                        int index = Long.bitCount(bits & ~(-1L >>> axis));
                        return this.mPackedAxisValues[index];
                    }
                } else {
                    throw new IllegalArgumentException("Axis out of range.");
                }
        }
    }

    public float getX() {
        return this.mTransform.mapPointX(this.mRawXCursorPosition, this.mRawYCursorPosition);
    }

    public float getY() {
        return this.mTransform.mapPointY(this.mRawXCursorPosition, this.mRawYCursorPosition);
    }

    @Deprecated
    private float getX(int pointerIndex) {
        return this.getAxisValue(0, pointerIndex);
    }

    @Deprecated
    private float getY(int pointerIndex) {
        return this.getAxisValue(1, pointerIndex);
    }

    public float getRawX() {
        return this.mRawXCursorPosition;
    }

    public float getRawY() {
        return this.mRawYCursorPosition;
    }

    @Deprecated
    private float getRawX(int pointerIndex) {
        return this.getRawAxisValue(0, pointerIndex);
    }

    @Deprecated
    private float getRawY(int pointerIndex) {
        return this.getRawAxisValue(1, pointerIndex);
    }

    public int getActionButton() {
        return this.mActionButton;
    }

    @Deprecated
    private float getXCursorPosition() {
        return 0.0F;
    }

    @Deprecated
    private float getYCursorPosition() {
        return 0.0F;
    }

    public void offsetLocation(float deltaX, float deltaY) {
        this.mTransform.preTranslate(deltaX, deltaY);
    }

    public void setLocation(float x, float y) {
        float oldX = this.getX();
        float oldY = this.getY();
        this.offsetLocation(x - oldX, y - oldY);
    }

    public void transform(@NonNull Matrix matrix) {
        this.mTransform.preConcat2D(matrix);
    }

    public int getModifiers() {
        return this.mModifiers;
    }

    public boolean hasModifiers(int modifiers) {
        return modifiers == 0 ? this.mModifiers == 0 : (this.mModifiers & modifiers) == modifiers;
    }

    public boolean isShiftPressed() {
        return (this.mModifiers & 1) != 0;
    }

    public boolean isCtrlPressed() {
        return (this.mModifiers & KeyEvent.META_CTRL_ON) != 0;
    }

    public boolean isAltPressed() {
        return (this.mModifiers & 4) != 0;
    }

    public boolean isSuperPressed() {
        return (this.mModifiers & 8) != 0;
    }

    public boolean isCapsLockOn() {
        return (this.mModifiers & 16) != 0;
    }

    public boolean isNumLockOn() {
        return (this.mModifiers & 32) != 0;
    }

    @NonNull
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("MotionEvent { action=").append(actionToString(this.getAction()));
        msg.append(", x=").append(this.getX());
        msg.append(", y=").append(this.getY());
        msg.append(", buttonState=").append(buttonStateToString(this.getButtonState()));
        if (this.mFlags != 0) {
            msg.append(", flags=0x").append(Integer.toHexString(this.mFlags));
        }
        msg.append(", eventTime=").append(this.getEventTime());
        msg.append(" }");
        return msg.toString();
    }

    @NonNull
    public static String actionToString(int action) {
        switch(action) {
            case 0:
                return "ACTION_DOWN";
            case 1:
                return "ACTION_UP";
            case 2:
                return "ACTION_MOVE";
            case 3:
                return "ACTION_CANCEL";
            case 4:
                return "ACTION_OUTSIDE";
            case 5:
            case 6:
            default:
                int index = (action & 0xFF00) >> 8;
                return switch(action & 0xFF) {
                    case 5 ->
                        "ACTION_POINTER_DOWN(" + index + ")";
                    case 6 ->
                        "ACTION_POINTER_UP(" + index + ")";
                    default ->
                        Integer.toString(action);
                };
            case 7:
                return "ACTION_HOVER_MOVE";
            case 8:
                return "ACTION_SCROLL";
            case 9:
                return "ACTION_HOVER_ENTER";
            case 10:
                return "ACTION_HOVER_EXIT";
            case 11:
                return "ACTION_BUTTON_PRESS";
            case 12:
                return "ACTION_BUTTON_RELEASE";
        }
    }

    public static String buttonStateToString(int buttonState) {
        if (buttonState == 0) {
            return "0";
        } else {
            StringBuilder result = null;
            for (int i = 0; buttonState != 0; i++) {
                boolean isSet = (buttonState & 1) != 0;
                buttonState >>>= 1;
                if (isSet) {
                    String name = BUTTON_SYMBOLIC_NAMES[i];
                    if (result == null) {
                        if (buttonState == 0) {
                            return name;
                        }
                        result = new StringBuilder(name);
                    } else {
                        result.append('|');
                        result.append(name);
                    }
                }
            }
            assert result != null;
            return result.toString();
        }
    }

    @Deprecated
    private static final class PointerCoords {

        private static final int INITIAL_PACKED_AXIS_VALUES = 8;

        private long mPackedAxisBits;

        private float[] mPackedAxisValues;

        public PointerCoords() {
        }

        public PointerCoords(MotionEvent.PointerCoords other) {
            this.copyFrom(other);
        }

        @NonNull
        public static MotionEvent.PointerCoords[] createArray(int size) {
            MotionEvent.PointerCoords[] array = new MotionEvent.PointerCoords[size];
            for (int i = 0; i < size; i++) {
                array[i] = new MotionEvent.PointerCoords();
            }
            return array;
        }

        public void reset() {
            this.mPackedAxisBits = 0L;
        }

        public void copyFrom(@NonNull MotionEvent.PointerCoords other) {
            long bits = other.mPackedAxisBits;
            this.mPackedAxisBits = bits;
            if (bits != 0L) {
                float[] otherValues = other.mPackedAxisValues;
                int count = Long.bitCount(bits);
                float[] values = this.mPackedAxisValues;
                if (values == null || count > values.length) {
                    values = new float[otherValues.length];
                    this.mPackedAxisValues = values;
                }
                System.arraycopy(otherValues, 0, values, 0, count);
            }
        }

        public float getAxisValue(int axis) {
            if (axis >= 0 && axis <= 63) {
                long bits = this.mPackedAxisBits;
                long axisBit = Long.MIN_VALUE >>> axis;
                if ((bits & axisBit) == 0L) {
                    return 0.0F;
                } else {
                    int index = Long.bitCount(bits & ~(-1L >>> axis));
                    return this.mPackedAxisValues[index];
                }
            } else {
                throw new IllegalArgumentException("Axis out of range.");
            }
        }

        public void setAxisValue(int axis, float value) {
            if (axis >= 0 && axis <= 63) {
                long bits = this.mPackedAxisBits;
                long axisBit = Long.MIN_VALUE >>> axis;
                int index = Long.bitCount(bits & ~(-1L >>> axis));
                float[] values = this.mPackedAxisValues;
                if ((bits & axisBit) == 0L) {
                    if (values == null) {
                        values = new float[8];
                        this.mPackedAxisValues = values;
                    } else {
                        int count = Long.bitCount(bits);
                        if (count < values.length) {
                            if (index != count) {
                                System.arraycopy(values, index, values, index + 1, count - index);
                            }
                        } else {
                            float[] newValues = new float[count * 2];
                            System.arraycopy(values, 0, newValues, 0, index);
                            System.arraycopy(values, index, newValues, index + 1, count - index);
                            values = newValues;
                            this.mPackedAxisValues = newValues;
                        }
                    }
                    this.mPackedAxisBits = bits | axisBit;
                }
                values[index] = value;
            } else {
                throw new IllegalArgumentException("Axis out of range.");
            }
        }
    }

    @Deprecated
    private static final class PointerProperties {

        public int id;

        public int toolType;

        public PointerProperties() {
            this.reset();
        }

        public PointerProperties(MotionEvent.PointerProperties other) {
            this.copyFrom(other);
        }

        @NonNull
        public static MotionEvent.PointerProperties[] createArray(int size) {
            MotionEvent.PointerProperties[] array = new MotionEvent.PointerProperties[size];
            for (int i = 0; i < size; i++) {
                array[i] = new MotionEvent.PointerProperties();
            }
            return array;
        }

        public void reset() {
            this.id = -1;
            this.toolType = 0;
        }

        public void copyFrom(@NonNull MotionEvent.PointerProperties other) {
            this.id = other.id;
            this.toolType = other.toolType;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                MotionEvent.PointerProperties that = (MotionEvent.PointerProperties) o;
                return this.id == that.id && this.toolType == that.toolType;
            } else {
                return false;
            }
        }

        public int hashCode() {
            return this.id | this.toolType << 8;
        }
    }
}