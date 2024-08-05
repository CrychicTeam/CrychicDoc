package icyllis.modernui.graphics.drawable;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.resources.Resources;
import icyllis.modernui.util.ColorStateList;

public abstract class DrawableWrapper extends Drawable implements Drawable.Callback {

    private DrawableWrapper.DrawableWrapperState mState;

    private Drawable mDrawable;

    private boolean mMutated;

    DrawableWrapper(DrawableWrapper.DrawableWrapperState state, Resources res) {
        this.mState = state;
        this.updateLocalState(res);
    }

    public DrawableWrapper(@Nullable Drawable dr) {
        this.mState = null;
        this.setDrawable(dr);
    }

    private void updateLocalState(Resources res) {
        if (this.mState != null && this.mState.mDrawableState != null) {
            Drawable dr = this.mState.mDrawableState.newDrawable(res);
            this.setDrawable(dr);
        }
    }

    public void setDrawable(@Nullable Drawable dr) {
        if (this.mDrawable != null) {
            this.mDrawable.setCallback(null);
        }
        this.mDrawable = dr;
        if (dr != null) {
            dr.setCallback(this);
            dr.setVisible(this.isVisible(), true);
            dr.setState(this.getState());
            dr.setLevel(this.getLevel());
            dr.setBounds(this.getBounds());
            dr.setLayoutDirection(this.getLayoutDirection());
            if (this.mState != null) {
                this.mState.mDrawableState = dr.getConstantState();
            }
        }
        this.invalidateSelf();
    }

    @Nullable
    public Drawable getDrawable() {
        return this.mDrawable;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        Drawable.Callback callback = this.getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        Drawable.Callback callback = this.getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        Drawable.Callback callback = this.getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (this.mDrawable != null) {
            this.mDrawable.draw(canvas);
        }
    }

    @Override
    public boolean getPadding(@NonNull Rect padding) {
        return this.mDrawable != null && this.mDrawable.getPadding(padding);
    }

    @Override
    public void setHotspot(float x, float y) {
        if (this.mDrawable != null) {
            this.mDrawable.setHotspot(x, y);
        }
    }

    @Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        if (this.mDrawable != null) {
            this.mDrawable.setHotspotBounds(left, top, right, bottom);
        }
    }

    @Override
    public void getHotspotBounds(@NonNull Rect outRect) {
        if (this.mDrawable != null) {
            this.mDrawable.getHotspotBounds(outRect);
        } else {
            outRect.set(this.getBounds());
        }
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean superChanged = super.setVisible(visible, restart);
        boolean changed = this.mDrawable != null && this.mDrawable.setVisible(visible, restart);
        return superChanged | changed;
    }

    @Override
    public void setAlpha(int alpha) {
        if (this.mDrawable != null) {
            this.mDrawable.setAlpha(alpha);
        }
    }

    @Override
    public int getAlpha() {
        return this.mDrawable != null ? this.mDrawable.getAlpha() : 255;
    }

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        if (this.mDrawable != null) {
            this.mDrawable.setTintList(tint);
        }
    }

    @Override
    public void setTintBlendMode(@NonNull BlendMode blendMode) {
        if (this.mDrawable != null) {
            this.mDrawable.setTintBlendMode(blendMode);
        }
    }

    @Override
    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return this.mDrawable != null && this.mDrawable.setLayoutDirection(layoutDirection);
    }

    @Override
    public boolean isStateful() {
        return this.mDrawable != null && this.mDrawable.isStateful();
    }

    @Override
    public boolean hasFocusStateSpecified() {
        return this.mDrawable != null && this.mDrawable.hasFocusStateSpecified();
    }

    @Override
    protected boolean onStateChange(@NonNull int[] state) {
        if (this.mDrawable != null && this.mDrawable.isStateful()) {
            boolean changed = this.mDrawable.setState(state);
            if (changed) {
                this.onBoundsChange(this.getBounds());
            }
            return changed;
        } else {
            return false;
        }
    }

    @Override
    public void jumpToCurrentState() {
        if (this.mDrawable != null) {
            this.mDrawable.jumpToCurrentState();
        }
    }

    @Override
    protected boolean onLevelChange(int level) {
        return this.mDrawable != null && this.mDrawable.setLevel(level);
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        if (this.mDrawable != null) {
            this.mDrawable.setBounds(bounds);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return this.mDrawable != null ? this.mDrawable.getIntrinsicWidth() : -1;
    }

    @Override
    public int getIntrinsicHeight() {
        return this.mDrawable != null ? this.mDrawable.getIntrinsicHeight() : -1;
    }

    @Nullable
    @Override
    public Drawable.ConstantState getConstantState() {
        return this.mState != null && this.mState.canConstantState() ? this.mState : null;
    }

    @NonNull
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = this.mutateConstantState();
            if (this.mDrawable != null) {
                this.mDrawable.mutate();
            }
            if (this.mState != null) {
                this.mState.mDrawableState = this.mDrawable != null ? this.mDrawable.getConstantState() : null;
            }
            this.mMutated = true;
        }
        return this;
    }

    DrawableWrapper.DrawableWrapperState mutateConstantState() {
        return this.mState;
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        if (this.mDrawable != null) {
            this.mDrawable.clearMutated();
        }
        this.mMutated = false;
    }

    abstract static class DrawableWrapperState extends Drawable.ConstantState {

        private int[] mThemeAttrs;

        int mChangingConfigurations;

        int mDensity = 72;

        int mSrcDensityOverride = 0;

        Drawable.ConstantState mDrawableState;

        DrawableWrapperState(@Nullable DrawableWrapper.DrawableWrapperState orig, @Nullable Resources res) {
            if (orig != null) {
                this.mThemeAttrs = orig.mThemeAttrs;
                this.mChangingConfigurations = orig.mChangingConfigurations;
                this.mDrawableState = orig.mDrawableState;
                this.mSrcDensityOverride = orig.mSrcDensityOverride;
            }
            int density;
            if (res != null) {
                density = res.getDisplayMetrics().densityDpi;
            } else if (orig != null) {
                density = orig.mDensity;
            } else {
                density = 0;
            }
            this.mDensity = density == 0 ? 72 : density;
        }

        public final void setDensity(int targetDensity) {
            if (this.mDensity != targetDensity) {
                int sourceDensity = this.mDensity;
                this.mDensity = targetDensity;
                this.onDensityChanged(sourceDensity, targetDensity);
            }
        }

        void onDensityChanged(int sourceDensity, int targetDensity) {
        }

        @NonNull
        @Override
        public Drawable newDrawable() {
            return this.newDrawable(null);
        }

        @NonNull
        @Override
        public abstract Drawable newDrawable(@Nullable Resources var1);

        public boolean canConstantState() {
            return this.mDrawableState != null;
        }
    }
}