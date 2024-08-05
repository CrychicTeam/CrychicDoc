package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Deprecated;
import info.journeymap.shaded.kotlin.kotlin.DeprecationLevel;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.UInt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Iterator;

/**
 * @deprecated
 */
@Deprecated(message = "This class is not going to be stabilized and is to be removed soon.", level = DeprecationLevel.ERROR)
@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\b\u0007\b'\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0016\u0010\u0004\u001a\u00020\u0002H\u0086\u0002ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u0005\u0010\u0006J\u0015\u0010\u0007\u001a\u00020\u0002H&ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\b\u0010\u0006ø\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006\t" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/collections/UIntIterator;", "", "Linfo/journeymap/shaded/kotlin/kotlin/UInt;", "()V", "next", "next-pVg5ArA", "()I", "nextUInt", "nextUInt-pVg5ArA", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.3")
public abstract class UIntIterator implements Iterator<UInt>, KMappedMarker {

    public final int next_pVg5ArA() /* $VF was: next-pVg5ArA*/
    {
        return this.nextUInt - pVg5ArA();
    }

    public abstract int nextUInt_pVg5ArA();

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}