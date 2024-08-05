package icyllis.arc3d.core;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import sun.misc.Unsafe;

public class Raster {

    public static final int FORMAT_UNKNOWN = 0;

    public static final int FORMAT_GRAY_8 = 1;

    public static final int FORMAT_GRAY_16 = 2;

    public static final int FORMAT_RGB_565 = 3;

    public static final int FORMAT_RGB_888 = 4;

    @Nullable
    protected final BufferedImage mBufImg;

    protected volatile PixelMap mPixelMap;

    protected final PixelRef mPixelRef;

    public Raster(@Nullable BufferedImage bufImg, @Nonnull ImageInfo info, @Nullable Object data, int baseOffset, int rowStride) {
        this.mBufImg = bufImg;
        this.mPixelMap = new PixelMap(info, data, (long) baseOffset, rowStride);
        this.mPixelRef = new PixelRef(info.width(), info.height(), data, (long) baseOffset, rowStride, null);
    }

    @Nonnull
    public static Raster createRaster(@Size(min = 1L, max = 32768L) int width, @Size(min = 1L, max = 32768L) int height, int format) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Image dimensions " + width + "x" + height + " must be positive");
        } else if (width <= 32768 && height <= 32768) {
            int ct;
            int at;
            int rowStride;
            int imageType = switch(format) {
                case 0 ->
                    {
                    }
                case 1 ->
                    {
                    }
                case 2 ->
                    {
                    }
                case 3 ->
                    {
                    }
                case 4 ->
                    {
                    }
                default ->
                    throw new IllegalArgumentException("Unrecognized format " + format);
            };
            ImageInfo info = new ImageInfo(width, height, ct, at);
            BufferedImage bufImg;
            Object data;
            int baseOffset;
            if (imageType != 0) {
                bufImg = new BufferedImage(width, height, imageType);
                Object var13;
                switch(imageType) {
                    case 5:
                    case 10:
                        DataBufferByte dataBuffer = (DataBufferByte) bufImg.getRaster().getDataBuffer();
                        assert dataBuffer.getNumBanks() == 1;
                        baseOffset = Unsafe.ARRAY_BYTE_BASE_OFFSET;
                        var13 = dataBuffer.getData();
                        break;
                    case 6:
                    case 7:
                    case 9:
                    default:
                        assert false;
                        baseOffset = 0;
                        var13 = null;
                        break;
                    case 8:
                    case 11:
                        DataBufferUShort dataBuffer = (DataBufferUShort) bufImg.getRaster().getDataBuffer();
                        assert dataBuffer.getNumBanks() == 1;
                        baseOffset = Unsafe.ARRAY_SHORT_BASE_OFFSET;
                        var13 = dataBuffer.getData();
                }
                data = var13;
            } else {
                bufImg = null;
                data = null;
                baseOffset = 0;
            }
            return new Raster(bufImg, info, data, baseOffset, rowStride);
        } else {
            throw new IllegalArgumentException("Image dimensions " + width + "x" + height + " must be less than or equal to 32768");
        }
    }

    public int getFormat() {
        if (this.mBufImg == null) {
            return 0;
        } else {
            return switch(this.mBufImg.getType()) {
                case 5 ->
                    4;
                default ->
                    {
                    }
                case 8 ->
                    3;
                case 10 ->
                    1;
                case 11 ->
                    2;
            };
        }
    }

    @Nonnull
    public ImageInfo getInfo() {
        return this.mPixelMap.getInfo();
    }

    public int getWidth() {
        return this.mPixelMap.getWidth();
    }

    public int getHeight() {
        return this.mPixelMap.getHeight();
    }

    public int getColorType() {
        return this.mPixelMap.getColorType();
    }

    public int getAlphaType() {
        return this.mPixelMap.getAlphaType();
    }

    @Nullable
    public ColorSpace getColorSpace() {
        return this.mPixelMap.getColorSpace();
    }

    public PixelMap getPixelMap() {
        return this.mPixelMap;
    }

    public PixelRef getPixelRef() {
        return this.mPixelRef;
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface Format {
    }
}