package icyllis.arc3d.engine.ops;

import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.engine.ClipResult;
import icyllis.arc3d.engine.Engine;
import icyllis.arc3d.engine.OpFlushState;
import icyllis.arc3d.engine.OpsRenderPass;
import icyllis.arc3d.engine.RecordingContext;
import icyllis.arc3d.engine.RenderTask;
import icyllis.arc3d.engine.RenderTaskManager;
import icyllis.arc3d.engine.SurfaceAllocator;
import icyllis.arc3d.engine.SurfaceProxy;
import icyllis.arc3d.engine.SurfaceView;
import icyllis.arc3d.engine.Swizzle;
import icyllis.arc3d.engine.TextureProxy;
import icyllis.arc3d.engine.TextureVisitor;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OpsTask extends RenderTask {

    public static final int STENCIL_CONTENT_DONT_CARE = 0;

    public static final int STENCIL_CONTENT_USER_BITS_CLEARED = 1;

    public static final int STENCIL_CONTENT_PRESERVED = 2;

    private final ArrayList<OpsTask.OpChain> mOpChains = new ArrayList(25);

    private final ObjectOpenHashSet<TextureProxy> mSampledTextures = new ObjectOpenHashSet();

    private final SurfaceView mWriteView;

    private int mPipelineFlags;

    private final Rect2f mTotalBounds = new Rect2f();

    private final Rect2i mContentBounds = new Rect2i();

    private byte mColorLoadOp = 0;

    private int mInitialStencilContent = 0;

    private final float[] mLoadClearColor = new float[4];

    public OpsTask(@Nonnull RenderTaskManager drawingMgr, @Nonnull SurfaceView writeView) {
        super(drawingMgr);
        this.mWriteView = writeView;
        this.addTarget(writeView.refSurface());
    }

    public void setColorLoadOp(byte loadOp, float red, float green, float blue, float alpha) {
        this.mColorLoadOp = loadOp;
        this.mLoadClearColor[0] = red;
        this.mLoadClearColor[1] = green;
        this.mLoadClearColor[2] = blue;
        this.mLoadClearColor[3] = alpha;
        Swizzle.apply(this.mWriteView.getSwizzle(), this.mLoadClearColor);
        if (loadOp == 1) {
            SurfaceProxy target = this.getTarget();
            this.mTotalBounds.set(0.0F, 0.0F, (float) target.getBackingWidth(), (float) target.getBackingHeight());
        }
    }

    public void setInitialStencilContent(int stencilContent) {
        this.mInitialStencilContent = stencilContent;
    }

    @Override
    public void gatherSurfaceIntervals(SurfaceAllocator alloc) {
        if (!this.mOpChains.isEmpty()) {
            int cur = alloc.curOp();
            alloc.addInterval(this.getTarget(), cur, cur + this.mOpChains.size() - 1, true);
            TextureVisitor gather = (p, __) -> alloc.addInterval(p, alloc.curOp(), alloc.curOp(), true);
            for (OpsTask.OpChain chain : this.mOpChains) {
                chain.visitProxies(gather);
                alloc.incOps();
            }
        } else {
            alloc.addInterval(this.getTarget(), alloc.curOp(), alloc.curOp(), true);
            alloc.incOps();
        }
    }

    @Override
    public void prepare(OpFlushState flushState) {
        for (OpsTask.OpChain chain : this.mOpChains) {
            if (chain.mHead != null) {
                int pipelineFlags = this.mPipelineFlags;
                if (chain.getClipState() != null) {
                    if (chain.getClipState().hasScissorClip()) {
                        pipelineFlags |= 8;
                    }
                    if (chain.getClipState().hasStencilClip()) {
                        pipelineFlags |= 16;
                    }
                }
                chain.mHead.onPrepare(flushState, this.mWriteView, pipelineFlags);
            }
        }
    }

    @Override
    public boolean execute(OpFlushState flushState) {
        assert this.numTargets() == 1;
        SurfaceProxy target = this.getTarget();
        assert target != null && target == this.mWriteView.getSurface();
        OpsRenderPass opsRenderPass = flushState.beginOpsRenderPass(this.mWriteView, this.mContentBounds, Engine.LoadStoreOps.make(this.mColorLoadOp, (byte) 0), (byte) 2, this.mLoadClearColor, this.mSampledTextures, this.mPipelineFlags);
        for (OpsTask.OpChain chain : this.mOpChains) {
            if (chain.mHead != null) {
                chain.mHead.onExecute(flushState, chain);
            }
        }
        opsRenderPass.end();
        return true;
    }

    @Override
    protected void onMakeClosed(RecordingContext context) {
        if (!this.mOpChains.isEmpty() || this.mColorLoadOp != 0) {
            SurfaceProxy target = this.getTarget();
            int rtHeight = target.getBackingHeight();
            Rect2f clippedContentBounds = new Rect2f(0.0F, 0.0F, (float) target.getBackingWidth(), (float) rtHeight);
            boolean result = clippedContentBounds.intersect(this.mTotalBounds);
            assert result;
            clippedContentBounds.roundOut(this.mContentBounds);
            TextureProxy proxy = target.asTexture();
            if (proxy != null) {
                if (proxy.isManualMSAAResolve()) {
                    int msaaTop;
                    int msaaBottom;
                    if (this.mWriteView.getOrigin() == 1) {
                        msaaTop = rtHeight - this.mContentBounds.mBottom;
                        msaaBottom = rtHeight - this.mContentBounds.mTop;
                    } else {
                        msaaTop = this.mContentBounds.mTop;
                        msaaBottom = this.mContentBounds.mBottom;
                    }
                    proxy.setResolveRect(this.mContentBounds.mLeft, msaaTop, this.mContentBounds.mRight, msaaBottom);
                }
                if (proxy.isMipmapped()) {
                    proxy.setMipmapsDirty(true);
                }
            }
        }
    }

    public void addOp(@Nonnull Op op) {
        this.recordOp(op, null, 0);
    }

    public void addDrawOp(@Nonnull DrawOp op, @Nullable ClipResult clip, int processorAnalysis) {
        TextureVisitor addDependency = (p, ss) -> {
            this.mSampledTextures.add(p);
            this.addDependency(p, ss);
        };
        op.visitProxies(addDependency);
        if ((processorAnalysis & 128) != 0) {
            this.mPipelineFlags |= 32;
        }
        this.recordOp(op, clip != null && clip.hasClip() ? clip : null, processorAnalysis);
    }

    void recordOp(@Nonnull Op op, @Nullable ClipResult clip, int processorAnalysis) {
        assert !this.isClosed();
        if (op.isFinite()) {
            this.mTotalBounds.join(op);
            int maxCandidates = Math.min(10, this.mOpChains.size());
            if (maxCandidates > 0) {
                int i = 0;
                OpsTask.OpChain candidate;
                do {
                    candidate = (OpsTask.OpChain) this.mOpChains.get(this.mOpChains.size() - 1 - i);
                    op = candidate.appendOp(op, clip, processorAnalysis);
                    if (op == null) {
                        return;
                    }
                } while (!Rect2f.rectsOverlap(candidate, op) && ++i != maxCandidates);
            }
            if (clip != null) {
                clip = clip.clone();
            }
            this.mOpChains.add(new OpsTask.OpChain(op, clip, processorAnalysis));
        }
    }

    private static class OpChain extends Rect2f {

        private Op mHead;

        private Op mTail;

        @Nullable
        private final ClipResult mClipResult;

        private final int mProcessorAnalysis;

        public OpChain(@Nonnull Op op, @Nullable ClipResult clipResult, int processorAnalysis) {
            this.mHead = op;
            this.mTail = op;
            this.mClipResult = clipResult;
            this.mProcessorAnalysis = processorAnalysis;
            this.set(op);
            assert this.validate();
        }

        public void visitProxies(TextureVisitor func) {
            for (Op op = this.mHead; op != null; op = op.nextInChain()) {
                op.visitProxies(func);
            }
        }

        @Nullable
        public ClipResult getClipState() {
            return this.mClipResult;
        }

        public void deleteOps() {
            while (this.mHead != null) {
                this.popHead();
            }
        }

        public Op popHead() {
            assert this.mHead != null;
            Op temp = this.mHead;
            this.mHead = this.mHead.chainSplit();
            if (this.mHead == null) {
                assert this.mTail == temp;
                this.mTail = null;
            }
            return temp;
        }

        public Op appendOp(@Nonnull Op op, @Nullable ClipResult clipResult, int processorAnalysis) {
            assert op.isChainHead() && op.isChainTail();
            assert op.validateChain(op);
            assert this.mHead != null;
            if ((this.mProcessorAnalysis & 8) != (processorAnalysis & 8) || (this.mProcessorAnalysis & 8) != 0 && Rect2f.rectsTouchOrOverlap(this, op) || !Objects.equals(this.mClipResult, clipResult)) {
                return op;
            } else if (this.mHead.mayChain(op)) {
                this.mTail.chainConcat(op);
                this.mTail = this.mTail.nextInChain();
                this.joinNoCheck(op);
                assert this.validate();
                return null;
            } else {
                return op;
            }
        }

        private boolean validate() {
            if (this.mHead != null) {
                assert this.mTail != null;
                assert this.mHead.validateChain(this.mTail);
            }
            for (Op op = this.mHead; op != null; op = op.nextInChain()) {
                assert this.mLeft <= op.mLeft && this.mTop <= op.mTop && this.mRight >= op.mRight && this.mBottom >= op.mBottom;
            }
            return true;
        }
    }
}