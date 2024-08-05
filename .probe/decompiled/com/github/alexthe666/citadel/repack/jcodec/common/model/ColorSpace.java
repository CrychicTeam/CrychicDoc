package com.github.alexthe666.citadel.repack.jcodec.common.model;

public final class ColorSpace {

    public static final int MAX_PLANES = 4;

    public int nComp;

    public int[] compPlane;

    public int[] compWidth;

    public int[] compHeight;

    public boolean planar;

    private String _name;

    public int bitsPerPixel;

    private static final int[] _000 = new int[] { 0, 0, 0 };

    private static final int[] _011 = new int[] { 0, 1, 1 };

    private static final int[] _012 = new int[] { 0, 1, 2 };

    public static final ColorSpace BGR = new ColorSpace("BGR", 3, _000, _000, _000, false);

    public static final ColorSpace RGB = new ColorSpace("RGB", 3, _000, _000, _000, false);

    public static final ColorSpace YUV420 = new ColorSpace("YUV420", 3, _012, _011, _011, true);

    public static final ColorSpace YUV420J = new ColorSpace("YUV420J", 3, _012, _011, _011, true);

    public static final ColorSpace YUV422 = new ColorSpace("YUV422", 3, _012, _011, _000, true);

    public static final ColorSpace YUV422J = new ColorSpace("YUV422J", 3, _012, _011, _000, true);

    public static final ColorSpace YUV444 = new ColorSpace("YUV444", 3, _012, _000, _000, true);

    public static final ColorSpace YUV444J = new ColorSpace("YUV444J", 3, _012, _000, _000, true);

    public static final ColorSpace YUV422_10 = new ColorSpace("YUV422_10", 3, _012, _011, _000, true);

    public static final ColorSpace GREY = new ColorSpace("GREY", 1, new int[] { 0 }, new int[] { 0 }, new int[] { 0 }, true);

    public static final ColorSpace MONO = new ColorSpace("MONO", 1, _000, _000, _000, true);

    public static final ColorSpace YUV444_10 = new ColorSpace("YUV444_10", 3, _012, _000, _000, true);

    public static final ColorSpace ANY = new ColorSpace("ANY", 0, null, null, null, true);

    public static final ColorSpace ANY_PLANAR = new ColorSpace("ANY_PLANAR", 0, null, null, null, true);

    public static final ColorSpace ANY_INTERLEAVED = new ColorSpace("ANY_INTERLEAVED", 0, null, null, null, false);

    public static final ColorSpace SAME = new ColorSpace("SAME", 0, null, null, null, false);

    private ColorSpace(String name, int nComp, int[] compPlane, int[] compWidth, int[] compHeight, boolean planar) {
        this._name = name;
        this.nComp = nComp;
        this.compPlane = compPlane;
        this.compWidth = compWidth;
        this.compHeight = compHeight;
        this.planar = planar;
        this.bitsPerPixel = calcBitsPerPixel(nComp, compWidth, compHeight);
    }

    public String toString() {
        return this._name;
    }

    private static int calcBitsPerPixel(int nComp, int[] compWidth, int[] compHeight) {
        int bitsPerPixel = 0;
        for (int i = 0; i < nComp; i++) {
            bitsPerPixel += 8 >> compWidth[i] >> compHeight[i];
        }
        return bitsPerPixel;
    }

    public int getWidthMask() {
        return ~(this.nComp > 1 ? this.compWidth[1] : 0);
    }

    public int getHeightMask() {
        return ~(this.nComp > 1 ? this.compHeight[1] : 0);
    }

    public boolean matches(ColorSpace inputColor) {
        if (inputColor == this) {
            return true;
        } else {
            return inputColor != ANY && this != ANY ? (inputColor == ANY_INTERLEAVED || this == ANY_INTERLEAVED || inputColor == ANY_PLANAR || this == ANY_PLANAR) && inputColor.planar == this.planar : true;
        }
    }

    public Size compSize(Size size, int comp) {
        return this.compWidth[comp] == 0 && this.compHeight[comp] == 0 ? size : new Size(size.getWidth() >> this.compWidth[comp], size.getHeight() >> this.compHeight[comp]);
    }
}