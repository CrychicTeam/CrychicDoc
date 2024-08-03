package info.journeymap.shaded.ar.com.hjg.pngj;

import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunksList;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.PngMetadata;
import java.io.File;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

public class PngReader {

    public static final long MAX_TOTAL_BYTES_READ_DEFAULT = 901001001L;

    public static final long MAX_BYTES_METADATA_DEFAULT = 5024024L;

    public static final long MAX_CHUNK_SIZE_SKIP = 2024024L;

    public final ImageInfo imgInfo;

    public final boolean interlaced;

    protected ChunkSeqReaderPng chunkseq;

    protected BufferedStreamFeeder streamFeeder;

    protected final PngMetadata metadata;

    protected int rowNum = -1;

    CRC32 idatCrca;

    Adler32 idatCrcb;

    protected IImageLineSet<? extends IImageLine> imlinesSet;

    private IImageLineSetFactory<? extends IImageLine> imageLineSetFactory;

    public PngReader(InputStream inputStream) {
        this(inputStream, true);
    }

    public PngReader(InputStream inputStream, boolean shouldCloseStream) {
        try {
            this.streamFeeder = new BufferedStreamFeeder(inputStream);
            this.streamFeeder.setCloseStream(shouldCloseStream);
            this.chunkseq = new ChunkSeqReaderPng(false);
            this.streamFeeder.setFailIfNoFeed(true);
            if (!this.streamFeeder.feedFixed(this.chunkseq, 36)) {
                throw new PngjInputException("error reading first 21 bytes");
            } else {
                this.imgInfo = this.chunkseq.getImageInfo();
                this.interlaced = this.chunkseq.getDeinterlacer() != null;
                this.setMaxBytesMetadata(5024024L);
                this.setMaxTotalBytesRead(901001001L);
                this.setSkipChunkMaxSize(2024024L);
                this.metadata = new PngMetadata(this.chunkseq.chunksList);
                this.setLineSetFactory(ImageLineSetDefault.getFactoryInt());
                this.rowNum = -1;
            }
        } catch (RuntimeException var4) {
            this.streamFeeder.close();
            if (this.chunkseq != null) {
                this.chunkseq.close();
            }
            throw var4;
        }
    }

    public PngReader(File file) {
        this(PngHelperInternal.istreamFromFile(file), true);
    }

    protected void readFirstChunks() {
        while (this.chunkseq.currentChunkGroup < 4) {
            this.streamFeeder.feed(this.chunkseq);
        }
    }

    public void setChunkLoadBehaviour(ChunkLoadBehaviour chunkLoadBehaviour) {
        this.chunkseq.setChunkLoadBehaviour(chunkLoadBehaviour);
    }

    public ChunksList getChunksList() {
        if (this.chunkseq.firstChunksNotYetRead()) {
            this.readFirstChunks();
        }
        return this.chunkseq.chunksList;
    }

    int getCurrentChunkGroup() {
        return this.chunkseq.currentChunkGroup;
    }

    public PngMetadata getMetadata() {
        if (this.chunkseq.firstChunksNotYetRead()) {
            this.readFirstChunks();
        }
        return this.metadata;
    }

    public IImageLine readRow() {
        return this.readRow(this.rowNum + 1);
    }

    public boolean hasMoreRows() {
        return this.rowNum < this.imgInfo.rows - 1;
    }

    public IImageLine readRow(int nrow) {
        if (this.chunkseq.firstChunksNotYetRead()) {
            this.readFirstChunks();
        }
        if (this.interlaced) {
            if (this.imlinesSet == null) {
                this.imlinesSet = this.createLineSet(false, this.imgInfo.rows, 0, 1);
                this.loadAllInterlaced(this.imgInfo.rows, 0, 1);
            }
            this.rowNum = nrow;
            return this.imlinesSet.getImageLine(nrow);
        } else {
            if (this.imlinesSet == null) {
                this.imlinesSet = this.createLineSet(true, 1, 0, 1);
            }
            IImageLine line = this.imlinesSet.getImageLine(nrow);
            if (nrow == this.rowNum) {
                return line;
            } else if (nrow < this.rowNum) {
                throw new PngjInputException("rows must be read in increasing order: " + nrow);
            } else {
                for (; this.rowNum < nrow; this.chunkseq.getIdatSet().advanceToNextRow()) {
                    while (!this.chunkseq.getIdatSet().isRowReady()) {
                        this.streamFeeder.feed(this.chunkseq);
                    }
                    this.rowNum++;
                    this.chunkseq.getIdatSet().updateCrcs(this.idatCrca, this.idatCrcb);
                    if (this.rowNum == nrow) {
                        line.readFromPngRaw(this.chunkseq.getIdatSet().getUnfilteredRow(), this.imgInfo.bytesPerRow + 1, 0, 1);
                        line.endReadFromPngRaw();
                    }
                }
                return line;
            }
        }
    }

    public IImageLineSet<? extends IImageLine> readRows() {
        return this.readRows(this.imgInfo.rows, 0, 1);
    }

    public IImageLineSet<? extends IImageLine> readRows(int nRows, int rowOffset, int rowStep) {
        if (this.chunkseq.firstChunksNotYetRead()) {
            this.readFirstChunks();
        }
        if (nRows < 0) {
            nRows = (this.imgInfo.rows - rowOffset) / rowStep;
        }
        if (rowStep < 1 || rowOffset < 0 || nRows == 0 || nRows * rowStep + rowOffset > this.imgInfo.rows) {
            throw new PngjInputException("bad args");
        } else if (this.rowNum >= 0) {
            throw new PngjInputException("readRows cannot be mixed with readRow");
        } else {
            this.imlinesSet = this.createLineSet(false, nRows, rowOffset, rowStep);
            if (!this.interlaced) {
                for (int m = -1; m < nRows - 1; this.chunkseq.getIdatSet().advanceToNextRow()) {
                    while (!this.chunkseq.getIdatSet().isRowReady()) {
                        this.streamFeeder.feed(this.chunkseq);
                    }
                    this.rowNum++;
                    this.chunkseq.getIdatSet().updateCrcs(this.idatCrca, this.idatCrcb);
                    m = (this.rowNum - rowOffset) / rowStep;
                    if (this.rowNum >= rowOffset && rowStep * m + rowOffset == this.rowNum) {
                        IImageLine line = this.imlinesSet.getImageLine(this.rowNum);
                        line.readFromPngRaw(this.chunkseq.getIdatSet().getUnfilteredRow(), this.imgInfo.bytesPerRow + 1, 0, 1);
                        line.endReadFromPngRaw();
                    }
                }
            } else {
                this.loadAllInterlaced(nRows, rowOffset, rowStep);
            }
            this.chunkseq.getIdatSet().done();
            this.end();
            return this.imlinesSet;
        }
    }

    public void setLineSetFactory(IImageLineSetFactory<? extends IImageLine> factory) {
        this.imageLineSetFactory = factory;
    }

    protected IImageLineSet<? extends IImageLine> createLineSet(boolean singleCursor, int nlines, int noffset, int step) {
        return this.imageLineSetFactory.create(this.imgInfo, singleCursor, nlines, noffset, step);
    }

    protected void loadAllInterlaced(int nRows, int rowOffset, int rowStep) {
        IdatSet idat = this.chunkseq.getIdatSet();
        int nread = 0;
        while (true) {
            while (this.chunkseq.getIdatSet().isRowReady()) {
                this.chunkseq.getIdatSet().updateCrcs(this.idatCrca, this.idatCrcb);
                int rowNumreal = idat.rowinfo.rowNreal;
                boolean inset = (rowNumreal - rowOffset) % rowStep == 0;
                if (inset) {
                    this.imlinesSet.getImageLine(rowNumreal).readFromPngRaw(idat.getUnfilteredRow(), idat.rowinfo.buflen, idat.rowinfo.oX, idat.rowinfo.dX);
                    nread++;
                }
                idat.advanceToNextRow();
                if (nread >= nRows && idat.isDone()) {
                    idat.done();
                    rowNumreal = 0;
                    for (int j = rowOffset; rowNumreal < nRows; j += rowStep) {
                        this.imlinesSet.getImageLine(j).endReadFromPngRaw();
                        rowNumreal++;
                    }
                    return;
                }
            }
            this.streamFeeder.feed(this.chunkseq);
        }
    }

    public void readSkippingAllRows() {
        this.chunkseq.addChunkToSkip("IDAT");
        if (this.chunkseq.firstChunksNotYetRead()) {
            this.readFirstChunks();
        }
        this.end();
    }

    public void setMaxTotalBytesRead(long maxTotalBytesToRead) {
        this.chunkseq.setMaxTotalBytesRead(maxTotalBytesToRead);
    }

    public void setMaxBytesMetadata(long maxBytesMetadata) {
        this.chunkseq.setMaxBytesMetadata(maxBytesMetadata);
    }

    public void setSkipChunkMaxSize(long skipChunkMaxSize) {
        this.chunkseq.setSkipChunkMaxSize(skipChunkMaxSize);
    }

    public void setChunksToSkip(String... chunksToSkip) {
        this.chunkseq.setChunksToSkip(chunksToSkip);
    }

    public void addChunkToSkip(String chunkToSkip) {
        this.chunkseq.addChunkToSkip(chunkToSkip);
    }

    public void setShouldCloseStream(boolean shouldCloseStream) {
        this.streamFeeder.setCloseStream(shouldCloseStream);
    }

    public void end() {
        try {
            if (this.chunkseq.firstChunksNotYetRead()) {
                this.readFirstChunks();
            }
            if (this.chunkseq.getIdatSet() != null && !this.chunkseq.getIdatSet().isDone()) {
                this.chunkseq.getIdatSet().done();
            }
            while (!this.chunkseq.isDone()) {
                this.streamFeeder.feed(this.chunkseq);
            }
        } finally {
            this.close();
        }
    }

    public void close() {
        try {
            if (this.chunkseq != null) {
                this.chunkseq.close();
            }
        } catch (Exception var2) {
            PngHelperInternal.LOGGER.warning("error closing chunk sequence:" + var2.getMessage());
        }
        if (this.streamFeeder != null) {
            this.streamFeeder.close();
        }
    }

    public boolean isInterlaced() {
        return this.interlaced;
    }

    public void setCrcCheckDisabled() {
        this.chunkseq.setCheckCrc(false);
    }

    public ChunkSeqReaderPng getChunkseq() {
        return this.chunkseq;
    }

    public void prepareSimpleDigestComputation() {
        if (this.idatCrca == null) {
            this.idatCrca = new CRC32();
        } else {
            this.idatCrca.reset();
        }
        if (this.idatCrcb == null) {
            this.idatCrcb = new Adler32();
        } else {
            this.idatCrcb.reset();
        }
        this.idatCrca.update((byte) this.imgInfo.rows);
        this.idatCrca.update((byte) (this.imgInfo.rows >> 8));
        this.idatCrca.update((byte) (this.imgInfo.rows >> 16));
        this.idatCrca.update((byte) this.imgInfo.cols);
        this.idatCrca.update((byte) (this.imgInfo.cols >> 8));
        this.idatCrca.update((byte) (this.imgInfo.cols >> 16));
        this.idatCrca.update((byte) this.imgInfo.channels);
        this.idatCrca.update((byte) this.imgInfo.bitDepth);
        this.idatCrca.update((byte) (this.imgInfo.indexed ? 10 : 20));
        this.idatCrcb.update((byte) this.imgInfo.bytesPerRow);
        this.idatCrcb.update((byte) this.imgInfo.channels);
        this.idatCrcb.update((byte) this.imgInfo.rows);
    }

    long getSimpleDigest() {
        return this.idatCrca == null ? 0L : this.idatCrca.getValue() ^ this.idatCrcb.getValue() << 31;
    }

    public String getSimpleDigestHex() {
        return String.format("%016X", this.getSimpleDigest());
    }

    public String toString() {
        return this.imgInfo.toString() + " interlaced=" + this.interlaced;
    }

    public String toStringCompact() {
        return this.imgInfo.toStringBrief() + (this.interlaced ? "i" : "");
    }
}