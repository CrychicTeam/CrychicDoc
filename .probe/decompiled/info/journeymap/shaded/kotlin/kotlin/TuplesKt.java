package info.journeymap.shaded.kotlin.kotlin;

import info.journeymap.shaded.kotlin.kotlin.collections.CollectionsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmName;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.List;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\u001a2\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0002H\u00022\u0006\u0010\u0004\u001a\u0002H\u0003H\u0086\u0004¢\u0006\u0002\u0010\u0005\u001a\"\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u000e\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\b0\u0001\u001a(\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u0014\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\b0\t¨\u0006\n" }, d2 = { "to", "Linfo/journeymap/shaded/kotlin/kotlin/Pair;", "A", "B", "that", "(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;", "toList", "", "T", "Linfo/journeymap/shaded/kotlin/kotlin/Triple;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@JvmName(name = "TuplesKt")
public final class TuplesKt {

    @NotNull
    public static final <A, B> Pair<A, B> to(A $this$to, B that) {
        return new Pair<>((A) $this$to, (B) that);
    }

    @NotNull
    public static final <T> List<T> toList(@NotNull Pair<? extends T, ? extends T> $this$toList) {
        Intrinsics.checkNotNullParameter($this$toList, "<this>");
        Object[] var1 = new Object[] { $this$toList.getFirst(), $this$toList.getSecond() };
        return CollectionsKt.listOf((T[]) var1);
    }

    @NotNull
    public static final <T> List<T> toList(@NotNull Triple<? extends T, ? extends T, ? extends T> $this$toList) {
        Intrinsics.checkNotNullParameter($this$toList, "<this>");
        Object[] var1 = new Object[] { $this$toList.getFirst(), $this$toList.getSecond(), $this$toList.getThird() };
        return CollectionsKt.listOf((T[]) var1);
    }
}