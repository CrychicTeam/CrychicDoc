package noppes.npcs.shared.client.model.util;

import org.joml.Vector3f;

public class Polygon {

    public final Vector3f normal;

    public final Vertex[] vertexes;

    public Polygon(Vector3f normal, Vertex... vertexes) {
        this.normal = normal;
        this.vertexes = vertexes;
    }
}