package info.journeymap.shaded.kotlin.kotlin.collections.builders;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.collections.AbstractMutableSet;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Map.Entry;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010&\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b \u0018\u0000*\u0014\b\u0000\u0010\u0001*\u000e\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u0002H\u00040\u0002*\u0004\b\u0001\u0010\u0003*\u0004\b\u0002\u0010\u00042\b\u0012\u0004\u0012\u0002H\u00010\u0005B\u0005¢\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00028\u0000H\u0086\u0002¢\u0006\u0002\u0010\nJ\u001c\u0010\u000b\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\u0002H&¨\u0006\f" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/collections/builders/AbstractMapBuilderEntrySet;", "E", "", "K", "V", "Linfo/journeymap/shaded/kotlin/kotlin/collections/AbstractMutableSet;", "()V", "contains", "", "element", "(Ljava/util/Map$Entry;)Z", "containsEntry", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public abstract class AbstractMapBuilderEntrySet<E extends Entry<? extends K, ? extends V>, K, V> extends AbstractMutableSet<E> {

    public final boolean contains(@NotNull E element) {
        Intrinsics.checkNotNullParameter(element, "element");
        return this.containsEntry(element);
    }

    public abstract boolean containsEntry(@NotNull Entry<? extends K, ? extends V> var1);
}