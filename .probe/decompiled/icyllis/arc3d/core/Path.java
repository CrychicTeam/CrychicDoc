package icyllis.arc3d.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Arrays;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.ApiStatus.Internal;

public class Path implements PathConsumer {

    public static final int FILL_NON_ZERO = 0;

    public static final int FILL_EVEN_ODD = 1;

    public static final byte VERB_MOVE = 0;

    public static final byte VERB_LINE = 1;

    public static final byte VERB_QUAD = 2;

    public static final byte VERB_CUBIC = 4;

    public static final byte VERB_CLOSE = 5;

    public static final int DIRECTION_CW = 0;

    public static final int DIRECTION_CCW = 1;

    public static final int SEGMENT_LINE = 1;

    public static final int SEGMENT_QUAD = 2;

    public static final int SEGMENT_CUBIC = 8;

    private static final byte CONVEXITY_CONVEX = 0;

    private static final byte CONVEXITY_CONCAVE = 1;

    private static final byte CONVEXITY_UNKNOWN = 2;

    private static final byte FIRST_DIRECTION_CW = 0;

    private static final byte FIRST_DIRECTION_CCW = 1;

    private static final byte FIRST_DIRECTION_UNKNOWN = 2;

    public static final int APPROXIMATE_ARC_WITH_CUBICS = 0;

    public static final int APPROXIMATE_CONIC_WITH_QUADS = 1;

    @SharedPtr
    private Path.Ref mRef;

    private int mLastMoveToIndex;

    private byte mConvexity;

    private byte mFirstDirection;

    private byte mFillRule;

    public Path() {
        this.mRef = RefCnt.create(Path.Ref.EMPTY);
        this.resetFields();
    }

    public Path(@Nonnull Path other) {
        this.mRef = RefCnt.create(other.mRef);
        this.copyFields(other);
    }

    private void resetFields() {
        this.mLastMoveToIndex = -1;
        this.mFillRule = 0;
        this.mConvexity = 2;
        this.mFirstDirection = 2;
    }

    private void copyFields(@Nonnull Path other) {
        this.mLastMoveToIndex = other.mLastMoveToIndex;
        this.mConvexity = other.mConvexity;
        this.mFirstDirection = other.mFirstDirection;
        this.mFillRule = other.mFillRule;
    }

    public int getFillRule() {
        return this.mFillRule;
    }

    public void setFillRule(int rule) {
        if ((rule & -2) != 0) {
            throw new IllegalArgumentException();
        } else {
            assert rule == 0 || rule == 1;
            this.mFillRule = (byte) rule;
        }
    }

    public void set(@Nonnull Path other) {
        if (other != this) {
            this.mRef = RefCnt.create(this.mRef, other.mRef);
            this.copyFields(other);
        }
    }

    public void move(@Nonnull Path other) {
        if (other != this) {
            this.mRef = RefCnt.move(this.mRef, other.mRef);
            other.mRef = RefCnt.create(Path.Ref.EMPTY);
            this.copyFields(other);
            other.resetFields();
        }
    }

    public void reset() {
        if (this.mRef.unique()) {
            this.mRef.reset();
        } else {
            this.mRef = RefCnt.create(this.mRef, Path.Ref.EMPTY);
        }
        this.resetFields();
    }

    public void clear() {
        if (this.mRef.unique()) {
            this.mRef.reset();
        } else {
            int verbSize = this.mRef.mVerbSize;
            int coordSize = this.mRef.mCoordSize;
            this.mRef = RefCnt.move(this.mRef, new Path.Ref(verbSize, coordSize));
        }
        this.resetFields();
    }

    public void recycle() {
        this.mRef = RefCnt.create(this.mRef, Path.Ref.EMPTY);
        this.resetFields();
    }

    public void trimToSize() {
        if (this.mRef.unique()) {
            this.mRef.trimToSize();
        } else {
            this.mRef = RefCnt.move(this.mRef, new Path.Ref(this.mRef));
        }
    }

    public boolean isEmpty() {
        assert this.mRef.mVerbSize == 0 == (this.mRef.mCoordSize == 0);
        return this.mRef.mVerbSize == 0;
    }

    public boolean isFinite() {
        return this.mRef.isFinite();
    }

    private void dirtyAfterEdit() {
        this.mConvexity = 2;
        this.mFirstDirection = 2;
    }

    @Override
    public void moveTo(float x, float y) {
        this.mLastMoveToIndex = this.mRef.mCoordSize;
        this.editor().addVerb((byte) 0).addPoint(x, y);
        this.dirtyAfterEdit();
    }

    public void moveToRel(float dx, float dy) {
        int n = this.mRef.mCoordSize;
        if (n != 0) {
            float px = this.mRef.mCoords[n - 2];
            float py = this.mRef.mCoords[n - 1];
            this.moveTo(px + dx, py + dy);
        } else {
            throw new IllegalStateException("No first point");
        }
    }

    @Override
    public void lineTo(float x, float y) {
        if (this.mLastMoveToIndex >= 0) {
            this.editor().addVerb((byte) 1).addPoint(x, y);
            this.dirtyAfterEdit();
        } else {
            throw new IllegalStateException("No initial point");
        }
    }

    public void lineToRel(float dx, float dy) {
        int n = this.mRef.mCoordSize;
        if (n != 0) {
            float px = this.mRef.mCoords[n - 2];
            float py = this.mRef.mCoords[n - 1];
            this.lineTo(px + dx, py + dy);
        } else {
            throw new IllegalStateException("No first point");
        }
    }

    @Override
    public void quadTo(float x1, float y1, float x2, float y2) {
        if (this.mLastMoveToIndex >= 0) {
            this.editor().addVerb((byte) 2).addPoint(x1, y1).addPoint(x2, y2);
            this.dirtyAfterEdit();
        } else {
            throw new IllegalStateException("No initial point");
        }
    }

    public void quadToRel(float dx1, float dy1, float dx2, float dy2) {
        int n = this.mRef.mCoordSize;
        if (n != 0) {
            float px = this.mRef.mCoords[n - 2];
            float py = this.mRef.mCoords[n - 1];
            this.quadTo(px + dx1, py + dy1, px + dx2, py + dy2);
        } else {
            throw new IllegalStateException("No first point");
        }
    }

    @Override
    public void cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        if (this.mLastMoveToIndex >= 0) {
            this.editor().addVerb((byte) 4).addPoint(x1, y1).addPoint(x2, y2).addPoint(x3, y3);
            this.dirtyAfterEdit();
        } else {
            throw new IllegalStateException("No initial point");
        }
    }

    public void cubicToRel(float dx1, float dy1, float dx2, float dy2, float dx3, float dy3) {
        int n = this.mRef.mCoordSize;
        if (n != 0) {
            float px = this.mRef.mCoords[n - 2];
            float py = this.mRef.mCoords[n - 1];
            this.cubicTo(px + dx1, py + dy1, px + dx2, py + dy2, px + dx3, py + dy3);
        } else {
            throw new IllegalStateException("No first point");
        }
    }

    @Override
    public void closePath() {
        int count = this.countVerbs();
        if (count != 0) {
            switch(this.mRef.mVerbs[count - 1]) {
                case 0:
                case 1:
                case 2:
                case 4:
                    this.editor().addVerb((byte) 5);
                    break;
                case 3:
                default:
                    throw new AssertionError();
                case 5:
            }
        }
        this.mLastMoveToIndex = this.mLastMoveToIndex ^ ~this.mLastMoveToIndex >> 31;
    }

    @Override
    public void pathDone() {
    }

    public int countVerbs() {
        return this.mRef.mVerbSize;
    }

    public int countPoints() {
        assert this.mRef.mCoordSize % 2 == 0;
        return this.mRef.mCoordSize >> 1;
    }

    @Nonnull
    public Rect2fc getBounds() {
        return this.mRef.getBounds();
    }

    public void updateBoundsCache() {
        this.mRef.updateBounds();
    }

    public int getSegmentMask() {
        return this.mRef.mSegmentMask;
    }

    public PathIterator iterator() {
        return new Path.Iterator();
    }

    public void forEach(@Nonnull PathConsumer action) {
        int n = this.countVerbs();
        if (n != 0) {
            byte[] vs = this.mRef.mVerbs;
            float[] cs = this.mRef.mCoords;
            int vi = 0;
            int ci = 0;
            label23: do {
                switch(vs[vi++]) {
                    case 0:
                        if (vi == n) {
                            break label23;
                        }
                        action.moveTo(cs[ci++], cs[ci++]);
                        break;
                    case 1:
                        action.lineTo(cs[ci++], cs[ci++]);
                        break;
                    case 2:
                        action.quadTo(cs, ci);
                        ci += 4;
                    case 3:
                    default:
                        break;
                    case 4:
                        action.cubicTo(cs, ci);
                        ci += 6;
                        break;
                    case 5:
                        action.closePath();
                }
            } while (vi < n);
        }
        action.pathDone();
    }

    public long estimatedByteSize() {
        long size = 24L;
        return size + this.mRef.estimatedByteSize();
    }

    public int hashCode() {
        int hash = this.mRef.hashCode();
        return 31 * hash + this.mFillRule;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return !(obj instanceof Path other) ? false : this.mFillRule == other.mFillRule && this.mRef.equals(other.mRef);
        }
    }

    void reversePop(@Nonnull PathConsumer out) {
        assert this.mRef != null;
        byte[] vs = this.mRef.mVerbs;
        float[] cs = this.mRef.mCoords;
        int vi = this.mRef.mVerbSize;
        int ci = this.mRef.mCoordSize - 2;
        label35: while (vi != 0) {
            vi--;
            switch(vs[vi]) {
                case 0:
                    assert vi == 0 && ci == 0;
                    break label35;
                case 1:
                    out.lineTo(cs[ci - 2], cs[ci - 1]);
                    ci -= 2;
                    break;
                case 2:
                    out.quadTo(cs[ci - 2], cs[ci - 1], cs[ci - 4], cs[ci - 3]);
                    ci -= 4;
                    break;
                case 3:
                default:
                    assert false;
                    break;
                case 4:
                    out.cubicTo(cs[ci - 2], cs[ci - 1], cs[ci - 4], cs[ci - 3], cs[ci - 6], cs[ci - 5]);
                    ci -= 6;
            }
        }
        this.clear();
        return;
    }

    private Path.Ref editor() {
        if (!this.mRef.unique()) {
            this.mRef = RefCnt.move(this.mRef, new Path.Ref(this.mRef));
        }
        this.mRef.mBounds.set(0.0F, 0.0F, -1.0F, -1.0F);
        return this.mRef;
    }

    int computeConvexity() {
        return !this.isFinite() ? 1 : 0;
    }

    @Nonnull
    private static byte[] growVerbs(@Nonnull byte[] old, int minGrow) {
        int oldCap = old.length;
        int grow;
        if (oldCap < 10) {
            grow = 10 - oldCap;
        } else if (oldCap > 500) {
            grow = Math.max(250, oldCap >> 3);
        } else {
            grow = oldCap >> 1;
        }
        int newCap = oldCap + Math.max(grow, minGrow);
        if (newCap < 0) {
            newCap = oldCap + minGrow;
            if (newCap < 0) {
                throw new IllegalStateException("Path is too big " + oldCap + " + " + minGrow);
            }
            newCap = Integer.MAX_VALUE;
        }
        return Arrays.copyOf(old, newCap);
    }

    @Nonnull
    private static float[] growCoords(@Nonnull float[] old, int minGrow) {
        int oldCap = old.length;
        int grow;
        if (oldCap < 20) {
            grow = 20 - oldCap;
        } else if (oldCap > 1000) {
            grow = Math.max(500, oldCap >> 4 << 1);
        } else {
            grow = oldCap >> 2 << 1;
        }
        assert oldCap % 2 == 0 && minGrow % 2 == 0;
        int newCap = oldCap + Math.max(grow, minGrow);
        if (newCap < 0) {
            newCap = oldCap + minGrow;
            if (newCap < 0) {
                throw new IllegalStateException("Path is too big " + oldCap + " + " + minGrow);
            }
            newCap = 2147483646;
        }
        return Arrays.copyOf(old, newCap);
    }

    static class ConvexState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FillRule {
    }

    private class Iterator implements PathIterator {

        private final int count = Path.this.countVerbs();

        private int verbPos;

        private int coordPos;

        @Override
        public int next(float[] coords, int offset) {
            if (this.verbPos == this.count) {
                return 6;
            } else {
                byte verb = Path.this.mRef.mVerbs[this.verbPos++];
                switch(verb) {
                    case 0:
                        if (this.verbPos == this.count) {
                            return 6;
                        }
                        if (coords != null) {
                            coords[offset] = Path.this.mRef.mCoords[this.coordPos];
                            coords[offset + 1] = Path.this.mRef.mCoords[this.coordPos + 1];
                        }
                        this.coordPos += 2;
                        break;
                    case 1:
                        if (coords != null) {
                            coords[offset] = Path.this.mRef.mCoords[this.coordPos];
                            coords[offset + 1] = Path.this.mRef.mCoords[this.coordPos + 1];
                        }
                        this.coordPos += 2;
                        break;
                    case 2:
                        if (coords != null) {
                            System.arraycopy(Path.this.mRef.mCoords, this.coordPos, coords, offset, 4);
                        }
                        this.coordPos += 4;
                    case 3:
                    default:
                        break;
                    case 4:
                        if (coords != null) {
                            System.arraycopy(Path.this.mRef.mCoords, this.coordPos, coords, offset, 6);
                        }
                        this.coordPos += 6;
                }
                return verb;
            }
        }
    }

    @Internal
    public class RawIterator {

        private final int count = Path.this.countVerbs();

        private int verbPos;

        private int coordPos;

        private int coordOff;

        private int coordInc;

        public boolean hasNext() {
            return this.verbPos < this.count;
        }

        public byte next() {
            if (this.verbPos == this.count) {
                return 6;
            } else {
                byte verb = Path.this.mRef.mVerbs[this.verbPos++];
                this.coordPos = this.coordPos + this.coordInc;
                switch(verb) {
                    case 0:
                    case 1:
                        this.coordInc = 2;
                        break;
                    case 2:
                        this.coordInc = 4;
                    case 3:
                    default:
                        break;
                    case 4:
                        this.coordInc = 6;
                        break;
                    case 5:
                        this.coordInc = 0;
                }
                this.coordOff = verb == 0 ? 0 : -2;
                return verb;
            }
        }

        public float x0() {
            return Path.this.mRef.mCoords[this.coordPos + this.coordOff];
        }

        public float y0() {
            return Path.this.mRef.mCoords[this.coordPos + this.coordOff + 1];
        }

        public float x1() {
            return Path.this.mRef.mCoords[this.coordPos + this.coordOff + 2];
        }

        public float y1() {
            return Path.this.mRef.mCoords[this.coordPos + this.coordOff + 3];
        }

        public float x2() {
            return Path.this.mRef.mCoords[this.coordPos + this.coordOff + 4];
        }

        public float y2() {
            return Path.this.mRef.mCoords[this.coordPos + this.coordOff + 5];
        }

        public float x3() {
            return Path.this.mRef.mCoords[this.coordPos + this.coordOff + 6];
        }

        public float y3() {
            return Path.this.mRef.mCoords[this.coordPos + this.coordOff + 7];
        }
    }

    static final class Ref implements RefCounted {

        private static final VarHandle USAGE_CNT;

        static final byte[] EMPTY_VERBS;

        static final float[] EMPTY_COORDS;

        static final Path.Ref EMPTY;

        transient volatile int mUsageCnt = 1;

        final Rect2f mBounds = new Rect2f(0.0F, 0.0F, -1.0F, -1.0F);

        byte[] mVerbs;

        float[] mCoords;

        int mVerbSize;

        int mCoordSize;

        byte mSegmentMask;

        Ref() {
            this(EMPTY_VERBS, EMPTY_COORDS);
        }

        Ref(int verbSize, int coordSize) {
            assert coordSize % 2 == 0;
            if (verbSize > 0) {
                assert coordSize > 0;
                this.mVerbs = new byte[verbSize];
                this.mCoords = new float[coordSize];
            } else {
                this.mVerbs = EMPTY_VERBS;
                this.mCoords = EMPTY_COORDS;
            }
        }

        Ref(byte[] verbs, float[] coords) {
            assert coords.length % 2 == 0;
            this.mVerbs = verbs;
            this.mCoords = coords;
        }

        Ref(@Nonnull Path.Ref other) {
            this.mBounds.set(other.mBounds);
            if (other.mVerbSize > 0) {
                assert other.mCoordSize > 0;
                this.mVerbs = Arrays.copyOf(other.mVerbs, other.mVerbSize);
                this.mCoords = Arrays.copyOf(other.mCoords, other.mCoordSize);
            } else {
                this.mVerbs = EMPTY_VERBS;
                this.mCoords = EMPTY_COORDS;
            }
            this.mVerbSize = other.mVerbSize;
            this.mCoordSize = other.mCoordSize;
            this.mSegmentMask = other.mSegmentMask;
        }

        Ref(@Nonnull Path.Ref other, int incVerbs, int incCoords) {
            assert incVerbs >= 0 && incCoords >= 0;
            assert incCoords % 2 == 0;
            this.mVerbs = new byte[other.mVerbSize + incVerbs];
            this.mCoords = new float[other.mCoordSize + incCoords];
            this.mBounds.set(other.mBounds);
            System.arraycopy(other.mVerbs, 0, this.mVerbs, 0, other.mVerbSize);
            System.arraycopy(other.mCoords, 0, this.mCoords, 0, other.mCoordSize);
            this.mVerbSize = other.mVerbSize;
            this.mCoordSize = other.mCoordSize;
            this.mSegmentMask = other.mSegmentMask;
        }

        boolean unique() {
            return (int) USAGE_CNT.getAcquire(this) == 1 && this != EMPTY;
        }

        @Override
        public void ref() {
            USAGE_CNT.getAndAddAcquire(this, 1);
        }

        @Override
        public void unref() {
            USAGE_CNT.getAndAdd(this, -1);
        }

        void reset() {
            this.mBounds.set(0.0F, 0.0F, -1.0F, -1.0F);
            this.mSegmentMask = 0;
            this.mVerbSize = 0;
            this.mCoordSize = 0;
        }

        void reserve(int incVerbs, int incCoords) {
            assert incVerbs >= 0 && incCoords >= 0;
            assert incCoords % 2 == 0;
            if (this.mVerbSize > this.mVerbs.length - incVerbs) {
                this.mVerbs = Path.growVerbs(this.mVerbs, incVerbs);
            }
            if (this.mCoordSize > this.mCoords.length - incCoords) {
                this.mCoords = Path.growCoords(this.mCoords, incCoords);
            }
        }

        void trimToSize() {
            if (this.mVerbSize > 0) {
                assert this.mCoordSize > 0;
                if (this.mVerbSize < this.mVerbs.length) {
                    this.mVerbs = Arrays.copyOf(this.mVerbs, this.mVerbSize);
                }
                if (this.mCoordSize < this.mCoords.length) {
                    this.mCoords = Arrays.copyOf(this.mCoords, this.mCoordSize);
                }
            } else {
                this.mVerbs = EMPTY_VERBS;
                this.mCoords = EMPTY_COORDS;
            }
        }

        Path.Ref addVerb(byte verb) {
            int coords = switch(verb) {
                case 0 ->
                    2;
                case 1 ->
                    {
                    }
                case 2 ->
                    {
                    }
                default ->
                    0;
                case 4 ->
                    {
                    }
            };
            this.reserve(1, coords);
            this.mVerbs[this.mVerbSize++] = verb;
            return this;
        }

        Path.Ref addPoint(float x, float y) {
            this.mCoords[this.mCoordSize++] = x;
            this.mCoords[this.mCoordSize++] = y;
            return this;
        }

        void updateBounds() {
            if (!this.mBounds.isSorted() && this.mBounds.isFinite()) {
                assert this.mCoordSize % 2 == 0;
                this.mBounds.setBoundsNoCheck(this.mCoords, 0, this.mCoordSize >> 1);
            }
        }

        boolean isFinite() {
            this.updateBounds();
            return this.mBounds.isFinite();
        }

        Rect2fc getBounds() {
            return (Rect2fc) (this.isFinite() ? this.mBounds : Rect2f.empty());
        }

        long estimatedByteSize() {
            if (this == EMPTY) {
                return 0L;
            } else {
                long size = 72L;
                if (this.mVerbs != EMPTY_VERBS) {
                    size += (long) (16 + MathUtil.align8(this.mVerbs.length));
                }
                if (this.mCoords != EMPTY_COORDS) {
                    assert this.mCoords.length % 2 == 0;
                    size += 16L + ((long) this.mCoords.length << 2);
                }
                return size;
            }
        }

        public int hashCode() {
            int hash = 7;
            int i = 0;
            for (int count = this.mVerbSize; i < count; i++) {
                hash = 11 * hash + this.mVerbs[i];
            }
            i = 0;
            for (int count = this.mCoordSize; i < count; i++) {
                hash = 11 * hash + Float.floatToIntBits(this.mCoords[i]);
            }
            return hash;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj instanceof Path.Ref other) {
                if (this.mSegmentMask != other.mSegmentMask) {
                    return false;
                } else if (this.mCoordSize != other.mCoordSize) {
                    return false;
                } else if (!Arrays.equals(this.mVerbs, 0, this.mVerbSize, other.mVerbs, 0, other.mVerbSize)) {
                    return false;
                } else {
                    int i = 0;
                    for (int count = this.mCoordSize; i < count; i++) {
                        if (this.mCoords[i] != other.mCoords[i]) {
                            return false;
                        }
                    }
                    return true;
                }
            } else {
                return false;
            }
        }

        static {
            Lookup lookup = MethodHandles.lookup();
            try {
                USAGE_CNT = lookup.findVarHandle(Path.Ref.class, "mUsageCnt", int.class);
            } catch (IllegalAccessException | NoSuchFieldException var2) {
                throw new RuntimeException(var2);
            }
            EMPTY_VERBS = new byte[0];
            EMPTY_COORDS = new float[0];
            EMPTY = new Path.Ref();
            EMPTY.updateBounds();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SegmentMask {
    }
}