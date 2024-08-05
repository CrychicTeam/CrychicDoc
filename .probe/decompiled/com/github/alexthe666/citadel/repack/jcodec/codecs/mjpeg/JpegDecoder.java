package com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg;

import com.github.alexthe666.citadel.repack.jcodec.api.UnhandledStateException;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.dct.SimpleIDCT10Bit;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLCBuilder;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rect;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class JpegDecoder extends VideoDecoder {

    private boolean interlace;

    private boolean topFieldFirst;

    int[] buf = new int[64];

    public void setInterlace(boolean interlace, boolean topFieldFirst) {
        this.interlace = interlace;
        this.topFieldFirst = topFieldFirst;
    }

    private Picture decodeScan(ByteBuffer data, FrameHeader header, ScanHeader scan, VLC[] huffTables, int[][] quant, byte[][] data2, int field, int step) {
        int blockW = header.getHmax();
        int blockH = header.getVmax();
        int mcuW = blockW << 3;
        int mcuH = blockH << 3;
        int width = header.width;
        int height = header.height;
        int xBlocks = width + mcuW - 1 >> blockW + 2;
        int yBlocks = height + mcuH - 1 >> blockH + 2;
        int nn = blockW + blockH;
        Picture result = new Picture(xBlocks << blockW + 2, yBlocks << blockH + 2, data2, (byte[][]) null, nn == 4 ? ColorSpace.YUV420J : (nn == 3 ? ColorSpace.YUV422J : ColorSpace.YUV444J), 0, new Rect(0, 0, width, height));
        BitReader bits = BitReader.createBitReader(data);
        int[] dcPredictor = new int[] { 1024, 1024, 1024 };
        for (int by = 0; by < yBlocks; by++) {
            for (int bx = 0; bx < xBlocks && bits.moreData(); bx++) {
                this.decodeMCU(bits, dcPredictor, quant, huffTables, result, bx, by, blockW, blockH, field, step);
            }
        }
        return result;
    }

    private static void putBlock(byte[] plane, int stride, int[] patch, int x, int y, int field, int step) {
        int dstride = step * stride;
        int i = 0;
        int off = field * stride + y * dstride + x;
        for (int poff = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                plane[j + off] = (byte) (MathUtil.clip(patch[j + poff], 0, 255) - 128);
            }
            off += dstride;
            poff += 8;
        }
    }

    private void decodeMCU(BitReader bits, int[] dcPredictor, int[][] quant, VLC[] huff, Picture result, int bx, int by, int blockH, int blockV, int field, int step) {
        int sx = bx << blockH - 1;
        int sy = by << blockV - 1;
        for (int i = 0; i < blockV; i++) {
            for (int j = 0; j < blockH; j++) {
                this.decodeBlock(bits, dcPredictor, quant, huff, result, this.buf, sx + j << 3, sy + i << 3, 0, 0, field, step);
            }
        }
        this.decodeBlock(bits, dcPredictor, quant, huff, result, this.buf, bx << 3, by << 3, 1, 1, field, step);
        this.decodeBlock(bits, dcPredictor, quant, huff, result, this.buf, bx << 3, by << 3, 2, 1, field, step);
    }

    void decodeBlock(BitReader bits, int[] dcPredictor, int[][] quant, VLC[] huff, Picture result, int[] buf, int blkX, int blkY, int plane, int chroma, int field, int step) {
        Arrays.fill(buf, 0);
        dcPredictor[plane] = buf[0] = readDCValue(bits, huff[chroma]) * quant[chroma][0] + dcPredictor[plane];
        this.readACValues(bits, buf, huff[chroma + 2], quant[chroma]);
        SimpleIDCT10Bit.idct10(buf, 0);
        putBlock(result.getPlaneData(plane), result.getPlaneWidth(plane), buf, blkX, blkY, field, step);
    }

    static int readDCValue(BitReader _in, VLC table) {
        int code = table.readVLC16(_in);
        return code != 0 ? toValue(_in.readNBit(code), code) : 0;
    }

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
                target[JpegConst.naturalOrder[curOff]] = toValue(_in.readNBit(len), len) * quantTable[curOff];
                curOff++;
            }
        } while (code != 0 && curOff < 64);
    }

    static int toValue(int raw, int length) {
        return length >= 1 && raw < 1 << length - 1 ? -(1 << length) + 1 + raw : raw;
    }

    @Override
    public Picture decodeFrame(ByteBuffer data, byte[][] data2) {
        if (this.interlace) {
            Picture r1 = this.decodeField(data, data2, this.topFieldFirst ? 0 : 1, 2);
            Picture r2 = this.decodeField(data, data2, this.topFieldFirst ? 1 : 0, 2);
            return Picture.createPicture(r1.getWidth(), r1.getHeight() << 1, data2, r1.getColor());
        } else {
            return this.decodeField(data, data2, 0, 1);
        }
    }

    public Picture decodeField(ByteBuffer data, byte[][] data2, int field, int step) {
        Picture result = null;
        FrameHeader header = null;
        VLC[] huffTables = new VLC[] { JpegConst.YDC_DEFAULT, JpegConst.CDC_DEFAULT, JpegConst.YAC_DEFAULT, JpegConst.CAC_DEFAULT };
        int[][] quant = new int[][] { JpegConst.DEFAULT_QUANT_LUMA, JpegConst.DEFAULT_QUANT_CHROMA };
        ScanHeader scan = null;
        boolean skipToNext = false;
        while (data.hasRemaining()) {
            int marker;
            if (!skipToNext) {
                marker = data.get() & 255;
            } else {
                while ((marker = data.get() & 255) != 255) {
                }
            }
            skipToNext = false;
            if (marker != 0) {
                if (marker != 255) {
                    throw new RuntimeException("@" + Long.toHexString((long) data.position()) + " Marker expected: 0x" + Integer.toHexString(marker));
                }
                int b;
                while ((b = data.get() & 255) == 255) {
                }
                if (b == 192) {
                    header = FrameHeader.read(data);
                } else if (b == 196) {
                    int len1 = data.getShort() & '\uffff';
                    ByteBuffer buf = NIOUtils.read(data, len1 - 2);
                    while (buf.hasRemaining()) {
                        int tableNo = buf.get() & 255;
                        huffTables[tableNo & 1 | tableNo >> 3 & 2] = readHuffmanTable(buf);
                    }
                } else if (b == 219) {
                    int len4 = data.getShort() & '\uffff';
                    ByteBuffer buf = NIOUtils.read(data, len4 - 2);
                    while (buf.hasRemaining()) {
                        int ind = buf.get() & 255;
                        quant[ind] = readQuantTable(buf);
                    }
                } else if (b == 218) {
                    if (scan != null) {
                        throw new UnhandledStateException("unhandled - more than one scan header");
                    }
                    scan = ScanHeader.read(data);
                    result = this.decodeScan(readToMarker(data), header, scan, huffTables, quant, data2, field, step);
                } else if (b != 216 && (b < 208 || b > 215)) {
                    if (b == 217) {
                        break;
                    }
                    if (b >= 224 && b <= 254) {
                        int len3 = data.getShort() & '\uffff';
                        NIOUtils.read(data, len3 - 2);
                    } else if (b == 221) {
                        Logger.warn("DRI not supported.");
                        skipToNext = true;
                    } else {
                        if (b != 0) {
                            Logger.warn("unhandled marker " + JpegConst.markerToString(b));
                        }
                        skipToNext = true;
                    }
                } else {
                    Logger.warn("SOI not supported.");
                    skipToNext = true;
                }
            }
        }
        return result;
    }

    private static ByteBuffer readToMarker(ByteBuffer data) {
        ByteBuffer out = ByteBuffer.allocate(data.remaining());
        while (data.hasRemaining()) {
            byte b0 = data.get();
            if (b0 == -1) {
                byte b1 = data.get();
                if (b1 != 0) {
                    data.position(data.position() - 2);
                    break;
                }
                out.put((byte) -1);
            } else {
                out.put(b0);
            }
        }
        out.flip();
        return out;
    }

    private static VLC readHuffmanTable(ByteBuffer data) {
        VLCBuilder builder = new VLCBuilder();
        byte[] levelSizes = NIOUtils.toArray(NIOUtils.read(data, 16));
        int levelStart = 0;
        for (int i = 0; i < 16; i++) {
            int length = levelSizes[i] & 255;
            for (int c = 0; c < length; c++) {
                int val = data.get() & 255;
                int code = levelStart++;
                builder.setInt(code, i + 1, val);
            }
            levelStart <<= 1;
        }
        return builder.getVLC();
    }

    private static int[] readQuantTable(ByteBuffer data) {
        int[] result = new int[64];
        for (int i = 0; i < 64; i++) {
            result[i] = data.get() & 255;
        }
        return result;
    }

    @Override
    public VideoCodecMeta getCodecMeta(ByteBuffer data) {
        FrameHeader header = null;
        while (data.hasRemaining()) {
            while (data.hasRemaining() && (data.get() & 255) != 255) {
            }
            int type;
            while ((type = data.get() & 255) == 255) {
            }
            if (type == 192) {
                header = FrameHeader.read(data);
                break;
            }
        }
        if (header != null) {
            int blockW = header.getHmax();
            int blockH = header.getVmax();
            int nn = blockW + blockH;
            ColorSpace color = nn == 4 ? ColorSpace.YUV420J : (nn == 3 ? ColorSpace.YUV422J : ColorSpace.YUV444J);
            return VideoCodecMeta.createSimpleVideoCodecMeta(new Size(header.width, header.height), color);
        } else {
            return null;
        }
    }
}