package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmName;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000Z\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\n\n\u0002\b\u0002\u001a4\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u0002H\u00010\u0005H\u0082\b¢\u0006\u0004\b\u0006\u0010\u0007\u001a\r\u0010\b\u001a\u00020\t*\u00020\u0003H\u0087\b\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001a\u000e\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u0003H\u0007\u001a\u0016\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0007\u001a\r\u0010\r\u001a\u00020\u000e*\u00020\u0003H\u0087\b\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u000e\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007\u001a\u0016\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0007\u001a\r\u0010\u0012\u001a\u00020\u0013*\u00020\u0003H\u0087\b\u001a\u0014\u0010\u0012\u001a\u00020\u0013*\u0004\u0018\u00010\u0003H\u0087\b¢\u0006\u0002\b\u0014\u001a\r\u0010\u0015\u001a\u00020\u0016*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0015\u001a\u00020\u0016*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0018*\u00020\u0003H\u0087\b\u001a\u0013\u0010\u0019\u001a\u0004\u0018\u00010\u0018*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u001a\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0003H\u0087\b\u001a\u0013\u0010\u001d\u001a\u0004\u0018\u00010\u001c*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u001e\u001a\r\u0010\u001f\u001a\u00020\u0010*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u001f\u001a\u00020\u0010*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010 \u001a\u00020!*\u00020\u0003H\u0087\b\u001a\u0015\u0010 \u001a\u00020!*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010\"\u001a\u00020#*\u00020\u0003H\u0087\b\u001a\u0015\u0010\"\u001a\u00020#*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010$\u001a\u00020\u0003*\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010$\u001a\u00020\u0003*\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010$\u001a\u00020\u0003*\u00020!2\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010$\u001a\u00020\u0003*\u00020#2\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b¨\u0006%" }, d2 = { "screenFloatValue", "T", "str", "", "parse", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "screenFloatValue$StringsKt__StringNumberConversionsJVMKt", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "toBigDecimal", "Ljava/math/BigDecimal;", "mathContext", "Ljava/math/MathContext;", "toBigDecimalOrNull", "toBigInteger", "Ljava/math/BigInteger;", "radix", "", "toBigIntegerOrNull", "toBoolean", "", "toBooleanNullable", "toByte", "", "toDouble", "", "toDoubleOrNull", "(Ljava/lang/String;)Ljava/lang/Double;", "toFloat", "", "toFloatOrNull", "(Ljava/lang/String;)Ljava/lang/Float;", "toInt", "toLong", "", "toShort", "", "toString", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/text/StringsKt")
class StringsKt__StringNumberConversionsJVMKt extends StringsKt__StringBuilderKt {

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(byte $this$toString, int radix) {
        String var2 = Integer.toString($this$toString, CharsKt.checkRadix(CharsKt.checkRadix(radix)));
        Intrinsics.checkNotNullExpressionValue(var2, "toString(this, checkRadix(radix))");
        return var2;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(short $this$toString, int radix) {
        String var2 = Integer.toString($this$toString, CharsKt.checkRadix(CharsKt.checkRadix(radix)));
        Intrinsics.checkNotNullExpressionValue(var2, "toString(this, checkRadix(radix))");
        return var2;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(int $this$toString, int radix) {
        String var2 = Integer.toString($this$toString, CharsKt.checkRadix(radix));
        Intrinsics.checkNotNullExpressionValue(var2, "toString(this, checkRadix(radix))");
        return var2;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(long $this$toString, int radix) {
        String var3 = Long.toString($this$toString, CharsKt.checkRadix(radix));
        Intrinsics.checkNotNullExpressionValue(var3, "toString(this, checkRadix(radix))");
        return var3;
    }

    @JvmName(name = "toBooleanNullable")
    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final boolean toBooleanNullable(String $this$toBoolean) {
        return Boolean.parseBoolean($this$toBoolean);
    }

    @InlineOnly
    private static final byte toByte(String $this$toByte) {
        Intrinsics.checkNotNullParameter($this$toByte, "<this>");
        return Byte.parseByte($this$toByte);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte toByte(String $this$toByte, int radix) {
        Intrinsics.checkNotNullParameter($this$toByte, "<this>");
        return Byte.parseByte($this$toByte, CharsKt.checkRadix(radix));
    }

    @InlineOnly
    private static final short toShort(String $this$toShort) {
        Intrinsics.checkNotNullParameter($this$toShort, "<this>");
        return Short.parseShort($this$toShort);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short toShort(String $this$toShort, int radix) {
        Intrinsics.checkNotNullParameter($this$toShort, "<this>");
        return Short.parseShort($this$toShort, CharsKt.checkRadix(radix));
    }

    @InlineOnly
    private static final int toInt(String $this$toInt) {
        Intrinsics.checkNotNullParameter($this$toInt, "<this>");
        return Integer.parseInt($this$toInt);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int toInt(String $this$toInt, int radix) {
        Intrinsics.checkNotNullParameter($this$toInt, "<this>");
        return Integer.parseInt($this$toInt, CharsKt.checkRadix(radix));
    }

    @InlineOnly
    private static final long toLong(String $this$toLong) {
        Intrinsics.checkNotNullParameter($this$toLong, "<this>");
        return Long.parseLong($this$toLong);
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long toLong(String $this$toLong, int radix) {
        Intrinsics.checkNotNullParameter($this$toLong, "<this>");
        return Long.parseLong($this$toLong, CharsKt.checkRadix(radix));
    }

    @InlineOnly
    private static final float toFloat(String $this$toFloat) {
        Intrinsics.checkNotNullParameter($this$toFloat, "<this>");
        return Float.parseFloat($this$toFloat);
    }

    @InlineOnly
    private static final double toDouble(String $this$toDouble) {
        Intrinsics.checkNotNullParameter($this$toDouble, "<this>");
        return Double.parseDouble($this$toDouble);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Float toFloatOrNull(@NotNull String $this$toFloatOrNull) {
        Intrinsics.checkNotNullParameter($this$toFloatOrNull, "<this>");
        int $i$f$screenFloatValue = 0;
        Float p0;
        try {
            Float var10000;
            if (ScreenFloatValueRegEx.value.matches((CharSequence) $this$toFloatOrNull)) {
                ???;
                var10000 = Float.parseFloat($this$toFloatOrNull);
            } else {
                var10000 = null;
            }
            p0 = var10000;
        } catch (NumberFormatException var4) {
            p0 = null;
        }
        return p0;
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Double toDoubleOrNull(@NotNull String $this$toDoubleOrNull) {
        Intrinsics.checkNotNullParameter($this$toDoubleOrNull, "<this>");
        int $i$f$screenFloatValue = 0;
        Double p0;
        try {
            Double var10000;
            if (ScreenFloatValueRegEx.value.matches((CharSequence) $this$toDoubleOrNull)) {
                ???;
                var10000 = Double.parseDouble($this$toDoubleOrNull);
            } else {
                var10000 = null;
            }
            p0 = var10000;
        } catch (NumberFormatException var4) {
            p0 = null;
        }
        return p0;
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(String $this$toBigInteger) {
        Intrinsics.checkNotNullParameter($this$toBigInteger, "<this>");
        return new BigInteger($this$toBigInteger);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(String $this$toBigInteger, int radix) {
        Intrinsics.checkNotNullParameter($this$toBigInteger, "<this>");
        return new BigInteger($this$toBigInteger, CharsKt.checkRadix(radix));
    }

    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigInteger toBigIntegerOrNull(@NotNull String $this$toBigIntegerOrNull) {
        Intrinsics.checkNotNullParameter($this$toBigIntegerOrNull, "<this>");
        return StringsKt.toBigIntegerOrNull($this$toBigIntegerOrNull, 10);
    }

    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigInteger toBigIntegerOrNull(@NotNull String $this$toBigIntegerOrNull, int radix) {
        Intrinsics.checkNotNullParameter($this$toBigIntegerOrNull, "<this>");
        CharsKt.checkRadix(radix);
        int length = $this$toBigIntegerOrNull.length();
        switch(length) {
            case 0:
                return null;
            case 1:
                if (CharsKt.digitOf($this$toBigIntegerOrNull.charAt(0), radix) < 0) {
                    return null;
                }
                break;
            default:
                int start = $this$toBigIntegerOrNull.charAt(0) == '-' ? 1 : 0;
                int var5 = start;
                while (var5 < length) {
                    int index = var5++;
                    if (CharsKt.digitOf($this$toBigIntegerOrNull.charAt(index), radix) < 0) {
                        return null;
                    }
                }
        }
        return new BigInteger($this$toBigIntegerOrNull, CharsKt.checkRadix(radix));
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(String $this$toBigDecimal) {
        Intrinsics.checkNotNullParameter($this$toBigDecimal, "<this>");
        return new BigDecimal($this$toBigDecimal);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(String $this$toBigDecimal, MathContext mathContext) {
        Intrinsics.checkNotNullParameter($this$toBigDecimal, "<this>");
        Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new BigDecimal($this$toBigDecimal, mathContext);
    }

    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigDecimal toBigDecimalOrNull(@NotNull String $this$toBigDecimalOrNull) {
        Intrinsics.checkNotNullParameter($this$toBigDecimalOrNull, "<this>");
        int $i$f$screenFloatValue = 0;
        BigDecimal it;
        try {
            BigDecimal var10000;
            if (ScreenFloatValueRegEx.value.matches((CharSequence) $this$toBigDecimalOrNull)) {
                ???;
                var10000 = new BigDecimal($this$toBigDecimalOrNull);
            } else {
                var10000 = null;
            }
            it = var10000;
        } catch (NumberFormatException var4) {
            it = null;
        }
        return it;
    }

    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigDecimal toBigDecimalOrNull(@NotNull String $this$toBigDecimalOrNull, @NotNull MathContext mathContext) {
        Intrinsics.checkNotNullParameter($this$toBigDecimalOrNull, "<this>");
        Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        int $i$f$screenFloatValue = 0;
        BigDecimal var6;
        try {
            BigDecimal var10000;
            if (ScreenFloatValueRegEx.value.matches((CharSequence) $this$toBigDecimalOrNull)) {
                ???;
                var10000 = new BigDecimal($this$toBigDecimalOrNull, mathContext);
            } else {
                var10000 = null;
            }
            var6 = var10000;
        } catch (NumberFormatException var7) {
            var6 = null;
        }
        return var6;
    }

    private static final <T> T screenFloatValue$StringsKt__StringNumberConversionsJVMKt(String str, Function1<? super String, ? extends T> parse) {
        int $i$f$screenFloatValue = 0;
        Object var3;
        try {
            var3 = ScreenFloatValueRegEx.value.matches((CharSequence) str) ? parse.invoke(str) : null;
        } catch (NumberFormatException var5) {
            var3 = null;
        }
        return (T) var3;
    }

    public StringsKt__StringNumberConversionsJVMKt() {
    }
}