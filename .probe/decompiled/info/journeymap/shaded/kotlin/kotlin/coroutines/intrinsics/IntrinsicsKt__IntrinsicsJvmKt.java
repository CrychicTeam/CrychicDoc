package info.journeymap.shaded.kotlin.kotlin.coroutines.intrinsics;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.ResultKt;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.Unit;
import info.journeymap.shaded.kotlin.kotlin.coroutines.Continuation;
import info.journeymap.shaded.kotlin.kotlin.coroutines.CoroutineContext;
import info.journeymap.shaded.kotlin.kotlin.coroutines.EmptyCoroutineContext;
import info.journeymap.shaded.kotlin.kotlin.coroutines.jvm.internal.BaseContinuationImpl;
import info.journeymap.shaded.kotlin.kotlin.coroutines.jvm.internal.ContinuationImpl;
import info.journeymap.shaded.kotlin.kotlin.coroutines.jvm.internal.DebugProbesKt;
import info.journeymap.shaded.kotlin.kotlin.coroutines.jvm.internal.RestrictedContinuationImpl;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function2;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function3;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.TypeIntrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\u001aF\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u00012\u001c\b\u0004\u0010\u0005\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006H\u0083\b¢\u0006\u0002\b\b\u001aD\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\n\u001a]\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f¢\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a\u001e\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001aA\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0003*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001aZ\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0003*#\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f¢\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001an\u0010\u0011\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0014\"\u0004\b\u0002\u0010\u0003*)\b\u0001\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0001\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0015¢\u0006\u0002\b\r2\u0006\u0010\u000e\u001a\u0002H\u000b2\u0006\u0010\u0016\u001a\u0002H\u00142\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0001H\u0081\bø\u0001\u0000¢\u0006\u0002\u0010\u0017\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0018" }, d2 = { "createCoroutineFromSuspendFunction", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/Continuation;", "", "T", "completion", "block", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "", "createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt", "createCoroutineUnintercepted", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "R", "Linfo/journeymap/shaded/kotlin/kotlin/Function2;", "Linfo/journeymap/shaded/kotlin/kotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "intercepted", "startCoroutineUninterceptedOrReturn", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "P", "Linfo/journeymap/shaded/kotlin/kotlin/Function3;", "param", "(Lkotlin/jvm/functions/Function3;Ljava/lang/Object;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/coroutines/intrinsics/IntrinsicsKt")
class IntrinsicsKt__IntrinsicsJvmKt {

    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final <T> Object startCoroutineUninterceptedOrReturn(Function1<? super Continuation<? super T>, ? extends Object> $this$startCoroutineUninterceptedOrReturn, Continuation<? super T> completion) {
        Intrinsics.checkNotNullParameter($this$startCoroutineUninterceptedOrReturn, "<this>");
        Intrinsics.checkNotNullParameter(completion, "completion");
        return ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity($this$startCoroutineUninterceptedOrReturn, 1)).invoke(completion);
    }

    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final <R, T> Object startCoroutineUninterceptedOrReturn(Function2<? super R, ? super Continuation<? super T>, ? extends Object> $this$startCoroutineUninterceptedOrReturn, R receiver, Continuation<? super T> completion) {
        Intrinsics.checkNotNullParameter($this$startCoroutineUninterceptedOrReturn, "<this>");
        Intrinsics.checkNotNullParameter(completion, "completion");
        return ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity($this$startCoroutineUninterceptedOrReturn, 2)).invoke(receiver, completion);
    }

    @InlineOnly
    private static final <R, P, T> Object startCoroutineUninterceptedOrReturn(Function3<? super R, ? super P, ? super Continuation<? super T>, ? extends Object> $this$startCoroutineUninterceptedOrReturn, R receiver, P param, Continuation<? super T> completion) {
        Intrinsics.checkNotNullParameter($this$startCoroutineUninterceptedOrReturn, "<this>");
        Intrinsics.checkNotNullParameter(completion, "completion");
        return ((Function3) TypeIntrinsics.beforeCheckcastToFunctionOfArity($this$startCoroutineUninterceptedOrReturn, 3)).invoke(receiver, param, completion);
    }

    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <T> Continuation<Unit> createCoroutineUnintercepted(@NotNull Function1<? super Continuation<? super T>, ? extends Object> $this$createCoroutineUnintercepted, @NotNull Continuation<? super T> completion) {
        Intrinsics.checkNotNullParameter($this$createCoroutineUnintercepted, "<this>");
        Intrinsics.checkNotNullParameter(completion, "completion");
        Continuation probeCompletion = DebugProbesKt.probeCoroutineCreated(completion);
        Continuation var10000;
        if ($this$createCoroutineUnintercepted instanceof BaseContinuationImpl) {
            var10000 = ((BaseContinuationImpl) $this$createCoroutineUnintercepted).create(probeCompletion);
        } else {
            int $i$f$createCoroutineFromSuspendFunction = 0;
            CoroutineContext context$iv = probeCompletion.getContext();
            var10000 = context$iv == EmptyCoroutineContext.INSTANCE ? new IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$1(probeCompletion, $this$createCoroutineUnintercepted) : new IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$2(probeCompletion, context$iv, $this$createCoroutineUnintercepted);
        }
        return var10000;
    }

    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <R, T> Continuation<Unit> createCoroutineUnintercepted(@NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> $this$createCoroutineUnintercepted, R receiver, @NotNull Continuation<? super T> completion) {
        Intrinsics.checkNotNullParameter($this$createCoroutineUnintercepted, "<this>");
        Intrinsics.checkNotNullParameter(completion, "completion");
        Continuation probeCompletion = DebugProbesKt.probeCoroutineCreated(completion);
        Continuation var10000;
        if ($this$createCoroutineUnintercepted instanceof BaseContinuationImpl) {
            var10000 = ((BaseContinuationImpl) $this$createCoroutineUnintercepted).create(receiver, probeCompletion);
        } else {
            int $i$f$createCoroutineFromSuspendFunction = 0;
            CoroutineContext context$iv = probeCompletion.getContext();
            var10000 = context$iv == EmptyCoroutineContext.INSTANCE ? new IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$3(probeCompletion, $this$createCoroutineUnintercepted, receiver) : new IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$4(probeCompletion, context$iv, $this$createCoroutineUnintercepted, receiver);
        }
        return var10000;
    }

    @SinceKotlin(version = "1.3")
    @NotNull
    public static final <T> Continuation<T> intercepted(@NotNull Continuation<? super T> $this$intercepted) {
        Intrinsics.checkNotNullParameter($this$intercepted, "<this>");
        ContinuationImpl var1 = $this$intercepted instanceof ContinuationImpl ? (ContinuationImpl) $this$intercepted : null;
        return var1 == null ? $this$intercepted : var1.intercepted();
    }

    @SinceKotlin(version = "1.3")
    private static final <T> Continuation<Unit> createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt(final Continuation<? super T> completion, final Function1<? super Continuation<? super T>, ? extends Object> block) {
        int $i$f$createCoroutineFromSuspendFunction = 0;
        final CoroutineContext context = completion.getContext();
        return (Continuation<Unit>) (context == EmptyCoroutineContext.INSTANCE ? new RestrictedContinuationImpl() {

            private int label;

            @Nullable
            @Override
            protected Object invokeSuspend(@NotNull Object result) {
                int var2 = this.label;
                Object var10000;
                switch(var2) {
                    case 0:
                        this.label = 1;
                        ResultKt.throwOnFailure(result);
                        var10000 = block.invoke(this);
                        break;
                    case 1:
                        this.label = 2;
                        ResultKt.throwOnFailure(result);
                        var10000 = result;
                        break;
                    default:
                        throw new IllegalStateException("This coroutine had already completed".toString());
                }
                return var10000;
            }
        } : new ContinuationImpl() {

            private int label;

            @Nullable
            @Override
            protected Object invokeSuspend(@NotNull Object result) {
                int var2 = this.label;
                Object var10000;
                switch(var2) {
                    case 0:
                        this.label = 1;
                        ResultKt.throwOnFailure(result);
                        var10000 = block.invoke(this);
                        break;
                    case 1:
                        this.label = 2;
                        ResultKt.throwOnFailure(result);
                        var10000 = result;
                        break;
                    default:
                        throw new IllegalStateException("This coroutine had already completed".toString());
                }
                return var10000;
            }
        });
    }

    public IntrinsicsKt__IntrinsicsJvmKt() {
    }
}