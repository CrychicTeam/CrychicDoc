package info.journeymap.shaded.kotlin.kotlin.internal;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.collections.ArraysKt;
import info.journeymap.shaded.kotlin.kotlin.collections.CollectionsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmField;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.random.FallbackThreadLocalRandom;
import info.journeymap.shaded.kotlin.kotlin.random.Random;
import info.journeymap.shaded.kotlin.kotlin.text.MatchGroup;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.MatchResult;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u0012B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u00112\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\u0013" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/internal/PlatformImplementations;", "", "()V", "addSuppressed", "", "cause", "", "exception", "defaultPlatformRandom", "Linfo/journeymap/shaded/kotlin/kotlin/random/Random;", "getMatchResultNamedGroup", "Linfo/journeymap/shaded/kotlin/kotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "getSuppressed", "", "ReflectThrowable", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public class PlatformImplementations {

    public void addSuppressed(@NotNull Throwable cause, @NotNull Throwable exception) {
        Intrinsics.checkNotNullParameter(cause, "cause");
        Intrinsics.checkNotNullParameter(exception, "exception");
        Method var3 = PlatformImplementations.ReflectThrowable.addSuppressed;
        if (var3 != null) {
            Object[] var4 = new Object[] { exception };
            var3.invoke(cause, var4);
        }
    }

    @NotNull
    public List<Throwable> getSuppressed(@NotNull Throwable exception) {
        Intrinsics.checkNotNullParameter(exception, "exception");
        Method var3 = PlatformImplementations.ReflectThrowable.getSuppressed;
        Object var2 = var3 == null ? null : var3.invoke(exception);
        List var10000;
        if (var2 == null) {
            var10000 = CollectionsKt.emptyList();
        } else {
            ???;
            var10000 = ArraysKt.asList((Throwable[]) var2);
        }
        return var10000;
    }

    @Nullable
    public MatchGroup getMatchResultNamedGroup(@NotNull MatchResult matchResult, @NotNull String name) {
        Intrinsics.checkNotNullParameter(matchResult, "matchResult");
        Intrinsics.checkNotNullParameter(name, "name");
        throw new UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
    }

    @NotNull
    public Random defaultPlatformRandom() {
        return new FallbackThreadLocalRandom();
    }

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/internal/PlatformImplementations$ReflectThrowable;", "", "()V", "addSuppressed", "Ljava/lang/reflect/Method;", "getSuppressed", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    private static final class ReflectThrowable {

        @NotNull
        public static final PlatformImplementations.ReflectThrowable INSTANCE = new PlatformImplementations.ReflectThrowable();

        @JvmField
        @Nullable
        public static final Method addSuppressed;

        @JvmField
        @Nullable
        public static final Method getSuppressed;

        static {
            Class throwableClass = Throwable.class;
            Method[] throwableMethods = throwableClass.getMethods();
            Intrinsics.checkNotNullExpressionValue(throwableMethods, "throwableMethods");
            Method[] var4 = throwableMethods;
            int var5 = 0;
            int var6 = throwableMethods.length;
            Method var16;
            while (true) {
                if (var5 >= var6) {
                    var16 = null;
                    break;
                }
                Method var7;
                label34: {
                    var7 = var4[var5];
                    var5++;
                    ???;
                    if (Intrinsics.areEqual(var7.getName(), "addSuppressed")) {
                        Class[] var10 = var7.getParameterTypes();
                        Intrinsics.checkNotNullExpressionValue(var10, "it.parameterTypes");
                        if (Intrinsics.areEqual(ArraysKt.singleOrNull(var10), throwableClass)) {
                            var10000 = true;
                            break label34;
                        }
                    }
                    var10000 = false;
                }
                if (var10000) {
                    var16 = var7;
                    break;
                }
            }
            addSuppressed = var16;
            var4 = throwableMethods;
            var5 = 0;
            var6 = throwableMethods.length;
            while (true) {
                if (var5 < var6) {
                    Method var14 = var4[var5];
                    var5++;
                    ???;
                    if (!Intrinsics.areEqual(var14.getName(), "getSuppressed")) {
                        continue;
                    }
                    var16 = var14;
                    break;
                }
                var16 = null;
                break;
            }
            getSuppressed = var16;
        }
    }
}