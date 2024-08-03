package net.minecraft.client.model.geom.builders;

public class UVPair {

    private final float u;

    private final float v;

    public UVPair(float float0, float float1) {
        this.u = float0;
        this.v = float1;
    }

    public float u() {
        return this.u;
    }

    public float v() {
        return this.v;
    }

    public String toString() {
        return "(" + this.u + "," + this.v + ")";
    }
}