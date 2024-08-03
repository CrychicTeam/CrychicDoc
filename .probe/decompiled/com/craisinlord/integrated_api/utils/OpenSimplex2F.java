package com.craisinlord.integrated_api.utils;

public class OpenSimplex2F {

    private static final int PSIZE = 2048;

    private static final int PMASK = 2047;

    private short[] perm = new short[2048];

    private OpenSimplex2F.Grad2[] permGrad2 = new OpenSimplex2F.Grad2[2048];

    private OpenSimplex2F.Grad3[] permGrad3 = new OpenSimplex2F.Grad3[2048];

    private OpenSimplex2F.Grad4[] permGrad4 = new OpenSimplex2F.Grad4[2048];

    private static final OpenSimplex2F.LatticePoint2D[] LOOKUP_2D = new OpenSimplex2F.LatticePoint2D[4];

    private static final OpenSimplex2F.LatticePoint3D[] LOOKUP_3D = new OpenSimplex2F.LatticePoint3D[8];

    private static final OpenSimplex2F.LatticePoint4D[] VERTICES_4D = new OpenSimplex2F.LatticePoint4D[16];

    private static final double N2 = 0.01001634121365712;

    private static final double N3 = 0.030485933181293584;

    private static final double N4 = 0.009202377986303158;

    private static final OpenSimplex2F.Grad2[] GRADIENTS_2D;

    private static final OpenSimplex2F.Grad3[] GRADIENTS_3D;

    private static final OpenSimplex2F.Grad4[] GRADIENTS_4D;

    public OpenSimplex2F(long seed) {
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
            this.permGrad4[ix] = GRADIENTS_4D[this.perm[ix]];
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
        int index = (int) ((ysi - xsi) / 2.0 + 1.0);
        double ssi = (xsi + ysi) * -0.211324865405187;
        double xi = xsi + ssi;
        double yi = ysi + ssi;
        for (int i = 0; i < 3; i++) {
            OpenSimplex2F.LatticePoint2D c = LOOKUP_2D[index + i];
            double dx = xi + c.dx;
            double dy = yi + c.dy;
            double attn = 0.5 - dx * dx - dy * dy;
            if (!(attn <= 0.0)) {
                int pxm = xsb + c.xsv & 2047;
                int pym = ysb + c.ysv & 2047;
                OpenSimplex2F.Grad2 grad = this.permGrad2[this.perm[pxm] ^ pym];
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
        OpenSimplex2F.LatticePoint3D c = LOOKUP_3D[index];
        while (c != null) {
            double dxr = xri + c.dxr;
            double dyr = yri + c.dyr;
            double dzr = zri + c.dzr;
            double attn = 0.5 - dxr * dxr - dyr * dyr - dzr * dzr;
            if (attn < 0.0) {
                c = c.nextOnFailure;
            } else {
                int pxm = xrb + c.xrv & 2047;
                int pym = yrb + c.yrv & 2047;
                int pzm = zrb + c.zrv & 2047;
                OpenSimplex2F.Grad3 grad = this.permGrad3[this.perm[this.perm[pxm] ^ pym] ^ pzm];
                double extrapolation = grad.dx * dxr + grad.dy * dyr + grad.dz * dzr;
                attn *= attn;
                value += attn * attn * extrapolation;
                c = c.nextOnSuccess;
            }
        }
        return value;
    }

    public double noise4_Classic(double x, double y, double z, double w) {
        double s = -0.138196601125011 * (x + y + z + w);
        double xs = x + s;
        double ys = y + s;
        double zs = z + s;
        double ws = w + s;
        return this.noise4_Base(xs, ys, zs, ws);
    }

    public double noise4_XYBeforeZW(double x, double y, double z, double w) {
        double s2 = (x + y) * -0.17827565795139938 + (z + w) * 0.21562339328884284;
        double t2 = (z + w) * -0.4039497625802071 + (x + y) * -0.3751990830100753;
        double xs = x + s2;
        double ys = y + s2;
        double zs = z + t2;
        double ws = w + t2;
        return this.noise4_Base(xs, ys, zs, ws);
    }

    public double noise4_XZBeforeYW(double x, double y, double z, double w) {
        double s2 = (x + z) * -0.17827565795139938 + (y + w) * 0.21562339328884284;
        double t2 = (y + w) * -0.4039497625802071 + (x + z) * -0.3751990830100753;
        double xs = x + s2;
        double ys = y + t2;
        double zs = z + s2;
        double ws = w + t2;
        return this.noise4_Base(xs, ys, zs, ws);
    }

    public double noise4_XYZBeforeW(double x, double y, double z, double w) {
        double xyz = x + y + z;
        double ww = w * 0.2236067977499788;
        double s2 = xyz * -0.16666666666666666 + ww;
        double xs = x + s2;
        double ys = y + s2;
        double zs = z + s2;
        double ws = -0.5 * xyz + ww;
        return this.noise4_Base(xs, ys, zs, ws);
    }

    private double noise4_Base(double xs, double ys, double zs, double ws) {
        double value = 0.0;
        int xsb = fastFloor(xs);
        int ysb = fastFloor(ys);
        int zsb = fastFloor(zs);
        int wsb = fastFloor(ws);
        double xsi = xs - (double) xsb;
        double ysi = ys - (double) ysb;
        double zsi = zs - (double) zsb;
        double wsi = ws - (double) wsb;
        double siSum = xsi + ysi + zsi + wsi;
        double ssi = siSum * 0.309016994374947;
        boolean inLowerHalf = siSum < 2.0;
        if (inLowerHalf) {
            xsi = 1.0 - xsi;
            ysi = 1.0 - ysi;
            zsi = 1.0 - zsi;
            wsi = 1.0 - wsi;
            siSum = 4.0 - siSum;
        }
        double aabb = xsi + ysi - zsi - wsi;
        double abab = xsi - ysi + zsi - wsi;
        double abba = xsi - ysi - zsi + wsi;
        double aabbScore = Math.abs(aabb);
        double ababScore = Math.abs(abab);
        double abbaScore = Math.abs(abba);
        int vertexIndex;
        int via;
        int vib;
        double asi;
        double bsi;
        if (aabbScore > ababScore && aabbScore > abbaScore) {
            if (aabb > 0.0) {
                asi = zsi;
                bsi = wsi;
                vertexIndex = 3;
                via = 7;
                vib = 11;
            } else {
                asi = xsi;
                bsi = ysi;
                vertexIndex = 12;
                via = 13;
                vib = 14;
            }
        } else if (ababScore > abbaScore) {
            if (abab > 0.0) {
                asi = ysi;
                bsi = wsi;
                vertexIndex = 5;
                via = 7;
                vib = 13;
            } else {
                asi = xsi;
                bsi = zsi;
                vertexIndex = 10;
                via = 11;
                vib = 14;
            }
        } else if (abba > 0.0) {
            asi = ysi;
            bsi = zsi;
            vertexIndex = 9;
            via = 11;
            vib = 13;
        } else {
            asi = xsi;
            bsi = wsi;
            vertexIndex = 6;
            via = 7;
            vib = 14;
        }
        if (bsi > asi) {
            via = vib;
            double temp = bsi;
            bsi = asi;
            asi = temp;
        }
        if (siSum + asi > 3.0) {
            vertexIndex = via;
            if (siSum + bsi > 4.0) {
                vertexIndex = 15;
            }
        }
        if (inLowerHalf) {
            xsi = 1.0 - xsi;
            ysi = 1.0 - ysi;
            zsi = 1.0 - zsi;
            wsi = 1.0 - wsi;
            vertexIndex ^= 15;
        }
        for (int i = 0; i < 5; i++) {
            OpenSimplex2F.LatticePoint4D c = VERTICES_4D[vertexIndex];
            xsb += c.xsv;
            ysb += c.ysv;
            zsb += c.zsv;
            wsb += c.wsv;
            double xi = xsi + ssi;
            double yi = ysi + ssi;
            double zi = zsi + ssi;
            double wi = wsi + ssi;
            double dx = xi + c.dx;
            double dy = yi + c.dy;
            double dz = zi + c.dz;
            double dw = wi + c.dw;
            double attn = 0.5 - dx * dx - dy * dy - dz * dz - dw * dw;
            if (attn > 0.0) {
                int pxm = xsb & 2047;
                int pym = ysb & 2047;
                int pzm = zsb & 2047;
                int pwm = wsb & 2047;
                OpenSimplex2F.Grad4 grad = this.permGrad4[this.perm[this.perm[this.perm[pxm] ^ pym] ^ pzm] ^ pwm];
                double ramped = grad.dx * dx + grad.dy * dy + grad.dz * dz + grad.dw * dw;
                attn *= attn;
                value += attn * attn * ramped;
            }
            if (i == 4) {
                break;
            }
            xsi += c.xsi;
            ysi += c.ysi;
            zsi += c.zsi;
            wsi += c.wsi;
            ssi += c.ssiDelta;
            double score0 = 1.0 + ssi * -3.2360679774997942;
            vertexIndex = 0;
            if (xsi >= ysi && xsi >= zsi && xsi >= wsi && xsi >= score0) {
                vertexIndex = 1;
            } else if (ysi > xsi && ysi >= zsi && ysi >= wsi && ysi >= score0) {
                vertexIndex = 2;
            } else if (zsi > xsi && zsi > ysi && zsi >= wsi && zsi >= score0) {
                vertexIndex = 4;
            } else if (wsi > xsi && wsi > ysi && wsi > zsi && wsi >= score0) {
                vertexIndex = 8;
            }
        }
        return value;
    }

    private static int fastFloor(double x) {
        int xi = (int) x;
        return x < (double) xi ? xi - 1 : xi;
    }

    static {
        LOOKUP_2D[0] = new OpenSimplex2F.LatticePoint2D(1, 0);
        LOOKUP_2D[1] = new OpenSimplex2F.LatticePoint2D(0, 0);
        LOOKUP_2D[2] = new OpenSimplex2F.LatticePoint2D(1, 1);
        LOOKUP_2D[3] = new OpenSimplex2F.LatticePoint2D(0, 1);
        for (int i = 0; i < 8; i++) {
            int i1 = i >> 0 & 1;
            int j1 = i >> 1 & 1;
            int k1 = i >> 2 & 1;
            int i2 = i1 ^ 1;
            int j2 = j1 ^ 1;
            int k2 = k1 ^ 1;
            OpenSimplex2F.LatticePoint3D c0 = new OpenSimplex2F.LatticePoint3D(i1, j1, k1, 0);
            OpenSimplex2F.LatticePoint3D c1 = new OpenSimplex2F.LatticePoint3D(i1 + i2, j1 + j2, k1 + k2, 1);
            OpenSimplex2F.LatticePoint3D c2 = new OpenSimplex2F.LatticePoint3D(i1 ^ 1, j1, k1, 0);
            OpenSimplex2F.LatticePoint3D c3 = new OpenSimplex2F.LatticePoint3D(i1, j1 ^ 1, k1, 0);
            OpenSimplex2F.LatticePoint3D c4 = new OpenSimplex2F.LatticePoint3D(i1, j1, k1 ^ 1, 0);
            OpenSimplex2F.LatticePoint3D c5 = new OpenSimplex2F.LatticePoint3D(i1 + (i2 ^ 1), j1 + j2, k1 + k2, 1);
            OpenSimplex2F.LatticePoint3D c6 = new OpenSimplex2F.LatticePoint3D(i1 + i2, j1 + (j2 ^ 1), k1 + k2, 1);
            OpenSimplex2F.LatticePoint3D c7 = new OpenSimplex2F.LatticePoint3D(i1 + i2, j1 + j2, k1 + (k2 ^ 1), 1);
            c0.nextOnFailure = c0.nextOnSuccess = c1;
            c1.nextOnFailure = c1.nextOnSuccess = c2;
            c2.nextOnFailure = c3;
            c2.nextOnSuccess = c6;
            c3.nextOnFailure = c4;
            c3.nextOnSuccess = c5;
            c4.nextOnFailure = c4.nextOnSuccess = c5;
            c5.nextOnFailure = c6;
            c5.nextOnSuccess = null;
            c6.nextOnFailure = c7;
            c6.nextOnSuccess = null;
            c7.nextOnFailure = c7.nextOnSuccess = null;
            LOOKUP_3D[i] = c0;
        }
        for (int i = 0; i < 16; i++) {
            VERTICES_4D[i] = new OpenSimplex2F.LatticePoint4D(i >> 0 & 1, i >> 1 & 1, i >> 2 & 1, i >> 3 & 1);
        }
        GRADIENTS_2D = new OpenSimplex2F.Grad2[2048];
        OpenSimplex2F.Grad2[] grad2 = new OpenSimplex2F.Grad2[] { new OpenSimplex2F.Grad2(0.130526192220052, 0.99144486137381), new OpenSimplex2F.Grad2(0.38268343236509, 0.923879532511287), new OpenSimplex2F.Grad2(0.608761429008721, 0.793353340291235), new OpenSimplex2F.Grad2(0.793353340291235, 0.608761429008721), new OpenSimplex2F.Grad2(0.923879532511287, 0.38268343236509), new OpenSimplex2F.Grad2(0.99144486137381, 0.130526192220051), new OpenSimplex2F.Grad2(0.99144486137381, -0.130526192220051), new OpenSimplex2F.Grad2(0.923879532511287, -0.38268343236509), new OpenSimplex2F.Grad2(0.793353340291235, -0.60876142900872), new OpenSimplex2F.Grad2(0.608761429008721, -0.793353340291235), new OpenSimplex2F.Grad2(0.38268343236509, -0.923879532511287), new OpenSimplex2F.Grad2(0.130526192220052, -0.99144486137381), new OpenSimplex2F.Grad2(-0.130526192220052, -0.99144486137381), new OpenSimplex2F.Grad2(-0.38268343236509, -0.923879532511287), new OpenSimplex2F.Grad2(-0.608761429008721, -0.793353340291235), new OpenSimplex2F.Grad2(-0.793353340291235, -0.608761429008721), new OpenSimplex2F.Grad2(-0.923879532511287, -0.38268343236509), new OpenSimplex2F.Grad2(-0.99144486137381, -0.130526192220052), new OpenSimplex2F.Grad2(-0.99144486137381, 0.130526192220051), new OpenSimplex2F.Grad2(-0.923879532511287, 0.38268343236509), new OpenSimplex2F.Grad2(-0.793353340291235, 0.608761429008721), new OpenSimplex2F.Grad2(-0.608761429008721, 0.793353340291235), new OpenSimplex2F.Grad2(-0.38268343236509, 0.923879532511287), new OpenSimplex2F.Grad2(-0.130526192220052, 0.99144486137381) };
        for (int i = 0; i < grad2.length; i++) {
            grad2[i].dx /= 0.01001634121365712;
            grad2[i].dy /= 0.01001634121365712;
        }
        for (int i = 0; i < 2048; i++) {
            GRADIENTS_2D[i] = grad2[i % grad2.length];
        }
        GRADIENTS_3D = new OpenSimplex2F.Grad3[2048];
        OpenSimplex2F.Grad3[] grad3 = new OpenSimplex2F.Grad3[] { new OpenSimplex2F.Grad3(-2.22474487139, -2.22474487139, -1.0), new OpenSimplex2F.Grad3(-2.22474487139, -2.22474487139, 1.0), new OpenSimplex2F.Grad3(-3.0862664687972017, -1.1721513422464978, 0.0), new OpenSimplex2F.Grad3(-1.1721513422464978, -3.0862664687972017, 0.0), new OpenSimplex2F.Grad3(-2.22474487139, -1.0, -2.22474487139), new OpenSimplex2F.Grad3(-2.22474487139, 1.0, -2.22474487139), new OpenSimplex2F.Grad3(-1.1721513422464978, 0.0, -3.0862664687972017), new OpenSimplex2F.Grad3(-3.0862664687972017, 0.0, -1.1721513422464978), new OpenSimplex2F.Grad3(-2.22474487139, -1.0, 2.22474487139), new OpenSimplex2F.Grad3(-2.22474487139, 1.0, 2.22474487139), new OpenSimplex2F.Grad3(-3.0862664687972017, 0.0, 1.1721513422464978), new OpenSimplex2F.Grad3(-1.1721513422464978, 0.0, 3.0862664687972017), new OpenSimplex2F.Grad3(-2.22474487139, 2.22474487139, -1.0), new OpenSimplex2F.Grad3(-2.22474487139, 2.22474487139, 1.0), new OpenSimplex2F.Grad3(-1.1721513422464978, 3.0862664687972017, 0.0), new OpenSimplex2F.Grad3(-3.0862664687972017, 1.1721513422464978, 0.0), new OpenSimplex2F.Grad3(-1.0, -2.22474487139, -2.22474487139), new OpenSimplex2F.Grad3(1.0, -2.22474487139, -2.22474487139), new OpenSimplex2F.Grad3(0.0, -3.0862664687972017, -1.1721513422464978), new OpenSimplex2F.Grad3(0.0, -1.1721513422464978, -3.0862664687972017), new OpenSimplex2F.Grad3(-1.0, -2.22474487139, 2.22474487139), new OpenSimplex2F.Grad3(1.0, -2.22474487139, 2.22474487139), new OpenSimplex2F.Grad3(0.0, -1.1721513422464978, 3.0862664687972017), new OpenSimplex2F.Grad3(0.0, -3.0862664687972017, 1.1721513422464978), new OpenSimplex2F.Grad3(-1.0, 2.22474487139, -2.22474487139), new OpenSimplex2F.Grad3(1.0, 2.22474487139, -2.22474487139), new OpenSimplex2F.Grad3(0.0, 1.1721513422464978, -3.0862664687972017), new OpenSimplex2F.Grad3(0.0, 3.0862664687972017, -1.1721513422464978), new OpenSimplex2F.Grad3(-1.0, 2.22474487139, 2.22474487139), new OpenSimplex2F.Grad3(1.0, 2.22474487139, 2.22474487139), new OpenSimplex2F.Grad3(0.0, 3.0862664687972017, 1.1721513422464978), new OpenSimplex2F.Grad3(0.0, 1.1721513422464978, 3.0862664687972017), new OpenSimplex2F.Grad3(2.22474487139, -2.22474487139, -1.0), new OpenSimplex2F.Grad3(2.22474487139, -2.22474487139, 1.0), new OpenSimplex2F.Grad3(1.1721513422464978, -3.0862664687972017, 0.0), new OpenSimplex2F.Grad3(3.0862664687972017, -1.1721513422464978, 0.0), new OpenSimplex2F.Grad3(2.22474487139, -1.0, -2.22474487139), new OpenSimplex2F.Grad3(2.22474487139, 1.0, -2.22474487139), new OpenSimplex2F.Grad3(3.0862664687972017, 0.0, -1.1721513422464978), new OpenSimplex2F.Grad3(1.1721513422464978, 0.0, -3.0862664687972017), new OpenSimplex2F.Grad3(2.22474487139, -1.0, 2.22474487139), new OpenSimplex2F.Grad3(2.22474487139, 1.0, 2.22474487139), new OpenSimplex2F.Grad3(1.1721513422464978, 0.0, 3.0862664687972017), new OpenSimplex2F.Grad3(3.0862664687972017, 0.0, 1.1721513422464978), new OpenSimplex2F.Grad3(2.22474487139, 2.22474487139, -1.0), new OpenSimplex2F.Grad3(2.22474487139, 2.22474487139, 1.0), new OpenSimplex2F.Grad3(3.0862664687972017, 1.1721513422464978, 0.0), new OpenSimplex2F.Grad3(1.1721513422464978, 3.0862664687972017, 0.0) };
        for (int i = 0; i < grad3.length; i++) {
            grad3[i].dx /= 0.030485933181293584;
            grad3[i].dy /= 0.030485933181293584;
            grad3[i].dz /= 0.030485933181293584;
        }
        for (int i = 0; i < 2048; i++) {
            GRADIENTS_3D[i] = grad3[i % grad3.length];
        }
        GRADIENTS_4D = new OpenSimplex2F.Grad4[2048];
        OpenSimplex2F.Grad4[] grad4 = new OpenSimplex2F.Grad4[] { new OpenSimplex2F.Grad4(-0.753341017856078, -0.37968289875261624, -0.37968289875261624, -0.37968289875261624), new OpenSimplex2F.Grad4(-0.7821684431180708, -0.4321472685365301, -0.4321472685365301, 0.12128480194602098), new OpenSimplex2F.Grad4(-0.7821684431180708, -0.4321472685365301, 0.12128480194602098, -0.4321472685365301), new OpenSimplex2F.Grad4(-0.7821684431180708, 0.12128480194602098, -0.4321472685365301, -0.4321472685365301), new OpenSimplex2F.Grad4(-0.8586508742123365, -0.508629699630796, 0.044802370851755174, 0.044802370851755174), new OpenSimplex2F.Grad4(-0.8586508742123365, 0.044802370851755174, -0.508629699630796, 0.044802370851755174), new OpenSimplex2F.Grad4(-0.8586508742123365, 0.044802370851755174, 0.044802370851755174, -0.508629699630796), new OpenSimplex2F.Grad4(-0.9982828964265062, -0.03381941603233842, -0.03381941603233842, -0.03381941603233842), new OpenSimplex2F.Grad4(-0.37968289875261624, -0.753341017856078, -0.37968289875261624, -0.37968289875261624), new OpenSimplex2F.Grad4(-0.4321472685365301, -0.7821684431180708, -0.4321472685365301, 0.12128480194602098), new OpenSimplex2F.Grad4(-0.4321472685365301, -0.7821684431180708, 0.12128480194602098, -0.4321472685365301), new OpenSimplex2F.Grad4(0.12128480194602098, -0.7821684431180708, -0.4321472685365301, -0.4321472685365301), new OpenSimplex2F.Grad4(-0.508629699630796, -0.8586508742123365, 0.044802370851755174, 0.044802370851755174), new OpenSimplex2F.Grad4(0.044802370851755174, -0.8586508742123365, -0.508629699630796, 0.044802370851755174), new OpenSimplex2F.Grad4(0.044802370851755174, -0.8586508742123365, 0.044802370851755174, -0.508629699630796), new OpenSimplex2F.Grad4(-0.03381941603233842, -0.9982828964265062, -0.03381941603233842, -0.03381941603233842), new OpenSimplex2F.Grad4(-0.37968289875261624, -0.37968289875261624, -0.753341017856078, -0.37968289875261624), new OpenSimplex2F.Grad4(-0.4321472685365301, -0.4321472685365301, -0.7821684431180708, 0.12128480194602098), new OpenSimplex2F.Grad4(-0.4321472685365301, 0.12128480194602098, -0.7821684431180708, -0.4321472685365301), new OpenSimplex2F.Grad4(0.12128480194602098, -0.4321472685365301, -0.7821684431180708, -0.4321472685365301), new OpenSimplex2F.Grad4(-0.508629699630796, 0.044802370851755174, -0.8586508742123365, 0.044802370851755174), new OpenSimplex2F.Grad4(0.044802370851755174, -0.508629699630796, -0.8586508742123365, 0.044802370851755174), new OpenSimplex2F.Grad4(0.044802370851755174, 0.044802370851755174, -0.8586508742123365, -0.508629699630796), new OpenSimplex2F.Grad4(-0.03381941603233842, -0.03381941603233842, -0.9982828964265062, -0.03381941603233842), new OpenSimplex2F.Grad4(-0.37968289875261624, -0.37968289875261624, -0.37968289875261624, -0.753341017856078), new OpenSimplex2F.Grad4(-0.4321472685365301, -0.4321472685365301, 0.12128480194602098, -0.7821684431180708), new OpenSimplex2F.Grad4(-0.4321472685365301, 0.12128480194602098, -0.4321472685365301, -0.7821684431180708), new OpenSimplex2F.Grad4(0.12128480194602098, -0.4321472685365301, -0.4321472685365301, -0.7821684431180708), new OpenSimplex2F.Grad4(-0.508629699630796, 0.044802370851755174, 0.044802370851755174, -0.8586508742123365), new OpenSimplex2F.Grad4(0.044802370851755174, -0.508629699630796, 0.044802370851755174, -0.8586508742123365), new OpenSimplex2F.Grad4(0.044802370851755174, 0.044802370851755174, -0.508629699630796, -0.8586508742123365), new OpenSimplex2F.Grad4(-0.03381941603233842, -0.03381941603233842, -0.03381941603233842, -0.9982828964265062), new OpenSimplex2F.Grad4(-0.6740059517812944, -0.3239847771997537, -0.3239847771997537, 0.5794684678643381), new OpenSimplex2F.Grad4(-0.7504883828755602, -0.4004672082940195, 0.15296486218853164, 0.5029860367700724), new OpenSimplex2F.Grad4(-0.7504883828755602, 0.15296486218853164, -0.4004672082940195, 0.5029860367700724), new OpenSimplex2F.Grad4(-0.8828161875373585, 0.08164729285680945, 0.08164729285680945, 0.4553054119602712), new OpenSimplex2F.Grad4(-0.4553054119602712, -0.08164729285680945, -0.08164729285680945, 0.8828161875373585), new OpenSimplex2F.Grad4(-0.5029860367700724, -0.15296486218853164, 0.4004672082940195, 0.7504883828755602), new OpenSimplex2F.Grad4(-0.5029860367700724, 0.4004672082940195, -0.15296486218853164, 0.7504883828755602), new OpenSimplex2F.Grad4(-0.5794684678643381, 0.3239847771997537, 0.3239847771997537, 0.6740059517812944), new OpenSimplex2F.Grad4(-0.3239847771997537, -0.6740059517812944, -0.3239847771997537, 0.5794684678643381), new OpenSimplex2F.Grad4(-0.4004672082940195, -0.7504883828755602, 0.15296486218853164, 0.5029860367700724), new OpenSimplex2F.Grad4(0.15296486218853164, -0.7504883828755602, -0.4004672082940195, 0.5029860367700724), new OpenSimplex2F.Grad4(0.08164729285680945, -0.8828161875373585, 0.08164729285680945, 0.4553054119602712), new OpenSimplex2F.Grad4(-0.08164729285680945, -0.4553054119602712, -0.08164729285680945, 0.8828161875373585), new OpenSimplex2F.Grad4(-0.15296486218853164, -0.5029860367700724, 0.4004672082940195, 0.7504883828755602), new OpenSimplex2F.Grad4(0.4004672082940195, -0.5029860367700724, -0.15296486218853164, 0.7504883828755602), new OpenSimplex2F.Grad4(0.3239847771997537, -0.5794684678643381, 0.3239847771997537, 0.6740059517812944), new OpenSimplex2F.Grad4(-0.3239847771997537, -0.3239847771997537, -0.6740059517812944, 0.5794684678643381), new OpenSimplex2F.Grad4(-0.4004672082940195, 0.15296486218853164, -0.7504883828755602, 0.5029860367700724), new OpenSimplex2F.Grad4(0.15296486218853164, -0.4004672082940195, -0.7504883828755602, 0.5029860367700724), new OpenSimplex2F.Grad4(0.08164729285680945, 0.08164729285680945, -0.8828161875373585, 0.4553054119602712), new OpenSimplex2F.Grad4(-0.08164729285680945, -0.08164729285680945, -0.4553054119602712, 0.8828161875373585), new OpenSimplex2F.Grad4(-0.15296486218853164, 0.4004672082940195, -0.5029860367700724, 0.7504883828755602), new OpenSimplex2F.Grad4(0.4004672082940195, -0.15296486218853164, -0.5029860367700724, 0.7504883828755602), new OpenSimplex2F.Grad4(0.3239847771997537, 0.3239847771997537, -0.5794684678643381, 0.6740059517812944), new OpenSimplex2F.Grad4(-0.6740059517812944, -0.3239847771997537, 0.5794684678643381, -0.3239847771997537), new OpenSimplex2F.Grad4(-0.7504883828755602, -0.4004672082940195, 0.5029860367700724, 0.15296486218853164), new OpenSimplex2F.Grad4(-0.7504883828755602, 0.15296486218853164, 0.5029860367700724, -0.4004672082940195), new OpenSimplex2F.Grad4(-0.8828161875373585, 0.08164729285680945, 0.4553054119602712, 0.08164729285680945), new OpenSimplex2F.Grad4(-0.4553054119602712, -0.08164729285680945, 0.8828161875373585, -0.08164729285680945), new OpenSimplex2F.Grad4(-0.5029860367700724, -0.15296486218853164, 0.7504883828755602, 0.4004672082940195), new OpenSimplex2F.Grad4(-0.5029860367700724, 0.4004672082940195, 0.7504883828755602, -0.15296486218853164), new OpenSimplex2F.Grad4(-0.5794684678643381, 0.3239847771997537, 0.6740059517812944, 0.3239847771997537), new OpenSimplex2F.Grad4(-0.3239847771997537, -0.6740059517812944, 0.5794684678643381, -0.3239847771997537), new OpenSimplex2F.Grad4(-0.4004672082940195, -0.7504883828755602, 0.5029860367700724, 0.15296486218853164), new OpenSimplex2F.Grad4(0.15296486218853164, -0.7504883828755602, 0.5029860367700724, -0.4004672082940195), new OpenSimplex2F.Grad4(0.08164729285680945, -0.8828161875373585, 0.4553054119602712, 0.08164729285680945), new OpenSimplex2F.Grad4(-0.08164729285680945, -0.4553054119602712, 0.8828161875373585, -0.08164729285680945), new OpenSimplex2F.Grad4(-0.15296486218853164, -0.5029860367700724, 0.7504883828755602, 0.4004672082940195), new OpenSimplex2F.Grad4(0.4004672082940195, -0.5029860367700724, 0.7504883828755602, -0.15296486218853164), new OpenSimplex2F.Grad4(0.3239847771997537, -0.5794684678643381, 0.6740059517812944, 0.3239847771997537), new OpenSimplex2F.Grad4(-0.3239847771997537, -0.3239847771997537, 0.5794684678643381, -0.6740059517812944), new OpenSimplex2F.Grad4(-0.4004672082940195, 0.15296486218853164, 0.5029860367700724, -0.7504883828755602), new OpenSimplex2F.Grad4(0.15296486218853164, -0.4004672082940195, 0.5029860367700724, -0.7504883828755602), new OpenSimplex2F.Grad4(0.08164729285680945, 0.08164729285680945, 0.4553054119602712, -0.8828161875373585), new OpenSimplex2F.Grad4(-0.08164729285680945, -0.08164729285680945, 0.8828161875373585, -0.4553054119602712), new OpenSimplex2F.Grad4(-0.15296486218853164, 0.4004672082940195, 0.7504883828755602, -0.5029860367700724), new OpenSimplex2F.Grad4(0.4004672082940195, -0.15296486218853164, 0.7504883828755602, -0.5029860367700724), new OpenSimplex2F.Grad4(0.3239847771997537, 0.3239847771997537, 0.6740059517812944, -0.5794684678643381), new OpenSimplex2F.Grad4(-0.6740059517812944, 0.5794684678643381, -0.3239847771997537, -0.3239847771997537), new OpenSimplex2F.Grad4(-0.7504883828755602, 0.5029860367700724, -0.4004672082940195, 0.15296486218853164), new OpenSimplex2F.Grad4(-0.7504883828755602, 0.5029860367700724, 0.15296486218853164, -0.4004672082940195), new OpenSimplex2F.Grad4(-0.8828161875373585, 0.4553054119602712, 0.08164729285680945, 0.08164729285680945), new OpenSimplex2F.Grad4(-0.4553054119602712, 0.8828161875373585, -0.08164729285680945, -0.08164729285680945), new OpenSimplex2F.Grad4(-0.5029860367700724, 0.7504883828755602, -0.15296486218853164, 0.4004672082940195), new OpenSimplex2F.Grad4(-0.5029860367700724, 0.7504883828755602, 0.4004672082940195, -0.15296486218853164), new OpenSimplex2F.Grad4(-0.5794684678643381, 0.6740059517812944, 0.3239847771997537, 0.3239847771997537), new OpenSimplex2F.Grad4(-0.3239847771997537, 0.5794684678643381, -0.6740059517812944, -0.3239847771997537), new OpenSimplex2F.Grad4(-0.4004672082940195, 0.5029860367700724, -0.7504883828755602, 0.15296486218853164), new OpenSimplex2F.Grad4(0.15296486218853164, 0.5029860367700724, -0.7504883828755602, -0.4004672082940195), new OpenSimplex2F.Grad4(0.08164729285680945, 0.4553054119602712, -0.8828161875373585, 0.08164729285680945), new OpenSimplex2F.Grad4(-0.08164729285680945, 0.8828161875373585, -0.4553054119602712, -0.08164729285680945), new OpenSimplex2F.Grad4(-0.15296486218853164, 0.7504883828755602, -0.5029860367700724, 0.4004672082940195), new OpenSimplex2F.Grad4(0.4004672082940195, 0.7504883828755602, -0.5029860367700724, -0.15296486218853164), new OpenSimplex2F.Grad4(0.3239847771997537, 0.6740059517812944, -0.5794684678643381, 0.3239847771997537), new OpenSimplex2F.Grad4(-0.3239847771997537, 0.5794684678643381, -0.3239847771997537, -0.6740059517812944), new OpenSimplex2F.Grad4(-0.4004672082940195, 0.5029860367700724, 0.15296486218853164, -0.7504883828755602), new OpenSimplex2F.Grad4(0.15296486218853164, 0.5029860367700724, -0.4004672082940195, -0.7504883828755602), new OpenSimplex2F.Grad4(0.08164729285680945, 0.4553054119602712, 0.08164729285680945, -0.8828161875373585), new OpenSimplex2F.Grad4(-0.08164729285680945, 0.8828161875373585, -0.08164729285680945, -0.4553054119602712), new OpenSimplex2F.Grad4(-0.15296486218853164, 0.7504883828755602, 0.4004672082940195, -0.5029860367700724), new OpenSimplex2F.Grad4(0.4004672082940195, 0.7504883828755602, -0.15296486218853164, -0.5029860367700724), new OpenSimplex2F.Grad4(0.3239847771997537, 0.6740059517812944, 0.3239847771997537, -0.5794684678643381), new OpenSimplex2F.Grad4(0.5794684678643381, -0.6740059517812944, -0.3239847771997537, -0.3239847771997537), new OpenSimplex2F.Grad4(0.5029860367700724, -0.7504883828755602, -0.4004672082940195, 0.15296486218853164), new OpenSimplex2F.Grad4(0.5029860367700724, -0.7504883828755602, 0.15296486218853164, -0.4004672082940195), new OpenSimplex2F.Grad4(0.4553054119602712, -0.8828161875373585, 0.08164729285680945, 0.08164729285680945), new OpenSimplex2F.Grad4(0.8828161875373585, -0.4553054119602712, -0.08164729285680945, -0.08164729285680945), new OpenSimplex2F.Grad4(0.7504883828755602, -0.5029860367700724, -0.15296486218853164, 0.4004672082940195), new OpenSimplex2F.Grad4(0.7504883828755602, -0.5029860367700724, 0.4004672082940195, -0.15296486218853164), new OpenSimplex2F.Grad4(0.6740059517812944, -0.5794684678643381, 0.3239847771997537, 0.3239847771997537), new OpenSimplex2F.Grad4(0.5794684678643381, -0.3239847771997537, -0.6740059517812944, -0.3239847771997537), new OpenSimplex2F.Grad4(0.5029860367700724, -0.4004672082940195, -0.7504883828755602, 0.15296486218853164), new OpenSimplex2F.Grad4(0.5029860367700724, 0.15296486218853164, -0.7504883828755602, -0.4004672082940195), new OpenSimplex2F.Grad4(0.4553054119602712, 0.08164729285680945, -0.8828161875373585, 0.08164729285680945), new OpenSimplex2F.Grad4(0.8828161875373585, -0.08164729285680945, -0.4553054119602712, -0.08164729285680945), new OpenSimplex2F.Grad4(0.7504883828755602, -0.15296486218853164, -0.5029860367700724, 0.4004672082940195), new OpenSimplex2F.Grad4(0.7504883828755602, 0.4004672082940195, -0.5029860367700724, -0.15296486218853164), new OpenSimplex2F.Grad4(0.6740059517812944, 0.3239847771997537, -0.5794684678643381, 0.3239847771997537), new OpenSimplex2F.Grad4(0.5794684678643381, -0.3239847771997537, -0.3239847771997537, -0.6740059517812944), new OpenSimplex2F.Grad4(0.5029860367700724, -0.4004672082940195, 0.15296486218853164, -0.7504883828755602), new OpenSimplex2F.Grad4(0.5029860367700724, 0.15296486218853164, -0.4004672082940195, -0.7504883828755602), new OpenSimplex2F.Grad4(0.4553054119602712, 0.08164729285680945, 0.08164729285680945, -0.8828161875373585), new OpenSimplex2F.Grad4(0.8828161875373585, -0.08164729285680945, -0.08164729285680945, -0.4553054119602712), new OpenSimplex2F.Grad4(0.7504883828755602, -0.15296486218853164, 0.4004672082940195, -0.5029860367700724), new OpenSimplex2F.Grad4(0.7504883828755602, 0.4004672082940195, -0.15296486218853164, -0.5029860367700724), new OpenSimplex2F.Grad4(0.6740059517812944, 0.3239847771997537, 0.3239847771997537, -0.5794684678643381), new OpenSimplex2F.Grad4(0.03381941603233842, 0.03381941603233842, 0.03381941603233842, 0.9982828964265062), new OpenSimplex2F.Grad4(-0.044802370851755174, -0.044802370851755174, 0.508629699630796, 0.8586508742123365), new OpenSimplex2F.Grad4(-0.044802370851755174, 0.508629699630796, -0.044802370851755174, 0.8586508742123365), new OpenSimplex2F.Grad4(-0.12128480194602098, 0.4321472685365301, 0.4321472685365301, 0.7821684431180708), new OpenSimplex2F.Grad4(0.508629699630796, -0.044802370851755174, -0.044802370851755174, 0.8586508742123365), new OpenSimplex2F.Grad4(0.4321472685365301, -0.12128480194602098, 0.4321472685365301, 0.7821684431180708), new OpenSimplex2F.Grad4(0.4321472685365301, 0.4321472685365301, -0.12128480194602098, 0.7821684431180708), new OpenSimplex2F.Grad4(0.37968289875261624, 0.37968289875261624, 0.37968289875261624, 0.753341017856078), new OpenSimplex2F.Grad4(0.03381941603233842, 0.03381941603233842, 0.9982828964265062, 0.03381941603233842), new OpenSimplex2F.Grad4(-0.044802370851755174, 0.044802370851755174, 0.8586508742123365, 0.508629699630796), new OpenSimplex2F.Grad4(-0.044802370851755174, 0.508629699630796, 0.8586508742123365, -0.044802370851755174), new OpenSimplex2F.Grad4(-0.12128480194602098, 0.4321472685365301, 0.7821684431180708, 0.4321472685365301), new OpenSimplex2F.Grad4(0.508629699630796, -0.044802370851755174, 0.8586508742123365, -0.044802370851755174), new OpenSimplex2F.Grad4(0.4321472685365301, -0.12128480194602098, 0.7821684431180708, 0.4321472685365301), new OpenSimplex2F.Grad4(0.4321472685365301, 0.4321472685365301, 0.7821684431180708, -0.12128480194602098), new OpenSimplex2F.Grad4(0.37968289875261624, 0.37968289875261624, 0.753341017856078, 0.37968289875261624), new OpenSimplex2F.Grad4(0.03381941603233842, 0.9982828964265062, 0.03381941603233842, 0.03381941603233842), new OpenSimplex2F.Grad4(-0.044802370851755174, 0.8586508742123365, -0.044802370851755174, 0.508629699630796), new OpenSimplex2F.Grad4(-0.044802370851755174, 0.8586508742123365, 0.508629699630796, -0.044802370851755174), new OpenSimplex2F.Grad4(-0.12128480194602098, 0.7821684431180708, 0.4321472685365301, 0.4321472685365301), new OpenSimplex2F.Grad4(0.508629699630796, 0.8586508742123365, -0.044802370851755174, -0.044802370851755174), new OpenSimplex2F.Grad4(0.4321472685365301, 0.7821684431180708, -0.12128480194602098, 0.4321472685365301), new OpenSimplex2F.Grad4(0.4321472685365301, 0.7821684431180708, 0.4321472685365301, -0.12128480194602098), new OpenSimplex2F.Grad4(0.37968289875261624, 0.753341017856078, 0.37968289875261624, 0.37968289875261624), new OpenSimplex2F.Grad4(0.9982828964265062, 0.03381941603233842, 0.03381941603233842, 0.03381941603233842), new OpenSimplex2F.Grad4(0.8586508742123365, -0.044802370851755174, -0.044802370851755174, 0.508629699630796), new OpenSimplex2F.Grad4(0.8586508742123365, -0.044802370851755174, 0.508629699630796, -0.044802370851755174), new OpenSimplex2F.Grad4(0.7821684431180708, -0.12128480194602098, 0.4321472685365301, 0.4321472685365301), new OpenSimplex2F.Grad4(0.8586508742123365, 0.508629699630796, -0.044802370851755174, -0.044802370851755174), new OpenSimplex2F.Grad4(0.7821684431180708, 0.4321472685365301, -0.12128480194602098, 0.4321472685365301), new OpenSimplex2F.Grad4(0.7821684431180708, 0.4321472685365301, 0.4321472685365301, -0.12128480194602098), new OpenSimplex2F.Grad4(0.753341017856078, 0.37968289875261624, 0.37968289875261624, 0.37968289875261624) };
        for (int i = 0; i < grad4.length; i++) {
            grad4[i].dx /= 0.009202377986303158;
            grad4[i].dy /= 0.009202377986303158;
            grad4[i].dz /= 0.009202377986303158;
            grad4[i].dw /= 0.009202377986303158;
        }
        for (int i = 0; i < 2048; i++) {
            GRADIENTS_4D[i] = grad4[i % grad4.length];
        }
    }

    private static class Grad2 {

        double dx;

        double dy;

        public Grad2(double dx, double dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private static class Grad3 {

        double dx;

        double dy;

        double dz;

        public Grad3(double dx, double dy, double dz) {
            this.dx = dx;
            this.dy = dy;
            this.dz = dz;
        }
    }

    private static class Grad4 {

        double dx;

        double dy;

        double dz;

        double dw;

        public Grad4(double dx, double dy, double dz, double dw) {
            this.dx = dx;
            this.dy = dy;
            this.dz = dz;
            this.dw = dw;
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

        OpenSimplex2F.LatticePoint3D nextOnFailure;

        OpenSimplex2F.LatticePoint3D nextOnSuccess;

        public LatticePoint3D(int xrv, int yrv, int zrv, int lattice) {
            this.dxr = (double) (-xrv) + (double) lattice * 0.5;
            this.dyr = (double) (-yrv) + (double) lattice * 0.5;
            this.dzr = (double) (-zrv) + (double) lattice * 0.5;
            this.xrv = xrv + lattice * 1024;
            this.yrv = yrv + lattice * 1024;
            this.zrv = zrv + lattice * 1024;
        }
    }

    private static class LatticePoint4D {

        int xsv;

        int ysv;

        int zsv;

        int wsv;

        double dx;

        double dy;

        double dz;

        double dw;

        double xsi;

        double ysi;

        double zsi;

        double wsi;

        double ssiDelta;

        public LatticePoint4D(int xsv, int ysv, int zsv, int wsv) {
            this.xsv = xsv + 409;
            this.ysv = ysv + 409;
            this.zsv = zsv + 409;
            this.wsv = wsv + 409;
            double ssv = (double) (xsv + ysv + zsv + wsv) * 0.309016994374947;
            this.dx = (double) (-xsv) - ssv;
            this.dy = (double) (-ysv) - ssv;
            this.dz = (double) (-zsv) - ssv;
            this.dw = (double) (-wsv) - ssv;
            this.xsi = 0.2 - (double) xsv;
            this.ysi = 0.2 - (double) ysv;
            this.zsi = 0.2 - (double) zsv;
            this.wsi = 0.2 - (double) wsv;
            this.ssiDelta = (0.8 - (double) xsv - (double) ysv - (double) zsv - (double) wsv) * 0.309016994374947;
        }
    }
}