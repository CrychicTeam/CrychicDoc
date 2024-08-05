package info.journeymap.shaded.kotlin.kotlin.sequences;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.OverloadResolutionByLambdaReturnType;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmName;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000D\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001aA\u0010\u0005\u001a\u0002H\u0006\"\u0010\b\u0000\u0010\u0006*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0007\"\u0004\b\u0001\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\b\u001a\u0002H\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004¢\u0006\u0002\u0010\t\u001a5\u0010\n\u001a\u00020\u000b\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00012\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020\u000b0\u000eH\u0087\bø\u0001\u0000¢\u0006\u0002\b\u000f\u001a5\u0010\n\u001a\u00020\u0010\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00012\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020\u00100\u000eH\u0087\bø\u0001\u0000¢\u0006\u0002\b\u0011\u001a&\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\f0\u0013\"\u000e\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u0014*\b\u0012\u0004\u0012\u0002H\f0\u0001\u001a8\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\f0\u0013\"\u0004\b\u0000\u0010\f*\b\u0012\u0004\u0012\u0002H\f0\u00012\u001a\u0010\u0015\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\f0\u0016j\n\u0012\u0006\b\u0000\u0012\u0002H\f`\u0017\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0018" }, d2 = { "filterIsInstance", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/Sequence;", "R", "klass", "Ljava/lang/Class;", "filterIsInstanceTo", "C", "", "destination", "(Lkotlin/sequences/Sequence;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "sumOf", "Ljava/math/BigDecimal;", "T", "selector", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "", "comparator", "Ljava/util/Comparator;", "Linfo/journeymap/shaded/kotlin/kotlin/Comparator;", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/sequences/SequencesKt")
class SequencesKt___SequencesJvmKt extends SequencesKt__SequencesKt {

    @NotNull
    public static final <R> Sequence<R> filterIsInstance(@NotNull Sequence<?> $this$filterIsInstance, @NotNull final Class<R> klass) {
        Intrinsics.checkNotNullParameter($this$filterIsInstance, "<this>");
        Intrinsics.checkNotNullParameter(klass, "klass");
        return SequencesKt.filter($this$filterIsInstance, (Function1<? super R, Boolean>) (new Function1<Object, Boolean>() {

            @NotNull
            public final Boolean invoke(@Nullable Object it) {
                return klass.isInstance(it);
            }
        }));
    }

    @NotNull
    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(@NotNull Sequence<?> $this$filterIsInstanceTo, @NotNull C destination, @NotNull Class<R> klass) {
        Intrinsics.checkNotNullParameter($this$filterIsInstanceTo, "<this>");
        Intrinsics.checkNotNullParameter(destination, "destination");
        Intrinsics.checkNotNullParameter(klass, "klass");
        for (Object element : $this$filterIsInstanceTo) {
            if (klass.isInstance(element)) {
                destination.add(element);
            }
        }
        return (C) destination;
    }

    @NotNull
    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(@NotNull Sequence<? extends T> $this$toSortedSet) {
        Intrinsics.checkNotNullParameter($this$toSortedSet, "<this>");
        return SequencesKt.toCollection($this$toSortedSet, (Collection) (new TreeSet()));
    }

    @NotNull
    public static final <T> SortedSet<T> toSortedSet(@NotNull Sequence<? extends T> $this$toSortedSet, @NotNull Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter($this$toSortedSet, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return SequencesKt.toCollection($this$toSortedSet, (Collection) (new TreeSet(comparator)));
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigDecimal")
    @InlineOnly
    private static final <T> BigDecimal sumOfBigDecimal(Sequence<? extends T> $this$sumOf, Function1<? super T, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal element = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(element, "valueOf(this.toLong())");
        BigDecimal sum = element;
        for (Object elementx : $this$sumOf) {
            BigDecimal var5 = sum.add((BigDecimal) selector.invoke(elementx));
            Intrinsics.checkNotNullExpressionValue(var5, "this.add(other)");
            sum = var5;
        }
        return sum;
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigInteger")
    @InlineOnly
    private static final <T> BigInteger sumOfBigInteger(Sequence<? extends T> $this$sumOf, Function1<? super T, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger element = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(element, "valueOf(this.toLong())");
        BigInteger sum = element;
        for (Object elementx : $this$sumOf) {
            BigInteger var5 = sum.add((BigInteger) selector.invoke(elementx));
            Intrinsics.checkNotNullExpressionValue(var5, "this.add(other)");
            sum = var5;
        }
        return sum;
    }

    public SequencesKt___SequencesJvmKt() {
    }
}