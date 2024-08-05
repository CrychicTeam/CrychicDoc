package icyllis.arc3d.core;

import java.lang.ref.WeakReference;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.system.NativeType;

public class PixelMap {

    @Nonnull
    protected final ImageInfo mInfo;

    @Nullable
    protected final WeakReference<Object> mBase;

    protected final long mAddress;

    protected final int mRowStride;

    public PixelMap(@Nonnull ImageInfo info, @Nullable Object base, @NativeType("const void *") long address, int rowStride) {
        this(info, base != null ? new WeakReference(base) : null, address, rowStride);
    }

    public PixelMap(@Nonnull ImageInfo newInfo, @Nonnull PixelMap oldPixelMap) {
        this(newInfo, oldPixelMap.mBase, oldPixelMap.mAddress, oldPixelMap.mRowStride);
    }

    PixelMap(@Nonnull ImageInfo info, @Nullable WeakReference<Object> base, @NativeType("const void *") long address, int rowStride) {
        this.mInfo = (ImageInfo) Objects.requireNonNull(info);
        this.mBase = base;
        this.mAddress = address;
        this.mRowStride = rowStride;
    }

    @Nonnull
    public ImageInfo getInfo() {
        return this.mInfo;
    }

    public int getWidth() {
        return this.mInfo.width();
    }

    public int getHeight() {
        return this.mInfo.height();
    }

    public int getColorType() {
        return this.mInfo.colorType();
    }

    public int getAlphaType() {
        return this.mInfo.alphaType();
    }

    @Nullable
    public ColorSpace getColorSpace() {
        return this.mInfo.colorSpace();
    }

    @Nullable
    public Object getBase() {
        return this.mBase != null ? this.mBase.get() : null;
    }

    public long getAddress() {
        return this.mAddress;
    }

    public int getRowStride() {
        return this.mRowStride;
    }

    public boolean clear(float red, float green, float blue, float alpha, @Nullable Rect2ic subset) {
        if (this.getColorType() == 0) {
            return false;
        } else {
            Rect2i clip = new Rect2i(0, 0, this.getWidth(), this.getHeight());
            if (subset != null && !clip.intersect(subset)) {
                return true;
            } else if (this.getColorType() != 6) {
                return false;
            } else {
                int c = (int) (alpha * 255.0F + 0.5F) << 24 | (int) (blue * 255.0F + 0.5F) << 16 | (int) (green * 255.0F + 0.5F) << 8 | (int) (red * 255.0F + 0.5F);
                Object base = this.getBase();
                for (int y = clip.mTop; y < clip.mBottom; y++) {
                    long addr = this.getAddress() + (long) y * (long) this.getRowStride() + (long) clip.x() << 2;
                    PixelUtils.setPixel32(base, addr, c, clip.width());
                }
                return true;
            }
        }
    }

    public String toString() {
        return "PixelMap{mInfo=" + this.mInfo + ", mBase=" + this.getBase() + ", mAddress=0x" + Long.toHexString(this.mAddress) + ", mRowStride=" + this.mRowStride + "}";
    }
}