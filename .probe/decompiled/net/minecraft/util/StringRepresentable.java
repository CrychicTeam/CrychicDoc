package net.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Keyable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface StringRepresentable {

    int PRE_BUILT_MAP_THRESHOLD = 16;

    String getSerializedName();

    static <E extends Enum<E> & StringRepresentable> StringRepresentable.EnumCodec<E> fromEnum(Supplier<E[]> supplierE0) {
        return fromEnumWithMapping(supplierE0, p_275327_ -> p_275327_);
    }

    static <E extends Enum<E> & StringRepresentable> StringRepresentable.EnumCodec<E> fromEnumWithMapping(Supplier<E[]> supplierE0, Function<String, String> functionStringString1) {
        E[] $$2 = (E[]) supplierE0.get();
        if ($$2.length > 16) {
            Map<String, E> $$3 = (Map<String, E>) Arrays.stream($$2).collect(Collectors.toMap(p_274905_ -> (String) functionStringString1.apply(((StringRepresentable) p_274905_).getSerializedName()), p_274903_ -> p_274903_));
            return new StringRepresentable.EnumCodec<>($$2, p_216438_ -> p_216438_ == null ? null : (Enum) $$3.get(p_216438_));
        } else {
            return new StringRepresentable.EnumCodec<>($$2, p_274908_ -> {
                for (E $$3x : $$2) {
                    if (((String) functionStringString1.apply($$3x.getSerializedName())).equals(p_274908_)) {
                        return $$3x;
                    }
                }
                return null;
            });
        }
    }

    static Keyable keys(final StringRepresentable[] stringRepresentable0) {
        return new Keyable() {

            public <T> Stream<T> keys(DynamicOps<T> p_184758_) {
                return Arrays.stream(stringRepresentable0).map(StringRepresentable::m_7912_).map(p_184758_::createString);
            }
        };
    }

    @Deprecated
    public static class EnumCodec<E extends Enum<E> & StringRepresentable> implements Codec<E> {

        private final Codec<E> codec;

        private final Function<String, E> resolver;

        public EnumCodec(E[] e0, Function<String, E> functionStringE1) {
            this.codec = ExtraCodecs.orCompressed(ExtraCodecs.stringResolverCodec(p_216461_ -> ((StringRepresentable) p_216461_).getSerializedName(), functionStringE1), ExtraCodecs.idResolverCodec(p_216454_ -> ((Enum) p_216454_).ordinal(), p_216459_ -> p_216459_ >= 0 && p_216459_ < e0.length ? e0[p_216459_] : null, -1));
            this.resolver = functionStringE1;
        }

        public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> dynamicOpsT0, T t1) {
            return this.codec.decode(dynamicOpsT0, t1);
        }

        public <T> DataResult<T> encode(E e0, DynamicOps<T> dynamicOpsT1, T t2) {
            return this.codec.encode(e0, dynamicOpsT1, t2);
        }

        @Nullable
        public E byName(@Nullable String string0) {
            return (E) this.resolver.apply(string0);
        }

        public E byName(@Nullable String string0, E e1) {
            return (E) Objects.requireNonNullElse(this.byName(string0), e1);
        }
    }
}