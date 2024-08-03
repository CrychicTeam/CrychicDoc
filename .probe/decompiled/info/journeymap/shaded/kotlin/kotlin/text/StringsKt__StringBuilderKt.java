package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Deprecated;
import info.journeymap.shaded.kotlin.kotlin.DeprecationLevel;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.ReplaceWith;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.Unit;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000F\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\u0010\f\n\u0002\u0010\u0019\n\u0002\u0010\r\n\u0000\u001a>\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u001b\u0010\u0004\u001a\u0017\u0012\b\u0012\u00060\u0006j\u0002`\u0007\u0012\u0004\u0012\u00020\b0\u0005¢\u0006\u0002\b\tH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001\u001a6\u0010\u0000\u001a\u00020\u00012\u001b\u0010\u0004\u001a\u0017\u0012\b\u0012\u00060\u0006j\u0002`\u0007\u0012\u0004\u0012\u00020\b0\u0005¢\u0006\u0002\b\tH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a\u001f\u0010\n\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0087\b\u001a/\u0010\n\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0016\u0010\r\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\f0\u000e\"\u0004\u0018\u00010\f¢\u0006\u0002\u0010\u000f\u001a/\u0010\n\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0016\u0010\r\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00010\u000e\"\u0004\u0018\u00010\u0001¢\u0006\u0002\u0010\u0010\u001a\u0015\u0010\u0011\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u0007H\u0087\b\u001a\u001f\u0010\u0011\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\r\u001a\u0004\u0018\u00010\fH\u0087\b\u001a\u001d\u0010\u0011\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\r\u001a\u00020\u0012H\u0087\b\u001a\u001d\u0010\u0011\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\r\u001a\u00020\u0013H\u0087\b\u001a\u001d\u0010\u0011\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\r\u001a\u00020\u0014H\u0087\b\u001a\u001f\u0010\u0011\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\r\u001a\u0004\u0018\u00010\u0015H\u0087\b\u001a\u001f\u0010\u0011\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\r\u001a\u0004\u0018\u00010\u0001H\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0016" }, d2 = { "buildString", "", "capacity", "", "builderAction", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "Ljava/lang/StringBuilder;", "Linfo/journeymap/shaded/kotlin/kotlin/text/StringBuilder;", "", "Linfo/journeymap/shaded/kotlin/kotlin/ExtensionFunctionType;", "append", "obj", "", "value", "", "(Ljava/lang/StringBuilder;[Ljava/lang/Object;)Ljava/lang/StringBuilder;", "(Ljava/lang/StringBuilder;[Ljava/lang/String;)Ljava/lang/StringBuilder;", "appendLine", "", "", "", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/text/StringsKt")
class StringsKt__StringBuilderKt extends StringsKt__StringBuilderJVMKt {

    /**
     * @deprecated
     */
    @Deprecated(message = "Use append(value: Any?) instead", replaceWith = @ReplaceWith(expression = "append(value = obj)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder append(StringBuilder $this$append, Object obj) {
        Intrinsics.checkNotNullParameter($this$append, "<this>");
        StringBuilder var2 = $this$append.append(obj);
        Intrinsics.checkNotNullExpressionValue(var2, "this.append(obj)");
        return var2;
    }

    @InlineOnly
    private static final String buildString(Function1<? super StringBuilder, Unit> builderAction) {
        Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        StringBuilder var2 = new StringBuilder();
        builderAction.invoke(var2);
        String var1 = var2.toString();
        Intrinsics.checkNotNullExpressionValue(var1, "StringBuilder().apply(builderAction).toString()");
        return var1;
    }

    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String buildString(int capacity, Function1<? super StringBuilder, Unit> builderAction) {
        Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        StringBuilder var3 = new StringBuilder(capacity);
        builderAction.invoke(var3);
        String var2 = var3.toString();
        Intrinsics.checkNotNullExpressionValue(var2, "StringBuilder(capacity).…builderAction).toString()");
        return var2;
    }

    @NotNull
    public static final StringBuilder append(@NotNull StringBuilder $this$append, @NotNull String... value) {
        Intrinsics.checkNotNullParameter($this$append, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        String[] var2 = value;
        int var3 = 0;
        int var4 = value.length;
        while (var3 < var4) {
            String item = var2[var3];
            var3++;
            $this$append.append(item);
        }
        return $this$append;
    }

    @NotNull
    public static final StringBuilder append(@NotNull StringBuilder $this$append, @NotNull Object... value) {
        Intrinsics.checkNotNullParameter($this$append, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        Object[] var2 = value;
        int var3 = 0;
        int var4 = value.length;
        while (var3 < var4) {
            Object item = var2[var3];
            var3++;
            $this$append.append(item);
        }
        return $this$append;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var1 = $this$appendLine.append('\n');
        Intrinsics.checkNotNullExpressionValue(var1, "append('\\n')");
        return var1;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, CharSequence value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, String value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, Object value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, char[] value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, char value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, boolean value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    public StringsKt__StringBuilderKt() {
    }
}