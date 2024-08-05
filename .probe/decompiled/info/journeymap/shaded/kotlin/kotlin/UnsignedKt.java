package info.journeymap.shaded.kotlin.kotlin;

import info.journeymap.shaded.kotlin.kotlin.jvm.JvmName;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.text.CharsKt;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u0004\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0001\u001a\"\u0010\f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b\r\u0010\u000e\u001a\"\u0010\u000f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u000e\u001a\u0010\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\tH\u0001\u001a\u0018\u0010\u0012\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\u0013H\u0001\u001a\"\u0010\u0014\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a\"\u0010\u0017\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0018\u0010\u0016\u001a\u0010\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0013H\u0001\u001a\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u0013H\u0000\u001a\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\tH\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001d" }, d2 = { "doubleToUInt", "Linfo/journeymap/shaded/kotlin/kotlin/UInt;", "v", "", "(D)I", "doubleToULong", "Linfo/journeymap/shaded/kotlin/kotlin/ULong;", "(D)J", "uintCompare", "", "v1", "v2", "uintDivide", "uintDivide-J1ME1BU", "(II)I", "uintRemainder", "uintRemainder-J1ME1BU", "uintToDouble", "ulongCompare", "", "ulongDivide", "ulongDivide-eb3DHEI", "(JJ)J", "ulongRemainder", "ulongRemainder-eb3DHEI", "ulongToDouble", "ulongToString", "", "base", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@JvmName(name = "UnsignedKt")
public final class UnsignedKt {

    @PublishedApi
    public static final int uintCompare(int v1, int v2) {
        return Intrinsics.compare(v1 ^ Integer.MIN_VALUE, v2 ^ Integer.MIN_VALUE);
    }

    @PublishedApi
    public static final int ulongCompare(long v1, long v2) {
        return Intrinsics.compare(v1 ^ Long.MIN_VALUE, v2 ^ Long.MIN_VALUE);
    }

    @PublishedApi
    public static final int uintDivide_J1ME1BU(/* $VF was: uintDivide-J1ME1BU*/
    int v1, int v2) {
        long var2 = ((long) v1 & 4294967295L) / ((long) v2 & 4294967295L);
        return UInt.constructor - impl((int) var2);
    }

    @PublishedApi
    public static final int uintRemainder_J1ME1BU(/* $VF was: uintRemainder-J1ME1BU*/
    int v1, int v2) {
        long var2 = ((long) v1 & 4294967295L) % ((long) v2 & 4294967295L);
        return UInt.constructor - impl((int) var2);
    }

    @PublishedApi
    public static final long ulongDivide_eb3DHEI(/* $VF was: ulongDivide-eb3DHEI*/
    long v1, long v2) {
        if (v2 < 0L) {
            return ulongCompare(v1, v2) < 0 ? ULong.constructor - impl(0L) : ULong.constructor - impl(1L);
        } else if (v1 >= 0L) {
            return ULong.constructor - impl(v1 / v2);
        } else {
            long quotient = (v1 >>> 1) / v2 << 1;
            long rem = v1 - quotient * v2;
            long var12 = ULong.constructor - impl(rem);
            long var14 = ULong.constructor - impl(v2);
            return ULong.constructor - impl(quotient + (long) (ulongCompare(var12, var14) >= 0 ? 1 : 0));
        }
    }

    @PublishedApi
    public static final long ulongRemainder_eb3DHEI(/* $VF was: ulongRemainder-eb3DHEI*/
    long v1, long v2) {
        if (v2 < 0L) {
            return ulongCompare(v1, v2) < 0 ? v1 : ULong.constructor - impl(v1 - v2);
        } else if (v1 >= 0L) {
            return ULong.constructor - impl(v1 % v2);
        } else {
            long quotient = (v1 >>> 1) / v2 << 1;
            long rem = v1 - quotient * v2;
            long var12 = ULong.constructor - impl(rem);
            long var14 = ULong.constructor - impl(v2);
            return ULong.constructor - impl(rem - (ulongCompare(var12, var14) >= 0 ? v2 : 0L));
        }
    }

    @PublishedApi
    public static final int doubleToUInt(double v) {
        return Double.isNaN(v) ? 0 : (v <= uintToDouble(0) ? 0 : (v >= uintToDouble(-1) ? -1 : (v <= 2.147483647E9 ? UInt.constructor - impl((int) v) : UInt.constructor - impl(UInt.constructor - impl((int) (v - (double) Integer.MAX_VALUE)) + UInt.constructor - impl(Integer.MAX_VALUE)))));
    }

    @PublishedApi
    public static final long doubleToULong(double v) {
        return Double.isNaN(v) ? 0L : (v <= ulongToDouble(0L) ? 0L : (v >= ulongToDouble(-1L) ? -1L : (v < 9.223372E18F ? ULong.constructor - impl((long) v) : ULong.constructor - impl(ULong.constructor - impl((long) (v - 9.223372E18F)) + Long.MIN_VALUE))));
    }

    @PublishedApi
    public static final double uintToDouble(int v) {
        return (double) (v & 2147483647) + (double) (v >>> 31 << 30) * (double) 2;
    }

    @PublishedApi
    public static final double ulongToDouble(long v) {
        return (double) (v >>> 11) * (double) 2048 + (double) (v & 2047L);
    }

    @NotNull
    public static final String ulongToString(long v) {
        return ulongToString(v, 10);
    }

    @NotNull
    public static final String ulongToString(long v, int base) {
        if (v >= 0L) {
            String var10 = Long.toString(v, CharsKt.checkRadix(base));
            Intrinsics.checkNotNullExpressionValue(var10, "toString(this, checkRadix(radix))");
            return var10;
        } else {
            long quotient = (v >>> 1) / (long) base << 1;
            long rem = v - quotient * (long) base;
            if (rem >= (long) base) {
                rem -= (long) base;
                quotient++;
            }
            String var9 = Long.toString(quotient, CharsKt.checkRadix(base));
            Intrinsics.checkNotNullExpressionValue(var9, "toString(this, checkRadix(radix))");
            String var11 = Long.toString(rem, CharsKt.checkRadix(base));
            Intrinsics.checkNotNullExpressionValue(var11, "toString(this, checkRadix(radix))");
            return Intrinsics.stringPlus(var9, var11);
        }
    }
}