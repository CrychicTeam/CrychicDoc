package icyllis.arc3d.engine.shading;

import icyllis.arc3d.engine.GeometryProcessor;
import icyllis.arc3d.engine.ShaderVar;

public interface VertexGeomBuilder extends ShaderBuilder {

    void emitAttributes(GeometryProcessor var1);

    void emitNormalizedPosition(ShaderVar var1);
}