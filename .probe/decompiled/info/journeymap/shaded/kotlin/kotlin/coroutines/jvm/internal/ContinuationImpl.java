package info.journeymap.shaded.kotlin.kotlin.coroutines.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.coroutines.Continuation;
import info.journeymap.shaded.kotlin.kotlin.coroutines.ContinuationInterceptor;
import info.journeymap.shaded.kotlin.kotlin.coroutines.CoroutineContext;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b!\u0018\u00002\u00020\u0001B\u0019\b\u0016\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0005B!\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0003J\b\u0010\r\u001a\u00020\u000eH\u0014R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0018\u0010\f\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/ContinuationImpl;", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/BaseContinuationImpl;", "completion", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/Continuation;", "", "(Lkotlin/coroutines/Continuation;)V", "_context", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/Continuation;Lkotlin/coroutines/CoroutineContext;)V", "context", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "intercepted", "releaseIntercepted", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.3")
public abstract class ContinuationImpl extends BaseContinuationImpl {

    @Nullable
    private final CoroutineContext _context;

    @Nullable
    private transient Continuation<Object> intercepted;

    public ContinuationImpl(@Nullable Continuation<Object> completion, @Nullable CoroutineContext _context) {
        super(completion);
        this._context = _context;
    }

    public ContinuationImpl(@Nullable Continuation<Object> completion) {
        this(completion, completion == null ? null : completion.getContext());
    }

    @NotNull
    @Override
    public CoroutineContext getContext() {
        CoroutineContext var10000 = this._context;
        Intrinsics.checkNotNull(this._context);
        return var10000;
    }

    @NotNull
    public final Continuation<Object> intercepted() {
        Continuation var1 = this.intercepted;
        Continuation var10000;
        if (var1 == null) {
            ContinuationInterceptor var2 = this.getContext().get(ContinuationInterceptor.Key);
            Continuation var5 = var2 == null ? this : var2.interceptContinuation(this);
            ???;
            this.intercepted = var5;
            var10000 = var5;
        } else {
            var10000 = var1;
        }
        return var10000;
    }

    @Override
    protected void releaseIntercepted() {
        Continuation intercepted = this.intercepted;
        if (intercepted != null && intercepted != this) {
            CoroutineContext.Element var10000 = this.getContext().get(ContinuationInterceptor.Key);
            Intrinsics.checkNotNull(var10000);
            ((ContinuationInterceptor) var10000).releaseInterceptedContinuation(intercepted);
        }
        this.intercepted = CompletedContinuation.INSTANCE;
    }
}