package mezz.jei.library.color;

import com.mojang.blaze3d.platform.NativeImage;
import java.util.Arrays;
import org.jetbrains.annotations.Nullable;

public class ColorThief {

    public static int[][] getPalette(NativeImage sourceImage, int colorCount, int quality, boolean ignoreWhite) {
        MMCQ.CMap cmap = getColorMap(sourceImage, colorCount, quality, ignoreWhite);
        return cmap == null ? new int[0][0] : cmap.palette();
    }

    @Nullable
    public static MMCQ.CMap getColorMap(NativeImage sourceImage, int colorCount, int quality, boolean ignoreWhite) {
        if (sourceImage.format() == NativeImage.Format.RGBA) {
            int[][] pixelArray = getPixels(sourceImage, quality, ignoreWhite);
            return MMCQ.quantize(pixelArray, colorCount);
        } else {
            return null;
        }
    }

    private static int[][] getPixels(NativeImage sourceImage, int quality, boolean ignoreWhite) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int pixelCount = width * height;
        int numRegardedPixels = (pixelCount + quality - 1) / quality;
        int numUsedPixels = 0;
        int[][] pixelArray = new int[numRegardedPixels][];
        int i = 0;
        while (i < pixelCount) {
            int x = i % width;
            int y = i / width;
            int rgba = sourceImage.getPixelRGBA(x, y);
            int a = rgba >> 24 & 0xFF;
            int b = rgba >> 16 & 0xFF;
            int g = rgba >> 8 & 0xFF;
            int r = rgba & 0xFF;
            if (a < 125 || ignoreWhite && r > 250 && g > 250 && b > 250) {
                i++;
            } else {
                pixelArray[numUsedPixels] = new int[] { r, g, b };
                numUsedPixels++;
                i += quality;
            }
        }
        return (int[][]) Arrays.copyOfRange(pixelArray, 0, numUsedPixels);
    }
}