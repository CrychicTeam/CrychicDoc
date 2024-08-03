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

public class CircleProcessor extends GeometryProcessor {

    public static final GeometryProcessor.Attribute POSITION = new GeometryProcessor.Attribute("Position", (byte) 1, (byte) 14);

    public static final GeometryProcessor.Attribute CIRCLE_EDGE = new GeometryProcessor.Attribute("CircleEdge", (byte) 3, (byte) 16);

    public static final GeometryProcessor.Attribute COLOR = new GeometryProcessor.Attribute("Color", (byte) 3, (byte) 16);

    public static final GeometryProcessor.Attribute CLIP_PLANE = new GeometryProcessor.Attribute("ClipPlane", (byte) 2, (byte) 15);

    public static final GeometryProcessor.Attribute ISECT_PLANE = new GeometryProcessor.Attribute("IsectPlane", (byte) 2, (byte) 15);

    public static final GeometryProcessor.Attribute UNION_PLANE = new GeometryProcessor.Attribute("UnionPlane", (byte) 2, (byte) 15);

    public static final GeometryProcessor.Attribute ROUND_CAP_CENTERS = new GeometryProcessor.Attribute("RoundCapCenters", (byte) 3, (byte) 16);

    public static final GeometryProcessor.Attribute MODEL_VIEW = new GeometryProcessor.Attribute("ModelView", (byte) 2, (byte) 18);

    public static final GeometryProcessor.AttributeSet VERTEX_FORMAT = GeometryProcessor.AttributeSet.makeImplicit(POSITION, CIRCLE_EDGE);

    public static final GeometryProcessor.AttributeSet INSTANCE_FORMAT = GeometryProcessor.AttributeSet.makeImplicit(COLOR, CLIP_PLANE, ISECT_PLANE, UNION_PLANE, ROUND_CAP_CENTERS, MODEL_VIEW);

    private final int mFlags;

    public CircleProcessor(boolean stroke, boolean clipPlane, boolean isectPlane, boolean unionPlane, boolean roundCaps) {
        super(2);
        assert !roundCaps || stroke && clipPlane;
        int instanceMask = (clipPlane ? 2 : 0) | (isectPlane ? 4 : 0) | (unionPlane ? 8 : 0) | (roundCaps ? 16 : 0);
        this.mFlags = (stroke ? 1 : 0) | instanceMask;
        this.setVertexAttributes(3);
        this.setInstanceAttributes(1 | instanceMask | 32);
    }

    @Nonnull
    @Override
    public String name() {
        return "Circle_GeometryProcessor";
    }

    @Override
    public byte primitiveType() {
        return 3;
    }

    @Override
    public void appendToKey(@Nonnull KeyBuilder b) {
        b.addBits(5, this.mFlags, "stroke|clipPlane|isectPlane|unionPlane|roundCaps");
    }

    @Nonnull
    @Override
    public GeometryProcessor.ProgramImpl makeProgramImpl(ShaderCaps caps) {
        return new CircleProcessor.Impl();
    }

    @Override
    protected GeometryProcessor.AttributeSet allVertexAttributes() {
        return VERTEX_FORMAT;
    }

    @Override
    protected GeometryProcessor.AttributeSet allInstanceAttributes() {
        return INSTANCE_FORMAT;
    }

    private static class Impl extends GeometryProcessor.ProgramImpl {

        @Override
        public void setData(UniformDataManager manager, GeometryProcessor geomProc) {
        }

        @Override
        protected void onEmitCode(VertexGeomBuilder vertBuilder, FPFragmentBuilder fragBuilder, VaryingHandler varyingHandler, UniformHandler uniformHandler, ShaderCaps shaderCaps, GeometryProcessor geomProc, String outputColor, String outputCoverage, int[] texSamplers, ShaderVar localPos, ShaderVar worldPos) {
            int flags = ((CircleProcessor) geomProc).mFlags;
            boolean stroke = (flags & 1) != 0;
            boolean clipPlane = (flags & 2) != 0;
            boolean isectPlane = (flags & 4) != 0;
            boolean unionPlane = (flags & 8) != 0;
            boolean roundCaps = (flags & 16) != 0;
            vertBuilder.emitAttributes(geomProc);
            fragBuilder.codeAppend("vec4 circleEdge;\n");
            varyingHandler.addPassThroughAttribute(CircleProcessor.CIRCLE_EDGE, "circleEdge");
            if (clipPlane) {
                fragBuilder.codeAppend("vec3 clipPlane;\n");
                varyingHandler.addPassThroughAttribute(CircleProcessor.CLIP_PLANE, "clipPlane", 1);
            }
            if (isectPlane) {
                fragBuilder.codeAppend("vec3 isectPlane;\n");
                varyingHandler.addPassThroughAttribute(CircleProcessor.ISECT_PLANE, "isectPlane", 1);
            }
            if (unionPlane) {
                fragBuilder.codeAppend("vec3 unionPlane;\n");
                varyingHandler.addPassThroughAttribute(CircleProcessor.UNION_PLANE, "unionPlane", 1);
            }
            Varying capRadius = new Varying((byte) 13);
            if (roundCaps) {
                fragBuilder.codeAppend("vec4 roundCapCenters;\n");
                varyingHandler.addPassThroughAttribute(CircleProcessor.ROUND_CAP_CENTERS, "roundCapCenters", 1);
                varyingHandler.addVarying("CapRadius", capRadius, 1);
                vertBuilder.codeAppendf("%s = (1.0 - %s.w) / 2.0;\n", new Object[] { capRadius.vsOut(), CircleProcessor.CIRCLE_EDGE.name() });
            }
            fragBuilder.codeAppendf("vec4 %s;\n", new Object[] { outputColor });
            varyingHandler.addPassThroughAttribute(CircleProcessor.COLOR, outputColor, 1);
            localPos.set(CircleProcessor.POSITION.name(), CircleProcessor.POSITION.dstType());
            writeWorldPosition(vertBuilder, localPos, CircleProcessor.MODEL_VIEW.name(), worldPos);
            fragBuilder.codeAppend("float d = length(circleEdge.xy);\nfloat distanceToOuterEdge = circleEdge.z * (1.0 - d);\nfloat edgeAlpha = clamp(distanceToOuterEdge, 0.0, 1.0);\n");
            if (stroke) {
                fragBuilder.codeAppend("float distanceToInnerEdge = circleEdge.z * (d - circleEdge.w);\nfloat innerAlpha = clamp(distanceToInnerEdge, 0.0, 1.0);\nedgeAlpha *= innerAlpha;\n");
            }
            if (clipPlane) {
                fragBuilder.codeAppend("float clip = clamp(circleEdge.z * dot(circleEdge.xy,\n        clipPlane.xy) + clipPlane.z, 0.0, 1.0);\n");
                if (isectPlane) {
                    fragBuilder.codeAppend("clip *= clamp(circleEdge.z * dot(circleEdge.xy,\n        isectPlane.xy) + isectPlane.z, 0.0, 1.0);\n");
                }
                if (unionPlane) {
                    fragBuilder.codeAppend("clip = clamp(clip + clamp(circleEdge.z * dot(circleEdge.xy,\n        unionPlane.xy) + unionPlane.z, 0.0, 1.0), 0.0, 1.0);\n");
                }
                fragBuilder.codeAppend("edgeAlpha *= clip;\n");
                if (roundCaps) {
                    fragBuilder.codeAppendf("float dcap1 = circleEdge.z * (%s - length(circleEdge.xy -\n                                          roundCapCenters.xy));\nfloat dcap2 = circleEdge.z * (%s - length(circleEdge.xy -\n                                          roundCapCenters.zw));\nfloat capAlpha = (1.0 - clip) * (max(dcap1, 0.0) + max(dcap2, 0.0));\nedgeAlpha = min(edgeAlpha + capAlpha, 1.0);\n", new Object[] { capRadius.fsIn(), capRadius.fsIn() });
                }
            }
            fragBuilder.codeAppendf("vec4 %s = vec4(edgeAlpha);\n", new Object[] { outputCoverage });
        }
    }
}