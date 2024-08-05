package software.bernie.geckolib.cache.object;

import org.joml.Vector3f;

public record GeoVertex(Vector3f position, float texU, float texV) {

    public GeoVertex(double x, double y, double z) {
        this(new Vector3f((float) x, (float) y, (float) z), 0.0F, 0.0F);
    }

    public GeoVertex withUVs(float texU, float texV) {
        return new GeoVertex(this.position, texU, texV);
    }
}