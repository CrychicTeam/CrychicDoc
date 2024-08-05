package info.journeymap.shaded.ar.com.hjg.pngj.pixels;

import info.journeymap.shaded.ar.com.hjg.pngj.FilterType;
import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjOutputException;
import java.util.Arrays;

public class PixelsWriterDefault extends PixelsWriter {

    protected byte[] rowb;

    protected byte[] rowbprev;

    protected byte[] rowbfilter;

    protected FiltersPerformance filtersPerformance;

    protected FilterType curfilterType;

    protected int adaptMaxSkip;

    protected int adaptSkipIncreaseSinceRow;

    protected double adaptSkipIncreaseFactor;

    protected int adaptNextRow = 0;

    public PixelsWriterDefault(ImageInfo imgInfo) {
        super(imgInfo);
        this.filtersPerformance = new FiltersPerformance(imgInfo);
    }

    @Override
    protected void initParams() {
        super.initParams();
        if (this.rowb == null || this.rowb.length < this.buflen) {
            this.rowb = new byte[this.buflen];
        }
        if (this.rowbfilter == null || this.rowbfilter.length < this.buflen) {
            this.rowbfilter = new byte[this.buflen];
        }
        if (this.rowbprev != null && this.rowbprev.length >= this.buflen) {
            Arrays.fill(this.rowbprev, (byte) 0);
        } else {
            this.rowbprev = new byte[this.buflen];
        }
        if (this.imgInfo.cols < 3 && !FilterType.isValidStandard(this.filterType)) {
            this.filterType = FilterType.FILTER_DEFAULT;
        }
        if (this.imgInfo.rows < 3 && !FilterType.isValidStandard(this.filterType)) {
            this.filterType = FilterType.FILTER_DEFAULT;
        }
        if (this.imgInfo.getTotalPixels() <= 1024L && !FilterType.isValidStandard(this.filterType)) {
            this.filterType = this.getDefaultFilter();
        }
        if (FilterType.isAdaptive(this.filterType)) {
            this.adaptNextRow = 0;
            if (this.filterType == FilterType.FILTER_ADAPTIVE_FAST) {
                this.adaptMaxSkip = 200;
                this.adaptSkipIncreaseSinceRow = 3;
                this.adaptSkipIncreaseFactor = 0.25;
            } else if (this.filterType == FilterType.FILTER_ADAPTIVE_MEDIUM) {
                this.adaptMaxSkip = 8;
                this.adaptSkipIncreaseSinceRow = 32;
                this.adaptSkipIncreaseFactor = 0.0125;
            } else {
                if (this.filterType != FilterType.FILTER_ADAPTIVE_FULL) {
                    throw new PngjOutputException("bad filter " + this.filterType);
                }
                this.adaptMaxSkip = 0;
                this.adaptSkipIncreaseSinceRow = 128;
                this.adaptSkipIncreaseFactor = 0.008333333333333333;
            }
        }
    }

    @Override
    protected void filterAndWrite(byte[] rowb) {
        if (rowb != this.rowb) {
            throw new RuntimeException("??");
        } else {
            this.decideCurFilterType();
            byte[] filtered = this.filterRowWithFilterType(this.curfilterType, rowb, this.rowbprev, this.rowbfilter);
            this.sendToCompressedStream(filtered);
            byte[] aux = this.rowb;
            this.rowb = this.rowbprev;
            this.rowbprev = aux;
        }
    }

    protected void decideCurFilterType() {
        if (FilterType.isValidStandard(this.getFilterType())) {
            this.curfilterType = this.getFilterType();
        } else if (this.getFilterType() == FilterType.FILTER_PRESERVE) {
            this.curfilterType = FilterType.getByVal(this.rowb[0]);
        } else if (this.getFilterType() == FilterType.FILTER_CYCLIC) {
            this.curfilterType = FilterType.getByVal(this.currentRow % 5);
        } else if (this.getFilterType() == FilterType.FILTER_DEFAULT) {
            this.setFilterType(this.getDefaultFilter());
            this.curfilterType = this.getFilterType();
        } else {
            if (!FilterType.isAdaptive(this.getFilterType())) {
                throw new PngjOutputException("not implemented filter: " + this.getFilterType());
            }
            if (this.currentRow == this.adaptNextRow) {
                for (FilterType ftype : FilterType.getAllStandard()) {
                    this.filtersPerformance.updateFromRaw(ftype, this.rowb, this.rowbprev, this.currentRow);
                }
                this.curfilterType = this.filtersPerformance.getPreferred();
                int skip = this.currentRow >= this.adaptSkipIncreaseSinceRow ? (int) Math.round((double) (this.currentRow - this.adaptSkipIncreaseSinceRow) * this.adaptSkipIncreaseFactor) : 0;
                if (skip > this.adaptMaxSkip) {
                    skip = this.adaptMaxSkip;
                }
                if (this.currentRow == 0) {
                    skip = 0;
                }
                this.adaptNextRow = this.currentRow + 1 + skip;
            }
        }
        if (this.currentRow == 0 && this.curfilterType != FilterType.FILTER_NONE && this.curfilterType != FilterType.FILTER_SUB) {
            this.curfilterType = FilterType.FILTER_SUB;
        }
    }

    @Override
    public byte[] getRowb() {
        if (!this.initdone) {
            this.init();
        }
        return this.rowb;
    }

    @Override
    public void close() {
        super.close();
    }

    public void setPreferenceForNone(double preferenceForNone) {
        this.filtersPerformance.setPreferenceForNone(preferenceForNone);
    }

    public void tuneMemory(double m) {
        this.filtersPerformance.tuneMemory(m);
    }

    public void setFilterWeights(double[] weights) {
        this.filtersPerformance.setFilterWeights(weights);
    }
}