package me.lucko.spark.lib.adventure.examination.string;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.AbstractExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringExaminer extends AbstractExaminer<String> {

    private static final Function<String, String> DEFAULT_ESCAPER = string -> string.replace("\"", "\\\"").replace("\\", "\\\\").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");

    private static final Collector<CharSequence, ?, String> COMMA_CURLY = Collectors.joining(", ", "{", "}");

    private static final Collector<CharSequence, ?, String> COMMA_SQUARE = Collectors.joining(", ", "[", "]");

    private final Function<String, String> escaper;

    @NotNull
    public static StringExaminer simpleEscaping() {
        return StringExaminer.Instances.SIMPLE_ESCAPING;
    }

    public StringExaminer(@NotNull final Function<String, String> escaper) {
        this.escaper = escaper;
    }

    @NotNull
    protected <E> String array(@NotNull final E[] array, @NotNull final Stream<String> elements) {
        return (String) elements.collect(COMMA_SQUARE);
    }

    @NotNull
    protected <E> String collection(@NotNull final Collection<E> collection, @NotNull final Stream<String> elements) {
        return (String) elements.collect(COMMA_SQUARE);
    }

    @NotNull
    protected String examinable(@NotNull final String name, @NotNull final Stream<Entry<String, String>> properties) {
        return name + (String) properties.map(property -> (String) property.getKey() + '=' + (String) property.getValue()).collect(COMMA_CURLY);
    }

    @NotNull
    protected <K, V> String map(@NotNull final Map<K, V> map, @NotNull final Stream<Entry<String, String>> entries) {
        return (String) entries.map(entry -> (String) entry.getKey() + '=' + (String) entry.getValue()).collect(COMMA_CURLY);
    }

    @NotNull
    protected String nil() {
        return "null";
    }

    @NotNull
    protected String scalar(@NotNull final Object value) {
        return String.valueOf(value);
    }

    @NotNull
    public String examine(final boolean value) {
        return String.valueOf(value);
    }

    @NotNull
    public String examine(final byte value) {
        return String.valueOf(value);
    }

    @NotNull
    public String examine(final char value) {
        return Strings.wrapIn((String) this.escaper.apply(String.valueOf(value)), '\'');
    }

    @NotNull
    public String examine(final double value) {
        return Strings.withSuffix(String.valueOf(value), 'd');
    }

    @NotNull
    public String examine(final float value) {
        return Strings.withSuffix(String.valueOf(value), 'f');
    }

    @NotNull
    public String examine(final int value) {
        return String.valueOf(value);
    }

    @NotNull
    public String examine(final long value) {
        return String.valueOf(value);
    }

    @NotNull
    public String examine(final short value) {
        return String.valueOf(value);
    }

    @NotNull
    protected <T> String stream(@NotNull final Stream<T> stream) {
        return (String) stream.map(this::examine).collect(COMMA_SQUARE);
    }

    @NotNull
    protected String stream(@NotNull final DoubleStream stream) {
        return (String) stream.mapToObj(this::examine).collect(COMMA_SQUARE);
    }

    @NotNull
    protected String stream(@NotNull final IntStream stream) {
        return (String) stream.mapToObj(this::examine).collect(COMMA_SQUARE);
    }

    @NotNull
    protected String stream(@NotNull final LongStream stream) {
        return (String) stream.mapToObj(this::examine).collect(COMMA_SQUARE);
    }

    @NotNull
    public String examine(@Nullable final String value) {
        return value == null ? this.nil() : Strings.wrapIn((String) this.escaper.apply(value), '"');
    }

    @NotNull
    protected String array(final int length, final IntFunction<String> value) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < length; i++) {
            sb.append((String) value.apply(i));
            if (i + 1 < length) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }

    private static final class Instances {

        static final StringExaminer SIMPLE_ESCAPING = new StringExaminer(StringExaminer.DEFAULT_ESCAPER);
    }
}