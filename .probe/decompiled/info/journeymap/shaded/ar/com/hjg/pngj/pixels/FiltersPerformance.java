package info.journeymap.shaded.ar.com.hjg.pngj.pixels;

import info.journeymap.shaded.ar.com.hjg.pngj.FilterType;
import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngHelperInternal;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjExceptionInternal;
import java.util.Arrays;

public class FiltersPerformance {

    private final ImageInfo iminfo;

    private double memoryA = 0.7;

    private int lastrow = -1;

    private double[] absum = new double[5];

    private double[] entropy = new double[5];

    private double[] cost = new double[5];

    private int[] histog = new int[256];

    private int lastprefered = -1;

    private boolean initdone = false;

    private double preferenceForNone = 1.0;

    public static final double[] FILTER_WEIGHTS_DEFAULT = new double[] { 0.73, 1.03, 0.97, 1.11, 1.22 };

    private double[] filter_weights = new double[] { -1.0, -1.0, -1.0, -1.0, -1.0 };

    private static final double LOG2NI = -1.0 / Math.log(2.0);

    public FiltersPerformance(ImageInfo imgInfo) {
        this.iminfo = imgInfo;
    }

    private void init() {
        if (this.filter_weights[0] < 0.0) {
            System.arraycopy(FILTER_WEIGHTS_DEFAULT, 0, this.filter_weights, 0, 5);
            double wNone = this.filter_weights[0];
            if (this.iminfo.bitDepth == 16) {
                wNone = 1.2;
            } else if (this.iminfo.alpha) {
                wNone = 0.8;
            } else if (this.iminfo.indexed || this.iminfo.bitDepth < 8) {
                wNone = 0.4;
            }
            wNone /= this.preferenceForNone;
            this.filter_weights[0] = wNone;
        }
        Arrays.fill(this.cost, 1.0);
        this.initdone = true;
    }

    public void updateFromFiltered(FilterType ftype, byte[] rowff, int rown) {
        this.updateFromRawOrFiltered(ftype, rowff, null, null, rown);
    }

    public void updateFromRaw(FilterType ftype, byte[] rowb, byte[] rowbprev, int rown) {
        this.updateFromRawOrFiltered(ftype, null, rowb, rowbprev, rown);
    }

    private void updateFromRawOrFiltered(FilterType ftype, byte[] rowff, byte[] rowb, byte[] rowbprev, int rown) {
        if (!this.initdone) {
            this.init();
        }
        if (rown != this.lastrow) {
            Arrays.fill(this.absum, Double.NaN);
            Arrays.fill(this.entropy, Double.NaN);
        }
        this.lastrow = rown;
        if (rowff != null) {
            this.computeHistogram(rowff);
        } else {
            this.computeHistogramForFilter(ftype, rowb, rowbprev);
        }
        if (ftype == FilterType.FILTER_NONE) {
            this.entropy[ftype.val] = this.computeEntropyFromHistogram();
        } else {
            this.absum[ftype.val] = this.computeAbsFromHistogram();
        }
    }

    public FilterType getPreferred() {
        int fi = 0;
        double vali = Double.MAX_VALUE;
        double val = 0.0;
        for (int i = 0; i < 5; i++) {
            if (!Double.isNaN(this.absum[i])) {
                val = this.absum[i];
            } else {
                if (Double.isNaN(this.entropy[i])) {
                    continue;
                }
                val = (Math.pow(2.0, this.entropy[i]) - 1.0) * 0.5;
            }
            val *= this.filter_weights[i];
            val = this.cost[i] * this.memoryA + (1.0 - this.memoryA) * val;
            this.cost[i] = val;
            if (val < vali) {
                vali = val;
                fi = i;
            }
        }
        this.lastprefered = fi;
        return FilterType.getByVal(this.lastprefered);
    }

    public final void computeHistogramForFilter(FilterType filterType, byte[] rowb, byte[] rowbprev) {
        Arrays.fill(this.histog, 0);
        int imax = this.iminfo.bytesPerRow;
        switch(filterType) {
            case FILTER_NONE:
                for (int i = 1; i <= imax; i++) {
                    this.histog[rowb[i] & 255]++;
                }
                break;
            case FILTER_PAETH:
                for (int var11 = 1; var11 <= imax; var11++) {
                    this.histog[PngHelperInternal.filterRowPaeth(rowb[var11], 0, rowbprev[var11] & 255, 0)]++;
                }
                int j = 1;
                for (int var12 = this.iminfo.bytesPixel + 1; var12 <= imax; j++) {
                    this.histog[PngHelperInternal.filterRowPaeth(rowb[var12], rowb[j] & 255, rowbprev[var12] & 255, rowbprev[j] & 255)]++;
                    var12++;
                }
                break;
            case FILTER_SUB:
                for (int var9 = 1; var9 <= this.iminfo.bytesPixel; var9++) {
                    this.histog[rowb[var9] & 255]++;
                }
                int var14 = 1;
                for (int var10 = this.iminfo.bytesPixel + 1; var10 <= imax; var14++) {
                    this.histog[rowb[var10] - rowb[var14] & 0xFF]++;
                    var10++;
                }
                break;
            case FILTER_UP:
                for (int var8 = 1; var8 <= this.iminfo.bytesPerRow; var8++) {
                    this.histog[rowb[var8] - rowbprev[var8] & 0xFF]++;
                }
                break;
            case FILTER_AVERAGE:
                for (int i = 1; i <= this.iminfo.bytesPixel; i++) {
                    this.histog[(rowb[i] & 255) - (rowbprev[i] & 255) / 2 & 0xFF]++;
                }
                int j = 1;
                for (int var7 = this.iminfo.bytesPixel + 1; var7 <= imax; j++) {
                    this.histog[(rowb[var7] & 255) - ((rowbprev[var7] & 255) + (rowb[j] & 255)) / 2 & 0xFF]++;
                    var7++;
                }
                break;
            default:
                throw new PngjExceptionInternal("Bad filter:" + filterType);
        }
    }

    public void computeHistogram(byte[] rowff) {
        Arrays.fill(this.histog, 0);
        for (int i = 1; i < this.iminfo.bytesPerRow; i++) {
            this.histog[rowff[i] & 255]++;
        }
    }

    public double computeAbsFromHistogram() {
        int s = 0;
        for (int i = 1; i < 128; i++) {
            s += this.histog[i] * i;
        }
        int i = 128;
        for (int j = 128; j > 0; j--) {
            s += this.histog[i] * j;
            i++;
        }
        return (double) s / (double) this.iminfo.bytesPerRow;
    }

    public final double computeEntropyFromHistogram() {
        double s = 1.0 / (double) this.iminfo.bytesPerRow;
        double ls = Math.log(s);
        double h = 0.0;
        for (int x : this.histog) {
            if (x > 0) {
                h += (Math.log((double) x) + ls) * (double) x;
            }
        }
        h *= s * LOG2NI;
        if (h < 0.0) {
            h = 0.0;
        }
        return h;
    }

    public void setPreferenceForNone(double preferenceForNone) {
        this.preferenceForNone = preferenceForNone;
    }

    public void tuneMemory(double m) {
        if (m == 0.0) {
            this.memoryA = 0.0;
        } else {
            this.memoryA = Math.pow(this.memoryA, 1.0 / m);
        }
    }

    public void setFilterWeights(double[] weights) {
        System.arraycopy(weights, 0, this.filter_weights, 0, 5);
    }
}