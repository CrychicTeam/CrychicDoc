package net.minecraft.client.telemetry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class TelemetryPropertyMap {

    final Map<TelemetryProperty<?>, Object> entries;

    TelemetryPropertyMap(Map<TelemetryProperty<?>, Object> mapTelemetryPropertyObject0) {
        this.entries = mapTelemetryPropertyObject0;
    }

    public static TelemetryPropertyMap.Builder builder() {
        return new TelemetryPropertyMap.Builder();
    }

    public static Codec<TelemetryPropertyMap> createCodec(final List<TelemetryProperty<?>> listTelemetryProperty0) {
        return (new MapCodec<TelemetryPropertyMap>() {

            public <T> RecordBuilder<T> encode(TelemetryPropertyMap p_261525_, DynamicOps<T> p_262068_, RecordBuilder<T> p_261850_) {
                RecordBuilder<T> $$3 = p_261850_;
                for (TelemetryProperty<?> $$4 : listTelemetryProperty0) {
                    $$3 = this.encodeProperty(p_261525_, $$3, $$4);
                }
                return $$3;
            }

            private <T, V> RecordBuilder<T> encodeProperty(TelemetryPropertyMap p_262128_, RecordBuilder<T> p_261947_, TelemetryProperty<V> p_261911_) {
                V $$3 = p_262128_.get(p_261911_);
                return $$3 != null ? p_261947_.add(p_261911_.id(), $$3, p_261911_.codec()) : p_261947_;
            }

            public <T> DataResult<TelemetryPropertyMap> decode(DynamicOps<T> p_261767_, MapLike<T> p_262176_) {
                DataResult<TelemetryPropertyMap.Builder> $$2 = DataResult.success(new TelemetryPropertyMap.Builder());
                for (TelemetryProperty<?> $$3 : listTelemetryProperty0) {
                    $$2 = this.decodeProperty($$2, p_261767_, p_262176_, $$3);
                }
                return $$2.map(TelemetryPropertyMap.Builder::m_260981_);
            }

            private <T, V> DataResult<TelemetryPropertyMap.Builder> decodeProperty(DataResult<TelemetryPropertyMap.Builder> p_261892_, DynamicOps<T> p_261859_, MapLike<T> p_261668_, TelemetryProperty<V> p_261627_) {
                T $$4 = (T) p_261668_.get(p_261627_.id());
                if ($$4 != null) {
                    DataResult<V> $$5 = p_261627_.codec().parse(p_261859_, $$4);
                    return p_261892_.apply2stable((p_262028_, p_261796_) -> p_262028_.put(p_261627_, (V) p_261796_), $$5);
                } else {
                    return p_261892_;
                }
            }

            public <T> Stream<T> keys(DynamicOps<T> p_261746_) {
                return listTelemetryProperty0.stream().map(TelemetryProperty::f_260687_).map(p_261746_::createString);
            }
        }).codec();
    }

    @Nullable
    public <T> T get(TelemetryProperty<T> telemetryPropertyT0) {
        return (T) this.entries.get(telemetryPropertyT0);
    }

    public String toString() {
        return this.entries.toString();
    }

    public Set<TelemetryProperty<?>> propertySet() {
        return this.entries.keySet();
    }

    public static class Builder {

        private final Map<TelemetryProperty<?>, Object> entries = new Reference2ObjectOpenHashMap();

        Builder() {
        }

        public <T> TelemetryPropertyMap.Builder put(TelemetryProperty<T> telemetryPropertyT0, T t1) {
            this.entries.put(telemetryPropertyT0, t1);
            return this;
        }

        public <T> TelemetryPropertyMap.Builder putIfNotNull(TelemetryProperty<T> telemetryPropertyT0, @Nullable T t1) {
            if (t1 != null) {
                this.entries.put(telemetryPropertyT0, t1);
            }
            return this;
        }

        public TelemetryPropertyMap.Builder putAll(TelemetryPropertyMap telemetryPropertyMap0) {
            this.entries.putAll(telemetryPropertyMap0.entries);
            return this;
        }

        public TelemetryPropertyMap build() {
            return new TelemetryPropertyMap(this.entries);
        }
    }
}