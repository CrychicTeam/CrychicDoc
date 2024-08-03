package icyllis.arc3d.engine.shading;

import icyllis.arc3d.engine.Caps;
import icyllis.arc3d.engine.GeometryProcessor;
import icyllis.arc3d.engine.PipelineDesc;
import icyllis.arc3d.engine.PipelineInfo;
import icyllis.arc3d.engine.ShaderCaps;
import icyllis.arc3d.engine.ShaderVar;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PipelineBuilder {

    private int mStageIndex = -1;

    private final IntArrayList mSubstageIndices = new IntArrayList();

    public VertexShaderBuilder mVS;

    public FragmentShaderBuilder mFS;

    public final PipelineDesc mDesc;

    public final PipelineInfo mPipelineInfo;

    public int mProjectionUniform = -1;

    public GeometryProcessor.ProgramImpl mGPImpl;

    private int mNumFragmentSamplers;

    public PipelineBuilder(PipelineDesc desc, PipelineInfo pipelineInfo) {
        this.mDesc = desc;
        this.mPipelineInfo = pipelineInfo;
        this.mVS = new VertexShaderBuilder(this);
        this.mFS = new FragmentShaderBuilder(this);
    }

    public abstract Caps caps();

    public final ShaderCaps shaderCaps() {
        return this.caps().shaderCaps();
    }

    public final String nameVariable(char prefix, String name) {
        return this.nameVariable(prefix, name, true);
    }

    public final String nameVariable(char prefix, String name, boolean mangle) {
        String out;
        if (prefix == 0) {
            out = name;
        } else if (name.startsWith("_")) {
            out = prefix + "_x" + name;
        } else {
            out = prefix + "_" + name;
        }
        if (mangle) {
            String suffix = this.getMangleSuffix();
            if (out.endsWith("_")) {
                out = out + "x" + suffix;
            } else {
                out = out + suffix;
            }
        }
        assert !out.contains("__");
        return out;
    }

    public abstract UniformHandler uniformHandler();

    public abstract VaryingHandler varyingHandler();

    public final void addExtension(int shaderFlags, @Nullable String extensionName) {
        if (extensionName != null) {
            if ((shaderFlags & 1) != 0) {
                this.mVS.addExtension(extensionName);
            }
            if ((shaderFlags & 2) != 0) {
                this.mFS.addExtension(extensionName);
            }
        }
    }

    protected final boolean emitAndInstallProcs() {
        String[] input = new String[2];
        if (!this.emitAndInstallGeomProc(input)) {
            return false;
        } else if (!this.emitAndInstallFragProcs(input)) {
            return false;
        } else {
            this.mFS.codeAppendf("%s = %s * %s;\n", new Object[] { "FragColor0", input[0], input[1] });
            return true;
        }
    }

    private void advanceStage() {
        this.mStageIndex++;
        this.mFS.nextStage();
    }

    @Nonnull
    private String getMangleSuffix() {
        assert this.mStageIndex >= 0;
        StringBuilder suffix = new StringBuilder("_S").append(this.mStageIndex);
        IntListIterator var2 = this.mSubstageIndices.iterator();
        while (var2.hasNext()) {
            int c = (Integer) var2.next();
            suffix.append("_c").append(c);
        }
        return suffix.toString();
    }

    private boolean emitAndInstallGeomProc(String[] output) {
        GeometryProcessor geomProc = this.mPipelineInfo.geomProc();
        this.advanceStage();
        if (output[0] == null) {
            output[0] = this.nameVariable('\u0000', "outputColor");
        }
        if (output[1] == null) {
            output[1] = this.nameVariable('\u0000', "outputCoverage");
        }
        assert this.mProjectionUniform == -1;
        this.mProjectionUniform = this.uniformHandler().addUniform(null, 1, (byte) 16, "SV_Projection");
        this.mFS.codeAppendf("// Stage %d, %s\n", new Object[] { this.mStageIndex, geomProc.name() });
        this.mVS.codeAppendf("// Geometry Processor %s\n", new Object[] { geomProc.name() });
        assert this.mGPImpl == null;
        this.mGPImpl = geomProc.makeProgramImpl(this.shaderCaps());
        int[] texSamplers = new int[geomProc.numTextureSamplers()];
        for (int i = 0; i < texSamplers.length; i++) {
            String name = "TextureSampler" + i;
            texSamplers[i] = this.emitSampler(geomProc.textureSamplerState(i), geomProc.textureSamplerSwizzle(i), name);
            if (texSamplers[i] == -1) {
                return false;
            }
        }
        this.mGPImpl.emitCode(this.mVS, this.mFS, this.varyingHandler(), this.uniformHandler(), this.shaderCaps(), geomProc, output[0], output[1], texSamplers);
        return true;
    }

    private boolean emitAndInstallFragProcs(String[] input) {
        return true;
    }

    private int emitSampler(int samplerState, short swizzle, String name) {
        this.mNumFragmentSamplers++;
        return this.uniformHandler().addSampler(samplerState, swizzle, name);
    }

    void appendDecls(ArrayList<ShaderVar> vars, StringBuilder out) {
        for (ShaderVar var : vars) {
            var.appendDecl(out);
            out.append(";\n");
        }
    }
}