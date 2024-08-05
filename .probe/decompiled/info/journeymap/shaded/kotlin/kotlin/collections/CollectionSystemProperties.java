package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmField;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/collections/CollectionSystemProperties;", "", "()V", "brittleContainsOptimizationEnabled", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class CollectionSystemProperties {

    @NotNull
    public static final CollectionSystemProperties INSTANCE = new CollectionSystemProperties();

    @JvmField
    public static final boolean brittleContainsOptimizationEnabled;

    private CollectionSystemProperties() {
    }

    static {
        String var0 = System.getProperty("info.journeymap.shaded.kotlin.kotlin.collections.convert_arg_to_set_in_removeAll");
        brittleContainsOptimizationEnabled = var0 == null ? false : Boolean.parseBoolean(var0);
    }
}