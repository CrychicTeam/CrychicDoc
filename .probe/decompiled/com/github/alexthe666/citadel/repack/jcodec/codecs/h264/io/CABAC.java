package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io;

import com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari.MDecoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari.MEncoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils2;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.CABACContst;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.MBType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class CABAC {

    private int chromaPredModeLeft;

    private int[] chromaPredModeTop;

    private int prevMbQpDelta;

    private int prevCBP;

    private int[][] codedBlkLeft;

    private int[][] codedBlkTop;

    private int[] codedBlkDCLeft;

    private int[][] codedBlkDCTop;

    private int[][] refIdxLeft;

    private int[][] refIdxTop;

    private boolean skipFlagLeft;

    private boolean[] skipFlagsTop;

    private int[][][] mvdTop;

    private int[][][] mvdLeft;

    public int[] tmp = new int[16];

    public CABAC(int mbWidth) {
        this.chromaPredModeLeft = 0;
        this.chromaPredModeTop = new int[mbWidth];
        this.codedBlkLeft = new int[][] { new int[4], new int[2], new int[2] };
        this.codedBlkTop = new int[][] { new int[mbWidth << 2], new int[mbWidth << 1], new int[mbWidth << 1] };
        this.codedBlkDCLeft = new int[3];
        this.codedBlkDCTop = new int[3][mbWidth];
        this.refIdxLeft = new int[2][4];
        this.refIdxTop = new int[2][mbWidth << 2];
        this.skipFlagsTop = new boolean[mbWidth];
        this.mvdTop = new int[2][2][mbWidth << 2];
        this.mvdLeft = new int[2][2][4];
    }

    public int readCoeffs(MDecoder decoder, CABAC.BlockType blockType, int[] out, int first, int num, int[] reorder, int[] scMapping, int[] lscMapping) {
        boolean[] sigCoeff = new boolean[num];
        int numCoeff;
        for (numCoeff = 0; numCoeff < num - 1; numCoeff++) {
            sigCoeff[numCoeff] = decoder.decodeBin(blockType.sigCoeffFlagCtxOff + scMapping[numCoeff]) == 1;
            if (sigCoeff[numCoeff] && decoder.decodeBin(blockType.lastSigCoeffCtxOff + lscMapping[numCoeff]) == 1) {
                break;
            }
        }
        sigCoeff[numCoeff++] = true;
        int numGt1 = 0;
        int numEq1 = 0;
        for (int j = numCoeff - 1; j >= 0; j--) {
            if (sigCoeff[j]) {
                int absLev = this.readCoeffAbsLevel(decoder, blockType, numGt1, numEq1);
                if (absLev == 0) {
                    numEq1++;
                } else {
                    numGt1++;
                }
                out[reorder[j + first]] = MathUtil.toSigned(absLev + 1, -decoder.decodeBinBypass());
            }
        }
        return numGt1 + numEq1;
    }

    private int readCoeffAbsLevel(MDecoder decoder, CABAC.BlockType blockType, int numDecodAbsLevelGt1, int numDecodAbsLevelEq1) {
        int incB0 = numDecodAbsLevelGt1 != 0 ? 0 : Math.min(4, 1 + numDecodAbsLevelEq1);
        int incBN = 5 + Math.min(4 - blockType.coeffAbsLevelAdjust, numDecodAbsLevelGt1);
        int b = decoder.decodeBin(blockType.coeffAbsLevelCtxOff + incB0);
        int val;
        for (val = 0; b != 0 && val < 13; val++) {
            b = decoder.decodeBin(blockType.coeffAbsLevelCtxOff + incBN);
        }
        val += b;
        if (val == 14) {
            int log = -2;
            int add = 0;
            int sum = 0;
            do {
                log++;
                b = decoder.decodeBinBypass();
            } while (b != 0);
            while (log >= 0) {
                add |= decoder.decodeBinBypass() << log;
                sum += 1 << log;
                log--;
            }
            val += add + sum;
        }
        return val;
    }

    public void writeCoeffs(MEncoder encoder, CABAC.BlockType blockType, int[] _out, int first, int num, int[] reorder) {
        for (int i = 0; i < num; i++) {
            this.tmp[i] = _out[reorder[first + i]];
        }
        int numCoeff = 0;
        for (int i = 0; i < num; i++) {
            if (this.tmp[i] != 0) {
                numCoeff = i + 1;
            }
        }
        for (int ix = 0; ix < Math.min(numCoeff, num - 1); ix++) {
            if (this.tmp[ix] != 0) {
                encoder.encodeBin(blockType.sigCoeffFlagCtxOff + ix, 1);
                encoder.encodeBin(blockType.lastSigCoeffCtxOff + ix, ix == numCoeff - 1 ? 1 : 0);
            } else {
                encoder.encodeBin(blockType.sigCoeffFlagCtxOff + ix, 0);
            }
        }
        int numGt1 = 0;
        int numEq1 = 0;
        for (int j = numCoeff - 1; j >= 0; j--) {
            if (this.tmp[j] != 0) {
                int absLev = MathUtil.abs(this.tmp[j]) - 1;
                this.writeCoeffAbsLevel(encoder, blockType, numGt1, numEq1, absLev);
                if (absLev == 0) {
                    numEq1++;
                } else {
                    numGt1++;
                }
                encoder.encodeBinBypass(MathUtil.sign(this.tmp[j]));
            }
        }
    }

    private void writeCoeffAbsLevel(MEncoder encoder, CABAC.BlockType blockType, int numDecodAbsLevelGt1, int numDecodAbsLevelEq1, int absLev) {
        int incB0 = numDecodAbsLevelGt1 != 0 ? 0 : Math.min(4, 1 + numDecodAbsLevelEq1);
        int incBN = 5 + Math.min(4 - blockType.coeffAbsLevelAdjust, numDecodAbsLevelGt1);
        if (absLev == 0) {
            encoder.encodeBin(blockType.coeffAbsLevelCtxOff + incB0, 0);
        } else {
            encoder.encodeBin(blockType.coeffAbsLevelCtxOff + incB0, 1);
            if (absLev < 14) {
                for (int i = 1; i < absLev; i++) {
                    encoder.encodeBin(blockType.coeffAbsLevelCtxOff + incBN, 1);
                }
                encoder.encodeBin(blockType.coeffAbsLevelCtxOff + incBN, 0);
            } else {
                for (int i = 1; i < 14; i++) {
                    encoder.encodeBin(blockType.coeffAbsLevelCtxOff + incBN, 1);
                }
                absLev -= 14;
                int sufLen = 0;
                for (int pow = 1; absLev >= pow; pow = 1 << ++sufLen) {
                    encoder.encodeBinBypass(1);
                    absLev -= pow;
                }
                encoder.encodeBinBypass(0);
                sufLen--;
                while (sufLen >= 0) {
                    encoder.encodeBinBypass(absLev >> sufLen & 1);
                    sufLen--;
                }
            }
        }
    }

    public void initModels(int[][] cm, SliceType sliceType, int cabacIdc, int sliceQp) {
        int[] tabA = sliceType.isIntra() ? CABACContst.cabac_context_init_I_A : CABACContst.cabac_context_init_PB_A[cabacIdc];
        int[] tabB = sliceType.isIntra() ? CABACContst.cabac_context_init_I_B : CABACContst.cabac_context_init_PB_B[cabacIdc];
        for (int i = 0; i < 1024; i++) {
            int preCtxState = MathUtil.clip((tabA[i] * MathUtil.clip(sliceQp, 0, 51) >> 4) + tabB[i], 1, 126);
            if (preCtxState <= 63) {
                cm[0][i] = 63 - preCtxState;
                cm[1][i] = 0;
            } else {
                cm[0][i] = preCtxState - 64;
                cm[1][i] = 1;
            }
        }
    }

    public int readMBTypeI(MDecoder decoder, MBType left, MBType top, boolean leftAvailable, boolean topAvailable) {
        int ctx = 3;
        ctx += leftAvailable && left != MBType.I_NxN ? 1 : 0;
        ctx += topAvailable && top != MBType.I_NxN ? 1 : 0;
        if (decoder.decodeBin(ctx) == 0) {
            return 0;
        } else {
            return decoder.decodeFinalBin() == 1 ? 25 : 1 + this.readMBType16x16(decoder);
        }
    }

    private int readMBType16x16(MDecoder decoder) {
        int type = decoder.decodeBin(6) * 12;
        return decoder.decodeBin(7) == 0 ? type + (decoder.decodeBin(9) << 1) + decoder.decodeBin(10) : type + (decoder.decodeBin(8) << 2) + (decoder.decodeBin(9) << 1) + decoder.decodeBin(10) + 4;
    }

    public int readMBTypeP(MDecoder decoder) {
        if (decoder.decodeBin(14) == 1) {
            return 5 + this.readIntraP(decoder, 17);
        } else if (decoder.decodeBin(15) == 0) {
            return decoder.decodeBin(16) == 0 ? 0 : 3;
        } else {
            return decoder.decodeBin(17) == 0 ? 2 : 1;
        }
    }

    private int readIntraP(MDecoder decoder, int ctxOff) {
        if (decoder.decodeBin(ctxOff) == 0) {
            return 0;
        } else {
            return decoder.decodeFinalBin() == 1 ? 25 : 1 + this.readMBType16x16P(decoder, ctxOff);
        }
    }

    private int readMBType16x16P(MDecoder decoder, int ctxOff) {
        int type = decoder.decodeBin(++ctxOff) * 12;
        ctxOff++;
        return decoder.decodeBin(ctxOff) == 0 ? type + (decoder.decodeBin(++ctxOff) << 1) + decoder.decodeBin(ctxOff) : type + (decoder.decodeBin(ctxOff) << 2) + (decoder.decodeBin(ctxOff + 1) << 1) + decoder.decodeBin(ctxOff + 1) + 4;
    }

    public int readMBTypeB(MDecoder mDecoder, MBType left, MBType top, boolean leftAvailable, boolean topAvailable) {
        int ctx = 27;
        ctx += leftAvailable && left != null && left != MBType.B_Direct_16x16 ? 1 : 0;
        ctx += topAvailable && top != null && top != MBType.B_Direct_16x16 ? 1 : 0;
        if (mDecoder.decodeBin(ctx) == 0) {
            return 0;
        } else if (mDecoder.decodeBin(30) == 0) {
            return 1 + mDecoder.decodeBin(32);
        } else {
            int b1 = mDecoder.decodeBin(31);
            if (b1 == 0) {
                return 3 + (mDecoder.decodeBin(32) << 2 | mDecoder.decodeBin(32) << 1 | mDecoder.decodeBin(32));
            } else if (mDecoder.decodeBin(32) == 0) {
                return 12 + (mDecoder.decodeBin(32) << 2 | mDecoder.decodeBin(32) << 1 | mDecoder.decodeBin(32));
            } else {
                switch((mDecoder.decodeBin(32) << 1) + mDecoder.decodeBin(32)) {
                    case 0:
                        return 20 + mDecoder.decodeBin(32);
                    case 1:
                        return 23 + this.readIntraP(mDecoder, 32);
                    case 2:
                        return 11;
                    case 3:
                        return 22;
                    default:
                        return 0;
                }
            }
        }
    }

    public void writeMBTypeI(MEncoder encoder, MBType left, MBType top, boolean leftAvailable, boolean topAvailable, int mbType) {
        int ctx = 3;
        ctx += leftAvailable && left != MBType.I_NxN ? 1 : 0;
        ctx += topAvailable && top != MBType.I_NxN ? 1 : 0;
        if (mbType == 0) {
            encoder.encodeBin(ctx, 0);
        } else {
            encoder.encodeBin(ctx, 1);
            if (mbType == 25) {
                encoder.encodeBinFinal(1);
            } else {
                encoder.encodeBinFinal(0);
                this.writeMBType16x16(encoder, mbType - 1);
            }
        }
    }

    private void writeMBType16x16(MEncoder encoder, int mbType) {
        if (mbType < 12) {
            encoder.encodeBin(6, 0);
        } else {
            encoder.encodeBin(6, 1);
            mbType -= 12;
        }
        if (mbType < 4) {
            encoder.encodeBin(7, 0);
            encoder.encodeBin(9, mbType >> 1);
            encoder.encodeBin(10, mbType & 1);
        } else {
            mbType -= 4;
            encoder.encodeBin(7, 1);
            encoder.encodeBin(8, mbType >> 2);
            encoder.encodeBin(9, mbType >> 1 & 1);
            encoder.encodeBin(10, mbType & 1);
        }
    }

    public int readMBQpDelta(MDecoder decoder, MBType prevMbType) {
        int ctx = 60;
        ctx += prevMbType != null && prevMbType != MBType.I_PCM && (prevMbType == MBType.I_16x16 || this.prevCBP != 0) && this.prevMbQpDelta != 0 ? 1 : 0;
        int val = 0;
        if (decoder.decodeBin(ctx) == 1) {
            val++;
            if (decoder.decodeBin(62) == 1) {
                val++;
                while (decoder.decodeBin(63) == 1) {
                    val++;
                }
            }
        }
        this.prevMbQpDelta = H264Utils2.golomb2Signed(val);
        return this.prevMbQpDelta;
    }

    public void writeMBQpDelta(MEncoder encoder, MBType prevMbType, int mbQpDelta) {
        int ctx = 60;
        ctx += prevMbType != null && prevMbType != MBType.I_PCM && (prevMbType == MBType.I_16x16 || this.prevCBP != 0) && this.prevMbQpDelta != 0 ? 1 : 0;
        this.prevMbQpDelta = mbQpDelta;
        if (mbQpDelta-- == 0) {
            encoder.encodeBin(ctx, 0);
        } else {
            encoder.encodeBin(ctx, 1);
            if (mbQpDelta-- == 0) {
                encoder.encodeBin(62, 0);
            } else {
                while (mbQpDelta-- > 0) {
                    encoder.encodeBin(63, 1);
                }
                encoder.encodeBin(63, 0);
            }
        }
    }

    public int readIntraChromaPredMode(MDecoder decoder, int mbX, MBType left, MBType top, boolean leftAvailable, boolean topAvailable) {
        int ctx = 64;
        ctx += leftAvailable && left != null && left.isIntra() && this.chromaPredModeLeft != 0 ? 1 : 0;
        ctx += topAvailable && top != null && top.isIntra() && this.chromaPredModeTop[mbX] != 0 ? 1 : 0;
        int mode;
        if (decoder.decodeBin(ctx) == 0) {
            mode = 0;
        } else if (decoder.decodeBin(67) == 0) {
            mode = 1;
        } else if (decoder.decodeBin(67) == 0) {
            mode = 2;
        } else {
            mode = 3;
        }
        this.chromaPredModeLeft = this.chromaPredModeTop[mbX] = mode;
        return mode;
    }

    public void writeIntraChromaPredMode(MEncoder encoder, int mbX, MBType left, MBType top, boolean leftAvailable, boolean topAvailable, int mode) {
        int ctx = 64;
        ctx += leftAvailable && left.isIntra() && this.chromaPredModeLeft != 0 ? 1 : 0;
        ctx += topAvailable && top.isIntra() && this.chromaPredModeTop[mbX] != 0 ? 1 : 0;
        encoder.encodeBin(ctx, mode-- == 0 ? 0 : 1);
        for (int i = 0; mode >= 0 && i < 2; i++) {
            encoder.encodeBin(67, mode-- == 0 ? 0 : 1);
        }
        this.chromaPredModeLeft = this.chromaPredModeTop[mbX] = mode;
    }

    public int condTerm(MBType mbCur, boolean nAvb, MBType mbN, boolean nBlkAvb, int cbpN) {
        if (!nAvb) {
            return mbCur.isIntra() ? 1 : 0;
        } else if (mbN == MBType.I_PCM) {
            return 1;
        } else {
            return !nBlkAvb ? 0 : cbpN;
        }
    }

    public int readCodedBlockFlagLumaDC(MDecoder decoder, int mbX, MBType left, MBType top, boolean leftAvailable, boolean topAvailable, MBType cur) {
        int tLeft = this.condTerm(cur, leftAvailable, left, left == MBType.I_16x16, this.codedBlkDCLeft[0]);
        int tTop = this.condTerm(cur, topAvailable, top, top == MBType.I_16x16, this.codedBlkDCTop[0][mbX]);
        int decoded = decoder.decodeBin(CABAC.BlockType.LUMA_16_DC.codedBlockCtxOff + tLeft + 2 * tTop);
        this.codedBlkDCLeft[0] = decoded;
        this.codedBlkDCTop[0][mbX] = decoded;
        return decoded;
    }

    public int readCodedBlockFlagChromaDC(MDecoder decoder, int mbX, int comp, MBType left, MBType top, boolean leftAvailable, boolean topAvailable, int leftCBPChroma, int topCBPChroma, MBType cur) {
        int tLeft = this.condTerm(cur, leftAvailable, left, left != null && leftCBPChroma != 0, this.codedBlkDCLeft[comp]);
        int tTop = this.condTerm(cur, topAvailable, top, top != null && topCBPChroma != 0, this.codedBlkDCTop[comp][mbX]);
        int decoded = decoder.decodeBin(CABAC.BlockType.CHROMA_DC.codedBlockCtxOff + tLeft + 2 * tTop);
        this.codedBlkDCLeft[comp] = decoded;
        this.codedBlkDCTop[comp][mbX] = decoded;
        return decoded;
    }

    public int readCodedBlockFlagLumaAC(MDecoder decoder, CABAC.BlockType blkType, int blkX, int blkY, int comp, MBType left, MBType top, boolean leftAvailable, boolean topAvailable, int leftCBPLuma, int topCBPLuma, int curCBPLuma, MBType cur) {
        int blkOffLeft = blkX & 3;
        int blkOffTop = blkY & 3;
        int tLeft;
        if (blkOffLeft == 0) {
            tLeft = this.condTerm(cur, leftAvailable, left, left != null && left != MBType.I_PCM && this.cbp(leftCBPLuma, 3, blkOffTop), this.codedBlkLeft[comp][blkOffTop]);
        } else {
            tLeft = this.condTerm(cur, true, cur, this.cbp(curCBPLuma, blkOffLeft - 1, blkOffTop), this.codedBlkLeft[comp][blkOffTop]);
        }
        int tTop;
        if (blkOffTop == 0) {
            tTop = this.condTerm(cur, topAvailable, top, top != null && top != MBType.I_PCM && this.cbp(topCBPLuma, blkOffLeft, 3), this.codedBlkTop[comp][blkX]);
        } else {
            tTop = this.condTerm(cur, true, cur, this.cbp(curCBPLuma, blkOffLeft, blkOffTop - 1), this.codedBlkTop[comp][blkX]);
        }
        int decoded = decoder.decodeBin(blkType.codedBlockCtxOff + tLeft + 2 * tTop);
        this.codedBlkLeft[comp][blkOffTop] = decoded;
        this.codedBlkTop[comp][blkX] = decoded;
        return decoded;
    }

    public int readCodedBlockFlagLuma64(MDecoder decoder, int blkX, int blkY, int comp, MBType left, MBType top, boolean leftAvailable, boolean topAvailable, int leftCBPLuma, int topCBPLuma, int curCBPLuma, MBType cur, boolean is8x8Left, boolean is8x8Top) {
        int blkOffLeft = blkX & 3;
        int blkOffTop = blkY & 3;
        int tLeft;
        if (blkOffLeft == 0) {
            tLeft = this.condTerm(cur, leftAvailable, left, left != null && left != MBType.I_PCM && is8x8Left && this.cbp(leftCBPLuma, 3, blkOffTop), this.codedBlkLeft[comp][blkOffTop]);
        } else {
            tLeft = this.condTerm(cur, true, cur, this.cbp(curCBPLuma, blkOffLeft - 1, blkOffTop), this.codedBlkLeft[comp][blkOffTop]);
        }
        int tTop;
        if (blkOffTop == 0) {
            tTop = this.condTerm(cur, topAvailable, top, top != null && top != MBType.I_PCM && is8x8Top && this.cbp(topCBPLuma, blkOffLeft, 3), this.codedBlkTop[comp][blkX]);
        } else {
            tTop = this.condTerm(cur, true, cur, this.cbp(curCBPLuma, blkOffLeft, blkOffTop - 1), this.codedBlkTop[comp][blkX]);
        }
        int decoded = decoder.decodeBin(CABAC.BlockType.LUMA_64.codedBlockCtxOff + tLeft + 2 * tTop);
        this.codedBlkLeft[comp][blkOffTop] = decoded;
        this.codedBlkTop[comp][blkX] = decoded;
        return decoded;
    }

    private boolean cbp(int cbpLuma, int blkX, int blkY) {
        int x8x8 = (blkY & 2) + (blkX >> 1);
        return (cbpLuma >> x8x8 & 1) == 1;
    }

    public int readCodedBlockFlagChromaAC(MDecoder decoder, int blkX, int blkY, int comp, MBType left, MBType top, boolean leftAvailable, boolean topAvailable, int leftCBPChroma, int topCBPChroma, MBType cur) {
        int blkOffLeft = blkX & 1;
        int blkOffTop = blkY & 1;
        int tLeft;
        if (blkOffLeft == 0) {
            tLeft = this.condTerm(cur, leftAvailable, left, left != null && left != MBType.I_PCM && (leftCBPChroma & 2) != 0, this.codedBlkLeft[comp][blkOffTop]);
        } else {
            tLeft = this.condTerm(cur, true, cur, true, this.codedBlkLeft[comp][blkOffTop]);
        }
        int tTop;
        if (blkOffTop == 0) {
            tTop = this.condTerm(cur, topAvailable, top, top != null && top != MBType.I_PCM && (topCBPChroma & 2) != 0, this.codedBlkTop[comp][blkX]);
        } else {
            tTop = this.condTerm(cur, true, cur, true, this.codedBlkTop[comp][blkX]);
        }
        int decoded = decoder.decodeBin(CABAC.BlockType.CHROMA_AC.codedBlockCtxOff + tLeft + 2 * tTop);
        this.codedBlkLeft[comp][blkOffTop] = decoded;
        this.codedBlkTop[comp][blkX] = decoded;
        return decoded;
    }

    public boolean prev4x4PredModeFlag(MDecoder decoder) {
        return decoder.decodeBin(68) == 1;
    }

    public int rem4x4PredMode(MDecoder decoder) {
        return decoder.decodeBin(69) | decoder.decodeBin(69) << 1 | decoder.decodeBin(69) << 2;
    }

    public int codedBlockPatternIntra(MDecoder mDecoder, boolean leftAvailable, boolean topAvailable, int cbpLeft, int cbpTop, MBType mbLeft, MBType mbTop) {
        int cbp0 = mDecoder.decodeBin(73 + this._condTerm(leftAvailable, mbLeft, cbpLeft >> 1 & 1) + 2 * this._condTerm(topAvailable, mbTop, cbpTop >> 2 & 1));
        int cbp1 = mDecoder.decodeBin(73 + (1 - cbp0) + 2 * this._condTerm(topAvailable, mbTop, cbpTop >> 3 & 1));
        int cbp2 = mDecoder.decodeBin(73 + this._condTerm(leftAvailable, mbLeft, cbpLeft >> 3 & 1) + 2 * (1 - cbp0));
        int cbp3 = mDecoder.decodeBin(73 + (1 - cbp2) + 2 * (1 - cbp1));
        int cr0 = mDecoder.decodeBin(77 + this.condTermCr0(leftAvailable, mbLeft, cbpLeft >> 4) + 2 * this.condTermCr0(topAvailable, mbTop, cbpTop >> 4));
        int cr1 = cr0 != 0 ? mDecoder.decodeBin(81 + this.condTermCr1(leftAvailable, mbLeft, cbpLeft >> 4) + 2 * this.condTermCr1(topAvailable, mbTop, cbpTop >> 4)) : 0;
        return cbp0 | cbp1 << 1 | cbp2 << 2 | cbp3 << 3 | cr0 << 4 | cr1 << 5;
    }

    private int condTermCr0(boolean avb, MBType mbt, int cbpChroma) {
        return !avb || mbt != MBType.I_PCM && (mbt == null || cbpChroma == 0) ? 0 : 1;
    }

    private int condTermCr1(boolean avb, MBType mbt, int cbpChroma) {
        return !avb || mbt != MBType.I_PCM && (mbt == null || (cbpChroma & 2) == 0) ? 0 : 1;
    }

    private int _condTerm(boolean avb, MBType mbt, int cbp) {
        return avb && mbt != MBType.I_PCM && (mbt == null || cbp != 1) ? 1 : 0;
    }

    public void setPrevCBP(int prevCBP) {
        this.prevCBP = prevCBP;
    }

    public int readMVD(MDecoder decoder, int comp, boolean leftAvailable, boolean topAvailable, MBType leftType, MBType topType, H264Const.PartPred leftPred, H264Const.PartPred topPred, H264Const.PartPred curPred, int mbX, int partX, int partY, int partW, int partH, int list) {
        int ctx = comp == 0 ? 40 : 47;
        int partAbsX = (mbX << 2) + partX;
        boolean predEqA = leftPred != null && leftPred != H264Const.PartPred.Direct && (leftPred == H264Const.PartPred.Bi || leftPred == curPred || curPred == H264Const.PartPred.Bi && H264Const.usesList(leftPred, list));
        boolean predEqB = topPred != null && topPred != H264Const.PartPred.Direct && (topPred == H264Const.PartPred.Bi || topPred == curPred || curPred == H264Const.PartPred.Bi && H264Const.usesList(topPred, list));
        int absMvdComp = leftAvailable && leftType != null && !leftType.isIntra() && predEqA ? Math.abs(this.mvdLeft[list][comp][partY]) : 0;
        absMvdComp += topAvailable && topType != null && !topType.isIntra() && predEqB ? Math.abs(this.mvdTop[list][comp][partAbsX]) : 0;
        int b = decoder.decodeBin(ctx + (absMvdComp < 3 ? 0 : (absMvdComp > 32 ? 2 : 1)));
        int val;
        for (val = 0; b != 0 && val < 8; val++) {
            b = decoder.decodeBin(Math.min(ctx + val + 3, ctx + 6));
        }
        val += b;
        if (val != 0) {
            if (val == 9) {
                int log = 2;
                int add = 0;
                int sum = 0;
                int leftover = 0;
                do {
                    sum += leftover;
                    log++;
                    b = decoder.decodeBinBypass();
                    leftover = 1 << log;
                } while (b != 0);
                log--;
                while (log >= 0) {
                    add |= decoder.decodeBinBypass() << log;
                    log--;
                }
                val += add + sum;
            }
            val = MathUtil.toSigned(val, -decoder.decodeBinBypass());
        }
        for (int i = 0; i < partW; i++) {
            this.mvdTop[list][comp][partAbsX + i] = val;
        }
        for (int i = 0; i < partH; i++) {
            this.mvdLeft[list][comp][partY + i] = val;
        }
        return val;
    }

    public int readRefIdx(MDecoder mDecoder, boolean leftAvailable, boolean topAvailable, MBType leftType, MBType topType, H264Const.PartPred leftPred, H264Const.PartPred topPred, H264Const.PartPred curPred, int mbX, int partX, int partY, int partW, int partH, int list) {
        int partAbsX = (mbX << 2) + partX;
        boolean predEqA = leftPred != null && leftPred != H264Const.PartPred.Direct && (leftPred == H264Const.PartPred.Bi || leftPred == curPred || curPred == H264Const.PartPred.Bi && H264Const.usesList(leftPred, list));
        boolean predEqB = topPred != null && topPred != H264Const.PartPred.Direct && (topPred == H264Const.PartPred.Bi || topPred == curPred || curPred == H264Const.PartPred.Bi && H264Const.usesList(topPred, list));
        int ctA = leftAvailable && leftType != null && !leftType.isIntra() && predEqA && this.refIdxLeft[list][partY] != 0 ? 1 : 0;
        int ctB = topAvailable && topType != null && !topType.isIntra() && predEqB && this.refIdxTop[list][partAbsX] != 0 ? 1 : 0;
        int b0 = mDecoder.decodeBin(54 + ctA + 2 * ctB);
        int val;
        if (b0 == 0) {
            val = 0;
        } else {
            int b1 = mDecoder.decodeBin(58);
            if (b1 == 0) {
                val = 1;
            } else {
                val = 2;
                while (mDecoder.decodeBin(59) == 1) {
                    val++;
                }
            }
        }
        for (int i = 0; i < partW; i++) {
            this.refIdxTop[list][partAbsX + i] = val;
        }
        for (int i = 0; i < partH; i++) {
            this.refIdxLeft[list][partY + i] = val;
        }
        return val;
    }

    public boolean readMBSkipFlag(MDecoder mDecoder, SliceType slType, boolean leftAvailable, boolean topAvailable, int mbX) {
        int base = slType == SliceType.P ? 11 : 24;
        boolean ret = mDecoder.decodeBin(base + (leftAvailable && !this.skipFlagLeft ? 1 : 0) + (topAvailable && !this.skipFlagsTop[mbX] ? 1 : 0)) == 1;
        this.skipFlagLeft = this.skipFlagsTop[mbX] = ret;
        return ret;
    }

    public int readSubMbTypeP(MDecoder mDecoder) {
        if (mDecoder.decodeBin(21) == 1) {
            return 0;
        } else if (mDecoder.decodeBin(22) == 0) {
            return 1;
        } else {
            return mDecoder.decodeBin(23) == 1 ? 2 : 3;
        }
    }

    public int readSubMbTypeB(MDecoder mDecoder) {
        if (mDecoder.decodeBin(36) == 0) {
            return 0;
        } else if (mDecoder.decodeBin(37) == 0) {
            return 1 + mDecoder.decodeBin(39);
        } else if (mDecoder.decodeBin(38) == 0) {
            return 3 + (mDecoder.decodeBin(39) << 1) + mDecoder.decodeBin(39);
        } else {
            return mDecoder.decodeBin(39) == 0 ? 7 + (mDecoder.decodeBin(39) << 1) + mDecoder.decodeBin(39) : 11 + mDecoder.decodeBin(39);
        }
    }

    public boolean readTransform8x8Flag(MDecoder mDecoder, boolean leftAvailable, boolean topAvailable, MBType leftType, MBType topType, boolean is8x8Left, boolean is8x8Top) {
        int ctx = 399 + (leftAvailable && leftType != null && is8x8Left ? 1 : 0) + (topAvailable && topType != null && is8x8Top ? 1 : 0);
        return mDecoder.decodeBin(ctx) == 1;
    }

    public void setCodedBlock(int blkX, int blkY) {
        this.codedBlkLeft[0][blkY & 3] = this.codedBlkTop[0][blkX] = 1;
    }

    public static final class BlockType {

        public static final CABAC.BlockType LUMA_16_DC = new CABAC.BlockType(85, 105, 166, 277, 338, 227, 0);

        public static final CABAC.BlockType LUMA_15_AC = new CABAC.BlockType(89, 120, 181, 292, 353, 237, 0);

        public static final CABAC.BlockType LUMA_16 = new CABAC.BlockType(93, 134, 195, 306, 367, 247, 0);

        public static final CABAC.BlockType CHROMA_DC = new CABAC.BlockType(97, 149, 210, 321, 382, 257, 1);

        public static final CABAC.BlockType CHROMA_AC = new CABAC.BlockType(101, 152, 213, 324, 385, 266, 0);

        public static final CABAC.BlockType LUMA_64 = new CABAC.BlockType(1012, 402, 417, 436, 451, 426, 0);

        public static final CABAC.BlockType CB_16_DC = new CABAC.BlockType(460, 484, 572, 776, 864, 952, 0);

        public static final CABAC.BlockType CB_15x16_AC = new CABAC.BlockType(464, 499, 587, 791, 879, 962, 0);

        public static final CABAC.BlockType CB_16 = new CABAC.BlockType(468, 513, 601, 805, 893, 972, 0);

        public static final CABAC.BlockType CB_64 = new CABAC.BlockType(1016, 660, 690, 675, 699, 708, 0);

        public static final CABAC.BlockType CR_16_DC = new CABAC.BlockType(472, 528, 616, 820, 908, 982, 0);

        public static final CABAC.BlockType CR_15x16_AC = new CABAC.BlockType(476, 543, 631, 835, 923, 992, 0);

        public static final CABAC.BlockType CR_16 = new CABAC.BlockType(480, 557, 645, 849, 937, 1002, 0);

        public static final CABAC.BlockType CR_64 = new CABAC.BlockType(1020, 718, 748, 733, 757, 766, 0);

        public int codedBlockCtxOff;

        public int sigCoeffFlagCtxOff;

        public int lastSigCoeffCtxOff;

        public int sigCoeffFlagFldCtxOff;

        public int lastSigCoeffFldCtxOff;

        public int coeffAbsLevelCtxOff;

        public int coeffAbsLevelAdjust;

        private BlockType(int codecBlockCtxOff, int sigCoeffCtxOff, int lastSigCoeffCtxOff, int sigCoeffFlagFldCtxOff, int lastSigCoeffFldCtxOff, int coeffAbsLevelCtxOff, int coeffAbsLevelAdjust) {
            this.codedBlockCtxOff = codecBlockCtxOff;
            this.sigCoeffFlagCtxOff = sigCoeffCtxOff;
            this.lastSigCoeffCtxOff = lastSigCoeffCtxOff;
            this.sigCoeffFlagFldCtxOff = sigCoeffFlagFldCtxOff;
            this.lastSigCoeffFldCtxOff = sigCoeffFlagFldCtxOff;
            this.coeffAbsLevelCtxOff = coeffAbsLevelCtxOff;
            this.coeffAbsLevelAdjust = coeffAbsLevelAdjust;
        }
    }
}