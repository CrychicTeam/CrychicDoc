package com.github.alexthe666.citadel.repack.jcodec.codecs.prores;

import com.github.alexthe666.citadel.repack.jcodec.common.VideoEncoder;
import com.github.alexthe666.citadel.repack.jcodec.common.dct.SimpleIDCT10Bit;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rect;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.ImageOP;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.nio.ByteBuffer;

public class ProresEncoder extends VideoEncoder {

    private static final int LOG_DEFAULT_SLICE_MB_WIDTH = 3;

    private static final int DEFAULT_SLICE_MB_WIDTH = 8;

    protected ProresEncoder.Profile profile;

    private int[][] scaledLuma;

    private int[][] scaledChroma;

    private boolean interlaced;

    public static ProresEncoder createProresEncoder(String profile, boolean interlaced) {
        return new ProresEncoder(profile == null ? ProresEncoder.Profile.HQ : ProresEncoder.Profile.valueOf(profile), interlaced);
    }

    public ProresEncoder(ProresEncoder.Profile profile, boolean interlaced) {
        this.profile = profile;
        this.scaledLuma = this.scaleQMat(profile.qmatLuma, 1, 16);
        this.scaledChroma = this.scaleQMat(profile.qmatChroma, 1, 16);
        this.interlaced = interlaced;
    }

    private int[][] scaleQMat(int[] qmatLuma, int start, int count) {
        int[][] result = new int[count][];
        for (int i = 0; i < count; i++) {
            result[i] = new int[qmatLuma.length];
            for (int j = 0; j < qmatLuma.length; j++) {
                result[i][j] = qmatLuma[j] * (i + start);
            }
        }
        return result;
    }

    public static final void writeCodeword(BitWriter writer, Codebook codebook, int val) {
        int firstExp = codebook.switchBits + 1 << codebook.riceOrder;
        if (val >= firstExp) {
            val -= firstExp;
            val += 1 << codebook.expOrder;
            int exp = MathUtil.log2(val);
            int zeros = exp - codebook.expOrder + codebook.switchBits + 1;
            for (int i = 0; i < zeros; i++) {
                writer.write1Bit(0);
            }
            writer.write1Bit(1);
            writer.writeNBit(val, exp);
        } else if (codebook.riceOrder > 0) {
            for (int i = 0; i < val >> codebook.riceOrder; i++) {
                writer.write1Bit(0);
            }
            writer.write1Bit(1);
            writer.writeNBit(val & (1 << codebook.riceOrder) - 1, codebook.riceOrder);
        } else {
            for (int i = 0; i < val; i++) {
                writer.write1Bit(0);
            }
            writer.write1Bit(1);
        }
    }

    private static final int qScale(int[] qMat, int ind, int val) {
        return val / qMat[ind];
    }

    private static final int toGolumb(int val) {
        return val << 1 ^ val >> 31;
    }

    private static final int toGolumbSign(int val, int sign) {
        return val == 0 ? 0 : (val << 1) + sign;
    }

    private static final int diffSign(int val, int sign) {
        return val >> 31 ^ sign;
    }

    public static final int getLevel(int val) {
        int sign = val >> 31;
        return (val ^ sign) - sign;
    }

    static final void writeDCCoeffs(BitWriter bits, int[] qMat, int[] _in, int blocksPerSlice) {
        int prevDc = qScale(qMat, 0, _in[0] - 16384);
        writeCodeword(bits, ProresConsts.firstDCCodebook, toGolumb(prevDc));
        int code = 5;
        int sign = 0;
        int idx = 64;
        for (int i = 1; i < blocksPerSlice; idx += 64) {
            int newDc = qScale(qMat, 0, _in[idx] - 16384);
            int delta = newDc - prevDc;
            int newCode = toGolumbSign(getLevel(delta), diffSign(delta, sign));
            writeCodeword(bits, ProresConsts.dcCodebooks[Math.min(code, 6)], newCode);
            code = newCode;
            sign = delta >> 31;
            prevDc = newDc;
            i++;
        }
    }

    static final void writeACCoeffs(BitWriter bits, int[] qMat, int[] _in, int blocksPerSlice, int[] scan, int maxCoeff) {
        int prevRun = 4;
        int prevLevel = 2;
        int run = 0;
        for (int i = 1; i < maxCoeff; i++) {
            int indp = scan[i];
            for (int j = 0; j < blocksPerSlice; j++) {
                int val = qScale(qMat, indp, _in[(j << 6) + indp]);
                if (val == 0) {
                    run++;
                } else {
                    writeCodeword(bits, ProresConsts.runCodebooks[Math.min(prevRun, 15)], run);
                    prevRun = run;
                    run = 0;
                    int level = getLevel(val);
                    writeCodeword(bits, ProresConsts.levCodebooks[Math.min(prevLevel, 9)], level - 1);
                    prevLevel = level;
                    bits.write1Bit(MathUtil.sign(val));
                }
            }
        }
    }

    static final void encodeOnePlane(BitWriter bits, int blocksPerSlice, int[] qMat, int[] scan, int[] _in) {
        writeDCCoeffs(bits, qMat, _in, blocksPerSlice);
        writeACCoeffs(bits, qMat, _in, blocksPerSlice, scan, 64);
    }

    private void dctOnePlane(int blocksPerSlice, byte[] src, byte[] hibd, int[] dst) {
        for (int i = 0; i < src.length; i++) {
            dst[i] = src[i] + 128 << 2;
        }
        if (hibd != null) {
            for (int i = 0; i < src.length; i++) {
                dst[i] += hibd[i];
            }
        }
        for (int i = 0; i < blocksPerSlice; i++) {
            SimpleIDCT10Bit.fdctProres10(dst, i << 6);
        }
    }

    protected int encodeSlice(ByteBuffer out, int[][] scaledLuma, int[][] scaledChroma, int[] scan, int sliceMbCount, int mbX, int mbY, Picture result, int prevQp, int mbWidth, int mbHeight, boolean unsafe, int vStep, int vOffset) {
        Picture striped = this.splitSlice(result, mbX, mbY, sliceMbCount, unsafe, vStep, vOffset);
        int[][] ac = new int[][] { new int[sliceMbCount << 8], new int[sliceMbCount << 7], new int[sliceMbCount << 7] };
        byte[][] data = striped.getData();
        byte[][] lowBits = striped.getLowBits();
        this.dctOnePlane(sliceMbCount << 2, data[0], lowBits == null ? null : lowBits[0], ac[0]);
        this.dctOnePlane(sliceMbCount << 1, data[1], lowBits == null ? null : lowBits[1], ac[1]);
        this.dctOnePlane(sliceMbCount << 1, data[2], lowBits == null ? null : lowBits[2], ac[2]);
        int est = (sliceMbCount >> 2) * this.profile.bitrate;
        int low = est - (est >> 3);
        int high = est + (est >> 3);
        int qp = prevQp;
        out.put((byte) 48);
        ByteBuffer fork = out.duplicate();
        NIOUtils.skip(out, 5);
        int rem = out.position();
        int[] sizes = new int[3];
        encodeSliceData(out, scaledLuma[prevQp - 1], scaledChroma[prevQp - 1], scan, sliceMbCount, ac, prevQp, sizes);
        if (bits(sizes) > high && prevQp < this.profile.lastQp) {
            do {
                qp++;
                out.position(rem);
                encodeSliceData(out, scaledLuma[qp - 1], scaledChroma[qp - 1], scan, sliceMbCount, ac, qp, sizes);
            } while (bits(sizes) > high && qp < this.profile.lastQp);
        } else if (bits(sizes) < low && prevQp > this.profile.firstQp) {
            do {
                qp--;
                out.position(rem);
                encodeSliceData(out, scaledLuma[qp - 1], scaledChroma[qp - 1], scan, sliceMbCount, ac, qp, sizes);
            } while (bits(sizes) < low && qp > this.profile.firstQp);
        }
        fork.put((byte) qp);
        fork.putShort((short) sizes[0]);
        fork.putShort((short) sizes[1]);
        return qp;
    }

    static final int bits(int[] sizes) {
        return sizes[0] + sizes[1] + sizes[2] << 3;
    }

    protected static final void encodeSliceData(ByteBuffer out, int[] qmatLuma, int[] qmatChroma, int[] scan, int sliceMbCount, int[][] ac, int qp, int[] sizes) {
        sizes[0] = onePlane(out, sliceMbCount << 2, qmatLuma, scan, ac[0]);
        sizes[1] = onePlane(out, sliceMbCount << 1, qmatChroma, scan, ac[1]);
        sizes[2] = onePlane(out, sliceMbCount << 1, qmatChroma, scan, ac[2]);
    }

    static final int onePlane(ByteBuffer out, int blocksPerSlice, int[] qmatLuma, int[] scan, int[] data) {
        int rem = out.position();
        BitWriter bits = new BitWriter(out);
        encodeOnePlane(bits, blocksPerSlice, qmatLuma, scan, data);
        bits.flush();
        return out.position() - rem;
    }

    protected void encodePicture(ByteBuffer out, int[][] scaledLuma, int[][] scaledChroma, int[] scan, Picture picture, int vStep, int vOffset) {
        int mbWidth = picture.getWidth() + 15 >> 4;
        int shift = 4 + vStep;
        int round = (1 << shift) - 1;
        int mbHeight = picture.getHeight() + round >> shift;
        int qp = this.profile.firstQp;
        int nSlices = this.calcNSlices(mbWidth, mbHeight);
        writePictureHeader(3, nSlices, out);
        ByteBuffer fork = out.duplicate();
        NIOUtils.skip(out, nSlices << 1);
        int i = 0;
        int[] total = new int[nSlices];
        for (int mbY = 0; mbY < mbHeight; mbY++) {
            int mbX = 0;
            for (int sliceMbCount = 8; mbX < mbWidth; mbX += sliceMbCount) {
                while (mbWidth - mbX < sliceMbCount) {
                    sliceMbCount >>= 1;
                }
                int sliceStart = out.position();
                boolean unsafeBottom = picture.getHeight() % 16 != 0 && mbY == mbHeight - 1;
                boolean unsafeRight = picture.getWidth() % 16 != 0 && mbX + sliceMbCount == mbWidth;
                qp = this.encodeSlice(out, scaledLuma, scaledChroma, scan, sliceMbCount, mbX, mbY, picture, qp, mbWidth, mbHeight, unsafeBottom || unsafeRight, vStep, vOffset);
                fork.putShort((short) (out.position() - sliceStart));
                total[i++] = (short) (out.position() - sliceStart);
            }
        }
    }

    public static void writePictureHeader(int logDefaultSliceMbWidth, int nSlices, ByteBuffer out) {
        int headerLen = 8;
        out.put((byte) (headerLen << 3));
        out.putInt(0);
        out.putShort((short) nSlices);
        out.put((byte) (logDefaultSliceMbWidth << 4));
    }

    private int calcNSlices(int mbWidth, int mbHeight) {
        int nSlices = mbWidth >> 3;
        for (int i = 0; i < 3; i++) {
            nSlices += mbWidth >> i & 1;
        }
        return nSlices * mbHeight;
    }

    private Picture splitSlice(Picture result, int mbX, int mbY, int sliceMbCount, boolean unsafe, int vStep, int vOffset) {
        Picture out = Picture.createCroppedHiBD(sliceMbCount << 4, 16, result.getLowBitsNum(), ColorSpace.YUV422, null);
        if (unsafe) {
            int mbHeightPix = 16 << vStep;
            Picture filled = Picture.create(sliceMbCount << 4, mbHeightPix, ColorSpace.YUV422);
            ImageOP.subImageWithFillPic8(result, filled, new Rect(mbX << 4, mbY << 4 + vStep, sliceMbCount << 4, mbHeightPix));
            this.split(filled, out, 0, 0, sliceMbCount, vStep, vOffset);
        } else {
            this.split(result, out, mbX, mbY, sliceMbCount, vStep, vOffset);
        }
        return out;
    }

    private void split(Picture src, Picture dst, int mbX, int mbY, int sliceMbCount, int vStep, int vOffset) {
        byte[][] inData = src.getData();
        byte[][] inhbdData = src.getLowBits();
        byte[][] outData = dst.getData();
        byte[][] outhbdData = dst.getLowBits();
        this.doSplit(inData[0], outData[0], src.getPlaneWidth(0), mbX, mbY, sliceMbCount, 0, vStep, vOffset);
        this.doSplit(inData[1], outData[1], src.getPlaneWidth(1), mbX, mbY, sliceMbCount, 1, vStep, vOffset);
        this.doSplit(inData[2], outData[2], src.getPlaneWidth(2), mbX, mbY, sliceMbCount, 1, vStep, vOffset);
        if (src.getLowBits() != null) {
            this.doSplit(inhbdData[0], outhbdData[0], src.getPlaneWidth(0), mbX, mbY, sliceMbCount, 0, vStep, vOffset);
            this.doSplit(inhbdData[1], outhbdData[1], src.getPlaneWidth(1), mbX, mbY, sliceMbCount, 1, vStep, vOffset);
            this.doSplit(inhbdData[2], outhbdData[2], src.getPlaneWidth(2), mbX, mbY, sliceMbCount, 1, vStep, vOffset);
        }
    }

    private void doSplit(byte[] _in, byte[] out, int stride, int mbX, int mbY, int sliceMbCount, int chroma, int vStep, int vOffset) {
        int outOff = 0;
        int off = (mbY << 4) * (stride << vStep) + (mbX << 4 - chroma) + stride * vOffset;
        stride <<= vStep;
        for (int i = 0; i < sliceMbCount; i++) {
            this.splitBlock(_in, stride, off, out, outOff);
            this.splitBlock(_in, stride, off + (stride << 3), out, outOff + (128 >> chroma));
            if (chroma == 0) {
                this.splitBlock(_in, stride, off + 8, out, outOff + 64);
                this.splitBlock(_in, stride, off + (stride << 3) + 8, out, outOff + 192);
            }
            outOff += 256 >> chroma;
            off += 16 >> chroma;
        }
    }

    private void splitBlock(byte[] y, int stride, int off, byte[] out, int outOff) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                out[outOff++] = y[off++];
            }
            off += stride - 8;
        }
    }

    @Override
    public VideoEncoder.EncodedFrame encodeFrame(Picture pic, ByteBuffer buffer) {
        ByteBuffer out = buffer.duplicate();
        ByteBuffer fork = out.duplicate();
        int[] scan = this.interlaced ? ProresConsts.interlaced_scan : ProresConsts.progressive_scan;
        writeFrameHeader(out, new ProresConsts.FrameHeader(0, pic.getCroppedWidth(), pic.getCroppedHeight(), this.interlaced ? 1 : 0, true, scan, this.profile.qmatLuma, this.profile.qmatChroma, 2));
        this.encodePicture(out, this.scaledLuma, this.scaledChroma, scan, pic, this.interlaced ? 1 : 0, 0);
        if (this.interlaced) {
            this.encodePicture(out, this.scaledLuma, this.scaledChroma, scan, pic, this.interlaced ? 1 : 0, 1);
        }
        out.flip();
        fork.putInt(out.remaining());
        return new VideoEncoder.EncodedFrame(out, true);
    }

    public static void writeFrameHeader(ByteBuffer outp, ProresConsts.FrameHeader header) {
        short headerSize = 148;
        outp.putInt(headerSize + 8 + header.payloadSize);
        outp.put(new byte[] { 105, 99, 112, 102 });
        outp.putShort(headerSize);
        outp.putShort((short) 0);
        outp.put(new byte[] { 97, 112, 108, 48 });
        outp.putShort((short) header.width);
        outp.putShort((short) header.height);
        outp.put((byte) (header.frameType == 0 ? 131 : 135));
        outp.put(new byte[] { 0, 2, 2, 6, 32, 0 });
        outp.put((byte) 3);
        writeQMat(outp, header.qMatLuma);
        writeQMat(outp, header.qMatChroma);
    }

    static final void writeQMat(ByteBuffer out, int[] qmat) {
        for (int i = 0; i < 64; i++) {
            out.put((byte) qmat[i]);
        }
    }

    @Override
    public ColorSpace[] getSupportedColorSpaces() {
        return new ColorSpace[] { ColorSpace.YUV422 };
    }

    @Override
    public int estimateBufferSize(Picture frame) {
        return 3 * frame.getWidth() * frame.getHeight() / 2;
    }

    public static final class Profile {

        public static final ProresEncoder.Profile PROXY = new ProresEncoder.Profile("PROXY", ProresConsts.QMAT_LUMA_APCO, ProresConsts.QMAT_CHROMA_APCO, "apco", 1000, 4, 8);

        public static final ProresEncoder.Profile LT = new ProresEncoder.Profile("LT", ProresConsts.QMAT_LUMA_APCS, ProresConsts.QMAT_CHROMA_APCS, "apcs", 2100, 1, 9);

        public static final ProresEncoder.Profile STANDARD = new ProresEncoder.Profile("STANDARD", ProresConsts.QMAT_LUMA_APCN, ProresConsts.QMAT_CHROMA_APCN, "apcn", 3500, 1, 6);

        public static final ProresEncoder.Profile HQ = new ProresEncoder.Profile("HQ", ProresConsts.QMAT_LUMA_APCH, ProresConsts.QMAT_CHROMA_APCH, "apch", 5400, 1, 6);

        private static final ProresEncoder.Profile[] _values = new ProresEncoder.Profile[] { PROXY, LT, STANDARD, HQ };

        final String name;

        final int[] qmatLuma;

        final int[] qmatChroma;

        public final String fourcc;

        final int bitrate;

        final int firstQp;

        final int lastQp;

        public static ProresEncoder.Profile[] values() {
            return _values;
        }

        public static ProresEncoder.Profile valueOf(String name) {
            String nameU = name.toUpperCase();
            for (ProresEncoder.Profile profile2 : _values) {
                if (name.equals(nameU)) {
                    return profile2;
                }
            }
            return null;
        }

        private Profile(String name, int[] qmatLuma, int[] qmatChroma, String fourcc, int bitrate, int firstQp, int lastQp) {
            this.name = name;
            this.qmatLuma = qmatLuma;
            this.qmatChroma = qmatChroma;
            this.fourcc = fourcc;
            this.bitrate = bitrate;
            this.firstQp = firstQp;
            this.lastQp = lastQp;
        }
    }
}