package me.jellysquid.mods.sodium.client.render.chunk.compile;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import me.jellysquid.mods.sodium.client.gl.util.VertexRange;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.BakedChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionMeshParts;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import me.jellysquid.mods.sodium.client.util.NativeBuffer;
import org.embeddedt.embeddium.render.chunk.sorting.TranslucentQuadAnalyzer;

public class ChunkBuildBuffers {

    private static final ModelQuadFacing[] ONLY_UNASSIGNED = new ModelQuadFacing[] { ModelQuadFacing.UNASSIGNED };

    private final Reference2ReferenceOpenHashMap<TerrainRenderPass, BakedChunkModelBuilder> builders = new Reference2ReferenceOpenHashMap();

    private final ChunkVertexType vertexType;

    public ChunkBuildBuffers(ChunkVertexType vertexType) {
        this.vertexType = vertexType;
        for (TerrainRenderPass pass : DefaultTerrainRenderPasses.ALL) {
            ChunkMeshBufferBuilder[] vertexBuffers = new ChunkMeshBufferBuilder[ModelQuadFacing.COUNT];
            for (int facing = 0; facing < ModelQuadFacing.COUNT; facing++) {
                vertexBuffers[facing] = new ChunkMeshBufferBuilder(this.vertexType, 131072, pass.isSorted() && facing == ModelQuadFacing.UNASSIGNED.ordinal());
            }
            this.builders.put(pass, new BakedChunkModelBuilder(vertexBuffers, !pass.isSorted()));
        }
    }

    public void init(BuiltSectionInfo.Builder renderData, int sectionIndex) {
        ObjectIterator var3 = this.builders.values().iterator();
        while (var3.hasNext()) {
            BakedChunkModelBuilder builder = (BakedChunkModelBuilder) var3.next();
            builder.begin(renderData, sectionIndex);
        }
    }

    public ChunkModelBuilder get(Material material) {
        return (ChunkModelBuilder) this.builders.get(material.pass);
    }

    public ChunkModelBuilder get(TerrainRenderPass pass) {
        return (ChunkModelBuilder) this.builders.get(pass);
    }

    public BuiltSectionMeshParts createMesh(TerrainRenderPass pass) {
        BakedChunkModelBuilder builder = (BakedChunkModelBuilder) this.builders.get(pass);
        List<ByteBuffer> vertexBuffers = new ArrayList();
        VertexRange[] vertexRanges = new VertexRange[ModelQuadFacing.COUNT];
        int vertexCount = 0;
        ModelQuadFacing[] facingsToUpload = pass.isSorted() ? ONLY_UNASSIGNED : ModelQuadFacing.VALUES;
        TranslucentQuadAnalyzer.SortState sortState = pass.isSorted() ? builder.getVertexBuffer(ModelQuadFacing.UNASSIGNED).getSortState() : null;
        for (ModelQuadFacing facing : facingsToUpload) {
            ChunkMeshBufferBuilder buffer = builder.getVertexBuffer(facing);
            if (!buffer.isEmpty()) {
                vertexBuffers.add(buffer.slice());
                vertexRanges[facing.ordinal()] = new VertexRange(vertexCount, buffer.count());
                vertexCount += buffer.count();
            }
        }
        if (vertexCount == 0) {
            return null;
        } else {
            NativeBuffer mergedBuffer = new NativeBuffer(vertexCount * this.vertexType.getVertexFormat().getStride());
            ByteBuffer mergedBufferBuilder = mergedBuffer.getDirectBuffer();
            for (ByteBuffer buffer : vertexBuffers) {
                mergedBufferBuilder.put(buffer);
            }
            mergedBufferBuilder.flip();
            NativeBuffer mergedIndexBuffer;
            if (pass.isSorted()) {
                mergedIndexBuffer = new NativeBuffer(vertexCount / 4 * 6 * 4);
                int bufOffset = 0;
                for (ModelQuadFacing facingx : facingsToUpload) {
                    ChunkMeshBufferBuilder buffer = builder.getVertexBuffer(facingx);
                    if (!buffer.isEmpty()) {
                        int numPrimitives = buffer.count() / 4;
                        ChunkBufferSorter.generateSimpleIndexBuffer(mergedIndexBuffer, numPrimitives, bufOffset);
                        bufOffset += numPrimitives * 6;
                    }
                }
            } else {
                mergedIndexBuffer = null;
            }
            return new BuiltSectionMeshParts(mergedBuffer, mergedIndexBuffer, sortState, vertexRanges);
        }
    }

    public void destroy() {
        ObjectIterator var1 = this.builders.values().iterator();
        while (var1.hasNext()) {
            BakedChunkModelBuilder builder = (BakedChunkModelBuilder) var1.next();
            builder.destroy();
        }
    }

    public ChunkVertexType getVertexType() {
        return this.vertexType;
    }
}