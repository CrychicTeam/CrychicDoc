package info.journeymap.shaded.kotlin.kotlin.sequences;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.Unit;
import info.journeymap.shaded.kotlin.kotlin.coroutines.Continuation;
import info.journeymap.shaded.kotlin.kotlin.coroutines.RestrictsSuspension;
import info.journeymap.shaded.kotlin.kotlin.coroutines.intrinsics.IntrinsicsKt;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Iterator;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u0002B\u0007\b\u0000¢\u0006\u0002\u0010\u0003J\u0019\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00028\u0000H¦@ø\u0001\u0000¢\u0006\u0002\u0010\u0007J\u001f\u0010\b\u001a\u00020\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u001f\u0010\b\u001a\u00020\u00052\f\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\rH¦@ø\u0001\u0000¢\u0006\u0002\u0010\u000eJ\u001f\u0010\b\u001a\u00020\u00052\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u0010H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0011\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/sequences/SequenceScope;", "T", "", "()V", "yield", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "yieldAll", "elements", "", "(Ljava/lang/Iterable;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "iterator", "", "(Ljava/util/Iterator;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sequence", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/Sequence;", "(Lkotlin/sequences/Sequence;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@RestrictsSuspension
@SinceKotlin(version = "1.3")
public abstract class SequenceScope<T> {

    @Nullable
    public abstract Object yield(T var1, @NotNull Continuation<? super Unit> var2);

    @Nullable
    public abstract Object yieldAll(@NotNull Iterator<? extends T> var1, @NotNull Continuation<? super Unit> var2);

    @Nullable
    public final Object yieldAll(@NotNull Iterable<? extends T> elements, @NotNull Continuation<? super Unit> $completion) {
        if (elements instanceof Collection && ((Collection) elements).isEmpty()) {
            return Unit.INSTANCE;
        } else {
            Object var10000 = this.yieldAll(elements.iterator(), $completion);
            return var10000 == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? var10000 : Unit.INSTANCE;
        }
    }

    @Nullable
    public final Object yieldAll(@NotNull Sequence<? extends T> sequence, @NotNull Continuation<? super Unit> $completion) {
        Object var10000 = this.yieldAll(sequence.iterator(), $completion);
        return var10000 == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? var10000 : Unit.INSTANCE;
    }
}