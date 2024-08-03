package info.journeymap.shaded.kotlin.kotlin.reflect;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0002\u000e\u000fR\u0018\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\u0007\u001a\u00020\b8&X§\u0004¢\u0006\f\u0012\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\u000bR\u001a\u0010\f\u001a\u00020\b8&X§\u0004¢\u0006\f\u0012\u0004\b\r\u0010\n\u001a\u0004\b\f\u0010\u000b¨\u0006\u0010" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty;", "V", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KCallable;", "getter", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty$Getter;", "getGetter", "()Lkotlin/reflect/KProperty$Getter;", "isConst", "", "isConst$annotations", "()V", "()Z", "isLateinit", "isLateinit$annotations", "Accessor", "Getter", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public interface KProperty<V> extends KCallable<V> {

    boolean isLateinit();

    boolean isConst();

    @NotNull
    KProperty.Getter<V> getGetter();

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u00020\u0002R\u0018\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty$Accessor;", "V", "", "property", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty;", "getProperty", "()Lkotlin/reflect/KProperty;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public interface Accessor<V> {

        @NotNull
        KProperty<V> getProperty();
    }

    @Metadata(mv = { 1, 6, 0 }, k = 3, xi = 48)
    public static final class DefaultImpls {
    }

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003¨\u0006\u0004" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty$Getter;", "V", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty$Accessor;", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KFunction;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public interface Getter<V> extends KProperty.Accessor<V>, KFunction<V> {
    }
}