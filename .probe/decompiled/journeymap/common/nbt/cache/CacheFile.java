package journeymap.common.nbt.cache;

import com.google.common.annotations.VisibleForTesting;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionBitmap;
import net.minecraft.world.level.chunk.storage.RegionFileVersion;

public class CacheFile implements AutoCloseable {

    private static final int SECTOR_BYTES = 4096;

    @VisibleForTesting
    protected static final int SECTOR_INTS = 1024;

    private static final int CHUNK_HEADER_SIZE = 5;

    private static final int HEADER_OFFSET = 0;

    private static final ByteBuffer PADDING_BUFFER = ByteBuffer.allocateDirect(1);

    private static final int CHUNK_NOT_PRESENT = 0;

    private final FileChannel file;

    final RegionFileVersion fileVersion;

    private final ByteBuffer header = ByteBuffer.allocateDirect(8192);

    private final IntBuffer offsets;

    private final IntBuffer timestamps;

    @VisibleForTesting
    protected final RegionBitmap usedSectors = new RegionBitmap();

    public CacheFile(Path path, Path folderPath, boolean async) throws IOException {
        this(path, folderPath, RegionFileVersion.VERSION_DEFLATE, async);
    }

    public CacheFile(Path path, Path folderPath, RegionFileVersion fileVersion, boolean async) throws IOException {
        this.fileVersion = fileVersion;
        if (!Files.isDirectory(folderPath, new LinkOption[0])) {
            throw new IllegalArgumentException("Expected directory, got " + folderPath.toAbsolutePath());
        } else {
            this.offsets = this.header.asIntBuffer();
            this.offsets.limit(1024);
            this.header.position(4096);
            this.timestamps = this.header.asIntBuffer();
            if (async) {
                this.file = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC);
            } else {
                this.file = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
            }
            this.usedSectors.force(0, 2);
            this.header.position(0);
            int readBytes = this.file.read(this.header, 0L);
            if (readBytes != -1) {
                long size = Files.size(path);
                for (int k = 0; k < 1024; k++) {
                    int offsetIndex = this.offsets.get(k);
                    if (offsetIndex != 0) {
                        int sectorNumber = getSectorNumber(offsetIndex);
                        int numSectors = getNumSectors(offsetIndex);
                        if (sectorNumber >= 2 && numSectors != 0 && (long) sectorNumber * 4096L <= size) {
                            this.usedSectors.force(sectorNumber, numSectors);
                        } else {
                            this.offsets.put(k, 0);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    public synchronized DataInputStream getChunkDataInputStream(ChunkPos chunkPos) throws IOException {
        int offset = this.getOffset(chunkPos);
        if (offset != 0) {
            int sectorNumber = getSectorNumber(offset);
            int numSectors = getNumSectors(offset);
            int bytes = numSectors * 4096;
            ByteBuffer bytebuffer = ByteBuffer.allocate(bytes);
            this.file.read(bytebuffer, (long) sectorNumber * 4096L);
            bytebuffer.flip();
            if (bytebuffer.remaining() >= 5) {
                int size = bytebuffer.getInt();
                byte buf = bytebuffer.get();
                if (size != 0) {
                    int position = size - 1;
                    if (position <= bytebuffer.remaining() && position >= 0) {
                        return this.createChunkInputStream(buf, createStream(bytebuffer, position));
                    }
                }
            }
        }
        return null;
    }

    private static int getTimestamp() {
        return (int) (Util.getEpochMillis() / 1000L);
    }

    @Nullable
    private DataInputStream createChunkInputStream(byte version, InputStream inputStream) throws IOException {
        RegionFileVersion fileVersion = RegionFileVersion.fromId(version);
        return fileVersion != null ? new DataInputStream(new BufferedInputStream(fileVersion.wrap(inputStream))) : null;
    }

    private static ByteArrayInputStream createStream(ByteBuffer buf, int length) {
        return new ByteArrayInputStream(buf.array(), buf.position(), length);
    }

    private int packSectorOffset(int allocated, int sectors) {
        return allocated << 8 | sectors;
    }

    private static int getNumSectors(int offset) {
        return offset & 0xFF;
    }

    private static int getSectorNumber(int offset) {
        return offset >> 8 & 16777215;
    }

    private static int sizeToSectors(int remaining) {
        return (remaining + 4096 - 1) / 4096;
    }

    public DataOutputStream getChunkDataOutputStream(ChunkPos pos) throws IOException {
        return new DataOutputStream(this.fileVersion.wrap(new CacheFile.ChunkBuffer(pos)));
    }

    public void flush() throws IOException {
        this.file.force(true);
    }

    public void clear(ChunkPos pos) throws IOException {
        int offsetIndex = getOffsetIndex(pos);
        int offset = this.offsets.get(offsetIndex);
        if (offset != 0) {
            this.offsets.put(offsetIndex, 0);
            this.timestamps.put(offsetIndex, getTimestamp());
            this.writeHeader();
            this.usedSectors.free(getSectorNumber(offset), getNumSectors(offset));
        }
    }

    protected synchronized void write(ChunkPos pos, ByteBuffer buffer) throws IOException {
        int offsetIndex = getOffsetIndex(pos);
        int offset = this.offsets.get(offsetIndex);
        int sectorNumber = getSectorNumber(offset);
        int numSectors = getNumSectors(offset);
        int remaining = buffer.remaining();
        int sectors = sizeToSectors(remaining);
        int allocated = this.usedSectors.allocate(sectors);
        this.file.write(buffer, (long) allocated * 4096L);
        this.offsets.put(offsetIndex, this.packSectorOffset(allocated, sectors));
        this.timestamps.put(offsetIndex, getTimestamp());
        this.writeHeader();
        if (sectorNumber != 0) {
            this.usedSectors.free(sectorNumber, numSectors);
        }
    }

    private void writeHeader() throws IOException {
        this.header.position(0);
        this.file.write(this.header, 0L);
    }

    private int getOffset(ChunkPos pos) {
        return this.offsets.get(getOffsetIndex(pos));
    }

    public boolean hasChunk(ChunkPos pos) {
        return this.getOffset(pos) != 0;
    }

    private static int getOffsetIndex(ChunkPos pos) {
        return pos.getRegionLocalX() + pos.getRegionLocalZ() * 32;
    }

    public void close() throws IOException {
        try {
            this.padToFullSector();
        } finally {
            try {
                this.file.force(true);
            } finally {
                this.file.close();
            }
        }
    }

    private void padToFullSector() throws IOException {
        int size = (int) this.file.size();
        int sectorSize = sizeToSectors(size) * 4096;
        if (size != sectorSize) {
            ByteBuffer bytebuffer = PADDING_BUFFER.duplicate();
            bytebuffer.position(0);
            this.file.write(bytebuffer, (long) (sectorSize - 1));
        }
    }

    class ChunkBuffer extends ByteArrayOutputStream {

        private final ChunkPos pos;

        public ChunkBuffer(ChunkPos pos) {
            super(8096);
            super.write(0);
            super.write(0);
            super.write(0);
            super.write(0);
            super.write(CacheFile.this.fileVersion.getId());
            this.pos = pos;
        }

        public void close() throws IOException {
            ByteBuffer bytebuffer = ByteBuffer.wrap(this.buf, 0, this.count);
            bytebuffer.putInt(0, this.count - 5 + 1);
            CacheFile.this.write(this.pos, bytebuffer);
        }
    }
}