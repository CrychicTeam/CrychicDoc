package me.jellysquid.mods.sodium.client.render.chunk.vertex.format.impl;

import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexAttributeFormat;
import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexFormat;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkMeshAttribute;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import org.lwjgl.system.MemoryUtil;

public class CompactChunkVertex implements ChunkVertexType {

    public static final GlVertexFormat<ChunkMeshAttribute> VERTEX_FORMAT = GlVertexFormat.builder(ChunkMeshAttribute.class, 20).addElement(ChunkMeshAttribute.POSITION_MATERIAL_MESH, 0, GlVertexAttributeFormat.UNSIGNED_SHORT, 4, false, true).addElement(ChunkMeshAttribute.COLOR_SHADE, 8, GlVertexAttributeFormat.UNSIGNED_BYTE, 4, true, false).addElement(ChunkMeshAttribute.BLOCK_TEXTURE, 12, GlVertexAttributeFormat.UNSIGNED_SHORT, 2, false, false).addElement(ChunkMeshAttribute.LIGHT_TEXTURE, 16, GlVertexAttributeFormat.UNSIGNED_SHORT, 2, false, true).build();

    public static final int STRIDE = 20;

    private static final int POSITION_MAX_VALUE = 65536;

    private static final int TEXTURE_MAX_VALUE = 32768;

    private static final float MODEL_ORIGIN = 8.0F;

    private static final float MODEL_RANGE = 32.0F;

    private static final float MODEL_SCALE = 4.8828125E-4F;

    private static final float MODEL_SCALE_INV = 2048.0F;

    private static final float TEXTURE_SCALE = 3.0517578E-5F;

    @Override
    public float getTextureScale() {
        return 3.0517578E-5F;
    }

    @Override
    public float getPositionScale() {
        return 4.8828125E-4F;
    }

    @Override
    public float getPositionOffset() {
        return -8.0F;
    }

    @Override
    public GlVertexFormat<ChunkMeshAttribute> getVertexFormat() {
        return VERTEX_FORMAT;
    }

    @Override
    public ChunkVertexEncoder getEncoder() {
        return (ptr, material, vertex, sectionIndex) -> {
            MemoryUtil.memPutShort(ptr + 0L, encodePosition(vertex.x));
            MemoryUtil.memPutShort(ptr + 2L, encodePosition(vertex.y));
            MemoryUtil.memPutShort(ptr + 4L, encodePosition(vertex.z));
            MemoryUtil.memPutByte(ptr + 6L, (byte) (material.bits() & 0xFF));
            MemoryUtil.memPutByte(ptr + 7L, (byte) (sectionIndex & 0xFF));
            MemoryUtil.memPutInt(ptr + 8L, vertex.color);
            MemoryUtil.memPutShort(ptr + 12L, encodeTexture(vertex.u));
            MemoryUtil.memPutShort(ptr + 14L, encodeTexture(vertex.v));
            MemoryUtil.memPutInt(ptr + 16L, vertex.light);
            return ptr + 20L;
        };
    }

    private static short encodePosition(float value) {
        return (short) ((int) ((8.0F + value) * 2048.0F));
    }

    public static float decodePosition(short value) {
        return (float) Short.toUnsignedInt(value) / 2048.0F - 8.0F;
    }

    private static short encodeTexture(float value) {
        return (short) (Math.round(value * 32768.0F) & 65535);
    }
}