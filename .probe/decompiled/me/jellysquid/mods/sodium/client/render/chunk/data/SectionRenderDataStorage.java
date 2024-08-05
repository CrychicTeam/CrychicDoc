package me.jellysquid.mods.sodium.client.render.chunk.data;

import java.util.Arrays;
import me.jellysquid.mods.sodium.client.gl.arena.GlBufferSegment;
import me.jellysquid.mods.sodium.client.gl.util.VertexRange;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import org.jetbrains.annotations.Nullable;

public class SectionRenderDataStorage {

    private final GlBufferSegment[] allocations = new GlBufferSegment[256];

    private final GlBufferSegment[] indexAllocations = new GlBufferSegment[256];

    private final long pMeshDataArray = SectionRenderDataUnsafe.allocateHeap(256);

    public void setMeshes(int localSectionIndex, GlBufferSegment allocation, @Nullable GlBufferSegment indexAllocation, VertexRange[] ranges) {
        if (this.allocations[localSectionIndex] != null) {
            this.allocations[localSectionIndex].delete();
            this.allocations[localSectionIndex] = null;
        }
        if (this.indexAllocations[localSectionIndex] != null) {
            this.indexAllocations[localSectionIndex].delete();
            this.indexAllocations[localSectionIndex] = null;
        }
        this.allocations[localSectionIndex] = allocation;
        this.indexAllocations[localSectionIndex] = indexAllocation;
        long pMeshData = this.getDataPointer(localSectionIndex);
        int sliceMask = 0;
        int vertexOffset = allocation.getOffset();
        int indexOffset = indexAllocation != null ? indexAllocation.getOffset() * 4 : 0;
        for (int facingIndex = 0; facingIndex < ModelQuadFacing.COUNT; facingIndex++) {
            VertexRange vertexRange = ranges[facingIndex];
            int vertexCount;
            if (vertexRange != null) {
                vertexCount = vertexRange.vertexCount();
            } else {
                vertexCount = 0;
            }
            int indexCount = (vertexCount >> 2) * 6;
            SectionRenderDataUnsafe.setVertexOffset(pMeshData, facingIndex, vertexOffset);
            SectionRenderDataUnsafe.setElementCount(pMeshData, facingIndex, indexCount);
            SectionRenderDataUnsafe.setIndexOffset(pMeshData, facingIndex, indexOffset);
            if (vertexCount > 0) {
                sliceMask |= 1 << facingIndex;
            }
            vertexOffset += vertexCount;
            indexOffset += indexCount * 4;
        }
        SectionRenderDataUnsafe.setSliceMask(pMeshData, sliceMask);
    }

    public void removeMeshes(int localSectionIndex) {
        if (this.allocations[localSectionIndex] != null) {
            this.allocations[localSectionIndex].delete();
            this.allocations[localSectionIndex] = null;
            SectionRenderDataUnsafe.clear(this.getDataPointer(localSectionIndex));
        }
        this.removeIndexBuffer(localSectionIndex);
    }

    public void removeIndexBuffer(int localSectionIndex) {
        if (this.indexAllocations[localSectionIndex] != null) {
            this.indexAllocations[localSectionIndex].delete();
            this.indexAllocations[localSectionIndex] = null;
        }
    }

    public void replaceIndexBuffer(int localSectionIndex, GlBufferSegment indexAllocation) {
        this.removeIndexBuffer(localSectionIndex);
        this.indexAllocations[localSectionIndex] = indexAllocation;
        long pMeshData = this.getDataPointer(localSectionIndex);
        int indexOffset = indexAllocation != null ? indexAllocation.getOffset() * 4 : 0;
        for (int facingIndex = 0; facingIndex < ModelQuadFacing.COUNT; facingIndex++) {
            SectionRenderDataUnsafe.setIndexOffset(pMeshData, facingIndex, indexOffset);
            int indexCount = SectionRenderDataUnsafe.getElementCount(pMeshData, facingIndex);
            indexOffset += indexCount * 4;
        }
    }

    public void onBufferResized() {
        for (int sectionIndex = 0; sectionIndex < 256; sectionIndex++) {
            this.updateMeshes(sectionIndex);
        }
    }

    private void updateMeshes(int sectionIndex) {
        GlBufferSegment allocation = this.allocations[sectionIndex];
        if (allocation != null) {
            GlBufferSegment indexAllocation = this.indexAllocations[sectionIndex];
            int vertexOffset = allocation.getOffset();
            int indexOffset = indexAllocation != null ? indexAllocation.getOffset() * 4 : 0;
            long data = this.getDataPointer(sectionIndex);
            for (int facing = 0; facing < ModelQuadFacing.COUNT; facing++) {
                SectionRenderDataUnsafe.setVertexOffset(data, facing, vertexOffset);
                SectionRenderDataUnsafe.setIndexOffset(data, facing, indexOffset);
                int indexCount = SectionRenderDataUnsafe.getElementCount(data, facing);
                vertexOffset += indexCount / 6 * 4;
                indexOffset += indexCount * 4;
            }
        }
    }

    public long getDataPointer(int sectionIndex) {
        return SectionRenderDataUnsafe.heapPointer(this.pMeshDataArray, sectionIndex);
    }

    public void delete() {
        for (GlBufferSegment allocation : this.allocations) {
            if (allocation != null) {
                allocation.delete();
            }
        }
        for (GlBufferSegment allocationx : this.indexAllocations) {
            if (allocationx != null) {
                allocationx.delete();
            }
        }
        Arrays.fill(this.allocations, null);
        Arrays.fill(this.indexAllocations, null);
        SectionRenderDataUnsafe.freeHeap(this.pMeshDataArray);
    }
}