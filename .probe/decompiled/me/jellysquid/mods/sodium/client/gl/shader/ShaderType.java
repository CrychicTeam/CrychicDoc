package me.jellysquid.mods.sodium.client.gl.shader;

public enum ShaderType {

    VERTEX(35633), FRAGMENT(35632);

    public final int id;

    private ShaderType(int id) {
        this.id = id;
    }
}