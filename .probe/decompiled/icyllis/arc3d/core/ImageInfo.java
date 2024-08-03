package icyllis.arc3d.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class ImageInfo {

    public static final int COMPRESSION_NONE = 0;

    public static final int COMPRESSION_ETC2_RGB8_UNORM = 1;

    public static final int COMPRESSION_BC1_RGB8_UNORM = 2;

    public static final int COMPRESSION_BC1_RGBA8_UNORM = 3;

    public static final int COMPRESSION_COUNT = 4;

    public static final int AT_UNKNOWN = 0;

    public static final int AT_OPAQUE = 1;

    public static final int AT_PREMUL = 2;

    public static final int AT_UNPREMUL = 3;

    public static final int CT_UNKNOWN = 0;

    public static final int CT_RGB_565 = 1;

    public static final int CT_R_8 = 2;

    public static final int CT_RG_88 = 3;

    @Internal
    public static final int CT_RGB_888 = 4;

    public static final int CT_RGB_888x = 5;

    public static final int CT_RGBA_8888 = 6;

    public static final int CT_BGRA_8888 = 7;

    @Internal
    public static final int CT_RGBA_8888_SRGB = 8;

    public static final int CT_RGBA_1010102 = 9;

    public static final int CT_BGRA_1010102 = 10;

    @Internal
    public static final int CT_R_16 = 11;

    @Internal
    public static final int CT_R_F16 = 12;

    @Internal
    public static final int CT_RG_1616 = 13;

    @Internal
    public static final int CT_RG_F16 = 14;

    public static final int CT_RGBA_16161616 = 15;

    public static final int CT_RGBA_F16 = 16;

    public static final int CT_RGBA_F16_CLAMPED = 17;

    public static final int CT_RGBA_F32 = 18;

    public static final int CT_ALPHA_8 = 19;

    public static final int CT_ALPHA_16 = 20;

    public static final int CT_ALPHA_F16 = 21;

    public static final int CT_GRAY_8 = 22;

    @Internal
    public static final int CT_GRAY_ALPHA_88 = 23;

    public static final int CT_R5G6B5_UNORM = 1;

    public static final int CT_R8G8_UNORM = 3;

    public static final int CT_A16_UNORM = 20;

    public static final int CT_A16_FLOAT = 21;

    public static final int CT_A16G16_UNORM = 13;

    public static final int CT_R16G16_FLOAT = 14;

    public static final int CT_R16G16B16A16_UNORM = 15;

    @Internal
    public static final int CT_R_8xxx = 24;

    @Internal
    public static final int CT_ALPHA_8xxx = 25;

    @Internal
    public static final int CT_ALPHA_F32xxx = 26;

    @Internal
    public static final int CT_GRAY_8xxx = 27;

    @Internal
    public static final int CT_COUNT = 28;

    @Size(min = 0L)
    private int width;

    @Size(min = 0L)
    private int height;

    private final short colorType;

    private final short alphaType;

    @Nullable
    private final ColorSpace colorSpace;

    public static int bytesPerPixel(int ct) {
        return switch(ct) {
            case 0 ->
                0;
            case 1, 3, 11, 12, 20, 21, 23 ->
                2;
            case 2, 19, 22 ->
                1;
            case 4 ->
                3;
            case 5, 6, 7, 8, 9, 10, 13, 14, 24, 25, 27 ->
                4;
            case 15, 16, 17 ->
                8;
            case 18, 26 ->
                16;
            default ->
                throw new AssertionError(ct);
        };
    }

    public static int validateAlphaType(int ct, int at) {
        switch(ct) {
            case 0:
                at = 0;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 11:
            case 12:
            case 13:
            case 14:
            case 22:
                at = 1;
                break;
            case 19:
            case 20:
            case 21:
                if (at == 3) {
                    at = 2;
                }
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 15:
            case 16:
            case 17:
            case 18:
            case 23:
                if (at != 1 && at != 2 && at != 3) {
                    throw new IllegalArgumentException("at is unknown");
                }
                break;
            default:
                throw new AssertionError(ct);
        }
        return at;
    }

    @Internal
    public static int colorTypeChannelFlags(int ct) {
        return switch(ct) {
            case 0 ->
                0;
            case 1, 4, 5 ->
                7;
            case 2, 11, 12, 24 ->
                1;
            case 3, 13, 14 ->
                3;
            case 6, 7, 8, 9, 10, 15, 16, 17, 18 ->
                15;
            case 19, 20, 21, 25, 26 ->
                8;
            case 22, 27 ->
                16;
            case 23 ->
                24;
            default ->
                throw new AssertionError(ct);
        };
    }

    public static String colorTypeToString(int ct) {
        return switch(ct) {
            case 0 ->
                "UNKNOWN";
            case 1 ->
                "RGB_565";
            case 2 ->
                "R_8";
            case 3 ->
                "RG_88";
            case 4 ->
                "RGB_888";
            case 5 ->
                "RGB_888x";
            case 6 ->
                "RGBA_8888";
            case 7 ->
                "BGRA_8888";
            case 8 ->
                "RGBA_8888_SRGB";
            case 9 ->
                "RGBA_1010102";
            case 10 ->
                "BGRA_1010102";
            case 11 ->
                "R_16";
            case 12 ->
                "R_F16";
            case 13 ->
                "RG_1616";
            case 14 ->
                "RG_F16";
            case 15 ->
                "RGBA_16161616";
            case 16 ->
                "RGBA_F16";
            case 17 ->
                "RGBA_F16_CLAMPED";
            case 18 ->
                "RGBA_F32";
            case 19 ->
                "ALPHA_8";
            case 20 ->
                "ALPHA_16";
            case 21 ->
                "ALPHA_F16";
            case 22 ->
                "GRAY_8";
            case 23 ->
                "GRAY_ALPHA_88";
            case 24 ->
                "R_8xxx";
            case 25 ->
                "ALPHA_8xxx";
            case 26 ->
                "ALPHA_F32xxx";
            case 27 ->
                "GRAY_8xxx";
            default ->
                throw new AssertionError(ct);
        };
    }

    @Nonnull
    public static ImageInfo make(@Size(min = 0L) int width, @Size(min = 0L) int height, int ct, int at, @Nullable ColorSpace cs) {
        return new ImageInfo(width, height, ct, at, cs);
    }

    @Nonnull
    public static ImageInfo makeUnknown(@Size(min = 0L) int width, @Size(min = 0L) int height) {
        return new ImageInfo(width, height, 0, 0, null);
    }

    public ImageInfo() {
        this(0, 0);
    }

    public ImageInfo(@Size(min = 0L) int width, @Size(min = 0L) int height) {
        this(width, height, 0, 0);
    }

    public ImageInfo(@Size(min = 0L) int width, @Size(min = 0L) int height, int colorType, int alphaType) {
        this(width, height, colorType, alphaType, null);
    }

    @Internal
    public ImageInfo(@Size(min = 0L) int w, @Size(min = 0L) int h, int ct, int at, @Nullable ColorSpace cs) {
        this.width = w;
        this.height = h;
        this.colorType = (short) ct;
        this.alphaType = (short) at;
        this.colorSpace = cs;
    }

    @Internal
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public int colorType() {
        return this.colorType;
    }

    public int alphaType() {
        return this.alphaType;
    }

    @Nullable
    public ColorSpace colorSpace() {
        return this.colorSpace;
    }

    public int bytesPerPixel() {
        return bytesPerPixel(this.colorType());
    }

    public int minRowBytes() {
        return this.width() * this.bytesPerPixel();
    }

    public boolean isEmpty() {
        return this.width <= 0 && this.height <= 0;
    }

    public boolean isOpaque() {
        return this.alphaType == 1 || (colorTypeChannelFlags(this.colorType) & 8) == 0;
    }

    public boolean isValid() {
        return this.width > 0 && this.height > 0 && this.colorType != 0 && this.alphaType != 0;
    }

    @Nonnull
    public ImageInfo makeWH(int newWidth, int newHeight) {
        return new ImageInfo(newWidth, newHeight, this.colorType, this.alphaType, this.colorSpace);
    }

    @Nonnull
    public ImageInfo makeAlphaType(int newAlphaType) {
        return new ImageInfo(this.width, this.height, this.colorType, newAlphaType, this.colorSpace);
    }

    @Nonnull
    public ImageInfo makeColorType(int newColorType) {
        return new ImageInfo(this.width, this.height, newColorType, this.alphaType, this.colorSpace);
    }

    @Nonnull
    public ImageInfo makeColorSpace(@Nullable ColorSpace newColorSpace) {
        return new ImageInfo(this.width, this.height, this.colorType, this.alphaType, newColorSpace);
    }

    public int hashCode() {
        int result = this.width;
        result = 31 * result + this.height;
        result = 31 * result + this.colorType;
        result = 31 * result + this.alphaType;
        return 31 * result + Objects.hashCode(this.colorSpace);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof ImageInfo ii) ? false : this.width == ii.width && this.height == ii.height && this.colorType == ii.colorType && this.alphaType == ii.alphaType && Objects.equals(this.colorSpace, ii.colorSpace);
        }
    }

    public String toString() {
        return "{dimensions=" + this.width + "x" + this.height + ", colorType=" + this.colorType + ", alphaType=" + this.alphaType + ", colorSpace=" + this.colorSpace + "}";
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface AlphaType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface ColorType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface CompressionType {
    }
}