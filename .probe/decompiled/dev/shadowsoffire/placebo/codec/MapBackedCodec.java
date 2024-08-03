package dev.shadowsoffire.placebo.codec;

import com.google.common.collect.BiMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.shadowsoffire.placebo.Placebo;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public class MapBackedCodec<V extends CodecProvider<? super V>> implements Codec<V> {

    protected final String name;

    protected final BiMap<ResourceLocation, Codec<? extends V>> registry;

    protected final Supplier<Codec<? extends V>> defaultCodec;

    public MapBackedCodec(String name, BiMap<ResourceLocation, Codec<? extends V>> registry, Supplier<Codec<? extends V>> defaultCodec) {
        this.name = name;
        this.registry = registry;
        this.defaultCodec = defaultCodec;
    }

    public MapBackedCodec(String name, BiMap<ResourceLocation, Codec<? extends V>> registry) {
        this(name, registry, () -> null);
    }

    public <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
        Optional<T> type = ops.get(input, "type").resultOrPartial(str -> {
        });
        Optional<ResourceLocation> key = type.map(t -> (ResourceLocation) ((Pair) ResourceLocation.CODEC.decode(ops, t).resultOrPartial(Placebo.LOGGER::error).get()).getFirst());
        Codec codec = (Codec) key.map(this.registry::get).orElse((Codec) this.defaultCodec.get());
        return codec == null ? DataResult.error(() -> "Failure when parsing a " + this.name + ". Unrecognized type: " + (String) key.map(ResourceLocation::toString).orElse("null")) : codec.decode(ops, input);
    }

    public <T> DataResult<T> encode(V input, DynamicOps<T> ops, T prefix) {
        Codec<V> codec = (Codec<V>) input.getCodec();
        ResourceLocation key = (ResourceLocation) this.registry.inverse().get(codec);
        if (key == null) {
            return DataResult.error(() -> "Attempted to serialize an element of type " + this.name + " with an unregistered codec! Object: " + input);
        } else {
            T encodedKey = (T) ResourceLocation.CODEC.encodeStart(ops, key).getOrThrow(false, Placebo.LOGGER::error);
            T encodedObj = (T) codec.encode(input, ops, prefix).getOrThrow(false, Placebo.LOGGER::error);
            return ops.mergeToMap(encodedObj, ops.createString("type"), encodedKey);
        }
    }
}