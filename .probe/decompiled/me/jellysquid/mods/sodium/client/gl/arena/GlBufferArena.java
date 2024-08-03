package me.jellysquid.mods.sodium.client.gl.arena;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.jellysquid.mods.sodium.client.gl.arena.staging.StagingBuffer;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBuffer;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferUsage;
import me.jellysquid.mods.sodium.client.gl.buffer.GlMutableBuffer;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;

public class GlBufferArena {

    static final boolean CHECK_ASSERTIONS = false;

    private static final GlBufferUsage BUFFER_USAGE = GlBufferUsage.STATIC_DRAW;

    private static final int RESIZE_FACTOR = 2;

    private int resizeIncrement;

    private final StagingBuffer stagingBuffer;

    private GlMutableBuffer arenaBuffer;

    private GlBufferSegment head;

    private int capacity;

    private int used;

    private final int stride;

    public GlBufferArena(CommandList commands, int initialCapacity, int stride, StagingBuffer stagingBuffer) {
        this.capacity = initialCapacity;
        this.resizeIncrement = initialCapacity / 2;
        this.stride = stride;
        this.head = new GlBufferSegment(this, 0, initialCapacity);
        this.head.setFree(true);
        this.arenaBuffer = commands.createMutableBuffer();
        commands.allocateStorage(this.arenaBuffer, (long) this.capacity * (long) stride, BUFFER_USAGE);
        this.stagingBuffer = stagingBuffer;
    }

    private void resize(CommandList commandList, int newCapacity) {
        if (this.used > newCapacity) {
            throw new UnsupportedOperationException("New capacity must be larger than used size");
        } else {
            this.checkAssertions();
            int tail = newCapacity - this.used;
            List<GlBufferSegment> usedSegments = this.getUsedSegments();
            List<PendingBufferCopyCommand> pendingCopies = this.buildTransferList(usedSegments, tail);
            this.transferSegments(commandList, pendingCopies, newCapacity);
            this.head = new GlBufferSegment(this, 0, tail);
            this.head.setFree(true);
            if (usedSegments.isEmpty()) {
                this.head.setNext(null);
            } else {
                this.head.setNext((GlBufferSegment) usedSegments.get(0));
                this.head.getNext().setPrev(this.head);
            }
            this.checkAssertions();
        }
    }

    private List<PendingBufferCopyCommand> buildTransferList(List<GlBufferSegment> usedSegments, int base) {
        List<PendingBufferCopyCommand> pendingCopies = new ArrayList();
        PendingBufferCopyCommand currentCopyCommand = null;
        int writeOffset = base;
        for (int i = 0; i < usedSegments.size(); i++) {
            GlBufferSegment s = (GlBufferSegment) usedSegments.get(i);
            if (currentCopyCommand != null && currentCopyCommand.readOffset + currentCopyCommand.length == s.getOffset()) {
                currentCopyCommand.length = currentCopyCommand.length + s.getLength();
            } else {
                if (currentCopyCommand != null) {
                    pendingCopies.add(currentCopyCommand);
                }
                currentCopyCommand = new PendingBufferCopyCommand(s.getOffset(), writeOffset, s.getLength());
            }
            s.setOffset(writeOffset);
            if (i + 1 < usedSegments.size()) {
                s.setNext((GlBufferSegment) usedSegments.get(i + 1));
            } else {
                s.setNext(null);
            }
            if (i - 1 < 0) {
                s.setPrev(null);
            } else {
                s.setPrev((GlBufferSegment) usedSegments.get(i - 1));
            }
            writeOffset += s.getLength();
        }
        if (currentCopyCommand != null) {
            pendingCopies.add(currentCopyCommand);
        }
        return pendingCopies;
    }

    private void transferSegments(CommandList commandList, Collection<PendingBufferCopyCommand> list, int capacity) {
        GlMutableBuffer srcBufferObj = this.arenaBuffer;
        GlMutableBuffer dstBufferObj = commandList.createMutableBuffer();
        commandList.allocateStorage(dstBufferObj, (long) capacity * (long) this.stride, BUFFER_USAGE);
        for (PendingBufferCopyCommand cmd : list) {
            commandList.copyBufferSubData(srcBufferObj, dstBufferObj, (long) cmd.readOffset * (long) this.stride, (long) cmd.writeOffset * (long) this.stride, (long) cmd.length * (long) this.stride);
        }
        commandList.deleteBuffer(srcBufferObj);
        this.arenaBuffer = dstBufferObj;
        this.capacity = capacity;
        this.resizeIncrement = this.capacity / 2;
    }

    private ArrayList<GlBufferSegment> getUsedSegments() {
        ArrayList<GlBufferSegment> used = new ArrayList();
        GlBufferSegment seg = this.head;
        while (seg != null) {
            GlBufferSegment next = seg.getNext();
            if (!seg.isFree()) {
                used.add(seg);
            }
            seg = next;
        }
        return used;
    }

    @Deprecated
    public int getDeviceUsedMemory() {
        return this.used * this.stride;
    }

    @Deprecated
    public int getDeviceAllocatedMemory() {
        return this.capacity * this.stride;
    }

    public long getDeviceUsedMemoryL() {
        return (long) this.used * (long) this.stride;
    }

    public long getDeviceAllocatedMemoryL() {
        return (long) this.capacity * (long) this.stride;
    }

    private GlBufferSegment alloc(int size) {
        GlBufferSegment a = this.findFree(size);
        if (a == null) {
            return null;
        } else {
            GlBufferSegment result;
            if (a.getLength() == size) {
                a.setFree(false);
                result = a;
            } else {
                GlBufferSegment b = new GlBufferSegment(this, a.getEnd() - size, size);
                b.setNext(a.getNext());
                b.setPrev(a);
                if (b.getNext() != null) {
                    b.getNext().setPrev(b);
                }
                a.setLength(a.getLength() - size);
                a.setNext(b);
                result = b;
            }
            this.used = this.used + result.getLength();
            this.checkAssertions();
            return result;
        }
    }

    private GlBufferSegment findFree(int size) {
        GlBufferSegment entry = this.head;
        GlBufferSegment best;
        for (best = null; entry != null; entry = entry.getNext()) {
            if (entry.isFree()) {
                if (entry.getLength() == size) {
                    return entry;
                }
                if (entry.getLength() >= size && (best == null || best.getLength() > entry.getLength())) {
                    best = entry;
                }
            }
        }
        return best;
    }

    public void free(GlBufferSegment entry) {
        if (entry.isFree()) {
            throw new IllegalStateException("Already freed");
        } else {
            entry.setFree(true);
            this.used = this.used - entry.getLength();
            GlBufferSegment next = entry.getNext();
            if (next != null && next.isFree()) {
                entry.mergeInto(next);
            }
            GlBufferSegment prev = entry.getPrev();
            if (prev != null && prev.isFree()) {
                prev.mergeInto(entry);
            }
            this.checkAssertions();
        }
    }

    public void delete(CommandList commands) {
        commands.deleteBuffer(this.arenaBuffer);
    }

    public boolean isEmpty() {
        return this.used <= 0;
    }

    public GlBuffer getBufferObject() {
        return this.arenaBuffer;
    }

    public boolean upload(CommandList commandList, Stream<PendingUpload> stream) {
        GlBuffer buffer = this.arenaBuffer;
        List<PendingUpload> queue = (List<PendingUpload>) stream.collect(Collectors.toCollection(LinkedList::new));
        this.tryUploads(commandList, queue);
        if (!queue.isEmpty()) {
            int remainingElements = (int) (queue.stream().mapToLong(upload -> (long) upload.getDataBuffer().getLength()).sum() / (long) this.stride);
            this.ensureCapacity(commandList, remainingElements);
            this.tryUploads(commandList, queue);
            if (!queue.isEmpty()) {
                throw new RuntimeException("Failed to upload all buffers");
            }
        }
        return this.arenaBuffer != buffer;
    }

    private void tryUploads(CommandList commandList, List<PendingUpload> queue) {
        queue.removeIf(upload -> this.tryUpload(commandList, upload));
        this.stagingBuffer.flush(commandList);
    }

    private boolean tryUpload(CommandList commandList, PendingUpload upload) {
        ByteBuffer data = upload.getDataBuffer().getDirectBuffer();
        int elementCount = data.remaining() / this.stride;
        GlBufferSegment dst = this.alloc(elementCount);
        if (dst == null) {
            return false;
        } else {
            this.stagingBuffer.enqueueCopy(commandList, data, this.arenaBuffer, (long) dst.getOffset() * (long) this.stride);
            upload.setResult(dst);
            return true;
        }
    }

    public void ensureCapacity(CommandList commandList, int elementCount) {
        int elementsNeeded = elementCount - (this.capacity - this.used);
        this.resize(commandList, Math.max(this.capacity + this.resizeIncrement, this.capacity + elementsNeeded));
    }

    private void checkAssertions() {
    }

    private void checkAssertions0() {
        GlBufferSegment seg = this.head;
        int used = 0;
        while (seg != null) {
            if (seg.getOffset() < 0) {
                throw new IllegalStateException("segment.start < 0: out of bounds");
            }
            if (seg.getEnd() > this.capacity) {
                throw new IllegalStateException("segment.end > arena.capacity: out of bounds");
            }
            if (!seg.isFree()) {
                used += seg.getLength();
            }
            GlBufferSegment next = seg.getNext();
            if (next != null) {
                if (next.getOffset() < seg.getEnd()) {
                    throw new IllegalStateException("segment.next.start < segment.end: overlapping segments (corrupted)");
                }
                if (next.getOffset() > seg.getEnd()) {
                    throw new IllegalStateException("segment.next.start > segment.end: not truly connected (sparsity error)");
                }
                if (next.isFree() && next.getNext() != null && next.getNext().isFree()) {
                    throw new IllegalStateException("segment.free && segment.next.free: not merged consecutive segments");
                }
            }
            GlBufferSegment prev = seg.getPrev();
            if (prev != null) {
                if (prev.getEnd() > seg.getOffset()) {
                    throw new IllegalStateException("segment.prev.end > segment.start: overlapping segments (corrupted)");
                }
                if (prev.getEnd() < seg.getOffset()) {
                    throw new IllegalStateException("segment.prev.end < segment.start: not truly connected (sparsity error)");
                }
                if (prev.isFree() && prev.getPrev() != null && prev.getPrev().isFree()) {
                    throw new IllegalStateException("segment.free && segment.prev.free: not merged consecutive segments");
                }
            }
            seg = next;
        }
        if (this.used < 0) {
            throw new IllegalStateException("arena.used < 0: failure to track");
        } else if (this.used > this.capacity) {
            throw new IllegalStateException("arena.used > arena.capacity: failure to track");
        } else if (this.used != used) {
            throw new IllegalStateException("arena.used is invalid");
        }
    }
}