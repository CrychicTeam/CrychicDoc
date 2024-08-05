package me.jellysquid.mods.sodium.client.render.chunk.vertex.format;

import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;

public interface ChunkVertexEncoder {

    long write(long var1, Material var3, ChunkVertexEncoder.Vertex var4, int var5);

    public static class Vertex {

        public float x;

        public float y;

        public float z;

        public int color;

        public float u;

        public float v;

        public int light;

        public static ChunkVertexEncoder.Vertex[] uninitializedQuad() {
            ChunkVertexEncoder.Vertex[] vertices = new ChunkVertexEncoder.Vertex[4];
            for (int i = 0; i < 4; i++) {
                vertices[i] = new ChunkVertexEncoder.Vertex();
            }
            return vertices;
        }
    }
}