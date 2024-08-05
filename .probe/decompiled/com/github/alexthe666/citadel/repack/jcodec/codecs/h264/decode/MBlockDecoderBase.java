package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.Frame;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.MBType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.util.Arrays;

public class MBlockDecoderBase {

    protected DecoderState s;

    protected SliceHeader sh;

    protected DeblockerInput di;

    protected int poc;

    protected BlockInterpolator interpolator = new BlockInterpolator();

    protected Picture[] mbb;

    protected int[][] scalingMatrix;

    public MBlockDecoderBase(SliceHeader sh, DeblockerInput di, int poc, DecoderState decoderState) {
        this.s = decoderState;
        this.sh = sh;
        this.di = di;
        this.poc = poc;
        this.mbb = new Picture[] { Picture.create(16, 16, this.s.chromaFormat), Picture.create(16, 16, this.s.chromaFormat) };
        this.scalingMatrix = initScalingMatrix(sh);
    }

    void residualLuma(MBlock mBlock, boolean leftAvailable, boolean topAvailable, int mbX, int mbY) {
        if (!mBlock.transform8x8Used) {
            this.residualLuma4x4(mBlock);
        } else if (this.sh.pps.entropyCodingModeFlag) {
            this.residualLuma8x8CABAC(mBlock);
        } else {
            this.residualLuma8x8CAVLC(mBlock);
        }
    }

    private void residualLuma4x4(MBlock mBlock) {
        for (int i = 0; i < 16; i++) {
            if ((mBlock.cbpLuma() & 1 << (i >> 2)) != 0) {
                CoeffTransformer.dequantizeAC(mBlock.ac[0][i], this.s.qp, this.getScalingList(mBlock.curMbType.intra ? 0 : 3));
                CoeffTransformer.idct4x4(mBlock.ac[0][i]);
            }
        }
    }

    protected int[] getScalingList(int which) {
        return this.scalingMatrix == null ? null : this.scalingMatrix[which];
    }

    protected static int[][] initScalingMatrix(SliceHeader sh2) {
        if (sh2.sps.scalingMatrix != null || sh2.pps.extended != null && sh2.pps.extended.scalingMatrix != null) {
            int[][] merged = new int[][] { H264Const.defaultScalingList4x4Intra, null, null, H264Const.defaultScalingList4x4Inter, null, null, H264Const.defaultScalingList8x8Intra, H264Const.defaultScalingList8x8Inter, null, null, null, null };
            for (int i = 0; i < 8; i++) {
                if (sh2.sps.scalingMatrix != null && sh2.sps.scalingMatrix[i] != null) {
                    merged[i] = sh2.sps.scalingMatrix[i];
                }
                if (sh2.pps.extended != null && sh2.pps.extended.scalingMatrix != null && sh2.pps.extended.scalingMatrix[i] != null) {
                    merged[i] = sh2.pps.extended.scalingMatrix[i];
                }
            }
            if (merged[1] == null) {
                merged[1] = merged[0];
            }
            if (merged[2] == null) {
                merged[2] = merged[0];
            }
            if (merged[4] == null) {
                merged[4] = merged[3];
            }
            if (merged[5] == null) {
                merged[5] = merged[3];
            }
            if (merged[8] == null) {
                merged[8] = merged[6];
            }
            if (merged[10] == null) {
                merged[10] = merged[6];
            }
            if (merged[9] == null) {
                merged[9] = merged[7];
            }
            if (merged[11] == null) {
                merged[11] = merged[7];
            }
            return merged;
        } else {
            return (int[][]) null;
        }
    }

    private void residualLuma8x8CABAC(MBlock mBlock) {
        for (int i = 0; i < 4; i++) {
            if ((mBlock.cbpLuma() & 1 << i) != 0) {
                CoeffTransformer.dequantizeAC8x8(mBlock.ac[0][i], this.s.qp, this.getScalingList(mBlock.curMbType.intra ? 6 : 7));
                CoeffTransformer.idct8x8(mBlock.ac[0][i]);
            }
        }
    }

    private void residualLuma8x8CAVLC(MBlock mBlock) {
        for (int i = 0; i < 4; i++) {
            if ((mBlock.cbpLuma() & 1 << i) != 0) {
                CoeffTransformer.dequantizeAC8x8(mBlock.ac[0][i], this.s.qp, this.getScalingList(mBlock.curMbType.intra ? 6 : 7));
                CoeffTransformer.idct8x8(mBlock.ac[0][i]);
            }
        }
    }

    public void decodeChroma(MBlock mBlock, int mbX, int mbY, boolean leftAvailable, boolean topAvailable, Picture mb, int qp) {
        if (this.s.chromaFormat == ColorSpace.MONO) {
            Arrays.fill(mb.getPlaneData(1), (byte) 0);
            Arrays.fill(mb.getPlaneData(2), (byte) 0);
        } else {
            int qp1 = calcQpChroma(qp, this.s.chromaQpOffset[0]);
            int qp2 = calcQpChroma(qp, this.s.chromaQpOffset[1]);
            if (mBlock.cbpChroma() != 0) {
                this.decodeChromaResidual(mBlock, leftAvailable, topAvailable, mbX, mbY, qp1, qp2);
            }
            int addr = mbY * (this.sh.sps.picWidthInMbsMinus1 + 1) + mbX;
            this.di.mbQps[1][addr] = qp1;
            this.di.mbQps[2][addr] = qp2;
            ChromaPredictionBuilder.predictWithMode(mBlock.ac[1], mBlock.chromaPredictionMode, mbX, leftAvailable, topAvailable, this.s.leftRow[1], this.s.topLine[1], this.s.topLeft[1], mb.getPlaneData(1));
            ChromaPredictionBuilder.predictWithMode(mBlock.ac[2], mBlock.chromaPredictionMode, mbX, leftAvailable, topAvailable, this.s.leftRow[2], this.s.topLine[2], this.s.topLeft[2], mb.getPlaneData(2));
        }
    }

    void decodeChromaResidual(MBlock mBlock, boolean leftAvailable, boolean topAvailable, int mbX, int mbY, int crQp1, int crQp2) {
        if (mBlock.cbpChroma() != 0) {
            if ((mBlock.cbpChroma() & 3) > 0) {
                this.chromaDC(mbX, leftAvailable, topAvailable, mBlock.dc1, 1, crQp1, mBlock.curMbType);
                this.chromaDC(mbX, leftAvailable, topAvailable, mBlock.dc2, 2, crQp2, mBlock.curMbType);
            }
            this.chromaAC(leftAvailable, topAvailable, mbX, mbY, mBlock.dc1, 1, crQp1, mBlock.curMbType, (mBlock.cbpChroma() & 2) > 0, mBlock.ac[1]);
            this.chromaAC(leftAvailable, topAvailable, mbX, mbY, mBlock.dc2, 2, crQp2, mBlock.curMbType, (mBlock.cbpChroma() & 2) > 0, mBlock.ac[2]);
        }
    }

    private void chromaDC(int mbX, boolean leftAvailable, boolean topAvailable, int[] dc, int comp, int crQp, MBType curMbType) {
        CoeffTransformer.invDC2x2(dc);
        CoeffTransformer.dequantizeDC2x2(dc, crQp, this.getScalingList((curMbType.intra ? 6 : 7) + comp * 2));
    }

    private void chromaAC(boolean leftAvailable, boolean topAvailable, int mbX, int mbY, int[] dc, int comp, int crQp, MBType curMbType, boolean codedAC, int[][] residualOut) {
        for (int i = 0; i < dc.length; i++) {
            int[] ac = residualOut[i];
            if (codedAC) {
                CoeffTransformer.dequantizeAC(ac, crQp, this.getScalingList((curMbType.intra ? 0 : 3) + comp));
            }
            ac[0] = dc[i];
            CoeffTransformer.idct4x4(ac);
        }
    }

    static int calcQpChroma(int qp, int crQpOffset) {
        return H264Const.QP_SCALE_CR[MathUtil.clip(qp + crQpOffset, 0, 51)];
    }

    public void predictChromaInter(Frame[][] refs, H264Utils.MvList vectors, int x, int y, int comp, Picture mb, H264Const.PartPred[] predType) {
        for (int blk8x8 = 0; blk8x8 < 4; blk8x8++) {
            for (int list = 0; list < 2; list++) {
                if (H264Const.usesList(predType[blk8x8], list)) {
                    for (int blk4x4 = 0; blk4x4 < 4; blk4x4++) {
                        int i = H264Const.BLK_INV_MAP[(blk8x8 << 2) + blk4x4];
                        int mv = vectors.getMv(i, list);
                        Picture ref = refs[list][H264Utils.Mv.mvRef(mv)];
                        int blkPox = (i & 3) << 1;
                        int blkPoy = i >> 2 << 1;
                        int xx = (x + blkPox << 3) + H264Utils.Mv.mvX(mv);
                        int yy = (y + blkPoy << 3) + H264Utils.Mv.mvY(mv);
                        BlockInterpolator.getBlockChroma(ref.getPlaneData(comp), ref.getPlaneWidth(comp), ref.getPlaneHeight(comp), this.mbb[list].getPlaneData(comp), blkPoy * mb.getPlaneWidth(comp) + blkPox, mb.getPlaneWidth(comp), xx, yy, 2, 2);
                    }
                }
            }
            int blk4x4 = H264Const.BLK8x8_BLOCKS[blk8x8][0];
            PredictionMerger.mergePrediction(this.sh, vectors.mv0R(blk4x4), vectors.mv1R(blk4x4), predType[blk8x8], comp, this.mbb[0].getPlaneData(comp), this.mbb[1].getPlaneData(comp), H264Const.BLK_8x8_MB_OFF_CHROMA[blk8x8], mb.getPlaneWidth(comp), 4, 4, mb.getPlaneData(comp), refs, this.poc);
        }
    }
}