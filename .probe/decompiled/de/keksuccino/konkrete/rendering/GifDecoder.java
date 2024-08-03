package de.keksuccino.konkrete.rendering;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class GifDecoder {

    static final boolean DEBUG_MODE = false;

    public static final GifDecoder.GifImage read(byte[] in) throws IOException {
        GifDecoder decoder = new GifDecoder();
        GifDecoder.GifImage img = decoder.new GifImage();
        GifDecoder.GifFrame frame = null;
        int pos = readHeader(in, img);
        pos = readLogicalScreenDescriptor(img, in, pos);
        if (img.hasGlobColTbl) {
            img.globalColTbl = new int[img.sizeOfGlobColTbl];
            pos = readColTbl(in, img.globalColTbl, pos);
        }
        while (pos < in.length) {
            int block = in[pos] & 255;
            switch(block) {
                case 33:
                    if (pos + 1 >= in.length) {
                        throw new IOException("Unexpected end of file.");
                    }
                    switch(in[pos + 1] & 0xFF) {
                        case 1:
                            frame = null;
                            pos = readTextExtension(in, pos);
                            continue;
                        case 249:
                            if (frame == null) {
                                frame = decoder.new GifFrame();
                                img.frames.add(frame);
                            }
                            pos = readGraphicControlExt(frame, in, pos);
                            continue;
                        case 254:
                            pos = readTextExtension(in, pos);
                            continue;
                        case 255:
                            pos = readAppExt(img, in, pos);
                            continue;
                        default:
                            throw new IOException("Unknown extension at " + pos);
                    }
                case 44:
                    if (frame == null) {
                        frame = decoder.new GifFrame();
                        img.frames.add(frame);
                    }
                    pos = readImgDescr(frame, in, pos);
                    if (frame.hasLocColTbl) {
                        frame.localColTbl = new int[frame.sizeOfLocColTbl];
                        pos = readColTbl(in, frame.localColTbl, pos);
                    }
                    pos = readImgData(frame, in, pos);
                    frame = null;
                    break;
                case 59:
                    return img;
                default:
                    double progress = 1.0 * (double) pos / (double) in.length;
                    if (progress < 0.9) {
                        throw new IOException("Unknown block at: " + pos);
                    }
                    pos = in.length;
            }
        }
        return img;
    }

    public static final GifDecoder.GifImage read(InputStream is) throws IOException {
        byte[] data = new byte[is.available()];
        is.read(data, 0, data.length);
        return read(data);
    }

    static final int readAppExt(GifDecoder.GifImage img, byte[] in, int i) {
        img.appId = new String(in, i + 3, 8);
        img.appAuthCode = new String(in, i + 11, 3);
        i += 14;
        int subBlockSize = in[i] & 255;
        if (subBlockSize == 3) {
            img.repetitions = in[i + 2] & 255 | in[i + 3] & '\uff00';
            return i + 5;
        } else {
            while ((in[i] & 255) != 0) {
                i += (in[i] & 255) + 1;
            }
            return i + 1;
        }
    }

    static final int readColTbl(byte[] in, int[] colors, int i) {
        int numColors = colors.length;
        for (int c = 0; c < numColors; c++) {
            int a = 255;
            int r = in[i++] & 255;
            int g = in[i++] & 255;
            int b = in[i++] & 255;
            colors[c] = ((0xFF00 | r) << 8 | g) << 8 | b;
        }
        return i;
    }

    static final int readGraphicControlExt(GifDecoder.GifFrame fr, byte[] in, int i) {
        fr.disposalMethod = (in[i + 3] & 28) >>> 2;
        fr.transpColFlag = (in[i + 3] & 1) == 1;
        fr.delay = in[i + 4] & 255 | (in[i + 5] & 255) << 8;
        fr.transpColIndex = in[i + 6] & 255;
        return i + 8;
    }

    static final int readHeader(byte[] in, GifDecoder.GifImage img) throws IOException {
        if (in.length < 6) {
            throw new IOException("Image is truncated.");
        } else {
            img.header = new String(in, 0, 6);
            if (!img.header.equals("GIF87a") && !img.header.equals("GIF89a")) {
                throw new IOException("Invalid GIF header.");
            } else {
                return 6;
            }
        }
    }

    static final int readImgData(GifDecoder.GifFrame fr, byte[] in, int i) {
        int fileSize = in.length;
        int minCodeSize = in[i++] & 255;
        int clearCode = 1 << minCodeSize;
        fr.firstCodeSize = minCodeSize + 1;
        fr.clearCode = clearCode;
        fr.endOfInfoCode = clearCode + 1;
        int imgDataSize = readImgDataSize(in, i);
        byte[] imgData = new byte[imgDataSize + 2];
        int imgDataPos = 0;
        int subBlockSize = in[i] & 255;
        while (subBlockSize > 0) {
            try {
                int nextSubBlockSizePos = i + subBlockSize + 1;
                int nextSubBlockSize = in[nextSubBlockSizePos] & 255;
                System.arraycopy(in, i + 1, imgData, imgDataPos, subBlockSize);
                imgDataPos += subBlockSize;
                i = nextSubBlockSizePos;
                subBlockSize = nextSubBlockSize;
            } catch (Exception var12) {
                subBlockSize = fileSize - i - 1;
                System.arraycopy(in, i + 1, imgData, imgDataPos, subBlockSize);
                imgDataPos += subBlockSize;
                i += subBlockSize + 1;
                break;
            }
        }
        fr.data = imgData;
        return i + 1;
    }

    static final int readImgDataSize(byte[] in, int i) {
        int fileSize = in.length;
        int imgDataPos = 0;
        int subBlockSize = in[i] & 255;
        while (subBlockSize > 0) {
            try {
                int nextSubBlockSizePos = i + subBlockSize + 1;
                int nextSubBlockSize = in[nextSubBlockSizePos] & 255;
                imgDataPos += subBlockSize;
                i = nextSubBlockSizePos;
                subBlockSize = nextSubBlockSize;
            } catch (Exception var7) {
                subBlockSize = fileSize - i - 1;
                imgDataPos += subBlockSize;
                break;
            }
        }
        return imgDataPos;
    }

    static final int readImgDescr(GifDecoder.GifFrame fr, byte[] in, int i) {
        fr.x = in[++i] & 255 | (in[++i] & 255) << 8;
        fr.y = in[++i] & 255 | (in[++i] & 255) << 8;
        fr.w = in[++i] & 255 | (in[++i] & 255) << 8;
        fr.h = in[++i] & 255 | (in[++i] & 255) << 8;
        fr.wh = fr.w * fr.h;
        byte b = in[++i];
        fr.hasLocColTbl = (b & 128) >>> 7 == 1;
        fr.interlaceFlag = (b & 64) >>> 6 == 1;
        fr.sortFlag = (b & 32) >>> 5 == 1;
        int colTblSizePower = (b & 7) + 1;
        fr.sizeOfLocColTbl = 1 << colTblSizePower;
        return i + 1;
    }

    static final int readLogicalScreenDescriptor(GifDecoder.GifImage img, byte[] in, int i) {
        img.w = in[i] & 255 | (in[i + 1] & 255) << 8;
        img.h = in[i + 2] & 255 | (in[i + 3] & 255) << 8;
        img.wh = img.w * img.h;
        byte b = in[i + 4];
        img.hasGlobColTbl = (b & 128) >>> 7 == 1;
        int colResPower = ((b & 112) >>> 4) + 1;
        img.colorResolution = 1 << colResPower;
        img.sortFlag = (b & 8) >>> 3 == 1;
        int globColTblSizePower = (b & 7) + 1;
        img.sizeOfGlobColTbl = 1 << globColTblSizePower;
        img.bgColIndex = in[i + 5] & 255;
        img.pxAspectRatio = in[i + 6] & 255;
        return i + 7;
    }

    static final int readTextExtension(byte[] in, int pos) {
        int i = pos + 2;
        for (int subBlockSize = in[i++] & 255; subBlockSize != 0 && i < in.length; subBlockSize = in[i++] & 255) {
            i += subBlockSize;
        }
        return i;
    }

    static final class BitReader {

        private int bitPos;

        private int numBits;

        private int bitMask;

        private byte[] in;

        private final void init(byte[] in) {
            this.in = in;
            this.bitPos = 0;
        }

        private final int read() {
            int i = this.bitPos >>> 3;
            int rBits = this.bitPos & 7;
            int b0 = this.in[i++] & 255;
            int b1 = this.in[i++] & 255;
            int b2 = this.in[i] & 255;
            int buf = ((b2 << 8 | b1) << 8 | b0) >>> rBits;
            this.bitPos = this.bitPos + this.numBits;
            return buf & this.bitMask;
        }

        private final void setNumBits(int numBits) {
            this.numBits = numBits;
            this.bitMask = (1 << numBits) - 1;
        }
    }

    static final class CodeTable {

        private final int[][] tbl = new int[4096][1];

        private int initTableSize;

        private int initCodeSize;

        private int initCodeLimit;

        private int codeSize;

        private int nextCode;

        private int nextCodeLimit;

        private GifDecoder.BitReader br;

        public CodeTable() {
        }

        private final int add(int[] indices) {
            if (this.nextCode < 4096) {
                if (this.nextCode == this.nextCodeLimit && this.codeSize < 12) {
                    this.codeSize++;
                    this.br.setNumBits(this.codeSize);
                    this.nextCodeLimit = (1 << this.codeSize) - 1;
                }
                this.tbl[this.nextCode++] = indices;
            }
            return this.codeSize;
        }

        private final int clear() {
            this.codeSize = this.initCodeSize;
            this.br.setNumBits(this.codeSize);
            this.nextCodeLimit = this.initCodeLimit;
            this.nextCode = this.initTableSize;
            return this.codeSize;
        }

        private final void init(GifDecoder.GifFrame fr, int[] activeColTbl, GifDecoder.BitReader br) {
            this.br = br;
            int numColors = activeColTbl.length;
            this.initCodeSize = fr.firstCodeSize;
            this.initCodeLimit = (1 << this.initCodeSize) - 1;
            this.initTableSize = fr.endOfInfoCode + 1;
            this.nextCode = this.initTableSize;
            for (int c = numColors - 1; c >= 0; c--) {
                this.tbl[c][0] = activeColTbl[c];
            }
            this.tbl[fr.clearCode] = new int[] { fr.clearCode };
            this.tbl[fr.endOfInfoCode] = new int[] { fr.endOfInfoCode };
            if (fr.transpColFlag && fr.transpColIndex < numColors) {
                this.tbl[fr.transpColIndex][0] = 0;
            }
        }
    }

    final class GifFrame {

        private int disposalMethod;

        private boolean transpColFlag;

        private int delay;

        private int transpColIndex;

        private int x;

        private int y;

        private int w;

        private int h;

        private int wh;

        private boolean hasLocColTbl;

        private boolean interlaceFlag;

        private boolean sortFlag;

        private int sizeOfLocColTbl;

        private int[] localColTbl;

        private int firstCodeSize;

        private int clearCode;

        private int endOfInfoCode;

        private byte[] data;

        private BufferedImage img;
    }

    public final class GifImage {

        public String header;

        private int w;

        private int h;

        private int wh;

        public boolean hasGlobColTbl;

        public int colorResolution;

        public boolean sortFlag;

        public int sizeOfGlobColTbl;

        public int bgColIndex;

        public int pxAspectRatio;

        public int[] globalColTbl;

        private final List<GifDecoder.GifFrame> frames = new ArrayList(64);

        public String appId = "";

        public String appAuthCode = "";

        public int repetitions = 0;

        private BufferedImage img = null;

        private int[] prevPx = null;

        private final GifDecoder.BitReader bits = new GifDecoder.BitReader();

        private final GifDecoder.CodeTable codes = new GifDecoder.CodeTable();

        private Graphics2D g;

        private final int[] decode(GifDecoder.GifFrame fr, int[] activeColTbl) {
            this.codes.init(fr, activeColTbl, this.bits);
            this.bits.init(fr.data);
            int clearCode = fr.clearCode;
            int endCode = fr.endOfInfoCode;
            int[] out = new int[this.wh];
            int[][] tbl = this.codes.tbl;
            int outPos = 0;
            this.codes.clear();
            this.bits.read();
            int code = this.bits.read();
            int[] pixels = tbl[code];
            System.arraycopy(pixels, 0, out, outPos, pixels.length);
            outPos += pixels.length;
            try {
                while (true) {
                    int prevCode = code;
                    code = this.bits.read();
                    if (code != clearCode) {
                        if (code == endCode) {
                            break;
                        }
                        int[] prevVals = tbl[prevCode];
                        int[] prevValsAndK = new int[prevVals.length + 1];
                        System.arraycopy(prevVals, 0, prevValsAndK, 0, prevVals.length);
                        if (code < this.codes.nextCode) {
                            pixels = tbl[code];
                            System.arraycopy(pixels, 0, out, outPos, pixels.length);
                            outPos += pixels.length;
                            prevValsAndK[prevVals.length] = tbl[code][0];
                        } else {
                            prevValsAndK[prevVals.length] = prevVals[0];
                            System.arraycopy(prevValsAndK, 0, out, outPos, prevValsAndK.length);
                            outPos += prevValsAndK.length;
                        }
                        this.codes.add(prevValsAndK);
                    } else {
                        this.codes.clear();
                        code = this.bits.read();
                        pixels = tbl[code];
                        System.arraycopy(pixels, 0, out, outPos, pixels.length);
                        outPos += pixels.length;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException var13) {
            }
            return out;
        }

        private final int[] deinterlace(int[] src, GifDecoder.GifFrame fr) {
            int w = fr.w;
            int h = fr.h;
            int wh = fr.wh;
            int[] dest = new int[src.length];
            int set2Y = h + 7 >>> 3;
            int set3Y = set2Y + (h + 3 >>> 3);
            int set4Y = set3Y + (h + 1 >>> 2);
            int set2 = w * set2Y;
            int set3 = w * set3Y;
            int set4 = w * set4Y;
            int w2 = w << 1;
            int w4 = w2 << 1;
            int w8 = w4 << 1;
            int from = 0;
            for (int to = 0; from < set2; to += w8) {
                System.arraycopy(src, from, dest, to, w);
                from += w;
            }
            for (int var18 = w4; from < set3; var18 += w8) {
                System.arraycopy(src, from, dest, var18, w);
                from += w;
            }
            for (int var19 = w2; from < set4; var19 += w4) {
                System.arraycopy(src, from, dest, var19, w);
                from += w;
            }
            for (int var20 = w; from < wh; var20 += w2) {
                System.arraycopy(src, from, dest, var20, w);
                from += w;
            }
            return dest;
        }

        private final void drawFrame(GifDecoder.GifFrame fr) {
            int[] activeColTbl = fr.hasLocColTbl ? fr.localColTbl : this.globalColTbl;
            int[] pixels = this.decode(fr, activeColTbl);
            if (fr.interlaceFlag) {
                pixels = this.deinterlace(pixels, fr);
            }
            BufferedImage frame = new BufferedImage(fr.w, fr.h, 2);
            System.arraycopy(pixels, 0, ((DataBufferInt) frame.getRaster().getDataBuffer()).getData(), 0, fr.wh);
            this.g.drawImage(frame, fr.x, fr.y, null);
            this.prevPx = new int[this.wh];
            System.arraycopy(((DataBufferInt) this.img.getRaster().getDataBuffer()).getData(), 0, this.prevPx, 0, this.wh);
            fr.img = new BufferedImage(this.w, this.h, 2);
            System.arraycopy(this.prevPx, 0, ((DataBufferInt) fr.img.getRaster().getDataBuffer()).getData(), 0, this.wh);
            if (fr.disposalMethod == 2) {
                this.g.clearRect(fr.x, fr.y, fr.w, fr.h);
            } else if (fr.disposalMethod == 3 && this.prevPx != null) {
                System.arraycopy(this.prevPx, 0, ((DataBufferInt) this.img.getRaster().getDataBuffer()).getData(), 0, this.wh);
            }
        }

        public final int getBackgroundColor() {
            GifDecoder.GifFrame frame = (GifDecoder.GifFrame) this.frames.get(0);
            if (frame.hasLocColTbl) {
                return frame.localColTbl[this.bgColIndex];
            } else {
                return this.hasGlobColTbl ? this.globalColTbl[this.bgColIndex] : 0;
            }
        }

        public final int getDelay(int index) {
            return ((GifDecoder.GifFrame) this.frames.get(index)).delay;
        }

        public final BufferedImage getFrame(int index) {
            if (this.img == null) {
                this.img = new BufferedImage(this.w, this.h, 2);
                this.g = this.img.createGraphics();
                this.g.setBackground(new Color(0, true));
            }
            GifDecoder.GifFrame fr = (GifDecoder.GifFrame) this.frames.get(index);
            if (fr.img == null) {
                for (int i = 0; i <= index; i++) {
                    fr = (GifDecoder.GifFrame) this.frames.get(i);
                    if (fr.img == null) {
                        this.drawFrame(fr);
                    }
                }
            }
            return fr.img;
        }

        public final int getFrameCount() {
            return this.frames.size();
        }

        public final int getHeight() {
            return this.h;
        }

        public final int getWidth() {
            return this.w;
        }
    }
}