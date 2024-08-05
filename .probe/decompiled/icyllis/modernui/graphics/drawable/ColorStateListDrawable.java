package icyllis.modernui.graphics.drawable;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.util.ColorStateList;
import java.util.Objects;

public class ColorStateListDrawable extends Drawable implements Drawable.Callback {

    private ColorDrawable mColorDrawable;

    private ColorStateListDrawable.ColorStateListDrawableState mState;

    private boolean mMutated = false;

    public ColorStateListDrawable() {
        this.mState = new ColorStateListDrawable.ColorStateListDrawableState();
        this.initializeColorDrawable();
    }

    public ColorStateListDrawable(@NonNull ColorStateList colorStateList) {
        this.mState = new ColorStateListDrawable.ColorStateListDrawableState();
        this.initializeColorDrawable();
        this.setColorStateList(colorStateList);
    }

    private ColorStateListDrawable(@NonNull ColorStateListDrawable.ColorStateListDrawableState state) {
        this.mState = state;
        this.initializeColorDrawable();
        this.onStateChange(this.getState());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        this.mColorDrawable.draw(canvas);
    }

    @Override
    public int getAlpha() {
        return this.mColorDrawable.getAlpha();
    }

    @Override
    public boolean isStateful() {
        return this.mState.isStateful();
    }

    @Override
    public boolean hasFocusStateSpecified() {
        return this.mState.hasFocusStateSpecified();
    }

    @NonNull
    @Override
    public Drawable getCurrent() {
        return this.mColorDrawable;
    }

    @Override
    public void setAlpha(int alpha) {
        this.mState.mAlpha = alpha;
        this.onStateChange(this.getState());
    }

    public void clearAlpha() {
        this.mState.mAlpha = -1;
        this.onStateChange(this.getState());
    }

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        this.mState.mTint = tint;
        this.mColorDrawable.setTintList(tint);
        this.onStateChange(this.getState());
    }

    @Override
    public void setTintBlendMode(@NonNull BlendMode blendMode) {
        this.mState.mBlendMode = blendMode;
        this.mColorDrawable.setTintBlendMode(blendMode);
        this.onStateChange(this.getState());
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);
        this.mColorDrawable.setBounds(bounds);
    }

    @Override
    protected boolean onStateChange(@NonNull int[] state) {
        if (this.mState.mColor != null) {
            int color = this.mState.mColor.getColorForState(state, this.mState.mColor.getDefaultColor());
            if (this.mState.mAlpha != -1) {
                color = color & 16777215 | this.mState.mAlpha << 24;
            }
            if (color != this.mColorDrawable.getColor()) {
                this.mColorDrawable.setColor(color);
                this.mColorDrawable.setState(state);
                return true;
            } else {
                return this.mColorDrawable.setState(state);
            }
        } else {
            return false;
        }
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        if (who == this.mColorDrawable && this.getCallback() != null) {
            this.getCallback().invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        if (who == this.mColorDrawable && this.getCallback() != null) {
            this.getCallback().scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        if (who == this.mColorDrawable && this.getCallback() != null) {
            this.getCallback().unscheduleDrawable(this, what);
        }
    }

    @NonNull
    @Override
    public Drawable.ConstantState getConstantState() {
        return this.mState;
    }

    @NonNull
    public ColorStateList getColorStateList() {
        return (ColorStateList) Objects.requireNonNullElseGet(this.mState.mColor, () -> ColorStateList.valueOf(this.mColorDrawable.getColor()));
    }

    @NonNull
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = new ColorStateListDrawable.ColorStateListDrawableState(this.mState);
            this.mMutated = true;
        }
        return this;
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    public void setColorStateList(@NonNull ColorStateList colorStateList) {
        this.mState.mColor = colorStateList;
        this.onStateChange(this.getState());
    }

    private void initializeColorDrawable() {
        this.mColorDrawable = new ColorDrawable();
        this.mColorDrawable.setCallback(this);
        if (this.mState.mTint != null) {
            this.mColorDrawable.setTintList(this.mState.mTint);
        }
        if (this.mState.mBlendMode != DEFAULT_BLEND_MODE) {
            this.mColorDrawable.setTintBlendMode(this.mState.mBlendMode);
        }
    }

    static final class ColorStateListDrawableState extends Drawable.ConstantState {

        ColorStateList mColor = null;

        ColorStateList mTint = null;

        int mAlpha = -1;

        BlendMode mBlendMode = Drawable.DEFAULT_BLEND_MODE;

        ColorStateListDrawableState() {
        }

        ColorStateListDrawableState(ColorStateListDrawable.ColorStateListDrawableState state) {
            this.mColor = state.mColor;
            this.mTint = state.mTint;
            this.mAlpha = state.mAlpha;
            this.mBlendMode = state.mBlendMode;
        }

        @NonNull
        @Override
        public Drawable newDrawable() {
            return new ColorStateListDrawable(this);
        }

        public boolean isStateful() {
            return this.mColor != null && this.mColor.isStateful() || this.mTint != null && this.mTint.isStateful();
        }

        public boolean hasFocusStateSpecified() {
            return this.mColor != null && this.mColor.hasFocusStateSpecified() || this.mTint != null && this.mTint.hasFocusStateSpecified();
        }
    }
}