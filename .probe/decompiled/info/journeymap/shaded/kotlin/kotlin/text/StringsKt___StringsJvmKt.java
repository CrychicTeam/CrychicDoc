package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.OverloadResolutionByLambdaReturnType;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmName;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000,\n\u0000\n\u0002\u0010\f\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a)\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00060\bH\u0087\bø\u0001\u0000¢\u0006\u0002\b\t\u001a)\u0010\u0005\u001a\u00020\n*\u00020\u00022\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\n0\bH\u0087\bø\u0001\u0000¢\u0006\u0002\b\u000b\u001a\u0010\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\r*\u00020\u0002\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u000e" }, d2 = { "elementAt", "", "", "index", "", "sumOf", "Ljava/math/BigDecimal;", "selector", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/text/StringsKt")
class StringsKt___StringsJvmKt extends StringsKt__StringsKt {

    @InlineOnly
    private static final char elementAt(CharSequence $this$elementAt, int index) {
        Intrinsics.checkNotNullParameter($this$elementAt, "<this>");
        return $this$elementAt.charAt(index);
    }

    @NotNull
    public static final SortedSet<Character> toSortedSet(@NotNull CharSequence $this$toSortedSet) {
        Intrinsics.checkNotNullParameter($this$toSortedSet, "<this>");
        return StringsKt.toCollection($this$toSortedSet, (Collection) (new TreeSet()));
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigDecimal")
    @InlineOnly
    private static final BigDecimal sumOfBigDecimal(CharSequence $this$sumOf, Function1<? super Character, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal var4 = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(var4, "valueOf(this.toLong())");
        BigDecimal sum = var4;
        CharSequence var3 = $this$sumOf;
        int var7 = 0;
        while (var7 < var3.length()) {
            char element = var3.charAt(var7);
            var7++;
            BigDecimal var6 = sum.add((BigDecimal) selector.invoke(element));
            Intrinsics.checkNotNullExpressionValue(var6, "this.add(other)");
            sum = var6;
        }
        return sum;
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigInteger")
    @InlineOnly
    private static final BigInteger sumOfBigInteger(CharSequence $this$sumOf, Function1<? super Character, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger var4 = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(var4, "valueOf(this.toLong())");
        BigInteger sum = var4;
        CharSequence var3 = $this$sumOf;
        int var7 = 0;
        while (var7 < var3.length()) {
            char element = var3.charAt(var7);
            var7++;
            BigInteger var6 = sum.add((BigInteger) selector.invoke(element));
            Intrinsics.checkNotNullExpressionValue(var6, "this.add(other)");
            sum = var6;
        }
        return sum;
    }

    public StringsKt___StringsJvmKt() {
    }
}