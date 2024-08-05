package info.journeymap.shaded.kotlin.kotlin.sequences;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.Result;
import info.journeymap.shaded.kotlin.kotlin.ResultKt;
import info.journeymap.shaded.kotlin.kotlin.Unit;
import info.journeymap.shaded.kotlin.kotlin.coroutines.Continuation;
import info.journeymap.shaded.kotlin.kotlin.coroutines.CoroutineContext;
import info.journeymap.shaded.kotlin.kotlin.coroutines.EmptyCoroutineContext;
import info.journeymap.shaded.kotlin.kotlin.coroutines.intrinsics.IntrinsicsKt;
import info.journeymap.shaded.kotlin.kotlin.coroutines.jvm.internal.DebugProbesKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMappedMarker;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.NoSuchElementException;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\b\u0012\u0004\u0012\u00020\u00050\u0004B\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\t\u0010\u0018\u001a\u00020\u0019H\u0096\u0002J\u000e\u0010\u001a\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u001bJ\r\u0010\u001c\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u001bJ\u001e\u0010\u001d\u001a\u00020\u00052\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00050\u001fH\u0016ø\u0001\u0000¢\u0006\u0002\u0010 J\u0019\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00028\u0000H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010#J\u001f\u0010$\u001a\u00020\u00052\f\u0010%\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010&R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0012\u0010\u0011\u001a\u0004\u0018\u00018\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0012R\u0012\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006'" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/sequences/SequenceBuilderIterator;", "T", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/SequenceScope;", "", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/Continuation;", "", "()V", "context", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "nextIterator", "nextStep", "getNextStep", "()Lkotlin/coroutines/Continuation;", "setNextStep", "(Lkotlin/coroutines/Continuation;)V", "nextValue", "Ljava/lang/Object;", "state", "", "Linfo/journeymap/shaded/kotlin/kotlin/sequences/State;", "exceptionalState", "", "hasNext", "", "next", "()Ljava/lang/Object;", "nextNotReady", "resumeWith", "result", "Linfo/journeymap/shaded/kotlin/kotlin/Result;", "(Ljava/lang/Object;)V", "yield", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "yieldAll", "iterator", "(Ljava/util/Iterator;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
final class SequenceBuilderIterator<T> extends SequenceScope<T> implements Iterator<T>, Continuation<Unit>, KMappedMarker {

    private int state;

    @Nullable
    private T nextValue;

    @Nullable
    private Iterator<? extends T> nextIterator;

    @Nullable
    private Continuation<? super Unit> nextStep;

    public SequenceBuilderIterator() {
    }

    @Nullable
    public final Continuation<Unit> getNextStep() {
        return this.nextStep;
    }

    public final void setNextStep(@Nullable Continuation<? super Unit> var1) {
        ???;
    }

    public boolean hasNext() {
        while (true) {
            int step = this.state;
            switch(step) {
                case 1:
                    Iterator var10000 = this.nextIterator;
                    Intrinsics.checkNotNull(this.nextIterator);
                    if (var10000.hasNext()) {
                        this.state = 2;
                        return true;
                    }
                    this.nextIterator = null;
                case 0:
                    this.state = 5;
                    Continuation var4 = this.nextStep;
                    Intrinsics.checkNotNull(this.nextStep);
                    Continuation stepx = var4;
                    this.nextStep = null;
                    Result.Companion var2 = Result.Companion;
                    stepx.resumeWith(Result.constructor - impl(Unit.INSTANCE));
                    break;
                case 2:
                case 3:
                    return true;
                case 4:
                    return false;
                default:
                    throw this.exceptionalState();
            }
        }
    }

    public T next() {
        int var1 = this.state;
        switch(var1) {
            case 0:
            case 1:
                return this.nextNotReady();
            case 2:
                this.state = 1;
                Iterator var10000 = this.nextIterator;
                Intrinsics.checkNotNull(this.nextIterator);
                return (T) var10000.next();
            case 3:
                this.state = 0;
                Object result = this.nextValue;
                this.nextValue = null;
                return (T) result;
            default:
                throw this.exceptionalState();
        }
    }

    private final T nextNotReady() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        } else {
            return this.next();
        }
    }

    private final Throwable exceptionalState() {
        int var1 = this.state;
        Throwable var10000;
        switch(var1) {
            case 4:
                var10000 = (Throwable) (new NoSuchElementException());
                break;
            case 5:
                var10000 = (Throwable) (new IllegalStateException("Iterator has failed."));
                break;
            default:
                var10000 = (Throwable) (new IllegalStateException(Intrinsics.stringPlus("Unexpected state of the iterator: ", this.state)));
        }
        return var10000;
    }

    @Nullable
    @Override
    public Object yield(T value, @NotNull Continuation<? super Unit> $completion) {
        this.nextValue = (T) value;
        this.state = 3;
        ???;
        this.setNextStep($completion);
        Object var10000 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (var10000 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended($completion);
        }
        return var10000 == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? var10000 : Unit.INSTANCE;
    }

    @Nullable
    @Override
    public Object yieldAll(@NotNull Iterator<? extends T> iterator, @NotNull Continuation<? super Unit> $completion) {
        if (!iterator.hasNext()) {
            return Unit.INSTANCE;
        } else {
            this.nextIterator = iterator;
            this.state = 2;
            ???;
            this.setNextStep($completion);
            Object var10000 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (var10000 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended($completion);
            }
            return var10000 == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? var10000 : Unit.INSTANCE;
        }
    }

    @Override
    public void resumeWith(@NotNull Object result) {
        ResultKt.throwOnFailure(result);
        this.state = 4;
    }

    @NotNull
    @Override
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}