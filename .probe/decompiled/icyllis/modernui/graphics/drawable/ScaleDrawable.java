package icyllis.modernui.graphics.drawable;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.resources.Resources;
import icyllis.modernui.view.Gravity;

public class ScaleDrawable extends DrawableWrapper {

    private final Rect mTmpRect = new Rect();

    private ScaleDrawable.ScaleState mState;

    ScaleDrawable() {
        this(new ScaleDrawable.ScaleState(null, null), null);
    }

    public ScaleDrawable(Drawable drawable, int gravity, float scaleWidth, float scaleHeight) {
        this(new ScaleDrawable.ScaleState(null, null), null);
        this.mState.mGravity = gravity;
        this.mState.mScaleWidth = scaleWidth;
        this.mState.mScaleHeight = scaleHeight;
        this.setDrawable(drawable);
    }

    public void setGravity(int gravity) {
        this.mState.mGravity = gravity;
        this.invalidateSelf();
    }

    public void setScaleWidth(float scaleWidth) {
        this.mState.mScaleWidth = scaleWidth;
        this.invalidateSelf();
    }

    public void setScaleHeight(float scaleHeight) {
        this.mState.mScaleHeight = scaleHeight;
        this.invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Drawable d = this.getDrawable();
        if (d != null && d.getLevel() != 0) {
            d.draw(canvas);
        }
    }

    @Override
    protected boolean onLevelChange(int level) {
        super.onLevelChange(level);
        this.onBoundsChange(this.getBounds());
        this.invalidateSelf();
        return true;
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        Drawable d = this.getDrawable();
        if (d != null) {
            Rect r = this.mTmpRect;
            boolean min = this.mState.mUseIntrinsicSizeAsMin;
            int level = this.getLevel();
            int w = bounds.width();
            if (this.mState.mScaleWidth > 0.0F) {
                int iw = min ? d.getIntrinsicWidth() : 0;
                w -= (int) ((float) ((w - iw) * (10000 - level)) * this.mState.mScaleWidth / 10000.0F);
            }
            int h = bounds.height();
            if (this.mState.mScaleHeight > 0.0F) {
                int ih = min ? d.getIntrinsicHeight() : 0;
                h -= (int) ((float) ((h - ih) * (10000 - level)) * this.mState.mScaleHeight / 10000.0F);
            }
            int layoutDirection = this.getLayoutDirection();
            Gravity.apply(this.mState.mGravity, w, h, bounds, r, layoutDirection);
            if (w > 0 && h > 0) {
                d.setBounds(r.left, r.top, r.right, r.bottom);
            }
        }
    }

    @Override
    DrawableWrapper.DrawableWrapperState mutateConstantState() {
        this.mState = new ScaleDrawable.ScaleState(this.mState, null);
        return this.mState;
    }

    private ScaleDrawable(ScaleDrawable.ScaleState state, Resources res) {
        super(state, res);
        this.mState = state;
        this.updateLocalState();
    }

    private void updateLocalState() {
        this.setLevel(this.mState.mInitialLevel);
    }

    static final class ScaleState extends DrawableWrapper.DrawableWrapperState {

        private static final float DO_NOT_SCALE = -1.0F;

        float mScaleWidth = -1.0F;

        float mScaleHeight = -1.0F;

        int mGravity = 3;

        boolean mUseIntrinsicSizeAsMin = false;

        int mInitialLevel = 0;

        ScaleState(ScaleDrawable.ScaleState orig, Resources res) {
            super(orig, res);
            if (orig != null) {
                this.mScaleWidth = orig.mScaleWidth;
                this.mScaleHeight = orig.mScaleHeight;
                this.mGravity = orig.mGravity;
                this.mUseIntrinsicSizeAsMin = orig.mUseIntrinsicSizeAsMin;
                this.mInitialLevel = orig.mInitialLevel;
            }
        }

        @NonNull
        @Override
        public Drawable newDrawable(Resources res) {
            return new ScaleDrawable(this, res);
        }
    }
}