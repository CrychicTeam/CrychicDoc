package icyllis.arc3d.engine.shading;

import icyllis.arc3d.engine.ShaderVar;
import javax.annotation.Nullable;

public class FragmentShaderBuilder extends ShaderBuilderBase implements FPFragmentBuilder, XPFragmentBuilder {

    public static final int MAIN_DRAW_BUFFER_INDEX = 0;

    public static final int PRIMARY_COLOR_OUTPUT_INDEX = 0;

    public static final int SECONDARY_COLOR_OUTPUT_INDEX = 1;

    public static final String PRIMARY_COLOR_OUTPUT_NAME = "FragColor0";

    public static final String SECONDARY_COLOR_OUTPUT_NAME = "FragColor1";

    private final ShaderVar mPrimaryOutput;

    private ShaderVar mSecondaryOutput;

    public FragmentShaderBuilder(PipelineBuilder pipelineBuilder) {
        super(pipelineBuilder);
        String layoutQualifier = "location = 0";
        this.mPrimaryOutput = new ShaderVar("FragColor0", (byte) 16, (byte) 1, 0, layoutQualifier, "");
        this.mPrimaryOutput.addLayoutQualifier("index = 0");
    }

    @Override
    protected void onFinish() {
        this.mPipelineBuilder.uniformHandler().appendUniformDecls(2, this.uniforms());
        this.mPipelineBuilder.varyingHandler().getFragDecls(this.inputs());
        this.mPrimaryOutput.appendDecl(this.outputs());
        this.outputs().append(";\n");
        if (this.mSecondaryOutput != null) {
            this.mSecondaryOutput.appendDecl(this.outputs());
            this.outputs().append(";\n");
        }
    }

    public void enableSecondaryOutput() {
        assert this.mSecondaryOutput == null;
        String layoutQualifier = "location = 0";
        this.mSecondaryOutput = new ShaderVar("FragColor1", (byte) 16, (byte) 1, 0, layoutQualifier, "");
        this.mSecondaryOutput.addLayoutQualifier("index = 1");
    }

    @Nullable
    public String getSecondaryColorOutputName() {
        return this.mSecondaryOutput == null ? null : "FragColor1";
    }
}