package mezz.jei.core.util.function;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class LazySupplier<T> implements Supplier<T> {

    private final Supplier<T> supplier;

    @Nullable
    private T cachedResult;

    public LazySupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (this.cachedResult == null) {
            this.cachedResult = (T) this.supplier.get();
        }
        return this.cachedResult;
    }
}