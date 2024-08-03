package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjInputException;
import java.io.ByteArrayInputStream;

public class PngChunkIHDR extends PngChunkSingle {

    public static final String ID = "IHDR";

    private int cols;

    private int rows;

    private int bitspc;

    private int colormodel;

    private int compmeth;

    private int filmeth;

    private int interlaced;

    public PngChunkIHDR(ImageInfo info) {
        super("IHDR", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.NA;
    }

    @Override
    public ChunkRaw createRawChunk() {
        ChunkRaw c = new ChunkRaw(13, ChunkHelper.b_IHDR, true);
        int offset = 0;
        PngHelperInternal.writeInt4tobytes(this.cols, c.data, offset);
        offset += 4;
        PngHelperInternal.writeInt4tobytes(this.rows, c.data, offset);
        offset += 4;
        c.data[offset++] = (byte) this.bitspc;
        c.data[offset++] = (byte) this.colormodel;
        c.data[offset++] = (byte) this.compmeth;
        c.data[offset++] = (byte) this.filmeth;
        c.data[offset++] = (byte) this.interlaced;
        return c;
    }

    @Override
    public void parseFromRaw(ChunkRaw c) {
        if (c.len != 13) {
            throw new PngjException("Bad IDHR len " + c.len);
        } else {
            ByteArrayInputStream st = c.getAsByteStream();
            this.cols = PngHelperInternal.readInt4(st);
            this.rows = PngHelperInternal.readInt4(st);
            this.bitspc = PngHelperInternal.readByte(st);
            this.colormodel = PngHelperInternal.readByte(st);
            this.compmeth = PngHelperInternal.readByte(st);
            this.filmeth = PngHelperInternal.readByte(st);
            this.interlaced = PngHelperInternal.readByte(st);
        }
    }

    public int getCols() {
        return this.cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getBitspc() {
        return this.bitspc;
    }

    public void setBitspc(int bitspc) {
        this.bitspc = bitspc;
    }

    public int getColormodel() {
        return this.colormodel;
    }

    public void setColormodel(int colormodel) {
        this.colormodel = colormodel;
    }

    public int getCompmeth() {
        return this.compmeth;
    }

    public void setCompmeth(int compmeth) {
        this.compmeth = compmeth;
    }

    public int getFilmeth() {
        return this.filmeth;
    }

    public void setFilmeth(int filmeth) {
        this.filmeth = filmeth;
    }

    public int getInterlaced() {
        return this.interlaced;
    }

    public void setInterlaced(int interlaced) {
        this.interlaced = interlaced;
    }

    public boolean isInterlaced() {
        return this.getInterlaced() == 1;
    }

    public ImageInfo createImageInfo() {
        this.check();
        boolean alpha = (this.getColormodel() & 4) != 0;
        boolean palette = (this.getColormodel() & 1) != 0;
        boolean grayscale = this.getColormodel() == 0 || this.getColormodel() == 4;
        return new ImageInfo(this.getCols(), this.getRows(), this.getBitspc(), alpha, grayscale, palette);
    }

    public void check() {
        if (this.cols < 1 || this.rows < 1 || this.compmeth != 0 || this.filmeth != 0) {
            throw new PngjInputException("bad IHDR: col/row/compmethod/filmethod invalid");
        } else if (this.bitspc != 1 && this.bitspc != 2 && this.bitspc != 4 && this.bitspc != 8 && this.bitspc != 16) {
            throw new PngjInputException("bad IHDR: bitdepth invalid");
        } else if (this.interlaced >= 0 && this.interlaced <= 1) {
            switch(this.colormodel) {
                case 0:
                    break;
                case 1:
                case 5:
                default:
                    throw new PngjInputException("bad IHDR: invalid colormodel");
                case 2:
                case 4:
                case 6:
                    if (this.bitspc != 8 && this.bitspc != 16) {
                        throw new PngjInputException("bad IHDR: bitdepth invalid");
                    }
                    break;
                case 3:
                    if (this.bitspc == 16) {
                        throw new PngjInputException("bad IHDR: bitdepth invalid");
                    }
            }
        } else {
            throw new PngjInputException("bad IHDR: interlace invalid");
        }
    }
}