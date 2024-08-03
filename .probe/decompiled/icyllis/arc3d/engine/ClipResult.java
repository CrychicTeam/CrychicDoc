package icyllis.arc3d.engine;

import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.core.Rect2ic;

public final class ClipResult implements Cloneable {

    private int mStencilSeq;

    private int mScissorX0;

    private int mScissorY0;

    private int mScissorX1;

    private int mScissorY1;

    private int mScreenWidth;

    private int mScreenHeight;

    public ClipResult init(int logicalWidth, int logicalHeight, int physicalWidth, int physicalHeight) {
        assert logicalWidth > 0 && logicalHeight > 0;
        assert physicalWidth > 0 && physicalHeight > 0;
        assert logicalWidth <= physicalWidth && logicalHeight <= physicalHeight;
        this.mScreenWidth = physicalWidth;
        this.mScreenHeight = physicalHeight;
        this.setScissor(0, 0, logicalWidth, logicalHeight);
        this.mStencilSeq = 0;
        return this;
    }

    public boolean addScissor(Rect2ic rect, Rect2f clippedBounds) {
        return this.intersect(rect.left(), rect.top(), rect.right(), rect.bottom()) && clippedBounds.intersect(rect);
    }

    public void setScissor(int l, int t, int r, int b) {
        this.mScissorX0 = 0;
        this.mScissorY0 = 0;
        this.mScissorX1 = this.mScreenWidth;
        this.mScissorY1 = this.mScreenHeight;
        this.intersect(l, t, r, b);
    }

    private boolean intersect(int l, int t, int r, int b) {
        int tmpL = Math.max(this.mScissorX0, l);
        int tmpT = Math.max(this.mScissorY0, t);
        int tmpR = Math.min(this.mScissorX1, r);
        int tmpB = Math.min(this.mScissorY1, b);
        if (tmpR > tmpL && tmpB > tmpT) {
            this.mScissorX0 = tmpL;
            this.mScissorY0 = tmpT;
            this.mScissorX1 = tmpR;
            this.mScissorY1 = tmpB;
            return true;
        } else {
            this.mScissorX0 = 0;
            this.mScissorY0 = 0;
            this.mScissorX1 = 0;
            this.mScissorY1 = 0;
            return false;
        }
    }

    public boolean hasScissorClip() {
        return this.mScissorX0 > 0 || this.mScissorY0 > 0 || this.mScissorX1 < this.mScreenWidth || this.mScissorY1 < this.mScreenHeight;
    }

    public int getScissorX0() {
        return this.mScissorX0;
    }

    public int getScissorY0() {
        return this.mScissorY0;
    }

    public int getScissorX1() {
        return this.mScissorX1;
    }

    public int getScissorY1() {
        return this.mScissorY1;
    }

    public void setStencil(int seq) {
        assert this.mStencilSeq == 0;
        this.mStencilSeq = seq;
    }

    public boolean hasStencilClip() {
        return this.mStencilSeq != 0;
    }

    public int getStencilSeq() {
        return this.mStencilSeq;
    }

    public boolean hasClip() {
        return this.hasScissorClip() || this.hasStencilClip();
    }

    public int hashCode() {
        int h = this.mStencilSeq;
        h = 31 * h + this.mScissorX0;
        h = 31 * h + this.mScissorY0;
        h = 31 * h + this.mScissorX1;
        h = 31 * h + this.mScissorY1;
        h = 31 * h + this.mScreenWidth;
        return 31 * h + this.mScreenHeight;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ClipResult other = (ClipResult) o;
            return this.mStencilSeq == other.mStencilSeq && this.mScissorX0 == other.mScissorX0 && this.mScissorY0 == other.mScissorY0 && this.mScissorX1 == other.mScissorX1 && this.mScissorY1 == other.mScissorY1 && this.mScreenWidth == other.mScreenWidth && this.mScreenHeight == other.mScreenHeight;
        } else {
            return false;
        }
    }

    public ClipResult clone() {
        try {
            return (ClipResult) super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new InternalError(var2);
        }
    }
}