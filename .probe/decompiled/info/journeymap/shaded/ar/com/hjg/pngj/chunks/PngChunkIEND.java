package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;

public class PngChunkIEND extends PngChunkSingle {

    public static final String ID = "IEND";

    public PngChunkIEND(ImageInfo info) {
        super("IEND", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NA;
    }

    @Override
    public ChunkRaw createRawChunk() {
        return new ChunkRaw(0, ChunkHelper.b_IEND, false);
    }

    @Override
    public void parseFromRaw(ChunkRaw c) {
    }
}