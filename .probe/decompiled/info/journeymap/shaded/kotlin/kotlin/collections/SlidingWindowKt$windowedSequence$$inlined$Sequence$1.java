package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.sequences.Sequence;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.List;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u000f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096\u0002¨\u0006\u0004¸\u0006\u0000" }, d2 = { "info/journeymap/shaded/kotlin/kotlin/sequences/SequencesKt__SequencesKt$Sequence$1", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/Sequence;", "iterator", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class SlidingWindowKt$windowedSequence$$inlined$Sequence$1 implements Sequence<List<? extends T>> {

    public SlidingWindowKt$windowedSequence$$inlined$Sequence$1(Sequence var1, int var2, int var3, boolean var4, boolean var5) {
        this.$this_windowedSequence$inlined = var1;
        this.$size$inlined = var2;
        this.$step$inlined = var3;
        this.$partialWindows$inlined = var4;
        this.$reuseBuffer$inlined = var5;
    }

    @NotNull
    @Override
    public Iterator<List<? extends T>> iterator() {
        ???;
        return SlidingWindowKt.windowedIterator(this.$this_windowedSequence$inlined.iterator(), this.$size$inlined, this.$step$inlined, this.$partialWindows$inlined, this.$reuseBuffer$inlined);
    }
}