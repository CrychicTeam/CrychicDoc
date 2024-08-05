package icyllis.modernui.graphics;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;

public class RectF {

    public float left;

    public float top;

    public float right;

    public float bottom;

    public RectF() {
    }

    public RectF(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public RectF(@Nullable RectF r) {
        if (r != null) {
            this.left = r.left;
            this.top = r.top;
            this.right = r.right;
            this.bottom = r.bottom;
        }
    }

    public RectF(@Nullable Rect r) {
        if (r != null) {
            this.left = (float) r.left;
            this.top = (float) r.top;
            this.right = (float) r.right;
            this.bottom = (float) r.bottom;
        }
    }

    @NonNull
    public static RectF copy(@Nullable RectF r) {
        return r == null ? new RectF() : r.copy();
    }

    public final boolean isEmpty() {
        return !(this.left < this.right) || !(this.top < this.bottom);
    }

    public final boolean isSorted() {
        return this.left <= this.right && this.top <= this.bottom;
    }

    public final boolean isFinite() {
        return Float.isFinite(this.left) && Float.isFinite(this.top) && Float.isFinite(this.right) && Float.isFinite(this.bottom);
    }

    public final float width() {
        return this.right - this.left;
    }

    public final float height() {
        return this.bottom - this.top;
    }

    public final float centerX() {
        return (this.left + this.right) * 0.5F;
    }

    public final float centerY() {
        return (this.top + this.bottom) * 0.5F;
    }

    public void setEmpty() {
        this.left = this.right = this.top = this.bottom = 0.0F;
    }

    public void set(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void set(@NonNull RectF src) {
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    public void set(@NonNull Rect src) {
        this.left = (float) src.left;
        this.top = (float) src.top;
        this.right = (float) src.right;
        this.bottom = (float) src.bottom;
    }

    public void offset(float dx, float dy) {
        this.left += dx;
        this.top += dy;
        this.right += dx;
        this.bottom += dy;
    }

    public void offsetTo(float newLeft, float newTop) {
        this.right = this.right + (newLeft - this.left);
        this.bottom = this.bottom + (newTop - this.top);
        this.left = newLeft;
        this.top = newTop;
    }

    public void inset(float dx, float dy) {
        this.left += dx;
        this.top += dy;
        this.right -= dx;
        this.bottom -= dy;
    }

    public boolean contains(float x, float y) {
        return this.left < this.right && this.top < this.bottom && x >= this.left && x < this.right && y >= this.top && y < this.bottom;
    }

    public boolean contains(float left, float top, float right, float bottom) {
        return this.left < this.right && this.top < this.bottom && this.left <= left && this.top <= top && this.right >= right && this.bottom >= bottom;
    }

    public boolean contains(@NonNull RectF r) {
        return this.left < this.right && this.top < this.bottom && this.left <= r.left && this.top <= r.top && this.right >= r.right && this.bottom >= r.bottom;
    }

    public boolean intersect(float left, float top, float right, float bottom) {
        if (this.left < right && left < this.right && this.top < bottom && top < this.bottom) {
            if (this.left < left) {
                this.left = left;
            }
            if (this.top < top) {
                this.top = top;
            }
            if (this.right > right) {
                this.right = right;
            }
            if (this.bottom > bottom) {
                this.bottom = bottom;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean intersect(@NonNull RectF r) {
        return this.intersect(r.left, r.top, r.right, r.bottom);
    }

    public boolean setIntersect(@NonNull RectF a, @NonNull RectF b) {
        if (a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom) {
            this.left = Math.max(a.left, b.left);
            this.top = Math.max(a.top, b.top);
            this.right = Math.min(a.right, b.right);
            this.bottom = Math.min(a.bottom, b.bottom);
            return true;
        } else {
            return false;
        }
    }

    public boolean intersects(float left, float top, float right, float bottom) {
        return this.left < right && left < this.right && this.top < bottom && top < this.bottom;
    }

    public static boolean intersects(@NonNull RectF a, @NonNull RectF b) {
        return a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom;
    }

    public void round(@NonNull Rect dst) {
        dst.set(Math.round(this.left), Math.round(this.top), Math.round(this.right), Math.round(this.bottom));
    }

    public void roundOut(@NonNull Rect dst) {
        dst.set((int) Math.floor((double) this.left), (int) Math.floor((double) this.top), (int) Math.ceil((double) this.right), (int) Math.ceil((double) this.bottom));
    }

    public void union(float left, float top, float right, float bottom) {
        if (left < right && top < bottom) {
            if (this.left < this.right && this.top < this.bottom) {
                if (this.left > left) {
                    this.left = left;
                }
                if (this.top > top) {
                    this.top = top;
                }
                if (this.right < right) {
                    this.right = right;
                }
                if (this.bottom < bottom) {
                    this.bottom = bottom;
                }
            } else {
                this.left = left;
                this.top = top;
                this.right = right;
                this.bottom = bottom;
            }
        }
    }

    public void union(@NonNull RectF r) {
        this.union(r.left, r.top, r.right, r.bottom);
    }

    public void union(float x, float y) {
        if (x < this.left) {
            this.left = x;
        } else if (x > this.right) {
            this.right = x;
        }
        if (y < this.top) {
            this.top = y;
        } else if (y > this.bottom) {
            this.bottom = y;
        }
    }

    public void sort() {
        if (this.left > this.right) {
            float temp = this.left;
            this.left = this.right;
            this.right = temp;
        }
        if (this.top > this.bottom) {
            float temp = this.top;
            this.top = this.bottom;
            this.bottom = temp;
        }
    }

    public void transform(@NonNull Matrix m) {
        m.mapRect(this);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            RectF r = (RectF) o;
            return this.left == r.left && this.top == r.top && this.right == r.right && this.bottom == r.bottom;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.left != 0.0F ? Float.floatToIntBits(this.left) : 0;
        result = 31 * result + (this.top != 0.0F ? Float.floatToIntBits(this.top) : 0);
        result = 31 * result + (this.right != 0.0F ? Float.floatToIntBits(this.right) : 0);
        return 31 * result + (this.bottom != 0.0F ? Float.floatToIntBits(this.bottom) : 0);
    }

    public String toString() {
        return "RectF(" + this.left + ", " + this.top + ", " + this.right + ", " + this.bottom + ")";
    }

    @NonNull
    public RectF copy() {
        return new RectF(this.left, this.top, this.right, this.bottom);
    }
}