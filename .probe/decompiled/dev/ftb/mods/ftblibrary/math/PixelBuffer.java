package dev.ftb.mods.ftblibrary.math;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;

public class PixelBuffer {

    private final int width;

    private final int height;

    private final int[] pixels;

    public PixelBuffer(int w, int h) {
        this.width = w;
        this.height = h;
        this.pixels = new int[w * h];
    }

    public static PixelBuffer from(BufferedImage img) {
        PixelBuffer buffer = new PixelBuffer(img.getWidth(), img.getHeight());
        buffer.setPixels(img.getRGB(0, 0, buffer.getWidth(), buffer.getHeight(), buffer.getPixels(), 0, buffer.getWidth()));
        return buffer;
    }

    public static PixelBuffer from(InputStream stream) throws Exception {
        return from(ImageIO.read(stream));
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int[] getPixels() {
        return this.pixels;
    }

    public void setPixels(int[] p) {
        if (p.length == this.pixels.length) {
            System.arraycopy(p, 0, this.pixels, 0, this.pixels.length);
        }
    }

    public void setRGB(int x, int y, int col) {
        this.pixels[x + y * this.width] = col;
    }

    public int getRGB(int x, int y) {
        return this.pixels[x + y * this.width];
    }

    public void setRGB(int startX, int startY, int w, int h, int[] rgbArray) {
        if (startX == 0 && startY == 0 && w == this.getWidth() && h == this.getHeight()) {
            this.setPixels(rgbArray);
        } else {
            int off = -1;
            for (int y = startY; y < startY + h; y++) {
                for (int x = startX; x < startX + w; x++) {
                    this.setRGB(x, y, rgbArray[++off]);
                }
            }
        }
    }

    public void setRGB(int startX, int startY, PixelBuffer buffer) {
        this.setRGB(startX, startY, buffer.getWidth(), buffer.getHeight(), buffer.getPixels());
    }

    public int[] getRGB(int startX, int startY, int w, int h, @Nullable int[] p) {
        if (p == null || p.length != w * h) {
            p = new int[w * h];
        }
        int off = -1;
        w += startX;
        h += startY;
        for (int y = startY; y < h; y++) {
            for (int x = startX; x < w; x++) {
                off++;
                p[off] = this.getRGB(x, y);
            }
        }
        return p;
    }

    public void fill(int col) {
        int[] pixels = this.getPixels();
        Arrays.fill(pixels, col);
        this.setPixels(pixels);
    }

    public void fill(int startX, int startY, int w, int h, int col) {
        for (int y = startY; y < startY + h; y++) {
            for (int x = startX; x < startX + w; x++) {
                this.setRGB(x, y, col);
            }
        }
    }

    public BufferedImage toImage(int type) {
        BufferedImage image = new BufferedImage(this.width, this.height, type);
        image.setRGB(0, 0, this.width, this.height, this.pixels, 0, this.width);
        return image;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o == this) {
            return true;
        } else {
            if (o instanceof PixelBuffer b && this.width == b.width && this.height == b.height) {
                for (int i = 0; i < this.pixels.length; i++) {
                    if (this.pixels[i] != b.pixels[i]) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        return Arrays.hashCode(this.getPixels());
    }

    public PixelBuffer copy() {
        PixelBuffer b = new PixelBuffer(this.width, this.height);
        System.arraycopy(this.pixels, 0, b.pixels, 0, this.pixels.length);
        return b;
    }

    public PixelBuffer getSubimage(int x, int y, int w, int h) {
        PixelBuffer b = new PixelBuffer(w, h);
        this.getRGB(x, y, w, h, b.pixels);
        return b;
    }

    public ByteBuffer toByteBuffer(boolean alpha) {
        int[] pixels = this.getPixels();
        ByteBuffer bb = BufferUtils.createByteBuffer(pixels.length * 4);
        byte alpha255 = -1;
        for (int c : pixels) {
            bb.put((byte) (c >> 16));
            bb.put((byte) (c >> 8));
            bb.put((byte) c);
            bb.put(alpha ? (byte) (c >> 24) : alpha255);
        }
        bb.flip();
        return bb;
    }
}