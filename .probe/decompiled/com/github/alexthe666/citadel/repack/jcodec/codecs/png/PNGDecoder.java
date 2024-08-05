package com.github.alexthe666.citadel.repack.jcodec.codecs.png;

import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class PNGDecoder extends VideoDecoder {

    private static final int FILTER_TYPE_LOCO = 64;

    private static final int FILTER_VALUE_NONE = 0;

    private static final int FILTER_VALUE_SUB = 1;

    private static final int FILTER_VALUE_UP = 2;

    private static final int FILTER_VALUE_AVG = 3;

    private static final int FILTER_VALUE_PAETH = 4;

    private static final int PNG_COLOR_TYPE_GRAY = 0;

    private static final int PNG_COLOR_TYPE_PALETTE = 3;

    private static final int PNG_COLOR_TYPE_RGB = 2;

    private static final int alphaR = 127;

    private static final int alphaG = 127;

    private static final int alphaB = 127;

    private static final int[] logPassStep = new int[] { 3, 3, 2, 2, 1, 1, 0 };

    private static final int[] logPassRowStep = new int[] { 3, 3, 3, 2, 2, 1, 1 };

    private static final int[] passOff = new int[] { 0, 4, 0, 2, 0, 1, 0 };

    private static final int[] passRowOff = new int[] { 0, 0, 4, 0, 2, 0, 1 };

    private byte[] ca = new byte[4];

    @Override
    public Picture decodeFrame(ByteBuffer data, byte[][] buffer) {
        if (!ispng(data)) {
            throw new RuntimeException("Not a PNG file.");
        } else {
            IHDR ihdr = null;
            PNGDecoder.PLTE plte = null;
            PNGDecoder.TRNS trns = null;
            List<ByteBuffer> list = new ArrayList();
            while (data.remaining() >= 8) {
                int length = data.getInt();
                int tag = data.getInt();
                if (data.remaining() < length) {
                    break;
                }
                switch(tag) {
                    case 1229209940:
                        list.add(NIOUtils.read(data, length));
                        NIOUtils.skip(data, 4);
                        break;
                    case 1229278788:
                        NIOUtils.skip(data, 4);
                        break;
                    case 1229472850:
                        ihdr = new IHDR();
                        ihdr.parse(data);
                        break;
                    case 1347179589:
                        plte = new PNGDecoder.PLTE();
                        plte.parse(data, length);
                        break;
                    case 1951551059:
                        if (ihdr == null) {
                            throw new IllegalStateException("tRNS tag before IHDR");
                        }
                        trns = new PNGDecoder.TRNS(ihdr.colorType);
                        trns.parse(data, length);
                        break;
                    default:
                        data.position(data.position() + length + 4);
                }
            }
            if (ihdr != null) {
                try {
                    this.decodeData(ihdr, plte, trns, list, buffer);
                } catch (DataFormatException var9) {
                    return null;
                }
                return Picture.createPicture(ihdr.width, ihdr.height, buffer, ihdr.colorSpace());
            } else {
                throw new IllegalStateException("no IHDR tag");
            }
        }
    }

    private void decodeData(IHDR ihdr, PNGDecoder.PLTE plte, PNGDecoder.TRNS trns, List<ByteBuffer> list, byte[][] buffer) throws DataFormatException {
        int bpp = ihdr.getBitsPerPixel() + 7 >> 3;
        int passes = ihdr.interlaceType == 0 ? 1 : 7;
        Inflater inflater = new Inflater();
        Iterator<ByteBuffer> it = list.iterator();
        for (int pass = 0; pass < passes; pass++) {
            int rowSize;
            int rowStart;
            int rowStep;
            int colStart;
            int colStep;
            if (ihdr.interlaceType == 0) {
                rowSize = ihdr.rowSize() + 1;
                rowStart = 0;
                colStart = 0;
                rowStep = 1;
                colStep = 1;
            } else {
                int round = (1 << logPassStep[pass]) - 1;
                rowSize = (ihdr.width + round >> logPassStep[pass]) + 1;
                rowStart = passRowOff[pass];
                rowStep = 1 << logPassRowStep[pass];
                colStart = passOff[pass];
                colStep = 1 << logPassStep[pass];
            }
            byte[] lastRow = new byte[rowSize - 1];
            byte[] uncompressed = new byte[rowSize];
            int bptr = 3 * (ihdr.width * rowStart + colStart);
            for (int row = rowStart; row < ihdr.height; row += rowStep) {
                int count = inflater.inflate(uncompressed);
                if (count < uncompressed.length && inflater.needsInput()) {
                    if (!it.hasNext()) {
                        Logger.warn(String.format("Data truncation at row %d", row));
                        break;
                    }
                    ByteBuffer next = (ByteBuffer) it.next();
                    inflater.setInput(NIOUtils.toArray(next));
                    int toRead = uncompressed.length - count;
                    count = inflater.inflate(uncompressed, count, toRead);
                    if (count != toRead) {
                        Logger.warn(String.format("Data truncation at row %d", row));
                        break;
                    }
                }
                int filter = uncompressed[0];
                switch(filter) {
                    case 0:
                        System.arraycopy(uncompressed, 1, lastRow, 0, rowSize - 1);
                        break;
                    case 1:
                        filterSub(uncompressed, rowSize - 1, lastRow, bpp);
                        break;
                    case 2:
                        filterUp(uncompressed, rowSize - 1, lastRow);
                        break;
                    case 3:
                        filterAvg(uncompressed, rowSize - 1, lastRow, bpp);
                        break;
                    case 4:
                        this.filterPaeth(uncompressed, rowSize - 1, lastRow, bpp);
                }
                int bptrWas = bptr;
                if ((ihdr.colorType & 1) != 0) {
                    for (int i = 0; i < rowSize - 1; bptr += 3 * colStep) {
                        int plt = plte.palette[lastRow[i] & 255];
                        buffer[0][bptr] = (byte) ((plt >> 16 & 0xFF) - 128);
                        buffer[0][bptr + 1] = (byte) ((plt >> 8 & 0xFF) - 128);
                        buffer[0][bptr + 2] = (byte) ((plt & 0xFF) - 128);
                        i += bpp;
                    }
                } else if ((ihdr.colorType & 2) != 0) {
                    for (int i = 0; i < rowSize - 1; bptr += 3 * colStep) {
                        buffer[0][bptr] = (byte) ((lastRow[i] & 255) - 128);
                        buffer[0][bptr + 1] = (byte) ((lastRow[i + 1] & 255) - 128);
                        buffer[0][bptr + 2] = (byte) ((lastRow[i + 2] & 255) - 128);
                        i += bpp;
                    }
                } else {
                    for (int i = 0; i < rowSize - 1; bptr += 3 * colStep) {
                        buffer[0][bptr] = buffer[0][bptr + 1] = buffer[0][bptr + 2] = (byte) ((lastRow[i] & 255) - 128);
                        i += bpp;
                    }
                }
                if ((ihdr.colorType & 4) != 0) {
                    int i = bpp - 1;
                    for (int j = bptrWas; i < rowSize - 1; j += 3 * colStep) {
                        int alpha = lastRow[i] & 255;
                        int nalpha = 256 - alpha;
                        buffer[0][j] = (byte) (127 * nalpha + buffer[0][j] * alpha >> 8);
                        buffer[0][j + 1] = (byte) (127 * nalpha + buffer[0][j + 1] * alpha >> 8);
                        buffer[0][j + 2] = (byte) (127 * nalpha + buffer[0][j + 2] * alpha >> 8);
                        i += bpp;
                    }
                } else if (trns != null) {
                    if (ihdr.colorType == 3) {
                        int i = 0;
                        for (int j = bptrWas; i < rowSize - 1; j += 3 * colStep) {
                            int alpha = trns.alphaPal[lastRow[i] & 255] & 255;
                            int nalpha = 256 - alpha;
                            buffer[0][j] = (byte) (127 * nalpha + buffer[0][j] * alpha >> 8);
                            buffer[0][j + 1] = (byte) (127 * nalpha + buffer[0][j + 1] * alpha >> 8);
                            buffer[0][j + 2] = (byte) (127 * nalpha + buffer[0][j + 2] * alpha >> 8);
                            i++;
                        }
                    } else if (ihdr.colorType == 2) {
                        int ar = (trns.alphaR & 255) - 128;
                        int ag = (trns.alphaG & 255) - 128;
                        int ab = (trns.alphaB & 255) - 128;
                        if (ab != 127 || ag != 127 || ar != 127) {
                            int i = 0;
                            for (int j = bptrWas; i < rowSize - 1; j += 3 * colStep) {
                                if (buffer[0][j] == ar && buffer[0][j + 1] == ag && buffer[0][j + 2] == ab) {
                                    buffer[0][j] = 127;
                                    buffer[0][j + 1] = 127;
                                    buffer[0][j + 2] = 127;
                                }
                                i += bpp;
                            }
                        }
                    } else if (ihdr.colorType == 0) {
                        int i = 0;
                        for (int j = bptrWas; i < rowSize - 1; j += 3 * colStep) {
                            if (lastRow[i] == trns.alphaGrey) {
                                buffer[0][j] = 127;
                                buffer[0][j + 1] = 127;
                                buffer[0][j + 2] = 127;
                            }
                            i++;
                        }
                    }
                }
                bptr = bptrWas + 3 * ihdr.width * rowStep;
            }
        }
    }

    private void filterPaeth(byte[] uncompressed, int rowSize, byte[] lastRow, int bpp) {
        for (int i = 0; i < bpp; i++) {
            this.ca[i] = lastRow[i];
            lastRow[i] = (byte) ((uncompressed[i + 1] & 255) + (lastRow[i] & 255));
        }
        for (int i = bpp; i < rowSize; i++) {
            int a = lastRow[i - bpp] & 255;
            int b = lastRow[i] & 255;
            int c = this.ca[i % bpp] & 255;
            int p = b - c;
            int pc = a - c;
            int pa = MathUtil.abs(p);
            int pb = MathUtil.abs(pc);
            pc = MathUtil.abs(p + pc);
            if (pa <= pb && pa <= pc) {
                p = a;
            } else if (pb <= pc) {
                p = b;
            } else {
                p = c;
            }
            this.ca[i % bpp] = lastRow[i];
            lastRow[i] = (byte) (p + (uncompressed[i + 1] & 255));
        }
    }

    private static void filterSub(byte[] uncompressed, int rowSize, byte[] lastRow, int bpp) {
        switch(bpp) {
            case 1:
                filterSub1(uncompressed, lastRow, rowSize);
                break;
            case 2:
                filterSub2(uncompressed, lastRow, rowSize);
                break;
            case 3:
                filterSub3(uncompressed, lastRow, rowSize);
                break;
            default:
                filterSub4(uncompressed, lastRow, rowSize);
        }
    }

    private static void filterAvg(byte[] uncompressed, int rowSize, byte[] lastRow, int bpp) {
        switch(bpp) {
            case 1:
                filterAvg1(uncompressed, lastRow, rowSize);
                break;
            case 2:
                filterAvg2(uncompressed, lastRow, rowSize);
                break;
            case 3:
                filterAvg3(uncompressed, lastRow, rowSize);
                break;
            default:
                filterAvg4(uncompressed, lastRow, rowSize);
        }
    }

    private static void filterSub1(byte[] uncompressed, byte[] lastRow, int rowSize) {
        byte p = lastRow[0] = uncompressed[1];
        for (int i = 1; i < rowSize; i++) {
            p = lastRow[i] = (byte) ((p & 255) + (uncompressed[i + 1] & 255));
        }
    }

    private static void filterUp(byte[] uncompressed, int rowSize, byte[] lastRow) {
        for (int i = 0; i < rowSize; i++) {
            lastRow[i] = (byte) ((lastRow[i] & 255) + (uncompressed[i + 1] & 255));
        }
    }

    private static void filterAvg1(byte[] uncompressed, byte[] lastRow, int rowSize) {
        byte p = lastRow[0] = (byte) ((uncompressed[1] & 255) + ((lastRow[0] & 255) >> 1));
        for (int i = 1; i < rowSize; i++) {
            p = lastRow[i] = (byte) (((lastRow[i] & 255) + (p & 255) >> 1) + (uncompressed[i + 1] & 255));
        }
    }

    private static void filterSub2(byte[] uncompressed, byte[] lastRow, int rowSize) {
        byte p0 = lastRow[0] = uncompressed[1];
        byte p1 = lastRow[1] = uncompressed[2];
        for (int i = 2; i < rowSize; i += 2) {
            p0 = lastRow[i] = (byte) ((p0 & 255) + (uncompressed[1 + i] & 255));
            p1 = lastRow[i + 1] = (byte) ((p1 & 255) + (uncompressed[2 + i] & 255));
        }
    }

    private static void filterAvg2(byte[] uncompressed, byte[] lastRow, int rowSize) {
        byte p0 = lastRow[0] = (byte) ((uncompressed[1] & 255) + ((lastRow[0] & 255) >> 1));
        byte p1 = lastRow[1] = (byte) ((uncompressed[2] & 255) + ((lastRow[1] & 255) >> 1));
        for (int i = 2; i < rowSize; i += 2) {
            p0 = lastRow[i] = (byte) (((lastRow[i] & 255) + (p0 & 255) >> 1) + (uncompressed[1 + i] & 255));
            p1 = lastRow[i + 1] = (byte) (((lastRow[i + 1] & 255) + (p1 & 255) >> 1) + (uncompressed[i + 2] & 255));
        }
    }

    private static void filterSub3(byte[] uncompressed, byte[] lastRow, int rowSize) {
        byte p0 = lastRow[0] = uncompressed[1];
        byte p1 = lastRow[1] = uncompressed[2];
        byte p2 = lastRow[2] = uncompressed[3];
        for (int i = 3; i < rowSize; i += 3) {
            p0 = lastRow[i] = (byte) ((p0 & 255) + (uncompressed[i + 1] & 255));
            p1 = lastRow[i + 1] = (byte) ((p1 & 255) + (uncompressed[i + 2] & 255));
            p2 = lastRow[i + 2] = (byte) ((p2 & 255) + (uncompressed[i + 3] & 255));
        }
    }

    private static void filterAvg3(byte[] uncompressed, byte[] lastRow, int rowSize) {
        byte p0 = lastRow[0] = (byte) ((uncompressed[1] & 255) + ((lastRow[0] & 255) >> 1));
        byte p1 = lastRow[1] = (byte) ((uncompressed[2] & 255) + ((lastRow[1] & 255) >> 1));
        byte p2 = lastRow[2] = (byte) ((uncompressed[3] & 255) + ((lastRow[2] & 255) >> 1));
        for (int i = 3; i < rowSize; i += 3) {
            p0 = lastRow[i] = (byte) (((lastRow[i] & 255) + (p0 & 255) >> 1) + (uncompressed[i + 1] & 255));
            p1 = lastRow[i + 1] = (byte) (((lastRow[i + 1] & 255) + (p1 & 255) >> 1) + (uncompressed[i + 2] & 255));
            p2 = lastRow[i + 2] = (byte) (((lastRow[i + 2] & 255) + (p2 & 255) >> 1) + (uncompressed[i + 3] & 255));
        }
    }

    private static void filterSub4(byte[] uncompressed, byte[] lastRow, int rowSize) {
        byte p0 = lastRow[0] = uncompressed[1];
        byte p1 = lastRow[1] = uncompressed[2];
        byte p2 = lastRow[2] = uncompressed[3];
        byte p3 = lastRow[3] = uncompressed[4];
        for (int i = 4; i < rowSize; i += 4) {
            p0 = lastRow[i] = (byte) ((p0 & 255) + (uncompressed[i + 1] & 255));
            p1 = lastRow[i + 1] = (byte) ((p1 & 255) + (uncompressed[i + 2] & 255));
            p2 = lastRow[i + 2] = (byte) ((p2 & 255) + (uncompressed[i + 3] & 255));
            p3 = lastRow[i + 3] = (byte) ((p3 & 255) + (uncompressed[i + 4] & 255));
        }
    }

    private static void filterAvg4(byte[] uncompressed, byte[] lastRow, int rowSize) {
        byte p0 = lastRow[0] = (byte) ((uncompressed[1] & 255) + ((lastRow[0] & 255) >> 1));
        byte p1 = lastRow[1] = (byte) ((uncompressed[2] & 255) + ((lastRow[1] & 255) >> 1));
        byte p2 = lastRow[2] = (byte) ((uncompressed[3] & 255) + ((lastRow[2] & 255) >> 1));
        byte p3 = lastRow[3] = (byte) ((uncompressed[4] & 255) + ((lastRow[3] & 255) >> 1));
        for (int i = 4; i < rowSize; i += 4) {
            p0 = lastRow[i] = (byte) (((lastRow[i] & 255) + (p0 & 255) >> 1) + (uncompressed[i + 1] & 255));
            p1 = lastRow[i + 1] = (byte) (((lastRow[i + 1] & 255) + (p1 & 255) >> 1) + (uncompressed[i + 2] & 255));
            p2 = lastRow[i + 2] = (byte) (((lastRow[i + 2] & 255) + (p2 & 255) >> 1) + (uncompressed[i + 3] & 255));
            p3 = lastRow[i + 3] = (byte) (((lastRow[i + 3] & 255) + (p3 & 255) >> 1) + (uncompressed[i + 4] & 255));
        }
    }

    @Override
    public VideoCodecMeta getCodecMeta(ByteBuffer _data) {
        ByteBuffer data = _data.duplicate();
        if (!ispng(data)) {
            throw new RuntimeException("Not a PNG file.");
        } else {
            while (data.remaining() >= 8) {
                int length = data.getInt();
                int tag = data.getInt();
                if (data.remaining() < length) {
                    break;
                }
                switch(tag) {
                    case 1229472850:
                        IHDR ihdr = new IHDR();
                        ihdr.parse(data);
                        return VideoCodecMeta.createSimpleVideoCodecMeta(new Size(ihdr.width, ihdr.height), ColorSpace.RGB);
                    default:
                        data.position(data.position() + length + 4);
                }
            }
            return null;
        }
    }

    private static boolean ispng(ByteBuffer data) {
        int sighi = data.getInt();
        int siglo = data.getInt();
        return (sighi == -1991225785 || sighi == -1974645177) && (siglo == 218765834 || siglo == 218765834);
    }

    public static int probe(ByteBuffer data) {
        return !ispng(data) ? 100 : 0;
    }

    public static byte[] deflate(byte[] data, Inflater inflater) throws DataFormatException {
        inflater.setInput(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[16384];
        while (!inflater.needsInput()) {
            int count = inflater.inflate(buffer);
            baos.write(buffer, 0, count);
            System.out.println(baos.size());
        }
        return baos.toByteArray();
    }

    private static class PLTE {

        int[] palette;

        private PLTE() {
        }

        public void parse(ByteBuffer data, int length) {
            if (length % 3 == 0 && length <= 768) {
                int n = length / 3;
                this.palette = new int[n];
                int i;
                for (i = 0; i < n; i++) {
                    this.palette[i] = 0xFF000000 | (data.get() & 255) << 16 | (data.get() & 255) << 8 | data.get() & 255;
                }
                while (i < 256) {
                    this.palette[i] = -16777216;
                    i++;
                }
                data.getInt();
            } else {
                throw new RuntimeException("Invalid data");
            }
        }
    }

    public static class TRNS {

        private int colorType;

        byte[] alphaPal;

        byte alphaGrey;

        byte alphaR;

        byte alphaG;

        byte alphaB;

        TRNS(byte colorType) {
            this.colorType = colorType;
        }

        public void parse(ByteBuffer data, int length) {
            if (this.colorType == 3) {
                this.alphaPal = new byte[256];
                data.get(this.alphaPal, 0, length);
                for (int i = length; i < 256; i++) {
                    this.alphaPal[i] = -1;
                }
            } else if (this.colorType == 0) {
                this.alphaGrey = data.get();
            } else if (this.colorType == 2) {
                this.alphaR = data.get();
                this.alphaG = data.get();
                this.alphaG = data.get();
            }
            data.getInt();
        }
    }
}