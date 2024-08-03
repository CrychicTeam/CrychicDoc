package icyllis.arc3d.core;

import javax.annotation.Nonnull;

public non-sealed class Rect2f implements Rect2fc {

    private static final Rect2fc EMPTY = new Rect2f();

    public float mLeft;

    public float mTop;

    public float mRight;

    public float mBottom;

    public Rect2f() {
    }

    public Rect2f(float left, float top, float right, float bottom) {
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
    }

    public Rect2f(@Nonnull Rect2fc r) {
        r.store(this);
    }

    public Rect2f(@Nonnull Rect2ic r) {
        r.store(this);
    }

    @Nonnull
    public static Rect2fc empty() {
        return EMPTY;
    }

    @Override
    public final boolean isEmpty() {
        return !(this.mLeft < this.mRight) || !(this.mTop < this.mBottom);
    }

    @Override
    public final boolean isSorted() {
        return this.mLeft <= this.mRight && this.mTop <= this.mBottom;
    }

    @Override
    public final boolean isFinite() {
        return 0.0F * this.mLeft * this.mTop * this.mRight * this.mBottom == 0.0F;
    }

    public static boolean isFinite(float left, float top, float right, float bottom) {
        return 0.0F * left * top * right * bottom == 0.0F;
    }

    @Override
    public final float x() {
        return this.mLeft;
    }

    @Override
    public final float y() {
        return this.mTop;
    }

    @Override
    public final float left() {
        return this.mLeft;
    }

    @Override
    public final float top() {
        return this.mTop;
    }

    @Override
    public final float right() {
        return this.mRight;
    }

    @Override
    public final float bottom() {
        return this.mBottom;
    }

    @Override
    public final float width() {
        return this.mRight - this.mLeft;
    }

    @Override
    public final float height() {
        return this.mBottom - this.mTop;
    }

    @Override
    public final float centerX() {
        return (float) (((double) this.mLeft + (double) this.mRight) * 0.5);
    }

    @Override
    public final float centerY() {
        return (float) (((double) this.mTop + (double) this.mBottom) * 0.5);
    }

    public final void setEmpty() {
        this.mLeft = this.mRight = this.mTop = this.mBottom = 0.0F;
    }

    @Override
    public void store(@Nonnull Rect2f dst) {
        dst.mLeft = this.mLeft;
        dst.mTop = this.mTop;
        dst.mRight = this.mRight;
        dst.mBottom = this.mBottom;
    }

    public final void set(float left, float top, float right, float bottom) {
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
    }

    public final void set(Rect2fc src) {
        src.store(this);
    }

    public final void set(Rect2ic src) {
        src.store(this);
    }

    public final boolean setBounds(float[] pts, int pos, int count) {
        if (count <= 0) {
            this.setEmpty();
            return true;
        } else {
            float maxX;
            float minX = maxX = pts[pos++];
            float maxY;
            float minY = maxY = pts[pos++];
            count--;
            float prodX = 0.0F * minX;
            float prodY;
            for (prodY = 0.0F * minY; count != 0; count--) {
                float x = pts[pos++];
                float y = pts[pos++];
                prodX *= x;
                prodY *= y;
                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
            }
            if (prodX == 0.0F && prodY == 0.0F) {
                this.set(minX, minY, maxX, maxY);
                return true;
            } else {
                this.setEmpty();
                return false;
            }
        }
    }

    public final void setBoundsNoCheck(float[] pts, int pos, int count) {
        if (!this.setBounds(pts, pos, count)) {
            this.set(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
        }
    }

    public final void offset(float dx, float dy) {
        this.mLeft += dx;
        this.mTop += dy;
        this.mRight += dx;
        this.mBottom += dy;
    }

    public final void offsetTo(float newLeft, float newTop) {
        this.mRight = this.mRight + (newLeft - this.mLeft);
        this.mBottom = this.mBottom + (newTop - this.mTop);
        this.mLeft = newLeft;
        this.mTop = newTop;
    }

    public final void inset(float dx, float dy) {
        this.mLeft += dx;
        this.mTop += dy;
        this.mRight -= dx;
        this.mBottom -= dy;
    }

    public final void outset(float dx, float dy) {
        this.inset(-dx, -dy);
    }

    public final void inset(float left, float top, float right, float bottom) {
        this.mLeft += left;
        this.mTop += top;
        this.mRight -= right;
        this.mBottom -= bottom;
    }

    public final void inset(Rect2fc insets) {
        this.mLeft = this.mLeft + insets.left();
        this.mTop = this.mTop + insets.top();
        this.mRight = this.mRight - insets.right();
        this.mBottom = this.mBottom - insets.bottom();
    }

    public final void inset(Rect2ic insets) {
        this.mLeft = this.mLeft + (float) insets.left();
        this.mTop = this.mTop + (float) insets.top();
        this.mRight = this.mRight - (float) insets.right();
        this.mBottom = this.mBottom - (float) insets.bottom();
    }

    public final void adjust(float left, float top, float right, float bottom) {
        this.mLeft += left;
        this.mTop += top;
        this.mRight += right;
        this.mBottom += bottom;
    }

    public final void adjust(Rect2fc adjusts) {
        this.mLeft = this.mLeft + adjusts.left();
        this.mTop = this.mTop + adjusts.top();
        this.mRight = this.mRight + adjusts.right();
        this.mBottom = this.mBottom + adjusts.bottom();
    }

    public final void adjust(Rect2ic adjusts) {
        this.mLeft = this.mLeft + (float) adjusts.left();
        this.mTop = this.mTop + (float) adjusts.top();
        this.mRight = this.mRight + (float) adjusts.right();
        this.mBottom = this.mBottom + (float) adjusts.bottom();
    }

    @Override
    public final boolean contains(float x, float y) {
        return x >= this.mLeft && x < this.mRight && y >= this.mTop && y < this.mBottom;
    }

    public final boolean contains(float left, float top, float right, float bottom) {
        return this.mLeft < this.mRight && this.mTop < this.mBottom && this.mLeft <= left && this.mTop <= top && this.mRight >= right && this.mBottom >= bottom;
    }

    @Override
    public final boolean contains(@Nonnull Rect2fc r) {
        return this.mLeft < this.mRight && this.mTop < this.mBottom && this.mLeft <= r.left() && this.mTop <= r.top() && this.mRight >= r.right() && this.mBottom >= r.bottom();
    }

    @Override
    public final boolean contains(@Nonnull Rect2ic r) {
        return this.mLeft < this.mRight && this.mTop < this.mBottom && this.mLeft <= (float) r.left() && this.mTop <= (float) r.top() && this.mRight >= (float) r.right() && this.mBottom >= (float) r.bottom();
    }

    public static boolean subtract(Rect2fc a, Rect2fc b, Rect2f out) {
        assert out != b;
        if (!a.isEmpty() && !b.isEmpty() && intersects(a, b)) {
            float aHeight = a.height();
            float aWidth = a.width();
            float leftArea = 0.0F;
            float rightArea = 0.0F;
            float topArea = 0.0F;
            float bottomArea = 0.0F;
            int positiveCount = 0;
            if (b.left() > a.left()) {
                leftArea = (b.left() - a.left()) / aWidth;
                positiveCount++;
            }
            if (a.right() > b.right()) {
                rightArea = (a.right() - b.right()) / aWidth;
                positiveCount++;
            }
            if (b.top() > a.top()) {
                topArea = (b.top() - a.top()) / aHeight;
                positiveCount++;
            }
            if (a.bottom() > b.bottom()) {
                bottomArea = (a.bottom() - b.bottom()) / aHeight;
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

    public final boolean intersect(float left, float top, float right, float bottom) {
        float tmpL = Math.max(this.mLeft, left);
        float tmpT = Math.max(this.mTop, top);
        float tmpR = Math.min(this.mRight, right);
        float tmpB = Math.min(this.mBottom, bottom);
        if (!(tmpR <= tmpL) && !(tmpB <= tmpT)) {
            this.mLeft = tmpL;
            this.mTop = tmpT;
            this.mRight = tmpR;
            this.mBottom = tmpB;
            return true;
        } else {
            return false;
        }
    }

    public final boolean intersect(Rect2fc r) {
        return this.intersect(r.left(), r.top(), r.right(), r.bottom());
    }

    public final boolean intersect(Rect2ic r) {
        return this.intersect((float) r.left(), (float) r.top(), (float) r.right(), (float) r.bottom());
    }

    public final void intersectNoCheck(float left, float top, float right, float bottom) {
        this.mLeft = Math.max(this.mLeft, left);
        this.mTop = Math.max(this.mTop, top);
        this.mRight = Math.min(this.mRight, right);
        this.mBottom = Math.min(this.mBottom, bottom);
    }

    public final void intersectNoCheck(Rect2fc r) {
        this.intersectNoCheck(r.left(), r.top(), r.right(), r.bottom());
    }

    public final void intersectNoCheck(Rect2ic r) {
        this.intersectNoCheck((float) r.left(), (float) r.top(), (float) r.right(), (float) r.bottom());
    }

    public final boolean intersect(Rect2fc a, Rect2fc b) {
        float tmpL = Math.max(a.left(), b.left());
        float tmpT = Math.max(a.top(), b.top());
        float tmpR = Math.min(a.right(), b.right());
        float tmpB = Math.min(a.bottom(), b.bottom());
        if (!(tmpR <= tmpL) && !(tmpB <= tmpT)) {
            this.mLeft = tmpL;
            this.mTop = tmpT;
            this.mRight = tmpR;
            this.mBottom = tmpB;
            return true;
        } else {
            return false;
        }
    }

    public final boolean intersects(float left, float top, float right, float bottom) {
        float tmpL = Math.max(this.mLeft, left);
        float tmpT = Math.max(this.mTop, top);
        float tmpR = Math.min(this.mRight, right);
        float tmpB = Math.min(this.mBottom, bottom);
        return tmpR > tmpL && tmpB > tmpT;
    }

    @Override
    public final boolean intersects(@Nonnull Rect2fc r) {
        return this.intersects(r.left(), r.top(), r.right(), r.bottom());
    }

    @Override
    public final boolean intersects(@Nonnull Rect2ic r) {
        return this.intersects((float) r.left(), (float) r.top(), (float) r.right(), (float) r.bottom());
    }

    public static boolean intersects(Rect2fc a, Rect2fc b) {
        float tmpL = Math.max(a.left(), b.left());
        float tmpT = Math.max(a.top(), b.top());
        float tmpR = Math.min(a.right(), b.right());
        float tmpB = Math.min(a.bottom(), b.bottom());
        return tmpR > tmpL && tmpB > tmpT;
    }

    public static boolean rectsOverlap(Rect2fc a, Rect2fc b) {
        assert !a.isFinite() || a.left() <= a.right() && a.top() <= a.bottom();
        assert !isFinite(b.left(), b.top(), b.right(), b.bottom()) || b.left() <= b.right() && b.top() <= b.bottom();
        return a.right() > b.left() && a.bottom() > b.top() && b.right() > a.left() && b.bottom() > a.top();
    }

    public static boolean rectsTouchOrOverlap(Rect2fc a, Rect2fc b) {
        assert !a.isFinite() || a.left() <= a.right() && a.top() <= a.bottom();
        assert !isFinite(b.left(), b.top(), b.right(), b.bottom()) || b.left() <= b.right() && b.top() <= b.bottom();
        return a.right() >= b.left() && a.bottom() >= b.top() && b.right() >= a.left() && b.bottom() >= a.top();
    }

    @Override
    public final void round(@Nonnull Rect2i dst) {
        dst.set(Math.round(this.mLeft), Math.round(this.mTop), Math.round(this.mRight), Math.round(this.mBottom));
    }

    @Override
    public final void roundIn(@Nonnull Rect2i dst) {
        dst.set((int) Math.ceil((double) this.mLeft), (int) Math.ceil((double) this.mTop), (int) Math.floor((double) this.mRight), (int) Math.floor((double) this.mBottom));
    }

    @Override
    public final void roundOut(@Nonnull Rect2i dst) {
        dst.set((int) Math.floor((double) this.mLeft), (int) Math.floor((double) this.mTop), (int) Math.ceil((double) this.mRight), (int) Math.ceil((double) this.mBottom));
    }

    @Override
    public final void round(@Nonnull Rect2f dst) {
        dst.set((float) Math.round(this.mLeft), (float) Math.round(this.mTop), (float) Math.round(this.mRight), (float) Math.round(this.mBottom));
    }

    @Override
    public final void roundIn(@Nonnull Rect2f dst) {
        dst.set((float) Math.ceil((double) this.mLeft), (float) Math.ceil((double) this.mTop), (float) Math.floor((double) this.mRight), (float) Math.floor((double) this.mBottom));
    }

    @Override
    public final void roundOut(@Nonnull Rect2f dst) {
        dst.set((float) Math.floor((double) this.mLeft), (float) Math.floor((double) this.mTop), (float) Math.ceil((double) this.mRight), (float) Math.ceil((double) this.mBottom));
    }

    public final void join(float left, float top, float right, float bottom) {
        if (!(left >= right) && !(top >= bottom)) {
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

    public final void join(Rect2fc r) {
        this.join(r.left(), r.top(), r.right(), r.bottom());
    }

    public final void join(Rect2ic r) {
        this.join((float) r.left(), (float) r.top(), (float) r.right(), (float) r.bottom());
    }

    public final void joinNoCheck(float left, float top, float right, float bottom) {
        this.mLeft = Math.min(this.mLeft, left);
        this.mTop = Math.min(this.mTop, top);
        this.mRight = Math.max(this.mRight, right);
        this.mBottom = Math.max(this.mBottom, bottom);
    }

    public final void joinNoCheck(Rect2fc r) {
        this.joinNoCheck(r.left(), r.top(), r.right(), r.bottom());
    }

    public final void joinNoCheck(Rect2ic r) {
        this.joinNoCheck((float) r.left(), (float) r.top(), (float) r.right(), (float) r.bottom());
    }

    public final void join(float x, float y) {
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
            float temp = this.mLeft;
            this.mLeft = this.mRight;
            this.mRight = temp;
        }
        if (this.mTop > this.mBottom) {
            float temp = this.mTop;
            this.mTop = this.mBottom;
            this.mBottom = temp;
        }
    }

    public boolean equals(Object o) {
        return !(o instanceof Rect2fc r) ? false : this.mLeft == r.left() && this.mTop == r.top() && this.mRight == r.right() && this.mBottom == r.bottom();
    }

    public int hashCode() {
        int result = Float.floatToIntBits(this.mLeft);
        result = 31 * result + Float.floatToIntBits(this.mTop);
        result = 31 * result + Float.floatToIntBits(this.mRight);
        return 31 * result + Float.floatToIntBits(this.mBottom);
    }

    public String toString() {
        return "Rect2f(" + this.mLeft + ", " + this.mTop + ", " + this.mRight + ", " + this.mBottom + ")";
    }
}