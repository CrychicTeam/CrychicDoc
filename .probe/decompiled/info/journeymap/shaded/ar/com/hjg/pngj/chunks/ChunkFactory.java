package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.IChunkFactory;
import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;

public class ChunkFactory implements IChunkFactory {

    boolean parse;

    public ChunkFactory() {
        this(true);
    }

    public ChunkFactory(boolean parse) {
        this.parse = parse;
    }

    @Override
    public final PngChunk createChunk(ChunkRaw chunkRaw, ImageInfo imgInfo) {
        PngChunk c = this.createEmptyChunkKnown(chunkRaw.id, imgInfo);
        if (c == null) {
            c = this.createEmptyChunkExtended(chunkRaw.id, imgInfo);
        }
        if (c == null) {
            c = this.createEmptyChunkUnknown(chunkRaw.id, imgInfo);
        }
        c.setRaw(chunkRaw);
        if (this.parse && chunkRaw.data != null) {
            c.parseFromRaw(chunkRaw);
        }
        return c;
    }

    protected final PngChunk createEmptyChunkKnown(String id, ImageInfo imgInfo) {
        if (id.equals("IDAT")) {
            return new PngChunkIDAT(imgInfo);
        } else if (id.equals("IHDR")) {
            return new PngChunkIHDR(imgInfo);
        } else if (id.equals("PLTE")) {
            return new PngChunkPLTE(imgInfo);
        } else if (id.equals("IEND")) {
            return new PngChunkIEND(imgInfo);
        } else if (id.equals("tEXt")) {
            return new PngChunkTEXT(imgInfo);
        } else if (id.equals("iTXt")) {
            return new PngChunkITXT(imgInfo);
        } else if (id.equals("zTXt")) {
            return new PngChunkZTXT(imgInfo);
        } else if (id.equals("bKGD")) {
            return new PngChunkBKGD(imgInfo);
        } else if (id.equals("gAMA")) {
            return new PngChunkGAMA(imgInfo);
        } else if (id.equals("pHYs")) {
            return new PngChunkPHYS(imgInfo);
        } else if (id.equals("iCCP")) {
            return new PngChunkICCP(imgInfo);
        } else if (id.equals("tIME")) {
            return new PngChunkTIME(imgInfo);
        } else if (id.equals("tRNS")) {
            return new PngChunkTRNS(imgInfo);
        } else if (id.equals("cHRM")) {
            return new PngChunkCHRM(imgInfo);
        } else if (id.equals("sBIT")) {
            return new PngChunkSBIT(imgInfo);
        } else if (id.equals("sRGB")) {
            return new PngChunkSRGB(imgInfo);
        } else if (id.equals("hIST")) {
            return new PngChunkHIST(imgInfo);
        } else {
            return id.equals("sPLT") ? new PngChunkSPLT(imgInfo) : null;
        }
    }

    protected final PngChunk createEmptyChunkUnknown(String id, ImageInfo imgInfo) {
        return new PngChunkUNKNOWN(id, imgInfo);
    }

    protected PngChunk createEmptyChunkExtended(String id, ImageInfo imgInfo) {
        if (id.equals("oFFs")) {
            return new PngChunkOFFS(imgInfo);
        } else {
            return id.equals("sTER") ? new PngChunkSTER(imgInfo) : null;
        }
    }
}