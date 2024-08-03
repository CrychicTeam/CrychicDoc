package icyllis.arc3d.engine.effects;

import icyllis.arc3d.engine.BlendInfo;
import icyllis.arc3d.engine.TransferProcessor;
import icyllis.arc3d.engine.shading.XPFragmentBuilder;
import javax.annotation.Nonnull;

public class HardXferProc extends TransferProcessor {

    public static final HardXferProc SIMPLE_SRC_OVER = new HardXferProc(2, 0, false, new BlendInfo(0, 1, 7, 0.0F, 0.0F, 0.0F, 0.0F, true));

    private final BlendInfo mBlendInfo;

    private final int mPrimaryOutputType;

    private final int mSecondaryOutputType;

    public HardXferProc(int primaryOutputType, int secondaryOutputType, boolean isLCDCoverage, BlendInfo blendInfo) {
        super(6, false, isLCDCoverage);
        this.mPrimaryOutputType = primaryOutputType;
        this.mSecondaryOutputType = secondaryOutputType;
        this.mBlendInfo = blendInfo;
    }

    @Nonnull
    @Override
    public String name() {
        return "Hardware Transfer Processor";
    }

    @Nonnull
    @Override
    public TransferProcessor.ProgramImpl makeProgramImpl() {
        return new HardXferProc.Impl();
    }

    @Nonnull
    @Override
    public BlendInfo getBlendInfo() {
        return this.mBlendInfo;
    }

    @Override
    public boolean hasSecondaryOutput() {
        return this.mSecondaryOutputType != 0;
    }

    private static class Impl extends TransferProcessor.ProgramImpl {

        private static void appendOutput(XPFragmentBuilder fragBuilder, int outputType, String output, String inColor, String inCoverage) {
            switch(outputType) {
                case 0:
                    fragBuilder.codeAppendf("%s = vec4(0.0);", new Object[] { output });
                    break;
                case 1:
                    fragBuilder.codeAppendf("%s = %s;", new Object[] { output, inCoverage });
                    break;
                case 2:
                    fragBuilder.codeAppendf("%s = %s * %s;", new Object[] { output, inColor, inCoverage });
                    break;
                case 3:
                    fragBuilder.codeAppendf("%s = %s.a * %s;", new Object[] { output, inColor, inCoverage });
                    break;
                case 4:
                    fragBuilder.codeAppendf("%s = (1.0 - %s.a) * %s;", new Object[] { output, inColor, inCoverage });
                    break;
                case 5:
                    fragBuilder.codeAppendf("%s = (1.0 - %s) * %s;", new Object[] { output, inColor, inCoverage });
                    break;
                default:
                    throw new AssertionError("Unsupported output type.");
            }
        }

        @Override
        protected void emitOutputsForBlendState(TransferProcessor.ProgramImpl.EmitArgs args) {
            HardXferProc xp = (HardXferProc) args.xferProc;
            if (xp.hasSecondaryOutput()) {
                appendOutput(args.fragBuilder, xp.mSecondaryOutputType, args.outputSecondary, args.inputColor, args.inputCoverage);
            }
            appendOutput(args.fragBuilder, xp.mPrimaryOutputType, args.outputPrimary, args.inputColor, args.inputCoverage);
        }
    }
}