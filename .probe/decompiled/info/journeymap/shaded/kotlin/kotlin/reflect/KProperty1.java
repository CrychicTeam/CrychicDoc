package info.journeymap.shaded.kotlin.kotlin.reflect;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0002\bf\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0006\b\u0001\u0010\u0002 \u00012\b\u0012\u0004\u0012\u0002H\u00020\u00032\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0004:\u0001\u000eJ\u0015\u0010\t\u001a\u00028\u00012\u0006\u0010\n\u001a\u00028\u0000H&¢\u0006\u0002\u0010\u000bJ\u0017\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\n\u001a\u00028\u0000H'¢\u0006\u0002\u0010\u000bR\u001e\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0006X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u000f" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty1;", "T", "V", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty;", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "getter", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty1$Getter;", "getGetter", "()Lkotlin/reflect/KProperty1$Getter;", "get", "receiver", "(Ljava/lang/Object;)Ljava/lang/Object;", "getDelegate", "", "Getter", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public interface KProperty1<T, V> extends KProperty<V>, Function1<T, V> {

    V get(T var1);

    @SinceKotlin(version = "1.1")
    @Nullable
    Object getDelegate(T var1);

    @NotNull
    KProperty1.Getter<T, V> getGetter();

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0006\b\u0003\u0010\u0002 \u00012\b\u0012\u0004\u0012\u0002H\u00020\u00032\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0004¨\u0006\u0005" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty1$Getter;", "T", "V", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty$Getter;", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public interface Getter<T, V> extends KProperty.Getter<V>, Function1<T, V> {
    }
}