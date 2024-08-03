package mezz.jei.core.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class WeakList<T> {

    private final List<WeakReference<T>> list = new ArrayList();

    public void add(T item) {
        this.list.add(new WeakReference(item));
    }

    public void forEach(Consumer<T> consumer) {
        Iterator<WeakReference<T>> iterator = this.list.iterator();
        while (iterator.hasNext()) {
            WeakReference<T> reference = (WeakReference<T>) iterator.next();
            T item = (T) reference.get();
            if (item == null) {
                iterator.remove();
            } else {
                consumer.accept(item);
            }
        }
    }

    public boolean isEmpty() {
        Iterator<WeakReference<T>> iterator = this.list.iterator();
        while (iterator.hasNext()) {
            WeakReference<T> reference = (WeakReference<T>) iterator.next();
            T item = (T) reference.get();
            if (item != null) {
                return false;
            }
            iterator.remove();
        }
        return true;
    }
}