package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000.\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000b\u001a\u001b\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\f\u001a\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000f\u001a\u001b\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0010\u001a\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0013\u001a\u001b\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0014¨\u0006\u0015" }, d2 = { "numberFormatError", "", "input", "", "toByteOrNull", "", "(Ljava/lang/String;)Ljava/lang/Byte;", "radix", "", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLongOrNull", "", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShortOrNull", "", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/text/StringsKt")
class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt {

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Byte toByteOrNull(@NotNull String $this$toByteOrNull) {
        Intrinsics.checkNotNullParameter($this$toByteOrNull, "<this>");
        return StringsKt.toByteOrNull($this$toByteOrNull, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Byte toByteOrNull(@NotNull String $this$toByteOrNull, int radix) {
        Intrinsics.checkNotNullParameter($this$toByteOrNull, "<this>");
        Integer var3 = StringsKt.toIntOrNull($this$toByteOrNull, radix);
        if (var3 == null) {
            return null;
        } else {
        }
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Short toShortOrNull(@NotNull String $this$toShortOrNull) {
        Intrinsics.checkNotNullParameter($this$toShortOrNull, "<this>");
        return StringsKt.toShortOrNull($this$toShortOrNull, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Short toShortOrNull(@NotNull String $this$toShortOrNull, int radix) {
        Intrinsics.checkNotNullParameter($this$toShortOrNull, "<this>");
        Integer var3 = StringsKt.toIntOrNull($this$toShortOrNull, radix);
        if (var3 == null) {
            return null;
        } else {
        }
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Integer toIntOrNull(@NotNull String $this$toIntOrNull) {
        Intrinsics.checkNotNullParameter($this$toIntOrNull, "<this>");
        return StringsKt.toIntOrNull($this$toIntOrNull, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Integer toIntOrNull(@NotNull String $this$toIntOrNull, int radix) {
        Intrinsics.checkNotNullParameter($this$toIntOrNull, "<this>");
        CharsKt.checkRadix(radix);
        int length = $this$toIntOrNull.length();
        if (length == 0) {
            return null;
        } else {
            int start = 0;
            boolean isNegative = false;
            int limit = 0;
            char firstChar = $this$toIntOrNull.charAt(0);
            byte var13;
            if (Intrinsics.compare(firstChar, 48) < 0) {
                if (length == 1) {
                    return null;
                }
                var13 = 1;
                if (firstChar == '-') {
                    isNegative = true;
                    limit = Integer.MIN_VALUE;
                } else {
                    if (firstChar != '+') {
                        return null;
                    }
                    isNegative = false;
                    limit = -2147483647;
                }
            } else {
                var13 = 0;
                isNegative = false;
                limit = -2147483647;
            }
            int limitForMaxRadix = -59652323;
            int limitBeforeMul = limitForMaxRadix;
            int result = 0;
            int var10 = var13;
            while (var10 < length) {
                int i = var10++;
                int digit = CharsKt.digitOf($this$toIntOrNull.charAt(i), radix);
                if (digit < 0) {
                    return null;
                }
                if (result < limitBeforeMul) {
                    if (limitBeforeMul != limitForMaxRadix) {
                        return null;
                    }
                    limitBeforeMul = limit / radix;
                    if (result < limitBeforeMul) {
                        return null;
                    }
                }
                result *= radix;
                if (result < limit + digit) {
                    return null;
                }
                result -= digit;
            }
            return isNegative ? result : -result;
        }
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Long toLongOrNull(@NotNull String $this$toLongOrNull) {
        Intrinsics.checkNotNullParameter($this$toLongOrNull, "<this>");
        return StringsKt.toLongOrNull($this$toLongOrNull, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Long toLongOrNull(@NotNull String $this$toLongOrNull, int radix) {
        Intrinsics.checkNotNullParameter($this$toLongOrNull, "<this>");
        CharsKt.checkRadix(radix);
        int length = $this$toLongOrNull.length();
        if (length == 0) {
            return null;
        } else {
            int start = 0;
            boolean isNegative = false;
            long limit = 0L;
            char firstChar = $this$toLongOrNull.charAt(0);
            byte var17;
            if (Intrinsics.compare(firstChar, 48) < 0) {
                if (length == 1) {
                    return null;
                }
                var17 = 1;
                if (firstChar == '-') {
                    isNegative = true;
                    limit = Long.MIN_VALUE;
                } else {
                    if (firstChar != '+') {
                        return null;
                    }
                    isNegative = false;
                    limit = -9223372036854775807L;
                }
            } else {
                var17 = 0;
                isNegative = false;
                limit = -9223372036854775807L;
            }
            long limitForMaxRadix = -256204778801521550L;
            long limitBeforeMul = limitForMaxRadix;
            long result = 0L;
            int var14 = var17;
            while (var14 < length) {
                int i = var14++;
                int digit = CharsKt.digitOf($this$toLongOrNull.charAt(i), radix);
                if (digit < 0) {
                    return null;
                }
                if (result < limitBeforeMul) {
                    if (limitBeforeMul != limitForMaxRadix) {
                        return null;
                    }
                    limitBeforeMul = limit / (long) radix;
                    if (result < limitBeforeMul) {
                        return null;
                    }
                }
                result *= (long) radix;
                if (result < limit + (long) digit) {
                    return null;
                }
                result -= (long) digit;
            }
            return isNegative ? result : -result;
        }
    }

    @NotNull
    public static final Void numberFormatError(@NotNull String input) {
        Intrinsics.checkNotNullParameter(input, "input");
        throw new NumberFormatException("Invalid number format: '" + input + '\'');
    }

    public StringsKt__StringNumberConversionsKt() {
    }
}