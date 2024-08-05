package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjExceptionInternal;
import java.io.OutputStream;

public abstract class PngChunk {

    public final String id;

    public final boolean crit;

    public final boolean pub;

    public final boolean safe;

    protected final ImageInfo imgInfo;

    protected ChunkRaw raw;

    private boolean priority = false;

    protected int chunkGroup = -1;

    public PngChunk(String id, ImageInfo imgInfo) {
        this.id = id;
        this.imgInfo = imgInfo;
        this.crit = ChunkHelper.isCritical(id);
        this.pub = ChunkHelper.isPublic(id);
        this.safe = ChunkHelper.isSafeToCopy(id);
    }

    protected final ChunkRaw createEmptyChunk(int len, boolean alloc) {
        return new ChunkRaw(len, ChunkHelper.toBytes(this.id), alloc);
    }

    public final int getChunkGroup() {
        return this.chunkGroup;
    }

    final void setChunkGroup(int chunkGroup) {
        this.chunkGroup = chunkGroup;
    }

    public boolean hasPriority() {
        return this.priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    final void write(OutputStream os) {
        if (this.raw == null || this.raw.data == null) {
            this.raw = this.createRawChunk();
        }
        if (this.raw == null) {
            throw new PngjExceptionInternal("null chunk ! creation failed for " + this);
        } else {
            this.raw.writeChunk(os);
        }
    }

    protected abstract ChunkRaw createRawChunk();

    protected abstract void parseFromRaw(ChunkRaw var1);

    protected abstract boolean allowsMultiple();

    public ChunkRaw getRaw() {
        return this.raw;
    }

    void setRaw(ChunkRaw raw) {
        this.raw = raw;
    }

    public int getLen() {
        return this.raw != null ? this.raw.len : -1;
    }

    public long getOffset() {
        return this.raw != null ? this.raw.getOffset() : -1L;
    }

    public void invalidateRawData() {
        this.raw = null;
    }

    public abstract PngChunk.ChunkOrderingConstraint getOrderingConstraint();

    public String toString() {
        return "chunk id= " + this.id + " (len=" + this.getLen() + " offset=" + this.getOffset() + ")";
    }

    public static enum ChunkOrderingConstraint {

        NONE,
        BEFORE_PLTE_AND_IDAT,
        AFTER_PLTE_BEFORE_IDAT,
        AFTER_PLTE_BEFORE_IDAT_PLTE_REQUIRED,
        BEFORE_IDAT,
        NA;

        public boolean mustGoBeforePLTE() {
            return this == BEFORE_PLTE_AND_IDAT;
        }

        public boolean mustGoBeforeIDAT() {
            return this == BEFORE_IDAT || this == BEFORE_PLTE_AND_IDAT || this == AFTER_PLTE_BEFORE_IDAT;
        }

        public boolean mustGoAfterPLTE() {
            return this == AFTER_PLTE_BEFORE_IDAT || this == AFTER_PLTE_BEFORE_IDAT_PLTE_REQUIRED;
        }

        public boolean isOk(int currentChunkGroup, boolean hasplte) {
            if (this == NONE) {
                return true;
            } else if (this == BEFORE_IDAT) {
                return currentChunkGroup < 4;
            } else if (this == BEFORE_PLTE_AND_IDAT) {
                return currentChunkGroup < 2;
            } else if (this != AFTER_PLTE_BEFORE_IDAT) {
                return false;
            } else {
                return hasplte ? currentChunkGroup < 4 : currentChunkGroup < 4 && currentChunkGroup > 2;
            }
        }
    }
}