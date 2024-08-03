package icyllis.arc3d.core;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public abstract class RectanglePacker {

    public static final int ALGORITHM_SKYLINE = 0;

    public static final int ALGORITHM_HORIZON = 1;

    public static final int ALGORITHM_HORIZON_OLD = 2;

    public static final int ALGORITHM_BINARY_TREE = 3;

    public static final int ALGORITHM_POWER2_LINE = 4;

    public static final int ALGORITHM_SKYLINE_NEW = 7;

    protected final int mWidth;

    protected final int mHeight;

    protected int mArea;

    protected RectanglePacker(int width, int height) {
        if (width >= 1 && height >= 1 && width <= 32768 && height <= 32768) {
            this.mWidth = width;
            this.mHeight = height;
        } else {
            throw new IllegalArgumentException("width " + width + " or height " + height + " is out of range 1..32768");
        }
    }

    public static RectanglePacker make(int width, int height) {
        return new RectanglePacker.Skyline(width, height);
    }

    public static RectanglePacker make(int width, int height, int algorithm) {
        return (RectanglePacker) (switch(algorithm) {
            case 0 ->
                new RectanglePacker.Skyline(width, height);
            case 1 ->
                new RectanglePacker.Horizon(width, height);
            case 2 ->
                new RectanglePacker.HorizonOld(width, height);
            case 3 ->
                new RectanglePacker.BinaryTree(width, height);
            case 4 ->
                new RectanglePacker.Power2Line(width, height);
            default ->
                throw new AssertionError(algorithm);
            case 7 ->
                new RectanglePacker.SkylineNew(width, height);
        });
    }

    public final int getWidth() {
        return this.mWidth;
    }

    public final int getHeight() {
        return this.mHeight;
    }

    public abstract void clear();

    public abstract boolean addRect(Rect2i var1);

    public final double getCoverage() {
        return (double) this.mArea / (double) this.mWidth / (double) this.mHeight;
    }

    public void free() {
    }

    public static final class BinaryTree extends RectanglePacker {

        private RectanglePacker.BinaryTree.Node mRoot;

        public BinaryTree(int width, int height) {
            super(width, height);
            this.mRoot = new RectanglePacker.BinaryTree.Node(0, 0, width, height);
        }

        @Override
        public void clear() {
            this.mArea = 0;
            this.mRoot = new RectanglePacker.BinaryTree.Node(0, 0, this.mWidth, this.mHeight);
        }

        @Override
        public boolean addRect(Rect2i rect) {
            int width = rect.width();
            int height = rect.height();
            assert width > 0 && height > 0;
            if (width <= this.mWidth && height <= this.mHeight) {
                RectanglePacker.BinaryTree.Node node = this.mRoot.insert(width, height);
                if (node != null) {
                    rect.offsetTo(node.x, node.y);
                    this.mArea += width * height;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        private static final class Node {

            private final int x;

            private final int y;

            private final int width;

            private final int height;

            @Nullable
            private RectanglePacker.BinaryTree.Node left;

            @Nullable
            private RectanglePacker.BinaryTree.Node right;

            private boolean filled;

            Node(int x, int y, int width, int height) {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
            }

            @Nullable
            RectanglePacker.BinaryTree.Node insert(int width, int height) {
                if (this.left != null && this.right != null) {
                    RectanglePacker.BinaryTree.Node node = this.left.insert(width, height);
                    if (node == null) {
                        node = this.right.insert(width, height);
                    }
                    return node;
                } else if (this.filled) {
                    return null;
                } else if (width > this.width || height > this.height) {
                    return null;
                } else if (width == this.width && height == this.height) {
                    this.filled = true;
                    return this;
                } else {
                    int widthLeft = this.width - width;
                    int heightLeft = this.height - height;
                    if (widthLeft > heightLeft) {
                        this.left = new RectanglePacker.BinaryTree.Node(this.x, this.y, width, this.height);
                        this.right = new RectanglePacker.BinaryTree.Node(this.x + width, this.y, widthLeft, this.height);
                    } else {
                        this.left = new RectanglePacker.BinaryTree.Node(this.x, this.y, this.width, height);
                        this.right = new RectanglePacker.BinaryTree.Node(this.x, this.y + height, this.width, heightLeft);
                    }
                    return this.left.insert(width, height);
                }
            }
        }
    }

    public static final class Horizon extends RectanglePacker {

        private final List<RectanglePacker.Horizon.Level> mLevels = new ArrayList();

        private static final int MIN_HEIGHT = 8;

        private static final int ROUND_UP = 4;

        private int mRecentUsedLevelIndex = 0;

        private int mHeightOffset;

        public Horizon(int width, int height) {
            super(width, height);
        }

        @Override
        public void clear() {
            this.mArea = 0;
            this.mLevels.clear();
            this.mHeightOffset = 0;
            this.mRecentUsedLevelIndex = 0;
        }

        @Override
        public boolean addRect(Rect2i rect) {
            int width = rect.width();
            int height = rect.height();
            assert width > 0 && height > 0;
            if (width <= this.mWidth && height <= this.mHeight) {
                int newHeight = MathUtil.alignUp(Math.max(8, height), 4);
                int newIndex;
                if (this.mRecentUsedLevelIndex < this.mLevels.size() && ((RectanglePacker.Horizon.Level) this.mLevels.get(this.mRecentUsedLevelIndex)).height != newHeight) {
                    newIndex = binarySearch(this.mLevels, newHeight);
                } else {
                    newIndex = this.mRecentUsedLevelIndex;
                }
                boolean newLevelFlag = this.mHeightOffset + newHeight <= this.mHeight;
                int i = newIndex;
                for (int max = this.mLevels.size(); i < max; i++) {
                    RectanglePacker.Horizon.Level level = (RectanglePacker.Horizon.Level) this.mLevels.get(i);
                    if (level.height > newHeight + 8 && newLevelFlag) {
                        break;
                    }
                    if (level.add(rect, this.mWidth, width, height)) {
                        this.mRecentUsedLevelIndex = i;
                        this.mArea += width * height;
                        return true;
                    }
                }
                if (!newLevelFlag) {
                    return false;
                } else {
                    RectanglePacker.Horizon.Level newLevel = new RectanglePacker.Horizon.Level(this.mHeightOffset, newHeight);
                    this.mHeightOffset += newHeight;
                    if (newIndex < this.mLevels.size() && ((RectanglePacker.Horizon.Level) this.mLevels.get(newIndex)).height <= newHeight) {
                        this.mLevels.add(newIndex + 1, newLevel);
                        this.mRecentUsedLevelIndex = newIndex + 1;
                    } else {
                        this.mLevels.add(newIndex, newLevel);
                        this.mRecentUsedLevelIndex = newIndex;
                    }
                    if (newLevel.add(rect, this.mWidth, width, height)) {
                        this.mArea += width * height;
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        private static int binarySearch(List<RectanglePacker.Horizon.Level> levels, int k) {
            int key = k + 1;
            int from = 0;
            int to = levels.size() - 1;
            int mid = 0;
            int midSize = 0;
            if (to < 0) {
                return 0;
            } else {
                while (from <= to) {
                    mid = (from + to) / 2;
                    midSize = ((RectanglePacker.Horizon.Level) levels.get(mid)).height;
                    if (key < midSize) {
                        to = mid - 1;
                    } else {
                        from = mid + 1;
                    }
                }
                if (midSize < k) {
                    return mid + 1;
                } else if (midSize > k) {
                    return mid > 0 ? mid - 1 : 0;
                } else {
                    return mid;
                }
            }
        }

        private static final class Level {

            private final int y;

            private final int height;

            private int x;

            Level(int y, int height) {
                this.y = y;
                this.height = height;
            }

            boolean add(Rect2i rect, int levelWidth, int requestedLength, int requestedSize) {
                if (this.x + requestedLength <= levelWidth && requestedSize <= this.height) {
                    rect.offsetTo(this.x, this.y);
                    this.x += requestedLength;
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public static final class HorizonOld extends RectanglePacker {

        public static final int INITIAL_SIZE = 512;

        private int mPosX;

        private int mPosY;

        private int mLineHeight;

        private int mCurrWidth;

        private int mCurrHeight;

        public HorizonOld(int width, int height) {
            super(width, height);
        }

        @Override
        public void clear() {
            this.mArea = 0;
            this.mPosX = 0;
            this.mPosY = 0;
            this.mLineHeight = 0;
            this.mCurrWidth = 0;
            this.mCurrHeight = 0;
        }

        @Override
        public boolean addRect(Rect2i rect) {
            int width = rect.width();
            int height = rect.height();
            assert width > 0 && height > 0;
            if (width <= this.mWidth && height <= this.mHeight) {
                if (this.mCurrWidth == 0) {
                    this.resize();
                }
                if (this.mPosX + width >= this.mCurrWidth) {
                    this.mPosX = 0;
                    if (this.mCurrWidth == this.mCurrHeight && this.mCurrWidth != 512) {
                        this.mPosX = this.mPosX + (this.mCurrWidth >> 1);
                    }
                    this.mPosY = this.mPosY + this.mLineHeight;
                    this.mLineHeight = 0;
                }
                if (this.mPosY + height >= this.mCurrHeight) {
                    if (this.mCurrWidth != this.mCurrHeight) {
                        this.mPosX = this.mCurrWidth;
                        this.mPosY = 0;
                    }
                    if (!this.resize()) {
                        return false;
                    }
                }
                rect.offsetTo(this.mPosX, this.mPosY);
                this.mPosX += width;
                this.mLineHeight = Math.max(this.mLineHeight, height);
                this.mArea += width * height;
                return true;
            } else {
                return false;
            }
        }

        private boolean resize() {
            if (this.mCurrWidth == 0) {
                this.mCurrWidth = this.mCurrHeight = 512;
            } else {
                if (this.mCurrWidth == super.mWidth && this.mCurrHeight == super.mHeight) {
                    return false;
                }
                if (this.mCurrHeight != this.mCurrWidth) {
                    this.mCurrWidth <<= 1;
                } else {
                    this.mCurrHeight <<= 1;
                }
            }
            return true;
        }
    }

    public static final class Power2Line extends RectanglePacker {

        private int mNextStripY;

        private final RectanglePacker.Power2Line.Row[] mRows = new RectanglePacker.Power2Line.Row[16];

        public Power2Line(int width, int height) {
            super(width, height);
            for (int i = 0; i < 16; i++) {
                this.mRows[i] = new RectanglePacker.Power2Line.Row();
            }
        }

        @Override
        public void clear() {
            this.mArea = 0;
            this.mNextStripY = 0;
            for (int i = 0; i < 16; i++) {
                this.mRows[i] = new RectanglePacker.Power2Line.Row();
            }
        }

        @Override
        public boolean addRect(Rect2i rect) {
            int width = rect.width();
            int height = rect.height();
            assert width > 0 && height > 0;
            if (width <= this.mWidth && height <= this.mHeight) {
                int area = width * height;
                height = MathUtil.ceilPow2(height);
                height = Math.max(height, 2);
                RectanglePacker.Power2Line.Row row = this.mRows[toRowIndex(height)];
                assert row.height == 0 || row.height == height;
                if (0 == row.height) {
                    if (this.mNextStripY + height > this.mHeight) {
                        return false;
                    }
                    this.initRow(row, height);
                } else if (!row.canAddWidth(width, this.mWidth)) {
                    if (this.mNextStripY + height > this.mHeight) {
                        return false;
                    }
                    this.initRow(row, height);
                }
                assert row.height == height;
                assert row.canAddWidth(width, this.mWidth);
                rect.offsetTo(row.x, row.y);
                row.x += width;
                assert row.x <= this.mWidth;
                assert row.y <= this.mHeight;
                assert this.mNextStripY <= this.mHeight;
                this.mArea += area;
                return true;
            } else {
                return false;
            }
        }

        void initRow(RectanglePacker.Power2Line.Row row, int rowHeight) {
            row.x = 0;
            row.y = this.mNextStripY;
            row.height = rowHeight;
            this.mNextStripY += rowHeight;
        }

        static int toRowIndex(int height) {
            assert height >= 2;
            int index = 32 - Integer.numberOfLeadingZeros(height - 1);
            assert index < 16;
            return index;
        }

        private static final class Row {

            private int x;

            private int y;

            private int height;

            boolean canAddWidth(int width, int containerWidth) {
                return this.x + width <= containerWidth;
            }
        }
    }

    public static final class Skyline extends RectanglePacker {

        private static final int X = 0;

        private static final int Y = 1;

        private static final int WIDTH = 2;

        private static final int COLUMNS = 3;

        private final short[] mData;

        private int mSize;

        public Skyline(int width, int height) {
            super(width, height);
            this.mData = new short[(width + 10) * 3];
            this.mData[2] = (short) this.mWidth;
            this.mSize = 1;
        }

        @Override
        public void clear() {
            this.mArea = 0;
            this.mData[0] = 0;
            this.mData[1] = 0;
            this.mData[2] = (short) this.mWidth;
            this.mSize = 1;
        }

        @Override
        public boolean addRect(Rect2i rect) {
            int width = rect.width();
            int height = rect.height();
            if (width > 0 && height > 0) {
                if (width <= this.mWidth && height <= this.mHeight) {
                    int bestWidth = this.mWidth + 1;
                    int bestX = 0;
                    int bestY = this.mHeight + 1;
                    int bestIndex = -1;
                    short[] data = this.mData;
                    int index = 0;
                    label52: for (int limit = this.mSize * 3; index < limit; index += 3) {
                        int x = data[index + 0] & '\uffff';
                        if (x + width <= this.mWidth) {
                            int y = data[index + 1] & '\uffff';
                            int i = index;
                            int widthLeft = width;
                            while (widthLeft > 0) {
                                y = Math.max(y, data[i + 1] & '\uffff');
                                if (y + height > this.mHeight) {
                                    continue label52;
                                }
                                widthLeft -= data[i + 2] & '\uffff';
                                i += 3;
                                assert i < limit || widthLeft <= 0;
                            }
                            i = data[index + 2] & '\uffff';
                            if (y < bestY || y == bestY && i < bestWidth) {
                                bestIndex = index;
                                bestWidth = i;
                                bestX = x;
                                bestY = y;
                            }
                        }
                    }
                    if (bestIndex != -1) {
                        this.addLevel(bestIndex, bestX, bestY, width, height);
                        rect.offsetTo(bestX, bestY);
                        this.mArea += width * height;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                rect.offsetTo(0, 0);
                return true;
            }
        }

        private void addLevel(int index, int x, int y, int width, int height) {
            assert x + width <= this.mWidth;
            assert y + height <= this.mHeight;
            short[] data = this.mData;
            System.arraycopy(data, index, data, index + 3, this.mSize * 3 - index);
            data[index + 0] = (short) x;
            data[index + 1] = (short) (y + height);
            data[index + 2] = (short) width;
            this.mSize++;
            for (int i = index + 3; i < this.mSize * 3; this.mSize--) {
                int cx = data[i + 0] & '\uffff';
                int px = data[i - 3 + 0] & '\uffff';
                assert cx >= px;
                int cw = data[i + 2] & '\uffff';
                int pw = data[i - 3 + 2] & '\uffff';
                int shrink = px + pw - cx;
                if (shrink <= 0) {
                    break;
                }
                int nw = cw - shrink;
                if (nw > 0) {
                    data[i + 0] = (short) (cx + shrink);
                    data[i + 2] = (short) nw;
                    break;
                }
                System.arraycopy(data, i + 3, data, i, (this.mSize - 1) * 3 - i);
            }
            int i = 0;
            while (i < (this.mSize - 1) * 3) {
                if (data[i + 1] == data[i + 3 + 1]) {
                    data[i + 2] = (short) ((data[i + 2] & '\uffff') + (data[i + 3 + 2] & '\uffff'));
                    System.arraycopy(data, i + 6, data, i + 3, (this.mSize - 2) * 3 - i);
                    this.mSize--;
                } else {
                    i += 3;
                }
            }
        }
    }

    private static final class SkylineNew extends RectanglePacker {

        private static final int X_SHIFT = 0;

        private static final int Y_SHIFT = 16;

        private static final int WIDTH_SHIFT = 32;

        private static final int NEXT_SHIFT = 48;

        private static final int SLOT_MASK = 65535;

        private static final int NULL_PTR = 65535;

        private final long[] mData;

        private int mActiveHead;

        private int mFreeHead;

        private SkylineNew(int width, int height) {
            super(width, height);
            this.mData = new long[width + 10];
            this.clear();
        }

        @Override
        public void clear() {
            this.mArea = 0;
            this.mData[this.mActiveHead = 0] = (long) this.mWidth << 32 | -281474976710656L;
            int i = this.mFreeHead = 1;
            for (int e = this.mData.length; i < e; i++) {
                this.mData[i] = (long) (i + 1) << 48;
            }
        }

        @Override
        public boolean addRect(Rect2i rect) {
            int width = rect.width();
            int height = rect.height();
            if (width > 0 && height > 0) {
                if (width <= this.mWidth && height <= this.mHeight) {
                    int bestWidth = this.mWidth + 1;
                    int bestX = 0;
                    int bestY = this.mHeight + 1;
                    int bestIndex = -1;
                    int bestPrev = -1;
                    long[] data = this.mData;
                    int index = this.mActiveHead;
                    for (int prev = 65535; index != 65535; index = (int) (data[index] >>> 48)) {
                        long value = data[index];
                        int x = (int) (value >> 0 & 65535L);
                        if (x + width <= this.mWidth) {
                            int y = (int) (value >> 16 & 65535L);
                            int i = index;
                            int widthLeft = width;
                            while (true) {
                                if (widthLeft <= 0) {
                                    i = (int) (value >> 32 & 65535L);
                                    if (y < bestY || y == bestY && i < bestWidth) {
                                        bestIndex = index;
                                        bestPrev = prev;
                                        bestWidth = i;
                                        bestX = x;
                                        bestY = y;
                                    }
                                    break;
                                }
                                long v = data[i];
                                y = Math.max(y, (int) (v >> 16 & 65535L));
                                if (y + height > this.mHeight) {
                                    break;
                                }
                                widthLeft -= (int) (v >> 32 & 65535L);
                                i = (int) (v >>> 48);
                                assert i != 65535 || widthLeft <= 0;
                            }
                        }
                        prev = index;
                    }
                    if (bestIndex != -1) {
                        this.addLevel(bestPrev, bestIndex, bestX, bestY, width, height);
                        rect.offsetTo(bestX, bestY);
                        this.mArea += width * height;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                rect.offsetTo(0, 0);
                return true;
            }
        }

        private void addLevel(int prev, int index, int x, int y, int width, int height) {
            assert x + width <= this.mWidth;
            assert y + height <= this.mHeight;
            long[] data = this.mData;
            int freeIndex = this.mFreeHead;
            assert freeIndex != 65535;
            this.mFreeHead = (int) (data[freeIndex] >>> 48);
            data[freeIndex] = (long) x << 0 | (long) (y + height) << 16 | (long) width << 32 | (long) index << 48;
            if (prev == 65535) {
                assert index == this.mActiveHead;
                this.mActiveHead = freeIndex;
            } else {
                assert index != this.mActiveHead;
                assert (int) (data[prev] >>> 48) == index;
                data[prev] &= 281474976710655L;
                data[prev] |= (long) freeIndex << 48;
            }
            prev = freeIndex;
            freeIndex = index;
            while (freeIndex != 65535) {
                long value = data[freeIndex];
                int cx = (int) (data[freeIndex] >> 0 & 65535L);
                assert (long) cx >= (data[prev] >> 0 & 65535L);
                int right = (int) ((data[prev] >> 0 & 65535L) + (data[prev] >> 32 & 65535L));
                if (cx >= right) {
                    break;
                }
                int shrink = right - cx;
                int newWidth = (int) (value >> 32 & 65535L) - shrink;
                if (newWidth > 0) {
                    data[freeIndex] &= -281470681808896L;
                    data[freeIndex] |= (long) (cx + shrink) << 0;
                    data[freeIndex] |= (long) newWidth << 32;
                    break;
                }
                int next = (int) (value >>> 48);
                data[freeIndex] = (long) this.mFreeHead << 48;
                this.mFreeHead = freeIndex;
                data[prev] &= 281474976710655L;
                data[prev] |= (long) next << 48;
                freeIndex = next;
            }
            freeIndex = this.mActiveHead;
            do {
                long valuex = data[freeIndex];
                int next = (int) (valuex >>> 48);
                if (next == 65535) {
                    break;
                }
                int cy = (int) (valuex >> 16 & 65535L);
                int ny = (int) (data[next] >> 16 & 65535L);
                if (cy == ny) {
                    data[freeIndex] &= -281470681743361L;
                    int cw = (int) (valuex >> 32 & 65535L);
                    int nw = (int) (data[next] >> 32 & 65535L);
                    data[freeIndex] |= (long) (cw + nw) << 32;
                    int nextNext = (int) (data[next] >>> 48);
                    data[next] = (long) this.mFreeHead << 48;
                    this.mFreeHead = next;
                    data[freeIndex] &= 281474976710655L;
                    data[freeIndex] |= (long) nextNext << 48;
                    freeIndex = nextNext;
                } else {
                    freeIndex = next;
                }
            } while (freeIndex != 65535);
        }
    }
}