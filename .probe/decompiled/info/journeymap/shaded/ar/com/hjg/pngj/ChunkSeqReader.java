package info.journeymap.shaded.ar.com.hjg.pngj;

import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class ChunkSeqReader implements IBytesConsumer {

    protected static final int SIGNATURE_LEN = 8;

    protected final boolean withSignature;

    private byte[] buf0 = new byte[8];

    private int buf0len = 0;

    private boolean signatureDone = false;

    private boolean done = false;

    private int chunkCount = 0;

    private long bytesCount = 0L;

    private DeflatedChunksSet curReaderDeflatedSet;

    private ChunkReader curChunkReader;

    private long idatBytes;

    public ChunkSeqReader() {
        this(true);
    }

    public ChunkSeqReader(boolean withSignature) {
        this.withSignature = withSignature;
        this.signatureDone = !withSignature;
    }

    @Override
    public int consume(byte[] buffer, int offset, int len) {
        if (this.done) {
            return -1;
        } else if (len == 0) {
            return 0;
        } else if (len < 0) {
            throw new PngjInputException("Bad len: " + len);
        } else {
            int processed = 0;
            if (this.signatureDone) {
                if (this.curChunkReader != null && !this.curChunkReader.isDone()) {
                    int read1 = this.curChunkReader.feedBytes(buffer, offset, len);
                    processed += read1;
                    this.bytesCount += (long) read1;
                } else {
                    int read0 = 8 - this.buf0len;
                    if (read0 > len) {
                        read0 = len;
                    }
                    System.arraycopy(buffer, offset, this.buf0, this.buf0len, read0);
                    this.buf0len += read0;
                    processed += read0;
                    this.bytesCount += (long) read0;
                    len -= read0;
                    offset += read0;
                    if (this.buf0len == 8) {
                        this.chunkCount++;
                        int clen = PngHelperInternal.readInt4fromBytes(this.buf0, 0);
                        String cid = ChunkHelper.toString(this.buf0, 4, 4);
                        this.startNewChunk(clen, cid, this.bytesCount - 8L);
                        this.buf0len = 0;
                    }
                }
            } else {
                int read = 8 - this.buf0len;
                if (read > len) {
                    read = len;
                }
                System.arraycopy(buffer, offset, this.buf0, this.buf0len, read);
                this.buf0len += read;
                if (this.buf0len == 8) {
                    this.checkSignature(this.buf0);
                    this.buf0len = 0;
                    this.signatureDone = true;
                }
                processed += read;
                this.bytesCount += (long) read;
            }
            return processed;
        }
    }

    public boolean feedAll(byte[] buf, int off, int len) {
        while (len > 0) {
            int n = this.consume(buf, off, len);
            if (n < 1) {
                return false;
            }
            len -= n;
            off += n;
        }
        return true;
    }

    protected void startNewChunk(int len, String id, long offset) {
        if (id.equals("IDAT")) {
            this.idatBytes += (long) len;
        }
        boolean checkCrc = this.shouldCheckCrc(len, id);
        boolean skip = this.shouldSkipContent(len, id);
        boolean isIdatType = this.isIdatKind(id);
        boolean forCurrentIdatSet = false;
        if (this.curReaderDeflatedSet != null) {
            forCurrentIdatSet = this.curReaderDeflatedSet.ackNextChunkId(id);
        }
        if (isIdatType && !skip) {
            if (!forCurrentIdatSet) {
                if (this.curReaderDeflatedSet != null) {
                    throw new PngjInputException("too many IDAT (or idatlike) chunks");
                }
                this.curReaderDeflatedSet = this.createIdatSet(id);
            }
            this.curChunkReader = new DeflatedChunkReader(len, id, checkCrc, offset, this.curReaderDeflatedSet) {

                @Override
                protected void chunkDone() {
                    ChunkSeqReader.this.postProcessChunk(this);
                }
            };
        } else {
            this.curChunkReader = this.createChunkReaderForNewChunk(id, len, offset, skip);
            if (!checkCrc) {
                this.curChunkReader.setCrcCheck(false);
            }
        }
    }

    protected ChunkReader createChunkReaderForNewChunk(String id, int len, long offset, boolean skip) {
        return new ChunkReader(len, id, offset, skip ? ChunkReader.ChunkReaderMode.SKIP : ChunkReader.ChunkReaderMode.BUFFER) {

            @Override
            protected void chunkDone() {
                ChunkSeqReader.this.postProcessChunk(this);
            }

            @Override
            protected void processData(int offsetinChhunk, byte[] buf, int off, int len) {
                throw new PngjExceptionInternal("should never happen");
            }
        };
    }

    protected void postProcessChunk(ChunkReader chunkR) {
        if (this.chunkCount == 1) {
            String cid = this.firstChunkId();
            if (cid != null && !cid.equals(chunkR.getChunkRaw().id)) {
                throw new PngjInputException("Bad first chunk: " + chunkR.getChunkRaw().id + " expected: " + this.firstChunkId());
            }
        }
        if (chunkR.getChunkRaw().id.equals(this.endChunkId())) {
            this.done = true;
        }
    }

    protected DeflatedChunksSet createIdatSet(String id) {
        return new DeflatedChunksSet(id, 1024, 1024);
    }

    protected boolean isIdatKind(String id) {
        return false;
    }

    protected boolean shouldSkipContent(int len, String id) {
        return false;
    }

    protected boolean shouldCheckCrc(int len, String id) {
        return true;
    }

    protected void checkSignature(byte[] buf) {
        if (!Arrays.equals(buf, PngHelperInternal.getPngIdSignature())) {
            throw new PngjInputException("Bad PNG signature");
        }
    }

    public boolean isSignatureDone() {
        return this.signatureDone;
    }

    public boolean isDone() {
        return this.done;
    }

    public long getBytesCount() {
        return this.bytesCount;
    }

    public int getChunkCount() {
        return this.chunkCount;
    }

    public ChunkReader getCurChunkReader() {
        return this.curChunkReader;
    }

    public DeflatedChunksSet getCurReaderDeflatedSet() {
        return this.curReaderDeflatedSet;
    }

    public void close() {
        if (this.curReaderDeflatedSet != null) {
            this.curReaderDeflatedSet.close();
        }
        this.done = true;
    }

    public boolean isAtChunkBoundary() {
        return this.bytesCount == 0L || this.bytesCount == 8L || this.done || this.curChunkReader == null || this.curChunkReader.isDone();
    }

    protected String firstChunkId() {
        return "IHDR";
    }

    public long getIdatBytes() {
        return this.idatBytes;
    }

    protected String endChunkId() {
        return "IEND";
    }

    public void feedFromFile(File f) {
        try {
            this.feedFromInputStream(new FileInputStream(f), true);
        } catch (FileNotFoundException var3) {
            throw new PngjInputException(var3.getMessage());
        }
    }

    public void feedFromInputStream(InputStream is, boolean closeStream) {
        BufferedStreamFeeder sf = new BufferedStreamFeeder(is);
        sf.setCloseStream(closeStream);
        try {
            while (sf.hasMoreToFeed()) {
                sf.feed(this);
            }
        } finally {
            this.close();
            sf.close();
        }
    }

    public void feedFromInputStream(InputStream is) {
        this.feedFromInputStream(is, true);
    }
}