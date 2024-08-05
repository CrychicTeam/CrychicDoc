package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkBKGD extends PngChunkSingle {

    public static final String ID = "bKGD";

    private int gray;

    private int red;

    private int green;

    private int blue;

    private int paletteIndex;

    public PngChunkBKGD(ImageInfo info) {
        super("bKGD", info);
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
            c = this.createEmptyChunk(1, true);
            c.data[0] = (byte) this.paletteIndex;
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
            this.paletteIndex = c.data[0] & 255;
        } else {
            this.red = PngHelperInternal.readInt2fromBytes(c.data, 0);
            this.green = PngHelperInternal.readInt2fromBytes(c.data, 2);
            this.blue = PngHelperInternal.readInt2fromBytes(c.data, 4);
        }
    }

    public void setGray(int gray) {
        if (!this.imgInfo.greyscale) {
            throw new PngjException("only gray images support this");
        } else {
            this.gray = gray;
        }
    }

    public int getGray() {
        if (!this.imgInfo.greyscale) {
            throw new PngjException("only gray images support this");
        } else {
            return this.gray;
        }
    }

    public void setPaletteIndex(int i) {
        if (!this.imgInfo.indexed) {
            throw new PngjException("only indexed (pallete) images support this");
        } else {
            this.paletteIndex = i;
        }
    }

    public int getPaletteIndex() {
        if (!this.imgInfo.indexed) {
            throw new PngjException("only indexed (pallete) images support this");
        } else {
            return this.paletteIndex;
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
}