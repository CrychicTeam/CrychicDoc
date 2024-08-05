package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkTEXT extends PngChunkTextVar {

    public static final String ID = "tEXt";

    public PngChunkTEXT(ImageInfo info) {
        super("tEXt", info);
    }

    @Override
    public ChunkRaw createRawChunk() {
        if (this.key != null && this.key.trim().length() != 0) {
            byte[] b = ChunkHelper.toBytes(this.key + "\u0000" + this.val);
            ChunkRaw chunk = this.createEmptyChunk(b.length, false);
            chunk.data = b;
            return chunk;
        } else {
            throw new PngjException("Text chunk key must be non empty");
        }
    }

    @Override
    public void parseFromRaw(ChunkRaw c) {
        int i = 0;
        while (i < c.data.length && c.data[i] != 0) {
            i++;
        }
        this.key = ChunkHelper.toString(c.data, 0, i);
        i++;
        this.val = i < c.data.length ? ChunkHelper.toString(c.data, i, c.data.length - i) : "";
    }
}