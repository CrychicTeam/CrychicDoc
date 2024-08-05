package com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg;

import com.github.alexthe666.citadel.repack.jcodec.common.dct.IDCT2x2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rect;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.nio.ByteBuffer;

public class JpegToThumb2x2 extends JpegDecoder {

    private static final int[] mapping2x2 = new int[] { 0, 1, 2, 4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 };

    @Override
    void decodeBlock(BitReader bits, int[] dcPredictor, int[][] quant, VLC[] huff, Picture result, int[] buf, int blkX, int blkY, int plane, int chroma, int field, int step) {
        buf[1] = buf[2] = buf[3] = 0;
        dcPredictor[plane] = buf[0] = readDCValue(bits, huff[chroma]) * quant[chroma][0] + dcPredictor[plane];
        this.readACValues(bits, buf, huff[chroma + 2], quant[chroma]);
        IDCT2x2.idct(buf, 0);
        putBlock2x2(result.getPlaneData(plane), result.getPlaneWidth(plane), buf, blkX, blkY, field, step);
    }

    private static void putBlock2x2(byte[] plane, int stride, int[] patch, int x, int y, int field, int step) {
        stride >>= 2;
        int dstride = stride * step;
        int off = field * stride + (y >> 2) * dstride + (x >> 2);
        plane[off] = (byte) (MathUtil.clip(patch[0], 0, 255) - 128);
        plane[off + 1] = (byte) (MathUtil.clip(patch[1], 0, 255) - 128);
        plane[off + dstride] = (byte) (MathUtil.clip(patch[2], 0, 255) - 128);
        plane[off + dstride + 1] = (byte) (MathUtil.clip(patch[3], 0, 255) - 128);
    }

    @Override
    void readACValues(BitReader _in, int[] target, VLC table, int[] quantTable) {
        int curOff = 1;
        int code;
        do {
            code = table.readVLC16(_in);
            if (code == 240) {
                curOff += 16;
            } else if (code > 0) {
                int rle = code >> 4;
                curOff += rle;
                int len = code & 15;
                target[mapping2x2[curOff]] = toValue(_in.readNBit(len), len) * quantTable[curOff];
                curOff++;
            }
        } while (code != 0 && curOff < 5);
        if (code != 0) {
            do {
                code = table.readVLC16(_in);
                if (code == 240) {
                    curOff += 16;
                } else if (code > 0) {
                    int rle = code >> 4;
                    curOff += rle;
                    int len = code & 15;
                    _in.skip(len);
                    curOff++;
                }
            } while (code != 0 && curOff < 64);
        }
    }

    @Override
    public Picture decodeField(ByteBuffer data, byte[][] data2, int field, int step) {
        Picture res = super.decodeField(data, data2, field, step);
        return new Picture(res.getWidth() >> 2, res.getHeight() >> 2, res.getData(), (byte[][]) null, res.getColor(), 0, new Rect(0, 0, res.getCroppedWidth() >> 2, res.getCroppedHeight() >> 2));
    }
}