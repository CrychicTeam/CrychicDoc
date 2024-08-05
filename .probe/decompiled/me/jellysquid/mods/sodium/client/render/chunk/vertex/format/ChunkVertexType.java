package me.jellysquid.mods.sodium.client.render.chunk.vertex.format;

import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexFormat;

public interface ChunkVertexType {

    float getPositionScale();

    float getPositionOffset();

    float getTextureScale();

    GlVertexFormat<ChunkMeshAttribute> getVertexFormat();

    ChunkVertexEncoder getEncoder();
}