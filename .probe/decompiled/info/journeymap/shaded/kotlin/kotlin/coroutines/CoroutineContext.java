package info.journeymap.shaded.kotlin.kotlin.coroutines;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function2;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001:\u0002\u0011\u0012J5\u0010\u0002\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u00032\u0006\u0010\u0004\u001a\u0002H\u00032\u0018\u0010\u0005\u001a\u0014\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u0002H\u00030\u0006H&¢\u0006\u0002\u0010\bJ(\u0010\t\u001a\u0004\u0018\u0001H\n\"\b\b\u0000\u0010\n*\u00020\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\n0\fH¦\u0002¢\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u00020\u00002\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\fH&J\u0011\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u0000H\u0096\u0002¨\u0006\u0013" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext;", "", "fold", "R", "initial", "operation", "Linfo/journeymap/shaded/kotlin/kotlin/Function2;", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext$Element;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "key", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusKey", "plus", "context", "Element", "Key", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.3")
public interface CoroutineContext {

    @Nullable
    <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Key<E> var1);

    <R> R fold(R var1, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> var2);

    @NotNull
    CoroutineContext plus(@NotNull CoroutineContext var1);

    @NotNull
    CoroutineContext minusKey(@NotNull CoroutineContext.Key<?> var1);

    @Metadata(mv = { 1, 6, 0 }, k = 3, xi = 48)
    public static final class DefaultImpls {

        @NotNull
        public static CoroutineContext plus(@NotNull CoroutineContext var0, @NotNull CoroutineContext context) {
            Intrinsics.checkNotNullParameter(this, "this");
            Intrinsics.checkNotNullParameter(context, "context");
            ???;
        }
    }

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J5\u0010\u0006\u001a\u0002H\u0007\"\u0004\b\u0000\u0010\u00072\u0006\u0010\b\u001a\u0002H\u00072\u0018\u0010\t\u001a\u0014\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u00020\u0000\u0012\u0004\u0012\u0002H\u00070\nH\u0016¢\u0006\u0002\u0010\u000bJ(\u0010\f\u001a\u0004\u0018\u0001H\r\"\b\b\u0000\u0010\r*\u00020\u00002\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\r0\u0003H\u0096\u0002¢\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u00020\u00012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003H\u0016R\u0016\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u0010" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext$Element;", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext;", "key", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext$Key;", "getKey", "()Lkotlin/coroutines/CoroutineContext$Key;", "fold", "R", "initial", "operation", "Linfo/journeymap/shaded/kotlin/kotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "minusKey", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public interface Element extends CoroutineContext {

        @NotNull
        CoroutineContext.Key<?> getKey();

        @Nullable
        @Override
        <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Key<E> var1);

        @Override
        <R> R fold(R var1, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> var2);

        @NotNull
        @Override
        CoroutineContext minusKey(@NotNull CoroutineContext.Key<?> var1);

        @Metadata(mv = { 1, 6, 0 }, k = 3, xi = 48)
        public static final class DefaultImpls {

            @Nullable
            public static <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Element var0, @NotNull CoroutineContext.Key<E> key) {
                Intrinsics.checkNotNullParameter(this, "this");
                Intrinsics.checkNotNullParameter(key, "key");
                return (E) (Intrinsics.areEqual(this.getKey(), key) ? this : null);
            }

            public static <R> R fold(@NotNull CoroutineContext.Element var0, R initial, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
                Intrinsics.checkNotNullParameter(this, "this");
                Intrinsics.checkNotNullParameter(operation, "operation");
                return (R) operation.invoke(initial, this);
            }

            @NotNull
            public static CoroutineContext minusKey(@NotNull CoroutineContext.Element var0, @NotNull CoroutineContext.Key<?> key) {
                Intrinsics.checkNotNullParameter(this, "this");
                Intrinsics.checkNotNullParameter(key, "key");
                return Intrinsics.areEqual(this.getKey(), key) ? EmptyCoroutineContext.INSTANCE : this;
            }

            @NotNull
            public static CoroutineContext plus(@NotNull CoroutineContext.Element var0, @NotNull CoroutineContext context) {
                Intrinsics.checkNotNullParameter(this, "this");
                Intrinsics.checkNotNullParameter(context, "context");
                return CoroutineContext.DefaultImpls.plus(this, context);
            }
        }
    }

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\bf\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003¨\u0006\u0004" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext$Key;", "E", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/CoroutineContext$Element;", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public interface Key<E extends CoroutineContext.Element> {
    }
}