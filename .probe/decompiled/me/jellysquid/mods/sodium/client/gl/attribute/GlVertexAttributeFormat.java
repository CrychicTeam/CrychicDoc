package me.jellysquid.mods.sodium.client.gl.attribute;

public record GlVertexAttributeFormat(int typeId, int size) {

    public static final GlVertexAttributeFormat FLOAT = new GlVertexAttributeFormat(5126, 4);

    public static final GlVertexAttributeFormat UNSIGNED_SHORT = new GlVertexAttributeFormat(5123, 2);

    public static final GlVertexAttributeFormat UNSIGNED_BYTE = new GlVertexAttributeFormat(5121, 1);

    public static final GlVertexAttributeFormat UNSIGNED_INT = new GlVertexAttributeFormat(5125, 4);
}