package fr.frinn.custommachinery.impl.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import fr.frinn.custommachinery.api.codec.NamedCodec;

public abstract class NumberCodec<A extends Number> implements NamedCodec<A> {

    public abstract <T> DataResult<A> parse(DynamicOps<T> var1, T var2);

    public abstract A fromString(String var1) throws NumberFormatException;

    @Override
    public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
        DataResult<A> result = this.parse(ops, input);
        if (result.result().isPresent()) {
            return result.map(n -> Pair.of(n, ops.empty()));
        } else {
            DataResult<String> stringResult = ops.getStringValue(input);
            if (stringResult.result().isPresent()) {
                String s = (String) stringResult.result().get();
                try {
                    return DataResult.success(Pair.of(this.fromString(s), ops.empty()));
                } catch (NumberFormatException var7) {
                }
            }
            return result.map(n -> Pair.of(n, input));
        }
    }

    public <T> DataResult<T> encode(DynamicOps<T> ops, A input, T prefix) {
        return ops.mergeToPrimitive(prefix, ops.createNumeric(input));
    }
}