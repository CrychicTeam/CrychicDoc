package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.PublishedApi;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmName;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.NoSuchElementException;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000\u001e\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0002\u001a3\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0001*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u0001¢\u0006\u0004\b\u0005\u0010\u0006\u001aQ\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0001*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\u00032!\u0010\b\u001a\u001d\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u0004\u0012\u0004\u0012\u0002H\u00010\t\u001aX\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0001*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\f2!\u0010\b\u001a\u001d\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u0004\u0012\u0004\u0012\u0002H\u00010\tH\u0007¢\u0006\u0002\b\r¨\u0006\u000e" }, d2 = { "getOrImplicitDefault", "V", "K", "", "key", "getOrImplicitDefaultNullable", "(Ljava/util/Map;Ljava/lang/Object;)Ljava/lang/Object;", "withDefault", "defaultValue", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "Linfo/journeymap/shaded/kotlin/kotlin/ParameterName;", "name", "", "withDefaultMutable", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/collections/MapsKt")
class MapsKt__MapWithDefaultKt {

    @JvmName(name = "getOrImplicitDefaultNullable")
    @PublishedApi
    public static final <K, V> V getOrImplicitDefaultNullable(@NotNull Map<K, ? extends V> $this$getOrImplicitDefault, K key) {
        Intrinsics.checkNotNullParameter($this$getOrImplicitDefault, "<this>");
        if ($this$getOrImplicitDefault instanceof MapWithDefault) {
            return (V) ((MapWithDefault) $this$getOrImplicitDefault).getOrImplicitDefault(key);
        } else {
            int $i$f$getOrElseNullable = 0;
            Object value$iv = $this$getOrImplicitDefault.get(key);
            if (value$iv == null && !$this$getOrImplicitDefault.containsKey(key)) {
                ???;
                throw new NoSuchElementException("Key " + key + " is missing in the map.");
            } else {
                return (V) value$iv;
            }
        }
    }

    @NotNull
    public static final <K, V> Map<K, V> withDefault(@NotNull Map<K, ? extends V> $this$withDefault, @NotNull Function1<? super K, ? extends V> defaultValue) {
        Intrinsics.checkNotNullParameter($this$withDefault, "<this>");
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return (Map<K, V>) ($this$withDefault instanceof MapWithDefault ? MapsKt.withDefault(((MapWithDefault) $this$withDefault).getMap(), defaultValue) : new MapWithDefaultImpl<>($this$withDefault, defaultValue));
    }

    @JvmName(name = "withDefaultMutable")
    @NotNull
    public static final <K, V> Map<K, V> withDefaultMutable(@NotNull Map<K, V> $this$withDefault, @NotNull Function1<? super K, ? extends V> defaultValue) {
        Intrinsics.checkNotNullParameter($this$withDefault, "<this>");
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return (Map<K, V>) ($this$withDefault instanceof MutableMapWithDefault ? MapsKt.withDefaultMutable(((MutableMapWithDefault) $this$withDefault).getMap(), defaultValue) : new MutableMapWithDefaultImpl<>($this$withDefault, defaultValue));
    }

    public MapsKt__MapWithDefaultKt() {
    }
}