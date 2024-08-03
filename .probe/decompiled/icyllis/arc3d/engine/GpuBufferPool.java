package icyllis.arc3d.engine;

import icyllis.arc3d.core.MathUtil;
import icyllis.arc3d.core.SharedPtr;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.system.MemoryUtil;

public abstract class GpuBufferPool {

    public static final int DEFAULT_BUFFER_SIZE = 131072;

    private final ResourceProvider mResourceProvider;

    private final int mBufferType;

    @SharedPtr
    protected GpuBuffer[] mBuffers = new GpuBuffer[8];

    protected int[] mFreeBytes = new int[8];

    protected int mIndex = -1;

    protected long mBufferPtr;

    private int mBytesInUse;

    protected ByteBuffer mCachedWriter;

    protected GpuBufferPool(ResourceProvider resourceProvider, int bufferType) {
        assert bufferType == 1 || bufferType == 2;
        this.mResourceProvider = resourceProvider;
        this.mBufferType = bufferType;
    }

    @Nonnull
    public static GpuBufferPool makeVertexPool(ResourceProvider resourceProvider) {
        return new GpuBufferPool.VertexPool(resourceProvider);
    }

    @Nonnull
    public static GpuBufferPool makeInstancePool(ResourceProvider resourceProvider) {
        return new GpuBufferPool.InstancePool(resourceProvider);
    }

    @Nonnull
    public static GpuBufferPool makeIndexPool(ResourceProvider resourceProvider) {
        return new GpuBufferPool.IndexPool(resourceProvider);
    }

    public void flush() {
        if (this.mBufferPtr != 0L) {
            assert this.mIndex >= 0;
            GpuBuffer buffer = this.mBuffers[this.mIndex];
            int usedBytes = buffer.getSize() - this.mFreeBytes[this.mIndex];
            assert buffer.isLocked();
            assert buffer.getLockedBuffer() == this.mBufferPtr;
            buffer.unlock(0, usedBytes);
            this.mBufferPtr = 0L;
        }
    }

    public void reset() {
        this.mBytesInUse = 0;
        if (this.mIndex >= 0) {
            GpuBuffer buffer = this.mBuffers[this.mIndex];
            if (buffer.isLocked()) {
                assert this.mBufferPtr != 0L;
                assert buffer.getLockedBuffer() == this.mBufferPtr;
                buffer.unlock();
                this.mBufferPtr = 0L;
            }
        }
        while (this.mIndex >= 0) {
            GpuBuffer buffer = this.mBuffers[this.mIndex];
            assert !buffer.isLocked();
            this.mBuffers[this.mIndex--] = GpuResource.move(buffer);
        }
        assert this.mIndex == -1;
        assert this.mBufferPtr == 0L;
    }

    public void submit(CommandBuffer cmdBuffer) {
        this.mBytesInUse = 0;
        if (this.mIndex >= 0) {
            GpuBuffer buffer = this.mBuffers[this.mIndex];
            if (buffer.isLocked()) {
                assert this.mBufferPtr != 0L;
                assert buffer.getLockedBuffer() == this.mBufferPtr;
                buffer.unlock();
                this.mBufferPtr = 0L;
            }
        }
        while (this.mIndex >= 0) {
            GpuBuffer buffer = this.mBuffers[this.mIndex];
            assert !buffer.isLocked();
            cmdBuffer.moveAndTrackGpuBuffer(buffer);
            this.mBuffers[this.mIndex--] = null;
        }
        assert this.mIndex == -1;
        assert this.mBufferPtr == 0L;
    }

    public void putBack(int bytes) {
        while (bytes > 0) {
            assert this.mIndex >= 0;
            GpuBuffer buffer = this.mBuffers[this.mIndex];
            int usedBytes = buffer.getSize() - this.mFreeBytes[this.mIndex];
            if (bytes < usedBytes) {
                this.mFreeBytes[this.mIndex] = this.mFreeBytes[this.mIndex] + bytes;
                this.mBytesInUse -= bytes;
                break;
            }
            bytes -= usedBytes;
            this.mBytesInUse -= usedBytes;
            assert buffer.isLocked();
            assert buffer.getLockedBuffer() == this.mBufferPtr;
            buffer.unlock(0, usedBytes);
            assert !buffer.isLocked();
            this.mBuffers[this.mIndex--] = GpuResource.move(buffer);
            this.mBufferPtr = 0L;
        }
    }

    public abstract long makeSpace(Mesh var1);

    @Nullable
    public abstract ByteBuffer makeWriter(Mesh var1);

    protected long makeSpace(int size, int alignment) {
        assert size > 0;
        assert alignment > 0;
        if (this.mBufferPtr != 0L) {
            assert this.mIndex >= 0;
            GpuBuffer buffer = this.mBuffers[this.mIndex];
            int pos = buffer.getSize() - this.mFreeBytes[this.mIndex];
            int pad = MathUtil.alignUpPad(pos, alignment);
            int alignedSize = size + pad;
            if (alignedSize <= 0) {
                return 0L;
            }
            if (alignedSize <= this.mFreeBytes[this.mIndex]) {
                this.mFreeBytes[this.mIndex] = this.mFreeBytes[this.mIndex] - alignedSize;
                this.mBytesInUse += alignedSize;
                return this.mBufferPtr + (long) pos + (long) pad;
            }
        }
        int blockSize = Math.max(size, 131072);
        GpuBuffer bufferx = this.mResourceProvider.createBuffer(blockSize, this.mBufferType | 256);
        if (bufferx == null) {
            return 0L;
        } else {
            this.flush();
            int cap = this.mBuffers.length;
            if (++this.mIndex >= cap) {
                cap += cap >> 1;
                this.mBuffers = (GpuBuffer[]) Arrays.copyOf(this.mBuffers, cap);
                this.mFreeBytes = Arrays.copyOf(this.mFreeBytes, cap);
            }
            this.mBuffers[this.mIndex] = bufferx;
            this.mFreeBytes[this.mIndex] = bufferx.getSize() - size;
            this.mBytesInUse += size;
            assert this.mBufferPtr == 0L;
            this.mBufferPtr = bufferx.lock();
            assert this.mBufferPtr != 0L;
            assert bufferx.isLocked();
            assert bufferx.getLockedBuffer() == this.mBufferPtr;
            return this.mBufferPtr;
        }
    }

    @Nonnull
    private static ByteBuffer getMappedBuffer(@Nullable ByteBuffer buffer, long address, int capacity) {
        return buffer != null && MemoryUtil.memAddress0(buffer) == address && buffer.capacity() == capacity ? buffer : MemoryUtil.memByteBuffer(address, capacity);
    }

    private static class IndexPool extends GpuBufferPool {

        public IndexPool(ResourceProvider resourceProvider) {
            super(resourceProvider, 2);
        }

        @Override
        public long makeSpace(Mesh mesh) {
            int indexSize = 2;
            int indexCount = mesh.getIndexCount();
            assert indexCount > 0;
            int totalSize = 2 * indexCount;
            long ptr = this.makeSpace(totalSize, 2);
            if (ptr == 0L) {
                return 0L;
            } else {
                GpuBuffer buffer = this.mBuffers[this.mIndex];
                int offset = (int) (ptr - this.mBufferPtr);
                assert offset % 2 == 0;
                mesh.setIndexBuffer(buffer, offset / 2, indexCount);
                return ptr;
            }
        }

        @Nullable
        @Override
        public ByteBuffer makeWriter(Mesh mesh) {
            int indexSize = 2;
            int indexCount = mesh.getIndexCount();
            assert indexCount > 0;
            int totalSize = 2 * indexCount;
            long ptr = this.makeSpace(totalSize, 2);
            if (ptr == 0L) {
                return null;
            } else {
                GpuBuffer buffer = this.mBuffers[this.mIndex];
                int offset = (int) (ptr - this.mBufferPtr);
                assert offset % 2 == 0;
                mesh.setIndexBuffer(buffer, offset / 2, indexCount);
                ByteBuffer writer = GpuBufferPool.getMappedBuffer(this.mCachedWriter, this.mBufferPtr, buffer.getSize());
                writer.limit(offset + totalSize);
                writer.position(offset);
                this.mCachedWriter = writer;
                return writer;
            }
        }
    }

    private static class InstancePool extends GpuBufferPool {

        public InstancePool(ResourceProvider resourceProvider) {
            super(resourceProvider, 1);
        }

        @Override
        public long makeSpace(Mesh mesh) {
            int instanceSize = mesh.getInstanceSize();
            int instanceCount = mesh.getInstanceCount();
            assert instanceSize > 0 && instanceCount > 0;
            int totalSize = instanceSize * instanceCount;
            long ptr = this.makeSpace(totalSize, instanceSize);
            if (ptr == 0L) {
                return 0L;
            } else {
                GpuBuffer buffer = this.mBuffers[this.mIndex];
                int offset = (int) (ptr - this.mBufferPtr);
                assert offset % instanceSize == 0;
                mesh.setInstanceBuffer(buffer, offset / instanceSize, instanceCount);
                return ptr;
            }
        }

        @Nullable
        @Override
        public ByteBuffer makeWriter(Mesh mesh) {
            int instanceSize = mesh.getInstanceSize();
            int instanceCount = mesh.getInstanceCount();
            assert instanceSize > 0 && instanceCount > 0;
            int totalSize = instanceSize * instanceCount;
            long ptr = this.makeSpace(totalSize, instanceSize);
            if (ptr == 0L) {
                return null;
            } else {
                GpuBuffer buffer = this.mBuffers[this.mIndex];
                int offset = (int) (ptr - this.mBufferPtr);
                assert offset % instanceSize == 0;
                mesh.setInstanceBuffer(buffer, offset / instanceSize, instanceCount);
                ByteBuffer writer = GpuBufferPool.getMappedBuffer(this.mCachedWriter, this.mBufferPtr, buffer.getSize());
                writer.limit(offset + totalSize);
                writer.position(offset);
                this.mCachedWriter = writer;
                return writer;
            }
        }
    }

    private static class VertexPool extends GpuBufferPool {

        public VertexPool(ResourceProvider resourceProvider) {
            super(resourceProvider, 1);
        }

        @Override
        public long makeSpace(Mesh mesh) {
            int vertexSize = mesh.getVertexSize();
            int vertexCount = mesh.getVertexCount();
            assert vertexSize > 0 && vertexCount > 0;
            int totalSize = vertexSize * vertexCount;
            long ptr = this.makeSpace(totalSize, vertexSize);
            if (ptr == 0L) {
                return 0L;
            } else {
                GpuBuffer buffer = this.mBuffers[this.mIndex];
                int offset = (int) (ptr - this.mBufferPtr);
                assert offset % vertexSize == 0;
                mesh.setVertexBuffer(buffer, offset / vertexSize, vertexCount);
                return ptr;
            }
        }

        @Nullable
        @Override
        public ByteBuffer makeWriter(Mesh mesh) {
            int vertexSize = mesh.getVertexSize();
            int vertexCount = mesh.getVertexCount();
            assert vertexSize > 0 && vertexCount > 0;
            int totalSize = vertexSize * vertexCount;
            long ptr = this.makeSpace(totalSize, vertexSize);
            if (ptr == 0L) {
                return null;
            } else {
                GpuBuffer buffer = this.mBuffers[this.mIndex];
                int offset = (int) (ptr - this.mBufferPtr);
                assert offset % vertexSize == 0;
                mesh.setVertexBuffer(buffer, offset / vertexSize, vertexCount);
                ByteBuffer writer = GpuBufferPool.getMappedBuffer(this.mCachedWriter, this.mBufferPtr, buffer.getSize());
                writer.limit(offset + totalSize);
                writer.position(offset);
                this.mCachedWriter = writer;
                return writer;
            }
        }
    }
}