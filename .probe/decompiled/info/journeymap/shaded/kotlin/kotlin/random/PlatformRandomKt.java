package info.journeymap.shaded.kotlin.kotlin.random;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.internal.PlatformImplementationsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\t\u0010\u0000\u001a\u00020\u0001H\u0081\b\u001a\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0000\u001a\f\u0010\u0007\u001a\u00020\b*\u00020\u0001H\u0007\u001a\f\u0010\t\u001a\u00020\u0001*\u00020\bH\u0007¨\u0006\n" }, d2 = { "defaultPlatformRandom", "Linfo/journeymap/shaded/kotlin/kotlin/random/Random;", "doubleFromParts", "", "hi26", "", "low27", "asJavaRandom", "Ljava/util/Random;", "asKotlinRandom", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class PlatformRandomKt {

    @SinceKotlin(version = "1.3")
    @NotNull
    public static final java.util.Random asJavaRandom(@NotNull Random $this$asJavaRandom) {
        Intrinsics.checkNotNullParameter($this$asJavaRandom, "<this>");
        AbstractPlatformRandom var1 = $this$asJavaRandom instanceof AbstractPlatformRandom ? (AbstractPlatformRandom) $this$asJavaRandom : null;
        return var1 == null ? new KotlinRandom($this$asJavaRandom) : var1.getImpl();
    }

    @SinceKotlin(version = "1.3")
    @NotNull
    public static final Random asKotlinRandom(@NotNull java.util.Random $this$asKotlinRandom) {
        Intrinsics.checkNotNullParameter($this$asKotlinRandom, "<this>");
        KotlinRandom var1 = $this$asKotlinRandom instanceof KotlinRandom ? (KotlinRandom) $this$asKotlinRandom : null;
        return var1 == null ? new PlatformRandom($this$asKotlinRandom) : var1.getImpl();
    }

    @InlineOnly
    private static final Random defaultPlatformRandom() {
        return PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();
    }

    public static final double doubleFromParts(int hi26, int low27) {
        return (double) (((long) hi26 << 27) + (long) low27) / 9.007199E15F;
    }
}