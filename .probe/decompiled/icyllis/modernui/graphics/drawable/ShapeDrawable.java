package icyllis.modernui.graphics.drawable;

import icyllis.arc3d.core.ColorFilter;
import icyllis.arc3d.core.effects.BlendModeColorFilter;
import icyllis.modernui.annotation.ColorInt;
import icyllis.modernui.annotation.FloatRange;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.RectF;
import icyllis.modernui.resources.Resources;
import icyllis.modernui.util.ColorStateList;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ShapeDrawable extends Drawable {

    public static final int RECTANGLE = 0;

    public static final int CIRCLE = 1;

    public static final int RING = 2;

    public static final int HLINE = 3;

    public static final int VLINE = 4;

    private static final float DEFAULT_INNER_RADIUS_RATIO = 3.0F;

    private static final float DEFAULT_THICKNESS_RATIO = 9.0F;

    private ShapeDrawable.ShapeState mShapeState;

    private Rect mPadding;

    private final Paint mFillPaint = new Paint();

    private Paint mStrokePaint;

    private BlendModeColorFilter mBlendModeColorFilter;

    private boolean mShapeIsDirty;

    private final RectF mRect = new RectF();

    private int mAlpha = 255;

    private boolean mMutated;

    public static int modulateAlpha(int srcAlpha, int dstAlpha) {
        int multiplier = dstAlpha + (dstAlpha >> 7);
        return srcAlpha * multiplier >> 8;
    }

    public ShapeDrawable() {
        this(new ShapeDrawable.ShapeState(), null);
    }

    ShapeDrawable(@NonNull ShapeDrawable.ShapeState state, @Nullable Resources res) {
        this.mShapeState = state;
        this.updateLocalState(res);
    }

    private void updateLocalState(@Nullable Resources res) {
        ShapeDrawable.ShapeState state = this.mShapeState;
        if (state.mSolidColors != null) {
            int[] currentState = this.getState();
            int stateColor = state.mSolidColors.getColorForState(currentState, 0);
            this.mFillPaint.setColor(stateColor);
        } else {
            this.mFillPaint.setColor(0);
        }
        this.mPadding = state.mPadding;
        if (state.mStrokeWidth >= 0) {
            this.mStrokePaint = new Paint();
            this.mStrokePaint.setStyle(1);
            this.mStrokePaint.setStrokeWidth((float) state.mStrokeWidth);
            if (state.mStrokeColors != null) {
                int[] currentState = this.getState();
                int strokeStateColor = state.mStrokeColors.getColorForState(currentState, 0);
                this.mStrokePaint.setColor(strokeStateColor);
            }
        }
        this.mBlendModeColorFilter = this.updateBlendModeFilter(this.mBlendModeColorFilter, state.mTint, state.mBlendMode);
        this.mShapeIsDirty = true;
    }

    boolean updateRectIsEmpty() {
        if (this.mShapeIsDirty) {
            this.mShapeIsDirty = false;
            this.mRect.set(this.getBounds());
            if (this.mStrokePaint != null) {
                float inset = this.mStrokePaint.getStrokeWidth() * 0.5F;
                this.mRect.inset(inset, inset);
            }
        }
        return this.mShapeState.mStrokeWidth > 0 ? false : this.mRect.isEmpty();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (!this.updateRectIsEmpty()) {
            int prevFillAlpha = this.mFillPaint.getAlpha();
            int prevStrokeAlpha = this.mStrokePaint != null ? this.mStrokePaint.getAlpha() : 0;
            int currFillAlpha = modulateAlpha(prevFillAlpha, this.mAlpha);
            int currStrokeAlpha = modulateAlpha(prevStrokeAlpha, this.mAlpha);
            boolean haveFill = currFillAlpha > 0;
            boolean haveStroke = currStrokeAlpha > 0 && this.mStrokePaint != null && this.mStrokePaint.getStrokeWidth() > 0.0F;
            ShapeDrawable.ShapeState st = this.mShapeState;
            ColorFilter colorFilter = this.mBlendModeColorFilter;
            this.mFillPaint.setAlpha(currFillAlpha);
            this.mFillPaint.setColorFilter(colorFilter);
            if (colorFilter != null && st.mSolidColors == null) {
                this.mFillPaint.setColor(this.mAlpha << 24);
            }
            if (haveStroke) {
                this.mStrokePaint.setAlpha(currStrokeAlpha);
                this.mStrokePaint.setColorFilter(colorFilter);
            }
            RectF r = this.mRect;
            switch(st.mShape) {
                case 0:
                    if (st.mRadius > 0.0F) {
                        float rad = Math.min(st.mRadius, Math.min(r.width(), r.height()) * 0.5F);
                        canvas.drawRoundRect(r, rad, this.mFillPaint);
                        if (haveStroke) {
                            this.mStrokePaint.setStrokeCap(4);
                            canvas.drawRoundRect(r, rad, this.mStrokePaint);
                        }
                    } else {
                        if (this.mFillPaint.getColor() != 0 || colorFilter != null || this.mFillPaint.getShader() != null) {
                            canvas.drawRect(r, this.mFillPaint);
                        }
                        if (haveStroke) {
                            this.mStrokePaint.setStrokeCap(8);
                            canvas.drawRect(r, this.mStrokePaint);
                        }
                    }
                    break;
                case 1:
                    {
                        float cx = r.centerX();
                        float cy = r.centerY();
                        float radius = Math.min(r.width(), r.height()) * 0.5F;
                        if (st.mUseLevelForShape) {
                            float sweepx = 360.0F * (float) this.getLevel() / 10000.0F;
                            canvas.drawPie(cx, cy, radius, -90.0F, sweepx, this.mFillPaint);
                            if (haveStroke) {
                                this.mStrokePaint.setStrokeCap(0);
                                canvas.drawPie(cx, cy, radius, -90.0F, sweepx, this.mStrokePaint);
                            }
                        } else {
                            canvas.drawCircle(cx, cy, radius, this.mFillPaint);
                            if (haveStroke) {
                                this.mStrokePaint.setStrokeCap(0);
                                canvas.drawCircle(cx, cy, radius, this.mStrokePaint);
                            }
                        }
                        break;
                    }
                case 2:
                    {
                        float cx = r.centerX();
                        float cy = r.centerY();
                        float thickness = st.mThickness != -1 ? (float) st.mThickness : r.width() / st.mThicknessRatio;
                        float radius = st.mInnerRadius != -1 ? (float) st.mInnerRadius : r.width() / st.mInnerRadiusRatio;
                        radius += thickness * 0.5F;
                        Paint paint = Paint.obtain();
                        paint.set(this.mFillPaint);
                        paint.setStrokeWidth(thickness);
                        float sweep = st.mUseLevelForShape ? 360.0F * (float) this.getLevel() / 10000.0F : 360.0F;
                        canvas.drawArc(cx, cy, radius, -90.0F, sweep, paint);
                        paint.recycle();
                        break;
                    }
                case 3:
                    float y = r.centerY();
                    if (haveStroke) {
                        this.mStrokePaint.setStrokeCap(st.mRadius > 0.0F ? 4 : 8);
                        canvas.drawLine(r.left, y, r.right, y, this.mStrokePaint);
                    } else {
                        this.mFillPaint.setStrokeCap(st.mRadius > 0.0F ? 4 : 8);
                        canvas.drawLine(r.left, y, r.right, y, r.height(), this.mFillPaint);
                    }
                    break;
                case 4:
                    float x = r.centerX();
                    if (haveStroke) {
                        this.mStrokePaint.setStrokeCap(st.mRadius > 0.0F ? 4 : 8);
                        canvas.drawLine(x, r.top, x, r.bottom, this.mStrokePaint);
                    } else {
                        this.mFillPaint.setStrokeCap(st.mRadius > 0.0F ? 4 : 8);
                        canvas.drawLine(x, r.top, x, r.bottom, r.width(), this.mFillPaint);
                    }
            }
            this.mFillPaint.setAlpha(prevFillAlpha);
            if (haveStroke) {
                this.mStrokePaint.setAlpha(prevStrokeAlpha);
            }
        }
    }

    public void setShape(int shape) {
        this.mShapeState.setShape(shape);
        this.invalidateSelf();
    }

    public int getShape() {
        return this.mShapeState.mShape;
    }

    public void setUseLevelForShape(boolean useLevelForShape) {
        this.mShapeState.mUseLevelForShape = useLevelForShape;
        this.invalidateSelf();
    }

    public boolean getUseLevelForShape() {
        return this.mShapeState.mUseLevelForShape;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (this.mShapeState.mPadding == null) {
            this.mShapeState.mPadding = new Rect();
        }
        this.mShapeState.mPadding.set(left, top, right, bottom);
        this.mPadding = this.mShapeState.mPadding;
        this.invalidateSelf();
    }

    @Override
    public boolean getPadding(@NonNull Rect padding) {
        if (this.mPadding != null) {
            padding.set(this.mPadding);
            return true;
        } else {
            return super.getPadding(padding);
        }
    }

    public void setSize(int width, int height) {
        this.mShapeState.setSize(width, height);
        this.invalidateSelf();
    }

    public void setCornerRadius(float radius) {
        this.mShapeState.setCornerRadius(radius);
        this.invalidateSelf();
    }

    public float getCornerRadius() {
        return this.mShapeState.mRadius;
    }

    public void setColor(@ColorInt int color) {
        this.mShapeState.setSolidColors(ColorStateList.valueOf(color));
        this.mFillPaint.setColor(color);
        this.invalidateSelf();
    }

    public void setColor(@Nullable ColorStateList colorStateList) {
        if (colorStateList == null) {
            this.setColor(0);
        } else {
            int[] stateSet = this.getState();
            int color = colorStateList.getColorForState(stateSet, 0);
            this.mShapeState.setSolidColors(colorStateList);
            this.mFillPaint.setColor(color);
            this.invalidateSelf();
        }
    }

    @Nullable
    public ColorStateList getColor() {
        return this.mShapeState.mSolidColors;
    }

    public void setStroke(int width, @ColorInt int color) {
        this.mShapeState.setStroke(width, ColorStateList.valueOf(color));
        this.setStrokeInternal(width, color);
    }

    public void setStroke(int width, @Nullable ColorStateList colorStateList) {
        this.mShapeState.setStroke(width, colorStateList);
        int color;
        if (colorStateList == null) {
            color = 0;
        } else {
            int[] stateSet = this.getState();
            color = colorStateList.getColorForState(stateSet, 0);
        }
        this.setStrokeInternal(width, color);
    }

    private void setStrokeInternal(int width, int color) {
        if (this.mStrokePaint == null) {
            this.mStrokePaint = new Paint();
            this.mStrokePaint.setStyle(1);
        }
        this.mStrokePaint.setStrokeWidth((float) width);
        this.mStrokePaint.setColor(color);
        this.mShapeIsDirty = true;
        this.invalidateSelf();
    }

    public void setInnerRadiusRatio(@FloatRange(from = 0.0, fromInclusive = false) float innerRadiusRatio) {
        if (innerRadiusRatio <= 0.0F) {
            throw new IllegalArgumentException("Ratio must be greater than zero");
        } else {
            this.mShapeState.mInnerRadiusRatio = innerRadiusRatio;
            this.invalidateSelf();
        }
    }

    public float getInnerRadiusRatio() {
        return this.mShapeState.mInnerRadiusRatio;
    }

    public void setInnerRadius(int innerRadius) {
        this.mShapeState.mInnerRadius = innerRadius;
        this.invalidateSelf();
    }

    public int getInnerRadius() {
        return this.mShapeState.mInnerRadius;
    }

    public void setThicknessRatio(@FloatRange(from = 0.0, fromInclusive = false) float thicknessRatio) {
        if (thicknessRatio <= 0.0F) {
            throw new IllegalArgumentException("Ratio must be greater than zero");
        } else {
            this.mShapeState.mThicknessRatio = thicknessRatio;
            this.invalidateSelf();
        }
    }

    public float getThicknessRatio() {
        return this.mShapeState.mThicknessRatio;
    }

    public void setThickness(int thickness) {
        this.mShapeState.mThickness = thickness;
        this.invalidateSelf();
    }

    public int getThickness() {
        return this.mShapeState.mThickness;
    }

    @Override
    protected boolean onStateChange(@NonNull int[] stateSet) {
        boolean invalidateSelf = false;
        ShapeDrawable.ShapeState s = this.mShapeState;
        ColorStateList solidColors = s.mSolidColors;
        if (solidColors != null) {
            int newColor = solidColors.getColorForState(stateSet, 0);
            int oldColor = this.mFillPaint.getColor();
            if (oldColor != newColor) {
                this.mFillPaint.setColor(newColor);
                invalidateSelf = true;
            }
        }
        Paint strokePaint = this.mStrokePaint;
        if (strokePaint != null) {
            ColorStateList strokeColors = s.mStrokeColors;
            if (strokeColors != null) {
                int newColor = strokeColors.getColorForState(stateSet, 0);
                int oldColor = strokePaint.getColor();
                if (oldColor != newColor) {
                    strokePaint.setColor(newColor);
                    invalidateSelf = true;
                }
            }
        }
        if (s.mTint != null && s.mBlendMode != null) {
            this.mBlendModeColorFilter = this.updateBlendModeFilter(this.mBlendModeColorFilter, s.mTint, s.mBlendMode);
            invalidateSelf = true;
        }
        if (invalidateSelf) {
            this.invalidateSelf();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isStateful() {
        ShapeDrawable.ShapeState s = this.mShapeState;
        return super.isStateful() || s.mSolidColors != null && s.mSolidColors.isStateful() || s.mStrokeColors != null && s.mStrokeColors.isStateful();
    }

    @Override
    public boolean hasFocusStateSpecified() {
        ShapeDrawable.ShapeState s = this.mShapeState;
        return s.mSolidColors != null && s.mSolidColors.hasFocusStateSpecified() || s.mStrokeColors != null && s.mStrokeColors.hasFocusStateSpecified();
    }

    @Override
    public void setAlpha(int alpha) {
        if (alpha != this.mAlpha) {
            this.mAlpha = alpha;
            this.invalidateSelf();
        }
    }

    @Override
    public int getAlpha() {
        return this.mAlpha;
    }

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        this.mShapeState.mTint = tint;
        this.mBlendModeColorFilter = this.updateBlendModeFilter(this.mBlendModeColorFilter, tint, this.mShapeState.mBlendMode);
        this.invalidateSelf();
    }

    @Override
    public void setTintBlendMode(@NonNull BlendMode blendMode) {
        this.mShapeState.mBlendMode = blendMode;
        this.mBlendModeColorFilter = this.updateBlendModeFilter(this.mBlendModeColorFilter, this.mShapeState.mTint, blendMode);
        this.invalidateSelf();
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);
        this.mShapeIsDirty = true;
    }

    @Override
    protected boolean onLevelChange(int level) {
        super.onLevelChange(level);
        this.mShapeIsDirty = true;
        this.invalidateSelf();
        return true;
    }

    @Override
    public int getIntrinsicWidth() {
        return this.mShapeState.mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return this.mShapeState.mHeight;
    }

    @Override
    public Drawable.ConstantState getConstantState() {
        return this.mShapeState;
    }

    @NonNull
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mShapeState = new ShapeDrawable.ShapeState(this.mShapeState, null);
            this.updateLocalState(null);
            this.mMutated = true;
        }
        return this;
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Shape {
    }

    static class ShapeState extends Drawable.ConstantState {

        public int mShape = 0;

        public ColorStateList mSolidColors;

        public ColorStateList mStrokeColors;

        public int mStrokeWidth = -1;

        public float mRadius = 0.0F;

        public Rect mPadding = null;

        public int mWidth = -1;

        public int mHeight = -1;

        public float mInnerRadiusRatio = 3.0F;

        public float mThicknessRatio = 9.0F;

        public int mInnerRadius = -1;

        public int mThickness = -1;

        boolean mUseLevelForShape = true;

        ColorStateList mTint = null;

        BlendMode mBlendMode = Drawable.DEFAULT_BLEND_MODE;

        public ShapeState() {
        }

        public ShapeState(@NonNull ShapeDrawable.ShapeState orig, @Nullable Resources res) {
            this.mShape = orig.mShape;
            this.mSolidColors = orig.mSolidColors;
            this.mStrokeColors = orig.mStrokeColors;
            this.mStrokeWidth = orig.mStrokeWidth;
            this.mRadius = orig.mRadius;
            if (orig.mPadding != null) {
                this.mPadding = new Rect(orig.mPadding);
            }
            this.mWidth = orig.mWidth;
            this.mHeight = orig.mHeight;
            this.mInnerRadiusRatio = orig.mInnerRadiusRatio;
            this.mThicknessRatio = orig.mThicknessRatio;
            this.mInnerRadius = orig.mInnerRadius;
            this.mThickness = orig.mThickness;
            this.mUseLevelForShape = orig.mUseLevelForShape;
        }

        public void setShape(int shape) {
            this.mShape = shape;
        }

        public void setSize(int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
        }

        public void setSolidColors(@Nullable ColorStateList colors) {
            this.mSolidColors = colors;
        }

        public void setStroke(int width, @Nullable ColorStateList colors) {
            this.mStrokeWidth = width;
            this.mStrokeColors = colors;
        }

        public void setCornerRadius(float radius) {
            this.mRadius = Math.max(radius, 0.0F);
        }

        @NonNull
        @Override
        public Drawable newDrawable() {
            return new ShapeDrawable(this, null);
        }

        @NonNull
        @Override
        public Drawable newDrawable(@Nullable Resources res) {
            return new ShapeDrawable(this, res);
        }
    }
}