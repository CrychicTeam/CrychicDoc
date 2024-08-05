package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.collections.CollectionsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.sequences.SequencesKt;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002¢\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\u0082\b¢\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\n\u0010\u0013\u001a\u00020\u0002*\u00020\u0002\u001a\u0014\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002¨\u0006\u0015" }, d2 = { "getIndentFunction", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/text/StringsKt")
class StringsKt__IndentKt extends StringsKt__AppendableKt {

    @NotNull
    public static final String trimMargin(@NotNull String $this$trimMargin, @NotNull String marginPrefix) {
        Intrinsics.checkNotNullParameter($this$trimMargin, "<this>");
        Intrinsics.checkNotNullParameter(marginPrefix, "marginPrefix");
        return StringsKt.replaceIndentByMargin($this$trimMargin, "", marginPrefix);
    }

    @NotNull
    public static final String replaceIndentByMargin(@NotNull String $this$replaceIndentByMargin, @NotNull String newIndent, @NotNull String marginPrefix) {
        Intrinsics.checkNotNullParameter($this$replaceIndentByMargin, "<this>");
        Intrinsics.checkNotNullParameter(newIndent, "newIndent");
        Intrinsics.checkNotNullParameter(marginPrefix, "marginPrefix");
        boolean lines = !StringsKt.isBlank((CharSequence) marginPrefix);
        if (!lines) {
            ???;
            String var44 = "marginPrefix must be non-blank string.";
            throw new IllegalArgumentException(var44.toString());
        } else {
            List linesx = StringsKt.lines((CharSequence) $this$replaceIndentByMargin);
            int resultSizeEstimate$iv = $this$replaceIndentByMargin.length() + newIndent.length() * linesx.size();
            Function1 indentAddFunction$iv = getIndentFunction$StringsKt__IndentKt(newIndent);
            int $i$f$reindent = 0;
            int lastIndex$iv = CollectionsKt.getLastIndex(linesx);
            Iterable $this$mapIndexedNotNull$iv$iv = (Iterable) linesx;
            int $i$f$mapIndexedNotNull = 0;
            Collection destination$iv$iv$iv = (Collection) (new ArrayList());
            int $i$f$mapIndexedNotNullTo = 0;
            int $i$f$forEachIndexed = 0;
            int index$iv$iv$iv$iv = 0;
            for (Object item$iv$iv$iv$iv : $this$mapIndexedNotNull$iv$iv) {
                int var19 = index$iv$iv$iv$iv++;
                if (var19 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                ???;
                String value$iv = (String) item$iv$iv$iv$iv;
                ???;
                String var48;
                if ((var19 == 0 || var19 == lastIndex$iv) && StringsKt.isBlank((CharSequence) value$iv)) {
                    var48 = null;
                } else {
                    ???;
                    CharSequence $this$indexOfFirst$iv = (CharSequence) value$iv;
                    int $i$f$indexOfFirst = 0;
                    int var30 = 0;
                    int var31 = $this$indexOfFirst$iv.length();
                    while (true) {
                        if (var30 < var31) {
                            int index$iv = var30++;
                            char it = $this$indexOfFirst$iv.charAt(index$iv);
                            ???;
                            if (CharsKt.isWhitespace(it)) {
                                continue;
                            }
                            var10000 = index$iv;
                            break;
                        }
                        var10000 = -1;
                        break;
                    }
                    int firstNonWhitespaceIndex = var10000;
                    if (firstNonWhitespaceIndex == -1) {
                        var48 = null;
                    } else if (StringsKt.startsWith$default(value$iv, marginPrefix, firstNonWhitespaceIndex, false, 4, null)) {
                        $i$f$indexOfFirst = firstNonWhitespaceIndex + marginPrefix.length();
                        String var46 = value$iv.substring($i$f$indexOfFirst);
                        Intrinsics.checkNotNullExpressionValue(var46, "this as java.lang.String).substring(startIndex)");
                        var48 = var46;
                    } else {
                        var48 = null;
                    }
                    String var36 = var48;
                    var48 = var36 == null ? value$iv : (String) indentAddFunction$iv.invoke(var36);
                }
                String var38 = var48;
                if (var38 != null) {
                    ???;
                    destination$iv$iv$iv.add(var38);
                }
            }
            String var42 = ((StringBuilder) CollectionsKt.joinTo$default((Iterable) ((List) destination$iv$iv$iv), (Appendable) (new StringBuilder(resultSizeEstimate$iv)), (CharSequence) "\n", null, null, 0, null, null, 124, null)).toString();
            Intrinsics.checkNotNullExpressionValue(var42, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
            return var42;
        }
    }

    @NotNull
    public static final String trimIndent(@NotNull String $this$trimIndent) {
        Intrinsics.checkNotNullParameter($this$trimIndent, "<this>");
        return StringsKt.replaceIndent($this$trimIndent, "");
    }

    @NotNull
    public static final String replaceIndent(@NotNull String $this$replaceIndent, @NotNull String newIndent) {
        Intrinsics.checkNotNullParameter($this$replaceIndent, "<this>");
        Intrinsics.checkNotNullParameter(newIndent, "newIndent");
        List lines = StringsKt.lines((CharSequence) $this$replaceIndent);
        Iterable resultSizeEstimate$iv = (Iterable) lines;
        int $i$f$filter = 0;
        Collection destination$iv$iv = (Collection) (new ArrayList());
        int $i$f$filterTo = 0;
        for (Object element$iv$iv : resultSizeEstimate$iv) {
            String p0 = (String) element$iv$iv;
            ???;
            if (!StringsKt.isBlank((CharSequence) p0)) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        resultSizeEstimate$iv = (Iterable) ((List) destination$iv$iv);
        $i$f$filter = 0;
        destination$iv$iv = (Collection) (new ArrayList(CollectionsKt.collectionSizeOrDefault(resultSizeEstimate$iv, 10)));
        $i$f$filterTo = 0;
        for (Object item$iv$iv : resultSizeEstimate$iv) {
            String var48 = (String) item$iv$iv;
            ???;
            Integer var36 = indentWidth$StringsKt__IndentKt(var48);
            destination$iv$iv.add(var36);
        }
        Integer $this$reindent$iv = CollectionsKt.minOrNull((Iterable<? extends Integer>) ((List) destination$iv$iv));
        int minCommonIndent = $this$reindent$iv == null ? 0 : $this$reindent$iv;
        int var38 = $this$replaceIndent.length() + newIndent.length() * lines.size();
        Function1 indentAddFunction$iv = getIndentFunction$StringsKt__IndentKt(newIndent);
        int $i$f$reindent = 0;
        int lastIndex$iv = CollectionsKt.getLastIndex(lines);
        Iterable $this$mapIndexedNotNull$iv$iv = (Iterable) lines;
        int $i$f$mapIndexedNotNull = 0;
        Collection destination$iv$iv$iv = (Collection) (new ArrayList());
        int $i$f$mapIndexedNotNullTo = 0;
        int $i$f$forEachIndexed = 0;
        int index$iv$iv$iv$iv = 0;
        for (Object item$iv$iv$iv$iv : $this$mapIndexedNotNull$iv$iv) {
            int var19 = index$iv$iv$iv$iv++;
            if (var19 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            ???;
            String value$iv = (String) item$iv$iv$iv$iv;
            ???;
            String var10000;
            if ((var19 == 0 || var19 == lastIndex$iv) && StringsKt.isBlank((CharSequence) value$iv)) {
                var10000 = null;
            } else {
                ???;
                String var28 = StringsKt.drop(value$iv, minCommonIndent);
                var10000 = var28 == null ? value$iv : (String) indentAddFunction$iv.invoke(var28);
            }
            String var30 = var10000;
            if (var30 != null) {
                ???;
                destination$iv$iv$iv.add(var30);
            }
        }
        String var34 = ((StringBuilder) CollectionsKt.joinTo$default((Iterable) ((List) destination$iv$iv$iv), (Appendable) (new StringBuilder(var38)), (CharSequence) "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(var34, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return var34;
    }

    @NotNull
    public static final String prependIndent(@NotNull String $this$prependIndent, @NotNull final String indent) {
        Intrinsics.checkNotNullParameter($this$prependIndent, "<this>");
        Intrinsics.checkNotNullParameter(indent, "indent");
        return SequencesKt.joinToString$default(SequencesKt.map(StringsKt.lineSequence((CharSequence) $this$prependIndent), (Function1) (new Function1<String, String>() {

            @NotNull
            public final String invoke(@NotNull String it) {
                Intrinsics.checkNotNullParameter(it, "it");
                return StringsKt.isBlank((CharSequence) it) ? (it.length() < indent.length() ? indent : it) : Intrinsics.stringPlus(indent, it);
            }
        })), (CharSequence) "\n", null, null, 0, null, null, 62, null);
    }

    private static final int indentWidth$StringsKt__IndentKt(String $this$indentWidth) {
    }

    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(final String indent) {
        ???;
    }

    private static final String reindent$StringsKt__IndentKt(List<String> $this$reindent, int resultSizeEstimate, Function1<? super String, String> indentAddFunction, Function1<? super String, String> indentCutFunction) {
        int $i$f$reindent = 0;
        int lastIndex = CollectionsKt.getLastIndex($this$reindent);
        Iterable $this$mapIndexedNotNull$iv = (Iterable) $this$reindent;
        int $i$f$mapIndexedNotNull = 0;
        Collection destination$iv$iv = (Collection) (new ArrayList());
        int $i$f$mapIndexedNotNullTo = 0;
        int $i$f$forEachIndexed = 0;
        int index$iv$iv$iv = 0;
        for (Object item$iv$iv$iv : $this$mapIndexedNotNull$iv) {
            int var17 = index$iv$iv$iv++;
            if (var17 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            ???;
            String value = (String) item$iv$iv$iv;
            ???;
            String var10000;
            if ((var17 == 0 || var17 == lastIndex) && StringsKt.isBlank((CharSequence) value)) {
                var10000 = null;
            } else {
                String var24 = (String) indentCutFunction.invoke(value);
                var10000 = var24 == null ? value : (String) indentAddFunction.invoke(var24);
            }
            String var26 = var10000;
            if (var26 != null) {
                ???;
                destination$iv$iv.add(var26);
            }
        }
        String var6 = ((StringBuilder) CollectionsKt.joinTo$default((Iterable) ((List) destination$iv$iv), (Appendable) (new StringBuilder(resultSizeEstimate)), (CharSequence) "\n", null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkNotNullExpressionValue(var6, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return var6;
    }

    public StringsKt__IndentKt() {
    }
}