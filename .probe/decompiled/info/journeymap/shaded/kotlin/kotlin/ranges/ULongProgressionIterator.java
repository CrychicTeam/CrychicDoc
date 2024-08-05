package info.journeymap.shaded.kotlin.kotlin.ranges;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.ULong;
import info.journeymap.shaded.kotlin.kotlin.UnsignedKt;
import info.journeymap.shaded.kotlin.kotlin.collections.ULongIterator;
import java.util.NoSuchElementException;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B \u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006ø\u0001\u0000¢\u0006\u0002\u0010\u0007J\t\u0010\n\u001a\u00020\u000bH\u0096\u0002J\u0015\u0010\r\u001a\u00020\u0003H\u0016ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u000e\u0010\u000fR\u0016\u0010\b\u001a\u00020\u0003X\u0082\u0004ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\u00020\u0003X\u0082\u000eø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\tR\u0016\u0010\u0005\u001a\u00020\u0003X\u0082\u0004ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\t\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006\u0010" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/ranges/ULongProgressionIterator;", "Linfo/journeymap/shaded/kotlin/kotlin/collections/ULongIterator;", "first", "Linfo/journeymap/shaded/kotlin/kotlin/ULong;", "last", "step", "", "(JJJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "finalElement", "J", "hasNext", "", "next", "nextULong", "nextULong-s-VKNKU", "()J", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.3")
final class ULongProgressionIterator extends ULongIterator {

    private final long finalElement;

    private boolean hasNext;

    private final long step;

    private long next;

    private ULongProgressionIterator(long first, long last, long step) {
        this.finalElement = last;
        this.hasNext = step > 0L ? UnsignedKt.ulongCompare(first, last) <= 0 : UnsignedKt.ulongCompare(first, last) >= 0;
        this.step = ULong.constructor - impl(step);
        this.next = this.hasNext ? first : this.finalElement;
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public long nextULong_s_VKNKU() /* $VF was: nextULong-s-VKNKU*/
    {
        long value = this.next;
        if (value == this.finalElement) {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            this.hasNext = false;
        } else {
            this.next = ULong.constructor - impl(this.next + this.step);
        }
        return value;
    }
}