package icyllis.arc3d.core;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.Range;

public abstract class ColorSpace {

    public static final float[] ILLUMINANT_A = new float[] { 0.44757F, 0.40745F };

    public static final float[] ILLUMINANT_B = new float[] { 0.34842F, 0.35161F };

    public static final float[] ILLUMINANT_C = new float[] { 0.31006F, 0.31616F };

    public static final float[] ILLUMINANT_D50 = new float[] { 0.34567F, 0.3585F };

    public static final float[] ILLUMINANT_D55 = new float[] { 0.33242F, 0.34743F };

    public static final float[] ILLUMINANT_D60 = new float[] { 0.32168F, 0.33767F };

    public static final float[] ILLUMINANT_D65 = new float[] { 0.31271F, 0.32902F };

    public static final float[] ILLUMINANT_D75 = new float[] { 0.29902F, 0.31485F };

    public static final float[] ILLUMINANT_E = new float[] { 0.33333F, 0.33333F };

    public static final int MIN_ID = -1;

    public static final int MAX_ID = 63;

    private static final float[] SRGB_PRIMARIES = new float[] { 0.64F, 0.33F, 0.3F, 0.6F, 0.15F, 0.06F };

    private static final float[] NTSC_1953_PRIMARIES = new float[] { 0.67F, 0.33F, 0.21F, 0.71F, 0.14F, 0.08F };

    private static final float[] GRAY_PRIMARIES = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F };

    private static final float[] ILLUMINANT_D50_XYZ = new float[] { 0.964212F, 1.0F, 0.825188F };

    private static final ColorSpace.Rgb.TransferParameters SRGB_TRANSFER_PARAMETERS = new ColorSpace.Rgb.TransferParameters(0.9478672985781991, 0.05213270142180095, 0.07739938080495357, 0.04045, 2.4);

    @Nonnull
    private final String mName;

    @Nonnull
    private final ColorSpace.Model mModel;

    @Range(from = -1L, to = 63L)
    private final int mId;

    ColorSpace(@Nonnull @Size(min = 1L) String name, @Nonnull ColorSpace.Model model, @Range(from = -1L, to = 63L) int id) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The name of a color space cannot be null and must contain at least 1 character");
        } else if (id >= -1 && id <= 63) {
            this.mName = name;
            this.mModel = model;
            this.mId = id;
        } else {
            throw new IllegalArgumentException("The id must be between -1 and 63");
        }
    }

    @Nonnull
    public String getName() {
        return this.mName;
    }

    @Range(from = -1L, to = 63L)
    public int getId() {
        return this.mId;
    }

    @Nonnull
    public ColorSpace.Model getModel() {
        return this.mModel;
    }

    @Range(from = 1L, to = 4L)
    public int getComponentCount() {
        return this.mModel.getComponentCount();
    }

    public abstract boolean isWideGamut();

    public boolean isSrgb() {
        return false;
    }

    public abstract float getMinValue(@Range(from = 0L, to = 3L) int var1);

    public abstract float getMaxValue(@Range(from = 0L, to = 3L) int var1);

    @Nonnull
    @Size(3L)
    public float[] toXyz(float r, float g, float b) {
        return this.toXyz(new float[] { r, g, b });
    }

    @Nonnull
    @Size(min = 3L)
    public abstract float[] toXyz(@Nonnull @Size(min = 3L) float[] var1);

    @Nonnull
    @Size(min = 3L)
    public float[] fromXyz(float x, float y, float z) {
        float[] xyz = new float[this.mModel.getComponentCount()];
        xyz[0] = x;
        xyz[1] = y;
        xyz[2] = z;
        return this.fromXyz(xyz);
    }

    @Nonnull
    @Size(min = 3L)
    public abstract float[] fromXyz(@Nonnull @Size(min = 3L) float[] var1);

    public int hashCode() {
        int result = this.mName.hashCode();
        result = 31 * result + this.mModel.hashCode();
        return 31 * result + this.mId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ColorSpace that = (ColorSpace) o;
            return this.mId == that.mId && this.mModel == that.mModel && this.mName.equals(that.mName);
        } else {
            return false;
        }
    }

    @Nonnull
    public String toString() {
        return this.mName + " (id=" + this.mId + ", model=" + this.mModel + ")";
    }

    @Nonnull
    public static ColorSpace.Connector connect(@Nonnull ColorSpace source, @Nonnull ColorSpace destination) {
        return connect(source, destination, ColorSpace.RenderIntent.PERCEPTUAL);
    }

    @Nonnull
    public static ColorSpace.Connector connect(@Nonnull ColorSpace source, @Nonnull ColorSpace destination, @Nonnull ColorSpace.RenderIntent intent) {
        if (source.equals(destination)) {
            return ColorSpace.Connector.identity(source);
        } else {
            return (ColorSpace.Connector) (source.getModel() == ColorSpace.Model.RGB && destination.getModel() == ColorSpace.Model.RGB ? new ColorSpace.Connector.Rgb((ColorSpace.Rgb) source, (ColorSpace.Rgb) destination, intent) : new ColorSpace.Connector(source, destination, intent));
        }
    }

    @Nonnull
    public static ColorSpace.Connector connect(@Nonnull ColorSpace source) {
        return connect(source, ColorSpace.RenderIntent.PERCEPTUAL);
    }

    @Nonnull
    public static ColorSpace.Connector connect(@Nonnull ColorSpace source, @Nonnull ColorSpace.RenderIntent intent) {
        if (source.isSrgb()) {
            return ColorSpace.Connector.identity(source);
        } else {
            return (ColorSpace.Connector) (source.getModel() == ColorSpace.Model.RGB ? new ColorSpace.Connector.Rgb((ColorSpace.Rgb) source, (ColorSpace.Rgb) get(ColorSpace.Named.SRGB), intent) : new ColorSpace.Connector(source, get(ColorSpace.Named.SRGB), intent));
        }
    }

    @Nonnull
    public static ColorSpace adapt(@Nonnull ColorSpace colorSpace, @Nonnull @Size(min = 2L, max = 3L) float[] whitePoint) {
        return adapt(colorSpace, whitePoint, ColorSpace.Adaptation.BRADFORD);
    }

    @Nonnull
    public static ColorSpace adapt(@Nonnull ColorSpace colorSpace, @Nonnull @Size(min = 2L, max = 3L) float[] whitePoint, @Nonnull ColorSpace.Adaptation adaptation) {
        if (colorSpace.getModel() == ColorSpace.Model.RGB) {
            ColorSpace.Rgb rgb = (ColorSpace.Rgb) colorSpace;
            if (compare(rgb.mWhitePoint, whitePoint)) {
                return colorSpace;
            } else {
                float[] xyz = whitePoint.length == 3 ? Arrays.copyOf(whitePoint, 3) : xyYToXyz(whitePoint);
                float[] adaptationTransform = chromaticAdaptation(adaptation.mTransform, xyYToXyz(rgb.getWhitePoint()), xyz);
                float[] transform = mul3x3(adaptationTransform, rgb.mTransform);
                return new ColorSpace.Rgb(rgb, transform, whitePoint);
            }
        } else {
            return colorSpace;
        }
    }

    @Nonnull
    static ColorSpace get(@Range(from = -1L, to = 63L) int index) {
        if (index >= 0 && index < ColorSpace.Named.sNamedColorSpaces.length) {
            return ColorSpace.Named.sNamedColorSpaces[index];
        } else {
            throw new IllegalArgumentException("Invalid ID, must be in the range [0.." + ColorSpace.Named.sNamedColorSpaces.length + ")");
        }
    }

    @Nonnull
    public static ColorSpace get(@Nonnull ColorSpace.Named name) {
        return ColorSpace.Named.sNamedColorSpaces[name.ordinal()];
    }

    @Nullable
    public static ColorSpace match(@Nonnull @Size(9L) float[] toXYZD50, @Nonnull ColorSpace.Rgb.TransferParameters function) {
        for (ColorSpace colorSpace : ColorSpace.Named.sNamedColorSpaces) {
            if (colorSpace.getModel() == ColorSpace.Model.RGB) {
                ColorSpace.Rgb rgb = (ColorSpace.Rgb) adapt(colorSpace, ILLUMINANT_D50_XYZ);
                if (compare(toXYZD50, rgb.mTransform) && compare(function, rgb.mTransferParameters)) {
                    return colorSpace;
                }
            }
        }
        return null;
    }

    @Nonnull
    @Size(9L)
    private static float[] adaptToIlluminantD50(@Nonnull @Size(2L) float[] origWhitePoint, @Nonnull @Size(9L) float[] origTransform) {
        float[] desired = ILLUMINANT_D50;
        if (compare(origWhitePoint, desired)) {
            return origTransform;
        } else {
            float[] xyz = xyYToXyz(desired);
            float[] adaptationTransform = chromaticAdaptation(ColorSpace.Adaptation.BRADFORD.mTransform, xyYToXyz(origWhitePoint), xyz);
            return mul3x3(adaptationTransform, origTransform);
        }
    }

    private static double rcpResponse(double x, double a, double b, double c, double d, double g) {
        return x >= d * c ? (Math.pow(x, 1.0 / g) - b) / a : x / c;
    }

    private static double response(double x, double a, double b, double c, double d, double g) {
        return x >= d ? Math.pow(a * x + b, g) : c * x;
    }

    private static double rcpResponse(double x, double a, double b, double c, double d, double e, double f, double g) {
        return x >= d * c ? (Math.pow(x - e, 1.0 / g) - b) / a : (x - f) / c;
    }

    private static double response(double x, double a, double b, double c, double d, double e, double f, double g) {
        return x >= d ? Math.pow(a * x + b, g) + e : c * x + f;
    }

    private static double absRcpResponse(double x, double a, double b, double c, double d, double g) {
        return Math.copySign(rcpResponse(x < 0.0 ? -x : x, a, b, c, d, g), x);
    }

    private static double absResponse(double x, double a, double b, double c, double d, double g) {
        return Math.copySign(response(x < 0.0 ? -x : x, a, b, c, d, g), x);
    }

    private static boolean compare(@Nullable ColorSpace.Rgb.TransferParameters a, @Nullable ColorSpace.Rgb.TransferParameters b) {
        return a == null && b == null ? true : a != null && b != null && Math.abs(a.a - b.a) < 0.001 && Math.abs(a.b - b.b) < 0.001 && Math.abs(a.c - b.c) < 0.001 && Math.abs(a.d - b.d) < 0.002 && Math.abs(a.e - b.e) < 0.001 && Math.abs(a.f - b.f) < 0.001 && Math.abs(a.g - b.g) < 0.001;
    }

    private static boolean compare(@Nonnull float[] a, @Nonnull float[] b) {
        if (a == b) {
            return true;
        } else {
            for (int i = 0; i < a.length; i++) {
                if (Float.compare(a[i], b[i]) != 0 && Math.abs(a[i] - b[i]) > 0.001F) {
                    return false;
                }
            }
            return true;
        }
    }

    @Nonnull
    @Size(9L)
    private static float[] inverse3x3(@Nonnull @Size(9L) float[] m) {
        float a = m[0];
        float b = m[3];
        float c = m[6];
        float d = m[1];
        float e = m[4];
        float f = m[7];
        float g = m[2];
        float h = m[5];
        float i = m[8];
        float A = e * i - f * h;
        float B = f * g - d * i;
        float C = d * h - e * g;
        float det = a * A + b * B + c * C;
        float[] inverted = new float[m.length];
        inverted[0] = A / det;
        inverted[1] = B / det;
        inverted[2] = C / det;
        inverted[3] = (c * h - b * i) / det;
        inverted[4] = (a * i - c * g) / det;
        inverted[5] = (b * g - a * h) / det;
        inverted[6] = (b * f - c * e) / det;
        inverted[7] = (c * d - a * f) / det;
        inverted[8] = (a * e - b * d) / det;
        return inverted;
    }

    @Nonnull
    @Size(9L)
    private static float[] mul3x3(@Nonnull @Size(9L) float[] lhs, @Nonnull @Size(9L) float[] rhs) {
        return new float[] { lhs[0] * rhs[0] + lhs[3] * rhs[1] + lhs[6] * rhs[2], lhs[1] * rhs[0] + lhs[4] * rhs[1] + lhs[7] * rhs[2], lhs[2] * rhs[0] + lhs[5] * rhs[1] + lhs[8] * rhs[2], lhs[0] * rhs[3] + lhs[3] * rhs[4] + lhs[6] * rhs[5], lhs[1] * rhs[3] + lhs[4] * rhs[4] + lhs[7] * rhs[5], lhs[2] * rhs[3] + lhs[5] * rhs[4] + lhs[8] * rhs[5], lhs[0] * rhs[6] + lhs[3] * rhs[7] + lhs[6] * rhs[8], lhs[1] * rhs[6] + lhs[4] * rhs[7] + lhs[7] * rhs[8], lhs[2] * rhs[6] + lhs[5] * rhs[7] + lhs[8] * rhs[8] };
    }

    @Nonnull
    @Size(min = 3L)
    private static float[] mul3x3Float3(@Nonnull @Size(9L) float[] lhs, @Nonnull @Size(min = 3L) float[] rhs) {
        float r0 = rhs[0];
        float r1 = rhs[1];
        float r2 = rhs[2];
        rhs[0] = lhs[0] * r0 + lhs[3] * r1 + lhs[6] * r2;
        rhs[1] = lhs[1] * r0 + lhs[4] * r1 + lhs[7] * r2;
        rhs[2] = lhs[2] * r0 + lhs[5] * r1 + lhs[8] * r2;
        return rhs;
    }

    @Nonnull
    @Size(9L)
    private static float[] mul3x3Diag(@Nonnull @Size(3L) float[] lhs, @Nonnull @Size(9L) float[] rhs) {
        return new float[] { lhs[0] * rhs[0], lhs[1] * rhs[1], lhs[2] * rhs[2], lhs[0] * rhs[3], lhs[1] * rhs[4], lhs[2] * rhs[5], lhs[0] * rhs[6], lhs[1] * rhs[7], lhs[2] * rhs[8] };
    }

    @Nonnull
    @Size(3L)
    private static float[] xyYToXyz(@Nonnull @Size(2L) float[] xyY) {
        return new float[] { xyY[0] / xyY[1], 1.0F, (1.0F - xyY[0] - xyY[1]) / xyY[1] };
    }

    @Nonnull
    @Size(9L)
    private static float[] chromaticAdaptation(@Nonnull @Size(9L) float[] matrix, @Nonnull @Size(3L) float[] srcWhitePoint, @Nonnull @Size(3L) float[] dstWhitePoint) {
        float[] srcLMS = mul3x3Float3(matrix, srcWhitePoint);
        float[] dstLMS = mul3x3Float3(matrix, dstWhitePoint);
        float[] LMS = new float[] { dstLMS[0] / srcLMS[0], dstLMS[1] / srcLMS[1], dstLMS[2] / srcLMS[2] };
        return mul3x3(inverse3x3(matrix), mul3x3Diag(LMS, matrix));
    }

    @Nonnull
    @Size(3L)
    public static float[] cctToXyz(@Range(from = 1L, to = 2147483647L) int cct) {
        if (cct < 1) {
            throw new IllegalArgumentException("Temperature must be greater than 0");
        } else {
            float icct = 1000.0F / (float) cct;
            float icct2 = icct * icct;
            float x = (float) cct <= 4000.0F ? 0.17991F + 0.8776956F * icct - 0.2343589F * icct2 - 0.2661239F * icct2 * icct : 0.24039F + 0.2226347F * icct + 2.1070378F * icct2 - 3.025847F * icct2 * icct;
            float x2 = x * x;
            float y = (float) cct <= 2222.0F ? -0.20219684F + 2.1855583F * x - 1.3481102F * x2 - 1.1063814F * x2 * x : ((float) cct <= 4000.0F ? -0.16748866F + 2.09137F * x - 1.3741859F * x2 - 0.9549476F * x2 * x : -0.37001482F + 3.7511299F * x - 5.873387F * x2 + 3.081758F * x2 * x);
            return xyYToXyz(new float[] { x, y });
        }
    }

    @Nonnull
    @Size(9L)
    public static float[] chromaticAdaptation(@Nonnull ColorSpace.Adaptation adaptation, @Nonnull @Size(min = 2L, max = 3L) float[] srcWhitePoint, @Nonnull @Size(min = 2L, max = 3L) float[] dstWhitePoint) {
        if ((srcWhitePoint.length == 2 || srcWhitePoint.length == 3) && (dstWhitePoint.length == 2 || dstWhitePoint.length == 3)) {
            float[] srcXyz = srcWhitePoint.length == 3 ? Arrays.copyOf(srcWhitePoint, 3) : xyYToXyz(srcWhitePoint);
            float[] dstXyz = dstWhitePoint.length == 3 ? Arrays.copyOf(dstWhitePoint, 3) : xyYToXyz(dstWhitePoint);
            return compare(srcXyz, dstXyz) ? new float[] { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F } : chromaticAdaptation(adaptation.mTransform, srcXyz, dstXyz);
        } else {
            throw new IllegalArgumentException("A white point array must have 2 or 3 floats");
        }
    }

    public static enum Adaptation {

        BRADFORD(new float[] { 0.8951F, -0.7502F, 0.0389F, 0.2664F, 1.7135F, -0.0685F, -0.1614F, 0.0367F, 1.0296F }), VON_KRIES(new float[] { 0.40024F, -0.2263F, 0.0F, 0.7076F, 1.16532F, 0.0F, -0.08081F, 0.0457F, 0.91822F }), CIECAT02(new float[] { 0.7328F, -0.7036F, 0.003F, 0.4296F, 1.6975F, 0.0136F, -0.1624F, 0.0061F, 0.9834F });

        final float[] mTransform;

        private Adaptation(@Nonnull @Size(9L) float[] transform) {
            this.mTransform = transform;
        }
    }

    public static class Connector {

        @Nonnull
        private final ColorSpace mSource;

        @Nonnull
        private final ColorSpace mDestination;

        @Nonnull
        private final ColorSpace mTransformSource;

        @Nonnull
        private final ColorSpace mTransformDestination;

        @Nonnull
        private final ColorSpace.RenderIntent mIntent;

        @Nullable
        @Size(3L)
        private final float[] mTransform;

        Connector(@Nonnull ColorSpace source, @Nonnull ColorSpace destination, @Nonnull ColorSpace.RenderIntent intent) {
            this(source, destination, source.getModel() == ColorSpace.Model.RGB ? ColorSpace.adapt(source, ColorSpace.ILLUMINANT_D50_XYZ) : source, destination.getModel() == ColorSpace.Model.RGB ? ColorSpace.adapt(destination, ColorSpace.ILLUMINANT_D50_XYZ) : destination, intent, computeTransform(source, destination, intent));
        }

        private Connector(@Nonnull ColorSpace source, @Nonnull ColorSpace destination, @Nonnull ColorSpace transformSource, @Nonnull ColorSpace transformDestination, @Nonnull ColorSpace.RenderIntent intent, @Nullable @Size(3L) float[] transform) {
            this.mSource = source;
            this.mDestination = destination;
            this.mTransformSource = transformSource;
            this.mTransformDestination = transformDestination;
            this.mIntent = intent;
            this.mTransform = transform;
        }

        @Nullable
        private static float[] computeTransform(@Nonnull ColorSpace source, @Nonnull ColorSpace destination, @Nonnull ColorSpace.RenderIntent intent) {
            if (intent != ColorSpace.RenderIntent.ABSOLUTE) {
                return null;
            } else {
                boolean srcRGB = source.getModel() == ColorSpace.Model.RGB;
                boolean dstRGB = destination.getModel() == ColorSpace.Model.RGB;
                if (srcRGB && dstRGB) {
                    return null;
                } else if (!srcRGB && !dstRGB) {
                    return null;
                } else {
                    ColorSpace.Rgb rgb = (ColorSpace.Rgb) (srcRGB ? source : destination);
                    float[] srcXYZ = srcRGB ? ColorSpace.xyYToXyz(rgb.mWhitePoint) : ColorSpace.ILLUMINANT_D50_XYZ;
                    float[] dstXYZ = dstRGB ? ColorSpace.xyYToXyz(rgb.mWhitePoint) : ColorSpace.ILLUMINANT_D50_XYZ;
                    return new float[] { srcXYZ[0] / dstXYZ[0], srcXYZ[1] / dstXYZ[1], srcXYZ[2] / dstXYZ[2] };
                }
            }
        }

        @Nonnull
        public ColorSpace getSource() {
            return this.mSource;
        }

        @Nonnull
        public ColorSpace getDestination() {
            return this.mDestination;
        }

        public ColorSpace.RenderIntent getRenderIntent() {
            return this.mIntent;
        }

        @Nonnull
        @Size(3L)
        public float[] transform(float r, float g, float b) {
            return this.transform(new float[] { r, g, b });
        }

        @Nonnull
        @Size(min = 3L)
        public float[] transform(@Nonnull @Size(min = 3L) float[] v) {
            float[] xyz = this.mTransformSource.toXyz(v);
            if (this.mTransform != null) {
                xyz[0] *= this.mTransform[0];
                xyz[1] *= this.mTransform[1];
                xyz[2] *= this.mTransform[2];
            }
            return this.mTransformDestination.fromXyz(xyz);
        }

        @Nonnull
        static ColorSpace.Connector identity(@Nonnull ColorSpace source) {
            return new ColorSpace.Connector(source, ColorSpace.RenderIntent.RELATIVE) {

                @Nonnull
                @Override
                public float[] transform(@Nonnull @Size(min = 3L) float[] v) {
                    return v;
                }
            };
        }

        private static class Rgb extends ColorSpace.Connector {

            @Nonnull
            private final ColorSpace.Rgb mSource;

            @Nonnull
            private final ColorSpace.Rgb mDestination;

            @Nonnull
            private final float[] mTransform;

            Rgb(@Nonnull ColorSpace.Rgb source, @Nonnull ColorSpace.Rgb destination, @Nonnull ColorSpace.RenderIntent intent) {
                super(source, destination, source, destination, intent, null);
                this.mSource = source;
                this.mDestination = destination;
                this.mTransform = computeTransform(source, destination, intent);
            }

            @Nonnull
            @Override
            public float[] transform(@Nonnull @Size(min = 3L) float[] rgb) {
                rgb[0] = (float) this.mSource.mClampedEotf.applyAsDouble((double) rgb[0]);
                rgb[1] = (float) this.mSource.mClampedEotf.applyAsDouble((double) rgb[1]);
                rgb[2] = (float) this.mSource.mClampedEotf.applyAsDouble((double) rgb[2]);
                ColorSpace.mul3x3Float3(this.mTransform, rgb);
                rgb[0] = (float) this.mDestination.mClampedOetf.applyAsDouble((double) rgb[0]);
                rgb[1] = (float) this.mDestination.mClampedOetf.applyAsDouble((double) rgb[1]);
                rgb[2] = (float) this.mDestination.mClampedOetf.applyAsDouble((double) rgb[2]);
                return rgb;
            }

            @Nonnull
            @Size(9L)
            private static float[] computeTransform(@Nonnull ColorSpace.Rgb source, @Nonnull ColorSpace.Rgb destination, @Nonnull ColorSpace.RenderIntent intent) {
                if (ColorSpace.compare(source.mWhitePoint, destination.mWhitePoint)) {
                    return ColorSpace.mul3x3(destination.mInverseTransform, source.mTransform);
                } else {
                    float[] transform = source.mTransform;
                    float[] inverseTransform = destination.mInverseTransform;
                    float[] srcXYZ = ColorSpace.xyYToXyz(source.mWhitePoint);
                    float[] dstXYZ = ColorSpace.xyYToXyz(destination.mWhitePoint);
                    if (!ColorSpace.compare(source.mWhitePoint, ColorSpace.ILLUMINANT_D50)) {
                        float[] srcAdaptation = ColorSpace.chromaticAdaptation(ColorSpace.Adaptation.BRADFORD.mTransform, srcXYZ, Arrays.copyOf(ColorSpace.ILLUMINANT_D50_XYZ, 3));
                        transform = ColorSpace.mul3x3(srcAdaptation, source.mTransform);
                    }
                    if (!ColorSpace.compare(destination.mWhitePoint, ColorSpace.ILLUMINANT_D50)) {
                        float[] dstAdaptation = ColorSpace.chromaticAdaptation(ColorSpace.Adaptation.BRADFORD.mTransform, dstXYZ, Arrays.copyOf(ColorSpace.ILLUMINANT_D50_XYZ, 3));
                        inverseTransform = ColorSpace.inverse3x3(ColorSpace.mul3x3(dstAdaptation, destination.mTransform));
                    }
                    if (intent == ColorSpace.RenderIntent.ABSOLUTE) {
                        transform = ColorSpace.mul3x3Diag(new float[] { srcXYZ[0] / dstXYZ[0], srcXYZ[1] / dstXYZ[1], srcXYZ[2] / dstXYZ[2] }, transform);
                    }
                    return ColorSpace.mul3x3(inverseTransform, transform);
                }
            }
        }
    }

    private static final class Lab extends ColorSpace {

        private static final float A = 0.008856452F;

        private static final float B = 7.787037F;

        private static final float C = 0.13793103F;

        private static final float D = 0.20689656F;

        private Lab(@Nonnull String name, @Range(from = -1L, to = 63L) int id) {
            super(name, ColorSpace.Model.LAB, id);
        }

        @Override
        public boolean isWideGamut() {
            return true;
        }

        @Override
        public float getMinValue(@Range(from = 0L, to = 3L) int component) {
            return component == 0 ? 0.0F : -128.0F;
        }

        @Override
        public float getMaxValue(@Range(from = 0L, to = 3L) int component) {
            return component == 0 ? 100.0F : 128.0F;
        }

        @Nonnull
        @Override
        public float[] toXyz(@Nonnull @Size(min = 3L) float[] v) {
            v[0] = MathUtil.clamp(v[0], 0.0F, 100.0F);
            v[1] = MathUtil.clamp(v[1], -128.0F, 128.0F);
            v[2] = MathUtil.clamp(v[2], -128.0F, 128.0F);
            float fy = (v[0] + 16.0F) / 116.0F;
            float fx = fy + v[1] * 0.002F;
            float fz = fy - v[2] * 0.005F;
            float X = fx > 0.20689656F ? fx * fx * fx : 0.12841855F * (fx - 0.13793103F);
            float Y = fy > 0.20689656F ? fy * fy * fy : 0.12841855F * (fy - 0.13793103F);
            float Z = fz > 0.20689656F ? fz * fz * fz : 0.12841855F * (fz - 0.13793103F);
            v[0] = X * ColorSpace.ILLUMINANT_D50_XYZ[0];
            v[1] = Y * ColorSpace.ILLUMINANT_D50_XYZ[1];
            v[2] = Z * ColorSpace.ILLUMINANT_D50_XYZ[2];
            return v;
        }

        @Nonnull
        @Override
        public float[] fromXyz(@Nonnull @Size(min = 3L) float[] v) {
            float X = v[0] / ColorSpace.ILLUMINANT_D50_XYZ[0];
            float Y = v[1] / ColorSpace.ILLUMINANT_D50_XYZ[1];
            float Z = v[2] / ColorSpace.ILLUMINANT_D50_XYZ[2];
            float fx = X > 0.008856452F ? (float) Math.pow((double) X, 0.3333333333333333) : 7.787037F * X + 0.13793103F;
            float fy = Y > 0.008856452F ? (float) Math.pow((double) Y, 0.3333333333333333) : 7.787037F * Y + 0.13793103F;
            float fz = Z > 0.008856452F ? (float) Math.pow((double) Z, 0.3333333333333333) : 7.787037F * Z + 0.13793103F;
            float L = 116.0F * fy - 16.0F;
            float a = 500.0F * (fx - fy);
            float b = 200.0F * (fy - fz);
            v[0] = MathUtil.clamp(L, 0.0F, 100.0F);
            v[1] = MathUtil.clamp(a, -128.0F, 128.0F);
            v[2] = MathUtil.clamp(b, -128.0F, 128.0F);
            return v;
        }
    }

    public static enum Model {

        RGB(3), XYZ(3), LAB(3), CMYK(4);

        private final int mComponentCount;

        private Model(int componentCount) {
            this.mComponentCount = componentCount;
        }

        public int getComponentCount() {
            return this.mComponentCount;
        }
    }

    public static enum Named {

        SRGB,
        LINEAR_SRGB,
        EXTENDED_SRGB,
        LINEAR_EXTENDED_SRGB,
        BT709,
        BT2020,
        DCI_P3,
        DISPLAY_P3,
        NTSC_1953,
        SMPTE_C,
        ADOBE_RGB,
        PRO_PHOTO_RGB,
        ACES,
        ACESCG,
        CIE_XYZ,
        CIE_LAB;

        static final ColorSpace[] sNamedColorSpaces = new ColorSpace[values().length];

        static {
            sNamedColorSpaces[SRGB.ordinal()] = new ColorSpace.Rgb("sRGB IEC61966-2.1", ColorSpace.SRGB_PRIMARIES, ColorSpace.ILLUMINANT_D65, null, ColorSpace.SRGB_TRANSFER_PARAMETERS, SRGB.ordinal());
            sNamedColorSpaces[LINEAR_SRGB.ordinal()] = new ColorSpace.Rgb("sRGB IEC61966-2.1 (Linear)", ColorSpace.SRGB_PRIMARIES, ColorSpace.ILLUMINANT_D65, 1.0, 0.0F, 1.0F, LINEAR_SRGB.ordinal());
            sNamedColorSpaces[EXTENDED_SRGB.ordinal()] = new ColorSpace.Rgb("scRGB-nl IEC 61966-2-2:2003", ColorSpace.SRGB_PRIMARIES, ColorSpace.ILLUMINANT_D65, null, x -> ColorSpace.absRcpResponse(x, 0.9478672985781991, 0.05213270142180095, 0.07739938080495357, 0.04045, 2.4), x -> ColorSpace.absResponse(x, 0.9478672985781991, 0.05213270142180095, 0.07739938080495357, 0.04045, 2.4), -0.799F, 2.399F, ColorSpace.SRGB_TRANSFER_PARAMETERS, EXTENDED_SRGB.ordinal());
            sNamedColorSpaces[LINEAR_EXTENDED_SRGB.ordinal()] = new ColorSpace.Rgb("scRGB IEC 61966-2-2:2003", ColorSpace.SRGB_PRIMARIES, ColorSpace.ILLUMINANT_D65, 1.0, -0.5F, 7.499F, LINEAR_EXTENDED_SRGB.ordinal());
            sNamedColorSpaces[BT709.ordinal()] = new ColorSpace.Rgb("Rec. ITU-R BT.709-5", new float[] { 0.64F, 0.33F, 0.3F, 0.6F, 0.15F, 0.06F }, ColorSpace.ILLUMINANT_D65, null, new ColorSpace.Rgb.TransferParameters(0.9099181073703367, 0.09008189262966333, 0.2222222222222222, 0.081, 2.2222222222222223), BT709.ordinal());
            sNamedColorSpaces[BT2020.ordinal()] = new ColorSpace.Rgb("Rec. ITU-R BT.2020-1", new float[] { 0.708F, 0.292F, 0.17F, 0.797F, 0.131F, 0.046F }, ColorSpace.ILLUMINANT_D65, null, new ColorSpace.Rgb.TransferParameters(0.9096697898662786, 0.09033021013372146, 0.2222222222222222, 0.08145, 2.2222222222222223), BT2020.ordinal());
            sNamedColorSpaces[DCI_P3.ordinal()] = new ColorSpace.Rgb("SMPTE RP 431-2-2007 DCI (P3)", new float[] { 0.68F, 0.32F, 0.265F, 0.69F, 0.15F, 0.06F }, new float[] { 0.314F, 0.351F }, 2.6, 0.0F, 1.0F, DCI_P3.ordinal());
            sNamedColorSpaces[DISPLAY_P3.ordinal()] = new ColorSpace.Rgb("Display P3", new float[] { 0.68F, 0.32F, 0.265F, 0.69F, 0.15F, 0.06F }, ColorSpace.ILLUMINANT_D65, null, ColorSpace.SRGB_TRANSFER_PARAMETERS, DISPLAY_P3.ordinal());
            sNamedColorSpaces[NTSC_1953.ordinal()] = new ColorSpace.Rgb("NTSC (1953)", ColorSpace.NTSC_1953_PRIMARIES, ColorSpace.ILLUMINANT_C, null, new ColorSpace.Rgb.TransferParameters(0.9099181073703367, 0.09008189262966333, 0.2222222222222222, 0.081, 2.2222222222222223), NTSC_1953.ordinal());
            sNamedColorSpaces[SMPTE_C.ordinal()] = new ColorSpace.Rgb("SMPTE-C RGB", new float[] { 0.63F, 0.34F, 0.31F, 0.595F, 0.155F, 0.07F }, ColorSpace.ILLUMINANT_D65, null, new ColorSpace.Rgb.TransferParameters(0.9099181073703367, 0.09008189262966333, 0.2222222222222222, 0.081, 2.2222222222222223), SMPTE_C.ordinal());
            sNamedColorSpaces[ADOBE_RGB.ordinal()] = new ColorSpace.Rgb("Adobe RGB (1998)", new float[] { 0.64F, 0.33F, 0.21F, 0.71F, 0.15F, 0.06F }, ColorSpace.ILLUMINANT_D65, 2.2, 0.0F, 1.0F, ADOBE_RGB.ordinal());
            sNamedColorSpaces[PRO_PHOTO_RGB.ordinal()] = new ColorSpace.Rgb("ROMM RGB ISO 22028-2:2013", new float[] { 0.7347F, 0.2653F, 0.1596F, 0.8404F, 0.0366F, 1.0E-4F }, ColorSpace.ILLUMINANT_D50, null, new ColorSpace.Rgb.TransferParameters(1.0, 0.0, 0.0625, 0.031248, 1.8), PRO_PHOTO_RGB.ordinal());
            sNamedColorSpaces[ACES.ordinal()] = new ColorSpace.Rgb("SMPTE ST 2065-1:2012 ACES", new float[] { 0.7347F, 0.2653F, 0.0F, 1.0F, 1.0E-4F, -0.077F }, ColorSpace.ILLUMINANT_D60, 1.0, -65504.0F, 65504.0F, ACES.ordinal());
            sNamedColorSpaces[ACESCG.ordinal()] = new ColorSpace.Rgb("Academy S-2014-004 ACEScg", new float[] { 0.713F, 0.293F, 0.165F, 0.83F, 0.128F, 0.044F }, ColorSpace.ILLUMINANT_D60, 1.0, -65504.0F, 65504.0F, ACESCG.ordinal());
            sNamedColorSpaces[CIE_XYZ.ordinal()] = new ColorSpace.Xyz("Generic XYZ", CIE_XYZ.ordinal());
            sNamedColorSpaces[CIE_LAB.ordinal()] = new ColorSpace.Lab("Generic L*a*b*", CIE_LAB.ordinal());
        }
    }

    public static enum RenderIntent {

        PERCEPTUAL, RELATIVE, SATURATION, ABSOLUTE
    }

    public static class Rgb extends ColorSpace {

        @Nonnull
        private final float[] mWhitePoint;

        @Nonnull
        private final float[] mPrimaries;

        @Nonnull
        private final float[] mTransform;

        @Nonnull
        private final float[] mInverseTransform;

        @Nonnull
        private final DoubleUnaryOperator mOetf;

        @Nonnull
        private final DoubleUnaryOperator mEotf;

        @Nonnull
        private final DoubleUnaryOperator mClampedOetf;

        @Nonnull
        private final DoubleUnaryOperator mClampedEotf;

        private final float mMin;

        private final float mMax;

        private final boolean mIsWideGamut;

        private final boolean mIsSrgb;

        @Nullable
        private final ColorSpace.Rgb.TransferParameters mTransferParameters;

        public Rgb(@Nonnull @Size(min = 1L) String name, @Nonnull @Size(9L) float[] toXYZ, @Nonnull DoubleUnaryOperator oetf, @Nonnull DoubleUnaryOperator eotf) {
            this(name, computePrimaries(toXYZ), computeWhitePoint(toXYZ), null, oetf, eotf, 0.0F, 1.0F, null, -1);
        }

        public Rgb(@Nonnull @Size(min = 1L) String name, @Nonnull @Size(min = 6L, max = 9L) float[] primaries, @Nonnull @Size(min = 2L, max = 3L) float[] whitePoint, @Nonnull DoubleUnaryOperator oetf, @Nonnull DoubleUnaryOperator eotf, float min, float max) {
            this(name, primaries, whitePoint, null, oetf, eotf, min, max, null, -1);
        }

        public Rgb(@Nonnull @Size(min = 1L) String name, @Nonnull @Size(9L) float[] toXYZ, @Nonnull ColorSpace.Rgb.TransferParameters function) {
            this(name, isGray(toXYZ) ? ColorSpace.GRAY_PRIMARIES : computePrimaries(toXYZ), computeWhitePoint(toXYZ), isGray(toXYZ) ? toXYZ : null, function, -1);
        }

        public Rgb(@Nonnull @Size(min = 1L) String name, @Nonnull @Size(min = 6L, max = 9L) float[] primaries, @Nonnull @Size(min = 2L, max = 3L) float[] whitePoint, @Nonnull ColorSpace.Rgb.TransferParameters function) {
            this(name, primaries, whitePoint, null, function, -1);
        }

        private Rgb(@Nonnull @Size(min = 1L) String name, @Nonnull @Size(min = 6L, max = 9L) float[] primaries, @Nonnull @Size(min = 2L, max = 3L) float[] whitePoint, @Nullable @Size(9L) float[] transform, @Nonnull ColorSpace.Rgb.TransferParameters function, @Range(from = -1L, to = 63L) int id) {
            this(name, primaries, whitePoint, transform, function.e == 0.0 && function.f == 0.0 ? x -> ColorSpace.rcpResponse(x, function.a, function.b, function.c, function.d, function.g) : x -> ColorSpace.rcpResponse(x, function.a, function.b, function.c, function.d, function.e, function.f, function.g), function.e == 0.0 && function.f == 0.0 ? x -> ColorSpace.response(x, function.a, function.b, function.c, function.d, function.g) : x -> ColorSpace.response(x, function.a, function.b, function.c, function.d, function.e, function.f, function.g), 0.0F, 1.0F, function, id);
        }

        public Rgb(@Nonnull @Size(min = 1L) String name, @Nonnull @Size(9L) float[] toXYZ, double gamma) {
            this(name, computePrimaries(toXYZ), computeWhitePoint(toXYZ), gamma, 0.0F, 1.0F, -1);
        }

        public Rgb(@Nonnull @Size(min = 1L) String name, @Nonnull @Size(min = 6L, max = 9L) float[] primaries, @Nonnull @Size(min = 2L, max = 3L) float[] whitePoint, double gamma) {
            this(name, primaries, whitePoint, gamma, 0.0F, 1.0F, -1);
        }

        private Rgb(@Nonnull @Size(min = 1L) String name, @Nonnull @Size(min = 6L, max = 9L) float[] primaries, @Nonnull @Size(min = 2L, max = 3L) float[] whitePoint, double gamma, float min, float max, @Range(from = -1L, to = 63L) int id) {
            this(name, primaries, whitePoint, null, gamma == 1.0 ? DoubleUnaryOperator.identity() : x -> Math.pow(Math.max(x, 0.0), 1.0 / gamma), gamma == 1.0 ? DoubleUnaryOperator.identity() : x -> Math.pow(Math.max(x, 0.0), gamma), min, max, new ColorSpace.Rgb.TransferParameters(1.0, 0.0, 0.0, 0.0, gamma), id);
        }

        private Rgb(@Nonnull @Size(min = 1L) String name, @Nonnull @Size(min = 6L, max = 9L) float[] primaries, @Nonnull @Size(min = 2L, max = 3L) float[] whitePoint, @Nullable @Size(9L) float[] transform, @Nonnull DoubleUnaryOperator oetf, @Nonnull DoubleUnaryOperator eotf, float min, float max, @Nullable ColorSpace.Rgb.TransferParameters transferParameters, @Range(from = -1L, to = 63L) int id) {
            super(name, ColorSpace.Model.RGB, id);
            if (primaries.length != 6 && primaries.length != 9) {
                throw new IllegalArgumentException("The color space's primaries must be defined as an array of 6 floats in xyY or 9 floats in XYZ");
            } else if (whitePoint.length != 2 && whitePoint.length != 3) {
                throw new IllegalArgumentException("The color space's white point must be defined as an array of 2 floats in xyY or 3 float in XYZ");
            } else {
                Objects.requireNonNull(oetf, "The transfer functions of a color space cannot be null");
                Objects.requireNonNull(eotf, "The transfer functions of a color space cannot be null");
                if (min >= max) {
                    throw new IllegalArgumentException("Invalid range: min=" + min + ", max=" + max + "; min must be strictly < max");
                } else {
                    this.mWhitePoint = xyWhitePoint(whitePoint);
                    this.mPrimaries = xyPrimaries(primaries);
                    if (transform == null) {
                        this.mTransform = computeXYZMatrix(this.mPrimaries, this.mWhitePoint);
                    } else {
                        if (transform.length != 9) {
                            throw new IllegalArgumentException("Transform must have 9 entries! Has " + transform.length);
                        }
                        this.mTransform = transform;
                    }
                    this.mInverseTransform = ColorSpace.inverse3x3(this.mTransform);
                    this.mOetf = oetf;
                    this.mEotf = eotf;
                    this.mMin = min;
                    this.mMax = max;
                    DoubleUnaryOperator clamp = x -> MathUtil.clamp(x, (double) min, (double) max);
                    this.mClampedOetf = oetf.andThen(clamp);
                    this.mClampedEotf = clamp.andThen(eotf);
                    this.mTransferParameters = transferParameters;
                    this.mIsWideGamut = isWideGamut(this.mPrimaries, min, max);
                    this.mIsSrgb = isSrgb(this.mPrimaries, this.mWhitePoint, oetf, eotf, min, max, id);
                }
            }
        }

        private Rgb(@Nonnull ColorSpace.Rgb colorSpace, @Nonnull @Size(9L) float[] transform, @Nonnull @Size(min = 2L, max = 3L) float[] whitePoint) {
            this(colorSpace.getName(), colorSpace.mPrimaries, whitePoint, transform, colorSpace.mOetf, colorSpace.mEotf, colorSpace.mMin, colorSpace.mMax, colorSpace.mTransferParameters, -1);
        }

        @Nonnull
        @Size(min = 2L)
        public float[] getWhitePoint(@Nonnull @Size(min = 2L) float[] whitePoint) {
            whitePoint[0] = this.mWhitePoint[0];
            whitePoint[1] = this.mWhitePoint[1];
            return whitePoint;
        }

        @Nonnull
        @Size(2L)
        public float[] getWhitePoint() {
            return (float[]) this.mWhitePoint.clone();
        }

        @Nonnull
        @Size(min = 6L)
        public float[] getPrimaries(@Nonnull @Size(min = 6L) float[] primaries) {
            System.arraycopy(this.mPrimaries, 0, primaries, 0, this.mPrimaries.length);
            return primaries;
        }

        @Nonnull
        @Size(6L)
        public float[] getPrimaries() {
            return (float[]) this.mPrimaries.clone();
        }

        @Nonnull
        @Size(min = 9L)
        public float[] getTransform(@Nonnull @Size(min = 9L) float[] transform) {
            System.arraycopy(this.mTransform, 0, transform, 0, this.mTransform.length);
            return transform;
        }

        @Nonnull
        @Size(9L)
        public float[] getTransform() {
            return (float[]) this.mTransform.clone();
        }

        @Nonnull
        @Size(min = 9L)
        public float[] getInverseTransform(@Nonnull @Size(min = 9L) float[] inverseTransform) {
            System.arraycopy(this.mInverseTransform, 0, inverseTransform, 0, this.mInverseTransform.length);
            return inverseTransform;
        }

        @Nonnull
        @Size(9L)
        public float[] getInverseTransform() {
            return (float[]) this.mInverseTransform.clone();
        }

        @Nonnull
        public DoubleUnaryOperator getOetf() {
            return this.mClampedOetf;
        }

        @Nonnull
        public DoubleUnaryOperator getEotf() {
            return this.mClampedEotf;
        }

        @Nullable
        public ColorSpace.Rgb.TransferParameters getTransferParameters() {
            return this.mTransferParameters;
        }

        @Override
        public boolean isSrgb() {
            return this.mIsSrgb;
        }

        @Override
        public boolean isWideGamut() {
            return this.mIsWideGamut;
        }

        @Override
        public float getMinValue(int component) {
            return this.mMin;
        }

        @Override
        public float getMaxValue(int component) {
            return this.mMax;
        }

        @Nonnull
        @Size(3L)
        public float[] toLinear(float r, float g, float b) {
            return this.toLinear(new float[] { r, g, b });
        }

        @Nonnull
        @Size(min = 3L)
        public float[] toLinear(@Nonnull @Size(min = 3L) float[] v) {
            v[0] = (float) this.mClampedEotf.applyAsDouble((double) v[0]);
            v[1] = (float) this.mClampedEotf.applyAsDouble((double) v[1]);
            v[2] = (float) this.mClampedEotf.applyAsDouble((double) v[2]);
            return v;
        }

        @Nonnull
        @Size(3L)
        public float[] fromLinear(float r, float g, float b) {
            return this.fromLinear(new float[] { r, g, b });
        }

        @Nonnull
        @Size(min = 3L)
        public float[] fromLinear(@Nonnull @Size(min = 3L) float[] v) {
            v[0] = (float) this.mClampedOetf.applyAsDouble((double) v[0]);
            v[1] = (float) this.mClampedOetf.applyAsDouble((double) v[1]);
            v[2] = (float) this.mClampedOetf.applyAsDouble((double) v[2]);
            return v;
        }

        @Nonnull
        @Size(min = 3L)
        @Override
        public float[] toXyz(@Nonnull @Size(min = 3L) float[] v) {
            v[0] = (float) this.mClampedEotf.applyAsDouble((double) v[0]);
            v[1] = (float) this.mClampedEotf.applyAsDouble((double) v[1]);
            v[2] = (float) this.mClampedEotf.applyAsDouble((double) v[2]);
            return ColorSpace.mul3x3Float3(this.mTransform, v);
        }

        @Nonnull
        @Size(min = 3L)
        @Override
        public float[] fromXyz(@Nonnull @Size(min = 3L) float[] v) {
            ColorSpace.mul3x3Float3(this.mInverseTransform, v);
            v[0] = (float) this.mClampedOetf.applyAsDouble((double) v[0]);
            v[1] = (float) this.mClampedOetf.applyAsDouble((double) v[1]);
            v[2] = (float) this.mClampedOetf.applyAsDouble((double) v[2]);
            return v;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + Arrays.hashCode(this.mWhitePoint);
            result = 31 * result + Arrays.hashCode(this.mPrimaries);
            result = 31 * result + (this.mMin != 0.0F ? Float.floatToIntBits(this.mMin) : 0);
            result = 31 * result + (this.mMax != 0.0F ? Float.floatToIntBits(this.mMax) : 0);
            result = 31 * result + (this.mTransferParameters != null ? this.mTransferParameters.hashCode() : 0);
            if (this.mTransferParameters == null) {
                result = 31 * result + this.mOetf.hashCode();
                result = 31 * result + this.mEotf.hashCode();
            }
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o == null || this.getClass() != o.getClass()) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                ColorSpace.Rgb rgb = (ColorSpace.Rgb) o;
                if (Float.compare(rgb.mMin, this.mMin) != 0) {
                    return false;
                } else if (Float.compare(rgb.mMax, this.mMax) != 0) {
                    return false;
                } else if (!Arrays.equals(this.mWhitePoint, rgb.mWhitePoint)) {
                    return false;
                } else if (!Arrays.equals(this.mPrimaries, rgb.mPrimaries)) {
                    return false;
                } else if (this.mTransferParameters != null) {
                    return this.mTransferParameters.equals(rgb.mTransferParameters);
                } else if (rgb.mTransferParameters == null) {
                    return true;
                } else {
                    return !this.mOetf.equals(rgb.mOetf) ? false : this.mEotf.equals(rgb.mEotf);
                }
            }
        }

        private static boolean isSrgb(@Nonnull @Size(6L) float[] primaries, @Nonnull @Size(2L) float[] whitePoint, @Nonnull DoubleUnaryOperator oetf, @Nonnull DoubleUnaryOperator eotf, float min, float max, @Range(from = -1L, to = 63L) int id) {
            if (id == 0) {
                return true;
            } else if (!ColorSpace.compare(primaries, ColorSpace.SRGB_PRIMARIES)) {
                return false;
            } else if (!ColorSpace.compare(whitePoint, ILLUMINANT_D65)) {
                return false;
            } else if (min != 0.0F) {
                return false;
            } else if (max != 1.0F) {
                return false;
            } else {
                ColorSpace.Rgb srgb = (ColorSpace.Rgb) get(ColorSpace.Named.SRGB);
                for (double x = 0.0; x <= 1.0; x += 0.00392156862745098) {
                    if (!compare(x, oetf, srgb.mOetf)) {
                        return false;
                    }
                    if (!compare(x, eotf, srgb.mEotf)) {
                        return false;
                    }
                }
                return true;
            }
        }

        private static boolean isGray(@Nonnull @Size(9L) float[] toXYZ) {
            return toXYZ.length == 9 && toXYZ[1] == 0.0F && toXYZ[2] == 0.0F && toXYZ[3] == 0.0F && toXYZ[5] == 0.0F && toXYZ[6] == 0.0F && toXYZ[7] == 0.0F;
        }

        private static boolean compare(double point, @Nonnull DoubleUnaryOperator a, @Nonnull DoubleUnaryOperator b) {
            double rA = a.applyAsDouble(point);
            double rB = b.applyAsDouble(point);
            return Math.abs(rA - rB) <= 0.001;
        }

        private static boolean isWideGamut(@Nonnull @Size(6L) float[] primaries, float min, float max) {
            return area(primaries) / area(ColorSpace.NTSC_1953_PRIMARIES) > 0.9F && contains(primaries, ColorSpace.SRGB_PRIMARIES) || min < 0.0F && max > 1.0F;
        }

        private static float area(@Nonnull @Size(6L) float[] primaries) {
            float Rx = primaries[0];
            float Ry = primaries[1];
            float Gx = primaries[2];
            float Gy = primaries[3];
            float Bx = primaries[4];
            float By = primaries[5];
            float det = Rx * Gy + Ry * Bx + Gx * By - Gy * Bx - Ry * Gx - Rx * By;
            float r = 0.5F * det;
            return r < 0.0F ? -r : r;
        }

        private static float cross(float ax, float ay, float bx, float by) {
            return ax * by - ay * bx;
        }

        private static boolean contains(@Nonnull @Size(6L) float[] p1, @Nonnull @Size(6L) float[] p2) {
            float[] p0 = new float[] { p1[0] - p2[0], p1[1] - p2[1], p1[2] - p2[2], p1[3] - p2[3], p1[4] - p2[4], p1[5] - p2[5] };
            if (!(cross(p0[0], p0[1], p2[0] - p2[4], p2[1] - p2[5]) < 0.0F) && !(cross(p2[0] - p2[2], p2[1] - p2[3], p0[0], p0[1]) < 0.0F)) {
                return !(cross(p0[2], p0[3], p2[2] - p2[0], p2[3] - p2[1]) < 0.0F) && !(cross(p2[2] - p2[4], p2[3] - p2[5], p0[2], p0[3]) < 0.0F) ? !(cross(p0[4], p0[5], p2[4] - p2[2], p2[5] - p2[3]) < 0.0F) && !(cross(p2[4] - p2[0], p2[5] - p2[1], p0[4], p0[5]) < 0.0F) : false;
            } else {
                return false;
            }
        }

        @Nonnull
        @Size(6L)
        private static float[] computePrimaries(@Nonnull @Size(9L) float[] toXYZ) {
            float[] r = ColorSpace.mul3x3Float3(toXYZ, new float[] { 1.0F, 0.0F, 0.0F });
            float[] g = ColorSpace.mul3x3Float3(toXYZ, new float[] { 0.0F, 1.0F, 0.0F });
            float[] b = ColorSpace.mul3x3Float3(toXYZ, new float[] { 0.0F, 0.0F, 1.0F });
            float rSum = r[0] + r[1] + r[2];
            float gSum = g[0] + g[1] + g[2];
            float bSum = b[0] + b[1] + b[2];
            return new float[] { r[0] / rSum, r[1] / rSum, g[0] / gSum, g[1] / gSum, b[0] / bSum, b[1] / bSum };
        }

        @Nonnull
        @Size(2L)
        private static float[] computeWhitePoint(@Nonnull @Size(9L) float[] toXYZ) {
            float[] w = ColorSpace.mul3x3Float3(toXYZ, new float[] { 1.0F, 1.0F, 1.0F });
            float sum = w[0] + w[1] + w[2];
            return new float[] { w[0] / sum, w[1] / sum };
        }

        @Nonnull
        @Size(6L)
        private static float[] xyPrimaries(@Nonnull @Size(min = 6L, max = 9L) float[] primaries) {
            float[] xyPrimaries = new float[6];
            if (primaries.length == 9) {
                float sum = primaries[0] + primaries[1] + primaries[2];
                xyPrimaries[0] = primaries[0] / sum;
                xyPrimaries[1] = primaries[1] / sum;
                sum = primaries[3] + primaries[4] + primaries[5];
                xyPrimaries[2] = primaries[3] / sum;
                xyPrimaries[3] = primaries[4] / sum;
                sum = primaries[6] + primaries[7] + primaries[8];
                xyPrimaries[4] = primaries[6] / sum;
                xyPrimaries[5] = primaries[7] / sum;
            } else {
                System.arraycopy(primaries, 0, xyPrimaries, 0, 6);
            }
            return xyPrimaries;
        }

        @Nonnull
        @Size(2L)
        private static float[] xyWhitePoint(@Nonnull @Size(min = 2L, max = 3L) float[] whitePoint) {
            float[] xyWhitePoint = new float[2];
            if (whitePoint.length == 3) {
                float sum = whitePoint[0] + whitePoint[1] + whitePoint[2];
                xyWhitePoint[0] = whitePoint[0] / sum;
                xyWhitePoint[1] = whitePoint[1] / sum;
            } else {
                System.arraycopy(whitePoint, 0, xyWhitePoint, 0, 2);
            }
            return xyWhitePoint;
        }

        @Nonnull
        @Size(9L)
        private static float[] computeXYZMatrix(@Nonnull @Size(6L) float[] primaries, @Nonnull @Size(2L) float[] whitePoint) {
            float Rx = primaries[0];
            float Ry = primaries[1];
            float Gx = primaries[2];
            float Gy = primaries[3];
            float Bx = primaries[4];
            float By = primaries[5];
            float Wx = whitePoint[0];
            float Wy = whitePoint[1];
            float oneRxRy = (1.0F - Rx) / Ry;
            float oneGxGy = (1.0F - Gx) / Gy;
            float oneBxBy = (1.0F - Bx) / By;
            float oneWxWy = (1.0F - Wx) / Wy;
            float RxRy = Rx / Ry;
            float GxGy = Gx / Gy;
            float BxBy = Bx / By;
            float WxWy = Wx / Wy;
            float BY = ((oneWxWy - oneRxRy) * (GxGy - RxRy) - (WxWy - RxRy) * (oneGxGy - oneRxRy)) / ((oneBxBy - oneRxRy) * (GxGy - RxRy) - (BxBy - RxRy) * (oneGxGy - oneRxRy));
            float GY = (WxWy - RxRy - BY * (BxBy - RxRy)) / (GxGy - RxRy);
            float RY = 1.0F - GY - BY;
            float RYRy = RY / Ry;
            float GYGy = GY / Gy;
            float BYBy = BY / By;
            return new float[] { RYRy * Rx, RY, RYRy * (1.0F - Rx - Ry), GYGy * Gx, GY, GYGy * (1.0F - Gx - Gy), BYBy * Bx, BY, BYBy * (1.0F - Bx - By) };
        }

        public static class TransferParameters {

            public final double a;

            public final double b;

            public final double c;

            public final double d;

            public final double e;

            public final double f;

            public final double g;

            public TransferParameters(double a, double b, double c, double d, double g) {
                this(a, b, c, d, 0.0, 0.0, g);
            }

            public TransferParameters(double a, double b, double c, double d, double e, double f, double g) {
                if (Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c) || Double.isNaN(d) || Double.isNaN(e) || Double.isNaN(f) || Double.isNaN(g)) {
                    throw new IllegalArgumentException("Parameters cannot be NaN");
                } else if (d >= 0.0 && d <= (double) (1.0F + Math.ulp(1.0F))) {
                    if (d != 0.0 || a != 0.0 && g != 0.0) {
                        if (d >= 1.0 && c == 0.0) {
                            throw new IllegalArgumentException("Parameter c is zero, the transfer function is constant");
                        } else if ((a == 0.0 || g == 0.0) && c == 0.0) {
                            throw new IllegalArgumentException("Parameter a or g is zero, and c is zero, the transfer function is constant");
                        } else if (c < 0.0) {
                            throw new IllegalArgumentException("The transfer function must be increasing");
                        } else if (!(a < 0.0) && !(g < 0.0)) {
                            this.a = a;
                            this.b = b;
                            this.c = c;
                            this.d = d;
                            this.e = e;
                            this.f = f;
                            this.g = g;
                        } else {
                            throw new IllegalArgumentException("The transfer function must be positive or increasing");
                        }
                    } else {
                        throw new IllegalArgumentException("Parameter a or g is zero, the transfer function is constant");
                    }
                } else {
                    throw new IllegalArgumentException("Parameter d must be in the range [0..1], was " + d);
                }
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else if (o != null && this.getClass() == o.getClass()) {
                    ColorSpace.Rgb.TransferParameters that = (ColorSpace.Rgb.TransferParameters) o;
                    if (Double.compare(that.a, this.a) != 0) {
                        return false;
                    } else if (Double.compare(that.b, this.b) != 0) {
                        return false;
                    } else if (Double.compare(that.c, this.c) != 0) {
                        return false;
                    } else if (Double.compare(that.d, this.d) != 0) {
                        return false;
                    } else if (Double.compare(that.e, this.e) != 0) {
                        return false;
                    } else {
                        return Double.compare(that.f, this.f) != 0 ? false : Double.compare(that.g, this.g) == 0;
                    }
                } else {
                    return false;
                }
            }

            public int hashCode() {
                long temp = Double.doubleToLongBits(this.a);
                int result = (int) (temp ^ temp >>> 32);
                temp = Double.doubleToLongBits(this.b);
                result = 31 * result + (int) (temp ^ temp >>> 32);
                temp = Double.doubleToLongBits(this.c);
                result = 31 * result + (int) (temp ^ temp >>> 32);
                temp = Double.doubleToLongBits(this.d);
                result = 31 * result + (int) (temp ^ temp >>> 32);
                temp = Double.doubleToLongBits(this.e);
                result = 31 * result + (int) (temp ^ temp >>> 32);
                temp = Double.doubleToLongBits(this.f);
                result = 31 * result + (int) (temp ^ temp >>> 32);
                temp = Double.doubleToLongBits(this.g);
                return 31 * result + (int) (temp ^ temp >>> 32);
            }
        }
    }

    private static final class Xyz extends ColorSpace {

        private Xyz(@Nonnull String name, @Range(from = -1L, to = 63L) int id) {
            super(name, ColorSpace.Model.XYZ, id);
        }

        @Override
        public boolean isWideGamut() {
            return true;
        }

        @Override
        public float getMinValue(@Range(from = 0L, to = 3L) int component) {
            return -2.0F;
        }

        @Override
        public float getMaxValue(@Range(from = 0L, to = 3L) int component) {
            return 2.0F;
        }

        @Nonnull
        @Override
        public float[] toXyz(@Nonnull @Size(min = 3L) float[] v) {
            v[0] = MathUtil.clamp(v[0], -2.0F, 2.0F);
            v[1] = MathUtil.clamp(v[1], -2.0F, 2.0F);
            v[2] = MathUtil.clamp(v[2], -2.0F, 2.0F);
            return v;
        }

        @Nonnull
        @Override
        public float[] fromXyz(@Nonnull @Size(min = 3L) float[] v) {
            v[0] = MathUtil.clamp(v[0], -2.0F, 2.0F);
            v[1] = MathUtil.clamp(v[1], -2.0F, 2.0F);
            v[2] = MathUtil.clamp(v[2], -2.0F, 2.0F);
            return v;
        }
    }
}