package info.journeymap.shaded.kotlin.kotlin.coroutines;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function2;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bg\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fJ(\u0010\u0002\u001a\u0004\u0018\u0001H\u0003\"\b\b\u0000\u0010\u0003*\u00020\u00012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0005H\u0096\u0002¢\u0006\u0002\u0010\u0006J\"\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\t0\b\"\u0004\b\u0000\u0010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\t0\bH&J\u0014\u0010\u000b\u001a\u00020\f2\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J\u0014\u0010\r\u001a\u00020\u000e2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\bH\u0016¨\u0006\u0010" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/ContinuationInterceptor;", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext$Element;", "get", "E", "key", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "interceptContinuation", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/Continuation;", "T", "continuation", "minusKey", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext;", "releaseInterceptedContinuation", "", "Key", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.3")
public interface ContinuationInterceptor extends CoroutineContext.Element {

    @NotNull
    ContinuationInterceptor.Key Key = ContinuationInterceptor.Key.$$INSTANCE;

    @NotNull
    <T> Continuation<T> interceptContinuation(@NotNull Continuation<? super T> var1);

    void releaseInterceptedContinuation(@NotNull Continuation<?> var1);

    @Nullable
    @Override
    <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Key<E> var1);

    @NotNull
    @Override
    CoroutineContext minusKey(@NotNull CoroutineContext.Key<?> var1);

    @Metadata(mv = { 1, 6, 0 }, k = 3, xi = 48)
    public static final class DefaultImpls {

        public static void releaseInterceptedContinuation(@NotNull ContinuationInterceptor var0, @NotNull Continuation<?> continuation) {
            Intrinsics.checkNotNullParameter(this, "this");
            Intrinsics.checkNotNullParameter(continuation, "continuation");
        }

        @Nullable
        public static <E extends CoroutineContext.Element> E get(@NotNull ContinuationInterceptor var0, @NotNull CoroutineContext.Key<E> key) {
            Intrinsics.checkNotNullParameter(this, "this");
            Intrinsics.checkNotNullParameter(key, "key");
            if (key instanceof AbstractCoroutineContextKey) {
                CoroutineContext.Element var10000;
                if (((AbstractCoroutineContextKey) key).isSubKey$kotlin_stdlib(this.getKey())) {
                    CoroutineContext.Element var2 = ((AbstractCoroutineContextKey) key).tryCast$kotlin_stdlib(this);
                    var10000 = var2 instanceof CoroutineContext.Element ? var2 : null;
                } else {
                    var10000 = null;
                }
                return (E) var10000;
            } else {
                return (E) (ContinuationInterceptor.Key == key ? this : null);
            }
        }

        @NotNull
        public static CoroutineContext minusKey(@NotNull ContinuationInterceptor var0, @NotNull CoroutineContext.Key<?> key) {
            Intrinsics.checkNotNullParameter(this, "this");
            Intrinsics.checkNotNullParameter(key, "key");
            if (!(key instanceof AbstractCoroutineContextKey)) {
                return ContinuationInterceptor.Key == key ? EmptyCoroutineContext.INSTANCE : this;
            } else {
                return ((AbstractCoroutineContextKey) key).isSubKey$kotlin_stdlib(this.getKey()) && ((AbstractCoroutineContextKey) key).tryCast$kotlin_stdlib(this) != null ? EmptyCoroutineContext.INSTANCE : this;
            }
        }

        public static <R> R fold(@NotNull ContinuationInterceptor var0, R initial, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
            Intrinsics.checkNotNullParameter(this, "this");
            Intrinsics.checkNotNullParameter(operation, "operation");
            return CoroutineContext.Element.DefaultImpls.fold(this, (R) initial, operation);
        }

        @NotNull
        public static CoroutineContext plus(@NotNull ContinuationInterceptor var0, @NotNull CoroutineContext context) {
            Intrinsics.checkNotNullParameter(this, "this");
            Intrinsics.checkNotNullParameter(context, "context");
            return CoroutineContext.Element.DefaultImpls.plus(this, context);
        }
    }

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/ContinuationInterceptor$Key;", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext$Key;", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/ContinuationInterceptor;", "()V", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public static final class Key implements CoroutineContext.Key<ContinuationInterceptor> {

        private Key() {
        }
    }
}