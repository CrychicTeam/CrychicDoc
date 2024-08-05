package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.ArrayIteratorsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMappedMarker;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Iterator;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0011\n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u000f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0003H\u0096\u0002¨\u0006\u0004¸\u0006\u0000" }, d2 = { "info/journeymap/shaded/kotlin/kotlin/collections/CollectionsKt__IterablesKt$Iterable$1", "", "iterator", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class ArraysKt___ArraysKt$asIterable$$inlined$Iterable$2 implements Iterable<Byte>, KMappedMarker {

    public ArraysKt___ArraysKt$asIterable$$inlined$Iterable$2(byte[] var1) {
        this.$this_asIterable$inlined = var1;
    }

    @NotNull
    public Iterator<Byte> iterator() {
        ???;
        return ArrayIteratorsKt.iterator(this.$this_asIterable$inlined);
    }
}