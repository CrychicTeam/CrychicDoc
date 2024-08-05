package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMappedMarker;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Iterator;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0010\n\n\u0002\b\u0005\b&\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0002H&¨\u0006\u0007" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/collections/ShortIterator;", "", "", "()V", "next", "()Ljava/lang/Short;", "nextShort", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public abstract class ShortIterator implements Iterator<Short>, KMappedMarker {

    @NotNull
    public final Short next() {
        return this.nextShort();
    }

    public abstract short nextShort();

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}