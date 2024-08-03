package net.minecraft.client.model.geom.builders;

public class CubeDeformation {

    public static final CubeDeformation NONE = new CubeDeformation(0.0F);

    final float growX;

    final float growY;

    final float growZ;

    public CubeDeformation(float float0, float float1, float float2) {
        this.growX = float0;
        this.growY = float1;
        this.growZ = float2;
    }

    public CubeDeformation(float float0) {
        this(float0, float0, float0);
    }

    public CubeDeformation extend(float float0) {
        return new CubeDeformation(this.growX + float0, this.growY + float0, this.growZ + float0);
    }

    public CubeDeformation extend(float float0, float float1, float float2) {
        return new CubeDeformation(this.growX + float0, this.growY + float1, this.growZ + float2);
    }
}