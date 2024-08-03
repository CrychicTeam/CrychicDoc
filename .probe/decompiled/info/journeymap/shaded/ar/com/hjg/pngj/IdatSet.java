package info.journeymap.shaded.ar.com.hjg.pngj;

import java.util.Arrays;
import java.util.zip.Checksum;
import java.util.zip.Inflater;

public class IdatSet extends DeflatedChunksSet {

    protected byte[] rowUnfiltered;

    protected byte[] rowUnfilteredPrev;

    protected final ImageInfo imgInfo;

    protected final Deinterlacer deinterlacer;

    final RowInfo rowinfo;

    protected int[] filterUseStat = new int[5];

    public IdatSet(String id, ImageInfo iminfo, Deinterlacer deinterlacer) {
        this(id, iminfo, deinterlacer, null, null);
    }

    public IdatSet(String id, ImageInfo iminfo, Deinterlacer deinterlacer, Inflater inf, byte[] buffer) {
        super(id, deinterlacer != null ? deinterlacer.getBytesToRead() + 1 : iminfo.bytesPerRow + 1, iminfo.bytesPerRow + 1, inf, buffer);
        this.imgInfo = iminfo;
        this.deinterlacer = deinterlacer;
        this.rowinfo = new RowInfo(iminfo, deinterlacer);
    }

    public void unfilterRow() {
        this.unfilterRow(this.rowinfo.bytesRow);
    }

    protected void unfilterRow(int nbytes) {
        if (this.rowUnfiltered == null || this.rowUnfiltered.length < this.row.length) {
            this.rowUnfiltered = new byte[this.row.length];
            this.rowUnfilteredPrev = new byte[this.row.length];
        }
        if (this.rowinfo.rowNsubImg == 0) {
            Arrays.fill(this.rowUnfiltered, (byte) 0);
        }
        byte[] tmp = this.rowUnfiltered;
        this.rowUnfiltered = this.rowUnfilteredPrev;
        this.rowUnfilteredPrev = tmp;
        int ftn = this.row[0];
        FilterType ft = FilterType.getByVal(ftn);
        if (ft == null) {
            throw new PngjInputException("Filter type " + ftn + " invalid");
        } else {
            this.filterUseStat[ftn]++;
            this.rowUnfiltered[0] = this.row[0];
            switch(ft) {
                case FILTER_NONE:
                    this.unfilterRowNone(nbytes);
                    break;
                case FILTER_SUB:
                    this.unfilterRowSub(nbytes);
                    break;
                case FILTER_UP:
                    this.unfilterRowUp(nbytes);
                    break;
                case FILTER_AVERAGE:
                    this.unfilterRowAverage(nbytes);
                    break;
                case FILTER_PAETH:
                    this.unfilterRowPaeth(nbytes);
                    break;
                default:
                    throw new PngjInputException("Filter type " + ftn + " not implemented");
            }
        }
    }

    private void unfilterRowAverage(int nbytes) {
        int j = 1 - this.imgInfo.bytesPixel;
        for (int i = 1; i <= nbytes; j++) {
            int x = j > 0 ? this.rowUnfiltered[j] & 255 : 0;
            this.rowUnfiltered[i] = (byte) (this.row[i] + (x + (this.rowUnfilteredPrev[i] & 255)) / 2);
            i++;
        }
    }

    private void unfilterRowNone(int nbytes) {
        for (int i = 1; i <= nbytes; i++) {
            this.rowUnfiltered[i] = this.row[i];
        }
    }

    private void unfilterRowPaeth(int nbytes) {
        int j = 1 - this.imgInfo.bytesPixel;
        for (int i = 1; i <= nbytes; j++) {
            int x = j > 0 ? this.rowUnfiltered[j] & 255 : 0;
            int y = j > 0 ? this.rowUnfilteredPrev[j] & 255 : 0;
            this.rowUnfiltered[i] = (byte) (this.row[i] + PngHelperInternal.filterPaethPredictor(x, this.rowUnfilteredPrev[i] & 255, y));
            i++;
        }
    }

    private void unfilterRowSub(int nbytes) {
        for (int i = 1; i <= this.imgInfo.bytesPixel; i++) {
            this.rowUnfiltered[i] = this.row[i];
        }
        int j = 1;
        for (int var4 = this.imgInfo.bytesPixel + 1; var4 <= nbytes; j++) {
            this.rowUnfiltered[var4] = (byte) (this.row[var4] + this.rowUnfiltered[j]);
            var4++;
        }
    }

    private void unfilterRowUp(int nbytes) {
        for (int i = 1; i <= nbytes; i++) {
            this.rowUnfiltered[i] = (byte) (this.row[i] + this.rowUnfilteredPrev[i]);
        }
    }

    @Override
    protected void preProcessRow() {
        super.preProcessRow();
        this.rowinfo.update(this.getRown());
        this.unfilterRow();
        this.rowinfo.updateBuf(this.rowUnfiltered, this.rowinfo.bytesRow + 1);
    }

    @Override
    protected int processRowCallback() {
        return this.advanceToNextRow();
    }

    @Override
    protected void processDoneCallback() {
    }

    public int advanceToNextRow() {
        int bytesNextRow;
        if (this.deinterlacer == null) {
            bytesNextRow = this.getRown() >= this.imgInfo.rows - 1 ? 0 : this.imgInfo.bytesPerRow + 1;
        } else {
            boolean more = this.deinterlacer.nextRow();
            bytesNextRow = more ? this.deinterlacer.getBytesToRead() + 1 : 0;
        }
        if (!this.isCallbackMode()) {
            this.prepareForNextRow(bytesNextRow);
        }
        return bytesNextRow;
    }

    @Override
    public boolean isRowReady() {
        return !this.isWaitingForMoreInput();
    }

    public byte[] getUnfilteredRow() {
        return this.rowUnfiltered;
    }

    public Deinterlacer getDeinterlacer() {
        return this.deinterlacer;
    }

    void updateCrcs(Checksum... idatCrcs) {
        for (Checksum idatCrca : idatCrcs) {
            if (idatCrca != null) {
                idatCrca.update(this.getUnfilteredRow(), 1, this.getRowFilled() - 1);
            }
        }
    }

    @Override
    public void close() {
        super.close();
        this.rowUnfiltered = null;
        this.rowUnfilteredPrev = null;
    }

    public int[] getFilterUseStat() {
        return this.filterUseStat;
    }
}