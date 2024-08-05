package me.jellysquid.mods.sodium.client.render.chunk;

import java.util.Iterator;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexAttributeBinding;
import me.jellysquid.mods.sodium.client.gl.attribute.GlVertexFormat;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.gl.device.DrawCommandList;
import me.jellysquid.mods.sodium.client.gl.device.MultiDrawBatch;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.gl.tessellation.GlIndexType;
import me.jellysquid.mods.sodium.client.gl.tessellation.GlPrimitiveType;
import me.jellysquid.mods.sodium.client.gl.tessellation.GlTessellation;
import me.jellysquid.mods.sodium.client.gl.tessellation.TessellationBinding;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.render.chunk.data.SectionRenderDataStorage;
import me.jellysquid.mods.sodium.client.render.chunk.data.SectionRenderDataUnsafe;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderListIterable;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkMeshAttribute;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkMeshFormats;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import me.jellysquid.mods.sodium.client.render.viewport.CameraTransform;
import me.jellysquid.mods.sodium.client.util.BitwiseMath;
import me.jellysquid.mods.sodium.client.util.iterator.ByteIterator;
import org.lwjgl.system.MemoryUtil;

public class DefaultChunkRenderer extends ShaderChunkRenderer {

    private final MultiDrawBatch batch = new MultiDrawBatch(ModelQuadFacing.COUNT * 256 + 1);

    private final SharedQuadIndexBuffer sharedIndexBuffer;

    private final GlVertexAttributeBinding[] vertexAttributeBindings;

    private boolean isIndexedPass;

    private static final int MODEL_UNASSIGNED = ModelQuadFacing.UNASSIGNED.ordinal();

    private static final int MODEL_POS_X = ModelQuadFacing.POS_X.ordinal();

    private static final int MODEL_POS_Y = ModelQuadFacing.POS_Y.ordinal();

    private static final int MODEL_POS_Z = ModelQuadFacing.POS_Z.ordinal();

    private static final int MODEL_NEG_X = ModelQuadFacing.NEG_X.ordinal();

    private static final int MODEL_NEG_Y = ModelQuadFacing.NEG_Y.ordinal();

    private static final int MODEL_NEG_Z = ModelQuadFacing.NEG_Z.ordinal();

    public DefaultChunkRenderer(RenderDevice device, ChunkVertexType vertexType) {
        super(device, vertexType);
        this.sharedIndexBuffer = new SharedQuadIndexBuffer(device.createCommandList(), SharedQuadIndexBuffer.IndexType.INTEGER);
        this.vertexAttributeBindings = this.getBindingsForType();
    }

    @Override
    public void render(ChunkRenderMatrices matrices, CommandList commandList, ChunkRenderListIterable renderLists, TerrainRenderPass renderPass, CameraTransform camera) {
        super.begin(renderPass);
        boolean useBlockFaceCulling = SodiumClientMod.options().performance.useBlockFaceCulling;
        ChunkShaderInterface shader = this.activeProgram.getInterface();
        shader.setProjectionMatrix(matrices.projection());
        shader.setModelViewMatrix(matrices.modelView());
        Iterator<ChunkRenderList> iterator = renderLists.iterator(renderPass.isReverseOrder());
        this.isIndexedPass = renderPass.isSorted();
        while (iterator.hasNext()) {
            ChunkRenderList renderList = (ChunkRenderList) iterator.next();
            RenderRegion region = renderList.getRegion();
            SectionRenderDataStorage storage = region.getStorage(renderPass);
            if (storage != null) {
                fillCommandBuffer(this.batch, region, storage, renderList, camera, renderPass, useBlockFaceCulling);
                if (!this.batch.isEmpty()) {
                    if (!this.isIndexedPass) {
                        this.sharedIndexBuffer.ensureCapacity(commandList, this.batch.getIndexBufferSize());
                    }
                    GlTessellation tessellation = this.prepareTessellation(commandList, region);
                    setModelMatrixUniforms(shader, region, camera);
                    executeDrawBatch(commandList, tessellation, this.batch);
                }
            }
        }
        super.end(renderPass);
    }

    private static void fillCommandBuffer(MultiDrawBatch batch, RenderRegion renderRegion, SectionRenderDataStorage renderDataStorage, ChunkRenderList renderList, CameraTransform camera, TerrainRenderPass pass, boolean useBlockFaceCulling) {
        batch.clear();
        ByteIterator iterator = renderList.sectionsWithGeometryIterator(pass.isReverseOrder());
        if (iterator != null) {
            int originX = renderRegion.getChunkX();
            int originY = renderRegion.getChunkY();
            int originZ = renderRegion.getChunkZ();
            int indexPointerMask = pass.isSorted() ? -1 : 0;
            while (iterator.hasNext()) {
                int sectionIndex = iterator.nextByteAsInt();
                int chunkX = originX + LocalSectionIndex.unpackX(sectionIndex);
                int chunkY = originY + LocalSectionIndex.unpackY(sectionIndex);
                int chunkZ = originZ + LocalSectionIndex.unpackZ(sectionIndex);
                long pMeshData = renderDataStorage.getDataPointer(sectionIndex);
                int slices;
                if (useBlockFaceCulling && !pass.isSorted()) {
                    slices = getVisibleFaces(camera.intX, camera.intY, camera.intZ, chunkX, chunkY, chunkZ);
                } else {
                    slices = ModelQuadFacing.ALL;
                }
                slices &= SectionRenderDataUnsafe.getSliceMask(pMeshData);
                if (slices != 0) {
                    addDrawCommands(batch, pMeshData, slices, indexPointerMask);
                }
            }
        }
    }

    private static void addDrawCommands(MultiDrawBatch batch, long pMeshData, int mask, int indexPointerMask) {
        long pBaseVertex = batch.pBaseVertex;
        long pElementCount = batch.pElementCount;
        long pElementPointer = batch.pElementPointer;
        int size = batch.size;
        for (int facing = 0; facing < ModelQuadFacing.COUNT; facing++) {
            MemoryUtil.memPutInt(pBaseVertex + (long) (size << 2), SectionRenderDataUnsafe.getVertexOffset(pMeshData, facing));
            MemoryUtil.memPutInt(pElementCount + (long) (size << 2), SectionRenderDataUnsafe.getElementCount(pMeshData, facing));
            MemoryUtil.memPutAddress(pElementPointer + (long) (size << 3), (long) (SectionRenderDataUnsafe.getIndexOffset(pMeshData, facing) & indexPointerMask));
            size += mask >> facing & 1;
        }
        batch.size = size;
    }

    private static int getVisibleFaces(int originX, int originY, int originZ, int chunkX, int chunkY, int chunkZ) {
        int boundsMinX = chunkX << 4;
        int boundsMaxX = boundsMinX + 16;
        int boundsMinY = chunkY << 4;
        int boundsMaxY = boundsMinY + 16;
        int boundsMinZ = chunkZ << 4;
        int boundsMaxZ = boundsMinZ + 16;
        int planes = 1 << MODEL_UNASSIGNED;
        planes |= BitwiseMath.greaterThan(originX, boundsMinX - 3) << MODEL_POS_X;
        planes |= BitwiseMath.greaterThan(originY, boundsMinY - 3) << MODEL_POS_Y;
        planes |= BitwiseMath.greaterThan(originZ, boundsMinZ - 3) << MODEL_POS_Z;
        planes |= BitwiseMath.lessThan(originX, boundsMaxX + 3) << MODEL_NEG_X;
        planes |= BitwiseMath.lessThan(originY, boundsMaxY + 3) << MODEL_NEG_Y;
        return planes | BitwiseMath.lessThan(originZ, boundsMaxZ + 3) << MODEL_NEG_Z;
    }

    private static void setModelMatrixUniforms(ChunkShaderInterface shader, RenderRegion region, CameraTransform camera) {
        float x = getCameraTranslation(region.getOriginX(), camera.intX, camera.fracX);
        float y = getCameraTranslation(region.getOriginY(), camera.intY, camera.fracY);
        float z = getCameraTranslation(region.getOriginZ(), camera.intZ, camera.fracZ);
        shader.setRegionOffset(x, y, z);
    }

    private static float getCameraTranslation(int chunkBlockPos, int cameraBlockPos, float cameraPos) {
        return (float) (chunkBlockPos - cameraBlockPos) - cameraPos;
    }

    private GlTessellation prepareTessellation(CommandList commandList, RenderRegion region) {
        RenderRegion.DeviceResources resources = region.getResources();
        GlTessellation tessellation = this.isIndexedPass ? resources.getIndexedTessellation() : resources.getTessellation();
        if (tessellation == null) {
            tessellation = this.createRegionTessellation(commandList, resources);
            if (this.isIndexedPass) {
                resources.updateIndexedTessellation(commandList, tessellation);
            } else {
                resources.updateTessellation(commandList, tessellation);
            }
        }
        return tessellation;
    }

    private GlVertexAttributeBinding[] getBindingsForType() {
        if (this.vertexType == ChunkMeshFormats.COMPACT) {
            return new GlVertexAttributeBinding[] { new GlVertexAttributeBinding(0, this.vertexFormat.getAttribute(ChunkMeshAttribute.POSITION_MATERIAL_MESH)), new GlVertexAttributeBinding(1, this.vertexFormat.getAttribute(ChunkMeshAttribute.COLOR_SHADE)), new GlVertexAttributeBinding(2, this.vertexFormat.getAttribute(ChunkMeshAttribute.BLOCK_TEXTURE)), new GlVertexAttributeBinding(3, this.vertexFormat.getAttribute(ChunkMeshAttribute.LIGHT_TEXTURE)) };
        } else if (this.vertexType == ChunkMeshFormats.VANILLA_LIKE) {
            GlVertexFormat<ChunkMeshAttribute> vanillaFormat = this.vertexFormat;
            return new GlVertexAttributeBinding[] { new GlVertexAttributeBinding(0, vanillaFormat.getAttribute(ChunkMeshAttribute.POSITION_MATERIAL_MESH)), new GlVertexAttributeBinding(1, vanillaFormat.getAttribute(ChunkMeshAttribute.COLOR_SHADE)), new GlVertexAttributeBinding(2, vanillaFormat.getAttribute(ChunkMeshAttribute.BLOCK_TEXTURE)), new GlVertexAttributeBinding(3, vanillaFormat.getAttribute(ChunkMeshAttribute.LIGHT_TEXTURE)) };
        } else {
            return null;
        }
    }

    private GlTessellation createRegionTessellation(CommandList commandList, RenderRegion.DeviceResources resources) {
        return commandList.createTessellation(GlPrimitiveType.TRIANGLES, new TessellationBinding[] { TessellationBinding.forVertexBuffer(resources.getVertexBuffer(), this.vertexAttributeBindings), TessellationBinding.forElementBuffer(this.isIndexedPass ? resources.getIndexBuffer() : this.sharedIndexBuffer.getBufferObject()) });
    }

    private static void executeDrawBatch(CommandList commandList, GlTessellation tessellation, MultiDrawBatch batch) {
        try (DrawCommandList drawCommandList = commandList.beginTessellating(tessellation)) {
            drawCommandList.multiDrawElementsBaseVertex(batch, GlIndexType.UNSIGNED_INT);
        }
    }

    @Override
    public void delete(CommandList commandList) {
        super.delete(commandList);
        this.sharedIndexBuffer.delete(commandList);
        this.batch.delete();
    }
}