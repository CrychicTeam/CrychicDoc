package noppes.npcs.shared.client.model.util;

import noppes.npcs.shared.common.util.NopVector3f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL21;

public class Mesh {

    public int[] indices;

    public Vertex[] vertices;

    public NopVector3f[] normals;

    private int vbo = -1;

    private static NopVector3f calcNormal(Vector3f v1, Vector3f v2, Vector3f v3) {
        NopVector3f calU = new NopVector3f(v2.x() - v1.x(), v2.y() - v1.y(), v2.z() - v1.z());
        NopVector3f calV = new NopVector3f(v3.x() - v1.x(), v3.y() - v1.y(), v3.z() - v1.z());
        NopVector3f output = new NopVector3f(calU.y * calV.z - calU.z * calV.y, calU.z * calV.x - calU.x * calV.z, calU.x * calV.y - calU.y * calV.x);
        return output.normalize();
    }

    public void delete() {
        if (this.vbo != -1) {
            GL21.glDeleteBuffers(this.vbo);
            this.vbo = -1;
        }
    }
}