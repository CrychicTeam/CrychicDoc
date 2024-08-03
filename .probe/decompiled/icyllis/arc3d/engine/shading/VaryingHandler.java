package icyllis.arc3d.engine.shading;

import icyllis.arc3d.core.SLDataType;
import icyllis.arc3d.engine.GeometryProcessor;
import icyllis.arc3d.engine.ShaderCaps;
import icyllis.arc3d.engine.ShaderVar;
import java.util.ArrayList;

public class VaryingHandler {

    public static final int kSmooth_Interpolation = 0;

    public static final int kCanBeFlat_Interpolation = 1;

    public static final int kRequiredToBeFlat_Interpolation = 2;

    protected final ArrayList<VaryingHandler.VaryingInfo> mVaryings = new ArrayList();

    protected final ArrayList<ShaderVar> mVertexOutputs = new ArrayList();

    protected final ArrayList<ShaderVar> mFragInputs = new ArrayList();

    protected final PipelineBuilder mPipelineBuilder;

    private String mDefaultInterpolationModifier = "";

    public VaryingHandler(PipelineBuilder pipelineBuilder) {
        this.mPipelineBuilder = pipelineBuilder;
    }

    public final void setNoPerspective() {
        this.mDefaultInterpolationModifier = "noperspective";
    }

    public final void addVarying(String name, Varying varying) {
        this.addVarying(name, varying, 0);
    }

    public final void addVarying(String name, Varying varying, int interpolation) {
        assert varying.mType != 0;
        assert SLDataType.isFloatType(varying.mType) || interpolation == 2;
        VaryingHandler.VaryingInfo v = new VaryingHandler.VaryingInfo();
        v.mType = varying.mType;
        v.mIsFlat = useFlatInterpolation(interpolation, this.mPipelineBuilder.shaderCaps());
        v.mVsOut = this.mPipelineBuilder.nameVariable('f', name);
        v.mVisibility = 0;
        if (varying.isInVertexShader()) {
            varying.mVsOut = v.mVsOut;
            v.mVisibility |= 1;
        }
        if (varying.isInFragmentShader()) {
            varying.mFsIn = v.mVsOut;
            v.mVisibility |= 2;
        }
        this.mVaryings.add(v);
    }

    public final void addPassThroughAttribute(GeometryProcessor.Attribute attr, String output) {
        this.addPassThroughAttribute(attr, output, 0);
    }

    public final void addPassThroughAttribute(GeometryProcessor.Attribute attr, String output, int interpolation) {
        assert attr.dstType() != 0;
        assert !output.isEmpty();
        Varying v = new Varying(attr.dstType());
        this.addVarying(attr.name(), v, interpolation);
        this.mPipelineBuilder.mVS.codeAppendf("%s = %s;\n", new Object[] { v.vsOut(), attr.name() });
        this.mPipelineBuilder.mFS.codeAppendf("%s = %s;\n", new Object[] { output, v.fsIn() });
    }

    private static boolean useFlatInterpolation(int interpolation, ShaderCaps shaderCaps) {
        return switch(interpolation) {
            case 0 ->
                false;
            case 1 ->
                shaderCaps.mPreferFlatInterpolation;
            case 2 ->
                true;
            default ->
                throw new AssertionError(interpolation);
        };
    }

    public final void finish() {
        int locationIndex = 0;
        for (VaryingHandler.VaryingInfo v : this.mVaryings) {
            String layoutQualifier;
            if (this.mPipelineBuilder.shaderCaps().mEnhancedLayouts) {
                layoutQualifier = "location = " + locationIndex;
                String extensionName = this.mPipelineBuilder.shaderCaps().mEnhancedLayoutsExtensionName;
                this.mPipelineBuilder.mVS.addExtension(extensionName);
                this.mPipelineBuilder.mFS.addExtension(extensionName);
            } else {
                layoutQualifier = "";
            }
            String modifier = v.mIsFlat ? "flat" : this.mDefaultInterpolationModifier;
            if ((v.mVisibility & 1) != 0) {
                this.mVertexOutputs.add(new ShaderVar(v.mVsOut, v.mType, (byte) 1, 0, layoutQualifier, modifier));
            }
            if ((v.mVisibility & 2) != 0) {
                String fsIn = v.mVsOut;
                this.mFragInputs.add(new ShaderVar(fsIn, v.mType, (byte) 2, 0, layoutQualifier, modifier));
            }
            int locations = SLDataType.locations(v.mType);
            assert locations > 0;
            locationIndex += locations;
        }
        this.onFinish();
    }

    protected void onFinish() {
    }

    public final void getVertDecls(StringBuilder outputDecls) {
        this.mPipelineBuilder.appendDecls(this.mVertexOutputs, outputDecls);
    }

    public final void getFragDecls(StringBuilder inputDecls) {
        this.mPipelineBuilder.appendDecls(this.mFragInputs, inputDecls);
    }

    protected static class VaryingInfo {

        public byte mType;

        public boolean mIsFlat;

        public String mVsOut;

        public int mVisibility;

        public VaryingInfo() {
        }
    }
}