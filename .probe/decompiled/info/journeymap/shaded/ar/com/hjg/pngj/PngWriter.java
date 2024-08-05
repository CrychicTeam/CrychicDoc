package info.journeymap.shaded.ar.com.hjg.pngj;

import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkPredicate;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunksList;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunksListForWrite;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.PngChunk;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.PngChunkIEND;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.PngChunkIHDR;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.PngMetadata;
import info.journeymap.shaded.ar.com.hjg.pngj.pixels.PixelsWriter;
import info.journeymap.shaded.ar.com.hjg.pngj.pixels.PixelsWriterDefault;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PngWriter {

    public final ImageInfo imgInfo;

    protected int rowNum = -1;

    private final ChunksListForWrite chunksList;

    private final PngMetadata metadata;

    protected int currentChunkGroup = -1;

    private int passes = 1;

    private int currentpass = 0;

    private boolean shouldCloseStream = true;

    private int idatMaxSize = 0;

    private PngIDatChunkOutputStream datStream;

    protected PixelsWriter pixelsWriter;

    private final OutputStream os;

    private ChunkPredicate copyFromPredicate = null;

    private ChunksList copyFromList = null;

    protected StringBuilder debuginfo = new StringBuilder();

    public PngWriter(File file, ImageInfo imgInfo, boolean allowoverwrite) {
        this(PngHelperInternal.ostreamFromFile(file, allowoverwrite), imgInfo);
        this.setShouldCloseStream(true);
    }

    public PngWriter(File file, ImageInfo imgInfo) {
        this(file, imgInfo, true);
    }

    public PngWriter(OutputStream outputStream, ImageInfo imgInfo) {
        this.os = outputStream;
        this.imgInfo = imgInfo;
        this.chunksList = new ChunksListForWrite(imgInfo);
        this.metadata = new PngMetadata(this.chunksList);
        this.pixelsWriter = this.createPixelsWriter(imgInfo);
        this.setCompLevel(9);
    }

    private void initIdat() {
        this.datStream = new PngIDatChunkOutputStream(this.os, this.idatMaxSize);
        this.pixelsWriter.setOs(this.datStream);
        this.writeSignatureAndIHDR();
        this.writeFirstChunks();
    }

    private void writeEndChunk() {
        PngChunkIEND c = new PngChunkIEND(this.imgInfo);
        c.createRawChunk().writeChunk(this.os);
        this.chunksList.getChunks().add(c);
    }

    private void writeFirstChunks() {
        if (this.currentChunkGroup < 4) {
            int nw = 0;
            this.currentChunkGroup = 1;
            this.queueChunksFromOther();
            nw = this.chunksList.writeChunks(this.os, this.currentChunkGroup);
            this.currentChunkGroup = 2;
            nw = this.chunksList.writeChunks(this.os, this.currentChunkGroup);
            if (nw > 0 && this.imgInfo.greyscale) {
                throw new PngjOutputException("cannot write palette for this format");
            } else if (nw == 0 && this.imgInfo.indexed) {
                throw new PngjOutputException("missing palette");
            } else {
                this.currentChunkGroup = 3;
                nw = this.chunksList.writeChunks(this.os, this.currentChunkGroup);
                this.currentChunkGroup = 4;
            }
        }
    }

    private void writeLastChunks() {
        this.queueChunksFromOther();
        this.currentChunkGroup = 5;
        this.chunksList.writeChunks(this.os, this.currentChunkGroup);
        List<PngChunk> pending = this.chunksList.getQueuedChunks();
        if (!pending.isEmpty()) {
            throw new PngjOutputException(pending.size() + " chunks were not written! Eg: " + ((PngChunk) pending.get(0)).toString());
        } else {
            this.currentChunkGroup = 6;
        }
    }

    private void writeSignatureAndIHDR() {
        this.currentChunkGroup = 0;
        PngHelperInternal.writeBytes(this.os, PngHelperInternal.getPngIdSignature());
        PngChunkIHDR ihdr = new PngChunkIHDR(this.imgInfo);
        ihdr.setCols(this.imgInfo.cols);
        ihdr.setRows(this.imgInfo.rows);
        ihdr.setBitspc(this.imgInfo.bitDepth);
        int colormodel = 0;
        if (this.imgInfo.alpha) {
            colormodel += 4;
        }
        if (this.imgInfo.indexed) {
            colormodel++;
        }
        if (!this.imgInfo.greyscale) {
            colormodel += 2;
        }
        ihdr.setColormodel(colormodel);
        ihdr.setCompmeth(0);
        ihdr.setFilmeth(0);
        ihdr.setInterlaced(0);
        ihdr.createRawChunk().writeChunk(this.os);
        this.chunksList.getChunks().add(ihdr);
    }

    private void queueChunksFromOther() {
        if (this.copyFromList != null && this.copyFromPredicate != null) {
            boolean idatDone = this.currentChunkGroup >= 4;
            for (PngChunk chunk : this.copyFromList.getChunks()) {
                if (chunk.getRaw().data != null) {
                    int group = chunk.getChunkGroup();
                    if ((group > 4 || !idatDone) && (group < 4 || idatDone) && (!chunk.crit || chunk.id.equals("PLTE"))) {
                        boolean copy = this.copyFromPredicate.match(chunk);
                        if (copy && this.chunksList.getEquivalent(chunk).isEmpty() && this.chunksList.getQueuedEquivalent(chunk).isEmpty()) {
                            this.chunksList.queue(chunk);
                        }
                    }
                }
            }
        }
    }

    public void queueChunk(PngChunk chunk) {
        for (PngChunk other : this.chunksList.getQueuedEquivalent(chunk)) {
            this.getChunksList().removeChunk(other);
        }
        this.chunksList.queue(chunk);
    }

    public void copyChunksFrom(ChunksList chunks, int copyMask) {
        this.copyChunksFrom(chunks, ChunkCopyBehaviour.createPredicate(copyMask, this.imgInfo));
    }

    public void copyChunksFrom(ChunksList chunks) {
        this.copyChunksFrom(chunks, 8);
    }

    public void copyChunksFrom(ChunksList chunks, ChunkPredicate predicate) {
        if (this.copyFromList != null && chunks != null) {
            PngHelperInternal.LOGGER.warning("copyChunksFrom should only be called once");
        }
        if (predicate == null) {
            throw new PngjOutputException("copyChunksFrom requires a predicate");
        } else {
            this.copyFromList = chunks;
            this.copyFromPredicate = predicate;
        }
    }

    public double computeCompressionRatio() {
        if (this.currentChunkGroup < 6) {
            throw new PngjOutputException("must be called after end()");
        } else {
            double compressed = (double) this.datStream.getCountFlushed();
            double raw = (double) ((this.imgInfo.bytesPerRow + 1) * this.imgInfo.rows);
            return compressed / raw;
        }
    }

    public void end() {
        if (this.rowNum != this.imgInfo.rows - 1) {
            throw new PngjOutputException("all rows have not been written");
        } else {
            try {
                this.datStream.flush();
                this.writeLastChunks();
                this.writeEndChunk();
            } catch (IOException var5) {
                throw new PngjOutputException(var5);
            } finally {
                this.close();
            }
        }
    }

    public void close() {
        try {
            if (this.datStream != null) {
                this.datStream.close();
            }
        } catch (Exception var3) {
        }
        if (this.pixelsWriter != null) {
            this.pixelsWriter.close();
        }
        if (this.shouldCloseStream && this.os != null) {
            try {
                this.os.close();
            } catch (Exception var2) {
                PngHelperInternal.LOGGER.warning("Error closing writer " + var2.toString());
            }
        }
    }

    public ChunksListForWrite getChunksList() {
        return this.chunksList;
    }

    public PngMetadata getMetadata() {
        return this.metadata;
    }

    public void setFilterType(FilterType filterType) {
        this.pixelsWriter.setFilterType(filterType);
    }

    public void setCompLevel(int complevel) {
        this.pixelsWriter.setDeflaterCompLevel(complevel);
    }

    public void setFilterPreserve(boolean filterPreserve) {
        if (filterPreserve) {
            this.pixelsWriter.setFilterType(FilterType.FILTER_PRESERVE);
        } else if (this.pixelsWriter.getFilterType() == null) {
            this.pixelsWriter.setFilterType(FilterType.FILTER_DEFAULT);
        }
    }

    public void setIdatMaxSize(int idatMaxSize) {
        this.idatMaxSize = idatMaxSize;
    }

    public void setShouldCloseStream(boolean shouldCloseStream) {
        this.shouldCloseStream = shouldCloseStream;
    }

    public void writeRow(IImageLine imgline) {
        this.writeRow(imgline, this.rowNum + 1);
    }

    public void writeRows(IImageLineSet<? extends IImageLine> imglines) {
        for (int i = 0; i < this.imgInfo.rows; i++) {
            this.writeRow(imglines.getImageLine(i));
        }
    }

    public void writeRow(IImageLine imgline, int rownumber) {
        this.rowNum++;
        if (this.rowNum == this.imgInfo.rows) {
            this.rowNum = 0;
        }
        if (rownumber == this.imgInfo.rows) {
            rownumber = 0;
        }
        if (rownumber >= 0 && this.rowNum != rownumber) {
            throw new PngjOutputException("rows must be written in order: expected:" + this.rowNum + " passed:" + rownumber);
        } else {
            if (this.rowNum == 0) {
                this.currentpass++;
            }
            if (rownumber == 0 && this.currentpass == this.passes) {
                this.initIdat();
                this.writeFirstChunks();
            }
            byte[] rowb = this.pixelsWriter.getRowb();
            imgline.writeToPngRaw(rowb);
            this.pixelsWriter.processRow(rowb);
        }
    }

    public void writeRowInt(int[] buf) {
        this.writeRow(new ImageLineInt(this.imgInfo, buf));
    }

    protected PixelsWriter createPixelsWriter(ImageInfo imginfo) {
        return new PixelsWriterDefault(imginfo);
    }

    public final PixelsWriter getPixelsWriter() {
        return this.pixelsWriter;
    }

    public String getDebuginfo() {
        return this.debuginfo.toString();
    }
}