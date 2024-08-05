package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;

public class PngChunkIDAT extends PngChunkMultiple {

    public static final String ID = "IDAT";

    public PngChunkIDAT(ImageInfo i) {
        super("IDAT", i);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NA;
    }

    @Override
    public ChunkRaw createRawChunk() {
        return null;
    }

    @Override
    public void parseFromRaw(ChunkRaw c) {
    }
}