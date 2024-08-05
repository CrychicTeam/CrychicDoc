package malte0811.ferritecore.hash;

import it.unimi.dsi.fastutil.Hash.Strategy;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

public class LambdaBasedHash<T> implements Strategy<T> {

    private final ToIntFunction<T> hash;

    private final BiPredicate<T, T> equal;

    public LambdaBasedHash(ToIntFunction<T> hash, BiPredicate<T, T> equal) {
        this.hash = hash;
        this.equal = equal;
    }

    public int hashCode(T o) {
        return this.hash.applyAsInt(o);
    }

    public boolean equals(T a, T b) {
        return this.equal.test(a, b);
    }
}