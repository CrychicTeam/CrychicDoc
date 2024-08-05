package icyllis.modernui.text;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.util.List;

public interface Spanned extends CharSequence {

    int SPAN_POINT_MARK_MASK = 51;

    int SPAN_MARK_MARK = 17;

    int SPAN_MARK_POINT = 18;

    int SPAN_POINT_MARK = 33;

    int SPAN_POINT_POINT = 34;

    int SPAN_PARAGRAPH = 51;

    int SPAN_INCLUSIVE_EXCLUSIVE = 17;

    int SPAN_INCLUSIVE_INCLUSIVE = 18;

    int SPAN_EXCLUSIVE_EXCLUSIVE = 33;

    int SPAN_EXCLUSIVE_INCLUSIVE = 34;

    int SPAN_COMPOSING = 256;

    int SPAN_INTERMEDIATE = 512;

    int SPAN_USER_SHIFT = 24;

    int SPAN_USER = -16777216;

    int SPAN_PRIORITY_SHIFT = 16;

    int SPAN_PRIORITY = 16711680;

    @NonNull
    <T> List<T> getSpans(int var1, int var2, @Nullable Class<? extends T> var3, @Nullable List<T> var4);

    @NonNull
    default <T> List<T> getSpans(int start, int end, @Nullable Class<? extends T> type) {
        return this.getSpans(start, end, type, null);
    }

    int getSpanStart(@NonNull Object var1);

    int getSpanEnd(@NonNull Object var1);

    int getSpanFlags(@NonNull Object var1);

    int nextSpanTransition(int var1, int var2, @Nullable Class<?> var3);
}