package me.lucko.spark.lib.adventure.key;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

final class KeyImpl implements Key {

    static final Comparator<? super Key> COMPARATOR = Comparator.comparing(Key::value).thenComparing(Key::namespace);

    static final String NAMESPACE_PATTERN = "[a-z0-9_\\-.]+";

    static final String VALUE_PATTERN = "[a-z0-9_\\-./]+";

    private final String namespace;

    private final String value;

    KeyImpl(@NotNull final String namespace, @NotNull final String value) {
        if (!Key.parseableNamespace(namespace)) {
            throw new InvalidKeyException(namespace, value, String.format("Non [a-z0-9_.-] character in namespace of Key[%s]", asString(namespace, value)));
        } else if (!Key.parseableValue(value)) {
            throw new InvalidKeyException(namespace, value, String.format("Non [a-z0-9/._-] character in value of Key[%s]", asString(namespace, value)));
        } else {
            this.namespace = (String) Objects.requireNonNull(namespace, "namespace");
            this.value = (String) Objects.requireNonNull(value, "value");
        }
    }

    static boolean allowedInNamespace(final char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.';
    }

    static boolean allowedInValue(final char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.' || character == '/';
    }

    @NotNull
    @Override
    public String namespace() {
        return this.namespace;
    }

    @NotNull
    @Override
    public String value() {
        return this.value;
    }

    @NotNull
    @Override
    public String asString() {
        return asString(this.namespace, this.value);
    }

    @NotNull
    private static String asString(@NotNull final String namespace, @NotNull final String value) {
        return namespace + ':' + value;
    }

    @NotNull
    public String toString() {
        return this.asString();
    }

    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("namespace", this.namespace), ExaminableProperty.of("value", this.value));
    }

    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Key)) {
            return false;
        } else {
            Key that = (Key) other;
            return Objects.equals(this.namespace, that.namespace()) && Objects.equals(this.value, that.value());
        }
    }

    public int hashCode() {
        int result = this.namespace.hashCode();
        return 31 * result + this.value.hashCode();
    }

    @Override
    public int compareTo(@NotNull final Key that) {
        return Key.super.compareTo(that);
    }
}