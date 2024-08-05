package io.github.apace100.origins.badge;

import com.mojang.serialization.Codec;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableData.Instance;
import io.github.apace100.calio.registry.DataObjectFactory;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;

public final class BadgeFactory implements DataObjectFactory<Badge> {

    private final ResourceLocation id;

    private final SerializableData data;

    private final Function<Instance, Badge> factory;

    private final Codec<Badge> codec;

    public BadgeFactory(ResourceLocation id, SerializableData data, Function<Instance, Badge> factory) {
        this.id = id;
        this.data = data;
        this.factory = factory;
        this.codec = data.xmap(factory, x -> {
            SerializableData var10003 = this.data();
            Objects.requireNonNull(var10003);
            return x.toData(new Instance(var10003));
        }).codec();
    }

    public Codec<Badge> getCodec() {
        return this.codec;
    }

    public SerializableData getData() {
        return this.data;
    }

    public Badge fromData(Instance instance) {
        return (Badge) this.factory().apply(instance);
    }

    public Instance toData(Badge badge) {
        SerializableData var10003 = this.data;
        Objects.requireNonNull(this.data);
        return badge.toData(new Instance(var10003));
    }

    public ResourceLocation id() {
        return this.id;
    }

    public SerializableData data() {
        return this.data;
    }

    public Function<Instance, Badge> factory() {
        return this.factory;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            BadgeFactory that = (BadgeFactory) obj;
            return Objects.equals(this.id, that.id) && Objects.equals(this.data, that.data) && Objects.equals(this.factory, that.factory);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.id, this.data, this.factory });
    }

    public String toString() {
        return "BadgeFactory[id=" + this.id + ", data=" + this.data + ", factory=" + this.factory + "]";
    }
}