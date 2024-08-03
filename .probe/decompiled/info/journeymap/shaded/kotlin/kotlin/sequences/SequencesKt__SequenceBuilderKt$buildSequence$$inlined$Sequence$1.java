package info.journeymap.shaded.kotlin.kotlin.sequences;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function2;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Iterator;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 176, d1 = { "\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u000f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096\u0002¨\u0006\u0004¸\u0006\u0000" }, d2 = { "info/journeymap/shaded/kotlin/kotlin/sequences/SequencesKt__SequencesKt$Sequence$1", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/Sequence;", "iterator", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class SequencesKt__SequenceBuilderKt$buildSequence$$inlined$Sequence$1 implements Sequence<T> {

    public SequencesKt__SequenceBuilderKt$buildSequence$$inlined$Sequence$1(Function2 var1) {
        this.$builderAction$inlined = var1;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        ???;
        return SequencesKt.iterator(this.$builderAction$inlined);
    }
}