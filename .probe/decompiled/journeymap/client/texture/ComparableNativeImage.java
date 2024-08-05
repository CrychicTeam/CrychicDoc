package journeymap.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import java.util.stream.IntStream;

public class ComparableNativeImage extends NativeImage {

    private boolean changed = false;

    public ComparableNativeImage(NativeImage other) {
        super(other.format(), other.getWidth(), other.getHeight(), false);
        this.m_85054_(other);
    }

    public ComparableNativeImage(NativeImage.Format format, int width, int height) {
        super(format, width, height, false);
    }

    @Override
    public synchronized void setPixelRGBA(int x, int y, int argb) {
        if (!this.changed && super.getPixelRGBA(x, y) != argb) {
            this.changed = true;
        }
        super.setPixelRGBA(x, y, argb);
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean val) {
        this.changed = val;
    }

    public boolean identicalTo(NativeImage other) {
        return areIdentical(this.getPixelData(), getPixelData(other));
    }

    public static boolean areIdentical(int[] pixels, int[] otherPixels) {
        return IntStream.range(0, pixels.length).map(i -> ~pixels[i] | otherPixels[i]).allMatch(n -> n == -1);
    }

    public int[] getPixelData() {
        return getPixelData(this);
    }

    public static int[] getPixelData(NativeImage image) {
        return image.makePixelArray();
    }

    @Override
    public void close() {
        super.close();
    }
}