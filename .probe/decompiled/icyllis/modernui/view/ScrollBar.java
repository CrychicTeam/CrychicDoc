package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;

final class ScrollBar extends Drawable implements Drawable.Callback {

    private Drawable mVerticalTrack;

    private Drawable mVerticalThumb;

    private Drawable mHorizontalTrack;

    private Drawable mHorizontalThumb;

    private int mRange;

    private int mOffset;

    private int mExtent;

    private boolean mVertical;

    private boolean mBoundsChanged;

    private boolean mRangeChanged;

    private boolean mAlwaysDrawHorizontalTrack;

    private boolean mAlwaysDrawVerticalTrack;

    private int mAlpha = 255;

    private boolean mHasSetAlpha;

    public void setAlwaysDrawHorizontalTrack(boolean alwaysDrawTrack) {
        this.mAlwaysDrawHorizontalTrack = alwaysDrawTrack;
    }

    public void setAlwaysDrawVerticalTrack(boolean alwaysDrawTrack) {
        this.mAlwaysDrawVerticalTrack = alwaysDrawTrack;
    }

    public boolean getAlwaysDrawVerticalTrack() {
        return this.mAlwaysDrawVerticalTrack;
    }

    public boolean getAlwaysDrawHorizontalTrack() {
        return this.mAlwaysDrawHorizontalTrack;
    }

    public void setParameters(int range, int offset, int extent, boolean vertical) {
        if (this.mVertical != vertical) {
            this.mVertical = vertical;
            this.mBoundsChanged = true;
        }
        if (this.mRange != range || this.mOffset != offset || this.mExtent != extent) {
            this.mRange = range;
            this.mOffset = offset;
            this.mExtent = extent;
            this.mRangeChanged = true;
        }
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);
        this.mBoundsChanged = true;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect r = this.getBounds();
        if (!canvas.quickReject((float) r.left, (float) r.top, (float) r.right, (float) r.bottom)) {
            boolean vertical = this.mVertical;
            int extent = this.mExtent;
            int range = this.mRange;
            boolean drawTrack = true;
            boolean drawThumb = true;
            if (extent <= 0 || range <= extent) {
                drawTrack = vertical ? this.mAlwaysDrawVerticalTrack : this.mAlwaysDrawHorizontalTrack;
                drawThumb = false;
            }
            if (drawTrack) {
                this.drawTrack(canvas, r, vertical);
            }
            if (drawThumb) {
                int barLength = vertical ? r.height() : r.width();
                int thickness = vertical ? r.width() : r.height();
                int thumbLength = Math.max(Math.round((float) barLength * (float) extent / (float) range), thickness * 2);
                int thumbOffset = Math.min(Math.round((float) (barLength - thumbLength) * (float) this.mOffset / (float) (range - extent)), barLength - thumbLength);
                this.drawThumb(canvas, r, thumbOffset, thumbLength, vertical);
            }
        }
    }

    private void drawTrack(Canvas canvas, Rect bounds, boolean vertical) {
        Drawable track;
        if (vertical) {
            track = this.mVerticalTrack;
        } else {
            track = this.mHorizontalTrack;
        }
        if (track != null) {
            if (this.mBoundsChanged) {
                track.setBounds(bounds);
            }
            track.draw(canvas);
        }
    }

    private void drawThumb(Canvas canvas, Rect bounds, int offset, int length, boolean vertical) {
        boolean changed = this.mRangeChanged || this.mBoundsChanged;
        if (vertical) {
            if (this.mVerticalThumb != null) {
                Drawable thumb = this.mVerticalThumb;
                if (changed) {
                    thumb.setBounds(bounds.left, bounds.top + offset, bounds.right, bounds.top + offset + length);
                }
                thumb.draw(canvas);
            }
        } else if (this.mHorizontalThumb != null) {
            Drawable thumb = this.mHorizontalThumb;
            if (changed) {
                thumb.setBounds(bounds.left + offset, bounds.top, bounds.left + offset + length, bounds.bottom);
            }
            thumb.draw(canvas);
        }
    }

    public int getSize(boolean vertical) {
        if (vertical) {
            return this.mVerticalTrack != null ? this.mVerticalTrack.getIntrinsicWidth() : (this.mVerticalThumb != null ? this.mVerticalThumb.getIntrinsicWidth() : 0);
        } else {
            return this.mHorizontalTrack != null ? this.mHorizontalTrack.getIntrinsicHeight() : (this.mHorizontalThumb != null ? this.mHorizontalThumb.getIntrinsicHeight() : 0);
        }
    }

    @Nullable
    public Drawable getVerticalTrackDrawable() {
        return this.mVerticalTrack;
    }

    @Nullable
    public Drawable getVerticalThumbDrawable() {
        return this.mVerticalThumb;
    }

    @Nullable
    public Drawable getHorizontalTrackDrawable() {
        return this.mHorizontalTrack;
    }

    @Nullable
    public Drawable getHorizontalThumbDrawable() {
        return this.mHorizontalThumb;
    }

    public void setVerticalThumbDrawable(@Nullable Drawable thumb) {
        if (this.mVerticalThumb != null) {
            this.mVerticalThumb.setCallback(null);
        }
        this.propagateCurrentState(thumb);
        this.mVerticalThumb = thumb;
    }

    public void setVerticalTrackDrawable(@Nullable Drawable track) {
        if (this.mVerticalTrack != null) {
            this.mVerticalTrack.setCallback(null);
        }
        this.propagateCurrentState(track);
        this.mVerticalTrack = track;
    }

    public void setHorizontalThumbDrawable(@Nullable Drawable thumb) {
        if (this.mHorizontalThumb != null) {
            this.mHorizontalThumb.setCallback(null);
        }
        this.propagateCurrentState(thumb);
        this.mHorizontalThumb = thumb;
    }

    public void setHorizontalTrackDrawable(@Nullable Drawable track) {
        if (this.mHorizontalTrack != null) {
            this.mHorizontalTrack.setCallback(null);
        }
        this.propagateCurrentState(track);
        this.mHorizontalTrack = track;
    }

    private void propagateCurrentState(@Nullable Drawable d) {
        if (d != null) {
            d.setCallback(this);
            if (this.mHasSetAlpha) {
                d.setAlpha(this.mAlpha);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
        this.mHasSetAlpha = true;
        if (this.mVerticalTrack != null) {
            this.mVerticalTrack.setAlpha(alpha);
        }
        if (this.mVerticalThumb != null) {
            this.mVerticalThumb.setAlpha(alpha);
        }
        if (this.mHorizontalTrack != null) {
            this.mHorizontalTrack.setAlpha(alpha);
        }
        if (this.mHorizontalThumb != null) {
            this.mHorizontalThumb.setAlpha(alpha);
        }
    }

    @Override
    public int getAlpha() {
        return this.mAlpha;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        this.invalidateSelf();
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        this.scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        this.unscheduleSelf(what);
    }
}