package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.ResultKt;
import info.journeymap.shaded.kotlin.kotlin.Unit;
import info.journeymap.shaded.kotlin.kotlin.coroutines.Continuation;
import info.journeymap.shaded.kotlin.kotlin.coroutines.intrinsics.IntrinsicsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function2;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.ranges.RangesKt;
import info.journeymap.shaded.kotlin.kotlin.sequences.Sequence;
import info.journeymap.shaded.kotlin.kotlin.sequences.SequenceScope;
import info.journeymap.shaded.kotlin.kotlin.sequences.SequencesKt;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u001aH\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u0006\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000\u001aD\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u000e\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000¨\u0006\u000f" }, d2 = { "checkWindowSizeStep", "", "size", "", "step", "windowedIterator", "", "", "T", "iterator", "partialWindows", "", "reuseBuffer", "windowedSequence", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/Sequence;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class SlidingWindowKt {

    public static final void checkWindowSizeStep(int size, int step) {
        boolean var2 = size > 0 && step > 0;
        if (!var2) {
            ???;
            String var4 = size != step ? "Both size " + size + " and step " + step + " must be greater than zero." : "size " + size + " must be greater than zero.";
            throw new IllegalArgumentException(var4.toString());
        }
    }

    @NotNull
    public static final <T> Sequence<List<T>> windowedSequence(@NotNull Sequence<? extends T> $this$windowedSequence, int size, int step, boolean partialWindows, boolean reuseBuffer) {
        Intrinsics.checkNotNullParameter($this$windowedSequence, "<this>");
        checkWindowSizeStep(size, step);
        return new SlidingWindowKt$windowedSequence$$inlined$Sequence$1($this$windowedSequence, size, step, partialWindows, reuseBuffer);
    }

    @NotNull
    public static final <T> Iterator<List<T>> windowedIterator(@NotNull final Iterator<? extends T> iterator, final int size, final int step, final boolean partialWindows, final boolean reuseBuffer) {
        Intrinsics.checkNotNullParameter(iterator, "iterator");
        ???;
    }
}