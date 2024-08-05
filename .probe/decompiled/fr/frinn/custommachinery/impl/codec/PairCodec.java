package fr.frinn.custommachinery.impl.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import java.util.Objects;

public class PairCodec<F, S> implements NamedCodec<Pair<F, S>> {

    private final NamedCodec<F> first;

    private final NamedCodec<S> second;

    public static <F, S> PairCodec<F, S> of(NamedCodec<F> first, NamedCodec<S> second) {
        return new PairCodec<>(first, second);
    }

    private PairCodec(NamedCodec<F> first, NamedCodec<S> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public <T> DataResult<Pair<Pair<F, S>, T>> decode(DynamicOps<T> ops, T input) {
        return this.first.decode(ops, input).flatMap(p1 -> this.second.decode(ops, p1.getSecond()).map(p2 -> Pair.of(Pair.of(p1.getFirst(), p2.getFirst()), p2.getSecond())));
    }

    public <T> DataResult<T> encode(DynamicOps<T> ops, Pair<F, S> value, T rest) {
        return this.second.encode(ops, (S) value.getSecond(), rest).flatMap(f -> this.first.encode(ops, (F) value.getFirst(), f));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof PairCodec<?, ?> codec) ? false : Objects.equals(this.first, codec.first) && Objects.equals(this.second, codec.second);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.first, this.second });
    }

    @Override
    public String name() {
        return "Pair<" + this.first.name() + ", " + this.second.name() + ">";
    }
}