package com.yungnickyoung.minecraft.yungsapi.noise;

public class OpenSimplex2S implements INoiseLibrary {

    private static final int PSIZE = 2048;

    private static final int PMASK = 2047;

    private short[] perm;

    private OpenSimplex2S.Grad2[] permGrad2;

    private OpenSimplex2S.Grad3[] permGrad3;

    private long seed;

    private int octaves;

    private double gain;

    private double frequency;

    private double lacunarity;

    private static final OpenSimplex2S.LatticePoint2D[] LOOKUP_2D = new OpenSimplex2S.LatticePoint2D[32];

    private static final OpenSimplex2S.LatticePoint3D[] LOOKUP_3D = new OpenSimplex2S.LatticePoint3D[8];

    public static final double N2 = 0.05481866495625118;

    public static final double N3 = 0.2781926117527186;

    private static final OpenSimplex2S.Grad2[] GRADIENTS_2D;

    private static final OpenSimplex2S.Grad3[] GRADIENTS_3D;

    public OpenSimplex2S(long seed) {
        this.seed = seed;
        this.perm = new short[2048];
        this.permGrad2 = new OpenSimplex2S.Grad2[2048];
        this.permGrad3 = new OpenSimplex2S.Grad3[2048];
        short[] source = new short[2048];
        short i = 0;
        while (i < 2048) {
            source[i] = i++;
        }
        for (int ix = 2047; ix >= 0; ix--) {
            seed = seed * 6364136223846793005L + 1442695040888963407L;
            int r = (int) ((seed + 31L) % (long) (ix + 1));
            if (r < 0) {
                r += ix + 1;
            }
            this.perm[ix] = source[r];
            this.permGrad2[ix] = GRADIENTS_2D[this.perm[ix]];
            this.permGrad3[ix] = GRADIENTS_3D[this.perm[ix]];
            source[r] = source[ix];
        }
    }

    public double noise2(double x, double y) {
        double s = 0.366025403784439 * (x + y);
        double xs = x + s;
        double ys = y + s;
        return this.noise2_Base(xs, ys);
    }

    public double noise2_XBeforeY(double x, double y) {
        double xx = x * 0.7071067811865476;
        double yy = y * 1.224744871380249;
        return this.noise2_Base(yy + xx, yy - xx);
    }

    private double noise2_Base(double xs, double ys) {
        double value = 0.0;
        int xsb = fastFloor(xs);
        int ysb = fastFloor(ys);
        double xsi = xs - (double) xsb;
        double ysi = ys - (double) ysb;
        int a = (int) (xsi + ysi);
        int index = a << 2 | (int) (xsi - ysi / 2.0 + 1.0 - (double) a / 2.0) << 3 | (int) (ysi - xsi / 2.0 + 1.0 - (double) a / 2.0) << 4;
        double ssi = (xsi + ysi) * -0.211324865405187;
        double xi = xsi + ssi;
        double yi = ysi + ssi;
        for (int i = 0; i < 4; i++) {
            OpenSimplex2S.LatticePoint2D c = LOOKUP_2D[index + i];
            double dx = xi + c.dx;
            double dy = yi + c.dy;
            double attn = 0.6666666666666666 - dx * dx - dy * dy;
            if (!(attn <= 0.0)) {
                int pxm = xsb + c.xsv & 2047;
                int pym = ysb + c.ysv & 2047;
                OpenSimplex2S.Grad2 grad = this.permGrad2[this.perm[pxm] ^ pym];
                double extrapolation = grad.dx * dx + grad.dy * dy;
                attn *= attn;
                value += attn * attn * extrapolation;
            }
        }
        return value;
    }

    public double noise3_Classic(double x, double y, double z) {
        double r = 0.6666666666666666 * (x + y + z);
        double xr = r - x;
        double yr = r - y;
        double zr = r - z;
        return this.noise3_BCC(xr, yr, zr);
    }

    public double noise3_XYBeforeZ(double x, double y, double z) {
        double xy = x + y;
        double s2 = xy * -0.211324865405187;
        double zz = z * 0.577350269189626;
        double xr = x + s2 - zz;
        double yr = y + s2 - zz;
        double zr = xy * 0.577350269189626 + zz;
        return this.noise3_BCC(xr, yr, zr);
    }

    public double noise3_XZBeforeY(double x, double y, double z) {
        double xz = x + z;
        double s2 = xz * -0.211324865405187;
        double yy = y * 0.577350269189626;
        double xr = x + s2 - yy;
        double zr = z + s2 - yy;
        double yr = xz * 0.577350269189626 + yy;
        return this.noise3_BCC(xr, yr, zr);
    }

    @Override
    public float GetNoise(float x, float y, float z) {
        x = (float) ((double) x * this.frequency);
        y = (float) ((double) y * this.frequency);
        z = (float) ((double) z * this.frequency);
        float sum = 1.0F - (float) Math.abs(this.noise3_XZBeforeY((double) x, (double) y, (double) z));
        float amp = 1.0F;
        for (int i = 1; i < this.octaves; i++) {
            x = (float) ((double) x * this.lacunarity);
            y = (float) ((double) y * this.lacunarity);
            z = (float) ((double) z * this.lacunarity);
            amp = (float) ((double) amp * this.gain);
            sum -= (1.0F - (float) Math.abs(this.noise3_XZBeforeY((double) x, (double) y, (double) z))) * amp;
        }
        return sum;
    }

    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public void setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    private double noise3_BCC(double xr, double yr, double zr) {
        int xrb = fastFloor(xr);
        int yrb = fastFloor(yr);
        int zrb = fastFloor(zr);
        double xri = xr - (double) xrb;
        double yri = yr - (double) yrb;
        double zri = zr - (double) zrb;
        int xht = (int) (xri + 0.5);
        int yht = (int) (yri + 0.5);
        int zht = (int) (zri + 0.5);
        int index = xht << 0 | yht << 1 | zht << 2;
        double value = 0.0;
        OpenSimplex2S.LatticePoint3D c = LOOKUP_3D[index];
        while (c != null) {
            double dxr = xri + c.dxr;
            double dyr = yri + c.dyr;
            double dzr = zri + c.dzr;
            double attn = 0.75 - dxr * dxr - dyr * dyr - dzr * dzr;
            if (attn < 0.0) {
                c = c.nextOnFailure;
            } else {
                int pxm = xrb + c.xrv & 2047;
                int pym = yrb + c.yrv & 2047;
                int pzm = zrb + c.zrv & 2047;
                OpenSimplex2S.Grad3 grad = this.permGrad3[this.perm[this.perm[pxm] ^ pym] ^ pzm];
                double extrapolation = grad.dx * dxr + grad.dy * dyr + grad.dz * dzr;
                attn *= attn;
                value += attn * attn * extrapolation;
                c = c.nextOnSuccess;
            }
        }
        return value;
    }

    private static int fastFloor(double x) {
        int xi = (int) x;
        return x < (double) xi ? xi - 1 : xi;
    }

    static {
        for (int i = 0; i < 8; i++) {
            int i1;
            int j1;
            int i2;
            int j2;
            if ((i & 1) == 0) {
                if ((i & 2) == 0) {
                    i1 = -1;
                    j1 = 0;
                } else {
                    i1 = 1;
                    j1 = 0;
                }
                if ((i & 4) == 0) {
                    i2 = 0;
                    j2 = -1;
                } else {
                    i2 = 0;
                    j2 = 1;
                }
            } else {
                if ((i & 2) != 0) {
                    i1 = 2;
                    j1 = 1;
                } else {
                    i1 = 0;
                    j1 = 1;
                }
                if ((i & 4) != 0) {
                    i2 = 1;
                    j2 = 2;
                } else {
                    i2 = 1;
                    j2 = 0;
                }
            }
            LOOKUP_2D[i * 4] = new OpenSimplex2S.LatticePoint2D(0, 0);
            LOOKUP_2D[i * 4 + 1] = new OpenSimplex2S.LatticePoint2D(1, 1);
            LOOKUP_2D[i * 4 + 2] = new OpenSimplex2S.LatticePoint2D(i1, j1);
            LOOKUP_2D[i * 4 + 3] = new OpenSimplex2S.LatticePoint2D(i2, j2);
        }
        for (int i = 0; i < 8; i++) {
            int i1 = i & 1;
            int j1 = i >> 1 & 1;
            int k1 = i >> 2 & 1;
            int i2 = i1 ^ 1;
            int j2 = j1 ^ 1;
            int k2 = k1 ^ 1;
            OpenSimplex2S.LatticePoint3D c0 = new OpenSimplex2S.LatticePoint3D(i1, j1, k1, 0);
            OpenSimplex2S.LatticePoint3D c1 = new OpenSimplex2S.LatticePoint3D(i1 + i2, j1 + j2, k1 + k2, 1);
            OpenSimplex2S.LatticePoint3D c2 = new OpenSimplex2S.LatticePoint3D(i1 ^ 1, j1, k1, 0);
            OpenSimplex2S.LatticePoint3D c3 = new OpenSimplex2S.LatticePoint3D(i1, j1 ^ 1, k1 ^ 1, 0);
            OpenSimplex2S.LatticePoint3D c4 = new OpenSimplex2S.LatticePoint3D(i1 + (i2 ^ 1), j1 + j2, k1 + k2, 1);
            OpenSimplex2S.LatticePoint3D c5 = new OpenSimplex2S.LatticePoint3D(i1 + i2, j1 + (j2 ^ 1), k1 + (k2 ^ 1), 1);
            OpenSimplex2S.LatticePoint3D c6 = new OpenSimplex2S.LatticePoint3D(i1, j1 ^ 1, k1, 0);
            OpenSimplex2S.LatticePoint3D c7 = new OpenSimplex2S.LatticePoint3D(i1 ^ 1, j1, k1 ^ 1, 0);
            OpenSimplex2S.LatticePoint3D c8 = new OpenSimplex2S.LatticePoint3D(i1 + i2, j1 + (j2 ^ 1), k1 + k2, 1);
            OpenSimplex2S.LatticePoint3D c9 = new OpenSimplex2S.LatticePoint3D(i1 + (i2 ^ 1), j1 + j2, k1 + (k2 ^ 1), 1);
            OpenSimplex2S.LatticePoint3D cA = new OpenSimplex2S.LatticePoint3D(i1, j1, k1 ^ 1, 0);
            OpenSimplex2S.LatticePoint3D cB = new OpenSimplex2S.LatticePoint3D(i1 ^ 1, j1 ^ 1, k1, 0);
            OpenSimplex2S.LatticePoint3D cC = new OpenSimplex2S.LatticePoint3D(i1 + i2, j1 + j2, k1 + (k2 ^ 1), 1);
            OpenSimplex2S.LatticePoint3D cD = new OpenSimplex2S.LatticePoint3D(i1 + (i2 ^ 1), j1 + (j2 ^ 1), k1 + k2, 1);
            c0.nextOnFailure = c0.nextOnSuccess = c1;
            c1.nextOnFailure = c1.nextOnSuccess = c2;
            c2.nextOnFailure = c3;
            c2.nextOnSuccess = c5;
            c3.nextOnFailure = c4;
            c3.nextOnSuccess = c4;
            c4.nextOnFailure = c5;
            c4.nextOnSuccess = c6;
            c5.nextOnFailure = c5.nextOnSuccess = c6;
            c6.nextOnFailure = c7;
            c6.nextOnSuccess = c9;
            c7.nextOnFailure = c8;
            c7.nextOnSuccess = c8;
            c8.nextOnFailure = c9;
            c8.nextOnSuccess = cA;
            c9.nextOnFailure = c9.nextOnSuccess = cA;
            cA.nextOnFailure = cB;
            cA.nextOnSuccess = cD;
            cB.nextOnFailure = cC;
            cB.nextOnSuccess = cC;
            cC.nextOnFailure = cD;
            cC.nextOnSuccess = null;
            cD.nextOnFailure = cD.nextOnSuccess = null;
            LOOKUP_3D[i] = c0;
        }
        GRADIENTS_2D = new OpenSimplex2S.Grad2[2048];
        OpenSimplex2S.Grad2[] grad2 = new OpenSimplex2S.Grad2[] { new OpenSimplex2S.Grad2(0.130526192220052, 0.99144486137381), new OpenSimplex2S.Grad2(0.38268343236509, 0.923879532511287), new OpenSimplex2S.Grad2(0.608761429008721, 0.793353340291235), new OpenSimplex2S.Grad2(0.793353340291235, 0.608761429008721), new OpenSimplex2S.Grad2(0.923879532511287, 0.38268343236509), new OpenSimplex2S.Grad2(0.99144486137381, 0.130526192220051), new OpenSimplex2S.Grad2(0.99144486137381, -0.130526192220051), new OpenSimplex2S.Grad2(0.923879532511287, -0.38268343236509), new OpenSimplex2S.Grad2(0.793353340291235, -0.60876142900872), new OpenSimplex2S.Grad2(0.608761429008721, -0.793353340291235), new OpenSimplex2S.Grad2(0.38268343236509, -0.923879532511287), new OpenSimplex2S.Grad2(0.130526192220052, -0.99144486137381), new OpenSimplex2S.Grad2(-0.130526192220052, -0.99144486137381), new OpenSimplex2S.Grad2(-0.38268343236509, -0.923879532511287), new OpenSimplex2S.Grad2(-0.608761429008721, -0.793353340291235), new OpenSimplex2S.Grad2(-0.793353340291235, -0.608761429008721), new OpenSimplex2S.Grad2(-0.923879532511287, -0.38268343236509), new OpenSimplex2S.Grad2(-0.99144486137381, -0.130526192220052), new OpenSimplex2S.Grad2(-0.99144486137381, 0.130526192220051), new OpenSimplex2S.Grad2(-0.923879532511287, 0.38268343236509), new OpenSimplex2S.Grad2(-0.793353340291235, 0.608761429008721), new OpenSimplex2S.Grad2(-0.608761429008721, 0.793353340291235), new OpenSimplex2S.Grad2(-0.38268343236509, 0.923879532511287), new OpenSimplex2S.Grad2(-0.130526192220052, 0.99144486137381) };
        OpenSimplex2S.Grad2[] grad2XBeforeY = new OpenSimplex2S.Grad2[grad2.length];
        for (OpenSimplex2S.Grad2 value : grad2) {
            value.dx /= 0.05481866495625118;
            value.dy /= 0.05481866495625118;
        }
        for (int i = 0; i < 2048; i++) {
            GRADIENTS_2D[i] = grad2[i % grad2.length];
        }
        GRADIENTS_3D = new OpenSimplex2S.Grad3[2048];
        OpenSimplex2S.Grad3[] grad3 = new OpenSimplex2S.Grad3[] { new OpenSimplex2S.Grad3(-2.22474487139, -2.22474487139, -1.0), new OpenSimplex2S.Grad3(-2.22474487139, -2.22474487139, 1.0), new OpenSimplex2S.Grad3(-3.0862664687972017, -1.1721513422464978, 0.0), new OpenSimplex2S.Grad3(-1.1721513422464978, -3.0862664687972017, 0.0), new OpenSimplex2S.Grad3(-2.22474487139, -1.0, -2.22474487139), new OpenSimplex2S.Grad3(-2.22474487139, 1.0, -2.22474487139), new OpenSimplex2S.Grad3(-1.1721513422464978, 0.0, -3.0862664687972017), new OpenSimplex2S.Grad3(-3.0862664687972017, 0.0, -1.1721513422464978), new OpenSimplex2S.Grad3(-2.22474487139, -1.0, 2.22474487139), new OpenSimplex2S.Grad3(-2.22474487139, 1.0, 2.22474487139), new OpenSimplex2S.Grad3(-3.0862664687972017, 0.0, 1.1721513422464978), new OpenSimplex2S.Grad3(-1.1721513422464978, 0.0, 3.0862664687972017), new OpenSimplex2S.Grad3(-2.22474487139, 2.22474487139, -1.0), new OpenSimplex2S.Grad3(-2.22474487139, 2.22474487139, 1.0), new OpenSimplex2S.Grad3(-1.1721513422464978, 3.0862664687972017, 0.0), new OpenSimplex2S.Grad3(-3.0862664687972017, 1.1721513422464978, 0.0), new OpenSimplex2S.Grad3(-1.0, -2.22474487139, -2.22474487139), new OpenSimplex2S.Grad3(1.0, -2.22474487139, -2.22474487139), new OpenSimplex2S.Grad3(0.0, -3.0862664687972017, -1.1721513422464978), new OpenSimplex2S.Grad3(0.0, -1.1721513422464978, -3.0862664687972017), new OpenSimplex2S.Grad3(-1.0, -2.22474487139, 2.22474487139), new OpenSimplex2S.Grad3(1.0, -2.22474487139, 2.22474487139), new OpenSimplex2S.Grad3(0.0, -1.1721513422464978, 3.0862664687972017), new OpenSimplex2S.Grad3(0.0, -3.0862664687972017, 1.1721513422464978), new OpenSimplex2S.Grad3(-1.0, 2.22474487139, -2.22474487139), new OpenSimplex2S.Grad3(1.0, 2.22474487139, -2.22474487139), new OpenSimplex2S.Grad3(0.0, 1.1721513422464978, -3.0862664687972017), new OpenSimplex2S.Grad3(0.0, 3.0862664687972017, -1.1721513422464978), new OpenSimplex2S.Grad3(-1.0, 2.22474487139, 2.22474487139), new OpenSimplex2S.Grad3(1.0, 2.22474487139, 2.22474487139), new OpenSimplex2S.Grad3(0.0, 3.0862664687972017, 1.1721513422464978), new OpenSimplex2S.Grad3(0.0, 1.1721513422464978, 3.0862664687972017), new OpenSimplex2S.Grad3(2.22474487139, -2.22474487139, -1.0), new OpenSimplex2S.Grad3(2.22474487139, -2.22474487139, 1.0), new OpenSimplex2S.Grad3(1.1721513422464978, -3.0862664687972017, 0.0), new OpenSimplex2S.Grad3(3.0862664687972017, -1.1721513422464978, 0.0), new OpenSimplex2S.Grad3(2.22474487139, -1.0, -2.22474487139), new OpenSimplex2S.Grad3(2.22474487139, 1.0, -2.22474487139), new OpenSimplex2S.Grad3(3.0862664687972017, 0.0, -1.1721513422464978), new OpenSimplex2S.Grad3(1.1721513422464978, 0.0, -3.0862664687972017), new OpenSimplex2S.Grad3(2.22474487139, -1.0, 2.22474487139), new OpenSimplex2S.Grad3(2.22474487139, 1.0, 2.22474487139), new OpenSimplex2S.Grad3(1.1721513422464978, 0.0, 3.0862664687972017), new OpenSimplex2S.Grad3(3.0862664687972017, 0.0, 1.1721513422464978), new OpenSimplex2S.Grad3(2.22474487139, 2.22474487139, -1.0), new OpenSimplex2S.Grad3(2.22474487139, 2.22474487139, 1.0), new OpenSimplex2S.Grad3(3.0862664687972017, 1.1721513422464978, 0.0), new OpenSimplex2S.Grad3(1.1721513422464978, 3.0862664687972017, 0.0) };
        for (OpenSimplex2S.Grad3 value : grad3) {
            value.dx /= 0.2781926117527186;
            value.dy /= 0.2781926117527186;
            value.dz /= 0.2781926117527186;
        }
        for (int i = 0; i < 2048; i++) {
            GRADIENTS_3D[i] = grad3[i % grad3.length];
        }
    }

    public static class Grad2 {

        double dx;

        double dy;

        public Grad2(double dx, double dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    public static class Grad3 {

        double dx;

        double dy;

        double dz;

        public Grad3(double dx, double dy, double dz) {
            this.dx = dx;
            this.dy = dy;
            this.dz = dz;
        }
    }

    private static class LatticePoint2D {

        int xsv;

        int ysv;

        double dx;

        double dy;

        public LatticePoint2D(int xsv, int ysv) {
            this.xsv = xsv;
            this.ysv = ysv;
            double ssv = (double) (xsv + ysv) * -0.211324865405187;
            this.dx = (double) (-xsv) - ssv;
            this.dy = (double) (-ysv) - ssv;
        }
    }

    private static class LatticePoint3D {

        public double dxr;

        public double dyr;

        public double dzr;

        public int xrv;

        public int yrv;

        public int zrv;

        OpenSimplex2S.LatticePoint3D nextOnFailure;

        OpenSimplex2S.LatticePoint3D nextOnSuccess;

        public LatticePoint3D(int xrv, int yrv, int zrv, int lattice) {
            this.dxr = (double) (-xrv) + (double) lattice * 0.5;
            this.dyr = (double) (-yrv) + (double) lattice * 0.5;
            this.dzr = (double) (-zrv) + (double) lattice * 0.5;
            this.xrv = xrv + lattice * 1024;
            this.yrv = yrv + lattice * 1024;
            this.zrv = zrv + lattice * 1024;
        }
    }
}