package info.journeymap.shaded.ar.com.hjg.pngj;

import info.journeymap.shaded.ar.com.hjg.pngj.chunks.PngChunkPLTE;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.PngChunkTRNS;

public class ImageLineHelper {

    static int[] DEPTH_UNPACK_1;

    static int[] DEPTH_UNPACK_2;

    static int[] DEPTH_UNPACK_4;

    static int[][] DEPTH_UNPACK;

    private static void initDepthScale() {
        DEPTH_UNPACK_1 = new int[2];
        for (int i = 0; i < 2; i++) {
            DEPTH_UNPACK_1[i] = i * 255;
        }
        DEPTH_UNPACK_2 = new int[4];
        for (int i = 0; i < 4; i++) {
            DEPTH_UNPACK_2[i] = i * 255 / 3;
        }
        DEPTH_UNPACK_4 = new int[16];
        for (int i = 0; i < 16; i++) {
            DEPTH_UNPACK_4[i] = i * 255 / 15;
        }
        DEPTH_UNPACK = new int[][] { null, DEPTH_UNPACK_1, DEPTH_UNPACK_2, null, DEPTH_UNPACK_4 };
    }

    public static void scaleUp(IImageLineArray line) {
        if (!line.getImageInfo().indexed && line.getImageInfo().bitDepth < 8) {
            if (DEPTH_UNPACK_1 == null || DEPTH_UNPACK == null) {
                initDepthScale();
            }
            int[] scaleArray = DEPTH_UNPACK[line.getImageInfo().bitDepth];
            if (line instanceof ImageLineInt) {
                ImageLineInt iline = (ImageLineInt) line;
                for (int i = 0; i < iline.getSize(); i++) {
                    iline.scanline[i] = scaleArray[iline.scanline[i]];
                }
            } else {
                if (!(line instanceof ImageLineByte)) {
                    throw new PngjException("not implemented");
                }
                ImageLineByte iline = (ImageLineByte) line;
                for (int i = 0; i < iline.getSize(); i++) {
                    iline.scanline[i] = (byte) scaleArray[iline.scanline[i]];
                }
            }
        }
    }

    public static void scaleDown(IImageLineArray line) {
        if (!line.getImageInfo().indexed && line.getImageInfo().bitDepth < 8) {
            if (!(line instanceof ImageLineInt)) {
                throw new PngjException("not implemented");
            } else {
                int scalefactor = 8 - line.getImageInfo().bitDepth;
                if (line instanceof ImageLineInt) {
                    ImageLineInt iline = (ImageLineInt) line;
                    for (int i = 0; i < line.getSize(); i++) {
                        iline.scanline[i] = iline.scanline[i] >> scalefactor;
                    }
                } else if (line instanceof ImageLineByte) {
                    ImageLineByte iline = (ImageLineByte) line;
                    for (int i = 0; i < line.getSize(); i++) {
                        iline.scanline[i] = (byte) ((iline.scanline[i] & 255) >> scalefactor);
                    }
                }
            }
        }
    }

    public static byte scaleUp(int bitdepth, byte v) {
        return bitdepth < 8 ? (byte) DEPTH_UNPACK[bitdepth][v] : v;
    }

    public static byte scaleDown(int bitdepth, byte v) {
        return bitdepth < 8 ? (byte) (v >> 8 - bitdepth) : v;
    }

    public static int[] palette2rgb(ImageLineInt line, PngChunkPLTE pal, PngChunkTRNS trns, int[] buf) {
        return palette2rgb(line, pal, trns, buf, false);
    }

    static int[] lineToARGB32(ImageLineByte line, PngChunkPLTE pal, PngChunkTRNS trns, int[] buf) {
        boolean alphachannel = line.imgInfo.alpha;
        int cols = line.getImageInfo().cols;
        if (buf == null || buf.length < cols) {
            buf = new int[cols];
        }
        if (line.getImageInfo().indexed) {
            int nindexesWithAlpha = trns != null ? trns.getPalletteAlpha().length : 0;
            for (int c = 0; c < cols; c++) {
                int index = line.scanline[c] & 255;
                int rgb = pal.getEntry(index);
                int alpha = index < nindexesWithAlpha ? trns.getPalletteAlpha()[index] : 255;
                buf[c] = alpha << 24 | rgb;
            }
        } else if (line.imgInfo.greyscale) {
            int ga = trns != null ? trns.getGray() : -1;
            int c = 0;
            for (int c2 = 0; c < cols; c++) {
                int g = line.scanline[c2++] & 255;
                int alpha = alphachannel ? line.scanline[c2++] & 255 : (g != ga ? 255 : 0);
                buf[c] = alpha << 24 | g | g << 8 | g << 16;
            }
        } else {
            int ga = trns != null ? trns.getRGB888() : -1;
            int c = 0;
            for (int c2 = 0; c < cols; c++) {
                int rgb = (line.scanline[c2++] & 255) << 16 | (line.scanline[c2++] & 255) << 8 | line.scanline[c2++] & 255;
                int alpha = alphachannel ? line.scanline[c2++] & 255 : (rgb != ga ? 255 : 0);
                buf[c] = alpha << 24 | rgb;
            }
        }
        return buf;
    }

    static byte[] lineToRGBA8888(ImageLineByte line, PngChunkPLTE pal, PngChunkTRNS trns, byte[] buf) {
        boolean alphachannel = line.imgInfo.alpha;
        int cols = line.imgInfo.cols;
        int bytes = cols * 4;
        if (buf == null || buf.length < bytes) {
            buf = new byte[bytes];
        }
        if (line.imgInfo.indexed) {
            int nindexesWithAlpha = trns != null ? trns.getPalletteAlpha().length : 0;
            int c = 0;
            for (int b = 0; c < cols; c++) {
                int index = line.scanline[c] & 255;
                int rgb = pal.getEntry(index);
                buf[b++] = (byte) (rgb >> 16 & 0xFF);
                buf[b++] = (byte) (rgb >> 8 & 0xFF);
                buf[b++] = (byte) (rgb & 0xFF);
                buf[b++] = (byte) (index < nindexesWithAlpha ? trns.getPalletteAlpha()[index] : 255);
            }
        } else if (line.imgInfo.greyscale) {
            int ga = trns != null ? trns.getGray() : -1;
            int c = 0;
            int b = 0;
            while (b < bytes) {
                byte val = line.scanline[c++];
                buf[b++] = val;
                buf[b++] = val;
                buf[b++] = val;
                buf[b++] = (byte) (alphachannel ? line.scanline[c++] : ((val & 255) == ga ? 0 : -1));
            }
        } else if (alphachannel) {
            System.arraycopy(line.scanline, 0, buf, 0, bytes);
        } else {
            int c = 0;
            int b = 0;
            while (b < bytes) {
                buf[b++] = line.scanline[c++];
                buf[b++] = line.scanline[c++];
                buf[b++] = line.scanline[c++];
                buf[b++] = -1;
                if (trns != null && buf[b - 3] == (byte) trns.getRGB()[0] && buf[b - 2] == (byte) trns.getRGB()[1] && buf[b - 1] == (byte) trns.getRGB()[2]) {
                    buf[b - 1] = 0;
                }
            }
        }
        return buf;
    }

    static byte[] lineToRGB888(ImageLineByte line, PngChunkPLTE pal, byte[] buf) {
        boolean alphachannel = line.imgInfo.alpha;
        int cols = line.imgInfo.cols;
        int bytes = cols * 3;
        if (buf == null || buf.length < bytes) {
            buf = new byte[bytes];
        }
        int[] rgb = new int[3];
        if (line.imgInfo.indexed) {
            int c = 0;
            for (int b = 0; c < cols; c++) {
                pal.getEntryRgb(line.scanline[c] & 255, rgb);
                buf[b++] = (byte) rgb[0];
                buf[b++] = (byte) rgb[1];
                buf[b++] = (byte) rgb[2];
            }
        } else if (line.imgInfo.greyscale) {
            int c = 0;
            int b = 0;
            while (b < bytes) {
                byte val = line.scanline[c++];
                buf[b++] = val;
                buf[b++] = val;
                buf[b++] = val;
                if (alphachannel) {
                    c++;
                }
            }
        } else if (!alphachannel) {
            System.arraycopy(line.scanline, 0, buf, 0, bytes);
        } else {
            int c = 0;
            for (int b = 0; b < bytes; c++) {
                buf[b++] = line.scanline[c++];
                buf[b++] = line.scanline[c++];
                buf[b++] = line.scanline[c++];
            }
        }
        return buf;
    }

    public static int[] palette2rgba(ImageLineInt line, PngChunkPLTE pal, PngChunkTRNS trns, int[] buf) {
        return palette2rgb(line, pal, trns, buf, true);
    }

    public static int[] palette2rgb(ImageLineInt line, PngChunkPLTE pal, int[] buf) {
        return palette2rgb(line, pal, null, buf, false);
    }

    private static int[] palette2rgb(IImageLine line, PngChunkPLTE pal, PngChunkTRNS trns, int[] buf, boolean alphaForced) {
        boolean isalpha = trns != null;
        int channels = isalpha ? 4 : 3;
        ImageLineInt linei = (ImageLineInt) (line instanceof ImageLineInt ? line : null);
        ImageLineByte lineb = (ImageLineByte) (line instanceof ImageLineByte ? line : null);
        boolean isbyte = lineb != null;
        int cols = linei != null ? linei.imgInfo.cols : lineb.imgInfo.cols;
        int nsamples = cols * channels;
        if (buf == null || buf.length < nsamples) {
            buf = new int[nsamples];
        }
        int nindexesWithAlpha = trns != null ? trns.getPalletteAlpha().length : 0;
        for (int c = 0; c < cols; c++) {
            int index = isbyte ? lineb.scanline[c] & 255 : linei.scanline[c];
            pal.getEntryRgb(index, buf, c * channels);
            if (isalpha) {
                int alpha = index < nindexesWithAlpha ? trns.getPalletteAlpha()[index] : 255;
                buf[c * channels + 3] = alpha;
            }
        }
        return buf;
    }

    public static String infoFirstLastPixels(ImageLineInt line) {
        return line.imgInfo.channels == 1 ? String.format("first=(%d) last=(%d)", line.scanline[0], line.scanline[line.scanline.length - 1]) : String.format("first=(%d %d %d) last=(%d %d %d)", line.scanline[0], line.scanline[1], line.scanline[2], line.scanline[line.scanline.length - line.imgInfo.channels], line.scanline[line.scanline.length - line.imgInfo.channels + 1], line.scanline[line.scanline.length - line.imgInfo.channels + 2]);
    }

    public static int getPixelRGB8(IImageLine line, int column) {
        if (line instanceof ImageLineInt) {
            int offset = column * ((ImageLineInt) line).imgInfo.channels;
            int[] scanline = ((ImageLineInt) line).getScanline();
            return scanline[offset] << 16 | scanline[offset + 1] << 8 | scanline[offset + 2];
        } else if (line instanceof ImageLineByte) {
            int offset = column * ((ImageLineByte) line).imgInfo.channels;
            byte[] scanline = ((ImageLineByte) line).getScanline();
            return (scanline[offset] & 0xFF) << 16 | (scanline[offset + 1] & 0xFF) << 8 | scanline[offset + 2] & 0xFF;
        } else {
            throw new PngjException("Not supported " + line.getClass());
        }
    }

    public static int getPixelARGB8(IImageLine line, int column) {
        if (line instanceof ImageLineInt) {
            int offset = column * ((ImageLineInt) line).imgInfo.channels;
            int[] scanline = ((ImageLineInt) line).getScanline();
            return scanline[offset + 3] << 24 | scanline[offset] << 16 | scanline[offset + 1] << 8 | scanline[offset + 2];
        } else if (line instanceof ImageLineByte) {
            int offset = column * ((ImageLineByte) line).imgInfo.channels;
            byte[] scanline = ((ImageLineByte) line).getScanline();
            return (scanline[offset + 3] & 0xFF) << 24 | (scanline[offset] & 0xFF) << 16 | (scanline[offset + 1] & 0xFF) << 8 | scanline[offset + 2] & 0xFF;
        } else {
            throw new PngjException("Not supported " + line.getClass());
        }
    }

    public static void setPixelsRGB8(ImageLineInt line, int[] rgb) {
        int i = 0;
        for (int j = 0; i < line.imgInfo.cols; i++) {
            line.scanline[j++] = rgb[i] >> 16 & 0xFF;
            line.scanline[j++] = rgb[i] >> 8 & 0xFF;
            line.scanline[j++] = rgb[i] & 0xFF;
        }
    }

    public static void setPixelRGB8(ImageLineInt line, int col, int r, int g, int b) {
        col *= line.imgInfo.channels;
        line.scanline[col++] = r;
        line.scanline[col++] = g;
        line.scanline[col] = b;
    }

    public static void setPixelRGB8(ImageLineInt line, int col, int rgb) {
        setPixelRGB8(line, col, rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF);
    }

    public static void setPixelsRGBA8(ImageLineInt line, int[] rgb) {
        int i = 0;
        for (int j = 0; i < line.imgInfo.cols; i++) {
            line.scanline[j++] = rgb[i] >> 16 & 0xFF;
            line.scanline[j++] = rgb[i] >> 8 & 0xFF;
            line.scanline[j++] = rgb[i] & 0xFF;
            line.scanline[j++] = rgb[i] >> 24 & 0xFF;
        }
    }

    public static void setPixelRGBA8(ImageLineInt line, int col, int r, int g, int b, int a) {
        col *= line.imgInfo.channels;
        line.scanline[col++] = r;
        line.scanline[col++] = g;
        line.scanline[col++] = b;
        line.scanline[col] = a;
    }

    public static void setPixelRGBA8(ImageLineInt line, int col, int rgb) {
        setPixelRGBA8(line, col, rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF, rgb >> 24 & 0xFF);
    }

    public static void setValD(ImageLineInt line, int i, double d) {
        line.scanline[i] = double2int(line, d);
    }

    public static int interpol(int a, int b, int c, int d, double dx, double dy) {
        double e = (double) a * (1.0 - dx) + (double) b * dx;
        double f = (double) c * (1.0 - dx) + (double) d * dx;
        return (int) (e * (1.0 - dy) + f * dy + 0.5);
    }

    public static double int2double(ImageLineInt line, int p) {
        return line.imgInfo.bitDepth == 16 ? (double) p / 65535.0 : (double) p / 255.0;
    }

    public static double int2doubleClamped(ImageLineInt line, int p) {
        double d = line.imgInfo.bitDepth == 16 ? (double) p / 65535.0 : (double) p / 255.0;
        return d <= 0.0 ? 0.0 : (d >= 1.0 ? 1.0 : d);
    }

    public static int double2int(ImageLineInt line, double d) {
        d = d <= 0.0 ? 0.0 : (d >= 1.0 ? 1.0 : d);
        return line.imgInfo.bitDepth == 16 ? (int) (d * 65535.0 + 0.5) : (int) (d * 255.0 + 0.5);
    }

    public static int double2intClamped(ImageLineInt line, double d) {
        d = d <= 0.0 ? 0.0 : (d >= 1.0 ? 1.0 : d);
        return line.imgInfo.bitDepth == 16 ? (int) (d * 65535.0 + 0.5) : (int) (d * 255.0 + 0.5);
    }

    public static int clampTo_0_255(int i) {
        return i > 255 ? 255 : (i < 0 ? 0 : i);
    }

    public static int clampTo_0_65535(int i) {
        return i > 65535 ? 65535 : (i < 0 ? 0 : i);
    }

    public static int clampTo_128_127(int x) {
        return x > 127 ? 127 : (x < -128 ? -128 : x);
    }

    static int getMaskForPackedFormats(int bitDepth) {
        if (bitDepth == 4) {
            return 240;
        } else {
            return bitDepth == 2 ? 192 : 128;
        }
    }

    static int getMaskForPackedFormatsLs(int bitDepth) {
        if (bitDepth == 4) {
            return 15;
        } else {
            return bitDepth == 2 ? 3 : 1;
        }
    }
}