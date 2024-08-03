package icyllis.modernui.view.menu;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import icyllis.modernui.material.MaterialDrawable;
import icyllis.modernui.util.TypedValue;
import java.nio.FloatBuffer;

public class SubMenuArrowDrawable extends MaterialDrawable {

    private final int mSize;

    private final FloatBuffer mPoints;

    public SubMenuArrowDrawable(Context context) {
        this.mSize = (int) TypedValue.applyDimension(1, 24.0F, context.getResources().getDisplayMetrics());
        this.mPoints = FloatBuffer.allocate(6);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Paint paint = Paint.obtain();
        paint.setColor(this.mColor);
        paint.setAlpha(ShapeDrawable.modulateAlpha(paint.getAlpha(), this.mAlpha));
        if (paint.getAlpha() != 0) {
            canvas.drawTriangleListMesh(this.mPoints, null, paint);
        }
        paint.recycle();
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        this.buildArrowPoints((float) bounds.width(), (float) bounds.height(), this.getLayoutDirection());
    }

    @Override
    protected boolean onLayoutDirectionChanged(int layoutDirection) {
        Rect bounds = this.getBounds();
        this.buildArrowPoints((float) bounds.width(), (float) bounds.height(), layoutDirection);
        return true;
    }

    private void buildArrowPoints(float w, float h, int layoutDirection) {
        boolean mirror = layoutDirection == 1;
        if (mirror) {
            this.mPoints.put(0.6666667F * w).put(0.29166666F * h).put(0.33333334F * w).put(0.5F * h).put(0.6666667F * w).put(0.7083333F * h).flip();
        } else {
            this.mPoints.put(0.33333334F * w).put(0.7083333F * h).put(0.6666667F * w).put(0.5F * h).put(0.33333334F * w).put(0.29166666F * h).flip();
        }
    }

    @Override
    public boolean isAutoMirrored() {
        return true;
    }

    @Override
    public int getIntrinsicWidth() {
        return this.mSize;
    }

    @Override
    public int getIntrinsicHeight() {
        return this.mSize;
    }
}