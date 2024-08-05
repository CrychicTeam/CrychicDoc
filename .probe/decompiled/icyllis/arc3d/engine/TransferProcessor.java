package icyllis.arc3d.engine;

import icyllis.arc3d.engine.shading.UniformHandler;
import icyllis.arc3d.engine.shading.XPFragmentBuilder;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public abstract class TransferProcessor extends Processor {

    protected final boolean mReadsDstColor;

    protected final boolean mIsLCDCoverage;

    protected TransferProcessor(int classID) {
        this(classID, false, false);
    }

    protected TransferProcessor(int classID, boolean readsDstColor, boolean isLCDCoverage) {
        super(classID);
        this.mReadsDstColor = readsDstColor;
        this.mIsLCDCoverage = isLCDCoverage;
    }

    public void addToKey(KeyBuilder b) {
    }

    @Nonnull
    public abstract TransferProcessor.ProgramImpl makeProgramImpl();

    @Nonnull
    public BlendInfo getBlendInfo() {
        assert this.readsDstColor();
        return BlendInfo.SRC;
    }

    public boolean hasSecondaryOutput() {
        assert this.readsDstColor();
        return false;
    }

    public final boolean readsDstColor() {
        return this.mReadsDstColor;
    }

    public final boolean isLCDCoverage() {
        return this.mIsLCDCoverage;
    }

    public abstract static class ProgramImpl {

        public final void emitCode(TransferProcessor.ProgramImpl.EmitArgs args) {
            if (!args.xferProc.readsDstColor()) {
                this.emitOutputsForBlendState(args);
            }
        }

        protected void emitOutputsForBlendState(TransferProcessor.ProgramImpl.EmitArgs args) {
            throw new UnsupportedOperationException("emitOutputsForBlendState not implemented");
        }

        protected void emitBlendCodeForDstRead(TransferProcessor.ProgramImpl.EmitArgs args) {
            throw new UnsupportedOperationException("emitBlendCodeForDstRead not implemented");
        }

        public static final class EmitArgs {

            public XPFragmentBuilder fragBuilder;

            public UniformHandler uniformHandler;

            public TransferProcessor xferProc;

            public String inputColor;

            public String inputCoverage;

            public String outputPrimary;

            public String outputSecondary;

            public EmitArgs(XPFragmentBuilder fragBuilder, UniformHandler uniformHandler, TransferProcessor xferProc, String inputColor, String inputCoverage, String outputPrimary, String outputSecondary) {
                this.fragBuilder = fragBuilder;
                this.uniformHandler = uniformHandler;
                this.xferProc = xferProc;
                this.inputColor = inputColor;
                this.inputCoverage = inputCoverage;
                this.outputPrimary = outputPrimary;
                this.outputSecondary = outputSecondary;
            }
        }
    }
}