package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.PublishedApi;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.Unit;
import info.journeymap.shaded.kotlin.kotlin.collections.builders.SetBuilder;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000B\n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0001\u001a?\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0006\u001a\u00020\u00072\u001d\u0010\b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0004\u0012\u00020\n0\t¢\u0006\u0002\b\u000bH\u0081\bø\u0001\u0000\u001a7\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u001d\u0010\b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0004\u0012\u0004\u0012\u00020\n0\t¢\u0006\u0002\b\u000bH\u0081\bø\u0001\u0000\u001a\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\"\u0004\b\u0000\u0010\u0002H\u0001\u001a\u001c\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0006\u001a\u00020\u0007H\u0001\u001a\u001f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u0001\"\u0004\b\u0000\u0010\u000e2\u0006\u0010\u000f\u001a\u0002H\u000e¢\u0006\u0002\u0010\u0010\u001a+\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u0012\"\u0004\b\u0000\u0010\u000e2\u0012\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u000e0\u0014\"\u0002H\u000e¢\u0006\u0002\u0010\u0015\u001aG\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u0012\"\u0004\b\u0000\u0010\u000e2\u001a\u0010\u0016\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0017j\n\u0012\u0006\b\u0000\u0012\u0002H\u000e`\u00182\u0012\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u000e0\u0014\"\u0002H\u000e¢\u0006\u0002\u0010\u0019\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001a" }, d2 = { "build", "", "E", "builder", "", "buildSetInternal", "capacity", "", "builderAction", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "", "Linfo/journeymap/shaded/kotlin/kotlin/ExtensionFunctionType;", "createSetBuilder", "setOf", "T", "element", "(Ljava/lang/Object;)Ljava/util/Set;", "sortedSetOf", "Ljava/util/TreeSet;", "elements", "", "([Ljava/lang/Object;)Ljava/util/TreeSet;", "comparator", "Ljava/util/Comparator;", "Linfo/journeymap/shaded/kotlin/kotlin/Comparator;", "(Ljava/util/Comparator;[Ljava/lang/Object;)Ljava/util/TreeSet;", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/collections/SetsKt")
class SetsKt__SetsJVMKt {

    @NotNull
    public static final <T> Set<T> setOf(T element) {
        Set var1 = Collections.singleton(element);
        Intrinsics.checkNotNullExpressionValue(var1, "singleton(element)");
        return var1;
    }

    @PublishedApi
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final <E> Set<E> buildSetInternal(Function1<? super Set<E>, Unit> builderAction) {
        Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        Set var1 = SetsKt.createSetBuilder();
        builderAction.invoke(var1);
        return SetsKt.build(var1);
    }

    @PublishedApi
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final <E> Set<E> buildSetInternal(int capacity, Function1<? super Set<E>, Unit> builderAction) {
        Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        Set var2 = SetsKt.createSetBuilder(capacity);
        builderAction.invoke(var2);
        return SetsKt.build(var2);
    }

    @PublishedApi
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <E> Set<E> createSetBuilder() {
        return new SetBuilder<>();
    }

    @PublishedApi
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <E> Set<E> createSetBuilder(int capacity) {
        return new SetBuilder<>(capacity);
    }

    @PublishedApi
    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <E> Set<E> build(@NotNull Set<E> builder) {
        Intrinsics.checkNotNullParameter(builder, "builder");
        return ((SetBuilder) builder).build();
    }

    @NotNull
    public static final <T> TreeSet<T> sortedSetOf(@NotNull T... elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return ArraysKt.toCollection(elements, new TreeSet());
    }

    @NotNull
    public static final <T> TreeSet<T> sortedSetOf(@NotNull Comparator<? super T> comparator, @NotNull T... elements) {
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        Intrinsics.checkNotNullParameter(elements, "elements");
        return ArraysKt.toCollection(elements, new TreeSet(comparator));
    }

    public SetsKt__SetsJVMKt() {
    }
}