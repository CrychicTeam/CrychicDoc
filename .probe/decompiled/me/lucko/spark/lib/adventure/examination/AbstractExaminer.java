package me.lucko.spark.lib.adventure.examination;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.function.IntFunction;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractExaminer<R> implements Examiner<R> {

    @NotNull
    @Override
    public R examine(@Nullable final Object value) {
        if (value == null) {
            return this.nil();
        } else if (value instanceof String) {
            return this.examine((String) value);
        } else if (value instanceof Examinable) {
            return this.examine((Examinable) value);
        } else if (value instanceof Collection) {
            return this.collection((Collection) value);
        } else if (value instanceof Map) {
            return this.map((Map) value);
        } else if (value.getClass().isArray()) {
            Class<?> type = value.getClass().getComponentType();
            if (type.isPrimitive()) {
                if (type == boolean.class) {
                    return this.examine((boolean[]) value);
                }
                if (type == byte.class) {
                    return this.examine((byte[]) value);
                }
                if (type == char.class) {
                    return this.examine((char[]) value);
                }
                if (type == double.class) {
                    return this.examine((double[]) value);
                }
                if (type == float.class) {
                    return this.examine((float[]) value);
                }
                if (type == int.class) {
                    return this.examine((int[]) value);
                }
                if (type == long.class) {
                    return this.examine((long[]) value);
                }
                if (type == short.class) {
                    return this.examine((short[]) value);
                }
            }
            return this.array((Object[]) value);
        } else if (value instanceof Boolean) {
            return this.examine(((Boolean) value).booleanValue());
        } else if (value instanceof Character) {
            return this.examine(((Character) value).charValue());
        } else {
            if (value instanceof Number) {
                if (value instanceof Byte) {
                    return this.examine(((Byte) value).byteValue());
                }
                if (value instanceof Double) {
                    return this.examine(((Double) value).doubleValue());
                }
                if (value instanceof Float) {
                    return this.examine(((Float) value).floatValue());
                }
                if (value instanceof Integer) {
                    return this.examine(((Integer) value).intValue());
                }
                if (value instanceof Long) {
                    return this.examine(((Long) value).longValue());
                }
                if (value instanceof Short) {
                    return this.examine(((Short) value).shortValue());
                }
            } else if (value instanceof BaseStream) {
                if (value instanceof Stream) {
                    return this.stream((Stream) value);
                }
                if (value instanceof DoubleStream) {
                    return this.stream((DoubleStream) value);
                }
                if (value instanceof IntStream) {
                    return this.stream((IntStream) value);
                }
                if (value instanceof LongStream) {
                    return this.stream((LongStream) value);
                }
            }
            return this.scalar(value);
        }
    }

    @NotNull
    private <E> R array(@NotNull final E[] array) {
        return this.array(array, Arrays.stream(array).map(this::examine));
    }

    @NotNull
    protected abstract <E> R array(@NotNull final E[] array, @NotNull final Stream<R> elements);

    @NotNull
    private <E> R collection(@NotNull final Collection<E> collection) {
        return this.collection(collection, collection.stream().map(this::examine));
    }

    @NotNull
    protected abstract <E> R collection(@NotNull final Collection<E> collection, @NotNull final Stream<R> elements);

    @NotNull
    @Override
    public R examine(@NotNull final String name, @NotNull final Stream<? extends ExaminableProperty> properties) {
        return this.examinable(name, properties.map(property -> new SimpleImmutableEntry(property.name(), property.examine(this))));
    }

    @NotNull
    protected abstract R examinable(@NotNull final String name, @NotNull final Stream<Entry<String, R>> properties);

    @NotNull
    private <K, V> R map(@NotNull final Map<K, V> map) {
        return this.map(map, map.entrySet().stream().map(entry -> new SimpleImmutableEntry(this.examine(entry.getKey()), this.examine(entry.getValue()))));
    }

    @NotNull
    protected abstract <K, V> R map(@NotNull final Map<K, V> map, @NotNull final Stream<Entry<R, R>> entries);

    @NotNull
    protected abstract R nil();

    @NotNull
    protected abstract R scalar(@NotNull final Object value);

    @NotNull
    protected abstract <T> R stream(@NotNull final Stream<T> stream);

    @NotNull
    protected abstract R stream(@NotNull final DoubleStream stream);

    @NotNull
    protected abstract R stream(@NotNull final IntStream stream);

    @NotNull
    protected abstract R stream(@NotNull final LongStream stream);

    @NotNull
    @Override
    public R examine(@Nullable final boolean[] values) {
        return values == null ? this.nil() : this.array(values.length, index -> this.examine(values[index]));
    }

    @NotNull
    @Override
    public R examine(@Nullable final byte[] values) {
        return values == null ? this.nil() : this.array(values.length, index -> this.examine(values[index]));
    }

    @NotNull
    @Override
    public R examine(@Nullable final char[] values) {
        return values == null ? this.nil() : this.array(values.length, index -> this.examine(values[index]));
    }

    @NotNull
    @Override
    public R examine(@Nullable final double[] values) {
        return values == null ? this.nil() : this.array(values.length, index -> this.examine(values[index]));
    }

    @NotNull
    @Override
    public R examine(@Nullable final float[] values) {
        return values == null ? this.nil() : this.array(values.length, index -> this.examine(values[index]));
    }

    @NotNull
    @Override
    public R examine(@Nullable final int[] values) {
        return values == null ? this.nil() : this.array(values.length, index -> this.examine(values[index]));
    }

    @NotNull
    @Override
    public R examine(@Nullable final long[] values) {
        return values == null ? this.nil() : this.array(values.length, index -> this.examine(values[index]));
    }

    @NotNull
    @Override
    public R examine(@Nullable final short[] values) {
        return values == null ? this.nil() : this.array(values.length, index -> this.examine(values[index]));
    }

    @NotNull
    protected abstract R array(final int length, final IntFunction<R> value);
}