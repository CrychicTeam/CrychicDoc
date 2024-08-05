package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmField;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/text/SystemProperties;", "", "()V", "LINE_SEPARATOR", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
final class SystemProperties {

    @NotNull
    public static final SystemProperties INSTANCE = new SystemProperties();

    @JvmField
    @NotNull
    public static final String LINE_SEPARATOR;

    private SystemProperties() {
    }

    static {
        String var10000 = System.getProperty("line.separator");
        Intrinsics.checkNotNull(var10000);
        LINE_SEPARATOR = var10000;
    }
}