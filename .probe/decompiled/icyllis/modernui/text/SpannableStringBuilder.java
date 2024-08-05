package icyllis.modernui.text;

import icyllis.modernui.ModernUI;
import icyllis.modernui.util.GrowingArrayUtils;
import icyllis.modernui.util.Pools;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class SpannableStringBuilder implements Editable, Spannable, GetChars, Appendable {

    public static final Marker MARKER = MarkerManager.getMarker("SpannableStringBuilder");

    private static final Pools.Pool<IntArrayList> sIntBufferPool = Pools.newSynchronizedPool(2);

    private static final int MARK = 1;

    private static final int POINT = 2;

    private static final int PARAGRAPH = 3;

    private static final int START_MASK = 240;

    private static final int END_MASK = 15;

    private static final int START_SHIFT = 4;

    private static final int SPAN_ADDED = 2048;

    private static final int SPAN_START_AT_START = 4096;

    private static final int SPAN_START_AT_END = 8192;

    private static final int SPAN_END_AT_START = 16384;

    private static final int SPAN_END_AT_END = 32768;

    private static final int SPAN_START_END_MASK = 61440;

    private static final InputFilter[] NO_FILTERS = new InputFilter[0];

    @Nonnull
    private InputFilter[] mFilters = NO_FILTERS;

    private char[] mText;

    private int mGapStart;

    private int mGapLength;

    private Object[] mSpans;

    private int[] mSpanStarts;

    private int[] mSpanEnds;

    private int[] mSpanMax;

    private int[] mSpanFlags;

    private int[] mSpanOrder;

    private int mSpanInsertCount;

    private int mSpanCount;

    @Nullable
    private IdentityHashMap<Object, Integer> mIndexOfSpan;

    private int mLowWaterMark;

    private int mTextWatcherDepth;

    public SpannableStringBuilder() {
        this("");
    }

    public SpannableStringBuilder(@Nonnull CharSequence text) {
        this(text, 0, text.length());
    }

    public SpannableStringBuilder(@Nonnull CharSequence text, int start, int end) {
        int srcLen = end - start;
        if (srcLen < 0) {
            throw new StringIndexOutOfBoundsException();
        } else {
            this.mText = new char[GrowingArrayUtils.growSize(srcLen)];
            this.mGapStart = srcLen;
            this.mGapLength = this.mText.length - srcLen;
            TextUtils.getChars(text, start, end, this.mText, 0);
            this.mSpanCount = 0;
            this.mSpanInsertCount = 0;
            this.mSpans = ObjectArrays.EMPTY_ARRAY;
            this.mSpanStarts = IntArrays.EMPTY_ARRAY;
            this.mSpanEnds = IntArrays.EMPTY_ARRAY;
            this.mSpanFlags = IntArrays.EMPTY_ARRAY;
            this.mSpanMax = IntArrays.EMPTY_ARRAY;
            this.mSpanOrder = IntArrays.EMPTY_ARRAY;
            if (text instanceof Spanned sp) {
                for (Object span : sp.getSpans(start, end, Object.class)) {
                    if (!(span instanceof NoCopySpan)) {
                        int st = sp.getSpanStart(span) - start;
                        int en = sp.getSpanEnd(span) - start;
                        int fl = sp.getSpanFlags(span);
                        if (st < 0) {
                            st = 0;
                        }
                        if (st > end - start) {
                            st = end - start;
                        }
                        if (en < 0) {
                            en = 0;
                        }
                        if (en > end - start) {
                            en = end - start;
                        }
                        this.setSpan(false, span, st, en, fl, false);
                    }
                }
                this.restoreInvariants();
            }
        }
    }

    @Nonnull
    public static SpannableStringBuilder valueOf(@Nonnull CharSequence source) {
        return source instanceof SpannableStringBuilder ? (SpannableStringBuilder) source : new SpannableStringBuilder(source);
    }

    public char charAt(int where) {
        int len = this.length();
        if (where < 0) {
            throw new IndexOutOfBoundsException("charAt: " + where + " < 0");
        } else if (where >= len) {
            throw new IndexOutOfBoundsException("charAt: " + where + " >= length " + len);
        } else {
            return where >= this.mGapStart ? this.mText[where + this.mGapLength] : this.mText[where];
        }
    }

    public int length() {
        return this.mText.length - this.mGapLength;
    }

    private void resizeFor(int size) {
        int oldLength = this.mText.length;
        if (size + 1 > oldLength) {
            char[] newText = new char[GrowingArrayUtils.growSize(size)];
            System.arraycopy(this.mText, 0, newText, 0, this.mGapStart);
            int newLength = newText.length;
            int delta = newLength - oldLength;
            int after = oldLength - (this.mGapStart + this.mGapLength);
            System.arraycopy(this.mText, oldLength - after, newText, newLength - after, after);
            this.mText = newText;
            this.mGapLength += delta;
            if (this.mGapLength < 1) {
                new Exception("mGapLength < 1").printStackTrace();
            }
            if (this.mSpanCount != 0) {
                for (int i = 0; i < this.mSpanCount; i++) {
                    if (this.mSpanStarts[i] > this.mGapStart) {
                        this.mSpanStarts[i] = this.mSpanStarts[i] + delta;
                    }
                    if (this.mSpanEnds[i] > this.mGapStart) {
                        this.mSpanEnds[i] = this.mSpanEnds[i] + delta;
                    }
                }
                this.calcMax(this.treeRoot());
            }
        }
    }

    private void moveGapTo(int where) {
        if (where != this.mGapStart) {
            boolean atEnd = where == this.length();
            if (where < this.mGapStart) {
                int overlap = this.mGapStart - where;
                System.arraycopy(this.mText, where, this.mText, this.mGapStart + this.mGapLength - overlap, overlap);
            } else {
                int overlap = where - this.mGapStart;
                System.arraycopy(this.mText, where + this.mGapLength - overlap, this.mText, this.mGapStart, overlap);
            }
            if (this.mSpanCount != 0) {
                for (int i = 0; i < this.mSpanCount; i++) {
                    int start = this.mSpanStarts[i];
                    int end = this.mSpanEnds[i];
                    if (start > this.mGapStart) {
                        start -= this.mGapLength;
                    }
                    if (start > where) {
                        start += this.mGapLength;
                    } else if (start == where) {
                        int flag = (this.mSpanFlags[i] & 240) >> 4;
                        if (flag == 2 || atEnd && flag == 3) {
                            start += this.mGapLength;
                        }
                    }
                    if (end > this.mGapStart) {
                        end -= this.mGapLength;
                    }
                    if (end > where) {
                        end += this.mGapLength;
                    } else if (end == where) {
                        int flag = this.mSpanFlags[i] & 15;
                        if (flag == 2 || atEnd && flag == 3) {
                            end += this.mGapLength;
                        }
                    }
                    this.mSpanStarts[i] = start;
                    this.mSpanEnds[i] = end;
                }
                this.calcMax(this.treeRoot());
            }
            this.mGapStart = where;
        }
    }

    public SpannableStringBuilder insert(int where, @Nonnull CharSequence tb, int start, int end) {
        return this.replace(where, where, tb, start, end);
    }

    public SpannableStringBuilder insert(int where, @Nonnull CharSequence tb) {
        return this.replace(where, where, tb, 0, tb.length());
    }

    public SpannableStringBuilder delete(int start, int end) {
        SpannableStringBuilder ret = this.replace(start, end, "", 0, 0);
        if (this.mGapLength > 2 * this.length()) {
            this.resizeFor(this.length());
        }
        return ret;
    }

    @Override
    public void clear() {
        this.replace(0, this.length(), "", 0, 0);
        this.mSpanInsertCount = 0;
    }

    @Override
    public void clearSpans() {
        for (int i = this.mSpanCount - 1; i >= 0; i--) {
            Object what = this.mSpans[i];
            int ostart = this.mSpanStarts[i];
            int oend = this.mSpanEnds[i];
            if (ostart > this.mGapStart) {
                ostart -= this.mGapLength;
            }
            if (oend > this.mGapStart) {
                oend -= this.mGapLength;
            }
            this.mSpanCount = i;
            this.mSpans[i] = null;
            this.sendSpanRemoved(what, ostart, oend);
        }
        if (this.mIndexOfSpan != null) {
            this.mIndexOfSpan.clear();
        }
        this.mSpanInsertCount = 0;
    }

    public SpannableStringBuilder append(@Nonnull CharSequence text) {
        int length = this.length();
        return this.replace(length, length, text, 0, text.length());
    }

    public SpannableStringBuilder append(@Nonnull CharSequence text, @Nonnull Object what, int flags) {
        int start = this.length();
        this.append(text);
        this.setSpan(what, start, this.length(), flags);
        return this;
    }

    public SpannableStringBuilder append(@Nonnull CharSequence text, int start, int end) {
        int length = this.length();
        return this.replace(length, length, text, start, end);
    }

    public SpannableStringBuilder append(char text) {
        return this.append(String.valueOf(text));
    }

    private boolean removeSpansForChange(int start, int end, boolean textIsRemoved, int i) {
        if ((i & 1) != 0 && this.resolveGap(this.mSpanMax[i]) >= start && this.removeSpansForChange(start, end, textIsRemoved, leftChild(i))) {
            return true;
        } else if (i >= this.mSpanCount) {
            return false;
        } else if ((this.mSpanFlags[i] & 33) != 33 || this.mSpanStarts[i] < start || this.mSpanStarts[i] >= this.mGapStart + this.mGapLength || this.mSpanEnds[i] < start || this.mSpanEnds[i] >= this.mGapStart + this.mGapLength || !textIsRemoved && this.mSpanStarts[i] <= start && this.mSpanEnds[i] >= this.mGapStart) {
            return this.resolveGap(this.mSpanStarts[i]) <= end && (i & 1) != 0 && this.removeSpansForChange(start, end, textIsRemoved, rightChild(i));
        } else {
            assert this.mIndexOfSpan != null;
            this.mIndexOfSpan.remove(this.mSpans[i]);
            this.removeSpan(i, 0);
            return true;
        }
    }

    private void change(int start, int end, @Nonnull CharSequence cs, int csStart, int csEnd) {
        int replacedLength = end - start;
        int replacementLength = csEnd - csStart;
        int nbNewChars = replacementLength - replacedLength;
        boolean changed = false;
        for (int i = this.mSpanCount - 1; i >= 0; i--) {
            int spanStart = this.mSpanStarts[i];
            if (spanStart > this.mGapStart) {
                spanStart -= this.mGapLength;
            }
            int spanEnd = this.mSpanEnds[i];
            if (spanEnd > this.mGapStart) {
                spanEnd -= this.mGapLength;
            }
            if ((this.mSpanFlags[i] & 51) == 51) {
                int ost = spanStart;
                int oen = spanEnd;
                int clen = this.length();
                if (spanStart > start && spanStart <= end) {
                    spanStart = end;
                    while (spanStart < clen && (spanStart <= end || this.charAt(spanStart - 1) != '\n')) {
                        spanStart++;
                    }
                }
                if (spanEnd > start && spanEnd <= end) {
                    spanEnd = end;
                    while (spanEnd < clen && (spanEnd <= end || this.charAt(spanEnd - 1) != '\n')) {
                        spanEnd++;
                    }
                }
                if (spanStart != ost || spanEnd != oen) {
                    this.setSpan(false, this.mSpans[i], spanStart, spanEnd, this.mSpanFlags[i], true);
                    changed = true;
                }
            }
            int flags = 0;
            if (spanStart == start) {
                flags |= 4096;
            } else if (spanStart == end + nbNewChars) {
                flags |= 8192;
            }
            if (spanEnd == start) {
                flags |= 16384;
            } else if (spanEnd == end + nbNewChars) {
                flags |= 32768;
            }
            this.mSpanFlags[i] = this.mSpanFlags[i] | flags;
        }
        if (changed) {
            this.restoreInvariants();
        }
        this.moveGapTo(end);
        if (nbNewChars >= this.mGapLength) {
            this.resizeFor(this.mText.length + nbNewChars - this.mGapLength);
        }
        boolean textIsRemoved = replacementLength == 0;
        if (replacedLength > 0) {
            while (this.mSpanCount > 0 && this.removeSpansForChange(start, end, textIsRemoved, this.treeRoot())) {
            }
        }
        this.mGapStart += nbNewChars;
        this.mGapLength -= nbNewChars;
        if (this.mGapLength < 1) {
            new Exception("mGapLength < 1").printStackTrace();
        }
        TextUtils.getChars(cs, csStart, csEnd, this.mText, start);
        if (replacedLength > 0) {
            boolean atEnd = this.mGapStart + this.mGapLength == this.mText.length;
            for (int i = 0; i < this.mSpanCount; i++) {
                int startFlag = (this.mSpanFlags[i] & 240) >> 4;
                this.mSpanStarts[i] = this.updatedIntervalBound(this.mSpanStarts[i], start, nbNewChars, startFlag, atEnd, textIsRemoved);
                int endFlag = this.mSpanFlags[i] & 15;
                this.mSpanEnds[i] = this.updatedIntervalBound(this.mSpanEnds[i], start, nbNewChars, endFlag, atEnd, textIsRemoved);
            }
            this.restoreInvariants();
        }
        if (cs instanceof Spanned sp) {
            for (Object span : sp.getSpans(csStart, csEnd, Object.class)) {
                int st = sp.getSpanStart(span);
                int en = sp.getSpanEnd(span);
                if (st < csStart) {
                    st = csStart;
                }
                if (en > csEnd) {
                    en = csEnd;
                }
                if (this.getSpanStart(span) < 0) {
                    int copySpanStart = st - csStart + start;
                    int copySpanEnd = en - csStart + start;
                    int copySpanFlags = sp.getSpanFlags(span) | 2048;
                    this.setSpan(false, span, copySpanStart, copySpanEnd, copySpanFlags, false);
                }
            }
            this.restoreInvariants();
        }
    }

    private int updatedIntervalBound(int offset, int start, int nbNewChars, int flag, boolean atEnd, boolean textIsRemoved) {
        if (offset >= start && offset < this.mGapStart + this.mGapLength) {
            if (flag == 2) {
                if (textIsRemoved || offset > start) {
                    return this.mGapStart + this.mGapLength;
                }
            } else {
                if (flag != 3) {
                    if (!textIsRemoved && offset >= this.mGapStart - nbNewChars) {
                        return this.mGapStart;
                    }
                    return start;
                }
                if (atEnd) {
                    return this.mGapStart + this.mGapLength;
                }
            }
        }
        return offset;
    }

    private void removeSpan(int i, int flags) {
        Object object = this.mSpans[i];
        int start = this.mSpanStarts[i];
        int end = this.mSpanEnds[i];
        if (start > this.mGapStart) {
            start -= this.mGapLength;
        }
        if (end > this.mGapStart) {
            end -= this.mGapLength;
        }
        int count = this.mSpanCount - (i + 1);
        System.arraycopy(this.mSpans, i + 1, this.mSpans, i, count);
        System.arraycopy(this.mSpanStarts, i + 1, this.mSpanStarts, i, count);
        System.arraycopy(this.mSpanEnds, i + 1, this.mSpanEnds, i, count);
        System.arraycopy(this.mSpanFlags, i + 1, this.mSpanFlags, i, count);
        System.arraycopy(this.mSpanOrder, i + 1, this.mSpanOrder, i, count);
        this.mSpanCount--;
        this.invalidateIndex(i);
        this.mSpans[this.mSpanCount] = null;
        this.restoreInvariants();
        if ((flags & 512) == 0) {
            this.sendSpanRemoved(object, start, end);
        }
    }

    public SpannableStringBuilder replace(int start, int end, @Nonnull CharSequence tb) {
        return this.replace(start, end, tb, 0, tb.length());
    }

    public SpannableStringBuilder replace(int start, int end, @Nonnull CharSequence tb, int tbstart, int tbend) {
        this.checkRange("replace", start, end);
        for (InputFilter filter : this.mFilters) {
            CharSequence repl = filter.filter(tb, tbstart, tbend, this, start, end);
            if (repl != null) {
                tb = repl;
                tbstart = 0;
                tbend = repl.length();
            }
        }
        int origLen = end - start;
        int newLen = tbend - tbstart;
        if (origLen == 0 && newLen == 0 && !hasNonExclusiveExclusiveSpanAt(tb, tbstart)) {
            return this;
        } else {
            List<TextWatcher> textWatchers = this.getSpans(start, end, TextWatcher.class);
            if (textWatchers != null) {
                this.sendBeforeTextChanged(textWatchers, start, origLen, newLen);
            }
            if (origLen != 0 && newLen != 0) {
                int selectionStart = Selection.getSelectionStart(this);
                int selectionEnd = Selection.getSelectionEnd(this);
                this.change(start, end, tb, tbstart, tbend);
                boolean changed = false;
                if (selectionStart > start && selectionStart < end) {
                    long diff = (long) (selectionStart - start);
                    int offset = Math.toIntExact(diff * (long) newLen / (long) origLen);
                    selectionStart = start + offset;
                    changed = true;
                    this.setSpan(false, Selection.SELECTION_START, selectionStart, selectionStart, 34, true);
                }
                if (selectionEnd > start && selectionEnd < end) {
                    long diff = (long) (selectionEnd - start);
                    int offset = Math.toIntExact(diff * (long) newLen / (long) origLen);
                    selectionEnd = start + offset;
                    changed = true;
                    this.setSpan(false, Selection.SELECTION_END, selectionEnd, selectionEnd, 34, true);
                }
                if (changed) {
                    this.restoreInvariants();
                }
            } else {
                this.change(start, end, tb, tbstart, tbend);
            }
            if (textWatchers != null) {
                this.sendTextChanged(textWatchers, start, origLen, newLen);
                this.sendAfterTextChanged(textWatchers);
            }
            this.sendToSpanWatchers(start, end, newLen - origLen);
            return this;
        }
    }

    private static boolean hasNonExclusiveExclusiveSpanAt(CharSequence text, int offset) {
        if (text instanceof Spanned spanned) {
            for (Object span : spanned.getSpans(offset, offset, Object.class)) {
                int flags = spanned.getSpanFlags(span);
                if (flags != 33) {
                    return true;
                }
            }
        }
        return false;
    }

    private void sendToSpanWatchers(int replaceStart, int replaceEnd, int nbNewChars) {
        for (int i = 0; i < this.mSpanCount; i++) {
            int spanFlags = this.mSpanFlags[i];
            if ((spanFlags & 2048) == 0) {
                int spanStart = this.mSpanStarts[i];
                int spanEnd = this.mSpanEnds[i];
                if (spanStart > this.mGapStart) {
                    spanStart -= this.mGapLength;
                }
                if (spanEnd > this.mGapStart) {
                    spanEnd -= this.mGapLength;
                }
                int newReplaceEnd = replaceEnd + nbNewChars;
                boolean spanChanged = false;
                int previousSpanStart = spanStart;
                if (spanStart > newReplaceEnd) {
                    if (nbNewChars != 0) {
                        previousSpanStart = spanStart - nbNewChars;
                        spanChanged = true;
                    }
                } else if (spanStart >= replaceStart && (spanStart != replaceStart || (spanFlags & 4096) != 4096) && (spanStart != newReplaceEnd || (spanFlags & 8192) != 8192)) {
                    spanChanged = true;
                }
                int previousSpanEnd = spanEnd;
                if (spanEnd > newReplaceEnd) {
                    if (nbNewChars != 0) {
                        previousSpanEnd = spanEnd - nbNewChars;
                        spanChanged = true;
                    }
                } else if (spanEnd >= replaceStart && (spanEnd != replaceStart || (spanFlags & 16384) != 16384) && (spanEnd != newReplaceEnd || (spanFlags & 32768) != 32768)) {
                    spanChanged = true;
                }
                if (spanChanged) {
                    this.sendSpanChanged(this.mSpans[i], previousSpanStart, previousSpanEnd, spanStart, spanEnd);
                }
                this.mSpanFlags[i] = this.mSpanFlags[i] & -61441;
            }
        }
        for (int ix = 0; ix < this.mSpanCount; ix++) {
            int spanFlags = this.mSpanFlags[ix];
            if ((spanFlags & 2048) != 0) {
                this.mSpanFlags[ix] = this.mSpanFlags[ix] & -2049;
                int spanStartx = this.mSpanStarts[ix];
                int spanEndx = this.mSpanEnds[ix];
                if (spanStartx > this.mGapStart) {
                    spanStartx -= this.mGapLength;
                }
                if (spanEndx > this.mGapStart) {
                    spanEndx -= this.mGapLength;
                }
                this.sendSpanAdded(this.mSpans[ix], spanStartx, spanEndx);
            }
        }
    }

    @Override
    public void setSpan(@Nonnull Object what, int start, int end, int flags) {
        this.setSpan(true, what, start, end, flags, true);
    }

    private void setSpan(boolean send, @Nonnull Object what, int start, int end, int flags, boolean enforceParagraph) {
        this.checkRange("setSpan", start, end);
        int flagsStart = (flags & 240) >> 4;
        if (this.isInvalidParagraph(start, flagsStart)) {
            if (enforceParagraph) {
                throw new RuntimeException("PARAGRAPH span must start at paragraph boundary (" + start + " follows " + this.charAt(start - 1) + ")");
            }
        } else {
            int flagsEnd = flags & 15;
            if (this.isInvalidParagraph(end, flagsEnd)) {
                if (enforceParagraph) {
                    throw new RuntimeException("PARAGRAPH span must end at paragraph boundary (" + end + " follows " + this.charAt(end - 1) + ")");
                }
            } else if (flagsStart == 2 && flagsEnd == 1 && start == end) {
                if (send) {
                    ModernUI.LOGGER.error(MARKER, "SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length");
                }
            } else {
                int nstart = start;
                int nend = end;
                if (start > this.mGapStart) {
                    start += this.mGapLength;
                } else if (start == this.mGapStart && (flagsStart == 2 || flagsStart == 3 && start == this.length())) {
                    start += this.mGapLength;
                }
                if (end > this.mGapStart) {
                    end += this.mGapLength;
                } else if (end == this.mGapStart && (flagsEnd == 2 || flagsEnd == 3 && end == this.length())) {
                    end += this.mGapLength;
                }
                if (this.mIndexOfSpan != null) {
                    Integer index = (Integer) this.mIndexOfSpan.get(what);
                    if (index != null) {
                        int i = index;
                        int ostart = this.mSpanStarts[i];
                        int oend = this.mSpanEnds[i];
                        if (ostart > this.mGapStart) {
                            ostart -= this.mGapLength;
                        }
                        if (oend > this.mGapStart) {
                            oend -= this.mGapLength;
                        }
                        this.mSpanStarts[i] = start;
                        this.mSpanEnds[i] = end;
                        this.mSpanFlags[i] = flags;
                        if (send) {
                            this.restoreInvariants();
                            this.sendSpanChanged(what, ostart, oend, nstart, nend);
                        }
                        return;
                    }
                }
                this.mSpans = GrowingArrayUtils.append(this.mSpans, this.mSpanCount, what);
                this.mSpanStarts = GrowingArrayUtils.append(this.mSpanStarts, this.mSpanCount, start);
                this.mSpanEnds = GrowingArrayUtils.append(this.mSpanEnds, this.mSpanCount, end);
                this.mSpanFlags = GrowingArrayUtils.append(this.mSpanFlags, this.mSpanCount, flags);
                this.mSpanOrder = GrowingArrayUtils.append(this.mSpanOrder, this.mSpanCount, this.mSpanInsertCount);
                this.invalidateIndex(this.mSpanCount);
                this.mSpanCount++;
                this.mSpanInsertCount++;
                int sizeOfMax = 2 * this.treeRoot() + 1;
                if (this.mSpanMax.length < sizeOfMax) {
                    this.mSpanMax = new int[sizeOfMax];
                }
                if (send) {
                    this.restoreInvariants();
                    this.sendSpanAdded(what, nstart, nend);
                }
            }
        }
    }

    private boolean isInvalidParagraph(int index, int flag) {
        return flag == 3 && index != 0 && index != this.length() && this.charAt(index - 1) != '\n';
    }

    @Override
    public void removeSpan(@Nonnull Object what) {
        this.removeSpan(what, 0);
    }

    @Override
    public void removeSpan(@Nonnull Object what, int flags) {
        if (this.mIndexOfSpan != null) {
            Integer i = (Integer) this.mIndexOfSpan.remove(what);
            if (i != null) {
                this.removeSpan(i.intValue(), flags);
            }
        }
    }

    private int resolveGap(int i) {
        return i > this.mGapStart ? i - this.mGapLength : i;
    }

    @Override
    public int getSpanStart(@Nonnull Object what) {
        if (this.mIndexOfSpan == null) {
            return -1;
        } else {
            Integer i = (Integer) this.mIndexOfSpan.get(what);
            return i == null ? -1 : this.resolveGap(this.mSpanStarts[i]);
        }
    }

    @Override
    public int getSpanEnd(@Nonnull Object what) {
        if (this.mIndexOfSpan == null) {
            return -1;
        } else {
            Integer i = (Integer) this.mIndexOfSpan.get(what);
            return i == null ? -1 : this.resolveGap(this.mSpanEnds[i]);
        }
    }

    @Override
    public int getSpanFlags(@Nonnull Object what) {
        if (this.mIndexOfSpan == null) {
            return 0;
        } else {
            Integer i = (Integer) this.mIndexOfSpan.get(what);
            return i == null ? 0 : this.mSpanFlags[i];
        }
    }

    @Nonnull
    @Override
    public <T> List<T> getSpans(int queryStart, int queryEnd, @Nullable Class<? extends T> kind, @Nullable List<T> out) {
        return this.getSpans(queryStart, queryEnd, kind, true, out);
    }

    @Nonnull
    public <T> List<T> getSpans(int queryStart, int queryEnd, @Nullable Class<? extends T> kind, boolean sortByInsertionOrder, @Nullable List<T> dest) {
        if (dest != null) {
            dest.clear();
        }
        if (this.mSpanCount == 0) {
            return dest != null ? dest : Collections.emptyList();
        } else {
            if (kind == null) {
                kind = Object.class;
            }
            if (dest == null) {
                dest = new ArrayList();
            }
            IntArrayList prioSortBuffer = sortByInsertionOrder ? obtain() : null;
            IntArrayList orderSortBuffer = sortByInsertionOrder ? obtain() : null;
            this.getSpansRec(queryStart, queryEnd, kind, this.treeRoot(), dest, prioSortBuffer, orderSortBuffer, 0, sortByInsertionOrder);
            if (sortByInsertionOrder) {
                if (!dest.isEmpty()) {
                    sort(dest, prioSortBuffer, orderSortBuffer);
                }
                recycle(prioSortBuffer);
                recycle(orderSortBuffer);
            }
            return dest;
        }
    }

    private int countSpans(int queryStart, int queryEnd, @Nonnull Class<?> kind, int i) {
        int count = 0;
        if ((i & 1) != 0) {
            int left = leftChild(i);
            int spanMax = this.mSpanMax[left];
            if (spanMax > this.mGapStart) {
                spanMax -= this.mGapLength;
            }
            if (spanMax >= queryStart) {
                count = this.countSpans(queryStart, queryEnd, kind, left);
            }
        }
        if (i < this.mSpanCount) {
            int spanStart = this.mSpanStarts[i];
            if (spanStart > this.mGapStart) {
                spanStart -= this.mGapLength;
            }
            if (spanStart <= queryEnd) {
                int spanEnd = this.mSpanEnds[i];
                if (spanEnd > this.mGapStart) {
                    spanEnd -= this.mGapLength;
                }
                if (spanEnd >= queryStart && (spanStart == spanEnd || queryStart == queryEnd || spanStart != queryEnd && spanEnd != queryStart) && (Object.class == kind || kind.isInstance(this.mSpans[i]))) {
                    count++;
                }
                if ((i & 1) != 0) {
                    count += this.countSpans(queryStart, queryEnd, kind, rightChild(i));
                }
            }
        }
        return count;
    }

    private int getSpansRec(int queryStart, int queryEnd, @Nonnull Class<?> kind, int i, @Nonnull List<Object> ret, IntArrayList priority, IntArrayList insertionOrder, int count, boolean sort) {
        if ((i & 1) != 0) {
            int left = leftChild(i);
            int spanMax = this.mSpanMax[left];
            if (spanMax > this.mGapStart) {
                spanMax -= this.mGapLength;
            }
            if (spanMax >= queryStart) {
                count = this.getSpansRec(queryStart, queryEnd, kind, left, ret, priority, insertionOrder, count, sort);
            }
        }
        if (i >= this.mSpanCount) {
            return count;
        } else {
            int spanStart = this.mSpanStarts[i];
            if (spanStart > this.mGapStart) {
                spanStart -= this.mGapLength;
            }
            if (spanStart <= queryEnd) {
                int spanEnd = this.mSpanEnds[i];
                if (spanEnd > this.mGapStart) {
                    spanEnd -= this.mGapLength;
                }
                if (spanEnd >= queryStart && (spanStart == spanEnd || queryStart == queryEnd || spanStart != queryEnd && spanEnd != queryStart) && (Object.class == kind || kind.isInstance(this.mSpans[i]))) {
                    int spanPriority = this.mSpanFlags[i] & 0xFF0000;
                    if (sort) {
                        priority.add(spanPriority);
                        insertionOrder.add(this.mSpanOrder[i]);
                        ret.add(this.mSpans[i]);
                    } else if (spanPriority != 0) {
                        int j;
                        for (j = 0; j < count; j++) {
                            int p = this.getSpanFlags(ret.get(j)) & 0xFF0000;
                            if (spanPriority > p) {
                                break;
                            }
                        }
                        ret.add(j, this.mSpans[i]);
                    } else {
                        ret.add(this.mSpans[i]);
                    }
                    count++;
                }
                if ((i & 1) != 0) {
                    count = this.getSpansRec(queryStart, queryEnd, kind, rightChild(i), ret, priority, insertionOrder, count, sort);
                }
            }
            return count;
        }
    }

    @Nonnull
    private static IntArrayList obtain() {
        IntArrayList result = sIntBufferPool.acquire();
        if (result == null) {
            result = new IntArrayList();
        }
        return result;
    }

    private static void recycle(@Nonnull IntArrayList buffer) {
        buffer.clear();
        sIntBufferPool.release(buffer);
    }

    private static <T> void sort(@Nonnull List<T> list, IntArrayList priority, IntArrayList insertionOrder) {
        int size = list.size();
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i, list, size, priority, insertionOrder);
        }
        for (int i = size - 1; i > 0; i--) {
            T tmpSpan = (T) list.get(0);
            list.set(0, list.get(i));
            list.set(i, tmpSpan);
            int tmpPriority = priority.getInt(0);
            priority.set(0, priority.getInt(i));
            priority.set(i, tmpPriority);
            int tmpOrder = insertionOrder.getInt(0);
            insertionOrder.set(0, insertionOrder.getInt(i));
            insertionOrder.set(i, tmpOrder);
            siftDown(0, list, i, priority, insertionOrder);
        }
    }

    private static <T> void siftDown(int index, @Nonnull List<T> list, int size, IntArrayList priority, IntArrayList insertionOrder) {
        for (int left = 2 * index + 1; left < size; left = 2 * left + 1) {
            if (left < size - 1 && compareSpans(left, left + 1, priority, insertionOrder) < 0) {
                left++;
            }
            if (compareSpans(index, left, priority, insertionOrder) >= 0) {
                break;
            }
            T tmpSpan = (T) list.get(index);
            list.set(index, list.get(left));
            list.set(left, tmpSpan);
            int tmpPriority = priority.getInt(index);
            priority.set(index, priority.getInt(left));
            priority.set(left, tmpPriority);
            int tmpOrder = insertionOrder.getInt(index);
            insertionOrder.set(index, insertionOrder.getInt(left));
            insertionOrder.set(left, tmpOrder);
            index = left;
        }
    }

    private static int compareSpans(int left, int right, @Nonnull IntArrayList priority, @Nonnull IntArrayList insertionOrder) {
        int priority1 = priority.getInt(left);
        int priority2 = priority.getInt(right);
        return priority1 == priority2 ? Integer.compare(insertionOrder.getInt(left), insertionOrder.getInt(right)) : Integer.compare(priority2, priority1);
    }

    @Override
    public int nextSpanTransition(int start, int limit, @Nullable Class<?> kind) {
        if (this.mSpanCount == 0) {
            return limit;
        } else {
            if (kind == null) {
                kind = Object.class;
            }
            return this.nextSpanTransitionRec(start, limit, kind, this.treeRoot());
        }
    }

    private int nextSpanTransitionRec(int start, int limit, @Nonnull Class<?> kind, int i) {
        if ((i & 1) != 0) {
            int left = leftChild(i);
            if (this.resolveGap(this.mSpanMax[left]) > start) {
                limit = this.nextSpanTransitionRec(start, limit, kind, left);
            }
        }
        if (i < this.mSpanCount) {
            int st = this.resolveGap(this.mSpanStarts[i]);
            int en = this.resolveGap(this.mSpanEnds[i]);
            if (st > start && st < limit && (kind == Object.class || kind.isInstance(this.mSpans[i]))) {
                limit = st;
            }
            if (en > start && en < limit && (kind == Object.class || kind.isInstance(this.mSpans[i]))) {
                limit = en;
            }
            if (st < limit && (i & 1) != 0) {
                limit = this.nextSpanTransitionRec(start, limit, kind, rightChild(i));
            }
        }
        return limit;
    }

    public CharSequence subSequence(int start, int end) {
        return new SpannableStringBuilder(this, start, end);
    }

    @Override
    public void getChars(int start, int end, char[] dest, int destoff) {
        this.checkRange("getChars", start, end);
        if (end <= this.mGapStart) {
            System.arraycopy(this.mText, start, dest, destoff, end - start);
        } else if (start >= this.mGapStart) {
            System.arraycopy(this.mText, start + this.mGapLength, dest, destoff, end - start);
        } else {
            System.arraycopy(this.mText, start, dest, destoff, this.mGapStart - start);
            System.arraycopy(this.mText, this.mGapStart + this.mGapLength, dest, destoff + (this.mGapStart - start), end - this.mGapStart);
        }
    }

    public String toString() {
        int len = this.length();
        char[] buf = new char[len];
        this.getChars(0, len, buf, 0);
        return new String(buf);
    }

    public String substring(int start, int end) {
        char[] buf = new char[end - start];
        this.getChars(start, end, buf, 0);
        return new String(buf);
    }

    public int getTextWatcherDepth() {
        return this.mTextWatcherDepth;
    }

    private void sendBeforeTextChanged(@Nonnull List<TextWatcher> watchers, int start, int before, int after) {
        this.mTextWatcherDepth++;
        for (TextWatcher watcher : watchers) {
            watcher.beforeTextChanged(this, start, before, after);
        }
        this.mTextWatcherDepth--;
    }

    private void sendTextChanged(@Nonnull List<TextWatcher> watchers, int start, int before, int after) {
        this.mTextWatcherDepth++;
        for (TextWatcher watcher : watchers) {
            watcher.onTextChanged(this, start, before, after);
        }
        this.mTextWatcherDepth--;
    }

    private void sendAfterTextChanged(@Nonnull List<TextWatcher> watchers) {
        this.mTextWatcherDepth++;
        for (TextWatcher watcher : watchers) {
            watcher.afterTextChanged(this);
        }
        this.mTextWatcherDepth--;
    }

    private void sendSpanAdded(Object what, int start, int end) {
        for (SpanWatcher watcher : this.getSpans(start, end, SpanWatcher.class)) {
            watcher.onSpanAdded(this, what, start, end);
        }
    }

    private void sendSpanRemoved(Object what, int start, int end) {
        for (SpanWatcher watcher : this.getSpans(start, end, SpanWatcher.class)) {
            watcher.onSpanRemoved(this, what, start, end);
        }
    }

    private void sendSpanChanged(Object what, int oldStart, int oldEnd, int start, int end) {
        for (SpanWatcher watcher : this.getSpans(Math.min(oldStart, start), Math.min(Math.max(oldEnd, end), this.length()), SpanWatcher.class)) {
            watcher.onSpanChanged(this, what, oldStart, oldEnd, start, end);
        }
    }

    @Nonnull
    private static String region(int start, int end) {
        return "(" + start + " ... " + end + ")";
    }

    private void checkRange(String operation, int start, int end) {
        if (end < start) {
            throw new IndexOutOfBoundsException(operation + " " + region(start, end) + " has end before start");
        } else {
            int len = this.length();
            if (start > len || end > len) {
                throw new IndexOutOfBoundsException(operation + " " + region(start, end) + " ends beyond length " + len);
            } else if (start < 0 || end < 0) {
                throw new IndexOutOfBoundsException(operation + " " + region(start, end) + " starts before 0");
            }
        }
    }

    @Override
    public void setFilters(@Nonnull InputFilter[] filters) {
        this.mFilters = filters;
    }

    @Nonnull
    @Override
    public InputFilter[] getFilters() {
        return this.mFilters;
    }

    public boolean equals(Object o) {
        if (o instanceof Spanned other && this.toString().equals(o.toString())) {
            List<?> otherSpans = other.getSpans(0, other.length(), Object.class);
            List<?> spans = this.getSpans(0, this.length(), Object.class);
            if (otherSpans.isEmpty() && spans.isEmpty()) {
                return true;
            }
            if (!otherSpans.isEmpty() && !spans.isEmpty() && otherSpans.size() == spans.size()) {
                for (int i = 0; i < spans.size(); i++) {
                    Object span = spans.get(i);
                    Object otherSpan = otherSpans.get(i);
                    if (span == this) {
                        if (other != otherSpan || this.getSpanStart(span) != other.getSpanStart(otherSpan) || this.getSpanEnd(span) != other.getSpanEnd(otherSpan) || this.getSpanFlags(span) != other.getSpanFlags(otherSpan)) {
                            return false;
                        }
                    } else if (!span.equals(otherSpan) || this.getSpanStart(span) != other.getSpanStart(otherSpan) || this.getSpanEnd(span) != other.getSpanEnd(otherSpan) || this.getSpanFlags(span) != other.getSpanFlags(otherSpan)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hash = this.toString().hashCode();
        hash = hash * 31 + this.mSpanCount;
        for (int i = 0; i < this.mSpanCount; i++) {
            Object span = this.mSpans[i];
            if (span != this) {
                hash = hash * 31 + span.hashCode();
            }
            hash = hash * 31 + this.getSpanStart(span);
            hash = hash * 31 + this.getSpanEnd(span);
            hash = hash * 31 + this.getSpanFlags(span);
        }
        return hash;
    }

    private int treeRoot() {
        return Integer.highestOneBit(this.mSpanCount) - 1;
    }

    private static int leftChild(int i) {
        return i - ((i + 1 & ~i) >> 1);
    }

    private static int rightChild(int i) {
        return i + ((i + 1 & ~i) >> 1);
    }

    private int calcMax(int i) {
        int max = 0;
        if ((i & 1) != 0) {
            max = this.calcMax(leftChild(i));
        }
        if (i < this.mSpanCount) {
            max = Math.max(max, this.mSpanEnds[i]);
            if ((i & 1) != 0) {
                max = Math.max(max, this.calcMax(rightChild(i)));
            }
        }
        this.mSpanMax[i] = max;
        return max;
    }

    private void restoreInvariants() {
        if (this.mSpanCount != 0) {
            for (int i = 1; i < this.mSpanCount; i++) {
                if (this.mSpanStarts[i] < this.mSpanStarts[i - 1]) {
                    Object span = this.mSpans[i];
                    int start = this.mSpanStarts[i];
                    int end = this.mSpanEnds[i];
                    int flags = this.mSpanFlags[i];
                    int insertionOrder = this.mSpanOrder[i];
                    int j = i;
                    do {
                        this.mSpans[j] = this.mSpans[j - 1];
                        this.mSpanStarts[j] = this.mSpanStarts[j - 1];
                        this.mSpanEnds[j] = this.mSpanEnds[j - 1];
                        this.mSpanFlags[j] = this.mSpanFlags[j - 1];
                        this.mSpanOrder[j] = this.mSpanOrder[j - 1];
                        j--;
                    } while (j > 0 && start < this.mSpanStarts[j - 1]);
                    this.mSpans[j] = span;
                    this.mSpanStarts[j] = start;
                    this.mSpanEnds[j] = end;
                    this.mSpanFlags[j] = flags;
                    this.mSpanOrder[j] = insertionOrder;
                    this.invalidateIndex(j);
                }
            }
            this.calcMax(this.treeRoot());
            if (this.mIndexOfSpan == null) {
                this.mIndexOfSpan = new IdentityHashMap();
            }
            for (int ix = this.mLowWaterMark; ix < this.mSpanCount; ix++) {
                Integer existing = (Integer) this.mIndexOfSpan.get(this.mSpans[ix]);
                if (existing == null || existing != ix) {
                    this.mIndexOfSpan.put(this.mSpans[ix], ix);
                }
            }
            this.mLowWaterMark = Integer.MAX_VALUE;
        }
    }

    private void invalidateIndex(int i) {
        this.mLowWaterMark = Math.min(i, this.mLowWaterMark);
    }
}