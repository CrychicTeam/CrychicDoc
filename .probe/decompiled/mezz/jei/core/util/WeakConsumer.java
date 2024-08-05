package mezz.jei.core.util;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

public class WeakConsumer<T> implements Consumer<T> {

    private final WeakReference<Consumer<T>> weakReference;

    public WeakConsumer(Consumer<T> consumer) {
        this.weakReference = new WeakReference(consumer);
    }

    public void accept(T t) {
        Consumer<T> consumer = (Consumer<T>) this.weakReference.get();
        if (consumer != null) {
            consumer.accept(t);
        }
    }
}