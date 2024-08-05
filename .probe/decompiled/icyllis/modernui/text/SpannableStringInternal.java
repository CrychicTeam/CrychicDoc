package icyllis.modernui.text;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

abstract class SpannableStringInternal implements Spanned, GetChars {

    private static final int START = 0;

    private static final int END = 1;

    private static final int FLAGS = 2;

    private static final int COLUMNS = 3;

    private final String mText;

    private Object[] mSpans;

    private int[] mSpanData;

    private int mSpanCount;

    SpannableStringInternal(CharSequence source, int start, int end, boolean ignoreNoCopySpan) {
        if (start == 0 && end == source.length()) {
            this.mText = source.toString();
        } else {
            this.mText = source.toString().substring(start, end);
        }
        this.mSpans = ObjectArrays.EMPTY_ARRAY;
        this.mSpanData = IntArrays.EMPTY_ARRAY;
        if (source instanceof Spanned) {
            if (source instanceof SpannableStringInternal) {
                this.copySpansFromInternal((SpannableStringInternal) source, start, end, ignoreNoCopySpan);
            } else {
                this.copySpansFromSpanned((Spanned) source, start, end, ignoreNoCopySpan);
            }
        }
    }

    private void copySpansFromSpanned(@NonNull Spanned src, int start, int end, boolean ignoreNoCopySpan) {
        for (Object span : src.getSpans(start, end, Object.class)) {
            if (!ignoreNoCopySpan || !(span instanceof NoCopySpan)) {
                int st = src.getSpanStart(span);
                int en = src.getSpanEnd(span);
                int fl = src.getSpanFlags(span);
                if (st < start) {
                    st = start;
                }
                if (en > end) {
                    en = end;
                }
                this.setSpan(span, st - start, en - start, fl, false);
            }
        }
    }

    private void copySpansFromInternal(@NonNull SpannableStringInternal src, int start, int end, boolean ignoreNoCopySpan) {
        int count = 0;
        int[] srcData = src.mSpanData;
        Object[] srcSpans = src.mSpans;
        int limit = src.mSpanCount;
        boolean hasNoCopySpan = false;
        for (int i = 0; i < limit; i++) {
            int spanStart = srcData[i * 3 + 0];
            int spanEnd = srcData[i * 3 + 1];
            if (!isOutOfCopyRange(start, end, spanStart, spanEnd)) {
                if (srcSpans[i] instanceof NoCopySpan) {
                    hasNoCopySpan = true;
                    if (ignoreNoCopySpan) {
                        continue;
                    }
                }
                count++;
            }
        }
        if (count != 0) {
            if (!hasNoCopySpan && start == 0 && end == src.length()) {
                this.mSpans = new Object[src.mSpans.length];
                this.mSpanData = new int[src.mSpanData.length];
                this.mSpanCount = src.mSpanCount;
                System.arraycopy(src.mSpans, 0, this.mSpans, 0, src.mSpans.length);
                System.arraycopy(src.mSpanData, 0, this.mSpanData, 0, this.mSpanData.length);
            } else {
                this.mSpanCount = count;
                this.mSpans = new Object[this.mSpanCount];
                this.mSpanData = new int[this.mSpans.length * 3];
                int ix = 0;
                for (int j = 0; ix < limit; ix++) {
                    int spanStart = srcData[ix * 3 + 0];
                    int spanEnd = srcData[ix * 3 + 1];
                    if (!isOutOfCopyRange(start, end, spanStart, spanEnd) && (!ignoreNoCopySpan || !(srcSpans[ix] instanceof NoCopySpan))) {
                        if (spanStart < start) {
                            spanStart = start;
                        }
                        if (spanEnd > end) {
                            spanEnd = end;
                        }
                        this.mSpans[j] = srcSpans[ix];
                        this.mSpanData[j * 3 + 0] = spanStart - start;
                        this.mSpanData[j * 3 + 1] = spanEnd - start;
                        this.mSpanData[j * 3 + 2] = srcData[ix * 3 + 2];
                        j++;
                    }
                }
            }
        }
    }

    private static boolean isOutOfCopyRange(int start, int end, int spanStart, int spanEnd) {
        if (spanStart > end || spanEnd < start) {
            return true;
        } else {
            return spanStart != spanEnd && start != end ? spanStart == end || spanEnd == start : false;
        }
    }

    public void setSpan(@NonNull Object span, int start, int end, int flags) {
        this.setSpan(span, start, end, flags, true);
    }

    private boolean isIndexFollowsNextLine(int index) {
        return index != 0 && index != this.length() && this.charAt(index - 1) != '\n';
    }

    private void setSpan(@NonNull Object span, int start, int end, int flags, boolean enforceParagraph) {
        Objects.requireNonNull(span, "span");
        if ((start | end - start | this.length() - end) < 0) {
            throw new IndexOutOfBoundsException(String.format("Range [%d, %d) out of bounds for length %d", start, end, this.length()));
        } else {
            if ((flags & 51) == 51) {
                if (this.isIndexFollowsNextLine(start)) {
                    if (!enforceParagraph) {
                        return;
                    }
                    throw new RuntimeException("PARAGRAPH span must start at paragraph boundary (" + start + " follows " + this.charAt(start - 1) + ")");
                }
                if (this.isIndexFollowsNextLine(end)) {
                    if (!enforceParagraph) {
                        return;
                    }
                    throw new RuntimeException("PARAGRAPH span must end at paragraph boundary (" + end + " follows " + this.charAt(end - 1) + ")");
                }
            }
            int count = this.mSpanCount;
            Object[] spans = this.mSpans;
            int[] data = this.mSpanData;
            for (int i = 0; i < count; i++) {
                if (spans[i] == span) {
                    int ic = i * 3;
                    int ost = data[ic + 0];
                    int oen = data[ic + 1];
                    data[ic + 0] = start;
                    data[ic + 1] = end;
                    data[ic + 2] = flags;
                    this.sendSpanChanged(span, ost, oen, start, end);
                    return;
                }
            }
            if (count == spans.length) {
                int newSize;
                if (count == 0) {
                    newSize = 10;
                } else if (count < 1000) {
                    newSize = count + (count >> 1);
                } else {
                    newSize = count + (count >> 2);
                }
                Object[] newSpans = new Object[newSize];
                int[] newData = new int[newSize * 3];
                System.arraycopy(spans, 0, newSpans, 0, count);
                System.arraycopy(data, 0, newData, 0, count * 3);
                this.mSpans = newSpans;
                this.mSpanData = newData;
            }
            int ic = count * 3;
            this.mSpans[count] = span;
            this.mSpanData[ic + 0] = start;
            this.mSpanData[ic + 1] = end;
            this.mSpanData[ic + 2] = flags;
            this.mSpanCount++;
            if (this instanceof Spannable) {
                this.sendSpanAdded(span, start, end);
            }
        }
    }

    public void removeSpan(@NonNull Object span) {
        this.removeSpan(span, 0);
    }

    public void removeSpan(@NonNull Object span, int flags) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == span) {
                int ost = data[i * 3 + 0];
                int oen = data[i * 3 + 1];
                int c = count - (i + 1);
                if (c != 0) {
                    System.arraycopy(spans, i + 1, spans, i, c);
                    System.arraycopy(data, (i + 1) * 3, data, i * 3, c * 3);
                }
                this.mSpanCount--;
                if ((flags & 512) == 0) {
                    this.sendSpanRemoved(span, ost, oen);
                }
                return;
            }
        }
    }

    @NonNull
    @Override
    public <T> List<T> getSpans(int start, int end, Class<? extends T> type, @Nullable List<T> dest) {
        if (dest != null) {
            dest.clear();
        }
        if (this.mSpanCount == 0) {
            return dest != null ? dest : Collections.emptyList();
        } else {
            int count = this.mSpanCount;
            Object[] spans = this.mSpans;
            int[] data = this.mSpanData;
            boolean check = type != null && type != Object.class;
            int found = 0;
            T first = null;
            int i = 0;
            for (int ic = 0; i < count; ic += 3) {
                int spanStart = data[ic + 0];
                int spanEnd = data[ic + 1];
                if (spanStart <= end && spanEnd >= start && (spanStart == spanEnd || start == end || spanStart != end && spanEnd != start) && (!check || type.isInstance(spans[i]))) {
                    if (dest == null && found <= 0) {
                        assert found == 0;
                        assert first == null;
                        first = (T) spans[i];
                    } else {
                        if (dest == null) {
                            dest = new ArrayList();
                            dest.add(first);
                        }
                        int priority = data[ic + 2] & 0xFF0000;
                        if (priority != 0) {
                            int j;
                            for (j = 0; j < found; j++) {
                                int p = this.getSpanFlags(dest.get(j)) & 0xFF0000;
                                if (priority > p) {
                                    break;
                                }
                            }
                            dest.add(j, spans[i]);
                        } else {
                            dest.add(spans[i]);
                        }
                    }
                    found++;
                }
                i++;
            }
            if (dest != null) {
                assert found == dest.size();
                return dest;
            } else if (found == 0) {
                return Collections.emptyList();
            } else {
                assert found == 1;
                assert first != null;
                return List.of(first);
            }
        }
    }

    final <T> void getSpansSpanSet(int start, int end, Class<? extends T> type, @NonNull SpanSet<T> dest) {
        dest.clear();
        if (this.mSpanCount != 0) {
            int count = this.mSpanCount;
            Object[] spans = this.mSpans;
            int[] data = this.mSpanData;
            boolean check = type != null && type != Object.class;
            int i = 0;
            for (int ic = 0; i < count; ic += 3) {
                int spanStart = data[ic + 0];
                int spanEnd = data[ic + 1];
                if (spanStart <= end && spanEnd >= start && (spanStart == spanEnd || start == end || spanStart != end && spanEnd != start) && (!check || type.isInstance(spans[i])) && (!dest.mIgnoreEmptySpans || spanStart != spanEnd)) {
                    int flags = data[ic + 2];
                    int priority = flags & 0xFF0000;
                    if (priority == 0) {
                        dest.add((T) spans[i], spanStart, spanEnd, flags);
                    } else {
                        int j;
                        for (j = 0; j < dest.size(); j++) {
                            int p = dest.mSpanFlags[j] & 0xFF0000;
                            if (priority > p) {
                                break;
                            }
                        }
                        dest.add(j, (T) spans[i], spanStart, spanEnd, flags);
                    }
                }
                i++;
            }
        }
    }

    @Override
    public int getSpanStart(@NonNull Object span) {
        Object[] spans = this.mSpans;
        for (int i = this.mSpanCount - 1; i >= 0; i--) {
            if (spans[i] == span) {
                return this.mSpanData[i * 3 + 0];
            }
        }
        return -1;
    }

    @Override
    public int getSpanEnd(@NonNull Object span) {
        Object[] spans = this.mSpans;
        for (int i = this.mSpanCount - 1; i >= 0; i--) {
            if (spans[i] == span) {
                return this.mSpanData[i * 3 + 1];
            }
        }
        return -1;
    }

    @Override
    public int getSpanFlags(@NonNull Object span) {
        Object[] spans = this.mSpans;
        for (int i = this.mSpanCount - 1; i >= 0; i--) {
            if (spans[i] == span) {
                return this.mSpanData[i * 3 + 2];
            }
        }
        return 0;
    }

    @Override
    public int nextSpanTransition(int start, int limit, @Nullable Class<?> type) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        boolean any = type == null || type == Object.class;
        for (int i = 0; i < count; i++) {
            int st = data[i * 3 + 0];
            int en = data[i * 3 + 1];
            if (st > start && st < limit && (any || type.isInstance(spans[i]))) {
                limit = st;
            }
            if (en > start && en < limit && (any || type.isInstance(spans[i]))) {
                limit = en;
            }
        }
        return limit;
    }

    private void sendSpanAdded(Object span, int start, int end) {
        for (SpanWatcher watcher : this.getSpans(start, end, SpanWatcher.class)) {
            watcher.onSpanAdded((Spannable) this, span, start, end);
        }
    }

    private void sendSpanRemoved(Object span, int start, int end) {
        for (SpanWatcher watcher : this.getSpans(start, end, SpanWatcher.class)) {
            watcher.onSpanRemoved((Spannable) this, span, start, end);
        }
    }

    private void sendSpanChanged(Object span, int s, int e, int st, int en) {
        for (SpanWatcher watcher : this.getSpans(Math.min(s, st), Math.max(e, en), SpanWatcher.class)) {
            watcher.onSpanChanged((Spannable) this, span, s, e, st, en);
        }
    }

    @NonNull
    public final String toString() {
        return this.mText;
    }

    public final int length() {
        return this.mText.length();
    }

    public final char charAt(int index) {
        return this.mText.charAt(index);
    }

    @Override
    public final void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        this.mText.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public boolean isEmpty() {
        return this.mText.isEmpty();
    }

    @NonNull
    public IntStream chars() {
        return this.mText.chars();
    }

    @NonNull
    public IntStream codePoints() {
        return this.mText.codePoints();
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
}