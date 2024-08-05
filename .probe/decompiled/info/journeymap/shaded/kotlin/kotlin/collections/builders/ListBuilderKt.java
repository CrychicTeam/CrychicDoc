package info.journeymap.shaded.kotlin.kotlin.collections.builders;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a!\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0000¢\u0006\u0002\u0010\u0005\u001a+\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0001\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\b\u001a\u00020\u0004H\u0000¢\u0006\u0002\u0010\t\u001a%\u0010\n\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\f\u001a\u00020\u0004H\u0000¢\u0006\u0002\u0010\r\u001a-\u0010\u000e\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0000¢\u0006\u0002\u0010\u0011\u001a9\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00042\n\u0010\u0016\u001a\u0006\u0012\u0002\b\u00030\u0017H\u0002¢\u0006\u0002\u0010\u0018\u001a-\u0010\u0019\u001a\u00020\u0004\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004H\u0002¢\u0006\u0002\u0010\u001a\u001a/\u0010\u001b\u001a\u00020\u001c\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004H\u0002¢\u0006\u0002\u0010\u001d¨\u0006\u001e" }, d2 = { "arrayOfUninitializedElements", "", "E", "size", "", "(I)[Ljava/lang/Object;", "copyOfUninitializedElements", "T", "newSize", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "resetAt", "", "index", "([Ljava/lang/Object;I)V", "resetRange", "fromIndex", "toIndex", "([Ljava/lang/Object;II)V", "subarrayContentEquals", "", "offset", "length", "other", "", "([Ljava/lang/Object;IILjava/util/List;)Z", "subarrayContentHashCode", "([Ljava/lang/Object;II)I", "subarrayContentToString", "", "([Ljava/lang/Object;II)Ljava/lang/String;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class ListBuilderKt {

    @NotNull
    public static final <E> E[] arrayOfUninitializedElements(int size) {
        boolean var1 = size >= 0;
        if (!var1) {
            ???;
            String var3 = "capacity must be non-negative.";
            throw new IllegalArgumentException(var3.toString());
        } else {
            return (E[]) (new Object[size]);
        }
    }

    private static final <T> String subarrayContentToString(T[] $this$subarrayContentToString, int offset, int length) {
        StringBuilder sb = new StringBuilder(2 + length * 3);
        sb.append("[");
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append($this$subarrayContentToString[offset + i]);
        }
        sb.append("]");
        String var5 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(var5, "sb.toString()");
        return var5;
    }

    private static final <T> int subarrayContentHashCode(T[] $this$subarrayContentHashCode, int offset, int length) {
        int result = 1;
        for (int i = 0; i < length; i++) {
            Object nextElement = $this$subarrayContentHashCode[offset + i];
            result = result * 31 + (nextElement == null ? 0 : nextElement.hashCode());
        }
        return result;
    }

    private static final <T> boolean subarrayContentEquals(T[] $this$subarrayContentEquals, int offset, int length, List<?> other) {
        if (length != other.size()) {
            return false;
        } else {
            for (int i = 0; i < length; i++) {
                if (!Intrinsics.areEqual($this$subarrayContentEquals[offset + i], other.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    @NotNull
    public static final <T> T[] copyOfUninitializedElements(@NotNull T[] $this$copyOfUninitializedElements, int newSize) {
        Intrinsics.checkNotNullParameter($this$copyOfUninitializedElements, "<this>");
        Object[] var3 = Arrays.copyOf($this$copyOfUninitializedElements, newSize);
        Intrinsics.checkNotNullExpressionValue(var3, "copyOf(this, newSize)");
        return (T[]) var3;
    }

    public static final <E> void resetAt(@NotNull E[] $this$resetAt, int index) {
        Intrinsics.checkNotNullParameter($this$resetAt, "<this>");
        $this$resetAt[index] = null;
    }

    public static final <E> void resetRange(@NotNull E[] $this$resetRange, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$resetRange, "<this>");
        int var3 = fromIndex;
        while (var3 < toIndex) {
            int index = var3++;
            resetAt($this$resetRange, index);
        }
    }
}