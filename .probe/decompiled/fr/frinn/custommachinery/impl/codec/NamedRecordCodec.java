package fr.frinn.custommachinery.impl.codec;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.MapEncoder.Implementation;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class NamedRecordCodec<O, F> implements App<NamedRecordCodec.Mu<O>, F> {

    private final Function<O, F> getter;

    private final Function<O, MapEncoder<F>> encoder;

    private final MapDecoder<F> decoder;

    public static <O, F> NamedRecordCodec<O, F> of(Function<O, F> getter, NamedMapCodec<F> codec) {
        return new NamedRecordCodec<>(getter, o -> codec, codec);
    }

    public static <O, F> NamedRecordCodec<O, F> point(F instance) {
        return new NamedRecordCodec<>(o -> instance, o -> Encoder.empty(), Decoder.unit(instance));
    }

    public static <O, F> NamedRecordCodec<O, F> unbox(App<NamedRecordCodec.Mu<O>, F> box) {
        return (NamedRecordCodec<O, F>) box;
    }

    public static <O> NamedRecordCodec.Instance<O> instance() {
        return new NamedRecordCodec.Instance<>();
    }

    private NamedRecordCodec(Function<O, F> getter, Function<O, MapEncoder<F>> encoder, MapDecoder<F> decoder) {
        this.getter = getter;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public static <O> NamedMapCodec<O> create(Function<NamedRecordCodec.Instance<O>, ? extends App<NamedRecordCodec.Mu<O>, O>> builder, String name) {
        return build((App<NamedRecordCodec.Mu<O>, O>) builder.apply(instance()), name);
    }

    public static <O> NamedMapCodec<O> build(App<NamedRecordCodec.Mu<O>, O> builderBox, String name) {
        final NamedRecordCodec<O, O> builder = unbox(builderBox);
        return new NamedMapCodec<O>() {

            public <T> DataResult<O> decode(DynamicOps<T> ops, MapLike<T> input) {
                return builder.decoder.decode(ops, input);
            }

            public <T> RecordBuilder<T> encode(O input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                return ((MapEncoder) builder.encoder.apply(input)).encode(input, ops, prefix);
            }

            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return builder.decoder.keys(ops);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    public static final class Instance<O> implements Applicative<NamedRecordCodec.Mu<O>, NamedRecordCodec.Instance.Mu<O>> {

        public <A> App<NamedRecordCodec.Mu<O>, A> point(A a) {
            return NamedRecordCodec.point(a);
        }

        public <A, R> Function<App<NamedRecordCodec.Mu<O>, A>, App<NamedRecordCodec.Mu<O>, R>> lift1(App<NamedRecordCodec.Mu<O>, Function<A, R>> function) {
            return fa -> {
                final NamedRecordCodec<O, Function<A, R>> f = NamedRecordCodec.unbox(function);
                final NamedRecordCodec<O, A> a = (NamedRecordCodec<O, A>) NamedRecordCodec.unbox(fa);
                return new NamedRecordCodec(o -> ((Function) f.getter.apply(o)).apply(a.getter.apply(o)), o -> {
                    final MapEncoder<Function<A, R>> fEnc = (MapEncoder<Function<A, R>>) f.encoder.apply(o);
                    final MapEncoder<A> aEnc = (MapEncoder<A>) a.encoder.apply(o);
                    final A aFromO = (A) a.getter.apply(o);
                    return new Implementation<R>() {

                        public <T> RecordBuilder<T> encode(R input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                            aEnc.encode(aFromO, ops, prefix);
                            fEnc.encode((Function) a1 -> input, ops, prefix);
                            return prefix;
                        }

                        public <T> Stream<T> keys(DynamicOps<T> ops) {
                            return Stream.concat(aEnc.keys(ops), fEnc.keys(ops));
                        }

                        public String toString() {
                            return fEnc + " * " + aEnc;
                        }
                    };
                }, (MapDecoder<F>) (new com.mojang.serialization.MapDecoder.Implementation<R>() {

                    public <T> DataResult<R> decode(DynamicOps<T> ops, MapLike<T> input) {
                        return a.decoder.decode(ops, input).flatMap(ar -> f.decoder.decode(ops, input).map(fr -> fr.apply(ar)));
                    }

                    public <T> Stream<T> keys(DynamicOps<T> ops) {
                        return Stream.concat(a.decoder.keys(ops), f.decoder.keys(ops));
                    }

                    public String toString() {
                        return f.decoder + " * " + a.decoder;
                    }
                }));
            };
        }

        public <A, B, R> App<NamedRecordCodec.Mu<O>, R> ap2(App<NamedRecordCodec.Mu<O>, BiFunction<A, B, R>> func, App<NamedRecordCodec.Mu<O>, A> a, App<NamedRecordCodec.Mu<O>, B> b) {
            final NamedRecordCodec<O, BiFunction<A, B, R>> function = NamedRecordCodec.unbox(func);
            final NamedRecordCodec<O, A> fa = (NamedRecordCodec<O, A>) NamedRecordCodec.unbox((App<NamedRecordCodec.Mu<O>, F>) a);
            final NamedRecordCodec<O, B> fb = (NamedRecordCodec<O, B>) NamedRecordCodec.unbox((App<NamedRecordCodec.Mu<O>, F>) b);
            return (App<NamedRecordCodec.Mu<O>, R>) (new NamedRecordCodec<>(o -> ((BiFunction) function.getter.apply(o)).apply(fa.getter.apply(o), fb.getter.apply(o)), o -> {
                final MapEncoder<BiFunction<A, B, R>> fEncoder = (MapEncoder<BiFunction<A, B, R>>) function.encoder.apply(o);
                final MapEncoder<A> aEncoder = (MapEncoder<A>) fa.encoder.apply(o);
                final A aFromO = (A) fa.getter.apply(o);
                final MapEncoder<B> bEncoder = (MapEncoder<B>) fb.encoder.apply(o);
                final B bFromO = (B) fb.getter.apply(o);
                return new Implementation<R>() {

                    public <T> RecordBuilder<T> encode(R input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                        aEncoder.encode(aFromO, ops, prefix);
                        bEncoder.encode(bFromO, ops, prefix);
                        fEncoder.encode((BiFunction) (a1, b1) -> input, ops, prefix);
                        return prefix;
                    }

                    public <T> Stream<T> keys(DynamicOps<T> ops) {
                        return Stream.of(fEncoder.keys(ops), aEncoder.keys(ops), bEncoder.keys(ops)).flatMap(Function.identity());
                    }

                    public String toString() {
                        return fEncoder + " * " + aEncoder + " * " + bEncoder;
                    }
                };
            }, (MapDecoder<F>) (new com.mojang.serialization.MapDecoder.Implementation<R>() {

                public <T> DataResult<R> decode(DynamicOps<T> ops, MapLike<T> input) {
                    return DataResult.unbox(DataResult.instance().ap2(function.decoder.decode(ops, input), fa.decoder.decode(ops, input), fb.decoder.decode(ops, input)));
                }

                public <T> Stream<T> keys(DynamicOps<T> ops) {
                    return Stream.of(function.decoder.keys(ops), fa.decoder.keys(ops), fb.decoder.keys(ops)).flatMap(Function.identity());
                }

                public String toString() {
                    return function.decoder + " * " + fa.decoder + " * " + fb.decoder;
                }
            })));
        }

        public <T1, T2, T3, R> App<NamedRecordCodec.Mu<O>, R> ap3(App<NamedRecordCodec.Mu<O>, Function3<T1, T2, T3, R>> func, App<NamedRecordCodec.Mu<O>, T1> t1, App<NamedRecordCodec.Mu<O>, T2> t2, App<NamedRecordCodec.Mu<O>, T3> t3) {
            final NamedRecordCodec<O, Function3<T1, T2, T3, R>> function = NamedRecordCodec.unbox(func);
            final NamedRecordCodec<O, T1> f1 = (NamedRecordCodec<O, T1>) NamedRecordCodec.unbox((App<NamedRecordCodec.Mu<O>, F>) t1);
            final NamedRecordCodec<O, T2> f2 = (NamedRecordCodec<O, T2>) NamedRecordCodec.unbox((App<NamedRecordCodec.Mu<O>, F>) t2);
            final NamedRecordCodec<O, T3> f3 = (NamedRecordCodec<O, T3>) NamedRecordCodec.unbox((App<NamedRecordCodec.Mu<O>, F>) t3);
            return (App<NamedRecordCodec.Mu<O>, R>) (new NamedRecordCodec<>(o -> ((Function3) function.getter.apply(o)).apply(f1.getter.apply(o), f2.getter.apply(o), f3.getter.apply(o)), o -> {
                final MapEncoder<Function3<T1, T2, T3, R>> fEncoder = (MapEncoder<Function3<T1, T2, T3, R>>) function.encoder.apply(o);
                final MapEncoder<T1> e1 = (MapEncoder<T1>) f1.encoder.apply(o);
                final T1 v1 = (T1) f1.getter.apply(o);
                final MapEncoder<T2> e2 = (MapEncoder<T2>) f2.encoder.apply(o);
                final T2 v2 = (T2) f2.getter.apply(o);
                final MapEncoder<T3> e3 = (MapEncoder<T3>) f3.encoder.apply(o);
                final T3 v3 = (T3) f3.getter.apply(o);
                return new Implementation<R>() {

                    public <T> RecordBuilder<T> encode(R input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                        e1.encode(v1, ops, prefix);
                        e2.encode(v2, ops, prefix);
                        e3.encode(v3, ops, prefix);
                        fEncoder.encode((Function3) (t1, t2, t3) -> input, ops, prefix);
                        return prefix;
                    }

                    public <T> Stream<T> keys(DynamicOps<T> ops) {
                        return Stream.of(fEncoder.keys(ops), e1.keys(ops), e2.keys(ops), e3.keys(ops)).flatMap(Function.identity());
                    }

                    public String toString() {
                        return fEncoder + " * " + e1 + " * " + e2 + " * " + e3;
                    }
                };
            }, (MapDecoder<F>) (new com.mojang.serialization.MapDecoder.Implementation<R>() {

                public <T> DataResult<R> decode(DynamicOps<T> ops, MapLike<T> input) {
                    return DataResult.unbox(DataResult.instance().ap3(function.decoder.decode(ops, input), f1.decoder.decode(ops, input), f2.decoder.decode(ops, input), f3.decoder.decode(ops, input)));
                }

                public <T> Stream<T> keys(DynamicOps<T> ops) {
                    return Stream.of(function.decoder.keys(ops), f1.decoder.keys(ops), f2.decoder.keys(ops), f3.decoder.keys(ops)).flatMap(Function.identity());
                }

                public String toString() {
                    return function.decoder + " * " + f1.decoder + " * " + f2.decoder + " * " + f3.decoder;
                }
            })));
        }

        public <T1, T2, T3, T4, R> App<NamedRecordCodec.Mu<O>, R> ap4(App<NamedRecordCodec.Mu<O>, Function4<T1, T2, T3, T4, R>> func, App<NamedRecordCodec.Mu<O>, T1> t1, App<NamedRecordCodec.Mu<O>, T2> t2, App<NamedRecordCodec.Mu<O>, T3> t3, App<NamedRecordCodec.Mu<O>, T4> t4) {
            final NamedRecordCodec<O, Function4<T1, T2, T3, T4, R>> function = NamedRecordCodec.unbox(func);
            final NamedRecordCodec<O, T1> f1 = (NamedRecordCodec<O, T1>) NamedRecordCodec.unbox((App<NamedRecordCodec.Mu<O>, F>) t1);
            final NamedRecordCodec<O, T2> f2 = (NamedRecordCodec<O, T2>) NamedRecordCodec.unbox((App<NamedRecordCodec.Mu<O>, F>) t2);
            final NamedRecordCodec<O, T3> f3 = (NamedRecordCodec<O, T3>) NamedRecordCodec.unbox((App<NamedRecordCodec.Mu<O>, F>) t3);
            final NamedRecordCodec<O, T4> f4 = (NamedRecordCodec<O, T4>) NamedRecordCodec.unbox((App<NamedRecordCodec.Mu<O>, F>) t4);
            return (App<NamedRecordCodec.Mu<O>, R>) (new NamedRecordCodec<>(o -> ((Function4) function.getter.apply(o)).apply(f1.getter.apply(o), f2.getter.apply(o), f3.getter.apply(o), f4.getter.apply(o)), o -> {
                final MapEncoder<Function4<T1, T2, T3, T4, R>> fEncoder = (MapEncoder<Function4<T1, T2, T3, T4, R>>) function.encoder.apply(o);
                final MapEncoder<T1> e1 = (MapEncoder<T1>) f1.encoder.apply(o);
                final T1 v1 = (T1) f1.getter.apply(o);
                final MapEncoder<T2> e2 = (MapEncoder<T2>) f2.encoder.apply(o);
                final T2 v2 = (T2) f2.getter.apply(o);
                final MapEncoder<T3> e3 = (MapEncoder<T3>) f3.encoder.apply(o);
                final T3 v3 = (T3) f3.getter.apply(o);
                final MapEncoder<T4> e4 = (MapEncoder<T4>) f4.encoder.apply(o);
                final T4 v4 = (T4) f4.getter.apply(o);
                return new Implementation<R>() {

                    public <T> RecordBuilder<T> encode(R input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                        e1.encode(v1, ops, prefix);
                        e2.encode(v2, ops, prefix);
                        e3.encode(v3, ops, prefix);
                        e4.encode(v4, ops, prefix);
                        fEncoder.encode((Function4) (t1, t2, t3, t4) -> input, ops, prefix);
                        return prefix;
                    }

                    public <T> Stream<T> keys(DynamicOps<T> ops) {
                        return Stream.of(fEncoder.keys(ops), e1.keys(ops), e2.keys(ops), e3.keys(ops), e4.keys(ops)).flatMap(Function.identity());
                    }

                    public String toString() {
                        return fEncoder + " * " + e1 + " * " + e2 + " * " + e3 + " * " + e4;
                    }
                };
            }, (MapDecoder<F>) (new com.mojang.serialization.MapDecoder.Implementation<R>() {

                public <T> DataResult<R> decode(DynamicOps<T> ops, MapLike<T> input) {
                    return DataResult.unbox(DataResult.instance().ap4(function.decoder.decode(ops, input), f1.decoder.decode(ops, input), f2.decoder.decode(ops, input), f3.decoder.decode(ops, input), f4.decoder.decode(ops, input)));
                }

                public <T> Stream<T> keys(DynamicOps<T> ops) {
                    return Stream.of(function.decoder.keys(ops), f1.decoder.keys(ops), f2.decoder.keys(ops), f3.decoder.keys(ops), f4.decoder.keys(ops)).flatMap(Function.identity());
                }

                public String toString() {
                    return function.decoder + " * " + f1.decoder + " * " + f2.decoder + " * " + f3.decoder + " * " + f4.decoder;
                }
            })));
        }

        public <T, R> App<NamedRecordCodec.Mu<O>, R> map(Function<? super T, ? extends R> func, App<NamedRecordCodec.Mu<O>, T> ts) {
            NamedRecordCodec<O, T> unbox = (NamedRecordCodec<O, T>) NamedRecordCodec.unbox((App<NamedRecordCodec.Mu<O>, F>) ts);
            Function<O, T> getter = unbox.getter;
            return (App<NamedRecordCodec.Mu<O>, R>) (new NamedRecordCodec<>(getter.andThen(func), o -> new Implementation<R>() {

                private final MapEncoder<T> encoder = (MapEncoder<T>) unbox.encoder.apply(o);

                public <U> RecordBuilder<U> encode(R input, DynamicOps<U> ops, RecordBuilder<U> prefix) {
                    return this.encoder.encode(getter.apply(o), ops, prefix);
                }

                public <U> Stream<U> keys(DynamicOps<U> ops) {
                    return this.encoder.keys(ops);
                }

                public String toString() {
                    return this.encoder + "[mapped]";
                }
            }, unbox.decoder.map(func)));
        }

        private static final class Mu<O> implements com.mojang.datafixers.kinds.Applicative.Mu {
        }
    }

    public static final class Mu<O> implements K1 {
    }
}