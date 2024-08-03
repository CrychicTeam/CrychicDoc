package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Deprecated;
import info.journeymap.shaded.kotlin.kotlin.DeprecationLevel;
import info.journeymap.shaded.kotlin.kotlin.ExperimentalStdlibApi;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.ReplaceWith;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.WasExperimental;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000\\\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\u001a\u001f\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0006H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\tH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a%\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u000e\u0010\u0003\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\u0002H\u0087\b\u001a-\u0010\u000b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a-\u0010\u000b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a\u0014\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u0012H\u0007\u001a\u001d\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u00122\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u00122\b\u0010\u0003\u001a\u0004\u0018\u00010\u000fH\u0087\b\u001a\u0014\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002H\u0007\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0014H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0015H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\fH\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u000fH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0006H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\tH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0016H\u0087\b\u001a%\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u000e\u0010\u0003\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\u0002H\u0087\b\u001a\u0014\u0010\u0017\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002H\u0007\u001a\u001d\u0010\u0018\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\bH\u0087\b\u001a%\u0010\u001a\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a5\u0010\u001b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a5\u0010\u001b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a!\u0010\u001c\u001a\u00020\u001d*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\n\u001a-\u0010\u001e\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0016H\u0087\b\u001a7\u0010\u001f\u001a\u00020\u001d*\u00060\u0001j\u0002`\u00022\u0006\u0010 \u001a\u00020\f2\b\b\u0002\u0010!\u001a\u00020\b2\b\b\u0002\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\bH\u0087\b¨\u0006\"" }, d2 = { "appendLine", "Ljava/lang/StringBuilder;", "Linfo/journeymap/shaded/kotlin/kotlin/text/StringBuilder;", "value", "Ljava/lang/StringBuffer;", "", "", "", "", "", "", "appendRange", "", "startIndex", "endIndex", "", "appendln", "Ljava/lang/Appendable;", "Linfo/journeymap/shaded/kotlin/kotlin/text/Appendable;", "", "", "", "", "clear", "deleteAt", "index", "deleteRange", "insertRange", "set", "", "setRange", "toCharArray", "destination", "destinationOffset", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/text/StringsKt")
class StringsKt__StringBuilderJVMKt extends StringsKt__RegexExtensionsKt {

    @SinceKotlin(version = "1.3")
    @NotNull
    public static final StringBuilder clear(@NotNull StringBuilder $this$clear) {
        Intrinsics.checkNotNullParameter($this$clear, "<this>");
        ???;
        $this$clear.setLength(0);
        return $this$clear;
    }

    @InlineOnly
    private static final void set(StringBuilder $this$set, int index, char value) {
        Intrinsics.checkNotNullParameter($this$set, "<this>");
        $this$set.setCharAt(index, value);
    }

    @SinceKotlin(version = "1.4")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @InlineOnly
    private static final StringBuilder setRange(StringBuilder $this$setRange, int startIndex, int endIndex, String value) {
        Intrinsics.checkNotNullParameter($this$setRange, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        StringBuilder var4 = $this$setRange.replace(startIndex, endIndex, value);
        Intrinsics.checkNotNullExpressionValue(var4, "this.replace(startIndex, endIndex, value)");
        return var4;
    }

    @SinceKotlin(version = "1.4")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @InlineOnly
    private static final StringBuilder deleteAt(StringBuilder $this$deleteAt, int index) {
        Intrinsics.checkNotNullParameter($this$deleteAt, "<this>");
        StringBuilder var2 = $this$deleteAt.deleteCharAt(index);
        Intrinsics.checkNotNullExpressionValue(var2, "this.deleteCharAt(index)");
        return var2;
    }

    @SinceKotlin(version = "1.4")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @InlineOnly
    private static final StringBuilder deleteRange(StringBuilder $this$deleteRange, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$deleteRange, "<this>");
        StringBuilder var3 = $this$deleteRange.delete(startIndex, endIndex);
        Intrinsics.checkNotNullExpressionValue(var3, "this.delete(startIndex, endIndex)");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @InlineOnly
    private static final void toCharArray(StringBuilder $this$toCharArray, char[] destination, int destinationOffset, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$toCharArray, "<this>");
        Intrinsics.checkNotNullParameter(destination, "destination");
        $this$toCharArray.getChars(startIndex, endIndex, destination, destinationOffset);
    }

    @SinceKotlin(version = "1.4")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @InlineOnly
    private static final StringBuilder appendRange(StringBuilder $this$appendRange, char[] value, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$appendRange, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        StringBuilder var4 = $this$appendRange.append(value, startIndex, endIndex - startIndex);
        Intrinsics.checkNotNullExpressionValue(var4, "this.append(value, start…x, endIndex - startIndex)");
        return var4;
    }

    @SinceKotlin(version = "1.4")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @InlineOnly
    private static final StringBuilder appendRange(StringBuilder $this$appendRange, CharSequence value, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$appendRange, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        StringBuilder var4 = $this$appendRange.append(value, startIndex, endIndex);
        Intrinsics.checkNotNullExpressionValue(var4, "this.append(value, startIndex, endIndex)");
        return var4;
    }

    @SinceKotlin(version = "1.4")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @InlineOnly
    private static final StringBuilder insertRange(StringBuilder $this$insertRange, int index, char[] value, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$insertRange, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        StringBuilder var5 = $this$insertRange.insert(index, value, startIndex, endIndex - startIndex);
        Intrinsics.checkNotNullExpressionValue(var5, "this.insert(index, value…x, endIndex - startIndex)");
        return var5;
    }

    @SinceKotlin(version = "1.4")
    @WasExperimental(markerClass = { ExperimentalStdlibApi.class })
    @InlineOnly
    private static final StringBuilder insertRange(StringBuilder $this$insertRange, int index, CharSequence value, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$insertRange, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        StringBuilder var5 = $this$insertRange.insert(index, value, startIndex, endIndex);
        Intrinsics.checkNotNullExpressionValue(var5, "this.insert(index, value, startIndex, endIndex)");
        return var5;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, StringBuffer value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, StringBuilder value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append((CharSequence) value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, int value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, short value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value.toInt())");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, byte value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value.toInt())");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, long value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var3 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var3, "append(value)");
        StringBuilder var4 = var3.append('\n');
        Intrinsics.checkNotNullExpressionValue(var4, "append('\\n')");
        return var4;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, float value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var2 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        StringBuilder var3 = var2.append('\n');
        Intrinsics.checkNotNullExpressionValue(var3, "append('\\n')");
        return var3;
    }

    @SinceKotlin(version = "1.4")
    @InlineOnly
    private static final StringBuilder appendLine(StringBuilder $this$appendLine, double value) {
        Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        StringBuilder var3 = $this$appendLine.append(value);
        Intrinsics.checkNotNullExpressionValue(var3, "append(value)");
        StringBuilder var4 = var3.append('\n');
        Intrinsics.checkNotNullExpressionValue(var4, "append('\\n')");
        return var4;
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine()", imports = {}), level = DeprecationLevel.WARNING)
    @NotNull
    public static final Appendable appendln(@NotNull Appendable $this$appendln) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        Appendable var1 = $this$appendln.append((CharSequence) SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkNotNullExpressionValue(var1, "append(SystemProperties.LINE_SEPARATOR)");
        return var1;
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final Appendable appendln(Appendable $this$appendln, CharSequence value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        Appendable var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final Appendable appendln(Appendable $this$appendln, char value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        Appendable var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine()", imports = {}), level = DeprecationLevel.WARNING)
    @NotNull
    public static final StringBuilder appendln(@NotNull StringBuilder $this$appendln) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var1 = $this$appendln.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkNotNullExpressionValue(var1, "append(SystemProperties.LINE_SEPARATOR)");
        return var1;
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, StringBuffer value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, CharSequence value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, String value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, Object value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, StringBuilder value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append((CharSequence) value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, char[] value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        Intrinsics.checkNotNullParameter(value, "value");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, char value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, boolean value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, int value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, short value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value.toInt())");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, byte value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value.toInt())");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, long value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var3 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var3, "append(value)");
        return StringsKt.appendln(var3);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, float value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var2 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var2, "append(value)");
        return StringsKt.appendln(var2);
    }

    @Deprecated(message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @ReplaceWith(expression = "appendLine(value)", imports = {}), level = DeprecationLevel.WARNING)
    @InlineOnly
    private static final StringBuilder appendln(StringBuilder $this$appendln, double value) {
        Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        StringBuilder var3 = $this$appendln.append(value);
        Intrinsics.checkNotNullExpressionValue(var3, "append(value)");
        return StringsKt.appendln(var3);
    }

    public StringsKt__StringBuilderJVMKt() {
    }
}