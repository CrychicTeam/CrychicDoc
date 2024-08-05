package icyllis.arc3d.engine.geom;

import icyllis.arc3d.engine.GeometryProcessor;
import icyllis.arc3d.engine.KeyBuilder;
import icyllis.arc3d.engine.ShaderCaps;
import icyllis.arc3d.engine.ShaderVar;
import icyllis.arc3d.engine.UniformDataManager;
import icyllis.arc3d.engine.shading.FPFragmentBuilder;
import icyllis.arc3d.engine.shading.UniformHandler;
import icyllis.arc3d.engine.shading.Varying;
import icyllis.arc3d.engine.shading.VaryingHandler;
import icyllis.arc3d.engine.shading.VertexGeomBuilder;
import javax.annotation.Nonnull;

public class SDFRoundRectGeoProc extends GeometryProcessor {

    public static final GeometryProcessor.Attribute POSITION = new GeometryProcessor.Attribute("Position", (byte) 1, (byte) 14);

    public static final GeometryProcessor.Attribute COLOR = new GeometryProcessor.Attribute("Color", (byte) 3, (byte) 16);

    public static final GeometryProcessor.Attribute LOCAL_RECT = new GeometryProcessor.Attribute("LocalRect", (byte) 3, (byte) 16);

    public static final GeometryProcessor.Attribute RADII = new GeometryProcessor.Attribute("Radii", (byte) 1, (byte) 14);

    public static final GeometryProcessor.Attribute MODEL_VIEW = new GeometryProcessor.Attribute("ModelView", (byte) 2, (byte) 18);

    public static final GeometryProcessor.AttributeSet VERTEX_ATTRIBS = GeometryProcessor.AttributeSet.makeImplicit(POSITION);

    public static final GeometryProcessor.AttributeSet INSTANCE_ATTRIBS = GeometryProcessor.AttributeSet.makeImplicit(COLOR, LOCAL_RECT, RADII, MODEL_VIEW);

    private final boolean mStroke;

    public SDFRoundRectGeoProc(boolean stroke) {
        super(3);
        this.mStroke = stroke;
        this.setVertexAttributes(1);
        this.setInstanceAttributes(15);
    }

    @Nonnull
    @Override
    public String name() {
        return "SDFRoundRect_GeomProc";
    }

    @Override
    public byte primitiveType() {
        return 4;
    }

    @Override
    public void appendToKey(@Nonnull KeyBuilder b) {
        b.addBool(this.mStroke, "stroke");
    }

    @Nonnull
    @Override
    public GeometryProcessor.ProgramImpl makeProgramImpl(ShaderCaps caps) {
        return new SDFRoundRectGeoProc.Impl();
    }

    @Override
    protected GeometryProcessor.AttributeSet allVertexAttributes() {
        return VERTEX_ATTRIBS;
    }

    @Override
    protected GeometryProcessor.AttributeSet allInstanceAttributes() {
        return INSTANCE_ATTRIBS;
    }

    private static class Impl extends GeometryProcessor.ProgramImpl {

        @Override
        public void setData(UniformDataManager manager, GeometryProcessor geomProc) {
        }

        @Override
        protected void onEmitCode(VertexGeomBuilder vertBuilder, FPFragmentBuilder fragBuilder, VaryingHandler varyingHandler, UniformHandler uniformHandler, ShaderCaps shaderCaps, GeometryProcessor geomProc, String outputColor, String outputCoverage, int[] texSamplers, ShaderVar localPos, ShaderVar worldPos) {
            boolean stroke = ((SDFRoundRectGeoProc) geomProc).mStroke;
            vertBuilder.emitAttributes(geomProc);
            Varying rectEdge = new Varying((byte) 14);
            varyingHandler.addVarying("RectEdge", rectEdge);
            vertBuilder.codeAppendf("vec2 rectEdge = (%s.xz + %s.y + 1.0) * %s;\n%s = rectEdge;\n", new Object[] { SDFRoundRectGeoProc.LOCAL_RECT.name(), SDFRoundRectGeoProc.RADII.name(), SDFRoundRectGeoProc.POSITION.name(), rectEdge.vsOut() });
            fragBuilder.codeAppendf("vec2 rectEdge = %s;\n", new Object[] { rectEdge.fsIn() });
            fragBuilder.codeAppendf("vec4 %s;\n", new Object[] { outputColor });
            varyingHandler.addPassThroughAttribute(SDFRoundRectGeoProc.COLOR, outputColor, 1);
            Varying sizeAndRadii = new Varying((byte) 16);
            varyingHandler.addVarying("SizeAndRadii", sizeAndRadii, 1);
            vertBuilder.codeAppendf("%s = vec4(%s.xz, %s);\n", new Object[] { sizeAndRadii.vsOut(), SDFRoundRectGeoProc.LOCAL_RECT.name(), SDFRoundRectGeoProc.RADII.name() });
            fragBuilder.codeAppendf("vec4 sizeAndRadii = %s;\n", new Object[] { sizeAndRadii.fsIn() });
            vertBuilder.codeAppendf("vec2 localPos = rectEdge + %s.yw;\n", new Object[] { SDFRoundRectGeoProc.LOCAL_RECT.name() });
            localPos.set("localPos", (byte) 14);
            writeWorldPosition(vertBuilder, localPos, SDFRoundRectGeoProc.MODEL_VIEW.name(), worldPos);
            if (stroke) {
                fragBuilder.codeAppend("vec2 q = abs(rectEdge) - sizeAndRadii.xy + sizeAndRadii.z;\nfloat d = min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - sizeAndRadii.z;\n");
            } else {
                fragBuilder.codeAppend("vec2 q = abs(rectEdge) - sizeAndRadii.xy + sizeAndRadii.z;\nfloat d = length(max(q, 0.0)) - sizeAndRadii.z;\n");
            }
            if (stroke) {
                fragBuilder.codeAppend("d = abs(d) - sizeAndRadii.w;\n");
            }
            fragBuilder.codeAppend("float afwidth = length(vec2(dFdx(d),dFdy(d)))*0.7;\nfloat edgeAlpha = 1.0 - smoothstep(-afwidth,afwidth,d);\n");
            fragBuilder.codeAppendf("vec4 %s = vec4(edgeAlpha);\n", new Object[] { outputCoverage });
        }
    }
}