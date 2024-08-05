package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkTRNS extends PngChunkSingle {

    public static final String ID = "tRNS";

    private int gray;

    private int red;

    private int green;

    private int blue;

    private int[] paletteAlpha = new int[0];

    public PngChunkTRNS(ImageInfo info) {
        super("tRNS", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.AFTER_PLTE_BEFORE_IDAT;
    }

    @Override
    public ChunkRaw createRawChunk() {
        ChunkRaw c = null;
        if (this.imgInfo.greyscale) {
            c = this.createEmptyChunk(2, true);
            PngHelperInternal.writeInt2tobytes(this.gray, c.data, 0);
        } else if (this.imgInfo.indexed) {
            c = this.createEmptyChunk(this.paletteAlpha.length, true);
            for (int n = 0; n < c.len; n++) {
                c.data[n] = (byte) this.paletteAlpha[n];
            }
        } else {
            c = this.createEmptyChunk(6, true);
            PngHelperInternal.writeInt2tobytes(this.red, c.data, 0);
            PngHelperInternal.writeInt2tobytes(this.green, c.data, 0);
            PngHelperInternal.writeInt2tobytes(this.blue, c.data, 0);
        }
        return c;
    }

    @Override
    public void parseFromRaw(ChunkRaw c) {
        if (this.imgInfo.greyscale) {
            this.gray = PngHelperInternal.readInt2fromBytes(c.data, 0);
        } else if (this.imgInfo.indexed) {
            int nentries = c.data.length;
            this.paletteAlpha = new int[nentries];
            for (int n = 0; n < nentries; n++) {
                this.paletteAlpha[n] = c.data[n] & 255;
            }
        } else {
            this.red = PngHelperInternal.readInt2fromBytes(c.data, 0);
            this.green = PngHelperInternal.readInt2fromBytes(c.data, 2);
            this.blue = PngHelperInternal.readInt2fromBytes(c.data, 4);
        }
    }

    public void setRGB(int r, int g, int b) {
        if (!this.imgInfo.greyscale && !this.imgInfo.indexed) {
            this.red = r;
            this.green = g;
            this.blue = b;
        } else {
            throw new PngjException("only rgb or rgba images support this");
        }
    }

    public int[] getRGB() {
        if (!this.imgInfo.greyscale && !this.imgInfo.indexed) {
            return new int[] { this.red, this.green, this.blue };
        } else {
            throw new PngjException("only rgb or rgba images support this");
        }
    }

    public int getRGB888() {
        if (!this.imgInfo.greyscale && !this.imgInfo.indexed) {
            return this.red << 16 | this.green << 8 | this.blue;
        } else {
            throw new PngjException("only rgb or rgba images support this");
        }
    }

    public void setGray(int g) {
        if (!this.imgInfo.greyscale) {
            throw new PngjException("only grayscale images support this");
        } else {
            this.gray = g;
        }
    }

    public int getGray() {
        if (!this.imgInfo.greyscale) {
            throw new PngjException("only grayscale images support this");
        } else {
            return this.gray;
        }
    }

    public void setPalletteAlpha(int[] palAlpha) {
        if (!this.imgInfo.indexed) {
            throw new PngjException("only indexed images support this");
        } else {
            this.paletteAlpha = palAlpha;
        }
    }

    public void setIndexEntryAsTransparent(int palAlphaIndex) {
        if (!this.imgInfo.indexed) {
            throw new PngjException("only indexed images support this");
        } else {
            this.paletteAlpha = new int[] { palAlphaIndex + 1 };
            for (int i = 0; i < palAlphaIndex; i++) {
                this.paletteAlpha[i] = 255;
            }
            this.paletteAlpha[palAlphaIndex] = 0;
        }
    }

    public int[] getPalletteAlpha() {
        return this.paletteAlpha;
    }
}