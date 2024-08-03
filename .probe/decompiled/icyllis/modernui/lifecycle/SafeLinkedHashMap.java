package icyllis.modernui.lifecycle;

import java.util.HashMap;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SafeLinkedHashMap<T, E extends Supplier<T>> extends SafeLinkedList<T, E> {

    private final HashMap<T, SafeLinkedList.Node<E>> mHashMap = new HashMap();

    @Override
    protected SafeLinkedList.Node<E> find(T token) {
        return (SafeLinkedList.Node<E>) this.mHashMap.get(token);
    }

    @Override
    public E putIfAbsent(@Nonnull E e) {
        SafeLinkedList.Node<E> node = this.find((T) e.get());
        if (node != null) {
            return node.mElement;
        } else {
            this.mHashMap.put(e.get(), this.put(e));
            return null;
        }
    }

    @Override
    public E remove(@Nonnull T token) {
        E removed = super.remove(token);
        this.mHashMap.remove(token);
        return removed;
    }

    public boolean contains(@Nonnull T token) {
        return this.mHashMap.containsKey(token);
    }

    @Nullable
    public E ceil(T token) {
        if (this.contains(token)) {
            SafeLinkedList.Node<E> n = ((SafeLinkedList.Node) this.mHashMap.get(token)).mPrev;
            return n == null ? null : n.mElement;
        } else {
            return null;
        }
    }
}