package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;

public class PngChunkUNKNOWN extends PngChunkMultiple {

    public PngChunkUNKNOWN(String id, ImageInfo info) {
        super(id, info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NONE;
    }

    @Override
    public ChunkRaw createRawChunk() {
        return this.raw;
    }

    @Override
    public void parseFromRaw(ChunkRaw c) {
    }

    public byte[] getData() {
        return this.raw.data;
    }

    public void setData(byte[] data) {
        this.raw.data = data;
    }
}