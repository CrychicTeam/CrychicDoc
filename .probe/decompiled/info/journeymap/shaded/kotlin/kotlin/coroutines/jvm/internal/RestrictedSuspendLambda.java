package info.journeymap.shaded.kotlin.kotlin.coroutines.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.coroutines.Continuation;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.FunctionBase;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Reflection;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b!\u0018\u00002\u00020\u00012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u00022\u00020\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007B\u001f\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0010\u0010\b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0018\u00010\t¢\u0006\u0002\u0010\nJ\b\u0010\r\u001a\u00020\u000eH\u0016R\u0014\u0010\u0005\u001a\u00020\u0006X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u000f" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/RestrictedSuspendLambda;", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/RestrictedContinuationImpl;", "Linfo/journeymap/shaded/kotlin/kotlin/jvm/internal/FunctionBase;", "", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/SuspendFunction;", "arity", "", "(I)V", "completion", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/Continuation;", "(ILkotlin/coroutines/Continuation;)V", "getArity", "()I", "toString", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.3")
public abstract class RestrictedSuspendLambda extends RestrictedContinuationImpl implements FunctionBase<Object>, SuspendFunction {

    private final int arity;

    public RestrictedSuspendLambda(int arity, @Nullable Continuation<Object> completion) {
        super(completion);
        this.arity = arity;
    }

    @Override
    public int getArity() {
        return this.arity;
    }

    public RestrictedSuspendLambda(int arity) {
        this(arity, null);
    }

    @NotNull
    @Override
    public String toString() {
        String var10000;
        if (this.getCompletion() == null) {
            String var1 = Reflection.renderLambdaToString(this);
            Intrinsics.checkNotNullExpressionValue(var1, "renderLambdaToString(this)");
            var10000 = var1;
        } else {
            var10000 = super.toString();
        }
        return var10000;
    }
}