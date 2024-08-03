package info.journeymap.shaded.ar.com.hjg.pngj.pixels;

import info.journeymap.shaded.ar.com.hjg.pngj.FilterType;
import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjOutputException;
import java.io.OutputStream;

public abstract class PixelsWriter {

    protected final ImageInfo imgInfo;

    protected final int buflen;

    protected final int bytesPixel;

    protected final int bytesRow;

    private CompressorStream compressorStream;

    protected int deflaterCompLevel = 6;

    protected int deflaterStrategy = 0;

    protected boolean initdone = false;

    protected FilterType filterType;

    private int[] filtersUsed = new int[5];

    private OutputStream os;

    protected int currentRow;

    public PixelsWriter(ImageInfo imgInfo) {
        this.imgInfo = imgInfo;
        this.bytesRow = imgInfo.bytesPerRow;
        this.buflen = this.bytesRow + 1;
        this.bytesPixel = imgInfo.bytesPixel;
        this.currentRow = -1;
        this.filterType = FilterType.FILTER_DEFAULT;
    }

    public final void processRow(byte[] rowb) {
        if (!this.initdone) {
            this.init();
        }
        this.currentRow++;
        this.filterAndWrite(rowb);
    }

    protected void sendToCompressedStream(byte[] rowf) {
        this.compressorStream.write(rowf, 0, rowf.length);
        this.filtersUsed[rowf[0]]++;
    }

    protected abstract void filterAndWrite(byte[] var1);

    protected final byte[] filterRowWithFilterType(FilterType _filterType, byte[] _rowb, byte[] _rowbprev, byte[] _rowf) {
        if (_filterType == FilterType.FILTER_NONE) {
            _rowf = _rowb;
        }
        _rowf[0] = (byte) _filterType.val;
        switch(_filterType) {
            case FILTER_NONE:
                break;
            case FILTER_PAETH:
                for (int i = 1; i <= this.bytesPixel; i++) {
                    _rowf[i] = (byte) PngHelperInternal.filterRowPaeth(_rowb[i], 0, _rowbprev[i] & 255, 0);
                }
                int j = 1;
                for (int var12 = this.bytesPixel + 1; var12 <= this.bytesRow; j++) {
                    _rowf[var12] = (byte) PngHelperInternal.filterRowPaeth(_rowb[var12], _rowb[j] & 255, _rowbprev[var12] & 255, _rowbprev[j] & 255);
                    var12++;
                }
                break;
            case FILTER_SUB:
                for (int var9 = 1; var9 <= this.bytesPixel; var9++) {
                    _rowf[var9] = _rowb[var9];
                }
                int var13 = 1;
                for (int var10 = this.bytesPixel + 1; var10 <= this.bytesRow; var13++) {
                    _rowf[var10] = (byte) (_rowb[var10] - _rowb[var13]);
                    var10++;
                }
                break;
            case FILTER_AVERAGE:
                for (int var7 = 1; var7 <= this.bytesPixel; var7++) {
                    _rowf[var7] = (byte) (_rowb[var7] - (_rowbprev[var7] & 255) / 2);
                }
                int j = 1;
                for (int var8 = this.bytesPixel + 1; var8 <= this.bytesRow; j++) {
                    _rowf[var8] = (byte) (_rowb[var8] - ((_rowbprev[var8] & 255) + (_rowb[j] & 255)) / 2);
                    var8++;
                }
                break;
            case FILTER_UP:
                for (int i = 1; i <= this.bytesRow; i++) {
                    _rowf[i] = (byte) (_rowb[i] - _rowbprev[i]);
                }
                break;
            default:
                throw new PngjOutputException("Filter type not recognized: " + _filterType);
        }
        return _rowf;
    }

    public abstract byte[] getRowb();

    protected final void init() {
        if (!this.initdone) {
            this.initParams();
            this.initdone = true;
        }
    }

    protected void initParams() {
        if (this.compressorStream == null) {
            this.compressorStream = new CompressorStreamDeflater(this.os, this.buflen, this.imgInfo.getTotalRawBytes(), this.deflaterCompLevel, this.deflaterStrategy);
        }
    }

    public void close() {
        if (this.compressorStream != null) {
            this.compressorStream.close();
        }
    }

    public void setDeflaterStrategy(Integer deflaterStrategy) {
        this.deflaterStrategy = deflaterStrategy;
    }

    public void setDeflaterCompLevel(Integer deflaterCompLevel) {
        this.deflaterCompLevel = deflaterCompLevel;
    }

    public Integer getDeflaterCompLevel() {
        return this.deflaterCompLevel;
    }

    public final void setOs(OutputStream datStream) {
        this.os = datStream;
    }

    public OutputStream getOs() {
        return this.os;
    }

    public final FilterType getFilterType() {
        return this.filterType;
    }

    public final void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public double getCompression() {
        return this.compressorStream.isDone() ? this.compressorStream.getCompressionRatio() : 1.0;
    }

    public void setCompressorStream(CompressorStream compressorStream) {
        this.compressorStream = compressorStream;
    }

    public long getTotalBytesToWrite() {
        return this.imgInfo.getTotalRawBytes();
    }

    protected FilterType getDefaultFilter() {
        if (!this.imgInfo.indexed && this.imgInfo.bitDepth >= 8) {
            if (this.imgInfo.getTotalPixels() < 1024L) {
                return FilterType.FILTER_NONE;
            } else if (this.imgInfo.rows == 1) {
                return FilterType.FILTER_SUB;
            } else {
                return this.imgInfo.cols == 1 ? FilterType.FILTER_UP : FilterType.FILTER_PAETH;
            }
        } else {
            return FilterType.FILTER_NONE;
        }
    }

    public final String getFiltersUsed() {
        return String.format("%d,%d,%d,%d,%d", (int) ((double) this.filtersUsed[0] * 100.0 / (double) this.imgInfo.rows + 0.5), (int) ((double) this.filtersUsed[1] * 100.0 / (double) this.imgInfo.rows + 0.5), (int) ((double) this.filtersUsed[2] * 100.0 / (double) this.imgInfo.rows + 0.5), (int) ((double) this.filtersUsed[3] * 100.0 / (double) this.imgInfo.rows + 0.5), (int) ((double) this.filtersUsed[4] * 100.0 / (double) this.imgInfo.rows + 0.5));
    }
}