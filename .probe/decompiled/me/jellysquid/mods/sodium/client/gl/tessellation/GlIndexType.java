package me.jellysquid.mods.sodium.client.gl.tessellation;

public enum GlIndexType {

    UNSIGNED_BYTE(5121, 1), UNSIGNED_SHORT(5123, 2), UNSIGNED_INT(5125, 4);

    private final int id;

    private final int stride;

    private GlIndexType(int id, int stride) {
        this.id = id;
        this.stride = stride;
    }

    public int getFormatId() {
        return this.id;
    }

    public int getStride() {
        return this.stride;
    }
}