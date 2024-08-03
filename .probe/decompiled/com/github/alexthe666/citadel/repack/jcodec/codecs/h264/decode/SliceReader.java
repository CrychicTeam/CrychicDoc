package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari.MDecoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso.Mapper;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.CABAC;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.CAVLC;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.MBType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnit;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.PictureParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;

public class SliceReader {

    private PictureParameterSet activePps;

    private CABAC cabac;

    private MDecoder mDecoder;

    private CAVLC[] cavlc;

    private BitReader reader;

    private Mapper mapper;

    private SliceHeader sh;

    private NALUnit nalUnit;

    private boolean prevMbSkipped = false;

    private int mbIdx;

    private MBType prevMBType = null;

    private int mbSkipRun;

    private boolean endOfData;

    MBType[] topMBType;

    MBType leftMBType;

    int leftCBPLuma;

    int[] topCBPLuma;

    int leftCBPChroma;

    int[] topCBPChroma;

    ColorSpace chromaFormat;

    boolean transform8x8;

    int[] numRef;

    boolean tf8x8Left;

    boolean[] tf8x8Top;

    int[] i4x4PredTop;

    int[] i4x4PredLeft;

    H264Const.PartPred[] predModeLeft;

    H264Const.PartPred[] predModeTop;

    public SliceReader(PictureParameterSet activePps, CABAC cabac, CAVLC[] cavlc, MDecoder mDecoder, BitReader reader, Mapper mapper, SliceHeader sh, NALUnit nalUnit) {
        this.activePps = activePps;
        this.cabac = cabac;
        this.mDecoder = mDecoder;
        this.cavlc = cavlc;
        this.reader = reader;
        this.mapper = mapper;
        this.sh = sh;
        this.nalUnit = nalUnit;
        int mbWidth = sh.sps.picWidthInMbsMinus1 + 1;
        this.topMBType = new MBType[mbWidth];
        this.topCBPLuma = new int[mbWidth];
        this.topCBPChroma = new int[mbWidth];
        this.chromaFormat = sh.sps.chromaFormatIdc;
        this.transform8x8 = sh.pps.extended == null ? false : sh.pps.extended.transform8x8ModeFlag;
        if (sh.numRefIdxActiveOverrideFlag) {
            this.numRef = new int[] { sh.numRefIdxActiveMinus1[0] + 1, sh.numRefIdxActiveMinus1[1] + 1 };
        } else {
            this.numRef = new int[] { sh.pps.numRefIdxActiveMinus1[0] + 1, sh.pps.numRefIdxActiveMinus1[1] + 1 };
        }
        this.tf8x8Top = new boolean[mbWidth];
        this.predModeLeft = new H264Const.PartPred[2];
        this.predModeTop = new H264Const.PartPred[mbWidth << 1];
        this.i4x4PredLeft = new int[4];
        this.i4x4PredTop = new int[mbWidth << 2];
    }

    public boolean readMacroblock(MBlock mBlock) {
        int mbWidth = this.sh.sps.picWidthInMbsMinus1 + 1;
        int mbHeight = this.sh.sps.picHeightInMapUnitsMinus1 + 1;
        if ((!this.endOfData || this.mbSkipRun != 0) && this.mbIdx < mbWidth * mbHeight) {
            mBlock.mbIdx = this.mbIdx;
            mBlock.prevMbType = this.prevMBType;
            boolean mbaffFrameFlag = this.sh.sps.mbAdaptiveFrameFieldFlag && !this.sh.fieldPicFlag;
            if (this.sh.sliceType.isInter() && !this.activePps.entropyCodingModeFlag) {
                if (!this.prevMbSkipped && this.mbSkipRun == 0) {
                    this.mbSkipRun = CAVLCReader.readUEtrace(this.reader, "mb_skip_run");
                    if (!CAVLCReader.moreRBSPData(this.reader)) {
                        this.endOfData = true;
                    }
                }
                if (this.mbSkipRun > 0) {
                    this.mbSkipRun--;
                    int mbAddr = this.mapper.getAddress(this.mbIdx);
                    this.prevMbSkipped = true;
                    this.prevMBType = null;
                    MBlockDecoderUtils.debugPrint("---------------------- MB (%d,%d) ---------------------", mbAddr % mbWidth, mbAddr / mbWidth);
                    mBlock.skipped = true;
                    int mbX = this.mapper.getMbX(mBlock.mbIdx);
                    this.topMBType[mbX] = this.leftMBType = null;
                    int blk8x8X = mbX << 1;
                    this.predModeLeft[0] = this.predModeLeft[1] = this.predModeTop[blk8x8X] = this.predModeTop[blk8x8X + 1] = H264Const.PartPred.L0;
                    this.mbIdx++;
                    return true;
                }
                this.prevMbSkipped = false;
            }
            int mbAddr = this.mapper.getAddress(this.mbIdx);
            int mbX = mbAddr % mbWidth;
            int mbY = mbAddr / mbWidth;
            MBlockDecoderUtils.debugPrint("---------------------- MB (%d,%d) ---------------------", mbX, mbY);
            if (!this.sh.sliceType.isIntra() && this.activePps.entropyCodingModeFlag && this.readMBSkipFlag(this.sh.sliceType, this.mapper.leftAvailable(this.mbIdx), this.mapper.topAvailable(this.mbIdx), mbX)) {
                this.prevMBType = null;
                this.prevMbSkipped = true;
                mBlock.skipped = true;
                int blk8x8X = mbX << 1;
                this.predModeLeft[0] = this.predModeLeft[1] = this.predModeTop[blk8x8X] = this.predModeTop[blk8x8X + 1] = H264Const.PartPred.L0;
            } else {
                boolean mb_field_decoding_flag = false;
                if (mbaffFrameFlag && (this.mbIdx % 2 == 0 || this.mbIdx % 2 == 1 && this.prevMbSkipped)) {
                    mb_field_decoding_flag = CAVLCReader.readBool(this.reader, "mb_field_decoding_flag");
                }
                mBlock.fieldDecoding = mb_field_decoding_flag;
                this.readMBlock(mBlock, this.sh.sliceType);
                this.prevMBType = mBlock.curMbType;
            }
            this.endOfData = this.activePps.entropyCodingModeFlag && this.mDecoder.decodeFinalBin() == 1 || !this.activePps.entropyCodingModeFlag && !CAVLCReader.moreRBSPData(this.reader);
            this.mbIdx++;
            this.topMBType[this.mapper.getMbX(mBlock.mbIdx)] = this.leftMBType = mBlock.curMbType;
            return true;
        } else {
            return false;
        }
    }

    int readMBQpDelta(MBType prevMbType) {
        int mbQPDelta;
        if (!this.activePps.entropyCodingModeFlag) {
            mbQPDelta = CAVLCReader.readSE(this.reader, "mb_qp_delta");
        } else {
            mbQPDelta = this.cabac.readMBQpDelta(this.mDecoder, prevMbType);
        }
        return mbQPDelta;
    }

    int readChromaPredMode(int mbX, boolean leftAvailable, boolean topAvailable) {
        int chromaPredictionMode;
        if (!this.activePps.entropyCodingModeFlag) {
            chromaPredictionMode = CAVLCReader.readUEtrace(this.reader, "MBP: intra_chroma_pred_mode");
        } else {
            chromaPredictionMode = this.cabac.readIntraChromaPredMode(this.mDecoder, mbX, this.leftMBType, this.topMBType[mbX], leftAvailable, topAvailable);
        }
        return chromaPredictionMode;
    }

    boolean readTransform8x8Flag(boolean leftAvailable, boolean topAvailable, MBType leftType, MBType topType, boolean is8x8Left, boolean is8x8Top) {
        return !this.activePps.entropyCodingModeFlag ? CAVLCReader.readBool(this.reader, "transform_size_8x8_flag") : this.cabac.readTransform8x8Flag(this.mDecoder, leftAvailable, topAvailable, leftType, topType, is8x8Left, is8x8Top);
    }

    protected int readCodedBlockPatternIntra(boolean leftAvailable, boolean topAvailable, int leftCBP, int topCBP, MBType leftMB, MBType topMB) {
        return !this.activePps.entropyCodingModeFlag ? H264Const.CODED_BLOCK_PATTERN_INTRA_COLOR[CAVLCReader.readUEtrace(this.reader, "coded_block_pattern")] : this.cabac.codedBlockPatternIntra(this.mDecoder, leftAvailable, topAvailable, leftCBP, topCBP, leftMB, topMB);
    }

    protected int readCodedBlockPatternInter(boolean leftAvailable, boolean topAvailable, int leftCBP, int topCBP, MBType leftMB, MBType topMB) {
        if (!this.activePps.entropyCodingModeFlag) {
            int code = CAVLCReader.readUEtrace(this.reader, "coded_block_pattern");
            return H264Const.CODED_BLOCK_PATTERN_INTER_COLOR[code];
        } else {
            return this.cabac.codedBlockPatternIntra(this.mDecoder, leftAvailable, topAvailable, leftCBP, topCBP, leftMB, topMB);
        }
    }

    int readRefIdx(boolean leftAvailable, boolean topAvailable, MBType leftType, MBType topType, H264Const.PartPred leftPred, H264Const.PartPred topPred, H264Const.PartPred curPred, int mbX, int partX, int partY, int partW, int partH, int list) {
        return !this.activePps.entropyCodingModeFlag ? CAVLCReader.readTE(this.reader, this.numRef[list] - 1) : this.cabac.readRefIdx(this.mDecoder, leftAvailable, topAvailable, leftType, topType, leftPred, topPred, curPred, mbX, partX, partY, partW, partH, list);
    }

    int readMVD(int comp, boolean leftAvailable, boolean topAvailable, MBType leftType, MBType topType, H264Const.PartPred leftPred, H264Const.PartPred topPred, H264Const.PartPred curPred, int mbX, int partX, int partY, int partW, int partH, int list) {
        return !this.activePps.entropyCodingModeFlag ? CAVLCReader.readSE(this.reader, "mvd_l0_x") : this.cabac.readMVD(this.mDecoder, comp, leftAvailable, topAvailable, leftType, topType, leftPred, topPred, curPred, mbX, partX, partY, partW, partH, list);
    }

    int readPredictionI4x4Block(boolean leftAvailable, boolean topAvailable, MBType leftMBType, MBType topMBType, int blkX, int blkY, int mbX) {
        int mode = 2;
        if ((leftAvailable || blkX > 0) && (topAvailable || blkY > 0)) {
            int predModeB = topMBType != MBType.I_NxN && blkY <= 0 ? 2 : this.i4x4PredTop[(mbX << 2) + blkX];
            int predModeA = leftMBType != MBType.I_NxN && blkX <= 0 ? 2 : this.i4x4PredLeft[blkY];
            mode = Math.min(predModeB, predModeA);
        }
        if (!this.prev4x4PredMode()) {
            int rem_intra4x4_pred_mode = this.rem4x4PredMode();
            mode = rem_intra4x4_pred_mode + (rem_intra4x4_pred_mode < mode ? 0 : 1);
        }
        this.i4x4PredTop[(mbX << 2) + blkX] = this.i4x4PredLeft[blkY] = mode;
        return mode;
    }

    int rem4x4PredMode() {
        return !this.activePps.entropyCodingModeFlag ? CAVLCReader.readNBit(this.reader, 3, "MB: rem_intra4x4_pred_mode") : this.cabac.rem4x4PredMode(this.mDecoder);
    }

    boolean prev4x4PredMode() {
        return !this.activePps.entropyCodingModeFlag ? CAVLCReader.readBool(this.reader, "MBP: prev_intra4x4_pred_mode_flag") : this.cabac.prev4x4PredModeFlag(this.mDecoder);
    }

    void read16x16DC(boolean leftAvailable, boolean topAvailable, int mbX, int[] dc) {
        if (!this.activePps.entropyCodingModeFlag) {
            this.cavlc[0].readLumaDCBlock(this.reader, dc, mbX, leftAvailable, this.leftMBType, topAvailable, this.topMBType[mbX], CoeffTransformer.zigzag4x4);
        } else if (this.cabac.readCodedBlockFlagLumaDC(this.mDecoder, mbX, this.leftMBType, this.topMBType[mbX], leftAvailable, topAvailable, MBType.I_16x16) == 1) {
            this.cabac.readCoeffs(this.mDecoder, CABAC.BlockType.LUMA_16_DC, dc, 0, 16, CoeffTransformer.zigzag4x4, H264Const.identityMapping16, H264Const.identityMapping16);
        }
    }

    int read16x16AC(boolean leftAvailable, boolean topAvailable, int mbX, int cbpLuma, int[] ac, int blkOffLeft, int blkOffTop, int blkX, int blkY) {
        if (this.activePps.entropyCodingModeFlag) {
            return this.cabac.readCodedBlockFlagLumaAC(this.mDecoder, CABAC.BlockType.LUMA_15_AC, blkX, blkOffTop, 0, this.leftMBType, this.topMBType[mbX], leftAvailable, topAvailable, this.leftCBPLuma, this.topCBPLuma[mbX], cbpLuma, MBType.I_16x16) == 1 ? this.cabac.readCoeffs(this.mDecoder, CABAC.BlockType.LUMA_15_AC, ac, 1, 15, CoeffTransformer.zigzag4x4, H264Const.identityMapping16, H264Const.identityMapping16) : 0;
        } else {
            return this.cavlc[0].readACBlock(this.reader, ac, blkX, blkOffTop, blkOffLeft != 0 || leftAvailable, blkOffLeft == 0 ? this.leftMBType : MBType.I_16x16, blkOffTop != 0 || topAvailable, blkOffTop == 0 ? this.topMBType[mbX] : MBType.I_16x16, 1, 15, CoeffTransformer.zigzag4x4);
        }
    }

    int readResidualAC(boolean leftAvailable, boolean topAvailable, int mbX, MBType curMbType, int cbpLuma, int blkOffLeft, int blkOffTop, int blkX, int blkY, int[] ac) {
        if (!this.activePps.entropyCodingModeFlag) {
            return this.reader.remaining() <= 0 ? 0 : this.cavlc[0].readACBlock(this.reader, ac, blkX, blkOffTop, blkOffLeft != 0 || leftAvailable, blkOffLeft == 0 ? this.leftMBType : curMbType, blkOffTop != 0 || topAvailable, blkOffTop == 0 ? this.topMBType[mbX] : curMbType, 0, 16, CoeffTransformer.zigzag4x4);
        } else {
            return this.cabac.readCodedBlockFlagLumaAC(this.mDecoder, CABAC.BlockType.LUMA_16, blkX, blkOffTop, 0, this.leftMBType, this.topMBType[mbX], leftAvailable, topAvailable, this.leftCBPLuma, this.topCBPLuma[mbX], cbpLuma, curMbType) == 1 ? this.cabac.readCoeffs(this.mDecoder, CABAC.BlockType.LUMA_16, ac, 0, 16, CoeffTransformer.zigzag4x4, H264Const.identityMapping16, H264Const.identityMapping16) : 0;
        }
    }

    public void setZeroCoeff(int comp, int blkX, int blkOffTop) {
        this.cavlc[comp].setZeroCoeff(blkX, blkOffTop);
    }

    public void savePrevCBP(int codedBlockPattern) {
        if (this.activePps.entropyCodingModeFlag) {
            this.cabac.setPrevCBP(codedBlockPattern);
        }
    }

    public int readLumaAC(boolean leftAvailable, boolean topAvailable, int mbX, MBType curMbType, int blkX, int j, int[] ac16, int blkOffLeft, int blkOffTop) {
        return this.cavlc[0].readACBlock(this.reader, ac16, blkX + (j & 1), blkOffTop, blkOffLeft != 0 || leftAvailable, blkOffLeft == 0 ? this.leftMBType : curMbType, blkOffTop != 0 || topAvailable, blkOffTop == 0 ? this.topMBType[mbX] : curMbType, 0, 16, H264Const.identityMapping16);
    }

    public int readLumaAC8x8(int blkX, int blkY, int[] ac) {
        int readCoeffs = this.cabac.readCoeffs(this.mDecoder, CABAC.BlockType.LUMA_64, ac, 0, 64, CoeffTransformer.zigzag8x8, H264Const.sig_coeff_map_8x8, H264Const.last_sig_coeff_map_8x8);
        this.cabac.setCodedBlock(blkX, blkY);
        this.cabac.setCodedBlock(blkX + 1, blkY);
        this.cabac.setCodedBlock(blkX, blkY + 1);
        this.cabac.setCodedBlock(blkX + 1, blkY + 1);
        return readCoeffs;
    }

    public int readSubMBTypeP() {
        return !this.activePps.entropyCodingModeFlag ? CAVLCReader.readUEtrace(this.reader, "SUB: sub_mb_type") : this.cabac.readSubMbTypeP(this.mDecoder);
    }

    public int readSubMBTypeB() {
        return !this.activePps.entropyCodingModeFlag ? CAVLCReader.readUEtrace(this.reader, "SUB: sub_mb_type") : this.cabac.readSubMbTypeB(this.mDecoder);
    }

    public void readChromaDC(int mbX, boolean leftAvailable, boolean topAvailable, int[] dc, int comp, MBType curMbType) {
        if (!this.activePps.entropyCodingModeFlag) {
            this.cavlc[comp].readChromaDCBlock(this.reader, dc, leftAvailable, topAvailable);
        } else if (this.cabac.readCodedBlockFlagChromaDC(this.mDecoder, mbX, comp, this.leftMBType, this.topMBType[mbX], leftAvailable, topAvailable, this.leftCBPChroma, this.topCBPChroma[mbX], curMbType) == 1) {
            this.cabac.readCoeffs(this.mDecoder, CABAC.BlockType.CHROMA_DC, dc, 0, 4, H264Const.identityMapping16, H264Const.identityMapping16, H264Const.identityMapping16);
        }
    }

    public void readChromaAC(boolean leftAvailable, boolean topAvailable, int mbX, int comp, MBType curMbType, int[] ac, int blkOffLeft, int blkOffTop, int blkX) {
        if (!this.activePps.entropyCodingModeFlag) {
            if (this.reader.remaining() <= 0) {
                return;
            }
            this.cavlc[comp].readACBlock(this.reader, ac, blkX, blkOffTop, blkOffLeft != 0 || leftAvailable, blkOffLeft == 0 ? this.leftMBType : curMbType, blkOffTop != 0 || topAvailable, blkOffTop == 0 ? this.topMBType[mbX] : curMbType, 1, 15, CoeffTransformer.zigzag4x4);
        } else if (this.cabac.readCodedBlockFlagChromaAC(this.mDecoder, blkX, blkOffTop, comp, this.leftMBType, this.topMBType[mbX], leftAvailable, topAvailable, this.leftCBPChroma, this.topCBPChroma[mbX], curMbType) == 1) {
            this.cabac.readCoeffs(this.mDecoder, CABAC.BlockType.CHROMA_AC, ac, 1, 15, CoeffTransformer.zigzag4x4, H264Const.identityMapping16, H264Const.identityMapping16);
        }
    }

    public int decodeMBTypeI(int mbIdx, boolean leftAvailable, boolean topAvailable, MBType leftMBType, MBType topMBType) {
        int mbType;
        if (!this.activePps.entropyCodingModeFlag) {
            mbType = CAVLCReader.readUEtrace(this.reader, "MB: mb_type");
        } else {
            mbType = this.cabac.readMBTypeI(this.mDecoder, leftMBType, topMBType, leftAvailable, topAvailable);
        }
        return mbType;
    }

    public int readMBTypeP() {
        int mbType;
        if (!this.activePps.entropyCodingModeFlag) {
            mbType = CAVLCReader.readUEtrace(this.reader, "MB: mb_type");
        } else {
            mbType = this.cabac.readMBTypeP(this.mDecoder);
        }
        return mbType;
    }

    public int readMBTypeB(int mbIdx, boolean leftAvailable, boolean topAvailable, MBType leftMBType, MBType topMBType) {
        int mbType;
        if (!this.activePps.entropyCodingModeFlag) {
            mbType = CAVLCReader.readUEtrace(this.reader, "MB: mb_type");
        } else {
            mbType = this.cabac.readMBTypeB(this.mDecoder, leftMBType, topMBType, leftAvailable, topAvailable);
        }
        return mbType;
    }

    public boolean readMBSkipFlag(SliceType slType, boolean leftAvailable, boolean topAvailable, int mbX) {
        return this.cabac.readMBSkipFlag(this.mDecoder, slType, leftAvailable, topAvailable, mbX);
    }

    public void readIntra16x16(int mbType, MBlock mBlock) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        mBlock.cbp(mbType / 12 * 15, mbType / 4 % 3);
        mBlock.luma16x16Mode = mbType % 4;
        mBlock.chromaPredictionMode = this.readChromaPredMode(mbX, leftAvailable, topAvailable);
        mBlock.mbQPDelta = this.readMBQpDelta(mBlock.prevMbType);
        this.read16x16DC(leftAvailable, topAvailable, mbX, mBlock.dc);
        for (int i = 0; i < 16; i++) {
            int blkOffLeft = H264Const.MB_BLK_OFF_LEFT[i];
            int blkOffTop = H264Const.MB_BLK_OFF_TOP[i];
            int blkX = (mbX << 2) + blkOffLeft;
            int blkY = (mbY << 2) + blkOffTop;
            if ((mBlock.cbpLuma() & 1 << (i >> 2)) != 0) {
                mBlock.nCoeff[i] = this.read16x16AC(leftAvailable, topAvailable, mbX, mBlock.cbpLuma(), mBlock.ac[0][i], blkOffLeft, blkOffTop, blkX, blkY);
            } else if (!this.sh.pps.entropyCodingModeFlag) {
                this.setZeroCoeff(0, blkX, blkOffTop);
            }
        }
        if (this.chromaFormat != ColorSpace.MONO) {
            this.readChromaResidual(mBlock, leftAvailable, topAvailable, mbX);
        }
    }

    public void readMBlockBDirect(MBlock mBlock) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean lAvb = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean tAvb = this.mapper.topAvailable(mBlock.mbIdx);
        mBlock._cbp = this.readCodedBlockPatternInter(lAvb, tAvb, this.leftCBPLuma | this.leftCBPChroma << 4, this.topCBPLuma[mbX] | this.topCBPChroma[mbX] << 4, this.leftMBType, this.topMBType[mbX]);
        mBlock.transform8x8Used = false;
        if (this.transform8x8 && mBlock.cbpLuma() != 0 && this.sh.sps.direct8x8InferenceFlag) {
            mBlock.transform8x8Used = this.readTransform8x8Flag(lAvb, tAvb, this.leftMBType, this.topMBType[mbX], this.tf8x8Left, this.tf8x8Top[mbX]);
        }
        if (mBlock.cbpLuma() > 0 || mBlock.cbpChroma() > 0) {
            mBlock.mbQPDelta = this.readMBQpDelta(mBlock.prevMbType);
        }
        this.readResidualLuma(mBlock, lAvb, tAvb, mbX, mbY);
        this.readChromaResidual(mBlock, lAvb, tAvb, mbX);
        this.predModeTop[mbX << 1] = this.predModeTop[(mbX << 1) + 1] = this.predModeLeft[0] = this.predModeLeft[1] = H264Const.PartPred.Direct;
    }

    public void readInter16x16(H264Const.PartPred p0, MBlock mBlock) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        for (int list = 0; list < 2; list++) {
            if (H264Const.usesList(p0, list) && this.numRef[list] > 1) {
                mBlock.pb16x16.refIdx[list] = this.readRefIdx(leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.predModeLeft[0], this.predModeTop[mbX << 1], p0, mbX, 0, 0, 4, 4, list);
            }
        }
        for (int listx = 0; listx < 2; listx++) {
            this.readPredictionInter16x16(mBlock, mbX, leftAvailable, topAvailable, listx, p0);
        }
        this.readResidualInter(mBlock, leftAvailable, topAvailable, mbX, mbY);
        this.predModeLeft[0] = this.predModeLeft[1] = this.predModeTop[mbX << 1] = this.predModeTop[(mbX << 1) + 1] = p0;
    }

    private void readPredInter8x16(MBlock mBlock, int mbX, boolean leftAvailable, boolean topAvailable, int list, H264Const.PartPred p0, H264Const.PartPred p1) {
        int blk8x8X = mbX << 1;
        if (H264Const.usesList(p0, list)) {
            mBlock.pb168x168.mvdX1[list] = this.readMVD(0, leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.predModeLeft[0], this.predModeTop[blk8x8X], p0, mbX, 0, 0, 2, 4, list);
            mBlock.pb168x168.mvdY1[list] = this.readMVD(1, leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.predModeLeft[0], this.predModeTop[blk8x8X], p0, mbX, 0, 0, 2, 4, list);
        }
        if (H264Const.usesList(p1, list)) {
            mBlock.pb168x168.mvdX2[list] = this.readMVD(0, true, topAvailable, MBType.P_8x16, this.topMBType[mbX], p0, this.predModeTop[blk8x8X + 1], p1, mbX, 2, 0, 2, 4, list);
            mBlock.pb168x168.mvdY2[list] = this.readMVD(1, true, topAvailable, MBType.P_8x16, this.topMBType[mbX], p0, this.predModeTop[blk8x8X + 1], p1, mbX, 2, 0, 2, 4, list);
        }
    }

    private void readPredictionInter16x8(MBlock mBlock, int mbX, boolean leftAvailable, boolean topAvailable, H264Const.PartPred p0, H264Const.PartPred p1, int list) {
        int blk8x8X = mbX << 1;
        if (H264Const.usesList(p0, list)) {
            mBlock.pb168x168.mvdX1[list] = this.readMVD(0, leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.predModeLeft[0], this.predModeTop[blk8x8X], p0, mbX, 0, 0, 4, 2, list);
            mBlock.pb168x168.mvdY1[list] = this.readMVD(1, leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.predModeLeft[0], this.predModeTop[blk8x8X], p0, mbX, 0, 0, 4, 2, list);
        }
        if (H264Const.usesList(p1, list)) {
            mBlock.pb168x168.mvdX2[list] = this.readMVD(0, leftAvailable, true, this.leftMBType, MBType.P_16x8, this.predModeLeft[1], p0, p1, mbX, 0, 2, 4, 2, list);
            mBlock.pb168x168.mvdY2[list] = this.readMVD(1, leftAvailable, true, this.leftMBType, MBType.P_16x8, this.predModeLeft[1], p0, p1, mbX, 0, 2, 4, 2, list);
        }
    }

    public void readInter16x8(H264Const.PartPred p0, H264Const.PartPred p1, MBlock mBlock) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        for (int list = 0; list < 2; list++) {
            if (H264Const.usesList(p0, list) && this.numRef[list] > 1) {
                mBlock.pb168x168.refIdx1[list] = this.readRefIdx(leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.predModeLeft[0], this.predModeTop[mbX << 1], p0, mbX, 0, 0, 4, 2, list);
            }
            if (H264Const.usesList(p1, list) && this.numRef[list] > 1) {
                mBlock.pb168x168.refIdx2[list] = this.readRefIdx(leftAvailable, true, this.leftMBType, mBlock.curMbType, this.predModeLeft[1], p0, p1, mbX, 0, 2, 4, 2, list);
            }
        }
        for (int list = 0; list < 2; list++) {
            this.readPredictionInter16x8(mBlock, mbX, leftAvailable, topAvailable, p0, p1, list);
        }
        this.readResidualInter(mBlock, leftAvailable, topAvailable, mbX, mbY);
        this.predModeLeft[0] = p0;
        this.predModeLeft[1] = this.predModeTop[mbX << 1] = this.predModeTop[(mbX << 1) + 1] = p1;
    }

    public void readIntra8x16(H264Const.PartPred p0, H264Const.PartPred p1, MBlock mBlock) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        for (int list = 0; list < 2; list++) {
            if (H264Const.usesList(p0, list) && this.numRef[list] > 1) {
                mBlock.pb168x168.refIdx1[list] = this.readRefIdx(leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.predModeLeft[0], this.predModeTop[mbX << 1], p0, mbX, 0, 0, 2, 4, list);
            }
            if (H264Const.usesList(p1, list) && this.numRef[list] > 1) {
                mBlock.pb168x168.refIdx2[list] = this.readRefIdx(true, topAvailable, mBlock.curMbType, this.topMBType[mbX], p0, this.predModeTop[(mbX << 1) + 1], p1, mbX, 2, 0, 2, 4, list);
            }
        }
        for (int list = 0; list < 2; list++) {
            this.readPredInter8x16(mBlock, mbX, leftAvailable, topAvailable, list, p0, p1);
        }
        this.readResidualInter(mBlock, leftAvailable, topAvailable, mbX, mbY);
        this.predModeTop[mbX << 1] = p0;
        this.predModeTop[(mbX << 1) + 1] = this.predModeLeft[0] = this.predModeLeft[1] = p1;
    }

    private void readPredictionInter16x16(MBlock mBlock, int mbX, boolean leftAvailable, boolean topAvailable, int list, H264Const.PartPred curPred) {
        int blk8x8X = mbX << 1;
        if (H264Const.usesList(curPred, list)) {
            mBlock.pb16x16.mvdX[list] = this.readMVD(0, leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.predModeLeft[0], this.predModeTop[blk8x8X], curPred, mbX, 0, 0, 4, 4, list);
            mBlock.pb16x16.mvdY[list] = this.readMVD(1, leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.predModeLeft[0], this.predModeTop[blk8x8X], curPred, mbX, 0, 0, 4, 4, list);
        }
    }

    private void readResidualInter(MBlock mBlock, boolean leftAvailable, boolean topAvailable, int mbX, int mbY) {
        mBlock._cbp = this.readCodedBlockPatternInter(leftAvailable, topAvailable, this.leftCBPLuma | this.leftCBPChroma << 4, this.topCBPLuma[mbX] | this.topCBPChroma[mbX] << 4, this.leftMBType, this.topMBType[mbX]);
        mBlock.transform8x8Used = false;
        if (mBlock.cbpLuma() != 0 && this.transform8x8) {
            mBlock.transform8x8Used = this.readTransform8x8Flag(leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.tf8x8Left, this.tf8x8Top[mbX]);
        }
        if (mBlock.cbpLuma() > 0 || mBlock.cbpChroma() > 0) {
            mBlock.mbQPDelta = this.readMBQpDelta(mBlock.prevMbType);
        }
        this.readResidualLuma(mBlock, leftAvailable, topAvailable, mbX, mbY);
        if (this.chromaFormat != ColorSpace.MONO) {
            this.readChromaResidual(mBlock, leftAvailable, topAvailable, mbX);
        }
    }

    public void readMBlock8x8(MBlock mBlock) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        boolean noSubMBLessThen8x8;
        if (mBlock.curMbType != MBType.P_8x8 && mBlock.curMbType != MBType.P_8x8ref0) {
            this.readPrediction8x8B(mBlock, mbX, leftAvailable, topAvailable);
            noSubMBLessThen8x8 = H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[0]] == 0 && H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[1]] == 0 && H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[2]] == 0 && H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[3]] == 0;
        } else {
            this.readPrediction8x8P(mBlock, mbX, leftAvailable, topAvailable);
            noSubMBLessThen8x8 = mBlock.pb8x8.subMbTypes[0] == 0 && mBlock.pb8x8.subMbTypes[1] == 0 && mBlock.pb8x8.subMbTypes[2] == 0 && mBlock.pb8x8.subMbTypes[3] == 0;
        }
        mBlock._cbp = this.readCodedBlockPatternInter(leftAvailable, topAvailable, this.leftCBPLuma | this.leftCBPChroma << 4, this.topCBPLuma[mbX] | this.topCBPChroma[mbX] << 4, this.leftMBType, this.topMBType[mbX]);
        mBlock.transform8x8Used = false;
        if (this.transform8x8 && mBlock.cbpLuma() != 0 && noSubMBLessThen8x8) {
            mBlock.transform8x8Used = this.readTransform8x8Flag(leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.tf8x8Left, this.tf8x8Top[mbX]);
        }
        if (mBlock.cbpLuma() > 0 || mBlock.cbpChroma() > 0) {
            mBlock.mbQPDelta = this.readMBQpDelta(mBlock.prevMbType);
        }
        this.readResidualLuma(mBlock, leftAvailable, topAvailable, mbX, mbY);
        this.readChromaResidual(mBlock, leftAvailable, topAvailable, mbX);
    }

    private void readPrediction8x8P(MBlock mBlock, int mbX, boolean leftAvailable, boolean topAvailable) {
        for (int i = 0; i < 4; i++) {
            mBlock.pb8x8.subMbTypes[i] = this.readSubMBTypeP();
        }
        if (this.numRef[0] > 1 && mBlock.curMbType != MBType.P_8x8ref0) {
            mBlock.pb8x8.refIdx[0][0] = this.readRefIdx(leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], H264Const.PartPred.L0, H264Const.PartPred.L0, H264Const.PartPred.L0, mbX, 0, 0, 2, 2, 0);
            mBlock.pb8x8.refIdx[0][1] = this.readRefIdx(true, topAvailable, MBType.P_8x8, this.topMBType[mbX], H264Const.PartPred.L0, H264Const.PartPred.L0, H264Const.PartPred.L0, mbX, 2, 0, 2, 2, 0);
            mBlock.pb8x8.refIdx[0][2] = this.readRefIdx(leftAvailable, true, this.leftMBType, MBType.P_8x8, H264Const.PartPred.L0, H264Const.PartPred.L0, H264Const.PartPred.L0, mbX, 0, 2, 2, 2, 0);
            mBlock.pb8x8.refIdx[0][3] = this.readRefIdx(true, true, MBType.P_8x8, MBType.P_8x8, H264Const.PartPred.L0, H264Const.PartPred.L0, H264Const.PartPred.L0, mbX, 2, 2, 2, 2, 0);
        }
        this.readSubMb8x8(mBlock, 0, mBlock.pb8x8.subMbTypes[0], topAvailable, leftAvailable, 0, 0, mbX, this.leftMBType, this.topMBType[mbX], MBType.P_8x8, H264Const.PartPred.L0, H264Const.PartPred.L0, H264Const.PartPred.L0, 0);
        this.readSubMb8x8(mBlock, 1, mBlock.pb8x8.subMbTypes[1], topAvailable, true, 2, 0, mbX, MBType.P_8x8, this.topMBType[mbX], MBType.P_8x8, H264Const.PartPred.L0, H264Const.PartPred.L0, H264Const.PartPred.L0, 0);
        this.readSubMb8x8(mBlock, 2, mBlock.pb8x8.subMbTypes[2], true, leftAvailable, 0, 2, mbX, this.leftMBType, MBType.P_8x8, MBType.P_8x8, H264Const.PartPred.L0, H264Const.PartPred.L0, H264Const.PartPred.L0, 0);
        this.readSubMb8x8(mBlock, 3, mBlock.pb8x8.subMbTypes[3], true, true, 2, 2, mbX, MBType.P_8x8, MBType.P_8x8, MBType.P_8x8, H264Const.PartPred.L0, H264Const.PartPred.L0, H264Const.PartPred.L0, 0);
        int blk8x8X = mbX << 1;
        this.predModeLeft[0] = this.predModeLeft[1] = this.predModeTop[blk8x8X] = this.predModeTop[blk8x8X + 1] = H264Const.PartPred.L0;
    }

    private void readPrediction8x8B(MBlock mBlock, int mbX, boolean leftAvailable, boolean topAvailable) {
        H264Const.PartPred[] p = new H264Const.PartPred[4];
        for (int i = 0; i < 4; i++) {
            mBlock.pb8x8.subMbTypes[i] = this.readSubMBTypeB();
            p[i] = H264Const.bPartPredModes[mBlock.pb8x8.subMbTypes[i]];
        }
        for (int list = 0; list < 2; list++) {
            if (this.numRef[list] > 1) {
                if (H264Const.usesList(p[0], list)) {
                    mBlock.pb8x8.refIdx[list][0] = this.readRefIdx(leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.predModeLeft[0], this.predModeTop[mbX << 1], p[0], mbX, 0, 0, 2, 2, list);
                }
                if (H264Const.usesList(p[1], list)) {
                    mBlock.pb8x8.refIdx[list][1] = this.readRefIdx(true, topAvailable, MBType.B_8x8, this.topMBType[mbX], p[0], this.predModeTop[(mbX << 1) + 1], p[1], mbX, 2, 0, 2, 2, list);
                }
                if (H264Const.usesList(p[2], list)) {
                    mBlock.pb8x8.refIdx[list][2] = this.readRefIdx(leftAvailable, true, this.leftMBType, MBType.B_8x8, this.predModeLeft[1], p[0], p[2], mbX, 0, 2, 2, 2, list);
                }
                if (H264Const.usesList(p[3], list)) {
                    mBlock.pb8x8.refIdx[list][3] = this.readRefIdx(true, true, MBType.B_8x8, MBType.B_8x8, p[2], p[1], p[3], mbX, 2, 2, 2, 2, list);
                }
            }
        }
        MBlockDecoderUtils.debugPrint("Pred: " + p[0] + ", " + p[1] + ", " + p[2] + ", " + p[3]);
        int blk8x8X = mbX << 1;
        for (int listx = 0; listx < 2; listx++) {
            if (H264Const.usesList(p[0], listx)) {
                this.readSubMb8x8(mBlock, 0, H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[0]], topAvailable, leftAvailable, 0, 0, mbX, this.leftMBType, this.topMBType[mbX], MBType.B_8x8, this.predModeLeft[0], this.predModeTop[blk8x8X], p[0], listx);
            }
            if (H264Const.usesList(p[1], listx)) {
                this.readSubMb8x8(mBlock, 1, H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[1]], topAvailable, true, 2, 0, mbX, MBType.B_8x8, this.topMBType[mbX], MBType.B_8x8, p[0], this.predModeTop[blk8x8X + 1], p[1], listx);
            }
            if (H264Const.usesList(p[2], listx)) {
                this.readSubMb8x8(mBlock, 2, H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[2]], true, leftAvailable, 0, 2, mbX, this.leftMBType, MBType.B_8x8, MBType.B_8x8, this.predModeLeft[1], p[0], p[2], listx);
            }
            if (H264Const.usesList(p[3], listx)) {
                this.readSubMb8x8(mBlock, 3, H264Const.bSubMbTypes[mBlock.pb8x8.subMbTypes[3]], true, true, 2, 2, mbX, MBType.B_8x8, MBType.B_8x8, MBType.B_8x8, p[2], p[1], p[3], listx);
            }
        }
        this.predModeLeft[0] = p[1];
        this.predModeTop[blk8x8X] = p[2];
        this.predModeLeft[1] = this.predModeTop[blk8x8X + 1] = p[3];
    }

    private void readSubMb8x8(MBlock mBlock, int partNo, int subMbType, boolean tAvb, boolean lAvb, int blk8x8X, int blk8x8Y, int mbX, MBType leftMBType, MBType topMBType, MBType curMBType, H264Const.PartPred leftPred, H264Const.PartPred topPred, H264Const.PartPred partPred, int list) {
        switch(subMbType) {
            case 0:
                this.readSub8x8(mBlock, partNo, tAvb, lAvb, blk8x8X, blk8x8Y, mbX, leftMBType, topMBType, leftPred, topPred, partPred, list);
                break;
            case 1:
                this.readSub8x4(mBlock, partNo, tAvb, lAvb, blk8x8X, blk8x8Y, mbX, leftMBType, topMBType, curMBType, leftPred, topPred, partPred, list);
                break;
            case 2:
                this.readSub4x8(mBlock, partNo, tAvb, lAvb, blk8x8X, blk8x8Y, mbX, leftMBType, topMBType, curMBType, leftPred, topPred, partPred, list);
                break;
            case 3:
                this.readSub4x4(mBlock, partNo, tAvb, lAvb, blk8x8X, blk8x8Y, mbX, leftMBType, topMBType, curMBType, leftPred, topPred, partPred, list);
        }
    }

    private void readSub8x8(MBlock mBlock, int partNo, boolean tAvb, boolean lAvb, int blk8x8X, int blk8x8Y, int mbX, MBType leftMBType, MBType topMBType, H264Const.PartPred leftPred, H264Const.PartPred topPred, H264Const.PartPred partPred, int list) {
        mBlock.pb8x8.mvdX1[list][partNo] = this.readMVD(0, lAvb, tAvb, leftMBType, topMBType, leftPred, topPred, partPred, mbX, blk8x8X, blk8x8Y, 2, 2, list);
        mBlock.pb8x8.mvdY1[list][partNo] = this.readMVD(1, lAvb, tAvb, leftMBType, topMBType, leftPred, topPred, partPred, mbX, blk8x8X, blk8x8Y, 2, 2, list);
        MBlockDecoderUtils.debugPrint("mvd: (%d, %d)", mBlock.pb8x8.mvdX1[list][partNo], mBlock.pb8x8.mvdY1[list][partNo]);
    }

    private void readSub8x4(MBlock mBlock, int partNo, boolean tAvb, boolean lAvb, int blk8x8X, int blk8x8Y, int mbX, MBType leftMBType, MBType topMBType, MBType curMBType, H264Const.PartPred leftPred, H264Const.PartPred topPred, H264Const.PartPred partPred, int list) {
        mBlock.pb8x8.mvdX1[list][partNo] = this.readMVD(0, lAvb, tAvb, leftMBType, topMBType, leftPred, topPred, partPred, mbX, blk8x8X, blk8x8Y, 2, 1, list);
        mBlock.pb8x8.mvdY1[list][partNo] = this.readMVD(1, lAvb, tAvb, leftMBType, topMBType, leftPred, topPred, partPred, mbX, blk8x8X, blk8x8Y, 2, 1, list);
        mBlock.pb8x8.mvdX2[list][partNo] = this.readMVD(0, lAvb, true, leftMBType, curMBType, leftPred, partPred, partPred, mbX, blk8x8X, blk8x8Y + 1, 2, 1, list);
        mBlock.pb8x8.mvdY2[list][partNo] = this.readMVD(1, lAvb, true, leftMBType, curMBType, leftPred, partPred, partPred, mbX, blk8x8X, blk8x8Y + 1, 2, 1, list);
    }

    private void readSub4x8(MBlock mBlock, int partNo, boolean tAvb, boolean lAvb, int blk8x8X, int blk8x8Y, int mbX, MBType leftMBType, MBType topMBType, MBType curMBType, H264Const.PartPred leftPred, H264Const.PartPred topPred, H264Const.PartPred partPred, int list) {
        mBlock.pb8x8.mvdX1[list][partNo] = this.readMVD(0, lAvb, tAvb, leftMBType, topMBType, leftPred, topPred, partPred, mbX, blk8x8X, blk8x8Y, 1, 2, list);
        mBlock.pb8x8.mvdY1[list][partNo] = this.readMVD(1, lAvb, tAvb, leftMBType, topMBType, leftPred, topPred, partPred, mbX, blk8x8X, blk8x8Y, 1, 2, list);
        mBlock.pb8x8.mvdX2[list][partNo] = this.readMVD(0, true, tAvb, curMBType, topMBType, partPred, topPred, partPred, mbX, blk8x8X + 1, blk8x8Y, 1, 2, list);
        mBlock.pb8x8.mvdY2[list][partNo] = this.readMVD(1, true, tAvb, curMBType, topMBType, partPred, topPred, partPred, mbX, blk8x8X + 1, blk8x8Y, 1, 2, list);
    }

    private void readSub4x4(MBlock mBlock, int partNo, boolean tAvb, boolean lAvb, int blk8x8X, int blk8x8Y, int mbX, MBType leftMBType, MBType topMBType, MBType curMBType, H264Const.PartPred leftPred, H264Const.PartPred topPred, H264Const.PartPred partPred, int list) {
        mBlock.pb8x8.mvdX1[list][partNo] = this.readMVD(0, lAvb, tAvb, leftMBType, topMBType, leftPred, topPred, partPred, mbX, blk8x8X, blk8x8Y, 1, 1, list);
        mBlock.pb8x8.mvdY1[list][partNo] = this.readMVD(1, lAvb, tAvb, leftMBType, topMBType, leftPred, topPred, partPred, mbX, blk8x8X, blk8x8Y, 1, 1, list);
        mBlock.pb8x8.mvdX2[list][partNo] = this.readMVD(0, true, tAvb, curMBType, topMBType, partPred, topPred, partPred, mbX, blk8x8X + 1, blk8x8Y, 1, 1, list);
        mBlock.pb8x8.mvdY2[list][partNo] = this.readMVD(1, true, tAvb, curMBType, topMBType, partPred, topPred, partPred, mbX, blk8x8X + 1, blk8x8Y, 1, 1, list);
        mBlock.pb8x8.mvdX3[list][partNo] = this.readMVD(0, lAvb, true, leftMBType, curMBType, leftPred, partPred, partPred, mbX, blk8x8X, blk8x8Y + 1, 1, 1, list);
        mBlock.pb8x8.mvdY3[list][partNo] = this.readMVD(1, lAvb, true, leftMBType, curMBType, leftPred, partPred, partPred, mbX, blk8x8X, blk8x8Y + 1, 1, 1, list);
        mBlock.pb8x8.mvdX4[list][partNo] = this.readMVD(0, true, true, curMBType, curMBType, partPred, partPred, partPred, mbX, blk8x8X + 1, blk8x8Y + 1, 1, 1, list);
        mBlock.pb8x8.mvdY4[list][partNo] = this.readMVD(1, true, true, curMBType, curMBType, partPred, partPred, partPred, mbX, blk8x8X + 1, blk8x8Y + 1, 1, 1, list);
    }

    public void readIntraNxN(MBlock mBlock) {
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        int mbY = this.mapper.getMbY(mBlock.mbIdx);
        boolean leftAvailable = this.mapper.leftAvailable(mBlock.mbIdx);
        boolean topAvailable = this.mapper.topAvailable(mBlock.mbIdx);
        mBlock.transform8x8Used = false;
        if (this.transform8x8) {
            mBlock.transform8x8Used = this.readTransform8x8Flag(leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], this.tf8x8Left, this.tf8x8Top[mbX]);
        }
        if (!mBlock.transform8x8Used) {
            for (int i = 0; i < 16; i++) {
                int blkX = H264Const.MB_BLK_OFF_LEFT[i];
                int blkY = H264Const.MB_BLK_OFF_TOP[i];
                mBlock.lumaModes[i] = this.readPredictionI4x4Block(leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], blkX, blkY, mbX);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                int blkX = (i & 1) << 1;
                int blkY = i & 2;
                mBlock.lumaModes[i] = this.readPredictionI4x4Block(leftAvailable, topAvailable, this.leftMBType, this.topMBType[mbX], blkX, blkY, mbX);
                this.i4x4PredLeft[blkY + 1] = this.i4x4PredLeft[blkY];
                this.i4x4PredTop[(mbX << 2) + blkX + 1] = this.i4x4PredTop[(mbX << 2) + blkX];
            }
        }
        mBlock.chromaPredictionMode = this.readChromaPredMode(mbX, leftAvailable, topAvailable);
        mBlock._cbp = this.readCodedBlockPatternIntra(leftAvailable, topAvailable, this.leftCBPLuma | this.leftCBPChroma << 4, this.topCBPLuma[mbX] | this.topCBPChroma[mbX] << 4, this.leftMBType, this.topMBType[mbX]);
        if (mBlock.cbpLuma() > 0 || mBlock.cbpChroma() > 0) {
            mBlock.mbQPDelta = this.readMBQpDelta(mBlock.prevMbType);
        }
        this.readResidualLuma(mBlock, leftAvailable, topAvailable, mbX, mbY);
        if (this.chromaFormat != ColorSpace.MONO) {
            this.readChromaResidual(mBlock, leftAvailable, topAvailable, mbX);
        }
    }

    public void readResidualLuma(MBlock mBlock, boolean leftAvailable, boolean topAvailable, int mbX, int mbY) {
        if (!mBlock.transform8x8Used) {
            this.readLuma(mBlock, leftAvailable, topAvailable, mbX, mbY);
        } else if (this.sh.pps.entropyCodingModeFlag) {
            this.readLuma8x8CABAC(mBlock, mbX, mbY);
        } else {
            this.readLuma8x8CAVLC(mBlock, leftAvailable, topAvailable, mbX, mbY);
        }
    }

    private void readLuma(MBlock mBlock, boolean leftAvailable, boolean topAvailable, int mbX, int mbY) {
        for (int i = 0; i < 16; i++) {
            int blkOffLeft = H264Const.MB_BLK_OFF_LEFT[i];
            int blkOffTop = H264Const.MB_BLK_OFF_TOP[i];
            int blkX = (mbX << 2) + blkOffLeft;
            int blkY = (mbY << 2) + blkOffTop;
            if ((mBlock.cbpLuma() & 1 << (i >> 2)) == 0) {
                if (!this.sh.pps.entropyCodingModeFlag) {
                    this.setZeroCoeff(0, blkX, blkOffTop);
                }
            } else {
                mBlock.nCoeff[i] = this.readResidualAC(leftAvailable, topAvailable, mbX, mBlock.curMbType, mBlock.cbpLuma(), blkOffLeft, blkOffTop, blkX, blkY, mBlock.ac[0][i]);
            }
        }
        this.savePrevCBP(mBlock._cbp);
    }

    private void readLuma8x8CABAC(MBlock mBlock, int mbX, int mbY) {
        for (int i = 0; i < 4; i++) {
            int blkOffLeft = (i & 1) << 1;
            int blkOffTop = i & 2;
            int blkX = (mbX << 2) + blkOffLeft;
            int blkY = (mbY << 2) + blkOffTop;
            if ((mBlock.cbpLuma() & 1 << i) != 0) {
                int nCoeff = this.readLumaAC8x8(blkX, blkY, mBlock.ac[0][i]);
                int blk4x4Offset = i << 2;
                mBlock.nCoeff[blk4x4Offset] = mBlock.nCoeff[blk4x4Offset + 1] = mBlock.nCoeff[blk4x4Offset + 2] = mBlock.nCoeff[blk4x4Offset + 3] = nCoeff;
            }
        }
        this.savePrevCBP(mBlock._cbp);
    }

    private void readLuma8x8CAVLC(MBlock mBlock, boolean leftAvailable, boolean topAvailable, int mbX, int mbY) {
        for (int i = 0; i < 4; i++) {
            int blk8x8OffLeft = (i & 1) << 1;
            int blk8x8OffTop = i & 2;
            int blkX = (mbX << 2) + blk8x8OffLeft;
            int blkY = (mbY << 2) + blk8x8OffTop;
            if ((mBlock.cbpLuma() & 1 << i) == 0) {
                this.setZeroCoeff(0, blkX, blk8x8OffTop);
                this.setZeroCoeff(0, blkX + 1, blk8x8OffTop);
                this.setZeroCoeff(0, blkX, blk8x8OffTop + 1);
                this.setZeroCoeff(0, blkX + 1, blk8x8OffTop + 1);
            } else {
                int coeffs = 0;
                for (int j = 0; j < 4; j++) {
                    int[] ac16 = new int[16];
                    int blkOffLeft = blk8x8OffLeft + (j & 1);
                    int blkOffTop = blk8x8OffTop + (j >> 1);
                    coeffs += this.readLumaAC(leftAvailable, topAvailable, mbX, mBlock.curMbType, blkX, j, ac16, blkOffLeft, blkOffTop);
                    for (int k = 0; k < 16; k++) {
                        mBlock.ac[0][i][CoeffTransformer.zigzag8x8[(k << 2) + j]] = ac16[k];
                    }
                }
                int blk4x4Offset = i << 2;
                mBlock.nCoeff[blk4x4Offset] = mBlock.nCoeff[blk4x4Offset + 1] = mBlock.nCoeff[blk4x4Offset + 2] = mBlock.nCoeff[blk4x4Offset + 3] = coeffs;
            }
        }
    }

    public void readChromaResidual(MBlock mBlock, boolean leftAvailable, boolean topAvailable, int mbX) {
        if (mBlock.cbpChroma() != 0) {
            if ((mBlock.cbpChroma() & 3) > 0) {
                this.readChromaDC(mbX, leftAvailable, topAvailable, mBlock.dc1, 1, mBlock.curMbType);
                this.readChromaDC(mbX, leftAvailable, topAvailable, mBlock.dc2, 2, mBlock.curMbType);
            }
            this._readChromaAC(leftAvailable, topAvailable, mbX, mBlock.dc1, 1, mBlock.curMbType, (mBlock.cbpChroma() & 2) > 0, mBlock.ac[1]);
            this._readChromaAC(leftAvailable, topAvailable, mbX, mBlock.dc2, 2, mBlock.curMbType, (mBlock.cbpChroma() & 2) > 0, mBlock.ac[2]);
        } else if (!this.sh.pps.entropyCodingModeFlag) {
            this.setZeroCoeff(1, mbX << 1, 0);
            this.setZeroCoeff(1, (mbX << 1) + 1, 1);
            this.setZeroCoeff(2, mbX << 1, 0);
            this.setZeroCoeff(2, (mbX << 1) + 1, 1);
        }
    }

    private void _readChromaAC(boolean leftAvailable, boolean topAvailable, int mbX, int[] dc, int comp, MBType curMbType, boolean codedAC, int[][] residualOut) {
        for (int i = 0; i < dc.length; i++) {
            int[] ac = residualOut[i];
            int blkOffLeft = H264Const.MB_BLK_OFF_LEFT[i];
            int blkOffTop = H264Const.MB_BLK_OFF_TOP[i];
            int blkX = (mbX << 1) + blkOffLeft;
            if (codedAC) {
                this.readChromaAC(leftAvailable, topAvailable, mbX, comp, curMbType, ac, blkOffLeft, blkOffTop, blkX);
            } else if (!this.sh.pps.entropyCodingModeFlag) {
                this.setZeroCoeff(comp, blkX, blkOffTop);
            }
        }
    }

    private void readIPCM(MBlock mBlock) {
        this.reader.align();
        for (int i = 0; i < 256; i++) {
            mBlock.ipcm.samplesLuma[i] = this.reader.readNBit(8);
        }
        int MbWidthC = 16 >> this.chromaFormat.compWidth[1];
        int MbHeightC = 16 >> this.chromaFormat.compHeight[1];
        for (int i = 0; i < 2 * MbWidthC * MbHeightC; i++) {
            mBlock.ipcm.samplesChroma[i] = this.reader.readNBit(8);
        }
    }

    public void readMBlock(MBlock mBlock, SliceType sliceType) {
        if (sliceType == SliceType.I) {
            this.readMBlockI(mBlock);
        } else if (sliceType == SliceType.P) {
            this.readMBlockP(mBlock);
        } else {
            this.readMBlockB(mBlock);
        }
        int mbX = this.mapper.getMbX(mBlock.mbIdx);
        this.topCBPLuma[mbX] = this.leftCBPLuma = mBlock.cbpLuma();
        this.topCBPChroma[mbX] = this.leftCBPChroma = mBlock.cbpChroma();
        this.tf8x8Left = this.tf8x8Top[mbX] = mBlock.transform8x8Used;
    }

    private void readMBlockI(MBlock mBlock) {
        mBlock.mbType = this.decodeMBTypeI(mBlock.mbIdx, this.mapper.leftAvailable(mBlock.mbIdx), this.mapper.topAvailable(mBlock.mbIdx), this.leftMBType, this.topMBType[this.mapper.getMbX(mBlock.mbIdx)]);
        this.readMBlockIInt(mBlock, mBlock.mbType);
    }

    private void readMBlockIInt(MBlock mBlock, int mbType) {
        if (mbType == 0) {
            mBlock.curMbType = MBType.I_NxN;
            this.readIntraNxN(mBlock);
        } else if (mbType >= 1 && mbType <= 24) {
            mBlock.curMbType = MBType.I_16x16;
            this.readIntra16x16(mbType - 1, mBlock);
        } else {
            Logger.warn("IPCM macroblock found. Not tested, may cause unpredictable behavior.");
            mBlock.curMbType = MBType.I_PCM;
            this.readIPCM(mBlock);
        }
    }

    private void readMBlockP(MBlock mBlock) {
        mBlock.mbType = this.readMBTypeP();
        switch(mBlock.mbType) {
            case 0:
                mBlock.curMbType = MBType.P_16x16;
                this.readInter16x16(H264Const.PartPred.L0, mBlock);
                break;
            case 1:
                mBlock.curMbType = MBType.P_16x8;
                this.readInter16x8(H264Const.PartPred.L0, H264Const.PartPred.L0, mBlock);
                break;
            case 2:
                mBlock.curMbType = MBType.P_8x16;
                this.readIntra8x16(H264Const.PartPred.L0, H264Const.PartPred.L0, mBlock);
                break;
            case 3:
                mBlock.curMbType = MBType.P_8x8;
                this.readMBlock8x8(mBlock);
                break;
            case 4:
                mBlock.curMbType = MBType.P_8x8ref0;
                this.readMBlock8x8(mBlock);
                break;
            default:
                this.readMBlockIInt(mBlock, mBlock.mbType - 5);
        }
    }

    private void readMBlockB(MBlock mBlock) {
        mBlock.mbType = this.readMBTypeB(mBlock.mbIdx, this.mapper.leftAvailable(mBlock.mbIdx), this.mapper.topAvailable(mBlock.mbIdx), this.leftMBType, this.topMBType[this.mapper.getMbX(mBlock.mbIdx)]);
        if (mBlock.mbType >= 23) {
            this.readMBlockIInt(mBlock, mBlock.mbType - 23);
        } else {
            mBlock.curMbType = H264Const.bMbTypes[mBlock.mbType];
            if (mBlock.mbType == 0) {
                this.readMBlockBDirect(mBlock);
            } else if (mBlock.mbType <= 3) {
                this.readInter16x16(H264Const.bPredModes[mBlock.mbType][0], mBlock);
            } else if (mBlock.mbType == 22) {
                this.readMBlock8x8(mBlock);
            } else if ((mBlock.mbType & 1) == 0) {
                this.readInter16x8(H264Const.bPredModes[mBlock.mbType][0], H264Const.bPredModes[mBlock.mbType][1], mBlock);
            } else {
                this.readIntra8x16(H264Const.bPredModes[mBlock.mbType][0], H264Const.bPredModes[mBlock.mbType][1], mBlock);
            }
        }
    }

    public SliceHeader getSliceHeader() {
        return this.sh;
    }

    public NALUnit getNALUnit() {
        return this.nalUnit;
    }
}