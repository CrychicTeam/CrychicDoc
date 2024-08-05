package net.raphimc.immediatelyfast.feature.fast_buffer_upload;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.lwjgl.opengl.GL32C;
import org.lwjgl.opengl.GL42C;
import org.lwjgl.opengl.GL45C;
import org.lwjgl.system.MemoryUtil;

public class PersistentMappedStreamingBuffer {

    private final int id;

    private final long size;

    private final long addr;

    private final long syncSectionSize;

    private final List<PersistentMappedStreamingBuffer.Batch> batches = new ArrayList();

    private final long[] fences = new long[8];

    private long batchOffset;

    private long offset;

    public PersistentMappedStreamingBuffer(long size) {
        this.id = GL45C.glCreateBuffers();
        this.size = size;
        this.syncSectionSize = size / (long) this.fences.length;
        Arrays.fill(this.fences, -1L);
        int flags = 66;
        if (ImmediatelyFast.config.fast_buffer_upload_explicit_flush) {
            flags |= 16;
        } else {
            flags |= 128;
        }
        GL45C.glNamedBufferStorage(this.id, size, flags & -17);
        this.addr = GL45C.nglMapNamedBufferRange(this.id, 0L, size, flags | 32 | 4);
    }

    public void addUpload(int destinationId, ByteBuffer data) {
        int dataSize = data.remaining();
        if ((long) dataSize > this.size) {
            throw new RuntimeException("Data size is bigger than buffer size");
        } else if (dataSize <= 0) {
            throw new RuntimeException("Data is empty");
        } else {
            int oldFenceIdx = (int) (this.offset / this.syncSectionSize);
            if (oldFenceIdx >= this.fences.length) {
                oldFenceIdx = this.fences.length - 1;
            }
            if (this.offset + (long) dataSize > this.size) {
                this.flush();
                if ((long) dataSize >= this.offset) {
                    long fence = this.fences[oldFenceIdx];
                    if (fence != -1L) {
                        GL32C.glClientWaitSync(fence, 1, -1L);
                    } else {
                        GL32C.glFinish();
                    }
                } else if (this.fences[oldFenceIdx] == -1L) {
                    this.fences[oldFenceIdx] = GL32C.glFenceSync(37143, 0);
                }
                oldFenceIdx = -1;
                this.offset = 0L;
                this.batchOffset = 0L;
            }
            long newOffset = this.offset + (long) dataSize;
            int newFenceIdx = (int) ((newOffset - 1L) / this.syncSectionSize);
            long fence = -1L;
            for (int i = newFenceIdx; i > oldFenceIdx && fence == -1L; i--) {
                fence = this.fences[i];
            }
            if (fence != -1L) {
                GL32C.glClientWaitSync(fence, 1, -1L);
            }
            MemoryUtil.memCopy(MemoryUtil.memAddress(data), this.addr + this.offset, (long) dataSize);
            this.batches.add(new PersistentMappedStreamingBuffer.Batch(destinationId, dataSize));
            this.offset = newOffset;
        }
    }

    public void flush() {
        if (!this.batches.isEmpty()) {
            if (ImmediatelyFast.config.fast_buffer_upload_explicit_flush) {
                GL45C.glFlushMappedNamedBufferRange(this.id, this.batchOffset, this.offset - this.batchOffset);
                GL42C.glMemoryBarrier(16384);
            }
            int oldFenceIdx = (int) (this.batchOffset / this.syncSectionSize);
            for (PersistentMappedStreamingBuffer.Batch batch : this.batches) {
                GL45C.glCopyNamedBufferSubData(this.id, batch.destinationId, this.batchOffset, 0L, (long) batch.size);
                this.batchOffset = this.batchOffset + (long) batch.size;
            }
            this.batches.clear();
            GL42C.glMemoryBarrier(512);
            int nextFenceIdx = (int) (this.batchOffset / this.syncSectionSize);
            int newFenceIdx = (int) ((this.batchOffset - 1L) / this.syncSectionSize);
            for (int i = oldFenceIdx; i <= newFenceIdx; i++) {
                long fence = this.fences[i];
                if (fence != -1L) {
                    GL32C.glDeleteSync(fence);
                    this.fences[i] = -1L;
                }
            }
            if (oldFenceIdx != nextFenceIdx) {
                this.fences[oldFenceIdx] = GL32C.glFenceSync(37143, 0);
            }
        }
    }

    public long getSize() {
        return this.size;
    }

    public long getOffset() {
        return this.offset;
    }

    private static record Batch(int destinationId, int size) {
    }
}