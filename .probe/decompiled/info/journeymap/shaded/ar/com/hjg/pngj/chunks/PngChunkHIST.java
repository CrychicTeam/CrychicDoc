package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkHIST extends PngChunkSingle {

    public static final String ID = "hIST";

    private int[] hist = new int[0];

    public PngChunkHIST(ImageInfo info) {
        super("hIST", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.AFTER_PLTE_BEFORE_IDAT;
    }

    @Override
    public void parseFromRaw(ChunkRaw c) {
        if (!this.imgInfo.indexed) {
            throw new PngjException("only indexed images accept a HIST chunk");
        } else {
            int nentries = c.data.length / 2;
            this.hist = new int[nentries];
            for (int i = 0; i < this.hist.length; i++) {
                this.hist[i] = PngHelperInternal.readInt2fromBytes(c.data, i * 2);
            }
        }
    }

    @Override
    public ChunkRaw createRawChunk() {
        if (!this.imgInfo.indexed) {
            throw new PngjException("only indexed images accept a HIST chunk");
        } else {
            ChunkRaw c = null;
            c = this.createEmptyChunk(this.hist.length * 2, true);
            for (int i = 0; i < this.hist.length; i++) {
                PngHelperInternal.writeInt2tobytes(this.hist[i], c.data, i * 2);
            }
            return c;
        }
    }

    public int[] getHist() {
        return this.hist;
    }

    public void setHist(int[] hist) {
        this.hist = hist;
    }
}