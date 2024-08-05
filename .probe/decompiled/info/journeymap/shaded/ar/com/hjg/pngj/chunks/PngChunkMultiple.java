package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;

public abstract class PngChunkMultiple extends PngChunk {

    protected PngChunkMultiple(String id, ImageInfo imgInfo) {
        super(id, imgInfo);
    }

    @Override
    public final boolean allowsMultiple() {
        return true;
    }
}