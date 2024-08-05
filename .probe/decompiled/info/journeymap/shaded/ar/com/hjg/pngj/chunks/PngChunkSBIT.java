package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkSBIT extends PngChunkSingle {

    public static final String ID = "sBIT";

    private int graysb;

    private int alphasb;

    private int redsb;

    private int greensb;

    private int bluesb;

    public PngChunkSBIT(ImageInfo info) {
        super("sBIT", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_PLTE_AND_IDAT;
    }

    private int getCLen() {
        int len = this.imgInfo.greyscale ? 1 : 3;
        if (this.imgInfo.alpha) {
            len++;
        }
        return len;
    }

    @Override
    public void parseFromRaw(ChunkRaw c) {
        if (c.len != this.getCLen()) {
            throw new PngjException("bad chunk length " + c);
        } else {
            if (this.imgInfo.greyscale) {
                this.graysb = PngHelperInternal.readInt1fromByte(c.data, 0);
                if (this.imgInfo.alpha) {
                    this.alphasb = PngHelperInternal.readInt1fromByte(c.data, 1);
                }
            } else {
                this.redsb = PngHelperInternal.readInt1fromByte(c.data, 0);
                this.greensb = PngHelperInternal.readInt1fromByte(c.data, 1);
                this.bluesb = PngHelperInternal.readInt1fromByte(c.data, 2);
                if (this.imgInfo.alpha) {
                    this.alphasb = PngHelperInternal.readInt1fromByte(c.data, 3);
                }
            }
        }
    }

    @Override
    public ChunkRaw createRawChunk() {
        ChunkRaw c = null;
        c = this.createEmptyChunk(this.getCLen(), true);
        if (this.imgInfo.greyscale) {
            c.data[0] = (byte) this.graysb;
            if (this.imgInfo.alpha) {
                c.data[1] = (byte) this.alphasb;
            }
        } else {
            c.data[0] = (byte) this.redsb;
            c.data[1] = (byte) this.greensb;
            c.data[2] = (byte) this.bluesb;
            if (this.imgInfo.alpha) {
                c.data[3] = (byte) this.alphasb;
            }
        }
        return c;
    }

    public void setGraysb(int gray) {
        if (!this.imgInfo.greyscale) {
            throw new PngjException("only greyscale images support this");
        } else {
            this.graysb = gray;
        }
    }

    public int getGraysb() {
        if (!this.imgInfo.greyscale) {
            throw new PngjException("only greyscale images support this");
        } else {
            return this.graysb;
        }
    }

    public void setAlphasb(int a) {
        if (!this.imgInfo.alpha) {
            throw new PngjException("only images with alpha support this");
        } else {
            this.alphasb = a;
        }
    }

    public int getAlphasb() {
        if (!this.imgInfo.alpha) {
            throw new PngjException("only images with alpha support this");
        } else {
            return this.alphasb;
        }
    }

    public void setRGB(int r, int g, int b) {
        if (!this.imgInfo.greyscale && !this.imgInfo.indexed) {
            this.redsb = r;
            this.greensb = g;
            this.bluesb = b;
        } else {
            throw new PngjException("only rgb or rgba images support this");
        }
    }

    public int[] getRGB() {
        if (!this.imgInfo.greyscale && !this.imgInfo.indexed) {
            return new int[] { this.redsb, this.greensb, this.bluesb };
        } else {
            throw new PngjException("only rgb or rgba images support this");
        }
    }
}