package icyllis.modernui.graphics.drawable;

import icyllis.arc3d.core.Color;
import icyllis.arc3d.core.effects.BlendModeColorFilter;
import icyllis.modernui.annotation.ColorInt;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.resources.Resources;
import icyllis.modernui.resources.Theme;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.StateSet;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class Drawable {

    private static final Rect ZERO_BOUNDS_RECT = new Rect();

    static final BlendMode DEFAULT_BLEND_MODE = BlendMode.SRC_IN;

    private int[] mStateSet = StateSet.WILD_CARD;

    private int mLevel = 0;

    private int mChangingConfigurations = 0;

    private Rect mBounds = ZERO_BOUNDS_RECT;

    @Nullable
    private WeakReference<Drawable.Callback> mCallback = null;

    private boolean mVisible = true;

    private int mLayoutDirection;

    public static final int MAX_LEVEL = 10000;

    public abstract void draw(@NonNull Canvas var1);

    public void setBounds(int left, int top, int right, int bottom) {
        Rect oldBounds = this.mBounds;
        if (oldBounds == ZERO_BOUNDS_RECT) {
            oldBounds = this.mBounds = new Rect();
        }
        if (oldBounds.left != left || oldBounds.top != top || oldBounds.right != right || oldBounds.bottom != bottom) {
            if (!oldBounds.isEmpty()) {
                this.invalidateSelf();
            }
            this.mBounds.set(left, top, right, bottom);
            this.onBoundsChange(this.mBounds);
        }
    }

    public void setBounds(@NonNull Rect bounds) {
        this.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    public final void copyBounds(@NonNull Rect bounds) {
        bounds.set(this.mBounds);
    }

    @NonNull
    public final Rect copyBounds() {
        return new Rect(this.mBounds);
    }

    @NonNull
    public final Rect getBounds() {
        if (this.mBounds == ZERO_BOUNDS_RECT) {
            this.mBounds = new Rect();
        }
        return this.mBounds;
    }

    public void setChangingConfigurations(int configs) {
        this.mChangingConfigurations = configs;
    }

    public int getChangingConfigurations() {
        return this.mChangingConfigurations;
    }

    public final void setCallback(@Nullable Drawable.Callback cb) {
        this.mCallback = cb != null ? new WeakReference(cb) : null;
    }

    @Nullable
    public Drawable.Callback getCallback() {
        return this.mCallback != null ? (Drawable.Callback) this.mCallback.get() : null;
    }

    public void invalidateSelf() {
        Drawable.Callback callback = this.getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    public void scheduleSelf(@NonNull Runnable what, long when) {
        Drawable.Callback callback = this.getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    public void unscheduleSelf(@NonNull Runnable what) {
        Drawable.Callback callback = this.getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    public int getLayoutDirection() {
        return this.mLayoutDirection;
    }

    public final boolean setLayoutDirection(int layoutDirection) {
        if (this.mLayoutDirection != layoutDirection) {
            this.mLayoutDirection = layoutDirection;
            return this.onLayoutDirectionChanged(layoutDirection);
        } else {
            return false;
        }
    }

    protected boolean onLayoutDirectionChanged(int layoutDirection) {
        return false;
    }

    public void setAlpha(int alpha) {
    }

    public int getAlpha() {
        return 255;
    }

    public void setTint(@ColorInt int tintColor) {
        this.setTintList(ColorStateList.valueOf(tintColor));
    }

    public void setTintList(@Nullable ColorStateList tint) {
    }

    public void setTintBlendMode(@NonNull BlendMode blendMode) {
    }

    public void setHotspot(float x, float y) {
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
    }

    public void getHotspotBounds(@NonNull Rect outRect) {
        outRect.set(this.getBounds());
    }

    public boolean isStateful() {
        return false;
    }

    public boolean hasFocusStateSpecified() {
        return false;
    }

    public boolean setState(@NonNull int[] stateSet) {
        if (!Arrays.equals(this.mStateSet, stateSet)) {
            this.mStateSet = stateSet;
            return this.onStateChange(stateSet);
        } else {
            return false;
        }
    }

    @NonNull
    public int[] getState() {
        return this.mStateSet;
    }

    public void jumpToCurrentState() {
    }

    @Nullable
    public Drawable getCurrent() {
        return this;
    }

    public final boolean setLevel(int level) {
        if (this.mLevel != level) {
            this.mLevel = level;
            return this.onLevelChange(level);
        } else {
            return false;
        }
    }

    public final int getLevel() {
        return this.mLevel;
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = this.mVisible != visible;
        if (changed) {
            this.mVisible = visible;
            this.invalidateSelf();
        }
        return changed;
    }

    public final boolean isVisible() {
        return this.mVisible;
    }

    public void setAutoMirrored(boolean mirrored) {
    }

    public boolean isAutoMirrored() {
        return false;
    }

    public void applyTheme(@NonNull Theme t) {
    }

    public boolean canApplyTheme() {
        return false;
    }

    protected boolean onStateChange(@NonNull int[] state) {
        return false;
    }

    protected boolean onLevelChange(int level) {
        return false;
    }

    protected void onBoundsChange(@NonNull Rect bounds) {
    }

    public int getIntrinsicWidth() {
        return -1;
    }

    public int getIntrinsicHeight() {
        return -1;
    }

    public int getMinimumWidth() {
        int intrinsicWidth = this.getIntrinsicWidth();
        return Math.max(intrinsicWidth, 0);
    }

    public int getMinimumHeight() {
        int intrinsicHeight = this.getIntrinsicHeight();
        return Math.max(intrinsicHeight, 0);
    }

    public boolean getPadding(@NonNull Rect padding) {
        padding.set(0, 0, 0, 0);
        return false;
    }

    @NonNull
    public Drawable mutate() {
        return this;
    }

    @Internal
    public void clearMutated() {
    }

    @Nullable
    public Drawable.ConstantState getConstantState() {
        return null;
    }

    @Nullable
    BlendModeColorFilter updateBlendModeFilter(@Nullable BlendModeColorFilter oldBlendFilter, @Nullable ColorStateList tint, @Nullable BlendMode blendMode) {
        if (tint != null && blendMode != null) {
            int color = tint.getColorForState(this.getState(), 0);
            if (oldBlendFilter != null && oldBlendFilter.getMode() == blendMode.nativeBlendMode()) {
                float[] color4f = Color.load_and_premul(color);
                return icyllis.modernui.graphics.Color.equals_within_tolerance(oldBlendFilter.getColor4f(), color4f, 9.765625E-4F) ? oldBlendFilter : BlendModeColorFilter.make(color4f, true, blendMode.nativeBlendMode());
            } else {
                return BlendModeColorFilter.make(color, blendMode.nativeBlendMode());
            }
        } else {
            return null;
        }
    }

    @Internal
    public static float scaleFromDensity(float pixels, int sourceDensity, int targetDensity) {
        return pixels * (float) targetDensity / (float) sourceDensity;
    }

    @Internal
    public static int scaleFromDensity(int pixels, int sourceDensity, int targetDensity, boolean isSize) {
        if (pixels != 0 && sourceDensity != targetDensity) {
            float result = (float) (pixels * targetDensity) / (float) sourceDensity;
            if (!isSize) {
                return (int) result;
            } else {
                int rounded = Math.round(result);
                if (rounded != 0) {
                    return rounded;
                } else {
                    return pixels > 0 ? 1 : -1;
                }
            }
        } else {
            return pixels;
        }
    }

    @Internal
    public static int resolveDensity(@Nullable Resources r, int parentDensity) {
        int densityDpi = r == null ? parentDensity : r.getDisplayMetrics().densityDpi;
        return densityDpi == 0 ? 72 : densityDpi;
    }

    public interface Callback {

        void invalidateDrawable(@NonNull Drawable var1);

        void scheduleDrawable(@NonNull Drawable var1, @NonNull Runnable var2, long var3);

        void unscheduleDrawable(@NonNull Drawable var1, @NonNull Runnable var2);
    }

    public abstract static class ConstantState {

        @NonNull
        public abstract Drawable newDrawable();

        @NonNull
        public Drawable newDrawable(@Nullable Resources res) {
            return this.newDrawable();
        }

        public int getChangingConfigurations() {
            return 0;
        }

        public boolean canApplyTheme() {
            return false;
        }
    }
}