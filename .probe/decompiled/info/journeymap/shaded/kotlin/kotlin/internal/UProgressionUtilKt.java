package info.journeymap.shaded.kotlin.kotlin.internal;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.PublishedApi;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.UInt;
import info.journeymap.shaded.kotlin.kotlin.ULong;
import info.journeymap.shaded.kotlin.kotlin.UnsignedKt;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a*\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a*\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0006\u001a*\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012" }, d2 = { "differenceModulo", "Linfo/journeymap/shaded/kotlin/kotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Linfo/journeymap/shaded/kotlin/kotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class UProgressionUtilKt {

    private static final int differenceModulo_WZ9TVnA(/* $VF was: differenceModulo-WZ9TVnA*/
    int a, int b, int c) {
        int ac = UnsignedKt.uintRemainder - J1ME1BU(a, c);
        int bc = UnsignedKt.uintRemainder - J1ME1BU(b, c);
        int var10000;
        if (UnsignedKt.uintCompare(ac, bc) >= 0) {
            var10000 = UInt.constructor - impl(ac - bc);
        } else {
            int var5 = UInt.constructor - impl(ac - bc);
            var10000 = UInt.constructor - impl(var5 + c);
        }
        return var10000;
    }

    private static final long differenceModulo_sambcqE(/* $VF was: differenceModulo-sambcqE*/
    long a, long b, long c) {
        long ac = UnsignedKt.ulongRemainder - eb3DHEI(a, c);
        long bc = UnsignedKt.ulongRemainder - eb3DHEI(b, c);
        long var10000;
        if (UnsignedKt.ulongCompare(ac, bc) >= 0) {
            var10000 = ULong.constructor - impl(ac - bc);
        } else {
            long var10 = ULong.constructor - impl(ac - bc);
            var10000 = ULong.constructor - impl(var10 + c);
        }
        return var10000;
    }

    @PublishedApi
    @SinceKotlin(version = "1.3")
    public static final int getProgressionLastElement_Nkh28Cs(/* $VF was: getProgressionLastElement-Nkh28Cs*/
    int start, int end, int step) {
        int var10000;
        if (step > 0) {
            if (UnsignedKt.uintCompare(start, end) >= 0) {
                var10000 = end;
            } else {
                int var3 = differenceModulo - WZ9TVnA(end, start, UInt.constructor - impl(step));
                var10000 = UInt.constructor - impl(end - var3);
            }
        } else {
            if (step >= 0) {
                throw new IllegalArgumentException("Step is zero.");
            }
            if (UnsignedKt.uintCompare(start, end) <= 0) {
                var10000 = end;
            } else {
                int var4 = -step;
                var4 = differenceModulo - WZ9TVnA(start, end, UInt.constructor - impl(var4));
                var10000 = UInt.constructor - impl(end + var4);
            }
        }
        return var10000;
    }

    @PublishedApi
    @SinceKotlin(version = "1.3")
    public static final long getProgressionLastElement_7ftBX0g(/* $VF was: getProgressionLastElement-7ftBX0g*/
    long start, long end, long step) {
        long var10000;
        if (step > 0L) {
            if (UnsignedKt.ulongCompare(start, end) >= 0) {
                var10000 = end;
            } else {
                long var6 = differenceModulo - sambcqE(end, start, ULong.constructor - impl(step));
                var10000 = ULong.constructor - impl(end - var6);
            }
        } else {
            if (step >= 0L) {
                throw new IllegalArgumentException("Step is zero.");
            }
            if (UnsignedKt.ulongCompare(start, end) <= 0) {
                var10000 = end;
            } else {
                long var8 = -step;
                var8 = differenceModulo - sambcqE(start, end, ULong.constructor - impl(var8));
                var10000 = ULong.constructor - impl(end + var8);
            }
        }
        return var10000;
    }
}