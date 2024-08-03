package info.journeymap.shaded.kotlin.kotlin.ranges;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.collections.CharIterator;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import java.util.NoSuchElementException;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0000\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\t\u001a\u00020\nH\u0096\u0002J\b\u0010\u000e\u001a\u00020\u0003H\u0016R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u000f" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/ranges/CharProgressionIterator;", "Linfo/journeymap/shaded/kotlin/kotlin/collections/CharIterator;", "first", "", "last", "step", "", "(CCI)V", "finalElement", "hasNext", "", "next", "getStep", "()I", "nextChar", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class CharProgressionIterator extends CharIterator {

    private final int step;

    private final int finalElement;

    private boolean hasNext;

    private int next;

    public CharProgressionIterator(char first, char last, int step) {
        this.step = step;
        this.finalElement = last;
        this.hasNext = this.step > 0 ? Intrinsics.compare(first, last) <= 0 : Intrinsics.compare(first, last) >= 0;
        this.next = this.hasNext ? first : this.finalElement;
    }

    public final int getStep() {
        return this.step;
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public char nextChar() {
        int value = this.next;
        if (value == this.finalElement) {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            this.hasNext = false;
        } else {
            this.next = this.next + this.step;
        }
        return (char) value;
    }
}