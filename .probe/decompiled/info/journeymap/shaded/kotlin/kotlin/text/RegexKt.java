package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.ranges.IntRange;
import info.journeymap.shaded.kotlin.kotlin.ranges.RangesKt;
import java.util.regex.Matcher;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000>\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0082\b\u001a\u001e\u0010\u0007\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\u0016\u0010\r\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\f\u0010\u000e\u001a\u00020\u000f*\u00020\u0010H\u0002\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0002\u001a\u0012\u0010\u0012\u001a\u00020\u0006*\b\u0012\u0004\u0012\u00020\u00030\u0013H\u0002¨\u0006\u0014" }, d2 = { "fromInt", "", "T", "Linfo/journeymap/shaded/kotlin/kotlin/text/FlagEnum;", "", "value", "", "findNext", "Linfo/journeymap/shaded/kotlin/kotlin/text/MatchResult;", "Ljava/util/regex/Matcher;", "from", "input", "", "matchEntire", "range", "Linfo/journeymap/shaded/kotlin/kotlin/ranges/IntRange;", "Ljava/util/regex/MatchResult;", "groupIndex", "toInt", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class RegexKt {

    private static final int toInt(Iterable<? extends FlagEnum> $this$toInt) {
        int initial$iv = 0;
        int $i$f$fold = 0;
        int accumulator$iv = initial$iv;
        for (Object element$iv : $this$toInt) {
            FlagEnum option = (FlagEnum) element$iv;
            ???;
            accumulator$iv |= option.getValue();
        }
        return accumulator$iv;
    }

    private static final MatchResult findNext(Matcher $this$findNext, int from, CharSequence input) {
        return !$this$findNext.find(from) ? null : new MatcherMatchResult($this$findNext, input);
    }

    private static final MatchResult matchEntire(Matcher $this$matchEntire, CharSequence input) {
        return !$this$matchEntire.matches() ? null : new MatcherMatchResult($this$matchEntire, input);
    }

    private static final IntRange range(java.util.regex.MatchResult $this$range) {
        return RangesKt.until($this$range.start(), $this$range.end());
    }

    private static final IntRange range(java.util.regex.MatchResult $this$range, int groupIndex) {
        return RangesKt.until($this$range.start(groupIndex), $this$range.end(groupIndex));
    }
}