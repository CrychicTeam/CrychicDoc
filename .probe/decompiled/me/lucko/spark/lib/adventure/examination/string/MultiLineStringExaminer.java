package me.lucko.spark.lib.adventure.examination.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.AbstractExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiLineStringExaminer extends AbstractExaminer<Stream<String>> {

    private static final String INDENT_2 = "  ";

    private final StringExaminer examiner;

    @NotNull
    public static MultiLineStringExaminer simpleEscaping() {
        return MultiLineStringExaminer.Instances.SIMPLE_ESCAPING;
    }

    public MultiLineStringExaminer(@NotNull final StringExaminer examiner) {
        this.examiner = examiner;
    }

    @NotNull
    protected <E> Stream<String> array(@NotNull final E[] array, @NotNull final Stream<Stream<String>> elements) {
        return this.arrayLike(elements);
    }

    @NotNull
    protected <E> Stream<String> collection(@NotNull final Collection<E> collection, @NotNull final Stream<Stream<String>> elements) {
        return this.arrayLike(elements);
    }

    @NotNull
    protected Stream<String> examinable(@NotNull final String name, @NotNull final Stream<Entry<String, Stream<String>>> properties) {
        Stream<String> flattened = flatten(",", properties.map(entry -> association(this.examine((String) entry.getKey()), " = ", (Stream<String>) entry.getValue())));
        Stream<String> indented = indent(flattened);
        return enclose(indented, name + "{", "}");
    }

    @NotNull
    protected <K, V> Stream<String> map(@NotNull final Map<K, V> map, @NotNull final Stream<Entry<Stream<String>, Stream<String>>> entries) {
        Stream<String> flattened = flatten(",", entries.map(entry -> association((Stream<String>) entry.getKey(), " = ", (Stream<String>) entry.getValue())));
        Stream<String> indented = indent(flattened);
        return enclose(indented, "{", "}");
    }

    @NotNull
    protected Stream<String> nil() {
        return Stream.of(this.examiner.nil());
    }

    @NotNull
    protected Stream<String> scalar(@NotNull final Object value) {
        return Stream.of(this.examiner.scalar(value));
    }

    @NotNull
    public Stream<String> examine(final boolean value) {
        return Stream.of(this.examiner.examine(value));
    }

    @NotNull
    public Stream<String> examine(final byte value) {
        return Stream.of(this.examiner.examine(value));
    }

    @NotNull
    public Stream<String> examine(final char value) {
        return Stream.of(this.examiner.examine(value));
    }

    @NotNull
    public Stream<String> examine(final double value) {
        return Stream.of(this.examiner.examine(value));
    }

    @NotNull
    public Stream<String> examine(final float value) {
        return Stream.of(this.examiner.examine(value));
    }

    @NotNull
    public Stream<String> examine(final int value) {
        return Stream.of(this.examiner.examine(value));
    }

    @NotNull
    public Stream<String> examine(final long value) {
        return Stream.of(this.examiner.examine(value));
    }

    @NotNull
    public Stream<String> examine(final short value) {
        return Stream.of(this.examiner.examine(value));
    }

    @NotNull
    protected Stream<String> array(final int length, final IntFunction<Stream<String>> value) {
        return this.arrayLike(length == 0 ? Stream.empty() : IntStream.range(0, length).mapToObj(value));
    }

    @NotNull
    protected <T> Stream<String> stream(@NotNull final Stream<T> stream) {
        return this.arrayLike(stream.map(this::examine));
    }

    @NotNull
    protected Stream<String> stream(@NotNull final DoubleStream stream) {
        return this.arrayLike(stream.mapToObj(this::examine));
    }

    @NotNull
    protected Stream<String> stream(@NotNull final IntStream stream) {
        return this.arrayLike(stream.mapToObj(this::examine));
    }

    @NotNull
    protected Stream<String> stream(@NotNull final LongStream stream) {
        return this.arrayLike(stream.mapToObj(this::examine));
    }

    @NotNull
    public Stream<String> examine(@Nullable final String value) {
        return Stream.of(this.examiner.examine(value));
    }

    private Stream<String> arrayLike(final Stream<Stream<String>> streams) {
        Stream<String> flattened = flatten(",", streams);
        Stream<String> indented = indent(flattened);
        return enclose(indented, "[", "]");
    }

    private static Stream<String> enclose(final Stream<String> lines, final String open, final String close) {
        return enclose((List<String>) lines.collect(Collectors.toList()), open, close);
    }

    private static Stream<String> enclose(final List<String> lines, final String open, final String close) {
        return lines.isEmpty() ? Stream.of(open + close) : (Stream) Stream.of(Stream.of(open), indent(lines.stream()), Stream.of(close)).reduce(Stream.empty(), Stream::concat);
    }

    private static Stream<String> flatten(final String delimiter, final Stream<Stream<String>> bumpy) {
        List<String> flat = new ArrayList();
        bumpy.forEachOrdered(lines -> {
            if (!flat.isEmpty()) {
                int last = flat.size() - 1;
                flat.set(last, (String) flat.get(last) + delimiter);
            }
            lines.forEachOrdered(flat::add);
        });
        return flat.stream();
    }

    private static Stream<String> association(final Stream<String> left, final String middle, final Stream<String> right) {
        return association((List<String>) left.collect(Collectors.toList()), middle, (List<String>) right.collect(Collectors.toList()));
    }

    private static Stream<String> association(final List<String> left, final String middle, final List<String> right) {
        int lefts = left.size();
        int rights = right.size();
        int height = Math.max(lefts, rights);
        int leftWidth = Strings.maxLength(left.stream());
        String leftPad = lefts < 2 ? "" : Strings.repeat(" ", leftWidth);
        String middlePad = lefts < 2 ? "" : Strings.repeat(" ", middle.length());
        List<String> result = new ArrayList(height);
        for (int i = 0; i < height; i++) {
            String l = i < lefts ? Strings.padEnd((String) left.get(i), leftWidth, ' ') : leftPad;
            String m = i == 0 ? middle : middlePad;
            String r = i < rights ? (String) right.get(i) : "";
            result.add(l + m + r);
        }
        return result.stream();
    }

    private static Stream<String> indent(final Stream<String> lines) {
        return lines.map(line -> "  " + line);
    }

    private static final class Instances {

        static final MultiLineStringExaminer SIMPLE_ESCAPING = new MultiLineStringExaminer(StringExaminer.simpleEscaping());
    }
}