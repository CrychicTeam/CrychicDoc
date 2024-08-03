package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx;

import com.github.alexthe666.citadel.repack.jcodec.common.ArrayUtil;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoEncoder;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class VP8Encoder extends VideoEncoder {

    private VPXBitstream bitstream;

    private byte[][] leftRow;

    private byte[][] topLine;

    private VPXQuantizer quantizer;

    private int[] tmp;

    private RateControl rc;

    private ByteBuffer headerBuffer;

    private ByteBuffer dataBuffer;

    public static VP8Encoder createVP8Encoder(int qp) {
        return new VP8Encoder(new NopRateControl(qp));
    }

    public VP8Encoder(RateControl rc) {
        this.rc = rc;
        this.tmp = new int[16];
    }

    @Override
    public VideoEncoder.EncodedFrame encodeFrame(Picture pic, ByteBuffer _buf) {
        ByteBuffer out = _buf.duplicate();
        int mbWidth = pic.getWidth() + 15 >> 4;
        int mbHeight = pic.getHeight() + 15 >> 4;
        this.prepareBuffers(mbWidth, mbHeight);
        this.bitstream = new VPXBitstream(VPXConst.tokenDefaultBinProbs, mbWidth);
        this.leftRow = new byte[][] { new byte[16], new byte[8], new byte[8] };
        this.topLine = new byte[][] { new byte[mbWidth << 4], new byte[mbWidth << 3], new byte[mbWidth << 3] };
        this.initValue(this.leftRow, (byte) 1);
        this.initValue(this.topLine, (byte) -1);
        this.quantizer = new VPXQuantizer();
        Picture outMB = Picture.create(16, 16, ColorSpace.YUV420);
        int[] segmentQps = this.rc.getSegmentQps();
        VPXBooleanEncoder boolEnc = new VPXBooleanEncoder(this.dataBuffer);
        int[] segmentMap = new int[mbWidth * mbHeight];
        int mbY = 0;
        for (int mbAddr = 0; mbY < mbHeight; mbY++) {
            this.initValue(this.leftRow, (byte) 1);
            for (int mbX = 0; mbX < mbWidth; mbAddr++) {
                int before = boolEnc.position();
                int segment = this.rc.getSegment();
                segmentMap[mbAddr] = segment;
                this.luma(pic, mbX, mbY, boolEnc, segmentQps[segment], outMB);
                this.chroma(pic, mbX, mbY, boolEnc, segmentQps[segment], outMB);
                this.rc.report(boolEnc.position() - before);
                this.collectPredictors(outMB, mbX);
                mbX++;
            }
        }
        boolEnc.stop();
        this.dataBuffer.flip();
        boolEnc = new VPXBooleanEncoder(this.headerBuffer);
        int[] probs = this.calcSegmentProbs(segmentMap);
        this.writeHeader2(boolEnc, segmentQps, probs);
        int mbYx = 0;
        for (int mbAddr = 0; mbYx < mbHeight; mbYx++) {
            for (int mbX = 0; mbX < mbWidth; mbAddr++) {
                this.writeSegmetId(boolEnc, segmentMap[mbAddr], probs);
                boolEnc.writeBit(145, 1);
                boolEnc.writeBit(156, 0);
                boolEnc.writeBit(163, 0);
                boolEnc.writeBit(142, 0);
                mbX++;
            }
        }
        boolEnc.stop();
        this.headerBuffer.flip();
        out.order(ByteOrder.LITTLE_ENDIAN);
        this.writeHeader(out, pic.getWidth(), pic.getHeight(), this.headerBuffer.remaining());
        out.put(this.headerBuffer);
        out.put(this.dataBuffer);
        out.flip();
        return new VideoEncoder.EncodedFrame(out, true);
    }

    private void prepareBuffers(int mbWidth, int mbHeight) {
        int dataBufSize = mbHeight * mbHeight << 10;
        int headerBufSize = 256 + mbWidth * mbHeight;
        if (this.headerBuffer != null && this.headerBuffer.capacity() >= headerBufSize) {
            this.headerBuffer.clear();
        } else {
            this.headerBuffer = ByteBuffer.allocate(headerBufSize);
        }
        if (this.dataBuffer != null && this.dataBuffer.capacity() >= dataBufSize) {
            this.dataBuffer.clear();
        } else {
            this.dataBuffer = ByteBuffer.allocate(dataBufSize);
        }
    }

    private void writeSegmetId(VPXBooleanEncoder boolEnc, int id, int[] probs) {
        int bit1 = id >> 1 & 1;
        boolEnc.writeBit(probs[0], bit1);
        boolEnc.writeBit(probs[1 + bit1], id & 1);
    }

    private int[] calcSegmentProbs(int[] segmentMap) {
        int[] result = new int[3];
        for (int i = 0; i < segmentMap.length; i++) {
            switch(segmentMap[i]) {
                case 0:
                    result[0]++;
                    result[1]++;
                    break;
                case 1:
                    result[0]++;
                    break;
                case 2:
                    result[2]++;
            }
        }
        for (int i = 0; i < 3; i++) {
            result[i] = MathUtil.clip((result[i] << 8) / segmentMap.length, 1, 255);
        }
        return result;
    }

    private void initValue(byte[][] leftRow2, byte val) {
        Arrays.fill(leftRow2[0], val);
        Arrays.fill(leftRow2[1], val);
        Arrays.fill(leftRow2[2], val);
    }

    private void writeHeader2(VPXBooleanEncoder boolEnc, int[] segmentQps, int[] probs) {
        boolEnc.writeBit(128, 0);
        boolEnc.writeBit(128, 0);
        boolEnc.writeBit(128, 1);
        boolEnc.writeBit(128, 1);
        boolEnc.writeBit(128, 1);
        boolEnc.writeBit(128, 1);
        for (int i = 0; i < segmentQps.length; i++) {
            boolEnc.writeBit(128, 1);
            this.writeInt(boolEnc, segmentQps[i], 7);
            boolEnc.writeBit(128, 0);
        }
        for (int i = segmentQps.length; i < 4; i++) {
            boolEnc.writeBit(128, 0);
        }
        boolEnc.writeBit(128, 0);
        boolEnc.writeBit(128, 0);
        boolEnc.writeBit(128, 0);
        boolEnc.writeBit(128, 0);
        for (int i = 0; i < 3; i++) {
            boolEnc.writeBit(128, 1);
            this.writeInt(boolEnc, probs[i], 8);
        }
        boolEnc.writeBit(128, 0);
        this.writeInt(boolEnc, 1, 6);
        this.writeInt(boolEnc, 0, 3);
        boolEnc.writeBit(128, 0);
        this.writeInt(boolEnc, 0, 2);
        this.writeInt(boolEnc, segmentQps[0], 7);
        boolEnc.writeBit(128, 0);
        boolEnc.writeBit(128, 0);
        boolEnc.writeBit(128, 0);
        boolEnc.writeBit(128, 0);
        boolEnc.writeBit(128, 0);
        boolEnc.writeBit(128, 0);
        int[][][][] probFlags = VPXConst.tokenProbUpdateFlagProbs;
        for (int i = 0; i < probFlags.length; i++) {
            for (int j = 0; j < probFlags[i].length; j++) {
                for (int k = 0; k < probFlags[i][j].length; k++) {
                    for (int l = 0; l < probFlags[i][j][k].length; l++) {
                        boolEnc.writeBit(probFlags[i][j][k][l], 0);
                    }
                }
            }
        }
        boolEnc.writeBit(128, 0);
    }

    void writeInt(VPXBooleanEncoder boolEnc, int data, int bits) {
        for (int bit = bits - 1; bit >= 0; bit--) {
            boolEnc.writeBit(128, 1 & data >> bit);
        }
    }

    private void writeHeader(ByteBuffer out, int width, int height, int firstPart) {
        int version = 0;
        int type = 0;
        int showFrame = 1;
        int header = firstPart << 5 | showFrame << 4 | version << 1 | type;
        out.put((byte) (header & 0xFF));
        out.put((byte) (header >> 8 & 0xFF));
        out.put((byte) (header >> 16 & 0xFF));
        out.put((byte) -99);
        out.put((byte) 1);
        out.put((byte) 42);
        out.putShort((short) width);
        out.putShort((short) height);
    }

    private void collectPredictors(Picture outMB, int mbX) {
        System.arraycopy(outMB.getPlaneData(0), 240, this.topLine[0], mbX << 4, 16);
        System.arraycopy(outMB.getPlaneData(1), 56, this.topLine[1], mbX << 3, 8);
        System.arraycopy(outMB.getPlaneData(2), 56, this.topLine[2], mbX << 3, 8);
        this.copyCol(outMB.getPlaneData(0), 15, 16, this.leftRow[0]);
        this.copyCol(outMB.getPlaneData(1), 7, 8, this.leftRow[1]);
        this.copyCol(outMB.getPlaneData(2), 7, 8, this.leftRow[2]);
    }

    private void copyCol(byte[] planeData, int off, int stride, byte[] out) {
        for (int i = 0; i < out.length; i++) {
            out[i] = planeData[off];
            off += stride;
        }
    }

    private void luma(Picture pic, int mbX, int mbY, VPXBooleanEncoder out, int qp, Picture outMB) {
        int x = mbX << 4;
        int y = mbY << 4;
        int[][] ac = this.transform(pic, 0, qp, x, y);
        int[] dc = this.extractDC(ac);
        this.writeLumaDC(mbX, mbY, out, qp, dc);
        this.writeLumaAC(mbX, mbY, out, ac, qp);
        this.restorePlaneLuma(dc, ac, qp);
        this.putLuma(outMB.getPlaneData(0), this.lumaDCPred(x, y), ac, 4);
    }

    private void writeLumaAC(int mbX, int mbY, VPXBooleanEncoder out, int[][] ac, int qp) {
        for (int i = 0; i < 16; i++) {
            this.quantizer.quantizeY(ac[i], qp);
            this.bitstream.encodeCoeffsDCT15(out, this.zigzag(ac[i], this.tmp), mbX, i & 3, i >> 2);
        }
    }

    private void writeLumaDC(int mbX, int mbY, VPXBooleanEncoder out, int qp, int[] dc) {
        VPXDCT.walsh4x4(dc);
        this.quantizer.quantizeY2(dc, qp);
        this.bitstream.encodeCoeffsWHT(out, this.zigzag(dc, this.tmp), mbX);
    }

    private void writeChroma(int comp, int mbX, int mbY, VPXBooleanEncoder boolEnc, int[][] ac, int qp) {
        for (int i = 0; i < 4; i++) {
            this.quantizer.quantizeUV(ac[i], qp);
            this.bitstream.encodeCoeffsDCTUV(boolEnc, this.zigzag(ac[i], this.tmp), comp, mbX, i & 1, i >> 1);
        }
    }

    private int[] zigzag(int[] zz, int[] tmp2) {
        for (int i = 0; i < 16; i++) {
            tmp2[i] = zz[VPXConst.zigzag[i]];
        }
        return tmp2;
    }

    private void chroma(Picture pic, int mbX, int mbY, VPXBooleanEncoder boolEnc, int qp, Picture outMB) {
        int x = mbX << 3;
        int y = mbY << 3;
        int chromaPred1 = this.chromaPredBlk(1, x, y);
        int chromaPred2 = this.chromaPredBlk(2, x, y);
        int[][] ac1 = this.transformChroma(pic, 1, qp, x, y, outMB, chromaPred1);
        int[][] ac2 = this.transformChroma(pic, 2, qp, x, y, outMB, chromaPred2);
        this.writeChroma(1, mbX, mbY, boolEnc, ac1, qp);
        this.writeChroma(2, mbX, mbY, boolEnc, ac2, qp);
        this.restorePlaneChroma(ac1, qp);
        this.putChroma(outMB.getData()[1], 1, x, y, ac1, chromaPred1);
        this.restorePlaneChroma(ac2, qp);
        this.putChroma(outMB.getData()[2], 2, x, y, ac2, chromaPred2);
    }

    private int[][] transformChroma(Picture pic, int comp, int qp, int x, int y, Picture outMB, int chromaPred) {
        int[][] ac = new int[4][16];
        for (int blk = 0; blk < ac.length; blk++) {
            int blkOffX = (blk & 1) << 2;
            int blkOffY = blk >> 1 << 2;
            this.takeSubtract(pic.getPlaneData(comp), pic.getPlaneWidth(comp), pic.getPlaneHeight(comp), x + blkOffX, y + blkOffY, ac[blk], chromaPred);
            VPXDCT.fdct4x4(ac[blk]);
        }
        return ac;
    }

    private void putChroma(byte[] mb, int comp, int x, int y, int[][] ac, int chromaPred) {
        for (int blk = 0; blk < 4; blk++) {
            this.putBlk(mb, chromaPred, ac[blk], 3, (blk & 1) << 2, blk >> 1 << 2);
        }
    }

    private final byte chromaPredOne(byte[] pix, int x) {
        return (byte) (pix[x] + pix[x + 1] + pix[x + 2] + pix[x + 3] + pix[x + 4] + pix[x + 5] + pix[x + 6] + pix[x + 7] + 4 >> 3);
    }

    private final byte chromaPredTwo(byte[] pix1, byte[] pix2, int x, int y) {
        return (byte) (pix1[x] + pix1[x + 1] + pix1[x + 2] + pix1[x + 3] + pix1[x + 4] + pix1[x + 5] + pix1[x + 6] + pix1[x + 7] + pix2[y] + pix2[y + 1] + pix2[y + 2] + pix2[y + 3] + pix2[y + 4] + pix2[y + 5] + pix2[y + 6] + pix2[y + 7] + 8 >> 4);
    }

    private byte chromaPredBlk(int comp, int x, int y) {
        int predY = y & 7;
        if (x != 0 && y != 0) {
            return this.chromaPredTwo(this.leftRow[comp], this.topLine[comp], predY, x);
        } else if (x != 0) {
            return this.chromaPredOne(this.leftRow[comp], predY);
        } else {
            return y != 0 ? this.chromaPredOne(this.topLine[comp], x) : 0;
        }
    }

    private void putLuma(byte[] planeData, int pred, int[][] ac, int log2stride) {
        for (int blk = 0; blk < ac.length; blk++) {
            int blkOffX = (blk & 3) << 2;
            int blkOffY = blk & -4;
            this.putBlk(planeData, pred, ac[blk], log2stride, blkOffX, blkOffY);
        }
    }

    private void putBlk(byte[] planeData, int pred, int[] block, int log2stride, int blkX, int blkY) {
        int stride = 1 << log2stride;
        int line = 0;
        int srcOff = 0;
        for (int dstOff = (blkY << log2stride) + blkX; line < 4; line++) {
            planeData[dstOff] = (byte) MathUtil.clip(block[srcOff] + pred, -128, 127);
            planeData[dstOff + 1] = (byte) MathUtil.clip(block[srcOff + 1] + pred, -128, 127);
            planeData[dstOff + 2] = (byte) MathUtil.clip(block[srcOff + 2] + pred, -128, 127);
            planeData[dstOff + 3] = (byte) MathUtil.clip(block[srcOff + 3] + pred, -128, 127);
            srcOff += 4;
            dstOff += stride;
        }
    }

    private void restorePlaneChroma(int[][] ac, int qp) {
        for (int i = 0; i < 4; i++) {
            this.quantizer.dequantizeUV(ac[i], qp);
            VPXDCT.idct4x4(ac[i]);
        }
    }

    private void restorePlaneLuma(int[] dc, int[][] ac, int qp) {
        this.quantizer.dequantizeY2(dc, qp);
        VPXDCT.iwalsh4x4(dc);
        for (int i = 0; i < 16; i++) {
            this.quantizer.dequantizeY(ac[i], qp);
            ac[i][0] = dc[i];
            VPXDCT.idct4x4(ac[i]);
        }
    }

    private int[] extractDC(int[][] ac) {
        int[] dc = new int[ac.length];
        for (int i = 0; i < ac.length; i++) {
            dc[i] = ac[i][0];
        }
        return dc;
    }

    private byte lumaDCPred(int x, int y) {
        if (x == 0 && y == 0) {
            return 0;
        } else if (y == 0) {
            return (byte) (ArrayUtil.sumByte(this.leftRow[0]) + 8 >> 4);
        } else {
            return x == 0 ? (byte) (ArrayUtil.sumByte3(this.topLine[0], x, 16) + 8 >> 4) : (byte) (ArrayUtil.sumByte(this.leftRow[0]) + ArrayUtil.sumByte3(this.topLine[0], x, 16) + 16 >> 5);
        }
    }

    private int[][] transform(Picture pic, int comp, int qp, int x, int y) {
        int dcc = this.lumaDCPred(x, y);
        int[][] ac = new int[16][16];
        for (int i = 0; i < ac.length; i++) {
            int[] coeff = ac[i];
            int blkOffX = (i & 3) << 2;
            int blkOffY = i & -4;
            this.takeSubtract(pic.getPlaneData(comp), pic.getPlaneWidth(comp), pic.getPlaneHeight(comp), x + blkOffX, y + blkOffY, coeff, dcc);
            VPXDCT.fdct4x4(coeff);
        }
        return ac;
    }

    private final void takeSubtract(byte[] planeData, int planeWidth, int planeHeight, int x, int y, int[] coeff, int dc) {
        if (x + 4 < planeWidth && y + 4 < planeHeight) {
            this.takeSubtractSafe(planeData, planeWidth, planeHeight, x, y, coeff, dc);
        } else {
            this.takeSubtractUnsafe(planeData, planeWidth, planeHeight, x, y, coeff, dc);
        }
    }

    private final void takeSubtractSafe(byte[] planeData, int planeWidth, int planeHeight, int x, int y, int[] coeff, int dc) {
        int i = 0;
        int srcOff = y * planeWidth + x;
        for (int dstOff = 0; i < 4; dstOff += 4) {
            coeff[dstOff] = planeData[srcOff] - dc;
            coeff[dstOff + 1] = planeData[srcOff + 1] - dc;
            coeff[dstOff + 2] = planeData[srcOff + 2] - dc;
            coeff[dstOff + 3] = planeData[srcOff + 3] - dc;
            i++;
            srcOff += planeWidth;
        }
    }

    private final void takeSubtractUnsafe(byte[] planeData, int planeWidth, int planeHeight, int x, int y, int[] coeff, int dc) {
        int outOff = 0;
        int i;
        for (i = y; i < Math.min(y + 4, planeHeight); i++) {
            int off = i * planeWidth + Math.min(x, planeWidth);
            int j;
            for (j = x; j < Math.min(x + 4, planeWidth); j++) {
                coeff[outOff++] = planeData[off++] - dc;
            }
            off--;
            while (j < x + 4) {
                coeff[outOff++] = planeData[off] - dc;
                j++;
            }
        }
        while (i < y + 4) {
            int off = planeHeight * planeWidth - planeWidth + Math.min(x, planeWidth);
            int j;
            for (j = x; j < Math.min(x + 4, planeWidth); j++) {
                coeff[outOff++] = planeData[off++] - dc;
            }
            off--;
            while (j < x + 4) {
                coeff[outOff++] = planeData[off] - dc;
                j++;
            }
            i++;
        }
    }

    @Override
    public ColorSpace[] getSupportedColorSpaces() {
        return new ColorSpace[] { ColorSpace.YUV420J };
    }

    @Override
    public int estimateBufferSize(Picture frame) {
        return frame.getWidth() * frame.getHeight() / 2;
    }
}