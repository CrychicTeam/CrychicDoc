package mezz.jei.core.util.function;

import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

public class CachedFunction<T, R> implements Function<T, R> {

    private final Function<T, R> function;

    @Nullable
    private T previousValue;

    @Nullable
    private R cachedResult;

    public CachedFunction(Function<T, R> function) {
        this.function = function;
    }

    public R apply(T currentValue) {
        if (currentValue.equals(this.previousValue)) {
            assert this.cachedResult != null;
            return this.cachedResult;
        } else {
            this.cachedResult = (R) this.function.apply(currentValue);
            this.previousValue = currentValue;
            return this.cachedResult;
        }
    }
}