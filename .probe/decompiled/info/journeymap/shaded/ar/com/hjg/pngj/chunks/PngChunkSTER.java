package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkSTER extends PngChunkSingle {

    public static final String ID = "sTER";

    private byte mode;

    public PngChunkSTER(ImageInfo info) {
        super("sTER", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_IDAT;
    }

    @Override
    public ChunkRaw createRawChunk() {
        ChunkRaw c = this.createEmptyChunk(1, true);
        c.data[0] = this.mode;
        return c;
    }

    @Override
    public void parseFromRaw(ChunkRaw chunk) {
        if (chunk.len != 1) {
            throw new PngjException("bad chunk length " + chunk);
        } else {
            this.mode = chunk.data[0];
        }
    }

    public byte getMode() {
        return this.mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }
}