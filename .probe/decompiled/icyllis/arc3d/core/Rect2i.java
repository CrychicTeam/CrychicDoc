package icyllis.arc3d.core;

import javax.annotation.Nonnull;

public non-sealed class Rect2i implements Rect2ic {

    private static final Rect2ic EMPTY = new Rect2i();

    public int mLeft;

    public int mTop;

    public int mRight;

    public int mBottom;

    public Rect2i() {
    }

    public Rect2i(int left, int top, int right, int bottom) {
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
    }

    public Rect2i(@Nonnull Rect2ic r) {
        r.store(this);
    }

    @Nonnull
    public static Rect2ic empty() {
        return EMPTY;
    }

    @Override
    public final boolean isEmpty() {
        return this.mRight <= this.mLeft || this.mBottom <= this.mTop;
    }

    @Override
    public final boolean isSorted() {
        return this.mLeft <= this.mRight && this.mTop <= this.mBottom;
    }

    @Override
    public final int x() {
        return this.mLeft;
    }

    @Override
    public final int y() {
        return this.mTop;
    }

    @Override
    public final int left() {
        return this.mLeft;
    }

    @Override
    public final int top() {
        return this.mTop;
    }

    @Override
    public final int right() {
        return this.mRight;
    }

    @Override
    public final int bottom() {
        return this.mBottom;
    }

    @Override
    public final int width() {
        return this.mRight - this.mLeft;
    }

    @Override
    public final int height() {
        return this.mBottom - this.mTop;
    }

    public final void setEmpty() {
        this.mLeft = this.mRight = this.mTop = this.mBottom = 0;
    }

    @Override
    public void store(@Nonnull Rect2i dst) {
        dst.mLeft = this.mLeft;
        dst.mTop = this.mTop;
        dst.mRight = this.mRight;
        dst.mBottom = this.mBottom;
    }

    @Override
    public void store(@Nonnull Rect2f dst) {
        dst.mLeft = (float) this.mLeft;
        dst.mTop = (float) this.mTop;
        dst.mRight = (float) this.mRight;
        dst.mBottom = (float) this.mBottom;
    }

    public final void set(int left, int top, int right, int bottom) {
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
    }

    public final void set(@Nonnull Rect2ic src) {
        src.store(this);
    }

    public final void offset(int dx, int dy) {
        this.mLeft += dx;
        this.mTop += dy;
        this.mRight += dx;
        this.mBottom += dy;
    }

    public final void offsetTo(int newLeft, int newTop) {
        this.mRight = this.mRight + (newLeft - this.mLeft);
        this.mBottom = this.mBottom + (newTop - this.mTop);
        this.mLeft = newLeft;
        this.mTop = newTop;
    }

    public final void inset(int dx, int dy) {
        this.mLeft += dx;
        this.mTop += dy;
        this.mRight -= dx;
        this.mBottom -= dy;
    }

    public final void inset(int left, int top, int right, int bottom) {
        this.mLeft += left;
        this.mTop += top;
        this.mRight -= right;
        this.mBottom -= bottom;
    }

    public final void inset(@Nonnull Rect2ic insets) {
        this.mLeft = this.mLeft + insets.left();
        this.mTop = this.mTop + insets.top();
        this.mRight = this.mRight - insets.right();
        this.mBottom = this.mBottom - insets.bottom();
    }

    public final void adjust(int left, int top, int right, int bottom) {
        this.mLeft += left;
        this.mTop += top;
        this.mRight += right;
        this.mBottom += bottom;
    }

    public final void adjust(@Nonnull Rect2ic adjusts) {
        this.mLeft = this.mLeft + adjusts.left();
        this.mTop = this.mTop + adjusts.top();
        this.mRight = this.mRight + adjusts.right();
        this.mBottom = this.mBottom + adjusts.bottom();
    }

    @Override
    public final boolean contains(int x, int y) {
        return x >= this.mLeft && x < this.mRight && y >= this.mTop && y < this.mBottom;
    }

    @Override
    public final boolean contains(float x, float y) {
        return x >= (float) this.mLeft && x < (float) this.mRight && y >= (float) this.mTop && y < (float) this.mBottom;
    }

    public final boolean contains(int left, int top, int right, int bottom) {
        return this.mLeft < this.mRight && this.mTop < this.mBottom && this.mLeft <= left && this.mTop <= top && this.mRight >= right && this.mBottom >= bottom;
    }

    @Override
    public final boolean contains(Rect2ic r) {
        return this.mLeft < this.mRight && this.mTop < this.mBottom && this.mLeft <= r.left() && this.mTop <= r.top() && this.mRight >= r.right() && this.mBottom >= r.bottom();
    }

    public final boolean contains(float left, float top, float right, float bottom) {
        return this.mLeft < this.mRight && this.mTop < this.mBottom && (float) this.mLeft <= left && (float) this.mTop <= top && (float) this.mRight >= right && (float) this.mBottom >= bottom;
    }

    @Override
    public final boolean contains(Rect2fc r) {
        return this.mLeft < this.mRight && this.mTop < this.mBottom && (float) this.mLeft <= r.left() && (float) this.mTop <= r.top() && (float) this.mRight >= r.right() && (float) this.mBottom >= r.bottom();
    }

    public static boolean subtract(Rect2ic a, Rect2ic b, Rect2i out) {
        assert out != b;
        if (!a.isEmpty() && !b.isEmpty() && intersects(a, b)) {
            float aHeight = (float) a.height();
            float aWidth = (float) a.width();
            float leftArea = 0.0F;
            float rightArea = 0.0F;
            float topArea = 0.0F;
            float bottomArea = 0.0F;
            int positiveCount = 0;
            if (b.left() > a.left()) {
                leftArea = (float) (b.left() - a.left()) / aWidth;
                positiveCount++;
            }
            if (a.right() > b.right()) {
                rightArea = (float) (a.right() - b.right()) / aWidth;
                positiveCount++;
            }
            if (b.top() > a.top()) {
                topArea = (float) (b.top() - a.top()) / aHeight;
                positiveCount++;
            }
            if (a.bottom() > b.bottom()) {
                bottomArea = (float) (a.bottom() - b.bottom()) / aHeight;
                positiveCount++;
            }
            if (positiveCount == 0) {
                assert b.contains(a);
                out.setEmpty();
                return true;
            } else {
                if (out != a) {
                    out.set(a);
                }
                if (leftArea > rightArea && leftArea > topArea && leftArea > bottomArea) {
                    out.mRight = b.left();
                } else if (rightArea > topArea && rightArea > bottomArea) {
                    out.mLeft = b.right();
                } else if (topArea > bottomArea) {
                    out.mBottom = b.top();
                } else {
                    assert bottomArea > 0.0F;
                    out.mTop = b.bottom();
                }
                assert !intersects(out, b);
                return positiveCount == 1;
            }
        } else {
            if (out != a) {
                out.set(a);
            }
            return true;
        }
    }

    public final boolean intersect(int left, int top, int right, int bottom) {
        int tmpL = Math.max(this.mLeft, left);
        int tmpT = Math.max(this.mTop, top);
        int tmpR = Math.min(this.mRight, right);
        int tmpB = Math.min(this.mBottom, bottom);
        if (tmpR > tmpL && tmpB > tmpT) {
            this.mLeft = tmpL;
            this.mTop = tmpT;
            this.mRight = tmpR;
            this.mBottom = tmpB;
            return true;
        } else {
            return false;
        }
    }

    public final boolean intersect(Rect2ic r) {
        return this.intersect(r.left(), r.top(), r.right(), r.bottom());
    }

    public final void intersectNoCheck(int left, int top, int right, int bottom) {
        this.mLeft = Math.max(this.mLeft, left);
        this.mTop = Math.max(this.mTop, top);
        this.mRight = Math.min(this.mRight, right);
        this.mBottom = Math.min(this.mBottom, bottom);
    }

    public final void intersectNoCheck(Rect2ic r) {
        this.intersectNoCheck(r.left(), r.top(), r.right(), r.bottom());
    }

    public final boolean intersect(Rect2ic a, Rect2ic b) {
        int tmpL = Math.max(a.left(), b.left());
        int tmpT = Math.max(a.top(), b.top());
        int tmpR = Math.min(a.right(), b.right());
        int tmpB = Math.min(a.bottom(), b.bottom());
        if (tmpR > tmpL && tmpB > tmpT) {
            this.mLeft = tmpL;
            this.mTop = tmpT;
            this.mRight = tmpR;
            this.mBottom = tmpB;
            return true;
        } else {
            return false;
        }
    }

    public final boolean intersects(int left, int top, int right, int bottom) {
        int tmpL = Math.max(this.mLeft, left);
        int tmpT = Math.max(this.mTop, top);
        int tmpR = Math.min(this.mRight, right);
        int tmpB = Math.min(this.mBottom, bottom);
        return tmpR > tmpL && tmpB > tmpT;
    }

    @Override
    public final boolean intersects(Rect2ic r) {
        return this.intersects(r.left(), r.top(), r.right(), r.bottom());
    }

    public static boolean intersects(Rect2ic a, Rect2ic b) {
        int tmpL = Math.max(a.left(), b.left());
        int tmpT = Math.max(a.top(), b.top());
        int tmpR = Math.min(a.right(), b.right());
        int tmpB = Math.min(a.bottom(), b.bottom());
        return tmpR > tmpL && tmpB > tmpT;
    }

    public final void join(int left, int top, int right, int bottom) {
        if (left < right && top < bottom) {
            if (this.mLeft < this.mRight && this.mTop < this.mBottom) {
                if (this.mLeft > left) {
                    this.mLeft = left;
                }
                if (this.mTop > top) {
                    this.mTop = top;
                }
                if (this.mRight < right) {
                    this.mRight = right;
                }
                if (this.mBottom < bottom) {
                    this.mBottom = bottom;
                }
            } else {
                this.mLeft = left;
                this.mTop = top;
                this.mRight = right;
                this.mBottom = bottom;
            }
        }
    }

    public final void join(Rect2ic r) {
        this.join(r.left(), r.top(), r.right(), r.bottom());
    }

    public final void joinNoCheck(int left, int top, int right, int bottom) {
        this.mLeft = Math.min(this.mLeft, left);
        this.mTop = Math.min(this.mTop, top);
        this.mRight = Math.max(this.mRight, right);
        this.mBottom = Math.max(this.mBottom, bottom);
    }

    public final void joinNoCheck(Rect2ic r) {
        this.joinNoCheck(r.left(), r.top(), r.right(), r.bottom());
    }

    public final void join(int x, int y) {
        if (this.mLeft < this.mRight && this.mTop < this.mBottom) {
            if (x < this.mLeft) {
                this.mLeft = x;
            } else if (x > this.mRight) {
                this.mRight = x;
            }
            if (y < this.mTop) {
                this.mTop = y;
            } else if (y > this.mBottom) {
                this.mBottom = y;
            }
        } else {
            this.mLeft = this.mRight = x;
            this.mTop = this.mBottom = y;
        }
    }

    public final void sort() {
        if (this.mLeft > this.mRight) {
            int temp = this.mLeft;
            this.mLeft = this.mRight;
            this.mRight = temp;
        }
        if (this.mTop > this.mBottom) {
            int temp = this.mTop;
            this.mTop = this.mBottom;
            this.mBottom = temp;
        }
    }

    public boolean equals(Object o) {
        return !(o instanceof Rect2ic r) ? false : this.mLeft == r.left() && this.mTop == r.top() && this.mRight == r.right() && this.mBottom == r.bottom();
    }

    public int hashCode() {
        int result = this.mLeft;
        result = 31 * result + this.mTop;
        result = 31 * result + this.mRight;
        return 31 * result + this.mBottom;
    }

    public String toString() {
        return "Rect2i(" + this.mLeft + ", " + this.mTop + ", " + this.mRight + ", " + this.mBottom + ")";
    }
}