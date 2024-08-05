package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkOFFS extends PngChunkSingle {

    public static final String ID = "oFFs";

    private long posX;

    private long posY;

    private int units;

    public PngChunkOFFS(ImageInfo info) {
        super("oFFs", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_IDAT;
    }

    @Override
    public ChunkRaw createRawChunk() {
        ChunkRaw c = this.createEmptyChunk(9, true);
        PngHelperInternal.writeInt4tobytes((int) this.posX, c.data, 0);
        PngHelperInternal.writeInt4tobytes((int) this.posY, c.data, 4);
        c.data[8] = (byte) this.units;
        return c;
    }

    @Override
    public void parseFromRaw(ChunkRaw chunk) {
        if (chunk.len != 9) {
            throw new PngjException("bad chunk length " + chunk);
        } else {
            this.posX = (long) PngHelperInternal.readInt4fromBytes(chunk.data, 0);
            if (this.posX < 0L) {
                this.posX += 4294967296L;
            }
            this.posY = (long) PngHelperInternal.readInt4fromBytes(chunk.data, 4);
            if (this.posY < 0L) {
                this.posY += 4294967296L;
            }
            this.units = PngHelperInternal.readInt1fromByte(chunk.data, 8);
        }
    }

    public int getUnits() {
        return this.units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public long getPosX() {
        return this.posX;
    }

    public void setPosX(long posX) {
        this.posX = posX;
    }

    public long getPosY() {
        return this.posY;
    }

    public void setPosY(long posY) {
        this.posY = posY;
    }
}