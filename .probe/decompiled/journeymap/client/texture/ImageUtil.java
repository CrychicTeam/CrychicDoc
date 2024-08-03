package journeymap.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import journeymap.client.cartography.color.RGB;
import journeymap.common.Journeymap;
import org.lwjgl.stb.STBImageResize;

public class ImageUtil {

    public static NativeImage getSizedImage(int width, int height, NativeImage from, boolean autoClose) {
        NativeImage scaledImage = new NativeImage(from.format(), width, height, false);
        STBImageResize.nstbir_resize_uint8_generic(from.pixels, from.getWidth(), from.getHeight(), 0, scaledImage.pixels, width, height, 0, from.format().components(), -1, 0, 1, 1, 0, 0L);
        if (autoClose) {
            from.close();
        }
        return scaledImage;
    }

    public static NativeImage getScaledImage(float scale, NativeImage from, boolean autoClose) {
        int scaledWidth = (int) ((float) from.getWidth() * scale);
        int scaledHeight = (int) ((float) from.getHeight() * scale);
        NativeImage scaledImage = new NativeImage(from.format(), scaledWidth, scaledHeight, false);
        STBImageResize.nstbir_resize_uint8_generic(from.pixels, from.getWidth(), from.getHeight(), 0, scaledImage.pixels, scaledWidth, scaledHeight, 0, from.format().components(), -1, 0, 1, 1, 0, 0L);
        if (autoClose) {
            from.close();
        }
        return scaledImage;
    }

    public static ComparableNativeImage getComparableSubImage(int x, int y, int width, int height, NativeImage from, boolean autoClose) {
        NativeImage to = new ComparableNativeImage(from.format(), width, height);
        return (ComparableNativeImage) getSubImage(x, y, width, height, from, to, autoClose);
    }

    public static NativeImage getSubImage(int x, int y, int width, int height, NativeImage from, boolean autoClose) {
        NativeImage to = new NativeImage(width, height, false);
        return getSubImage(x, y, width, height, from, to, autoClose);
    }

    public static NativeImage getSubImage(int x, int y, int width, int height, NativeImage from, NativeImage to, boolean autoClose) {
        if (from.pixels == 0L) {
            throw new IllegalStateException("Image is not allocated. " + from);
        } else if (to.format() != from.format()) {
            throw new UnsupportedOperationException("getSubImage only works for images of the same format.");
        } else {
            int i = from.format().components();
            STBImageResize.nstbir_resize_uint8_generic(from.pixels + ((long) x + (long) y * (long) from.getWidth()) * (long) i, width, height, from.getWidth() * i, to.pixels, to.getWidth(), to.getHeight(), 0, from.format().components(), -1, 0, 1, 1, 0, 0L);
            if (autoClose) {
                from.close();
            }
            return to;
        }
    }

    public static NativeImage recolorImage(NativeImage image, int color) {
        NativeImage tintedImage = new NativeImage(image.getWidth(), image.getHeight(), false);
        tintedImage.copyFrom(image);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getWidth(); y++) {
                int imgColor = image.getPixelRGBA(x, y);
                if (getAlpha(imgColor) > 1) {
                    tintedImage.blendPixel(x, y, RGB.toRgba(color, 0.75F));
                }
            }
        }
        return tintedImage;
    }

    public static void closeSafely(NativeImage image) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(image::close);
        } else {
            image.close();
        }
    }

    public static NativeImage getNewBlankImage(int width, int height) {
        NativeImage image = new NativeImage(width, height, false);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                image.setPixelRGBA(x, y, RGB.toRgba(0, 0.0F));
            }
        }
        return image;
    }

    public static void clearAndClose(NativeImage image) {
        if (image != null) {
            try {
                image.close();
            } catch (Exception var2) {
                Journeymap.getLogger().warn("Failed to clear and close image: ", var2);
            }
        }
    }

    public static int getAlpha(int color) {
        return color >> 24 & 0xFF;
    }
}