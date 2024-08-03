package me.jellysquid.mods.sodium.client.gl.buffer;

public enum GlBufferUsage {

    STREAM_DRAW(35040),
    STREAM_READ(35041),
    STREAM_COPY(35042),
    STATIC_DRAW(35044),
    STATIC_READ(35045),
    STATIC_COPY(35046),
    DYNAMIC_DRAW(35048),
    DYNAMIC_READ(35049),
    DYNAMIC_COPY(35050);

    private final int id;

    private GlBufferUsage(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}