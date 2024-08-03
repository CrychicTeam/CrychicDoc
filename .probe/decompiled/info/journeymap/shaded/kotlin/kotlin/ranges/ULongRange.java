package info.journeymap.shaded.kotlin.kotlin.ranges;

import info.journeymap.shaded.kotlin.kotlin.ExperimentalUnsignedTypes;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.ULong;
import info.journeymap.shaded.kotlin.kotlin.UnsignedKt;
import info.journeymap.shaded.kotlin.kotlin.WasExperimental;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00172\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002:\u0001\u0017B\u0018\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0002\u0010\u0006J\u001b\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0003H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\r\u0010\u000eJ\u0013\u0010\u000f\u001a\u00020\u000b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u000bH\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016R\u001a\u0010\u0005\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000ø\u0001\u0001¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\u0004\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000ø\u0001\u0001¢\u0006\u0006\u001a\u0004\b\t\u0010\bø\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006\u0018" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/ranges/ULongRange;", "Linfo/journeymap/shaded/kotlin/kotlin/ranges/ULongProgression;", "Linfo/journeymap/shaded/kotlin/kotlin/ranges/ClosedRange;", "Linfo/journeymap/shaded/kotlin/kotlin/ULong;", "start", "endInclusive", "(JJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "getEndInclusive-s-VKNKU", "()J", "getStart-s-VKNKU", "contains", "", "value", "contains-VKZWuLQ", "(J)Z", "equals", "other", "", "hashCode", "", "isEmpty", "toString", "", "Companion", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.5")
@WasExperimental(markerClass = { ExperimentalUnsignedTypes.class })
public final class ULongRange extends ULongProgression implements ClosedRange<ULong> {

    @NotNull
    public static final ULongRange.Companion Companion = new ULongRange.Companion(null);

    @NotNull
    private static final ULongRange EMPTY = new ULongRange(-1L, 0L, null);

    private ULongRange(long start, long endInclusive) {
        super(start, endInclusive, 1L, null);
    }

    public long getStart_s_VKNKU() /* $VF was: getStart-s-VKNKU*/
    {
        return this.getFirst - s - VKNKU();
    }

    public long getEndInclusive_s_VKNKU() /* $VF was: getEndInclusive-s-VKNKU*/
    {
        return this.getLast - s - VKNKU();
    }

    public boolean contains_VKZWuLQ(/* $VF was: contains-VKZWuLQ*/
    long value) {
        long var3 = this.getFirst - s - VKNKU();
        if (UnsignedKt.ulongCompare(var3, value) <= 0) {
            var3 = this.getLast - s - VKNKU();
            if (UnsignedKt.ulongCompare(value, var3) <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return UnsignedKt.ulongCompare(this.getFirst - s - VKNKU(), this.getLast - s - VKNKU()) > 0;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return other instanceof ULongRange && (this.isEmpty() && ((ULongRange) other).isEmpty() || this.getFirst - s - VKNKU() == ((ULongRange) other).getFirst - s - VKNKU() && this.getLast - s - VKNKU() == ((ULongRange) other).getLast - s - VKNKU());
    }

    @Override
    public int hashCode() {
        return this.isEmpty() ? -1 : 31 * (int) ULong.constructor - impl(this.getFirst - s - VKNKU() ^ ULong.constructor - impl(this.getFirst - s - VKNKU() >>> 32)) + (int) ULong.constructor - impl(this.getLast - s - VKNKU() ^ ULong.constructor - impl(this.getLast - s - VKNKU() >>> 32));
    }

    @NotNull
    @Override
    public String toString() {
        return ULong.toString - impl(this.getFirst - s - VKNKU()) + ".." + ULong.toString - impl(this.getLast - s - VKNKU());
    }

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/ranges/ULongRange$Companion;", "", "()V", "EMPTY", "Linfo/journeymap/shaded/kotlin/kotlin/ranges/ULongRange;", "getEMPTY", "()Lkotlin/ranges/ULongRange;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public static final class Companion {

        private Companion() {
        }

        @NotNull
        public final ULongRange getEMPTY() {
            return ULongRange.EMPTY;
        }
    }
}