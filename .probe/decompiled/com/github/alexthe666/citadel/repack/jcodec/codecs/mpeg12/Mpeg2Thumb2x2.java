package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.PictureHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.SequenceHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.dct.IDCT2x2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;

public class Mpeg2Thumb2x2 extends MPEGDecoder {

    private MPEGPred localPred;

    private MPEGPred oldPred;

    public static int[] BLOCK_POS_X = new int[] { 0, 2, 0, 2, 0, 0, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 2, 2, 2, 2 };

    public static int[] BLOCK_POS_Y = new int[] { 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1 };

    public static int[][] scan2x2 = new int[][] { { 0, 1, 2, 4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 }, { 0, 2, 4, 4, 1, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 } };

    @Override
    protected void blockIntra(BitReader bits, VLC vlcCoeff, int[] block, int[] intra_dc_predictor, int blkIdx, int[] scan, int escSize, int intra_dc_mult, int qScale, int[] qmat) {
        int cc = MPEGConst.BLOCK_TO_CC[blkIdx];
        int size = (cc == 0 ? MPEGConst.vlcDCSizeLuma : MPEGConst.vlcDCSizeChroma).readVLC(bits);
        int delta = size != 0 ? mpegSigned(bits, size) : 0;
        intra_dc_predictor[cc] += delta;
        block[0] = intra_dc_predictor[cc] * intra_dc_mult;
        block[1] = block[2] = block[3] = 0;
        int readVLC = 0;
        int idx = 0;
        while (idx < 6) {
            readVLC = vlcCoeff.readVLC(bits);
            if (readVLC == 2048) {
                break;
            }
            int level;
            if (readVLC == 2049) {
                idx += bits.readNBit(6) + 1;
                level = twosSigned(bits, escSize) * qScale * qmat[idx];
                level = level >= 0 ? level >> 4 : -(-level >> 4);
            } else {
                idx += (readVLC >> 6) + 1;
                level = toSigned((readVLC & 63) * qScale * qmat[idx] >> 4, bits.read1Bit());
            }
            block[scan[idx]] = level;
        }
        if (readVLC != 2048) {
            this.finishOff(bits, idx, vlcCoeff, escSize);
        }
        IDCT2x2.idct(block, 0);
    }

    private void finishOff(BitReader bits, int idx, VLC vlcCoeff, int escSize) {
        while (idx < 64) {
            int readVLC = vlcCoeff.readVLC(bits);
            if (readVLC != 2048) {
                if (readVLC == 2049) {
                    idx += bits.readNBit(6) + 1;
                    bits.readNBit(escSize);
                    continue;
                }
                bits.read1Bit();
                continue;
            }
            break;
        }
    }

    @Override
    protected void blockInter(BitReader bits, VLC vlcCoeff, int[] block, int[] scan, int escSize, int qScale, int[] qmat) {
        block[1] = block[2] = block[3] = 0;
        int idx = -1;
        if (vlcCoeff == MPEGConst.vlcCoeff0 && bits.checkNBit(1) == 1) {
            bits.read1Bit();
            block[0] = toSigned(quantInter(1, qScale * qmat[0]), bits.read1Bit());
            idx++;
        } else {
            block[0] = 0;
        }
        int readVLC = 0;
        while (idx < 6) {
            readVLC = vlcCoeff.readVLC(bits);
            if (readVLC == 2048) {
                break;
            }
            int ac;
            if (readVLC == 2049) {
                idx += bits.readNBit(6) + 1;
                ac = quantInterSigned(twosSigned(bits, escSize), qScale * qmat[idx]);
            } else {
                idx += (readVLC >> 6) + 1;
                ac = toSigned(quantInter(readVLC & 63, qScale * qmat[idx]), bits.read1Bit());
            }
            block[scan[idx]] = ac;
        }
        if (readVLC != 2048) {
            this.finishOff(bits, idx, vlcCoeff, escSize);
        }
        IDCT2x2.idct(block, 0);
    }

    @Override
    public int decodeMacroblock(PictureHeader ph, MPEGDecoder.Context context, int prevAddr, int[] qScaleCode, byte[][] buf, int stride, BitReader bits, int vertOff, int vertStep, MPEGPred pred) {
        if (this.localPred == null || this.oldPred != pred) {
            this.localPred = new MPEGPredQuad(pred);
            this.oldPred = pred;
        }
        return super.decodeMacroblock(ph, context, prevAddr, qScaleCode, buf, stride, bits, vertOff, vertStep, this.localPred);
    }

    @Override
    protected void mapBlock(int[] block, int[] out, int blkIdx, int dctType, int chromaFormat) {
        int stepVert = chromaFormat != 1 || blkIdx != 4 && blkIdx != 5 ? dctType : 0;
        int log2stride = blkIdx < 4 ? 2 : 2 - MPEGConst.SQUEEZE_X[chromaFormat];
        int blkIdxExt = blkIdx + (dctType << 4);
        int x = BLOCK_POS_X[blkIdxExt];
        int y = BLOCK_POS_Y[blkIdxExt];
        int off = (y << log2stride) + x;
        int stride = 1 << log2stride + stepVert;
        out[off] += block[0];
        out[off + 1] = out[off + 1] + block[1];
        out[off + stride] = out[off + stride] + block[2];
        out[off + stride + 1] = out[off + stride + 1] + block[3];
    }

    @Override
    protected void put(int[][] mbPix, byte[][] buf, int stride, int chromaFormat, int mbX, int mbY, int width, int height, int vertOff, int vertStep) {
        int chromaStride = stride + (1 << MPEGConst.SQUEEZE_X[chromaFormat]) - 1 >> MPEGConst.SQUEEZE_X[chromaFormat];
        int chromaMBW = 2 - MPEGConst.SQUEEZE_X[chromaFormat];
        int chromaMBH = 2 - MPEGConst.SQUEEZE_Y[chromaFormat];
        this.putSub(buf[0], (mbY << 2) * (stride << vertStep) + vertOff * stride + (mbX << 2), stride << vertStep, mbPix[0], 2, 2);
        this.putSub(buf[1], (mbY << chromaMBH) * (chromaStride << vertStep) + vertOff * chromaStride + (mbX << chromaMBW), chromaStride << vertStep, mbPix[1], chromaMBW, chromaMBH);
        this.putSub(buf[2], (mbY << chromaMBH) * (chromaStride << vertStep) + vertOff * chromaStride + (mbX << chromaMBW), chromaStride << vertStep, mbPix[2], chromaMBW, chromaMBH);
    }

    @Override
    protected void putSub(byte[] big, int off, int stride, int[] block, int mbW, int mbH) {
        int blOff = 0;
        if (mbW == 1) {
            big[off] = clipTo8Bit(block[blOff]);
            big[off + 1] = clipTo8Bit(block[blOff + 1]);
            big[off + stride] = clipTo8Bit(block[blOff + 2]);
            big[off + stride + 1] = clipTo8Bit(block[blOff + 3]);
            if (mbH == 2) {
                off += stride << 1;
                big[off] = clipTo8Bit(block[blOff + 4]);
                big[off + 1] = clipTo8Bit(block[blOff + 5]);
                big[off + stride] = clipTo8Bit(block[blOff + 6]);
                big[off + stride + 1] = clipTo8Bit(block[blOff + 7]);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                big[off] = clipTo8Bit(block[blOff]);
                big[off + 1] = clipTo8Bit(block[blOff + 1]);
                big[off + 2] = clipTo8Bit(block[blOff + 2]);
                big[off + 3] = clipTo8Bit(block[blOff + 3]);
                off += stride;
                blOff += 4;
            }
        }
    }

    @Override
    protected MPEGDecoder.Context initContext(SequenceHeader sh, PictureHeader ph) {
        MPEGDecoder.Context context = super.initContext(sh, ph);
        context.codedWidth >>= 2;
        context.codedHeight >>= 2;
        context.picWidth >>= 2;
        context.picHeight >>= 2;
        context.scan = scan2x2[ph.pictureCodingExtension == null ? 0 : ph.pictureCodingExtension.alternate_scan];
        return context;
    }
}