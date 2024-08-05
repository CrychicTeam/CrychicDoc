package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.ExperimentalStdlibApi;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.WasExperimental;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000\u001e\n\u0000\n\u0002\u0010\f\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0007\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0007\u001a\f\u0010\u0004\u001a\u00020\u0002*\u00020\u0001H\u0007\u001a\u0014\u0010\u0004\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0007\u001a\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007¢\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0005\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0007\u001a\u001c\u0010\b\u001a\u00020\t*\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\b\b\u0002\u0010\u000b\u001a\u00020\t\u001a\n\u0010\f\u001a\u00020\t*\u00020\u0001\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00012\u0006\u0010\n\u001a\u00020\u000eH\u0087\n\u001a\f\u0010\u000f\u001a\u00020\u000e*\u00020\u0001H\u0007¨\u0006\u0010" }, d2 = { "digitToChar", "", "", "radix", "digitToInt", "digitToIntOrNull", "(C)Ljava/lang/Integer;", "(CI)Ljava/lang/Integer;", "equals", "", "other", "ignoreCase", "isSurrogate", "plus", "", "titlecase", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/text/CharsKt")
class CharsKt__CharKt extends CharsKt__CharJVMKt {

    @SinceKotlin(version = "1.5")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    public static final int digitToInt(char $this$digitToInt) {
        int var1 = CharsKt.digitOf($this$digitToInt, 10);
        ???;
        if (var1 < 0) {
            throw new IllegalArgumentException("Char " + $this$digitToInt + " is not a decimal digit");
        } else {
            return var1;
        }
    }

    @SinceKotlin(version = "1.5")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    public static final int digitToInt(char $this$digitToInt, int radix) {
        Integer var2 = CharsKt.digitToIntOrNull($this$digitToInt, radix);
        if (var2 == null) {
            throw new IllegalArgumentException("Char " + $this$digitToInt + " is not a digit in the given radix=" + radix);
        } else {
            return var2;
        }
    }

    @SinceKotlin(version = "1.5")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @Nullable
    public static final Integer digitToIntOrNull(char $this$digitToIntOrNull) {
        Integer var1 = CharsKt.digitOf($this$digitToIntOrNull, 10);
        int it = ((Number) var1).intValue();
        ???;
        return it >= 0 ? var1 : null;
    }

    @SinceKotlin(version = "1.5")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @Nullable
    public static final Integer digitToIntOrNull(char $this$digitToIntOrNull, int radix) {
        CharsKt.checkRadix(radix);
        Integer var2 = CharsKt.digitOf($this$digitToIntOrNull, radix);
        int it = ((Number) var2).intValue();
        ???;
        return it >= 0 ? var2 : null;
    }

    @SinceKotlin(version = "1.5")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    public static final char digitToChar(int $this$digitToChar) {
        if (0 <= $this$digitToChar ? $this$digitToChar < 10 : false) {
            return (char) (48 + $this$digitToChar);
        } else {
            throw new IllegalArgumentException("Int " + $this$digitToChar + " is not a decimal digit");
        }
    }

    @SinceKotlin(version = "1.5")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    public static final char digitToChar(int $this$digitToChar, int radix) {
        if (2 <= radix ? radix >= 37 : true) {
            throw new IllegalArgumentException("Invalid radix: " + radix + ". Valid radix values are in range 2..36");
        } else if ($this$digitToChar >= 0 && $this$digitToChar < radix) {
            return $this$digitToChar < 10 ? (char) (48 + $this$digitToChar) : (char) ((char) (65 + $this$digitToChar) - '\n');
        } else {
            throw new IllegalArgumentException("Digit " + $this$digitToChar + " does not represent a valid digit in radix " + radix);
        }
    }

    @SinceKotlin(version = "1.5")
    @NotNull
    public static final String titlecase(char $this$titlecase) {
        return _OneToManyTitlecaseMappingsKt.titlecaseImpl($this$titlecase);
    }

    @InlineOnly
    private static final String plus(char $this$plus, String other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return $this$plus + other;
    }

    public static final boolean equals(char $this$equals, char other, boolean ignoreCase) {
        if ($this$equals == other) {
            return true;
        } else if (!ignoreCase) {
            return false;
        } else {
            char thisUpper = Character.toUpperCase($this$equals);
            char otherUpper = Character.toUpperCase(other);
            return thisUpper == otherUpper || Character.toLowerCase(thisUpper) == Character.toLowerCase(otherUpper);
        }
    }

    public static final boolean isSurrogate(char $this$isSurrogate) {
        return '\ud800' <= $this$isSurrogate ? $this$isSurrogate < '\ue000' : false;
    }

    public CharsKt__CharKt() {
    }
}