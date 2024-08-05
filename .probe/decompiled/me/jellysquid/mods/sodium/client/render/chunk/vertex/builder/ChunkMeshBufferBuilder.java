package me.jellysquid.mods.sodium.client.render.chunk.vertex.builder;

import java.nio.ByteBuffer;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import org.embeddedt.embeddium.render.chunk.sorting.TranslucentQuadAnalyzer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

public class ChunkMeshBufferBuilder {

    private final ChunkVertexEncoder encoder;

    private final int stride;

    private final int initialCapacity;

    private final TranslucentQuadAnalyzer analyzer;

    private ByteBuffer buffer;

    private int count;

    private int capacity;

    private int sectionIndex;

    public ChunkMeshBufferBuilder(ChunkVertexType vertexType, int initialCapacity, boolean collectSortState) {
        this.encoder = vertexType.getEncoder();
        this.stride = vertexType.getVertexFormat().getStride();
        this.buffer = null;
        this.capacity = initialCapacity;
        this.initialCapacity = initialCapacity;
        this.analyzer = collectSortState ? new TranslucentQuadAnalyzer() : null;
    }

    public void push(ChunkVertexEncoder.Vertex[] vertices, Material material) {
        int vertexStart = this.count;
        int vertexCount = vertices.length;
        if (this.count + vertexCount >= this.capacity) {
            this.grow(this.stride * vertexCount);
        }
        long ptr = MemoryUtil.memAddress(this.buffer, this.count * this.stride);
        if (this.analyzer != null) {
            for (ChunkVertexEncoder.Vertex vertex : vertices) {
                this.analyzer.capture(vertex);
            }
        }
        for (ChunkVertexEncoder.Vertex vertex : vertices) {
            ptr = this.encoder.write(ptr, material, vertex, this.sectionIndex);
        }
        this.count += vertexCount;
    }

    private void grow(int len) {
        int cap = Math.max(this.capacity * 2, this.capacity + len);
        this.setBufferSize(cap * this.stride);
    }

    private void setBufferSize(int capacity) {
        this.buffer = MemoryUtil.memRealloc(this.buffer, capacity * this.stride);
        this.capacity = capacity;
    }

    public void start(int sectionIndex) {
        this.count = 0;
        this.sectionIndex = sectionIndex;
        if (this.analyzer != null) {
            this.analyzer.clear();
        }
        this.setBufferSize(this.initialCapacity);
    }

    @Nullable
    public TranslucentQuadAnalyzer.SortState getSortState() {
        return this.analyzer != null ? this.analyzer.getSortState() : null;
    }

    public void destroy() {
        if (this.buffer != null) {
            MemoryUtil.memFree(this.buffer);
        }
        this.buffer = null;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public ByteBuffer slice() {
        if (this.isEmpty()) {
            throw new IllegalStateException("No vertex data in buffer");
        } else {
            return MemoryUtil.memSlice(this.buffer, 0, this.stride * this.count);
        }
    }

    public int count() {
        return this.count;
    }
}