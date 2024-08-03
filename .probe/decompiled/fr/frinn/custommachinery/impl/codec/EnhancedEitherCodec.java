package fr.frinn.custommachinery.impl.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.DataResult.PartialResult;
import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import java.util.Objects;

public class EnhancedEitherCodec<F, S> implements NamedCodec<Either<F, S>> {

    private final String error = "Can't deserialize %s using either %s or %s.%n%s%n%s";

    private final NamedCodec<F> first;

    private final NamedCodec<S> second;

    private final String name;

    public static <F, S> EnhancedEitherCodec<F, S> of(NamedCodec<F> first, NamedCodec<S> second) {
        return of(first, second, "Either<" + first.name() + ", " + second.name() + ">");
    }

    public static <F, S> EnhancedEitherCodec<F, S> of(NamedCodec<F> first, NamedCodec<S> second, String name) {
        return new EnhancedEitherCodec<>(first, second, name);
    }

    private EnhancedEitherCodec(NamedCodec<F> first, NamedCodec<S> second, String name) {
        this.first = first;
        this.second = second;
        this.name = name;
    }

    @Override
    public <T> DataResult<Pair<Either<F, S>, T>> decode(DynamicOps<T> ops, T input) {
        DataResult<Pair<Either<F, S>, T>> firstRead = this.first.decode(ops, input).map(vo -> vo.mapFirst(Either::left));
        if (firstRead.result().isPresent()) {
            return firstRead;
        } else {
            String firstError = (String) firstRead.error().map(PartialResult::message).orElse("");
            if (ICustomMachineryAPI.INSTANCE.config().logFirstEitherError()) {
                ICustomMachineryAPI.INSTANCE.logger().warn("Can't deserialize {} with {}, trying with {} now.\n{}", this, this.first.name(), this.second.name(), firstError);
            }
            return this.second.decode(ops, input).mapError(s -> String.format("Can't deserialize %s using either %s or %s.%n%s%n%s", this, this.first.name(), this.second.name(), firstError, s)).map(vo -> vo.mapFirst(Either::right));
        }
    }

    public <T> DataResult<T> encode(DynamicOps<T> ops, Either<F, S> input, T prefix) {
        return (DataResult<T>) input.map(value1 -> this.first.encode(ops, (F) value1, prefix), value2 -> this.second.encode(ops, (S) value2, prefix));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            EnhancedEitherCodec<?, ?> eitherCodec = (EnhancedEitherCodec<?, ?>) o;
            return Objects.equals(this.first, eitherCodec.first) && Objects.equals(this.second, eitherCodec.second);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.first, this.second });
    }

    @Override
    public String name() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}