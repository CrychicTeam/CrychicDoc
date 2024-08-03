package fr.frinn.custommachinery.impl.codec;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.CompressorHolder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.MapCodec.ResultFunction;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public abstract class NamedMapCodec<A> extends CompressorHolder implements MapDecoder<A>, MapEncoder<A>, NamedCodec<A> {

    protected final List<String> aliases = new ArrayList();

    public static <A> NamedMapCodec<A> of(MapDecoder<A> decoder, MapEncoder<A> encoder, String name) {
        return new NamedMapCodec<A>() {

            public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
                return decoder.decode(ops, input);
            }

            public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                return encoder.encode(input, ops, prefix);
            }

            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return Stream.concat(encoder.keys(ops), decoder.keys(ops));
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    public <O> NamedRecordCodec<O, A> forGetter(Function<O, A> getter) {
        return NamedRecordCodec.of(getter, this);
    }

    @Override
    public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
        return this.compressedDecode(ops, input).map(r -> Pair.of(r, input));
    }

    @Override
    public <T> DataResult<T> encode(DynamicOps<T> ops, A input, T prefix) {
        return this.encode(input, ops, this.compressedBuilder(ops)).build(prefix);
    }

    public NamedMapCodec<A> withLifecycle(Lifecycle lifecycle) {
        return new NamedMapCodec<A>() {

            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return NamedMapCodec.this.keys(ops);
            }

            public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
                return NamedMapCodec.this.decode(ops, input).setLifecycle(lifecycle);
            }

            public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                return NamedMapCodec.this.encode(input, ops, prefix).setLifecycle(lifecycle);
            }

            @Override
            public String name() {
                return NamedMapCodec.this.name();
            }

            public String toString() {
                return NamedMapCodec.this.toString();
            }
        };
    }

    public MapCodec<A> mapCodec() {
        return MapCodec.of(this, this);
    }

    public NamedMapCodec<A> mapResult(ResultFunction<A> function) {
        return new NamedMapCodec<A>() {

            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return NamedMapCodec.this.keys(ops);
            }

            public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                return function.coApply(ops, input, NamedMapCodec.this.encode(input, ops, prefix));
            }

            public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
                return function.apply(ops, input, NamedMapCodec.this.decode(ops, input));
            }

            @Override
            public String name() {
                return NamedMapCodec.this.name();
            }
        };
    }

    public NamedMapCodec<A> orElse(Consumer<String> onError, A value) {
        return this.orElse(DataFixUtils.consumerToFunction(onError), value);
    }

    public NamedMapCodec<A> orElse(UnaryOperator<String> onError, A value) {
        return this.mapResult(new ResultFunction<A>() {

            public <T> DataResult<A> apply(DynamicOps<T> ops, MapLike<T> input, DataResult<A> a) {
                return DataResult.success(a.mapError(onError).result().orElse(value));
            }

            public <T> RecordBuilder<T> coApply(DynamicOps<T> ops, A input, RecordBuilder<T> t) {
                return t.mapError(onError);
            }
        });
    }

    public NamedMapCodec<A> orElseGet(Consumer<String> onError, Supplier<? extends A> value) {
        return this.orElseGet(DataFixUtils.consumerToFunction(onError), value);
    }

    public NamedMapCodec<A> orElseGet(UnaryOperator<String> onError, Supplier<? extends A> value) {
        return this.mapResult(new ResultFunction<A>() {

            public <T> DataResult<A> apply(DynamicOps<T> ops, MapLike<T> input, DataResult<A> a) {
                return DataResult.success(a.mapError(onError).result().orElseGet(value));
            }

            public <T> RecordBuilder<T> coApply(DynamicOps<T> ops, A input, RecordBuilder<T> t) {
                return t.mapError(onError);
            }
        });
    }

    public NamedMapCodec<A> orElse(A value) {
        return this.orElseGet(() -> value);
    }

    public NamedMapCodec<A> orElseGet(Supplier<? extends A> value) {
        return new NamedMapCodec<A>() {

            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return NamedMapCodec.this.keys(ops);
            }

            public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                return value.get().equals(input) ? prefix : NamedMapCodec.this.encode(input, ops, prefix);
            }

            public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
                return DataResult.success(NamedMapCodec.this.decode(ops, input).result().orElseGet(value));
            }

            @Override
            public String name() {
                return NamedMapCodec.this.name();
            }
        };
    }

    public NamedMapCodec<A> aliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }
}