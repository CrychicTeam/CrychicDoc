package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import java.util.regex.Pattern;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000\f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b¨\u0006\u0003" }, d2 = { "toRegex", "Linfo/journeymap/shaded/kotlin/kotlin/text/Regex;", "Ljava/util/regex/Pattern;", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/text/StringsKt")
class StringsKt__RegexExtensionsJVMKt extends StringsKt__IndentKt {

    @InlineOnly
    private static final Regex toRegex(Pattern $this$toRegex) {
        Intrinsics.checkNotNullParameter($this$toRegex, "<this>");
        return new Regex($this$toRegex);
    }

    public StringsKt__RegexExtensionsJVMKt() {
    }
}