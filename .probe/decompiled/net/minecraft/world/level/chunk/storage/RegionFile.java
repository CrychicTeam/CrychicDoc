package net.minecraft.world.level.chunk.storage;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.logging.LogUtils;
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
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.world.level.ChunkPos;
import org.slf4j.Logger;

public class RegionFile implements AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int SECTOR_BYTES = 4096;

    @VisibleForTesting
    protected static final int SECTOR_INTS = 1024;

    private static final int CHUNK_HEADER_SIZE = 5;

    private static final int HEADER_OFFSET = 0;

    private static final ByteBuffer PADDING_BUFFER = ByteBuffer.allocateDirect(1);

    private static final String EXTERNAL_FILE_EXTENSION = ".mcc";

    private static final int EXTERNAL_STREAM_FLAG = 128;

    private static final int EXTERNAL_CHUNK_THRESHOLD = 256;

    private static final int CHUNK_NOT_PRESENT = 0;

    private final FileChannel file;

    private final Path externalFileDir;

    final RegionFileVersion version;

    private final ByteBuffer header = ByteBuffer.allocateDirect(8192);

    private final IntBuffer offsets;

    private final IntBuffer timestamps;

    @VisibleForTesting
    protected final RegionBitmap usedSectors = new RegionBitmap();

    public RegionFile(Path path0, Path path1, boolean boolean2) throws IOException {
        this(path0, path1, RegionFileVersion.VERSION_DEFLATE, boolean2);
    }

    public RegionFile(Path path0, Path path1, RegionFileVersion regionFileVersion2, boolean boolean3) throws IOException {
        this.version = regionFileVersion2;
        if (!Files.isDirectory(path1, new LinkOption[0])) {
            throw new IllegalArgumentException("Expected directory, got " + path1.toAbsolutePath());
        } else {
            this.externalFileDir = path1;
            this.offsets = this.header.asIntBuffer();
            this.offsets.limit(1024);
            this.header.position(4096);
            this.timestamps = this.header.asIntBuffer();
            if (boolean3) {
                this.file = FileChannel.open(path0, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC);
            } else {
                this.file = FileChannel.open(path0, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
            }
            this.usedSectors.force(0, 2);
            this.header.position(0);
            int $$4 = this.file.read(this.header, 0L);
            if ($$4 != -1) {
                if ($$4 != 8192) {
                    LOGGER.warn("Region file {} has truncated header: {}", path0, $$4);
                }
                long $$5 = Files.size(path0);
                for (int $$6 = 0; $$6 < 1024; $$6++) {
                    int $$7 = this.offsets.get($$6);
                    if ($$7 != 0) {
                        int $$8 = getSectorNumber($$7);
                        int $$9 = getNumSectors($$7);
                        if ($$8 < 2) {
                            LOGGER.warn("Region file {} has invalid sector at index: {}; sector {} overlaps with header", new Object[] { path0, $$6, $$8 });
                            this.offsets.put($$6, 0);
                        } else if ($$9 == 0) {
                            LOGGER.warn("Region file {} has an invalid sector at index: {}; size has to be > 0", path0, $$6);
                            this.offsets.put($$6, 0);
                        } else if ((long) $$8 * 4096L > $$5) {
                            LOGGER.warn("Region file {} has an invalid sector at index: {}; sector {} is out of bounds", new Object[] { path0, $$6, $$8 });
                            this.offsets.put($$6, 0);
                        } else {
                            this.usedSectors.force($$8, $$9);
                        }
                    }
                }
            }
        }
    }

    private Path getExternalChunkPath(ChunkPos chunkPos0) {
        String $$1 = "c." + chunkPos0.x + "." + chunkPos0.z + ".mcc";
        return this.externalFileDir.resolve($$1);
    }

    @Nullable
    public synchronized DataInputStream getChunkDataInputStream(ChunkPos chunkPos0) throws IOException {
        int $$1 = this.getOffset(chunkPos0);
        if ($$1 == 0) {
            return null;
        } else {
            int $$2 = getSectorNumber($$1);
            int $$3 = getNumSectors($$1);
            int $$4 = $$3 * 4096;
            ByteBuffer $$5 = ByteBuffer.allocate($$4);
            this.file.read($$5, (long) ($$2 * 4096));
            $$5.flip();
            if ($$5.remaining() < 5) {
                LOGGER.error("Chunk {} header is truncated: expected {} but read {}", new Object[] { chunkPos0, $$4, $$5.remaining() });
                return null;
            } else {
                int $$6 = $$5.getInt();
                byte $$7 = $$5.get();
                if ($$6 == 0) {
                    LOGGER.warn("Chunk {} is allocated, but stream is missing", chunkPos0);
                    return null;
                } else {
                    int $$8 = $$6 - 1;
                    if (isExternalStreamChunk($$7)) {
                        if ($$8 != 0) {
                            LOGGER.warn("Chunk has both internal and external streams");
                        }
                        return this.createExternalChunkInputStream(chunkPos0, getExternalChunkVersion($$7));
                    } else if ($$8 > $$5.remaining()) {
                        LOGGER.error("Chunk {} stream is truncated: expected {} but read {}", new Object[] { chunkPos0, $$8, $$5.remaining() });
                        return null;
                    } else if ($$8 < 0) {
                        LOGGER.error("Declared size {} of chunk {} is negative", $$6, chunkPos0);
                        return null;
                    } else {
                        return this.createChunkInputStream(chunkPos0, $$7, createStream($$5, $$8));
                    }
                }
            }
        }
    }

    private static int getTimestamp() {
        return (int) (Util.getEpochMillis() / 1000L);
    }

    private static boolean isExternalStreamChunk(byte byte0) {
        return (byte0 & 128) != 0;
    }

    private static byte getExternalChunkVersion(byte byte0) {
        return (byte) (byte0 & -129);
    }

    @Nullable
    private DataInputStream createChunkInputStream(ChunkPos chunkPos0, byte byte1, InputStream inputStream2) throws IOException {
        RegionFileVersion $$3 = RegionFileVersion.fromId(byte1);
        if ($$3 == null) {
            LOGGER.error("Chunk {} has invalid chunk stream version {}", chunkPos0, byte1);
            return null;
        } else {
            return new DataInputStream($$3.wrap(inputStream2));
        }
    }

    @Nullable
    private DataInputStream createExternalChunkInputStream(ChunkPos chunkPos0, byte byte1) throws IOException {
        Path $$2 = this.getExternalChunkPath(chunkPos0);
        if (!Files.isRegularFile($$2, new LinkOption[0])) {
            LOGGER.error("External chunk path {} is not file", $$2);
            return null;
        } else {
            return this.createChunkInputStream(chunkPos0, byte1, Files.newInputStream($$2));
        }
    }

    private static ByteArrayInputStream createStream(ByteBuffer byteBuffer0, int int1) {
        return new ByteArrayInputStream(byteBuffer0.array(), byteBuffer0.position(), int1);
    }

    private int packSectorOffset(int int0, int int1) {
        return int0 << 8 | int1;
    }

    private static int getNumSectors(int int0) {
        return int0 & 0xFF;
    }

    private static int getSectorNumber(int int0) {
        return int0 >> 8 & 16777215;
    }

    private static int sizeToSectors(int int0) {
        return (int0 + 4096 - 1) / 4096;
    }

    public boolean doesChunkExist(ChunkPos chunkPos0) {
        int $$1 = this.getOffset(chunkPos0);
        if ($$1 == 0) {
            return false;
        } else {
            int $$2 = getSectorNumber($$1);
            int $$3 = getNumSectors($$1);
            ByteBuffer $$4 = ByteBuffer.allocate(5);
            try {
                this.file.read($$4, (long) ($$2 * 4096));
                $$4.flip();
                if ($$4.remaining() != 5) {
                    return false;
                } else {
                    int $$5 = $$4.getInt();
                    byte $$6 = $$4.get();
                    if (isExternalStreamChunk($$6)) {
                        if (!RegionFileVersion.isValidVersion(getExternalChunkVersion($$6))) {
                            return false;
                        }
                        if (!Files.isRegularFile(this.getExternalChunkPath(chunkPos0), new LinkOption[0])) {
                            return false;
                        }
                    } else {
                        if (!RegionFileVersion.isValidVersion($$6)) {
                            return false;
                        }
                        if ($$5 == 0) {
                            return false;
                        }
                        int $$7 = $$5 - 1;
                        if ($$7 < 0 || $$7 > 4096 * $$3) {
                            return false;
                        }
                    }
                    return true;
                }
            } catch (IOException var9) {
                return false;
            }
        }
    }

    public DataOutputStream getChunkDataOutputStream(ChunkPos chunkPos0) throws IOException {
        return new DataOutputStream(this.version.wrap(new RegionFile.ChunkBuffer(chunkPos0)));
    }

    public void flush() throws IOException {
        this.file.force(true);
    }

    public void clear(ChunkPos chunkPos0) throws IOException {
        int $$1 = getOffsetIndex(chunkPos0);
        int $$2 = this.offsets.get($$1);
        if ($$2 != 0) {
            this.offsets.put($$1, 0);
            this.timestamps.put($$1, getTimestamp());
            this.writeHeader();
            Files.deleteIfExists(this.getExternalChunkPath(chunkPos0));
            this.usedSectors.free(getSectorNumber($$2), getNumSectors($$2));
        }
    }

    protected synchronized void write(ChunkPos chunkPos0, ByteBuffer byteBuffer1) throws IOException {
        int $$2 = getOffsetIndex(chunkPos0);
        int $$3 = this.offsets.get($$2);
        int $$4 = getSectorNumber($$3);
        int $$5 = getNumSectors($$3);
        int $$6 = byteBuffer1.remaining();
        int $$7 = sizeToSectors($$6);
        int $$9;
        RegionFile.CommitOp $$10;
        if ($$7 >= 256) {
            Path $$8 = this.getExternalChunkPath(chunkPos0);
            LOGGER.warn("Saving oversized chunk {} ({} bytes} to external file {}", new Object[] { chunkPos0, $$6, $$8 });
            $$7 = 1;
            $$9 = this.usedSectors.allocate($$7);
            $$10 = this.writeToExternalFile($$8, byteBuffer1);
            ByteBuffer $$11 = this.createExternalStub();
            this.file.write($$11, (long) ($$9 * 4096));
        } else {
            $$9 = this.usedSectors.allocate($$7);
            $$10 = () -> Files.deleteIfExists(this.getExternalChunkPath(chunkPos0));
            this.file.write(byteBuffer1, (long) ($$9 * 4096));
        }
        this.offsets.put($$2, this.packSectorOffset($$9, $$7));
        this.timestamps.put($$2, getTimestamp());
        this.writeHeader();
        $$10.run();
        if ($$4 != 0) {
            this.usedSectors.free($$4, $$5);
        }
    }

    private ByteBuffer createExternalStub() {
        ByteBuffer $$0 = ByteBuffer.allocate(5);
        $$0.putInt(1);
        $$0.put((byte) (this.version.getId() | 128));
        $$0.flip();
        return $$0;
    }

    private RegionFile.CommitOp writeToExternalFile(Path path0, ByteBuffer byteBuffer1) throws IOException {
        Path $$2 = Files.createTempFile(this.externalFileDir, "tmp", null);
        FileChannel $$3 = FileChannel.open($$2, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        try {
            byteBuffer1.position(5);
            $$3.write(byteBuffer1);
        } catch (Throwable var8) {
            if ($$3 != null) {
                try {
                    $$3.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }
            throw var8;
        }
        if ($$3 != null) {
            $$3.close();
        }
        return () -> Files.move($$2, path0, StandardCopyOption.REPLACE_EXISTING);
    }

    private void writeHeader() throws IOException {
        this.header.position(0);
        this.file.write(this.header, 0L);
    }

    private int getOffset(ChunkPos chunkPos0) {
        return this.offsets.get(getOffsetIndex(chunkPos0));
    }

    public boolean hasChunk(ChunkPos chunkPos0) {
        return this.getOffset(chunkPos0) != 0;
    }

    private static int getOffsetIndex(ChunkPos chunkPos0) {
        return chunkPos0.getRegionLocalX() + chunkPos0.getRegionLocalZ() * 32;
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
        int $$0 = (int) this.file.size();
        int $$1 = sizeToSectors($$0) * 4096;
        if ($$0 != $$1) {
            ByteBuffer $$2 = PADDING_BUFFER.duplicate();
            $$2.position(0);
            this.file.write($$2, (long) ($$1 - 1));
        }
    }

    class ChunkBuffer extends ByteArrayOutputStream {

        private final ChunkPos pos;

        public ChunkBuffer(ChunkPos chunkPos0) {
            super(8096);
            super.write(0);
            super.write(0);
            super.write(0);
            super.write(0);
            super.write(RegionFile.this.version.getId());
            this.pos = chunkPos0;
        }

        public void close() throws IOException {
            ByteBuffer $$0 = ByteBuffer.wrap(this.buf, 0, this.count);
            $$0.putInt(0, this.count - 5 + 1);
            RegionFile.this.write(this.pos, $$0);
        }
    }

    interface CommitOp {

        void run() throws IOException;
    }
}