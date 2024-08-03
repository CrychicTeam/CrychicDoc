package noppes.npcs.shared.client.model.util;

import noppes.npcs.shared.common.util.NopVector2f;
import noppes.npcs.shared.common.util.NopVector3f;
import org.joml.Vector3f;

public class Vertex {

    public final Vector3f pos;

    public final NopVector2f texCoords;

    public final NopVector3f normal;

    public final NopVector3f tangent;

    public Vertex(Vector3f pos, NopVector2f texCoords, NopVector3f normal, NopVector3f tangent) {
        this.pos = pos;
        this.texCoords = texCoords;
        this.normal = normal;
        this.tangent = tangent;
    }

    public Vertex(Vector3f pos, NopVector2f texCoords) {
        this(pos, texCoords, NopVector3f.ZERO, NopVector3f.ZERO);
    }

    public Vertex(float x, float y, float z, float u, float v) {
        this(new Vector3f(x, y, z), new NopVector2f(u, v), NopVector3f.ZERO, NopVector3f.ZERO);
    }
}