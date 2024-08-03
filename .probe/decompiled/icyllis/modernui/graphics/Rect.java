package icyllis.modernui.graphics;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;

public class Rect {

    public int left;

    public int top;

    public int right;

    public int bottom;

    public Rect() {
    }

    public Rect(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Rect(@Nullable Rect r) {
        if (r != null) {
            this.left = r.left;
            this.top = r.top;
            this.right = r.right;
            this.bottom = r.bottom;
        }
    }

    @NonNull
    public static Rect copy(@Nullable Rect r) {
        return r == null ? new Rect() : r.copy();
    }

    public final boolean isEmpty() {
        return this.left >= this.right || this.top >= this.bottom;
    }

    public final int x() {
        return this.left;
    }

    public final int y() {
        return this.top;
    }

    public final int width() {
        return this.right - this.left;
    }

    public final int height() {
        return this.bottom - this.top;
    }

    public final int centerX() {
        return this.left + this.right >> 1;
    }

    public final int centerY() {
        return this.top + this.bottom >> 1;
    }

    public final float exactCenterX() {
        return (float) (this.left + this.right) * 0.5F;
    }

    public final float exactCenterY() {
        return (float) (this.top + this.bottom) * 0.5F;
    }

    public void setEmpty() {
        this.left = this.right = this.top = this.bottom = 0;
    }

    public void set(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void set(@NonNull Rect src) {
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    public void offset(int dx, int dy) {
        this.left += dx;
        this.top += dy;
        this.right += dx;
        this.bottom += dy;
    }

    public void offsetTo(int newLeft, int newTop) {
        this.right = this.right + (newLeft - this.left);
        this.bottom = this.bottom + (newTop - this.top);
        this.left = newLeft;
        this.top = newTop;
    }

    public void inset(int dx, int dy) {
        this.left += dx;
        this.top += dy;
        this.right -= dx;
        this.bottom -= dy;
    }

    public void inset(@NonNull Rect insets) {
        this.left = this.left + insets.left;
        this.top = this.top + insets.top;
        this.right = this.right - insets.right;
        this.bottom = this.bottom - insets.bottom;
    }

    public void inset(int left, int top, int right, int bottom) {
        this.left += left;
        this.top += top;
        this.right -= right;
        this.bottom -= bottom;
    }

    public boolean contains(int x, int y) {
        return this.left < this.right && this.top < this.bottom && x >= this.left && x < this.right && y >= this.top && y < this.bottom;
    }

    public boolean contains(int left, int top, int right, int bottom) {
        return this.left < this.right && this.top < this.bottom && this.left <= left && this.top <= top && this.right >= right && this.bottom >= bottom;
    }

    public boolean contains(@NonNull Rect r) {
        return this.left < this.right && this.top < this.bottom && this.left <= r.left && this.top <= r.top && this.right >= r.right && this.bottom >= r.bottom;
    }

    public boolean intersect(int left, int top, int right, int bottom) {
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

    public boolean intersect(@NonNull Rect r) {
        return this.intersect(r.left, r.top, r.right, r.bottom);
    }

    public void intersectUnchecked(@NonNull Rect other) {
        this.left = Math.max(this.left, other.left);
        this.top = Math.max(this.top, other.top);
        this.right = Math.min(this.right, other.right);
        this.bottom = Math.min(this.bottom, other.bottom);
    }

    public boolean setIntersect(@NonNull Rect a, @NonNull Rect b) {
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

    public boolean intersects(int left, int top, int right, int bottom) {
        return this.left < right && left < this.right && this.top < bottom && top < this.bottom;
    }

    public static boolean intersects(@NonNull Rect a, @NonNull Rect b) {
        return a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom;
    }

    public final void join(int left, int top, int right, int bottom) {
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

    public void union(int left, int top, int right, int bottom) {
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

    public void union(@NonNull Rect r) {
        this.union(r.left, r.top, r.right, r.bottom);
    }

    public void union(int x, int y) {
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
            int temp = this.left;
            this.left = this.right;
            this.right = temp;
        }
        if (this.top > this.bottom) {
            int temp = this.top;
            this.top = this.bottom;
            this.bottom = temp;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Rect rect = (Rect) o;
            return this.left == rect.left && this.top == rect.top && this.right == rect.right && this.bottom == rect.bottom;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.left;
        result = 31 * result + this.top;
        result = 31 * result + this.right;
        return 31 * result + this.bottom;
    }

    public String toString() {
        return "Rect(" + this.left + ", " + this.top + " - " + this.right + ", " + this.bottom + ")";
    }

    @NonNull
    public String toShortString() {
        return "[" + this.left + "," + this.top + "][" + this.right + "," + this.bottom + "]";
    }

    @NonNull
    public Rect copy() {
        return new Rect(this.left, this.top, this.right, this.bottom);
    }
}