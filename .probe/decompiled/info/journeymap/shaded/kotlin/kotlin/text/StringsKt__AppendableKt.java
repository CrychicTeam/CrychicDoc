package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.ExperimentalStdlibApi;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.WasExperimental;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000:\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\u001a5\u0010\u0000\u001a\u0002H\u0001\"\f\b\u0000\u0010\u0001*\u00060\u0002j\u0002`\u0003*\u0002H\u00012\u0016\u0010\u0004\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00060\u0005\"\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007\u001a9\u0010\b\u001a\u00020\t\"\u0004\b\u0000\u0010\u0001*\u00060\u0002j\u0002`\u00032\u0006\u0010\n\u001a\u0002H\u00012\u0014\u0010\u000b\u001a\u0010\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u00020\u0006\u0018\u00010\fH\u0000¢\u0006\u0002\u0010\r\u001a\u0015\u0010\u000e\u001a\u00060\u0002j\u0002`\u0003*\u00060\u0002j\u0002`\u0003H\u0087\b\u001a\u001d\u0010\u000e\u001a\u00060\u0002j\u0002`\u0003*\u00060\u0002j\u0002`\u00032\u0006\u0010\u0004\u001a\u00020\u000fH\u0087\b\u001a\u001f\u0010\u000e\u001a\u00060\u0002j\u0002`\u0003*\u00060\u0002j\u0002`\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0006H\u0087\b\u001a7\u0010\u0010\u001a\u0002H\u0001\"\f\b\u0000\u0010\u0001*\u00060\u0002j\u0002`\u0003*\u0002H\u00012\u0006\u0010\u0004\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0007¢\u0006\u0002\u0010\u0014¨\u0006\u0015" }, d2 = { "append", "T", "Ljava/lang/Appendable;", "Linfo/journeymap/shaded/kotlin/kotlin/text/Appendable;", "value", "", "", "(Ljava/lang/Appendable;[Ljava/lang/CharSequence;)Ljava/lang/Appendable;", "appendElement", "", "element", "transform", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "(Ljava/lang/Appendable;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "appendLine", "", "appendRange", "startIndex", "", "endIndex", "(Ljava/lang/Appendable;Ljava/lang/CharSequence;II)Ljava/lang/Appendable;", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/text/StringsKt")
class StringsKt__AppendableKt {

    @SinceKotlin(version = "1.4")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @NotNull
    public static final <T extends Appendable> T appendRange(@NotNull T $this$appendRange, @NotNull CharSequence value, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$appendRange, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        Appendable var4 = $this$appendRange.append(value, startIndex, endIndex);
        if (var4 == null) {
            throw new NullPointerException("null cannot be cast to non-null type T of kotlin.text.StringsKt__AppendableKt.appendRange");
        } else {
            return (T) var4;
        }
    }

    @NotNull
    public static final <T extends Appendable> T append(@NotNull T $this$append, @NotNull CharSequence... value) {
        Intrinsics.checkNotNullParameter($this$append, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        CharSequence[] var2 = value;
        int var3 = 0;
        int var4 = value.length;
        while (var3 < var4) {
            CharSequence item = var2[var3];
            var3++;
            $this$append.append(item);
        }
        return (T) $this$append;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final Appendable appendLine(Appendable $this$appendLine) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        Appendable var1 = $this$appendLine.append('\n');
        Intrinsics.checkNotNullExpressionValue(var1, "append('\\n')");
        return var1;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final Appendable appendLine(Appendable $this$appendLine, CharSequence value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        Appendable var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        Appendable var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final Appendable appendLine(Appendable $this$appendLine, char value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        Appendable var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        Appendable var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    public static final <T> void appendElement(@NotNull Appendable $this$appendElement, T element, @Nullable Function1<? super T, ? extends CharSequence> transform) {
        Intrinsics.checkNotNullParameter($this$appendElement, "<this>");
        if (transform != null) {
            $this$appendElement.append((CharSequence) transform.invoke(element));
        } else if (element == null ? true : element instanceof CharSequence) {
            $this$appendElement.append((CharSequence) element);
        } else if (element instanceof Character) {
            $this$appendElement.append((Character) element);
        } else {
            $this$appendElement.append((CharSequence) String.valueOf(element));
        }
    }

    public StringsKt__AppendableKt() {
    }
}