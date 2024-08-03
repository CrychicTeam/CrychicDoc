package info.journeymap.shaded.kotlin.kotlin.reflect;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function0;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0002\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0001\fJ\r\u0010\b\u001a\u00028\u0000H&¢\u0006\u0002\u0010\tJ\n\u0010\n\u001a\u0004\u0018\u00010\u000bH'R\u0018\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\r" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty0;", "V", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty;", "Linfo/journeymap/shaded/kotlin/kotlin/Function0;", "getter", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty0$Getter;", "getGetter", "()Lkotlin/reflect/KProperty0$Getter;", "get", "()Ljava/lang/Object;", "getDelegate", "", "Getter", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public interface KProperty0<V> extends KProperty<V>, Function0<V> {

    V get();

    @SinceKotlin(version = "1.1")
    @Nullable
    Object getDelegate();

    @NotNull
    KProperty0.Getter<V> getGetter();

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003¨\u0006\u0004" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty0$Getter;", "V", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty$Getter;", "Linfo/journeymap/shaded/kotlin/kotlin/Function0;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public interface Getter<V> extends KProperty.Getter<V>, Function0<V> {
    }
}