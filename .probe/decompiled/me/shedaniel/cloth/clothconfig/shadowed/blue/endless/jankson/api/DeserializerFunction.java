package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl.serializer.InternalDeserializerFunction;

@FunctionalInterface
public interface DeserializerFunction<A, B> extends InternalDeserializerFunction<B> {

    B apply(A var1, Marshaller var2) throws DeserializationException;

    @Override
    default B deserialize(Object a, Marshaller m) throws DeserializationException {
        try {
            return this.apply((A) a, m);
        } catch (ClassCastException var4) {
            throw new DeserializationException(var4);
        }
    }
}