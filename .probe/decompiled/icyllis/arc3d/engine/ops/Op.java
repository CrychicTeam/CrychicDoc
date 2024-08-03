package icyllis.arc3d.engine.ops;

import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.engine.OpFlushState;
import icyllis.arc3d.engine.RecordingContext;
import icyllis.arc3d.engine.SurfaceView;
import icyllis.arc3d.engine.TextureVisitor;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Op extends Rect2f {

    private static final int BoundsFlag_AABloat = 1;

    private static final int BoundsFlag_ZeroArea = 2;

    private Op mNextInChain;

    private Op mPrevInChain;

    private int mBoundsFlags;

    public void visitProxies(TextureVisitor func) {
    }

    public final boolean mayChain(@Nonnull Op op) {
        assert op != this;
        if (this.getClass() != op.getClass()) {
            return false;
        } else {
            boolean result = this.onMayChain(op);
            if (result) {
                this.joinNoCheck(op);
                this.mBoundsFlags = this.mBoundsFlags | op.mBoundsFlags;
            }
            return result;
        }
    }

    protected boolean onMayChain(@Nonnull Op op) {
        return false;
    }

    protected final void setBoundsFlags(boolean aaBloat, boolean zeroArea) {
        this.mBoundsFlags = (aaBloat ? 1 : 0) | (zeroArea ? 2 : 0);
    }

    public final void setClippedBounds(Rect2f clippedBounds) {
        this.set(clippedBounds);
        this.mBoundsFlags = 0;
    }

    public final boolean hasAABloat() {
        return (this.mBoundsFlags & 1) != 0;
    }

    public final boolean hasZeroArea() {
        return (this.mBoundsFlags & 2) != 0;
    }

    public abstract void onPrePrepare(RecordingContext var1, SurfaceView var2, int var3);

    public abstract void onPrepare(OpFlushState var1, SurfaceView var2, int var3);

    public abstract void onExecute(OpFlushState var1, Rect2f var2);

    public final void chainConcat(@Nonnull Op next) {
        assert this.getClass() == next.getClass();
        assert this.isChainTail();
        assert next.isChainHead();
        this.mNextInChain = next;
        next.mPrevInChain = this;
    }

    public final boolean isChainHead() {
        return this.mPrevInChain == null;
    }

    public final boolean isChainTail() {
        return this.mNextInChain == null;
    }

    public final Op nextInChain() {
        return this.mNextInChain;
    }

    public final Op prevInChain() {
        return this.mPrevInChain;
    }

    @Nullable
    public final Op chainSplit() {
        Op next = this.mNextInChain;
        if (next != null) {
            next.mPrevInChain = null;
            this.mNextInChain = null;
            return next;
        } else {
            return null;
        }
    }

    public final boolean validateChain(Op expectedTail) {
        assert this.isChainHead();
        for (Op op = this; op != null; op = op.nextInChain()) {
            assert op == this || op.prevInChain() != null && op.prevInChain().nextInChain() == op;
            assert this.getClass() == op.getClass();
            if (op.nextInChain() != null) {
                assert op.nextInChain().prevInChain() == op;
                assert op != expectedTail;
            } else {
                assert expectedTail == null || op == expectedTail;
            }
        }
        return true;
    }
}