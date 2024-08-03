package me.jellysquid.mods.sodium.client.gl.arena.staging;

import it.unimi.dsi.fastutil.PriorityQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBuffer;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferMapFlags;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferMapping;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferStorageFlags;
import me.jellysquid.mods.sodium.client.gl.buffer.GlImmutableBuffer;
import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.gl.functions.BufferStorageFunctions;
import me.jellysquid.mods.sodium.client.gl.sync.GlFence;
import me.jellysquid.mods.sodium.client.gl.util.EnumBitField;
import me.jellysquid.mods.sodium.client.util.MathUtil;

public class MappedStagingBuffer implements StagingBuffer {

    private static final EnumBitField<GlBufferStorageFlags> STORAGE_FLAGS = EnumBitField.of(GlBufferStorageFlags.PERSISTENT, GlBufferStorageFlags.CLIENT_STORAGE, GlBufferStorageFlags.MAP_WRITE);

    private static final EnumBitField<GlBufferMapFlags> MAP_FLAGS = EnumBitField.of(GlBufferMapFlags.PERSISTENT, GlBufferMapFlags.INVALIDATE_BUFFER, GlBufferMapFlags.WRITE, GlBufferMapFlags.EXPLICIT_FLUSH);

    private final FallbackStagingBuffer fallbackStagingBuffer;

    private final MappedStagingBuffer.MappedBuffer mappedBuffer;

    private final PriorityQueue<MappedStagingBuffer.CopyCommand> pendingCopies = new ObjectArrayFIFOQueue();

    private final PriorityQueue<MappedStagingBuffer.FencedMemoryRegion> fencedRegions = new ObjectArrayFIFOQueue();

    private int start = 0;

    private int pos = 0;

    private final int capacity;

    private int remaining;

    public MappedStagingBuffer(CommandList commandList) {
        this(commandList, 16777216);
    }

    public MappedStagingBuffer(CommandList commandList, int capacity) {
        GlImmutableBuffer buffer = commandList.createImmutableBuffer((long) capacity, STORAGE_FLAGS);
        GlBufferMapping map = commandList.mapBuffer(buffer, 0L, (long) capacity, MAP_FLAGS);
        this.mappedBuffer = new MappedStagingBuffer.MappedBuffer(buffer, map);
        this.fallbackStagingBuffer = new FallbackStagingBuffer(commandList);
        this.capacity = capacity;
        this.remaining = this.capacity;
    }

    public static boolean isSupported(RenderDevice instance) {
        return instance.getDeviceFunctions().getBufferStorageFunctions() != BufferStorageFunctions.NONE;
    }

    @Override
    public void enqueueCopy(CommandList commandList, ByteBuffer data, GlBuffer dst, long writeOffset) {
        int length = data.remaining();
        if (length > this.remaining) {
            this.fallbackStagingBuffer.enqueueCopy(commandList, data, dst, writeOffset);
        } else {
            int remaining = this.capacity - this.pos;
            if (length > remaining) {
                int split = length - remaining;
                this.addTransfer(data.slice(0, remaining), dst, (long) this.pos, writeOffset);
                this.addTransfer(data.slice(remaining, split), dst, 0L, writeOffset + (long) remaining);
                this.pos = split;
            } else {
                this.addTransfer(data, dst, (long) this.pos, writeOffset);
                this.pos += length;
            }
            this.remaining -= length;
        }
    }

    private void addTransfer(ByteBuffer data, GlBuffer dst, long readOffset, long writeOffset) {
        this.mappedBuffer.map.write(data, (int) readOffset);
        this.pendingCopies.enqueue(new MappedStagingBuffer.CopyCommand(dst, readOffset, writeOffset, (long) data.remaining()));
    }

    @Override
    public void flush(CommandList commandList) {
        if (!this.pendingCopies.isEmpty()) {
            if (this.pos < this.start) {
                commandList.flushMappedRange(this.mappedBuffer.map, this.start, this.capacity - this.start);
                commandList.flushMappedRange(this.mappedBuffer.map, 0, this.pos);
            } else {
                commandList.flushMappedRange(this.mappedBuffer.map, this.start, this.pos - this.start);
            }
            int bytes = 0;
            for (MappedStagingBuffer.CopyCommand command : consolidateCopies(this.pendingCopies)) {
                bytes = (int) ((long) bytes + command.bytes);
                commandList.copyBufferSubData(this.mappedBuffer.buffer, command.buffer, command.readOffset, command.writeOffset, command.bytes);
            }
            this.fencedRegions.enqueue(new MappedStagingBuffer.FencedMemoryRegion(commandList.createFence(), bytes));
            this.start = this.pos;
        }
    }

    private static List<MappedStagingBuffer.CopyCommand> consolidateCopies(PriorityQueue<MappedStagingBuffer.CopyCommand> queue) {
        List<MappedStagingBuffer.CopyCommand> merged = new ArrayList();
        MappedStagingBuffer.CopyCommand last = null;
        while (!queue.isEmpty()) {
            MappedStagingBuffer.CopyCommand command = (MappedStagingBuffer.CopyCommand) queue.dequeue();
            if (last != null && last.buffer == command.buffer && last.writeOffset + last.bytes == command.writeOffset && last.readOffset + last.bytes == command.readOffset) {
                last.bytes = last.bytes + command.bytes;
            } else {
                merged.add(last = new MappedStagingBuffer.CopyCommand(command));
            }
        }
        return merged;
    }

    @Override
    public void delete(CommandList commandList) {
        this.mappedBuffer.delete(commandList);
        this.fallbackStagingBuffer.delete(commandList);
        this.pendingCopies.clear();
    }

    @Override
    public void flip() {
        while (!this.fencedRegions.isEmpty()) {
            MappedStagingBuffer.FencedMemoryRegion region = (MappedStagingBuffer.FencedMemoryRegion) this.fencedRegions.first();
            GlFence fence = region.fence();
            if (fence.isCompleted()) {
                fence.delete();
                this.fencedRegions.dequeue();
                this.remaining = this.remaining + region.length();
                continue;
            }
            break;
        }
    }

    public String toString() {
        return "Mapped (%s/%s MiB)".formatted(MathUtil.toMib((long) this.remaining), MathUtil.toMib((long) this.capacity));
    }

    private static final class CopyCommand {

        private final GlBuffer buffer;

        private final long readOffset;

        private final long writeOffset;

        private long bytes;

        private CopyCommand(GlBuffer buffer, long readOffset, long writeOffset, long bytes) {
            this.buffer = buffer;
            this.readOffset = readOffset;
            this.writeOffset = writeOffset;
            this.bytes = bytes;
        }

        public CopyCommand(MappedStagingBuffer.CopyCommand command) {
            this.buffer = command.buffer;
            this.writeOffset = command.writeOffset;
            this.readOffset = command.readOffset;
            this.bytes = command.bytes;
        }
    }

    private static record FencedMemoryRegion(GlFence fence, int length) {
    }

    private static record MappedBuffer(GlImmutableBuffer buffer, GlBufferMapping map) {

        public void delete(CommandList commandList) {
            commandList.unmap(this.map);
            commandList.deleteBuffer(this.buffer);
        }
    }
}