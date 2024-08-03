package fr.frinn.custommachinery.impl.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.DataResult.PartialResult;
import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class EnhancedListCodec<A> implements NamedCodec<List<A>> {

    private final NamedCodec<A> elementCodec;

    private final String name;

    public static <A> EnhancedListCodec<A> of(NamedCodec<A> elementCodec) {
        return of(elementCodec, "List<" + elementCodec.name() + ">");
    }

    public static <A> EnhancedListCodec<A> of(NamedCodec<A> elementCodec, String name) {
        return new EnhancedListCodec<>(elementCodec, name);
    }

    private EnhancedListCodec(NamedCodec<A> elementCodec, String name) {
        this.elementCodec = elementCodec;
        this.name = name;
    }

    public <T> DataResult<T> encode(DynamicOps<T> ops, List<A> input, T prefix) {
        if (input.isEmpty()) {
            return DataResult.success(ops.empty());
        } else {
            ListBuilder<T> builder = ops.listBuilder();
            for (A a : input) {
                builder.add(this.elementCodec.encodeStart(ops, a));
            }
            return builder.build(prefix);
        }
    }

    @Override
    public <T> DataResult<Pair<List<A>, T>> decode(DynamicOps<T> ops, T input) {
        if (ops.getStream(input).error().isPresent()) {
            return this.elementCodec.decode(ops, input).map(pair -> Pair.of(Collections.singletonList(pair.getFirst()), pair.getSecond()));
        } else {
            DataResult<Stream<T>> streamResult = ops.getStream(input);
            if (streamResult.result().isPresent()) {
                Stream<T> stream = (Stream<T>) streamResult.result().get();
                List<A> result = new ArrayList();
                stream.forEach(t -> {
                    DataResult<A> a = this.elementCodec.read(ops, t);
                    if (a.result().isPresent()) {
                        result.add(a.result().get());
                    } else if (a.error().isPresent()) {
                        ICustomMachineryAPI.INSTANCE.logger().warn("Error when parsing {} in list.\n{}", this.elementCodec.name(), ((PartialResult) a.error().get()).message());
                    }
                });
                return DataResult.success(Pair.of(result, ops.empty()));
            } else {
                return streamResult.map(s -> Pair.of(Collections.emptyList(), input));
            }
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            EnhancedListCodec<?> listCodec = (EnhancedListCodec<?>) o;
            return Objects.equals(this.elementCodec, listCodec.elementCodec);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.elementCodec });
    }

    @Override
    public String name() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}