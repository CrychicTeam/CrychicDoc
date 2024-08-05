package com.corosus.coroutil.repack.de.androidpit.colorthief;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

public class ColorThief {

    private static final int DEFAULT_QUALITY = 10;

    private static final boolean DEFAULT_IGNORE_WHITE = true;

    public static int[] getColor(BufferedImage sourceImage) {
        int[][] palette = getPalette(sourceImage, 5);
        return palette == null ? null : palette[0];
    }

    public static int[] getColor(BufferedImage sourceImage, int quality, boolean ignoreWhite) {
        int[][] palette = getPalette(sourceImage, 5, quality, ignoreWhite);
        return palette == null ? null : palette[0];
    }

    public static int[][] getPalette(BufferedImage sourceImage, int colorCount) {
        MMCQ.CMap cmap = getColorMap(sourceImage, colorCount);
        return cmap == null ? null : cmap.palette();
    }

    public static int[][] getPalette(BufferedImage sourceImage, int colorCount, int quality, boolean ignoreWhite) {
        MMCQ.CMap cmap = getColorMap(sourceImage, colorCount, quality, ignoreWhite);
        return cmap == null ? null : cmap.palette();
    }

    public static MMCQ.CMap getColorMap(BufferedImage sourceImage, int colorCount) {
        return getColorMap(sourceImage, colorCount, 10, true);
    }

    public static MMCQ.CMap getColorMap(BufferedImage sourceImage, int colorCount, int quality, boolean ignoreWhite) {
        if (quality < 1) {
            throw new IllegalArgumentException("Specified quality should be greater then 0.");
        } else {
            return MMCQ.quantize(switch(sourceImage.getType()) {
                case 5, 6 ->
                    getPixelsFast(sourceImage, quality, ignoreWhite);
                default ->
                    getPixelsSlow(sourceImage, quality, ignoreWhite);
            }, colorCount);
        }
    }

    private static int[][] getPixelsFast(BufferedImage sourceImage, int quality, boolean ignoreWhite) {
        DataBufferByte imageData = (DataBufferByte) sourceImage.getRaster().getDataBuffer();
        byte[] pixels = imageData.getData();
        int pixelCount = sourceImage.getWidth() * sourceImage.getHeight();
        int type = sourceImage.getType();
        int expectedDataLength = pixelCount * switch(type) {
            case 5 ->
                3;
            case 6 ->
                4;
            default ->
                throw new IllegalArgumentException("Unhandled type: " + type);
        };
        if (expectedDataLength != pixels.length) {
            throw new IllegalArgumentException("(expectedDataLength = " + expectedDataLength + ") != (pixels.length = " + pixels.length + ")");
        } else {
            int numRegardedPixels = (pixelCount + quality - 1) / quality;
            int numUsedPixels = 0;
            int[][] pixelArray = new int[numRegardedPixels][];
            switch(type) {
                case 5:
                    for (int ix = 0; ix < pixelCount; ix += quality) {
                        int offset = ix * 3;
                        int b = pixels[offset] & 255;
                        int g = pixels[offset + 1] & 255;
                        int r = pixels[offset + 2] & 255;
                        if (!ignoreWhite || r <= 250 || g <= 250 || b <= 250) {
                            pixelArray[numUsedPixels] = new int[] { r, g, b };
                            numUsedPixels++;
                        }
                    }
                    break;
                case 6:
                    for (int i = 0; i < pixelCount; i += quality) {
                        int offset = i * 4;
                        int a = pixels[offset] & 255;
                        int b = pixels[offset + 1] & 255;
                        int g = pixels[offset + 2] & 255;
                        int r = pixels[offset + 3] & 255;
                        if (a >= 125 && (!ignoreWhite || r <= 250 || g <= 250 || b <= 250)) {
                            pixelArray[numUsedPixels] = new int[] { r, g, b };
                            numUsedPixels++;
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled type: " + type);
            }
            return (int[][]) Arrays.copyOfRange(pixelArray, 0, numUsedPixels);
        }
    }

    private static int[][] getPixelsSlow(BufferedImage sourceImage, int quality, boolean ignoreWhite) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int pixelCount = width * height;
        int numRegardedPixels = (pixelCount + quality - 1) / quality;
        int numUsedPixels = 0;
        int[][] res = new int[numRegardedPixels][];
        for (int i = 0; i < pixelCount; i += quality) {
            int row = i / width;
            int col = i % width;
            int rgb = sourceImage.getRGB(col, row);
            int r = rgb >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            if (!ignoreWhite || r <= 250 || g <= 250 || b <= 250) {
                res[numUsedPixels] = new int[] { r, g, b };
                numUsedPixels++;
            }
        }
        return (int[][]) Arrays.copyOfRange(res, 0, numUsedPixels);
    }
}