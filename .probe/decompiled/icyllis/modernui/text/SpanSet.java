package icyllis.modernui.text;

import icyllis.modernui.annotation.NonNull;
import java.util.ArrayList;
import java.util.Iterator;

public class SpanSet<E> extends ArrayList<E> {

    final Class<? extends E> mType;

    final boolean mIgnoreEmptySpans;

    public int[] mSpanStarts;

    public int[] mSpanEnds;

    public int[] mSpanFlags;

    public SpanSet(@NonNull Class<? extends E> type) {
        this(type, true);
    }

    public SpanSet(@NonNull Class<? extends E> type, boolean ignoreEmptySpans) {
        this.mType = type;
        this.mIgnoreEmptySpans = ignoreEmptySpans;
    }

    void add(E span, int start, int end, int flags) {
        int size = this.size();
        this.grow(size + 1);
        this.mSpanStarts[size] = start;
        this.mSpanEnds[size] = end;
        this.mSpanFlags[size] = flags;
        this.add(span);
    }

    void add(int index, E span, int start, int end, int flags) {
        int size = this.size();
        this.grow(size + 1);
        if (index != size) {
            System.arraycopy(this.mSpanStarts, index, this.mSpanStarts, index + 1, size - index);
            System.arraycopy(this.mSpanEnds, index, this.mSpanEnds, index + 1, size - index);
            System.arraycopy(this.mSpanFlags, index, this.mSpanFlags, index + 1, size - index);
        }
        this.mSpanStarts[index] = start;
        this.mSpanEnds[index] = end;
        this.mSpanFlags[index] = flags;
        this.add(index, span);
    }

    private void grow(int length) {
        if (this.mSpanStarts == null) {
            length = Math.max(length, 10);
        } else if (this.mSpanStarts.length < length) {
            length = Math.max(length, this.mSpanStarts.length + (this.mSpanStarts.length >> 1));
        } else {
            length = 0;
        }
        if (length > 0) {
            this.mSpanStarts = new int[length];
            this.mSpanEnds = new int[length];
            this.mSpanFlags = new int[length];
        }
    }

    public boolean init(@NonNull Spanned spanned, int start, int limit) {
        if (spanned instanceof SpannableStringInternal internal) {
            internal.getSpansSpanSet(start, limit, this.mType, this);
            return !this.isEmpty();
        } else {
            spanned.getSpans(start, limit, this.mType, this);
            int length = this.size();
            if (length <= 0) {
                return false;
            } else {
                this.grow(length);
                int size = 0;
                Iterator<E> it = this.iterator();
                while (it.hasNext()) {
                    E span = (E) it.next();
                    int spanStart = spanned.getSpanStart(span);
                    int spanEnd = spanned.getSpanEnd(span);
                    if (this.mIgnoreEmptySpans && spanStart == spanEnd) {
                        it.remove();
                    } else {
                        int spanFlag = spanned.getSpanFlags(span);
                        this.mSpanStarts[size] = spanStart;
                        this.mSpanEnds[size] = spanEnd;
                        this.mSpanFlags[size] = spanFlag;
                        size++;
                    }
                }
                assert size == this.size();
                return size != 0;
            }
        }
    }

    public boolean hasSpansIntersecting(int start, int end) {
        for (int i = 0; i < this.size(); i++) {
            if (this.mSpanStarts[i] < end && this.mSpanEnds[i] > start) {
                return true;
            }
        }
        return false;
    }

    public int getNextTransition(int start, int limit) {
        for (int i = 0; i < this.size(); i++) {
            int spanStart = this.mSpanStarts[i];
            int spanEnd = this.mSpanEnds[i];
            if (spanStart > start && spanStart < limit) {
                limit = spanStart;
            }
            if (spanEnd > start && spanEnd < limit) {
                limit = spanEnd;
            }
        }
        return limit;
    }

    public void recycle() {
        this.clear();
    }
}