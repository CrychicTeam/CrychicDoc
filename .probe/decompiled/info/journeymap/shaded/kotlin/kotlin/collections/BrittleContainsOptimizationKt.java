package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.sequences.Sequence;
import info.journeymap.shaded.kotlin.kotlin.sequences.SequencesKt;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000 \n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a#\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0000¢\u0006\u0002\u0010\u0004\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0000\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0000\u001a,\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00052\f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H\u0000\u001a\u0018\u0010\t\u001a\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0002¨\u0006\u000b" }, d2 = { "convertToSetForSetOperation", "", "T", "", "([Ljava/lang/Object;)Ljava/util/Collection;", "", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/Sequence;", "convertToSetForSetOperationWith", "source", "safeToConvertToSet", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class BrittleContainsOptimizationKt {

    private static final <T> boolean safeToConvertToSet(Collection<? extends T> $this$safeToConvertToSet) {
        int $i$f$brittleContainsOptimizationEnabled = 0;
        return CollectionSystemProperties.brittleContainsOptimizationEnabled && $this$safeToConvertToSet.size() > 2 && $this$safeToConvertToSet instanceof ArrayList;
    }

    @NotNull
    public static final <T> Collection<T> convertToSetForSetOperationWith(@NotNull Iterable<? extends T> $this$convertToSetForSetOperationWith, @NotNull Iterable<? extends T> source) {
        Intrinsics.checkNotNullParameter($this$convertToSetForSetOperationWith, "<this>");
        Intrinsics.checkNotNullParameter(source, "source");
        Collection var10000;
        if ($this$convertToSetForSetOperationWith instanceof Set) {
            var10000 = (Collection) $this$convertToSetForSetOperationWith;
        } else if ($this$convertToSetForSetOperationWith instanceof Collection) {
            var10000 = source instanceof Collection && ((Collection) source).size() < 2 ? (Collection) $this$convertToSetForSetOperationWith : (safeToConvertToSet((Collection<? extends T>) $this$convertToSetForSetOperationWith) ? (Collection) CollectionsKt.toHashSet($this$convertToSetForSetOperationWith) : (Collection) $this$convertToSetForSetOperationWith);
        } else {
            int $i$f$brittleContainsOptimizationEnabled = 0;
            var10000 = CollectionSystemProperties.brittleContainsOptimizationEnabled ? (Collection) CollectionsKt.toHashSet($this$convertToSetForSetOperationWith) : (Collection) CollectionsKt.toList($this$convertToSetForSetOperationWith);
        }
        return var10000;
    }

    @NotNull
    public static final <T> Collection<T> convertToSetForSetOperation(@NotNull Iterable<? extends T> $this$convertToSetForSetOperation) {
        Intrinsics.checkNotNullParameter($this$convertToSetForSetOperation, "<this>");
        Collection var10000;
        if ($this$convertToSetForSetOperation instanceof Set) {
            var10000 = (Collection) $this$convertToSetForSetOperation;
        } else if ($this$convertToSetForSetOperation instanceof Collection) {
            var10000 = safeToConvertToSet((Collection<? extends T>) $this$convertToSetForSetOperation) ? (Collection) CollectionsKt.toHashSet($this$convertToSetForSetOperation) : (Collection) $this$convertToSetForSetOperation;
        } else {
            int $i$f$brittleContainsOptimizationEnabled = 0;
            var10000 = CollectionSystemProperties.brittleContainsOptimizationEnabled ? (Collection) CollectionsKt.toHashSet($this$convertToSetForSetOperation) : (Collection) CollectionsKt.toList($this$convertToSetForSetOperation);
        }
        return var10000;
    }

    @NotNull
    public static final <T> Collection<T> convertToSetForSetOperation(@NotNull Sequence<? extends T> $this$convertToSetForSetOperation) {
        Intrinsics.checkNotNullParameter($this$convertToSetForSetOperation, "<this>");
        int $i$f$brittleContainsOptimizationEnabled = 0;
        return CollectionSystemProperties.brittleContainsOptimizationEnabled ? (Collection) SequencesKt.<T>toHashSet($this$convertToSetForSetOperation) : (Collection) SequencesKt.<T>toList($this$convertToSetForSetOperation);
    }

    @NotNull
    public static final <T> Collection<T> convertToSetForSetOperation(@NotNull T[] $this$convertToSetForSetOperation) {
        Intrinsics.checkNotNullParameter($this$convertToSetForSetOperation, "<this>");
        int $i$f$brittleContainsOptimizationEnabled = 0;
        return CollectionSystemProperties.brittleContainsOptimizationEnabled ? (Collection) ArraysKt.<T>toHashSet((T[]) $this$convertToSetForSetOperation) : (Collection) ArraysKt.<T>asList((T[]) $this$convertToSetForSetOperation);
    }
}