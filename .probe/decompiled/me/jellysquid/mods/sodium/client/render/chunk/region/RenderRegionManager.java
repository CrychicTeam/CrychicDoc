package me.jellysquid.mods.sodium.client.render.chunk.region;

import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap.Entry;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap.FastEntrySet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gl.arena.GlBufferArena;
import me.jellysquid.mods.sodium.client.gl.arena.PendingUpload;
import me.jellysquid.mods.sodium.client.gl.arena.staging.FallbackStagingBuffer;
import me.jellysquid.mods.sodium.client.gl.arena.staging.MappedStagingBuffer;
import me.jellysquid.mods.sodium.client.gl.arena.staging.StagingBuffer;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionMeshParts;
import me.jellysquid.mods.sodium.client.render.chunk.data.SectionRenderDataStorage;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import org.jetbrains.annotations.NotNull;

public class RenderRegionManager {

    private final Long2ReferenceOpenHashMap<RenderRegion> regions = new Long2ReferenceOpenHashMap();

    private final StagingBuffer stagingBuffer;

    public RenderRegionManager(CommandList commandList) {
        this.stagingBuffer = createStagingBuffer(commandList);
    }

    public void update() {
        this.stagingBuffer.flip();
        try (CommandList commandList = RenderDevice.INSTANCE.createCommandList()) {
            Iterator<RenderRegion> it = this.regions.values().iterator();
            while (it.hasNext()) {
                RenderRegion region = (RenderRegion) it.next();
                region.update(commandList);
                if (region.isEmpty()) {
                    region.delete(commandList);
                    it.remove();
                }
            }
        }
    }

    public void uploadMeshes(CommandList commandList, Collection<ChunkBuildOutput> results) {
        ObjectIterator var3 = this.createMeshUploadQueues(results).iterator();
        while (var3.hasNext()) {
            Entry<RenderRegion, List<ChunkBuildOutput>> entry = (Entry<RenderRegion, List<ChunkBuildOutput>>) var3.next();
            this.uploadMeshes(commandList, (RenderRegion) entry.getKey(), ((List) entry.getValue()).stream().filter(o -> !o.isIndexOnlyUpload()).toList());
            this.uploadResorts(commandList, (RenderRegion) entry.getKey(), ((List) entry.getValue()).stream().filter(ChunkBuildOutput::isIndexOnlyUpload).toList());
        }
    }

    private void uploadMeshes(CommandList commandList, RenderRegion region, Collection<ChunkBuildOutput> results) {
        ArrayList<RenderRegionManager.PendingSectionUpload> uploads = new ArrayList();
        for (ChunkBuildOutput result : results) {
            for (TerrainRenderPass pass : DefaultTerrainRenderPasses.ALL) {
                SectionRenderDataStorage storage = region.getStorage(pass);
                if (storage != null) {
                    storage.removeMeshes(result.render.getSectionIndex());
                }
                BuiltSectionMeshParts mesh = result.getMesh(pass);
                if (mesh != null) {
                    uploads.add(new RenderRegionManager.PendingSectionUpload(result.render, mesh, pass, new PendingUpload(mesh.getVertexData()), mesh.getIndexData() != null ? new PendingUpload(mesh.getIndexData()) : null));
                }
            }
        }
        if (!uploads.isEmpty()) {
            RenderRegion.DeviceResources resources = region.createResources(commandList);
            GlBufferArena geometryArena = resources.getGeometryArena();
            boolean bufferChanged = geometryArena.upload(commandList, uploads.stream().map(upload -> upload.vertexUpload));
            bufferChanged |= resources.getIndexArena().upload(commandList, uploads.stream().map(upload -> upload.indexUpload).filter(Objects::nonNull));
            if (bufferChanged) {
                region.refresh(commandList);
            }
            for (RenderRegionManager.PendingSectionUpload upload : uploads) {
                SectionRenderDataStorage storagex = region.createStorage(upload.pass);
                storagex.setMeshes(upload.section.getSectionIndex(), upload.vertexUpload.getResult(), upload.indexUpload != null ? upload.indexUpload.getResult() : null, upload.meshData.getVertexRanges());
            }
        }
    }

    private void uploadResorts(CommandList commandList, RenderRegion region, Collection<ChunkBuildOutput> results) {
        ArrayList<RenderRegionManager.PendingResortUpload> uploads = new ArrayList();
        for (ChunkBuildOutput result : results) {
            for (TerrainRenderPass pass : DefaultTerrainRenderPasses.ALL) {
                BuiltSectionMeshParts mesh = result.getMesh(pass);
                if (mesh != null) {
                    SectionRenderDataStorage storage = region.getStorage(pass);
                    if (storage != null) {
                        storage.removeIndexBuffer(result.render.getSectionIndex());
                    }
                    Objects.requireNonNull(mesh.getIndexData());
                    uploads.add(new RenderRegionManager.PendingResortUpload(result.render, mesh, pass, new PendingUpload(mesh.getIndexData())));
                }
            }
        }
        if (!uploads.isEmpty()) {
            RenderRegion.DeviceResources resources = region.createResources(commandList);
            boolean bufferChanged = resources.getIndexArena().upload(commandList, uploads.stream().map(upload -> upload.indexUpload).filter(Objects::nonNull));
            if (bufferChanged) {
                region.refresh(commandList);
            }
            for (RenderRegionManager.PendingResortUpload upload : uploads) {
                SectionRenderDataStorage storage = region.createStorage(upload.pass);
                storage.replaceIndexBuffer(upload.section.getSectionIndex(), upload.indexUpload.getResult());
            }
        }
    }

    private FastEntrySet<RenderRegion, List<ChunkBuildOutput>> createMeshUploadQueues(Collection<ChunkBuildOutput> results) {
        Reference2ReferenceOpenHashMap<RenderRegion, List<ChunkBuildOutput>> map = new Reference2ReferenceOpenHashMap();
        for (ChunkBuildOutput result : results) {
            List<ChunkBuildOutput> queue = (List<ChunkBuildOutput>) map.computeIfAbsent(result.render.getRegion(), k -> new ArrayList());
            queue.add(result);
        }
        return map.reference2ReferenceEntrySet();
    }

    public void delete(CommandList commandList) {
        ObjectIterator var2 = this.regions.values().iterator();
        while (var2.hasNext()) {
            RenderRegion region = (RenderRegion) var2.next();
            region.delete(commandList);
        }
        this.regions.clear();
        this.stagingBuffer.delete(commandList);
    }

    public Collection<RenderRegion> getLoadedRegions() {
        return this.regions.values();
    }

    public StagingBuffer getStagingBuffer() {
        return this.stagingBuffer;
    }

    public RenderRegion createForChunk(int chunkX, int chunkY, int chunkZ) {
        return this.create(chunkX >> RenderRegion.REGION_WIDTH_SH, chunkY >> RenderRegion.REGION_HEIGHT_SH, chunkZ >> RenderRegion.REGION_LENGTH_SH);
    }

    @NotNull
    private RenderRegion create(int x, int y, int z) {
        long key = RenderRegion.key(x, y, z);
        RenderRegion instance = (RenderRegion) this.regions.get(key);
        if (instance == null) {
            this.regions.put(key, instance = new RenderRegion(x, y, z, this.stagingBuffer));
        }
        return instance;
    }

    private static StagingBuffer createStagingBuffer(CommandList commandList) {
        return (StagingBuffer) (SodiumClientMod.options().advanced.useAdvancedStagingBuffers && MappedStagingBuffer.isSupported(RenderDevice.INSTANCE) ? new MappedStagingBuffer(commandList) : new FallbackStagingBuffer(commandList));
    }

    private static record PendingResortUpload(RenderSection section, BuiltSectionMeshParts meshData, TerrainRenderPass pass, PendingUpload indexUpload) {
    }

    private static record PendingSectionUpload(RenderSection section, BuiltSectionMeshParts meshData, TerrainRenderPass pass, PendingUpload vertexUpload, PendingUpload indexUpload) {

        private PendingSectionUpload(RenderSection section, BuiltSectionMeshParts meshData, TerrainRenderPass pass, PendingUpload vertexUpload) {
            this(section, meshData, pass, vertexUpload, null);
        }
    }
}