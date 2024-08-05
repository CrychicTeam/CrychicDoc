package icyllis.modernui.text;

import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.text.style.UpdateLayout;
import icyllis.modernui.text.style.WrapTogetherSpan;
import icyllis.modernui.util.GrowingArrayUtils;
import icyllis.modernui.util.Pools;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.lang.ref.WeakReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DynamicLayout extends Layout {

    private static final Pools.Pool<DynamicLayout.Builder> sPool = Pools.newSynchronizedPool(2);

    private static StaticLayout sStaticLayout = null;

    private static StaticLayout.Builder sBuilder = null;

    private static final Object[] sLock = new Object[0];

    private static final int PRIORITY = 128;

    private static final int BLOCK_MINIMUM_CHARACTER_LENGTH = 400;

    private static final int START = 0;

    private static final int DIR = 0;

    private static final int TAB = 0;

    private static final int TOP = 1;

    private static final int DESCENT = 2;

    private static final int COLUMNS_NORMAL = 3;

    private static final int ELLIPSIS_START = 3;

    private static final int ELLIPSIS_COUNT = 4;

    private static final int COLUMNS_ELLIPSIZE = 5;

    private static final int START_MASK = 536870911;

    private static final int DIR_SHIFT = 30;

    private static final int TAB_MASK = 536870912;

    private static final int ELLIPSIS_UNDEFINED = Integer.MIN_VALUE;

    private CharSequence mBase;

    private final CharSequence mDisplay;

    private DynamicLayout.ChangeWatcher mWatcher;

    private final boolean mIncludePad;

    private boolean mFallbackLineSpacing;

    private boolean mEllipsize;

    private int mEllipsizedWidth;

    private TextUtils.TruncateAt mEllipsizeAt;

    private PackedIntVector mInts;

    private PackedObjectVector<Directions> mObjects;

    public static final int INVALID_BLOCK_INDEX = -1;

    private int[] mBlockEndLines;

    private int[] mBlockIndices;

    private IntArrayList mBlocksAlwaysNeedToBeRedrawn;

    private int mNumberOfBlocks;

    private int mIndexFirstChangedBlock;

    private int mTopPadding;

    private int mBottomPadding;

    @Nonnull
    public static DynamicLayout.Builder builder(@Nonnull CharSequence base, @Nonnull TextPaint paint, int width) {
        DynamicLayout.Builder b = sPool.acquire();
        if (b == null) {
            b = new DynamicLayout.Builder();
        }
        b.mBase = base;
        b.mDisplay = base;
        b.mPaint = paint;
        b.mWidth = width;
        b.mAlignment = Layout.Alignment.ALIGN_NORMAL;
        b.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
        b.mIncludePad = true;
        b.mFallbackLineSpacing = true;
        b.mEllipsizedWidth = width;
        b.mEllipsize = null;
        return b;
    }

    private DynamicLayout(@Nonnull DynamicLayout.Builder b) {
        super(createEllipsizer(b.mEllipsize, b.mDisplay), b.mPaint, b.mWidth, b.mAlignment, b.mTextDir);
        this.mDisplay = b.mDisplay;
        this.mIncludePad = b.mIncludePad;
        this.generate(b);
    }

    @Nonnull
    private static CharSequence createEllipsizer(@Nullable TextUtils.TruncateAt ellipsize, @Nonnull CharSequence display) {
        if (ellipsize == null) {
            return display;
        } else {
            return (CharSequence) (display instanceof Spanned ? new Layout.SpannedEllipsizer(display) : new Layout.Ellipsizer(display));
        }
    }

    private void generate(@Nonnull DynamicLayout.Builder b) {
        this.mBase = b.mBase;
        this.mFallbackLineSpacing = b.mFallbackLineSpacing;
        if (b.mEllipsize != null) {
            this.mInts = new PackedIntVector(5);
            this.mEllipsizedWidth = b.mEllipsizedWidth;
            this.mEllipsizeAt = b.mEllipsize;
            Layout.Ellipsizer e = (Layout.Ellipsizer) this.getText();
            e.mLayout = this;
            e.mWidth = b.mEllipsizedWidth;
            e.mMethod = b.mEllipsize;
            this.mEllipsize = true;
        } else {
            this.mInts = new PackedIntVector(3);
            this.mEllipsizedWidth = b.mWidth;
            this.mEllipsizeAt = null;
        }
        this.mObjects = new PackedObjectVector<>(1);
        int[] start;
        if (b.mEllipsize != null) {
            start = new int[5];
            start[3] = Integer.MIN_VALUE;
        } else {
            start = new int[3];
        }
        Directions[] dirs = new Directions[] { Directions.ALL_LEFT_TO_RIGHT };
        FontMetricsInt fm = b.mFontMetricsInt;
        b.mPaint.getFontMetricsInt(fm);
        int asc = fm.ascent;
        int desc = fm.descent;
        start[0] = 1073741824;
        start[1] = 0;
        start[2] = desc;
        this.mInts.insertAt(0, start);
        start[1] = desc - asc;
        this.mInts.insertAt(1, start);
        this.mObjects.insertAt(0, dirs);
        this.reflow(this.mBase, 0, 0, this.mDisplay.length());
        if (this.mBase instanceof Spannable sp) {
            if (this.mWatcher == null) {
                this.mWatcher = new DynamicLayout.ChangeWatcher(this);
            }
            int baseLength = this.mBase.length();
            for (DynamicLayout.ChangeWatcher span : sp.getSpans(0, baseLength, DynamicLayout.ChangeWatcher.class)) {
                sp.removeSpan(span);
            }
            sp.setSpan(this.mWatcher, 0, baseLength, 8388626);
        }
    }

    public void reflow(CharSequence s, int where, int before, int after) {
        if (s == this.mBase) {
            CharSequence text = this.mDisplay;
            int len = text.length();
            int find = TextUtils.lastIndexOf(text, '\n', where - 1);
            if (find < 0) {
                find = 0;
            } else {
                find++;
            }
            int diff = where - find;
            before += diff;
            after += diff;
            where -= diff;
            diff = TextUtils.indexOf(text, '\n', where + after);
            if (diff < 0) {
                diff = len;
            } else {
                diff++;
            }
            int change = diff - (where + after);
            before += change;
            after += change;
            boolean again;
            if (text instanceof Spanned sp) {
                do {
                    again = (boolean) 0;
                    for (Object span : sp.getSpans(where, where + after, WrapTogetherSpan.class)) {
                        int st = sp.getSpanStart(span);
                        int en = sp.getSpanEnd(span);
                        if (st < where) {
                            again = (boolean) 1;
                            int diffx = where - st;
                            before += diffx;
                            after += diffx;
                            where -= diffx;
                        }
                        if (en > where + after) {
                            again = (boolean) 1;
                            int diffx = en - (where + after);
                            before += diffx;
                            after += diffx;
                        }
                    }
                } while (again);
            }
            int startline = this.getLineForOffset(where);
            again = (boolean) this.getLineTop(startline);
            int endline = this.getLineForOffset(where + before);
            if (where + after == len) {
                endline = this.getLineCount();
            }
            int endv = this.getLineTop(endline);
            boolean islast = endline == this.getLineCount();
            StaticLayout reflowed;
            StaticLayout.Builder b;
            synchronized (sLock) {
                reflowed = sStaticLayout;
                b = sBuilder;
                sStaticLayout = null;
                sBuilder = null;
            }
            if (reflowed == null) {
                reflowed = new StaticLayout(null);
                b = StaticLayout.builder(text, where, where + after, this.getPaint(), this.getWidth());
            }
            b.setText(text, where, where + after).setPaint(this.getPaint()).setWidth(this.getWidth()).setTextDirection(this.getTextDirectionHeuristic()).setFallbackLineSpacing(this.mFallbackLineSpacing).setEllipsizedWidth(this.mEllipsizedWidth).setEllipsize(this.mEllipsizeAt);
            reflowed.generate(b, false, true);
            int n = reflowed.getLineCount();
            if (where + after != len && reflowed.getLineStart(n - 1) == where + after) {
                n--;
            }
            this.mInts.deleteAt(startline, endline - startline);
            this.mObjects.deleteAt(startline, endline - startline);
            int ht = reflowed.getLineTop(n);
            int toppad = 0;
            int botpad = 0;
            if (this.mIncludePad && startline == 0) {
                toppad = reflowed.getTopPadding();
                this.mTopPadding = toppad;
                ht -= toppad;
            }
            if (this.mIncludePad && islast) {
                botpad = reflowed.getBottomPadding();
                this.mBottomPadding = botpad;
                ht += botpad;
            }
            this.mInts.adjustValuesBelow(startline, 0, after - before);
            this.mInts.adjustValuesBelow(startline, 1, again - endv + ht);
            int[] ints;
            if (this.mEllipsize) {
                ints = new int[5];
                ints[3] = Integer.MIN_VALUE;
            } else {
                ints = new int[3];
            }
            Directions[] objects = new Directions[1];
            for (int i = 0; i < n; i++) {
                int start = reflowed.getLineStart(i);
                ints[0] = start;
                ints[0] |= reflowed.getParagraphDirection(i) << 30;
                ints[0] |= reflowed.getLineContainsTab(i) ? 536870912 : 0;
                int top = reflowed.getLineTop(i) + again;
                if (i > 0) {
                    top -= toppad;
                }
                ints[1] = top;
                int desc = reflowed.getLineDescent(i);
                if (i == n - 1) {
                    desc += botpad;
                }
                ints[2] = desc;
                objects[0] = reflowed.getLineDirections(i);
                if (this.mEllipsize) {
                    ints[3] = reflowed.getEllipsisStart(i);
                    ints[4] = reflowed.getEllipsisCount(i);
                }
                this.mInts.insertAt(startline + i, ints);
                this.mObjects.insertAt(startline + i, objects);
            }
            this.updateBlocks(startline, endline - 1, n);
            b.release();
            synchronized (sLock) {
                sStaticLayout = reflowed;
                sBuilder = b;
            }
        }
    }

    private void createBlocks() {
        int offset = 400;
        this.mNumberOfBlocks = 0;
        CharSequence text = this.mDisplay;
        while (true) {
            offset = TextUtils.indexOf(text, '\n', offset);
            if (offset < 0) {
                this.addBlockAtOffset(text.length());
                this.mBlockIndices = new int[this.mBlockEndLines.length];
                for (int i = 0; i < this.mBlockEndLines.length; i++) {
                    this.mBlockIndices[i] = -1;
                }
                return;
            }
            this.addBlockAtOffset(offset);
            offset += 400;
        }
    }

    @Nullable
    public IntArrayList getBlocksAlwaysNeedToBeRedrawn() {
        return this.mBlocksAlwaysNeedToBeRedrawn;
    }

    private void updateAlwaysNeedsToBeRedrawn(int blockIndex) {
        if (this.mBlocksAlwaysNeedToBeRedrawn == null) {
            this.mBlocksAlwaysNeedToBeRedrawn = new IntArrayList();
        }
        if (!this.mBlocksAlwaysNeedToBeRedrawn.contains(blockIndex)) {
            this.mBlocksAlwaysNeedToBeRedrawn.add(blockIndex);
        }
    }

    private void addBlockAtOffset(int offset) {
        int line = this.getLineForOffset(offset);
        if (this.mBlockEndLines == null) {
            this.mBlockEndLines = new int[1];
            this.mBlockEndLines[this.mNumberOfBlocks] = line;
            this.updateAlwaysNeedsToBeRedrawn(this.mNumberOfBlocks);
            this.mNumberOfBlocks++;
        } else {
            int previousBlockEndLine = this.mBlockEndLines[this.mNumberOfBlocks - 1];
            if (line > previousBlockEndLine) {
                this.mBlockEndLines = GrowingArrayUtils.append(this.mBlockEndLines, this.mNumberOfBlocks, line);
                this.updateAlwaysNeedsToBeRedrawn(this.mNumberOfBlocks);
                this.mNumberOfBlocks++;
            }
        }
    }

    public void updateBlocks(int startLine, int endLine, int newLineCount) {
        if (this.mBlockEndLines == null) {
            this.createBlocks();
        } else {
            int firstBlock = -1;
            int lastBlock = -1;
            for (int i = 0; i < this.mNumberOfBlocks; i++) {
                if (this.mBlockEndLines[i] >= startLine) {
                    firstBlock = i;
                    break;
                }
            }
            for (int ix = firstBlock; ix < this.mNumberOfBlocks; ix++) {
                if (this.mBlockEndLines[ix] >= endLine) {
                    lastBlock = ix;
                    break;
                }
            }
            int lastBlockEndLine = this.mBlockEndLines[lastBlock];
            boolean createBlockBefore = startLine > (firstBlock == 0 ? 0 : this.mBlockEndLines[firstBlock - 1] + 1);
            boolean createBlock = newLineCount > 0;
            boolean createBlockAfter = endLine < this.mBlockEndLines[lastBlock];
            int numAddedBlocks = 0;
            if (createBlockBefore) {
                numAddedBlocks++;
            }
            if (createBlock) {
                numAddedBlocks++;
            }
            if (createBlockAfter) {
                numAddedBlocks++;
            }
            int numRemovedBlocks = lastBlock - firstBlock + 1;
            int newNumberOfBlocks = this.mNumberOfBlocks + numAddedBlocks - numRemovedBlocks;
            if (newNumberOfBlocks == 0) {
                this.mBlockEndLines[0] = 0;
                this.mBlockIndices[0] = -1;
                this.mNumberOfBlocks = 1;
            } else {
                if (newNumberOfBlocks > this.mBlockEndLines.length) {
                    int[] blockEndLines = new int[Math.max(this.mBlockEndLines.length * 2, newNumberOfBlocks)];
                    int[] blockIndices = new int[blockEndLines.length];
                    System.arraycopy(this.mBlockEndLines, 0, blockEndLines, 0, firstBlock);
                    System.arraycopy(this.mBlockIndices, 0, blockIndices, 0, firstBlock);
                    System.arraycopy(this.mBlockEndLines, lastBlock + 1, blockEndLines, firstBlock + numAddedBlocks, this.mNumberOfBlocks - lastBlock - 1);
                    System.arraycopy(this.mBlockIndices, lastBlock + 1, blockIndices, firstBlock + numAddedBlocks, this.mNumberOfBlocks - lastBlock - 1);
                    this.mBlockEndLines = blockEndLines;
                    this.mBlockIndices = blockIndices;
                } else if (numAddedBlocks + numRemovedBlocks != 0) {
                    System.arraycopy(this.mBlockEndLines, lastBlock + 1, this.mBlockEndLines, firstBlock + numAddedBlocks, this.mNumberOfBlocks - lastBlock - 1);
                    System.arraycopy(this.mBlockIndices, lastBlock + 1, this.mBlockIndices, firstBlock + numAddedBlocks, this.mNumberOfBlocks - lastBlock - 1);
                }
                if (numAddedBlocks + numRemovedBlocks != 0 && this.mBlocksAlwaysNeedToBeRedrawn != null) {
                    IntArrayList list = new IntArrayList();
                    int changedBlockCount = numAddedBlocks - numRemovedBlocks;
                    for (int ixx = 0; ixx < this.mBlocksAlwaysNeedToBeRedrawn.size(); ixx++) {
                        int block = this.mBlocksAlwaysNeedToBeRedrawn.getInt(ixx);
                        if (block < firstBlock && !list.contains(block)) {
                            list.add(block);
                        }
                        if (block > lastBlock) {
                            block += changedBlockCount;
                            if (!list.contains(block)) {
                                list.add(block);
                            }
                        }
                    }
                    this.mBlocksAlwaysNeedToBeRedrawn = list;
                }
                this.mNumberOfBlocks = newNumberOfBlocks;
                int deltaLines = newLineCount - (endLine - startLine + 1);
                int newFirstChangedBlock;
                if (deltaLines != 0) {
                    newFirstChangedBlock = firstBlock + numAddedBlocks;
                    for (int ixx = newFirstChangedBlock; ixx < this.mNumberOfBlocks; ixx++) {
                        this.mBlockEndLines[ixx] = this.mBlockEndLines[ixx] + deltaLines;
                    }
                } else {
                    newFirstChangedBlock = this.mNumberOfBlocks;
                }
                this.mIndexFirstChangedBlock = Math.min(this.mIndexFirstChangedBlock, newFirstChangedBlock);
                int blockIndex = firstBlock;
                if (createBlockBefore) {
                    this.mBlockEndLines[firstBlock] = startLine - 1;
                    this.updateAlwaysNeedsToBeRedrawn(firstBlock);
                    this.mBlockIndices[firstBlock] = -1;
                    blockIndex = firstBlock + 1;
                }
                if (createBlock) {
                    this.mBlockEndLines[blockIndex] = startLine + newLineCount - 1;
                    this.updateAlwaysNeedsToBeRedrawn(blockIndex);
                    this.mBlockIndices[blockIndex] = -1;
                    blockIndex++;
                }
                if (createBlockAfter) {
                    this.mBlockEndLines[blockIndex] = lastBlockEndLine + deltaLines;
                    this.updateAlwaysNeedsToBeRedrawn(blockIndex);
                    this.mBlockIndices[blockIndex] = -1;
                }
            }
        }
    }

    public void setBlocksDataForTest(int[] blockEndLines, int[] blockIndices, int numberOfBlocks, int totalLines) {
        this.mBlockEndLines = new int[blockEndLines.length];
        this.mBlockIndices = new int[blockIndices.length];
        System.arraycopy(blockEndLines, 0, this.mBlockEndLines, 0, blockEndLines.length);
        System.arraycopy(blockIndices, 0, this.mBlockIndices, 0, blockIndices.length);
        this.mNumberOfBlocks = numberOfBlocks;
        while (this.mInts.size() < totalLines) {
            this.mInts.insertAt(this.mInts.size(), new int[3]);
        }
    }

    public int[] getBlockEndLines() {
        return this.mBlockEndLines;
    }

    public int[] getBlockIndices() {
        return this.mBlockIndices;
    }

    public int getBlockIndex(int index) {
        return this.mBlockIndices[index];
    }

    public void setBlockIndex(int index, int blockIndex) {
        this.mBlockIndices[index] = blockIndex;
    }

    public int getNumberOfBlocks() {
        return this.mNumberOfBlocks;
    }

    public int getIndexFirstChangedBlock() {
        return this.mIndexFirstChangedBlock;
    }

    public void setIndexFirstChangedBlock(int i) {
        this.mIndexFirstChangedBlock = i;
    }

    @Override
    public int getLineCount() {
        return this.mInts.size() - 1;
    }

    @Override
    public int getLineTop(int line) {
        return this.mInts.getValue(line, 1);
    }

    @Override
    public int getLineDescent(int line) {
        return this.mInts.getValue(line, 2);
    }

    @Override
    public int getLineStart(int line) {
        return this.mInts.getValue(line, 0) & 536870911;
    }

    @Override
    public boolean getLineContainsTab(int line) {
        return (this.mInts.getValue(line, 0) & 536870912) != 0;
    }

    @Override
    public int getParagraphDirection(int line) {
        return this.mInts.getValue(line, 0) >> 30;
    }

    @Override
    public final Directions getLineDirections(int line) {
        return this.mObjects.getValue(line, 0);
    }

    @Override
    public int getTopPadding() {
        return this.mTopPadding;
    }

    @Override
    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    @Override
    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    @Override
    public int getEllipsisStart(int line) {
        return this.mEllipsizeAt == null ? 0 : this.mInts.getValue(line, 3);
    }

    @Override
    public int getEllipsisCount(int line) {
        return this.mEllipsizeAt == null ? 0 : this.mInts.getValue(line, 4);
    }

    public static final class Builder {

        private final FontMetricsInt mFontMetricsInt = new FontMetricsInt();

        private CharSequence mBase;

        private CharSequence mDisplay;

        private TextPaint mPaint;

        private int mWidth;

        private Layout.Alignment mAlignment;

        private TextDirectionHeuristic mTextDir;

        private boolean mIncludePad;

        private boolean mFallbackLineSpacing;

        private TextUtils.TruncateAt mEllipsize;

        private int mEllipsizedWidth;

        private Builder() {
        }

        private void recycle() {
            this.mBase = null;
            this.mDisplay = null;
            this.mPaint = null;
            DynamicLayout.sPool.release(this);
        }

        @Nonnull
        public DynamicLayout.Builder setDisplayText(@Nonnull CharSequence display) {
            this.mDisplay = display;
            return this;
        }

        @Nonnull
        public DynamicLayout.Builder setAlignment(@Nonnull Layout.Alignment alignment) {
            this.mAlignment = alignment;
            return this;
        }

        @Nonnull
        public DynamicLayout.Builder setTextDirection(@Nonnull TextDirectionHeuristic textDir) {
            this.mTextDir = textDir;
            return this;
        }

        @Nonnull
        public DynamicLayout.Builder setIncludePad(boolean includePad) {
            this.mIncludePad = includePad;
            return this;
        }

        @Nonnull
        public DynamicLayout.Builder setFallbackLineSpacing(boolean fallbackLineSpacing) {
            this.mFallbackLineSpacing = fallbackLineSpacing;
            return this;
        }

        @Nonnull
        public DynamicLayout.Builder setEllipsizedWidth(int ellipsizedWidth) {
            this.mEllipsizedWidth = ellipsizedWidth;
            return this;
        }

        public DynamicLayout.Builder setEllipsize(@Nullable TextUtils.TruncateAt ellipsize) {
            this.mEllipsize = ellipsize;
            return this;
        }

        @Nonnull
        public DynamicLayout build() {
            DynamicLayout result = new DynamicLayout(this);
            this.recycle();
            return result;
        }
    }

    private static class ChangeWatcher implements TextWatcher, SpanWatcher {

        private final WeakReference<DynamicLayout> mLayout;

        public ChangeWatcher(DynamicLayout layout) {
            this.mLayout = new WeakReference(layout);
        }

        private void reflow(CharSequence s, int where, int before, int after) {
            DynamicLayout ml = (DynamicLayout) this.mLayout.get();
            if (ml != null) {
                ml.reflow(s, where, before, after);
            } else if (s instanceof Spannable) {
                ((Spannable) s).removeSpan(this);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int where, int before, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int where, int before, int after) {
            this.reflow(s, where, before, after);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onSpanAdded(Spannable s, Object o, int start, int end) {
            if (o instanceof UpdateLayout) {
                this.reflow(s, start, end - start, end - start);
            }
        }

        @Override
        public void onSpanRemoved(Spannable s, Object o, int start, int end) {
            if (o instanceof UpdateLayout) {
                this.reflow(s, start, end - start, end - start);
            }
        }

        @Override
        public void onSpanChanged(Spannable s, Object o, int start, int end, int nstart, int nend) {
            if (o instanceof UpdateLayout) {
                if (start > end) {
                    start = 0;
                }
                this.reflow(s, start, end - start, end - start);
                this.reflow(s, nstart, nend - nstart, nend - nstart);
            }
        }
    }
}