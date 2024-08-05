package fr.frinn.custommachinery.impl.codec;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.DataResult.PartialResult;
import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DefaultOptionalFieldCodec<A> extends NamedMapCodec<A> {

    private final String fieldName;

    private final NamedCodec<A> elementCodec;

    private final Supplier<A> defaultValue;

    private final String name;

    public static <A> NamedMapCodec<A> of(String fieldName, NamedCodec<A> elementCodec, Supplier<A> defaultValue, String name) {
        return new DefaultOptionalFieldCodec<>(fieldName, elementCodec, defaultValue, name);
    }

    private DefaultOptionalFieldCodec(String fieldName, NamedCodec<A> elementCodec, Supplier<A> defaultValue, String name) {
        this.fieldName = FieldCodec.toSnakeCase(fieldName);
        this.elementCodec = elementCodec;
        this.defaultValue = defaultValue;
        this.name = name;
    }

    public DefaultOptionalFieldCodec<A> aliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
        T value = FieldCodec.tryGetValue(ops, input, this.fieldName);
        if (value == null) {
            for (String alias : this.aliases) {
                value = (T) input.get(alias);
                if (value != null) {
                    break;
                }
            }
        }
        if (value == null) {
            if (ICustomMachineryAPI.INSTANCE.config().logMissingOptional()) {
                ICustomMachineryAPI.INSTANCE.logger().debug("Missing optional property: \"{}\" of type: {}, using default value: {}", this.fieldName, this.name, this.defaultValue.get());
            }
            return DataResult.success(this.defaultValue.get());
        } else {
            DataResult<A> result = this.elementCodec.read(ops, value);
            if (result.result().isPresent()) {
                return result;
            } else {
                if (result.error().isPresent()) {
                    ICustomMachineryAPI.INSTANCE.logger().warn("Couldn't parse \"{}\" for key \"{}\", using default value: {}\nError: {}", this.name, this.fieldName, this.defaultValue.get(), ((PartialResult) result.error().get()).message());
                }
                return DataResult.success(this.defaultValue.get());
            }
        }
    }

    public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
        return !Objects.equals(input, this.defaultValue.get()) ? prefix.add(this.fieldName, this.elementCodec.encodeStart(ops, input)) : prefix;
    }

    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return Stream.of(ops.createString(this.fieldName));
    }

    @Override
    public String name() {
        return this.name;
    }
}