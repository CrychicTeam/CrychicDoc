package icyllis.arc3d.core;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

public class PathMeasure {

    public static final int MATRIX_FLAG_GET_POSITION = 1;

    public static final int MATRIX_FLAG_GET_TANGENT = 2;

    public static final int MATRIX_FLAG_GET_POS_AND_TAN = 3;

    private final Path mPath = new Path();

    private float mTolerance;

    private boolean mForceClose;

    private Path.RawIterator mIterator;

    private static final int SEGMENT_COLUMNS = 3;

    private static final int SEGMENT_DISTANCE = 0;

    private static final int SEGMENT_COORD_INDEX = 1;

    private static final int SEGMENT_T_AND_TYPE = 2;

    private static final int SEGMENT_TYPE_SHIFT = 30;

    private static final int SEGMENT_T_MASK = 1073741823;

    private static final int MAX_T_VALUE = 1073741823;

    private static final int SEGMENT_LINE = 0;

    private static final int SEGMENT_QUAD = 1;

    private static final int SEGMENT_CUBIC = 3;

    private final IntArrayList mSegments = new IntArrayList();

    private final FloatArrayList mCoords = new FloatArrayList();

    private float mContourLength;

    private boolean mContourClosed;

    protected final float[] mTmp = new float[28];

    public PathMeasure() {
    }

    public PathMeasure(Path path, boolean forceClose) {
        this.reset(path, forceClose);
    }

    public PathMeasure(Path path, boolean forceClose, float resScale) {
        this.reset(path, forceClose, resScale);
    }

    public void reset() {
        this.mPath.recycle();
        this.mIterator = null;
        this.mSegments.clear();
    }

    public boolean reset(Path path, boolean forceClose) {
        return this.reset(path, forceClose, 1.0F);
    }

    public boolean reset(Path path, boolean forceClose, float resScale) {
        if (path != null && path.isFinite()) {
            this.mPath.set(path);
            this.mTolerance = 0.5F / resScale;
            this.mForceClose = forceClose;
            this.mIterator = this.mPath.new RawIterator();
            return this.nextContour();
        } else {
            this.reset();
            return false;
        }
    }

    public boolean nextContour() {
        if (this.mIterator == null) {
            return false;
        } else {
            while (this.mIterator.hasNext()) {
                if (this.computeSegments()) {
                    return true;
                }
            }
            this.reset();
            return false;
        }
    }

    public boolean hasContour() {
        return !this.mSegments.isEmpty();
    }

    public float getContourLength() {
        return this.hasContour() ? this.mContourLength : 0.0F;
    }

    public boolean isContourClosed() {
        return this.hasContour() && this.mContourClosed;
    }

    @CheckReturnValue
    public boolean getPosTan(float distance, @Nullable float[] position, int positionOff, @Nullable float[] tangent, int tangentOff) {
        if (!this.hasContour()) {
            return false;
        } else if (Float.isNaN(distance)) {
            return false;
        } else {
            float length = this.mContourLength;
            assert length > 0.0F;
            distance = MathUtil.clamp(distance, 0.0F, length);
            int segIndex = this.distanceToSegment(distance);
            float t = this.mTmp[0];
            if (!Float.isFinite(t)) {
                return false;
            } else {
                this.computePosAndTan(segIndex, t, position, positionOff, tangent, tangentOff);
                return true;
            }
        }
    }

    @CheckReturnValue
    public boolean getMatrix(float distance, @Nullable Matrix matrix, int flags) {
        if (!this.getPosTan(distance, (flags & 1) != 0 ? this.mTmp : null, 0, (flags & 2) != 0 ? this.mTmp : null, 2)) {
            return false;
        } else {
            if (matrix != null) {
                if ((flags & 2) != 0 && Point.normalize(this.mTmp, 2)) {
                    matrix.setSinCos(this.mTmp[3], this.mTmp[2]);
                } else {
                    matrix.setIdentity();
                }
                if ((flags & 1) != 0) {
                    matrix.postTranslate(this.mTmp[0], this.mTmp[1]);
                }
            }
            return true;
        }
    }

    @CheckReturnValue
    public boolean getSegment(float startDistance, float endDistance, PathConsumer dst, boolean startWithMoveTo) {
        if (!this.hasContour()) {
            return false;
        } else {
            float length = this.mContourLength;
            assert length > 0.0F;
            if (startDistance < 0.0F) {
                startDistance = 0.0F;
            }
            if (endDistance > length) {
                endDistance = length;
            }
            if (!(startDistance <= endDistance)) {
                return false;
            } else {
                assert !this.mSegments.isEmpty();
                int segIndex = this.distanceToSegment(startDistance);
                float startT = this.mTmp[0];
                if (!Float.isFinite(startT)) {
                    return false;
                } else {
                    int endSegIndex = this.distanceToSegment(endDistance);
                    float endT = this.mTmp[0];
                    if (!Float.isFinite(endT)) {
                        return false;
                    } else {
                        assert segIndex <= endSegIndex;
                        if (startWithMoveTo) {
                            this.computePosAndTan(segIndex, startT, this.mTmp, 0, null, 0);
                            dst.moveTo(this.mTmp[0], this.mTmp[1]);
                        }
                        int[] segments = this.mSegments.elements();
                        int endCoordIndex = getSegmentCoordIndex(segments, endSegIndex);
                        if (getSegmentCoordIndex(segments, segIndex) == endCoordIndex) {
                            this.segmentTo(segIndex, startT, endT, dst);
                        } else {
                            do {
                                this.segmentTo(segIndex, startT, 1.0F, dst);
                                int coordIndex = getSegmentCoordIndex(segments, segIndex);
                                while (getSegmentCoordIndex(segments, ++segIndex) == coordIndex) {
                                }
                                startT = 0.0F;
                            } while (getSegmentCoordIndex(segments, segIndex) < endCoordIndex);
                            this.segmentTo(segIndex, 0.0F, endT, dst);
                        }
                        return true;
                    }
                }
            }
        }
    }

    public float getTolerance() {
        return this.mTolerance;
    }

    private boolean computeSegments() {
        float distance = 0.0F;
        boolean hasClose = this.mForceClose;
        boolean hasMoveTo = false;
        this.mSegments.clear();
        FloatArrayList coords = this.mCoords;
        coords.clear();
        int coordIndex = -2;
        Path.RawIterator iter = this.mIterator;
        byte verb;
        while ((verb = iter.next()) != 6 && (!hasMoveTo || verb != false)) {
            switch(verb) {
                case 0:
                    coordIndex += 2;
                    coords.add(iter.x0());
                    coords.add(iter.y0());
                    hasMoveTo = true;
                    break;
                case 1:
                    assert hasMoveTo;
                    float prevD = distance;
                    distance = this.compute_line_segment(iter.x0(), iter.y0(), iter.x1(), iter.y1(), distance, coordIndex);
                    if (distance > prevD) {
                        coords.add(iter.x1());
                        coords.add(iter.y1());
                        coordIndex += 2;
                    }
                    break;
                case 2:
                    assert hasMoveTo;
                    float prevD = distance;
                    distance = this.compute_quad_segments(iter.x0(), iter.y0(), iter.x1(), iter.y1(), iter.x2(), iter.y2(), distance, 0, 1073741823, coordIndex);
                    if (distance > prevD) {
                        coords.add(iter.x1());
                        coords.add(iter.y1());
                        coords.add(iter.x2());
                        coords.add(iter.y2());
                        coordIndex += 4;
                    }
                case 3:
                default:
                    break;
                case 4:
                    assert hasMoveTo;
                    float prevD = distance;
                    distance = this.compute_cubic_segments(iter.x0(), iter.y0(), iter.x1(), iter.y1(), iter.x2(), iter.y2(), iter.x3(), iter.y3(), distance, 0, 1073741823, coordIndex);
                    if (distance > prevD) {
                        coords.add(iter.x1());
                        coords.add(iter.y1());
                        coords.add(iter.x2());
                        coords.add(iter.y2());
                        coords.add(iter.x3());
                        coords.add(iter.y3());
                        coordIndex += 6;
                    }
                    break;
                case 5:
                    hasClose = true;
            }
        }
        if (!Float.isFinite(distance)) {
            return false;
        } else if (this.mSegments.isEmpty()) {
            return false;
        } else {
            if (hasClose) {
                float prevD = distance;
                float firstX = coords.getFloat(0);
                float firstY = coords.getFloat(1);
                distance = this.compute_line_segment(coords.getFloat(coordIndex), coords.getFloat(coordIndex | 1), firstX, firstY, distance, coordIndex);
                if (distance > prevD) {
                    coords.add(firstX);
                    coords.add(firstY);
                }
            }
            this.mContourLength = distance;
            this.mContourClosed = hasClose;
            return true;
        }
    }

    private float compute_line_segment(float x0, float y0, float x1, float y1, float distance, int coordIndex) {
        float d = Point.distanceTo(x0, y0, x1, y1);
        distance += d;
        if (distance > distance) {
            this.mSegments.add(Float.floatToIntBits(distance));
            this.mSegments.add(coordIndex);
            this.mSegments.add(1073741823);
        }
        return distance;
    }

    private static boolean large_t_span(int tSpan) {
        return tSpan >> 10 != 0;
    }

    private static boolean check_quad(float x0, float y0, float x1, float y1, float x2, float y2, float tolerance) {
        float dx = 0.5F * x1 - 0.5F * 0.5F * (x0 + x2);
        float dy = 0.5F * y1 - 0.5F * 0.5F * (y0 + y2);
        float dist = Math.max(Math.abs(dx), Math.abs(dy));
        return dist > tolerance;
    }

    private float compute_quad_segments(float x0, float y0, float x1, float y1, float x2, float y2, float distance, int tMin, int tMax, int coordIndex) {
        if (large_t_span(tMax - tMin) && check_quad(x0, y0, x1, y1, x2, y2, this.mTolerance)) {
            int tMid = tMin + tMax >>> 1;
            float abx = x0 * 0.5F + x1 * 0.5F;
            float aby = y0 * 0.5F + y1 * 0.5F;
            float bcx = x1 * 0.5F + x2 * 0.5F;
            float bcy = y1 * 0.5F + y2 * 0.5F;
            float abcx = abx * 0.5F + bcx * 0.5F;
            float abcy = aby * 0.5F + bcy * 0.5F;
            distance = this.compute_quad_segments(x0, y0, abx, aby, abcx, abcy, distance, tMin, tMid, coordIndex);
            distance = this.compute_quad_segments(abcx, abcy, bcx, bcy, x2, y2, distance, tMid, tMax, coordIndex);
        } else {
            float d = Point.distanceTo(x0, y0, x2, y2);
            distance += d;
            if (distance > distance) {
                this.mSegments.add(Float.floatToIntBits(distance));
                this.mSegments.add(coordIndex);
                this.mSegments.add(tMax | 1073741824);
            }
        }
        return distance;
    }

    private static boolean check_cubic(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, float tolerance) {
        float x = MathUtil.lerp(x0, x3, 0.33333334F);
        float y = MathUtil.lerp(y0, y3, 0.33333334F);
        float dist = Math.max(Math.abs(x - x1), Math.abs(y - y1));
        if (dist > tolerance) {
            return true;
        } else {
            x = MathUtil.lerp(x0, x3, 0.6666667F);
            y = MathUtil.lerp(y0, y3, 0.6666667F);
            dist = Math.max(Math.abs(x - x2), Math.abs(y - y2));
            return dist > tolerance;
        }
    }

    private float compute_cubic_segments(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, float distance, int tMin, int tMax, int coordIndex) {
        if (large_t_span(tMax - tMin) && check_cubic(x0, y0, x1, y1, x2, y2, x3, y3, this.mTolerance)) {
            int tMid = tMin + tMax >>> 1;
            float abx = x0 * 0.5F + x1 * 0.5F;
            float aby = y0 * 0.5F + y1 * 0.5F;
            float bcx = x1 * 0.5F + x2 * 0.5F;
            float bcy = y1 * 0.5F + y2 * 0.5F;
            float cdx = x2 * 0.5F + x3 * 0.5F;
            float cdy = y2 * 0.5F + y3 * 0.5F;
            float abcx = abx * 0.5F + bcx * 0.5F;
            float abcy = aby * 0.5F + bcy * 0.5F;
            float bcdx = bcx * 0.5F + cdx * 0.5F;
            float bcdy = bcy * 0.5F + cdy * 0.5F;
            float abcdx = abcx * 0.5F + bcdx * 0.5F;
            float abcdy = abcy * 0.5F + bcdy * 0.5F;
            distance = this.compute_cubic_segments(x0, y0, abx, aby, abcx, abcy, abcdx, abcdy, distance, tMin, tMid, coordIndex);
            distance = this.compute_cubic_segments(abcdx, abcdy, bcdx, bcdy, cdx, cdy, x3, y3, distance, tMid, tMax, coordIndex);
        } else {
            float d = Point.distanceTo(x0, y0, x3, y3);
            distance += d;
            if (distance > distance) {
                this.mSegments.add(Float.floatToIntBits(distance));
                this.mSegments.add(coordIndex);
                this.mSegments.add(tMax | -1073741824);
            }
        }
        return distance;
    }

    private static float getSegmentDistance(int[] segments, int segIndex) {
        int d = segments[segIndex * 3 + 0];
        return Float.intBitsToFloat(d);
    }

    private static int getSegmentCoordIndex(int[] segments, int segIndex) {
        return segments[segIndex * 3 + 1];
    }

    private static float getSegmentT(int[] segments, int segIndex) {
        int t = segments[segIndex * 3 + 2];
        return (float) (t & 1073741823) * 9.313226E-10F;
    }

    private static int getSegmentType(int[] segments, int segIndex) {
        int t = segments[segIndex * 3 + 2];
        return t >>> 30;
    }

    private void computePosAndTan(int segIndex, float t, float[] pos, int posOff, float[] tangent, int tangentOff) {
        int ci = getSegmentCoordIndex(this.mSegments.elements(), segIndex);
        float[] pts = this.mCoords.elements();
        int segType = getSegmentType(this.mSegments.elements(), segIndex);
        switch(segType) {
            case 0:
                if (pos != null) {
                    pos[posOff] = MathUtil.lerp(pts[ci], pts[ci + 2], t);
                    pos[posOff + 1] = MathUtil.lerp(pts[ci + 1], pts[ci + 3], t);
                }
                if (tangent != null) {
                    tangent[tangentOff] = pts[ci + 2] - pts[ci];
                    tangent[tangentOff + 1] = pts[ci + 3] - pts[ci + 1];
                }
                break;
            case 1:
                Geometry.evalQuadAt(pts, ci, t, pos, posOff, tangent, tangentOff);
                break;
            case 2:
            default:
                assert false;
                break;
            case 3:
                Geometry.evalCubicAt(pts, ci, t, pos, posOff, tangent, tangentOff);
        }
    }

    private int distanceToSegment(float distance) {
        assert distance >= 0.0F && distance <= this.mContourLength;
        int index = 0;
        int[] segments = this.mSegments.elements();
        if (!this.mSegments.isEmpty()) {
            assert this.mSegments.size() % 3 == 0;
            int low = 0;
            int high = this.mSegments.size() / 3 - 1;
            while (low < high) {
                int mid = low + high >>> 1;
                if (getSegmentDistance(segments, mid) < distance) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            if (getSegmentDistance(segments, high) < distance) {
                index = high + 1;
            } else {
                index = high;
            }
        }
        float startT = 0.0F;
        float startD = 0.0F;
        if (index > 0) {
            startD = getSegmentDistance(segments, index - 1);
            if (getSegmentCoordIndex(segments, index) == getSegmentCoordIndex(segments, index - 1)) {
                startT = getSegmentT(segments, index - 1);
            }
        }
        assert getSegmentT(segments, index) > startT;
        assert distance >= startD;
        assert getSegmentDistance(segments, index) > startD;
        this.mTmp[0] = startT + (getSegmentT(segments, index) - startT) * (distance - startD) / (getSegmentDistance(segments, index) - startD);
        return index;
    }

    private void segmentTo(int segIndex, float startT, float endT, PathConsumer dst) {
        assert startT >= 0.0F && startT <= 1.0F;
        assert endT >= 0.0F && endT <= 1.0F;
        assert startT <= endT;
        if (startT != endT) {
            int ci = getSegmentCoordIndex(this.mSegments.elements(), segIndex);
            float[] pts = this.mCoords.elements();
            int segType = getSegmentType(this.mSegments.elements(), segIndex);
            switch(segType) {
                case 0:
                    if (endT == 1.0F) {
                        dst.lineTo(pts[ci + 2], pts[ci + 3]);
                    } else {
                        dst.lineTo(MathUtil.lerp(pts[ci], pts[ci + 2], endT), MathUtil.lerp(pts[ci + 1], pts[ci + 3], endT));
                    }
                    break;
                case 1:
                    if (startT == 0.0F) {
                        if (endT == 1.0F) {
                            dst.quadTo(pts, ci + 2);
                        } else {
                            Geometry.chopQuadAt(pts, ci, this.mTmp, 0, endT);
                            dst.quadTo(this.mTmp, 2);
                        }
                    } else {
                        Geometry.chopQuadAt(pts, ci, this.mTmp, 0, startT);
                        if (endT == 1.0F) {
                            dst.quadTo(this.mTmp, 6);
                        } else {
                            Geometry.chopQuadAt(this.mTmp, 4, this.mTmp, 10, (endT - startT) / (1.0F - startT));
                            dst.quadTo(this.mTmp, 12);
                        }
                    }
                case 2:
                default:
                    break;
                case 3:
                    if (startT == 0.0F) {
                        if (endT == 1.0F) {
                            dst.cubicTo(pts, ci + 2);
                        } else {
                            Geometry.chopCubicAt(pts, ci, this.mTmp, 0, endT);
                            dst.cubicTo(this.mTmp, 2);
                        }
                    } else {
                        Geometry.chopCubicAt(pts, ci, this.mTmp, 0, startT);
                        if (endT == 1.0F) {
                            dst.cubicTo(this.mTmp, 8);
                        } else {
                            Geometry.chopCubicAt(this.mTmp, 6, this.mTmp, 14, (endT - startT) / (1.0F - startT));
                            dst.cubicTo(this.mTmp, 16);
                        }
                    }
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MatrixFlags {
    }
}