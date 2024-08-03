package icyllis.modernui.core;

import icyllis.modernui.annotation.NonNull;
import java.util.Objects;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWVidMode.Buffer;

public final class VideoMode {

    private final int mWidth;

    private final int mHeight;

    private final int mRedBits;

    private final int mGreenBits;

    private final int mBlueBits;

    private final int mRefreshRate;

    public VideoMode(@NonNull GLFWVidMode gLFWVidMode) {
        this.mWidth = gLFWVidMode.width();
        this.mHeight = gLFWVidMode.height();
        this.mRedBits = gLFWVidMode.redBits();
        this.mGreenBits = gLFWVidMode.greenBits();
        this.mBlueBits = gLFWVidMode.blueBits();
        this.mRefreshRate = gLFWVidMode.refreshRate();
    }

    public VideoMode(@NonNull Buffer buffer) {
        this.mWidth = buffer.width();
        this.mHeight = buffer.height();
        this.mRedBits = buffer.redBits();
        this.mGreenBits = buffer.greenBits();
        this.mBlueBits = buffer.blueBits();
        this.mRefreshRate = buffer.refreshRate();
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getRedBits() {
        return this.mRedBits;
    }

    public int getGreenBits() {
        return this.mGreenBits;
    }

    public int getBlueBits() {
        return this.mBlueBits;
    }

    public int getRefreshRate() {
        return this.mRefreshRate;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            VideoMode videoMode = (VideoMode) o;
            return this.mWidth == videoMode.mWidth && this.mHeight == videoMode.mHeight && this.mRedBits == videoMode.mRedBits && this.mGreenBits == videoMode.mGreenBits && this.mBlueBits == videoMode.mBlueBits && this.mRefreshRate == videoMode.mRefreshRate;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.mWidth, this.mHeight, this.mRedBits, this.mGreenBits, this.mBlueBits, this.mRefreshRate });
    }

    public String toString() {
        int colorBitDepth = this.mRedBits + this.mGreenBits + this.mBlueBits;
        return String.format("VideoMode: %dx%d@%dHz (%d bits)", this.mWidth, this.mHeight, this.mRefreshRate, colorBitDepth);
    }
}