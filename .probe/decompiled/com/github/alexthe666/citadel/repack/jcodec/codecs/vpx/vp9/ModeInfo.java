package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.vp9;

import com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.VPXBooleanDecoder;

public class ModeInfo {

    private int segmentId;

    private boolean skip;

    private int txSize;

    private int yMode;

    private int subModes;

    private int uvMode;

    ModeInfo() {
    }

    public ModeInfo(int segmentId, boolean skip, int txSize, int yMode, int subModes, int uvMode) {
        this.segmentId = segmentId;
        this.skip = skip;
        this.txSize = txSize;
        this.yMode = yMode;
        this.subModes = subModes;
        this.uvMode = uvMode;
    }

    public int getSegmentId() {
        return this.segmentId;
    }

    public boolean isSkip() {
        return this.skip;
    }

    public int getTxSize() {
        return this.txSize;
    }

    public int getYMode() {
        return this.yMode;
    }

    public int getSubModes() {
        return this.subModes;
    }

    public int getUvMode() {
        return this.uvMode;
    }

    public ModeInfo read(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        int segmentId = 0;
        if (c.isSegmentationEnabled() && c.isUpdateSegmentMap()) {
            segmentId = readSegmentId(decoder, c);
        }
        boolean skip = true;
        if (!c.isSegmentFeatureActive(segmentId, 3)) {
            skip = this.readSkipFlag(miCol, miRow, blSz, decoder, c);
        }
        int txSize = this.readTxSize(miCol, miRow, blSz, true, decoder, c);
        int subModes = 0;
        int yMode;
        if (blSz >= 3) {
            yMode = this.readKfIntraMode(miCol, miRow, blSz, decoder, c);
        } else {
            subModes = this.readKfIntraModeSub(miCol, miRow, blSz, decoder, c);
            yMode = subModes & 0xFF;
        }
        int uvMode = this.readInterIntraUvMode(yMode, decoder, c);
        return new ModeInfo(segmentId, skip, txSize, yMode, subModes, uvMode);
    }

    public int readKfIntraMode(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        boolean availAbove = miRow > 0;
        boolean availLeft = miCol > c.getMiTileStartCol();
        int[] aboveIntraModes = c.getAboveModes();
        int[] leftIntraModes = c.getLeftModes();
        int aboveMode = availAbove ? aboveIntraModes[miCol] : 0;
        int leftMode = availLeft ? leftIntraModes[miRow % 8] : 0;
        int[][][] probs = c.getKfYModeProbs();
        int intraMode = decoder.readTree(Consts.TREE_INTRA_MODE, probs[aboveMode][leftMode]);
        aboveIntraModes[miCol] = intraMode;
        leftIntraModes[miRow % 8] = intraMode;
        return intraMode;
    }

    public int readKfIntraModeSub(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        boolean availAbove = miRow > 0;
        boolean availLeft = miCol > c.getMiTileStartCol();
        int[] aboveIntraModes = c.getAboveModes();
        int[] leftIntraModes = c.getLeftModes();
        int[][][] probs = c.getKfYModeProbs();
        int aboveMode = availAbove ? aboveIntraModes[miCol] : 0;
        int leftMode = availLeft ? leftIntraModes[miRow & 7] : 0;
        int mode0 = decoder.readTree(Consts.TREE_INTRA_MODE, probs[aboveMode][leftMode]);
        int mode1 = 0;
        int mode2 = 0;
        int mode3 = 0;
        if (blSz == 0) {
            mode1 = decoder.readTree(Consts.TREE_INTRA_MODE, probs[aboveMode][mode0]);
            mode2 = decoder.readTree(Consts.TREE_INTRA_MODE, probs[mode0][leftMode]);
            mode3 = decoder.readTree(Consts.TREE_INTRA_MODE, probs[mode1][mode2]);
            aboveIntraModes[miCol] = mode2;
            leftIntraModes[miRow & 7] = mode1;
            return vect4(mode0, mode1, mode2, mode3);
        } else if (blSz == 1) {
            mode1 = decoder.readTree(Consts.TREE_INTRA_MODE, probs[aboveMode][mode0]);
            aboveIntraModes[miCol] = mode0;
            leftIntraModes[miRow & 7] = mode1;
            return vect4(mode0, mode1, mode0, mode1);
        } else if (blSz == 2) {
            mode1 = decoder.readTree(Consts.TREE_INTRA_MODE, probs[mode0][leftMode]);
            aboveIntraModes[miCol] = mode1;
            leftIntraModes[miRow & 7] = mode0;
            return vect4(mode0, mode0, mode1, mode1);
        } else {
            return 0;
        }
    }

    public static int vect4(int val0, int val1, int val2, int val3) {
        return val0 | val1 << 8 | val2 << 16 | val3 << 24;
    }

    public static int vect4get(int vect, int ind) {
        return vect >> (ind << 3) & 0xFF;
    }

    public int readTxSize(int miCol, int miRow, int blSz, boolean allowSelect, VPXBooleanDecoder decoder, DecodingContext c) {
        if (blSz < 3) {
            return 0;
        } else {
            int maxTxSize = Consts.maxTxLookup[blSz];
            int txSize = Math.min(maxTxSize, c.getTxMode());
            if (allowSelect && c.getTxMode() == 4) {
                boolean availAbove = miRow > 0;
                boolean availLeft = miCol > c.getMiTileStartCol();
                int above = maxTxSize;
                int left = maxTxSize;
                if (availAbove && !c.getAboveSkipped()[miCol]) {
                    above = c.getAboveTxSizes()[miCol];
                }
                if (availLeft && !c.getLeftSkipped()[miRow & 7]) {
                    left = c.getLeftTxSizes()[miRow & 7];
                }
                if (!availLeft) {
                    left = above;
                }
                if (!availAbove) {
                    above = left;
                }
                int ctx = above + left > maxTxSize ? 1 : 0;
                int[][] probs = (int[][]) null;
                switch(maxTxSize) {
                    case 1:
                        probs = c.getTx8x8Probs();
                        break;
                    case 2:
                        probs = c.getTx16x16Probs();
                        break;
                    case 3:
                        probs = c.getTx32x32Probs();
                        break;
                    default:
                        throw new RuntimeException("Shouldn't happen");
                }
                txSize = decoder.readTree(Consts.TREE_TX_SIZE[maxTxSize], probs[ctx]);
            } else {
                txSize = Math.min(maxTxSize, c.getTxMode());
            }
            for (int i = 0; i < Consts.blH[blSz]; i++) {
                c.getLeftTxSizes()[miRow + i & 7] = txSize;
            }
            for (int j = 0; j < Consts.blW[blSz]; j++) {
                c.getAboveTxSizes()[miCol + j & 7] = txSize;
            }
            return txSize;
        }
    }

    public static int readSegmentId(VPXBooleanDecoder decoder, DecodingContext c) {
        int[] probs = c.getSegmentationTreeProbs();
        return decoder.readTree(Consts.TREE_SEGMENT_ID, probs);
    }

    public boolean readSkipFlag(int miCol, int miRow, int blSz, VPXBooleanDecoder decoder, DecodingContext c) {
        int ctx = 0;
        boolean availAbove = miRow > 0;
        boolean availLeft = miCol > c.getMiTileStartCol();
        boolean[] aboveSkipped = c.getAboveSkipped();
        boolean[] leftSkipped = c.getLeftSkipped();
        if (availAbove) {
            ctx += aboveSkipped[miCol] ? 1 : 0;
        }
        if (availLeft) {
            ctx += leftSkipped[miRow & 7] ? 1 : 0;
        }
        System.out.println("SKIP CTX: " + ctx);
        int[] probs = c.getSkipProbs();
        boolean ret = decoder.readBit(probs[ctx]) == 1;
        for (int i = 0; i < Consts.blH[blSz]; i++) {
            leftSkipped[i + miRow & 7] = ret;
        }
        for (int j = 0; j < Consts.blW[blSz]; j++) {
            aboveSkipped[j + miCol] = ret;
        }
        return ret;
    }

    public boolean isInter() {
        return false;
    }

    public int readInterIntraUvMode(int yMode, VPXBooleanDecoder decoder, DecodingContext c) {
        int[][] probs = c.getKfUVModeProbs();
        return decoder.readTree(Consts.TREE_INTRA_MODE, probs[yMode]);
    }
}