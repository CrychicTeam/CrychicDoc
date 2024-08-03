package me.jellysquid.mods.sodium.client.gl.tessellation;

public enum GlPrimitiveType {

    TRIANGLES(4);

    private final int id;

    private GlPrimitiveType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}