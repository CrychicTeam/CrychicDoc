package icyllis.modernui.graphics.drawable;

import icyllis.arc3d.core.ColorFilter;
import icyllis.arc3d.core.effects.BlendModeColorFilter;
import icyllis.modernui.annotation.ColorInt;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Color;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.util.ColorStateList;

public class ColorDrawable extends Drawable {

    private final Paint mPaint = new Paint();

    private ColorDrawable.ColorState mColorState;

    private BlendModeColorFilter mBlendModeColorFilter;

    private boolean mMutated;

    public ColorDrawable() {
        this.mColorState = new ColorDrawable.ColorState();
    }

    public ColorDrawable(@ColorInt int color) {
        this.mColorState = new ColorDrawable.ColorState();
        this.setColor(color);
    }

    private ColorDrawable(@NonNull ColorDrawable.ColorState state) {
        this.mColorState = state;
        this.updateLocalState();
    }

    private void updateLocalState() {
        this.mBlendModeColorFilter = this.updateBlendModeFilter(this.mBlendModeColorFilter, this.mColorState.mTint, this.mColorState.mBlendMode);
    }

    @NonNull
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mColorState = new ColorDrawable.ColorState(this.mColorState);
            this.mMutated = true;
        }
        return this;
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int color = this.mColorState.mUseColor;
        ColorFilter colorFilter = this.mPaint.getColorFilter();
        if (Color.alpha(color) != 0 || colorFilter != null || this.mBlendModeColorFilter != null) {
            if (colorFilter == null) {
                this.mPaint.setColorFilter(this.mBlendModeColorFilter);
            }
            this.mPaint.setColor(color);
            canvas.drawRect(this.getBounds(), this.mPaint);
            this.mPaint.setColorFilter(colorFilter);
        }
    }

    @ColorInt
    public int getColor() {
        return this.mColorState.mUseColor;
    }

    public void setColor(@ColorInt int color) {
        if (this.mColorState.mBaseColor != color || this.mColorState.mUseColor != color) {
            this.mColorState.mBaseColor = this.mColorState.mUseColor = color;
            this.invalidateSelf();
        }
    }

    @Override
    public int getAlpha() {
        return this.mColorState.mUseColor >>> 24;
    }

    @Override
    public void setAlpha(int alpha) {
        alpha += alpha >> 7;
        int baseAlpha = this.mColorState.mBaseColor >>> 24;
        int useAlpha = baseAlpha * alpha >> 8;
        int useColor = this.mColorState.mBaseColor << 8 >>> 8 | useAlpha << 24;
        if (this.mColorState.mUseColor != useColor) {
            this.mColorState.mUseColor = useColor;
            this.invalidateSelf();
        }
    }

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        this.mColorState.mTint = tint;
        this.mBlendModeColorFilter = this.updateBlendModeFilter(this.mBlendModeColorFilter, tint, this.mColorState.mBlendMode);
        this.invalidateSelf();
    }

    @Override
    public void setTintBlendMode(@NonNull BlendMode blendMode) {
        this.mColorState.mBlendMode = blendMode;
        this.mBlendModeColorFilter = this.updateBlendModeFilter(this.mBlendModeColorFilter, this.mColorState.mTint, blendMode);
        this.invalidateSelf();
    }

    @Override
    protected boolean onStateChange(@NonNull int[] stateSet) {
        ColorDrawable.ColorState state = this.mColorState;
        if (state.mTint != null && state.mBlendMode != null) {
            this.mBlendModeColorFilter = this.updateBlendModeFilter(this.mBlendModeColorFilter, state.mTint, state.mBlendMode);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isStateful() {
        return this.mColorState.mTint != null && this.mColorState.mTint.isStateful();
    }

    @Override
    public boolean hasFocusStateSpecified() {
        return this.mColorState.mTint != null && this.mColorState.mTint.hasFocusStateSpecified();
    }

    @Override
    public Drawable.ConstantState getConstantState() {
        return this.mColorState;
    }

    static final class ColorState extends Drawable.ConstantState {

        int mBaseColor;

        int mUseColor;

        ColorStateList mTint = null;

        BlendMode mBlendMode = Drawable.DEFAULT_BLEND_MODE;

        ColorState() {
        }

        ColorState(@NonNull ColorDrawable.ColorState state) {
            this.mBaseColor = state.mBaseColor;
            this.mUseColor = state.mUseColor;
            this.mTint = state.mTint;
            this.mBlendMode = state.mBlendMode;
        }

        @NonNull
        @Override
        public Drawable newDrawable() {
            return new ColorDrawable(this);
        }
    }
}