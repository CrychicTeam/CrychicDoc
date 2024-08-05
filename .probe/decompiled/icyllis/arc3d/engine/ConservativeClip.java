package icyllis.arc3d.engine;

import icyllis.arc3d.core.Matrix4;
import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.core.Rect2i;

public final class ConservativeClip {

    private final Rect2i mBounds = new Rect2i();

    private boolean mIsRect = true;

    private boolean mIsAA = false;

    private void applyOpParams(int op, boolean aa, boolean rect) {
        this.mIsAA |= aa;
        this.mIsRect &= op == 1 && rect;
    }

    public void set(ConservativeClip clip) {
        this.mBounds.set(clip.mBounds);
        this.mIsRect = clip.mIsRect;
        this.mIsAA = clip.mIsAA;
    }

    public boolean isEmpty() {
        return this.mBounds.isEmpty();
    }

    public boolean isRect() {
        return this.mIsRect;
    }

    public boolean isAA() {
        return this.mIsAA;
    }

    public Rect2i getBounds() {
        return this.mBounds;
    }

    public void setEmpty() {
        this.mBounds.setEmpty();
        this.mIsRect = true;
        this.mIsAA = false;
    }

    public void setRect(int left, int top, int right, int bottom) {
        this.mBounds.set(left, top, right, bottom);
        this.mIsRect = true;
        this.mIsAA = false;
    }

    public void setRect(Rect2i r) {
        this.mBounds.set(r);
        this.mIsRect = true;
        this.mIsAA = false;
    }

    public void replace(Rect2i globalRect, Matrix4 globalToDevice, Rect2i deviceBounds) {
        Rect2i deviceRect = new Rect2i();
        if (!deviceRect.intersect(deviceBounds)) {
            this.setEmpty();
        } else {
            this.setRect(deviceRect);
        }
    }

    public void opRect(Rect2f localRect, Matrix4 localToDevice, int clipOp, boolean doAA) {
        this.applyOpParams(clipOp, doAA, localToDevice.isScaleTranslate());
        switch(clipOp) {
            case 0:
                return;
            case 1:
                Rect2i deviceRect = new Rect2i();
                this.opRect(deviceRect, clipOp);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void opRect(Rect2i deviceRect, int clipOp) {
        this.applyOpParams(clipOp, false, true);
        if (clipOp == 1) {
            if (!this.mBounds.intersect(deviceRect)) {
                this.mBounds.setEmpty();
            }
        } else {
            if (clipOp == 0) {
                if (this.mBounds.isEmpty()) {
                    return;
                }
                if (deviceRect.isEmpty() || !Rect2i.intersects(this.mBounds, deviceRect)) {
                    return;
                }
                if (deviceRect.contains(this.mBounds)) {
                    this.mBounds.setEmpty();
                    return;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}