package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bg\u0018\u00002\u00020\u0001J\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H¦\u0002¨\u0006\u0006" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/text/MatchNamedGroupCollection;", "Linfo/journeymap/shaded/kotlin/kotlin/text/MatchGroupCollection;", "get", "Linfo/journeymap/shaded/kotlin/kotlin/text/MatchGroup;", "name", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.1")
public interface MatchNamedGroupCollection extends MatchGroupCollection {

    @Nullable
    MatchGroup get(@NotNull String var1);
}