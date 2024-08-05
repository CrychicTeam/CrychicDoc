package icyllis.arc3d.engine;

import icyllis.arc3d.core.MathUtil;
import org.jetbrains.annotations.Contract;

public final class SamplerState {

    public static final int FILTER_NEAREST = 0;

    public static final int FILTER_LINEAR = 1;

    public static final int MIPMAP_MODE_NONE = 0;

    public static final int MIPMAP_MODE_NEAREST = 1;

    public static final int MIPMAP_MODE_LINEAR = 2;

    public static final int ADDRESS_MODE_REPEAT = 0;

    public static final int ADDRESS_MODE_MIRRORED_REPEAT = 1;

    public static final int ADDRESS_MODE_CLAMP_TO_EDGE = 2;

    public static final int ADDRESS_MODE_CLAMP_TO_BORDER = 3;

    public static final int DEFAULT = 1188369;

    @Contract(pure = true)
    public static int make(int filter) {
        assert filter == 0 || filter == 1;
        return 1188352 | filter | filter << 4;
    }

    @Contract(pure = true)
    public static int make(int filter, int mipmap) {
        assert filter == 0 || filter == 1;
        assert mipmap == 0 || mipmap == 1 || mipmap == 2;
        return 1187840 | filter | filter << 4 | mipmap << 8;
    }

    @Contract(pure = true)
    public static int make(int filter, int mipmap, int address) {
        return make(filter, filter, mipmap, address, address);
    }

    @Contract(pure = true)
    public static int make(int magFilter, int minFilter, int mipmapMode, int addressModeX, int addressModeY) {
        assert magFilter == 0 || magFilter == 1;
        assert minFilter == 0 || minFilter == 1;
        assert mipmapMode == 0 || mipmapMode == 1 || mipmapMode == 2;
        assert addressModeX >= 0 && addressModeX <= 3;
        assert addressModeY >= 0 && addressModeY <= 3;
        return 1048576 | magFilter | minFilter << 4 | mipmapMode << 8 | addressModeX << 12 | addressModeY << 16;
    }

    @Contract(pure = true)
    public static int makeAnisotropy(int addressModeX, int addressModeY, int maxAnisotropy, boolean isMipmapped) {
        assert addressModeX >= 0 && addressModeX <= 3;
        assert addressModeY >= 0 && addressModeY <= 3;
        return 17 | addressModeX << 12 | addressModeY << 16 | (isMipmapped ? 2 : 0) << 8 | MathUtil.clamp(maxAnisotropy, 1, 1024) << 20;
    }

    @Contract(pure = true)
    public static int getMagFilter(int samplerState) {
        return samplerState & 15;
    }

    @Contract(pure = true)
    public static int getMinFilter(int samplerState) {
        return samplerState >> 4 & 15;
    }

    @Contract(pure = true)
    public static int getMipmapMode(int samplerState) {
        return samplerState >> 8 & 15;
    }

    @Contract(pure = true)
    public static int getAddressModeX(int samplerState) {
        return samplerState >> 12 & 15;
    }

    @Contract(pure = true)
    public static int getAddressModeY(int samplerState) {
        return samplerState >> 16 & 15;
    }

    @Contract(pure = true)
    public static boolean isMipmapped(int samplerState) {
        return getMipmapMode(samplerState) != 0;
    }

    @Contract(pure = true)
    public static boolean isRepeatedX(int samplerState) {
        int addressX = getAddressModeX(samplerState);
        return addressX == 0 || addressX == 1;
    }

    @Contract(pure = true)
    public static boolean isRepeatedY(int samplerState) {
        int addressY = getAddressModeY(samplerState);
        return addressY == 0 || addressY == 1;
    }

    @Contract(pure = true)
    public static boolean isRepeated(int samplerState) {
        return isRepeatedX(samplerState) || isRepeatedY(samplerState);
    }

    @Contract(pure = true)
    public static int getMaxAnisotropy(int samplerState) {
        return samplerState >>> 20;
    }

    @Contract(pure = true)
    public static boolean isAnisotropy(int samplerState) {
        return getMaxAnisotropy(samplerState) > 1;
    }

    @Contract(pure = true)
    public static int resetMipmapMode(int samplerState) {
        return samplerState & -3841;
    }

    static {
        assert make(1) == 1188369;
        assert make(1, 2) == 1188369;
        assert make(1, 2, 2) == 1188369;
        assert getMagFilter(1188369) == 1;
        assert getMinFilter(1188369) == 1;
        assert getMipmapMode(1188369) == 2;
        assert getAddressModeX(1188369) == 2;
        assert getAddressModeY(1188369) == 2;
        assert getMaxAnisotropy(1188369) == 1;
    }
}