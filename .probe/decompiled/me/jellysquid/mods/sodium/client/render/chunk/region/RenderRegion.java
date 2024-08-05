package me.jellysquid.mods.sodium.client.render.chunk.region;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.Arrays;
import java.util.Map;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gl.arena.GlBufferArena;
import me.jellysquid.mods.sodium.client.gl.arena.staging.StagingBuffer;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBuffer;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.gl.tessellation.GlTessellation;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.data.SectionRenderDataStorage;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkMeshFormats;
import me.jellysquid.mods.sodium.client.util.MathUtil;
import net.minecraft.core.SectionPos;
import org.apache.commons.lang3.Validate;

public class RenderRegion {

    public static final int REGION_WIDTH = 8;

    public static final int REGION_HEIGHT = 4;

    public static final int REGION_LENGTH = 8;

    private static final int REGION_WIDTH_M = 7;

    private static final int REGION_HEIGHT_M = 3;

    private static final int REGION_LENGTH_M = 7;

    protected static final int REGION_WIDTH_SH = Integer.bitCount(7);

    protected static final int REGION_HEIGHT_SH = Integer.bitCount(3);

    protected static final int REGION_LENGTH_SH = Integer.bitCount(7);

    public static final int REGION_SIZE = 256;

    private final StagingBuffer stagingBuffer;

    private final int x;

    private final int y;

    private final int z;

    private final ChunkRenderList renderList;

    private final RenderSection[] sections = new RenderSection[256];

    private int sectionCount;

    private final Map<TerrainRenderPass, SectionRenderDataStorage> sectionRenderData = new Reference2ReferenceOpenHashMap();

    private RenderRegion.DeviceResources resources;

    public RenderRegion(int x, int y, int z, StagingBuffer stagingBuffer) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.stagingBuffer = stagingBuffer;
        this.renderList = new ChunkRenderList(this);
    }

    public static long key(int x, int y, int z) {
        return SectionPos.asLong(x, y, z);
    }

    public int getChunkX() {
        return this.x << REGION_WIDTH_SH;
    }

    public int getChunkY() {
        return this.y << REGION_HEIGHT_SH;
    }

    public int getChunkZ() {
        return this.z << REGION_LENGTH_SH;
    }

    public int getOriginX() {
        return this.getChunkX() << 4;
    }

    public int getOriginY() {
        return this.getChunkY() << 4;
    }

    public int getOriginZ() {
        return this.getChunkZ() << 4;
    }

    public int getCenterX() {
        return this.getChunkX() + 4 << 4;
    }

    public int getCenterY() {
        return this.getChunkY() + 2 << 4;
    }

    public int getCenterZ() {
        return this.getChunkZ() + 4 << 4;
    }

    public void delete(CommandList commandList) {
        for (SectionRenderDataStorage storage : this.sectionRenderData.values()) {
            storage.delete();
        }
        this.sectionRenderData.clear();
        if (this.resources != null) {
            this.resources.delete(commandList);
            this.resources = null;
        }
        Arrays.fill(this.sections, null);
    }

    public boolean isEmpty() {
        return this.sectionCount == 0;
    }

    public SectionRenderDataStorage getStorage(TerrainRenderPass pass) {
        return (SectionRenderDataStorage) this.sectionRenderData.get(pass);
    }

    public SectionRenderDataStorage createStorage(TerrainRenderPass pass) {
        SectionRenderDataStorage storage = (SectionRenderDataStorage) this.sectionRenderData.get(pass);
        if (storage == null) {
            this.sectionRenderData.put(pass, storage = new SectionRenderDataStorage());
        }
        return storage;
    }

    public void refresh(CommandList commandList) {
        if (this.resources != null) {
            this.resources.deleteTessellations(commandList);
        }
        for (SectionRenderDataStorage storage : this.sectionRenderData.values()) {
            storage.onBufferResized();
        }
    }

    public void addSection(RenderSection section) {
        int sectionIndex = section.getSectionIndex();
        RenderSection prev = this.sections[sectionIndex];
        if (prev != null) {
            throw new IllegalStateException("Section has already been added to the region");
        } else {
            this.sections[sectionIndex] = section;
            this.sectionCount++;
        }
    }

    public void removeSection(RenderSection section) {
        int sectionIndex = section.getSectionIndex();
        RenderSection prev = this.sections[sectionIndex];
        if (prev == null) {
            throw new IllegalStateException("Section was not loaded within the region");
        } else if (prev != section) {
            throw new IllegalStateException("Tried to remove the wrong section");
        } else {
            for (SectionRenderDataStorage storage : this.sectionRenderData.values()) {
                storage.removeMeshes(sectionIndex);
            }
            this.sections[sectionIndex] = null;
            this.sectionCount--;
        }
    }

    public RenderSection getSection(int id) {
        return this.sections[id];
    }

    public RenderRegion.DeviceResources getResources() {
        return this.resources;
    }

    public RenderRegion.DeviceResources createResources(CommandList commandList) {
        if (this.resources == null) {
            this.resources = new RenderRegion.DeviceResources(commandList, this.stagingBuffer);
        }
        return this.resources;
    }

    public void update(CommandList commandList) {
        if (this.resources != null && this.resources.shouldDelete()) {
            this.resources.delete(commandList);
            this.resources = null;
        }
    }

    public ChunkRenderList getRenderList() {
        return this.renderList;
    }

    static {
        Validate.isTrue(MathUtil.isPowerOfTwo(8));
        Validate.isTrue(MathUtil.isPowerOfTwo(4));
        Validate.isTrue(MathUtil.isPowerOfTwo(8));
    }

    public static class DeviceResources {

        private final GlBufferArena geometryArena;

        private final GlBufferArena indexArena;

        private GlTessellation tessellation;

        private GlTessellation indexedTessellation;

        public DeviceResources(CommandList commandList, StagingBuffer stagingBuffer) {
            int stride;
            if (!SodiumClientMod.canUseVanillaVertices()) {
                stride = ChunkMeshFormats.COMPACT.getVertexFormat().getStride();
            } else {
                stride = ChunkMeshFormats.VANILLA_LIKE.getVertexFormat().getStride();
            }
            this.geometryArena = new GlBufferArena(commandList, 193536, stride, stagingBuffer);
            this.indexArena = new GlBufferArena(commandList, 145152, 4, stagingBuffer);
        }

        public void updateTessellation(CommandList commandList, GlTessellation tessellation) {
            if (this.tessellation != null) {
                this.tessellation.delete(commandList);
            }
            this.tessellation = tessellation;
        }

        public GlTessellation getTessellation() {
            return this.tessellation;
        }

        public void updateIndexedTessellation(CommandList commandList, GlTessellation tessellation) {
            if (this.indexedTessellation != null) {
                this.indexedTessellation.delete(commandList);
            }
            this.indexedTessellation = tessellation;
        }

        public GlTessellation getIndexedTessellation() {
            return this.indexedTessellation;
        }

        public void deleteTessellations(CommandList commandList) {
            if (this.tessellation != null) {
                this.tessellation.delete(commandList);
                this.tessellation = null;
            }
            if (this.indexedTessellation != null) {
                this.indexedTessellation.delete(commandList);
                this.indexedTessellation = null;
            }
        }

        public GlBuffer getVertexBuffer() {
            return this.geometryArena.getBufferObject();
        }

        public GlBuffer getIndexBuffer() {
            return this.indexArena.getBufferObject();
        }

        public void delete(CommandList commandList) {
            this.deleteTessellations(commandList);
            this.geometryArena.delete(commandList);
            this.indexArena.delete(commandList);
        }

        public GlBufferArena getGeometryArena() {
            return this.geometryArena;
        }

        public GlBufferArena getIndexArena() {
            return this.indexArena;
        }

        public boolean shouldDelete() {
            return this.geometryArena.isEmpty();
        }
    }
}