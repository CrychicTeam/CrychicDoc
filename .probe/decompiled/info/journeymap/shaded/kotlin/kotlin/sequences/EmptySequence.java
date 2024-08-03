package info.journeymap.shaded.kotlin.kotlin.sequences;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.collections.EmptyIterator;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Iterator;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010(\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\b\u0012\u0004\u0012\u00020\u00020\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00020\tH\u0096\u0002J\u0010\u0010\n\u001a\u00020\u00002\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\u000b" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/sequences/EmptySequence;", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/Sequence;", "", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/DropTakeSequence;", "()V", "drop", "n", "", "iterator", "", "take", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
final class EmptySequence implements Sequence, DropTakeSequence {

    @NotNull
    public static final EmptySequence INSTANCE = new EmptySequence();

    private EmptySequence() {
    }

    @NotNull
    @Override
    public Iterator iterator() {
        return (Iterator) EmptyIterator.INSTANCE;
    }

    @NotNull
    public EmptySequence drop(int n) {
        return INSTANCE;
    }

    @NotNull
    public EmptySequence take(int n) {
        return INSTANCE;
    }
}