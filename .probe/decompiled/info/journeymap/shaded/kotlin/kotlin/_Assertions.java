package info.journeymap.shaded.kotlin.kotlin;

import info.journeymap.shaded.kotlin.kotlin.jvm.JvmField;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\bÁ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\u00020\u00048\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0002¨\u0006\u0006" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/_Assertions;", "", "()V", "ENABLED", "", "getENABLED$annotations", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@PublishedApi
public final class _Assertions {

    @NotNull
    public static final _Assertions INSTANCE = new _Assertions();

    @JvmField
    public static final boolean ENABLED = INSTANCE.getClass().desiredAssertionStatus();

    private _Assertions() {
    }
}