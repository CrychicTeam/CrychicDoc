package icyllis.arc3d.engine.ops;

import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.engine.OpFlushState;
import icyllis.arc3d.engine.RecordingContext;
import icyllis.arc3d.engine.SurfaceView;
import javax.annotation.Nonnull;

public final class ClearOp extends Op {

    private static final int COLOR_BUFFER = 1;

    private static final int STENCIL_BUFFER = 2;

    private int mBuffer;

    private int mScissorLeft;

    private int mScissorTop;

    private int mScissorRight;

    private int mScissorBottom;

    private float mColorRed;

    private float mColorGreen;

    private float mColorBlue;

    private float mColorAlpha;

    private boolean mInsideMask;

    private ClearOp(int buffer, int scissorLeft, int scissorTop, int scissorRight, int scissorBottom, float colorRed, float colorGreen, float colorBlue, float colorAlpha, boolean insideMask) {
        this.mBuffer = buffer;
        this.mScissorLeft = scissorLeft;
        this.mScissorTop = scissorTop;
        this.mScissorRight = scissorRight;
        this.mScissorBottom = scissorBottom;
        this.mColorRed = colorRed;
        this.mColorGreen = colorGreen;
        this.mColorBlue = colorBlue;
        this.mColorAlpha = colorAlpha;
        this.mInsideMask = insideMask;
        this.set((float) scissorLeft, (float) scissorTop, (float) scissorRight, (float) scissorBottom);
        this.setBoundsFlags(false, false);
    }

    @Nonnull
    public static Op makeColor(int left, int top, int right, int bottom, float red, float green, float blue, float alpha) {
        return new ClearOp(1, left, top, right, bottom, red, green, blue, alpha, false);
    }

    @Nonnull
    public static Op makeStencil(int left, int top, int right, int bottom, boolean insideMask) {
        return new ClearOp(2, left, top, right, bottom, 0.0F, 0.0F, 0.0F, 0.0F, insideMask);
    }

    @Override
    public void onPrePrepare(RecordingContext context, SurfaceView writeView, int pipelineFlags) {
    }

    @Override
    public void onPrepare(OpFlushState state, SurfaceView writeView, int pipelineFlags) {
    }

    @Override
    public void onExecute(OpFlushState state, Rect2f chainBounds) {
        if ((this.mBuffer & 1) != 0) {
            state.getOpsRenderPass().clearColor(this.mScissorLeft, this.mScissorTop, this.mScissorRight, this.mScissorBottom, this.mColorRed, this.mColorGreen, this.mColorBlue, this.mColorAlpha);
        }
        if ((this.mBuffer & 2) != 0) {
            state.getOpsRenderPass().clearStencil(this.mScissorLeft, this.mScissorTop, this.mScissorRight, this.mScissorBottom, this.mInsideMask);
        }
    }
}