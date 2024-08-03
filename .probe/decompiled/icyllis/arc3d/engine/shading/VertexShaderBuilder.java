package icyllis.arc3d.engine.shading;

import icyllis.arc3d.core.SLDataType;
import icyllis.arc3d.engine.GeometryProcessor;
import icyllis.arc3d.engine.ShaderVar;
import java.util.ArrayList;

public class VertexShaderBuilder extends ShaderBuilderBase implements VertexGeomBuilder {

    private final ArrayList<ShaderVar> mInputs = new ArrayList();

    public VertexShaderBuilder(PipelineBuilder pipelineBuilder) {
        super(pipelineBuilder);
    }

    @Override
    protected void onFinish() {
        int locationIndex = 0;
        for (ShaderVar var : this.mInputs) {
            String location = "location = " + locationIndex;
            var.addLayoutQualifier(location);
            int locations = SLDataType.locations(var.getType());
            assert locations > 0;
            assert !var.isArray();
            locationIndex += locations;
        }
        this.mPipelineBuilder.uniformHandler().appendUniformDecls(1, this.uniforms());
        this.mPipelineBuilder.appendDecls(this.mInputs, this.inputs());
        this.mPipelineBuilder.varyingHandler().getVertDecls(this.outputs());
    }

    @Override
    public void emitAttributes(GeometryProcessor geomProc) {
        for (GeometryProcessor.Attribute attr : geomProc.vertexAttributes()) {
            ShaderVar var = attr.asShaderVar();
            assert var.getTypeModifier() == 2;
            this.mInputs.add(var);
        }
        for (GeometryProcessor.Attribute attr : geomProc.instanceAttributes()) {
            ShaderVar var = attr.asShaderVar();
            assert var.getTypeModifier() == 2;
            this.mInputs.add(var);
        }
    }

    @Override
    public void emitNormalizedPosition(ShaderVar devicePos) {
        if (devicePos.getType() == 15) {
            this.codeAppendf("gl_Position = vec4(%1$s.xy * %2$s.xz + %1$s.zz * %2$s.yw, 0.0, %1$s.z);\n", new Object[] { devicePos.getName(), "SV_Projection" });
        } else {
            assert devicePos.getType() == 14;
            this.codeAppendf("gl_Position = vec4(%1$s.xy * %2$s.xz + %2$s.yw, 0.0, 1.0);\n", new Object[] { devicePos.getName(), "SV_Projection" });
        }
    }
}