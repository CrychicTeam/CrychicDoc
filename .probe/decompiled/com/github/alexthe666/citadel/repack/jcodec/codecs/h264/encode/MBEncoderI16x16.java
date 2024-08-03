package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.CoeffTransformer;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.CAVLC;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.MBType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.write.CAVLCWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.ArrayUtil;
import com.github.alexthe666.citadel.repack.jcodec.common.SaveRestore;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class MBEncoderI16x16 implements SaveRestore {

    private CAVLC[] cavlc;

    private byte[][] leftRow;

    private byte[][] topLine;

    private static int[] DUMMY = new int[16];

    public MBEncoderI16x16(CAVLC[] cavlc, byte[][] leftRow, byte[][] topLine) {
        this.cavlc = cavlc;
        this.leftRow = leftRow;
        this.topLine = topLine;
    }

    @Override
    public void save() {
        for (int i = 0; i < this.cavlc.length; i++) {
            this.cavlc[i].save();
        }
    }

    @Override
    public void restore() {
        for (int i = 0; i < this.cavlc.length; i++) {
            this.cavlc[i].restore();
        }
    }

    public void encodeMacroblock(Picture pic, int mbX, int mbY, BitWriter out, EncodedMB outMB, EncodedMB leftOutMB, EncodedMB topOutMB, int qp, int qpDelta) {
        CAVLCWriter.writeUE(out, 0);
        CAVLCWriter.writeSE(out, qpDelta);
        outMB.setType(MBType.I_16x16);
        outMB.setQp(qp);
        this.luma(pic, mbX, mbY, out, qp, outMB.getPixels(), this.cavlc[0]);
        this.chroma(pic, mbX, mbY, out, qp, outMB.getPixels());
        new MBDeblocker().deblockMBI(outMB, leftOutMB, topOutMB);
    }

    private void chroma(Picture pic, int mbX, int mbY, BitWriter out, int qp, Picture outMB) {
        int x = mbX << 3;
        int y = mbY << 3;
        int[][] ac1 = new int[4][16];
        int[][] ac2 = new int[4][16];
        byte[][] pred1 = new byte[4][16];
        byte[][] pred2 = new byte[4][16];
        this.predictChroma(pic, ac1, pred1, 1, x, y);
        this.predictChroma(pic, ac2, pred2, 2, x, y);
        chromaResidual(pic, mbX, mbY, out, qp, ac1, ac2, this.cavlc[1], this.cavlc[2], MBType.I_16x16, MBType.I_16x16);
        this.putChroma(outMB.getData()[1], 1, x, y, ac1, pred1);
        this.putChroma(outMB.getData()[2], 2, x, y, ac2, pred2);
    }

    public static void chromaResidual(Picture pic, int mbX, int mbY, BitWriter out, int qp, int[][] ac1, int[][] ac2, CAVLC cavlc1, CAVLC cavlc2, MBType leftMBType, MBType topMBType) {
        transformChroma(ac1);
        transformChroma(ac2);
        int[] dc1 = extractDC(ac1);
        int[] dc2 = extractDC(ac2);
        writeDC(cavlc1, mbX, mbY, out, qp, mbX << 1, mbY << 1, dc1, leftMBType, topMBType);
        writeDC(cavlc2, mbX, mbY, out, qp, mbX << 1, mbY << 1, dc2, leftMBType, topMBType);
        writeAC(cavlc1, mbX, mbY, out, mbX << 1, mbY << 1, ac1, qp, leftMBType, topMBType, DUMMY);
        writeAC(cavlc2, mbX, mbY, out, mbX << 1, mbY << 1, ac2, qp, leftMBType, topMBType, DUMMY);
        restorePlane(dc1, ac1, qp);
        restorePlane(dc2, ac2, qp);
    }

    private void luma(Picture pic, int mbX, int mbY, BitWriter out, int qp, Picture outMB, CAVLC cavlc) {
        int x = mbX << 4;
        int y = mbY << 4;
        int[][] ac = new int[16][16];
        byte[][] pred = new byte[16][16];
        this.lumaDCPred(x, y, pred);
        this.transform(pic, 0, ac, pred, x, y);
        int[] dc = extractDC(ac);
        writeDC(cavlc, mbX, mbY, out, qp, mbX << 2, mbY << 2, dc, MBType.I_16x16, MBType.I_16x16);
        writeAC(cavlc, mbX, mbY, out, mbX << 2, mbY << 2, ac, qp, MBType.I_16x16, MBType.I_16x16, DUMMY);
        restorePlane(dc, ac, qp);
        for (int blk = 0; blk < ac.length; blk++) {
            MBEncoderHelper.putBlk(outMB.getPlaneData(0), ac[blk], pred[blk], 4, H264Const.BLK_X[blk], H264Const.BLK_Y[blk], 4, 4);
        }
    }

    private void putChroma(byte[] mb, int comp, int x, int y, int[][] ac, byte[][] pred) {
        MBEncoderHelper.putBlk(mb, ac[0], pred[0], 3, 0, 0, 4, 4);
        MBEncoderHelper.putBlk(mb, ac[1], pred[1], 3, 4, 0, 4, 4);
        MBEncoderHelper.putBlk(mb, ac[2], pred[2], 3, 0, 4, 4, 4);
        MBEncoderHelper.putBlk(mb, ac[3], pred[3], 3, 4, 4, 4, 4);
    }

    private static void restorePlane(int[] dc, int[][] ac, int qp) {
        if (dc.length == 4) {
            CoeffTransformer.invDC2x2(dc);
            CoeffTransformer.dequantizeDC2x2(dc, qp, null);
        } else if (dc.length == 8) {
            CoeffTransformer.invDC4x2(dc);
            CoeffTransformer.dequantizeDC4x2(dc, qp);
        } else {
            CoeffTransformer.invDC4x4(dc);
            CoeffTransformer.dequantizeDC4x4(dc, qp, null);
            CoeffTransformer.reorderDC4x4(dc);
        }
        for (int i = 0; i < ac.length; i++) {
            CoeffTransformer.dequantizeAC(ac[i], qp, null);
            ac[i][0] = dc[i];
            CoeffTransformer.idct4x4(ac[i]);
        }
    }

    private static int[] extractDC(int[][] ac) {
        int[] dc = new int[ac.length];
        for (int i = 0; i < ac.length; i++) {
            dc[i] = ac[i][0];
            ac[i][0] = 0;
        }
        return dc;
    }

    private static void writeAC(CAVLC cavlc, int mbX, int mbY, BitWriter out, int mbLeftBlk, int mbTopBlk, int[][] ac, int qp, MBType leftMBType, MBType topMBType, int[] nc) {
        for (int i = 0; i < ac.length; i++) {
            CoeffTransformer.quantizeAC(ac[i], qp);
            nc[H264Const.BLK_INV_MAP[i]] = CAVLC.totalCoeff(cavlc.writeACBlock(out, mbLeftBlk + H264Const.MB_BLK_OFF_LEFT[i], mbTopBlk + H264Const.MB_BLK_OFF_TOP[i], leftMBType, topMBType, ac[i], H264Const.totalZeros16, 1, 15, CoeffTransformer.zigzag4x4));
        }
    }

    private static void writeDC(CAVLC cavlc, int mbX, int mbY, BitWriter out, int qp, int mbLeftBlk, int mbTopBlk, int[] dc, MBType leftMBType, MBType topMBType) {
        if (dc.length == 4) {
            CoeffTransformer.quantizeDC2x2(dc, qp);
            CoeffTransformer.fvdDC2x2(dc);
            cavlc.writeChrDCBlock(out, dc, H264Const.totalZeros4, 0, dc.length, new int[] { 0, 1, 2, 3 });
        } else if (dc.length == 8) {
            CoeffTransformer.quantizeDC4x2(dc, qp);
            CoeffTransformer.fvdDC4x2(dc);
            cavlc.writeChrDCBlock(out, dc, H264Const.totalZeros8, 0, dc.length, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 });
        } else {
            CoeffTransformer.reorderDC4x4(dc);
            CoeffTransformer.quantizeDC4x4(dc, qp);
            CoeffTransformer.fvdDC4x4(dc);
            cavlc.writeLumaDCBlock(out, mbLeftBlk, mbTopBlk, leftMBType, topMBType, dc, H264Const.totalZeros16, 0, 16, CoeffTransformer.zigzag4x4);
        }
    }

    private static void transformChroma(int[][] ac) {
        for (int i = 0; i < 4; i++) {
            CoeffTransformer.fdct4x4(ac[i]);
        }
    }

    private void predictChroma(Picture pic, int[][] ac, byte[][] pred, int comp, int x, int y) {
        this.chromaPredBlk0(comp, x, y, pred[0]);
        this.chromaPredBlk1(comp, x, y, pred[1]);
        this.chromaPredBlk2(comp, x, y, pred[2]);
        this.chromaPredBlk3(comp, x, y, pred[3]);
        MBEncoderHelper.takeSubtract(pic.getPlaneData(comp), pic.getPlaneWidth(comp), pic.getPlaneHeight(comp), x, y, ac[0], pred[0], 4, 4);
        MBEncoderHelper.takeSubtract(pic.getPlaneData(comp), pic.getPlaneWidth(comp), pic.getPlaneHeight(comp), x + 4, y, ac[1], pred[1], 4, 4);
        MBEncoderHelper.takeSubtract(pic.getPlaneData(comp), pic.getPlaneWidth(comp), pic.getPlaneHeight(comp), x, y + 4, ac[2], pred[2], 4, 4);
        MBEncoderHelper.takeSubtract(pic.getPlaneData(comp), pic.getPlaneWidth(comp), pic.getPlaneHeight(comp), x + 4, y + 4, ac[3], pred[3], 4, 4);
    }

    private final int chromaPredOne(byte[] pix, int x) {
        return pix[x] + pix[x + 1] + pix[x + 2] + pix[x + 3] + 2 >> 2;
    }

    private final int chromaPredTwo(byte[] pix1, byte[] pix2, int x, int y) {
        return pix1[x] + pix1[x + 1] + pix1[x + 2] + pix1[x + 3] + pix2[y] + pix2[y + 1] + pix2[y + 2] + pix2[y + 3] + 4 >> 3;
    }

    private void chromaPredBlk0(int comp, int x, int y, byte[] pred) {
        int predY = y & 7;
        int dc;
        if (x != 0 && y != 0) {
            dc = this.chromaPredTwo(this.leftRow[comp], this.topLine[comp], predY, x);
        } else if (x != 0) {
            dc = this.chromaPredOne(this.leftRow[comp], predY);
        } else if (y != 0) {
            dc = this.chromaPredOne(this.topLine[comp], x);
        } else {
            dc = 0;
        }
        for (int i = 0; i < pred.length; i++) {
            pred[i] = (byte) (pred[i] + dc);
        }
    }

    private void chromaPredBlk1(int comp, int x, int y, byte[] pred) {
        int predY = y & 7;
        int dc;
        if (y != 0) {
            dc = this.chromaPredOne(this.topLine[comp], x + 4);
        } else if (x != 0) {
            dc = this.chromaPredOne(this.leftRow[comp], predY);
        } else {
            dc = 0;
        }
        for (int i = 0; i < pred.length; i++) {
            pred[i] = (byte) (pred[i] + dc);
        }
    }

    private void chromaPredBlk2(int comp, int x, int y, byte[] pred) {
        int predY = y & 7;
        int dc;
        if (x != 0) {
            dc = this.chromaPredOne(this.leftRow[comp], predY + 4);
        } else if (y != 0) {
            dc = this.chromaPredOne(this.topLine[comp], x);
        } else {
            dc = 0;
        }
        for (int i = 0; i < pred.length; i++) {
            pred[i] = (byte) (pred[i] + dc);
        }
    }

    private void chromaPredBlk3(int comp, int x, int y, byte[] pred) {
        int predY = y & 7;
        int dc;
        if (x != 0 && y != 0) {
            dc = this.chromaPredTwo(this.leftRow[comp], this.topLine[comp], predY + 4, x + 4);
        } else if (x != 0) {
            dc = this.chromaPredOne(this.leftRow[comp], predY + 4);
        } else if (y != 0) {
            dc = this.chromaPredOne(this.topLine[comp], x + 4);
        } else {
            dc = 0;
        }
        for (int i = 0; i < pred.length; i++) {
            pred[i] = (byte) (pred[i] + dc);
        }
    }

    private void lumaDCPred(int x, int y, byte[][] pred) {
        int dc;
        if (x == 0 && y == 0) {
            dc = 0;
        } else if (y == 0) {
            dc = ArrayUtil.sumByte(this.leftRow[0]) + 8 >> 4;
        } else if (x == 0) {
            dc = ArrayUtil.sumByte3(this.topLine[0], x, 16) + 8 >> 4;
        } else {
            dc = ArrayUtil.sumByte(this.leftRow[0]) + ArrayUtil.sumByte3(this.topLine[0], x, 16) + 16 >> 5;
        }
        for (int i = 0; i < pred.length; i++) {
            for (int j = 0; j < pred[i].length; j++) {
                pred[i][j] = (byte) (pred[i][j] + dc);
            }
        }
    }

    private void transform(Picture pic, int comp, int[][] ac, byte[][] pred, int x, int y) {
        for (int i = 0; i < ac.length; i++) {
            int[] coeff = ac[i];
            MBEncoderHelper.takeSubtract(pic.getPlaneData(comp), pic.getPlaneWidth(comp), pic.getPlaneHeight(comp), x + H264Const.BLK_X[i], y + H264Const.BLK_Y[i], coeff, pred[i], 4, 4);
            CoeffTransformer.fdct4x4(coeff);
        }
    }

    public int getPredMode(Picture pic, int mbX, int mbY) {
        return 2;
    }

    public int getCbpChroma(Picture pic, int mbX, int mbY) {
        return 2;
    }

    public int getCbpLuma(Picture pic, int mbX, int mbY) {
        return 15;
    }
}