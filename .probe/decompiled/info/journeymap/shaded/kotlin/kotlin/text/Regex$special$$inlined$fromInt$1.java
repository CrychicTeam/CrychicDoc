package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Lambda;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 3, xi = 48, d1 = { "\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\u0010\u0000\u001a\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u000e\u0010\u0005\u001a\n \u0006*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0007\u0010\b¨\u0006\t" }, d2 = { "<anonymous>", "", "T", "Linfo/journeymap/shaded/kotlin/kotlin/text/FlagEnum;", "", "it", "info.journeymap.shaded.kotlin.kotlin.jvm.PlatformType", "invoke", "(Ljava/lang/Enum;)Ljava/lang/Boolean;", "info/journeymap/shaded/kotlin/kotlin/text/RegexKt$fromInt$1$1" })
public final class Regex$special$$inlined$fromInt$1 extends Lambda implements Function1<RegexOption, Boolean> {

    public Regex$special$$inlined$fromInt$1(int $value) {
        super(1);
        this.$value = $value;
    }

    @NotNull
    public final Boolean invoke(RegexOption it) {
        return (this.$value & ((FlagEnum) it).getMask()) == ((FlagEnum) it).getValue();
    }
}