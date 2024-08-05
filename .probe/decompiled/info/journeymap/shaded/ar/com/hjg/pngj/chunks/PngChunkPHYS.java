package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkPHYS extends PngChunkSingle {

    public static final String ID = "pHYs";

    private long pixelsxUnitX;

    private long pixelsxUnitY;

    private int units;

    public PngChunkPHYS(ImageInfo info) {
        super("pHYs", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_IDAT;
    }

    @Override
    public ChunkRaw createRawChunk() {
        ChunkRaw c = this.createEmptyChunk(9, true);
        PngHelperInternal.writeInt4tobytes((int) this.pixelsxUnitX, c.data, 0);
        PngHelperInternal.writeInt4tobytes((int) this.pixelsxUnitY, c.data, 4);
        c.data[8] = (byte) this.units;
        return c;
    }

    @Override
    public void parseFromRaw(ChunkRaw chunk) {
        if (chunk.len != 9) {
            throw new PngjException("bad chunk length " + chunk);
        } else {
            this.pixelsxUnitX = (long) PngHelperInternal.readInt4fromBytes(chunk.data, 0);
            if (this.pixelsxUnitX < 0L) {
                this.pixelsxUnitX += 4294967296L;
            }
            this.pixelsxUnitY = (long) PngHelperInternal.readInt4fromBytes(chunk.data, 4);
            if (this.pixelsxUnitY < 0L) {
                this.pixelsxUnitY += 4294967296L;
            }
            this.units = PngHelperInternal.readInt1fromByte(chunk.data, 8);
        }
    }

    public long getPixelsxUnitX() {
        return this.pixelsxUnitX;
    }

    public void setPixelsxUnitX(long pixelsxUnitX) {
        this.pixelsxUnitX = pixelsxUnitX;
    }

    public long getPixelsxUnitY() {
        return this.pixelsxUnitY;
    }

    public void setPixelsxUnitY(long pixelsxUnitY) {
        this.pixelsxUnitY = pixelsxUnitY;
    }

    public int getUnits() {
        return this.units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public double getAsDpi() {
        return this.units == 1 && this.pixelsxUnitX == this.pixelsxUnitY ? (double) this.pixelsxUnitX * 0.0254 : -1.0;
    }

    public double[] getAsDpi2() {
        return this.units != 1 ? new double[] { -1.0, -1.0 } : new double[] { (double) this.pixelsxUnitX * 0.0254, (double) this.pixelsxUnitY * 0.0254 };
    }

    public void setAsDpi(double dpi) {
        this.units = 1;
        this.pixelsxUnitX = (long) (dpi / 0.0254 + 0.5);
        this.pixelsxUnitY = this.pixelsxUnitX;
    }

    public void setAsDpi2(double dpix, double dpiy) {
        this.units = 1;
        this.pixelsxUnitX = (long) (dpix / 0.0254 + 0.5);
        this.pixelsxUnitY = (long) (dpiy / 0.0254 + 0.5);
    }
}