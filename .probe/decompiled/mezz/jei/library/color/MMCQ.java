package mezz.jei.library.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class MMCQ {

    private static final int SIGBITS = 5;

    private static final int RSHIFT = 3;

    private static final int MULT = 8;

    private static final int HISTOSIZE = 32768;

    private static final int VBOX_LENGTH = 32;

    private static final double FRACT_BY_POPULATION = 0.75;

    private static final int MAX_ITERATIONS = 1000;

    private static final Comparator<MMCQ.VBox> COMPARATOR_COUNT = new Comparator<MMCQ.VBox>() {

        public int compare(MMCQ.VBox a, MMCQ.VBox b) {
            return a.count(false) - b.count(false);
        }
    };

    private static final Comparator<MMCQ.VBox> COMPARATOR_PRODUCT = new Comparator<MMCQ.VBox>() {

        public int compare(MMCQ.VBox a, MMCQ.VBox b) {
            int aCount = a.count(false);
            int bCount = b.count(false);
            int aVolume = a.volume(false);
            int bVolume = b.volume(false);
            return aCount == bCount ? aVolume - bVolume : aCount * aVolume - bCount * bVolume;
        }
    };

    static int getColorIndex(int r, int g, int b) {
        return (r << 10) + (g << 5) + b;
    }

    private static int[] getHisto(int[][] pixels) {
        int[] histo = new int[32768];
        for (int[] pixel : pixels) {
            int rval = pixel[0] >> 3;
            int gval = pixel[1] >> 3;
            int bval = pixel[2] >> 3;
            int index = getColorIndex(rval, gval, bval);
            histo[index]++;
        }
        return histo;
    }

    private static MMCQ.VBox vboxFromPixels(int[][] pixels, int[] histo) {
        int rmin = 1000000;
        int rmax = 0;
        int gmin = 1000000;
        int gmax = 0;
        int bmin = 1000000;
        int bmax = 0;
        for (int[] pixel : pixels) {
            int rval = pixel[0] >> 3;
            int gval = pixel[1] >> 3;
            int bval = pixel[2] >> 3;
            if (rval < rmin) {
                rmin = rval;
            } else if (rval > rmax) {
                rmax = rval;
            }
            if (gval < gmin) {
                gmin = gval;
            } else if (gval > gmax) {
                gmax = gval;
            }
            if (bval < bmin) {
                bmin = bval;
            } else if (bval > bmax) {
                bmax = bval;
            }
        }
        return new MMCQ.VBox(rmin, rmax, gmin, gmax, bmin, bmax, histo);
    }

    private static MMCQ.VBox[] medianCutApply(int[] histo, MMCQ.VBox vbox) {
        if (vbox.count(false) == 0) {
            return null;
        } else if (vbox.count(false) == 1) {
            return new MMCQ.VBox[] { vbox.clone(), null };
        } else {
            int rw = vbox.r2 - vbox.r1 + 1;
            int gw = vbox.g2 - vbox.g1 + 1;
            int bw = vbox.b2 - vbox.b1 + 1;
            int maxw = Math.max(Math.max(rw, gw), bw);
            int total = 0;
            int[] partialsum = new int[32];
            Arrays.fill(partialsum, -1);
            int[] lookaheadsum = new int[32];
            Arrays.fill(lookaheadsum, -1);
            if (maxw == rw) {
                for (int i = vbox.r1; i <= vbox.r2; i++) {
                    int sum = 0;
                    for (int j = vbox.g1; j <= vbox.g2; j++) {
                        for (int k = vbox.b1; k <= vbox.b2; k++) {
                            int index = getColorIndex(i, j, k);
                            sum += histo[index];
                        }
                    }
                    total += sum;
                    partialsum[i] = total;
                }
            } else if (maxw == gw) {
                for (int i = vbox.g1; i <= vbox.g2; i++) {
                    int sum = 0;
                    for (int j = vbox.r1; j <= vbox.r2; j++) {
                        for (int k = vbox.b1; k <= vbox.b2; k++) {
                            int index = getColorIndex(j, i, k);
                            sum += histo[index];
                        }
                    }
                    total += sum;
                    partialsum[i] = total;
                }
            } else {
                for (int i = vbox.b1; i <= vbox.b2; i++) {
                    int sum = 0;
                    for (int j = vbox.r1; j <= vbox.r2; j++) {
                        for (int k = vbox.g1; k <= vbox.g2; k++) {
                            int index = getColorIndex(j, k, i);
                            sum += histo[index];
                        }
                    }
                    total += sum;
                    partialsum[i] = total;
                }
            }
            for (int var16 = 0; var16 < 32; var16++) {
                if (partialsum[var16] != -1) {
                    lookaheadsum[var16] = total - partialsum[var16];
                }
            }
            return maxw == rw ? doCut('r', vbox, partialsum, lookaheadsum, total) : (maxw == gw ? doCut('g', vbox, partialsum, lookaheadsum, total) : doCut('b', vbox, partialsum, lookaheadsum, total));
        }
    }

    private static MMCQ.VBox[] doCut(char color, MMCQ.VBox vbox, int[] partialsum, int[] lookaheadsum, int total) {
        int vbox_dim1;
        int vbox_dim2;
        if (color == 'r') {
            vbox_dim1 = vbox.r1;
            vbox_dim2 = vbox.r2;
        } else if (color == 'g') {
            vbox_dim1 = vbox.g1;
            vbox_dim2 = vbox.g2;
        } else {
            vbox_dim1 = vbox.b1;
            vbox_dim2 = vbox.b2;
        }
        MMCQ.VBox vbox1 = null;
        MMCQ.VBox vbox2 = null;
        for (int i = vbox_dim1; i <= vbox_dim2; i++) {
            if (partialsum[i] > total / 2) {
                vbox1 = vbox.clone();
                vbox2 = vbox.clone();
                int left = i - vbox_dim1;
                int right = vbox_dim2 - i;
                int d2;
                if (left <= right) {
                    d2 = Math.min(vbox_dim2 - 1, ~(~(i + right / 2)));
                } else {
                    d2 = Math.max(vbox_dim1, ~(~((int) ((double) (i - 1) - (double) left / 2.0))));
                }
                while (d2 < 0 || partialsum[d2] <= 0) {
                    d2++;
                }
                int count2 = lookaheadsum[d2];
                while (count2 == 0 && d2 > 0 && partialsum[d2 - 1] > 0) {
                    count2 = lookaheadsum[--d2];
                }
                if (color == 'r') {
                    vbox1.r2 = d2;
                    vbox2.r1 = d2 + 1;
                } else if (color == 'g') {
                    vbox1.g2 = d2;
                    vbox2.g1 = d2 + 1;
                } else {
                    vbox1.b2 = d2;
                    vbox2.b1 = d2 + 1;
                }
                return new MMCQ.VBox[] { vbox1, vbox2 };
            }
        }
        throw new RuntimeException("VBox can't be cut");
    }

    @Nullable
    public static MMCQ.CMap quantize(int[][] pixels, int maxcolors) {
        if (pixels.length != 0 && maxcolors >= 1 && maxcolors <= 256) {
            int[] histo = getHisto(pixels);
            MMCQ.VBox vbox = vboxFromPixels(pixels, histo);
            ArrayList<MMCQ.VBox> pq = new ArrayList();
            pq.add(vbox);
            int target = (int) Math.ceil(0.75 * (double) maxcolors);
            iter(pq, COMPARATOR_COUNT, target, histo);
            Collections.sort(pq, COMPARATOR_PRODUCT);
            iter(pq, COMPARATOR_PRODUCT, maxcolors - pq.size(), histo);
            Collections.reverse(pq);
            MMCQ.CMap cmap = new MMCQ.CMap();
            for (MMCQ.VBox vb : pq) {
                cmap.push(vb);
            }
            return cmap;
        } else {
            return null;
        }
    }

    private static void iter(List<MMCQ.VBox> lh, Comparator<MMCQ.VBox> comparator, int target, int[] histo) {
        int ncolors = 1;
        int niters = 0;
        while (niters < 1000) {
            MMCQ.VBox vbox = (MMCQ.VBox) lh.get(lh.size() - 1);
            if (vbox.count(false) == 0) {
                Collections.sort(lh, comparator);
                niters++;
            } else {
                lh.remove(lh.size() - 1);
                MMCQ.VBox[] vboxes = medianCutApply(histo, vbox);
                MMCQ.VBox vbox1 = vboxes[0];
                MMCQ.VBox vbox2 = vboxes[1];
                if (vbox1 == null) {
                    throw new RuntimeException("vbox1 not defined; shouldn't happen!");
                }
                lh.add(vbox1);
                if (vbox2 != null) {
                    lh.add(vbox2);
                    ncolors++;
                }
                Collections.sort(lh, comparator);
                if (ncolors >= target) {
                    return;
                }
                if (niters++ > 1000) {
                    return;
                }
            }
        }
    }

    public static class CMap {

        public final ArrayList<MMCQ.VBox> vboxes = new ArrayList();

        public void push(MMCQ.VBox box) {
            this.vboxes.add(box);
        }

        public int[][] palette() {
            int numVBoxes = this.vboxes.size();
            int[][] palette = new int[numVBoxes][];
            int numChosen = 0;
            int maxCount = 0;
            for (int i = 0; i < numVBoxes; i++) {
                MMCQ.VBox vBox = (MMCQ.VBox) this.vboxes.get(i);
                if ((double) vBox.count(false) >= (double) maxCount * 0.25) {
                    palette[numChosen] = vBox.avg(false);
                    numChosen++;
                    maxCount = vBox.count(false);
                }
            }
            return (int[][]) Arrays.copyOfRange(palette, 0, numChosen);
        }

        public int size() {
            return this.vboxes.size();
        }

        @Nullable
        public int[] map(int[] color) {
            int numVBoxes = this.vboxes.size();
            for (int i = 0; i < numVBoxes; i++) {
                MMCQ.VBox vbox = (MMCQ.VBox) this.vboxes.get(i);
                if (vbox.contains(color)) {
                    return vbox.avg(false);
                }
            }
            return this.nearest(color);
        }

        @Nullable
        public int[] nearest(int[] color) {
            double d1 = Double.MAX_VALUE;
            int[] pColor = null;
            int numVBoxes = this.vboxes.size();
            for (int i = 0; i < numVBoxes; i++) {
                int[] vbColor = ((MMCQ.VBox) this.vboxes.get(i)).avg(false);
                double d2 = ColorUtil.fastPerceptualColorDistanceSquared(color, vbColor);
                if (d2 < d1) {
                    d1 = d2;
                    pColor = vbColor;
                }
            }
            return pColor;
        }
    }

    public static class VBox {

        int r1;

        int r2;

        int g1;

        int g2;

        int b1;

        int b2;

        private final int[] histo;

        private int[] _avg;

        private Integer _volume;

        private Integer _count;

        public VBox(int r1, int r2, int g1, int g2, int b1, int b2, int[] histo) {
            this.r1 = r1;
            this.r2 = r2;
            this.g1 = g1;
            this.g2 = g2;
            this.b1 = b1;
            this.b2 = b2;
            this.histo = histo;
        }

        public String toString() {
            return "r1: " + this.r1 + " / r2: " + this.r2 + " / g1: " + this.g1 + " / g2: " + this.g2 + " / b1: " + this.b1 + " / b2: " + this.b2;
        }

        public int volume(boolean force) {
            if (this._volume == null || force) {
                this._volume = (this.r2 - this.r1 + 1) * (this.g2 - this.g1 + 1) * (this.b2 - this.b1 + 1);
            }
            return this._volume;
        }

        public int count(boolean force) {
            if (this._count == null || force) {
                int npix = 0;
                for (int i = this.r1; i <= this.r2; i++) {
                    for (int j = this.g1; j <= this.g2; j++) {
                        for (int k = this.b1; k <= this.b2; k++) {
                            int index = MMCQ.getColorIndex(i, j, k);
                            npix += this.histo[index];
                        }
                    }
                }
                this._count = npix;
            }
            return this._count;
        }

        public MMCQ.VBox clone() {
            return new MMCQ.VBox(this.r1, this.r2, this.g1, this.g2, this.b1, this.b2, this.histo);
        }

        public int[] avg(boolean force) {
            if (this._avg == null || force) {
                int ntot = 0;
                int rsum = 0;
                int gsum = 0;
                int bsum = 0;
                for (int i = this.r1; i <= this.r2; i++) {
                    for (int j = this.g1; j <= this.g2; j++) {
                        for (int k = this.b1; k <= this.b2; k++) {
                            int histoindex = MMCQ.getColorIndex(i, j, k);
                            int hval = this.histo[histoindex];
                            ntot += hval;
                            rsum = (int) ((double) rsum + (double) hval * ((double) i + 0.5) * 8.0);
                            gsum = (int) ((double) gsum + (double) hval * ((double) j + 0.5) * 8.0);
                            bsum = (int) ((double) bsum + (double) hval * ((double) k + 0.5) * 8.0);
                        }
                    }
                }
                if (ntot > 0) {
                    this._avg = new int[] { ~(~(rsum / ntot)), ~(~(gsum / ntot)), ~(~(bsum / ntot)) };
                } else {
                    this._avg = new int[] { ~(~(8 * (this.r1 + this.r2 + 1) / 2)), ~(~(8 * (this.g1 + this.g2 + 1) / 2)), ~(~(8 * (this.b1 + this.b2 + 1) / 2)) };
                }
            }
            return this._avg;
        }

        public boolean contains(int[] pixel) {
            int rval = pixel[0] >> 3;
            int gval = pixel[1] >> 3;
            int bval = pixel[2] >> 3;
            return rval >= this.r1 && rval <= this.r2 && gval >= this.g1 && gval <= this.g2 && bval >= this.b1 && bval <= this.b2;
        }
    }
}