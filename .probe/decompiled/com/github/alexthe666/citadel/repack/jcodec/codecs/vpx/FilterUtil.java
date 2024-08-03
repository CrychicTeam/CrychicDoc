package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx;

import com.github.alexthe666.citadel.repack.jcodec.api.NotImplementedException;

public class FilterUtil {

    private static int clipPlus128(int v) {
        return clipSigned(v) + 128;
    }

    private static int clipSigned(int v) {
        return v < -128 ? -128 : (v > 127 ? 127 : v);
    }

    private static int minus128(int v) {
        return v - 128;
    }

    public static void loopFilterUV(VPXMacroblock[][] mbs, int sharpnessLevel, boolean keyFrame) {
        for (int y = 0; y < mbs.length - 2; y++) {
            for (int x = 0; x < mbs[0].length - 2; x++) {
                VPXMacroblock rmb = mbs[y + 1][x + 1];
                VPXMacroblock bmb = mbs[y + 1][x + 1];
                int loop_filter_level = rmb.filterLevel;
                if (loop_filter_level != 0) {
                    int interior_limit = rmb.filterLevel;
                    if (sharpnessLevel > 0) {
                        interior_limit >>= sharpnessLevel > 4 ? 2 : 1;
                        if (interior_limit > 9 - sharpnessLevel) {
                            interior_limit = 9 - sharpnessLevel;
                        }
                    }
                    if (interior_limit == 0) {
                        interior_limit = 1;
                    }
                    int hev_threshold = 0;
                    if (!keyFrame) {
                        throw new NotImplementedException("TODO: non-key frames are not supported yet.");
                    }
                    if (loop_filter_level >= 40) {
                        hev_threshold = 2;
                    } else if (loop_filter_level >= 15) {
                        hev_threshold = 1;
                    }
                    int mbedge_limit = (loop_filter_level + 2) * 2 + interior_limit;
                    int sub_bedge_limit = loop_filter_level * 2 + interior_limit;
                    if (x > 0) {
                        VPXMacroblock lmb = mbs[y + 1][x + 1 - 1];
                        for (int b = 0; b < 2; b++) {
                            VPXMacroblock.Subblock rsbU = rmb.uSubblocks[b][0];
                            VPXMacroblock.Subblock lsbU = lmb.uSubblocks[b][1];
                            VPXMacroblock.Subblock rsbV = rmb.vSubblocks[b][0];
                            VPXMacroblock.Subblock lsbV = lmb.vSubblocks[b][1];
                            for (int a = 0; a < 4; a++) {
                                FilterUtil.Segment seg = FilterUtil.Segment.horizontal(rsbU, lsbU, a);
                                seg.filterMb(hev_threshold, interior_limit, mbedge_limit);
                                seg.applyHorizontally(rsbU, lsbU, a);
                                seg = FilterUtil.Segment.horizontal(rsbV, lsbV, a);
                                seg.filterMb(hev_threshold, interior_limit, mbedge_limit);
                                seg.applyHorizontally(rsbV, lsbV, a);
                            }
                        }
                    }
                    if (!rmb.skipFilter) {
                        for (int a = 1; a < 2; a++) {
                            for (int b = 0; b < 2; b++) {
                                VPXMacroblock.Subblock lsbU = rmb.uSubblocks[b][a - 1];
                                VPXMacroblock.Subblock rsbU = rmb.uSubblocks[b][a];
                                VPXMacroblock.Subblock lsbV = rmb.vSubblocks[b][a - 1];
                                VPXMacroblock.Subblock rsbV = rmb.vSubblocks[b][a];
                                for (int c = 0; c < 4; c++) {
                                    FilterUtil.Segment seg = FilterUtil.Segment.horizontal(rsbU, lsbU, c);
                                    seg.filterSb(hev_threshold, interior_limit, sub_bedge_limit);
                                    seg.applyHorizontally(rsbU, lsbU, c);
                                    seg = FilterUtil.Segment.horizontal(rsbV, lsbV, c);
                                    seg.filterSb(hev_threshold, interior_limit, sub_bedge_limit);
                                    seg.applyHorizontally(rsbV, lsbV, c);
                                }
                            }
                        }
                    }
                    if (y > 0) {
                        VPXMacroblock tmb = mbs[y + 1 - 1][x + 1];
                        for (int b = 0; b < 2; b++) {
                            VPXMacroblock.Subblock tsbU = tmb.uSubblocks[1][b];
                            VPXMacroblock.Subblock bsbU = bmb.uSubblocks[0][b];
                            VPXMacroblock.Subblock tsbV = tmb.vSubblocks[1][b];
                            VPXMacroblock.Subblock bsbV = bmb.vSubblocks[0][b];
                            for (int a = 0; a < 4; a++) {
                                FilterUtil.Segment seg = FilterUtil.Segment.vertical(bsbU, tsbU, a);
                                seg.filterMb(hev_threshold, interior_limit, mbedge_limit);
                                seg.applyVertically(bsbU, tsbU, a);
                                seg = FilterUtil.Segment.vertical(bsbV, tsbV, a);
                                seg.filterMb(hev_threshold, interior_limit, mbedge_limit);
                                seg.applyVertically(bsbV, tsbV, a);
                            }
                        }
                    }
                    if (!rmb.skipFilter) {
                        for (int a = 1; a < 2; a++) {
                            for (int b = 0; b < 2; b++) {
                                VPXMacroblock.Subblock tsbU = bmb.uSubblocks[a - 1][b];
                                VPXMacroblock.Subblock bsbU = bmb.uSubblocks[a][b];
                                VPXMacroblock.Subblock tsbV = bmb.vSubblocks[a - 1][b];
                                VPXMacroblock.Subblock bsbV = bmb.vSubblocks[a][b];
                                for (int c = 0; c < 4; c++) {
                                    FilterUtil.Segment seg = FilterUtil.Segment.vertical(bsbU, tsbU, c);
                                    seg.filterSb(hev_threshold, interior_limit, sub_bedge_limit);
                                    seg.applyVertically(bsbU, tsbU, c);
                                    seg = FilterUtil.Segment.vertical(bsbV, tsbV, c);
                                    seg.filterSb(hev_threshold, interior_limit, sub_bedge_limit);
                                    seg.applyVertically(bsbV, tsbV, c);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void loopFilterY(VPXMacroblock[][] mbs, int sharpnessLevel, boolean keyFrame) {
        for (int y = 0; y < mbs.length - 2; y++) {
            for (int x = 0; x < mbs[0].length - 2; x++) {
                VPXMacroblock rmb = mbs[y + 1][x + 1];
                VPXMacroblock bmb = mbs[y + 1][x + 1];
                int loopFilterLevel = rmb.filterLevel;
                if (loopFilterLevel != 0) {
                    int interiorLimit = rmb.filterLevel;
                    if (sharpnessLevel > 0) {
                        interiorLimit >>= sharpnessLevel > 4 ? 2 : 1;
                        if (interiorLimit > 9 - sharpnessLevel) {
                            interiorLimit = 9 - sharpnessLevel;
                        }
                    }
                    if (interiorLimit == 0) {
                        interiorLimit = 1;
                    }
                    int varianceThreshold = 0;
                    if (!keyFrame) {
                        throw new NotImplementedException("TODO: non-key frames are not supported yet");
                    }
                    if (loopFilterLevel >= 40) {
                        varianceThreshold = 2;
                    } else if (loopFilterLevel >= 15) {
                        varianceThreshold = 1;
                    }
                    int edgeLimitMb = (loopFilterLevel + 2) * 2 + interiorLimit;
                    int edgeLimitSb = loopFilterLevel * 2 + interiorLimit;
                    if (x > 0) {
                        VPXMacroblock lmb = mbs[y + 1][x - 1 + 1];
                        for (int b = 0; b < 4; b++) {
                            VPXMacroblock.Subblock rsb = rmb.ySubblocks[b][0];
                            VPXMacroblock.Subblock lsb = lmb.ySubblocks[b][3];
                            for (int a = 0; a < 4; a++) {
                                FilterUtil.Segment seg = FilterUtil.Segment.horizontal(rsb, lsb, a);
                                seg.filterMb(varianceThreshold, interiorLimit, edgeLimitMb);
                                seg.applyHorizontally(rsb, lsb, a);
                            }
                        }
                    }
                    if (!rmb.skipFilter) {
                        for (int a = 1; a < 4; a++) {
                            for (int b = 0; b < 4; b++) {
                                VPXMacroblock.Subblock lsb = rmb.ySubblocks[b][a - 1];
                                VPXMacroblock.Subblock rsb = rmb.ySubblocks[b][a];
                                for (int c = 0; c < 4; c++) {
                                    FilterUtil.Segment seg = FilterUtil.Segment.horizontal(rsb, lsb, c);
                                    seg.filterSb(varianceThreshold, interiorLimit, edgeLimitSb);
                                    seg.applyHorizontally(rsb, lsb, c);
                                }
                            }
                        }
                    }
                    if (y > 0) {
                        VPXMacroblock tmb = mbs[y - 1 + 1][x + 1];
                        for (int b = 0; b < 4; b++) {
                            VPXMacroblock.Subblock tsb = tmb.ySubblocks[3][b];
                            VPXMacroblock.Subblock bsb = bmb.ySubblocks[0][b];
                            for (int a = 0; a < 4; a++) {
                                FilterUtil.Segment seg = FilterUtil.Segment.vertical(bsb, tsb, a);
                                seg.filterMb(varianceThreshold, interiorLimit, edgeLimitMb);
                                seg.applyVertically(bsb, tsb, a);
                            }
                        }
                    }
                    if (!rmb.skipFilter) {
                        for (int a = 1; a < 4; a++) {
                            for (int b = 0; b < 4; b++) {
                                VPXMacroblock.Subblock tsb = bmb.ySubblocks[a - 1][b];
                                VPXMacroblock.Subblock bsb = bmb.ySubblocks[a][b];
                                for (int c = 0; c < 4; c++) {
                                    FilterUtil.Segment seg = FilterUtil.Segment.vertical(bsb, tsb, c);
                                    seg.filterSb(varianceThreshold, interiorLimit, edgeLimitSb);
                                    seg.applyVertically(bsb, tsb, c);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static class Segment {

        int p0;

        int p1;

        int p2;

        int p3;

        int q0;

        int q1;

        int q2;

        int q3;

        public boolean isFilterRequired(int interior, int edge) {
            return (Math.abs(this.p0 - this.q0) << 2) + (Math.abs(this.p1 - this.q1) >> 2) <= edge && Math.abs(this.p3 - this.p2) <= interior && Math.abs(this.p2 - this.p1) <= interior && Math.abs(this.p1 - this.p0) <= interior && Math.abs(this.q3 - this.q2) <= interior && Math.abs(this.q2 - this.q1) <= interior && Math.abs(this.q1 - this.q0) <= interior;
        }

        public boolean isHighVariance(int threshold) {
            return Math.abs(this.p1 - this.p0) > threshold || Math.abs(this.q1 - this.q0) > threshold;
        }

        public FilterUtil.Segment getSigned() {
            FilterUtil.Segment seg = new FilterUtil.Segment();
            seg.p3 = FilterUtil.minus128(this.p3);
            seg.p2 = FilterUtil.minus128(this.p2);
            seg.p1 = FilterUtil.minus128(this.p1);
            seg.p0 = FilterUtil.minus128(this.p0);
            seg.q0 = FilterUtil.minus128(this.q0);
            seg.q1 = FilterUtil.minus128(this.q1);
            seg.q2 = FilterUtil.minus128(this.q2);
            seg.q3 = FilterUtil.minus128(this.q3);
            return seg;
        }

        public static FilterUtil.Segment horizontal(VPXMacroblock.Subblock right, VPXMacroblock.Subblock left, int a) {
            FilterUtil.Segment seg = new FilterUtil.Segment();
            seg.p0 = left.val[12 + a];
            seg.p1 = left.val[8 + a];
            seg.p2 = left.val[4 + a];
            seg.p3 = left.val[0 + a];
            seg.q0 = right.val[0 + a];
            seg.q1 = right.val[4 + a];
            seg.q2 = right.val[8 + a];
            seg.q3 = right.val[12 + a];
            return seg;
        }

        public static FilterUtil.Segment vertical(VPXMacroblock.Subblock lower, VPXMacroblock.Subblock upper, int a) {
            FilterUtil.Segment seg = new FilterUtil.Segment();
            seg.p0 = upper.val[a * 4 + 3];
            seg.p1 = upper.val[a * 4 + 2];
            seg.p2 = upper.val[a * 4 + 1];
            seg.p3 = upper.val[a * 4 + 0];
            seg.q0 = lower.val[a * 4 + 0];
            seg.q1 = lower.val[a * 4 + 1];
            seg.q2 = lower.val[a * 4 + 2];
            seg.q3 = lower.val[a * 4 + 3];
            return seg;
        }

        public void applyHorizontally(VPXMacroblock.Subblock right, VPXMacroblock.Subblock left, int a) {
            left.val[12 + a] = this.p0;
            left.val[8 + a] = this.p1;
            left.val[4 + a] = this.p2;
            left.val[0 + a] = this.p3;
            right.val[0 + a] = this.q0;
            right.val[4 + a] = this.q1;
            right.val[8 + a] = this.q2;
            right.val[12 + a] = this.q3;
        }

        public void applyVertically(VPXMacroblock.Subblock lower, VPXMacroblock.Subblock upper, int a) {
            upper.val[a * 4 + 3] = this.p0;
            upper.val[a * 4 + 2] = this.p1;
            upper.val[a * 4 + 1] = this.p2;
            upper.val[a * 4 + 0] = this.p3;
            lower.val[a * 4 + 0] = this.q0;
            lower.val[a * 4 + 1] = this.q1;
            lower.val[a * 4 + 2] = this.q2;
            lower.val[a * 4 + 3] = this.q3;
        }

        void filterMb(int hevThreshold, int interiorLimit, int edgeLimit) {
            FilterUtil.Segment signedSeg = this.getSigned();
            if (signedSeg.isFilterRequired(interiorLimit, edgeLimit)) {
                if (!signedSeg.isHighVariance(hevThreshold)) {
                    int w = FilterUtil.clipSigned(FilterUtil.clipSigned(signedSeg.p1 - signedSeg.q1) + 3 * (signedSeg.q0 - signedSeg.p0));
                    int a = 27 * w + 63 >> 7;
                    this.q0 = FilterUtil.clipPlus128(signedSeg.q0 - a);
                    this.p0 = FilterUtil.clipPlus128(signedSeg.p0 + a);
                    a = 18 * w + 63 >> 7;
                    this.q1 = FilterUtil.clipPlus128(signedSeg.q1 - a);
                    this.p1 = FilterUtil.clipPlus128(signedSeg.p1 + a);
                    a = 9 * w + 63 >> 7;
                    this.q2 = FilterUtil.clipPlus128(signedSeg.q2 - a);
                    this.p2 = FilterUtil.clipPlus128(signedSeg.p2 + a);
                } else {
                    this.adjust(true);
                }
            }
        }

        public void filterSb(int hev_threshold, int interior_limit, int edge_limit) {
            FilterUtil.Segment signedSeg = this.getSigned();
            if (signedSeg.isFilterRequired(interior_limit, edge_limit)) {
                boolean hv = signedSeg.isHighVariance(hev_threshold);
                int a = this.adjust(hv) + 1 >> 1;
                if (!hv) {
                    this.q1 = FilterUtil.clipPlus128(signedSeg.q1 - a);
                    this.p1 = FilterUtil.clipPlus128(signedSeg.p1 + a);
                }
            }
        }

        private int adjust(boolean use_outer_taps) {
            int p1 = FilterUtil.minus128(this.p1);
            int p0 = FilterUtil.minus128(this.p0);
            int q0 = FilterUtil.minus128(this.q0);
            int q1 = FilterUtil.minus128(this.q1);
            int a = FilterUtil.clipSigned((use_outer_taps ? FilterUtil.clipSigned(p1 - q1) : 0) + 3 * (q0 - p0));
            int b = FilterUtil.clipSigned(a + 3) >> 3;
            a = FilterUtil.clipSigned(a + 4) >> 3;
            this.q0 = FilterUtil.clipPlus128(q0 - a);
            this.p0 = FilterUtil.clipPlus128(p0 + b);
            return a;
        }
    }
}