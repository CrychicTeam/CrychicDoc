package icyllis.arc3d.core;

import java.util.Arrays;

public class Geometry {

    public static final int MAX_CONIC_TO_QUADS_LEVEL = 5;

    public static int findQuadRoots(float A, float B, float C, float[] roots, int off) {
        if (A == 0.0F) {
            return valid_divide(-C, B, roots, off);
        } else {
            double dis = (double) B * (double) B - 4.0 * (double) A * (double) C;
            if (dis < 0.0) {
                return 0;
            } else {
                float R = (float) Math.sqrt(dis);
                if (!Float.isFinite(R)) {
                    return 0;
                } else {
                    int var10;
                    if (B < 0.0F) {
                        float Q = -(B - R) / 2.0F;
                        var10 = off + valid_divide(Q, A, roots, off);
                        var10 += valid_divide(C, Q, roots, var10);
                    } else {
                        float Q = -(B + R) / 2.0F;
                        var10 = off + valid_divide(C, Q, roots, off);
                        var10 += valid_divide(Q, A, roots, var10);
                    }
                    return var10 - off == 2 && roots[off] == roots[off + 1] ? 1 : var10 - off;
                }
            }
        }
    }

    public static int findUnitQuadRoots(float A, float B, float C, float[] roots, int off) {
        if (A == 0.0F) {
            return valid_unit_divide(-C, B, roots, off);
        } else {
            double dis = (double) B * (double) B - 4.0 * (double) A * (double) C;
            if (dis < 0.0) {
                return 0;
            } else {
                float R = (float) Math.sqrt(dis);
                if (!Float.isFinite(R)) {
                    return 0;
                } else {
                    float Q = B < 0.0F ? -(B - R) / 2.0F : -(B + R) / 2.0F;
                    int ret = off + valid_unit_divide(Q, A, roots, off);
                    ret += valid_unit_divide(C, Q, roots, ret);
                    if (ret - off == 2) {
                        if (roots[off] > roots[off + 1]) {
                            float tmp = roots[off];
                            roots[off] = roots[off + 1];
                            roots[off + 1] = tmp;
                        } else if (roots[off] == roots[off + 1]) {
                            ret--;
                        }
                    }
                    return ret - off;
                }
            }
        }
    }

    static int valid_divide(float numer, float denom, float[] ratio, int off) {
        float r = numer / denom;
        if (!Float.isFinite(r)) {
            return 0;
        } else if (r == 0.0F) {
            return 0;
        } else {
            ratio[off] = r;
            return 1;
        }
    }

    static int valid_unit_divide(float numer, float denom, float[] ratio, int off) {
        if (numer < 0.0F) {
            numer = -numer;
            denom = -denom;
        }
        if (denom != 0.0F && numer != 0.0F && !(numer >= denom)) {
            float r = numer / denom;
            if (Float.isNaN(r)) {
                return 0;
            } else {
                assert r >= 0.0F && r < 1.0F;
                if (r == 0.0F) {
                    return 0;
                } else {
                    ratio[off] = r;
                    return 1;
                }
            }
        } else {
            return 0;
        }
    }

    public static float findQuadMaxCurvature(@Size(min = 6L) float[] src, int off) {
        return findQuadMaxCurvature(src[off], src[off + 1], src[off + 2], src[off + 3], src[off + 4], src[off + 5]);
    }

    public static float findQuadMaxCurvature(float x0, float y0, float x1, float y1, float x2, float y2) {
        float Ax = x1 - x0;
        float Ay = y1 - y0;
        float Bx = x2 - 2.0F * x1 + x0;
        float By = y2 - 2.0F * y1 + y0;
        float numer = -(Ax * Bx + Ay * By);
        float denom = Bx * Bx + By * By;
        if (denom < 0.0F) {
            numer = -numer;
            denom = -denom;
        }
        if (numer <= 0.0F) {
            return 0.0F;
        } else if (numer >= denom) {
            return 1.0F;
        } else {
            float t = numer / denom;
            assert 0.0F <= t && t < 1.0F || Float.isNaN(t);
            return t;
        }
    }

    public static void evalQuadAt(@Size(min = 6L) float[] src, int srcOff, @Size(min = 2L) float[] dst, int dstOff, float t) {
        evalQuadAt(src[srcOff], src[srcOff + 1], src[srcOff + 2], src[srcOff + 3], src[srcOff + 4], src[srcOff + 5], t, dst, dstOff);
    }

    public static void evalQuadAt(float x0, float y0, float x1, float y1, float x2, float y2, float t, @Size(min = 2L) float[] dst, int off) {
        assert t >= 0.0F && t <= 1.0F;
        float Ax = x2 - (x1 + x1) + x0;
        float Ay = y2 - (y1 + y1) + y0;
        float Bx = x1 - x0;
        float By = y1 - y0;
        dst[off] = (Ax * t + Bx + Bx) * t + x0;
        dst[off + 1] = (Ay * t + By + By) * t + y0;
    }

    public static void evalQuadAt(@Size(min = 6L) float[] src, int srcOff, float t, @Size(min = 2L) float[] pos, int posOff, @Size(min = 2L) float[] tangent, int tangentOff) {
        evalQuadAt(src[srcOff], src[srcOff + 1], src[srcOff + 2], src[srcOff + 3], src[srcOff + 4], src[srcOff + 5], t, pos, posOff, tangent, tangentOff);
    }

    public static void evalQuadAt(float x0, float y0, float x1, float y1, float x2, float y2, float t, @Size(min = 2L) float[] pos, int posOff, @Size(min = 2L) float[] tangent, int tangentOff) {
        assert t >= 0.0F && t <= 1.0F;
        float Ax = x2 - (x1 + x1) + x0;
        float Ay = y2 - (y1 + y1) + y0;
        float Bx = x1 - x0;
        float By = y1 - y0;
        if (pos != null) {
            pos[posOff] = (Ax * t + Bx + Bx) * t + x0;
            pos[posOff + 1] = (Ay * t + By + By) * t + y0;
        }
        if (tangent != null) {
            if ((t != 0.0F || x0 != x1 || y0 != y1) && (t != 1.0F || x1 != x2 || y1 != y2)) {
                tangent[tangentOff] = Ax * t + Bx;
                tangent[tangentOff + 1] = Ay * t + By;
            } else {
                tangent[tangentOff] = x2 - x0;
                tangent[tangentOff + 1] = y2 - y0;
            }
        }
    }

    public static void chopQuadAt(@Size(min = 6L) float[] src, int srcOff, @Size(min = 10L) float[] dst, int dstOff, float t) {
        chopQuadAt(src[srcOff], src[srcOff + 1], src[srcOff + 2], src[srcOff + 3], src[srcOff + 4], src[srcOff + 5], t, dst, dstOff);
    }

    public static void chopQuadAt(float x0, float y0, float x1, float y1, float x2, float y2, float t, @Size(min = 10L) float[] dst, int off) {
        assert t >= 0.0F && t <= 1.0F;
        if (t == 1.0F) {
            dst[off] = x0;
            dst[off + 1] = y0;
            dst[off + 2] = x1;
            dst[off + 3] = y1;
            dst[off + 4] = x2;
            dst[off + 5] = y2;
            dst[off + 6] = x2;
            dst[off + 7] = y2;
            dst[off + 8] = x2;
            dst[off + 9] = y2;
        } else {
            float abx = MathUtil.mix(x0, x1, t);
            float aby = MathUtil.mix(y0, y1, t);
            float bcx = MathUtil.mix(x1, x2, t);
            float bcy = MathUtil.mix(y1, y2, t);
            float abcx = MathUtil.mix(abx, bcx, t);
            float abcy = MathUtil.mix(aby, bcy, t);
            dst[off] = x0;
            dst[off + 1] = y0;
            dst[off + 2] = abx;
            dst[off + 3] = aby;
            dst[off + 4] = abcx;
            dst[off + 5] = abcy;
            dst[off + 6] = bcx;
            dst[off + 7] = bcy;
            dst[off + 8] = x2;
            dst[off + 9] = y2;
        }
    }

    public static int findCubicInflectionPoints(@Size(min = 8L) float[] src, int srcOff, @Size(min = 2L) float[] dst, int dstOff) {
        return findCubicInflectionPoints(src[srcOff], src[srcOff + 1], src[srcOff + 2], src[srcOff + 3], src[srcOff + 4], src[srcOff + 5], src[srcOff + 6], src[srcOff + 7], dst, dstOff);
    }

    public static int findCubicInflectionPoints(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, @Size(min = 2L) float[] roots, int off) {
        float Ax = x1 - x0;
        float Ay = y1 - y0;
        float Bx = x2 - 2.0F * x1 + x0;
        float By = y2 - 2.0F * y1 + y0;
        float Cx = x3 + 3.0F * (x1 - x2) - x0;
        float Cy = y3 + 3.0F * (y1 - y2) - y0;
        return findUnitQuadRoots(Bx * Cy - By * Cx, Ax * Cy - Ay * Cx, Ax * By - Ay * Bx, roots, off);
    }

    public static void evalCubicAt(@Size(min = 8L) float[] src, int srcOff, @Size(min = 2L) float[] dst, int dstOff, float t) {
        evalCubicAt(src[srcOff], src[srcOff + 1], src[srcOff + 2], src[srcOff + 3], src[srcOff + 4], src[srcOff + 5], src[srcOff + 6], src[srcOff + 7], t, dst, dstOff);
    }

    public static void evalCubicAt(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, float t, @Size(min = 2L) float[] dst, int off) {
        assert t >= 0.0F && t <= 1.0F;
        float Ax = x3 + 3.0F * (x1 - x2) - x0;
        float Ay = y3 + 3.0F * (y1 - y2) - y0;
        float Bx = 3.0F * (x2 - (x1 + x1) + x0);
        float By = 3.0F * (y2 - (y1 + y1) + y0);
        float Cx = 3.0F * (x1 - x0);
        float Cy = 3.0F * (y1 - y0);
        dst[off] = ((Ax * t + Bx) * t + Cx) * t + x0;
        dst[off + 1] = ((Ay * t + By) * t + Cy) * t + y0;
    }

    public static void evalCubicAt(@Size(min = 8L) float[] src, int srcOff, float t, @Size(min = 2L) float[] pos, int posOff, @Size(min = 2L) float[] tangent, int tangentOff) {
        evalCubicAt(src[srcOff], src[srcOff + 1], src[srcOff + 2], src[srcOff + 3], src[srcOff + 4], src[srcOff + 5], src[srcOff + 6], src[srcOff + 7], t, pos, posOff, tangent, tangentOff);
    }

    public static void evalCubicAt(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, float t, @Size(min = 2L) float[] pos, int posOff, @Size(min = 2L) float[] tangent, int tangentOff) {
        assert t >= 0.0F && t <= 1.0F;
        float Ax = x3 + 3.0F * (x1 - x2) - x0;
        float Ay = y3 + 3.0F * (y1 - y2) - y0;
        if (pos != null) {
            float Bx = 3.0F * (x2 - (x1 + x1) + x0);
            float By = 3.0F * (y2 - (y1 + y1) + y0);
            float Cx = 3.0F * (x1 - x0);
            float Cy = 3.0F * (y1 - y0);
            pos[posOff] = ((Ax * t + Bx) * t + Cx) * t + x0;
            pos[posOff + 1] = ((Ay * t + By) * t + Cy) * t + y0;
        }
        if (tangent != null) {
            if (t == 0.0F && x0 == x1 && y0 == y1 || t == 1.0F && x2 == x3 && y2 == y3) {
                float Tx;
                float Ty;
                if (t == 0.0F) {
                    Tx = x2 - x0;
                    Ty = y2 - y0;
                } else {
                    Tx = x3 - x1;
                    Ty = y3 - y1;
                }
                if (Tx == 0.0F && Ty == 0.0F) {
                    tangent[tangentOff] = x3 - x0;
                    tangent[tangentOff + 1] = y3 - y0;
                } else {
                    tangent[tangentOff] = Tx;
                    tangent[tangentOff + 1] = Ty;
                }
            } else {
                float Bx = 2.0F * (x2 - (x1 + x1) + x0);
                float By = 2.0F * (y2 - (y1 + y1) + y0);
                float Cx = x1 - x0;
                float Cy = y1 - y0;
                tangent[tangentOff] = (Ax * t + Bx) * t + Cx;
                tangent[tangentOff + 1] = (Ay * t + By) * t + Cy;
            }
        }
    }

    public static void eval_cubic_derivative(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, float t, @Size(min = 2L) float[] dst, int off) {
        float Ax = x3 + 3.0F * (x1 - x2) - x0;
        float Ay = y3 + 3.0F * (y1 - y2) - y0;
        float Bx = 2.0F * (x2 - (x1 + x1) + x0);
        float By = 2.0F * (y2 - (y1 + y1) + y0);
        float Cx = x1 - x0;
        float Cy = y1 - y0;
        dst[off] = (Ax * t + Bx) * t + Cx;
        dst[off + 1] = (Ay * t + By) * t + Cy;
    }

    public static void chopCubicAt(@Size(min = 8L) float[] src, int srcOff, @Size(min = 14L) float[] dst, int dstOff, float t) {
        chopCubicAt(src[srcOff], src[srcOff + 1], src[srcOff + 2], src[srcOff + 3], src[srcOff + 4], src[srcOff + 5], src[srcOff + 6], src[srcOff + 7], t, dst, dstOff);
    }

    public static void chopCubicAt(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, float t, @Size(min = 14L) float[] dst, int off) {
        assert t >= 0.0F && t <= 1.0F;
        if (t == 1.0F) {
            dst[off] = x0;
            dst[off + 1] = y0;
            dst[off + 2] = x1;
            dst[off + 3] = y1;
            dst[off + 4] = x2;
            dst[off + 5] = y2;
            dst[off + 6] = x3;
            dst[off + 7] = y3;
            dst[off + 8] = x3;
            dst[off + 9] = y3;
            dst[off + 10] = x3;
            dst[off + 11] = y3;
            dst[off + 12] = x3;
            dst[off + 13] = y3;
        } else {
            float abx = MathUtil.mix(x0, x1, t);
            float aby = MathUtil.mix(y0, y1, t);
            float bcx = MathUtil.mix(x1, x2, t);
            float bcy = MathUtil.mix(y1, y2, t);
            float cdx = MathUtil.mix(x2, x3, t);
            float cdy = MathUtil.mix(y2, y3, t);
            float abcx = MathUtil.mix(abx, bcx, t);
            float abcy = MathUtil.mix(aby, bcy, t);
            float bcdx = MathUtil.mix(bcx, cdx, t);
            float bcdy = MathUtil.mix(bcy, cdy, t);
            float abcdx = MathUtil.mix(abcx, bcdx, t);
            float abcdy = MathUtil.mix(abcy, bcdy, t);
            dst[off] = x0;
            dst[off + 1] = y0;
            dst[off + 2] = abx;
            dst[off + 3] = aby;
            dst[off + 4] = abcx;
            dst[off + 5] = abcy;
            dst[off + 6] = abcdx;
            dst[off + 7] = abcdy;
            dst[off + 8] = bcdx;
            dst[off + 9] = bcdy;
            dst[off + 10] = cdx;
            dst[off + 11] = cdy;
            dst[off + 12] = x3;
            dst[off + 13] = y3;
        }
    }

    public static int deduplicate_pairs(float[] arr, int off, int count) {
        for (int n = count; n > 1; n--) {
            if (arr[off] != arr[off + 1]) {
                off++;
            } else {
                for (int i = 1; i < n; i++) {
                    arr[off + i - 1] = arr[off + i];
                }
                count--;
            }
        }
        return count;
    }

    public static int findUnitCubicRoots(float A, float B, float C, float D, float[] roots, int off) {
        if (A == 0.0F) {
            return findUnitQuadRoots(B, C, D, roots, off);
        } else {
            double inv = 1.0 / (double) A;
            double a = (double) B * inv;
            double b = (double) C * inv;
            double c = (double) D * inv;
            inv = (a * a - b * 3.0) / 9.0;
            double R = (2.0 * a * a * a - 9.0 * a * b + 27.0 * c) / 54.0;
            double Q3 = inv * inv * inv;
            double R2MinusQ3 = R * R - Q3;
            double aDiv3 = a / 3.0;
            if (R2MinusQ3 < 0.0) {
                double theta = Math.acos(MathUtil.pin(R / Math.sqrt(Q3), 1.0, 1.0));
                double neg2RootQ = -2.0 * Math.sqrt(inv);
                roots[off] = (float) MathUtil.pin(neg2RootQ * Math.cos(theta / 3.0) - aDiv3, 0.0, 1.0);
                roots[off + 1] = (float) MathUtil.pin(neg2RootQ * Math.cos((theta + (Math.PI * 2)) / 3.0) - aDiv3, 0.0, 1.0);
                roots[off + 2] = (float) MathUtil.pin(neg2RootQ * Math.cos((theta - (Math.PI * 2)) / 3.0) - aDiv3, 0.0, 1.0);
                Arrays.sort(roots, off, off + 3);
                return deduplicate_pairs(roots, off, 3);
            } else {
                double S = Math.abs(R) + Math.sqrt(R2MinusQ3);
                S = Math.cbrt(S);
                if (R > 0.0) {
                    S = -S;
                }
                if (S != 0.0) {
                    S += inv / S;
                }
                roots[off] = (float) MathUtil.pin(S - aDiv3, 0.0, 1.0);
                return 1;
            }
        }
    }

    public static int findCubicMaxCurvature(@Size(min = 8L) float[] src, int srcOff, @Size(min = 3L) float[] dst, int dstOff) {
        return findCubicMaxCurvature(src[srcOff], src[srcOff + 1], src[srcOff + 2], src[srcOff + 3], src[srcOff + 4], src[srcOff + 5], src[srcOff + 6], src[srcOff + 7], dst, dstOff);
    }

    public static int findCubicMaxCurvature(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, @Size(min = 3L) float[] roots, int off) {
        float Ax = x1 - x0;
        float Ay = y1 - y0;
        float Bx = x2 - 2.0F * x1 + x0;
        float By = y2 - 2.0F * y1 + y0;
        float Cx = x3 + 3.0F * (x1 - x2) - x0;
        float Cy = y3 + 3.0F * (y1 - y2) - y0;
        return findUnitCubicRoots(Cx * Cx + Cy * Cy, 3.0F * (Bx * Cx + By * Cy), 2.0F * (Bx * Bx + By * By) + Cx * Ax + Cy * Ay, Ax * Bx + Ay * By, roots, off);
    }

    static boolean same_side(float s0x, float s0y, float s1x, float s1y, float d0x, float d0y, float d1x, float d1y) {
        float lx = d1x - d0x;
        float ly = d1y - d0y;
        return Point.crossProduct(lx, ly, s0x - d0x, s0y - d0y) * Point.crossProduct(lx, ly, s1x - d0x, s1y - d0y) >= 0.0F;
    }

    public static float findCubicCusp(@Size(min = 8L) float[] src, int off) {
        return findCubicCusp(src[off], src[off + 1], src[off + 2], src[off + 3], src[off + 4], src[off + 5], src[off + 6], src[off + 7]);
    }

    public static float findCubicCusp(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
        if (x0 == x1 && y0 == y1) {
            return -1.0F;
        } else if (x2 == x3 && y2 == y3) {
            return -1.0F;
        } else if (!same_side(x0, y0, x1, y1, x2, y2, x3, y3) && !same_side(x2, y2, x3, y3, x0, y0, x1, y1)) {
            float[] storage = new float[5];
            int roots = findCubicMaxCurvature(x0, y0, x1, y1, x2, y2, x3, y3, storage, 0);
            for (int index = 0; index < roots; index++) {
                float testT = storage[index];
                if (!(testT <= 0.0F) && !(testT >= 1.0F)) {
                    eval_cubic_derivative(x0, y0, x1, y1, x2, y2, x3, y3, testT, storage, 3);
                    float magnitude = Point.lengthSq(storage[3], storage[4]);
                    float precision = (Point.distanceToSq(x1, y1, x0, y0) + Point.distanceToSq(x2, y2, x1, y1) + Point.distanceToSq(x3, y3, x2, y2)) * 1.0E-8F;
                    if (magnitude < precision) {
                        return testT;
                    }
                }
            }
            return -1.0F;
        } else {
            return -1.0F;
        }
    }

    public static int computeConicToQuadsLevel(float x0, float y0, float x1, float y1, float x2, float y2, float w1, float tol) {
        float a = w1 - 1.0F;
        float k = a / (4.0F * (2.0F + a));
        float x = k * (x0 - 2.0F * x1 + x2);
        float y = k * (y0 - 2.0F * y1 + y2);
        int level = MathUtil.ceilLog16((x * x + y * y) / (tol * tol));
        return Math.min(level, 5);
    }

    public static int computeConicToQuads(float x0, float y0, float x1, float y1, float x2, float y2, float w1, float[] dst, int off, int level) {
        if (level >= 0 && level <= 5) {
            dst[off] = x0;
            dst[off + 1] = y0;
            int count = subdivideConic(x0, y0, x1, y1, x2, y2, w1, dst, off + 2, level);
            if (count == -9) {
                level = 1;
                count = 8;
            } else {
                assert 4 * (1 << level) == count;
            }
            float prod = 0.0F;
            for (int i = off; i < off + count + 2; i++) {
                prod *= dst[i];
            }
            if (prod != 0.0F) {
                for (int i = off + 2; i < off + count; i += 2) {
                    dst[i] = x1;
                    dst[i + 1] = y1;
                }
            }
            return 1 << level;
        } else {
            throw new IllegalArgumentException();
        }
    }

    static boolean between(float a, float b, float c) {
        return (a - b) * (c - b) <= 0.0F;
    }

    static int subdivideConic(float p0x, float p0y, float p1x, float p1y, float p2x, float p2y, float w1, float[] dst, int off, int level) {
        assert level >= 0;
        if (level == 0) {
            dst[off] = p1x;
            dst[off + 1] = p1y;
            dst[off + 2] = p2x;
            dst[off + 3] = p2y;
            return 4;
        } else {
            float scale = 1.0F / (1.0F + w1);
            float t0x = p0x * scale;
            float t0y = p0y * scale;
            float t1x = p1x * w1 * scale;
            float t1y = p1y * w1 * scale;
            float t2x = p2x * scale;
            float t2y = p2y * scale;
            float q1x = t0x + t1x;
            float q1y = t0y + t1y;
            float q2x = 0.5F * t0x + t1x + 0.5F * t2x;
            float q2y = 0.5F * t0y + t1y + 0.5F * t2y;
            float q3x = t1x + t2x;
            float q3y = t1y + t2y;
            if (level == 5 && Point.equals(q1x, q1y, q2x, q2y) && Point.equals(q2x, q2y, q3x, q3y)) {
                dst[off] = q1x;
                dst[off + 1] = q1y;
                dst[off + 2] = q1x;
                dst[off + 3] = q1y;
                dst[off + 4] = q1x;
                dst[off + 5] = q1y;
                dst[off + 6] = p2x;
                dst[off + 7] = p2y;
                return -9;
            } else {
                w1 = MathUtil.sqrt(0.5F + w1 * 0.5F);
                if (between(p0y, p1y, p2y)) {
                    if (!between(p0y, q2y, p2y)) {
                        q2y = Math.abs(q2y - p0y) < Math.abs(q2y - p2y) ? p0y : p2y;
                    }
                    if (!between(p0y, q1y, q2y)) {
                        q1y = p0y;
                    }
                    if (!between(q2y, q3y, p2y)) {
                        q3y = p2y;
                    }
                    assert between(p0y, q1y, q2y);
                    assert between(q1y, q2y, q3y);
                    assert between(q2y, q3y, p2y);
                }
                int ret = off + subdivideConic(p0x, p0y, q1x, q1y, q2x, q2y, w1, dst, off, --level);
                ret += subdivideConic(q2x, q2y, q3x, q3y, p2x, p2y, w1, dst, ret, level);
                return ret - off;
            }
        }
    }

    protected Geometry() {
        throw new UnsupportedOperationException();
    }
}