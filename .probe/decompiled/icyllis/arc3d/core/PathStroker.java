package icyllis.arc3d.core;

import javax.annotation.Nonnull;
import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.Contract;

public class PathStroker implements PathConsumer {

    private static final boolean DEBUG = true;

    private PathConsumer mOuter;

    private final Path mInner = new Path();

    private float mRadius;

    private float mInvMiterLimit;

    private float mResScale;

    private float mInvResScale;

    private float mInvResScaleSquared;

    private int mCapStyle;

    private int mJoinStyle;

    private PathStroker.Capper mCapper;

    private PathStroker.Joiner mJoiner;

    private float mFirstX;

    private float mFirstY;

    private float mPrevX;

    private float mPrevY;

    private float mFirstNormalX;

    private float mFirstNormalY;

    private float mPrevNormalX;

    private float mPrevNormalY;

    private float mFirstUnitNormalX;

    private float mFirstUnitNormalY;

    private float mPrevUnitNormalX;

    private float mPrevUnitNormalY;

    private float mFirstOuterX;

    private float mFirstOuterY;

    private boolean mJoinCompleted;

    private int mSegmentCount;

    private boolean mPrevIsLine;

    private static final int NORMAL_X = 0;

    private static final int NORMAL_Y = 1;

    private static final int UNIT_NORMAL_X = 2;

    private static final int UNIT_NORMAL_Y = 3;

    private final float[] mNormal = new float[4];

    static final int MAX_TANGENT_RECURSION_DEPTH = 15;

    static final int MAX_CUBIC_RECURSION_DEPTH = 24;

    static final int MAX_QUAD_RECURSION_DEPTH = 33;

    private int mRecursionDepth;

    private boolean mFoundTangents;

    private final PathStroker.QuadState[] mQuadStack = new PathStroker.QuadState[34];

    private static final int STROKE_TYPE_OUTER = 1;

    private static final int STROKE_TYPE_INNER = -1;

    private int mStrokeType;

    private final float[] mCurve;

    private static final int INTERSECT_SUBDIVIDE = 0;

    private static final int INTERSECT_DEGENERATE = 1;

    private static final int INTERSECT_QUADRATIC = 2;

    public PathStroker() {
        for (int i = 0; i < this.mQuadStack.length; i++) {
            this.mQuadStack[i] = new PathStroker.QuadState();
        }
        this.mCurve = new float[32];
    }

    public void init(@Nonnull PathConsumer out, float radius, int cap, int join, float miterLimit, float resScale) {
        assert out != this;
        this.mOuter = out;
        this.mRadius = radius;
        if (join == 0) {
            if (miterLimit <= 1.0F) {
                join = 32;
            } else {
                this.mInvMiterLimit = 1.0F / miterLimit;
            }
        }
        this.mCapStyle = cap;
        this.mJoinStyle = join;
        this.mCapper = PathStroker.Capper.get(cap);
        this.mJoiner = PathStroker.Joiner.get(join);
        this.mSegmentCount = -1;
        this.mPrevIsLine = false;
        this.mResScale = resScale;
        this.mInvResScale = 1.0F / (resScale * 4.0F);
        this.mInvResScaleSquared = this.mInvResScale * this.mInvResScale;
    }

    private void log(int result, PathStroker.QuadState pp, @PrintFormat String format, Object... args) {
        System.out.printf("[%d] ", this.mRecursionDepth);
        System.out.printf(format, args);
        String resultStr = switch(result) {
            case 0 ->
                "Subdivide";
            case 1 ->
                "Degenerate";
            case 2 ->
                "Quadratic";
            default ->
                throw new AssertionError();
        };
        System.out.printf("\n  %s t=(%g,%g)\n", resultStr, pp.t_from, pp.t_to);
    }

    @Override
    public void moveTo(float x, float y) {
        if (this.mSegmentCount > 0) {
            this.finish(false, false);
        }
        this.mSegmentCount = 0;
        this.mFirstX = this.mPrevX = x;
        this.mFirstY = this.mPrevY = y;
        this.mJoinCompleted = false;
    }

    private boolean preJoinTo(float x, float y, boolean isLine) {
        assert this.mSegmentCount >= 0;
        this.mNormal[0] = (x - this.mPrevX) * this.mResScale;
        this.mNormal[1] = (y - this.mPrevY) * this.mResScale;
        if (!Point.normalize(this.mNormal, 0)) {
            if (this.mCapStyle == 0) {
                return false;
            }
            this.mNormal[0] = this.mRadius;
            this.mNormal[1] = 0.0F;
            this.mNormal[2] = 1.0F;
            this.mNormal[3] = 0.0F;
        } else {
            float newX = this.mNormal[1];
            float newY = -this.mNormal[0];
            this.mNormal[0] = newX * this.mRadius;
            this.mNormal[1] = newY * this.mRadius;
            this.mNormal[2] = newX;
            this.mNormal[3] = newY;
        }
        if (this.mSegmentCount == 0) {
            this.mFirstNormalX = this.mNormal[0];
            this.mFirstNormalY = this.mNormal[1];
            this.mFirstUnitNormalX = this.mNormal[2];
            this.mFirstUnitNormalY = this.mNormal[3];
            this.mFirstOuterX = this.mPrevX + this.mFirstNormalX;
            this.mFirstOuterY = this.mPrevY + this.mFirstNormalY;
            this.mOuter.moveTo(this.mFirstOuterX, this.mFirstOuterY);
            this.mInner.moveTo(this.mPrevX - this.mFirstNormalX, this.mPrevY - this.mFirstNormalY);
        } else {
            this.mJoiner.join(this.mOuter, this.mInner, this.mPrevUnitNormalX, this.mPrevUnitNormalY, this.mPrevX, this.mPrevY, this.mNormal[2], this.mNormal[3], this.mRadius, this.mInvMiterLimit, this.mPrevIsLine, isLine);
        }
        this.mPrevIsLine = isLine;
        return true;
    }

    private void postJoinTo(float x, float y) {
        this.mJoinCompleted = true;
        this.mPrevX = x;
        this.mPrevY = y;
        this.mPrevNormalX = this.mNormal[0];
        this.mPrevNormalY = this.mNormal[1];
        this.mPrevUnitNormalX = this.mNormal[2];
        this.mPrevUnitNormalY = this.mNormal[3];
        this.mSegmentCount++;
    }

    @Override
    public void lineTo(float x, float y) {
        boolean degenerate = Point.isApproxEqual(this.mPrevX, this.mPrevY, x, y, 1.0E-5F * this.mInvResScale);
        if (!degenerate || this.mCapStyle != 0) {
            if (!degenerate || !this.mJoinCompleted) {
                if (this.preJoinTo(x, y, true)) {
                    this.mOuter.lineTo(x + this.mNormal[0], y + this.mNormal[1]);
                    this.mInner.lineTo(x - this.mNormal[0], y - this.mNormal[1]);
                    this.postJoinTo(x, y);
                }
            }
        }
    }

    private void initStroke(int type, PathStroker.QuadState pp, float tFrom, float tTo) {
        assert type == 1 || type == -1;
        this.mStrokeType = type;
        this.mFoundTangents = false;
        this.mRecursionDepth = 0;
        pp.init(tFrom, tTo);
    }

    private float[] getQuad(float x1, float y1, float x2, float y2) {
        float[] c = this.mCurve;
        c[0] = this.mPrevX;
        c[1] = this.mPrevY;
        c[2] = x1;
        c[3] = y1;
        c[4] = x2;
        c[5] = y2;
        return c;
    }

    private float[] getCubic(float x1, float y1, float x2, float y2, float x3, float y3) {
        float[] c = this.mCurve;
        c[0] = this.mPrevX;
        c[1] = this.mPrevY;
        c[2] = x1;
        c[3] = y1;
        c[4] = x2;
        c[5] = y2;
        c[6] = x3;
        c[7] = y3;
        return c;
    }

    private static boolean quad_in_line(float[] quad) {
        float pMax = -1.0F;
        int outer1 = 0;
        int outer2 = 0;
        for (int index = 0; index < 2; index++) {
            for (int inner = index + 1; inner < 3; inner++) {
                float testMax = Math.max(Math.abs(quad[inner << 1] - quad[index << 1]), Math.abs(quad[inner << 1 | 1] - quad[index << 1 | 1]));
                if (pMax < testMax) {
                    outer1 = index;
                    outer2 = inner;
                    pMax = testMax;
                }
            }
        }
        assert outer1 >= 0;
        assert outer2 >= 1;
        assert outer1 < outer2;
        int mid = outer1 ^ outer2 ^ 3;
        float lineSlop = pMax * pMax * 5.0E-6F;
        return Point.distanceToLineSegmentBetweenSq(quad[mid << 1], quad[mid << 1 | 1], quad[outer1 << 1], quad[outer1 << 1 | 1], quad[outer2 << 1], quad[outer2 << 1 | 1]) <= lineSlop;
    }

    private static boolean cubic_in_line(float[] cubic) {
        float pMax = -1.0F;
        int outer1 = 0;
        int outer2 = 0;
        for (int index = 0; index < 3; index++) {
            for (int inner = index + 1; inner < 4; inner++) {
                float testMax = Math.max(Math.abs(cubic[inner << 1] - cubic[index << 1]), Math.abs(cubic[inner << 1 | 1] - cubic[index << 1 | 1]));
                if (pMax < testMax) {
                    outer1 = index;
                    outer2 = inner;
                    pMax = testMax;
                }
            }
        }
        assert outer1 >= 0;
        assert outer2 >= 1;
        assert outer1 < outer2;
        int mid1 = 1 + (2 >> outer2) >> outer1;
        assert outer1 != mid1 && outer2 != mid1;
        int mid2 = outer1 ^ outer2 ^ mid1;
        assert mid2 >= 1;
        assert mid2 != outer1 && mid2 != outer2 && mid2 != mid1;
        assert (1 << outer1 | 1 << outer2 | 1 << mid1 | 1 << mid2) == 15;
        float lineSlop = pMax * pMax * 1.0E-5F;
        return Point.distanceToLineSegmentBetweenSq(cubic[mid1 << 1], cubic[mid1 << 1 | 1], cubic[outer1 << 1], cubic[outer1 << 1 | 1], cubic[outer2 << 1], cubic[outer2 << 1 | 1]) <= lineSlop && Point.distanceToLineSegmentBetweenSq(cubic[mid2 << 1], cubic[mid2 << 1 | 1], cubic[outer1 << 1], cubic[outer1 << 1 | 1], cubic[outer2 << 1], cubic[outer2 << 1 | 1]) <= lineSlop;
    }

    private int intersect_ray(PathStroker.QuadState pp, boolean computeControlPoint) {
        float startX = pp.q0x;
        float startY = pp.q0y;
        float endX = pp.q2x;
        float endY = pp.q2y;
        float aLenX = pp.tan0x - startX;
        float aLenY = pp.tan0y - startY;
        float bLenX = pp.tan2x - endX;
        float bLenY = pp.tan2y - endY;
        float denom = Point.crossProduct(aLenX, aLenY, bLenX, bLenY);
        if (denom != 0.0F && Float.isFinite(denom)) {
            pp.opposite_tangents = false;
            float ab0x = startX - endX;
            float ab0y = startY - endY;
            float numerA = Point.crossProduct(bLenX, bLenY, ab0x, ab0y);
            float numerB = Point.crossProduct(aLenX, aLenY, ab0x, ab0y);
            if (numerA >= 0.0F == numerB >= 0.0F) {
                float dist1 = Point.distanceToLineSegmentBetweenSq(startX, startY, endX, endY, pp.tan2x, pp.tan2y);
                float dist2 = Point.distanceToLineSegmentBetweenSq(endX, endY, startX, startY, pp.tan0x, pp.tan0y);
                if (Math.max(dist1, dist2) <= this.mInvResScaleSquared) {
                    this.log(1, pp, "max(dist1=%g, dist2=%g) <= mInvResScaleSquared", dist1, dist2);
                    return 1;
                } else {
                    this.log(0, pp, "(numerA=%g >= 0) == (numerB=%g >= 0)", numerA, numerB);
                    return 0;
                }
            } else {
                numerA /= denom;
                boolean validDivide = numerA > numerA - 1.0F;
                if (validDivide) {
                    if (computeControlPoint) {
                        pp.q1x = startX * (1.0F - numerA) + pp.tan0x * numerA;
                        pp.q1y = startY * (1.0F - numerA) + pp.tan0y * numerA;
                    }
                    this.log(2, pp, "(numerA=%g >= 0) != (numerB=%g >= 0)", numerA, numerB);
                    return 2;
                } else {
                    pp.opposite_tangents = Point.dotProduct(aLenX, aLenY, bLenX, bLenY) < 0.0F;
                    this.log(1, pp, "ApproxZero(denom=%g)", denom);
                    return 1;
                }
            }
        } else {
            pp.opposite_tangents = Point.dotProduct(aLenX, aLenY, bLenX, bLenY) < 0.0F;
            this.log(1, pp, "denom == 0");
            return 1;
        }
    }

    private void set_perpendicular_ray(float[] v) {
        if (!Point.setLength(v, 10, this.mRadius)) {
            v[10] = this.mRadius;
            v[11] = 0.0F;
        }
        float axis = (float) this.mStrokeType;
        v[12] = v[8] + axis * v[11];
        v[13] = v[9] - axis * v[10];
        v[14] = v[12] + v[10];
        v[15] = v[13] + v[11];
    }

    private void quad_perpendicular_ray(float[] quad, float t) {
        Geometry.evalQuadAt(quad, 0, t, quad, 8, quad, 10);
        if (quad[10] == 0.0F && quad[11] == 0.0F) {
            quad[10] = quad[4] - quad[0];
            quad[11] = quad[5] - quad[1];
        }
        this.set_perpendicular_ray(quad);
    }

    private void cubic_perpendicular_ray(float[] cubic, float t) {
        Geometry.evalCubicAt(cubic, 0, t, cubic, 8, cubic, 10);
        if (cubic[10] == 0.0F && cubic[11] == 0.0F) {
            int c = 0;
            if (MathUtil.isApproxZero(t)) {
                cubic[10] = cubic[4] - cubic[0];
                cubic[11] = cubic[5] - cubic[1];
            } else if (MathUtil.isApproxEqual(t, 1.0F)) {
                cubic[10] = cubic[6] - cubic[2];
                cubic[11] = cubic[7] - cubic[3];
            } else {
                Geometry.chopCubicAt(cubic, 0, cubic, 12, t);
                cubic[10] = cubic[18] - cubic[16];
                cubic[11] = cubic[19] - cubic[17];
                if (cubic[10] == 0.0F && cubic[11] == 0.0F) {
                    cubic[10] = cubic[18] - cubic[14];
                    cubic[11] = cubic[19] - cubic[15];
                    c = 12;
                }
            }
            if (cubic[10] == 0.0F && cubic[11] == 0.0F) {
                cubic[10] = cubic[c + 6] - cubic[c];
                cubic[11] = cubic[c + 7] - cubic[c + 1];
            }
        }
        this.set_perpendicular_ray(cubic);
    }

    private void quad_quad_ends(float[] quad, PathStroker.QuadState pp) {
        if (!pp.set0) {
            this.quad_perpendicular_ray(quad, pp.t_from);
            pp.q0x = quad[12];
            pp.q0y = quad[13];
            pp.tan0x = quad[14];
            pp.tan0y = quad[15];
            pp.set0 = true;
        }
        if (!pp.set2) {
            this.quad_perpendicular_ray(quad, pp.t_to);
            pp.q2x = quad[12];
            pp.q2y = quad[13];
            pp.tan2x = quad[14];
            pp.tan2y = quad[15];
            pp.set2 = true;
        }
    }

    private void cubic_quad_ends(float[] cubic, PathStroker.QuadState pp) {
        if (!pp.set0) {
            this.cubic_perpendicular_ray(cubic, pp.t_from);
            pp.q0x = cubic[12];
            pp.q0y = cubic[13];
            pp.tan0x = cubic[14];
            pp.tan0y = cubic[15];
            pp.set0 = true;
        }
        if (!pp.set2) {
            this.cubic_perpendicular_ray(cubic, pp.t_to);
            pp.q2x = cubic[12];
            pp.q2y = cubic[13];
            pp.tan2x = cubic[14];
            pp.tan2y = cubic[15];
            pp.set2 = true;
        }
    }

    private int check_quad_quad(float[] quad, PathStroker.QuadState pp) {
        this.quad_quad_ends(quad, pp);
        int result = this.intersect_ray(pp, true);
        if (result != 2) {
            return result;
        } else {
            this.quad_perpendicular_ray(quad, pp.t_mid);
            return this.check_close_enough(pp, quad[12], quad[13], quad[8], quad[9], quad);
        }
    }

    private int check_quad_cubic(float[] cubic, PathStroker.QuadState pp) {
        this.cubic_quad_ends(cubic, pp);
        int result = this.intersect_ray(pp, true);
        if (result != 2) {
            return result;
        } else {
            this.cubic_perpendicular_ray(cubic, pp.t_mid);
            return this.check_close_enough(pp, cubic[12], cubic[13], cubic[8], cubic[9], cubic);
        }
    }

    private int find_tangents(float[] cubic, PathStroker.QuadState pp) {
        this.cubic_quad_ends(cubic, pp);
        return this.intersect_ray(pp, false);
    }

    private static boolean sharp_angle(PathStroker.QuadState pp, float[] v) {
        float ax = pp.q1x - pp.q0x;
        float ay = pp.q1y - pp.q0y;
        float bx = pp.q1x - pp.q2x;
        float by = pp.q1y - pp.q2y;
        float aLen = Point.lengthSq(ax, ay);
        float bLen = Point.lengthSq(bx, by);
        if (aLen > bLen) {
            float t = ax;
            ax = bx;
            bx = t;
            t = ay;
            ay = by;
            by = t;
            bLen = aLen;
        }
        v[8] = ax;
        v[9] = ay;
        return Point.setLength(v, 8, bLen) ? Point.dotProduct(v[8], v[9], bx, by) > 0.0F : false;
    }

    private boolean quick_reject(PathStroker.QuadState pp, float x, float y) {
        float xMin = MathUtil.min(pp.q0x, pp.q1x, pp.q2x);
        if (x + this.mInvResScale < xMin) {
            return true;
        } else {
            float xMax = MathUtil.max(pp.q0x, pp.q1x, pp.q2x);
            if (x - this.mInvResScale > xMax) {
                return true;
            } else {
                float yMin = MathUtil.min(pp.q0y, pp.q1y, pp.q2y);
                if (y + this.mInvResScale < yMin) {
                    return true;
                } else {
                    float yMax = MathUtil.max(pp.q0y, pp.q1y, pp.q2y);
                    return y - this.mInvResScale > yMax;
                }
            }
        }
    }

    private int check_close_enough(PathStroker.QuadState pp, float ray0x, float ray0y, float ray1x, float ray1y, float[] v) {
        Geometry.evalQuadAt(pp.q0x, pp.q0y, pp.q1x, pp.q1y, pp.q2x, pp.q2y, 0.5F, v, 8);
        if (Point.distanceToSq(ray0x, ray0y, v[8], v[9]) <= this.mInvResScaleSquared) {
            if (sharp_angle(pp, v)) {
                this.log(0, pp, "sharp_angle (1) =%g,%g, %g,%g, %g,%g", pp.q0x, pp.q0y, pp.q1x, pp.q1y, pp.q2x, pp.q2y);
                return 0;
            } else {
                this.log(2, pp, "points_within_dist(ray[0]=%g,%g, strokeMid=%g,%g, mInvResScale=%g)", ray0x, ray0y, v[8], v[9], this.mInvResScale);
                return 2;
            }
        } else if (this.quick_reject(pp, ray0x, ray0y)) {
            this.log(0, pp, "!quick_reject(stroke=(%g,%g %g,%g %g,%g), ray[0]=%g,%g)", pp.q0x, pp.q0y, pp.q1x, pp.q1y, pp.q2x, pp.q2y, ray0x, ray0y);
            return 0;
        } else {
            float dx = ray1x - ray0x;
            float dy = ray1y - ray0y;
            float A = (pp.q2y - ray0y) * dx - (pp.q2x - ray0x) * dy;
            float B = (pp.q1y - ray0y) * dx - (pp.q1x - ray0x) * dy;
            float C = (pp.q0y - ray0y) * dx - (pp.q0x - ray0x) * dy;
            A += C - 2.0F * B;
            B -= C;
            int nRoots = Geometry.findUnitQuadRoots(A, 2.0F * B, C, v, 8);
            if (nRoots != 1) {
                this.log(0, pp, "nRoots=%d != 1", nRoots);
                return 0;
            } else {
                dx = v[8];
                Geometry.evalQuadAt(pp.q0x, pp.q0y, pp.q1x, pp.q1y, pp.q2x, pp.q2y, dx, v, 8);
                dy = this.mInvResScale * (1.0F - Math.abs(dx - 0.5F) * 2.0F);
                if (Point.distanceToSq(ray0x, ray0y, v[8], v[9]) <= dy * dy) {
                    if (sharp_angle(pp, v)) {
                        this.log(0, pp, "sharp_angle (2) =%g,%g, %g,%g, %g,%g", pp.q0x, pp.q0y, pp.q1x, pp.q1y, pp.q2x, pp.q2y);
                        return 0;
                    } else {
                        this.log(2, pp, "points_within_dist(ray[0]=%g,%g, quadPt=%g,%g, error=%g)", ray0x, ray0y, v[8], v[9], dy);
                        return 2;
                    }
                } else {
                    this.log(0, pp, "fallthrough");
                    return 0;
                }
            }
        }
    }

    private void emitDegenerateLine(PathStroker.QuadState pp) {
        PathConsumer path = (PathConsumer) (this.mStrokeType == 1 ? this.mOuter : this.mInner);
        path.lineTo(pp.q2x, pp.q2y);
    }

    private boolean strokeQuad(float[] quad, PathStroker.QuadState pp) {
        int result = this.check_quad_quad(quad, pp);
        if (result == 2) {
            PathConsumer path = (PathConsumer) (this.mStrokeType == 1 ? this.mOuter : this.mInner);
            path.quadTo(pp.q1x, pp.q1y, pp.q2x, pp.q2y);
            return true;
        } else if (result == 1) {
            this.emitDegenerateLine(pp);
            return true;
        } else if (++this.mRecursionDepth > 33) {
            return false;
        } else {
            PathStroker.QuadState mid = this.mQuadStack[this.mRecursionDepth];
            mid.init0(pp);
            if (!this.strokeQuad(quad, mid)) {
                return false;
            } else {
                mid.init2(pp);
                if (!this.strokeQuad(quad, mid)) {
                    return false;
                } else {
                    this.mRecursionDepth--;
                    return true;
                }
            }
        }
    }

    @Override
    public void quadTo(float x1, float y1, float x2, float y2) {
        boolean degenerateAB = Point.isDegenerate(x1 - this.mPrevX, y1 - this.mPrevY);
        boolean degenerateBC = Point.isDegenerate(x2 - x1, y2 - y1);
        if (degenerateAB & degenerateBC) {
            this.lineTo(x2, y2);
        } else if (degenerateAB | degenerateBC) {
            this.lineTo(x2, y2);
        } else {
            float[] quad = this.getQuad(x1, y1, x2, y2);
            if (quad_in_line(quad)) {
                float t = Geometry.findQuadMaxCurvature(quad, 0);
                if (!(t <= 0.0F) && !(t >= 1.0F)) {
                    Geometry.evalQuadAt(quad, 0, quad, 8, t);
                    this.lineTo(quad[8], quad[9]);
                    PathStroker.Joiner saveJoiner = this.mJoiner;
                    this.mJoiner = PathStroker.Joiner.get(16);
                    this.lineTo(x2, y2);
                    this.mJoiner = saveJoiner;
                } else {
                    this.lineTo(x2, y2);
                }
            } else {
                if (this.preJoinTo(x1, y1, false)) {
                    assert this.mRecursionDepth == 0;
                    PathStroker.QuadState pp = this.mQuadStack[0];
                    this.initStroke(1, pp, 0.0F, 1.0F);
                    this.strokeQuad(quad, pp);
                    this.initStroke(-1, pp, 0.0F, 1.0F);
                    this.strokeQuad(quad, pp);
                    assert this.mRecursionDepth == 0;
                    quad[8] = (x2 - x1) * this.mResScale;
                    quad[9] = (y2 - y1) * this.mResScale;
                    if (Point.normalize(quad, 8)) {
                        float newX = quad[9];
                        float newY = -quad[8];
                        this.mNormal[0] = newX * this.mRadius;
                        this.mNormal[1] = newY * this.mRadius;
                        this.mNormal[2] = newX;
                        this.mNormal[3] = newY;
                    }
                    this.postJoinTo(x2, y2);
                } else {
                    this.lineTo(x2, y2);
                }
            }
        }
    }

    private boolean strokeCubic(float[] cubic, PathStroker.QuadState pp) {
        if (!this.mFoundTangents) {
            int result = this.find_tangents(cubic, pp);
            if (result != 2) {
                if (result == 1 || Point.distanceToSq(pp.q0x, pp.q0y, pp.q2x, pp.q2y) <= this.mInvResScaleSquared) {
                    this.cubic_perpendicular_ray(cubic, pp.t_mid);
                    if (Point.distanceToLineSegmentBetweenSq(cubic[12], cubic[13], pp.q0x, pp.q0y, pp.q2x, pp.q2y) <= this.mInvResScaleSquared) {
                        this.emitDegenerateLine(pp);
                        return true;
                    }
                }
            } else {
                this.mFoundTangents = true;
            }
        }
        if (this.mFoundTangents) {
            int result = this.check_quad_cubic(cubic, pp);
            if (result == 2) {
                PathConsumer path = (PathConsumer) (this.mStrokeType == 1 ? this.mOuter : this.mInner);
                path.quadTo(pp.q1x, pp.q1y, pp.q2x, pp.q2y);
                return true;
            }
            if (result == 1 && !pp.opposite_tangents) {
                this.emitDegenerateLine(pp);
                return true;
            }
        }
        if (Float.isFinite(pp.q2x) && Float.isFinite(pp.q2y)) {
            if (++this.mRecursionDepth > (this.mFoundTangents ? 24 : 15)) {
                return false;
            } else {
                PathStroker.QuadState mid = this.mQuadStack[this.mRecursionDepth];
                if (!mid.init0(pp)) {
                    this.emitDegenerateLine(pp);
                    this.mRecursionDepth--;
                    return true;
                } else if (!this.strokeCubic(cubic, mid)) {
                    return false;
                } else if (!mid.init2(pp)) {
                    this.emitDegenerateLine(pp);
                    this.mRecursionDepth--;
                    return true;
                } else if (!this.strokeCubic(cubic, mid)) {
                    return false;
                } else {
                    this.mRecursionDepth--;
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        boolean degenerateAB = Point.isDegenerate(x1 - this.mPrevX, y1 - this.mPrevY);
        boolean degenerateBC = Point.isDegenerate(x2 - x1, y2 - y1);
        boolean degenerateCD = Point.isDegenerate(x3 - x2, y3 - y2);
        if (degenerateAB & degenerateBC & degenerateCD) {
            this.lineTo(x3, y3);
        } else if ((degenerateAB ? 1 : 0) + (degenerateBC ? 1 : 0) + (degenerateCD ? 1 : 0) == 2) {
            this.lineTo(x3, y3);
        } else {
            float[] cubic = this.getCubic(x1, y1, x2, y2, x3, y3);
            if (cubic_in_line(cubic)) {
                int count = Geometry.findCubicMaxCurvature(cubic, 0, cubic, 8);
                boolean any = false;
                PathStroker.Joiner saveJoiner = this.mJoiner;
                for (int index = 0; index < count; index++) {
                    float t = cubic[8 + index];
                    if (!(t <= 0.0F) && !(t >= 1.0F)) {
                        Geometry.evalCubicAt(cubic, 0, cubic, 12, t);
                        float evalX = cubic[12];
                        float evalY = cubic[13];
                        if (evalX != cubic[0] && evalY != cubic[1] && evalX != cubic[6] && evalY != cubic[7]) {
                            this.lineTo(evalX, evalY);
                            if (!any) {
                                this.mJoiner = PathStroker.Joiner.get(16);
                                any = true;
                            }
                        }
                    }
                }
                this.lineTo(x3, y3);
                if (any) {
                    this.mJoiner = saveJoiner;
                }
            } else {
                float tangentX;
                float tangentY;
                if (degenerateAB) {
                    tangentX = x2;
                    tangentY = y2;
                } else {
                    tangentX = x1;
                    tangentY = y1;
                }
                if (this.preJoinTo(tangentX, tangentY, false)) {
                    int infCount = Geometry.findCubicInflectionPoints(cubic, 0, cubic, 8);
                    float t0 = cubic[8];
                    float t1 = cubic[9];
                    float lastT = 0.0F;
                    for (int indexx = 0; indexx <= infCount; indexx++) {
                        float nextT;
                        if (indexx < infCount) {
                            assert indexx == 0 || indexx == 1;
                            if (indexx == 0) {
                                nextT = t0;
                            } else {
                                nextT = t1;
                            }
                        } else {
                            nextT = 1.0F;
                        }
                        assert this.mRecursionDepth == 0;
                        PathStroker.QuadState pp = this.mQuadStack[0];
                        this.initStroke(1, pp, lastT, nextT);
                        this.strokeCubic(cubic, pp);
                        this.initStroke(-1, pp, lastT, nextT);
                        this.strokeCubic(cubic, pp);
                        assert this.mRecursionDepth == 0;
                        lastT = nextT;
                    }
                    float cusp = Geometry.findCubicCusp(cubic, 0);
                    if (cusp > 0.0F) {
                    }
                    if (degenerateAB) {
                        degenerateAB = Point.isDegenerate(x2 - this.mPrevX, y2 - this.mPrevY);
                    }
                    if (degenerateCD) {
                        degenerateCD = Point.isDegenerate(x3 - x1, y3 - y1);
                        cubic[8] = (x3 - x1) * this.mResScale;
                        cubic[9] = (y3 - y1) * this.mResScale;
                    } else {
                        cubic[8] = (x3 - x2) * this.mResScale;
                        cubic[9] = (y3 - y2) * this.mResScale;
                    }
                    if (!degenerateAB && !degenerateCD && Point.normalize(cubic, 8)) {
                        float newX = cubic[9];
                        float newY = -cubic[8];
                        this.mNormal[0] = newX * this.mRadius;
                        this.mNormal[1] = newY * this.mRadius;
                        this.mNormal[2] = newX;
                        this.mNormal[3] = newY;
                    }
                    this.postJoinTo(x3, y3);
                } else {
                    this.lineTo(x3, y3);
                }
            }
        }
    }

    @Override
    public void closePath() {
        this.finish(true, this.mPrevIsLine);
    }

    @Override
    public void pathDone() {
        this.finish(false, this.mPrevIsLine);
        this.mOuter = null;
        assert this.mInner.isEmpty();
    }

    private void finish(boolean close, boolean isLine) {
        if (this.mSegmentCount > 0 && !close) {
            this.mCapper.cap(this.mOuter, this.mPrevX, this.mPrevY, this.mPrevNormalX, this.mPrevNormalY);
            this.mInner.reversePop(this.mOuter);
            this.mCapper.cap(this.mOuter, this.mFirstX, this.mFirstY, -this.mFirstNormalX, -this.mFirstNormalY);
            this.mOuter.closePath();
        }
        this.mSegmentCount = -1;
    }

    public interface Capper {

        float C = 0.55191505F;

        void cap(PathConsumer var1, float var2, float var3, float var4, float var5);

        static PathStroker.Capper get(int cap) {
            return switch(cap) {
                case 0 ->
                    PathStroker.Capper::doButtCap;
                case 4 ->
                    PathStroker.Capper::doRoundCap;
                case 8 ->
                    PathStroker.Capper::doSquareCap;
                default ->
                    throw new AssertionError(cap);
            };
        }

        static void doButtCap(PathConsumer path, float pivotX, float pivotY, float normalX, float normalY) {
            path.lineTo(pivotX - normalX, pivotY - normalY);
        }

        static void doRoundCap(PathConsumer path, float pivotX, float pivotY, float normalX, float normalY) {
            float Cmx = 0.55191505F * normalX;
            float Cmy = 0.55191505F * normalY;
            path.cubicTo(pivotX + normalX - Cmy, pivotY + normalY + Cmx, pivotX - normalY + Cmx, pivotY + normalX + Cmy, pivotX - normalY, pivotY + normalX);
            path.cubicTo(pivotX - normalY - Cmx, pivotY + normalX - Cmy, pivotX - normalX - Cmy, pivotY - normalY + Cmx, pivotX - normalX, pivotY - normalY);
        }

        static void doSquareCap(PathConsumer path, float pivotX, float pivotY, float normalX, float normalY) {
            path.lineTo(pivotX + normalX - normalY, pivotY + normalY + normalX);
            path.lineTo(pivotX - normalX - normalY, pivotY - normalY + normalX);
            path.lineTo(pivotX - normalX, pivotY - normalY);
        }
    }

    public interface Joiner {

        int ANGLE_NEARLY_0 = 0;

        int ANGLE_ACUTE = 1;

        int ANGLE_NEARLY_180 = 2;

        int ANGLE_OBTUSE = 3;

        int ANGLE_NEARLY_90 = 4;

        void join(PathConsumer var1, PathConsumer var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, boolean var11, boolean var12);

        static PathStroker.Joiner get(int join) {
            return switch(join) {
                case 0 ->
                    PathStroker.Joiner::doMiterJoin;
                case 16 ->
                    PathStroker.Joiner::doRoundJoin;
                case 32 ->
                    PathStroker.Joiner::doBevelJoin;
                default ->
                    throw new AssertionError(join);
            };
        }

        @Contract(pure = true)
        static boolean isCCW(float beforeX, float beforeY, float afterX, float afterY) {
            return Point.crossProduct(beforeX, beforeY, afterX, afterY) <= 0.0F;
        }

        static void doMiterJoin(PathConsumer outer, PathConsumer inner, float beforeUnitNormalX, float beforeUnitNormalY, float pivotX, float pivotY, float afterUnitNormalX, float afterUnitNormalY, float radius, float invMiterLimit, boolean prevIsLine, boolean currIsLine) {
            float dot = Point.dotProduct(beforeUnitNormalX, beforeUnitNormalY, afterUnitNormalX, afterUnitNormalY);
            int angleType;
            if (dot >= 0.0F) {
                angleType = dot >= 0.99999F ? 0 : 1;
            } else {
                angleType = dot <= -0.99999F ? 2 : 3;
            }
            if (angleType != 0) {
                if (angleType == 2) {
                    currIsLine = false;
                } else {
                    boolean doMiter = true;
                    float midX = 0.0F;
                    float midY = 0.0F;
                    boolean ccw = isCCW(beforeUnitNormalX, beforeUnitNormalY, afterUnitNormalX, afterUnitNormalY);
                    if (0.0F == dot && invMiterLimit <= 0.70710677F) {
                        midX = (beforeUnitNormalX + afterUnitNormalX) * radius;
                        midY = (beforeUnitNormalY + afterUnitNormalY) * radius;
                    } else {
                        float sinHalfAngle = (float) Math.sqrt((double) (1.0F + dot) * 0.5);
                        if (sinHalfAngle < invMiterLimit) {
                            currIsLine = false;
                            doMiter = false;
                        } else {
                            if (angleType == 3) {
                                if (ccw) {
                                    midX = beforeUnitNormalY - afterUnitNormalY;
                                    midY = afterUnitNormalX - beforeUnitNormalX;
                                } else {
                                    midX = afterUnitNormalY - beforeUnitNormalY;
                                    midY = beforeUnitNormalX - afterUnitNormalX;
                                }
                            } else {
                                midX = beforeUnitNormalX + afterUnitNormalX;
                                midY = beforeUnitNormalY + afterUnitNormalY;
                            }
                            double dmag = Math.sqrt((double) midX * (double) midX + (double) midY * (double) midY);
                            double dscale = (double) (radius / sinHalfAngle) / dmag;
                            midX = (float) ((double) midX * dscale);
                            midY = (float) ((double) midY * dscale);
                        }
                    }
                    if (doMiter) {
                        outer.lineTo(pivotX + midX, pivotY + midY);
                    }
                }
                float afterX = afterUnitNormalX * radius;
                float afterY = afterUnitNormalY * radius;
                if (!currIsLine) {
                    outer.lineTo(pivotX + afterX, pivotY + afterY);
                }
                inner.lineTo(pivotX - afterX, pivotY - afterY);
            }
        }

        static void doRoundJoin(PathConsumer outer, PathConsumer inner, float beforeUnitNormalX, float beforeUnitNormalY, float pivotX, float pivotY, float afterUnitNormalX, float afterUnitNormalY, float radius, float invMiterLimit, boolean prevIsLine, boolean currIsLine) {
            float dot = Point.dotProduct(beforeUnitNormalX, beforeUnitNormalY, afterUnitNormalX, afterUnitNormalY);
            int angleType;
            if (-1.0E-5F <= dot && dot <= 1.0E-5F) {
                angleType = 4;
            } else if (dot >= 0.0F) {
                angleType = dot >= 0.99999F ? 0 : 1;
            } else {
                angleType = dot <= -0.99999F ? 2 : 3;
            }
            if (angleType != 0) {
                boolean ccw = isCCW(beforeUnitNormalX, beforeUnitNormalY, afterUnitNormalX, afterUnitNormalY);
                if (ccw) {
                    PathConsumer tmp = outer;
                    outer = inner;
                    inner = tmp;
                    beforeUnitNormalX = -beforeUnitNormalX;
                    beforeUnitNormalY = -beforeUnitNormalY;
                    afterUnitNormalX = -afterUnitNormalX;
                    afterUnitNormalY = -afterUnitNormalY;
                }
                float afterY;
                float afterX;
                if (angleType == 1) {
                    doBezierApproxForArc(outer, beforeUnitNormalX, beforeUnitNormalY, pivotX, pivotY, afterUnitNormalX, afterUnitNormalY, radius, ccw);
                    afterX = afterUnitNormalX * radius;
                    afterY = afterUnitNormalY * radius;
                } else if (angleType == 4) {
                    afterX = afterUnitNormalX * radius;
                    afterY = afterUnitNormalY * radius;
                    doBezierApproxForArc(outer, beforeUnitNormalX * radius, beforeUnitNormalY * radius, pivotX, pivotY, afterX, afterY, ccw ? -0.55191505F : 0.55191505F);
                } else {
                    float unitNormalX;
                    float unitNormalY;
                    if (ccw) {
                        unitNormalX = beforeUnitNormalY - afterUnitNormalY;
                        unitNormalY = afterUnitNormalX - beforeUnitNormalX;
                    } else {
                        unitNormalX = afterUnitNormalY - beforeUnitNormalY;
                        unitNormalY = beforeUnitNormalX - afterUnitNormalX;
                    }
                    double dmag = Math.sqrt((double) unitNormalX * (double) unitNormalX + (double) unitNormalY * (double) unitNormalY);
                    double dscale = 1.0 / dmag;
                    unitNormalX = (float) ((double) unitNormalX * dscale);
                    unitNormalY = (float) ((double) unitNormalY * dscale);
                    if (angleType == 3) {
                        doBezierApproxForArc(outer, beforeUnitNormalX, beforeUnitNormalY, pivotX, pivotY, unitNormalX, unitNormalY, radius, ccw);
                        doBezierApproxForArc(outer, unitNormalX, unitNormalY, pivotX, pivotY, afterUnitNormalX, afterUnitNormalY, radius, ccw);
                        afterX = afterUnitNormalX * radius;
                        afterY = afterUnitNormalY * radius;
                    } else {
                        float normalX = unitNormalX * radius;
                        float normalY = unitNormalY * radius;
                        afterX = afterUnitNormalX * radius;
                        afterY = afterUnitNormalY * radius;
                        doBezierApproxForArc(outer, beforeUnitNormalX * radius, beforeUnitNormalY * radius, pivotX, pivotY, normalX, normalY, ccw ? -0.55191505F : 0.55191505F);
                        doBezierApproxForArc(outer, normalX, normalY, pivotX, pivotY, afterX, afterY, ccw ? -0.55191505F : 0.55191505F);
                    }
                }
                inner.lineTo(pivotX - afterX, pivotY - afterY);
            }
        }

        static void doBezierApproxForArc(PathConsumer path, float beforeUnitNormalX, float beforeUnitNormalY, float pivotX, float pivotY, float afterUnitNormalX, float afterUnitNormalY, float radius, boolean ccw) {
            float halfCosAngle = Point.dotProduct(beforeUnitNormalX, beforeUnitNormalY, afterUnitNormalX, afterUnitNormalY) * 0.5F;
            float C = (float) (1.3333333333333333 * Math.sqrt(0.5 - (double) halfCosAngle) / (1.0 + Math.sqrt(0.5 + (double) halfCosAngle)));
            doBezierApproxForArc(path, beforeUnitNormalX * radius, beforeUnitNormalY * radius, pivotX, pivotY, afterUnitNormalX * radius, afterUnitNormalY * radius, ccw ? -C : C);
        }

        static void doBezierApproxForArc(PathConsumer path, float beforeX, float beforeY, float pivotX, float pivotY, float afterX, float afterY, float k) {
            float x0 = pivotX + beforeX;
            float y0 = pivotY + beforeY;
            float x1 = x0 - k * beforeY;
            float y1 = y0 + k * beforeX;
            float x3 = pivotX + afterX;
            float y3 = pivotY + afterY;
            float x2 = x3 + k * afterY;
            float y2 = y3 - k * afterX;
            path.cubicTo(x1, y1, x2, y2, x3, y3);
        }

        static void doBevelJoin(PathConsumer outer, PathConsumer inner, float beforeUnitNormalX, float beforeUnitNormalY, float pivotX, float pivotY, float afterUnitNormalX, float afterUnitNormalY, float radius, float invMiterLimit, boolean prevIsLine, boolean currIsLine) {
            float afterX = afterUnitNormalX * radius;
            float afterY = afterUnitNormalY * radius;
            outer.lineTo(pivotX + afterX, pivotY + afterY);
            inner.lineTo(pivotX - afterX, pivotY - afterY);
        }
    }

    static class QuadState {

        float q0x;

        float q0y;

        float q1x;

        float q1y;

        float q2x;

        float q2y;

        float tan0x;

        float tan0y;

        float tan2x;

        float tan2y;

        float t_from;

        float t_mid;

        float t_to;

        boolean set0;

        boolean set2;

        boolean opposite_tangents;

        boolean init(float from, float to) {
            this.t_from = from;
            this.t_mid = (from + to) * 0.5F;
            this.t_to = to;
            this.set0 = this.set2 = false;
            return from < this.t_mid && this.t_mid < to;
        }

        boolean init0(PathStroker.QuadState pp) {
            if (!this.init(pp.t_from, pp.t_mid)) {
                return false;
            } else {
                this.q0x = pp.q0x;
                this.q0y = pp.q0y;
                this.tan0x = pp.tan0x;
                this.tan0y = pp.tan0y;
                this.set0 = true;
                return true;
            }
        }

        boolean init2(PathStroker.QuadState pp) {
            if (!this.init(pp.t_mid, pp.t_to)) {
                return false;
            } else {
                this.q2x = pp.q2x;
                this.q2y = pp.q2y;
                this.tan2x = pp.tan2x;
                this.tan2y = pp.tan2y;
                this.set2 = true;
                return true;
            }
        }
    }
}