package dev.shadowsoffire.placebo.codec;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public class CodecMap<V extends CodecProvider<? super V>> implements Codec<V> {

    protected final String name;

    private final BiMap<ResourceLocation, Codec<? extends V>> codecs = HashBiMap.create();

    private final Codec<V> codec;

    @Nullable
    protected Codec<? extends V> defaultCodec;

    public CodecMap(String name) {
        this.name = name;
        this.codec = new MapBackedCodec<>(this.name, this.codecs, this::getDefaultCodec);
    }

    @Nullable
    public Codec<? extends V> getDefaultCodec() {
        return this.defaultCodec;
    }

    public void setDefaultCodec(Codec<? extends V> codec) {
        synchronized (this.codecs) {
            if (this.defaultCodec != null) {
                throw new UnsupportedOperationException("Attempted to set the default codec after it has already been set.");
            } else if (this.getKey(codec) == null) {
                throw new UnsupportedOperationException("Attempted to set the default codec without registering it first.");
            } else {
                this.defaultCodec = codec;
            }
        }
    }

    public boolean isEmpty() {
        return this.codecs.isEmpty();
    }

    public boolean containsKey(ResourceLocation key) {
        return this.codecs.containsKey(key);
    }

    @Nullable
    public Codec<? extends V> getValue(ResourceLocation key) {
        return (Codec<? extends V>) this.codecs.get(key);
    }

    @Nullable
    public ResourceLocation getKey(Codec<?> codec) {
        return (ResourceLocation) this.codecs.inverse().get(codec);
    }

    public void register(ResourceLocation key, Codec<? extends V> codec) {
        synchronized (this.codecs) {
            if (this.codecs.containsKey(key)) {
                throw new UnsupportedOperationException("Attempted to register a " + this.name + " codec with key " + key + " but one already exists!");
            } else if (this.codecs.containsValue(codec)) {
                throw new UnsupportedOperationException("Attempted to register a " + this.name + " codec with key " + key + " but it is already registered under the key " + this.getKey(codec));
            } else {
                this.codecs.put(key, codec);
            }
        }
    }

    public final <T> DataResult<T> encode(V input, DynamicOps<T> ops, T prefix) {
        return this.codec.encode(input, ops, prefix);
    }

    public final <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
        return this.codec.decode(ops, input);
    }
}