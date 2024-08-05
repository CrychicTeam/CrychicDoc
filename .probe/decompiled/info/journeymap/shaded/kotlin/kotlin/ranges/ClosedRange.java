package info.journeymap.shaded.kotlin.kotlin.ranges;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000f\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\bf\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\nH\u0016R\u0012\u0010\u0004\u001a\u00028\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00028\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\u0006¨\u0006\u000e" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/ranges/ClosedRange;", "T", "", "", "endInclusive", "getEndInclusive", "()Ljava/lang/Comparable;", "start", "getStart", "contains", "", "value", "(Ljava/lang/Comparable;)Z", "isEmpty", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public interface ClosedRange<T extends Comparable<? super T>> {

    @NotNull
    T getStart();

    @NotNull
    T getEndInclusive();

    boolean contains(@NotNull T var1);

    boolean isEmpty();

    @Metadata(mv = { 1, 6, 0 }, k = 3, xi = 48)
    public static final class DefaultImpls {

        public static <T extends Comparable<? super T>> boolean contains(@NotNull ClosedRange<T> var0, @NotNull T value) {
            Intrinsics.checkNotNullParameter(this, "this");
            Intrinsics.checkNotNullParameter(value, "value");
            return value.compareTo(this.getStart()) >= 0 && value.compareTo(this.getEndInclusive()) <= 0;
        }

        public static <T extends Comparable<? super T>> boolean isEmpty(@NotNull ClosedRange<T> var0) {
            Intrinsics.checkNotNullParameter(this, "this");
            return this.getStart().compareTo(this.getEndInclusive()) > 0;
        }
    }
}