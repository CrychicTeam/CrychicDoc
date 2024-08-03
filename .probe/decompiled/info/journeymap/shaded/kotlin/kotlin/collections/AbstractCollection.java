package info.journeymap.shaded.kotlin.kotlin.collections;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.CollectionToArray;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMappedMarker;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Iterator;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010(\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b'\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0007\b\u0004¢\u0006\u0002\u0010\u0003J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\t2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002H\u0016J\b\u0010\u000e\u001a\u00020\tH\u0016J\u000f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u0010H¦\u0002J\u0015\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u0012H\u0015¢\u0006\u0002\u0010\u0014J'\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00150\u0012\"\u0004\b\u0001\u0010\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00150\u0012H\u0014¢\u0006\u0002\u0010\u0017J\b\u0010\u0018\u001a\u00020\u0019H\u0016R\u0012\u0010\u0004\u001a\u00020\u0005X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u001a" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/collections/AbstractCollection;", "E", "", "()V", "size", "", "getSize", "()I", "contains", "", "element", "(Ljava/lang/Object;)Z", "containsAll", "elements", "isEmpty", "iterator", "", "toArray", "", "", "()[Ljava/lang/Object;", "T", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toString", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.1")
public abstract class AbstractCollection<E> implements Collection<E>, KMappedMarker {

    protected AbstractCollection() {
    }

    public abstract int getSize();

    @NotNull
    public abstract Iterator<E> iterator();

    public boolean contains(E element) {
        Iterable $this$any$iv = (Iterable) this;
        int $i$f$any = 0;
        boolean var10000;
        if ($this$any$iv instanceof Collection && ((Collection) $this$any$iv).isEmpty()) {
            var10000 = false;
        } else {
            Iterator var4 = $this$any$iv.iterator();
            while (true) {
                if (!var4.hasNext()) {
                    var10000 = false;
                    break;
                }
                Object element$iv = var4.next();
                ???;
                if (Intrinsics.areEqual(element$iv, element)) {
                    var10000 = true;
                    break;
                }
            }
        }
        return var10000;
    }

    public boolean containsAll(@NotNull Collection<? extends Object> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        Iterable $this$all$iv = (Iterable) elements;
        int $i$f$all = 0;
        boolean var10000;
        if (((Collection) $this$all$iv).isEmpty()) {
            var10000 = true;
        } else {
            Iterator var4 = $this$all$iv.iterator();
            while (true) {
                if (!var4.hasNext()) {
                    var10000 = true;
                    break;
                }
                Object element$iv = var4.next();
                ???;
                if (!this.contains((E) element$iv)) {
                    var10000 = false;
                    break;
                }
            }
        }
        return var10000;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    @NotNull
    public String toString() {
        return CollectionsKt.joinToString$default((Iterable) this, (CharSequence) ", ", (CharSequence) "[", (CharSequence) "]", 0, null, (Function1) (new Function1<E, CharSequence>() {

            @NotNull
            public final CharSequence invoke(E it) {
                return it == AbstractCollection.this ? (CharSequence) "(this Collection)" : (CharSequence) String.valueOf(it);
            }
        }), 24, null);
    }

    @NotNull
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    @NotNull
    public <T> T[] toArray(@NotNull T[] array) {
        Intrinsics.checkNotNullParameter(array, "array");
        return (T[]) CollectionToArray.toArray(this, array);
    }

    public boolean add(E element) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean addAll(Collection<? extends E> elements) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean remove(Object element) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean removeAll(Collection<? extends Object> elements) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean retainAll(Collection<? extends Object> elements) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}