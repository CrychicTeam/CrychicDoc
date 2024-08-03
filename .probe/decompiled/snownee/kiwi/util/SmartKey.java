package snownee.kiwi.util;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.function.BooleanSupplier;
import net.minecraft.client.KeyMapping;

public class SmartKey extends KeyMapping {

    private static final long SHORT_PRESS_MAX_MS = 200L;

    private static final long DOUBLE_PRESS_INTERVAL_MS = 200L;

    private static final long LONG_PRESS_MIN_MS = 400L;

    protected long pressSince = -1L;

    protected long lastShortPress = -1L;

    protected SmartKey.State state = SmartKey.State.Idle;

    private final BooleanSupplier onShortPress;

    private final BooleanSupplier onLongPress;

    private final BooleanSupplier onDoublePress;

    private final BooleanSupplier hasDoublePress;

    private SmartKey(SmartKey.Builder builder) {
        super(builder.name, builder.type, builder.keyCode, builder.category);
        this.onShortPress = builder.onShortPress;
        this.onLongPress = builder.onLongPress;
        this.onDoublePress = builder.onDoublePress;
        this.hasDoublePress = builder.hasDoublePress;
    }

    public void tick() {
        if (!this.m_90862_()) {
            long time = net.minecraft.Util.getMillis();
            if (this.m_90857_()) {
                if (this.state != SmartKey.State.LongPress && this.pressSince != -1L && time - this.pressSince > 400L) {
                    this.state = SmartKey.State.LongPress;
                    this.onLongPress();
                }
            } else if (this.state == SmartKey.State.WaitingForDoublePress && time - this.lastShortPress > 200L) {
                this.onShortPress();
                this.pressSince = -1L;
                this.state = SmartKey.State.Idle;
            }
        }
    }

    @Override
    public void setDown(boolean isDown) {
        this.setDownWithResult(isDown);
    }

    public boolean setDownWithResult(boolean isDown) {
        if (this.m_90857_() == isDown) {
            return false;
        } else {
            super.setDown(isDown);
            long time = net.minecraft.Util.getMillis();
            boolean result = false;
            if (isDown) {
                if (this.state != SmartKey.State.WaitingForDoublePress || time - this.lastShortPress >= 200L) {
                    this.pressSince = time;
                    this.state = SmartKey.State.ShortPress;
                    return false;
                }
                this.lastShortPress = -1L;
                result = this.onDoublePress();
                this.state = SmartKey.State.Idle;
            } else if (this.state == SmartKey.State.ShortPress && time - this.pressSince < 200L) {
                this.lastShortPress = time;
                if (this.hasDoublePress()) {
                    this.state = SmartKey.State.WaitingForDoublePress;
                } else {
                    result = this.onShortPress();
                    this.state = SmartKey.State.Idle;
                }
            } else {
                this.state = SmartKey.State.Idle;
            }
            this.pressSince = -1L;
            return result;
        }
    }

    protected boolean hasDoublePress() {
        return this.onDoublePress != null && (this.hasDoublePress == null || this.hasDoublePress.getAsBoolean());
    }

    protected boolean onShortPress() {
        return this.onShortPress != null ? this.onShortPress.getAsBoolean() : false;
    }

    protected boolean onLongPress() {
        return this.onLongPress != null ? this.onLongPress.getAsBoolean() : false;
    }

    protected boolean onDoublePress() {
        return this.onDoublePress != null ? this.onDoublePress.getAsBoolean() : false;
    }

    public static class Builder {

        private final String name;

        private final String category;

        private InputConstants.Type type = InputConstants.Type.KEYSYM;

        private int keyCode = -1;

        private BooleanSupplier onShortPress;

        private BooleanSupplier onLongPress;

        private BooleanSupplier onDoublePress;

        private BooleanSupplier hasDoublePress;

        public Builder(String name, String category) {
            this.name = name;
            this.category = category;
        }

        public SmartKey.Builder key(InputConstants.Key key) {
            this.type = key.getType();
            this.keyCode = key.getValue();
            return this;
        }

        public SmartKey.Builder onShortPress(BooleanSupplier onShortPress) {
            this.onShortPress = onShortPress;
            return this;
        }

        public SmartKey.Builder onLongPress(BooleanSupplier onLongPress) {
            this.onLongPress = onLongPress;
            return this;
        }

        public SmartKey.Builder onDoublePress(BooleanSupplier onDoublePress) {
            this.onDoublePress = onDoublePress;
            return this;
        }

        public SmartKey.Builder hasDoublePress(BooleanSupplier hasDoublePress) {
            this.hasDoublePress = hasDoublePress;
            return this;
        }

        public SmartKey build() {
            return new SmartKey(this);
        }
    }

    public static enum State {

        Idle, ShortPress, WaitingForDoublePress, LongPress
    }
}