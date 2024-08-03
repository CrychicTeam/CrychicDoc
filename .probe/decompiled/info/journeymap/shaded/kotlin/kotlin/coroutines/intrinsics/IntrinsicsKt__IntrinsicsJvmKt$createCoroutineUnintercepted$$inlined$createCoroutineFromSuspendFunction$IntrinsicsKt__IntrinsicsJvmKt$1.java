package info.journeymap.shaded.kotlin.kotlin.coroutines.intrinsics;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.ResultKt;
import info.journeymap.shaded.kotlin.kotlin.coroutines.Continuation;
import info.journeymap.shaded.kotlin.kotlin.coroutines.jvm.internal.RestrictedContinuationImpl;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.TypeIntrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\"\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u000e\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0007H\u0014ø\u0001\u0000¢\u0006\u0002\u0010\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\t¸\u0006\u0000" }, d2 = { "info/journeymap/shaded/kotlin/kotlin/coroutines/intrinsics/IntrinsicsKt__IntrinsicsJvmKt$createCoroutineFromSuspendFunction$1", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/RestrictedContinuationImpl;", "label", "", "invokeSuspend", "", "result", "Linfo/journeymap/shaded/kotlin/kotlin/Result;", "(Ljava/lang/Object;)Ljava/lang/Object;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$1 extends RestrictedContinuationImpl {

    private int label;

    public IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnintercepted$$inlined$createCoroutineFromSuspendFunction$IntrinsicsKt__IntrinsicsJvmKt$1(Continuation $completion, Function1 var2) {
        super($completion);
        this.$completion = $completion;
        this.$this_createCoroutineUnintercepted$inlined = var2;
    }

    @Nullable
    @Override
    protected Object invokeSuspend(@NotNull Object result) {
        int var2 = this.label;
        Object var10000;
        switch(var2) {
            case 0:
                this.label = 1;
                ResultKt.throwOnFailure(result);
                Continuation it = this;
                ???;
                var10000 = ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(this.$this_createCoroutineUnintercepted$inlined, 1)).invoke(it);
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
}