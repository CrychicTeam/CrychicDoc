package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.vp9;

import com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari.Packed4BitList;
import com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.VPXBooleanDecoder;

public class InterModeInfo extends ModeInfo {

    private long mvl0;

    private long mvl1;

    private long mvl2;

    private long mvl3;

    InterModeInfo() {
    }

    public InterModeInfo(int segmentId, boolean skip, int txSize, int yMode, int subModes, int uvMode) {
        super(segmentId, skip, txSize, yMode, subModes, uvMode);
    }

    public InterModeInfo(int segmentId, boolean skip, int txSize, int yMode, int subModes, int uvMode, long mvl0, long mvl1, long mvl2, long mvl3) {
        super(segmentId, skip, txSize, yMode, subModes, uvMode);
        this.mvl0 = mvl0;
        this.mvl1 = mvl1;
        this.mvl2 = mvl2;
        this.mvl3 = mvl3;
    }

    @Override
    public boolean isInter() {
        return true;
    }

    public long getMvl0() {
        return this.mvl0;
    }

    public long getMvl1() {
        return this.mvl1;
    }

    public long getMvl2() {
        return this.mvl2;
    }

    public long getMvl3() {
        return this.mvl3;
    }

    public InterModeInfo read(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        int segmentId = 0;
        if (c.isSegmentationEnabled()) {
            segmentId = predicSegmentId(miCol, miRow, blSz, c);
            if (c.isUpdateSegmentMap() && (!c.isSegmentMapConditionalUpdate() || !readSegIdPredicted(miCol, miRow, blSz, decoder, c))) {
                segmentId = readSegmentId(decoder, c);
            }
        }
        boolean skip = true;
        if (!c.isSegmentFeatureActive(segmentId, 3)) {
            skip = this.readSkipFlag(miCol, miRow, blSz, decoder, c);
        }
        boolean isInter = c.getSegmentFeature(segmentId, 2) != 0;
        if (!c.isSegmentFeatureActive(segmentId, 2)) {
            isInter = this.readIsInter(miCol, miRow, blSz, decoder, c);
        }
        int txSize = this.readTxSize(miCol, miRow, blSz, !skip || !isInter, decoder, c);
        return !isInter ? this.readInterIntraMode(miCol, miRow, blSz, decoder, c, segmentId, skip, txSize) : this.readInterInterMode(miCol, miRow, blSz, decoder, c, segmentId, skip, txSize);
    }

    private InterModeInfo readInterInterMode(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c, int segmentId, boolean skip, int txSize) {
        int packedRefFrames = this.readRefFrames(miCol, miRow, blSz, segmentId, decoder, c);
        int lumaMode = 12;
        if (!c.isSegmentFeatureActive(segmentId, 3) && blSz >= 3) {
            lumaMode = this.readInterMode(miCol, miRow, blSz, decoder, c);
        }
        int interpFilter = c.getInterpFilter();
        if (interpFilter == 3) {
            interpFilter = this.readInterpFilter(miCol, miRow, blSz, decoder, c);
        }
        if (blSz < 3) {
            if (blSz == 0) {
                long[] mv4x4 = this.readMV4x4(miCol, miRow, blSz, decoder, c, packedRefFrames);
                return new InterModeInfo(segmentId, skip, txSize, -1, 0, -1, mv4x4[0], mv4x4[1], mv4x4[2], mv4x4[3]);
            } else {
                long[] mv12 = this.readMvSub8x8(miCol, miRow, blSz, decoder, c, packedRefFrames);
                return new InterModeInfo(segmentId, skip, txSize, 0, 0, 0, mv12[0], mv12[1], 0L, 0L);
            }
        } else {
            long mvl = this.readMV8x8AndAbove(miCol, miRow, blSz, decoder, c, packedRefFrames, lumaMode);
            return new InterModeInfo(segmentId, skip, txSize, lumaMode, 0, lumaMode, mvl, 0L, 0L, 0L);
        }
    }

    protected long readMV8x8AndAbove(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c, int packedRefFrames, int lumaMode) {
        long mvl = readSub0(miCol, miRow, blSz, decoder, c, lumaMode, packedRefFrames);
        updateMVLineBuffers(miCol, miRow, blSz, c, mvl);
        updateMVLineBuffers4x4(miCol, miRow, blSz, c, mvl, mvl);
        return mvl;
    }

    protected long[] readMvSub8x8(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c, int packedRefFrames) {
        int subMode0 = this.readInterMode(miCol, miRow, blSz, decoder, c);
        long mvl0 = readSub0(miCol, miRow, blSz, decoder, c, subMode0, packedRefFrames);
        int subMode1 = this.readInterMode(miCol, miRow, blSz, decoder, c);
        int blk = blSz == 1 ? 1 : 2;
        long mvl1 = readSub12(miCol, miRow, blSz, decoder, c, mvl0, subMode1, blk, packedRefFrames);
        if (blSz == 1) {
            updateMVLineBuffers4x4(miCol, miRow, blSz, c, mvl1, mvl0);
        } else {
            updateMVLineBuffers4x4(miCol, miRow, blSz, c, mvl0, mvl1);
        }
        updateMVLineBuffers(miCol, miRow, blSz, c, mvl1);
        return new long[] { mvl0, mvl1 };
    }

    protected long[] readMV4x4(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c, int packedRefFrames) {
        int subMode0 = this.readInterMode(miCol, miRow, blSz, decoder, c);
        long mvl0 = readSub0(miCol, miRow, blSz, decoder, c, subMode0, packedRefFrames);
        int subMode1 = this.readInterMode(miCol, miRow, blSz, decoder, c);
        long mvl1 = readSub12(miCol, miRow, blSz, decoder, c, mvl0, subMode1, 1, packedRefFrames);
        int subMode2 = this.readInterMode(miCol, miRow, blSz, decoder, c);
        long mvl2 = readSub12(miCol, miRow, blSz, decoder, c, mvl0, subMode2, 2, packedRefFrames);
        int subMode3 = this.readInterMode(miCol, miRow, blSz, decoder, c);
        long mvl3 = readMvSub3(miCol, miRow, blSz, decoder, c, mvl0, mvl1, mvl2, subMode3, packedRefFrames);
        updateMVLineBuffers(miCol, miRow, blSz, c, mvl3);
        updateMVLineBuffers4x4(miCol, miRow, blSz, c, mvl1, mvl2);
        return new long[] { mvl0, mvl1, mvl2, mvl3 };
    }

    private static long readSub0(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c, int lumaMode, int packedRefFrames) {
        int ref0 = Packed4BitList.get(packedRefFrames, 0);
        int ref1 = Packed4BitList.get(packedRefFrames, 1);
        boolean compoundPred = Packed4BitList.get(packedRefFrames, 2) == 1;
        long nearestNearMv00 = findBestMv(miCol, miRow, blSz, ref0, 0, c, true);
        long nearestNearMv01 = 0L;
        if (compoundPred) {
            nearestNearMv01 = findBestMv(miCol, miRow, blSz, ref1, 0, c, true);
        }
        int mv0 = 0;
        int mv1 = 0;
        if (lumaMode == 13) {
            mv0 = readDiffMv(decoder, c, nearestNearMv00);
            if (compoundPred) {
                mv1 = readDiffMv(decoder, c, nearestNearMv01);
            }
        } else if (lumaMode != 12) {
            mv0 = MVList.get(nearestNearMv00, lumaMode - 10);
            mv1 = MVList.get(nearestNearMv01, lumaMode - 10);
        }
        return MVList.create(mv0, mv1);
    }

    private static long readSub12(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c, long mvl0, int subMode1, int blk, int packedRefFrames) {
        int ref0 = Packed4BitList.get(packedRefFrames, 0);
        int ref1 = Packed4BitList.get(packedRefFrames, 1);
        boolean compoundPred = Packed4BitList.get(packedRefFrames, 2) == 1;
        int mv10 = 0;
        int mv11 = 0;
        long nearestNearMv00 = findBestMv(miCol, miRow, blSz, ref0, 0, c, true);
        long nearestNearMv01 = 0L;
        if (compoundPred) {
            nearestNearMv01 = findBestMv(miCol, miRow, blSz, ref1, 0, c, true);
        }
        if (subMode1 == 13) {
            mv10 = readDiffMv(decoder, c, nearestNearMv00);
            if (compoundPred) {
                mv11 = readDiffMv(decoder, c, nearestNearMv01);
            }
        } else if (subMode1 != 12) {
            long nearestNearMv10 = prepandSubMvBlk12(findBestMv(miCol, miRow, blSz, ref0, blk, c, false), MVList.get(mvl0, 0));
            long nearestNearMv11 = 0L;
            if (compoundPred) {
                nearestNearMv11 = prepandSubMvBlk12(findBestMv(miCol, miRow, blSz, ref1, blk, c, false), MVList.get(mvl0, 1));
            }
            mv10 = MVList.get(nearestNearMv10, subMode1 - 10);
            mv11 = MVList.get(nearestNearMv11, subMode1 - 10);
        }
        return MVList.create(mv10, mv11);
    }

    private static long readMvSub3(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c, long mvl0, long mvl1, long mvl2, int subMode3, int packedRefFrames) {
        int ref0 = Packed4BitList.get(packedRefFrames, 0);
        int ref1 = Packed4BitList.get(packedRefFrames, 1);
        boolean compoundPred = Packed4BitList.get(packedRefFrames, 2) == 1;
        long nearestNearMv00 = findBestMv(miCol, miRow, blSz, ref0, 0, c, true);
        long nearestNearMv01 = 0L;
        if (compoundPred) {
            nearestNearMv01 = findBestMv(miCol, miRow, blSz, ref1, 0, c, true);
        }
        int mv30 = 0;
        int mv31 = 0;
        if (subMode3 == 13) {
            mv30 = readDiffMv(decoder, c, nearestNearMv00);
            if (compoundPred) {
                mv31 = readDiffMv(decoder, c, nearestNearMv01);
            }
        } else if (subMode3 != 12) {
            long nearestNearMv30 = prepandSubMvBlk3(findBestMv(miCol, miRow, blSz, ref0, 3, c, false), MVList.get(mvl0, 0), MVList.get(mvl1, 0), MVList.get(mvl2, 0));
            long nearestNearMv31 = 0L;
            if (compoundPred) {
                nearestNearMv31 = prepandSubMvBlk3(findBestMv(miCol, miRow, blSz, ref1, 3, c, false), MVList.get(mvl0, 1), MVList.get(mvl1, 1), MVList.get(mvl2, 1));
            }
            mv30 = MVList.get(nearestNearMv30, subMode3 - 10);
            mv31 = MVList.get(nearestNearMv31, subMode3 - 10);
        }
        return MVList.create(mv30, mv31);
    }

    private int readRefFrames(int miCol, int miRow, int blSz, int segmentId, VPXBooleanDecoder decoder, DecodingContext c) {
        int ref0 = c.getSegmentFeature(segmentId, 2);
        int ref1 = 0;
        boolean compoundPred = false;
        if (!c.isSegmentFeatureActive(segmentId, 2)) {
            int refMode = c.getRefMode();
            compoundPred = refMode == 1;
            if (refMode == 2) {
                compoundPred = this.readRefMode(miCol, miRow, decoder, c);
            }
            if (compoundPred) {
                int compRef = this.readCompRef(miCol, miRow, blSz, decoder, c);
                int fixedRef = c.getCompFixedRef();
                if (c.refFrameSignBias(fixedRef) == 0) {
                    ref0 = fixedRef;
                    ref1 = compRef;
                } else {
                    ref0 = compRef;
                    ref1 = fixedRef;
                }
            } else {
                ref0 = this.readSingleRef(miCol, miRow, decoder, c);
            }
        }
        updateRefFrameLineBuffers(miCol, miRow, blSz, c, ref0, ref1, compoundPred);
        return Packed4BitList._3(ref0, ref1, compoundPred ? 1 : 0);
    }

    private static void updateMVLineBuffers(int miCol, int miRow, int blSz, DecodingContext c, long mv) {
        long[][] leftMVs = c.getLeftMVs();
        long[][] aboveMVs = c.getAboveMVs();
        long[][] aboveLeftMVs = c.getAboveLeftMVs();
        for (int i = 0; i < Math.max(3, Consts.blW[blSz]); i++) {
            aboveLeftMVs[2][i] = aboveLeftMVs[1][i];
            aboveLeftMVs[1][i] = aboveLeftMVs[0][i];
            aboveLeftMVs[0][i] = aboveMVs[i][miCol + i];
        }
        for (int i = 0; i < Math.max(3, Consts.blH[blSz]); i++) {
            int offTop = (miRow + i) % 8;
            aboveLeftMVs[i][2] = aboveLeftMVs[i][1];
            aboveLeftMVs[i][1] = aboveLeftMVs[i][0];
            aboveLeftMVs[i][0] = leftMVs[i][offTop];
        }
        for (int j = 0; j < Math.max(3, Consts.blH[blSz]); j++) {
            for (int i = 0; i < Consts.blW[blSz]; i++) {
                int offLeft = miCol + i;
                aboveMVs[2][offLeft] = aboveMVs[1][offLeft];
                aboveMVs[1][offLeft] = aboveMVs[0][offLeft];
                aboveMVs[0][offLeft] = mv;
            }
        }
        for (int j = 0; j < Math.max(3, Consts.blW[blSz]); j++) {
            for (int i = 0; i < Consts.blH[blSz]; i++) {
                int offTop = (miRow + i) % 8;
                leftMVs[2][offTop] = leftMVs[1][offTop];
                leftMVs[1][offTop] = leftMVs[0][offTop];
                leftMVs[0][offTop] = mv;
            }
        }
    }

    private static void updateMVLineBuffers4x4(int miCol, int miRow, int blSz, DecodingContext c, long mvLeft, long mvAbove) {
        long[] leftMVs = c.getLeft4x4MVs();
        long[] aboveMVs = c.getAbove4x4MVs();
        aboveMVs[miCol] = mvAbove;
        leftMVs[miRow % 8] = mvLeft;
    }

    public static int ref(int ref0, int ref1) {
        return (ref0 & 3) << 2 | ref1 & 3;
    }

    public static int getRef(int packed, int n) {
        return n == 0 ? packed & 3 : packed >> 2 & 3;
    }

    private static void updateRefFrameLineBuffers(int miCol, int miRow, int blSz, DecodingContext c, int ref0, int ref1, boolean compoundPred) {
        boolean[] aboveCompound = c.getAboveCompound();
        boolean[] leftCompound = c.getLeftCompound();
        for (int i = 0; i < Consts.blW[blSz]; i++) {
            aboveCompound[i + miCol] = compoundPred;
        }
        for (int i = 0; i < Consts.blH[blSz]; i++) {
            leftCompound[miRow + i & 7] = compoundPred;
        }
        for (int j = 0; j < Consts.blW[blSz]; j++) {
            c.getAboveRefs()[j] = ref(ref0, ref1);
        }
        for (int i = 0; i < Consts.blH[blSz]; i++) {
            c.getLeftRefs()[i & 7] = ref(ref0, ref1);
        }
    }

    private static int readDiffMv(VPXBooleanDecoder decoder, DecodingContext c, long nearNearest) {
        int bestMv = MVList.get(nearNearest, 0);
        boolean useHp = c.isAllowHpMv() && !largeMv(bestMv);
        int joint = decoder.readTree(Consts.TREE_MV_JOINT, c.getMvJointProbs());
        int diffMv0 = 0;
        int diffMv1 = 0;
        if (joint == 2 || joint == 3) {
            diffMv0 = readMvComponent(decoder, c, 0, useHp);
        }
        if (joint == 1 || joint == 3) {
            diffMv1 = readMvComponent(decoder, c, 1, useHp);
        }
        return MV.create(MV.x(bestMv) + diffMv0, MV.y(bestMv) + diffMv1, MV.ref(bestMv));
    }

    private static int readMvComponent(VPXBooleanDecoder decoder, DecodingContext c, int comp, boolean useHp) {
        boolean sign = decoder.readBitEq() == 1;
        int mvClass = decoder.readTree(Consts.MV_CLASS_TREE, c.getMvClassProbs()[comp]);
        int mag;
        if (mvClass == 0) {
            int mvClass0Bit = decoder.readBit(c.getMvClass0BitProbs()[comp]);
            int mvClass0Fr = decoder.readTree(Consts.MV_FR_TREE, c.getMvClass0FrProbs()[comp][mvClass0Bit]);
            int mvClass0Hp = useHp ? decoder.readBit(c.getMvClass0HpProbs()[comp]) : 1;
            mag = (mvClass0Bit << 3 | mvClass0Fr << 1 | mvClass0Hp) + 1;
        } else {
            int d = 0;
            for (int i = 0; i < mvClass; i++) {
                int mvBit = decoder.readBit(c.getMvBitsProb()[comp][i]);
                d |= mvBit << i;
            }
            mag = 2 << mvClass + 2;
            int mvFr = decoder.readTree(Consts.MV_FR_TREE, c.getMvFrProbs()[comp]);
            int mvHp = useHp ? decoder.readBit(c.getMvHpProbs()[comp]) : 1;
            mag += (d << 3 | mvFr << 1 | mvHp) + 1;
        }
        return sign ? -mag : mag;
    }

    private static boolean largeMv(int mv) {
        return (MV.x(mv) >= 64 || MV.x(mv) <= -64) && (MV.y(mv) >= 64 || MV.y(mv) <= -64);
    }

    public static long findBestMv(int miCol, int miRow, int blSz, int ref, int blk, DecodingContext c, boolean clearHp) {
        long[][] leftMVs = c.getLeftMVs();
        long[][] aboveMVs = c.getAboveMVs();
        long[][] aboveLeftMVs = c.getAboveLeftMVs();
        long[] left4x4MVs = c.getLeft4x4MVs();
        long[] above4x4MVs = c.getAbove4x4MVs();
        long list = 0L;
        boolean checkDifferentRef = false;
        int pt0 = Consts.mv_ref_blocks[blSz][0];
        int pt1 = Consts.mv_ref_blocks[blSz][1];
        long mvp0 = getMV(leftMVs, aboveMVs, aboveLeftMVs, pt0, miRow, miCol, c);
        long mvp1 = getMV(leftMVs, aboveMVs, aboveLeftMVs, pt1, miRow, miCol, c);
        if (blk == 1) {
            mvp0 = mvp0 == -1L ? -1L : left4x4MVs[miRow % 8];
        } else if (blk == 2) {
            mvp1 = mvp1 == -1L ? -1L : above4x4MVs[miCol];
        }
        checkDifferentRef = mvp0 != 0L | mvp1 != 0L;
        list = processCandidate(ref, list, mvp0);
        list = processCandidate(ref, list, mvp1);
        for (int i = 2; i < Consts.mv_ref_blocks[blSz].length && MVList.size(list) < 2; i++) {
            long mvi = getMV(leftMVs, aboveMVs, aboveLeftMVs, Consts.mv_ref_blocks[blSz][i], miRow, miCol, c);
            checkDifferentRef |= mvi != 0L;
            list = processCandidate(ref, list, mvi);
        }
        if (MVList.size(list) < 2 && c.isUsePrevFrameMvs()) {
            long[][] prevFrameMv = c.getPrevFrameMv();
            long prevMv = prevFrameMv[miCol][miRow];
            list = processCandidate(ref, list, prevMv);
        }
        if (MVList.size(list) < 2 && checkDifferentRef) {
            for (int i = 0; i < Consts.mv_ref_blocks[blSz].length && MVList.size(list) < 2; i++) {
                long mvp = getMV(leftMVs, aboveMVs, aboveLeftMVs, Consts.mv_ref_blocks[blSz][i], miRow, miCol, c);
                list = processNECandidate(ref, c, list, mvp);
            }
        }
        if (MVList.size(list) < 2 && c.isUsePrevFrameMvs()) {
            long[][] prevFrameMv = c.getPrevFrameMv();
            long prevMv = prevFrameMv[miCol][miRow];
            list = processNECandidate(ref, c, list, prevMv);
        }
        list = clampMvs(miCol, miRow, blSz, c, list);
        if (clearHp) {
            list = clearHp(c, list);
        }
        return list;
    }

    private static long clearHp(DecodingContext c, long list) {
        int mv0 = MVList.get(list, 0);
        if (!c.isAllowHpMv() || largeMv(mv0)) {
            mv0 = MV.create(MV.x(mv0) & -2, MV.y(mv0) & -2, MV.ref(mv0));
        }
        int mv1 = MVList.get(list, 1);
        if (!c.isAllowHpMv() || largeMv(mv1)) {
            mv1 = MV.create(MV.x(mv1) & -2, MV.y(mv1) & -2, MV.ref(mv1));
        }
        return MVList.create(mv0, mv1);
    }

    private static long clampMvs(int miCol, int miRow, int blSz, DecodingContext c, long list) {
        int mv0 = MVList.get(list, 0);
        int mv1 = MVList.get(list, 1);
        int mv0xCl = clampMvCol(miCol, blSz, c, MV.x(mv0));
        int mv0yCl = clampMvRow(miRow, blSz, c, MV.y(mv0));
        int mv1xCl = clampMvCol(miCol, blSz, c, MV.x(mv1));
        int mv1yCl = clampMvRow(miRow, blSz, c, MV.y(mv1));
        return MVList.create(MV.create(mv0xCl, mv0yCl, MV.ref(mv0)), MV.create(mv1xCl, mv1yCl, MV.ref(mv1)));
    }

    private static long processNECandidate(int ref, DecodingContext c, long list, long mvp) {
        int mv0 = MVList.get(mvp, 0);
        int mv1 = MVList.get(mvp, 1);
        boolean matchMv = MV.x(mv0) == MV.x(mv1) && MV.y(mv0) == MV.y(mv1);
        list = processNEComponent(ref, c, list, mv0);
        if (!matchMv) {
            list = processNEComponent(ref, c, list, mv1);
        }
        return list;
    }

    private static long processNEComponent(int ref, DecodingContext c, long list, int mv0) {
        int ref0 = MV.ref(mv0);
        if (ref0 != 0 && ref0 != ref) {
            int q = c.refFrameSignBias(ref0) * c.refFrameSignBias(ref);
            int mv = MV.create(MV.x(mv0) * q, MV.y(mv0), ref);
            list = MVList.addUniq(list, mv);
        }
        return list;
    }

    private static long processCandidate(int ref, long list, long mvp0) {
        int mv00 = MVList.get(mvp0, 0);
        int mv01 = MVList.get(mvp0, 1);
        if (MV.ref(mv00) == ref) {
            list = MVList.addUniq(list, mv00);
        } else if (MV.ref(mv01) == ref) {
            list = MVList.addUniq(list, mv01);
        }
        return list;
    }

    private static long prepandSubMvBlk12(long list, int blkMv) {
        long nlist = 0L;
        nlist = MVList.add(nlist, blkMv);
        nlist = MVList.addUniq(nlist, MVList.get(list, 0));
        return MVList.addUniq(nlist, MVList.get(list, 0));
    }

    private static long prepandSubMvBlk3(long list, int blk0Mv, int blk1Mv, int blk2Mv) {
        long nlist = 0L;
        nlist = MVList.add(nlist, blk2Mv);
        nlist = MVList.addUniq(nlist, blk1Mv);
        nlist = MVList.addUniq(nlist, blk0Mv);
        nlist = MVList.addUniq(nlist, MVList.get(list, 0));
        return MVList.addUniq(nlist, MVList.get(list, 0));
    }

    private static int clampMvRow(int miRow, int blSz, DecodingContext c, int mv) {
        int mbToTopEdge = -(miRow << 6);
        int mbToBottomEdge = c.getMiFrameHeight() - Consts.blH[blSz] - miRow << 6;
        return clip3(mbToTopEdge - 128, mbToBottomEdge + 128, mv);
    }

    private static int clip3(int from, int to, int v) {
        return v < from ? from : (v > to ? to : v);
    }

    private static int clampMvCol(int miCol, int blSz, DecodingContext c, int mv) {
        int mbToLeftEdge = -(miCol << 6);
        int mbToRightEdge = c.getMiFrameWidth() - Consts.blW[blSz] - miCol << 6;
        return clip3(mbToLeftEdge - 128, mbToRightEdge + 128, mv);
    }

    protected int readInterpFilter(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        boolean availAbove = miRow > 0;
        boolean availLeft = miCol > c.getMiTileStartCol();
        int[] aboveRefs = c.getAboveRefs();
        int[] leftRefs = c.getLeftRefs();
        int aboveRefFrame0 = getRef(aboveRefs[miCol], 0);
        int leftRefFrame0 = getRef(leftRefs[miRow & 7], 0);
        int[] leftInterpFilters = c.getLeftInterpFilters();
        int[] aboveInterpFilters = c.getAboveInterpFilters();
        int leftInterp = availLeft && leftRefFrame0 > 0 ? leftInterpFilters[miRow & 7] : 3;
        int aboveInterp = availAbove && aboveRefFrame0 > 0 ? aboveInterpFilters[miCol] : 3;
        int ctx;
        if (leftInterp == aboveInterp) {
            ctx = leftInterp;
        } else if (leftInterp == 3 && aboveInterp != 3) {
            ctx = aboveInterp;
        } else if (leftInterp != 3 && aboveInterp == 3) {
            ctx = leftInterp;
        } else {
            ctx = 3;
        }
        int[][] probs = c.getInterpFilterProbs();
        int ret = decoder.readTree(Consts.TREE_INTERP_FILTER, probs[ctx]);
        for (int i = 0; i < Consts.blW[blSz]; i++) {
            aboveInterpFilters[miCol + i] = ret;
        }
        for (int i = 0; i < Consts.blH[blSz]; i++) {
            leftInterpFilters[miRow + i & 7] = ret;
        }
        return ret;
    }

    public int readInterMode(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        int ind0 = Consts.mv_ref_blocks_sm[blSz][0];
        int ind1 = Consts.mv_ref_blocks_sm[blSz][1];
        int[] leftModes = c.getLeftModes();
        int[] aboveModes = c.getAboveModes();
        int mode0 = getMode(leftModes, aboveModes, ind0, miRow, miCol, c);
        int mode1 = getMode(leftModes, aboveModes, ind1, miRow, miCol, c);
        int ctx;
        if (mode0 != 11 && mode0 != 10) {
            if (mode0 == 12) {
                if (mode1 == 11 || mode1 == 10) {
                    ctx = 1;
                } else if (mode1 == 13) {
                    ctx = 3;
                } else if (mode1 == 12) {
                    ctx = 0;
                } else {
                    ctx = 5;
                }
            } else if (mode0 == 13) {
                if (mode1 == 11 || mode1 == 10) {
                    ctx = 3;
                } else if (mode1 == 13) {
                    ctx = 4;
                } else if (mode1 == 12) {
                    ctx = 3;
                } else {
                    ctx = 3;
                }
            } else {
                ctx = mode1 >= 10 ? 5 : 6;
            }
        } else if (mode1 == 11 || mode1 == 10) {
            ctx = 2;
        } else if (mode1 == 13) {
            ctx = 3;
        } else if (mode1 == 12) {
            ctx = 1;
        } else {
            ctx = 5;
        }
        System.out.println(String.format("inter_mode_ctx: %d\n", ctx));
        int[][] probs = c.getInterModeProbs();
        int ret = 10 + decoder.readTree(Consts.TREE_INTER_MODE, probs[ctx]);
        for (int i = 0; i < Consts.blW[blSz]; i++) {
            aboveModes[miCol + i] = ret;
        }
        for (int i = 0; i < Consts.blH[blSz]; i++) {
            leftModes[(miRow + i) % 8] = ret;
        }
        return ret;
    }

    private static int getMode(int[] leftModes, int[] aboveModes, int ind0, int miRow, int miCol, DecodingContext c) {
        switch(ind0) {
            case 0:
                return miCol > c.getMiTileStartCol() ? leftModes[miRow % 8] : 10;
            case 1:
                return miRow > 0 ? aboveModes[miCol] : 10;
            case 2:
                return miCol > c.getMiTileStartCol() && miRow < c.getMiFrameHeight() - 1 ? leftModes[miRow % 8 + 1] : 10;
            case 3:
                return miCol < c.getMiTileWidth() - 1 && miRow > 0 ? aboveModes[miCol + 1] : 10;
            case 4:
                return miCol > c.getMiTileStartCol() && miRow < c.getMiFrameHeight() - 3 ? leftModes[miRow % 8 + 3] : 10;
            case 5:
                return miCol < c.getMiTileWidth() - 3 && miRow > 0 ? aboveModes[miCol + 3] : 10;
            default:
                return 10;
        }
    }

    private static long getMV(long[][] leftMV, long[][] aboveMV, long[][] aboveLeftMV, int ind0, int miRow, int miCol, DecodingContext c) {
        int th = c.getMiTileHeight();
        int tw = c.getMiTileWidth();
        switch(ind0) {
            case 0:
                return miCol >= c.getMiTileStartCol() ? leftMV[0][miRow % 8] : 0L;
            case 1:
                return miRow > 0 ? aboveMV[0][miCol] : 0L;
            case 2:
                return miCol >= c.getMiTileStartCol() && miRow < th - 1 ? leftMV[0][miRow % 8 + 1] : 0L;
            case 3:
                return miRow > 0 && miCol < tw - 1 ? aboveMV[0][miCol + 1] : 0L;
            case 4:
                return miCol >= c.getMiTileStartCol() && miRow < th - 3 ? leftMV[0][miRow % 8 + 3] : 0L;
            case 5:
                return miRow > 0 && miCol < tw - 3 ? aboveMV[0][miCol + 3] : 0L;
            case 6:
                return miCol >= c.getMiTileStartCol() && miRow < th - 2 ? leftMV[0][miRow % 8 + 2] : 0L;
            case 7:
                return miRow > 0 && miCol < tw - 2 ? aboveMV[0][miCol + 2] : 0L;
            case 8:
                return miCol >= c.getMiTileStartCol() && miRow < th - 4 ? leftMV[0][miRow % 8 + 4] : 0L;
            case 9:
                return miRow > 0 && miCol < tw - 4 ? aboveMV[0][miCol + 4] : 0L;
            case 10:
                return miCol >= c.getMiTileStartCol() && miRow < th - 6 ? leftMV[0][miRow % 8 + 6] : 0L;
            case 11:
                return miCol >= c.getMiTileStartCol() && miRow > 0 ? aboveLeftMV[0][0] : 0L;
            case 12:
                return miCol >= c.getMiTileStartCol() + 1 ? leftMV[1][miRow % 8] : 0L;
            case 13:
                return miRow > 1 ? aboveMV[1][miCol] : 0L;
            case 14:
                return miCol >= c.getMiTileStartCol() + 2 ? leftMV[2][miRow % 8] : 0L;
            case 15:
                return miRow > 2 ? aboveMV[2][miCol] : 0L;
            case 16:
                return miCol >= c.getMiTileStartCol() + 1 && miRow > 0 ? aboveLeftMV[0][1] : 0L;
            case 17:
                return miCol >= c.getMiTileStartCol() && miRow > 1 ? aboveLeftMV[1][0] : 0L;
            case 18:
                return miCol >= c.getMiTileStartCol() + 1 && miRow > 1 ? aboveLeftMV[1][1] : 0L;
            case 19:
                return miCol >= c.getMiTileStartCol() + 2 && miRow > 2 ? aboveLeftMV[2][2] : 0L;
            default:
                return 0L;
        }
    }

    protected int readCompRef(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        int compFixedRef = c.getCompFixedRef();
        int fixRefIdx = c.refFrameSignBias(compFixedRef);
        boolean availAbove = miRow > 0;
        boolean availLeft = miCol > c.getMiTileStartCol();
        boolean[] aboveCompound = c.getAboveCompound();
        boolean[] leftCompound = c.getLeftCompound();
        int aboveRefFrame0 = getRef(c.getAboveRefs()[miCol], 0);
        int leftRefFrame0 = getRef(c.getLeftRefs()[miRow & 7], 0);
        int aboveRefFrame1 = getRef(c.getAboveRefs()[miCol], 1);
        int leftRefFrame1 = getRef(c.getLeftRefs()[miRow & 7], 1);
        boolean aboveIntra = aboveRefFrame0 <= 0;
        boolean leftIntra = leftRefFrame0 <= 0;
        boolean aboveSingle = !aboveCompound[miCol];
        boolean leftSingle = !leftCompound[miRow % 8];
        int aboveVarRefFrame;
        int leftVarRefFrame;
        if (fixRefIdx == 0) {
            aboveVarRefFrame = aboveRefFrame1;
            leftVarRefFrame = leftRefFrame1;
        } else {
            aboveVarRefFrame = aboveRefFrame0;
            leftVarRefFrame = leftRefFrame0;
        }
        int compVarRef0 = c.getCompVarRef(0);
        int compVarRef1 = c.getCompVarRef(1);
        int ctx;
        if (availAbove && availLeft) {
            if (aboveIntra && leftIntra) {
                ctx = 2;
            } else if (leftIntra) {
                if (aboveSingle) {
                    ctx = 1 + 2 * (aboveRefFrame0 != compVarRef1 ? 1 : 0);
                } else {
                    ctx = 1 + 2 * (aboveVarRefFrame != compVarRef1 ? 1 : 0);
                }
            } else if (aboveIntra) {
                if (leftSingle) {
                    ctx = 1 + 2 * (leftRefFrame0 != compVarRef1 ? 1 : 0);
                } else {
                    ctx = 1 + 2 * (leftVarRefFrame != compVarRef1 ? 1 : 0);
                }
            } else {
                int vrfa = aboveSingle ? aboveRefFrame0 : aboveVarRefFrame;
                int vrfl = leftSingle ? leftRefFrame0 : leftVarRefFrame;
                if (vrfa == vrfl && compVarRef1 == vrfa) {
                    ctx = 0;
                } else if (leftSingle && aboveSingle) {
                    if ((vrfa != compFixedRef || vrfl != compVarRef0) && (vrfl != compFixedRef || vrfa != compVarRef0)) {
                        if (vrfa == vrfl) {
                            ctx = 3;
                        } else {
                            ctx = 1;
                        }
                    } else {
                        ctx = 4;
                    }
                } else if (leftSingle || aboveSingle) {
                    int vrfc = leftSingle ? vrfa : vrfl;
                    int rfs = aboveSingle ? vrfa : vrfl;
                    if (vrfc == compVarRef1 && rfs != compVarRef1) {
                        ctx = 1;
                    } else if (rfs == compVarRef1 && vrfc != compVarRef1) {
                        ctx = 2;
                    } else {
                        ctx = 4;
                    }
                } else if (vrfa == vrfl) {
                    ctx = 4;
                } else {
                    ctx = 2;
                }
            }
        } else if (availAbove) {
            if (aboveIntra) {
                ctx = 2;
            } else if (aboveSingle) {
                ctx = 3 * (aboveRefFrame0 != compVarRef1 ? 1 : 0);
            } else {
                ctx = 4 * (aboveVarRefFrame != compVarRef1 ? 1 : 0);
            }
        } else if (availLeft) {
            if (leftIntra) {
                ctx = 2;
            } else if (leftSingle) {
                ctx = 3 * (leftRefFrame0 != compVarRef1 ? 1 : 0);
            } else {
                ctx = 4 * (leftVarRefFrame != compVarRef1 ? 1 : 0);
            }
        } else {
            ctx = 2;
        }
        int[] probs = c.getCompRefProbs();
        return decoder.readBit(probs[ctx]);
    }

    protected int readSingleRef(int miCol, int miRow, VPXBooleanDecoder decoder, DecodingContext c) {
        boolean singleRefP1 = this.readSingRefBin(0, miCol, miRow, decoder, c);
        if (singleRefP1) {
            boolean singleRefP2 = this.readSingRefBin(2, miCol, miRow, decoder, c);
            return singleRefP2 ? 2 : 3;
        } else {
            return 1;
        }
    }

    private boolean readSingRefBin(int bin, int miCol, int miRow, VPXBooleanDecoder decoder, DecodingContext c) {
        boolean availAbove = miRow > 0;
        boolean availLeft = miCol > c.getMiTileStartCol();
        boolean[] aboveCompound = c.getAboveCompound();
        boolean[] leftCompound = c.getLeftCompound();
        int aboveRefFrame0 = getRef(c.getAboveRefs()[miCol], 0);
        int leftRefFrame0 = getRef(c.getLeftRefs()[miRow & 7], 0);
        int aboveRefFrame1 = getRef(c.getAboveRefs()[miCol], 1);
        int leftRefFrame1 = getRef(c.getLeftRefs()[miRow & 7], 1);
        boolean aboveIntra = aboveRefFrame0 <= 0;
        boolean leftIntra = leftRefFrame0 <= 0;
        boolean aboveSingle = !aboveCompound[miCol];
        boolean leftSingle = !leftCompound[miRow % 8];
        int ctx;
        if (availAbove && availLeft) {
            if (aboveIntra && leftIntra) {
                ctx = 2;
            } else if (leftIntra) {
                if (aboveSingle) {
                    if (bin == 0) {
                        ctx = 4 * (aboveRefFrame0 == 1 ? 1 : 0);
                    } else if (aboveRefFrame0 == 1) {
                        ctx = 3;
                    } else {
                        ctx = 4 * (aboveRefFrame0 == 3 ? 1 : 0);
                    }
                } else if (bin == 0) {
                    ctx = 1 + (aboveRefFrame0 != 1 && aboveRefFrame1 != 1 ? 0 : 1);
                } else {
                    ctx = 1 + 2 * (aboveRefFrame0 != 3 && aboveRefFrame1 != 3 ? 0 : 1);
                }
            } else if (aboveIntra) {
                if (leftSingle) {
                    if (bin == 0) {
                        ctx = 4 * (leftRefFrame0 == 1 ? 1 : 0);
                    } else if (leftRefFrame0 == 1) {
                        ctx = 3;
                    } else {
                        ctx = 4 * (leftRefFrame0 == 3 ? 1 : 0);
                    }
                } else if (bin == 0) {
                    ctx = 1 + (leftRefFrame0 != 1 && leftRefFrame1 != 1 ? 0 : 1);
                } else {
                    ctx = 1 + 2 * (leftRefFrame0 != 3 && leftRefFrame1 != 3 ? 0 : 1);
                }
            } else if (aboveSingle && leftSingle) {
                if (bin == 0) {
                    ctx = 2 * (aboveRefFrame0 == 1 ? 1 : 0) + 2 * (leftRefFrame0 == 1 ? 1 : 0);
                } else if (aboveRefFrame0 == 1 && leftRefFrame0 == 1) {
                    ctx = 3;
                } else if (aboveRefFrame0 == 1) {
                    ctx = 4 * (leftRefFrame0 == 3 ? 1 : 0);
                } else if (leftRefFrame0 == 1) {
                    ctx = 4 * (aboveRefFrame0 == 3 ? 1 : 0);
                } else {
                    ctx = 2 * (aboveRefFrame0 == 3 ? 1 : 0) + 2 * (leftRefFrame0 == 3 ? 1 : 0);
                }
            } else if (aboveSingle || leftSingle) {
                int rfs = aboveSingle ? aboveRefFrame0 : leftRefFrame0;
                int crf1 = aboveSingle ? leftRefFrame0 : aboveRefFrame0;
                int crf2 = aboveSingle ? leftRefFrame1 : aboveRefFrame1;
                if (bin == 0) {
                    if (rfs == 1) {
                        ctx = 3 + (crf1 != 1 && crf2 != 1 ? 0 : 1);
                    } else {
                        ctx = crf1 != 1 && crf2 != 1 ? 0 : 1;
                    }
                } else if (rfs == 3) {
                    ctx = 3 + (crf1 != 3 && crf2 != 3 ? 0 : 1);
                } else if (rfs == 2) {
                    ctx = crf1 != 3 && crf2 != 3 ? 0 : 1;
                } else {
                    ctx = 1 + 2 * (crf1 != 3 && crf2 != 3 ? 0 : 1);
                }
            } else if (bin == 0) {
                ctx = 1 + (aboveRefFrame0 != 1 && aboveRefFrame1 != 1 && leftRefFrame0 != 1 && leftRefFrame1 != 1 ? 0 : 1);
            } else if (aboveRefFrame0 == leftRefFrame0 && aboveRefFrame1 == leftRefFrame1) {
                ctx = 3 * (aboveRefFrame0 != 3 && aboveRefFrame1 != 3 ? 0 : 1);
            } else {
                ctx = 2;
            }
        } else if (availAbove) {
            if (!aboveIntra && (bin != 1 || aboveRefFrame0 != 1 || !aboveSingle)) {
                if (aboveSingle) {
                    if (bin == 0) {
                        ctx = 4 * (aboveRefFrame0 == 1 ? 1 : 0);
                    } else {
                        ctx = 4 * (aboveRefFrame0 == 3 ? 1 : 0);
                    }
                } else if (bin == 0) {
                    ctx = 1 + (aboveRefFrame0 != 1 && aboveRefFrame1 != 1 ? 0 : 1);
                } else {
                    ctx = 3 * (aboveRefFrame0 != 3 && aboveRefFrame1 != 3 ? 0 : 1);
                }
            } else {
                ctx = 2;
            }
        } else if (availLeft) {
            if (!leftIntra && (bin != 1 || leftRefFrame0 != 1 || !leftSingle)) {
                if (leftSingle) {
                    if (bin == 0) {
                        ctx = 4 * (leftRefFrame0 == 1 ? 1 : 0);
                    } else {
                        ctx = 4 * (leftRefFrame0 == 3 ? 1 : 0);
                    }
                } else if (bin == 0) {
                    ctx = 1 + (leftRefFrame0 != 1 && leftRefFrame1 != 1 ? 0 : 1);
                } else {
                    ctx = 3 * (leftRefFrame0 != 3 && leftRefFrame1 != 3 ? 0 : 1);
                }
            } else {
                ctx = 2;
            }
        } else {
            ctx = 2;
        }
        int[][] probs = c.getSingleRefProbs();
        return decoder.readBit(probs[ctx][bin]) == 1;
    }

    protected boolean readRefMode(int miCol, int miRow, VPXBooleanDecoder decoder, DecodingContext c) {
        boolean availAbove = miRow > 0;
        boolean availLeft = miCol > c.getMiTileStartCol();
        boolean[] aboveCompound = c.getAboveCompound();
        boolean[] leftCompound = c.getLeftCompound();
        int aboveRefFrame0 = getRef(c.getAboveRefs()[miCol], 0);
        int leftRefFrame0 = getRef(c.getLeftRefs()[miRow & 7], 0);
        int compFixedRef = c.getCompFixedRef();
        boolean aboveSingle = !aboveCompound[miCol];
        boolean leftSingle = !leftCompound[miRow % 8];
        boolean aboveIntra = aboveRefFrame0 <= 0;
        boolean leftIntra = leftRefFrame0 <= 0;
        int ctx;
        if (availAbove && availLeft) {
            if (aboveSingle && leftSingle) {
                ctx = aboveRefFrame0 == compFixedRef ^ leftRefFrame0 == compFixedRef ? 1 : 0;
            } else if (aboveSingle) {
                ctx = 2 + (aboveRefFrame0 != compFixedRef && !aboveIntra ? 0 : 1);
            } else if (leftSingle) {
                ctx = 2 + (leftRefFrame0 != compFixedRef && !leftIntra ? 0 : 1);
            } else {
                ctx = 4;
            }
        } else if (availAbove) {
            if (aboveSingle) {
                ctx = aboveRefFrame0 == compFixedRef ? 1 : 0;
            } else {
                ctx = 3;
            }
        } else if (availLeft) {
            if (leftSingle) {
                ctx = leftRefFrame0 == compFixedRef ? 1 : 0;
            } else {
                ctx = 3;
            }
        } else {
            ctx = 1;
        }
        int[] probs = c.getCompModeProb();
        return decoder.readBit(probs[ctx]) == 1;
    }

    private InterModeInfo readInterIntraMode(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c, int segmentId, boolean skip, int txSize) {
        int subModes = 0;
        int yMode;
        if (blSz >= 3) {
            yMode = this.readInterIntraMode(miCol, miRow, blSz, decoder, c);
        } else {
            subModes = this.readInterIntraModeSub(miCol, miRow, blSz, decoder, c);
            yMode = subModes & 0xFF;
        }
        int uvMode = this.readKfUvMode(yMode, decoder, c);
        return new InterModeInfo(segmentId, skip, txSize, yMode, subModes, uvMode, 0L, 0L, 0L, 0L);
    }

    protected int readInterIntraMode(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        int[][] probs = c.getYModeProbs();
        return decoder.readTree(Consts.TREE_INTRA_MODE, probs[Consts.size_group_lookup[blSz]]);
    }

    protected int readInterIntraModeSub(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        int[][] probs = c.getYModeProbs();
        int mode0 = decoder.readTree(Consts.TREE_INTRA_MODE, probs[0]);
        int mode1 = decoder.readTree(Consts.TREE_INTRA_MODE, probs[0]);
        int mode2 = decoder.readTree(Consts.TREE_INTRA_MODE, probs[0]);
        int mode3 = decoder.readTree(Consts.TREE_INTRA_MODE, probs[0]);
        return ModeInfo.vect4(mode0, mode1, mode2, mode3);
    }

    public int readKfUvMode(int yMode, VPXBooleanDecoder decoder, DecodingContext c) {
        int[][] probs = c.getUvModeProbs();
        return decoder.readTree(Consts.TREE_INTRA_MODE, probs[yMode]);
    }

    protected boolean readIsInter(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        boolean availAbove = miRow > 0;
        boolean availLeft = miCol > c.getMiTileStartCol();
        int aboveRefFrame0 = getRef(c.getAboveRefs()[miCol], 0);
        int leftRefFrame0 = getRef(c.getLeftRefs()[miRow & 7], 0);
        boolean leftIntra = availLeft ? leftRefFrame0 <= 0 : true;
        boolean aboveIntra = availAbove ? aboveRefFrame0 <= 0 : true;
        int ctx = 0;
        if (availAbove && availLeft) {
            ctx = leftIntra && aboveIntra ? 3 : (!leftIntra && !aboveIntra ? 0 : 1);
        } else if (availAbove || availLeft) {
            ctx = 2 * (availAbove ? (aboveIntra ? 1 : 0) : (leftIntra ? 1 : 0));
        }
        int[] probs = c.getIsInterProbs();
        return decoder.readBit(probs[ctx]) == 1;
    }

    private static boolean readSegIdPredicted(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        boolean[] aboveSegIdPredicted = c.getAboveSegIdPredicted();
        boolean[] leftSegIdPredicted = c.getLeftSegIdPredicted();
        int ctx = (aboveSegIdPredicted[miRow] ? 1 : 0) + (leftSegIdPredicted[miCol] ? 1 : 0);
        int[] prob = c.getSegmentationPredProbs();
        boolean ret = decoder.readBit(prob[ctx]) == 1;
        for (int i = 0; i < Consts.blH[blSz]; i++) {
            aboveSegIdPredicted[miCol + i] = ret;
        }
        for (int i = 0; i < Consts.blW[blSz]; i++) {
            leftSegIdPredicted[miRow + i] = ret;
        }
        return false;
    }

    private static int predicSegmentId(int miCol, int miRow, int blSz, DecodingContext c) {
        int blWcl = Math.min(c.getMiTileWidth() - miCol, Consts.blW[blSz]);
        int blHcl = Math.min(c.getMiTileHeight() - miRow, Consts.blH[blSz]);
        int[][] prevSegmentIds = c.getPrevSegmentIds();
        int seg = 7;
        for (int y = 0; y < blHcl; y++) {
            for (int x = 0; x < blWcl; x++) {
                seg = Math.min(seg, prevSegmentIds[miRow + y][miCol + x]);
            }
        }
        return seg;
    }
}