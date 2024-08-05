package icyllis.modernui.widget;

import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.RectF;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.view.View;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class LinearPagerIndicator extends View {

    private float mLineWidth = 6.0F;

    private ViewPager mPager;

    private PagerAdapter mAdapter;

    private int mPageCount;

    private final RectF mLineRect = new RectF();

    private final LinearPagerIndicator.PageListener mPageListener = new LinearPagerIndicator.PageListener();

    private int mLineColor = -257237776;

    public LinearPagerIndicator(Context context) {
        super(context);
    }

    public void setLineWidth(float lineWidth) {
        this.mLineWidth = lineWidth;
    }

    public float getLineWidth() {
        return this.mLineWidth;
    }

    public void setLineColor(int lineColor) {
        this.mLineColor = lineColor;
    }

    public int getLineColor() {
        return this.mLineColor;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        Paint paint = Paint.obtain();
        paint.setColor(this.mLineColor);
        canvas.drawRoundRect(this.mLineRect, this.mLineWidth / 2.0F, paint);
        paint.recycle();
    }

    public void setPager(@Nullable ViewPager pager) {
        if (pager != null) {
            this.updateAdapter(this.mAdapter, pager.getAdapter());
            pager.setInternalPageChangeListener(this.mPageListener);
            pager.addOnAdapterChangeListener(this.mPageListener);
            this.mPager = pager;
        } else if (this.mPager != null) {
            this.updateAdapter(this.mAdapter, null);
            this.mPager.setInternalPageChangeListener(null);
            this.mPager.removeOnAdapterChangeListener(this.mPageListener);
            this.mPager = null;
        }
    }

    private void updateAdapter(@Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(this.mPageListener);
            this.mAdapter = null;
            this.mPageCount = 0;
        }
        if (newAdapter != null) {
            newAdapter.registerDataSetObserver(this.mPageListener);
            this.mAdapter = newAdapter;
            this.mPageCount = newAdapter.getCount();
        }
        this.invalidate();
    }

    private class PageListener implements DataSetObserver, ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener {

        @Override
        public void onChanged() {
            if (LinearPagerIndicator.this.mAdapter != null) {
                LinearPagerIndicator.this.mPageCount = LinearPagerIndicator.this.mAdapter.getCount();
            } else {
                LinearPagerIndicator.this.mPageCount = 0;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (LinearPagerIndicator.this.mPageCount != 0) {
                float segLength = ((float) LinearPagerIndicator.this.getWidth() - LinearPagerIndicator.this.mLineWidth) / (float) LinearPagerIndicator.this.mPageCount;
                float left = (float) position * segLength;
                float right = left + segLength + LinearPagerIndicator.this.mLineWidth;
                float v = ((float) LinearPagerIndicator.this.getHeight() - LinearPagerIndicator.this.mLineWidth) * 0.5F;
                LinearPagerIndicator.this.mLineRect.set(left + segLength * TimeInterpolator.DECELERATE.getInterpolation(positionOffset), v, right + segLength * TimeInterpolator.ACCELERATE.getInterpolation(positionOffset), v + LinearPagerIndicator.this.mLineWidth);
                LinearPagerIndicator.this.invalidate();
            }
        }

        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
            LinearPagerIndicator.this.updateAdapter(oldAdapter, newAdapter);
        }
    }
}