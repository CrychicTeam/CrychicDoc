package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.sequences.Sequence;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000\u001c\n\u0000\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a,\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0004\u001a,\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0086\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0086\u0002\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086\u0002\u001a,\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\u0004¨\u0006\r" }, d2 = { "minus", "", "T", "element", "(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;", "elements", "", "(Ljava/util/Set;[Ljava/lang/Object;)Ljava/util/Set;", "", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/Sequence;", "minusElement", "plus", "plusElement", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/collections/SetsKt")
class SetsKt___SetsKt extends SetsKt__SetsKt {

    @NotNull
    public static final <T> Set<T> minus(@NotNull Set<? extends T> $this$minus, T element) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        LinkedHashSet result = new LinkedHashSet(MapsKt.mapCapacity($this$minus.size()));
        boolean removed = false;
        Iterable $this$filterTo$iv = (Iterable) $this$minus;
        int $i$f$filterTo = 0;
        for (Object element$iv : $this$filterTo$iv) {
            ???;
            boolean var10000;
            if (!removed && Intrinsics.areEqual(element$iv, element)) {
                removed = true;
                var10000 = false;
            } else {
                var10000 = true;
            }
            if (var10000) {
                ((Collection) result).add(element$iv);
            }
        }
        return (Set<T>) ((Collection) result);
    }

    @NotNull
    public static final <T> Set<T> minus(@NotNull Set<? extends T> $this$minus, @NotNull T[] elements) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        LinkedHashSet result = new LinkedHashSet((Collection) $this$minus);
        CollectionsKt.removeAll((Collection<? super Object>) result, elements);
        return (Set<T>) result;
    }

    @NotNull
    public static final <T> Set<T> minus(@NotNull Set<? extends T> $this$minus, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        Collection other = BrittleContainsOptimizationKt.convertToSetForSetOperationWith(elements, (Iterable<? extends T>) $this$minus);
        if (other.isEmpty()) {
            return CollectionsKt.toSet((Iterable<? extends T>) $this$minus);
        } else if (other instanceof Set) {
            Iterable var10 = (Iterable) $this$minus;
            Collection destination$iv = (Collection) (new LinkedHashSet());
            int $i$f$filterNotTo = 0;
            for (Object element$iv : var10) {
                ???;
                if (!other.contains(element$iv)) {
                    destination$iv.add(element$iv);
                }
            }
            return (Set<T>) destination$iv;
        } else {
            LinkedHashSet result = new LinkedHashSet((Collection) $this$minus);
            result.removeAll(other);
            return (Set<T>) result;
        }
    }

    @NotNull
    public static final <T> Set<T> minus(@NotNull Set<? extends T> $this$minus, @NotNull Sequence<? extends T> elements) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        LinkedHashSet result = new LinkedHashSet((Collection) $this$minus);
        CollectionsKt.removeAll((Collection<? super T>) result, elements);
        return (Set<T>) result;
    }

    @InlineOnly
    private static final <T> Set<T> minusElement(Set<? extends T> $this$minusElement, T element) {
        Intrinsics.checkNotNullParameter($this$minusElement, "<this>");
        return SetsKt.minus($this$minusElement, (T) element);
    }

    @NotNull
    public static final <T> Set<T> plus(@NotNull Set<? extends T> $this$plus, T element) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        LinkedHashSet result = new LinkedHashSet(MapsKt.mapCapacity($this$plus.size() + 1));
        result.addAll((Collection) $this$plus);
        result.add(element);
        return (Set<T>) result;
    }

    @NotNull
    public static final <T> Set<T> plus(@NotNull Set<? extends T> $this$plus, @NotNull T[] elements) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        LinkedHashSet result = new LinkedHashSet(MapsKt.mapCapacity($this$plus.size() + elements.length));
        result.addAll((Collection) $this$plus);
        CollectionsKt.addAll((Collection<? super Object>) result, elements);
        return (Set<T>) result;
    }

    @NotNull
    public static final <T> Set<T> plus(@NotNull Set<? extends T> $this$plus, @NotNull Iterable<? extends T> elements) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        Integer var3 = CollectionsKt.collectionSizeOrNull(elements);
        int var10000;
        if (var3 == null) {
            var10000 = $this$plus.size() * 2;
        } else {
            int it = ((Number) var3).intValue();
            ???;
            int var7 = $this$plus.size() + it;
            var10000 = var7;
        }
        int var8 = MapsKt.mapCapacity(var10000);
        LinkedHashSet result = new LinkedHashSet(var8);
        result.addAll((Collection) $this$plus);
        CollectionsKt.addAll((Collection<? super T>) result, elements);
        return (Set<T>) result;
    }

    @NotNull
    public static final <T> Set<T> plus(@NotNull Set<? extends T> $this$plus, @NotNull Sequence<? extends T> elements) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        LinkedHashSet result = new LinkedHashSet(MapsKt.mapCapacity($this$plus.size() * 2));
        result.addAll((Collection) $this$plus);
        CollectionsKt.addAll((Collection<? super T>) result, elements);
        return (Set<T>) result;
    }

    @InlineOnly
    private static final <T> Set<T> plusElement(Set<? extends T> $this$plusElement, T element) {
        Intrinsics.checkNotNullParameter($this$plusElement, "<this>");
        return SetsKt.plus($this$plusElement, (T) element);
    }

    public SetsKt___SetsKt() {
    }
}