package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkGAMA extends PngChunkSingle {

    public static final String ID = "gAMA";

    private double gamma;

    public PngChunkGAMA(ImageInfo info) {
        super("gAMA", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_PLTE_AND_IDAT;
    }

    @Override
    public ChunkRaw createRawChunk() {
        ChunkRaw c = this.createEmptyChunk(4, true);
        int g = (int) (this.gamma * 100000.0 + 0.5);
        PngHelperInternal.writeInt4tobytes(g, c.data, 0);
        return c;
    }

    @Override
    public void parseFromRaw(ChunkRaw chunk) {
        if (chunk.len != 4) {
            throw new PngjException("bad chunk " + chunk);
        } else {
            int g = PngHelperInternal.readInt4fromBytes(chunk.data, 0);
            this.gamma = (double) g / 100000.0;
        }
    }

    public double getGamma() {
        return this.gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }
}