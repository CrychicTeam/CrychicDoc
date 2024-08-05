package com.github.alexthe666.citadel.repack.jcodec.codecs.prores;

import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.dct.SimpleIDCT10Bit;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rect;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ProresDecoder extends VideoDecoder {

    static final int[] table = new int[] { 8, 7, 6, 6, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    static final int[] mask = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, -1 };

    public static final int nZeros(int check16Bit) {
        int low = table[check16Bit & 0xFF];
        check16Bit >>= 8;
        int high = table[check16Bit];
        return high + (mask[high] & low);
    }

    public static final int readCodeword(BitReader reader, Codebook codebook) {
        int q = nZeros(reader.check16Bits());
        reader.skipFast(q + 1);
        if (q > codebook.switchBits) {
            int bits = codebook.golombBits + q;
            if (bits > 16) {
                Logger.error("Broken prores slice");
            }
            return (1 << bits | reader.readFast16(bits)) - codebook.golombOffset;
        } else {
            return codebook.riceOrder > 0 ? q << codebook.riceOrder | reader.readFast16(codebook.riceOrder) : q;
        }
    }

    public static final int golumbToSigned(int val) {
        return val >> 1 ^ golumbSign(val);
    }

    public static final int golumbSign(int val) {
        return -(val & 1);
    }

    public static final void readDCCoeffs(BitReader bits, int[] qMat, int[] out, int blocksPerSlice, int blkSize) {
        int c = readCodeword(bits, ProresConsts.firstDCCodebook);
        if (c >= 0) {
            int prevDc = golumbToSigned(c);
            out[0] = 4096 + qScale(qMat, 0, prevDc);
            int code = 5;
            int sign = 0;
            int idx = blkSize;
            for (int i = 1; i < blocksPerSlice; idx += blkSize) {
                code = readCodeword(bits, ProresConsts.dcCodebooks[Math.min(code, 6)]);
                if (code < 0) {
                    return;
                }
                if (code != 0) {
                    sign ^= golumbSign(code);
                } else {
                    sign = 0;
                }
                prevDc += MathUtil.toSigned(code + 1 >> 1, sign);
                out[idx] = 4096 + qScale(qMat, 0, prevDc);
                i++;
            }
        }
    }

    protected static final void readACCoeffs(BitReader bits, int[] qMat, int[] out, int blocksPerSlice, int[] scan, int max, int log2blkSize) {
        int run = 4;
        int level = 2;
        int blockMask = blocksPerSlice - 1;
        int log2BlocksPerSlice = MathUtil.log2(blocksPerSlice);
        int maxCoeffs = 64 << log2BlocksPerSlice;
        int pos = blockMask;
        while (bits.remaining() > 32 || bits.checkAllBits() != 0) {
            run = readCodeword(bits, ProresConsts.runCodebooks[Math.min(run, 15)]);
            if (run < 0 || run >= maxCoeffs - pos - 1) {
                return;
            }
            pos += run + 1;
            level = readCodeword(bits, ProresConsts.levCodebooks[Math.min(level, 9)]) + 1;
            if (level < 0 || level > 65535) {
                return;
            }
            int sign = -bits.read1Bit();
            int ind = pos >> log2BlocksPerSlice;
            if (ind >= max) {
                break;
            }
            out[((pos & blockMask) << log2blkSize) + scan[ind]] = qScale(qMat, ind, MathUtil.toSigned(level, sign));
        }
    }

    private static final int qScale(int[] qMat, int ind, int val) {
        return val * qMat[ind] >> 2;
    }

    protected void decodeOnePlane(BitReader bits, int blocksPerSlice, int[] out, int[] qMat, int[] scan, int mbX, int mbY, int plane) {
        try {
            readDCCoeffs(bits, qMat, out, blocksPerSlice, 64);
            readACCoeffs(bits, qMat, out, blocksPerSlice, scan, 64, 6);
        } catch (RuntimeException var10) {
            System.err.println("Suppressing slice error at [" + mbX + ", " + mbY + "].");
        }
        for (int i = 0; i < blocksPerSlice; i++) {
            SimpleIDCT10Bit.idct10(out, i << 6);
        }
    }

    @Override
    public Picture decodeFrame(ByteBuffer data, byte[][] target) {
        return this.decodeFrameHiBD(data, target, (byte[][]) null);
    }

    public Picture decodeFrameHiBD(ByteBuffer data, byte[][] target, byte[][] lowBits) {
        ProresConsts.FrameHeader fh = readFrameHeader(data);
        int codedWidth = fh.width + 15 & -16;
        int codedHeight = fh.height + 15 & -16;
        int lumaSize = codedWidth * codedHeight;
        int chromaSize = lumaSize >> 3 - fh.chromaType;
        if (target != null && target[0].length >= lumaSize && target[1].length >= chromaSize && target[2].length >= chromaSize) {
            if (fh.frameType == 0) {
                this.decodePicture(data, target, lowBits, codedWidth, codedHeight, codedWidth >> 4, fh.qMatLuma, fh.qMatChroma, fh.scan, 0, fh.chromaType);
            } else {
                this.decodePicture(data, target, lowBits, codedWidth, codedHeight >> 1, codedWidth >> 4, fh.qMatLuma, fh.qMatChroma, fh.scan, fh.topFieldFirst ? 1 : 2, fh.chromaType);
                this.decodePicture(data, target, lowBits, codedWidth, codedHeight >> 1, codedWidth >> 4, fh.qMatLuma, fh.qMatChroma, fh.scan, fh.topFieldFirst ? 2 : 1, fh.chromaType);
            }
            ColorSpace color = fh.chromaType == 2 ? ColorSpace.YUV422 : ColorSpace.YUV444;
            return new Picture(codedWidth, codedHeight, target, lowBits, color, lowBits == null ? 0 : 2, new Rect(0, 0, fh.width & color.getWidthMask(), fh.height & color.getHeightMask()));
        } else {
            throw new RuntimeException("Provided output picture won't fit into provided buffer");
        }
    }

    public Picture[] decodeFields(ByteBuffer data, byte[][][] target) {
        return this.decodeFieldsHiBD(data, target, (byte[][][]) null);
    }

    public Picture[] decodeFieldsHiBD(ByteBuffer data, byte[][][] target, byte[][][] lowBits) {
        ProresConsts.FrameHeader fh = readFrameHeader(data);
        int codedWidth = fh.width + 15 & -16;
        int codedHeight = fh.height + 15 & -16;
        int lumaSize = codedWidth * codedHeight;
        int chromaSize = lumaSize >> 1;
        if (fh.frameType == 0) {
            if (target != null && target[0][0].length >= lumaSize && target[0][1].length >= chromaSize && target[0][2].length >= chromaSize) {
                this.decodePicture(data, target[0], lowBits[0], fh.width, fh.height, codedWidth >> 4, fh.qMatLuma, fh.qMatChroma, fh.scan, 0, fh.chromaType);
                return new Picture[] { Picture.createPicture(codedWidth, codedHeight, target[0], ColorSpace.YUV422) };
            } else {
                throw new RuntimeException("Provided output picture won't fit into provided buffer");
            }
        } else {
            lumaSize >>= 1;
            chromaSize >>= 1;
            if (target != null && target[0][0].length >= lumaSize && target[0][1].length >= chromaSize && target[0][2].length >= chromaSize && target[1][0].length >= lumaSize && target[1][1].length >= chromaSize && target[1][2].length >= chromaSize) {
                this.decodePicture(data, target[fh.topFieldFirst ? 0 : 1], lowBits[fh.topFieldFirst ? 0 : 1], fh.width, fh.height >> 1, codedWidth >> 4, fh.qMatLuma, fh.qMatChroma, fh.scan, 0, fh.chromaType);
                this.decodePicture(data, target[fh.topFieldFirst ? 1 : 0], lowBits[fh.topFieldFirst ? 0 : 1], fh.width, fh.height >> 1, codedWidth >> 4, fh.qMatLuma, fh.qMatChroma, fh.scan, 0, fh.chromaType);
                return new Picture[] { Picture.createPicture(codedWidth, codedHeight >> 1, target[0], ColorSpace.YUV422), Picture.createPicture(codedWidth, codedHeight >> 1, target[1], ColorSpace.YUV422) };
            } else {
                throw new RuntimeException("Provided output picture won't fit into provided buffer");
            }
        }
    }

    public static ProresConsts.FrameHeader readFrameHeader(ByteBuffer inp) {
        int frameSize = inp.getInt();
        String sig = readSig(inp);
        if (!"icpf".equals(sig)) {
            throw new RuntimeException("Not a prores frame");
        } else {
            short hdrSize = inp.getShort();
            short version = inp.getShort();
            int res1 = inp.getInt();
            short width = inp.getShort();
            short height = inp.getShort();
            int flags1 = inp.get();
            int frameType = flags1 >> 2 & 3;
            int chromaType = flags1 >> 6 & 3;
            boolean topFieldFirst = false;
            int[] scan;
            if (frameType == 0) {
                scan = ProresConsts.progressive_scan;
            } else {
                scan = ProresConsts.interlaced_scan;
                if (frameType == 1) {
                    topFieldFirst = true;
                }
            }
            byte res2 = inp.get();
            byte prim = inp.get();
            byte transFunc = inp.get();
            byte matrix = inp.get();
            byte pixFmt = inp.get();
            byte res3 = inp.get();
            int flags2 = inp.get() & 255;
            int[] qMatLuma = new int[64];
            int[] qMatChroma = new int[64];
            if (hasQMatLuma(flags2)) {
                readQMat(inp, qMatLuma, scan);
            } else {
                Arrays.fill(qMatLuma, 4);
            }
            if (hasQMatChroma(flags2)) {
                readQMat(inp, qMatChroma, scan);
            } else {
                Arrays.fill(qMatChroma, 4);
            }
            inp.position(inp.position() + hdrSize - (20 + (hasQMatLuma(flags2) ? 64 : 0) + (hasQMatChroma(flags2) ? 64 : 0)));
            return new ProresConsts.FrameHeader(frameSize - hdrSize - 8, width, height, frameType, topFieldFirst, scan, qMatLuma, qMatChroma, chromaType);
        }
    }

    static final String readSig(ByteBuffer inp) {
        byte[] sig = new byte[4];
        inp.get(sig);
        return Platform.stringFromBytes(sig);
    }

    protected void decodePicture(ByteBuffer data, byte[][] result, byte[][] lowBits, int width, int height, int mbWidth, int[] qMatLuma, int[] qMatChroma, int[] scan, int pictureType, int chromaType) {
        ProresConsts.PictureHeader ph = readPictureHeader(data);
        int mbX = 0;
        int mbY = 0;
        int sliceMbCount = 1 << ph.log2SliceMbWidth;
        for (int i = 0; i < ph.sliceSizes.length; i++) {
            while (mbWidth - mbX < sliceMbCount) {
                sliceMbCount >>= 1;
            }
            this.decodeSlice(NIOUtils.read(data, ph.sliceSizes[i]), qMatLuma, qMatChroma, scan, sliceMbCount, mbX, mbY, ph.sliceSizes[i], result, lowBits, width, pictureType, chromaType);
            mbX += sliceMbCount;
            if (mbX == mbWidth) {
                sliceMbCount = 1 << ph.log2SliceMbWidth;
                mbX = 0;
                mbY++;
            }
        }
    }

    public static ProresConsts.PictureHeader readPictureHeader(ByteBuffer inp) {
        int hdrSize = (inp.get() & 255) >> 3;
        inp.getInt();
        int sliceCount = inp.getShort();
        int a = inp.get() & 255;
        int log2SliceMbWidth = a >> 4;
        short[] sliceSizes = new short[sliceCount];
        for (int i = 0; i < sliceCount; i++) {
            sliceSizes[i] = inp.getShort();
        }
        return new ProresConsts.PictureHeader(log2SliceMbWidth, sliceSizes);
    }

    private void decodeSlice(ByteBuffer data, int[] qMatLuma, int[] qMatChroma, int[] scan, int sliceMbCount, int mbX, int mbY, short sliceSize, byte[][] result, byte[][] lowBits, int lumaStride, int pictureType, int chromaType) {
        int hdrSize = (data.get() & 255) >> 3;
        int qScale = clip(data.get() & 255, 1, 224);
        qScale = qScale > 128 ? qScale - 96 << 2 : qScale;
        int yDataSize = data.getShort();
        int uDataSize = data.getShort();
        int vDataSize = hdrSize > 7 ? data.getShort() : sliceSize - uDataSize - yDataSize - hdrSize;
        int[] y = new int[sliceMbCount << 8];
        this.decodeOnePlane(bitstream(data, yDataSize), sliceMbCount << 2, y, scaleMat(qMatLuma, qScale), scan, mbX, mbY, 0);
        int chromaBlkCount = sliceMbCount << chromaType >> 1;
        int[] u = new int[chromaBlkCount << 6];
        this.decodeOnePlane(bitstream(data, uDataSize), chromaBlkCount, u, scaleMat(qMatChroma, qScale), scan, mbX, mbY, 1);
        int[] v = new int[chromaBlkCount << 6];
        this.decodeOnePlane(bitstream(data, vDataSize), chromaBlkCount, v, scaleMat(qMatChroma, qScale), scan, mbX, mbY, 2);
        this.putSlice(result, lowBits, lumaStride, mbX, mbY, y, u, v, pictureType == 0 ? 0 : 1, pictureType == 2 ? 1 : 0, chromaType, sliceMbCount);
    }

    public static final int[] scaleMat(int[] qMatLuma, int qScale) {
        int[] res = new int[qMatLuma.length];
        for (int i = 0; i < qMatLuma.length; i++) {
            res[i] = qMatLuma[i] * qScale;
        }
        return res;
    }

    static final BitReader bitstream(ByteBuffer data, int dataSize) {
        return BitReader.createBitReader(NIOUtils.read(data, dataSize));
    }

    static final int clip(int val, int min, int max) {
        return val < min ? min : (val > max ? max : val);
    }

    protected void putSlice(byte[][] result, byte[][] lowBits, int lumaStride, int mbX, int mbY, int[] y, int[] u, int[] v, int dist, int shift, int chromaType, int sliceMbCount) {
        int chromaStride = lumaStride >> 1;
        this.putLuma(result[0], lowBits != null ? lowBits[0] : null, shift * lumaStride, lumaStride << dist, mbX, mbY, y, sliceMbCount, dist, shift);
        if (chromaType == 2) {
            this.putChroma(result[1], lowBits != null ? lowBits[1] : null, shift * chromaStride, chromaStride << dist, mbX, mbY, u, sliceMbCount, dist, shift);
            this.putChroma(result[2], lowBits != null ? lowBits[2] : null, shift * chromaStride, chromaStride << dist, mbX, mbY, v, sliceMbCount, dist, shift);
        } else {
            this.putLuma(result[1], lowBits != null ? lowBits[1] : null, shift * lumaStride, lumaStride << dist, mbX, mbY, u, sliceMbCount, dist, shift);
            this.putLuma(result[2], lowBits != null ? lowBits[2] : null, shift * lumaStride, lumaStride << dist, mbX, mbY, v, sliceMbCount, dist, shift);
        }
    }

    private void putLuma(byte[] y, byte[] lowBits, int off, int stride, int mbX, int mbY, int[] luma, int mbPerSlice, int dist, int shift) {
        off += (mbX << 4) + (mbY << 4) * stride;
        for (int k = 0; k < mbPerSlice; k++) {
            this.putBlock(y, lowBits, off, stride, luma, k << 8, dist, shift);
            this.putBlock(y, lowBits, off + 8, stride, luma, (k << 8) + 64, dist, shift);
            this.putBlock(y, lowBits, off + 8 * stride, stride, luma, (k << 8) + 128, dist, shift);
            this.putBlock(y, lowBits, off + 8 * stride + 8, stride, luma, (k << 8) + 192, dist, shift);
            off += 16;
        }
    }

    private void putChroma(byte[] y, byte[] lowBits, int off, int stride, int mbX, int mbY, int[] chroma, int mbPerSlice, int dist, int shift) {
        off += (mbX << 3) + (mbY << 4) * stride;
        for (int k = 0; k < mbPerSlice; k++) {
            this.putBlock(y, lowBits, off, stride, chroma, k << 7, dist, shift);
            this.putBlock(y, lowBits, off + 8 * stride, stride, chroma, (k << 7) + 64, dist, shift);
            off += 8;
        }
    }

    private void putBlock(byte[] square, byte[] lowBits, int sqOff, int sqStride, int[] flat, int flOff, int dist, int shift) {
        int i = 0;
        int dstOff = sqOff;
        for (int srcOff = flOff; i < 8; srcOff += 8) {
            for (int j = 0; j < 8; j++) {
                int round = MathUtil.clip(flat[j + srcOff] + 2 >> 2, 1, 255);
                square[j + dstOff] = (byte) (round - 128);
            }
            i++;
            dstOff += sqStride;
        }
        if (lowBits != null) {
            i = 0;
            dstOff = sqOff;
            for (int srcOff = flOff; i < 8; srcOff += 8) {
                for (int j = 0; j < 8; j++) {
                    int val = MathUtil.clip(flat[j + srcOff], 4, 1019);
                    int round = MathUtil.clip(flat[j + srcOff] + 2 >> 2, 1, 255);
                    lowBits[j + dstOff] = (byte) (val - (round << 2));
                }
                i++;
                dstOff += sqStride;
            }
        }
    }

    static final boolean hasQMatChroma(int flags2) {
        return (flags2 & 1) != 0;
    }

    static final void readQMat(ByteBuffer inp, int[] qMatLuma, int[] scan) {
        byte[] b = new byte[64];
        inp.get(b);
        for (int i = 0; i < 64; i++) {
            qMatLuma[i] = b[scan[i]] & 255;
        }
    }

    static final boolean hasQMatLuma(int flags2) {
        return (flags2 & 2) != 0;
    }

    public boolean isProgressive(ByteBuffer data) {
        return ((data.get(20) & 255) >> 2 & 3) == 0;
    }

    @UsedViaReflection
    public static int probe(ByteBuffer data) {
        return data.get(4) == 105 && data.get(5) == 99 && data.get(6) == 112 && data.get(7) == 102 ? 100 : 0;
    }

    @Override
    public VideoCodecMeta getCodecMeta(ByteBuffer data) {
        ProresConsts.FrameHeader fh = readFrameHeader(data);
        return VideoCodecMeta.createSimpleVideoCodecMeta(new Size(fh.width, fh.height), fh.chromaType == 2 ? ColorSpace.YUV422 : ColorSpace.YUV444);
    }
}