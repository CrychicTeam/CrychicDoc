package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmName;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.ranges.IntRange;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.List;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000\u0018\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007¢\u0006\u0002\b\u0004\u001a\u001d\u0010\u0005\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\b\u001a\u001d\u0010\t\u001a\u00020\u0006*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0002\b\n¨\u0006\u000b" }, d2 = { "asReversed", "", "T", "", "asReversedMutable", "reverseElementIndex", "", "index", "reverseElementIndex$CollectionsKt__ReversedViewsKt", "reversePositionIndex", "reversePositionIndex$CollectionsKt__ReversedViewsKt", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/collections/CollectionsKt")
class CollectionsKt__ReversedViewsKt extends CollectionsKt__MutableCollectionsKt {

    private static final int reverseElementIndex$CollectionsKt__ReversedViewsKt(List<?> $this$reverseElementIndex, int index) {
        if (0 <= index ? index <= CollectionsKt.getLastIndex($this$reverseElementIndex) : false) {
            return CollectionsKt.getLastIndex($this$reverseElementIndex) - index;
        } else {
            throw new IndexOutOfBoundsException("Element index " + index + " must be in range [" + new IntRange(0, CollectionsKt.getLastIndex($this$reverseElementIndex)) + "].");
        }
    }

    private static final int reversePositionIndex$CollectionsKt__ReversedViewsKt(List<?> $this$reversePositionIndex, int index) {
        if (0 <= index ? index <= $this$reversePositionIndex.size() : false) {
            return $this$reversePositionIndex.size() - index;
        } else {
            throw new IndexOutOfBoundsException("Position index " + index + " must be in range [" + new IntRange(0, $this$reversePositionIndex.size()) + "].");
        }
    }

    @NotNull
    public static final <T> List<T> asReversed(@NotNull List<? extends T> $this$asReversed) {
        Intrinsics.checkNotNullParameter($this$asReversed, "<this>");
        return new ReversedListReadOnly<>($this$asReversed);
    }

    @JvmName(name = "asReversedMutable")
    @NotNull
    public static final <T> List<T> asReversedMutable(@NotNull List<T> $this$asReversed) {
        Intrinsics.checkNotNullParameter($this$asReversed, "<this>");
        return new ReversedList<>($this$asReversed);
    }

    public CollectionsKt__ReversedViewsKt() {
    }
}