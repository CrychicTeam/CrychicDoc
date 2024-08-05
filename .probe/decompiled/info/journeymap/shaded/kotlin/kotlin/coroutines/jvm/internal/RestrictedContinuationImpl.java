package info.journeymap.shaded.kotlin.kotlin.coroutines.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.coroutines.Continuation;
import info.journeymap.shaded.kotlin.kotlin.coroutines.CoroutineContext;
import info.journeymap.shaded.kotlin.kotlin.coroutines.EmptyCoroutineContext;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b!\u0018\u00002\u00020\u0001B\u0017\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\n" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/RestrictedContinuationImpl;", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/BaseContinuationImpl;", "completion", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/Continuation;", "", "(Lkotlin/coroutines/Continuation;)V", "context", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.3")
public abstract class RestrictedContinuationImpl extends BaseContinuationImpl {

    public RestrictedContinuationImpl(@Nullable Continuation<Object> completion) {
        super(completion);
        if (completion != null) {
            ???;
            boolean var6 = completion.getContext() == EmptyCoroutineContext.INSTANCE;
            if (!var6) {
                ???;
                String var8 = "Coroutines with restricted suspension must have EmptyCoroutineContext";
                throw new IllegalArgumentException(var8.toString());
            }
        }
    }

    @NotNull
    @Override
    public CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }
}