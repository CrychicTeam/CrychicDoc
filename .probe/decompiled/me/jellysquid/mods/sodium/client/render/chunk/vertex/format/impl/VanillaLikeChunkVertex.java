package me.jellysquid.mods.sodium.client.render.chunk.vertex.format.impl;

import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexAttributeFormat;
import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexFormat;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkMeshAttribute;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import org.lwjgl.system.MemoryUtil;

public class VanillaLikeChunkVertex implements ChunkVertexType {

    public static final int STRIDE = 28;

    public static final GlVertexFormat<ChunkMeshAttribute> VERTEX_FORMAT = GlVertexFormat.builder(ChunkMeshAttribute.class, 28).addElement(ChunkMeshAttribute.POSITION_MATERIAL_MESH, 0, GlVertexAttributeFormat.FLOAT, 3, false, false).addElement(ChunkMeshAttribute.COLOR_SHADE, 12, GlVertexAttributeFormat.UNSIGNED_BYTE, 4, true, false).addElement(ChunkMeshAttribute.BLOCK_TEXTURE, 16, GlVertexAttributeFormat.FLOAT, 2, false, false).addElement(ChunkMeshAttribute.LIGHT_TEXTURE, 24, GlVertexAttributeFormat.UNSIGNED_INT, 1, false, true).build();

    @Override
    public float getPositionScale() {
        return 1.0F;
    }

    @Override
    public float getPositionOffset() {
        return 0.0F;
    }

    @Override
    public float getTextureScale() {
        return 1.0F;
    }

    @Override
    public GlVertexFormat<ChunkMeshAttribute> getVertexFormat() {
        return VERTEX_FORMAT;
    }

    @Override
    public ChunkVertexEncoder getEncoder() {
        return (ptr, material, vertex, sectionIndex) -> {
            MemoryUtil.memPutFloat(ptr + 0L, vertex.x);
            MemoryUtil.memPutFloat(ptr + 4L, vertex.y);
            MemoryUtil.memPutFloat(ptr + 8L, vertex.z);
            MemoryUtil.memPutInt(ptr + 12L, vertex.color);
            MemoryUtil.memPutFloat(ptr + 16L, encodeTexture(vertex.u));
            MemoryUtil.memPutFloat(ptr + 20L, encodeTexture(vertex.v));
            MemoryUtil.memPutInt(ptr + 24L, encodeDrawParameters(material, sectionIndex) << 0 | encodeLight(vertex.light) << 16);
            return ptr + 28L;
        };
    }

    private static int encodeDrawParameters(Material material, int sectionIndex) {
        return (sectionIndex & 0xFF) << 8 | (material.bits() & 0xFF) << 0;
    }

    private static int encodeLight(int light) {
        int block = light & 0xFF;
        int sky = light >> 16 & 0xFF;
        return block << 0 | sky << 8;
    }

    private static float encodeTexture(float value) {
        return Math.min(0.99999994F, value);
    }
}