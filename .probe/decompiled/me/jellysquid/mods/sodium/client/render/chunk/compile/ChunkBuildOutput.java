package me.jellysquid.mods.sodium.client.render.chunk.compile;

import java.util.Map;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionMeshParts;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;

public class ChunkBuildOutput {

    public final RenderSection render;

    public final BuiltSectionInfo info;

    public final Map<TerrainRenderPass, BuiltSectionMeshParts> meshes;

    public final int buildTime;

    private boolean partialUpload;

    public ChunkBuildOutput(RenderSection render, BuiltSectionInfo info, Map<TerrainRenderPass, BuiltSectionMeshParts> meshes, int buildTime) {
        this.render = render;
        this.info = info;
        this.meshes = meshes;
        this.buildTime = buildTime;
    }

    public BuiltSectionMeshParts getMesh(TerrainRenderPass pass) {
        return (BuiltSectionMeshParts) this.meshes.get(pass);
    }

    public void delete() {
        for (BuiltSectionMeshParts data : this.meshes.values()) {
            if (data.getVertexData() != null) {
                data.getVertexData().free();
            }
            if (data.getIndexData() != null) {
                data.getIndexData().free();
            }
        }
    }

    public boolean isIndexOnlyUpload() {
        return this.partialUpload;
    }

    public void setIndexOnlyUpload(boolean flag) {
        this.partialUpload = flag;
    }
}