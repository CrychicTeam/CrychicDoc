package icyllis.arc3d.engine.ops;

import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.engine.GeometryProcessor;
import icyllis.arc3d.engine.KeyBuilder;
import icyllis.arc3d.engine.MeshDrawTarget;
import icyllis.arc3d.engine.OpFlushState;
import icyllis.arc3d.engine.PipelineInfo;
import icyllis.arc3d.engine.ShaderCaps;
import icyllis.arc3d.engine.ShaderVar;
import icyllis.arc3d.engine.SurfaceView;
import icyllis.arc3d.engine.UniformDataManager;
import icyllis.arc3d.engine.shading.FPFragmentBuilder;
import icyllis.arc3d.engine.shading.UniformHandler;
import icyllis.arc3d.engine.shading.VaryingHandler;
import icyllis.arc3d.engine.shading.VertexGeomBuilder;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public final class CircularRRectOp extends MeshDrawOp {

    @NotNull
    @Override
    protected PipelineInfo onCreatePipelineInfo(SurfaceView writeView, int pipelineFlags) {
        return null;
    }

    @Override
    protected void onPrepareDraws(MeshDrawTarget target) {
    }

    @Override
    public void onExecute(OpFlushState state, Rect2f chainBounds) {
    }

    private static class Processor extends GeometryProcessor {

        private static final GeometryProcessor.Attribute POS = new GeometryProcessor.Attribute("Pos", (byte) 1, (byte) 14);

        public Processor() {
            super(1);
        }

        @Nonnull
        @Override
        public String name() {
            return "CircularRRect_GeometryProcessor";
        }

        @Override
        public byte primitiveType() {
            return 0;
        }

        @Override
        public void appendToKey(@Nonnull KeyBuilder b) {
        }

        @Nonnull
        @Override
        public GeometryProcessor.ProgramImpl makeProgramImpl(ShaderCaps caps) {
            return new CircularRRectOp.Processor.Impl();
        }

        @Nullable
        @Override
        protected GeometryProcessor.AttributeSet allVertexAttributes() {
            return null;
        }

        @Nullable
        @Override
        protected GeometryProcessor.AttributeSet allInstanceAttributes() {
            return null;
        }

        private static class Impl extends GeometryProcessor.ProgramImpl {

            private int mSizeUniform;

            private int mRadiusUniform;

            @Override
            public void setData(UniformDataManager manager, GeometryProcessor geomProc) {
            }

            @Override
            protected void onEmitCode(VertexGeomBuilder vertBuilder, FPFragmentBuilder fragBuilder, VaryingHandler varyingHandler, UniformHandler uniformHandler, ShaderCaps shaderCaps, GeometryProcessor geomProc, String outputColor, String outputCoverage, int[] texSamplers, ShaderVar localPos, ShaderVar worldPos) {
                vertBuilder.emitAttributes(geomProc);
                varyingHandler.addPassThroughAttribute(CircularRRectOp.Processor.POS, "p");
                String sizeUniformName = uniformHandler.getUniformName(this.mSizeUniform = uniformHandler.addUniform(geomProc, 2, (byte) 14, "Size"));
                String radiusUniformName = uniformHandler.getUniformName(this.mRadiusUniform = uniformHandler.addUniform(geomProc, 2, (byte) 13, "Radius"));
                fragBuilder.codeAppendf("vec2 q = abs(p) - %1$s + %2$s;\nfloat d = min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - %2$s;\n", new Object[] { sizeUniformName, radiusUniformName });
                localPos.set(CircularRRectOp.Processor.POS.name(), CircularRRectOp.Processor.POS.dstType());
            }
        }
    }
}