package icyllis.arc3d.core;

import javax.annotation.Nonnull;

public enum BlendMode implements Blender {

    CLEAR {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            for (int i = 0; i < 4; i++) {
                out[i] = 0.0F;
            }
        }
    }
    ,
    SRC {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            System.arraycopy(src, 0, out, 0, 4);
        }
    }
    ,
    DST {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            System.arraycopy(dst, 0, out, 0, 4);
        }
    }
    ,
    SRC_OVER {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float df = 1.0F - src[3];
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] + dst[i] * df;
            }
        }
    }
    ,
    DST_OVER {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sf = 1.0F - dst[3];
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] * sf + dst[i];
            }
        }
    }
    ,
    SRC_IN {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sf = dst[3];
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] * sf;
            }
        }
    }
    ,
    DST_IN {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float df = src[3];
            for (int i = 0; i < 4; i++) {
                out[i] = dst[i] * df;
            }
        }
    }
    ,
    SRC_OUT {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sf = 1.0F - dst[3];
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] * sf;
            }
        }
    }
    ,
    DST_OUT {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float df = 1.0F - src[3];
            for (int i = 0; i < 4; i++) {
                out[i] = dst[i] * df;
            }
        }
    }
    ,
    SRC_ATOP {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sf = dst[3];
            float df = 1.0F - src[3];
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] * sf + dst[i] * df;
            }
        }
    }
    ,
    DST_ATOP {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sf = 1.0F - dst[3];
            float df = src[3];
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] * sf + dst[i] * df;
            }
        }
    }
    ,
    XOR {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sf = 1.0F - dst[3];
            float df = 1.0F - src[3];
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] * sf + dst[i] * df;
            }
        }
    }
    ,
    PLUS {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] + dst[i];
            }
        }
    }
    ,
    PLUS_CLAMPED {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            for (int i = 0; i < 4; i++) {
                out[i] = Math.min(src[i] + dst[i], 1.0F);
            }
        }
    }
    ,
    MINUS {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            for (int i = 0; i < 4; i++) {
                out[i] = dst[i] - src[i];
            }
        }
    }
    ,
    MINUS_CLAMPED {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            for (int i = 0; i < 4; i++) {
                out[i] = Math.max(dst[i] - src[i], 0.0F);
            }
        }
    }
    ,
    MODULATE {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] * dst[i];
            }
        }
    }
    ,
    MULTIPLY {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sf = 1.0F - dst[3];
            float df = 1.0F - src[3];
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] * dst[i] + src[i] * sf + dst[i] * df;
            }
        }
    }
    ,
    SCREEN {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] + dst[i] - src[i] * dst[i];
            }
        }
    }
    ,
    OVERLAY {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                out[i] = src[i] * (1.0F - da) + dst[i] * (1.0F - sa) + (2.0F * dst[i] <= da ? 2.0F * src[i] * dst[i] : sa * da - 2.0F * (sa - src[i]) * (da - dst[i]));
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    DARKEN {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] + dst[i] - Math.max(src[i] * da, dst[i] * sa);
            }
        }
    }
    ,
    LIGHTEN {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 4; i++) {
                out[i] = src[i] + dst[i] - Math.min(src[i] * da, dst[i] * sa);
            }
        }
    }
    ,
    COLOR_DODGE {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                float s = src[i];
                float d = dst[i];
                if (d <= 0.0F) {
                    out[i] = s * (1.0F - da);
                } else if (s >= sa) {
                    out[i] = sa * da + s * (1.0F - da) + d * (1.0F - sa);
                } else {
                    out[i] = sa * Math.min(da, d * sa / (sa - s)) + s * (1.0F - da) + d * (1.0F - sa);
                }
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    COLOR_BURN {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                float s = src[i];
                float d = dst[i];
                if (d >= da) {
                    out[i] = sa * da + s * (1.0F - da) + d * (1.0F - sa);
                } else if (s <= 0.0F) {
                    out[i] = d * (1.0F - sa);
                } else {
                    out[i] = sa * Math.max(0.0F, da - (da - d) * sa / s) + s * (1.0F - da) + d * (1.0F - sa);
                }
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    HARD_LIGHT {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                out[i] = src[i] * (1.0F - da) + dst[i] * (1.0F - sa) + (2.0F * src[i] <= sa ? 2.0F * src[i] * dst[i] : sa * da - 2.0F * (sa - src[i]) * (da - dst[i]));
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    SOFT_LIGHT {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                float s = src[i];
                float d = dst[i];
                if (2.0F * s <= sa) {
                    out[i] = d * d * (sa - 2.0F * s) / da + s * (1.0F - da) + d * (2.0F * s + 1.0F - sa);
                } else if (4.0F * d <= da) {
                    float dd = d * d;
                    float dada = da * da;
                    out[i] = (dada * (s + d * (6.0F * s - 3.0F * sa + 1.0F)) + 12.0F * da * dd * (sa - 2.0F * s) - 16.0F * dd * d * (sa - 2.0F * s) - dada * da * s) / dada;
                } else {
                    out[i] = d * (sa - 2.0F * s + 1.0F) + s * (1.0F - da) - (float) Math.sqrt((double) (d * da)) * (sa - 2.0F * s);
                }
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    DIFFERENCE {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                out[i] = src[i] + dst[i] - 2.0F * Math.min(src[i] * da, dst[i] * sa);
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    EXCLUSION {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            for (int i = 0; i < 3; i++) {
                out[i] = src[i] + dst[i] - 2.0F * src[i] * dst[i];
            }
            out[3] = src[3] + dst[3] * (1.0F - src[3]);
        }
    }
    ,
    SUBTRACT {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                out[i] = src[i] * (1.0F - da) + dst[i] - Math.min(src[i] * da, dst[i] * sa);
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    DIVIDE {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                out[i] = MathUtil.pin(dst[i] * sa / (src[i] * da), 0.0F, 1.0F) * sa * da + src[i] * (1.0F - da) + dst[i] * (1.0F - sa);
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    LINEAR_DODGE {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                out[i] = Math.min(src[i] + dst[i], sa * da + src[i] * (1.0F - da) + dst[i] * (1.0F - sa));
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    LINEAR_BURN {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                out[i] = Math.max(src[i] + dst[i] - sa * da, src[i] * (1.0F - da) + dst[i] * (1.0F - sa));
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    VIVID_LIGHT {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                float s = src[i];
                float d = dst[i];
                if (2.0F * s < sa) {
                    if (s <= 0.0F) {
                        out[i] = d * (1.0F - sa);
                    } else {
                        out[i] = sa * Math.max(0.0F, da - (da - d) * sa / (2.0F * s)) + s * (1.0F - da) + d * (1.0F - sa);
                    }
                } else if (s >= sa) {
                    out[i] = sa * da + s * (1.0F - da) + d * (1.0F - sa);
                } else {
                    out[i] = sa * Math.min(da, d * sa / (2.0F * (sa - s))) + s * (1.0F - da) + d * (1.0F - sa);
                }
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    LINEAR_LIGHT {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                float s = src[i];
                float d = dst[i];
                out[i] = MathUtil.clamp(2.0F * s * da + d * sa - sa * da, 0.0F, sa * da) + s * (1.0F - da) + d * (1.0F - sa);
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    PIN_LIGHT {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                float x = 2.0F * src[i] * da;
                float y = x - sa * da;
                float z = dst[i] * sa;
                out[i] = (y > z ? (2.0F * src[i] < sa ? 0.0F : y) : Math.min(x, z)) + src[i] * (1.0F - da) + dst[i] * (1.0F - sa);
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    HARD_MIX {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            for (int i = 0; i < 3; i++) {
                float b = src[i] * da + dst[i] * sa;
                float c = sa * da;
                out[i] = src[i] + dst[i] - b + (b < c ? 0.0F : c);
            }
            out[3] = sa + da * (1.0F - sa);
        }
    }
    ,
    DARKER_COLOR {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            if (BlendMode.lum(src) <= BlendMode.lum(dst)) {
                float df = 1.0F - src[3];
                for (int i = 0; i < 4; i++) {
                    out[i] = src[i] + dst[i] * df;
                }
            } else {
                float sf = 1.0F - dst[3];
                for (int i = 0; i < 4; i++) {
                    out[i] = src[i] * sf + dst[i];
                }
            }
        }
    }
    ,
    LIGHTER_COLOR {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            if (BlendMode.lum(src) >= BlendMode.lum(dst)) {
                float df = 1.0F - src[3];
                for (int i = 0; i < 4; i++) {
                    out[i] = src[i] + dst[i] * df;
                }
            } else {
                float sf = 1.0F - dst[3];
                for (int i = 0; i < 4; i++) {
                    out[i] = src[i] * sf + dst[i];
                }
            }
        }
    }
    ,
    HUE {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            float alpha = sa * da;
            float[] c = new float[] { src[0] * da, src[1] * da, src[2] * da };
            BlendMode.set_lum_sat(c, dst, sa, dst, sa, alpha);
            for (int i = 0; i < 3; i++) {
                out[i] = c[i] + src[i] * (1.0F - da) + dst[i] * (1.0F - sa);
            }
            out[3] = sa + da - alpha;
        }
    }
    ,
    SATURATION {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            float alpha = sa * da;
            float[] c = new float[] { dst[0] * sa, dst[1] * sa, dst[2] * sa };
            BlendMode.set_lum_sat(c, src, da, dst, sa, alpha);
            for (int i = 0; i < 3; i++) {
                out[i] = c[i] + src[i] * (1.0F - da) + dst[i] * (1.0F - sa);
            }
            out[3] = sa + da - alpha;
        }
    }
    ,
    COLOR {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            float alpha = sa * da;
            float[] c = new float[] { src[0] * da, src[1] * da, src[2] * da };
            BlendMode.set_lum(c, dst, sa, alpha);
            for (int i = 0; i < 3; i++) {
                out[i] = c[i] + src[i] * (1.0F - da) + dst[i] * (1.0F - sa);
            }
            out[3] = sa + da - alpha;
        }
    }
    ,
    LUMINOSITY {

        @Override
        public void apply(float[] src, float[] dst, float[] out) {
            float sa = src[3];
            float da = dst[3];
            float alpha = sa * da;
            float[] c = new float[] { dst[0] * sa, dst[1] * sa, dst[2] * sa };
            BlendMode.set_lum(c, src, da, alpha);
            for (int i = 0; i < 3; i++) {
                out[i] = c[i] + src[i] * (1.0F - da) + dst[i] * (1.0F - sa);
            }
            out[3] = sa + da - alpha;
        }
    }
    ;

    public static final BlendMode ADD = LINEAR_DODGE;

    private static final BlendMode[] VALUES = values();

    public static final int COUNT = VALUES.length;

    @Nonnull
    public static BlendMode mode(int index) {
        return VALUES[index];
    }

    @Override
    public BlendMode asBlendMode() {
        return this;
    }

    public boolean isAdvanced() {
        return this.ordinal() >= MULTIPLY.ordinal();
    }

    public abstract void apply(@Size(4L) float[] var1, @Size(4L) float[] var2, @Size(4L) float[] var3);

    private static float lum(float[] c) {
        return 0.299F * c[0] + 0.587F * c[1] + 0.114F * c[2];
    }

    private static void set_lum(float[] cbase, float[] clum, float alum, float alpha) {
        float ldiff = lum(clum) * alum - lum(cbase);
        for (int i = 0; i < 3; i++) {
            cbase[i] += ldiff;
        }
        float lum = lum(cbase);
        float mincol = MathUtil.min3(cbase);
        float maxcol = MathUtil.max3(cbase);
        if (mincol < 0.0F && lum != mincol) {
            for (int i = 0; i < 3; i++) {
                cbase[i] = lum + (cbase[i] - lum) * lum / (lum - mincol);
            }
        }
        if (maxcol > alpha && maxcol != lum) {
            for (int i = 0; i < 3; i++) {
                cbase[i] = lum + (cbase[i] - lum) * (alpha - lum) / (maxcol - lum);
            }
        }
    }

    private static void set_lum_sat(float[] cbase, float[] csat, float asat, float[] clum, float alum, float alpha) {
        float minbase = MathUtil.min3(cbase);
        float sbase = MathUtil.max3(cbase) - minbase;
        if (sbase > 0.0F) {
            float ssat = (MathUtil.max3(csat) - MathUtil.min3(csat)) * asat;
            for (int i = 0; i < 3; i++) {
                cbase[i] = (cbase[i] - minbase) * ssat / sbase;
            }
        } else {
            for (int i = 0; i < 3; i++) {
                cbase[i] = 0.0F;
            }
        }
        set_lum(cbase, clum, alum, alpha);
    }
}