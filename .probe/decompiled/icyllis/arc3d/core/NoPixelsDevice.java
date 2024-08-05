package icyllis.arc3d.core;

import javax.annotation.Nonnull;

public class NoPixelsDevice extends Device {

    private static final int CLIP_POOL_SIZE = 16;

    private NoPixelsDevice.ClipState[] mClipStack = new NoPixelsDevice.ClipState[16];

    private int mClipIndex = 0;

    private final Rect2i mTmpBounds = new Rect2i();

    public NoPixelsDevice(@Nonnull Rect2i bounds) {
        this(bounds.mLeft, bounds.mTop, bounds.mRight, bounds.mBottom);
    }

    public NoPixelsDevice(int left, int top, int right, int bottom) {
        super(new ImageInfo(right - left, bottom - top));
        this.setOrigin(null, left, top);
        NoPixelsDevice.ClipState state = new NoPixelsDevice.ClipState();
        state.setRect(this.mBounds);
        this.mClipStack[0] = state;
    }

    public final void resetForNextPicture(int left, int top, int right, int bottom) {
        this.resize(right - left, bottom - top);
        this.setOrigin(null, left, top);
        for (int i = this.mClipIndex; i > 0; i--) {
            this.pop();
        }
        NoPixelsDevice.ClipState state = this.mClipStack[0];
        state.setRect(this.mBounds);
        state.mDeferredSaveCount = 0;
    }

    @Nonnull
    private NoPixelsDevice.ClipState push() {
        int i = ++this.mClipIndex;
        NoPixelsDevice.ClipState[] stack = this.mClipStack;
        if (i == stack.length) {
            this.mClipStack = new NoPixelsDevice.ClipState[i + (i >> 1)];
            System.arraycopy(stack, 0, this.mClipStack, 0, i);
            stack = this.mClipStack;
        }
        NoPixelsDevice.ClipState state = stack[i];
        if (state == null) {
            stack[i] = state = new NoPixelsDevice.ClipState();
        }
        return state;
    }

    private void pop() {
        if (this.mClipIndex-- >= 16) {
            this.mClipStack[this.mClipIndex + 1] = null;
        }
    }

    @Nonnull
    private NoPixelsDevice.ClipState clip() {
        return this.mClipStack[this.mClipIndex];
    }

    @Nonnull
    private NoPixelsDevice.ClipState writableClip() {
        NoPixelsDevice.ClipState state = this.mClipStack[this.mClipIndex];
        if (state.mDeferredSaveCount > 0) {
            state.mDeferredSaveCount--;
            NoPixelsDevice.ClipState next = this.push();
            next.set(state);
            next.mDeferredSaveCount = 0;
            return next;
        } else {
            return state;
        }
    }

    @Override
    protected void onSave() {
        this.mClipStack[this.mClipIndex].mDeferredSaveCount++;
    }

    @Override
    protected void onRestore() {
        NoPixelsDevice.ClipState state = this.mClipStack[this.mClipIndex];
        if (state.mDeferredSaveCount > 0) {
            state.mDeferredSaveCount--;
        } else {
            this.pop();
        }
    }

    @Override
    public void clipRect(Rect2f rect, int clipOp, boolean doAA) {
        this.writableClip().opRect(rect, this.getLocalToDevice(), clipOp, doAA);
    }

    @Override
    protected void onReplaceClip(Rect2i globalRect) {
        Rect2i deviceRect = this.mTmpBounds;
        this.getGlobalToDevice().mapRect(globalRect, deviceRect);
        NoPixelsDevice.ClipState clip = this.writableClip();
        if (!deviceRect.intersect(this.mBounds)) {
            clip.setEmpty();
        } else {
            clip.setRect(deviceRect);
        }
    }

    @Override
    public boolean clipIsAA() {
        return this.clip().mIsAA;
    }

    @Override
    public boolean clipIsWideOpen() {
        return this.clip().mIsRect && this.getClipBounds().equals(this.mBounds);
    }

    @Override
    protected int getClipType() {
        NoPixelsDevice.ClipState clip = this.clip();
        if (clip.mClipBounds.isEmpty()) {
            return 0;
        } else {
            return clip.mIsRect ? 1 : 2;
        }
    }

    @Override
    protected Rect2ic getClipBounds() {
        return this.clip().getBounds();
    }

    @Override
    public void drawPaint(Paint paint) {
    }

    @Override
    public void drawRect(Rect2f r, Paint paint) {
    }

    private static final class ClipState {

        private final Rect2i mClipBounds = new Rect2i();

        private int mDeferredSaveCount = 0;

        private boolean mIsAA = false;

        private boolean mIsRect = true;

        ClipState() {
        }

        private void applyOpParams(int op, boolean aa, boolean rect) {
            this.mIsAA |= aa;
            this.mIsRect &= op == 1 && rect;
        }

        public void set(NoPixelsDevice.ClipState clip) {
            this.mClipBounds.set(clip.mClipBounds);
            this.mIsRect = clip.mIsRect;
            this.mIsAA = clip.mIsAA;
        }

        public Rect2ic getBounds() {
            return this.mClipBounds;
        }

        public void setEmpty() {
            this.mClipBounds.setEmpty();
            this.mIsRect = true;
            this.mIsAA = false;
        }

        public void setRect(int left, int top, int right, int bottom) {
            this.mClipBounds.set(left, top, right, bottom);
            this.mIsRect = true;
            this.mIsAA = false;
        }

        public void setRect(Rect2i r) {
            this.setRect(r.mLeft, r.mTop, r.mRight, r.mBottom);
        }

        public void opRect(Rect2f localRect, Matrix4 localToDevice, int clipOp, boolean doAA) {
            this.applyOpParams(clipOp, doAA, localToDevice.isScaleTranslate());
            switch(clipOp) {
                case 0:
                    return;
                case 1:
                    Rect2i deviceRect = new Rect2i();
                    if (doAA) {
                        localToDevice.mapRectOut(localRect, deviceRect);
                    } else {
                        localToDevice.mapRect(localRect, deviceRect);
                    }
                    this.opRect(deviceRect, clipOp);
                    return;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public void opRect(Rect2i deviceRect, int clipOp) {
            this.applyOpParams(clipOp, false, true);
            if (clipOp == 1) {
                if (!this.mClipBounds.intersect(deviceRect)) {
                    this.mClipBounds.setEmpty();
                }
            } else {
                if (clipOp == 0) {
                    if (this.mClipBounds.isEmpty()) {
                        return;
                    }
                    if (deviceRect.isEmpty() || !Rect2i.intersects(this.mClipBounds, deviceRect)) {
                        return;
                    }
                    if (deviceRect.contains(this.mClipBounds)) {
                        this.mClipBounds.setEmpty();
                        return;
                    }
                }
                throw new IllegalArgumentException();
            }
        }
    }
}