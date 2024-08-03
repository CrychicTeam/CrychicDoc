package info.journeymap.shaded.kotlin.kotlin.reflect;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.Unit;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0001\u0007R\u0018\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\b" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KMutableProperty;", "V", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty;", "setter", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KMutableProperty$Setter;", "getSetter", "()Lkotlin/reflect/KMutableProperty$Setter;", "Setter", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public interface KMutableProperty<V> extends KProperty<V> {

    @NotNull
    KMutableProperty.Setter<V> getSetter();

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\bf\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u00020\u00040\u0003¨\u0006\u0005" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KMutableProperty$Setter;", "V", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KProperty$Accessor;", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KFunction;", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public interface Setter<V> extends KProperty.Accessor<V>, KFunction<Unit> {
    }
}