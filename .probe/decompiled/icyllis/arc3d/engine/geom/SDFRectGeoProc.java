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

public class SDFRectGeoProc extends GeometryProcessor {

    public static final GeometryProcessor.Attribute POSITION = new GeometryProcessor.Attribute("Position", (byte) 1, (byte) 14);

    public static final GeometryProcessor.Attribute COLOR = new GeometryProcessor.Attribute("Color", (byte) 17, (byte) 16);

    public static final GeometryProcessor.Attribute BOX = new GeometryProcessor.Attribute("Box", (byte) 3, (byte) 16);

    public static final GeometryProcessor.Attribute STROKE = new GeometryProcessor.Attribute("Stroke", (byte) 1, (byte) 14);

    public static final GeometryProcessor.Attribute VIEW_MATRIX = new GeometryProcessor.Attribute("ViewMatrix", (byte) 2, (byte) 18);

    public static final GeometryProcessor.AttributeSet VERTEX_ATTRIBS = GeometryProcessor.AttributeSet.makeImplicit(POSITION);

    public static final GeometryProcessor.AttributeSet INSTANCE_ATTRIBS = GeometryProcessor.AttributeSet.makeImplicit(COLOR, BOX, STROKE, VIEW_MATRIX);

    public static final int FLAG_ANTIALIASING = 1;

    public static final int FLAG_STROKE = 2;

    public static final int FLAG_INSTANCED_MATRIX = 4;

    private final int mFlags;

    public SDFRectGeoProc(int flags) {
        super(5);
        this.mFlags = flags;
        this.setVertexAttributes(1);
        this.setInstanceAttributes(3 | (flags & 6) << 1);
    }

    @Nonnull
    @Override
    public String name() {
        return "SDFRect_GeomProc";
    }

    @Override
    public byte primitiveType() {
        return 4;
    }

    @Override
    public void appendToKey(@Nonnull KeyBuilder b) {
        b.addBits(3, this.mFlags, "gpFlags");
    }

    @Nonnull
    @Override
    public GeometryProcessor.ProgramImpl makeProgramImpl(ShaderCaps caps) {
        return new SDFRectGeoProc.Impl();
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
            SDFRectGeoProc gp = (SDFRectGeoProc) geomProc;
            boolean gpAA = (gp.mFlags & 1) != 0;
            boolean gpStroke = (gp.mFlags & 2) != 0;
            boolean gpMatrix = (gp.mFlags & 4) != 0;
            vertBuilder.emitAttributes(geomProc);
            Varying rectEdge = new Varying((byte) 14);
            varyingHandler.addVarying("RectEdge", rectEdge);
            vertBuilder.codeAppendf("vec2 rectEdge = (%s.xz + %s.x - %s.y + 1.0) * %s;\n%s = rectEdge;\n", new Object[] { SDFRectGeoProc.BOX.name(), SDFRectGeoProc.STROKE.name(), SDFRectGeoProc.STROKE.name(), SDFRectGeoProc.POSITION.name(), rectEdge.vsOut() });
            fragBuilder.codeAppendf("vec2 rectEdge = %s;\n", new Object[] { rectEdge.fsIn() });
            fragBuilder.codeAppendf("vec4 %s;\n", new Object[] { outputColor });
            varyingHandler.addPassThroughAttribute(SDFRectGeoProc.COLOR, outputColor, 1);
            Varying sizeAndRadii = new Varying((byte) 16);
            varyingHandler.addVarying("SizeAndRadii", sizeAndRadii, 1);
            vertBuilder.codeAppendf("%s = vec4(%s.xz, %s);\n", new Object[] { sizeAndRadii.vsOut(), SDFRectGeoProc.BOX.name(), SDFRectGeoProc.STROKE.name() });
            fragBuilder.codeAppendf("vec4 sizeAndRadii = %s;\n", new Object[] { sizeAndRadii.fsIn() });
            vertBuilder.codeAppendf("vec2 localPos = rectEdge + %s.yw;\n", new Object[] { SDFRectGeoProc.BOX.name() });
            localPos.set("localPos", (byte) 14);
            if (gpMatrix) {
                writeWorldPosition(vertBuilder, localPos, SDFRectGeoProc.VIEW_MATRIX.name(), worldPos);
            } else {
                writePassthroughWorldPosition(vertBuilder, localPos, worldPos);
            }
            fragBuilder.codeAppend("vec2 q = abs(rectEdge) - sizeAndRadii.xy;\nfloat d = min(max(q.x, q.y), 0.0) + length(max(q, 0.0));\n");
            if (gpStroke) {
                fragBuilder.codeAppend("d = abs(d + sizeAndRadii.w) - sizeAndRadii.z;\n");
            }
            if (gpAA) {
                fragBuilder.codeAppend("float afwidth = length(vec2(dFdx(d),dFdy(d)))*0.7;\nfloat edgeAlpha = 1.0 - smoothstep(-afwidth,afwidth,d);\n");
            } else {
                fragBuilder.codeAppend("float edgeAlpha = step(d,0.0);\n");
            }
            fragBuilder.codeAppendf("vec4 %s = vec4(edgeAlpha);\n", new Object[] { outputCoverage });
        }
    }
}