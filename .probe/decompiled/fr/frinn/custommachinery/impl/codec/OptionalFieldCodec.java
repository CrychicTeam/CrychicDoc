package fr.frinn.custommachinery.impl.codec;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.DataResult.PartialResult;
import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class OptionalFieldCodec<A> extends NamedMapCodec<Optional<A>> {

    private final String fieldName;

    private final NamedCodec<A> elementCodec;

    private final String name;

    public static <A> NamedMapCodec<Optional<A>> of(String fieldName, NamedCodec<A> elementCodec, String name) {
        return new OptionalFieldCodec<>(fieldName, elementCodec, name);
    }

    private OptionalFieldCodec(String fieldName, NamedCodec<A> elementCodec, String name) {
        this.fieldName = FieldCodec.toSnakeCase(fieldName);
        this.elementCodec = elementCodec;
        this.name = name;
    }

    public OptionalFieldCodec<A> aliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public <T> DataResult<Optional<A>> decode(DynamicOps<T> ops, MapLike<T> input) {
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
                ICustomMachineryAPI.INSTANCE.logger().debug("Missing optional property: \"{}\" of type: {}, using default value", this.fieldName, this.name);
            }
            return DataResult.success(Optional.empty());
        } else {
            DataResult<A> result = this.elementCodec.read(ops, value);
            if (result.result().isPresent()) {
                return result.map(Optional::of);
            } else {
                if (result.error().isPresent()) {
                    ICustomMachineryAPI.INSTANCE.logger().warn("Couldn't parse \"{}\" for key \"{}\", using default value\nError: {}", this.name, this.fieldName, ((PartialResult) result.error().get()).message());
                }
                return DataResult.success(Optional.empty());
            }
        }
    }

    public <T> RecordBuilder<T> encode(Optional<A> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
        return input.isPresent() ? prefix.add(this.fieldName, this.elementCodec.encodeStart(ops, (A) input.get())) : prefix;
    }

    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return Stream.of(ops.createString(this.fieldName));
    }

    @Override
    public String name() {
        return this.name;
    }
}