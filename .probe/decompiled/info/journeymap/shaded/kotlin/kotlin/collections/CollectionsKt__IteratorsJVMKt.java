package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Enumeration;
import java.util.Iterator;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000\u000e\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001f\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0086\u0002¨\u0006\u0004" }, d2 = { "iterator", "", "T", "Ljava/util/Enumeration;", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/collections/CollectionsKt")
class CollectionsKt__IteratorsJVMKt extends CollectionsKt__IterablesKt {

    @NotNull
    public static final <T> Iterator<T> iterator(@NotNull final Enumeration<T> $this$iterator) {
        Intrinsics.checkNotNullParameter($this$iterator, "<this>");
        return new Iterator<T>() {

            public boolean hasNext() {
                return $this$iterator.hasMoreElements();
            }

            public T next() {
                return (T) $this$iterator.nextElement();
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }
        };
    }

    public CollectionsKt__IteratorsJVMKt() {
    }
}