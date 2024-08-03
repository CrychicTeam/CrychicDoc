package info.journeymap.shaded.kotlin.kotlin.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.io.Serializable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b&\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\t\u001a\u00020\nH\u0016R\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u000b" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/jvm/internal/Lambda;", "R", "Linfo/journeymap/shaded/kotlin/kotlin/jvm/internal/FunctionBase;", "Ljava/io/Serializable;", "arity", "", "(I)V", "getArity", "()I", "toString", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public abstract class Lambda<R> implements FunctionBase<R>, Serializable {

    private final int arity;

    public Lambda(int arity) {
        this.arity = arity;
    }

    @Override
    public int getArity() {
        return this.arity;
    }

    @NotNull
    public String toString() {
        String var1 = Reflection.renderLambdaToString(this);
        Intrinsics.checkNotNullExpressionValue(var1, "renderLambdaToString(this)");
        return var1;
    }
}