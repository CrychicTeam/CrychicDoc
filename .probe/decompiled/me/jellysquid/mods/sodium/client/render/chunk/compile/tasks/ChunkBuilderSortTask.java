package me.jellysquid.mods.sodium.client.render.chunk.compile.tasks;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBufferSorter;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionMeshParts;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.util.NativeBuffer;
import me.jellysquid.mods.sodium.client.util.task.CancellationToken;
import org.embeddedt.embeddium.render.chunk.sorting.TranslucentQuadAnalyzer;

public class ChunkBuilderSortTask extends ChunkBuilderTask<ChunkBuildOutput> {

    private final RenderSection render;

    private final float cameraX;

    private final float cameraY;

    private final float cameraZ;

    private final int frame;

    private final Map<TerrainRenderPass, TranslucentQuadAnalyzer.SortState> translucentMeshes;

    public ChunkBuilderSortTask(RenderSection render, float cameraX, float cameraY, float cameraZ, int frame, Map<TerrainRenderPass, TranslucentQuadAnalyzer.SortState> translucentMeshes) {
        this.render = render;
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.cameraZ = cameraZ;
        this.frame = frame;
        this.translucentMeshes = translucentMeshes;
    }

    private static NativeBuffer makeNativeBuffer(ByteBuffer heapBuffer) {
        heapBuffer.rewind();
        NativeBuffer nb = new NativeBuffer(heapBuffer.capacity());
        nb.getDirectBuffer().put(heapBuffer);
        return nb;
    }

    public ChunkBuildOutput execute(ChunkBuildContext context, CancellationToken cancellationSource) {
        Map<TerrainRenderPass, BuiltSectionMeshParts> meshes = new Reference2ReferenceOpenHashMap();
        for (Entry<TerrainRenderPass, TranslucentQuadAnalyzer.SortState> entry : this.translucentMeshes.entrySet()) {
            TranslucentQuadAnalyzer.SortState sortBuffer = (TranslucentQuadAnalyzer.SortState) entry.getValue();
            NativeBuffer newIndexBuffer = new NativeBuffer(ChunkBufferSorter.getIndexBufferSize(sortBuffer.centers().length / 3));
            ChunkBufferSorter.sort(newIndexBuffer, sortBuffer, this.cameraX - (float) this.render.getOriginX(), this.cameraY - (float) this.render.getOriginY(), this.cameraZ - (float) this.render.getOriginZ());
            meshes.put((TerrainRenderPass) entry.getKey(), new BuiltSectionMeshParts(null, newIndexBuffer, sortBuffer, null));
        }
        ChunkBuildOutput result = new ChunkBuildOutput(this.render, null, meshes, this.frame);
        result.setIndexOnlyUpload(true);
        return result;
    }
}